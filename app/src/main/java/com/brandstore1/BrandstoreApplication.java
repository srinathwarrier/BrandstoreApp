package com.brandstore1;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.brandstore1.gcm.NotificationMessage;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.HashMap;

/**
 * Created by i076324 on 6/8/2015.
 */
public class BrandstoreApplication extends Application implements BootstrapNotifier {
    private static final String TAG = "BrandstoreApplication";

    // model
    private static int numUnreadMessages;
    private static NotificationCompat.InboxStyle inboxStyle;

    private static BrandstoreApplication mInstance;

    private static NotificationStatus notificationStatus = NotificationStatus.DEFAULT;

    private static String combinedUserSongId  ="";
    public static final int NOTIFICATION_ID = 1;

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mInstance.initializeInstance();

        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
        backgroundPowerSaver = new BackgroundPowerSaver(this);

    }

    private void initializeInstance() {
        // TODO: Initialize Image Loader and other libraries here.
    }

    public void removeAllNotifications(){
        setNumUnreadMessages(0);
        setInboxStyle(new NotificationCompat.InboxStyle());
        notificationStatus = NotificationStatus.DEFAULT;
        setCombinedUserSongId("");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public static BrandstoreApplication getInstance() {
        return mInstance;
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "did enter region.");
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "did exit region.");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.d(TAG, "didDetermineStateForRegion.");
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
    // The following line should be changed to include the correct property id.
    private static final String PROPERTY_ID = "UA-60355049-1";

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : null;
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

}
