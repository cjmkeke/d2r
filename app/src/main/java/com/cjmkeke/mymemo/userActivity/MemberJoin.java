package com.cjmkeke.mymemo.userActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cjmkeke.mymemo.modelClass.DTOMemberJoin;
import com.cjmkeke.mymemo.MainActivity;
import com.cjmkeke.mymemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MemberJoin extends AppCompatActivity {

    private TextView join, profile, home;
    private EditText email, password, rePassword, name;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    // 스토리지 변수
    private static final int RESULT_PICK_OK = 101;
    private FirebaseStorage storage;
    private Uri selectedImageUri;
    private static String profileImagesUrl;
    private static String subStringEmail;
    private static final String basicImages = "https://firebasestorage.googleapis.com/v0/b/mymemo-8f83b.appspot.com/o/images%2Fdocument.png?alt=media&token=8994e5f7-bbdc-40a1-ab8b-ef03e3d662e4";
    private Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        join = findViewById(R.id.tv_commit);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password1);
        rePassword = findViewById(R.id.et_password2);
        profile = findViewById(R.id.tv_profile);
        home = findViewById(R.id.tv_home);
        name = findViewById(R.id.et_name);
        int maxLength = 10; // 원하는 최대 글자 수로 변경
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(maxLength);
        name.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(maxLength), // 최대 글자 수 제한
                new InputFilter() {
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        // 입력된 문자열을 반복하여 특수 문자를 거부
                        for (int i = start; i < end; i++) {
                            char character = source.charAt(i);
                            if (!Character.isLetterOrDigit(character)) {
                                return ""; // 특수 문자를 거부
                            }
                        }
                        return null; // 특수 문자가 없으므로 필터링하지 않고 모든 입력을 허용
                    }
                }
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("registeredUser");

        storage = FirebaseStorage.getInstance();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, RESULT_PICK_OK);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignature();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberJoin.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setSignature() {

        String resultName = name.getText().toString();
        String resultEmail = email.getText().toString();
        String resultPw1 = password.getText().toString();
        String resultPw2 = rePassword.getText().toString();

        if (TextUtils.isEmpty(resultEmail)) {
            Toast.makeText(this, "이메일은 필수 입력입니다.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(resultName)) {
            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (profileImagesUrl == null) {
            Toast.makeText(this, "가입을 하려면 사진이 필요합니다.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(resultPw1)) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (resultPw1.equals(resultPw2)) {

            mAuth.createUserWithEmailAndPassword(resultEmail, resultPw1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        DTOMemberJoin dtoMemberJoin = new DTOMemberJoin();

                        dtoMemberJoin.setName(resultName);
                        dtoMemberJoin.setEmail(resultEmail);
                        dtoMemberJoin.setProfile(profileImagesUrl);
                        dtoMemberJoin.setToken(firebaseUser.getUid());

                        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                        DatabaseReference dataSave = firebaseDatabase1.getReference("registeredUser");
                        dataSave.child(firebaseUser.getUid()).setValue(dtoMemberJoin);
                        finish();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MemberJoin.this, "이미 가입된 이메일이 있습니다. 다른 이메일로 가입해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PICK_OK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            // 업로드할 파일 경로 (예: "profile_images" 폴더 안에 "profile.jpg" 파일로 저장)
//            String storagePath = "profile_images/profile.jpg";
            String resultEmail = email.getText().toString();
            int index = resultEmail.indexOf('@');
            if (index != -1) { // '@' 문자가 존재하는 경우
                subStringEmail = resultEmail.substring(0, index); // 인덱스 0부터 '@' 문자 인덱스 직전까지 추출합니다.
                Log.v("sub", subStringEmail);
            }
            String storagePath = "registeredUser/" + subStringEmail + "_profile_images";

            // Firebase Storage 레퍼런스
            StorageReference storageRef = storage.getReference().child(storagePath);

            customDialog = new Dialog(this);
            customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
            customDialog.setCancelable(false); // 터치금지
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명하게
            customDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 겉에 불투명도 없에기
            customDialog.setContentView(R.layout.progressbar);
            customDialog.show();

            // 이미지 업로드
            UploadTask uploadTask = storageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // 업로드 성공 시 이미지 URL을 받아올 수도 있습니다.
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImagesUrl = uri.toString();
                            customDialog.dismiss();
//                            String imageUrl = uri.toString();
                            // 이미지 업로드 성공 후 처리할 작업을 수행하세요.
                            // 예를 들어, 해당 이미지 URL을 Firestore에 저장하거나, 이미지를 표시하는 등의 작업을 수행할 수 있습니다.
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
    }
}
