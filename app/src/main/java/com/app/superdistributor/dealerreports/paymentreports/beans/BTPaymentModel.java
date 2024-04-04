package com.app.superdistributor.dealerreports.paymentreports.beans;

public class BTPaymentModel {
    String Amount, Date, TransactionScreenshotUrl;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTransactionScreenshotUrl() {
        return TransactionScreenshotUrl;
    }

    public void setTransactionScreenshotUrl(String transactionScreenshotUrl) {
        TransactionScreenshotUrl = transactionScreenshotUrl;
    }
}
