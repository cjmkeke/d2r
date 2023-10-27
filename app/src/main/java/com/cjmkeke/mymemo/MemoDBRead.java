package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.adapter.AdapterConnectedUser;
import com.cjmkeke.mymemo.adapter.AdapterReadImagesView;
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.modelClass.ModelImages;
import com.cjmkeke.mymemo.modelClass.ModelMemoWrite;
import com.cjmkeke.mymemo.modelClass.ModelMyFriends;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class MemoDBRead extends AppCompatActivity {

    private static final String TAG = "MemoRead";

    private View orange, pink, purple, boldPurple, black, blue, boldGreen, green, blackBlue, yellow, red, blackPink;
    private EditText title, mainText;
    private TextView commit, template, history, memoPrivate;
    private TextView mediaImages;
    private TextView delete, editLog, editLogDelete, galleryUpload;

    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    private LinearLayout table1, table2, table3, table4, table5, table6;
    private Intent intent;
    private View center;
    private ScrollView svImagesView, svHistory;

    private RecyclerView recyclerView;
    private AdapterConnectedUser adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelMyFriends> arrayList;

    // 이미지 업로드 하는 리사이클러뷰
    private RecyclerView recyclerViewImagesView;
    private AdapterReadImagesView adapterImagesView;
    private RecyclerView.LayoutManager layoutManagerImagesView;
    private ArrayList<ModelImages> arrayListImagesView;
    private List<Uri> selectedImageUris;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private static String writeToken;
    private static String writeDate;
    private static String connectedTokenKey;
    private static String connectedName;
    private String email;
    private static String pushKey;
    private boolean publicKey;
    private static String images0;
    private static String images1;
    private static String images2;
    private static String images3;
    private static String images4;
    private static String images5;
    private static String images6;

    private static final int MEDIA_IMAGES_MULTI = 100;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 101;
    private static final int maxNumPhotosAndVideos = 9;
    private static Dialog dialog;
    private FirebaseStorage storage;

    private TextView fontBlackColor, insertYouTube, checkBox;
    private RichEditor richEditor;
    private String saveMainText;
    private TextView fontSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_db_read);

        recyclerView = findViewById(R.id.connectedUserRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        layoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new AdapterConnectedUser(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 이미지 업로드 하는 리사이클러뷰
        recyclerViewImagesView = findViewById(R.id.recyclerView_ReadMemo_Images);
        recyclerViewImagesView.setHasFixedSize(true);
        layoutManagerImagesView = new LinearLayoutManager(this);
        layoutManagerImagesView.setItemPrefetchEnabled(false);
        recyclerViewImagesView.setLayoutManager(layoutManagerImagesView);
        arrayListImagesView = new ArrayList<>();
        adapterImagesView = new AdapterReadImagesView(arrayListImagesView, this);
        recyclerViewImagesView.setAdapter(adapterImagesView);
        selectedImageUris = new ArrayList<>();

        richEditor = findViewById(R.id.editor_read);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        // TODO 색상 FIND
        blackPink = findViewById(R.id.color_black_pink);
        red = findViewById(R.id.color_red);
        boldPurple = findViewById(R.id.color_bold_purple);
        blue = findViewById(R.id.color_blue);
        purple = findViewById(R.id.color_purple);
        black = findViewById(R.id.color_black);
        orange = findViewById(R.id.color_orange);
        pink = findViewById(R.id.color_pink);
        boldGreen = findViewById(R.id.color_bold_green);
        green = findViewById(R.id.color_green);
        blackBlue = findViewById(R.id.color_black_blue);
        yellow = findViewById(R.id.color_yellow);
        template = findViewById(R.id.tv_template);

        title = findViewById(R.id.et_title_result);
//        mainText = findViewById(R.id.et_mainText_result);
        commit = findViewById(R.id.tv_commit);
        delete = findViewById(R.id.tv_delete);
        editLog = findViewById(R.id.tv_edit_log);
        history = findViewById(R.id.tv_history);
        svHistory = findViewById(R.id.sv_history);
        editLogDelete = findViewById(R.id.tv_history_delete);
        memoPrivate = findViewById(R.id.tv_private);
        galleryUpload = findViewById(R.id.tv_photo_upload);

        fontSize = findViewById(R.id.fontSize);
//        svImagesView = findViewById(R.id.sv_read_images);
        center = findViewById(R.id.view_center);

        imageView1 = findViewById(R.id.iv_images_01);
        imageView2 = findViewById(R.id.iv_images_02);
        imageView3 = findViewById(R.id.iv_images_03);
        imageView4 = findViewById(R.id.iv_images_04);
        imageView5 = findViewById(R.id.iv_images_05);
        imageView6 = findViewById(R.id.iv_images_06);

        table1 = findViewById(R.id.ll_images_table_1);
        table2 = findViewById(R.id.ll_images_table_2);
        table3 = findViewById(R.id.ll_images_table_3);
        table4 = findViewById(R.id.ll_images_table_4);
        table5 = findViewById(R.id.ll_images_table_5);
        table6 = findViewById(R.id.ll_images_table_6);

        intent = getIntent();
        String resultMainText = intent.getStringExtra("mainText");
        String resultTitle = intent.getStringExtra("title");
//        int resultMainTextColor = intent.getIntExtra("mainColor", 0);
//        int resultTitleColor = intent.getIntExtra("titleColor", 0);
        writeDate = intent.getStringExtra("writeDate");
        writeToken = intent.getStringExtra("token");
        email = intent.getStringExtra("email");
        pushKey = intent.getStringExtra("pushKey");
        connectedTokenKey = intent.getStringExtra("connectedTokenKey");

        images0 = intent.getStringExtra("images0");
        images1 = intent.getStringExtra("images1");
        images2 = intent.getStringExtra("images2");
        images3 = intent.getStringExtra("images3");
        images4 = intent.getStringExtra("images4");
        images5 = intent.getStringExtra("images5");
        images6 = intent.getStringExtra("images6");
        publicKey = intent.getBooleanExtra("publicKey", false);

        title.setText(resultTitle);
        richEditor.setHtml(resultMainText);


        //TODO 게시판에 이미지 보이게
        recyclerViewImagesView.setVisibility(View.GONE);
        databaseReference.child("memoList").child(writeToken).child(writeDate).child("boardImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListImagesView.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.getValue(String.class); // 이미지 URL을 String으로 읽어옴
                    ModelImages modelImages = new ModelImages();
                    modelImages.setImages0(imageUrl); // 이미지 URL을 ModelImages 객체에 설정
                    arrayListImagesView.add(modelImages);
                }

                if (!arrayListImagesView.isEmpty()) {
                    recyclerViewImagesView.setVisibility(View.VISIBLE);
                }

                adapterImagesView = new AdapterReadImagesView(arrayListImagesView, MemoDBRead.this);
                recyclerViewImagesView.setAdapter(adapterImagesView);
                adapterImagesView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // TODO 게시판 이미지 업로드
        galleryUpload.setVisibility(View.GONE);
        if (firebaseUser.getUid().equals(writeToken)){
            galleryUpload.setVisibility(View.VISIBLE);
        } else {
            galleryUpload.setVisibility(View.GONE);
        }

        galleryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxNumPhotosAndVideos);
                    startActivityForResult(intent, MEDIA_IMAGES_MULTI);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 다중 선택 가능하도록 설정
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "사진 9장만 가능합니다"), MEDIA_IMAGES_MULTI);
                }
            }
        });

        memoPrivate.setVisibility(View.GONE);
        databaseReference.child("registeredUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tokenMe = snapshot.child(firebaseUser.getUid()).child("token").getValue(String.class);
                if (tokenMe.equals(writeToken)) {
                    delete.setVisibility(View.VISIBLE);
                    // 글쓴 사람은 히스토리 내역을 삭제하는 메소드 보이게 하고 싶으면 VISIBLE
                    editLogDelete.setVisibility(View.GONE);
                    memoPrivate.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                    editLogDelete.setVisibility(View.GONE);
                    memoPrivate.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(writeDate);
            }
        });

        if (publicKey == true) {
            memoPrivate.setText(R.string.share_to_private);
            Drawable newDrawable = getResources().getDrawable(R.drawable.icons_private_size);
            memoPrivate.setCompoundDrawablesWithIntrinsicBounds(newDrawable, null, null, null);
            memoPrivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(MemoDBRead.this);
                    dialog.setContentView(R.layout.item_private_danger_messages);
                    dialog.setCancelable(false);
                    TextView messages = dialog.findViewById(R.id.tv_textview);
                    Button btnOk = dialog.findViewById(R.id.btn_01);
                    Button btnCn = dialog.findViewById(R.id.btn_02);
                    messages.setText(R.string.share_private_messages);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("publicKey").setValue(false);
                            dialog.dismiss();
                        }
                    });

                    btnCn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        } else {
            memoPrivate.setText(R.string.share_to_public);
            Drawable newDrawable = getResources().getDrawable(R.drawable.icons_public_size);
            memoPrivate.setCompoundDrawablesWithIntrinsicBounds(newDrawable, null, null, null);
            memoPrivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(MemoDBRead.this);
                    dialog.setContentView(R.layout.item_private_danger_messages);
                    dialog.setCancelable(false);
                    TextView messages = dialog.findViewById(R.id.tv_textview);
                    Button btnOk = dialog.findViewById(R.id.btn_01);
                    Button btnCn = dialog.findViewById(R.id.btn_02);
                    messages.setText(R.string.share_public_messages);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("publicKey").setValue(true);
                            dialog.dismiss();
                        }
                    });

                    btnCn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

        //TODO 히스토리 보이게/보이지 않게
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (svHistory.getVisibility() == View.VISIBLE) {
                    // svHistory가 보이면 숨깁니다.
                    svHistory.setVisibility(View.GONE);
                    history.setText(R.string.history_list);

                } else {
                    // svHistory가 숨겨져 있으면 보이게 합니다.
                    svHistory.setVisibility(View.VISIBLE);
                    history.setText("히스토리 닫기");
                }
            }
        });

        editLogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteHistoryDialog(writeDate);
//                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").removeValue();
            }
        });

        DatabaseReference databaseReference1 = firebaseDatabase.getReference();
        databaseReference1.child("memoList").child(writeToken).child(writeDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("editLog").exists()) {
                    String s = snapshot.child("editLog").getValue(String.class);
                    editLog.setText(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.v("이메일 확인", email);
        String subName = email.substring(0, email.lastIndexOf("@"));
        Log.v("이메일 자름", subName);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (writeToken.equals(firebaseUser.getUid())) {
                    WriteDateList writeDateList = new WriteDateList();
                    databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(saveMainText);
                    databaseReference.child("memoList").child(writeToken).child(writeDate).child("title").setValue(title.getText().toString());

                    DatabaseReference databaseReference1 = firebaseDatabase.getReference();
                    databaseReference1.child("memoList").child(writeToken).child(writeDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("editLog").exists()) {
                                String s = snapshot.child("editLog").getValue(String.class);
                                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").setValue(s + "\n" + writeDateList.getTimeFormatAdapterViewSS() + " " + firebaseUser.getEmail());
                            } else {
                                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").setValue(writeDateList.getTimeFormatAdapterViewSS() + " " + firebaseUser.getEmail());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    finish();
                } else {
                    // 만약 다른 사람의 글을 수정했다면 기록을 남기자
                    WriteDateList writeDateList = new WriteDateList();
                    databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(saveMainText);

                    DatabaseReference databaseReference1 = firebaseDatabase.getReference();
                    databaseReference1.child("memoList").child(writeToken).child(writeDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("editLog").exists()) {
                                String s = snapshot.child("editLog").getValue(String.class);
                                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").setValue(s + "\n" + writeDateList.getTimeFormatAdapterViewSS() + " " + firebaseUser.getEmail());
                            } else {
                                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").setValue(writeDateList.getTimeFormatAdapterViewSS() + " " + firebaseUser.getEmail());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    finish();
                }

                // TODO 이미지 업로드
//                imagesUpload(subName, firebaseUser.getUid());
                if (!selectedImageUris.isEmpty()) {
                    for (int i = 0; i < selectedImageUris.size(); i++) {
                        Uri uri = selectedImageUris.get(i);
                        Log.v("Upload", String.valueOf(uri));

                        final int imagesI = i;
                        FirebaseStorage storage1 = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage1.getReference(subName + "_memo_images").child(writeDate);
                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("boardImages").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("boardImages").removeValue();

                                for (DataSnapshot deleteSnap : snapshot.getChildren()) {
                                    String imagesUrl = deleteSnap.getValue(String.class);
                                    Log.v("이미지 주소 획득", imagesUrl);

                                    Task<Void> storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(imagesUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        StorageReference storageReference1 = storageReference.child("reUpload_Images" + writeDate + imagesI);
                        UploadTask uploadTask = storageReference1.putFile(uri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imagesUrl = uri.toString();
                                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("boardImages").child("images" + imagesI).setValue(imagesUrl);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        });

        //TODO 에디터
        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                saveMainText = text;
            }
        });

        boldPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#673AB7"));
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#2196F3"));
            }
        });

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#001000"));
            }
        });

        boldGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#4CAF50"));
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#8BC34A"));
            }
        });

        blackBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#3F51B5"));
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#FFEB3B"));
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#FF0000"));
            }
        });

        blackPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#8F097E"));
            }
        });


        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#F44336"));
            }
        });

        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#E91E63"));
            }
        });

        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setTextColor(Color.parseColor("#9C27B0"));
            }
        });


        //TODO
        findViewById(R.id.tv_upload_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("","1");
                // 팝업 메뉴 레이아웃을 인플레이트합니다.
                View popupView = getLayoutInflater().inflate(R.layout.item_insert, null);
                // PopupWindow 객체를 생성하고 레이아웃을 설정합니다.
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // 팝업 메뉴가 나타날 때 화면에 대한 설정을 추가합니다.
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정
                popupWindow.showAsDropDown(view);

                TextView textView = popupView.findViewById(R.id.tv_send);
                EditText editText = popupView.findViewById(R.id.et_insert);
                editText.setHint("이미지 주소를 입력해주세요.");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str = editText.getText().toString();
                        richEditor.insertImage(str, "", 350, 280);
                    }
                });
            }
        });

        findViewById(R.id.insertYouTube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 팝업 메뉴 레이아웃을 인플레이트합니다.
                View popupView = getLayoutInflater().inflate(R.layout.item_insert, null);
                // PopupWindow 객체를 생성하고 레이아웃을 설정합니다.
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // 팝업 메뉴가 나타날 때 화면에 대한 설정을 추가합니다.
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정
                popupWindow.showAsDropDown(view);

                TextView button = popupView.findViewById(R.id.tv_send);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String subUrl = "&pp=";
                        String subUrlShare = "?si=";

                        String urlWatchPc = "https://www.youtube.com/watch?v=";
                        String urlWatchMobile = "https://m.youtube.com/watch?v=";
                        String urlShorts = "https://www.youtube.com/shorts/";
                        String urlShare = "https://youtu.be/";
                        String urlEmbed = "https://www.youtube.com/embed/";
                        EditText insert = popupView.findViewById(R.id.et_insert);
                        String str = insert.getText().toString();

                        int width = 400;
                        int height = 380;

                        if (str.contains(urlWatchPc)) {
                            String converterUrl = str.replace(urlWatchPc, urlEmbed);
                            richEditor.insertYoutubeVideo(converterUrl, width,height);
                            popupWindow.dismiss();
                            Log.v("urlWatchPc", converterUrl);
                        } else if (str.contains(urlWatchMobile)) {
                            String converterUrl = str.replace(urlWatchMobile, urlEmbed);
                            String finalConverter = converterUrl.substring(0, converterUrl.indexOf(subUrl));
                            richEditor.insertYoutubeVideo(finalConverter, width, height);
                            popupWindow.dismiss();
                            Log.v("urlWatchMobile", finalConverter);
                        }else if (str.contains(urlShare)){
                            String converterUrl = str.replace(urlShare, urlEmbed);
                            String finalConverter = converterUrl.substring(0, converterUrl.indexOf(subUrlShare));
                            richEditor.insertYoutubeVideo(finalConverter, width, height);
                            popupWindow.dismiss();
                            Log.v("urlShorts", finalConverter);
                        } else if (str.contains(urlShorts)){
                            String converterUrl = str.replace(urlShorts, urlEmbed);
                            richEditor.insertYoutubeVideo(converterUrl, width,height);
                            popupWindow.dismiss();
                            Log.v("urlShorts", converterUrl);

                        } else if (!str.contains("https")){
                            Toast.makeText(MemoDBRead.this, "주소에 https://www 포함이 된 모든 주소여야 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        findViewById(R.id.tv_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richEditor.setStrikeThrough();
            }
        });

        fontBlackColor = findViewById(R.id.fontBackColor);
        fontBlackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 팝업 메뉴 레이아웃을 인플레이트합니다.
                View popupView = getLayoutInflater().inflate(R.layout.item_bg_color, null);

                // PopupWindow 객체를 생성하고 레이아웃을 설정합니다.
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // 팝업 메뉴가 나타날 때 화면에 대한 설정을 추가합니다.
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정

//                popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 160, 950);
                popupWindow.showAsDropDown(view);

                popupView.findViewById(R.id.bg_color_orange).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#F44336"));
                        popupWindow.dismiss();
                    }
                });

                popupView.findViewById(R.id.bg_color_pink).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#E91E63"));
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.bg_color_purple).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#9C27B0"));
                        popupWindow.dismiss();
                    }
                });

                popupView.findViewById(R.id.bg_color_bold_purple).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#673AB7"));
                        popupWindow.dismiss();
                    }
                });


                popupView.findViewById(R.id.bg_color_blue).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#2196F3"));
                        popupWindow.dismiss();
                    }
                });
                popupView.findViewById(R.id.bg_color_bold_green).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#4CAF50"));
                        popupWindow.dismiss();
                    }
                });

            }
        });

        fontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 메뉴 레이아웃을 인플레이트합니다.
                View popupView = getLayoutInflater().inflate(R.layout.item_font_size, null);

                // PopupWindow 객체를 생성하고 레이아웃을 설정합니다.
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // 팝업 메뉴가 나타날 때 화면에 대한 설정을 추가합니다.
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정

//                popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 160, 950);
                popupWindow.showAsDropDown(view);

                // 팝업 메뉴의 아이템 클릭 리스너를 설정합니다.
                popupView.findViewById(R.id.font_size_7).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(1);
                        fontSize.setText("7");
                        popupWindow.dismiss();
                    }
                });

                popupView.findViewById(R.id.font_size_8).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(2);
                        fontSize.setText("8");

                        popupWindow.dismiss();

                    }
                });

                popupView.findViewById(R.id.font_size_9).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(3);
                        fontSize.setText("9");

                        popupWindow.dismiss();

                    }
                });

                popupView.findViewById(R.id.font_size_10).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(4);
                        fontSize.setText("10");

                        popupWindow.dismiss();

                    }
                });

                popupView.findViewById(R.id.font_size_11).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(5);
                        fontSize.setText("11");

                        popupWindow.dismiss();

                    }
                });
                popupView.findViewById(R.id.font_size_12).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(6);
                        fontSize.setText("12");

                        popupWindow.dismiss();

                    }
                });

                popupView.findViewById(R.id.font_size_13).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        richEditor.setFontSize(7);
                        fontSize.setText("13");

                        popupWindow.dismiss();

                    }
                });


            }
        });

    }

    private void showDeleteDialog(String writeDate) {
        // 다이얼로그를 표시하기 전에 이전 다이얼로그를 닫습니다.
        dismissDialog();

        dialog = new Dialog(MemoDBRead.this);
        dialog.setContentView(R.layout.item_dialog_messages_danger);

        // 다이얼로그 배경 설정
        Window window = dialog.getWindow();
        if (window != null) {
//            window.setBackgroundDrawableResource(R.drawable.activity_corner); // 다이얼로그 라운드 둥글게
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.verticalMargin = 0.03f;
            window.setAttributes(params);
        }

        TextView textView = dialog.findViewById(R.id.tv_messages);
        textView.setText(R.string.write_remove_messages);

        // 다이얼로그 내의 버튼 처리 => 확인
        Button button1 = dialog.findViewById(R.id.btn_01);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("memoList").child(writeToken).child(writeDate).child("boardImages").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot deleteSnp : snapshot.getChildren()) {
                            String imagesUrl = deleteSnp.getValue(String.class);
                            Log.v("삭제할 이미지 주소 받아오기", imagesUrl);

                            Task<Void> storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imagesUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {}
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
//                deleteImagesFile();
                databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).removeValue();
                finish();
            }
        });

        // 다이얼로그 내의 버튼 처리 => 취소
        Button button2 = dialog.findViewById(R.id.btn_02);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(); // 다이얼로그 닫기
            }
        });

        // 다이얼로그 표시
        dialog.show();
    }

    private void showDeleteHistoryDialog(String writeDate) {
        // 다이얼로그를 표시하기 전에 이전 다이얼로그를 닫습니다.
        dismissDialog();

        dialog = new Dialog(MemoDBRead.this);
        dialog.setContentView(R.layout.item_dialog_messages_danger);

        // 다이얼로그 배경 설정
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.activity_corner);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.verticalMargin = 0.03f;
            window.setAttributes(params);
        }

        TextView textView = dialog.findViewById(R.id.tv_messages);
        textView.setText(R.string.history_remove_messages);

        // 다이얼로그 내의 버튼 처리 => 확인
        Button button1 = dialog.findViewById(R.id.btn_01);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").removeValue();
                finish();
            }
        });

        // 다이얼로그 내의 버튼 처리 => 취소
        Button button2 = dialog.findViewById(R.id.btn_02);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(); // 다이얼로그 닫기
            }
        });

        // 다이얼로그 표시
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remove();
        removeSpecificConnectUser(pushKey);
        dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remove();
        removeSpecificConnectUser(pushKey);
    }

    private void removeSpecificConnectUser(String userKeyToRemove) {
        DatabaseReference connectUserRef = databaseReference
                .child("memoList")
                .child(writeToken)
                .child(writeDate)
                .child("connectUserList");

        // 특정 유저의 데이터를 삭제합니다.
        connectUserRef.child(userKeyToRemove).removeValue();
    }

    private void remove() {
        DatabaseReference connectUserListRef = databaseReference.child("memoList")
                .child(writeToken)
                .child(writeDate)
                .child("connectUserList");

        connectUserListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // 각 사용자의 토큰을 가져옵니다.
                        String userToken = userSnapshot.child("token").getValue(String.class);

                        if (userToken != null && userToken.equals(firebaseUser.getUid())) {
                            // 특정 조건에 해당하는 사용자의 데이터를 삭제합니다.
                            String pushKey = userToken;
                            removeSpecificConnectUser(pushKey);
                        }
                    }
                } else {
                    // "connectUserList" 노드가 비어있거나 존재하지 않을 때 처리할 내용을 여기에 작성합니다.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 작업이 취소되거나 오류가 발생한 경우 처리합니다.
            }
        });
    }

    // ...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여된 경우 실행할 코드
                } else {
                    // 권한이 거부된 경우 사용자에게 설명하거나 다른 조치를 취할 수 있습니다.
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA_IMAGES_MULTI && resultCode == RESULT_OK && data != null) {

            if (data.getClipData() != null) {
                int count = Math.min(data.getClipData().getItemCount(), maxNumPhotosAndVideos); // 최대 6개 이미지 선택
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }

            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
            }
//        }
        }
    }

    private void deleteImagesFile() {
        if (images0 != null && !images0.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images0);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });

        }

        if (images1 != null && !images1.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images1);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }

        if (images2 != null && !images2.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images2);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }

        if (images3 != null && !images3.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images3);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }

        if (images4 != null && !images4.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images4);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }

        if (images5 != null && !images5.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images5);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }

        if (images6 != null && !images6.equals(null)) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(images6);
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });
        }
    }

    private void imagesUpload(String subName, String userUid) {

        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri uri = selectedImageUris.get(i);
            Log.v("Upload", String.valueOf(uri));

            final int imagesI = i;
            FirebaseStorage storage1 = FirebaseStorage.getInstance();
            StorageReference storageReference = storage1.getReference(subName + "_memo_images").child(writeDate);
            databaseReference.child("memoList").child(userUid).child(writeDate).child("boardImages").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    databaseReference.child("memoList").child(userUid).child(writeDate).child("boardImages").removeValue();

                    for (DataSnapshot deleteSnap : snapshot.getChildren()) {
                        String imagesUrl = deleteSnap.getValue(String.class);
                        Log.v("이미지 주소 획득", imagesUrl);

                        Task<Void> storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(imagesUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            StorageReference storageReference1 = storageReference.child("reUpload_Images" + writeDate + imagesI);
            UploadTask uploadTask = storageReference1.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imagesUrl = uri.toString();
                            databaseReference.child("memoList").child(userUid).child(writeDate).child("boardImages").child("images" + imagesI).setValue(imagesUrl);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}