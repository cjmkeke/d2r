package com.cjmkeke.mymemo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import jp.wasabeef.richeditor.RichEditor;

public class a extends RichEditor {
    public a(Context context) {
        super(context);
    }

    public a(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public a(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    public void setOnImagesClickListener(String url){
        Log.v("","" );
    }

}
