package com.example.mm3.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
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
    private static final boolean D = false;
    private static Bitmap bg;
    public static HUD.Data hud_data = null;
    private Context context = WearService.this;
<<<<<<< HEAD
=======

>>>>>>> Android Wear SDK 0.8.9
    public WearService() {
        if(D)Log.i(TAG, "+ WearService");
        if(D)Log.i(TAG, "- WearService");
    }

    @Override
    public void onCreate() {
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.notification_bg);
        hud_data = new HUD.Data();
        super.onCreate();
    }

    private void buildNotification(HUD.Data data , boolean withDismissal){
<<<<<<< HEAD
        HUD.ACTION action = HUD.ACTION.values()[data.direction - 1];
=======
        if(D)Log.i(TAG, "+ buildNotification");
        if(D)Log.i(TAG,data.toString());

        HUD.ACTION action = HUD.getAction(data.direction);
>>>>>>> Android Wear SDK 0.8.9
        // 參考 Notifications 範例
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(action.getString())
                .setContentText(data.toString())
                        // Set a content intent to return to this sample
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, WearActivity.class), 0))
                .setSmallIcon(R.drawable.ic_launcher)    // 註解後不會顯示?
                .extend(new Notification.WearableExtender()
                        .setBackground(bg)
                        .setHintHideIcon(true)  // 隱藏右上角圖示
                        .setContentIcon(R.drawable.generic_confirmation_00180));

//        switch(icon_id){
//            case 0:
//                builder.setSmallIcon(R.drawable.turn_left_128);
//                //builder.extend(new NotificationCompat.WearableExtender().setContentIcon(R.drawable.turn_left_128));
//                break;
//            case 1:
//                builder.setSmallIcon(R.drawable.road_128);
//                //builder.extend(new WearableExtender().setContentIcon(R.drawable.road_128));
//                break;
//            case 2:
//                builder.setSmallIcon(R.drawable.turn_right_128);
//                //builder.extend(new WearableExtender().setContentIcon(R.drawable.turn_right_128));
//                break;
//        }

        if (withDismissal) {
            // 移除 Notification 的時候執行 PendingIntent
            Intent dismissIntent = new Intent(HUD.ACTION_DISMISS);
            dismissIntent.putExtra(HUD.KEY_NOTIFICATION_ID, HUD.NOTIFY_ID);
            PendingIntent pendingIntent = PendingIntent
                    .getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setDeleteIntent(pendingIntent);
        }

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(HUD.NOTIFY_ID, builder.build());
<<<<<<< HEAD
=======
        if(D)Log.i(TAG, "- buildNotification");
>>>>>>> Android Wear SDK 0.8.9
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        if(D)Log.i(TAG, "+ onDataChanged");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult = googleApiClient.blockingConnect(100,
                TimeUnit.MILLISECONDS);
        if (!connectionResult.isSuccess()) {
            if(D)Log.e(TAG, "Service failed to connect to GoogleApiClient.");
            return;
        }

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = event.getDataItem();
                // 通常在開發過程中是使用DataMap類實現DataItem接口，類似Bundle鍵值對的存儲方式
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
<<<<<<< HEAD
                HUD.Data data = new HUD.Data();
                data.direction      =  dataMap.getInt(HUD.KEY_DIRECTION);
                data.speed          =  dataMap.getInt(HUD.KEY_SPEED);
                data.speed_limit    =  dataMap.getInt(HUD.KEY_SPEED_LIMIT);
                data.distance       =  dataMap.getInt(HUD.KEY_DISTANCE);
                data.indicator      =  dataMap.getInt(HUD.KEY_INDICATOR);
                hud_data = data;
                Log.i(TAG, data.toString());
                buildNotification(data , true);
=======
                hud_data.direction      =  dataMap.getInt(HUD.KEY_DIRECTION);
                hud_data.speed          =  dataMap.getInt(HUD.KEY_SPEED);
                hud_data.speed_limit    =  dataMap.getInt(HUD.KEY_SPEED_LIMIT);
                hud_data.distance       =  dataMap.getInt(HUD.KEY_DISTANCE);
                hud_data.indicator      =  dataMap.getInt(HUD.KEY_INDICATOR);
                buildNotification(hud_data , true);
>>>>>>> Android Wear SDK 0.8.9
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                if(D)Log.i(TAG, "DataEvent.TYPE_DELETED");
            }
        }

        googleApiClient.disconnect();
        if(D)Log.i(TAG, "- onDataChanged");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if(D)Log.i(TAG, "+ onMessageReceived");
        if(D)Log.i(TAG, "- onMessageReceived");
    }
}
