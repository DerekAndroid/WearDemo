package com.example.mm3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
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
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class NaviActivity extends Activity implements
        View.OnClickListener,
        DataApi.DataListener,
        MessageApi.MessageListener,
        NodeApi.NodeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public static final String TAG = "NaviActivity";
    public final Context mContext = NaviActivity.this;
    // client
    private GoogleApiClient mGoogleApiClient;

    // widgets
    private ImageView mIcon;
    private TextView mMsg;

    // handler
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navi_activity);

        /**
         * The first step in doing any Wear communication is to get a reference to the Wear API.
         * The GoogleApiClient class will make this very easy.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets();
            }
        });

    }

    private void setupWidgets(){
        mIcon = (ImageView) findViewById(R.id.indicator_icon);
        mMsg = (TextView) findViewById(R.id.indicator_msg);
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
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
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
                mHandler.post(new UpdateWidgetsRunnable(msg, icon_id));
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i(TAG, "DataEvent.TYPE_DELETED");
            }
        }

        googleApiClient.disconnect();
        Log.i(TAG, "- onDataChanged");
    }

    class UpdateWidgetsRunnable implements Runnable{
        String msg;
        int icon_id;
        public UpdateWidgetsRunnable(String msg, int icon_id){
            this.msg = msg;
            this.icon_id = icon_id;
        }
        @Override
        public void run() {
            mMsg.setText(msg);
            switch(icon_id){
                case 0:
                    mIcon.setImageResource(R.drawable.turn_left_128);
                    break;
                case 1:
                    mIcon.setImageResource(R.drawable.road_128);
                    break;
                case 2:
                    mIcon.setImageResource(R.drawable.turn_right_128);
                    break;
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onPeerConnected(Node node) {

    }

    @Override
    public void onPeerDisconnected(Node node) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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

    }
}
