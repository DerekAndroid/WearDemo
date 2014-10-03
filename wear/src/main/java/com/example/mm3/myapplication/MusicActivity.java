package com.example.mm3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;


public class MusicActivity extends Activity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public static final String TAG = "MusicActivity";
    public final Context mContext = MusicActivity.this;
    // UI controller
    private Button mPlayButton = null;
    private Button mNextButton = null;
    private Button mPrevButton = null;
    private Button mMuteButton = null;
    private Button mVolUpButton = null;
    private Button mVolDownButton = null;
    // client
    public GoogleClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity);

        client = new GoogleClient(mContext);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets();
            }
        });

    }

    /**
     * Sets up the button for handling click events.
     */
    private void setupWidgets() {
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mPlayButton = (Button) findViewById(R.id.play_pause_button);
        mMuteButton = (Button) findViewById(R.id.mute_button);
        mVolUpButton = (Button) findViewById(R.id.vol_up_button);
        mVolDownButton = (Button) findViewById(R.id.vol_down_button);

        mPrevButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mMuteButton.setOnClickListener(this);
        mVolUpButton.setOnClickListener(this);
        mVolDownButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prev_button:
                client.sendCommand(PanelKeyEvent.PanelKey_Prev);
                break;
            case R.id.next_button:
                client.sendCommand(PanelKeyEvent.PanelKey_Next);
                break;
            case R.id.play_pause_button:
                client.sendCommand(PanelKeyEvent.PanelKey_Play);
                break;
            case R.id.mute_button:
                client.sendCommand(PanelKeyEvent.PanelKey_Mute);
                break;
            case R.id.vol_up_button:
                client.sendCommand(PanelKeyEvent.PanelKey_VolUp);
                break;
            case R.id.vol_down_button:
                client.sendCommand(PanelKeyEvent.PanelKey_VolDown);
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
