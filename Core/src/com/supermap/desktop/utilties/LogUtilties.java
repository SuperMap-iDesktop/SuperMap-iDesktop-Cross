package com.supermap.desktop.utilties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Administrator on 2016/3/22.
 */
public class LogUtilties {
	private static final Log log = LogFactory.getLog(LogUtilties.class);

	private static final int classNameLength = 50;
	private static final String separator = "-------------------";

	private LogUtilties() {
	}

	/**
	 * info 输出信息
	 *
	 * @param message 信息
	 */
	public static void outPut(String message) {
		StackTraceElement stackTrace = new Throwable().getStackTrace()[1];
		String[] split = stackTrace.getClassName().split("\\.");
		String classname = split[split.length - 1];
		String info = classname + "." + stackTrace.getMethodName() + "()";
		info = String.format("%-" + classNameLength + "s", info);// 格式化，空格补齐
		log.info(info + " - " + message);
	}

	/**
	 * 输出异常信息
	 *
	 * @param message   输出信息
	 * @param throwable 异常
	 */
	public static void error(String message, Throwable throwable) {
		log.error(message, throwable);
	}

	/**
	 * 输出debug时的信息，debug完后直接替换为outPut输出日志。
	 *
	 * @param message 信息
	 */
	public static void debug(String message) {
		String info = getCurrentInfo(2);
		System.err.println(info + " - " + message);
	}

	/**
	 * 输出debug时的信息，用于异常捕获，debug可以直接删除。
	 *
	 * @param message   输出信息
	 * @param throwable 异常
	 */
	public static void debug(String message, Throwable throwable) {
		String info = getCurrentInfo(2);
		System.err.println(info + " - " + message);
		throwable.printStackTrace();
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
	 * 得到分割符
	 *
	 * @return 分割符
	 */
	public static String getSeparator() {
		return separator;
	}
}
