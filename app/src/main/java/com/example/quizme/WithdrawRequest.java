package com.example.quizme;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String emailAddress;
    private String requestedBy;
    private String requestPhone;

    public WithdrawRequest() {

    }

    public WithdrawRequest(String userId, String emailAddress, String requestedBy,String requestPhone) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.requestedBy = requestedBy;
        this.requestPhone=requestPhone;
    }

    public String getRequestPhone(){
        return requestPhone;
    }
    public void setRequestPhone(String requestPhone) {
        this.requestPhone = requestPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @ServerTimestamp
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
