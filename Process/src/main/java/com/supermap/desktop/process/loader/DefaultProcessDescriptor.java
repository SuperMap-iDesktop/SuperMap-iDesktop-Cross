package com.supermap.desktop.process.loader;

import java.util.Map;

/**
 * Created by highsad on 2017/8/1.
 */
public class DefaultProcessDescriptor implements IProcessDescriptor {
	private final static int DEFAULT_INDEX = 9999;
	public final static String PROPERTY_CLASS_NAME = "ClassName";
	public final static String PROPERTY_KEY = "Key";
	public final static String PROPERTY_TITLE = "Title";
	public final static String PROPERTY_INDEX = "Index";

	private String className;
	private String key;
	private String title;
	private int index;

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void init(Map<String, String> properties) {
		if (properties.containsKey(PROPERTY_CLASS_NAME)) {
			this.className = properties.get(PROPERTY_CLASS_NAME);
		}

		if (properties.containsKey(PROPERTY_KEY)) {
			this.key = properties.get(PROPERTY_KEY);
		}

		if (properties.containsKey(PROPERTY_TITLE)) {
			this.title = properties.get(PROPERTY_TITLE);
		}

		if (properties.containsKey(PROPERTY_INDEX)) {
			String indexStr = properties.get(PROPERTY_INDEX);
			try {
				this.index = Integer.valueOf(indexStr);
			} catch (Exception e) {
				this.index = DEFAULT_INDEX;
			}
		}
	}
}
