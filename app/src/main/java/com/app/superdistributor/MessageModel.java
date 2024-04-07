package com.app.superdistributor;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageModel{
    String Sender;
    String Recipient;
    String Message;

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    String Timestamp;

    public MessageModel(String sender, String recipient, String message, String timestamp) {
        Sender = sender;
        Recipient = recipient;
        Message = message;
        Timestamp = timestamp;
    }

    public MessageModel() {
    }


    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "Sender='" + Sender + '\'' +
                ", Recipient='" + Recipient + '\'' +
                ", Message='" + Message + '\'' +
                ", Timestamp=" + Timestamp +
                '}';
    }


}
