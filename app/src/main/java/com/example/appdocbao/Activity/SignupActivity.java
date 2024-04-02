package com.example.appdocbao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appdocbao.Model.User;
import com.example.appdocbao.R;
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

public class SignupActivity extends AppCompatActivity {
    private EditText signupName, signupPhoneNumber, signupUsername, signupPassword;
    private TextView txtlogin;
    private Button btnregis;
    private FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Uri uri;
    String imageUrl;
    ImageView imageUserUpload;
    boolean isUploadImage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        imageUserUpload = (ImageView) findViewById(R.id.imageUserUpload);
        signupName = (EditText) findViewById(R.id.signupName);
        signupPhoneNumber = (EditText) findViewById(R.id.signupPhoneNumber);
        signupUsername = (EditText) findViewById(R.id.signupUsername);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        btnregis = (Button) findViewById(R.id.signupBtn);
        txtlogin = (TextView) findViewById(R.id.textSignUp);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            isUploadImage = true;
                            Intent data = result.getData();
                            uri = data.getData();
                            imageUserUpload.setImageURI(uri);
                        }else{
                            Toast.makeText(SignupActivity.this, "No Image Upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUploadImage) {
                    onClickPushData();
                    register();
                }else{
                    Toast.makeText(SignupActivity.this,"Vui lòng chọn ảnh !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageUserUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void onClickPushData() {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Image User")
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageUrl = uri.toString();
                                            uploadData();
                                            dialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            // Xử lý lỗi khi không thể lấy URL tải xuống
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            // Xử lý lỗi khi không thể tải lên hình ảnh
                        }
                    });
        } else {
            Toast.makeText(this, "Không thể tạo đối tượng StorageReference với giá trị URI null", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadData(){
        String name = signupName.getText().toString();
        String phone = signupPhoneNumber.getText().toString();
        String email = signupUsername.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
        String id = userFirebase.getUid();
        User user;
        if(imageUrl == null){
            user = new User(id, name,email,0,phone,"https://firebasestorage.googleapis.com/v0/b/appdocbao-75d78.appspot.com/o/user-profile-icon.png?alt=media&token=c41f08e0-0f6f-413d-bc33-4a1f3f638dd9");
        }else{
            user = new User(id, name,email,0,phone,imageUrl);
        }

        FirebaseDatabase.getInstance().getReference("users").child(id)
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void register(){
        String email, password;
        email = signupUsername.getText().toString();
        password = signupPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Vui lòng nhập password.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(it);
                }else{
                    Toast.makeText(getApplicationContext(),"Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}