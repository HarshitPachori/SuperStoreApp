package com.app.superdistributor.dealerreports.paymentreports.beans;

public class ChequePaymentModel {
    String ChequeScreenshotUrl,Date, User,UserType;

    public String getChequeScreenshotUrl() {
        return ChequeScreenshotUrl;
    }

    public void setChequeScreenshotUrl(String chequeScreenshotUrl) {
        ChequeScreenshotUrl = chequeScreenshotUrl;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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
}
