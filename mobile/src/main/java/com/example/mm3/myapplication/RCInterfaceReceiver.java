package com.example.mm3.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class RCInterfaceReceiver extends BroadcastReceiver{
	private static String TAG = "RCInterfaceReceiver";

	private Context mContext;
	
	
	
	private ToRCInterface mToRCListen = null;
	public interface ToRCInterface{
		public void req_enable(boolean bEnable);
		public void req_alive();
		
		public void req_getSysInf(); 
		public void req_doShellCmd(String[] cmd);
		public void doPanelKeyEvent(int key, boolean isLongPress);
		public void doFunctionKeyEvent(int key, boolean isLongPress);
	};
	public RCInterfaceReceiver(Context context, ToRCInterface toListen) {
		super();
		
    	mContext = context;
    	mToRCListen = toListen;
    	mFromRCListen = null;

    	try {
    		register();
		} catch (Exception e) {
			Log.e(TAG, "Exception", e);
		}
    }
	
	private FromRCInterface mFromRCListen = null;
	public interface FromRCInterface{
		public void notifyServiceReady(boolean bReady);
		public void ack_enable(boolean bEnable);
		public void ack_alive(boolean bRPCisWork);
		
		public void ack_getSysInf(String[] sysInf);
		public void ack_doShellCmd(String origCmd, String result);
		
		public void notifyHUD(int direction, int DrvDistance,
                              int DrvSpeed, int speedLimit, int speedCamera);
	};
	
	public RCInterfaceReceiver(Context context, FromRCInterface fromListen) {
		super();
		
    	mContext = context;
    	mToRCListen = null;
    	mFromRCListen = fromListen;

    	try {
    		register();
		} catch (Exception e) {
			Log.e(TAG, "Exception", e);
		}
    }
    
    public void Close(){
    	try {
			unregister();
		} catch (Exception e) {
			Log.e(TAG, "Exception", e);
		}
    }
    
    public void register(){
    	Log.d(TAG, "+register");
    	
    	if( mToRCListen != null ){//ToRCAction
			//Register
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(ToRCAction);  
			mContext.registerReceiver(this, intentFilter);
			
    	}else{//FromRCAction
			//Register
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(FromRCAction);  
			mContext.registerReceiver(this, intentFilter);
    	}
    	Log.d(TAG, "-register");
    }
    public void unregister(){
    	Log.e(TAG, "+unregister");
    	//Unregister
    	mContext.unregisterReceiver(this);
    	Log.e(TAG, "-unregister");
    }
    
    @Override  
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
    	Log.i(TAG, "onReceive: ");
    	
    	if( ToRCAction.equals(action) ){//ToRCAction
    		onReceive_ToRC(context, intent);
    	}else if( FromRCAction.equals(action) ){//FromRCAction
    		onReceive_FromRC(context, intent);
    	}
    }
	
    
    //+ToRCAction
	public static final String ToRCAction	= "com.asus.f1.pad.remoteconnection.interface.toRC.action";
    
	public static final String ToRC_ITEM_METHOD = "Method";
	public static final String ToRC_ITEM_ARG_1 = "Arg_1";
	public static final String ToRC_ITEM_ARG_2 = "Arg_2";
	
	public static final int ToRC_CMD_ServiceReady = 0;
	public static final int ToRC_CMD_Enable = 1;
	public static final int ToRC_CMD_Alive = 2;
	public static final int ToRC_CMD_GetSysInf = 3;
	public static final int ToRC_CMD_DoShellCmd = 4;
	public static final int ToRC_CMD_DoPanelKeyEvent = 5;
	public static final int ToRC_CMD_DoFunctionKeyEvent = 6;
	
    public void onReceive_ToRC(Context context, Intent intent) {
    	int method = intent.getIntExtra(ToRC_ITEM_METHOD, -1);
    	Log.i(TAG, "onReceive_ToRC: method="+method);
    	
    	switch(method){
	    	case ToRC_CMD_Enable:
		    	{
		    		boolean bEnable = intent.getBooleanExtra(ToRC_ITEM_ARG_1, false); 
		            if( mToRCListen != null ){
		            	mToRCListen.req_enable(bEnable);
		            }	    		
		    	}
				break;
	    	case ToRC_CMD_Alive:
		    	{
		            if( mToRCListen != null ){
		            	mToRCListen.req_alive();
		            }	    		
		    	}
	    		break;
	    	case ToRC_CMD_GetSysInf:
		    	{
		    		if( isEnableToRC() ){
			            if( mToRCListen != null ){
			            	mToRCListen.req_getSysInf();
			            }
		    		}
		    	}
	    		break;
	    	case ToRC_CMD_DoShellCmd:
		    	{
		    		if( isEnableToRC() ){
			    		String[] cmd = intent.getStringArrayExtra(ToRC_ITEM_ARG_1);
			    		if( cmd == null ){//error
			    			Log.i(TAG, "onReceive_ToRC: error cmd="+cmd);
			    			return;
			    		}
			            if( mToRCListen != null ){
			            	mToRCListen.req_doShellCmd(cmd);
			            }
		    		}
		    	}
	    		break;
	    	case ToRC_CMD_DoPanelKeyEvent:
		    	{
		    		if( isEnableToRC() ){
			    		int key = intent.getIntExtra(ToRC_ITEM_ARG_1, -1);
			    		boolean isLongPress = intent.getBooleanExtra(ToRC_ITEM_ARG_2, false); 
			    		if( key < 0 ){//error
			    			Log.i(TAG, "onReceive_ToRC: error key="+key);
			    			return;
			    		}
			            if( mToRCListen != null ){
			            	mToRCListen.doPanelKeyEvent(key, isLongPress);
			            }
		    		}
		    	}
	    		break;
	    	case ToRC_CMD_DoFunctionKeyEvent:
		    	{
		    		if( isEnableToRC() ){
			    		int key = intent.getIntExtra(ToRC_ITEM_ARG_1, -1);
			    		boolean isLongPress = intent.getBooleanExtra(ToRC_ITEM_ARG_2, false); 
			    		if( key < 0 ){//error
			    			Log.i(TAG, "onReceive_ToRC: error key="+key);
			    			return;
			    		}
			            if( mToRCListen != null ){
			            	mToRCListen.doFunctionKeyEvent(key, isLongPress);
			            }
		    		}
		    	}
	    		break;
	    	default:
	    		break;
    	}
    	
    }
	public static void req_enable(Context context, boolean bEnable){
		Intent intent = new Intent();
        intent.setAction(ToRCAction);
        intent.putExtra(ToRC_ITEM_METHOD, ToRC_CMD_Enable);
        intent.putExtra(ToRC_ITEM_ARG_1, bEnable);
        context.sendBroadcast(intent);
	}
	public static void req_alive(Context context){
		Intent intent = new Intent();
        intent.setAction(ToRCAction);
        intent.putExtra(ToRC_ITEM_METHOD, ToRC_CMD_Alive);
        context.sendBroadcast(intent);
	}
	public static void req_getSysInf(Context context){
		Intent intent = new Intent();
        intent.setAction(ToRCAction);
        intent.putExtra(ToRC_ITEM_METHOD, ToRC_CMD_GetSysInf);
        context.sendBroadcast(intent);
	}
	public static void req_doShellCmd(Context context, String[] cmd){
		Intent intent = new Intent();
        intent.setAction(ToRCAction);
        intent.putExtra(ToRC_ITEM_METHOD, ToRC_CMD_DoShellCmd);
        intent.putExtra(ToRC_ITEM_ARG_1, cmd);
        context.sendBroadcast(intent);
	}
	public static void doPanelKeyEvent(Context context, int key, boolean isLongPress){
		Intent intent = new Intent();
        intent.setAction(ToRCAction);
        intent.putExtra(ToRC_ITEM_METHOD, ToRC_CMD_DoPanelKeyEvent);
        intent.putExtra(ToRC_ITEM_ARG_1, key);
        intent.putExtra(ToRC_ITEM_ARG_2, isLongPress);
        context.sendBroadcast(intent);
	}
	public static void doFunctionKeyEvent(Context context, int key, boolean isLongPress){
		Intent intent = new Intent();
        intent.setAction(ToRCAction);
        intent.putExtra(ToRC_ITEM_METHOD, ToRC_CMD_DoFunctionKeyEvent);
        intent.putExtra(ToRC_ITEM_ARG_1, key);
        intent.putExtra(ToRC_ITEM_ARG_2, isLongPress);
        context.sendBroadcast(intent);
	}
    //-ToRCAction
    
    
    //+FromRCAction
	public static final String FromRCAction	= "com.asus.f1.pad.remoteconnection.interface.fromRC.action";
	
	public static final String FromRC_ITEM_METHOD = ToRC_ITEM_METHOD;
	public static final String FromRC_ITEM_ARG_1 = ToRC_ITEM_ARG_1;
	public static final String FromRC_ITEM_ARG_2 = ToRC_ITEM_ARG_2;
	
	public static final int FromRC_CMD_ServiceReady = ToRC_CMD_ServiceReady;
	public static final int FromRC_CMD_Enable = ToRC_CMD_Enable;
	public static final int FromRC_CMD_Alive = ToRC_CMD_Alive;
	public static final int FromRC_CMD_GetSysInf = ToRC_CMD_GetSysInf;
	public static final int FromRC_CMD_DoShellCmd = ToRC_CMD_DoShellCmd;
	//public static final int FromRC_CMD_DoPanelKeyEvent = ToRC_CMD_DoPanelKeyEvent;
	//public static final int FromRC_CMD_DoFunctionKeyEvent = ToRC_CMD_DoFunctionKeyEvent;
	public static final int FromRC_CMD_NotifyHUD = 6;
	
    public void onReceive_FromRC(Context context, Intent intent) {
    	int method = intent.getIntExtra(FromRC_ITEM_METHOD, -1);
    	Log.i(TAG, "onReceive_FromRC: method="+method);
    	
    	switch(method){
	    	case FromRC_CMD_ServiceReady:
		    	{
		    		boolean bReady = intent.getBooleanExtra(ToRC_ITEM_ARG_1, false); 
		            if( mFromRCListen != null ){
		            	mFromRCListen.notifyServiceReady(bReady);
		            }	    		
		    	}
				break;
	    	case FromRC_CMD_Enable:
		    	{
		    		boolean bEnable = intent.getBooleanExtra(ToRC_ITEM_ARG_1, false); 
		            if( mFromRCListen != null ){
		            	mFromRCListen.ack_enable(bEnable);
		            }	    		
		    	}
				break;
	    	case FromRC_CMD_Alive:
		    	{
		    		boolean bRPCisWork = intent.getBooleanExtra(ToRC_ITEM_ARG_1, false); 
		            if( mFromRCListen != null ){
		            	mFromRCListen.ack_alive(bRPCisWork);
		            }	    		
		    	}
	    		break;
	    	case FromRC_CMD_GetSysInf:
		    	{
		    		String[] sysInf = intent.getStringArrayExtra(ToRC_ITEM_ARG_1);
		    		if( sysInf == null ){//error
		    			Log.i(TAG, "onReceive_ToRC: error sysInf="+sysInf);
		    			return;
		    		}
		            if( mFromRCListen != null ){
		            	mFromRCListen.ack_getSysInf(sysInf);
		            }	    		
		    	}
	    		break;
	    	case FromRC_CMD_DoShellCmd:
		    	{
		    		String origCmd = intent.getStringExtra(ToRC_ITEM_ARG_1);
		    		String result = intent.getStringExtra(ToRC_ITEM_ARG_2);
		    		if( origCmd == null ){//error
		    			Log.i(TAG, "onReceive_ToRC: error origCmd="+origCmd);
		    			return;
		    		}
		            if( mFromRCListen != null ){
		            	mFromRCListen.ack_doShellCmd(origCmd, result);
		            }	    		
		    	}
	    		break;
	    	case FromRC_CMD_NotifyHUD:	
		    	{
		    		int[] data = intent.getIntArrayExtra(ToRC_ITEM_ARG_1);
		    		if( data == null || data.length < 5 ){//error
		    			Log.i(TAG, "onReceive_ToRC: error data="+data+", data.length="+data.length);
		    			return;
		    		}
		            if( mFromRCListen != null ){
		            	mFromRCListen.notifyHUD(data[0], data[1], data[2], data[3], data[4]);
		            }	    		
		    	}
	    		break;
	    	default:
	    		break;	    		
    	}
    }
	
	public void notifyServiceReady(Context context, boolean bReady){
		Intent intent = new Intent();
        intent.setAction(FromRCAction);
        intent.putExtra(FromRC_ITEM_METHOD, FromRC_CMD_ServiceReady);
        intent.putExtra(ToRC_ITEM_ARG_1, bReady);
        context.sendBroadcast(intent);
	}
	public void ack_enable(Context context, boolean bEnable){
		mENABLE_toRCinterface = bEnable;
		
		Intent intent = new Intent();
        intent.setAction(FromRCAction);
        intent.putExtra(FromRC_ITEM_METHOD, FromRC_CMD_Enable);
        intent.putExtra(ToRC_ITEM_ARG_1, bEnable);
        context.sendBroadcast(intent);
	}
	public void ack_alive(Context context, boolean bRPCisWork){
		Intent intent = new Intent();
        intent.setAction(FromRCAction);
        intent.putExtra(FromRC_ITEM_METHOD, FromRC_CMD_Alive);
        intent.putExtra(ToRC_ITEM_ARG_1, bRPCisWork);
        context.sendBroadcast(intent);
	}
	public void ack_getSysInf(Context context, String[] sysInf){
		if( isEnableToRC() ){
			Intent intent = new Intent();
	        intent.setAction(FromRCAction);
	        intent.putExtra(FromRC_ITEM_METHOD, FromRC_CMD_GetSysInf);
	        intent.putExtra(ToRC_ITEM_ARG_1, sysInf);
	        context.sendBroadcast(intent);
		}
	}
	public void ack_doShellCmd(Context context, String origCmd, String result){
		if( isEnableToRC() ){
			Intent intent = new Intent();
	        intent.setAction(FromRCAction);
	        intent.putExtra(FromRC_ITEM_METHOD, FromRC_CMD_DoShellCmd);
	        intent.putExtra(ToRC_ITEM_ARG_1, origCmd);
	        intent.putExtra(ToRC_ITEM_ARG_2, result);
	        context.sendBroadcast(intent);
		}
	}
	public void notifyHUD(Context context, int direction, int DrvDistance,
			int DrvSpeed, int speedLimit, int speedCamera){
		if( isEnableToRC() ){
			Intent intent = new Intent();
	        intent.setAction(FromRCAction);
	        intent.putExtra(FromRC_ITEM_METHOD, FromRC_CMD_NotifyHUD);
	        intent.putExtra(ToRC_ITEM_ARG_1, new int[]{direction, DrvDistance, DrvSpeed, speedLimit, speedCamera});
	        context.sendBroadcast(intent);
		}
	}
    //-FromRCAction
	
	//+mENABLE_toRCinterface
	private boolean mENABLE_toRCinterface = false;
	public boolean isEnableToRC(){
		return mENABLE_toRCinterface;
	}
	//-mENABLE_toRCinterface
	
}
