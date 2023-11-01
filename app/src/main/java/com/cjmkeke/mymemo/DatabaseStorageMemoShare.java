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
import android.widget.Toast;

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

    private String friendToken;
    private ArrayList<String> friendsArrays;

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
            ssss();
        } else {
        }

        return viewGroup;
    }

    private void ssss() {
        friendsArrays = new ArrayList<>();

        Query query = firebaseDatabase.getReference("registeredUser")
                .child(firebaseUser.getUid())
                .child("myFriend");

        query.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot tokenSnap : snapshot.getChildren()) {
                    friendToken = tokenSnap.getValue(String.class);
                    if (friendToken != null) {
                        friendsArrays.add(friendToken);
                    }
                }

                // 친구 토큰이 있는지 확인하고 나서 메모 데이터를 가져오도록 합니다.
                if (!friendsArrays.isEmpty()) {
                    fetchMemoDataForFriends();
                } else {
                    // 친구가 없는 경우 처리
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터베이스 오류 처리
            }
        });
    }



    private void fetchMemoDataForFriends() {
        ArrayList<ModelMemoWrite> tempList = new ArrayList<>(); // 모든 메모를 저장할 임시 목록

        for (String token : friendsArrays) {
            if (token != null) {
                Query query = firebaseDatabase.getReference().child("memoList").child(token);
                query.orderByChild("recyclerDateList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot showSnap : snapshot.getChildren()) {
                            ModelMemoWrite user = showSnap.getValue(ModelMemoWrite.class);
                            if (user != null && user.isPublicKey()) {
                                tempList.add(user);
                            }
                        }

                        // HashSet을 사용하여 중복 제거
                        HashSet<ModelMemoWrite> uniqueMemoSet = new HashSet<>(tempList);

                        // HashSet을 다시 ArrayList로 변환
                        ArrayList<ModelMemoWrite> uniqueMemoList = new ArrayList<>(uniqueMemoSet);

                        // 모든 친구의 메모를 가져왔을 때 실행되는 곳
                        // recyclerDateList를 기준으로 내림차순 정렬
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
    }





}

