package com.erinfa.quizme.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.erinfa.quizme.CategoryModel;
import com.erinfa.quizme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddQuizCategoryActivity extends AppCompatActivity {
    EditText name;
    CircleImageView profile;
    Button quit, update;
    RelativeLayout uploadImage;

    static final int PICK_IMAGE = 1;

    private final int REQ = 1;
    private Bitmap bitmap;
    String categoryImage, categoryName, categoryId;
    private StorageReference storageReference;
    String downloadUrl = "";
    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz_category);
        name = findViewById(R.id.nameBox);
        profile = findViewById(R.id.profileImage);
        quit = findViewById(R.id.quitBtn);
        update = findViewById(R.id.updateBtn);
        uploadImage = findViewById(R.id.uploadImageVIew);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Profile...");

        firebaseFirestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child("categoryImage");

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                categoryName = name.getText().toString();

                final CategoryModel categoryModel = new CategoryModel(categoryId, categoryName,categoryImage);


                if (!categoryName.isEmpty()) {
                    if (!(bitmap == null)) {
                        uploadImage();
                        firebaseFirestore.collection("categories").add(categoryModel)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()) {
                                            categoryId = task.getResult().getId();
                                            Log.d("uniqueId", task.getResult().getId());
                                            // Toast.makeText(AddQuizCategoryActivity.this, categoryId, Toast.LENGTH_SHORT).show();

                                            //uploadImage();
                                            dialog.dismiss();
                                            startActivity(new Intent(AddQuizCategoryActivity.this, AddQuizCategoryActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(AddQuizCategoryActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddQuizCategoryActivity.this, "Failed Upload", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    } else {
                        dialog.dismiss();
                        Toast.makeText(AddQuizCategoryActivity.this, "Upload Image", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(AddQuizCategoryActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadImage() {
        ByteArrayOutputStream base = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, base);
        byte[] finalImage = base.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddQuizCategoryActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    loadImageUrl();
                                    //Toast.makeText(UpdateProfile.this, downloadUrl, Toast.LENGTH_SHORT).show();
                                    // startActivity(getIntent());

                                }
                            });
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(AddQuizCategoryActivity.this, "Somethings went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadImageUrl() {
        firebaseFirestore.collection("categories").document(categoryId).update("categoryImage", downloadUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       // Toast.makeText(AddQuizCategoryActivity.this, categoryId, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddQuizCategoryActivity.this, "Some things went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profile.setImageBitmap(bitmap);
        }
    }
}