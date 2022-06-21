package com.erinfa.quizme;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {
    EditText name, phone;
    CircleImageView profile;
    Button quit, update;
    RelativeLayout uploadImage;


    static final int PICK_IMAGE = 1;

    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl = "";
    private StorageReference storageReference;

    FirebaseFirestore firebaseFirestore;
    DocumentReference reference;


    String currentId, nameResult, phoneResult, image;
    FirebaseUser user;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        name = findViewById(R.id.nameBox);
        phone = findViewById(R.id.phoneBox);
        profile = findViewById(R.id.profileImage);
        quit = findViewById(R.id.quitBtn);
        update = findViewById(R.id.updateBtn);
        uploadImage = findViewById(R.id.uploadImageVIew);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Profile...");

        user = FirebaseAuth.getInstance().getCurrentUser();
        currentId = user.getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child("profile");

        reference = firebaseFirestore.collection("users").document(currentId);

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    nameResult = task.getResult().getString("name");
                    phoneResult = task.getResult().getString("phone");
                    image = task.getResult().getString("profile");

                    name.setText(nameResult);

                    phone.setText(phoneResult);
                    Glide.with(getApplicationContext()).load(image).into(profile);

                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileFragment.class));
                finish();
            }
        });

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
                String newName = name.getText().toString();
                firebaseFirestore.collection("users").document(currentId)
                        .update("name", newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(UpdateProfile.this, "Update", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(UpdateProfile.this, "Some things went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


                if (!(bitmap ==null)) {
                    uploadImage();

                }
            }
        });

    }

    private void uploadImage() {
        ByteArrayOutputStream base = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, base);
        byte[] finalImage = base.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(currentId).child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(UpdateProfile.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    Log.d("URL",downloadUrl);
                                    loadImageUrl();
                                    //Toast.makeText(UpdateProfile.this, downloadUrl, Toast.LENGTH_SHORT).show();
                                   // startActivity(getIntent());

                                }
                            });
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(UpdateProfile.this, "Somethings went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadImageUrl() {
        firebaseFirestore.collection("users").document(currentId).update("profile",downloadUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       // Toast.makeText(UpdateProfile.this, "Update", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfile.this, "Some things went wrong", Toast.LENGTH_SHORT).show();

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