package com.stefan.helloworld;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Word{
    private String date;
    private String foreignWord;
    private String localWord;
    private char type;
    private LinkedHashMap<String, String> details = new LinkedHashMap<>();

    public Word(String date, String foreignWord, String localWord, char type, LinkedHashMap<String, String> details) {
        this.date = date;
        this.foreignWord = foreignWord;
        this.localWord = localWord;
        this.type = type;
        this.details = details;
    }

    public Word(String date, String foreignWord, String localWord, char type) {
        this.date = date;
        this.foreignWord = foreignWord;
        this.localWord = localWord;
        this.type = type;
    }

    public char getType() {
        return type;
    }
    public void setType(char type) {
        this.type = type;
    }
    public String getDate() {
        return date;
    }
    public String getForeignWord() {
        return foreignWord;
    }
    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }
    public String getLocalWord() {
        return localWord;
    }
    public void setLocalWord(String localWord) {
        this.localWord = localWord;
    }
    public HashMap<String, String> getDetails() {
        return details;
    }
}
