package com.example.mm3.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WearService extends WearableListenerService {
    public static final String TAG = "WearService";
    private static Bitmap bg;

    public WearService() {
        Log.i(TAG, "+ WearService");
        Log.i(TAG, "- WearService");
    }

    @Override
    public void onCreate() {
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.notification_bg);
        super.onCreate();
    }

    private void buildNotification(
        String title, String content, int icon_id, Asset asset, boolean withDismissal){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //.extend(new WearableExtender().setBackground(bg))
                .setContentTitle(title)
                .setContentText(content)
                .addAction(R.drawable.generic_confirmation_00180,
                        "GOGO!", null);

        switch(icon_id){
            case 0:
                builder.setSmallIcon(R.drawable.turn_left_128);
                //builder.extend(new NotificationCompat.WearableExtender().setContentIcon(R.drawable.turn_left_128));
                break;
            case 1:
                builder.setSmallIcon(R.drawable.road_128);
                //builder.extend(new WearableExtender().setContentIcon(R.drawable.road_128));
                break;
            case 2:
                builder.setSmallIcon(R.drawable.turn_right_128);
                //builder.extend(new WearableExtender().setContentIcon(R.drawable.turn_right_128));
                break;
        }

        if (withDismissal) {
            // 移除 Notification 的時候執行 PendingIntent
            Intent dismissIntent = new Intent(Constants.ACTION_DISMISS);
            dismissIntent.putExtra(Constants.KEY_NOTIFICATION_ID, Constants.NOTIFY_ID);
            PendingIntent pendingIntent = PendingIntent
                    .getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setDeleteIntent(pendingIntent);
        }

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(Constants.NOTIFY_ID, builder.build());
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.i(TAG, "+ onDataChanged");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult = googleApiClient.blockingConnect(100,
                TimeUnit.MILLISECONDS);
        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Service failed to connect to GoogleApiClient.");
            return;
        }

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                DataItem dataItem = event.getDataItem();
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                String msg = dataMap.getString(Constants.KEY_MESSAGE);
                int icon_id = dataMap.getInt(Constants.KEY_ICON_ID);
                Asset asset = dataMap.getAsset(Constants.KEY_ASSET);
                Log.i(TAG, "Get: " + icon_id + "/" + msg);
                buildNotification("Attention!", msg, icon_id, asset, true);
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i(TAG, "DataEvent.TYPE_DELETED");
            }
        }

        googleApiClient.disconnect();
        Log.i(TAG, "- onDataChanged");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.i(TAG, "+ onMessageReceived");
        Log.i(TAG, "- onMessageReceived");
    }
}
