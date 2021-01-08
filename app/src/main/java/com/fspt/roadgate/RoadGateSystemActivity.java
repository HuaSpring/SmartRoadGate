package com.fspt.roadgate;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.fspt.roadgate.database.BaseDB;
import com.fspt.roadgate.database.DBManager;
import com.fspt.roadgate.manager.TTSManager;
import com.fspt.roadgate.utils.TimeUtils;
import com.fspt.roadgate.utils.ToastUtils;

import java.util.Arrays;


public class RoadGateSystemActivity extends Activity {
    private ImageView iv;// 声明一个道闸杆图片控件类对象
    private static RoadGateHandler mHandler;
    private TTSManager ttsManager;// 创建一个语音播报管理类对象
    private Animation animRotateUp;// 声明两个动画对象变量
    private Animation animRotateDown;// 声明两个动画对象变量

    // DB
//    private RoadDB db;
//    private HashDB db;

    private BaseDB db;
    // Debug ...
    //  am broadcast -a "com.fspt.roadgate" --es "car_id" "01" --ei "car_balance" 9999  模拟卡内余额以及 car_id
    //  NlecloudManager  中可以用自己的账号密码登陆，登陆后可以在 global的  Constants 中设置执行器 ID 和项目ID

    private static final int MSG_OPEN_DOOR = 100;
    private static final int MSG_CLOSE_DOOR = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadgate_system_activity);
        //获得云平台操作对象
        ttsManager = new TTSManager(this);
        initView();// 初始化组件及成员变量
        initMoniRFID();
        mHandler = new RoadGateHandler();
    }


    //初始化控件
    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        // 加载XML动画文件rotate_up,rotate_down为动画对象animationUp,animationDown
        animRotateUp = AnimationUtils.loadAnimation(this, R.anim.rotate_up);
        animRotateDown = AnimationUtils.loadAnimation(this, R.anim.rotate_down);
    }


    private void initMoniRFID() {  // 假设读到一辆车进入
        registerReceiver(new CarEnterBroadcaseReceiver(), new IntentFilter("com.fspt.roadgate"));
        openDoorRunnable = new OpenCloseDoorRunnable(true);
        closeDoorRunnable = new OpenCloseDoorRunnable(false);

//        db = new RoadDB(RoadGateSystemActivity.this);
//        db = new HashDB(RoadGateSystemActivity.this);
        db = new DBManager().createDB(RoadGateSystemActivity.this, DBManager.DB);
    }


    private static OpenCloseDoorRunnable openDoorRunnable;
    private static OpenCloseDoorRunnable closeDoorRunnable;


    private class OpenCloseDoorRunnable implements Runnable {
        boolean bOpen;

        public OpenCloseDoorRunnable(boolean bOpen) {
            this.bOpen = bOpen;
        }

        @Override
        public void run() {
            if (this.bOpen) {  // 执行抬杆动作
                iv.startAnimation(animRotateUp);
            } else { // 执行落杆动作
                iv.startAnimation(animRotateDown);
            }
        }
    }

    //执行闸杆升降动画及开关灯的任务
    private Runnable openDoorTask = new Runnable() {
        @Override
        public void run() { // 通过  Handler 发送  MSG_OPEN_DOOR 消息
            mHandler.sendEmptyMessage(MSG_OPEN_DOOR);
        }
    };


    /**
     * 实现出入库的判断及语音播报功能
     *
     * @param carId   车牌号
     * @param balance 余额
     */
    private void processData(String carId, int balance) {
        // 读取文件中存储的标签号
        Long[] carInfos = db.queryCarIdExist(carId);
        boolean exist = carInfos[2] == 1;
        Log.d("HHHH", "processData CarInfos  car_enterTime,Car_Money, CarStatus :  "
                + Arrays.toString(carInfos) + " IsExist ? " + exist);
        if (exist) { // 如果存在，则一定是出库
            long stopTime = System.currentTimeMillis();
            // 获取车入库时间（入库时时间戳）
            long startTimetime = carInfos[0];
            //持续停车时间，注意转换为秒为单位，计算停车时长
            int countTime = (int) ((stopTime - startTimetime) / 1000);
            //计算消费金额，停车费用，每秒是2元
            int cost = countTime * 2;
            //获取日期格式的结束时间，将出库时间转换为年月日格式的字符串
            String simpleStartStopTime = TimeUtils.getSimpleTime(stopTime);
            //生成显示的提示条消息内容，卡号、结束时间、停车时长、消费金额
            String content = "卡号：" + carId + "\n结束时间：" + simpleStartStopTime
                    + "\n停车时长：" + countTime + "秒\n消费金额：" + cost + "元"
                    + " remain " + (carInfos[1] - cost) + "元";
            // 通过  吐丝   Toast  显示出库信息
            ToastUtils.showToast(RoadGateSystemActivity.this, content);
            //TTS语音读取金额
            ttsManager.speak(cost + " money" + " remain " + (carInfos[1] - cost) + "yuan ");
            //出库后，更新车辆状态
            db.updateCarStatus(carId, false, stopTime, carInfos[1] - cost);
        } else {// 如果车辆从未进入过停车场，现在要入库
            //取得当前系统时间戳作为车入库时间，将入库时间转换为年月日格式的字符串
            String simpleStartTime = TimeUtils.getSimpleTime(System.currentTimeMillis());
            //生成显示的提示条消息内容，提示卡号、开始时间、余额
            // 余额看是否是以前车辆
            long last_money = db.queryCarIdExist(carId)[1];
            long showBalance = last_money > 0 ? last_money : balance;
            String content = "卡号：" + carId + "\n开始时间：" + simpleStartTime + "\n余额:" + showBalance;
            // 提示条显示入库信息
            ToastUtils.showToast(RoadGateSystemActivity.this, content);
            // 存入车辆入库时间小于 1，则可能是已经入库但已经离开，或是之前从未入过库的
            if (carInfos[0] < 1) { // 从未来过
                db.addCarData(carId, true, System.currentTimeMillis(), balance);
            } else { // 已经存在过，则更新其状态
                db.updateCarStatus(carId, true, System.currentTimeMillis(), showBalance);
            }
        }
        mHandler.post(openDoorTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭TTS语音
        ttsManager.stopTTS();
    }


    ///////////////////////////////////////////////////////////////////////////
    //  模拟车辆进入
    ///////////////////////////////////////////////////////////////////////////
    boolean bCarEnter = false;
    int balance = 0;

    class CarEnterBroadcaseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            bCarEnter = intent.getBooleanExtra("car_enter", false);
            balance = intent.getIntExtra("car_balance", 1000);
            String carId = intent.getStringExtra("car_id");
            if (TextUtils.isEmpty(carId)) {
                carId = "default";
            }
            assert carId != null;
            processData(carId, balance);
        }
    }

    static class RoadGateHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_OPEN_DOOR:
                    mHandler.post(openDoorRunnable);
                    mHandler.sendEmptyMessageDelayed(MSG_CLOSE_DOOR, 3 * 1000);
                    break;
                case MSG_CLOSE_DOOR:
                    mHandler.post(closeDoorRunnable);
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
