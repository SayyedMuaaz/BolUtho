package com.example.bolutho.Fragments.ModelClasses;

public class Articles {
    private String article_Heading,article_Paragraph;

    public String getArticle_Heading() {
        return article_Heading;
    }

    public void setArticle_Heading(String article_Heading) {
        this.article_Heading = article_Heading;
    }

    public String getArticle_Paragraph() {
        return article_Paragraph;
    }

    public void setArticle_Paragraph(String article_Paragraph) {
        this.article_Paragraph = article_Paragraph;
    }

    public Articles() {
    }

    public Articles(String article_Heading, String article_Paragraph) {
        this.article_Heading = article_Heading;
        this.article_Paragraph = article_Paragraph;
    }
}
