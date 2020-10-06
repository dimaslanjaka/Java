package com.dimaslanjaka.tools.Service.Netspeed.v2;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public class Utils {

	private static final long B = 1;
	private static final long KB = B * 1024;
	private static final long MB = KB * 1024;
	private static final long GB = MB * 1024;
	private final static long KB_FACTOR = 1024;
	private final static long MB_FACTOR = 1024 * KB_FACTOR;
	private final static long GB_FACTOR = 1024 * MB_FACTOR;

	public static String parseSpeed(double bytes, boolean inBits) {

		double value = inBits ? bytes * 8 : bytes;
		if (value < KB) {
			return String.format(Locale.getDefault(), "%.1f " + (inBits ? "b" : "B") + "/s", value);
		} else if (value < MB) {
			return String.format(Locale.getDefault(), "%.1f K" + (inBits ? "b" : "B") + "/s", value / KB);
		} else if (value < GB) {
			return String.format(Locale.getDefault(), "%.1f M" + (inBits ? "b" : "B") + "/s", value / MB);
		} else {
			return String.format(Locale.getDefault(), "%.2f G" + (inBits ? "b" : "B") + "/s", value / GB);
		}
	}

	public static String humanReadableByteCountSI(long bytes) {
		if (-1000 < bytes && bytes < 1000) {
			return bytes + " B";
		}
		CharacterIterator ci = new StringCharacterIterator("kMGTPE");
		while (bytes <= -999_950 || bytes >= 999_950) {
			bytes /= 1000;
			ci.next();
		}
		return String.format("%.1f %cB", bytes / 1000.0, ci.current());
	}

	public static double parse(String arg0) {
		int spaceNdx = arg0.indexOf(" ");
		double ret = Double.parseDouble(arg0.substring(0, spaceNdx));
		switch (arg0.substring(spaceNdx + 1)) {
			case "GB":
				return ret * GB_FACTOR;
			case "MB":
				return ret * MB_FACTOR;
			case "KB":
				return ret * KB_FACTOR;
		}
		return -1;
	}

	public static String parseSpeed(double txBPS) {
		return parseSpeed(txBPS, false);
	}
}