package com.cjmkeke.mymemo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.adapter.MyPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DatabaseStorageMain extends Fragment {

    private ImageView subMenu;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String email;
    private String writeDateList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_database_stroage_main, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        try {
            databaseReference.child("registeredUser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    email = snapshot.child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (NullPointerException nullPointerException) {

        }

        MyPagerAdapter viewPager2Adapter = new MyPagerAdapter(getChildFragmentManager(), getLifecycle());
        ViewPager2 viewPager2 = viewGroup.findViewById(R.id.pager);
        viewPager2.setAdapter(viewPager2Adapter);
        viewPager2.setSaveEnabled(false);

        //=== TabLayout기능 추가 부분 ============================================
        TabLayout tabLayout = viewGroup.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                if (position == 0) {
                    tab.setText("내 메모");
                } else if (position == 1) {
                    tab.setText("공유된 메모");
                }

            }
        }).attach();
        //========================================================================

        subMenu = viewGroup.findViewById(R.id.iv_sub_menu);
        subMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.PopupMenuStyle);
                PopupMenu popup = new PopupMenu(contextThemeWrapper, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_memo, popup.getMenu());

                try {
                    Method method = popup.getClass().getDeclaredMethod("setForceShowIcon", boolean.class);
                    method.invoke(popup, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.memo_write:

                                try {
                                    if (firebaseUser != null) {
                                        Intent intent = new Intent(getActivity(), MemoDBWriting.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(contextThemeWrapper, R.string.service_error, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (NullPointerException e) {
                                }

                                break;
                            case R.id.memo_remove:

                                try {
                                    Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.item_dialog_messages);
                                    TextView textView = dialog.findViewById(R.id.tv_messages);
                                    textView.setText("모든 메모를 삭제 하시겠습니까?\n삭제하면 더이상 복구할수 없습니다.");
                                    Button okButton = dialog.findViewById(R.id.btn_01);
                                    okButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (firebaseUser != null) {
                                                int index = email.indexOf("@");
                                                String str = email.substring(0, index);
                                                Log.v("tag", str);
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference storageReference = storage.getReference(str + "_memo_images");
                                                Log.v("tag", str + "_memo_images");

                                                storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                    @Override
                                                    public void onSuccess(ListResult listResult) {
                                                        for (StorageReference deleteRef : listResult.getItems()) {
                                                            deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.v("tag", "파일이 성공적으로 삭제되었습니다: " + deleteRef.getName());
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e("tag", "파일 삭제 오류 " + deleteRef.getName() + ": " + e.getMessage());
                                                                }
                                                            });
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("tag", "파일 목록 열람 오류: " + e.getMessage());
                                                    }
                                                });

                                                databaseReference.child("memoList").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        for (DataSnapshot deleteSnap : snapshot.getChildren()) {
                                                            writeDateList = deleteSnap.child("recyclerDate").getValue(String.class);
                                                            Log.v("삭제할 날짜 데이터", writeDateList);

                                                            for (DataSnapshot writeDateSnap : snapshot.child(writeDateList).child("boardImages").getChildren()) {
                                                                String imagesUrl = writeDateSnap.getValue(String.class);
                                                                Log.v("삭제할 이미지 주소 획득", imagesUrl);

                                                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                                                StorageReference storageReference1 = firebaseStorage.getReference().child(str + "_memo_images");
                                                                storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(imagesUrl);
                                                                storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                                databaseReference.child("memoList").child(firebaseUser.getUid()).removeValue();
                                                Toast.makeText(contextThemeWrapper, "모든 메모를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(contextThemeWrapper, R.string.service_error, Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                    Button cancelButton = dialog.findViewById(R.id.btn_02);
                                    cancelButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                } catch (NullPointerException e) {
                                }

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
