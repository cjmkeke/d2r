package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShareFriendSearch extends AppCompatActivity {

    private static final String TAG = "ShareFriendSearch";
    private EditText shareEmail;
    private String myEmail;
    private Button searchCommit;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String token;
    private String profile;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private ImageView profileReconfirm;
    private TextView emailReconfirm;

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser == null) {
            finish();
            Toast.makeText(this, R.string.service_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_friend_search);

        shareEmail = findViewById(R.id.et_share_email);
        searchCommit = findViewById(R.id.bt_share_search);
        profileReconfirm = findViewById(R.id.iv_user_profile_reconfirm);
        profileReconfirm.setClipToOutline(true);
        emailReconfirm = findViewById(R.id.tv_email_reconfirm);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("registeredUser");
        try {
            databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    myEmail = snapshot.child("email").getValue(String.class);
                    Log.v("내 이메일", myEmail);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (NullPointerException nullPointerException){
        }


        shareEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String resultEmail = shareEmail.getText().toString();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean snapFlag = true;
                        for (DataSnapshot searchSnap : snapshot.getChildren()) {

                            String resultSnapProfile = searchSnap.child("profile").getValue(String.class);
                            String resultSnapEmail = searchSnap.child("email").getValue(String.class);
                            if (snapFlag) {
                                if (resultEmail.contains(resultSnapEmail)) {
                                    snapFlag = false;
                                    Glide.with(ShareFriendSearch.this).load(resultSnapProfile).into(profileReconfirm);
                                    emailReconfirm.setText("검색하신 이메일 " + "(" + resultSnapEmail + ")" + "\n프로필 사진의 주인이 맞는지 확인해주세요.\n맞는지 확인하신후 추가 버튼을 눌러주시길 바랍니다.");
                                } else {
                                    Glide.with(ShareFriendSearch.this).load(R.drawable.icons_empty).into(profileReconfirm);
                                    emailReconfirm.setText(R.string.share_messages3);
                                    if (TextUtils.isEmpty(shareEmail.getText().toString())) {
                                        Glide.with(ShareFriendSearch.this).load(R.drawable.profile_round_10dp).into(profileReconfirm);
                                        emailReconfirm.setText(null);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        searchCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = shareEmail.getText().toString();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean friendExists = false;

                        for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                            token = friendSnapshot.child("token").getValue(String.class);
                            profile = friendSnapshot.child("profile").getValue(String.class);
                            String email = friendSnapshot.child("email").getValue(String.class);
                            if (str.equals(email)) {
                                friendExists = true;
                                break;
                            }
                        }

                        if (friendExists) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myFriendsRef = database.getReference("registeredUser").child(token);

                            myFriendsRef.child("myFriend").orderByValue().equalTo(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String str = shareEmail.getText().toString();

                                    if (dataSnapshot.exists()) {
                                        // 이미 친구가 있는 경우
                                        Toast.makeText(ShareFriendSearch.this, "이미 친구가 존재합니다", Toast.LENGTH_SHORT).show();
                                    } else if (myEmail.equals(str)) {
                                        // 자기 자신 친구 추가 금지
                                        Toast.makeText(ShareFriendSearch.this, "자기 자신을 친구추가 할수 없습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 친구 추가 로직
                                        myFriendsRef.child("myFriend").push().setValue(firebaseUser.getUid());

                                        DatabaseReference addMyRef = database.getReference("registeredUser").child(firebaseUser.getUid()).child("addFriend");
                                        String friendKey = addMyRef.push().getKey();

                                        addMyRef.child(friendKey).child("token").setValue(token);
                                        addMyRef.child(friendKey).child("email").setValue(str); // 사용자가 입력한 이메일 사용
                                        addMyRef.child(friendKey).child("profile").setValue(profile);
                                        addMyRef.child(friendKey).child("friendKey").setValue(friendKey);
                                        Toast.makeText(ShareFriendSearch.this, "친구가 성공적으로 추가되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("FirebaseExample", "데이터 읽기 오류: " + databaseError.getMessage());
                                }
                            });

                        } else {
                            // friendExists가 false인 경우
                            // 이곳에 관련 처리 추가
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FirebaseExample", "Error reading data: " + databaseError.getMessage());
                    }
                });
            }
        });

    }
}