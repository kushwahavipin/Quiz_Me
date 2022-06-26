package com.erinfa.quizme.Admin.Question;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.erinfa.quizme.R;

public class ShowQuestionActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);
        recyclerView = findViewById(R.id.showQuestionAdd);


    }

}