package com.stefan.helloworld;

import android.content.Context;
import android.view.View;

import java.util.HashMap;

public class ApplicationManager {
    private static HashMap<String, Integer> countryFlags = new HashMap<>();
    public static HashMap<String, Integer> getCountryFlags(){
        if(countryFlags.size() == 0){
            countryFlags.put("empty", R.mipmap.empty_flag);
            countryFlags.put("english", R.mipmap.america_flag);
            countryFlags.put("french", R.mipmap.france_flag);
            countryFlags.put("spanish", R.mipmap.spain_flag);
            countryFlags.put("romanian", R.mipmap.romania_flag);
            countryFlags.put("italian", R.mipmap.italy_flag);
            countryFlags.put("german", R.mipmap.germany_flag);
            countryFlags.put("russian", R.mipmap.russian_flag);
            countryFlags.put("greek", R.mipmap.greece_flag);
            countryFlags.put("japanese", R.mipmap.japan_flag);
            countryFlags.put("norwegian", R.mipmap.norway_flag);
            countryFlags.put("georgian", R.mipmap.georgia_flag);
        }
        return countryFlags;
    }

    private static HashMap<Integer, Integer> IDIntCorrespondence = new HashMap<>();
    public static HashMap<Integer, Integer> getIDIntCorrespondence(){
        if(IDIntCorrespondence.size() == 0){
            IDIntCorrespondence.put(R.id.addWord_noun, 0);
            IDIntCorrespondence.put(R.id.addWord_verb, 1);
            IDIntCorrespondence.put(R.id.addWord_adj, 2);
            IDIntCorrespondence.put(R.id.addWord_expr, 3);
            IDIntCorrespondence.put(R.id.addWord_other, 4);
        }
        return IDIntCorrespondence;
    }

    private static HashMap<Integer, Integer> IDIntCorrespondenceSort = new HashMap<>();
    public static HashMap<Integer, Integer> getIDIntCorrespondenceSort(){
        if(IDIntCorrespondenceSort.size() == 0){
            IDIntCorrespondenceSort.put(R.id.sort_alpha_asc, 0);
            IDIntCorrespondenceSort.put(R.id.sort_alpha_desc, 1);
            IDIntCorrespondenceSort.put(R.id.sort_date_asc, 2);
            IDIntCorrespondenceSort.put(R.id.sort_date_desc, 3);
        }
        return IDIntCorrespondenceSort;
    }

    private static Context context;
    public static Context getContext(){ return context; }
    public static void setContext(Context contxt) { context = contxt; }

    private static View.OnClickListener onClickListener;
    public static View.OnClickListener getOnClickListener(){ return onClickListener; }
    public static void setOnClickListener(View.OnClickListener onClickListner) { onClickListener = onClickListner; }

}
