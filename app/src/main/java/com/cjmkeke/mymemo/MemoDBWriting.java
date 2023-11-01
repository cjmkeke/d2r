package com.cjmkeke.mymemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.library.ImageAdapter;
import com.cjmkeke.mymemo.library.WriteDateList;
import com.cjmkeke.mymemo.userActivity.MemberJoin;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private View orange, pink, purple, boldPurple, black, blue, boldGreen, green, blackBlue, yellow, red, blackPink;
    private TextView fontBlackColor, insertYouTube, checkBox;

    private EditText title, mainText;
    private TextView commit, template;
    private TextView mediaImages, calendar, fontSize;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<Uri> selectedImageUris = new ArrayList<>(); // 이미지 URI를 저장할 리스트
    private FirebaseStorage storage;
    private Dialog dialogDangerMessages;
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

    // 템플릿
    private Intent templateIntent;
    private SharedPreferences preferences;
    private Drawable drawable;

    private class ImageDownloader extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Drawable drawable = Drawable.createFromStream(input, "image");
                return drawable;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                // 이미지 다운로드 및 설정
                mainText.setBackground(drawable);
            }
        }
    }

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


        dialogDangerMessages = new Dialog(this);
        dialogDangerMessages.setContentView(R.layout.item_write_danger_messages);
        dialogDangerMessages.setCancelable(false);
        Button button = dialogDangerMessages.findViewById(R.id.btn_01);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDangerMessages.dismiss();
            }
        });
        dialogDangerMessages.show();

        calendar = findViewById(R.id.calendar);
        memoPublicCheck = findViewById(R.id.cb_public);
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
        red = findViewById(R.id.color_red);
        blackPink = findViewById(R.id.color_black_pink);
        fontSize = findViewById(R.id.fontSize);
        fontBlackColor = findViewById(R.id.fontBackColor);


        template = findViewById(R.id.tv_template);
        recyclerView = findViewById(R.id.imagesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImageAdapter(selectedImageUris);
        recyclerView.setAdapter(adapter);
        mediaImages = findViewById(R.id.iv_media_images_select);
        richEditor = findViewById(R.id.rich_editor);
        uploadImages = findViewById(R.id.tv_upload_images);


        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoDBWriting.this, Calendar.class);
                startActivity(intent);
            }
        });

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

        if (selectedImageUris.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        title = findViewById(R.id.et_title);
        mainText = findViewById(R.id.et_mainText);


        commit = findViewById(R.id.tv_commit);
        writeDateList = new WriteDateList();
        templateIntent = getIntent();
        preferences = getPreferences(0);
        SharedPreferences.Editor editor = preferences.edit();

        String value = getIntent().getStringExtra("NoteTemplate");
//        int value = getIntent().getIntExtra("NoteTemplate", 0);
        if (value != null) {
            // 리소스 아이디를 Drawable로 변환하여 배경 설정
            new ImageDownloader().execute(value);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mainText.setBackground(drawable);
                String shareValue = preferences.getString("main", "s1");
                String shareValueTitle = preferences.getString("title", "s1");
                int shareTitleValueColor = preferences.getInt("colorTitle", 0);
                int shareMainValueColor = preferences.getInt("colorMain", 0);
                title.setTextColor(shareTitleValueColor);
                mainText.setTextColor(shareMainValueColor);
                mainText.setText(shareValue);
                title.setText(shareValueTitle);
            } else {
                mainText.setBackgroundDrawable(drawable);
                String shareValue = preferences.getString("main", "s1");
                String shareValueTitle = preferences.getString("title", "s1");
                int shareTitleValueColor = preferences.getInt("colorTitle", 0);
                int shareMainValueColor = preferences.getInt("colorMain", 0);
                title.setTextColor(shareTitleValueColor);
                mainText.setTextColor(shareMainValueColor);
                mainText.setText(shareValue);
                title.setText(shareValueTitle);
            }
        }

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

        mediaImages.setOnClickListener(new View.OnClickListener() {
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
                    startActivityForResult(Intent.createChooser(intent, "사진 10장만 가능합니다"), MEDIA_IMAGES_MULTI);
                }
            }
        });

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
                        databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("images").child("images_"+finalI).setValue(saveImages);
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
                    finish();
                    if (drawable != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();

                        Drawable drawable2 = drawable;
                        Bitmap bitmap = ((BitmapDrawable) drawable2).getBitmap();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        StorageReference imageRef = storageRef.child("images/image.jpg");
                        UploadTask uploadTask = imageRef.putBytes(data);

                        uploadTask.addOnSuccessListener(taskSnapshot -> {
                            // 업로드 성공 시 이미지의 다운로드 URL 획득
                            Task<Uri> downloadUrlTask = imageRef.getDownloadUrl();
                            downloadUrlTask.addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("template").setValue(imageUrl);

                            });
                        }).addOnFailureListener(exception -> {
                            // 업로드 실패 처리
                        });

                    }

                    // 이미지 URI 리스트를 Firebase Storage에 업로드
                    if (selectedImageUris != null) {
                        String subName = email.substring(0, email.lastIndexOf("@"));
                        try {
                            for (int i = 0; i < selectedImageUris.size(); i++) {
                                Uri imageUri = selectedImageUris.get(i);
                                // 이미지를 비트맵으로 로드
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

                                // 이미지를 원하는 크기(800x600)로 리사이즈
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 1400, 800, false);

                                // 비트맵을 JPEG 형식으로 압축
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] data = byteArrayOutputStream.toByteArray();

                                String fileName = "image_" + i + "_" + System.currentTimeMillis() + "_" + date + ".jpg";
                                StorageReference storageRef = storage.getReference().child(subName + "_memo_images").child(fileName);
                                UploadTask uploadTask = storageRef.putBytes(data);

                                int finalI = i; // for accessing within the listeners
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // 이미지 업로드 성공 시 이미지 다운로드 URL을 가져와서 저장합니다.
                                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                imageUrl = uri.toString();
                                                // 이미지 URL을 Firebase Realtime Database에 저장
                                                databaseReference.child("memoList").child(firebaseUser.getUid()).child(date).child("boardImages").child("images" + finalI).setValue(imageUrl);

                                                // 모든 이미지 업로드가 완료되면 Activity를 종료합니다.
                                                if (finalI == selectedImageUris.size() - 1) {
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 업로드 실패 시 처리할 작업을 수행하세요.
                                    }
                                });
                            }
                        } catch (Exception e) {

                        }

                    }
                }
            }
        });

        template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mainTextValue = mainText.getText().toString();
                String titleValue = title.getText().toString();
                int colorValue = title.getTextColors().getDefaultColor();
                int colorValueMain = mainText.getTextColors().getDefaultColor();
                editor.putInt("colorTitle", colorValue);
                editor.putInt("colorMain", colorValueMain);
                editor.putString("main", mainTextValue);
                editor.putString("title", titleValue);
                editor.apply();
                Intent intent = new Intent(MemoDBWriting.this, Template.class);
                startActivity(intent);
            }
        });

//        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
//            @Override
//            public void onTextChange(String text) {
////                mainText.setText(text);
//
//                saveMainText = text;
//
//            }
//        });

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
        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                TODO 이미지 하나씩 업로드 하는 로직
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
                            richEditor.insertYoutubeVideo(converterUrl, width, height);
                            popupWindow.dismiss();
                            Log.v("urlWatchPc", converterUrl);
                        } else if (str.contains(urlWatchMobile)) {
                            String converterUrl = str.replace(urlWatchMobile, urlEmbed);
                            String finalConverter = converterUrl.substring(0, converterUrl.indexOf(subUrl));
                            richEditor.insertYoutubeVideo(finalConverter, width, height);
                            popupWindow.dismiss();
                            Log.v("urlWatchMobile", finalConverter);
                        } else if (str.contains(urlShare)) {
                            String converterUrl = str.replace(urlShare, urlEmbed);
                            String finalConverter = converterUrl.substring(0, converterUrl.indexOf(subUrlShare));
                            richEditor.insertYoutubeVideo(finalConverter, width, height);
                            popupWindow.dismiss();
                            Log.v("urlShorts", finalConverter);
                        } else if (str.contains(urlShorts)) {
                            String converterUrl = str.replace(urlShorts, urlEmbed);
                            richEditor.insertYoutubeVideo(converterUrl, width, height);
                            popupWindow.dismiss();
                            Log.v("urlShorts", converterUrl);

                        } else if (!str.contains("https")) {
                            Toast.makeText(MemoDBWriting.this, "주소에 https://www 포함이 된 모든 주소여야 가능합니다.", Toast.LENGTH_SHORT).show();
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

                popupView.findViewById(R.id.bg_color_white).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        richEditor.setTextBackgroundColor(Color.parseColor("#FFFFFF"));
                        popupWindow.dismiss();
                    }
                });

            }
        });
        fontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.item_font_size, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 배경을 투명하게 설정
                popupWindow.setFocusable(true); // 포커스 가능하도록 설정
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

        //TODO 리치에디터 Text
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

            //TODO 이미지 하나씩 업로드 하는 로직
            case REQUEST_READ_EXTERNAL_STORAGE:
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
                int count = Math.min(data.getClipData().getItemCount(), maxNumPhotosAndVideos);
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }

                if (selectedImageUris.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }

            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
            }
            adapter.notifyDataSetChanged();
        }
//        TODO 이미지 하나씩 업로드 하는 로직
        else if (requestCode == MEDIA_IMAGES && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                Uri imagesUri = data.getData();

//                richEditor.insertImage(imagesUri.toString(), "", 390, 280);
//                Log.v("", imagesUri.toString());

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

                            UploadTask uploadTask = storageRef.putFile(imagesUrl);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // 이미지가 성공적으로 업로드되면 다운로드 URL을 가져오고 삽입합니다.
                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadImagesUri = uri.toString();
                                            Log.v("aa", downloadImagesUri);

                                            ViewGroup.LayoutParams params = richEditor.getLayoutParams();
                                            int screenWidth = params.width;
                                            richEditor.insertImage(downloadImagesUri, "이미지 업로드 실패", screenWidth, 280);
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