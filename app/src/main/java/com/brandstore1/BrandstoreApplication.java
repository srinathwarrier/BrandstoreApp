package com.brandstore1;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.brandstore1.gcm.NotificationMessage;

/**
 * Created by i076324 on 6/8/2015.
 */
public class BrandstoreApplication extends Application {
    // model
    private static int numUnreadMessages;
    private static NotificationCompat.InboxStyle inboxStyle;


    private static NotificationStatus notificationStatus = NotificationStatus.DEFAULT;

    private static String combinedUserSongId  ="";
    public static final int NOTIFICATION_ID = 1;

    public static String getCombinedUserSongId() {
        return combinedUserSongId;
    }

    public static void setCombinedUserSongId(String combinedUserSongId) {
        BrandstoreApplication.combinedUserSongId = combinedUserSongId;
    }

    public NotificationCompat.InboxStyle getInboxStyle() {
        return inboxStyle;
    }

    public void setInboxStyle(NotificationCompat.InboxStyle inboxStyle) {
        this.inboxStyle = inboxStyle;
    }

    public int getNumUnreadMessages() {
        return numUnreadMessages;
    }

    public void setNumUnreadMessages(int numUnreadMessages) {
        this.numUnreadMessages = numUnreadMessages;
    }



    public static NotificationStatus addNotification(NotificationMessage notificationMessage){
        numUnreadMessages++;
        String newUserSongId = notificationMessage.getUserSongId();

        if(notificationMessage.getNotificationType() == NotificationType.ADD_SONG){
            setCombinedUserSongId("");
            switch(notificationStatus){
                case DEFAULT:
                    // Old was default(none) , New is ADD_SONG. So change to ADD_SONG
                    notificationStatus = NotificationStatus.ONLY_ADD_SONG;
                    break;
                case ONLY_ADD_SONG:
                    // Old was ADD_SONG , New is also ADD_SONG. So keep as ADD_SONG (DO NOTHING)
                    break;
                case SAME_TALK_SONG:
                    // Old was TALK_SONG for same song, New is ADD_SONG. So change to BOTH
                    notificationStatus = NotificationStatus.ALL_TYPES;
                    break;
                case ALL_TYPES:
                    // Old was BOTH , New is ADD_SONG. So keep as BOTH (DO NOTHING)
                    break;
                case DIFFERENT_TALK_SONG:
                    // Old was TALK_SONG for different song, New is ADD_SONG. So change to BOTH
                    notificationStatus = NotificationStatus.ALL_TYPES;
                    break;
                default:
                    break;
            }
        }
        else if (notificationMessage.getNotificationType() == NotificationType.TALK_SONG){
            switch(notificationStatus){
                case DEFAULT:
                    // Old was default(none) , New is TALK_SONG. So change to TALK_SONG
                    notificationStatus = NotificationStatus.SAME_TALK_SONG;
                    setCombinedUserSongId(newUserSongId);
                    break;
                case ONLY_ADD_SONG:
                    // Old was ADD_SONG , New is TALK_SONG. So change to BOTH
                    notificationStatus = NotificationStatus.ALL_TYPES;
                    setCombinedUserSongId("");
                    break;
                case SAME_TALK_SONG:
                    // Old was TALK_SONG for same song, New is TALK_SONG. So , check if UserSongId is same.
                    // if same, DO NOTHING
                    // if different, change to 4 :TALK_SONG for different Songs
                    if(!getCombinedUserSongId().equals(newUserSongId)){
                        setCombinedUserSongId("");
                        notificationStatus= NotificationStatus.DIFFERENT_TALK_SONG;
                    }

                    break;
                case ALL_TYPES:
                    // Old was BOTH , New is TALK_SONG. So keep as BOTH (DO NOTHING)
                    break;
                case DIFFERENT_TALK_SONG:
                    // Old was TALK_SONG for Different songs, New is TALK_SONG. So keep as TALK_SONG for Different songs(Do nothing)
                    break;
                default:
                    break;
            }

        }
        return notificationStatus;
    }

    public void removeAllNotifications(){
        setNumUnreadMessages(0);
        setInboxStyle(new NotificationCompat.InboxStyle());
        notificationStatus = NotificationStatus.DEFAULT;
        setCombinedUserSongId("");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public enum NotificationType{
        ADD_SONG , TALK_SONG;
    }

    public enum NotificationStatus{
        DEFAULT, ONLY_ADD_SONG, SAME_TALK_SONG,DIFFERENT_TALK_SONG  , ALL_TYPES
        /*
            Notification Status meanings:
             0: default,
             1: only ADD_SONG ,
             2: only TALK_SONG  (and for the same song),
             3 : both 1 and 2 ,
             4: only TALK_SONG but for DIFFERENT songs. !
          */
    }
}
