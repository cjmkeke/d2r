package com.cjmkeke.mymemo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import jp.wasabeef.richeditor.RichEditor;

public class RichEditorImageClick extends RichEditor implements onClickImagesListener{
    public RichEditorImageClick(Context context) {
        super(context);
    }

    public RichEditorImageClick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichEditorImageClick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickImagesListener(Context context, String url) {

        if (url.contains(url)){
            Toast.makeText(context, "tttt", Toast.LENGTH_SHORT).show();
        }


    }
}

