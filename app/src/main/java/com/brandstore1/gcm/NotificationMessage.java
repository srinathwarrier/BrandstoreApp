package com.brandstore1.gcm;

import com.brandstore1.BrandstoreApplication;

/**
 * Created by i076324 on 6/8/2015.
 */
public class NotificationMessage {
    private BrandstoreApplication.NotificationType notificationType; // 1: AddSong 2: AddTalk
    private String displayMessage;
    private String userSongId;

    public NotificationMessage(int notificationTypeInt, String displayMessage, String userSongId) {
        this.setNotificationType(getNotificationTypeFromIntValue(notificationTypeInt));
        this.displayMessage = displayMessage;
        this.userSongId = userSongId;
    }
    public NotificationMessage(String displayMessage) {
        this.setNotificationType(BrandstoreApplication.NotificationType.ADD_SONG);
        this.displayMessage = displayMessage;
    }


    public BrandstoreApplication.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(BrandstoreApplication.NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public BrandstoreApplication.NotificationType getNotificationTypeFromIntValue(int notificationType) {
        if(notificationType==1){
            return BrandstoreApplication.NotificationType.ADD_SONG;
        }
        else { //if(notificationType==2)
            return BrandstoreApplication.NotificationType.TALK_SONG;
        }
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getUserSongId() {
        return userSongId;
    }

    public void setUserSongId(String userSongId) {
        this.userSongId = userSongId;
    }

}
