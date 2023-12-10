package com.cjmkeke.mymemo.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.MemoDBRead;
import com.cjmkeke.mymemo.R;
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.cjmkeke.mymemo.userActivity.UserProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterMemoShare extends RecyclerView.Adapter<AdapterMemoShare.CustomViewHolder> {

    private static String TAG = "AdapterMemoShare";

    private ArrayList<ModelMemoWrite> arrayList;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private WriteDateList writeDateList;

    public AdapterMemoShare(ArrayList<ModelMemoWrite> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_memo_share, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();
        holder.mainText.setText(Html.fromHtml(arrayList.get(pos).getMainText()));
        holder.title.setText(arrayList.get(pos).getTitle());
        holder.profile.setClipToOutline(true);
        Glide.with(context).load(arrayList.get(pos).getProfile()).into(holder.profile);
        holder.email.setText(arrayList.get(pos).getEmail());
        holder.date.setText(" "+arrayList.get(pos).getDate());
        holder.name.setText(" "+arrayList.get(pos).getName());

        if (arrayList.get(pos).getBackgroundColor() != null){
            holder.bg.setBackgroundColor(Color.parseColor(arrayList.get(pos).getBackgroundColor()));
        } else {

        }

        if (arrayList.get(pos).getTitleColor() != null ){
            holder.date.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.title.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.mainText.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.email.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.name.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.sDate.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.sNick.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
            holder.sEmail.setTextColor(Color.parseColor(arrayList.get(pos).getTitleColor()));
        }

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView title, mainText;
        private LinearLayout mainTable, bg;
        private TextView email, date, name;
        private String profiles;
        private String connectedName;

        private TextView sEmail, sNick, sDate;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainText = itemView.findViewById(R.id.tv_mainText);
            this.title = itemView.findViewById(R.id.tv_title);
            this.profile = itemView.findViewById(R.id.iv_profile);
            this.mainTable = itemView.findViewById(R.id.ll_main_table);
            this.email = itemView.findViewById(R.id.tv_email);
            this.date = itemView.findViewById(R.id.tv_date);
            this.name = itemView.findViewById(R.id.tv_name);
            this.bg = itemView.findViewById(R.id.ll_bg_share);

            this.sEmail = itemView.findViewById(R.id.tv_set_email);
            this.sNick = itemView.findViewById(R.id.tv_set_nick);
            this.sDate = itemView.findViewById(R.id.tv_set_date);
            Log.v(TAG, TAG);

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String getImagesUrl = arrayList.get(pos).getProfile();
                    Intent intent = new Intent(context, UserProfileView.class);
                    intent.putExtra("getImagesUrl", getImagesUrl);
                    context.startActivity(intent);
                }
            });

            mainTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String mainText = arrayList.get(pos).getMainText();
                    String title = arrayList.get(pos).getTitle();
                    String writeDate = arrayList.get(pos).getRecyclerDate();
                    String token = arrayList.get(pos).getToken();
                    String email = arrayList.get(pos).getEmail();
                    boolean publicKey = arrayList.get(pos).isPublicKey();
                    String titleFontColor = arrayList.get(pos).getTitleColor();

                    String pushKey = databaseReference.push().getKey();
                    String images0 = arrayList.get(pos).getImages0();
                    String images1 = arrayList.get(pos).getImages1();
                    String images2 = arrayList.get(pos).getImages2();
                    String images3 = arrayList.get(pos).getImages3();
                    String images4 = arrayList.get(pos).getImages4();
                    String images5 = arrayList.get(pos).getImages5();
                    String images6 = arrayList.get(pos).getImages6();

                    Intent intent = new Intent(context, MemoDBRead.class);
                    intent.putExtra("mainText", mainText);
                    intent.putExtra("title", title);
//                    intent.putExtra("mainColor", mainColor);
//                    intent.putExtra("titleColor", titleColor);
                    intent.putExtra("writeDate", writeDate);
                    intent.putExtra("token", token);
                    intent.putExtra("email", email);
                    intent.putExtra("publicKey", publicKey);
                    intent.putExtra("pushKey", pushKey);
                    intent.putExtra("connectedTokenKey", firebaseUser.getUid());
                    intent.putExtra("titleFontColor", titleFontColor);

                    intent.putExtra("images0", images0);
                    intent.putExtra("images1", images1);
                    intent.putExtra("images2", images2);
                    intent.putExtra("images3", images3);
                    intent.putExtra("images4", images4);
                    intent.putExtra("images5", images5);
                    intent.putExtra("images6", images6);

                    if (firebaseUser.getUid() != token) {
                        databaseReference.child("registeredUser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String profileImages = snapshot.child("profile").getValue(String.class); // 본인 프로필을 받아오자.
                                String profileEmail = snapshot.child("email").getValue(String.class);
                                databaseReference.child("memoList").child(token).child(writeDate).child("connectUser").child(firebaseUser.getUid()).child("profile").setValue(profileImages);
                                databaseReference.child("memoList").child(token).child(writeDate).child("connectUser").child(firebaseUser.getUid()).child("email").setValue(profileEmail);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {

                    }

                    context.startActivity(intent);
                }
            });


        }
    }
}

