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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WordsRecViewAdapter.OnWordListener, View.OnClickListener {

    public static ArrayList<Word> words = new ArrayList<>();
    public static WordsRecViewAdapter adapter;



    boolean editing = false;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getApplicationContext().getSharedPreferences("WordsPreferences", MODE_PRIVATE);

        findViewById(R.id.submitWord).setOnClickListener(this);
        findViewById(R.id.submitQuerrySearch).setOnClickListener(this);
        findViewById(R.id.submitQuerrySort).setOnClickListener(this);

        findViewById(R.id.searchWords).setOnClickListener(this);
        findViewById(R.id.addWord).setOnClickListener(this);
        findViewById(R.id.sortWords).setOnClickListener(this);

        findViewById(R.id.resetWordsSearch).setOnClickListener(this);
        findViewById(R.id.resetWordsSort).setOnClickListener(this);

        RecyclerView wordsRecyclerView;
        wordsRecyclerView = findViewById(R.id.wordsRecyclerView);


        LinearLayout[] selectedAddWordCriteriaLayout = new LinearLayout[5];
        selectedAddWordCriteriaLayout[0] = findViewById(R.id.addWord_noun);
        selectedAddWordCriteriaLayout[0].setOnClickListener(this);
        selectedAddWordCriteriaLayout[1] = findViewById(R.id.addWord_verb);
        selectedAddWordCriteriaLayout[1].setOnClickListener(this);
        selectedAddWordCriteriaLayout[2] = findViewById(R.id.addWord_adj);
        selectedAddWordCriteriaLayout[2].setOnClickListener(this);
        selectedAddWordCriteriaLayout[3] = findViewById(R.id.addWord_expr);
        selectedAddWordCriteriaLayout[3].setOnClickListener(this);
        selectedAddWordCriteriaLayout[4] = findViewById(R.id.addWord_other);
        selectedAddWordCriteriaLayout[4].setOnClickListener(this);

        findViewById(R.id.sort_alpha_asc).setOnClickListener(this);
        findViewById(R.id.sort_alpha_desc).setOnClickListener(this);
        findViewById(R.id.sort_date_asc).setOnClickListener(this);
        findViewById(R.id.sort_date_desc).setOnClickListener(this);


        words = WordsHandler.parseWords(sp.getString("words", null));

        adapter = new WordsRecViewAdapter(this);
        for (Word word : words) {
            Log.d("main", "words " + word.getDetails());
        }
        adapter.setWords(words);
        UpdateWords(words);
        adapter.notifyDataSetChanged();

        wordsRecyclerView.setAdapter(adapter);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            default:
                break;
        }
    }

    @Override
    public boolean onWordClick(View itemView, boolean expanded) {
        if (!editing) {
            if (expanded) { //expand
                @ColorInt int color = MaterialColors.getColor(this, R.attr.container_color_selected, Color.WHITE);
                Log.d("main", color + "");
                itemView.setBackgroundTintList(ColorStateList.valueOf(color));
                itemView.findViewById(R.id.detailsHolder).setVisibility(View.VISIBLE);
            } else { // retract
                int color = MaterialColors.getColor(this, R.attr.container_color, Color.WHITE);
                itemView.setBackgroundTintList(ColorStateList.valueOf(color));
                itemView.findViewById(R.id.detailsHolder).setVisibility(View.GONE);
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
        adapter.notifyItemRangeChanged(pos, words.size());
    }

    @Override
    public void onAddDetail(View itemView, int pos) {
        LinearLayout container = itemView.findViewById(R.id.details);
        LayoutInflater.from(container.getContext()).inflate(R.layout.word_details, container, true);
    }

    int tempPos;
    EditText tempForeignWord, tempLocalWord;
    View tempView;
    @Override
    public void onChangeWord(int pos, EditText foreignWord, EditText localWord, View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!editing) {
            tempPos = pos; tempForeignWord = foreignWord; tempLocalWord = localWord; tempView = view;

            editing = true;
            foreignWord.setFocusableInTouchMode(true);
            localWord.setFocusableInTouchMode(true);
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
            foreignWord.clearFocus();
            localWord.clearFocus();

            words.get(pos).setForeignWord(foreignWord.getText().toString());
            words.get(pos).setLocalWord(localWord.getText().toString());
            LinearLayout details = view.findViewById(R.id.details);
            words.get(pos).getDetails().clear();
            for (int i = 0; i < details.getChildCount(); i++) {
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
            onChangeWord(tempPos, tempForeignWord, tempLocalWord, tempView);

        ActionBarBehaviourHandler.getInstance().onBackPressed(this);
    }


    public void UpdateWords(ArrayList<Word> words) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("words");
        editor.putString("words", WordsHandler.encryptWords(words));
        editor.apply();
    }

}