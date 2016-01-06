package com.supermap.desktop.Interface;

import com.supermap.desktop.PluginInfo;

public interface IDefaultValueCreator {

	String getDefaultLabel(String label);

	String getDefaultID(String id);

	Boolean isIDEnabled(String id);

	int getDefaultIndex();

	PluginInfo getDefaultPluginInfo();
}
