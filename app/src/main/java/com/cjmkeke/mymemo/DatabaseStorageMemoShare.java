package com.cjmkeke.mymemo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjmkeke.mymemo.adapter.AdapterMemoShare;
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
import java.util.Comparator;
import java.util.HashSet;

public class DatabaseStorageMemoShare extends Fragment {

    private static final String TAG = "DatabaseStorageMemoShare";
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private AdapterMemoShare adapter;
    private ArrayList<ModelMemoWrite> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView emptyMessages;
    private TextView loginMessages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_database_storage_memo_share, container, false);
        Log.v(TAG,TAG);
        emptyMessages = viewGroup.findViewById(R.id.empty_messages);
        loginMessages = viewGroup.findViewById(R.id.tv_login_messages);
        recyclerView = viewGroup.findViewById(R.id.memo_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<>();
        adapter = new AdapterMemoShare(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            loginMessages.setVisibility(View.VISIBLE);
            emptyMessages.setVisibility(View.GONE);
        } else {
            loginMessages.setVisibility(View.GONE);
        }

        if (firebaseUser != null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("memoList").child(firebaseUser.getUid());
            fetchMemoDataForFriends();
        }
        return viewGroup;
    }

    private void fetchMemoDataForFriends() {
        Query query = firebaseDatabase.getReference().child("memoList");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ModelMemoWrite> tempList = new ArrayList<>(); // 변경된 데이터를 저장할 임시 목록

                for (DataSnapshot memoSnap : snapshot.getChildren()) {
                    for (DataSnapshot userSnap : memoSnap.getChildren()) {
                        ModelMemoWrite memo = userSnap.getValue(ModelMemoWrite.class);

                        // openToken 필드가 사용자의 토큰을 포함하고 있는지 확인
                        DataSnapshot openTokenSnap = userSnap.child("openToken");
                        if (memo != null && openTokenSnap.hasChild(firebaseUser.getUid())) {
                            tempList.add(memo);
                        }
                    }
                }

                // 중복 제거를 위해 HashSet을 사용합니다
                HashSet<ModelMemoWrite> uniqueMemoSet = new HashSet<>(tempList);

                // 중복이 없는 값을 저장할 ArrayList를 만듭니다
                ArrayList<ModelMemoWrite> uniqueMemoList = new ArrayList<>(uniqueMemoSet);

                // 정렬
                Collections.sort(uniqueMemoList, new Comparator<ModelMemoWrite>() {
                    @Override
                    public int compare(ModelMemoWrite o1, ModelMemoWrite o2) {
                        return Long.compare(o2.getRecyclerDateList(), o1.getRecyclerDateList());
                    }
                });

                if (uniqueMemoList.isEmpty()){
                    emptyMessages.setVisibility(View.VISIBLE);
                } else {
                    emptyMessages.setVisibility(View.GONE);
                }

                Log.v("T",uniqueMemoList.toString());

                // RecyclerView에 데이터 설정
                arrayList.clear();
                arrayList.addAll(uniqueMemoList);

                // 어댑터를 업데이트합니다.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터베이스 오류 처리
            }
        });
    }
}
