package com.supermap.desktop;

import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkEnvironmentManager {

	private WorkEnvironmentCollection workEnvironments;
	private WorkEnvironment activeWorkEnvironment;
	private String workEnvrionmentRootPath;

	public WorkEnvironmentManager() {
		this.workEnvironments = new WorkEnvironmentCollection(this);
		this.setWorkEnvrionmentRootPath("");
		this.activeWorkEnvironment = null;

		initialize();
	}

	public Boolean initialize() {
		Boolean result = false;
		try {
			// 遍历所有工作环境
			String workEnvironmentPath = PathUtilities.getFullPathName(_XMLTag.g_FolderWorkEnvironment, true);
			File file = new File(workEnvironmentPath);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 遍历所有目录
				List<String> plugins = new ArrayList<String>();

				if (files[i].isDirectory()) {
					String[] pathPrams = new String[] { workEnvironmentPath, files[i].getName() };
					String workEnvironmentPathTemp = PathUtilities.combinePath(pathPrams, true);

					File[] childFiles = files[i].listFiles();
					for (int j = 0; j < childFiles.length; j++) {
						// 读取配置文件字符串也用XML的接口去读，同时也要有我们的命名空间
						pathPrams = new String[] { workEnvironmentPathTemp, childFiles[j].getName() };
						String configFile = PathUtilities.combinePath(pathPrams, false);
						Element pluginElement = XmlUtilities.getRootNode(configFile);
						if (pluginElement != null) {
							PluginInfo pluginInfo = new PluginInfo(pluginElement);
							if (pluginInfo.getBundleName().indexOf("SuperMap.Desktop.LBSClient") != -1)
							{
								int n = 0;
								int m = n;
							}
							if (pluginInfo.IsValid()
									&& (pluginInfo.getBundleName().indexOf("SuperMap.Desktop.Frame") != -1 || pluginInfo.getBundleName().indexOf(
											"SuperMap.Desktop.DataView") != -1)) {
								// // 判断一下工作环境中是否含有必要的插件的配置信息
								plugins.add(pluginInfo.getBundleName());
							}
						}
					}

					if (plugins.size() >= 2) {
						WorkEnvironment item = new WorkEnvironment(files[i].getName());
						this.workEnvironments.add(item);

						if (item.getName().toLowerCase().equalsIgnoreCase(_XMLTag.getG_strWorkEnvironment())) {
							this.activeWorkEnvironment = item;
						}
					}
				}
			}
		} catch (Exception ex) {
			// do nothing
		}

		return result;
	}

	public Boolean isWorkEnvironmentNameValid(String name) {
		Boolean valid = true;

		return valid;
	}

	/**
	 * 获取或设置当前激活的工作环境的名称。
	 */
	public String getActiveWorkEnvironmentName() {
		String name = "";
		if (this.activeWorkEnvironment != null) {
			name = this.activeWorkEnvironment.getName();
		}
		return name;
	}

	public void setActiveWorkEnvironmentName(String name) {
		// do nothing
	}

	/**
	 * 获取或设置当前激活的工作环境。
	 */
	public WorkEnvironment getActiveWorkEnvironment() {
		return this.activeWorkEnvironment;
	}

	public void setActiveWorkEnvironment(WorkEnvironment workEnvironment) {
		// 为了支持重新加载，不作相同工作环境的判断。这个判断可在外面调用前加上。
		// Boolean excute = workEnvironment != null;

	}

	public String getWorkEnvrionmentRootPath() {
		return workEnvrionmentRootPath;
	}

	public void setWorkEnvrionmentRootPath(String workEnvrionmentRootPath) {
		this.workEnvrionmentRootPath = workEnvrionmentRootPath;
	}

}
