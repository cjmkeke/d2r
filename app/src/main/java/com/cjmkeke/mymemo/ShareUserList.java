package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.cjmkeke.mymemo.adapter.AdapterMyFriends;
import com.cjmkeke.mymemo.modelClass.ModelMyFriends;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShareUserList extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private AdapterMyFriends adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelMyFriends> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_user_list);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("registeredUser");

        if (firebaseUser == null) {
            finish();
            Toast.makeText(this, R.string.service_error, Toast.LENGTH_SHORT).show();
        } else {
            recyclerView = findViewById(R.id.myFriendRecyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(ShareUserList.this);
            layoutManager.setItemPrefetchEnabled(false);
            recyclerView.setLayoutManager(layoutManager);
            arrayList = new ArrayList<>();
            adapter = new AdapterMyFriends(arrayList, ShareUserList.this);
            recyclerView.setAdapter(adapter);

            databaseReference.child(firebaseUser.getUid()).child("addFriend").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                        ModelMyFriends user = friendSnapshot.getValue(ModelMyFriends.class);
                        arrayList.add(user);
                    }
                    adapter = new AdapterMyFriends(arrayList, ShareUserList.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


    }
}