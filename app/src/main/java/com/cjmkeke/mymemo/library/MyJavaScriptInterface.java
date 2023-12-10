package com.cjmkeke.mymemo.library;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class MyJavaScriptInterface {
    private Context context;

    public MyJavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}