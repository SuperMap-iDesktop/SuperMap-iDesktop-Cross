package com.supermap.desktop;

import com.supermap.desktop.utilties.PathUtilities;
import com.supermap.desktop.utilties.XmlUtilities;
import org.osgi.framework.Bundle;
import org.w3c.dom.Element;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

public class PluginManager {

	private ArrayList<Plugin> plugins = null;

	public PluginManager() {
		this.plugins = new ArrayList<Plugin>();
	}

	/**
	 * 获取插件管理器中指定索引的插件对象。
	 * 
	 * @param index 插件索引
	 * @return 指定索引的插件
	 */
	public Plugin get(int index) {
		Plugin plugin = null;
		try {
			plugin = this.plugins.get(index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
			plugin = null;
		}
		return plugin;
	}

	/**
	 * 获取插件管理器中指定索引的插件对象。
	 *
	 * @return 指定索引的插件
	 */
	public Plugin get(String pluginName) {
		Plugin plugin = null;

		try {
			for (int i = 0; i < getCount(); i++) {
				if (this.plugins.get(i).getPluginInfo().getBundleName().equals(pluginName)) {
					plugin = this.plugins.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
			plugin = null;
		}

		return plugin;
	}

	/**
	 * 获取PluginManager中所包含的插件总数。
	 * 
	 * @return
	 */
	public int getCount() {
		return this.plugins.size();
	}

	/**
	 * 添加插件
	 */
	public int addPlugin(String name, Bundle bundle) {

		int index = -1;
		try {
			if (this.plugins == null) {
				this.plugins = new ArrayList<Plugin>();
			}

			Plugin plugin = getBundle(name);
			if (plugin == null) {
				// 查找所有默认工作场景下的插件配置文件
				String workEnvironmentPath = PathUtilities.getFullPathName(_XMLTag.g_FolderWorkEnvironment, true);
				WorkEnvironment workEnvironment = Application.getActiveApplication().getWorkEnvironmentManager().getActiveWorkEnvironment();
				String[] pathPrams = new String[] { workEnvironmentPath, workEnvironment.getName(), name + ".config" };
				String configFile = PathUtilities.combinePath(pathPrams, false);

				File file = new File(configFile);
				PluginInfo pluginInfo = null;
				if (file.exists()) {
					Element pluginElement = XmlUtilities.getRootNode(configFile);
					if (pluginElement != null) {
						pluginInfo = new PluginInfo(pluginElement);
						if (pluginInfo.IsValid()) {
							pluginInfo.setConfigLocation(configFile);
							pluginInfo.parseUI();
							if (workEnvironment != null) {
								workEnvironment.getPluginInfos().add(pluginInfo);
							}
						}
					}
				}

				plugin = new Plugin(name, bundle, pluginInfo);
				this.plugins.add(plugin);
				index = this.plugins.size();
			}

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return index;
	}

	public Plugin getBundle(String name) {
		Plugin plugin = null;
		try {
			for (int i = 0; i < this.plugins.size(); i++) {
				if (this.plugins.get(i).getName().equalsIgnoreCase(name)) {
					plugin = this.plugins.get(i);
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return plugin;
	}

	/**
	 * 获取指定的插件类
	 */
	public Class<?> getBundleClass(String pluginName, String className) {
		Class<?> result = null;
		try {
			Plugin plugin = getBundle(pluginName);
			if (plugin != null) {
				result = getBundleClass(plugin.getBundle(), className);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 获取指定的插件类
	 */
	public Class<?> getBundleClass(Bundle bundle, String className) {
		Class<?> result = null;
		try {
			if (bundle != null && className != null && className != "") {

				// 搜索Bundle中所有的class文件
				Enumeration<URL> classFileEntries = bundle.findEntries("/", className + ".class", true);
				if (classFileEntries == null || !classFileEntries.hasMoreElements()) {
					// do nothing
				} else {
					// 得到其中的一个类文件的URL
					URL url = classFileEntries.nextElement();
					// 得到路径信息
					String bundleOneClassName = url.getPath();
					// 将"/"替换为"."，得到类名称
					bundleOneClassName = bundleOneClassName.replace("/", ".").substring(0, bundleOneClassName.lastIndexOf("."));
					// 如果类名以"."开头，则移除这个点
					while (bundleOneClassName.startsWith(".")) {
						bundleOneClassName = bundleOneClassName.substring(1);
					}
					while (bundleOneClassName.startsWith("bin")) {
						bundleOneClassName = bundleOneClassName.substring(4);
					}

					// 让Bundle加载这个类
					if (bundleOneClassName != null) {
						result = bundle.loadClass(bundleOneClassName);
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return result;
	}

	// index为-1的时候加到最后
	public void load(Plugin plugin, int index) {
		try {
			// 对于同一个插件加载多次的问题需要处理一下
			// 除了同一个对象多次加载外，还有就是配置文件被复制的情况，暂时还未处理
			if (!this.plugins.contains(plugin) && plugin.initialize()) {
				if (index == -1) {
					this.plugins.add(plugin);
				} else {
					this.plugins.add(index, plugin);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
			this.plugins.remove(plugin);
		}
	}

	public void unload(Plugin plugin, Boolean isSaveConfig) {
		try {
			if (isSaveConfig && plugin != null) {
				ProcessPlugin.saveToFile(plugin.getPluginInfo());
			}
			plugin.exitInstance();
			this.plugins.remove(plugin);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
