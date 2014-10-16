package com.example.mm3.myapplication;

<<<<<<< HEAD
=======
import java.util.Random;

>>>>>>> Android Wear SDK 0.8.9
/**
 * Created by mm3 on 2014/10/7.
 */
public final class HUD {
    public static final int NOTIFY_ID = 0x5566;
    public static final String ACTION_DISMISS
            = "com.example.mm3.myapplication.DISMISS";

    public static final String KEY_NOTIFICATION_ID = "notification-id";
    // ICON
    public static final String KEY_ASSET = "ASSET";
    // HUD PATH
    public static final String KEY_PATH = "/hud_data";
    // Driving direction code
    public static final String KEY_DIRECTION = "HUD_DIRECTION";
    // Driving distance code
    public static final String KEY_DISTANCE = "HUD_DISTANCE";
    // Driving Speed code
    public static final String KEY_SPEED = "HUD_SPEED";
    // 道路速限資訊
    public static final String KEY_SPEED_LIMIT = "HUD_SPEED_LIMIT";
    // 限速照相警示
    public static final String KEY_INDICATOR = "HUD_INDICATOR";
    // ACC
    public static final String KEY_ACC = "HUD_ACC";

<<<<<<< HEAD
=======
    private static final int VALUE_TURN_LEFT                 = 0x303031;
    private static final int VALUE_TURN_RIGHT                = 0x303032;
    private static final int VALUE_TURN_UP_LEFT              = 0x303033;
    private static final int VALUE_TURN_UP_RIGHT             = 0x303034;
    private static final int VALUE_U_TURN_LEFT               = 0x303035;
    private static final int VALUE_U_TURN_RIGHT              = 0x303036;
    private static final int VALUE_GO_STRAIGHT               = 0x303037;
    private static final int VALUE_CLEAR_DIRECTION_DISPLAY   = 0x232323;

>>>>>>> Android Wear SDK 0.8.9
    public static class Data{
        public int direction;
        public int distance;
        public int speed;
        public int speed_limit;
        public int indicator;

        public Data(){
            direction = -1;
            distance = -1;
            speed = -1;
            speed_limit = -1;
            indicator = -1;
        }

<<<<<<< HEAD
        public String toString(){
            return
                    "Direct: " + direction +
                    "\nDistance: " + distance +
                    "\nSpeed: " + speed +
                    "\nSpeed_limit: " + speed_limit +
                    "\nIndicator: " + indicator +
=======
        public static HUD.Data geneData(){
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

        public String toString(){
            return
                    "Direct: " + getAction(direction).getString() +
                    "\nDistance: " + distance +
                    "\nSpeed: " + speed +
                    "\nSpeed_limit: " + speed_limit +
                    "\nIndicator: " + getIndicator(indicator) +
>>>>>>> Android Wear SDK 0.8.9
                    "\n";
        }
    }

    public interface Command {
        void execute();
    }

<<<<<<< HEAD
=======
    //hud value 轉換成 action
    public static ACTION getAction(int direction){
        switch(direction){
            case VALUE_TURN_LEFT:
                return ACTION.TURN_LEFT;
            case VALUE_TURN_RIGHT:
                return ACTION.TURN_RIGHT;
            case VALUE_TURN_UP_LEFT:
                return ACTION.TURN_UP_LEFT;
            case VALUE_TURN_UP_RIGHT:
                return ACTION.TURN_UP_RIGHT;
            case VALUE_U_TURN_LEFT:
                return ACTION.U_TURN_LEFT;
            case VALUE_U_TURN_RIGHT:
                return ACTION.U_TURN_RIGHT;
            case VALUE_GO_STRAIGHT:
                return ACTION.GO_STRAIGHT;
            case VALUE_CLEAR_DIRECTION_DISPLAY:
                return ACTION.CLEAR_DISPLAY;
            default:
                return null;
        }
    }

    public static String getIndicator(int indicator){
        switch(indicator) {
            case 0x31:
                return "限速照相";
            case 0x30:
                return "衝阿！";
            default:
                return "What the park?";
        }
    }

>>>>>>> Android Wear SDK 0.8.9
    public static enum ACTION implements Command{
        /************************
         * Driving direction code
         ************************/
        // 建構列舉常數自訂序列與字串
<<<<<<< HEAD
        TURN_LEFT(      "ACTION_TURN_LEFT"      , 0x0001),
        TURN_RIGHT(     "ACTION_TURN_RIGHT"     , 0x0002),
        TURN_UP_LEFT(   "ACTION_TURN_UP_LEFT"   , 0x0003),
        TURN_UP_RIGHT(  "ACTION_TURN_UP_RIGHT"  , 0x0004),
        U_TURN_LEFT(    "ACTION_U_TURN_LEFT"    , 0x0005),
        U_TURN_RIGHT(   "ACTION_U_TURN_RIGHT"   , 0x0006),
        GO_STRAIGHT(    "ACTION_GO_STRAIGHT"    , 0x0007),
        CLEAR_DISPLAY(  "ACTION_CLEAR_DISPLAY"  , 0x0008);
=======
        TURN_LEFT(      "ACTION_TURN_LEFT"      , VALUE_TURN_LEFT),
        TURN_RIGHT(     "ACTION_TURN_RIGHT"     , VALUE_TURN_RIGHT),
        TURN_UP_LEFT(   "ACTION_TURN_UP_LEFT"   , VALUE_TURN_UP_LEFT),
        TURN_UP_RIGHT(  "ACTION_TURN_UP_RIGHT"  , VALUE_TURN_UP_RIGHT),
        U_TURN_LEFT(    "ACTION_U_TURN_LEFT"    , VALUE_U_TURN_LEFT),
        U_TURN_RIGHT(   "ACTION_U_TURN_RIGHT"   , VALUE_U_TURN_RIGHT),
        GO_STRAIGHT(    "ACTION_GO_STRAIGHT"    , VALUE_GO_STRAIGHT),
        CLEAR_DISPLAY(  "ACTION_CLEAR_DISPLAY"  , VALUE_CLEAR_DIRECTION_DISPLAY);


>>>>>>> Android Wear SDK 0.8.9

        private int value;
        private String str;
        // 建構子
        private ACTION(String dir_str, int dir_value) {
            this.value = dir_value;
            this.str = dir_str;
        }

        public int getValue() {
            return value;
        }

        public String getString(){
            return str;
        }

        // 設定對應列舉常數後續執行動作
        public void execute() {
            switch(this) {
                case TURN_LEFT:
                    System.out.println("TURN_LEFT");
                    break;
                case TURN_RIGHT:
                    System.out.println("TURN_RIGHT");
                    break;
                case TURN_UP_LEFT:
                    System.out.println("TURN_UP_LEFT");
                    break;
                case TURN_UP_RIGHT:
                    System.out.println("TURN_UP_RIGHT");
                    break;
                case U_TURN_LEFT:
                    System.out.println("U_TURN_LEFT");
                    break;
                case U_TURN_RIGHT:
                    System.out.println("U_TURN_RIGHT");
                    break;
                case GO_STRAIGHT:
                    System.out.println("GO_STRAIGHT");
                    break;
                case CLEAR_DISPLAY:
                    System.out.println("CLEAR_DISPLAY");
                    break;
            }
        }
    }


<<<<<<< HEAD
//    private static final int ACTION_TURN_LEFT                 = 0x0001;
//    private static final int ACTION_TURN_RIGHT                = 0x0002;
//    private static final int ACTION_TURN_UP_LEFT              = 0x0003;
//    private static final int ACTION_TURN_UP_RIGHT             = 0x0004;
//    private static final int ACTION_U_TURN_LEFT               = 0x0005;
//    private static final int ACTION_U_TURN_RIGHT              = 0x0006;
//    private static final int ACTION_GO_STRAIGHT               = 0x0007;
//    private static final int ACTION_CLEAR_DIRECTION_DISPLAY   = 0x0008;
=======

>>>>>>> Android Wear SDK 0.8.9

//    private static final String[] getString = {
//            "Turn Left",
//            "Turn Right",
//            "Turn Up Left",
//            "Turn Up Right",
//            "U turn (Left)",
//            "U turn (Right)",
//            "Go Straight",
//            "Clear Direction Display"
//    };
}
