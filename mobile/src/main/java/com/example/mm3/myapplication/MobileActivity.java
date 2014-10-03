package com.example.mm3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
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
import java.util.Random;


public class MobileActivity extends Activity implements
        View.OnClickListener,
        DataApi.DataListener,
        MessageApi.MessageListener,
        NodeApi.NodeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private Context mContext = MobileActivity.this;
    private GoogleApiClient mGoogleApiClient;
    String TAG = "MobileActivity";
    TextView randTextView;
    Button randButton;
    private static Bitmap bg;

    static final String RAND_MSG = "前方 %d 公尺請\n%s ";
    static final String mDirection[] = {
      "左轉",
      "直走",
      "右轉"
    };

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

        randButton.setOnClickListener(this);

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.notification_bg);
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
        randTextView = (TextView) findViewById(R.id.rand_textView);
        randButton = (Button) findViewById(R.id.rand_button);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rand_button:
                int dist = new Random().nextInt(2000);
                int dir = new Random().nextInt(3);
                String msg = String.format(RAND_MSG, dist, mDirection[dir]);
                randTextView.setText(msg);

                // create and sync(send) data map

                //創建PutDataMapRequest對象，為DataItem設置path值(建立請求以 '/' 開頭,區別不同的DataItem)
                PutDataMapRequest mPutDataMapRequest = PutDataMapRequest.create(Constants.KEY_PATH);
                // 通常在開發過程中是使用DataMap類實現DataItem接口，類似Bundle鍵值對的存儲方式
                DataMap mDataMap = mPutDataMapRequest.getDataMap();
                // 使用put…()方法為DataMap設置需要的數據
                mDataMap.putString(Constants.KEY_MESSAGE, msg);
                mDataMap.putInt(Constants.KEY_ICON_ID, dir);
//                Asset asset = createAssetFromBitmap(bg);
//                mDataMap.putAsset(Constants.KEY_ASSET, asset);
                // 調用PutDataMapRequest.asPutDataRequest()創建PutDataRequest對象
                PutDataRequest mPutDataRequest = mPutDataMapRequest.asPutDataRequest();
                // 調用DataApi.putDataItem()請求系統創建DataItem
                PendingResult<DataApi.DataItemResult> pendingResult =
                        Wearable.DataApi.putDataItem(mGoogleApiClient, mPutDataRequest);
                break;
        }
    }

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
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i(TAG, "+ onMessageReceived");
        Log.i(TAG, "get: " + messageEvent.getPath());
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

}
