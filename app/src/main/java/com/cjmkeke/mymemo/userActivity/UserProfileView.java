package com.cjmkeke.mymemo.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.R;
public class UserProfileView extends Activity {

    private Intent intent;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_profile_view);

        imageView = findViewById(R.id.imageView);

        intent = getIntent();
        String profileUrl = intent.getStringExtra("getImagesUrl");
        Glide.with(this).load(profileUrl).into(imageView);

    }
}