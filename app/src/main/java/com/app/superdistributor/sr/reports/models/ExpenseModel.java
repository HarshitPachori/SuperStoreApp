package com.app.superdistributor.sr.reports.models;

public class ExpenseModel {
   String Amount,Date,Status,Type,Name;

    public ExpenseModel(String amount, String date, String status, String type,String name) {
        Amount = amount;
        Date = date;
        Status = status;
        Type = type;
        Name = name;
    }

    @Override
    public String toString() {
        return "ExpenseModel{" +
                "Amount='" + Amount + '\'' +
                ", Date='" + Date + '\'' +
                ", Status='" + Status + '\'' +
                ", Type='" + Type + '\'' +  ", Name='" + Name + '\'' +
                '}';
    }

    public ExpenseModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
