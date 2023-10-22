package com.cjmkeke.mymemo.library;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class ColorEditText extends AppCompatEditText {

    private int currentColor = Color.BLACK;

    public ColorEditText(Context context) {
        super(context);
    }

    public ColorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEditTextTextColor(int color) {
        currentColor = color;
        applyTextColor(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (lengthAfter > lengthBefore) {
            applyTextColor(getText());
        }
    }

    private void applyTextColor(Editable editable) {
        int pinkColor = Color.parseColor("#E91E63");
        int lengthBeforeChange = getText().length() - editable.length();

        if (lengthBeforeChange < 0) {
            SpannableStringBuilder builder = new SpannableStringBuilder(editable);
            ForegroundColorSpan pinkColorSpan = new ForegroundColorSpan(pinkColor);
            builder.setSpan(pinkColorSpan, 0, editable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(builder);
        }
    }
}