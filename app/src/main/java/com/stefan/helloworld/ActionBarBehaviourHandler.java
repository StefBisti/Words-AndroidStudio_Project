package com.stefan.helloworld;

import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
    public final RelativeLayout actionBar;
    public final LinearLayout actionBarExtension;
    private final View includeSearch, includeAdd, includeSort;
    private final InputMethodManager imm;
    private final LinearLayout[] selectedCriteriaLayout;
    private final int[] selectedCriteriaIndex = new int[]{0, 0, 0, 0, 0};

    private final SharedPreferences sp;

    public int mode = -1;

    public void ManageColorChangeForCriteria(View view, MainActivity context){

        HashMap<Integer, Integer> IDIntCorrespondence = ApplicationManager.getIDIntCorrespondence();

        if(mode == 1) {
            for (int i = 0; i < 5; i++) {
                if (selectedCriteriaIndex[i] == 1) {
                    selectedCriteriaLayout[i].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                    selectedCriteriaIndex[i] = 0;
                }
            }
        }
        if(IDIntCorrespondence.keySet().contains(view.getId())){
            if(selectedCriteriaIndex[IDIntCorrespondence.get(view.getId())] == 0){
                selectedCriteriaIndex[IDIntCorrespondence.get(view.getId())] = 1;
                selectedCriteriaLayout[IDIntCorrespondence.get(view.getId())].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));
            }
            else {
                if(mode == 0 || mode == 2){
                    selectedCriteriaIndex[IDIntCorrespondence.get(view.getId())] = 0;
                    selectedCriteriaLayout[IDIntCorrespondence.get(view.getId())].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
                }
            }
        }
    }

    int[] selectedSortElementsIndex = new int[]{0, 0, 0, 0};
    LinearLayout[] selectedSortElements;

    public void ManageSortElements(View view, MainActivity context){

        HashMap<Integer, Integer> IDIntCorrespondenceSort = ApplicationManager.getIDIntCorrespondenceSort();

        if(IDIntCorrespondenceSort.keySet().contains(view.getId())){
            if(selectedSortElementsIndex[IDIntCorrespondenceSort.get(view.getId())] == 0) {
                selectedSortElementsIndex[IDIntCorrespondenceSort.get(view.getId())] = 1;
                selectedSortElements[IDIntCorrespondenceSort.get(view.getId())].setBackgroundTintList(context.getResources().getColorStateList(R.color.selectedAddWordCriteriaItem, null));

                //Reset the other item in group
                int a = IDIntCorrespondenceSort.get(view.getId()) % 2 == 0 ? 1 : -1;
                selectedSortElementsIndex[IDIntCorrespondenceSort.get(view.getId()) + a] = 0;
                selectedSortElements[IDIntCorrespondenceSort.get(view.getId()) + a].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));

            }
            else{
                selectedSortElementsIndex[IDIntCorrespondenceSort.get(view.getId())] = 0;
                selectedSortElements[IDIntCorrespondenceSort.get(view.getId())].setBackgroundTintList(context.getResources().getColorStateList(R.color.unselectedAddWordCriteriaItem, null));
            }
        }
    }

    public void SubmitWord(View view, MainActivity context) {
        String foreignText = foreignWord_addWord.getText().toString();
        String localText = localWord_addWord.getText().toString();
        char type;
        if (selectedCriteriaIndex[0] == 1) type = 'N';
        else if (selectedCriteriaIndex[1] == 1) type = 'V';
        else if (selectedCriteriaIndex[2] == 1) type = 'A';
        else if (selectedCriteriaIndex[3] == 1) type = 'E';
        else if (selectedCriteriaIndex[4] == 1) type = 'O';
        else type = 'O';

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
            MainActivity.adapter.notifyItemInserted(0);
            MainActivity.wordsRecyclerView.smoothScrollToPosition(0);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            onBackPressed(context);

            String lang = sp.getString("chosenLanguage", null);
            if(lang != null && !lang.equals("")){
                String[] langs = lang.split("-");
                for(Language language : LanguagesHandler.getInstance().languages){
                    if(language.getForeignLanguage().equals(langs[0]) && language.getLocalLanguage().equals(langs[1])){
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
                        String todaysDate1 = dateFormat1.format(date);
                        language.setDate(todaysDate1);
                        language.setNumberOfWords(MainActivity.words.size());
                        LanguagesHandler.getInstance().ChangeLanguagesUI(LanguagesHandler.getInstance().languages);
                        LanguagesHandler.getInstance().WriteLanguagesSP(LanguagesHandler.getInstance().languages);
                    }
                }
            }
        }
    }

    public void SearchWords(View view, MainActivity context){

        ArrayList<Character> chars = new ArrayList<>();
        //could use a for loop
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
        if(mode == -2)
            return;

        mode = -1;
        actionBar.setVisibility(View.VISIBLE);
        actionBarExtension.setVisibility(View.GONE);

        foreignWord_addWord.clearFocus(); foreignWord_addWord.getText().clear();
        localWord_addWord.clearFocus(); localWord_addWord.getText().clear();
        keyWord_searchWord.clearFocus(); keyWord_searchWord.getText().clear();

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

    public void UpdateWords(ArrayList<Word> words) {
        String chosenLanguage = sp.getString("chosenLanguage", null);
        if(chosenLanguage == null || chosenLanguage.equals(""))
            return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(chosenLanguage);
        editor.putString(chosenLanguage, WordsHandler.encryptWords(words));
        editor.apply();
    }

    public void ResetWords(View view, MainActivity context){
        MainActivity.adapter.setWords(MainActivity.words);
        MainActivity.adapter.notifyDataSetChanged();
        MainActivity.wordsRecyclerView.smoothScrollToPosition(0);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        onBackPressed(context);
    }
}
