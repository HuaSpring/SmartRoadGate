package com.fspt.roadgate.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//时间工具类，按照指定格式显示当前系统时间
public class TimeUtils {
	public static String getSimpleTime(long time){
		Date date=new Date(time);//把传递过来的时间time构成Date类对象
		//创建一个简单日期格式对象，指定具体格式
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(date);//把date按照简单日期格式进行格式化
	}
}
