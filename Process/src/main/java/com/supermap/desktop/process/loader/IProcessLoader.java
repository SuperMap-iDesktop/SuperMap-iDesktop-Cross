package com.supermap.desktop.process.loader;

import com.supermap.desktop.process.core.IProcess;

import java.util.Map;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcessLoader {
	Map<String, String> getProperties();

	int getIndex();

	String getKey();

	String getTitle();

	IProcess loadProcess();
}
