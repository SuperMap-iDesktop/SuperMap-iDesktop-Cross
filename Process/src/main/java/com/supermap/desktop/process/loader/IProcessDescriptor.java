package com.supermap.desktop.process.loader;

import java.util.Map;

/**
 * Created by highsad on 2017/8/5.
 */
public interface IProcessDescriptor {
	String getClassName();

	String getTitle();

	String getKey();

	int getIndex();

	/**
	 * 使用 properties 属性集初始化自身属性
	 *
	 * @param properties
	 */
	void init(Map<String, String> properties);
}
