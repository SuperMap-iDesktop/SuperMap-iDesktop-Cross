package com.supermap.desktop;

import java.util.ArrayList;

public class ProcessPlugin {

	public static ArrayList<PluginInfo> processConfig() {
		ArrayList<PluginInfo> listInfos = new ArrayList<PluginInfo>();

		return listInfos;
	}

	private ProcessPlugin() {
		// 工具类不提供构造函数
	}

	// / <summary>
	// / 卸载当前工作环境中的插件
	// / </summary>
	// / <param name="isSaveConfig">是否保存工作环境的配置文件</param>
	// / <param name="reloadDockBar">是否重新加载浮动窗口</param>
	static public void unloadPlugins(Boolean isSaveConfig, Boolean reloadDockBar) {
		// do nothing
	}

	public static Plugin createPluginFromConfigFile() {
		Plugin plugin = null;
		return plugin;
	}

	public static void saveToFile(PluginInfo pluginfo) {
		// do nothing
	}
}