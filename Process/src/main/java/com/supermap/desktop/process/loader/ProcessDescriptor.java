package com.supermap.desktop.process.loader;

/**
 * Created by highsad on 2017/8/1.
 */
public class ProcessDescriptor {
	private String className;
	private String key;

	public ProcessDescriptor(String className, String key) {
		this.className = className;
		this.key = key;
	}

	public String getClassName() {
		return className;
	}

	public String getKey() {
		return key;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
