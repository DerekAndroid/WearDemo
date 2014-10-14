package com.example.mm3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Handler;

public class WearActivity extends Activity implements WearableListView.ClickListener{
    public static final String TAG = "WearActivity";
    public final Context mContext = WearActivity.this;
    private GoogleClient client;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setupWidgets();
        initClient();
    }


    /**
     * Initial Widgets.
     */
    private void setupWidgets(){
        WearableListView listView = (WearableListView) findViewById(R.id.list);
        listView.setAdapter(new ListAdapter(this));
        listView.setClickListener(this);
    }

    /**
     * Initial GoogleApiClient.
     */
    private void initClient(){
        client = new GoogleClient(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Log.i(TAG, "+ Click");
        int pos = (Integer) viewHolder.itemView.getTag();
        buildOperation(pos);
        Log.i(TAG, "- Click pos: " + pos);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    public void buildOperation(final int id){
        // Notify Mode Change.
        client.sendCommand(Functions.getModeKeyCode(id));
        // Delay 100 ms to build UI
        // avoid another Activity establish connection lead to sendMessage fail.
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                FunctionImp function = Functions.OPEN[id];
                function.buildUI(mContext);
            }
        },100);

//        Notification notify = function.buildNotification(mContext);
//        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
//                .notify(NOTIFICATION_ID, notify);
//        finish();
    }

    private static final class ListAdapter extends WearableListView.Adapter {
        private final Context mContext;
        private final LayoutInflater mInflater;

        private ListAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.notif_preset_list_item, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView view = (TextView) holder.itemView.findViewById(R.id.name);
            view.setText(Functions.ModeName[position]);
            view.setTextSize(20);
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return Functions.ModeName.length;
        }
    }
}
