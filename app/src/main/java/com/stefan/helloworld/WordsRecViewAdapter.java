package com.stefan.helloworld;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class WordsRecViewAdapter extends RecyclerView.Adapter <WordsRecViewAdapter.ViewHolder> {

    private final OnWordListener mOnWordListener;

    public WordsRecViewAdapter(OnWordListener onWordListener) {
        this.mOnWordListener = onWordListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView date;
        private EditText foreignWord, localWord, typeText;

        private ImageView typeImage;
        private LinearLayout container;
        private View itemView;
        private OnWordListener onWordListener;
        private boolean expanded = false;

        public ViewHolder(@NonNull View itemView, OnWordListener onWordListener){
            super(itemView);
            this.itemView = itemView;

            container = itemView.findViewById(R.id.details);
            date = itemView.findViewById(R.id.date);
            foreignWord = itemView.findViewById(R.id.foreignWord);
            localWord = itemView.findViewById(R.id.localWord);
            typeImage = itemView.findViewById(R.id.typeImage);
            typeText = itemView.findViewById(R.id.typeText);
            this.onWordListener = onWordListener;

            itemView.setOnClickListener(this);


            foreignWord.setOnClickListener(this);
            localWord.setOnClickListener(this);
            typeText.setOnClickListener(this);
            itemView.findViewById(R.id.edit_word).setOnClickListener(this);
            itemView.findViewById(R.id.add_detail).setOnClickListener(this);
            itemView.findViewById(R.id.deleteWord).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.itemBigContainer:
                    expanded = !expanded;
                    if(onWordListener.onWordClick(itemView, expanded))
                        if(!expanded)
                            notifyItemChanged(getAdapterPosition());
                    break;
                case R.id.deleteWord:
                    onWordListener.onDeleteWord(getAdapterPosition());
                    break;
                case R.id.localWord:
                case R.id.foreignWord:
                case R.id.typeText:
                    if(onWordListener.onEditTextsClick(itemView, !expanded)){
                        expanded = !expanded;
                        if(!expanded)
                            notifyItemChanged(getAdapterPosition());
                    }
                    break;
                case R.id.edit_word:
                    onWordListener.onChangeWord(getAdapterPosition(), foreignWord, localWord, typeText, itemView);
                    break;
                case R.id.add_detail:
                    onWordListener.onAddDetail(itemView, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<Word> words = new ArrayList<>();
    public void setWords(ArrayList<Word> words){
        this.words = words;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Eerily way of doing it
        // Variable 'view' is accessed from within inner class, needs to be final or effectively final error
        // Implemented Android Studio solving

        final View[] view = new View[1];

        ((AppCompatActivity)ApplicationManager.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view[0] = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_list_item, parent, false);
            }
        });

        return new ViewHolder(view[0], mOnWordListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String date = words.get(position).getDate();
        String foreignWord = words.get(position).getForeignWord();
        String localWord = words.get(position).getLocalWord();
        char type = words.get(position).getType();
        LinkedHashMap<String, String> details = words.get(position).getDetails();

        ((AppCompatActivity)ApplicationManager.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int color = MaterialColors.getColor(ApplicationManager.getContext(), R.attr.container_color, Color.WHITE);
                holder.itemView.setBackgroundTintList(ColorStateList.valueOf(color));
                holder.itemView.findViewById(R.id.detailsHolder).setVisibility(View.GONE);
                holder.expanded = false;
                holder.itemView.findViewById(R.id.edit_word).setBackgroundTintList(ColorStateList.valueOf(ApplicationManager.getContext().getResources().getColor(R.color.transparent, null)));
                holder.itemView.findViewById(R.id.add_detail).setBackgroundTintList(ColorStateList.valueOf(ApplicationManager.getContext().getResources().getColor(R.color.transparent, null)));
                holder.itemView.findViewById(R.id.deleteWord).setBackgroundTintList(ColorStateList.valueOf(ApplicationManager.getContext().getResources().getColor(R.color.transparent, null)));

                holder.date.setText(date);
                holder.foreignWord.setText(foreignWord);
                holder.localWord.setText(localWord);

                switch(type){
                    case 'N':
                        holder.typeText.setText("Noun");
                        holder.typeImage.setImageResource(R.drawable.circle_noun);
                        break;
                    case 'V':
                        holder.typeText.setText("Verb");
                        holder.typeImage.setImageResource(R.drawable.circle_verb);
                        break;
                    case 'A':
                        holder.typeText.setText("Adj.");
                        holder.typeImage.setImageResource(R.drawable.circle_adj);
                        break;
                    case 'E':
                        holder.typeText.setText("Expr.");
                        holder.typeImage.setImageResource(R.drawable.circle_expr);
                        break;
                    case 'O':
                        holder.typeText.setText("Other");
                        holder.typeImage.setImageResource(R.drawable.circle_other);
                        break;
                    default:
                        break;
                }
                for(int i=0; i<holder.container.getChildCount(); i++){
                    View v = holder.container.getChildAt(i);
                    if(v.getVisibility() == View.GONE) break;
                    v.setVisibility(View.GONE);
                    ((TextView)v.findViewById(R.id.detail_name)).setText("");
                    ((TextView)v.findViewById(R.id.detail_content)).setText("");
                }
                if(details != null && details.size() > 0){
                    int cnt = 0;
                    for(String category : details.keySet()){
                        TextView detailName, detailContent;
                        holder.container.getChildAt(cnt).setVisibility(View.VISIBLE);
                        detailName = holder.container.getChildAt(cnt).findViewById(R.id.detail_name);
                        detailContent = holder.container.getChildAt(cnt).findViewById(R.id.detail_content);
                        detailName.setText(category);
                        detailContent.setText(details.get(category));
                        cnt++;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public interface OnWordListener{
        boolean onWordClick(View view, boolean expanded);
        boolean onEditTextsClick(View view, boolean expanded);
        void onDeleteWord(int pos);
        void onAddDetail(View itemView, int pos);
        void onChangeWord(int pos, EditText foreignWord, EditText localWord, EditText type, View view);
    }
}
