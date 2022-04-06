package com.stefan.helloworld;

import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class ActionBarBehaviourHandler extends AppCompatActivity {

    private static ActionBarBehaviourHandler instance;


    private ActionBarBehaviourHandler(
            InputMethodManager imm,
            RelativeLayout actionBar,
            LinearLayout actionBarExtension,
            LinearLayout[] selectedCriteriaLayout,
            View includeSearch,
            View includeAdd,
            View includeSort,
            EditText foreignWord_addWord,
            EditText localWord_addWord,
            EditText keyWord_searchWord,
            LinearLayout[] selectedSortElements,
            SharedPreferences sp){
        this.imm = imm;
        this.actionBar = actionBar;
        this.actionBarExtension = actionBarExtension;
        this.selectedCriteriaLayout = selectedCriteriaLayout;
        this.includeAdd = includeAdd;
        this.includeSort = includeSort;
        this.includeSearch = includeSearch;
        this.foreignWord_addWord = foreignWord_addWord;
        this.localWord_addWord = localWord_addWord;
        this.keyWord_searchWord = keyWord_searchWord;
        this.selectedSortElements = selectedSortElements;
        this.sp = sp;

    }

    public static ActionBarBehaviourHandler getInstance(
            InputMethodManager imm,
            RelativeLayout actionBar,
            LinearLayout addWordPanel,
            LinearLayout[] selectedCriteriaLayout,
            View includeSearch,
            View includeAdd,
            View includeSort,
            SharedPreferences sp){
        if(instance == null)
            instance = new ActionBarBehaviourHandler(
                    imm,
                    actionBar,
                    addWordPanel,
                    selectedCriteriaLayout,
                    includeSearch,
                    includeAdd,
                    includeSort,
                    includeAdd.findViewById(R.id.foreignWord_addWord),
                    includeAdd.findViewById(R.id.localWord_addWord),
                    includeSearch.findViewById(R.id.keyWord_search),
                    new LinearLayout[]{
                            includeSort.findViewById(R.id.sort_alpha_asc),
                            includeSort.findViewById(R.id.sort_alpha_desc),
                            includeSort.findViewById(R.id.sort_date_asc),
                            includeSort.findViewById(R.id.sort_date_desc),
                    },
                    sp);
        return instance;
    }
    public static ActionBarBehaviourHandler getInstance(){
        return instance;
    }



    private final EditText foreignWord_addWord, localWord_addWord, keyWord_searchWord;
    private final RelativeLayout actionBar;
    private final LinearLayout actionBarExtension;
    private final View includeSearch, includeAdd, includeSort;
    private final InputMethodManager imm;
    private final LinearLayout[] selectedCriteriaLayout;
    private final int[] selectedCriteriaIndex = new int[]{0, 0, 0, 0, 0};

    private final SharedPreferences sp;

    int mode = -1;


    public void ManageColorChangeForCriteria(View view, MainActivity context){
        if(mode == 0 || mode == 2) {
            switch (view.getId()) {
                case R.id.addWord_noun:
                    if (selectedCriteriaIndex[0] == 0) {
                        selectedCriteriaIndex[0] = 1;
                        selectedCriteriaLayout[0].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    } else {
                        selectedCriteriaIndex[0] = 0;
                        selectedCriteriaLayout[0].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_verb:
                    if (selectedCriteriaIndex[1] == 0) {
                        selectedCriteriaIndex[1] = 1;
                        selectedCriteriaLayout[1].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    } else {
                        selectedCriteriaIndex[1] = 0;
                        selectedCriteriaLayout[1].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_adj:
                    if (selectedCriteriaIndex[2] == 0) {
                        selectedCriteriaIndex[2] = 1;
                        selectedCriteriaLayout[2].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    } else {
                        selectedCriteriaIndex[2] = 0;
                        selectedCriteriaLayout[2].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_expr:
                    if (selectedCriteriaIndex[3] == 0) {
                        selectedCriteriaIndex[3] = 1;
                        selectedCriteriaLayout[3].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    } else {
                        selectedCriteriaIndex[3] = 0;
                        selectedCriteriaLayout[3].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_other:
                    if (selectedCriteriaIndex[4] == 0) {
                        selectedCriteriaIndex[4] = 1;
                        selectedCriteriaLayout[4].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    } else {
                        selectedCriteriaIndex[4] = 0;
                        selectedCriteriaLayout[4].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    }
                    break;
                default:
                    break;

            }
        }
        if(mode == 1){
            for(int i=0; i < 5; i++){
                if(selectedCriteriaIndex[i] == 1) {
                    selectedCriteriaLayout[i].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    selectedCriteriaIndex[i] = 0;
                }
            }
            switch (view.getId()){
                case R.id.addWord_noun:
                    if(selectedCriteriaIndex[0] == 0) {
                        selectedCriteriaIndex[0] = 1;
                        selectedCriteriaLayout[0].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_verb:
                    if(selectedCriteriaIndex[1] == 0){
                        selectedCriteriaIndex[1] = 1;
                        selectedCriteriaLayout[1].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_adj:
                    if(selectedCriteriaIndex[2] == 0){
                        selectedCriteriaIndex[2] = 1;
                        selectedCriteriaLayout[2].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_expr:
                    if(selectedCriteriaIndex[3] == 0){
                        selectedCriteriaIndex[3] = 1;
                        selectedCriteriaLayout[3].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    }
                    break;
                case R.id.addWord_other:
                    if(selectedCriteriaIndex[4] == 0){
                        selectedCriteriaIndex[4] = 1;
                        selectedCriteriaLayout[4].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    }
                    break;
                default:
                    break;

            }
        }
    }

    int[] selectedSortElementsIndex = new int[]{0, 0, 0, 0};
    LinearLayout[] selectedSortElements;

    public void ManageSortElements(View view, MainActivity context){
        switch (view.getId()) {
            case R.id.sort_alpha_asc:
                if(selectedSortElementsIndex[0] == 0){
                    selectedSortElementsIndex[0] = 1;
                    selectedSortElements[0].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    selectedSortElementsIndex[1] = 0;
                    selectedSortElements[1].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                else{
                    selectedSortElementsIndex[0] = 0;
                    selectedSortElements[0].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                break;
            case R.id.sort_alpha_desc:
                if(selectedSortElementsIndex[1] == 0){
                    selectedSortElementsIndex[1] = 1;
                    selectedSortElements[1].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    selectedSortElementsIndex[0] = 0;
                    selectedSortElements[0].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                else{
                    selectedSortElementsIndex[1] = 0;
                    selectedSortElements[1].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                break;
            case R.id.sort_date_asc:
                if(selectedSortElementsIndex[2] == 0){
                    selectedSortElementsIndex[2] = 1;
                    selectedSortElements[2].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    selectedSortElementsIndex[3] = 0;
                    selectedSortElements[3].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                else{
                    selectedSortElementsIndex[2] = 0;
                    selectedSortElements[2].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                break;
            case R.id.sort_date_desc:
                if(selectedSortElementsIndex[3] == 0) {
                    selectedSortElementsIndex[3] = 1;
                    selectedSortElements[3].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
                    selectedSortElementsIndex[2] = 0;
                    selectedSortElements[2].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
                else{
                    selectedSortElementsIndex[3] = 0;
                    selectedSortElements[3].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
            default:
                break;
        }
    }

    public void SubmitWord(View view, MainActivity context) {
        String foreignText = foreignWord_addWord.getText().toString();
        String localText = localWord_addWord.getText().toString();
        char type;
        if (selectedCriteriaIndex[0] == 1)
            type = 'N';
        else if (selectedCriteriaIndex[1] == 1)
            type = 'V';
        else if (selectedCriteriaIndex[2] == 1)
            type = 'A';
        else if (selectedCriteriaIndex[3] == 1)
            type = 'E';
        else if (selectedCriteriaIndex[4] == 1)
            type = 'O';
        else
            type = 'O';

        if (foreignText.matches(""))
            Toast.makeText(context, "Provide a french word", Toast.LENGTH_SHORT).show();
        else if (localText.matches(""))
            Toast.makeText(context, "Provide an english word", Toast.LENGTH_SHORT).show();
        else {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd" + '\n' + "MMM");
            String todaysDate = dateFormat.format(date);

            MainActivity.words.add(0, new Word(todaysDate.toUpperCase(Locale.ROOT), foreignText, localText, type));
            MainActivity.adapter.setWords(MainActivity.words);
            UpdateWords(MainActivity.words);
            MainActivity.adapter.notifyDataSetChanged();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            onBackPressed(context);
        }
    }

    public void SearchWords(View view, MainActivity context){

        ArrayList<Character> chars = new ArrayList<>();
        if(selectedCriteriaIndex[0] == 1) chars.add('N');
        if(selectedCriteriaIndex[1] == 1) chars.add('V');
        if(selectedCriteriaIndex[2] == 1) chars.add('A');
        if(selectedCriteriaIndex[3] == 1) chars.add('E');
        if(selectedCriteriaIndex[4] == 1) chars.add('O');
        if(chars.size() == 0) chars.addAll(Arrays.asList('N', 'V', 'A', 'E', 'O'));

        String keyword = keyWord_searchWord.getText().toString();
        MainActivity.adapter.setWords(WordsHandler.searchedWords(MainActivity.words, keyword, chars));
        MainActivity.adapter.notifyDataSetChanged();

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        onBackPressed(context);
    }

    public void SortWords(View view, MainActivity context){

        int alpha = 0;
        if(selectedSortElementsIndex[0] == 1) alpha = 1;
        else if(selectedSortElementsIndex[1] == 1) alpha = -1;

        int date = 0;
        if(selectedSortElementsIndex[2] == 1) date = 1;
        else if(selectedSortElementsIndex[3] == 1) date = -1;

        ArrayList<Character> chars = new ArrayList<>();
        if(selectedCriteriaIndex[0] == 1) chars.add('N');
        if(selectedCriteriaIndex[1] == 1) chars.add('V');
        if(selectedCriteriaIndex[2] == 1) chars.add('A');
        if(selectedCriteriaIndex[3] == 1) chars.add('E');
        if(selectedCriteriaIndex[4] == 1) chars.add('O');
        if(chars.size() == 0) chars.addAll(Arrays.asList('N', 'V', 'A', 'E', 'O'));

        MainActivity.adapter.setWords(WordsHandler.sortedWords(MainActivity.words ,alpha, date, chars));
        MainActivity.adapter.notifyDataSetChanged();

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        onBackPressed(context);
    }

    public void onBackPressed(MainActivity context) {
        if(mode == 1){
            mode = -1;
            foreignWord_addWord.clearFocus(); foreignWord_addWord.getText().clear();
            localWord_addWord.clearFocus(); localWord_addWord.getText().clear();
            actionBar.setVisibility(View.VISIBLE);
            actionBarExtension.setVisibility(View.GONE);
            for(int i=0; i < 5; i++){
                if(selectedCriteriaIndex[i] == 1) {
                    selectedCriteriaLayout[i].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    selectedCriteriaIndex[i] = 0;
                }
            }
        }
        else if(mode == 0){
            mode = -1;
            keyWord_searchWord.clearFocus(); keyWord_searchWord.getText().clear();
            actionBar.setVisibility(View.VISIBLE);
            actionBarExtension.setVisibility(View.GONE);
            for(int i=0; i < 5; i++){
                if(selectedCriteriaIndex[i] == 1) {
                    selectedCriteriaLayout[i].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    selectedCriteriaIndex[i] = 0;
                }
            }
        }
        else if(mode == 2){
            mode = -1;
            actionBar.setVisibility(View.VISIBLE);
            actionBarExtension.setVisibility(View.GONE);
            for(int i=0; i < 5; i++){
                if(selectedCriteriaIndex[i] == 1) {
                    selectedCriteriaLayout[i].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    selectedCriteriaIndex[i] = 0;
                }
            }
            for(int i=0; i<4; i++){
                if(selectedSortElementsIndex[i] == 1){
                    selectedSortElements[i].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    selectedSortElementsIndex[i] = 0;
                }
            }
        }


    }

    public void showAddWord(){
        mode = 1;
        includeSearch.setVisibility(View.GONE);
        includeSort.setVisibility(View.GONE);
        includeAdd.setVisibility(View.VISIBLE);

        actionBarExtension.setVisibility(View.VISIBLE);
        foreignWord_addWord.requestFocus();
        imm.showSoftInput(foreignWord_addWord, InputMethodManager.SHOW_IMPLICIT);
        actionBar.setVisibility(View.GONE);
    }
    public void showSearchWords(){
        mode = 0;
        includeSearch.setVisibility(View.VISIBLE);
        includeSort.setVisibility(View.GONE);
        includeAdd.setVisibility(View.GONE);

        actionBarExtension.setVisibility(View.VISIBLE);
        keyWord_searchWord.requestFocus();
        imm.showSoftInput(keyWord_searchWord, InputMethodManager.SHOW_IMPLICIT);
        actionBar.setVisibility(View.GONE);
    }
    public void showSortWords(){
        mode = 2;
        includeSearch.setVisibility(View.GONE);
        includeSort.setVisibility(View.VISIBLE);
        includeAdd.setVisibility(View.GONE);

        actionBarExtension.setVisibility(View.VISIBLE);
        actionBar.setVisibility(View.GONE);
    }

    public void UpdateWords(ArrayList<Word> words){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("words");
        editor.putString("words", WordsHandler.encryptWords(words));
        editor.apply();
    }

    public void ResetWords(View view, MainActivity context){
        MainActivity.adapter.setWords(MainActivity.words);
        MainActivity.adapter.notifyDataSetChanged();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        onBackPressed(context);
    }
}
