package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.p.WriteDateList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WriteMemoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static String profileImagesUrl;
    private static String tokenValue;

    private View boldRed, pink, purple, black;
    private EditText title, mainText;
    private TextView commit;
    private int currentColor = Color.BLACK;
    private boolean isPinkButtonPressed = false;

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser == null){
            Intent intent = new Intent(WriteMemoActivity.this, MemberJoin.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        title = findViewById(R.id.et_title);
        mainText = findViewById(R.id.et_mainText);
        commit = findViewById(R.id.tv_commit);

        if (firebaseUser != null){
            databaseReference.child("registeredUser").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    profileImagesUrl = snapshot.child(firebaseUser.getUid()).child("profile").getValue(String.class);
                    tokenValue = snapshot.child(firebaseUser.getUid()).child("token").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title.getText().toString()) || TextUtils.isEmpty(mainText.getText().toString())) {
                    Toast.makeText(WriteMemoActivity.this, "제목과 본문에 내용이 필요합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WriteMemoActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    WriteDateList writeDateList = new WriteDateList();
                    String date = writeDateList.getTimeFormatAdapterList();
                    databaseReference.child("memoList").child(date).child("title").setValue(title.getText().toString());
                    databaseReference.child("memoList").child(date).child("mainText").setValue(mainText.getText().toString());
                    databaseReference.child("memoList").child(date).child("profile").setValue(profileImagesUrl);
                    databaseReference.child("memoList").child(date).child("token").setValue(tokenValue);
                    databaseReference.child("memoList").child(date).child("color").setValue(currentColor);
                    finish();
                }
            }
        });


        purple = findViewById(R.id.color_purple);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = Color.parseColor("#9C27B0");
                setEditTextTextColor(color);
                customMakeToast("선택된 색상으로 바뀌었습니다. 'Purple'");

            }
        });

        black = findViewById(R.id.color_black);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = Color.parseColor("#001000");
                setEditTextTextColor(color);

                customMakeToast("선택된 색상으로 바뀌었습니다. 'Black'");
            }
        });

        boldRed = findViewById(R.id.color_bold_red);
        boldRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = Color.parseColor("#F44336");
                setEditTextTextColor(color);

                customMakeToast("선택된 색상으로 바뀌었습니다. 'Bold Red'");
            }
        });

        pink = findViewById(R.id.color_pink);
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int color = Color.parseColor("#E91E63");
                setEditTextTextColor(color);
                isPinkButtonPressed = true; // Enable pink text color change
                customMakeToast("선택된 색상으로 바뀌었습니다. 'Pink'");
            }
        });
    }


    public void setEditTextTextColor(int color) {
        currentColor = color;
        mainText.setTextColor(currentColor);
    }

    public void customMakeToast(String messages){
        Toast toast = Toast.makeText(WriteMemoActivity.this, messages, Toast.LENGTH_SHORT);
        toast.show();

        int customDuration = 1000; // 1 second in milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, customDuration);
    }
}