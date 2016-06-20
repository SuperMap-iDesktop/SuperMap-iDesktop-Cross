package com.supermap.desktop;

import java.io.File;

import org.w3c.dom.Element;

import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.XmlUtilities;

public class WorkEnvironment {

	public final static String g_FileConfigExt = "*.config";

	/**
	 * 获取或设置工作环境的名称。
	 */
	private String name = "";

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取工作环境管理器。
	 */
	private WorkEnvironmentManager manager = null;

	public WorkEnvironmentManager getWorkEnvironmentManager() {
		return this.manager;
	}

	/**
	 * 获取工作环境对象是否是当前程序使用的活动工作环境。
	 */
	private Boolean actived;

	public Boolean getActived() {
		return this.actived;
	}

	/**
	 * 当前工作场景是否被加载了
	 */
	private Boolean isLoadded = false;

	public Boolean getIsLoadded() {
		return this.isLoadded;
	}

	private PluginInfoCollection pluginInfos = null;

	public PluginInfoCollection getPluginInfos() {
		return this.pluginInfos;
	}

	public WorkEnvironment(String name) {
		this.name = name;
		this.pluginInfos = new PluginInfoCollection();
		this.isLoadded = false;
	}

	public WorkEnvironment(WorkEnvironment workEnvironment) {
		this.actived = workEnvironment.getActived();
		this.isLoadded = workEnvironment.getIsLoadded();
		this.name = workEnvironment.getName();
		this.manager = workEnvironment.getWorkEnvironmentManager();
		this.pluginInfos = new PluginInfoCollection();
		for (int i = 0; i < workEnvironment.getPluginInfos().size(); i++) {
			PluginInfo pluginInfo = workEnvironment.getPluginInfos().get(i);
			this.pluginInfos.add(pluginInfo);
		}
		this.pluginInfos.mergeUIElements();
	}

	public WorkEnvironment(WorkEnvironmentManager manager, String name) {
		this.manager = manager;
		if (!this.manager.isWorkEnvironmentNameValid(name)) {
			// do nothing
		}

		this.pluginInfos = new PluginInfoCollection();

		this.name = name;
		this.isLoadded = false;
	}

	/**
	 * 加载工作环境
	 */
	public Boolean load() {
		try {
			if (!this.pluginInfos.getIsLoadded()) {
				initializePluginInfos();
			}

			loadWorkEnvironment();

			this.isLoadded = true;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return !pluginInfos.isEmpty();
	}

	public void initializePluginInfos() {
		this.pluginInfos.clear();
		// 查找所有默认工作场景下的插件配置文件
		String workEnvironmentPath = PathUtilities.getFullPathName(_XMLTag.g_FolderWorkEnvironment, true);
		String[] pathPrams = new String[] { workEnvironmentPath, getName() };
		workEnvironmentPath = PathUtilities.combinePath(pathPrams, true);

		File file = new File(workEnvironmentPath);
		File[] childFiles = file.listFiles();
		for (int j = 0; j < childFiles.length; j++) {
			// 读取配置文件字符串也用XML的接口去读，同时也要有我们的命名空间
			pathPrams = new String[] { workEnvironmentPath, childFiles[j].getName() };
			String configFile = PathUtilities.combinePath(pathPrams, false);
			Element pluginElement = XmlUtilities.getRootNode(configFile);

			if (pluginElement != null) {
				try {
					PluginInfo pluginInfo = new PluginInfo(pluginElement);
					if (pluginInfo.IsValid()) {
						pluginInfo.setConfigLocation(configFile);
						pluginInfo.parseUI();

						this.pluginInfos.add(pluginInfo);
					}
				} catch (Exception ex) {
					Application.getActiveApplication().getOutput().output(ex);
				}
			}
		}

		this.pluginInfos.mergeUIElements();
	}

	public void mergeUIElements() {
		this.pluginInfos.mergeUIElements();
	}

	/**
	 * 卸载当前工作环境中的插件
	 * 
	 * @param isSaveConfig 是否保存工作环境的配置文件
	 */
	public void unLoad(Boolean isSaveConfig) {
		this.actived = false;
		ProcessPlugin.unloadPlugins(isSaveConfig, true);
		this.isLoadded = false;
	}

	/**
	 * 工作环境类输出字符串，目前输出的是工作环境对象的名称。
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	public void toXML() {
		for (int i = 0; i < this.pluginInfos.size(); i++) {
			PluginInfo pluginInfo = this.pluginInfos.get(i);
			if (pluginInfo != null) {
				pluginInfo.toXML();
			}
		}
	}

	public void saveChange() {
		// do nothing
	}

	/**
	 * 根据当前的活动工作场景，重新装载界面
	 */
	private void loadWorkEnvironment() {
		try {
			// 如果在 Application.Initialize() 之前设置当前工作环境，会导致插件被加载两次，这里判断写再卸载掉
			if (Application.getActiveApplication().getPluginManager().getCount() > 0) {
				unLoad(false);
			}

			for (int i = 0; i < this.pluginInfos.size(); i++) {
				PluginInfo info = this.pluginInfos.get(i);
				if (info.getEnable()) {
					Plugin plugin = CreatePluginFromInfo(info);
					if (plugin != null) {
						Application.getActiveApplication().getPluginManager().load(plugin, -1);
						if (Application.getActiveApplication().getOutput() != null) {
							File file = new File(info.getName());
							Application.getActiveApplication().getOutput().output(file.getName());
						}
					}
				}
			}

			if (Application.getActiveApplication().getMainFrame() != null) {
				Application.getActiveApplication().getMainFrame().getFormManager().closeAll();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private Plugin CreatePluginFromInfo(PluginInfo pluginInfo) {
		Plugin plugin = null;
		return plugin;
	}
}
