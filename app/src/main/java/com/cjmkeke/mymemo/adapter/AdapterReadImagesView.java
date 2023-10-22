package com.cjmkeke.mymemo.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.cjmkeke.mymemo.MemoDBRead;
import com.cjmkeke.mymemo.R;
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.modelClass.ModelImages;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterReadImagesView extends RecyclerView.Adapter<AdapterReadImagesView.CustomViewHolder> {

    private static String TAG = "AdapterMemoList";

    private ArrayList<ModelImages> arrayList;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public AdapterReadImagesView(ArrayList<ModelImages> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_read_images, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Glide.with(context).load(arrayList.get(pos).getImages0()).into(holder.images);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView images;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.images = itemView.findViewById(R.id.iv_images);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(pos).getImages0()));
                    context.startActivity(intent);
                    Toast.makeText(context, arrayList.get(pos).getImages0(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

