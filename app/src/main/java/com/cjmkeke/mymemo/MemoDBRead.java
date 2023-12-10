package com.cjmkeke.mymemo;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.adapter.AdapterConnectedUser;
import com.cjmkeke.mymemo.library.HolidayDecorator;
import com.cjmkeke.mymemo.library.OneDayDecorator;
import com.cjmkeke.mymemo.library.SaturdayDecorator;
import com.cjmkeke.mymemo.library.SunDayDecorator;
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.modelClass.ModelConnectUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyencse.URLEmbeddedData;
import com.nguyencse.URLEmbeddedView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;

public class MemoDBRead extends AppCompatActivity {

    private static final String TAG = "MemoRead";

    private EditText title, mainText;
    private TextView template, history, memoPrivate;
    private TextView mediaImages, fontBackColor, fontColor;
    private TextView delete, editLog, editLogDelete;
    private Intent intent;
    private View center;
    private ScrollView svImagesView, svHistory;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private static TextView commit;
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

    private TextView fontBlackColor, insertYouTube, checkBox, backgroundColor;
    private RichEditor richEditor;
    private String saveMainText;
    private TextView fontSize, fontBold, underLine;
    private LinearLayout showImagesRoom;

    private TextView uploadImages;
    private static final int MEDIA_IMAGES = 105;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 106;
    private List<Uri> imagesUriArray = new ArrayList<>();
    private WriteDateList writeDateList;
    private String downloadImagesUri;
    private List<String> imagesList = new ArrayList<>();
    private List<String> containsUrlList = new ArrayList<>();
    private List<String> snapshotImagesList = new ArrayList<>();
    private List<String> longTypeImagesUrl = new ArrayList<>();

    private String stroageShotUrl;
    private String firebaseShotUrl;

    private WindowManager windowManager;
    private View floatingView;

    // 프로필 삭제
    private static String profiles0;
    private static String profiles1;

    private RecyclerView recyclerView;
    private AdapterConnectedUser adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelConnectUser> arrayList;

    private TextView visibilityConnect;
    boolean isConnect = true;

    private String backgroundColorValue;
    private TextView codeView; // 아직 안씀
    private LinearLayout richEditorPaddingColor;
    private PopupWindow popupWindow;

    private String resultMainText;
    private String resultTitle;

    private boolean codeViewSwitch = false;
    private final Handler handler = new Handler();
    private static final int DELAY_MILLIS = 1; // 적절한 딜레이 시간 설정
    private boolean isRichEditorTextChanging = false;
    private boolean isCodeEditorTextChanging = false;
    private TextView strike;

    private boolean calendarViewSwitch = false;
    private TextView calendarView;
    private MaterialCalendarView materialCalendarView;
    private CalendarDay firstSelectedDate; // 처음 선택한 날짜
    private CalendarDay lastSelectedDate;  // 마지막으로 선택한 날짜
    private String databaseValueDate;
    private boolean isCalendarSwitch;
    private String memoValue;
    private static String userName;

    private TextView settings;
    private String titleColor;
    private static String intentFontColor;

    private TextView insertUrl;
    private String urlTitle;
    private String urlImages;
    private String urlText;
    private String urlToUrl;
    private String htmlString;

    //TODO 변수 모음
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_db_read);
        this.getOnBackPressedDispatcher().addCallback(this, callback);
        showImagesRoom = findViewById(R.id.ll_images_empty);
        richEditor = findViewById(R.id.editor_read);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        fontColor = findViewById(R.id.font_color);
        fontBackColor = findViewById(R.id.fontBackColor);
        backgroundColor = findViewById(R.id.editor_color);
        template = findViewById(R.id.tv_template);
        title = findViewById(R.id.et_title_result);
        commit = findViewById(R.id.tv_commit);
        delete = findViewById(R.id.tv_delete);
        editLog = findViewById(R.id.tv_edit_log);
        history = findViewById(R.id.tv_history);
        svHistory = findViewById(R.id.sv_history);
        editLogDelete = findViewById(R.id.tv_history_delete);
        memoPrivate = findViewById(R.id.tv_private);
        fontSize = findViewById(R.id.fontSize);
        fontBold = findViewById(R.id.tv_bold);
        underLine = findViewById(R.id.tv_underline);
        center = findViewById(R.id.view_center);
        uploadImages = findViewById(R.id.tv_upload_images);
        visibilityConnect = findViewById(R.id.tv_connect_user_visibility);
        codeView = findViewById(R.id.code);
        richEditorPaddingColor = findViewById(R.id.ll_editor_padding);
        settings = findViewById(R.id.tv_settings_read);
        insertUrl = findViewById(R.id.insertUrl);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String tipValue = sharedPreferences.getString("startTip", "");
        Log.v("tipValue", tipValue);

        if (!"OK".equals(tipValue)) {
            Dialog dialog1 = new Dialog(this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.item_write_danger_messages);

            Window window = dialog1.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            ImageView imageView = dialog1.findViewById(R.id.iv_img_src);
            imageView.setImageResource(R.drawable.img_read_warring);

//            TextView textView = dialog1.findViewById(R.id.textView);
//            textView.setText("읽기 모드를 해제하려면 터치를 5초간 유지하세요.\n다시 읽기 모드로 변경하려면 터치를 5초간 유지하면 읽기 모드로 됩니다.");

            ImageView close = dialog1.findViewById(R.id.iv_close);

//            TextView button = dialog1.findViewById(R.id.btn_01);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("startTip", "OK");
                    editor.apply();
                    dialog1.dismiss();
                }
            });
            dialog1.show();
        }

        intent = getIntent();
        if (intent != null) {
            resultMainText = intent.getStringExtra("mainText");
            resultTitle = intent.getStringExtra("title");
            writeDate = intent.getStringExtra("writeDate");
            writeToken = intent.getStringExtra("token");
            email = intent.getStringExtra("email");
            pushKey = intent.getStringExtra("pushKey");
            connectedTokenKey = intent.getStringExtra("connectedTokenKey");
            publicKey = intent.getBooleanExtra("publicKey", false);
            intentFontColor = intent.getStringExtra("titleFontColor");
            if (intentFontColor != null) {
                title.setTextColor(Color.parseColor(intentFontColor));
            }
        }

        title.setText(resultTitle);
        richEditor.setHtml(resultMainText);
        writeDateList = new WriteDateList();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = getLayoutInflater().inflate(R.layout.item_background_title, null);
                TextView textView = view1.findViewById(R.id.messages);
                textView.setText("타이틀 글씨 색상을 선택하세요");
                new ColorPickerDialog.Builder(MemoDBRead.this)
                        .setCustomTitle(view1)
                        .setPositiveButton("확인", new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                titleColor = "#" + envelope.getHexCode();
                                title.setTextColor(Color.parseColor(titleColor));
                                Log.v("", titleColor);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .attachAlphaSlideBar(false)
                        .attachBrightnessSlideBar(true)
                        .show();
            }
        });

        databaseReference.child("registeredUser").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("name").getValue(String.class);
                Log.v("", userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("memoList").child(writeToken).child(writeDate).child("backgroundColor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String color = snapshot.getValue(String.class);
                    Log.v("222222", color);
                    richEditor.setBackgroundColor(Color.parseColor(color));
                    richEditorPaddingColor.setBackgroundColor(Color.parseColor(color));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showImagesRoom.setVisibility(View.GONE);
        databaseReference.child("registeredUser").child(firebaseUser.getUid()).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                profiles1 = snapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            // 플로팅 뷰 생성
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            // params 설정 (예: 시작 위치)
            params.gravity = Gravity.RIGHT | Gravity.TOP;
            params.x = 80;
            params.y = 200;

            // 플로팅 뷰 레이아웃 인플레이션 및 설정
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            floatingView = inflater.inflate(R.layout.floating_layout, null);

            // 플로팅 뷰를 윈도우 매니저에 추가
            windowManager.addView(floatingView, params);
            TextView userCount = floatingView.findViewById(R.id.tv_connect_user_count);
            recyclerView = floatingView.findViewById(R.id.recyclerView_user_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            layoutManager.setItemPrefetchEnabled(false);
            recyclerView.setLayoutManager(layoutManager);
            arrayList = new ArrayList<>();
            adapter = new AdapterConnectedUser(arrayList, this);
            recyclerView.setAdapter(adapter);

            visibilityConnect.setText("접속유저 보이기");
            floatingView.setVisibility(View.GONE);
            visibilityConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnect) {
                        visibilityConnect.setText("접속유저 숨기기");
                        floatingView.setVisibility(View.VISIBLE);
                        isConnect = false;
                    } else {
                        visibilityConnect.setText("접속유저 보이기");
                        floatingView.setVisibility(View.GONE);
                        isConnect = true;
                    }
                }
            });

            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = firebaseDatabase1.getReference().child("memoList").child(writeToken).child(writeDate).child("connectUser");
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        ModelConnectUser user = snap.getValue(ModelConnectUser.class);
                        arrayList.add(user);

                    }
                    adapter = new AdapterConnectedUser(arrayList, MemoDBRead.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    long count2 = snapshot.getChildrenCount();
                    Log.v("count", String.valueOf(count2));
                    userCount.setText("접속유저" + "  ( " + count2 + " )");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference databaseReference2 = firebaseDatabase1.getReference().child("memoList").child(writeToken).child(writeDate).child("connectUser");
            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        memoPrivate.setVisibility(View.GONE);
        databaseReference.child("registeredUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tokenMe = snapshot.child(firebaseUser.getUid()).child("token").getValue(String.class);
                if (tokenMe.equals(writeToken)) {
                    delete.setVisibility(View.VISIBLE);
                    // 글쓴 사람은 히스토리 내역을 삭제하는 메소드 보이게 하고 싶으면 VISIBLE
                    editLogDelete.setVisibility(View.VISIBLE);
//                    memoPrivate.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                    editLogDelete.setVisibility(View.GONE);
//                    memoPrivate.setVisibility(View.GONE);
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

        memoPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MemoDBRead.this, FriendsPopup.class);
                intent1.putExtra("token", writeToken);
                intent1.putExtra("writeDate ", writeDate);
                startActivity(intent1);

            }
        });


//        기존 비공개 / 공개 코드
//        if (publicKey == true) {
//            memoPrivate.setText(R.string.share_to_private);
//            Drawable newDrawable = getResources().getDrawable(R.drawable.icons_private_size);
//            memoPrivate.setCompoundDrawablesWithIntrinsicBounds(newDrawable, null, null, null);
//            memoPrivate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog = new Dialog(MemoDBRead.this);
//                    dialog.setContentView(R.layout.item_popup_messages);
//                    dialog.setCancelable(false);
//                    TextView messages = dialog.findViewById(R.id.tv_textview);
//                    Button btnOk = dialog.findViewById(R.id.btn_01);
//                    Button btnCn = dialog.findViewById(R.id.btn_02);
//                    messages.setText(R.string.share_private_messages);
//                    btnOk.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("publicKey").setValue(false);
//                            dialog.dismiss();
//                        }
//                    });
//
//                    btnCn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                }
//            });
//        } else {
//            memoPrivate.setText(R.string.share_to_public);
//            Drawable newDrawable = getResources().getDrawable(R.drawable.icons_public_size);
//            memoPrivate.setCompoundDrawablesWithIntrinsicBounds(newDrawable, null, null, null);
//            memoPrivate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog = new Dialog(MemoDBRead.this);
//                    dialog.setContentView(R.layout.item_popup_messages);
//                    dialog.setCancelable(false);
//                    TextView messages = dialog.findViewById(R.id.tv_textview);
//                    Button btnOk = dialog.findViewById(R.id.btn_01);
//                    Button btnCn = dialog.findViewById(R.id.btn_02);
//                    messages.setText(R.string.share_public_messages);
//                    btnOk.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            databaseReference.child("memoList").child(firebaseUser.getUid()).child(writeDate).child("publicKey").setValue(true);
//                            dialog.dismiss();
//                        }
//                    });
//
//                    btnCn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                }
//            });
//        }

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

        String subName = email.substring(0, email.lastIndexOf("@"));
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (writeToken.equals(firebaseUser.getUid())) {
                    Log.v("본인이 저장", "");
                    WriteDateList writeDateList = new WriteDateList();

                    for (int i = 0; i < imagesList.size(); i++) {
                        int finalI = i;
                        String saveImages = imagesList.get(i);
                        DatabaseReference imagesRef = databaseReference.child("memoList").child(writeToken).child(writeDate).child("images");
                        DatabaseReference newImageRef = imagesRef.push();
                        newImageRef.setValue(saveImages);
                    }

                    if (saveMainText != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(saveMainText);
                    } else {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(resultMainText);
                    }
                    if (backgroundColorValue != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("backgroundColor").setValue(backgroundColorValue);
                    }

                    if (titleColor != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("titleColor").setValue(titleColor);
                    } else if (intentFontColor != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("titleColor").setValue(intentFontColor);
                    } else {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("titleColor").setValue("#000000");
                    }

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
//                    Log.v("다른 사람 저장", "");
//                    Log.v("다른 사람 저장", saveMainText);
                    WriteDateList writeDateList = new WriteDateList();
                    for (int i = 0; i < imagesList.size(); i++) {
                        int finalI = i;
                        String saveImages = imagesList.get(i);
                        DatabaseReference imagesRef = databaseReference.child("memoList").child(writeToken).child(writeDate).child("images");
                        DatabaseReference newImageRef = imagesRef.push();
                        newImageRef.setValue(saveImages);
                    }

                    if (saveMainText != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(saveMainText);
                    } else {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("mainText").setValue(resultMainText);
                    }

                    if (backgroundColorValue != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("backgroundColor").setValue(backgroundColorValue);
                    }

                    if (titleColor != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("titleColor").setValue(titleColor);
                    } else if (intentFontColor != null) {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("titleColor").setValue(intentFontColor);
                    } else {
                        databaseReference.child("memoList").child(writeToken).child(writeDate).child("titleColor").setValue("#000000");
                    }

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
                }
            }
        });

        Button buttonOk = findViewById(R.id.btn_calendar_read_view);
        EditText calendarInput = findViewById(R.id.et_calendar_input);
        LinearLayout calendarLinear = findViewById(R.id.ll_calendar_view);
        LinearLayout calendarLinearKeyboard = findViewById(R.id.ll_calendar_view_keyboard);
        LinearLayout calendarLinearMemoView = findViewById(R.id.ll_calendar_view_memo);
        TextView calendarTextView = findViewById(R.id.tv_calendar_memo);
        materialCalendarView = findViewById(R.id.calendar_xml);
        calendarView = findViewById(R.id.calendar);
        calendarInput.setMaxLines(1);
        calendarLinear.setVisibility(View.GONE);
        calendarTextView.setVisibility(View.GONE);
        calendarLinearMemoView.setVisibility(View.GONE);
        calendarLinearKeyboard.setVisibility(View.GONE);
        List<String> selectDateList = new ArrayList<>();
        List<String> databaseDateList = new ArrayList<>();
        OneDayDecorator oneDayDecorator = new OneDayDecorator();
        SaturdayDecorator saturdayDecorator = new SaturdayDecorator();
        SunDayDecorator sunDayDecorator = new SunDayDecorator();
        HolidayDecorator holidayDecorator = new HolidayDecorator();
        HashSet<CalendarDay> selectedDates = new HashSet<>();
        selectedDates.add(CalendarDay.today()); // 현재 날짜를 선택된 날짜로 예시로 추가

        materialCalendarView.addDecorators(oneDayDecorator);
        materialCalendarView.addDecorators(saturdayDecorator);
        materialCalendarView.addDecorators(sunDayDecorator);
        holidayDecorator.fetchHolidayDates();
        materialCalendarView.addDecorators(holidayDecorator);
        materialCalendarView.invalidateDecorators();
        materialCalendarView.setSelectionColor(ContextCompat.getColor(this, R.color.black));

        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat("yyyy년 MM월", Locale.getDefault())));
        // Firebase에서 데이터 가져오기
        databaseReference.child("memoList").child(writeToken).child(writeDate).child("canlendarDay")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot getSnap : snapshot.getChildren()) {
                            String str = getSnap.getValue(String.class);
                            databaseDateList.add(str);
                            Log.v("ref", str);

                            // str을 CalendarDay로 변환
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            try {
                                Date date = sdf.parse(str);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                CalendarDay calendarDay = CalendarDay.from(calendar);

                                // MaterialCalendarView에 선택된 날짜로 표시
                                materialCalendarView.setDateSelected(calendarDay, true);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 에러 처리를 추가할 수 있습니다.
                    }
                });

        databaseReference.child("memoList").child(writeToken).child(writeDate).child("canlendarDayToMemo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder displayText = new StringBuilder(); // 여러 값을 저장할 StringBuilder 생성
                for (DataSnapshot memoRef : snapshot.getChildren()) {
                    String keyValue = memoRef.getKey();
                    String refValue = memoRef.getValue(String.class);
                    displayText.append(keyValue).append("일 [").append(refValue + "]").append("\n");
                }
                calendarTextView.setText(displayText.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (calendarViewSwitch) {
                    calendarLinear.setVisibility(View.GONE);
                    calendarLinearKeyboard.setVisibility(View.GONE);
                    calendarLinearMemoView.setVisibility(View.GONE);
                    calendarTextView.setVisibility(View.GONE);
                    calendarViewSwitch = false;
                } else {
                    calendarLinear.setVisibility(View.VISIBLE);
                    calendarLinearKeyboard.setVisibility(View.VISIBLE);
                    calendarLinearMemoView.setVisibility(View.VISIBLE);
                    calendarTextView.setVisibility(View.VISIBLE);
                    calendarViewSwitch = true;

                    materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                            isCalendarSwitch = selected;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            databaseValueDate = (sdf.format(date.getDate()));
                            selectDateList.add(databaseValueDate);
                            Log.v("add selectDateList", selectDateList.toString());

                            if (!isCalendarSwitch) {
                                databaseReference.child("memoList").child(writeToken).child(writeDate).child("canlendarDay").child(databaseValueDate).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error != null) {
                                            // 에러 처리를 추가할 수 있습니다.
                                            selectDateList.remove(databaseValueDate);
                                            Log.v("1 remove selectDateList", selectDateList.toString());

                                        } else {
                                            // 선택 해제된 날짜를 리스트에서 제거
                                            selectDateList.remove(databaseValueDate);
                                            Log.v("1-1 remove selectDateList", selectDateList.toString());

                                        }
                                    }
                                });

                                databaseReference.child("memoList").child(writeToken).child(writeDate).child("canlendarDayToMemo").child(databaseValueDate).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error != null) {
                                            // 에러 처리를 추가할 수 있습니다.
                                            selectDateList.remove(databaseValueDate);
                                            Log.v("2 remove selectDateList", selectDateList.toString());

                                        } else {
                                            // 선택 해제된 날짜를 리스트에서 제거
                                            selectDateList.remove(databaseValueDate);
                                            Log.v("2-1 remove selectDateList", selectDateList.toString());


                                        }
                                    }
                                });
                            }

                        }
                    });


                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.v("버튼 누름", "");
                            if (isCalendarSwitch != TextUtils.isEmpty(calendarInput.getText().toString())) {
                                for (int i = 0; i < selectDateList.size(); i++) {
                                    Log.v("selectDateList 1", selectDateList.get(i));
                                    memoValue = calendarInput.getText().toString();
                                    databaseReference.child("memoList").child(writeToken).child(writeDate).child("canlendarDay").child(selectDateList.get(i)).setValue(selectDateList.get(i));
                                    databaseReference.child("memoList").child(writeToken).child(writeDate).child("canlendarDayToMemo").child(selectDateList.get(i)).setValue(userName + " : " + memoValue);

                                    if (!selectDateList.isEmpty()) {
                                        Log.v("selectDateList 2", selectDateList.get(i));
                                    }

                                }

                            } else {
                                Toast.makeText(MemoDBRead.this, "날짜를 선택하고 메모를 입력해주세요", Toast.LENGTH_SHORT).show();
                            }
                            selectDateList.clear();

                        }
                    });
                }
            }
        });

        LinearLayout codeViewLinear = findViewById(R.id.ll_code_view);
        EditText codeEditor = findViewById(R.id.et_read_code_view);
        Button buttonSrc = findViewById(R.id.btn_html_src);
        codeViewLinear.setVisibility(View.GONE);
        codeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codeViewSwitch) {
                    codeViewLinear.setVisibility(View.GONE);
                    codeViewSwitch = false;
                } else {
                    codeViewLinear.setVisibility(View.VISIBLE);
                    codeEditor.setText(richEditor.getHtml().toString());
                    codeViewSwitch = true;

                    buttonSrc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dialog dialog1 = new Dialog(MemoDBRead.this);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); // 라운드 처리
                            dialog1.setContentView(R.layout.item_dialog_url);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 라운드 처리
                            Button buttonOk = dialog1.findViewById(R.id.btn_ok);
                            dialog1.show();
                            buttonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    EditText insertUrl = dialog1.findViewById(R.id.et_insert_url);
                                    String valueUrl = insertUrl.getText().toString();

                                    EditText insertWidth = dialog1.findViewById(R.id.et_insert_width);
                                    String valueWidth = insertWidth.getText().toString();

                                    EditText insertHeight = dialog1.findViewById(R.id.et_insert_height);
                                    String valueHeight = insertHeight.getText().toString();

                                    EditText insertAlt = dialog1.findViewById(R.id.et_insert_alt);
                                    String valueAlt = insertAlt.getText().toString();

                                    if (TextUtils.isEmpty(valueAlt) && TextUtils.isEmpty(valueWidth) && TextUtils.isEmpty(valueHeight) && TextUtils.isEmpty(valueUrl)) {
                                        Toast.makeText(MemoDBRead.this, "값이 없음", Toast.LENGTH_SHORT).show();
                                    } else {
                                        int cursorPosition = codeEditor.getSelectionEnd();

                                        String htmlString = "<img src=\"" + valueUrl + "\" width=\"" + valueWidth + "\" height=\"" + valueHeight + "\" alt=\"" + valueAlt + "\">";
                                        // 기존 텍스트를 가져와서 Editable 객체로 변환합니다.

                                        Editable editable = codeEditor.getEditableText();

                                        // 선택된 위치에 HTML 문자열을 삽입합니다.
                                        editable.insert(cursorPosition, htmlString);

                                        // 수정된 텍스트를 다시 EditText에 설정합니다.
                                        codeEditor.setText(editable);

                                        // 커서 위치를 조정합니다.
                                        codeEditor.setSelection(cursorPosition + htmlString.length());
                                        dialog1.dismiss();
                                    }

                                }
                            });

                        }
                    });
                }

            }
        });

        codeEditor.addTextChangedListener(new TextWatcher() {
            private boolean textChanged = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!isRichEditorTextChanging && !textChanged) {
                    isCodeEditorTextChanging = true;
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(() -> {
                        richEditor.setHtml(editable.toString());
                        isCodeEditorTextChanging = false;
                    }, DELAY_MILLIS);
                }
                textChanged = false;

                saveMainText = editable.toString();
            }
        });

        richEditor.setInputEnabled(false);
        richEditor.setOnLongClickListener(new View.OnLongClickListener() {
            private long lastClickTime = 0;
            private static final long LONG_CLICK_THRESHOLD = 5000; // 5초
            private boolean isEditable = false;

            @Override
            public boolean onLongClick(View view) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime >= LONG_CLICK_THRESHOLD) {
                    isEditable = !isEditable; // 5초마다 수정 가능/불가능 상태를 토글
                    if (isEditable) {
                        richEditor.setInputEnabled(true);
                        Toast.makeText(MemoDBRead.this, "메모를 수정할 수 있는 상태입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        richEditor.setInputEnabled(false);
                        Toast.makeText(MemoDBRead.this, "메모를 읽을수만 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                    lastClickTime = currentTime;
                }

                return true;
            }
        });


        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                // 이미지가 업로드되었을 때만 containsUrlList에 추가
                if (downloadImagesUri != null && !imagesList.contains(downloadImagesUri)) {
                    imagesList.add(downloadImagesUri);
                    String containsUrl = downloadImagesUri.split("%2F")[1].split("\\?alt=")[0];
                    containsUrlList.add(containsUrl);
                    Log.v("containsUrlList 이미지 파일 확인 !", containsUrl);
                }

                for (int i = 0; i < containsUrlList.size(); i++) {
                    if (downloadImagesUri != null && !text.contains(containsUrlList.get(i))) {
                        if (downloadImagesUri != null) {
                            String newURL = downloadImagesUri.split("%2F")[1].split("\\?alt=")[0];
                            int finalI = i;
                            String subName = email.substring(0, email.lastIndexOf("@"));
                            StorageReference storageReference = storage.getReference().child(subName + "_memo_images").child(containsUrlList.get(i));
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    containsUrlList.remove(finalI);
                                    imagesList.remove(finalI);
                                    imagesList.remove(downloadImagesUri);
                                    Log.v("containsUrlList 이미지 삭제 완료 1-1", "");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("이미지 삭제 실패 2", e.getMessage());
                                }
                            });
                        }
                    }

                }

                databaseReference.child("memoList").child(writeToken).child(writeDate).child("images").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot imagesSnap : snapshot.getChildren()) {
                            longTypeImagesUrl.clear();
                            snapshotImagesList.clear();

                            String imagesValue = imagesSnap.getValue(String.class);
                            String containsUrl = imagesValue.split("%2F")[1].split("\\?alt=")[0];
                            //text 안에 이미지 주소가 있으면
                            longTypeImagesUrl.add(imagesValue);
                            snapshotImagesList.add(containsUrl);


                            for (int i = 0; i < snapshotImagesList.size(); i++) {
                                if (!text.contains(snapshotImagesList.get(i))) {
                                    StorageReference storageReference = storage.getReference().child(subName + "_memo_images").child(snapshotImagesList.get(i));
                                    int finalI = i;
                                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            for (int j = 0; j < longTypeImagesUrl.size(); j++) {
                                                String str = longTypeImagesUrl.get(j);
                                                snapshotImagesList.remove(finalI); //
                                                imagesList.remove(longTypeImagesUrl.get(j));
                                                Log.v("longTypeImagesUrl 이미지 삭제 완료 3", str);
                                                imagesSnap.getRef().removeValue();


                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.v("이미지 삭제 실패 4", e.getMessage());
                                            for (int j = 0; j < longTypeImagesUrl.size(); j++) {
                                                String str = longTypeImagesUrl.get(j);
                                                Log.v("", str);
                                            }
                                            imagesSnap.getRef().removeValue();
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                String formattedText = text.replaceAll("(https?://\\S+)", "<a href=\"$1\">$1</a>");


                if (!isCodeEditorTextChanging) {
                    isRichEditorTextChanging = true;
                    codeEditor.setText(text);
                    isRichEditorTextChanging = false;
                }

                saveMainText = text;
//                saveMainText = formattedText;
            }

        });

        insertUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 팝업을 표시하려는 View
                View anchorView = findViewById(R.id.insertUrl);

                // View의 좌표를 가져옵니다.
                int[] location = new int[2];
                anchorView.getLocationOnScreen(location);

                // 팝업 윈도우 레이아웃을 설정
                View popupView = getLayoutInflater().inflate(R.layout.item_insert, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupWindow.setFocusable(true);

                // 팝업을 표시할 위치를 계산하고 설정
                int x = location[0];
                int y = location[1] - popupView.getHeight() - 280;
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

                EditText insert = popupView.findViewById(R.id.et_insert);
                TextView send = popupView.findViewById(R.id.tv_send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String getText = insert.getText().toString();

                        try {
                            URLEmbeddedView urlEmbeddedView = new URLEmbeddedView(getApplicationContext());
                            urlEmbeddedView.setURL(getText, new URLEmbeddedView.OnLoadURLListener() {
                                @Override
                                public void onLoadURLCompleted(URLEmbeddedData data) {

                                    urlTitle = data.getTitle();
                                    urlImages = data.getThumbnailURL();
                                    urlText = data.getDescription();

                                    if (urlTitle == null){
                                        urlTitle = "내용을 가져올수 없습니다.";
                                    }

                                    if (null == urlImages) {
                                        urlImages = "https://firebasestorage.googleapis.com/v0/b/mymemo-8f83b.appspot.com/o/images%2F5.jpg?alt=media&token=3923dc33-2fcc-4976-96e9-f702417b0ff0";
                                    }

                                    if (urlText != null){
                                        int subStr = urlText.length();
                                        if (subStr >= 20) {
                                            String str = urlText.substring(0, 20);
                                            urlText = str;
                                            Log.v("str", str);
                                        }
                                    } else {
                                        urlText = "내용을 가져올수 없습니다.";
                                    }


                                    Log.v("urlText", "내용 : " + urlText);
                                    Log.v("urlToUrl", getText);
                                    Log.v("urlImages", urlImages);

                                    String resultRich = richEditor.getHtml();

                                    htmlString = "<div style='font-size: 9pt; color: blue;'>" +
                                            "<a href='" + getText + "'>" +
                                            "<img src='" + urlImages + "' width='300' height='140' alt='" + getText + "'></br>" +
                                            "</div>" +
                                            "</a>" +
                                            "제목 : " + urlTitle + "</br>" +
                                            "내용 : " + urlText + "..." + "</br>" +
                                            "</br>" +
                                            "</br> ";

                                    richEditor.setHtml(resultRich + htmlString); // htmlString을 추가
                                    saveMainText = richEditor.getHtml(); // 현재 richEditor의 내용을 saveMainText에 저장
                                    insert.setText(null);
                                    popupWindow.dismiss();

                                }
                            });

                        } catch (Exception e) {
                            Log.v("널 포인트 주소만 가져오기", getText);
                            Log.v("널 포인트 에러 메시지", e.getMessage());
                        }
                    }
                });

            }
        });


        strike = findViewById(R.id.tv_strikethrough);
        strike.setOnClickListener(new View.OnClickListener() {
            private boolean isButtonSwitch = false;

            @Override
            public void onClick(View view) {
                richEditor.setStrikeThrough();
            }
        });

        fontBold.setOnClickListener(new View.OnClickListener() {
            private boolean isButtonSwitch = false;

            @Override
            public void onClick(View view) {
                richEditor.setBold();

            }
        });

        underLine.setOnClickListener(new View.OnClickListener() {
            private boolean isButtonSwitch = false;

            @Override
            public void onClick(View view) {
                richEditor.setUnderline();
//                isButtonSwitch = !isButtonSwitch;
//                underLine.setBackgroundResource(isButtonSwitch ? R.drawable.icons_font_underline_true_size : R.drawable.icons_font_underline_size);
            }
        });


        findViewById(R.id.insertYouTube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 팝업을 표시하려는 View
                View anchorView = findViewById(R.id.insertYouTube); // 수정하세요

                // View의 좌표를 가져옵니다.
                int[] location = new int[2];
                anchorView.getLocationOnScreen(location);

                // 팝업 윈도우 레이아웃을 설정
                View popupView = getLayoutInflater().inflate(R.layout.item_insert, null); // 수정하세요
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정
                // 팝업을 표시할 위치를 계산하고 설정
                int x = location[0]; // View의 X 좌표
                int y = location[1] - popupView.getHeight() - 280; // View의 Y 좌표에서 팝업의 높이를 빼서 위에 배치
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

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

                        int width = 300;
                        int height = 200;

                        if (str.contains(urlWatchPc)) {
                            String converterUrl = str.replace(urlWatchPc, urlEmbed);
                            richEditor.insertYoutubeVideo(converterUrl, width, height);
                            richEditor.insertLink(insert.getText().toString(), insert.getText().toString());
                            popupWindow.dismiss();
                            Log.v("urlWatchPc", converterUrl);
                        } else if (str.contains(urlWatchMobile)) {
                            String converterUrl = str.replace(urlWatchMobile, urlEmbed);
                            String finalConverter = converterUrl.substring(0, converterUrl.indexOf(subUrl));
                            richEditor.insertYoutubeVideo(finalConverter, width, height);
                            richEditor.insertLink(insert.getText().toString(), insert.getText().toString());
                            popupWindow.dismiss();
                            Log.v("urlWatchMobile", finalConverter);
                        } else if (str.contains(urlShare)) {
                            String converterUrl = str.replace(urlShare, urlEmbed);
                            String finalConverter = converterUrl.substring(0, converterUrl.indexOf(subUrlShare));
                            richEditor.insertYoutubeVideo(finalConverter, width, height);
                            richEditor.insertLink(insert.getText().toString(), insert.getText().toString());
                            popupWindow.dismiss();
                            Log.v("urlShorts", finalConverter);
                        } else if (str.contains(urlShorts)) {
                            String converterUrl = str.replace(urlShorts, urlEmbed);
                            richEditor.insertYoutubeVideo(converterUrl, width, height);
                            richEditor.insertLink(insert.getText().toString(), insert.getText().toString());
                            popupWindow.dismiss();
                            Log.v("urlShorts", converterUrl);

                        } else if (!str.contains("https")) {
                            Toast.makeText(MemoDBRead.this, "주소에 https://www 포함이 된 모든 주소여야 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        fontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업을 표시하려는 View
                View anchorView = findViewById(R.id.fontSize);

                // View의 좌표를 가져옵니다.
                int[] location = new int[2];
                anchorView.getLocationOnScreen(location);

                // 팝업 윈도우 레이아웃을 설정
                View popupView = getLayoutInflater().inflate(R.layout.item_font_size, null);
                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정
                // 팝업을 표시할 위치를 계산하고 설정
                int x = location[0]; // View의 X 좌표
                int y = location[1] - popupView.getHeight() - 280; // View의 Y 좌표에서 팝업의 높이를 빼서 위에 배치
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

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

        fontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customView = getLayoutInflater().inflate(R.layout.item_background_title, null);
                TextView messages = customView.findViewById(R.id.messages);
                messages.setText("폰트에 쓰일 색상을 선택하세요.");
                new ColorPickerDialog.Builder(MemoDBRead.this)
                        .setCustomTitle(customView)
                        .setPositiveButton("확인", new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                String color = envelope.getHexCode();
                                richEditor.setTextColor(Color.parseColor("#" + color));
                                Log.v("", color);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .attachAlphaSlideBar(false)
                        .attachBrightnessSlideBar(true)
                        .show();
            }
        });

        fontBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customView = getLayoutInflater().inflate(R.layout.item_background_title, null);
                TextView messages = customView.findViewById(R.id.messages);
                messages.setText("폰트 배경화면에 쓰일 색상을 선택하세요.");
                new ColorPickerDialog.Builder(MemoDBRead.this)
                        .setCustomTitle(customView)
                        .setPositiveButton("확인", new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                String color = "#" + envelope.getHexCode();
                                richEditor.setTextBackgroundColor(Color.parseColor(color));
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .attachAlphaSlideBar(false)
                        .attachBrightnessSlideBar(true)
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
            }
        });

        backgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customView = getLayoutInflater().inflate(R.layout.item_background_title, null);
                TextView messages = customView.findViewById(R.id.messages);
                messages.setText("배경화면에 쓰일 색상을 선택하세요.");
                new ColorPickerDialog.Builder(MemoDBRead.this)

                        .setCustomTitle(customView)
                        .setPositiveButton("확인", new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                backgroundColorValue = "#" + envelope.getHexCode();
                                richEditor.setBackgroundColor(Color.parseColor(backgroundColorValue));
                                richEditorPaddingColor.setBackgroundColor(Color.parseColor(backgroundColorValue));

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .attachBrightnessSlideBar(true)
                        .attachAlphaSlideBar(false)
                        .show();
            }
        });

        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("이미지 업로드", "");
                if (richEditor.hasFocus()) {
                    imagesUpload();
                } else {
                    Toast.makeText(MemoDBRead.this, "사진을 넣으려는 위치에 놓으세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            showExitDialog();
            Log.d("Debug", "handleOnBackPressed() called");
        }
    };

    @Override
    public void onBackPressed() {
        showExitDialog();
        Log.d("Debug", "onBackPressed() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("", "onResume");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setFloatingViewDelete();
        Log.v("", "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        setFloatingViewDelete();
        Log.v("", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setFloatingViewDelete();
        Log.v("", "onPause");
    }

    private void showExitDialog() {
        Dialog dialog1 = new Dialog(this);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.item_dialog_messages);
        TextView textView = dialog1.findViewById(R.id.tv_messages);
        Button buttonOk = dialog1.findViewById(R.id.btn_01);
        buttonOk.setText("저장 하고 종료");
        Button buttonCancel = dialog1.findViewById(R.id.btn_02);
        buttonCancel.setText("저장하지 않고 종료");
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit.performClick();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView.setText("저장을 하지 않고 종료하면 저장되지 않습니다.\n저장을 하겠습니까?");
        dialog1.show();
    }

    private void showDeleteDialog(String writeDate) {
        // 다이얼로그를 표시하기 전에 이전 다이얼로그를 닫습니다.
        dismissDialog();
        dialog = new Dialog(MemoDBRead.this);
        dialog.setContentView(R.layout.item_popup_messages);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.tv_textview);
        textView.setText(R.string.write_remove_messages);

        // 다이얼로그 내의 버튼 처리 => 확인
        Button button1 = dialog.findViewById(R.id.btn_01);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("memoList").child(writeToken).child(writeDate).child("images").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot deleteSnp : snapshot.getChildren()) {
                            String imagesUrl = deleteSnp.getValue(String.class);
                            Log.v("삭제할 이미지 주소 받아오기", imagesUrl);

                            Task<Void> storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imagesUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        dialog.setContentView(R.layout.item_popup_messages);
        dialog.setCancelable(false);
        TextView messages = dialog.findViewById(R.id.tv_textview);
        Button button1 = dialog.findViewById(R.id.btn_01);
        Button button2 = dialog.findViewById(R.id.btn_02);
        TextView textView = dialog.findViewById(R.id.tv_textview);
        textView.setText(R.string.history_remove_messages);

        // 다이얼로그 내의 버튼 처리 => 확인
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("memoList").child(writeToken).child(writeDate).child("editLog").removeValue();
                finish();
            }
        });

        // 다이얼로그 내의 버튼 처리 => 취소
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

            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여된 경우 실행할 코드
                } else {
                    // 권한이 거부된 경우 사용자에게 설명하거나 다른 조치를 취할 수 있습니다.
                }
                break;
        }
    }

    private void imagesUpload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            intent.putExtra(MediaStore.ACTION_PICK_IMAGES, 1); // 사진 1장만 선택하도록 설정
            startActivityForResult(intent, MEDIA_IMAGES);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // 다중 선택 비활성화
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "사진 1장만 가능합니다"), MEDIA_IMAGES);
        }
    }

    private void setFloatingViewDelete() {
        try {

            remove();
            removeSpecificConnectUser(pushKey);
            dismissDialog();

            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }

            // Check if the floatingView is attached to the window manager before removing it
            if (floatingView.getParent() != null) {
                windowManager.removeView(floatingView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference2 = databaseReference.child("memoList").child(writeToken).child(writeDate).child("connectUser");
        databaseReference2.child(firebaseUser.getUid()).removeValue();
    }

    public void outApp() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA_IMAGES && resultCode == RESULT_OK && data != null) {
            Log.v("셀렉트 2", "");

            if (data.getData() != null) {
                Uri imagesUri = data.getData();
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    String fileUrl = data.getData().toString();
                    Uri file = data.getClipData().getItemAt(i).getUri();
                    imagesUriArray.add(file);

                    if (imagesUriArray != null) {
                        for (int j = 0; j < imagesUriArray.size(); j++) {
                            final int fileNumber = j;
                            Uri imagesUrl = imagesUriArray.get(j);
                            String date = writeDateList.getTimeFormatAdapterList();

                            String subName = email.substring(0, email.lastIndexOf("@"));
                            String fileName = "image_" + j + "_" + System.currentTimeMillis() + "_" + date + ".jpg";
                            StorageReference storageRef = storage.getReference().child(subName + "_memo_images").child(fileName);
                            Dialog dialog1 = new Dialog(MemoDBRead.this);
                            dialog1.setContentView(R.layout.progressbar_upload);
                            dialog1.show();
                            dialog1.setCancelable(false);
                            UploadTask uploadTask = storageRef.putFile(imagesUrl);
                            ProgressBar progressBar = dialog1.findViewById(R.id.progress_bar);
                            uploadTask.addOnProgressListener(taskSnapshot -> {
                                // 업로드 진행 상황을 추적하여 프로그레스 바에 반영
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressBar.setProgress((int) progress);
                            });

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // 이미지가 성공적으로 업로드되면 다운로드 URL을 가져오고 삽입합니다.
                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadImagesUri = uri.toString();
                                            Log.v("downloadImagesUri", downloadImagesUri);
                                            dialog1.dismiss();
                                            ViewGroup.LayoutParams params = richEditor.getLayoutParams();

                                            int center = Gravity.CENTER;
                                            int screenWidth = params.width - 10;

                                            richEditor.insertImage(downloadImagesUri, "이미지를 삭제하거나 업로드가 실패되었습니다.", screenWidth, 280);
                                            imagesUriArray.clear();

                                        }
                                    });
                                }
                            });

                        }
                    }


                }


            }
        }
    }
}





