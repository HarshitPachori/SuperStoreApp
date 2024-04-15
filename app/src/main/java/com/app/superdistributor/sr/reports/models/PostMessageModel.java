package com.app.superdistributor.sr.reports.models;

import java.util.ArrayList;

public class PostMessageModel {
    String Message , AudioUrl;
    String Dealers,Date;

    public PostMessageModel() {
    }

    public PostMessageModel(String message, String audioUrl, String dealers, String date) {
        Message = message;
        AudioUrl = audioUrl;
        Dealers = dealers;
        Date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAudioUrl() {
        return AudioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        AudioUrl = audioUrl;
    }

    public String getDealers() {
        return Dealers;
    }

    public void setDealers(String dealers) {
        Dealers = dealers;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "PostMessageModel{" +
                "Message='" + Message + '\'' +
                ", AudioUrl='" + AudioUrl + '\'' +
                ", Dealers='" + Dealers + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
