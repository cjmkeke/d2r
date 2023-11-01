package com.cjmkeke.mymemo.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.cjmkeke.mymemo.R;
import com.cjmkeke.mymemo.MemoDBRead;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterMemo extends RecyclerView.Adapter<AdapterMemo.CustomViewHolder> {

    private static String TAG = "AdapterMemo";

    private ArrayList<ModelMemoWrite> arrayList;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private WriteDateList writeDateList;

    public AdapterMemo(ArrayList<ModelMemoWrite> arrayList, Context context) {
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
        holder.mainText.setText(Html.fromHtml(arrayList.get(pos).getMainText()));
        holder.title.setText(arrayList.get(pos).getTitle());
        holder.profile.setClipToOutline(true);
        Glide.with(context).load(arrayList.get(pos).getProfile()).into(holder.profile);
        holder.email.setText(arrayList.get(pos).getEmail());
        holder.date.setText(arrayList.get(pos).getDate());
        holder.name.setText(arrayList.get(pos).getName());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile, share;
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
            Log.v(TAG, TAG);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

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

