package com.example.mm3.myapplication;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mm3 on 2014/10/1.
 */
public class GoogleClient {
    public static final String TAG = "GoogleClient";
    private GoogleApiClient client;
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private static final String KEY_START_TIME = "KEY_START_TIME";
    private String nodeId;

    GoogleClient(Context context){
        initApi(context);
    }

    /**
     * Send message to the connected device.
     * @param command
     */
    public void sendCommand(final int command){
        Log.i(TAG, "+ sendCommand: " + command);

        //檢查是否有連線節點
        if (nodeId != null) {
            //紀錄傳送時間
            DataMap dataMap = new DataMap();
            dataMap.putLong(KEY_START_TIME, System.currentTimeMillis());

            //避免ANR
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, String.valueOf(command), null)
                        .setResultCallback(
                        //檢查是否傳遞成功
                        new ResultCallback<MessageApi.SendMessageResult>(){
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                if(!sendMessageResult.getStatus().isSuccess()){
                                    Log.e(TAG, "Failed to send message with status code:"
                                    + sendMessageResult.getStatus().getStatusCode());
                                }else{
                                    Log.i(TAG, "+ sendCommand: " + command);
                                }
                            }
                        }
                    );
                    //同步單一欄位,資料沒有變更將不會更新
                    /*
                    //創建PutDataMapRequest對象，為DataItem設置path值(建立請求以 '/' 開頭,區別不同的DataItem)
                    PutDataMapRequest mPutDataMapRequest = PutDataMapRequest.create("/action");
                    // 通常在開發過程中是使用DataMap類實現DataItem接口，類似Bundle鍵值對的存儲方式
                    DataMap mDataMap = mPutDataMapRequest.getDataMap();
                    // 使用put…()方法為DataMap設置需要的數據
                    mDataMap.putInt("OPERATOR", cmd);
                    // 調用PutDataMapRequest.asPutDataRequest()創建PutDataRequest對象
                    PutDataRequest mPutDataRequest = mPutDataMapRequest.asPutDataRequest();
                    // 調用DataApi.putDataItem()請求系統創建DataItem
                    PendingResult<DataApi.DataItemResult> pendingResult =
                            Wearable.DataApi.putDataItem(client, mPutDataRequest);
                    */
                    client.disconnect();
                }
            }).start();
        }else{
            Log.e(TAG,"Can't get Node Id!");
            retrieveDeviceNode();
        }
        Log.i(TAG, "- sendCommand: " + command);
    }

    /**
     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
     */
    private void initApi(Context context) {
        Log.i(TAG,"+ initApi");
        client = getGoogleApiClient(context);
        retrieveDeviceNode();
        Log.i(TAG,"- initApi");
    }

    /**
     * Returns a GoogleApiClient that can access the Wear API.
     * @param context
     * @return A GoogleApiClient that can make calls to the Wear API
     */
    private GoogleApiClient getGoogleApiClient(Context context) {
        Log.i(TAG,"getGoogleApiClient...");
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode() {
        Log.i(TAG,"retrieveDeviceNode...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if(nodes == null){
                    Log.e(TAG, "Nodes not found...");
                    return;
                }
                if (nodes.size() > 0) {
                    for(int i = 0 ; i < nodes.size() ; i++){
                        Log.d(TAG, "Get " + i + " : " + nodes.get(0).getId());
                    }
                    nodeId = nodes.get(0).getId();
                }
                client.disconnect();
            }
        }).start();
    }

    private void close(){
        client.disconnect();
    }

}
