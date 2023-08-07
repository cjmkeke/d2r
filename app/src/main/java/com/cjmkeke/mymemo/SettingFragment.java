package com.cjmkeke.mymemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    private TextView login, logout, signature, email;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private SettingFragment settingFragment;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("registeredUser");

        if (firebaseAuth.getUid() != null) {
            login.setVisibility(View.GONE);
            signature.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String resultEmail = snapshot.child(firebaseUser.getUid()).child("email").getValue(String.class);
                    email.setText(resultEmail);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            signature.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        settingFragment = new SettingFragment();

        login = viewGroup.findViewById(R.id.tv_login);
        logout = viewGroup.findViewById(R.id.tv_logout);
        signature = viewGroup.findViewById(R.id.tv_signature);
        email = viewGroup.findViewById(R.id.tv_email);

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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberLogin.class);
                startActivity(intent);
            }
        });

        return viewGroup;
    }
}