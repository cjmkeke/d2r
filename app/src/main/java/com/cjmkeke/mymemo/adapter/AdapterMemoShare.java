package com.cjmkeke.mymemo.adapter;


import android.content.Context;
import android.content.Intent;
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
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.cjmkeke.mymemo.userActivity.UserProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterMemoShare extends RecyclerView.Adapter<AdapterMemoShare.CustomViewHolder> {

    private static String TAG = "AdapterMemoList";

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo_share, parent, false);
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
//        holder.title.setTextColor(arrayList.get(pos).getColorTitle());
//        holder.mainText.setTextColor(arrayList.get(pos).getColorMainText());
        holder.email.setText(arrayList.get(pos).getEmail());
        holder.date.setText(arrayList.get(pos).getDate());
        holder.name.setText(arrayList.get(pos).getName());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView title, mainText;
        private LinearLayout mainTable;
        private TextView email, date, name;
        private String profiles;
        private String connectedName;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainText = itemView.findViewById(R.id.tv_mainText);
            this.title = itemView.findViewById(R.id.tv_title);
            this.profile = itemView.findViewById(R.id.iv_profile);
            this.mainTable = itemView.findViewById(R.id.ll_main_table);
            this.email = itemView.findViewById(R.id.tv_email);
            this.date = itemView.findViewById(R.id.tv_date);
            this.name = itemView.findViewById(R.id.tv_name);

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
//                    int mainColor = arrayList.get(pos).getColorMainText();
//                    int titleColor = arrayList.get(pos).getColorTitle();
                    String writeDate =arrayList.get(pos).getRecyclerDate();
                    String token = arrayList.get(pos).getToken();
                    String email = arrayList.get(pos).getEmail();
                    boolean publicKey = arrayList.get(pos).isPublicKey();

                    String pushKey = databaseReference.push().getKey();

//                  PostViewUser
//                  글을 보고 있는 유저의 데이터를 만들어 내는 작업
//                    databaseReference.child("registeredUser").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            profiles = snapshot.child(firebaseUser.getUid()).child("profile").getValue(String.class);
//                            connectedName = snapshot.child(firebaseUser.getUid()).child("name").getValue(String.class);
//                            databaseReference.child("memoList").child(token).child(writeDate).child("connectUserList").child(pushKey).child("token").setValue(firebaseUser.getUid());
//                            databaseReference.child("memoList").child(token).child(writeDate).child("connectUserList").child(pushKey).child("profile").setValue(profiles);
//                            databaseReference.child("memoList").child(token).child(writeDate).child("connectUserList").child(pushKey).child("name").setValue(connectedName);
//                            databaseReference.child("memoList").child(token).child(writeDate).child("connectUserList").child(pushKey).child("pushKey").setValue(pushKey);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

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
//                    intent.putExtra("connectedName", connectedName);

                    intent.putExtra("images0", images0);
                    intent.putExtra("images1", images1);
                    intent.putExtra("images2", images2);
                    intent.putExtra("images3", images3);
                    intent.putExtra("images4", images4);
                    intent.putExtra("images5", images5);
                    intent.putExtra("images6", images6);

                    context.startActivity(intent);
                }
            });


        }
    }
}

