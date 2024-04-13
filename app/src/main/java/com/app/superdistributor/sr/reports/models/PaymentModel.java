package com.app.superdistributor.sr.reports.models;

public class PaymentModel {
    String Amount,Status,User,UserType,PaymentMethod;

    public PaymentModel() {
    }

    public PaymentModel(String amount, String status, String user, String userType, String paymentMethod) {
        Amount = amount;
        Status = status;
        User = user;
        UserType = userType;
        PaymentMethod = paymentMethod;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "PaymentModel{" +
                "Amount='" + Amount + '\'' +
                ", Status='" + Status + '\'' +
                ", User='" + User + '\'' +
                ", UserType='" + UserType + '\'' +
                ", PaymentMethod='" + PaymentMethod + '\'' +
                '}';
    }
}
