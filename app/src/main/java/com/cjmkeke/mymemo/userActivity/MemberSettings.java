package com.cjmkeke.mymemo.userActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cjmkeke.mymemo.R;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MemberSettings extends AppCompatActivity {

    private static final String TAG = "MemberSettings";

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private Dialog dialog;

    private TextView selectProfile, name, changeName, changePassword, signDelete;
    private EditText rePassword1;
    private LinearLayout changePasswordTable;
    private Button passwordChangeCommit;

    private String profileImagesUrl;
    private String nameValue;
    private ImageView profile;
    private Uri selectProfileImagesData;
    private static final int RESULT_PICK_OK = 101;
    private static String userEmail;
    private boolean isChangePasswordTableVisible = false;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null || firebaseAuth == null || firebaseUser.getUid() == null) {
            finish();
            Toast.makeText(this, R.string.service_error, Toast.LENGTH_SHORT).show();
        }
        profile = findViewById(R.id.iv_profile);
        profile.setClipToOutline(true);
        selectProfile = findViewById(R.id.tv_select_profile);
        name = findViewById(R.id.tv_name);
        changeName = findViewById(R.id.tv_change_name);
        changePassword = findViewById(R.id.tv_change_password);
        changePasswordTable = findViewById(R.id.ll_change_password);
        rePassword1 = findViewById(R.id.et_password1);
        passwordChangeCommit = findViewById(R.id.btn_change_password_commit);
        signDelete = findViewById(R.id.tv_sign_delete);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if (firebaseUser != null) {
            databaseReference.child("registeredUser").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    profileImagesUrl = snapshot.child("profile").getValue(String.class);
                    userEmail = snapshot.child("email").getValue(String.class);
                    nameValue = snapshot.child("name").getValue(String.class);
                    if (!isDestroyed()) {
                        Glide.with(MemberSettings.this).load(profileImagesUrl).into(profile);
                    }
                    name.setText(nameValue);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        signDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(MemberSettings.this);
                dialog.setContentView(R.layout.item_dialog_messages_danger);
                TextView textView = dialog.findViewById(R.id.tv_messages);
                textView.setText("정말 회원 탈퇴를 하시겠습니까?\n회원 탈퇴를 하면 기존에 있었던 모든 데이터는 지워지며, 지워진 데이터와 계정은 복구는 되지 않습니다.");
                Button button1 = dialog.findViewById(R.id.btn_01);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MemberSettings.this, "회원 탈퇴 처리가 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                databaseReference.child("memoList").child(firebaseUser.getUid()).removeValue();
                                databaseReference.child("registeredUser").child(firebaseUser.getUid()).removeValue();

                                int index = userEmail.indexOf("@");
                                String subEmail = userEmail.substring(0, index);
                                String addressFinal = subEmail + "_profile_images";
                                Log.v(TAG, subEmail);
                                Log.v(TAG, addressFinal);

                                storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();
                                StorageReference deleteImages = storage.getReference(subEmail+"_memo_images");
                                storageRef.child("registeredUser").child(addressFinal).delete();
                                deleteImages.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                    @Override
                                    public void onSuccess(ListResult listResult) {
                                        for (StorageReference listDelete : listResult.getItems()) {
                                            listDelete.delete();
                                        }
                                    }
                                });
                                firebaseAuth.signOut();
                                finish();
                            }
                        });
                    }
                });

                Button button2 = dialog.findViewById(R.id.btn_02);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        changePasswordTable.setVisibility(View.GONE);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // changePasswordTable의 상태를 토글합니다.
                if (isChangePasswordTableVisible) {
                    changePasswordTable.setVisibility(View.GONE);
                } else {
                    changePasswordTable.setVisibility(View.VISIBLE);
                    passwordChangeCommit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog = new Dialog(MemberSettings.this);
                            dialog.setContentView(R.layout.item_dialog_messages_danger);
                            dialog.setCancelable(false);
                            TextView textView = dialog.findViewById(R.id.tv_messages);
                            textView.setText("비밀번호를 정말 바꾸시겠습니까?");
                            Button button1 = dialog.findViewById(R.id.btn_01);
                            button1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String changePw1 = rePassword1.getText().toString();
                                    firebaseUser.updatePassword(changePw1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MemberSettings.this, "패스워드가 바뀌었습니다.", Toast.LENGTH_SHORT).show();
                                            rePassword1.setText("");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                    dialog.dismiss();
                                }
                            });

                            Button button2 = dialog.findViewById(R.id.btn_02);
                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();


                        }
                    });


                }
                isChangePasswordTableVisible = !isChangePasswordTableVisible;
            }
        });

        selectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, RESULT_PICK_OK);
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MemberSettings.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.item_change_name);
                EditText editText = dialog.findViewById(R.id.et_change);
                TextView textView = dialog.findViewById(R.id.tv_messages);
                textView.setText("바꾸실 닉네임을 입력해주세요.");
                int maxLength = 10; // 원하는 최대 글자 수로 변경
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(maxLength);
                editText.setFilters(new InputFilter[]{
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
                Button button = dialog.findViewById(R.id.btn_01);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            Toast.makeText(MemberSettings.this, "닉네임은 공백이 될수는 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            String getEmail = editText.getText().toString();
                            databaseReference.child("registeredUser").child(firebaseUser.getUid()).child("name").setValue(getEmail);
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PICK_OK && data != null && resultCode == RESULT_OK) {
            selectProfileImagesData = data.getData();

            int index = userEmail.indexOf("@");
            String subEmail = userEmail.substring(0, index);
            String addressFinal = "/registeredUser/" + subEmail + "_profile_images";
            storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child(addressFinal);
            UploadTask uploadTask = storageReference.putFile(selectProfileImagesData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String uriImages = uri.toString();
                            databaseReference.child("registeredUser").child(firebaseUser.getUid()).child("profile").setValue(uriImages);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests(); // Glide 작업 일시 중지

    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).pauseRequests(); // Glide 작업 일시 중지
    }
}