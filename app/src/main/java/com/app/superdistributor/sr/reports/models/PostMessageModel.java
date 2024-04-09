package com.app.superdistributor.sr.reports.models;

import java.util.ArrayList;

public class PostMessageModel {
    String Message , AudioUrl;
    String Dealers;

    public PostMessageModel() {
    }

    public PostMessageModel(String message, String audioUrl, String dealers) {
        Message = message;
        AudioUrl = audioUrl;
        Dealers = dealers;
    }

    public String getDealers() {
        return Dealers;
    }

    public void setToDealers(String dealers) {
        Dealers = dealers;
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

    @Override
    public String toString() {
        return "PostMessageModel{" +
                "Message='" + Message + '\'' +
                ", AudioUrl='" + AudioUrl + '\'' +
                ", Dealers=" + Dealers +
                '}';
    }
}
