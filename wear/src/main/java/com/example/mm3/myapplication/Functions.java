package com.example.mm3.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mm3 on 2014/9/26.
 */
public class Functions {
    public static final String TAG = "Functions";
    public static final String[] ModeName = {
            "AUX" , "AM/FM", "USB MP3", "BT Audio", "Navi", "Mode"};

    // create UI
    public static final FunctionImp[] OPEN = new FunctionImp[] {
            new AuxOperation(),
            new RadioOperation(),
            new MusicOperation(),
            new BTMusicOperation(),
            new NaviOperation(),
            new ModeOperation(),
    };

    // item pos map to panelKey
    public static int getModeKeyCode(int pos){
        switch(pos){
            case 0:
                return PanelKeyEvent.PanelKey_AuxIn;
            case 1:
                return PanelKeyEvent.PanelKey_Radio;
            case 2:
                return PanelKeyEvent.PanelKey_Music;
            case 3:
                return PanelKeyEvent.PanelKey_Phone;
            case 4:
                return PanelKeyEvent.PanelKey_NAVI;
            case 5:
                return PanelKeyEvent.PanelKey_ModeKey;
        }
        return -1;
    }


    private static Notification.Builder buildBasicNotification(Context context) {
        return new Notification.Builder(context)
                .setContentTitle("title")
                .setContentText("content") // Set a content intent to return to this sample
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, WearActivity.class), 0))
                .setSmallIcon(R.drawable.ic_launcher);
    }

    //+ MUSIC
    private static class MusicOperation extends FunctionImp {
        public MusicOperation(){
            super("MusicOperation");
        }

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build Music Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("MP3 Music");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {
            Intent intent = new Intent();
            intent.setClass(context, MusicActivity.class);
            context.startActivity(intent);
        }
    }
    //- MUSIC

    //+ Navi
    private static class NaviOperation extends FunctionImp {
        public NaviOperation(){
            super("NaviOperation");
        }

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build NaviOperation Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("NaviOperation");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {
            Intent intent = new Intent();
            intent.setClass(context, NaviActivity.class);
            context.startActivity(intent);
        }
    }
    //- Navi

    private static class AuxOperation extends FunctionImp {
        public AuxOperation(){super("AuxOperation");}

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build AuxOperation Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("AuxOperation");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {

        }
    }

    private static class DialOperation extends FunctionImp {
        public DialOperation(){super("DialOperation");}

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build DialOperation Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("DialOperation");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {

        }
    }


    private static class RadioOperation extends FunctionImp {
        public RadioOperation(){super("RadioOperation");}

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build RadioOperation Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("RadioOperation");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {

        }
    }

    private static class BTMusicOperation extends FunctionImp {
        public BTMusicOperation(){super("BTMusicOperation");}

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build BTMusicOperation Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("BTMusicOperation");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {

        }
    }

    private static class ModeOperation extends FunctionImp {
        public ModeOperation(){super("ModeOperation");}

        @Override
        public Notification buildNotification(Context context) {
            Log.i(TAG, "build ModeOperation Card...");
            Notification.Builder builder = buildBasicNotification(context);
            builder.setContentTitle("ModeOperation");
            return builder.build();
        }

        @Override
        public void buildUI(Context context) {

        }
    }
}
