package com.topnews.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharPreUtil {

	public static Boolean getBoolean(Context context,String key,boolean defvalue){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getBoolean(key, defvalue);
	}
	
	public static void setBoolean(Context context,String key,boolean value){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static String getString(Context context,String key,String defvalue){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getString(key, defvalue);
	}
	
	public static void setString(Context context,String key,String value){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static int getInt(Context context,String key,int defvalue){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		return sp.getInt(key, defvalue);
	}
	
	public static void setInt(Context context,String key,int value){
		SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
}
