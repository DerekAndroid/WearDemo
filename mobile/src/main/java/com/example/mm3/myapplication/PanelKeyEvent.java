package com.example.mm3.myapplication;

public class PanelKeyEvent {

	//on CAR Panel
	//Mode Key
	public static final int PanelKey_ModeKey	= 0x0100;//on CAR steering wheel
	public static final int PanelKey_Radio		= 0x0101;
	public static final int PanelKey_AuxIn		= 0x0102;
	public static final int PanelKey_Music		= 0x0103;
	public static final int PanelKey_Time		= 0x0104;
	public static final int PanelKey_EQ			= 0x0105;
    public static final int PanelKey_NAVI		= 0x0106;

	//Global Key (Process on SystemManager)
	public static final int PanelKey_Phone		= 0x0200;
	public static final int PanelKey_VolUp		= 0x0201;
	public static final int PanelKey_VolDown	= 0x0202;
	public static final int PanelKey_Mute		= 0x0203;
	
	//Patch Key (Pass to Apps; Process on Apps)
	public static final int PanelKey_Num1		= 0x0001;
	public static final int PanelKey_Num2		= 0x0002;
	public static final int PanelKey_Num3		= 0x0003;
	public static final int PanelKey_Num4		= 0x0004;
	public static final int PanelKey_TuneInc	= 0x0005;
	public static final int PanelKey_TuneDec	= 0x0006;
	public static final int PanelKey_Prev		= 0x0007;
	public static final int PanelKey_Play		= 0x0008;
	public static final int PanelKey_Next		= 0x0009;
	public static final int PanelKey_Loop		= 0x000A;
	public static final int PanelKey_Scan		= 0x000B;
	
}
