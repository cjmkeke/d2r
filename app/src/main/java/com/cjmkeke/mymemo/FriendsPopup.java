package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.cjmkeke.mymemo.adapter.AdapterFriendsPopup;
import com.cjmkeke.mymemo.adapter.AdapterMyFriends;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.cjmkeke.mymemo.modelClass.ModelMyFriends;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendsPopup extends Activity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private AdapterFriendsPopup adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelMemoWrite> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Intent intentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friends_popup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("registeredUser"); // 수정하세요

        intentAdapter = getIntent();
        String token = intentAdapter.getStringExtra("token");
        String writeDate = intentAdapter.getStringExtra("writeDate");

        recyclerView = findViewById(R.id.recyclear_view_popup); // 수정하세요
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(FriendsPopup.this);
        layoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
//        adapter = new AdapterFriendsPopup(arrayList, FriendsPopup.this);
        recyclerView.setAdapter(adapter);

        databaseReference.child(firebaseUser.getUid()).child("addFriend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    ModelMemoWrite user = friendSnapshot.getValue(ModelMemoWrite.class); // 수정하세요
                    arrayList.add(user);
                }
                adapter = new AdapterFriendsPopup(arrayList, FriendsPopup.this, token, writeDate);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}