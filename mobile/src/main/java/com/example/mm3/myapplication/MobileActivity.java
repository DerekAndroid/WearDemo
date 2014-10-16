package com.example.mm3.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
<<<<<<< HEAD
=======
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
>>>>>>> Android Wear SDK 0.8.9
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
<<<<<<< HEAD
import java.util.Random;
=======
>>>>>>> Android Wear SDK 0.8.9
import java.util.Date;

public class MobileActivity extends Activity implements
        View.OnClickListener,
        DataApi.DataListener,
        MessageApi.MessageListener,
        NodeApi.NodeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        RCInterfaceReceiver.FromRCInterface{
    private Context mContext = MobileActivity.this;
    public RCInterfaceReceiver mRCInterfaceReceiver = null;
    private GoogleApiClient mGoogleApiClient;
    String TAG = "MobileActivity";
    TextView sendTextView;
    TextView recvTextView;
<<<<<<< HEAD
    Button randButton;
    private static Bitmap bg;
    private Handler mHandler = new Handler();
=======
    Button randHudButton;
    Button randTpmsButton;
    private static Bitmap bg;
    private Handler mHandler = new Handler();


>>>>>>> Android Wear SDK 0.8.9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        /**
         * The first step in doing any Wear communication is to get a reference to the Wear API.
         * The GoogleApiClient class will make this very easy.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        randHudButton.setOnClickListener(this);
        randTpmsButton.setOnClickListener(this);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.notification_bg);

//        //RCInterfaceReceiver
//        mRCInterfaceReceiver = new RCInterfaceReceiver(this, this);
//        //get init.
//        RCInterfaceReceiver.req_enable(MobileActivity.this, true);//enable this interface with RC.
//        RCInterfaceReceiver.req_alive(MobileActivity.this);//check if "RPC to 2-Din" is alive.

        //啟動服務
        Intent intent = new Intent(MobileActivity.this, MobileService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        //RCInterfaceReceiver
//        if( mRCInterfaceReceiver != null ){
//            RCInterfaceReceiver.req_enable(MobileActivity.this, false);//disable
//            mRCInterfaceReceiver.Close();
//            mRCInterfaceReceiver = null;
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        sendTextView = (TextView) findViewById(R.id.send_textView);
        recvTextView = (TextView) findViewById(R.id.recv_textView);
<<<<<<< HEAD
        randButton = (Button) findViewById(R.id.rand_button);
=======
        randHudButton = (Button) findViewById(R.id.rand_hud_button);
        randTpmsButton = (Button) findViewById(R.id.rand_tpms_button);
>>>>>>> Android Wear SDK 0.8.9
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
<<<<<<< HEAD
            case R.id.rand_button:
                HUD.Data data = geneData();
                sendTextView.setText(data.toString());

                notifyWear(data);
                //notifyMobile(data);
=======
            case R.id.rand_hud_button:
                HUD.Data data = HUD.Data.geneData();
                sendTextView.setText(data.toString());
                notifyWear(data);
                //notifyMobile(data);
                break;
            case R.id.rand_tpms_button:

>>>>>>> Android Wear SDK 0.8.9
                break;
        }
    }

    /********************************
     * send notification to local
     ********************************/
    public static final int NOTIFICATION_ID = 0x7788;
    public void notifyMobile(HUD.Data data){
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, MobileActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        HUD.ACTION action = HUD.ACTION.values()[data.direction - 1];

        String longText = action.getString();
        // Build notification
        // Actions are just fake
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.common_signin_btn_text_focus_light);
        Notification noti = new Notification.Builder(this)
                .setLargeIcon(bm)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(new Date().toGMTString())
                .setContentText("Subject")
                .setContentIntent(pIntent).setAutoCancel(true)
                .addAction(R.drawable.ic_launcher, "Call", pIntent)
                .addAction(R.drawable.ic_launcher, "More", pIntent)
                .addAction(R.drawable.ic_launcher, "And more", pIntent)
                .setStyle(new Notification.BigTextStyle().bigText(longText))
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Hide the notification after its selected

        notificationManager.notify(NOTIFICATION_ID, noti);

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
<<<<<<< HEAD
        Wearable.DataApi.putDataItem(mGoogleApiClient, mPutDataRequest);
    }

    public HUD.Data geneData(){
        HUD.Data data = new HUD.Data();

        int totalAction = HUD.ACTION.values().length;

        int randAct = new Random().nextInt(totalAction);    // 隨機挑選認一轉向
        int randDist = new Random().nextInt(14) * 50 + 300; // 隨機產生轉向距離
        int randSpeed = new Random().nextInt(16) * 5 + 50;   // 隨機產生當前速度
        int randSpeedLim = new Random().nextInt(6) * 10 + 70;// 隨機產生當前速度

        // get direction from random action
        HUD.ACTION action = HUD.ACTION.values()[randAct];
        data.direction = action.getValue();
        data.distance = randDist;
        data.speed = randSpeed;
        data.speed_limit = randSpeedLim;
        data.indicator = new Random().nextInt(1);
        System.out.print(data.toString());
        return data;
    }
=======
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


>>>>>>> Android Wear SDK 0.8.9

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
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
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i(TAG, "+ onDataChanged");

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d(TAG, "DataItem deleted: " + event.getDataItem().getUri());
            } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.d(TAG, "DataItem changed: " + event.getDataItem().getUri());
            }
        }
        Log.i(TAG, "- onDataChanged");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "+ onConnectionFailed: " + connectionResult);

        Log.i(TAG, "- onConnectionFailed");
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        Log.i(TAG, "+ onMessageReceived");
        Log.i(TAG, "get: " + messageEvent.getPath());
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                recvTextView.setText(messageEvent.getPath());
            }
        },100);
        Log.i(TAG, "- onMessageReceived");
    }

    @Override
    public void onPeerConnected(Node node) {
        Log.i(TAG, "+ onPeerConnected");

        Log.i(TAG, "- onPeerConnected");
    }

    @Override
    public void onPeerDisconnected(Node node) {
        Log.i(TAG, "+ onPeerDisconnected");

        Log.i(TAG, "- onPeerDisconnected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Log.i(TAG, "- notifyHUD");
    }
    //-F1 INTERFACE
}
