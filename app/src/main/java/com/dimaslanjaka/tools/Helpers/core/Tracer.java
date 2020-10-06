package com.dimaslanjaka.tools.Helpers.core;

public class Tracer {
	/**
	 * @return The line number of the code that ran this method
	 * @author Brian_Entei
	 */
	public static int getLineNumber() {
		return ___8drrd3148796d_Xaf();
	}

	/**
	 * This methods name is ridiculous on purpose to prevent any other method
	 * names in the stack trace from potentially matching this one.
	 *
	 * @return The line number of the code that called the method that called
	 * this method(Should only be called by getLineNumber()).
	 * @author Brian_Entei
	 */
	private static int ___8drrd3148796d_Xaf() {
		boolean thisOne = false;
		int thisOneCountDown = 1;
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : elements) {
			String methodName = element.getMethodName();
			int lineNum = element.getLineNumber();
			if (thisOne && (thisOneCountDown == 0)) {
				return lineNum;
			} else if (thisOne) {
				thisOneCountDown--;
			}
			if (methodName.equals("___8drrd3148796d_Xaf")) {
				thisOne = true;
			}
		}
		return -1;
	}
}
