package com.stefan.helloworld;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class WordsHandler {

    public static String encryptWords(ArrayList<Word> words){
        String encryptedWords = "";
        for(Word word : words){
            String s = "";
            s += (word.getDate() + "@");
            s += (word.getForeignWord() + "@");
            s += (word.getLocalWord() + "@");
            s += (word.getType() + "@");

            if(word.getDetails() != null && word.getDetails().size() > 0){
                String details = "";
                for(String key : word.getDetails().keySet())
                    details += (key + "-" + word.getDetails().get(key) + ";");
                s += details.substring(0, details.length() - 1);
            }
            encryptedWords += s + "#";
        }
        if(encryptedWords.equals("")) return "";
        return encryptedWords.substring(0, encryptedWords.length() - 1);
    }

    public static ArrayList<Word> parseWords(String allWordsString){
        ArrayList<Word> parsedWords = new ArrayList<>();
        if(allWordsString.equals(""))
            return parsedWords;

        String[] wordsStringList = allWordsString.split("#");
        for(String wordString : wordsStringList){
            LinkedHashMap<String, String> detailsList = new LinkedHashMap<>();
            String[] wordFieldsList = wordString.split("@");

            if(wordFieldsList.length > 4){
                for(String details : wordFieldsList[4].split(";")){
                    String[] detailContents = details.split("-");
                    detailsList.put(detailContents[0], detailContents[1]);
                }
            }
            Word parsedWord = new Word(
                    wordFieldsList[0],
                    wordFieldsList[1],
                    wordFieldsList[2],
                    wordFieldsList[3].charAt(0),
                    detailsList
            );
            parsedWords.add(parsedWord);
        }
        return parsedWords;
    }

    public static ArrayList<Word> searchedWords(ArrayList<Word> words, String querry, ArrayList<Character> chars){
        ArrayList<Word> newWords = new ArrayList<>();

        for(Word word : words)
            if((word.getForeignWord().contains(querry) || word.getLocalWord().contains(querry) || word.getDate().contains(querry)) && chars.contains(word.getType()))
                newWords.add(word);

        return newWords;
    }

    public static ArrayList<Word> sortedWords(ArrayList<Word> words, int alpha, int date, ArrayList<Character> chars){
        ArrayList<Word> newWords;
        newWords = searchedWords(words,"", chars);


        Comparator<Word> comparatorAlpha = new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                return word1.getForeignWord().compareTo(word2.getForeignWord());
            }
        };

        Comparator<Word> comparatorDate = new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd" + '\n' + "MMM");
                Date date1 = null, date2 = null;
                try {
                    date1 = dateFormat.parse(word1.getDate());
                    date2 = dateFormat.parse(word2.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date1.compareTo(date2);
            }
        };

        if(alpha == -1 || alpha == 1) Collections.sort(newWords, comparatorAlpha);
        if(alpha == -1) Collections.reverse(newWords);

        if(date == -1 || date == 1) Collections.sort(newWords, comparatorDate);
        if(date == -1) Collections.reverse(newWords);

        return newWords;
    }
}
