package com.supermap.desktop.event;


/**
 * @author XiaJT
 */
public class DesktopRuntimeEvent<T> {

	private StackTraceElement[] stackTrace = null;
	private String type = START;

	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String EXCEPTION = "exception";
	public static final String CANCLE = "cancle";
	private T currentObject;

	public DesktopRuntimeEvent() {

	}

	public DesktopRuntimeEvent(T currentObject, StackTraceElement[] stackTrace, String type) {
		this.currentObject = currentObject;
		this.stackTrace = stackTrace;
		this.type = type;
	}


	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	public void setType(String start) {
		this.type = start;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public String getType() {
		return type;
	}

	public T getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(T currentObject) {
		this.currentObject = currentObject;
	}
}
