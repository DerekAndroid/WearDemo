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
        //get init.
        RCInterfaceReceiver.req_enable(MobileService.this, true);//enable this interface with RC.
        RCInterfaceReceiver.req_alive(MobileService.this);//check if "RPC to 2-Din" is alive.
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
    public void notifyServiceReady(boolean bReady) {
        Log.i(TAG, "+ notifyServiceReady");
        Log.i(TAG, "- notifyServiceReady");
    }

    @Override
    public void notifyServiceReady(boolean bReady, boolean bRPCisWork, int version) {
        Log.i(TAG, "+ notifyServiceReady");
        Log.d(TAG, String.format("REDY: %b , RPC: %b , VER: %d",
                bReady, bRPCisWork, version));
        Log.i(TAG, "- notifyServiceReady");
    }

    @Override
    public void notifyTPMS(int id, int pressure, int tempture, int noSignal, int status, int leakGas) {
        Log.i(TAG, "+ notifyTPMS");
        Log.d(TAG, String.format("ID: %d , PRES: %d , TEMP: %d , NOSI: %d , STAT: %d , LEGA: %d",
                id, pressure, tempture, noSignal, status, leakGas));
        Log.i(TAG, "- notifyTPMS");
    }

    @Override
    public int notifyAllTPMS(int[] pressure, int[] tempture, int[] noSignal, int[] status, int[] leakGas) {
        Log.i(TAG, "+ notifyAllTPMS");
        Log.i(TAG, "- notifyAllTPMS");
        return 0;
    }

    @Override
    public void ack_enable(boolean bEnable) {
        Log.i(TAG, "+ ack_enable");
        Log.d(TAG, String.format("ENA: %b", bEnable));
        Log.i(TAG, "- ack_enable");
    }

    @Override
    public void ack_alive(boolean bRPCisWork) {
        Log.i(TAG, "+ ack_alive");
        Log.d(TAG, String.format("RPC: %b", bRPCisWork));
        Log.i(TAG, "- ack_alive");
    }

    @Override
    public void ack_getSysInf(String[] sysInf) {
        Log.i(TAG, "+ ack_getSysInf");
        Log.i(TAG, "- ack_getSysInf");
    }

    @Override
    public void ack_doShellCmd(String origCmd, String result) {
        Log.i(TAG, "+ ack_doShellCmd");
        Log.i(TAG, "- ack_doShellCmd");
    }

    @Override
    public void notifyHUD(int direction, int DrvDistance, int DrvSpeed, int speedLimit, int speedCamera) {
        Log.i(TAG, "+ notifyHUD");
        HUD.Data data = new HUD.Data();
        data.direction = direction;
        data.distance = DrvDistance;
        data.speed = DrvSpeed;
        data.speed_limit = speedLimit;
        data.indicator = speedCamera;
        System.out.print(data.toString());
        Log.i(TAG, "- notifyHUD");
    }
    //-F1 INTERFACE
}
