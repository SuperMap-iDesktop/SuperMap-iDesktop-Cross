package com.supermap.desktop.utilties;

import com.supermap.desktop.GlobalParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author xiajt
 */
public class LogUtilities {
	private static final Log log = LogFactory.getLog(LogUtilities.class);
	private static final boolean isDebug = true;

	private static final int classNameLength = 50;
	private static final String separator = "-------------------";

	private LogUtilities() {
	}

	/**
	 * info 输出信息
	 *
	 * @param message 信息
	 */
	public static void outPut(String message) {
		if (GlobalParameters.isOutPutToLog() && GlobalParameters.isLogInformation()) {
			StackTraceElement stackTrace = new Throwable().getStackTrace()[1];
			String[] split = stackTrace.getClassName().split("\\.");
			String classname = split[split.length - 1];
			String info = classname + "." + stackTrace.getMethodName() + "()";
			info = String.format("%-" + classNameLength + "s", info);// 格式化，空格补齐
			log.info(info + " - " + message);
		}
	}

	/**
	 * 输出异常信息
	 *
	 * @param message   输出信息
	 * @param throwable 异常
	 */

	public static void error(String message, Throwable throwable) {
		if (GlobalParameters.isOutPutToLog() && GlobalParameters.isLogException()) {
			log.error(message, throwable);
		}
	}

	/**
	 * 输出debug时的信息
	 * debug完后直接替换为outPut输出日志。
	 *
	 * @param message 信息
	 */
	public static void debug(String message) {
		if (!LogUtilities.isDebug()) {
			LogUtilities.outPut(message);
		} else {
			String info = getCurrentInfo(2);
			System.err.println(info + " - " + message);
		}
	}

	/**
	 * 输出debug时的信息，用于异常捕获。
	 * debug完成后直接删除。
	 *
	 * @param message   输出信息
	 * @param throwable 异常
	 */
	public static void debug(String message, Throwable throwable) {
		if (!LogUtilities.isDebug()) {
			LogUtilities.error(message, throwable);
		} else {
			String info = getCurrentInfo(2);
			System.err.println(info + " - " + message);
			throwable.printStackTrace();
		}
	}

	/**
	 * 得到拼接成的信息
	 *
	 * @param methodCount 需要得到的调用处到当前方法经过的方法数
	 * @return 类名.方法名（文件名：行号）
	 */
	private static String getCurrentInfo(int methodCount) {
		StackTraceElement stackTrace = new Throwable().getStackTrace()[methodCount];
		String[] split = stackTrace.getClassName().split("\\.");
		String classname = split[split.length - 1];
		return classname + "." + stackTrace.getMethodName() + "(" + stackTrace.getFileName() + ":" + stackTrace.getLineNumber() + ")  ";
	}

	/**
	 * 是否处于debug模式
	 *
	 * @return
	 */
	private static boolean isDebug() {
		return isDebug;
	}

	/**
	 * 得到分割符
	 *
	 * @return 分割符
	 */
	public static String getSeparator() {
		return separator;
	}
}
