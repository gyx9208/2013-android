package com.weibonju.configure;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 设置和状态的保存
 * @author gyx
 *
 */
public class ConfigureKeeper {
	private static final String PREFERENCES_NAME = "weibo_nju";
	
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}
	
	public static void saveSpinnerPos(Context context, int i){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt("spinner_pos", i);
		editor.commit();
	}
	
	public static int readSpinnerPos(Context context){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		int i=pref.getInt("spinner_pos", 0);
		return i;
	}
}
