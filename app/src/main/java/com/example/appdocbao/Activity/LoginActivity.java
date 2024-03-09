package com.example.appdocbao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appdocbao.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    EditText txtEmail, txtPassword;
    AppCompatButton btnLogin;
    Button loginBtn;
    ImageView imgIconGoogle;
    TextView lableSignUp;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.usernameEdt);
        txtPassword = (EditText) findViewById(R.id.passwordEdt);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        lableSignUp = (TextView) findViewById(R.id.textSignUp);

        imgIconGoogle = (ImageView) findViewById(R.id.imgIconGoogle);
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
    public void buttonGoogleSignIn(View view){

    }
}