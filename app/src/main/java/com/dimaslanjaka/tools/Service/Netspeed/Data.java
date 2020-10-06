package com.dimaslanjaka.tools.Service.Netspeed;

import java.util.Date;

public class Data {
	public static String speed = "0 Kb/s";
	public static String down = "Download 0 Kb/s";
	public static String up = "Upload 0 Kb/s";
	public static double drx = 10.0D;
	public static double dtx = 10.0D;
	public static double dall;
	public static String[] res = null;
	public static boolean flag = false;
	public static boolean sflag = false;
	public static long wifisend;
	public static long wifirec;
	public static long mobilesend;
	public static long mobilerec;
	public static long totalsend;
	public static long totalrec;
	public static long dailyTotlSend;
	public static long dailyTotalRecive;
	public static Date dailyDataUsage;

	public static long dailyMobileDataUsage;
	public static long dailyWifiDataUsage;


	public Data() {
	}

	public static void setData(String a, String b, String c, double all, double rx, double tx) {
		speed = a;
		down = b;
		up = c;
		dall = all;
		drx = rx;
		dtx = tx;
	}

	public static String[] getData() {
		res = new String[]{speed, down, up};
		return res;
	}
}