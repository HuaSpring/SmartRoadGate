package com.fspt.roadgate.database;

import android.content.Context;

/**
 * Create by Spring on 2020/9/10 08:08
 */

// TODO: 2020/9/10 工厂模式，产生应该的子类
public class DBManager {
    public static final String HASH = "hash";
    public static final String DB = "db";
    private DBManager mInstance;
    private BaseDB db;


    public BaseDB createDB(Context ctx, String type) {
        switch (type) {
            case HASH:
                db = new HashDB(ctx); // HashMap 形式的存储
                break;
            case DB:
                db = new RoadDB(ctx); // Sqlite 形式的持久化
                break;
            default:
                break;
        }
        return db;
    }


}
