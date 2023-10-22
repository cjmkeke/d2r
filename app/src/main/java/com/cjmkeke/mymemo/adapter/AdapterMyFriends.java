package com.cjmkeke.mymemo.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.modelClass.ModelMyFriends;
import com.cjmkeke.mymemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterMyFriends extends RecyclerView.Adapter<AdapterMyFriends.CustomViewHolder> {

    private static String TAG = "AdapterMemoList";

    private ArrayList<ModelMyFriends> arrayList;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference addFriendsRef;
    private DatabaseReference myFriendsRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ArrayList<String> removeList;

    public AdapterMyFriends(ArrayList<ModelMyFriends> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_friend, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();
        holder.email.setText(arrayList.get(pos).getEmail());

        if (arrayList.get(pos).getProfile() != null) {
            Glide.with(holder.itemView)
                    .load(arrayList.get(position).getProfile())
                    .circleCrop()
                    .into(holder.IvProfile);
        }

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView email, remove;
        private ImageView IvProfile;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.email = itemView.findViewById(R.id.tv_email);
            this.IvProfile = itemView.findViewById(R.id.iv_profile);
            this.remove = itemView.findViewById(R.id.tv_remove);


            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            removeList = new ArrayList<>();

            firebaseDatabase = FirebaseDatabase.getInstance();
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    addFriendsDelete(pos);
                    myFriendsDelete(pos);
                }
            });


        }
    }

    private void addFriendsDelete(int pos) {
        String friendKey = arrayList.get(pos).getFriendKey();
        addFriendsRef = firebaseDatabase.getReference("registeredUser").child(firebaseUser.getUid()).child("addFriend");
        addFriendsRef.child(friendKey).removeValue();
    }


    private void myFriendsDelete(int pos) {
        String token = arrayList.get(pos).getToken();
        Log.v("token", token);

        myFriendsRef = firebaseDatabase.getReference("registeredUser").child(token).child("myFriend");
        myFriendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot deleteSnap : snapshot.getChildren()) {

                    String tokenSnap = deleteSnap.getValue(String.class);
                    removeList.add(tokenSnap);
                    Log.v("tokenSnap", tokenSnap);


                    myFriendsRef.equalTo(tokenSnap).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            myFriendsRef.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

