package com.example.appdocbao.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

import com.example.appdocbao.Model.User;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    EditText txtEmail, txtPassword;
    AppCompatButton btnLogin;
    Button loginBtn;
    ImageView imgIconGoogle, backButton;
    TextView lableSignUp, forgotPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.usernameEdt);
        txtPassword = (EditText) findViewById(R.id.passwordEdt);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        lableSignUp = (TextView) findViewById(R.id.textSignUp);

        imgIconGoogle = (ImageView) findViewById(R.id.imgIconGoogle);
        backButton = (ImageView) findViewById(R.id.backButton);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
            }
        });
        lableSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(it);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        //Checking if user already signed in
        if(googleSignInAccount != null){


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                //đăng nhập tài khoản sau khi người dùng chọn tài khoản từ hộp thoại tài khoản google
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInTask(task);
            }
        });
        imgIconGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signinIntent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void onClickForgotPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = txtEmail.getText().toString().trim();
        if(emailAddress == null || emailAddress.toString().trim().isEmpty())
            Toast.makeText(LoginActivity.this, "Vui long nhap email!", Toast.LENGTH_SHORT).show();
        else{
            auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "Email sent fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void login(){
        String email, password;
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Vui lòng nhập password.", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Dăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                }else{
                    Toast.makeText(getApplicationContext(),"Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void handleSignInTask(Task<GoogleSignInAccount> task){
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            //Lấy dữ liệu người dùng
            final String getFullname = account.getDisplayName();
            final String getEmail = account.getEmail();
            final Uri getPhotoUrl = account.getPhotoUrl();
            final String getId = account.getId();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(getId)) {
                        // Nút "users" chứa ID cụ thể
                    } else {
                        // Thưc hiện việc thêm người dùng mới khi đăng nhập Google vào realtime
                        uploadData(account);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }catch (ApiException e){
            e.printStackTrace();
            Toast.makeText(this, "Failed or Cancelled",Toast.LENGTH_SHORT).show();
        }

    }
    private void uploadData(GoogleSignInAccount account){
        String name = account.getDisplayName();
        String phone = "";
        String email = account.getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        String id = account.getId();
        Uri uriImage = account.getPhotoUrl();
        User user = new User(id, name, email, 0, phone, uriImage.toString());

        FirebaseDatabase.getInstance().getReference("users").child(id)
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}