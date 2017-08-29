package com.supermap.desktop.process.loader;

import com.supermap.desktop.utilities.StringUtilities;

import java.util.Map;

/**
 * Created by highsad on 2017/8/10.
 */
public abstract class AbstractProcessLoader implements IProcessLoader {
	private final static int DEFAULT_INDEX = 9999;
	public final static String PROPERTY_CLASS_NAME = "ClassName";
	public final static String PROPERTY_KEY = "Key";
	public final static String PROPERTY_TITLE = "Title";
	public final static String PROPERTY_INDEX = "Index";

	private Map<String, String> properties;
	private int index;
	private String className;
	private String key;
	private String title;

	public AbstractProcessLoader(Map<String, String> properties, String index) {
		if (properties == null) {
			throw new NullPointerException();
		}

		if (!properties.containsKey(PROPERTY_CLASS_NAME) || !properties.containsKey(PROPERTY_KEY)) {
			throw new IllegalArgumentException();
		}

		this.properties = properties;
		this.className = properties.get(PROPERTY_CLASS_NAME);
		this.key = properties.get(PROPERTY_KEY);

		if (StringUtilities.isNullOrEmpty(this.className) || StringUtilities.isNullOrEmpty(this.key)) {
			throw new NullPointerException();
		}

		if (this.properties.containsKey(PROPERTY_TITLE)) {
			this.title = this.properties.get(PROPERTY_TITLE);
		} else {
			this.title = this.key;
		}

		try {
			this.index = Integer.valueOf(index);
		} catch (Exception e) {
			this.index = DEFAULT_INDEX;
		}
	}

	@Override
	public Map<String, String> getProperties() {
		return this.properties;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	public String getClassName() {
		return this.className;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getTitle() {
		return this.title;
	}
}
