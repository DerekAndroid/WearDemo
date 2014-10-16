package com.example.mm3.myapplication;

import android.app.Notification;
import android.content.Intent;
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
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MobileWearService extends WearableListenerService implements RCInterfaceReceiver.FromRCInterface{
    public static final String TAG = "MobileWearService";
    public RCInterfaceReceiver mRCInterfaceReceiver = null;

    public MobileWearService() {
        Log.i(TAG, "+ MobileWearService");
        Log.i(TAG, "- MobileWearService");
    }

    public void setForeground(){
        Notification mNotification = new Notification.Builder(getApplicationContext())
                .setContentTitle("Wear Sync")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_launcher)
                        //.setLargeIcon(aBitmap)
                .setTicker("Running...")
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        mNotification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startForeground(0x5566, mNotification);
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "+ onCreate");
        super.onCreate();
        //RCInterfaceReceiver
        mRCInterfaceReceiver = new RCInterfaceReceiver(this, this);
        //get init.
        RCInterfaceReceiver.req_enable(MobileWearService.this, true);//enable this interface with RC.
        RCInterfaceReceiver.req_alive(MobileWearService.this);//check if "RPC to 2-Din" is alive.

        setForeground();
        Log.i(TAG, "+ onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "+ onDestroy");
        super.onDestroy();
        //RCInterfaceReceiver
        if( mRCInterfaceReceiver != null ){
            RCInterfaceReceiver.req_enable(MobileWearService.this, false);//disable
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
            Log.e(TAG, "MobileWearService failed to connect to GoogleApiClient.");
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
        if( !mRCInterfaceEnable ){
            ShowLog("Please enable RCInterface first. mRCInterfaceEnable=" + mRCInterfaceEnable);
            return;
        }
        if( !mIsAliveRPC ){
            ShowLog("Please check if RPC is alive. mIsAliveRPC=" + mIsAliveRPC);
            return;
        }
        //RCInterfaceReceiver.doFunctionKeyEvent(MobileWearService.this, keyCode, false);
        switch(keyCode){
            case PanelKeyEvent.PanelKey_AuxIn:
                RCInterfaceReceiver.startUI(MobileWearService.this, MirroringUiID.AuxIn);
                break;
            case PanelKeyEvent.PanelKey_Radio:
                RCInterfaceReceiver.startUI(MobileWearService.this, MirroringUiID.Radio);
                break;
            case PanelKeyEvent.PanelKey_Music:
                RCInterfaceReceiver.startUI(MobileWearService.this, MirroringUiID.USB_MP3);
                break;
            case PanelKeyEvent.PanelKey_Phone:
                RCInterfaceReceiver.startUI(MobileWearService.this, MirroringUiID.BT_MP3);
                break;
            case PanelKeyEvent.PanelKey_NAVI:
                //RCInterfaceReceiver.startUI(MainActivity.this, MirroringUiID.None);
                break;
            default:
                RCInterfaceReceiver.doPanelKeyEvent(MobileWearService.this, keyCode, false);
                break;
        }

    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        super.onPeerDisconnected(peer);
    }

    //+F1 INTERFACE
    boolean mRCInterfaceEnable = false;
    boolean mIsAliveRPC = false;

    @Override
    public void notifyServiceReady(boolean bReady, boolean bRPCisWork, int version) {
        Log.i(TAG, "+ notifyServiceReady");
        Log.d(TAG, String.format("REDY: %b , RPC: %b , VER: %d",
                bReady, bRPCisWork, version));
        if( bReady ){
            RCInterfaceReceiver.req_enable(MobileWearService.this, true);//enable this interface with RC.
        }else{
            mRCInterfaceEnable = false;
        }
        mIsAliveRPC = bRPCisWork;
        Log.i(TAG, "- notifyServiceReady");
    }

    @Override
    public void notifyTPMS(int id, int pressure, int tempture, int noSignal, int status, int leakGas) {
        Log.i(TAG, "+ notifyTPMS");
        //Log.d(TAG, String.format("ID: %d , PRES: %d , TEMP: %d , NOSI: %d , STAT: %d , LEGA: %d",
        //        id, pressure, tempture, noSignal, status, leakGas));
        ShowLog("notifyHUD: id=" + id);
        ShowLog("notifyHUD: pressure=" + pressure);
        ShowLog("notifyHUD: tempture=" + tempture);
        ShowLog("notifyHUD: noSignal=" + noSignal);
        ShowLog("notifyHUD: status=" + status);
        ShowLog("notifyHUD: leakGas=" + leakGas);
        Log.i(TAG, "- notifyTPMS");
    }

    @Override
    public int notifyAllTPMS(int[] pressure, int[] tempture, int[] noSignal, int[] status, int[] leakGas) {
        Log.i(TAG, "+ notifyAllTPMS");
        ShowLog("notifyHUD: pressure=" + pressure[0] + ", " + pressure[1] + ", " + pressure[2] + ", " + pressure[3]);
        ShowLog("notifyHUD: tempture=" + tempture[0] + ", " + tempture[1] + ", " + tempture[2] + ", " + tempture[3]);
        ShowLog("notifyHUD: noSignal=" + noSignal[0] + ", " + noSignal[1] + ", " + noSignal[2] + ", " + noSignal[3]);
        ShowLog("notifyHUD: status=" + status[0] + ", " + status[1] + ", " + status[2] + ", " + status[3]);
        ShowLog("notifyHUD: leakGas=" + leakGas[0] + ", " + leakGas[1] + ", " + leakGas[2] + ", " + leakGas[3]);
        Log.i(TAG, "- notifyAllTPMS");
        return 0;
    }

    @Override
    public void ack_enable(boolean bEnable) {
        Log.i(TAG, "+ ack_enable");
        Log.d(TAG, String.format("ENA: %b", bEnable));
        mRCInterfaceEnable = bEnable;
        Log.i(TAG, "- ack_enable");
    }

    @Override
    public void ack_alive(boolean bRPCisWork) {
        Log.i(TAG, "+ ack_alive");
        Log.d(TAG, String.format("RPC: %b", bRPCisWork));
        mIsAliveRPC = bRPCisWork;
        Log.i(TAG, "- ack_alive");
    }

    @Override
    public void ack_getSysInf(String[] sysInf) {
        Log.i(TAG, "+ ack_getSysInf");
        ShowLog("ack_getSysInf: sysInf=" + sysInf);
        for(int i=0; i<sysInf.length; i++){
            ShowLog("ack_getSysInf: sysInf_" + i + ". " + sysInf[i]);
        }
        Log.i(TAG, "- ack_getSysInf");
    }

    @Override
    public void ack_doShellCmd(String origCmd, String result) {
        Log.i(TAG, "+ ack_doShellCmd");
        ShowLog("ack_doShellCmd: origCmd=" + origCmd);
        ShowLog("ack_doShellCmd: result=" + result);
        Log.i(TAG, "- ack_doShellCmd");
    }

    @Override
    public void notifyHUD(int direction, int DrvDistance, int DrvSpeed, int speedLimit, int speedCamera) {
        Log.i(TAG, "+ notifyHUD");
        ShowLog("notifyHUD: direction=" + direction);
        ShowLog("notifyHUD: DrvDistance=" + DrvDistance);
        ShowLog("notifyHUD: DrvSpeed=" + DrvSpeed);
        ShowLog("notifyHUD: speedLimit=" + speedLimit);
        ShowLog("notifyHUD: speedCamera=" + speedCamera);

//        HUD.Data data = new HUD.Data();
//        data.direction = direction;
//        data.distance = DrvDistance;
//        data.speed = DrvSpeed;
//        data.speed_limit = speedLimit;
//        data.indicator = speedCamera;
//        System.out.print(data.toString());
        Log.i(TAG, "- notifyHUD");
    }

    private void ShowLog(String msg){
        Log.e(TAG, msg);
    }
    //-F1 INTERFACE
}
