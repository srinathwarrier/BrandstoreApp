package com.brandstore1.utils;

import android.content.Context;

import com.brandstore1.BrandstoreApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Ravi on 03-Oct-15.
 */
public class GAUtils {
    private static final String TAG = GAUtils.class.getSimpleName();

    public static void sendEvent(Context context, String category, String action, String label, long value) {
        BrandstoreApplication app = BrandstoreApplication.getInstance();
        Tracker t = app.getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();

        if (category != null) {
            builder.setCategory(category);
        }
        if (action != null) {
            builder.setAction(action);
        }
        if (label != null) {
            builder.setLabel(label);
        }
        if (value > Integer.MIN_VALUE) {
            builder.setValue(value);
        }

        t.send(builder.build());
    }

    public static void sendEvent(Context context, String category, String action) {
        sendEvent(context, category, action, null, Integer.MIN_VALUE);
    }

    public static void sendEvent(Context context, String category, String action, String label) {
        sendEvent(context, category, action, label, Integer.MIN_VALUE);
    }

    public static void sendEvent(Context context, String category, String action, long value) {
        sendEvent(context, category, action, null, value);
    }
}
