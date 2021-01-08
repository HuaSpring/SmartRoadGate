package com.fspt.roadgate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * Create by Spring on 2020/9/10 08:11
 */
public class HashDB extends BaseDB {

    private HashMap<String, Long[]> maps = new HashMap<>();

    public HashDB(Context ctx) {
        super(ctx, "", null, 1);
    }

    public HashDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void updateCarStatus(String carId, boolean carStatus, long carTime, long money) {
        maps.put(carId, new Long[]{carTime, money, carStatus ? 1L : 0L});
    }

    /**
     * @param carId
     * @param carStatus 1: Enter  0: Out
     * @param startTime
     * @param money
     */
    public void addCarData(String carId, boolean carStatus, long startTime, long money) {
        maps.put(carId, new Long[]{startTime, money, carStatus ? 1L : 0L});
    }

    public Long[] queryCarIdExist(String carId) {
        if (maps.containsKey(carId)) {
            return maps.get(carId);
        }
        return new Long[]{0L, 0L, 0L}; // car_enterTime,Car_Money,CarStatus
    }

    public void removeCarID(String carID) {
        maps.remove(carID);
    }

}
