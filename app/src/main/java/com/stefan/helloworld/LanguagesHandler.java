package com.stefan.helloworld;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



class Language {
    private String foreignLanguage, localLanguage;
    private int numberOfWords;
    private String date;

    public Language(String foreignLanguage, String localLanguage, int numberOfWords, String date) {
        this.foreignLanguage = foreignLanguage;
        this.localLanguage = localLanguage;
        this.numberOfWords = numberOfWords;
        this.date = date;
    }

    public String getForeignLanguage() {
        return foreignLanguage;
    }

    public String getLocalLanguage() {
        return localLanguage;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }
    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

public class LanguagesHandler extends AppCompatActivity {

    private static LanguagesHandler instance;


    private LanguagesHandler(LinearLayout languagesHolder, ImageView foreignFlagUpper, ImageView localFlagUpper, TextView noWordsAvailable, EditText foreignTextInput, EditText localTextInput, SharedPreferences sp){
        this.languagesHolder = languagesHolder;
        this.foreignFlagUpper = foreignFlagUpper;
        this.localFlagUpper = localFlagUpper;
        this.noWordsAvailable = noWordsAvailable;
        this.foreignTextInput = foreignTextInput;
        this.localTextInput = localTextInput;
        this.sp = sp;

        this.languagesHolderUpper = languagesHolder.findViewById(R.id.languages_holder);
        this.languagesSelector = languagesHolder.findViewById(R.id.add_language_tab);

        this.foreignFLagSelector = languagesSelector.findViewById(R.id.foreign_flag_selector);
        this.localFlagSelector = languagesSelector.findViewById(R.id.local_flag_selector);

        languages = parseLanguages(sp.getString("languages", null));
        ChangeLanguagesUI(languages);

    }

    public static LanguagesHandler getInstance(LinearLayout languagesHolder, ImageView foreignFlagUpper, ImageView localFlagUpper, TextView noWordsAvailable, EditText foreignTextInput, EditText localTextInput, SharedPreferences sp){
        if(instance == null)
            instance = new LanguagesHandler(languagesHolder, foreignFlagUpper, localFlagUpper, noWordsAvailable, foreignTextInput, localTextInput, sp);
        return instance;
    }
    public static LanguagesHandler getInstance(){ return instance; }


    public String activeLanguage;

    private LinearLayout languagesHolder, languagesHolderUpper, languagesSelector;
    private ImageView foreignFlagUpper, localFlagUpper, foreignFLagSelector, localFlagSelector;
    private TextView noWordsAvailable;
    private EditText foreignTextInput, localTextInput;
    private final SharedPreferences sp;

    public ArrayList<Language> languages;

    public void showLanguagesHolder(boolean show){
        if(!show && languagesSelector.getVisibility() == View.VISIBLE){
            Handler handler = new Handler();
            handler.postDelayed(() -> showAddLanguage(), 300);
        }
        if(show && languages.size() == 0)
            showAddLanguage();

        languagesHolder.setVisibility(show ? View.VISIBLE : View.GONE);

        if(!show){
            foreignLanguageSelected = "";
            localLanguageSelected = "";

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                foreignFLagSelector.setImageResource(R.mipmap.empty_flag);
                localFlagSelector.setImageResource(R.mipmap.empty_flag);
            }, 300);
        }
    }

    public void showAddLanguage(){
        boolean upperExtended = languagesHolderUpper.getVisibility() == View.VISIBLE;
        languagesHolderUpper.setVisibility(upperExtended ? View.GONE : View.VISIBLE);
        languagesSelector.setVisibility(upperExtended ? View.VISIBLE : View.GONE);
    }

    public void setLanguage(String languagesQuerry){
        activeLanguage = languagesQuerry;

        if(languagesQuerry.equals("")){
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(sp.getString("chosenLanguage", null));

            MainActivity.words = new ArrayList<>();
            MainActivity.adapter.setWords(MainActivity.words);
            MainActivity.adapter.notifyDataSetChanged();

            editor.remove("chosenLanguage");
            editor.putString("chosenLanguage", "");
            foreignFlagUpper.setImageResource(ApplicationManager.getCountryFlags().get("empty"));
            localFlagUpper.setImageResource(ApplicationManager.getCountryFlags().get("empty"));

            showLanguagesHolder(false);
            editor.apply();
            noWordsAvailable.setVisibility(View.VISIBLE);
            noWordsAvailable.setText("No languages available\nCreate one in the top-right section!");
            ActionBarBehaviourHandler.getInstance().actionBar.setVisibility(View.GONE);
            ActionBarBehaviourHandler.getInstance().actionBarExtension.setVisibility(View.GONE);
            ActionBarBehaviourHandler.getInstance().mode = -2;

            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.remove("chosenLanguage");
        editor.putString("chosenLanguage", languagesQuerry);

        ArrayList<Word> words = WordsHandler.parseWords(sp.getString(languagesQuerry, null));
        MainActivity.words = words;
        MainActivity.adapter.setWords(words);
        MainActivity.adapter.notifyDataSetChanged();

        noWordsAvailable.setText("No words available\nTap the Add button to create a word");
        noWordsAvailable.setVisibility(words.size() > 0 ? View.GONE : View.VISIBLE);

        ActionBarBehaviourHandler.getInstance().actionBar.setVisibility(View.VISIBLE);
        ActionBarBehaviourHandler.getInstance().actionBarExtension.setVisibility(View.GONE);

        String[] langs = languagesQuerry.split("-");
        foreignFlagUpper.setImageResource(ApplicationManager.getCountryFlags().get(langs[0]));
        localFlagUpper.setImageResource(ApplicationManager.getCountryFlags().get(langs[1]));

        foreignTextInput.setHint(langs[0].substring(0, 1).toUpperCase() + langs[0].substring(1) + " word");
        localTextInput.setHint(langs[1].substring(0, 1).toUpperCase() + langs[1].substring(1) + " word");

        showLanguagesHolder(false);
        editor.apply();

        MainActivity.wordsRecyclerView.smoothScrollToPosition(0);
    }

    String foreignLanguageSelected, localLanguageSelected;
    public void drawFlags(int id, MainActivity context){
        String languageID = context.getResources().getResourceName(id).split("/")[1];
        String language = languageID.split("_")[2];
        if(languageID.charAt(0) == 'f'){
            foreignFLagSelector.setImageResource(ApplicationManager.getCountryFlags().get(language));
            foreignLanguageSelected = language;
        }
        if(languageID.charAt(0) == 'l'){
            localFlagSelector.setImageResource(ApplicationManager.getCountryFlags().get(language));
            localLanguageSelected = language;
        }
    }

    public void submitLanguage(MainActivity context){

        if(foreignLanguageSelected.equals("")){
            Toast.makeText(context, "You need to select a foreign language", Toast.LENGTH_SHORT).show();
            return;
        }
        if(localLanguageSelected.equals("")){
            Toast.makeText(context, "You need to select a local language", Toast.LENGTH_SHORT).show();
            return;
        }
        if(languages.size() >= 7){
            Toast.makeText(context, "You can't have more than 7 languages", Toast.LENGTH_SHORT).show();
            return;
        }
        languages.add(new Language(foreignLanguageSelected, localLanguageSelected, 0, "Just created"));
        ChangeLanguagesUI(languages);
        setLanguage(foreignLanguageSelected + "-" + localLanguageSelected);
        WriteLanguagesSP(languages);
    }

    public void ChangeLanguagesUI(ArrayList<Language> languages){
        LinearLayout ll = languagesHolder.findViewById(R.id.languages_holder);

        for(int j=0; j<7; j++)
            ll.getChildAt(j).setVisibility(View.GONE);

        int i = 0;
        for(Language lang : languages){
            View v = ll.getChildAt(i);
            v.setVisibility(View.VISIBLE);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setLanguage(lang.getForeignLanguage() + "-" + lang.getLocalLanguage());
                }
            });

            int finalI = i;
            v.findViewById(R.id.remove_language).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationManager.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("All the words related to the language will also be deleted.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            languages.remove(finalI);
                            ChangeLanguagesUI(languages);
                            WriteLanguagesSP(languages);

                            SharedPreferences.Editor editor = sp.edit();
                            editor.remove(lang.getForeignLanguage() + "-" + lang.getLocalLanguage());
                            editor.apply();

                            if(languages.size() > 0)
                                setLanguage(languages.get(0).getForeignLanguage() + "-" + languages.get(0).getLocalLanguage());
                            else
                                setLanguage("");
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            ((ImageView)v.findViewById(R.id.foreign_flag)).setImageResource(ApplicationManager.getCountryFlags().get(lang.getForeignLanguage()));
            ((ImageView)v.findViewById(R.id.local_flag)).setImageResource(ApplicationManager.getCountryFlags().get(lang.getLocalLanguage()));
            ((TextView)v.findViewById(R.id.number_of_words)).setText(lang.getNumberOfWords() + " words");
            ((TextView)v.findViewById(R.id.last_updated_date)).setText(lang.getDate());
            i++;
        }
    }

    static String encryptLanguages(ArrayList<Language> languages){
        String encryptedLanguages = "";
        for(Language language : languages){
            encryptedLanguages += language.getForeignLanguage() + "@";
            encryptedLanguages += language.getLocalLanguage() + "@";
            encryptedLanguages += language.getNumberOfWords() + "@";
            encryptedLanguages += language.getDate();
            encryptedLanguages += '#';
        }
        if(encryptedLanguages.equals("")) return "";
        return encryptedLanguages.substring(0, encryptedLanguages.length() - 1);
    }

    static ArrayList<Language> parseLanguages(String encryptedLanguages){
        ArrayList<Language> parsedLanguages = new ArrayList<>();
        if(encryptedLanguages == null || encryptedLanguages.equals(""))
            return parsedLanguages;

        String[] languages = encryptedLanguages.split("#");
        for(String language : languages){
            String[] fields = language.split("@");
            parsedLanguages.add(new Language(fields[0], fields[1], Integer.parseInt(fields[2]), fields[3]));
        }
        return parsedLanguages;
    }

    public void WriteLanguagesSP(ArrayList<Language> languages){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("languages");
        editor.putString("languages", encryptLanguages(languages));
        editor.apply();
    }
}