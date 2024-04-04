package com.app.superdistributor.sr.complaint;

public class ComplaintModel {
    String tag, description, dealer;

    public ComplaintModel(String tag, String description, String dealer) {
        this.tag = tag;
        this.description = description;
        this.dealer = dealer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }
}
