package com.cjmkeke.mymemo.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface {
    private Context mContext;

    public JavaScriptInterface(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void openHomepage(String url) {
        if (mContext != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
        }
    }
}