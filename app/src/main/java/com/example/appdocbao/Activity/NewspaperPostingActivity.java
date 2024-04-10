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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.text.SimpleDateFormat;

import com.example.appdocbao.Model.Article;
import com.example.appdocbao.Model.Category;
import com.example.appdocbao.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.internal.Storage;
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
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewspaperPostingActivity extends AppCompatActivity {
    Button btnDangBao, btnUploadPicture;
    Spinner spinnerCategory;
    EditText titleUpload, contentUpload, authorUpload;
    ImageView imageUpload, backMain;
    DatabaseReference spinnerRef;
    ArrayList<String> spinnerList;
    ArrayAdapter<String> adapter;
    List<Category> categoryList = new ArrayList<>();
    String imageUrl;
    boolean isUploadImage = false;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspaper_posting);

        imageUpload = (ImageView) findViewById(R.id.imageUpload);
        titleUpload = (EditText) findViewById(R.id.titleUpload);
        contentUpload = (EditText) findViewById(R.id.contentUpload);
        authorUpload = (EditText) findViewById(R.id.authorUpload);

        btnDangBao = (Button) findViewById(R.id.btnDangBao);
        btnUploadPicture = (Button) findViewById(R.id.btnUploadPicture);
        backMain = (ImageView) findViewById(R.id.backMain);

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerRef = FirebaseDatabase.getInstance().getReference("category");

        spinnerList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(NewspaperPostingActivity.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        ShowdataSpinner();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            isUploadImage = true;
                            Intent data = result.getData();
                            uri = data.getData();
                            imageUpload.setImageURI(uri);
                        }else{
                            Toast.makeText(NewspaperPostingActivity.this, "No Image Upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });
        btnDangBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUploadImage) onClickPushData();
                else{
                    Toast.makeText(NewspaperPostingActivity.this,"Vui lòng chọn ảnh !",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void TichDiem(String id_User){
        DatabaseReference pointReference =  FirebaseDatabase.getInstance().getReference("users/"+id_User+"/points");
        pointReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                // Lấy giá trị hiện tại của points
                Integer currentPoints = mutableData.getValue(Integer.class);

                if (currentPoints != null) {
                    // Tăng giá trị points lên 5
                    mutableData.setValue(currentPoints + 5);
                }

                // Trả về giá trị mới của points
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                if (committed) {
                    // Tăng giá trị points thành công
                    // Thực hiện các hành động phụ thuộc vào việc tăng points ở đây
                } else {
                    // Tăng giá trị points thất bại
                    // Xử lý lỗi nếu cần
                }
            }
        });
    }

    private void ShowdataSpinner(){
        spinnerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear(); // Xóa danh sách category hiện tại để cập nhật lại từ Firebase
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int categoryId = dataSnapshot.child("id").getValue(Integer.class);
                    String categoryName = dataSnapshot.child("name").getValue(String.class);
                    Category category = new Category(categoryId, categoryName);
                    categoryList.add(category);
                    spinnerList.add(categoryName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickPushData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Image Article")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(NewspaperPostingActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();

            }
        });
    }

    private void uploadData(){
        String title = titleUpload.getText().toString();
        String content = contentUpload.getText().toString();
        String author = authorUpload.getText().toString();
//        int id_category = Integer.parseInt(categoryUpload.getText().toString());

        String categoryName = spinnerCategory.getSelectedItem().toString();
        int id_category = getCategoryIdByName(categoryName);


        Date currentDateTime = new Date();
        long timestamp = currentDateTime.getTime();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("articles");

        String id = myRef.push().getKey();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        String idUserPost ="";
        if(mAuth != null){
            idUserPost = mAuth.getUid();
        }
        else if(googleSignInAccount!=null){
            idUserPost = googleSignInAccount.getId();
        }
        Article article = new Article(id, title,content,id_category,author,imageUrl, timestamp, idUserPost);
        TichDiem(idUserPost);

        FirebaseDatabase.getInstance().getReference("articles").child(id)
                .setValue(article).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewspaperPostingActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewspaperPostingActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private int getCategoryIdByName(String categoryName) {
        for (Category category : categoryList) {
            if (category.getName().equals(categoryName)) {
                return category.getId();
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy category
    }
}