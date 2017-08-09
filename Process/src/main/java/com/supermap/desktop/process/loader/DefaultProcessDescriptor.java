package com.supermap.desktop.process.loader;

import java.util.Map;

/**
 * Created by highsad on 2017/8/1.
 */
public class DefaultProcessDescriptor implements IProcessDescriptor {
	public final static String PROPERTY_CLASS_NAME = "ClassName";
	public final static String PROPERTY_KEY = "Key";
	public final static String PROPERTY_TITLE = "Title";

	private String className;
	private String key;
	private String title;

	public DefaultProcessDescriptor(String className, String key) {
		this.className = className;
		this.key = key;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void init(Map<String, String> properties) {
		if (properties.containsKey(PROPERTY_CLASS_NAME)) {
			this.className = properties.get(PROPERTY_CLASS_NAME);
		} else if (properties.containsKey(PROPERTY_KEY)) {
			this.key = properties.get(PROPERTY_KEY);
		} else if (properties.containsKey(PROPERTY_TITLE)) {
			this.title = properties.get(PROPERTY_TITLE);
		}
	}
}
