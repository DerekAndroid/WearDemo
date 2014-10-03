package com.example.mm3.myapplication;

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

public class MobileService extends WearableListenerService implements RCInterfaceReceiver.FromRCInterface{
    public static final String TAG = "MobileService";
    public RCInterfaceReceiver mRCInterfaceReceiver = null;

    public MobileService() {
        Log.i(TAG, "+ MobileService");
        Log.i(TAG, "- MobileService");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "+ onCreate");
        super.onCreate();
        //RCInterfaceReceiver
        mRCInterfaceReceiver = new RCInterfaceReceiver(this, this);
        RCInterfaceReceiver.req_enable(MobileService.this, true);//enable
        Log.i(TAG, "+ onCreate");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "+ onDestroy");
        super.onDestroy();
        //RCInterfaceReceiver
        if( mRCInterfaceReceiver != null ){
            RCInterfaceReceiver.req_enable(MobileService.this, false);//disable
            mRCInterfaceReceiver.Close();
            mRCInterfaceReceiver = null;
        }
        Log.i(TAG, "- onDestroy");
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
            Log.e(TAG, "MobileService failed to connect to GoogleApiClient.");
            return;
        }

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = event.getDataItem();
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                String msg = dataMap.getString(Constants.KEY_MESSAGE);
                Log.i(TAG, "Get: " + msg);
            } else if (event.getType() == DataEvent.TYPE_DELETED) {

            }
        }

        googleApiClient.disconnect();
        Log.i(TAG, "- onDataChanged");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        try {
            super.onMessageReceived(messageEvent);
            Log.i(TAG, "+ onMessageReceived");
            int cmd = Integer.parseInt(messageEvent.getPath());
            Log.i(TAG, "Get CMD: " + cmd);
            sendKey(cmd);
            Log.i(TAG, "- onMessageReceived");
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    public void sendKey(int keyCode){
        //RCInterfaceReceiver.doFunctionKeyEvent(MobileService.this, keyCode, false);
        RCInterfaceReceiver.doPanelKeyEvent(MobileService.this, keyCode, false);
    }

    //+F1 INTERFACE
    @Override
    public void notifyServiceReady(boolean bReady) {

    }

    @Override
    public void ack_enable(boolean bEnable) {

    }

    @Override
    public void ack_alive(boolean bRPCisWork) {

    }

    @Override
    public void ack_getSysInf(String[] sysInf) {

    }

    @Override
    public void ack_doShellCmd(String origCmd, String result) {

    }

    @Override
    public void notifyHUD(int direction, int DrvDistance, int DrvSpeed, int speedLimit, int speedCamera) {

    }
    //-F1 INTERFACE
}
