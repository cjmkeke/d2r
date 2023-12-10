package com.cjmkeke.mymemo.modelClass;

public class Memo {
    private String date;
    private String content;

    public Memo() {
        // Default constructor required for calls to DataSnapshot.getValue(Memo.class)
    }

    public Memo(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

