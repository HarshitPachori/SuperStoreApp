package com.app.superdistributor.sr.reports.models;

public class RaiseComplaintModel {

    String Dealer, Description, Tag,Date;

    public RaiseComplaintModel() {
    }

    public RaiseComplaintModel(String dealer, String description, String tag, String date) {
        Dealer = dealer;
        Description = description;
        Tag = tag;
        Date = date;
    }

    public String getDealer() {
        return Dealer;
    }

    public void setDealer(String dealer) {
        Dealer = dealer;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "RaiseComplaintModel{" +
                "Dealer='" + Dealer + '\'' +
                ", Description='" + Description + '\'' +
                ", Tag='" + Tag + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
