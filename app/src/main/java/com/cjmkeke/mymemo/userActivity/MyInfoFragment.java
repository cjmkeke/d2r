package com.cjmkeke.mymemo.userActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.ShareUserList;
import com.cjmkeke.mymemo.R;
import com.cjmkeke.mymemo.ShareFriendSearch;
import com.cjmkeke.mymemo.Version;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyInfoFragment extends Fragment {

    private TextView login, logout, signature, email, searchFriend, friendList, name, profileSettings, appUpdate;
    private ImageView profile;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private MyInfoFragment myInfoFragment;
    //    private EditText shareMemo;
//    private Button search;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String token;
    private String profileImages;
    private String userName;
    private String defaultImages;


    @Override
    public void onStart() {
        super.onStart();

        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase1.getReference();
        FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser1 = firebaseAuth1.getCurrentUser();
        if (firebaseUser1 != null && firebaseUser1.getUid() != null) {
            login.setVisibility(View.GONE);
            signature.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);

            databaseReference1.child("settings").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    defaultImages = snapshot.child("defaultProfileImages").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            databaseReference1.child("registeredUser").child(firebaseUser1.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String resultEmail = snapshot.child("email").getValue(String.class);
                    userName = snapshot.child("name").getValue(String.class);
                    email.setText(resultEmail);
                    name.setText(userName);

                    if (getActivity() != null) {
                        if (snapshot.child("profile").exists()) {
                            profileImages = snapshot.child("profile").getValue(String.class);
                            Glide.with(getActivity()).load(profileImages).into(profile);
                        } else {
                            Glide.with(getActivity()).load(defaultImages).into(profile);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            email.setText(R.string.service_error);
            name.setText(R.string.service_error);
            signature.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_my_info, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("registeredUser");
        myInfoFragment = new MyInfoFragment();

        login = viewGroup.findViewById(R.id.tv_login);
        profile = viewGroup.findViewById(R.id.iv_profile);
        profile.setClipToOutline(true);
        logout = viewGroup.findViewById(R.id.tv_logout);
        signature = viewGroup.findViewById(R.id.tv_signature);
        email = viewGroup.findViewById(R.id.tv_email);
        searchFriend = viewGroup.findViewById(R.id.tv_search_friend);
        profileSettings = viewGroup.findViewById(R.id.tv_profile_settings);
        name = viewGroup.findViewById(R.id.tv_name);
        appUpdate = viewGroup.findViewById(R.id.tv_app_update);

        friendList = viewGroup.findViewById(R.id.tv_friend_list);
        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShareUserList.class);
                startActivity(intent);
            }
        });

        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShareFriendSearch.class);
                startActivity(intent);
            }
        });

        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberSettings.class);
                startActivity(intent);
            }
        });

        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberJoin.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myInfoFragment).commit();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberLogin.class);
                startActivity(intent);
            }
        });

        appUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Version.class);
                startActivity(intent);
            }
        });

        return viewGroup;
    }
}