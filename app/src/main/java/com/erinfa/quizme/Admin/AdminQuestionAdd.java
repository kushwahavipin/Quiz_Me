package com.erinfa.quizme.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erinfa.quizme.Question;
import com.erinfa.quizme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminQuestionAdd extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    ProgressDialog dialog;
    EditText question1, option11, option21, option31, option41, answer1, index1;
    Button submit,showQuestion;
    String str,subjectName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_question_add);
        firebaseFirestore = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new question...");
        intent = getIntent();
        str = intent.getStringExtra("catId");

        question1 = findViewById(R.id.question);
        option11 = findViewById(R.id.option_1);
        option21 = findViewById(R.id.option_2);
        option31 = findViewById(R.id.option_3);
        option41 = findViewById(R.id.option_4);
        answer1 = findViewById(R.id.answer);
        submit = findViewById(R.id.createQuestion);
        showQuestion = findViewById(R.id.showQuestion);
        index1 = findViewById(R.id.index);



        showQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminQuestionAdd.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(AdminQuestionAdd.this, ShowQuestionActivity.class));
            }
        });

        subjectName = intent.getStringExtra("categoryName");
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(subjectName);






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = question1.getText().toString();
                String option1 = option11.getText().toString();
                String option2 = option21.getText().toString();
                String option3 = option31.getText().toString();
                String option4 = option41.getText().toString();
                String answer = answer1.getText().toString();
                long index = Long.parseLong(index1.getText().toString());





                if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
                    Toast.makeText(AdminQuestionAdd.this, "Filled All Data", Toast.LENGTH_SHORT).show();
                } else {
                    final Question question2 = new Question(question, option1, option2, option3, option4, answer, index);

                    //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                    dialog.show();
                    firebaseFirestore.collection("categories")
                            .document(str).collection("questions").add(question2)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        question1.setText("");
                                        option11.setText("");
                                        option21.setText("");
                                        option31.setText("");
                                        option41.setText("");
                                        answer1.setText("");
                                       // startActivity(new Intent(AdminQuestionAdd.this, AdminQuestionAdd.class));
                                    } else {
                                        Toast.makeText(AdminQuestionAdd.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        firebaseFirestore.collection(subjectName).document(str).collection("questions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                    Question question=snapshot.toObject(Question.class);
                    assert question != null;
                    index1.setText(String.valueOf(question.getIndex()));
                    Toast.makeText(AdminQuestionAdd.this, "Data Not Get", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AdminQuestionAdd.this, (int) question.getIndex(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}