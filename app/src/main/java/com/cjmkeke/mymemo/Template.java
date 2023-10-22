package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Template extends AppCompatActivity {

    private ImageView template1, template2, template3, template4,template5, template6;
    private View templateBackColorOrange;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private String templateUrl1,templateUrl2,templateUrl3,templateUrl4,templateUrl5,templateUrl6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        templateBackColorOrange = findViewById(R.id.color_template_orange);
        template1 = findViewById(R.id.iv_template_1);
        template2 = findViewById(R.id.iv_template_2);
        template3 = findViewById(R.id.iv_template_3);
        template4 = findViewById(R.id.iv_template_4);
        template5 = findViewById(R.id.iv_template_5);
        template6 = findViewById(R.id.iv_template_6);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("templateUrl");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                templateUrl1 = snapshot.child("template1").getValue(String.class);
                templateUrl2 = snapshot.child("template2").getValue(String.class);
                templateUrl3 = snapshot.child("template3").getValue(String.class);
                templateUrl4 = snapshot.child("template4").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        template1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Template.this, MemoDBWriting.class);
                intent.putExtra("NoteTemplate", templateUrl1);
                startActivity(intent);
            }
        });

        template2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Template.this, MemoDBWriting.class);
                intent.putExtra("NoteTemplate", templateUrl2);
                startActivity(intent);
            }
        });

        template3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Template.this, MemoDBWriting.class);
                intent.putExtra("NoteTemplate", templateUrl3);
                startActivity(intent);
            }
        });


        template4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Template.this, MemoDBWriting.class);
                intent.putExtra("NoteTemplate", templateUrl4);
                startActivity(intent);
            }
        });

        templateBackColorOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}