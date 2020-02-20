package com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.Article_DetailsActivity;
import com.example.bolutho.Fragments.ModelClasses.Articles;
import com.example.bolutho.R;

import java.util.ArrayList;
import java.util.List;

public class Article_Paragraph extends RecyclerView.Adapter<Article_Paragraph.ArticleViewHolder> {
    List<Articles> list;
    public ClickListener clickListener;

    public Article_Paragraph(List<Articles> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.article_paragraph, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.paragraph.setText(list.get(position).getArticle_Heading());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setClickListener(ClickListener clickListner) {
        this.clickListener = clickListner;
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView paragraph;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            paragraph = itemView.findViewById(R.id.paragraph);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }
    public interface ClickListener {
        void onItemClick(int position,View view);
    }
}
