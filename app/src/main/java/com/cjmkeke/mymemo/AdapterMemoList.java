package com.cjmkeke.mymemo;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterMemoList extends RecyclerView.Adapter<AdapterMemoList.CustomViewHolder> {

    private static String TAG = "AdapterMemoList";

    private ArrayList<DTOMemoList> arrayList;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public AdapterMemoList(ArrayList<DTOMemoList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();
        holder.mainText.setText(arrayList.get(pos).getMainText());
        holder.title.setText(arrayList.get(pos).getTitle());
        holder.profile.setClipToOutline(true);
        Glide.with(context).load(arrayList.get(pos).getProfile()).into(holder.profile);
        holder.title.setTextColor(arrayList.get(pos).getColor());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView title, mainText;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainText = itemView.findViewById(R.id.tv_mainText);
            this.title = itemView.findViewById(R.id.tv_title);
            this.profile = itemView.findViewById(R.id.iv_profile);

            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("MyCamping");

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

        }
    }
}

