package com.cjmkeke.mymemo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.modelClass.ModelMyFriends;
import com.cjmkeke.mymemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterConnectedUser extends RecyclerView.Adapter<AdapterConnectedUser.CustomViewHolder> {

    private static String TAG = "AdapterConnectedUser";

    private ArrayList<ModelMyFriends> arrayList;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference addFriendsRef;
    private DatabaseReference myFriendsRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ArrayList<String> removeList;

    public AdapterConnectedUser(ArrayList<ModelMyFriends> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connected_user, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();
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

        private ImageView IvProfile;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.IvProfile = itemView.findViewById(R.id.iv_profile);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            removeList = new ArrayList<>();

            firebaseDatabase = FirebaseDatabase.getInstance();
            IvProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String name = arrayList.get(pos).getName();
                    Toast.makeText(context, "선택된 유저는 " + name +" 입니다.", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}

