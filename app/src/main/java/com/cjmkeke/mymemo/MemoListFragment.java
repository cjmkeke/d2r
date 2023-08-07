package com.cjmkeke.mymemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MemoListFragment extends Fragment {

    private ImageView subMenu;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private AdapterMemoList adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DTOMemoList> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_memo_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        recyclerView = viewGroup.findViewById(R.id.memo_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new AdapterMemoList(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("memoList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DTOMemoList user = snapshot.getValue(DTOMemoList.class);
                    arrayList.add(user);
                }
                try {
                    Collections.sort(arrayList, new Comparator<DTOMemoList>() {
                        @Override
                        public int compare(DTOMemoList modelCommunity, DTOMemoList t1) {
                            return modelCommunity.getMainText().compareToIgnoreCase(t1.getTitle());
                        }
                    });
                } catch (Exception e) {

                }
                adapter = new AdapterMemoList(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        subMenu = viewGroup.findViewById(R.id.iv_sub_menu);
        subMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenuStyle);
                PopupMenu popup = new PopupMenu(contextThemeWrapper, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sub_menu, popup.getMenu());

                try {
                    Method method = popup.getClass().getDeclaredMethod("setForceShowIcon", boolean.class);
                    method.invoke(popup, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sub_write_memo :
                                Intent intent = new Intent(getActivity(), WriteMemoActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.two:
                                Toast.makeText(contextThemeWrapper, "로그아웃됨", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tree:
                                Toast.makeText(contextThemeWrapper, "Tree", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return false;
                    }
                });
                popup.show();
            }
        });
        return viewGroup;
    }
}