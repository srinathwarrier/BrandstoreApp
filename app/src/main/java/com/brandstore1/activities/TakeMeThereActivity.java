package com.brandstore1.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.brandstore1.BrandstoreApplication;
import com.brandstore1.R;
import com.brandstore1.asynctasks.TakeMeThereAsyncTask;
import com.brandstore1.adapters.TakeMeThereAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Sonika on 8/16/2015.
 */
public class TakeMeThereActivity extends ActionBarActivity implements BeaconConsumer {

    private static final String TAG = TakeMeThereActivity.class.getSimpleName();
    ListView pathListView;
    ListView outletListView;
    ArrayList<String> pathArrayList = new ArrayList();
    ArrayList<String> outletArrayList = new ArrayList();
    Toolbar toolbar;
    Menu menu;
    EditText to;
    TakeMeThereAdapter takeMeThereAdapter;
    private EditText from;
    private ArrayAdapter<String> listAdapter;
    String to_id;
    String from_id;
    TextView emptyView;

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    String id3ForFirstBeacon = "49094";
    String id3ForSecondBeacon = "2186";
    private static int count1=0;
    private static int count2=0;
    private static int basicCount=-1;
    String fromString = "315", toString = "172", offer1String = "57", offer2String = "39";
    String offer1desc = "BATA - Upto 50% Off.";
    String offer2desc = "Levi's - Upto 50% Off.";
    int offer1image = R.drawable.beaconoffer1;
    int offer2image = R.drawable.beaconoffer2;
    boolean isBeaconServiceConnected = false;


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.i("Beacon", "didRangeBeaconsInRegion started " + basicCount++);
                if (beacons.size() > 0) {
                    Log.i("Beacon","didRangeBeaconsInRegion if(size>0)started basicCount:"+basicCount++);
                    Beacon firstBeacon = beacons.iterator().next();
                    String id3 = firstBeacon.getId3().toString();
                    double distance = firstBeacon.getDistance();
                    if(distance<3 && id3.equals(id3ForFirstBeacon) && count1==0){
                        sendNotification(offer1desc,
                                Integer.parseInt(id3),
                                offer1String,
                                offer1image);
                        try {
                            beaconManager.stopMonitoringBeaconsInRegion(region);
                            beaconManager.stopRangingBeaconsInRegion(region);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        count1++;
                    }
                    else if(distance<3 && id3.equals(id3ForSecondBeacon) && count2<10){
                        sendNotification(offer2desc,
                                Integer.parseInt(id3),
                                offer2String,
                                offer2image);
                        try {
                            beaconManager.stopMonitoringBeaconsInRegion(region);
                            beaconManager.stopRangingBeaconsInRegion(region);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        count2++;
                    }
                    Log.i("Beacon","didRangeBeaconsInRegion if(size>0)ended ended"+basicCount++);

                    //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                    //logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                }
                Log.i("Beacon","didRangeBeaconsInRegion ended"+basicCount++);
            }

        });

        isBeaconServiceConnected = true;

    }

    private void sendNotification(String message, int id, String outletId, int imageId) {
        Intent intent = new Intent(this, OutletDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("id", outletId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.brandstorelogoicon)
                .setContentTitle("Brandstore")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(
                        BitmapFactory.decodeResource(this.getResources(), imageId)))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    public enum TMT_type{
        TO_KNOWN , TO_UNKNOWN ,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_me_there);

        // Get tracker.
        Tracker t = ((BrandstoreApplication) getApplication()).getTracker(BrandstoreApplication.TrackerName.APP_TRACKER);
        // Send a screen view.
        t.setScreenName(TAG);
        t.send(new HitBuilders.ScreenViewBuilder().build());


        toolbar = (Toolbar) findViewById(R.id.takemetheretoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setTitle("Take Me There");

        emptyView = (TextView) findViewById(R.id.tmt_list_empty_textView);

        pathListView = (ListView) findViewById(R.id.tmt_list_view);
        pathListView.setEmptyView(emptyView);

        takeMeThereAdapter = new TakeMeThereAdapter(pathArrayList, this, toolbar, emptyView);
        pathListView.setAdapter(takeMeThereAdapter);

        Bundle bundle = getIntent().getExtras();
        TMT_type tmtType = (TMT_type)bundle.get("type");
        to = (EditText) findViewById(R.id.tmt_to);
        from = (EditText)findViewById(R.id.tmt_from);

        from.getBackground().setColorFilter(getResources().getColor(R.color.tmt_bg), PorterDuff.Mode.SRC_ATOP);
        to.getBackground().setColorFilter(getResources().getColor(R.color.tmt_bg), PorterDuff.Mode.SRC_ATOP);

        switch(tmtType){
            case TO_KNOWN:
                to_id = bundle.getString("id");
                to.setText(bundle.getString("name"));
                break;
            case TO_UNKNOWN:
                to.setEnabled(true);
                to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoSearchTMTActivityScreen(to,"Enter destination outlet");
                    }
                });

                break;
            default:
                break;
        }


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearchTMTActivityScreen(from,"Enter source outlet");

            }
        });

        count1=0;
        count2=0;

        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(data != null){
           if(requestCode==2) {
               String outlet_name = data.getStringExtra("outlet_name");
               String outlet_id = data.getStringExtra("outlet_id");
               String to_from = data.getStringExtra("TO_FROM");

               if(to_from.compareToIgnoreCase("tmt_from")== 0) {
                   from.setText(outlet_name);
                   from_id = outlet_id;
               }
               else if (to_from.compareToIgnoreCase("tmt_to")== 0) {
                   to.setText(outlet_name);
                   to_id = outlet_id;
               }
               if(from_id!=null && to_id!=null){
                   TakeMeThereAsyncTask pathListAsyncTask = new TakeMeThereAsyncTask(takeMeThereAdapter, to_id,from_id, emptyView, toolbar, this);
                   pathListAsyncTask.execute();

                   if(isBeaconServiceConnected && from_id.equals(fromString) && to_id.equals(toString)){ //TODO: Make generic
                       try {
                           //ArrayList idList = new ArrayList<Identifier>();
                           Region region1 = new Region("myIdentifier1", Identifier.parse("f0018b9b-7509-4c31-a905-1a27d39c003c"), Identifier.parse("11886"), Identifier.parse("2186"));
                           Region region2 = new Region("myIdentifier2", Identifier.parse("f0018b9b-7509-4c31-a905-1a27d39c003c"), Identifier.parse("5306"), Identifier.parse("49094"));
                           beaconManager.startRangingBeaconsInRegion(region1);
                           beaconManager.startRangingBeaconsInRegion(region2);
                           beaconManager.setForegroundScanPeriod(1100);
                           beaconManager.setForegroundBetweenScanPeriod(1100);
                           beaconManager.updateScanPeriods();
                       } catch (RemoteException e) {
                           Log.d(TAG,e.getMessage());
                       }
                   }
               }
           }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_take_me_there, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void gotoSearchTMTActivityScreen(EditText search, String hint){

        search.clearFocus();
        Intent intent = new Intent(getApplicationContext(), SearchTMTActivity.class);
        intent.putExtra("search", search.getTag().toString());
        intent.putExtra("hint", hint);
        startActivityForResult(intent, 2);


    }
}
