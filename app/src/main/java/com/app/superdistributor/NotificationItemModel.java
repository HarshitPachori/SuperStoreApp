package com.app.superdistributor;

import java.util.Objects;

public class NotificationItemModel {
    String notificationType;
    String notificationTag;
    String notificationPriority = "No";

    String reportUrl;
    String NotificationId;

    public String getReportUrl() {
        return reportUrl;
    }


    public String getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(String notificationId) {
        NotificationId = notificationId;
    }

    public NotificationItemModel(String notificationType, String notificationTag, String notificationPriority, String reportUrl, String notificationDesc) {
        this.notificationType = notificationType;
        this.notificationTag = notificationTag;
        this.notificationPriority = notificationPriority;
        this.reportUrl = reportUrl;
        this.notificationDesc = notificationDesc;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getNotificationPriority() {
        return notificationPriority;
    }

    public void setNotificationPriority(String notificationPriority) {
        this.notificationPriority = notificationPriority;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    String notificationDesc;

    public NotificationItemModel(){}

    public NotificationItemModel(String notificationType, String notificationDesc, String notificationTag ) {
        this.notificationType = notificationType;
        this.notificationTag = notificationTag;
        this.notificationDesc = notificationDesc;
    }

    public String getNotificationTag() {
        return notificationTag;
    }

    public void setNotificationTag(String notificationTag) {
        this.notificationTag = notificationTag;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationItemModel)) return false;
        NotificationItemModel that = (NotificationItemModel) o;
        return getNotificationType().equals(that.getNotificationType()) && getNotificationTag().equals(that.getNotificationTag()) && getNotificationDesc().equals(that.getNotificationDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotificationType(), getNotificationTag(), getNotificationDesc());
    }
}
