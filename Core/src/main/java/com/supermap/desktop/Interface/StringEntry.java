package com.supermap.desktop.Interface;

/**
 * Created by highsad on 2017/8/21.
 */
public class StringEntry implements IDataEntry<String> {
	private String name;
	private String value;

	public StringEntry(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getKey() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}
}
