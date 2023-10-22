package com.cjmkeke.mymemo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.adapter.AdapterMemo;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseStorageMemo extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private AdapterMemo adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelMemoWrite> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String email;
    private TextView emptyMessages;
    private TextView loginMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_database_storage_memo, container, false);

        loginMessages = viewGroup.findViewById(R.id.tv_login_messages);
        emptyMessages = viewGroup.findViewById(R.id.empty_messages);
        emptyMessages.setVisibility(View.GONE);

        recyclerView = viewGroup.findViewById(R.id.memo_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        recyclerView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            loginMessages.setVisibility(View.VISIBLE);
        } else {
            loginMessages.setVisibility(View.GONE);
        }

        if (firebaseUser != null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            Query query = firebaseDatabase.getReference("memoList").child(firebaseUser.getUid());
            query.orderByValue().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ModelMemoWrite user = snapshot.getValue(ModelMemoWrite.class);
                        arrayList.add(user);
                    }

                    if (arrayList.isEmpty()){
                        emptyMessages.setVisibility(View.VISIBLE);
                    } else {
                        emptyMessages.setVisibility(View.GONE);
                    }

                    Collections.reverse(arrayList);
                    adapter = new AdapterMemo(arrayList, getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


//            databaseReference = firebaseDatabase.getReference("memoList").child(firebaseUser.getUid());
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    arrayList.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        ModelMemoWrite user = snapshot.getValue(ModelMemoWrite.class);
//                        arrayList.add(user);
//                    }
//
//                    adapter = new AdapterMemo(arrayList, getContext());
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });
        } else {
        }

        if (firebaseUser != null) {
            DatabaseReference userReference = firebaseDatabase.getReference("registeredUser").child(firebaseUser.getUid());
            userReference.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    email = snapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        return viewGroup;
        // Inflate the layout for this fragment
    }
}