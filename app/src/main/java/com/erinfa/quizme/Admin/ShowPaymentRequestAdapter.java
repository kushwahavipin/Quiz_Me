package com.erinfa.quizme.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erinfa.quizme.R;
import com.erinfa.quizme.WithdrawRequest;

import java.util.ArrayList;

public class ShowPaymentRequestAdapter extends RecyclerView.Adapter<ShowPaymentRequestAdapter.ShowPaymentRequestViewHolder> {
    Context context;
    ArrayList<WithdrawRequest> withdrawRequests;

    public ShowPaymentRequestAdapter(Context context, ArrayList<WithdrawRequest> withdrawRequests) {
        this.context = context;
        this.withdrawRequests = withdrawRequests;
    }

    @NonNull
    @Override
    public ShowPaymentRequestAdapter.ShowPaymentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment_request, parent, false);
        return new ShowPaymentRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPaymentRequestAdapter.ShowPaymentRequestViewHolder holder, int position) {



        WithdrawRequest withdrawRequest=withdrawRequests.get(position);
        holder.userName.setText(withdrawRequest.getRequestedBy());
        holder.userEmail.setText(withdrawRequest.getEmailAddress());
        holder.userPhone.setText(withdrawRequest.getRequestPhone());
        holder.userId.setText(withdrawRequest.getUserId());
        holder.requestCoins.setText(String.valueOf(withdrawRequest.getCoins()));
        holder.requestDate.setText(String.valueOf(withdrawRequest.getCreatedAt()));


    }

    @Override
    public int getItemCount() {
        return withdrawRequests.size();
    }

    public static class ShowPaymentRequestViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail, userPhone, userId, requestDate,requestCoins;

        public ShowPaymentRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userPhone = itemView.findViewById(R.id.userPhone);
            userId = itemView.findViewById(R.id.userId);
            requestDate = itemView.findViewById(R.id.requestDate);
            requestCoins = itemView.findViewById(R.id.requestCoins);

        }
    }
}
