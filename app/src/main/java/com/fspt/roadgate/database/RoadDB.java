package com.fspt.roadgate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class RoadDB extends BaseDB {

    public static final String CREATE_CAR_TABLE = "" +
            "CREATE TABLE car ( car_id CHAR(20) UNIQUE, car_status bool, car_staytime long(32),car_balance long(32) )";

    public RoadDB(@Nullable Context context) {
        super(context, "car", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }




    public void updateCarStatus(String carId, boolean carStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("car_id", carId);
        cv.put("car_status", carStatus);
        db.execSQL("UPDATE car SET car_status = " + carStatus + " where car_id = " + carId);
    }

    public void removeCarID(String carID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM car where car_id = \"" + carID + '\"');
    }

    /**
     * 离开，则计费时间归为0
     * @param carId
     * @param carStatus
     * @param startTime
     * @param money
     */
    public void updateCarStatus(String carId, boolean carStatus, long startTime, long money) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("car_id", carId);
        cv.put("car_status", carStatus);
        cv.put("car_balance", money);
        cv.put("car_staytime", startTime);
        db.update("car", cv, "car_id = ?", new String[]{carId});

    }

    public Long[] queryCarIdExist(String carId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("car", new String[]{"car_id", "car_status", "car_staytime", "car_balance"},
                "car_id=?", new String[]{carId}, "", "", "");
        while (cursor.moveToNext()) {
            int car_Status = cursor.getInt(cursor.getColumnIndexOrThrow("car_status"));  // 1 :Car Enter  , 0:  Car Out
            long car_staytime = cursor.getLong(cursor.getColumnIndexOrThrow("car_staytime"));
            long car_balanc = cursor.getLong(cursor.getColumnIndexOrThrow("car_balance"));
            return new Long[]{car_staytime, car_balanc, (long) car_Status}; // car_enterTime,Car_Money,CarStatus
        }
        return new Long[]{0L, 0L, 0L};
    }


    public void addCarData(String carId, boolean carStatus, long startTime, long balance) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("car_id", carId);
        cv.put("car_status", carStatus ? 1 : 0);
        cv.put("car_staytime", startTime);
        cv.put("car_balance", balance);
        db.insertOrThrow("car", "", cv);
    }
}
