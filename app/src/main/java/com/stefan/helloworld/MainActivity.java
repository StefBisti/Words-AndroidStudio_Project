package com.stefan.helloworld;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.color.MaterialColors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WordsRecViewAdapter.OnWordListener, View.OnClickListener {

    public static ArrayList<Word> words = new ArrayList<>();
    public static WordsRecViewAdapter adapter;
    public static RecyclerView wordsRecyclerView;

    boolean editing = false;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy()).detectLeakedClosableObjects().build());
        setContentView(R.layout.activity_main);

        ApplicationManager.setContext(this);
        ApplicationManager.setOnClickListener(this);

        sp = getApplicationContext().getSharedPreferences("WordsPreferences", MODE_PRIVATE);

        System.gc();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListeners();

                LinearLayout[] selectedAddWordCriteriaLayout = new LinearLayout[5];
                selectedAddWordCriteriaLayout[0] = findViewById(R.id.addWord_noun);
                selectedAddWordCriteriaLayout[0].setOnClickListener(ApplicationManager.getOnClickListener());
                selectedAddWordCriteriaLayout[1] = findViewById(R.id.addWord_verb);
                selectedAddWordCriteriaLayout[1].setOnClickListener(ApplicationManager.getOnClickListener());
                selectedAddWordCriteriaLayout[2] = findViewById(R.id.addWord_adj);
                selectedAddWordCriteriaLayout[2].setOnClickListener(ApplicationManager.getOnClickListener());
                selectedAddWordCriteriaLayout[3] = findViewById(R.id.addWord_expr);
                selectedAddWordCriteriaLayout[3].setOnClickListener(ApplicationManager.getOnClickListener());
                selectedAddWordCriteriaLayout[4] = findViewById(R.id.addWord_other);
                selectedAddWordCriteriaLayout[4].setOnClickListener(ApplicationManager.getOnClickListener());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                ActionBarBehaviourHandler.getInstance(
                        imm,
                        findViewById(R.id.addWordPanelHotBar),
                        findViewById(R.id.action_bar),
                        selectedAddWordCriteriaLayout,
                        findViewById(R.id.actionBar_include_search),
                        findViewById(R.id.actionBar_include_add),
                        findViewById(R.id.actionBar_include_sort),
                        sp);
                LanguagesHandler.getInstance(
                        findViewById(R.id.languages_holder_with_add),
                        findViewById(R.id.foreign_flag_upper),
                        findViewById(R.id.local_flag_upper),
                        findViewById(R.id.no_words_text),
                        findViewById(R.id.foreignWord_addWord),
                        findViewById(R.id.localWord_addWord),
                        sp
                );

                String lang = sp.getString("chosenLanguage", null);
                if(lang != null && !lang.equals("")){
                    words = WordsHandler.parseWords(sp.getString(lang, null));

                    String[] langs = lang.split("-");
                    ((ImageView)findViewById(R.id.foreign_flag_upper)).setImageResource(ApplicationManager.getCountryFlags().get(langs[0]));
                    ((ImageView)findViewById(R.id.local_flag_upper)).setImageResource(ApplicationManager.getCountryFlags().get(langs[1]));

                    ((EditText)findViewById(R.id.foreignWord_addWord)).setHint(langs[0].substring(0, 1).toUpperCase() + langs[0].substring(1) + " word");
                    ((EditText)findViewById(R.id.localWord_addWord)).setHint(langs[1].substring(0, 1).toUpperCase() + langs[1].substring(1) + " word");
                }
                else{
                    ((ImageView)findViewById(R.id.foreign_flag_upper)).setImageResource(ApplicationManager.getCountryFlags().get("empty"));
                    ((ImageView)findViewById(R.id.local_flag_upper)).setImageResource(ApplicationManager.getCountryFlags().get("empty"));
                    ActionBarBehaviourHandler.getInstance().actionBar.setVisibility(View.GONE);
                    findViewById(R.id.no_words_text).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.no_words_text)).setText("No languages available\nCreate one in the top-right section!");
                }
            }
        });

        System.gc();

        if(words.size() == 0)
            findViewById(R.id.no_words_text).setVisibility(View.VISIBLE);

        adapter = new WordsRecViewAdapter(this);
        System.gc();
        adapter.setWords(words);
        UpdateWords(words);
        adapter.notifyDataSetChanged();
        System.gc();
        wordsRecyclerView = findViewById(R.id.wordsRecyclerView);
        wordsRecyclerView.setAdapter(adapter);
        wordsRecyclerView.setItemViewCacheSize(0);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        System.gc();
    }

    private void setListeners(){
        findViewById(R.id.submitWord).setOnClickListener(this);
        findViewById(R.id.submitQuerrySearch).setOnClickListener(this);
        findViewById(R.id.submitQuerrySort).setOnClickListener(this);

        findViewById(R.id.searchWords).setOnClickListener(this);
        findViewById(R.id.addWord).setOnClickListener(this);
        findViewById(R.id.sortWords).setOnClickListener(this);

        findViewById(R.id.resetWordsSearch).setOnClickListener(this);
        findViewById(R.id.resetWordsSort).setOnClickListener(this);
        findViewById(R.id.sort_alpha_asc).setOnClickListener(this);
        findViewById(R.id.sort_alpha_desc).setOnClickListener(this);
        findViewById(R.id.sort_date_asc).setOnClickListener(this);
        findViewById(R.id.sort_date_desc).setOnClickListener(this);

        findViewById(R.id.close_languages_tab).setOnClickListener(this);
        findViewById(R.id.show_languages_tab).setOnClickListener(this);
        findViewById(R.id.add_language).setOnClickListener(this);

        findViewById(R.id.forgn_language_english).setOnClickListener(this);
        findViewById(R.id.forgn_language_romanian).setOnClickListener(this);
        findViewById(R.id.forgn_language_french).setOnClickListener(this);
        findViewById(R.id.forgn_language_spanish).setOnClickListener(this);
        findViewById(R.id.forgn_language_german).setOnClickListener(this);
        findViewById(R.id.forgn_language_italian).setOnClickListener(this);
        findViewById(R.id.forgn_language_russian).setOnClickListener(this);
        findViewById(R.id.forgn_language_greek).setOnClickListener(this);
        findViewById(R.id.forgn_language_japanese).setOnClickListener(this);
        findViewById(R.id.forgn_language_norwegian).setOnClickListener(this);
        findViewById(R.id.forgn_language_georgian).setOnClickListener(this);

        findViewById(R.id.local_language_english).setOnClickListener(this);
        findViewById(R.id.local_language_romanian).setOnClickListener(this);
        findViewById(R.id.local_language_french).setOnClickListener(this);
        findViewById(R.id.local_language_spanish).setOnClickListener(this);
        findViewById(R.id.local_language_german).setOnClickListener(this);
        findViewById(R.id.local_language_italian).setOnClickListener(this);
        findViewById(R.id.local_language_russian).setOnClickListener(this);
        findViewById(R.id.local_language_greek).setOnClickListener(this);
        findViewById(R.id.local_language_japanese).setOnClickListener(this);
        findViewById(R.id.local_language_norwegian).setOnClickListener(this);

        findViewById(R.id.submit_language).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addWord:
                ActionBarBehaviourHandler.getInstance().showAddWord();
                break;
            case R.id.searchWords:
                ActionBarBehaviourHandler.getInstance().showSearchWords();
                break;
            case R.id.sortWords:
                ActionBarBehaviourHandler.getInstance().showSortWords();
                break;
            case R.id.addWord_noun:
            case R.id.addWord_verb:
            case R.id.addWord_adj:
            case R.id.addWord_expr:
            case R.id.addWord_other:
                ActionBarBehaviourHandler.getInstance().ManageColorChangeForCriteria(view, this);
                break;
            case R.id.sort_alpha_asc:
            case R.id.sort_alpha_desc:
            case R.id.sort_date_asc:
            case R.id.sort_date_desc:
                ActionBarBehaviourHandler.getInstance().ManageSortElements(view, this);
                break;
            case R.id.submitWord:
                ActionBarBehaviourHandler.getInstance().SubmitWord(view, this);
                if(words.size() == 1)
                    findViewById(R.id.no_words_text).setVisibility(View.GONE);
                break;
            case R.id.submitQuerrySearch:
                ActionBarBehaviourHandler.getInstance().SearchWords(view, this);
                break;
            case R.id.submitQuerrySort:
                ActionBarBehaviourHandler.getInstance().SortWords(view, this);
                break;
            case R.id.resetWordsSearch:
            case R.id.resetWordsSort:
                ActionBarBehaviourHandler.getInstance().ResetWords(view, this);
                break;
            case R.id.close_languages_tab:
                LanguagesHandler.getInstance().showLanguagesHolder(false);
                break;
            case R.id.show_languages_tab:
                LanguagesHandler.getInstance().showLanguagesHolder(true);
                break;
            case R.id.add_language:
                LanguagesHandler.getInstance().showAddLanguage();
                break;
            case R.id.forgn_language_english: case R.id.forgn_language_romanian: case R.id.forgn_language_french:case R.id.forgn_language_spanish:case R.id.forgn_language_german:case R.id.forgn_language_italian:case R.id.forgn_language_russian:case R.id.forgn_language_greek:case R.id.forgn_language_japanese:case R.id.forgn_language_norwegian:case R.id.forgn_language_georgian:
            case R.id.local_language_english: case R.id.local_language_romanian: case R.id.local_language_french:case R.id.local_language_spanish:case R.id.local_language_german:case R.id.local_language_italian:case R.id.local_language_russian:case R.id.local_language_greek:case R.id.local_language_japanese:case R.id.local_language_norwegian:case R.id.local_language_georgian:
                LanguagesHandler.getInstance().drawFlags(view.getId(), this);
                break;
            case R.id.submit_language:
                LanguagesHandler.getInstance().submitLanguage(this);
            default:
                break;
        }
    }

    @Override
    public boolean onWordClick(View itemView, boolean expanded) {
        if (!editing) {
            if (expanded) { //expand
                @ColorInt int color = MaterialColors.getColor(this, R.attr.container_color_selected, Color.WHITE);
                itemView.setBackgroundTintList(ColorStateList.valueOf(color));
                itemView.findViewById(R.id.detailsHolder).setVisibility(View.VISIBLE);
                
                itemView.findViewById(R.id.edit_word).setBackgroundTintList(ColorStateList.valueOf(color));
                itemView.findViewById(R.id.add_detail).setBackgroundTintList(ColorStateList.valueOf(color));
                itemView.findViewById(R.id.deleteWord).setBackgroundTintList(ColorStateList.valueOf(color));
            } else { // retract
                int color = MaterialColors.getColor(this, R.attr.container_color, Color.WHITE);
                itemView.setBackgroundTintList(ColorStateList.valueOf(color));
                itemView.findViewById(R.id.detailsHolder).setVisibility(View.GONE);

                itemView.findViewById(R.id.edit_word).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent, null)));
                itemView.findViewById(R.id.add_detail).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent, null)));
                itemView.findViewById(R.id.deleteWord).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent, null)));
            }
            return true;
        }
        return false;

    }

    @Override
    public boolean onEditTextsClick(View view, boolean expanded) {
        if (!editing) {
            onWordClick(view, expanded);
            return true;
        }
        return false;
    }

    @Override
    public void onDeleteWord(int pos) {
        words.remove(pos);
        adapter.setWords(words);
        UpdateWords(words);
        adapter.notifyItemRemoved(pos);

        if(words.size() == 0)
            findViewById(R.id.no_words_text).setVisibility(View.VISIBLE);

        String lang = sp.getString("chosenLanguage", null);
        if(lang != null && !lang.equals("")){
            String[] langs = lang.split("-");
            for(Language language : LanguagesHandler.getInstance().languages){
                if(language.getForeignLanguage().equals(langs[0]) && language.getLocalLanguage().equals(langs[1])){
                    language.setNumberOfWords(MainActivity.words.size());
                    LanguagesHandler.getInstance().ChangeLanguagesUI(LanguagesHandler.getInstance().languages);
                    LanguagesHandler.getInstance().WriteLanguagesSP(LanguagesHandler.getInstance().languages);
                }
            }
        }
    }

    @Override
    public void onAddDetail(View itemView, int pos) {
        LinearLayout container = itemView.findViewById(R.id.details);

        if(container.getChildCount() >= 10 + 10){
            Toast.makeText(this, "You can't have more than 10 details.", Toast.LENGTH_SHORT).show();
            return;
        }

        int i=0;
        while(i <= 10){
            if(container.getChildAt(i).getVisibility() == View.GONE) break;
            i++;
        }
        View detail = container.getChildAt(i);

        if(editing){
            detail.findViewById(R.id.detail_name).setFocusableInTouchMode(true);
            detail.findViewById(R.id.detail_content).setFocusableInTouchMode(true);
        }
        detail.setVisibility(View.VISIBLE);
    }

    int tempPos;
    EditText tempForeignWord, tempLocalWord, tempType;
    View tempView;
    @Override
    public void onChangeWord(int pos, EditText foreignWord, EditText localWord, EditText type, View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!editing) {
            tempPos = pos; tempForeignWord = foreignWord; tempLocalWord = localWord; tempType = type; tempView = view;

            editing = true;
            foreignWord.setFocusableInTouchMode(true);
            localWord.setFocusableInTouchMode(true);
            type.setFocusableInTouchMode(true);
            foreignWord.requestFocus();
            foreignWord.setSelection(foreignWord.getText().length());
            imm.showSoftInput(foreignWord, InputMethodManager.SHOW_IMPLICIT);

            findViewById(R.id.addWordPanelHotBar).setVisibility(View.GONE);

            LinearLayout details = view.findViewById(R.id.details);
            for (int i = 0; i < details.getChildCount(); i++) {
                details.getChildAt(i).findViewById(R.id.detail_name).setFocusableInTouchMode(true);
                details.getChildAt(i).findViewById(R.id.detail_content).setFocusableInTouchMode(true);
            }
        } else {
            editing = false;
            foreignWord.setFocusableInTouchMode(false);
            localWord.setFocusableInTouchMode(false);
            type.setFocusableInTouchMode(false);
            foreignWord.clearFocus();
            localWord.clearFocus();
            type.clearFocus();

            words.get(pos).setForeignWord(foreignWord.getText().toString());
            words.get(pos).setLocalWord(localWord.getText().toString());

            if(type.getText().toString().charAt(0) != words.get(pos).getType())
                switch(type.getText().toString()){
                    case "N": case "V": case "A": case "E": case "O":
                        words.get(pos).setType(type.getText().toString().charAt(0));
                        break;
                    default:
                        Toast.makeText(this, "Valid inputs: N, V, A, E, O", Toast.LENGTH_SHORT).show();
                }

            LinearLayout details = view.findViewById(R.id.details);
            words.get(pos).getDetails().clear();
            for (int i = 0; i < details.getChildCount(); i++) {
                if(details.getChildAt(i).getVisibility() == View.GONE) break;
                EditText a = details.getChildAt(i).findViewById(R.id.detail_name);
                EditText b = details.getChildAt(i).findViewById(R.id.detail_content);
                a.clearFocus();
                b.clearFocus();
                if (a.getText().toString().length() > 0 && b.getText().toString().length() > 0)
                    words.get(pos).getDetails().put(a.getText().toString(), b.getText().toString());
            }
            Log.d("main", words.get(pos).getDetails().toString());

            adapter.setWords(words);
            UpdateWords(words);
            adapter.notifyItemChanged(pos);
            onWordClick(view, false);

            View v = this.getCurrentFocus();
            if (v != null)
                imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
            findViewById(R.id.addWordPanelHotBar).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if(editing)
            onChangeWord(tempPos, tempForeignWord, tempLocalWord, tempType, tempView);

        ActionBarBehaviourHandler.getInstance().onBackPressed(this);
    }


    public void UpdateWords(ArrayList<Word> words) {
        String chosenLanguage = sp.getString("chosenLanguage", null);
        if(chosenLanguage == null || chosenLanguage.equals(""))
            return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(chosenLanguage);
        editor.putString(chosenLanguage, WordsHandler.encryptWords(words));
        editor.apply();
    }
}