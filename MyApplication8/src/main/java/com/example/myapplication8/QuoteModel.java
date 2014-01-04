package com.example.myapplication8;

/**
 * Created by SBaar on 1/3/14.
 */
public class QuoteModel {

    private String text;
    private boolean selected;

    public QuoteModel(String text) {
        this.text = text;
        selected = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}