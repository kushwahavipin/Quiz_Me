package com.example.quizme.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizme.LeaderboardsAdapter;
import com.example.quizme.LoginActivity;
import com.example.quizme.R;
import com.example.quizme.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminHomeScreenActivity extends AppCompatActivity {
    FirebaseFirestore database;
    CardView addQuizCategory,addQuestion,showRequest,showQuestion;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);
        recyclerView=findViewById(R.id.topStudent);

        addQuizCategory=findViewById(R.id.addQuizCategory);
        addQuestion=findViewById(R.id.addQuestion);
        showRequest=findViewById(R.id.showRequest);
        showQuestion=findViewById(R.id.showQuestion);


        database = FirebaseFirestore.getInstance();

        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapter adapter = new LeaderboardsAdapter(getApplicationContext(), users);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        database.collection("users")
                .orderBy("coins", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    User user = snapshot.toObject(User.class);
                    users.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        });

        addQuizCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeScreenActivity.this, AddQuizCategoryActivity.class));
            }
        });
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeScreenActivity.this, AddQuizQuestionActivity.class));
            }
        });
        showRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeScreenActivity.this, ShowRequestActivity.class));
            }
        });
        showQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminHomeScreenActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(AdminHomeScreenActivity.this, ShowQuestionActivity.class));
            }
        });
    }
}