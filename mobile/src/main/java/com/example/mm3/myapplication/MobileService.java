package com.example.mm3.myapplication;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MobileService extends Service implements
        DataApi.DataListener,
        MessageApi.MessageListener,
        NodeApi.NodeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        RCInterfaceReceiver.FromRCInterface{
    public static final String TAG = "MobileService";
    public RCInterfaceReceiver mRCInterfaceReceiver = null;
    private GoogleApiClient mGoogleApiClient;

    public MobileService() {
        Log.i(TAG, "+ MobileService");
        Log.i(TAG, "- MobileService");
    }

    public void setForeground(){
        Notification mNotification = new Notification.Builder(getApplicationContext())
                .setContentTitle("MobileService")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_launcher)
                        //.setLargeIcon(aBitmap)
                .setTicker("Running...")
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        mNotification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startForeground(0x7788, mNotification);
    }

    private void init(){
        Log.i(TAG, "init()");
        /**
         * The first step in doing any Wear communication is to get a reference to the Wear API.
         * The GoogleApiClient class will make this very easy.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //RCInterfaceReceiver
        mRCInterfaceReceiver = new RCInterfaceReceiver(this, this);
        //get init.
        RCInterfaceReceiver.req_enable(MobileService.this, true);//enable this interface with RC.
        RCInterfaceReceiver.req_alive(MobileService.this);//check if "RPC to 2-Din" is alive.

        setForeground();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "+ onCreate");
        super.onCreate();
<<<<<<< HEAD
        //RCInterfaceReceiver
        mRCInterfaceReceiver = new RCInterfaceReceiver(this, this);
        //get init.
        RCInterfaceReceiver.req_enable(MobileService.this, true);//enable this interface with RC.
        RCInterfaceReceiver.req_alive(MobileService.this);//check if "RPC to 2-Din" is alive.
=======
        init();
>>>>>>> Android Wear SDK 0.8.9
        Log.i(TAG, "+ onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
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

        //Google Client Interface
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        Log.i(TAG, "- onDestroy");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i(TAG, "+ onDataChanged");
//        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
//        dataEvents.close();
//
//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Wearable.API)
//                .build();
//
//        ConnectionResult connectionResult = googleApiClient.blockingConnect(100,
//                TimeUnit.MILLISECONDS);
//        if (!connectionResult.isSuccess()) {
//            Log.e(TAG, "MobileService failed to connect to GoogleApiClient.");
//            return;
//        }
//
//        for (DataEvent event : events) {
//            if (event.getType() == DataEvent.TYPE_CHANGED) {
//                DataItem dataItem = event.getDataItem();
//                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
//                String msg = dataMap.getString(Constants.KEY_MESSAGE);
//                Log.i(TAG, "Get: " + msg);
//            } else if (event.getType() == DataEvent.TYPE_DELETED) {
//
//            }
//        }
//
//        googleApiClient.disconnect();

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem dataItem = event.getDataItem();
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                String msg = dataMap.getString(Constants.KEY_MESSAGE);
                Log.i(TAG, "Get: " + msg);
            } else if (event.getType() == DataEvent.TYPE_DELETED) {

            }
        }
        Log.i(TAG, "- onDataChanged");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        try {
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
        //RCInterfaceReceiver.doFunctionKeyEvent(MobileService.this, keyCode, false);
        switch(keyCode){
            case PanelKeyEvent.PanelKey_AuxIn:
                RCInterfaceReceiver.startUI(MobileService.this, MirroringUiID.AuxIn);
                break;
            case PanelKeyEvent.PanelKey_Radio:
                RCInterfaceReceiver.startUI(MobileService.this, MirroringUiID.Radio);
                break;
            case PanelKeyEvent.PanelKey_Music:
                RCInterfaceReceiver.startUI(MobileService.this, MirroringUiID.USB_MP3);
                break;
            case PanelKeyEvent.PanelKey_Phone:
                RCInterfaceReceiver.startUI(MobileService.this, MirroringUiID.BT_MP3);
                break;
            case PanelKeyEvent.PanelKey_NAVI:
                //RCInterfaceReceiver.startUI(MainActivity.this, MirroringUiID.None);
                break;
            default:
                RCInterfaceReceiver.doPanelKeyEvent(MobileService.this, keyCode, false);
                break;
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "+ onConnected: " + bundle);
        // Now you can use the data layer API
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
        Log.i(TAG, "- onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "+ onConnectionSuspended: " + i);

        Log.i(TAG, "- onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "+ onConnectionFailed: " + connectionResult);

        Log.i(TAG, "- onConnectionFailed");
    }

    @Override
    public void onPeerConnected(Node peer) {

    }

    @Override
    public void onPeerDisconnected(Node peer) {

    }

    /********************************
     * create and sync(send) data map
     ********************************/
    public void notifyWear(HUD.Data data){
        //創建PutDataMapRequest對象，為DataItem設置path值(建立請求以 '/' 開頭,區別不同的DataItem)
        PutDataMapRequest mPutDataMapRequest = PutDataMapRequest.create(HUD.KEY_PATH);
        // 通常在開發過程中是使用DataMap類實現DataItem接口，類似Bundle鍵值對的存儲方式
        DataMap mDataMap = mPutDataMapRequest.getDataMap();
        // 使用put…()方法為DataMap設置需要的數據
        mDataMap.putInt(HUD.KEY_DIRECTION       , data.direction);
        mDataMap.putInt(HUD.KEY_SPEED           , data.speed);
        mDataMap.putInt(HUD.KEY_SPEED_LIMIT     , data.speed_limit);
        mDataMap.putInt(HUD.KEY_DISTANCE        , data.distance);
        mDataMap.putInt(HUD.KEY_INDICATOR       , data.indicator);
        // 傳遞圖檔..etc
        //Asset asset = createAssetFromBitmap(bg);
        //mDataMap.putAsset(Constants.KEY_ASSET, asset);
        // 調用PutDataMapRequest.asPutDataRequest()創建PutDataRequest對象
        PutDataRequest mPutDataRequest = mPutDataMapRequest.asPutDataRequest();
        // 調用DataApi.putDataItem()請求系統創建DataItem
        PendingResult<DataApi.DataItemResult> result = Wearable.DataApi.putDataItem(mGoogleApiClient, mPutDataRequest);
        result.setResultCallback(new ResultCallback<DataApi.DataItemResult>(){
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                if(!dataItemResult.getStatus().isSuccess()){
                    Log.e(TAG, "Failed to send message with status code:"
                            + dataItemResult.getStatus().getStatusCode());
                }else{
                    Log.i(TAG, "OK!");
                }
            }
        });
    }

    //+F1 INTERFACE
<<<<<<< HEAD
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
=======
    boolean mRCInterfaceEnable = false;
    boolean mIsAliveRPC = false;

    @Override
    public void notifyServiceReady(boolean bReady, boolean bRPCisWork, int version) {
        Log.i(TAG, "+ notifyServiceReady");
        Log.d(TAG, String.format("REDY: %b , RPC: %b , VER: %d",
                bReady, bRPCisWork, version));
        if( bReady ){
            RCInterfaceReceiver.req_enable(MobileService.this, true);//enable this interface with RC.
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
>>>>>>> Android Wear SDK 0.8.9
        Log.i(TAG, "- ack_alive");
    }

    @Override
    public void ack_getSysInf(String[] sysInf) {
        Log.i(TAG, "+ ack_getSysInf");
<<<<<<< HEAD
=======
        ShowLog("ack_getSysInf: sysInf=" + sysInf);
        for(int i=0; i<sysInf.length; i++){
            ShowLog("ack_getSysInf: sysInf_" + i + ". " + sysInf[i]);
        }
>>>>>>> Android Wear SDK 0.8.9
        Log.i(TAG, "- ack_getSysInf");
    }

    @Override
    public void ack_doShellCmd(String origCmd, String result) {
        Log.i(TAG, "+ ack_doShellCmd");
<<<<<<< HEAD
=======
        ShowLog("ack_doShellCmd: origCmd=" + origCmd);
        ShowLog("ack_doShellCmd: result=" + result);
>>>>>>> Android Wear SDK 0.8.9
        Log.i(TAG, "- ack_doShellCmd");
    }

    @Override
    public void notifyHUD(int direction, int DrvDistance, int DrvSpeed, int speedLimit, int speedCamera) {
        Log.i(TAG, "+ notifyHUD");
<<<<<<< HEAD
=======
        ShowLog("notifyHUD: direction=" + direction);
        ShowLog("notifyHUD: DrvDistance=" + DrvDistance);
        ShowLog("notifyHUD: DrvSpeed=" + DrvSpeed);
        ShowLog("notifyHUD: speedLimit=" + speedLimit);
        ShowLog("notifyHUD: speedCamera=" + speedCamera);

>>>>>>> Android Wear SDK 0.8.9
        HUD.Data data = new HUD.Data();
        data.direction = direction;
        data.distance = DrvDistance;
        data.speed = DrvSpeed;
        data.speed_limit = speedLimit;
        data.indicator = speedCamera;
<<<<<<< HEAD
        System.out.print(data.toString());
        Log.i(TAG, "- notifyHUD");
=======
        notifyWear(data);
        Log.i(TAG, "- notifyHUD");
    }

    private void ShowLog(String msg){
        Log.e(TAG, msg);
>>>>>>> Android Wear SDK 0.8.9
    }
    //-F1 INTERFACE
}
