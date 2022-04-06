package com.stefan.helloworld;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WordsRecViewAdapter extends RecyclerView.Adapter <WordsRecViewAdapter.ViewHolder> {

    private final OnWordListener mOnWordListener;

    public WordsRecViewAdapter(OnWordListener onWordListener) {
        this.mOnWordListener = onWordListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView date, typeText;
        private final EditText foreignWord, localWord;

        private final ImageView typeImage;
        private final LinearLayout container;
        private final View itemView;
        private final OnWordListener onWordListener;
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
            itemView.findViewById(R.id.deleteWord).setOnClickListener(this);

            foreignWord.setOnClickListener(this);
            localWord.setOnClickListener(this);
            itemView.findViewById(R.id.edit_word).setOnClickListener(this);
            itemView.findViewById(R.id.add_detail).setOnClickListener(this);
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
                    Log.d("Main", "ok");
                    if(onWordListener.onEditTextsClick(itemView, !expanded)){
                        expanded = !expanded;
                        if(!expanded)
                            notifyItemChanged(getAdapterPosition());
                    }
                    break;
                case R.id.edit_word:
                    onWordListener.onChangeWord(getAdapterPosition(), foreignWord, localWord, itemView);
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
        for(Word word : words){
            Log.d("adapter", "words " + word.getDetails());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_list_item, parent, false);
        return new ViewHolder(view, mOnWordListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.date.setText(words.get(position).getDate());
        holder.foreignWord.setText(words.get(position).getForeignWord());
        holder.localWord.setText(words.get(position).getLocalWord());

        switch(words.get(position).getType()){
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

        HashMap<String, String> details = words.get(position).getDetails();


        Log.d("Words adapter1", "list: " + details);

        holder.container.removeAllViewsInLayout();
        if(details != null){
            List<String> detailsReversed = new ArrayList<>(details.keySet());
            //Collections.reverse(detailsReversed);
            for(String category : detailsReversed){
                //Log.d("Words adapter2", "list: " + detailsReversed + "cnt: " + holder.container.getChildCount() + " pos: " + position + " holder: " + holder.foreignWord.getText().toString());
                TextView detailName, detailContent;

                View detailUI = LayoutInflater.from(holder.container.getContext()).inflate(R.layout.word_details, holder.container, false);


                detailName = detailUI.findViewById(R.id.detail_name);
                detailContent = detailUI.findViewById(R.id.detail_content);
                detailName.setText(category);
                Log.d("main", "category " + category);
                detailContent.setText(details.get(category));

                holder.container.addView(detailUI);
            }
        }

        Log.d("main", "nr: " + holder.container.getChildCount());
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
        void onChangeWord(int pos, EditText foreignWord, EditText localWord, View view);
    }
}
