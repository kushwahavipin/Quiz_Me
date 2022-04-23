package com.example.quizme.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toolbar;

import com.example.quizme.CategoryAdapter;
import com.example.quizme.CategoryModel;
import com.example.quizme.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddQuizQuestionActivity extends AppCompatActivity {
    FirebaseFirestore database;
    RecyclerView categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);


        categoryList=findViewById(R.id.categoryList);
        database=FirebaseFirestore.getInstance();
        final ArrayList<CategoryModel> categories = new ArrayList<>();

        final AdminCategoryAdapter adapter = new AdminCategoryAdapter(getApplicationContext(), categories);
        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        categoryList.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        categoryList.setAdapter(adapter);
    }
}