package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CodeView extends AppCompatActivity {

    private EditText codeEditor;
    private Intent intent;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button confirm;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_view);

        codeEditor = findViewById(R.id.et_code_view);
        confirm = findViewById(R.id.btn_confirm);
        cancel = findViewById(R.id.btn_cancel);

        intent = getIntent();
        String writeDate = intent.getStringExtra("writeDate");
        String writeToken = intent.getStringExtra("token");
        Log.v("1", writeDate);
        Log.v("2", writeToken);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("memoList").child(writeToken).child(writeDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String str = snapshot.child("mainText").getValue(String.class);
                codeEditor.setText(str);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(codeEditor.getText().toString());
                        Intent intent1 = new Intent(CodeView.this, MemoDBRead.class);
                        intent1.putExtra("mainText", codeEditor.getText().toString());
                        Log.v("1", codeEditor.getText().toString());
                        startActivity(intent1);

                        finish();
                        MemoDBRead memoDBRead = new MemoDBRead();
                        memoDBRead.outApp();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    finish();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}