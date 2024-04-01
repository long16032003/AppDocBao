package com.example.appdocbao.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appdocbao.Model.Article;
import com.example.appdocbao.Model.Category;
import com.example.appdocbao.R;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UpdateArticleActivity extends AppCompatActivity {
    Button btnCapnhat;
    TextView editImage;
    EditText titleUpload, contentUpload, authorUpload;
    ImageView imageUpload, backMain;
    String imageUrl;
    Uri uri;
    boolean isUploadImage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_article);
        imageUpload = (ImageView) findViewById(R.id.imageUpload);
        titleUpload = (EditText) findViewById(R.id.titleUpload);
        contentUpload = (EditText) findViewById(R.id.contentUpload);
        authorUpload = (EditText) findViewById(R.id.authorUpload);
        editImage = (TextView) findViewById(R.id.editImage);

        btnCapnhat = (Button) findViewById(R.id.btnDangBao);
        backMain = (ImageView) findViewById(R.id.backMain);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String titleArticle = bundle.getString("Title");
            String contentArticle = bundle.getString("Content");
            String authorArticle = bundle.getString("Author");

            titleUpload.setText(titleArticle);
            contentUpload.setText(contentArticle);
            authorUpload.setText(authorArticle);


            Glide.with(this)
                    .load(bundle.getString("Image"))
                    .into(imageUpload);

            String id_Article = bundle.getString("Id");
            btnCapnhat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPushData(id_Article);
                    finish();
                }
            });
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        uri = data.getData();
                        imageUpload.setImageURI(uri);
                    }else{
                        Toast.makeText(UpdateArticleActivity.this, "No Image Upload", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

    }
    private void onClickPushData(String id_Article){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imageFromBundle = bundle.getString("Image");
            if (uri == null) {
                if (imageFromBundle != null) {
                    imageUrl = imageFromBundle;
                    uploadData(id_Article);
                } else {
                    Toast.makeText(UpdateArticleActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            } else {
                imageUrl = uri.toString();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("Image Article")
                        .child(uri.getLastPathSegment());
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateArticleActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri urlImage) {
                                imageUrl = urlImage.toString();
                                uploadData(id_Article);
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
    }

    private void uploadData(String id_Article){
        String title = titleUpload.getText().toString();
        String content = contentUpload.getText().toString();
        String author = authorUpload.getText().toString();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String idUserPost = user.getUid();

        DatabaseReference articleRef = FirebaseDatabase.getInstance().getReference("articles").child(id_Article);
        // Tạo một HashMap để lưu trữ các giá trị cần cập nhật
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("title", title);
        updateData.put("author", author);
        updateData.put("content", content);
        updateData.put("img", imageUrl);

        articleRef.updateChildren(updateData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(UpdateArticleActivity.this, "Cập nhật bài báo thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateArticleActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}