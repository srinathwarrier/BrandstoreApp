package com.brandstore1.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.brandstore1.BrandstoreApplication;
import com.brandstore1.R;
import com.brandstore1.activities.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.brandstore1.BrandstoreApplication.NotificationStatus;

/**
 * Created by I076324 on 5/13/2015.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    static final String TAG = "GCMDemo";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String testMessage =   intent.getStringExtra("key1");
//                String notificationTypeString = intent.getStringExtra("type");
//                String displayMessage = intent.getStringExtra("message");
//                String userSongId = intent.getStringExtra("usersongid");
//                if(notificationTypeString!=null) {
//                    int notificationTypeInt = Integer.parseInt(notificationTypeString);
//                    NotificationMessage notificationMessage = new NotificationMessage(notificationTypeInt , displayMessage,userSongId );
//                    sendNotification(notificationMessage);
//                }
                NotificationMessage notificationMessage = new NotificationMessage(testMessage );
                sendNotification(notificationMessage);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(NotificationMessage notificationMessage) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);


//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra("IS_NOTIFICATION", true);
//        intent.putExtra("NOTIFICATION_TYPE", notificationMessage.getNotificationType());


        /*BrandstoreApplication application = (BrandstoreApplication) getApplication();
        NotificationStatus notificationStatus = BrandstoreApplication.addNotification(notificationMessage);
        int numMessages = application.getNumUnreadMessages();

        intent.putExtra("USER_SONG_ID", BrandstoreApplication.getCombinedUserSongId());
        //intent.putExtra("NOTIFICATION_STATUS", notificationStatus);


        NotificationCompat.InboxStyle inboxStyle = application.getInboxStyle();
        if(inboxStyle==null){
            application.setInboxStyle(new NotificationCompat.InboxStyle());
            inboxStyle = application.getInboxStyle();
        }
        inboxStyle.addLine(notificationMessage.getDisplayMessage());

        String contextText ="Check these out !";
        String contentTitle = "Slate";
        if(numMessages==1){
            // For SINGLE notification
            contentTitle = "1 new notification";
            contextText =notificationMessage.getDisplayMessage();
        }
        else{
            // For more than 1 notifications
            contentTitle = numMessages+" new notifications";
            contextText = numMessages+" notifications";
            if(notificationStatus == NotificationStatus.ONLY_ADD_SONG){ // 1: only ADD_SONG
                contextText =  numMessages +" songs have been added";
            }
            else if(notificationStatus ==NotificationStatus.SAME_TALK_SONG){// 2: only TALK_SONG
                contextText = numMessages+" new talks";
            }
            else if(notificationStatus ==NotificationStatus.ALL_TYPES){ // 3 : both
                contextText = "New songs, New talks";
            }
        }
*/
        String testMessage = notificationMessage.getDisplayMessage();




        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.brandstorelogoicon)
                        .setContentTitle(testMessage)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(testMessage))
                        .setContentText(testMessage);

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    //TODO: This is the default method. Remove this method.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.brandstorelogoicon)
                        .setContentTitle("Brandstore")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}