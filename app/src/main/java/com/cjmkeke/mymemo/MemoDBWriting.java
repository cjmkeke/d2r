package com.cjmkeke.mymemo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.library.HolidayDecorator;
import com.cjmkeke.mymemo.library.ImageAdapter;
import com.cjmkeke.mymemo.library.OneDayDecorator;
import com.cjmkeke.mymemo.library.SaturdayDecorator;
import com.cjmkeke.mymemo.library.SunDayDecorator;
import com.cjmkeke.mymemo.library.TempDecorator;
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.userActivity.MemberJoin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.DayViewDecorator;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;

public class MemoDBWriting extends AppCompatActivity {

    private static String TAG = "MemoDBWriting";

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static String profileImagesUrl;
    private static String tokenValue;
    private static String userName;
    private static String email;
    //    private View orange, pink, purple, boldPurple, black, blue, boldGreen, green, blackBlue, yellow, red, blackPink;
    private TextView fontBackColor, insertYouTube, checkBox, fontBold, underLine, fontColor;

    private EditText title, mainText;
    private TextView commit, template;
    private TextView mediaImages, fontSize;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<Uri> selectedImageUris = new ArrayList<>(); // 이미지 URI를 저장할 리스트
    private FirebaseStorage storage;
    private Dialog dialog1;
    private CheckBox memoPublicCheck;
    private boolean checkPublic = false;
    private static final int MEDIA_IMAGES_MULTI = 100;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 101;

    private int currentColorMain;
    private int currentColorTitle;
    private String currentColor = "#000000";
    private String currentColorTitleDb = "#000000";

    private WriteDateList writeDateList;
    private String selectedDateString;
    private String memoContent;
    private static final int maxNumPhotosAndVideos = 10; // 사진 올리는 갯수
    private boolean applyColor = false;
    private boolean applyBold = false;
    private boolean isBgColor = true;
    private String imageUrl;

    private RichEditor richEditor;
    private String saveMainText;
    private static final int MEDIA_IMAGES = 105;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 106;
    private TextView uploadImages;
    private List<Uri> imagesUriArray = new ArrayList<>();

    private String downloadImagesUri;
    private List<String> imagesList = new ArrayList<>();
    private List<String> containsUrlList = new ArrayList<>();
    private String fileName;

    // 템플릿
    private Intent templateIntent;
    private Drawable drawable;
    private String backgroundColorValue;
    private TextView backgroundColor;
    private LinearLayout richEditorPaddingColor;

    private boolean codeViewSwitch = false;
    private final Handler handler = new Handler();
    private static final int DELAY_MILLIS = 1; // 적절한 딜레이 시간 설정
    private boolean isRichEditorTextChanging = false;
    private boolean isCodeEditorTextChanging = false;
    private TextView codeView; // 아직 안씀

    private boolean calendarViewSwitch = false;
    private TextView calendarView;
    private CalendarDay firstSelectedDate; // 처음 선택한 날짜
    private CalendarDay lastSelectedDate;  // 마지막으로 선택한 날짜
    private String databaseValueDate;
    private boolean isCalendarSwitch;
    private List<String> selectDateList = new ArrayList<>();
    private List<String> selectDateListDateSave = new ArrayList<>();
    private EditText calendarInput;
    private HashSet<CalendarDay> selectedDates = new HashSet<>();
    private MaterialCalendarView materialCalendarView;
    private String userText;
    private List<String> sbList = new ArrayList<>();

    private TextView settings;
    private static String titleColor;


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser == null) {
            Intent intent = new Intent(MemoDBWriting.this, MemberJoin.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_db_writing);
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        SharedPreferences sharedPreferences = getSharedPreferences("", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String valeue = sharedPreferences.getString("confirm","");
        Log.v("valeue",valeue);

        if (!"OK".equals(valeue)){
            dialog1 = new Dialog(this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.item_write_danger_messages);

            Window window = dialog1.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            ImageView imageView = dialog1.findViewById(R.id.iv_img_src);
            imageView.setImageResource(R.drawable.img_write_warring);
            ImageView close = dialog1.findViewById(R.id.iv_close);

//            TextView textView = dialog1.findViewById(R.id.textView);
//            textView.setText("개인정보를 메모장에 입력하지 마세요.\n초기 설정 값은 메모장 비공개입니다");
//            TextView button = dialog1.findViewById(R.id.btn_01);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("confirm","OK");
                    editor.apply();
                    dialog1.dismiss();
                }
            });
            dialog1.show();
        }

        memoPublicCheck = findViewById(R.id.cb_public);
        fontSize = findViewById(R.id.fontSize);
        fontColor = findViewById(R.id.font_color);
        fontBackColor = findViewById(R.id.fontBackColor);
        fontBold = findViewById(R.id.tv_bold);
        underLine = findViewById(R.id.tv_underline);
        calendarInput = findViewById(R.id.et_calendar_input_write);
        richEditor = findViewById(R.id.rich_editor);
        uploadImages = findViewById(R.id.tv_upload_images);
        backgroundColor = findViewById(R.id.editor_color);
        richEditorPaddingColor = findViewById(R.id.ll_editor_padding);
        settings = findViewById(R.id.tv_settings_write);
        codeView = findViewById(R.id.code);
        title = findViewById(R.id.et_title);
        mainText = findViewById(R.id.et_mainText);
        commit = findViewById(R.id.tv_commit);

        SharedPreferences sharedPreferences1 = getSharedPreferences("memo", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        String tempTitleValue = sharedPreferences1.getString("title","");
        String tempMainTextValue = sharedPreferences1.getString("maintext","");
        Log.v("title",tempTitleValue);
        Log.v("maintext",tempMainTextValue);

        if (tempMainTextValue != null && tempTitleValue != null){
            title.setText(tempTitleValue);
            richEditor.setHtml(tempMainTextValue);
        } else {
            title.setText("");
            richEditor.setHtml("");
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = getLayoutInflater().inflate(R.layout.item_background_title, null);
                TextView textView = view1.findViewById(R.id.messages);
                textView.setText("타이틀 글씨 색상을 선택하세요");
                new ColorPickerDialog.Builder(MemoDBWriting.this)
                        .setCustomTitle(view1)
                        .setPositiveButton("확인", new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                titleColor = "#"+envelope.getHexCode();
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

        memoPublicCheck.setVisibility(View.GONE);
        memoPublicCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    checkPublic = true;
                } else {
                    checkPublic = false;
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        writeDateList = new WriteDateList();
        if (firebaseUser != null) {
            databaseReference.child("registeredUser").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    profileImagesUrl = snapshot.child(firebaseUser.getUid()).child("profile").getValue(String.class);
                    tokenValue = snapshot.child(firebaseUser.getUid()).child("token").getValue(String.class);
                    userName = snapshot.child(firebaseUser.getUid()).child("name").getValue(String.class);
                    email = snapshot.child(firebaseUser.getUid()).child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

//        mediaImages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
//                    intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxNumPhotosAndVideos);
//                    startActivityForResult(intent, MEDIA_IMAGES_MULTI);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 다중 선택 가능하도록 설정
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "사진 10장만 가능합니다"), MEDIA_IMAGES_MULTI);
//                }
//            }
//        });

        storage = FirebaseStorage.getInstance();
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(title.getText().toString()) || TextUtils.isEmpty(saveMainText)) {
                    Toast.makeText(MemoDBWriting.this, "제목과 본문에 내용이 필요합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    WriteDateList writeDateList = new WriteDateList();
                    String date = writeDateList.getTimeFormatAdapterList();

                    Intent intent1;
                    intent1 = getIntent();
                    memoContent = intent1.getStringExtra("memoContent");
                    selectedDateString = intent1.getStringExtra("selectedDateString");
                    if (intent1 != null) {
                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("memoContent").setValue(memoContent);
                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("selectedDateString").setValue(selectedDateString);
                    }
                    long recyclerDate = writeDateList.getTimeFormatAdapterListLong();

                    for (int i = 0; i < imagesList.size(); i++) {
                        int finalI = i;
                        String saveImages = imagesList.get(i);
                        DatabaseReference imagesRef = databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("images");
                        DatabaseReference newImageRef = imagesRef.push();
                        newImageRef.setValue(saveImages);
                    }
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("mainText").setValue(saveMainText);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("title").setValue(title.getText().toString());
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("profile").setValue(profileImagesUrl);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("token").setValue(tokenValue);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("date").setValue(writeDateList.getTimeFormatAdapterView());
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("email").setValue(firebaseUser.getEmail());
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("recyclerDate").setValue(date);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("recyclerDateList").setValue(recyclerDate);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("name").setValue(userName);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("publicKey").setValue(checkPublic);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("remove").setValue(false);
                    databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("backgroundColor").setValue(backgroundColorValue);

                    if (titleColor != null){
                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("titleColor").setValue(titleColor);
                    } else {
                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("titleColor").setValue("#000000");
                    }

                    if (!TextUtils.isEmpty(userText)) {
                        for (int i = 0; i < selectDateListDateSave.size(); i++) {
                            databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("canlendarDay").child(selectDateListDateSave.get(i)).setValue(selectDateListDateSave.get(i));
                            databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("canlendarDayToMemo").child(selectDateListDateSave.get(i)).setValue(userText);
                        }
                        selectDateListDateSave.clear();
                    }

                    finish();
                }


                if (tempTitleValue != null && tempMainTextValue != null){
                    editor1.clear();
                    editor1.commit();
                }

            }
        });


        TextView strike = findViewById(R.id.tv_strikethrough);
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
            }
        });


        findViewById(R.id.insertYouTube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 팝업을 표시하려는 View
                View anchorView = findViewById(R.id.insertYouTube);

                // View의 좌표를 가져옵니다.
                int[] location = new int[2];
                anchorView.getLocationOnScreen(location);

                // 팝업 윈도우 레이아웃을 설정
                View popupView = getLayoutInflater().inflate(R.layout.item_insert, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                            Toast.makeText(MemoDBWriting.this, "주소에 https://www 포함이 된 모든 주소여야 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
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
                new ColorPickerDialog.Builder(MemoDBWriting.this)
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
                        .attachBrightnessSlideBar(false)
                        .show();
            }
        });

        fontBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customView = getLayoutInflater().inflate(R.layout.item_background_title, null);
                TextView messages = customView.findViewById(R.id.messages);
                messages.setText("폰트 배경화면에 쓰일 색상을 선택하세요.");
                new ColorPickerDialog.Builder(MemoDBWriting.this)
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
                        .attachBrightnessSlideBar(false)
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
                new ColorPickerDialog.Builder(MemoDBWriting.this)
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
                        .attachAlphaSlideBar(false)
                        .attachBrightnessSlideBar(false)
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
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
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정
                // 팝업을 표시할 위치를 계산하고 설정
                int x = location[0]; // View의 X 좌표
                int y = location[1] - popupView.getHeight() - 280; // View의 Y 좌표에서 팝업의 높이를 빼서 위에 배치
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);

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

        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("이미지 업로드", "");
                if (richEditor.hasFocus()) {
                    imagesUpload();
                } else {
                    Toast.makeText(MemoDBWriting.this, "사진을 넣으려는 위치에 놓으세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // TODO Calendar 에디터
        calendarView = findViewById(R.id.calendar);
        Button buttonOk = findViewById(R.id.btn_calendar_read_view_write);
        LinearLayout calendarLinear = findViewById(R.id.ll_calendar_view_write);
        LinearLayout calendarLinearKeyboard = findViewById(R.id.ll_calendar_view_keyboard_write);
        LinearLayout calendarLinearKeyboard2 = findViewById(R.id.ll_calendar_memo_view_write);
        TextView calendarMemoView = findViewById(R.id.tv_calendar_view_write);
        materialCalendarView = findViewById(R.id.calendar_xml_write);
        calendarInput.setMaxLines(1);
        calendarMemoView.setVisibility(View.GONE);
        calendarLinear.setVisibility(View.GONE);
        calendarLinearKeyboard.setVisibility(View.GONE);
        calendarLinearKeyboard2.setVisibility(View.GONE);
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
        materialCalendarView.setSelectionColor(ContextCompat.getColor(this, R.color.black));
        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat("yyyy년 MM월", Locale.getDefault())));
        StringBuilder sb = new StringBuilder(calendarMemoView.getText().toString());

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarViewSwitch) {
                    calendarLinear.setVisibility(View.GONE);
                    calendarLinearKeyboard.setVisibility(View.GONE);
                    calendarMemoView.setVisibility(View.GONE);
                    calendarLinearKeyboard2.setVisibility(View.GONE);
                    calendarViewSwitch = false;
                } else {
                    // Code to show the calendar elements
                    calendarLinear.setVisibility(View.VISIBLE);
                    calendarLinearKeyboard.setVisibility(View.VISIBLE);
                    calendarMemoView.setVisibility(View.VISIBLE);
                    calendarLinearKeyboard2.setVisibility(View.VISIBLE);
                    calendarViewSwitch = true;

                    materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                            isCalendarSwitch = selected;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            databaseValueDate = (sdf.format(date.getDate()));

                            if (isCalendarSwitch) {
                                // 날짜가 선택된 경우에만 목록에 추가
                                selectDateList.add(databaseValueDate);
                                selectDateListDateSave.add(databaseValueDate);
                            } else {
                                // 날짜가 선택 해제된 경우 목록에서 제거
                                selectDateList.remove(databaseValueDate);
                                selectDateListDateSave.remove(databaseValueDate);

                                // sbList에서도 해당 날짜에 대한 메모 제거
                                Iterator<String> iterator = sbList.iterator();
                                while (iterator.hasNext()) {
                                    String memo = iterator.next();
                                    if (memo.startsWith(databaseValueDate)) {
                                        iterator.remove();
                                    }
                                }
                                // sbList의 내용으로 calendarMemoView 업데이트
                                calendarMemoView.setText(TextUtils.join("\n", sbList));
                            }
                        }
                    });

                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(calendarInput.getText().toString())){
                                userText = calendarInput.getText().toString();
                                sbList.clear();

                                for (String selectedDate : selectDateList) {
                                    String combinedMemo = selectedDate + " : " + userText;
                                    sb.append(combinedMemo).append("\n");
                                    Log.v("1", selectDateList.toString());
                                    // combinedMemo를 기준으로 중복 체크
                                    if (!sbList.contains(combinedMemo)) {
                                        sbList.add(combinedMemo);
                                        calendarMemoView.setText(TextUtils.join("\n", sbList)); // sbList의 내용으로 calendarMemoView 업데이트
                                    }
                                }
                            } else {
                                Toast.makeText(MemoDBWriting.this, "날짜를 선택하고 메모를 입력해주세요", Toast.LENGTH_SHORT).show();
                            }
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
                    if (codeEditor.getText().toString() == null) {
                        codeEditor.setText(richEditor.getHtml().toString());
                    } else {

                    }
                    codeViewSwitch = true;

                    buttonSrc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dialog dialog1 = new Dialog(MemoDBWriting.this);
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
                                        Toast.makeText(MemoDBWriting.this, "값이 없음", Toast.LENGTH_SHORT).show();
                                    } else {
                                        int cursorPosition = codeEditor.getSelectionStart();

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

        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                // 이미지가 업로드되었을 때만 containsUrlList에 추가
                if (downloadImagesUri != null && !imagesList.contains(downloadImagesUri)) {
                    imagesList.add(downloadImagesUri);
                    String containsUrl = downloadImagesUri.split("%2F")[1].split("\\?alt=")[0];
                    containsUrlList.add(containsUrl);
                    Log.v("이미지 파일 확인", containsUrl);
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
                                    Log.v("이미지 삭제 완료", "");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("이미지 삭제 실패", e.getMessage());
                                }
                            });
                        }
                    }
                }

                if (!isCodeEditorTextChanging) {
                    isRichEditorTextChanging = true;
                    codeEditor.setText(text);
                    isRichEditorTextChanging = false;
                }

                saveMainText = text;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA_IMAGES && resultCode == RESULT_OK && data != null) {
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
                            fileName = "image_" + j + "_" + System.currentTimeMillis() + "_" + date + ".jpg";
                            StorageReference storageRef = storage.getReference().child(subName + "_memo_images").child(fileName);

                            UploadTask uploadTask = storageRef.putFile(imagesUrl);
                            Dialog dialog = new Dialog(MemoDBWriting.this);
                            dialog.setContentView(R.layout.progressbar_upload);
                            ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
                            dialog.show();
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
                                            Log.v("aa", downloadImagesUri);
                                            dialog.dismiss();

                                            ViewGroup.LayoutParams params = richEditor.getLayoutParams();
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

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            showExitDialog();
            Log.d("Debug", "handleOnBackPressed() called");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        showExitDialog();
        Log.d("Debug", "onBackPressed() called");
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
                tempSaveMemo();
                finish();
            }
        });

        textView.setText("저장을 하지 않고 종료하면 저장되지 않습니다.\n저장을 하겠습니까?");
        dialog1.show();
    }

    private void tempSaveMemo(){
        SharedPreferences sharedPreferences = getSharedPreferences("memo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Date date = new Date();
        long saveTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);

        editor.putString("title", title.getText().toString());
        editor.putString("maintext", saveMainText+"</br>"+time+"<font color=#00FF00>"+" "+userName+"</font>"+" 님이 임시 저장함");
        editor.apply();
    }

}
