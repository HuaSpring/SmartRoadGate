package com.fspt.roadgate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

/**
 * Create by Spring on 2020/9/10 08:19
 */
public abstract class BaseDB extends SQLiteOpenHelper {

    public BaseDB(Context ctx){
        super(ctx,"",null,1);
    }

    public BaseDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    ///////////////////////////////////////////////////////////////////////////
    // 不管以 hash 简易的方式，还是在 sqlite 的形式都要实现如下方法， 更新，查询，和添加
    // 同学们还可以添加 其它方法给予扩充
    ///////////////////////////////////////////////////////////////////////////
    public abstract void updateCarStatus(String carId, boolean carStatus, long startTime, long money);
    /**
     * @param carId  车牌唯一标识
     * @return  Long[]  第一个是：某车辆的进出的时间   第二个是： 该车对应的卡内余额  第三个是： 车辆的进出状态， 1为入库，0为出库
     */
    public abstract Long[] queryCarIdExist(String carId);
    public abstract void addCarData(String carId, boolean carStatus, long startTime, long balance);
}
