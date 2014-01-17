package com.example.myapplication8;

/**
 * Created by SBaar on 1/3/14.
 */
public class QuoteModel {

    private String text;
    private boolean selected;
    private boolean mIsExpanded;
    private int mCollapsedHeight;
    private int mExpandedHeight;

    public QuoteModel(String text) {
        this.text = text;
        selected = false;
        mCollapsedHeight = 200;//TODO generalize cell default height
        mIsExpanded = false;
        mExpandedHeight = -1;
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

    public boolean isExpanded() {
        return mIsExpanded;
    }
    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }
    public int getCollapsedHeight() {
        return mCollapsedHeight;
    }
}