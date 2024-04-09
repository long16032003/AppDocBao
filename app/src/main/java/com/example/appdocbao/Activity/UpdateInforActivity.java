package com.example.appdocbao.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Fragment.UserFragment;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;

public class UpdateInforActivity extends AppCompatActivity {
    ImageView imgUpdate, imgback;
    EditText usernameUpdate, emailUpdate, phoneUpdate;
    Button btnUpdate, btnPicture;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference databaseReference;
    String imageURL;
    Uri URI;

    boolean isUploadImage = true;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor);
        imgUpdate = (ImageView) findViewById(R.id.imgUserUpdate);
        usernameUpdate = (EditText) findViewById(R.id.updateName);
        phoneUpdate = (EditText) findViewById(R.id.updatePhoneNumber);
        emailUpdate = (EditText) findViewById(R.id.updateEmail);
        mAuth = FirebaseAuth.getInstance();

        btnPicture = (Button) findViewById(R.id.update_Picture);
        btnUpdate = (Button) findViewById(R.id.updateBtn1);
        imgback = (ImageView) findViewById(R.id.backImg);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        final String id_User = mAuth!=null ? mAuth.getUid() : (googleSignInAccount != null ? googleSignInAccount.getId() : "" );


        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(id_User);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy dữ liệu người dùng từ dataSnapshot
                String name = dataSnapshot.child("name").getValue(String.class);
                String emailSnapshot = dataSnapshot.child("email").getValue(String.class);
                String phoneSnapshot = dataSnapshot.child("phone").getValue(String.class);
                imageURL = dataSnapshot.child("img").getValue(String.class);

                if (!isFinishing() && !isDestroyed()) {
                    Glide.with(UpdateInforActivity.this).load(imageURL).into(imgUpdate);
                }
                usernameUpdate.setText(name);
                emailUpdate.setText(emailSnapshot);
                emailUpdate.setEnabled(false);
                phoneUpdate.setText(phoneSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Xử lí lỗi
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData(id_User);
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        URI = data.getData();
                        imgUpdate.setImageURI(URI);
                    }else{
                        Toast.makeText(UpdateInforActivity.this, "No Image Upload", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });
    }
    private void onClickPushData(String id_User){
        if (URI == null) {
            if (imageURL != null) {
                uploadData(id_User);
            } else {
                Toast.makeText(UpdateInforActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            imageURL = URI.toString();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Image User")
                    .child(URI.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInforActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri urlImage) {
                            imageURL = urlImage.toString();
                            uploadData(id_User);
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void uploadData(String id_User){
        String userName = usernameUpdate.getText().toString();
        String phoneNumber = phoneUpdate.getText().toString();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(id_User);
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("name", userName);
        updateData.put("phone", phoneNumber);
        updateData.put("img", imageURL);

        usersRef.updateChildren(updateData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(UpdateInforActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateInforActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}