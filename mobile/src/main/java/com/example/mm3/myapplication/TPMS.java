package com.example.mm3.myapplication;

import java.util.Random;

/**
 * Created by mm3 on 2014/10/16.
 */
public class TPMS {
    public static final String KEY_ID           = "TPMS_KEY_ID";
    public static final String KEY_PRESSURE     = "TPMS_KEY_PRESSURE";
    public static final String KEY_TEMPTURE     = "TPMS_KEY_TEMPTURE";
    public static final String KEY_NO_SIGNAL    = "TPMS_KEY_NO_SIGNAL";
    public static final String KEY_STATUS       = "TPMS_KEY_STATUS";
    public static final String KEY_LEAK_GAS     = "TPMS_KEY_LEAK_GAS";


    public static class Data{
        public int id;
        public int pressure;
        public int tempture;
        public int noSignal;
        public int status;
        public int leakGas;

        public Data(){
            id = -1;
            pressure = -1;
            tempture = -1;
            noSignal = -1;
            status = -1;
            leakGas = -1;
        }

        public static TPMS.Data geneData(int id){
            TPMS.Data data = new TPMS.Data();
            data.id          = id;    // 挑選一輪胎
            data.pressure    = new Random().nextInt(0x80) + 0x80;// 隨機產生壓力
            data.tempture    = new Random().nextInt(0x255);      // 隨機產生當前速度
            data.noSignal    = new Random().nextInt(6) * 10 + 70;// 隨機產生當前速度
            data.status      = new Random().nextInt(6) * 10 + 70;// 隨機產生當前速度
            data.leakGas     = new Random().nextInt(6) * 10 + 70;// 隨機產生當前速度
            System.out.print(data.toString());
            return data;
        }

        public String toString(){
            return
                "id: " + id +
                "\npressure: "  + pressure +
                "\ntempture: "  + tempture +
                "\nnoSignal: "  + noSignal +
                "\nstatus: "    + status +
                "\nleakGas: "   + leakGas +
                "\n";
        }
    }

}
