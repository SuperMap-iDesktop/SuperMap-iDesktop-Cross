package com.supermap.desktop.process.loader;

/**
 * Created by highsad on 2017/8/1.
 */
public class ProcessDescriptor {
	private String className;
	private String key;
	private String serialID;

	public String getClassName() {
		return className;
	}

	public String getKey() {
		return key;
	}

	public String getSerialID() {
		return serialID;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setSerialID(String serialID) {
		this.serialID = serialID;
	}
}
