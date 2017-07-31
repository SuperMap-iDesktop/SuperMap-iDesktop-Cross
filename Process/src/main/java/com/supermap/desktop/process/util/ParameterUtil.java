package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterClassBundleNode;
import org.osgi.framework.Bundle;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author XiaJT
 */
public class ParameterUtil {
	public static final Dimension LABEL_DEFAULT_SIZE = new Dimension(90, 23);

	public static Class getParameterPanel(String parameterType, ArrayList<ParameterClassBundleNode> packs) {
		for (int i = packs.size() - 1; i >= 0; i--) {
			ParameterClassBundleNode pack = packs.get(i);
			List<Class<?>> classes = getClasses(pack);
			if (classes.size() > 0) {
				for (Class<?> aClass : classes) {
					Class<?>[] interfaces = aClass.getInterfaces();
					if (IParameterPanel.class.isAssignableFrom(aClass)) {
						ParameterPanelDescribe annotation = aClass.getAnnotation(ParameterPanelDescribe.class);
						if (annotation != null && annotation.parameterPanelType().equals(parameterType)) {
							return aClass;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 从包package中获取所有的Class
	 *
	 * @param pack
	 * @return
	 */
	private static List<Class<?>> getClasses(ParameterClassBundleNode pack) {

		// 第一个class类的集合
		java.util.List<Class<?>> classes = new ArrayList<>();
		// 获取包的名字 并进行替换
		String packageName = pack.getPackageName();
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			Bundle bundle = Application.getActiveApplication().getPluginManager().getBundle(pack.getBundleName()).getBundle();
			dirs = bundle.findEntries(packageDirName, "*.class", true);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
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
				classes.add(bundle.loadClass(bundleOneClassName));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

		return classes;
	}
}
