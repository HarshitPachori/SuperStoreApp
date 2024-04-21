package com.app.superdistributor.sr.reports.models;

public class PaymentModel {
    String Amount,Status,Dealer,Type,Date;

    public PaymentModel() {
    }

    public PaymentModel(String amount, String status, String dealer, String type, String date) {
        Amount = amount;
        Status = status;
        Dealer = dealer;
        Type = type;
        Date = date;
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

    public String getDealer() {
        return Dealer;
    }

    public void setDealer(String dealer) {
        Dealer = dealer;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "PaymentModel{" +
                "Amount='" + Amount + '\'' +
                ", Status='" + Status + '\'' +
                ", Dealer='" + Dealer + '\'' +
                ", Type='" + Type + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
