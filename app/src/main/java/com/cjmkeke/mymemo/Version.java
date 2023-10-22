package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Version extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView messages, download, systemVersion;

    private int resultOldVersion;
    private int resultNewVersion;
    private String resultMessages;
    private String newResultMessages;
    private final int appVersion = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        messages = findViewById(R.id.tv_version);
        download = findViewById(R.id.tv_download);
        systemVersion = findViewById(R.id.tv_system_app_version);
        systemVersion.setText(String.valueOf(" : "+appVersion));

        databaseReference.child("settings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                resultNewVersion = snapshot.child("newVersion").getValue(Integer.class);
                if (resultNewVersion == appVersion){
                    resultMessages = snapshot.child("versionMessages").getValue(String.class);
                    messages.setText(resultMessages);
                } else {
                    newResultMessages = snapshot.child("newVersionMessages").getValue(String.class);
                    messages.setText(newResultMessages);
                    download.setVisibility(View.VISIBLE);
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String storeUrl = "https://play.google.com/store/apps/details?id=com.cjmkeke.mymemo";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl));
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}