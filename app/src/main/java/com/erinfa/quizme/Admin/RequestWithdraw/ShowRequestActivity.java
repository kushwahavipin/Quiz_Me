package com.erinfa.quizme.Admin.RequestWithdraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.erinfa.quizme.Admin.Adapter.ShowPaymentRequestAdapter;
import com.erinfa.quizme.R;
import com.erinfa.quizme.UI.WithdrawRequest.WithdrawRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowRequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_request);
        recyclerView=findViewById(R.id.paymentRequest);
        database = FirebaseFirestore.getInstance();

        final ArrayList<WithdrawRequest> withdrawRequests = new ArrayList<>();
        final ShowPaymentRequestAdapter adapter = new ShowPaymentRequestAdapter(getApplicationContext(), withdrawRequests);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        database.collection("withdrawal")
                .orderBy("coins", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    WithdrawRequest withdrawRequest = snapshot.toObject(WithdrawRequest.class);
                    withdrawRequests.add(withdrawRequest);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}