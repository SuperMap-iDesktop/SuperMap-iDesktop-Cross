package com.supermap.desktop.process.loader;

import java.util.Map;

/**
 * Created by highsad on 2017/8/5.
 */
public interface IProcessDescriptor {
	String getClassName();

	String getKey();

	void init(Map<String, String> properties);
}
