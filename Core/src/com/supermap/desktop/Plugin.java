package com.supermap.desktop;

import org.osgi.framework.Bundle;

public class Plugin {

	/**
	 * 根据指定的插件信息构造一个新的 Plugin 对象。
	 * 
	 * @param pluginInfo 创建插件信息
	 */
	public Plugin(String name, Bundle bundle, PluginInfo pluginInfo) {
		this.name = name;
		this.bundle = bundle;
		this.pluginInfo = pluginInfo;
	}

	private String name = "";

	public String getName() {
		return this.name;
	}

	Bundle bundle = null;

	public Bundle getBundle() {
		return this.bundle;
	}

	private String defaultConfigString = "";

	/**
	 * 获取配置文件的内容。默认返回插件初始状态时的配置文件内容。
	 */
	public String getDefaultConfigString() {
		return this.defaultConfigString;
	}

	public void setDefaultConfigString(String defaultConfigString) {
		this.defaultConfigString = defaultConfigString;
	}

	private PluginInfo pluginInfo = null;

	/**
	 * 获取插件信息。
	 */
	public PluginInfo getPluginInfo() {
		return this.pluginInfo;
	}

	/**
	 * 初始化插件。插件初始化时将调用该方法。
	 * 
	 * @return 初始化成功返回 true；否则返回 false。
	 */
	public/* virtual */Boolean initialize() {
		return true;
	}

	/**
	 * 卸载插件。插件被卸载时将调用该方法。
	 * 
	 * @return 卸载成功返回 true；否则返回 false。
	 */
	public/* virtual */Boolean exitInstance() {
		return true;
	}
}
