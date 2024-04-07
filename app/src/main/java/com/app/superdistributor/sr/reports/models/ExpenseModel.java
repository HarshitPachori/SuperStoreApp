package com.app.superdistributor.sr.reports.models;

public class ExpenseModel {
   String Amount,Date,Status,Type;

    public ExpenseModel(String amount, String date, String status, String type) {
        Amount = amount;
        Date = date;
        Status = status;
        Type = type;
    }

    @Override
    public String toString() {
        return "ExpenseModel{" +
                "Amount='" + Amount + '\'' +
                ", Date='" + Date + '\'' +
                ", Status='" + Status + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }

    public ExpenseModel() {
    }

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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
