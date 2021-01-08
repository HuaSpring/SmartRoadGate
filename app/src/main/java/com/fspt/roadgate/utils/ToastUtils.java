package com.fspt.roadgate.utils;

import android.content.Context;
import android.widget.Toast;

//Toast工具类，传递上下文，显示内容，显示持续时间三个参数，即可显示提示条
public class ToastUtils {
	public static void showToast(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
}
