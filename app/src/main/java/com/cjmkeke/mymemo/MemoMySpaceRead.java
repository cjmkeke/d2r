package com.cjmkeke.mymemo;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.cjmkeke.mymemo.R;

public class MemoMySpaceRead extends AppCompatActivity {
    private View black, orange, purple, pink, boldPurple, blue, boldGreen, green;
    private View blackBlue, yellow, white, blackPink;
    private EditText mainText;
    private int currentColor = Color.BLACK; // 현재 텍스트 색상 저장 변수
    private boolean applyColor = false; // 색상을 적용해야 할지 여부를 나타내는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_my_space_read);

        mainText = findViewById(R.id.et_mainText);

        pink = findViewById(R.id.color_pink);
        orange = findViewById(R.id.color_orange);
        purple = findViewById(R.id.color_purple);
        boldPurple = findViewById(R.id.color_bold_purple);
        blue = findViewById(R.id.color_blue);
        black = findViewById(R.id.color_black);
        boldGreen = findViewById(R.id.color_bold_green);
        green = findViewById(R.id.color_green);
        blackBlue = findViewById(R.id.color_black_blue);
        yellow = findViewById(R.id.color_yellow);
        white = findViewById(R.id.color_white);
        blackPink = findViewById(R.id.color_black_pink);

        currentColor = Color.BLACK;

        boldPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#673AB7");
                applyColor = true;
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#2196F3");
                applyColor = true;
            }
        });

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#000000");
                applyColor = true;
            }
        });

        boldGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#4CAF50");
                applyColor = true;
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#8BC34A");
                applyColor = true;
            }
        });

        blackBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#3F51B5");
                applyColor = true;
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#FFEB3B");
                applyColor = true;
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#FFFFFF");
                applyColor = true;
            }
        });

        blackPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#8F097E");
                applyColor = true;
            }
        });


        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#E91E63");
                applyColor = true;
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#F44336");
                applyColor = true;
            }
        });

        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentColor = Color.parseColor("#9C27B0");
                applyColor = true;
            }
        });

        mainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 이 기능에는 필요하지 않습니다.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 이 기능에는 필요하지 않습니다.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (applyColor) {
                    int cursorPosition = mainText.getSelectionStart();
                    int length = editable.length();

                    if (cursorPosition >= 0) {
                        // 오른쪽부터 왼쪽으로 선택한 색상을 적용합니다.
                        editable.setSpan(new ForegroundColorSpan(currentColor), cursorPosition - 1, cursorPosition, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                    applyColor = false; // 색상 변경이 완료되면 다음 입력에 대해 준비
                }
            }
        });
    }
}

