package com.supermap.desktop.Interface;

/**
 * Created by highsad on 2017/8/21.
 */
public interface IDataEntry<T> {
	String getKey();

	void setKey(String key);

	T getValue();

	void setValue(T value);
}
