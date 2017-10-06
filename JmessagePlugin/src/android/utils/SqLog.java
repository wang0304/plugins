package com.baimei.jmessage.utils;

import android.util.Log;

/**
 *  自定义Log输出
 * @author baimei
 * @version 1.0.0
 */
public class SqLog {

	private static int v = 0;
	private static int d = 1;
	private static int i = 2;
	private static int w = 3;
	private static int e = 4;
	private static int TAG = 5;	//TAG<0时,日志关闭
	public static boolean IS_ALL_SHOW = false;

	public static void v(String aTag, String aMsg){
		if (v < TAG || IS_ALL_SHOW) {
			Log.v(aTag, aMsg);
		}
	}

	public static void v(String aTag, String aMsg, Exception ex){
		if (v < TAG || IS_ALL_SHOW) {
			Log.v(aTag, aMsg, ex);
		}
	}

	public static void d(String aTag, String aMsg){
		if (d < TAG || IS_ALL_SHOW) {
			Log.d(aTag, aMsg);
		}
	}

	public static void d(String aTag, String aMsg, Exception ex){
		if (d < TAG || IS_ALL_SHOW) {
			Log.d(aTag, aMsg, ex);
		}
	}

	public static void i(String aTag, String aMsg){
		if (i < TAG || IS_ALL_SHOW) {
			Log.i(aTag, aMsg);
		}
	}

	public static void i(String aTag, String aMsg, Exception ex){
		if (i < TAG || IS_ALL_SHOW) {
			Log.i(aTag, aMsg, ex);
		}
	}

	public static void w(String aTag, String aMsg){
		if (w < TAG || IS_ALL_SHOW) {
			Log.w(aTag, aMsg);
		}
	}

	public static void w(String aTag, String aMsg, Exception ex){
		if (w < TAG || IS_ALL_SHOW) {
			Log.w(aTag, aMsg, ex);
		}
	}

	public static void e(String aTag, String aMsg){
		if (e < TAG || IS_ALL_SHOW) {
			Log.e(aTag, aMsg);
		}
	}

	public static void e(String aTag, String aMsg, Exception ex){
		if (e < TAG || IS_ALL_SHOW) {
			Log.e(aTag, aMsg, ex);
		}
	}

	/**
	 * Debug模式下运行输出信息
	 *
	 * @param aTag
	 * @param aMsg
	 */
	public static void debug(String aTag, String aMsg){
		if(IS_ALL_SHOW){
			Log.i(aTag, aMsg);
		}
	}

	/**
	 * Dubug模式下运行输出信息
	 *
	 * @param aTag
	 * @param aMsg
	 * @param ex
	 */
	public static void debug(String aTag, String aMsg, Exception ex){
		if(IS_ALL_SHOW){
			Log.i(aTag, aMsg, ex);
		}
	}

}
