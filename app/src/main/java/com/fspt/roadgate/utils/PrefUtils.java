package com.fspt.roadgate.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 *
 *SharedPreferences 工具类
 *
 */
public class PrefUtils {
	private static final String PREF_NAME = "config";

	public static void setBoolean(Context context, String key, boolean value) {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
				.putBoolean(key, value).apply();
	}

	public static boolean getBoolean(Context context, String key,
									 boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}


	public static void setString(Context context, String key, String value) {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
				.putString(key, value).apply();
	}

	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static void setLong(Context context, String key, long value) {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
				.putLong(key, value).apply();
	}

	public static long getLong(Context context, String key, long defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
	}

}
