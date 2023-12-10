package com.cjmkeke.mymemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.FriendsPopup;
import com.cjmkeke.mymemo.R;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AdapterFriendsPopup extends RecyclerView.Adapter<AdapterFriendsPopup.CustomViewHolder> {

    private static String TAG = "AdapterMyFriends";
    private ArrayList<ModelMemoWrite> arrayList;
    private Context context;
    private String token;
    private String writeDate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<String> removeList;
    private boolean isOpens = true;

    public AdapterFriendsPopup(ArrayList<ModelMemoWrite> arrayList, Context context, String token, String writeDate) {
        this.arrayList = arrayList;
        this.context = context;
        this.token = token;
        this.writeDate = writeDate;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_friends_popup, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        String selectToken = arrayList.get(pos).getToken();
        holder.email.setText(arrayList.get(pos).getEmail());
        Glide.with(context).load(arrayList.get(pos).getProfile()).into(holder.profile);

        if (pos == arrayList.size() -1){
            holder.endLine.setVisibility(View.GONE);
        }

        databaseReference.child("memoList").child(token).child(writeDate).child("openToken").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isSharing = false;
                for (DataSnapshot dst1 : snapshot.getChildren()) {
                    String str = dst1.getValue(String.class);
                    Log.v("str", str);
                    Log.v("select", selectToken);

                    if (str.contains(selectToken)) {
                        isSharing = true;
                        break;
                    }
                }

                if (isSharing) {
                    holder.openSharing.setText("공유중");
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icons_sharing_size);
                    int leftPadding = 60; // Padding for the text (adjust as needed)
                    int leftInset = -10; // Inset for the drawable (adjust as needed)
                    holder.openSharing.setPaddingRelative(leftPadding, 0, 0, 0);
                    InsetDrawable insetDrawable = new InsetDrawable(drawable, leftInset, 0, 0, 0);
                    insetDrawable.setBounds(0, 0, 60, 60);
                    holder.openSharing.setCompoundDrawablesRelative(insetDrawable, null, null, null);
                } else {
                    holder.openSharing.setText("공유하지 않음");
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icons_sharing_no_size);
                    int leftPadding = 60; // Padding for the text (adjust as needed)
                    int leftInset = -10; // Inset for the drawable (adjust as needed)
                    holder.openSharing.setPaddingRelative(leftPadding, 0, 0, 0);
                    InsetDrawable insetDrawable = new InsetDrawable(drawable, leftInset, 0, 0, 0);
                    insetDrawable.setBounds(0, 0, 60, 60);
                    holder.openSharing.setCompoundDrawablesRelative(insetDrawable, null, null, null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("memoList").child(token).child(writeDate).child("openToken").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dst1 : snapshot.getChildren()) {
                    String str = dst1.getValue(String.class);

                    if (str.contains(selectToken)){
                        isOpens = false;
                    } else {
                        isOpens = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView email;
        private TextView openSharing;
        private View endLine;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.email = itemView.findViewById(R.id.tv_email);
            this.profile = itemView.findViewById(R.id.iv_profile);
            this.openSharing = itemView.findViewById(R.id.tv_open);
            this.endLine = itemView.findViewById(R.id.vw_line);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            openSharing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String selectToken = arrayList.get(position).getToken();
                    if (isOpens) {
                        databaseReference.child("memoList").child(token).child(writeDate).child("openToken").child(selectToken).setValue(selectToken);
                        isOpens = false;
                    } else {
                        databaseReference.child("memoList").child(token).child(writeDate).child("openToken").child(selectToken).removeValue();
                        isOpens = true;
                    }
                }
            });
        }
    }
}

