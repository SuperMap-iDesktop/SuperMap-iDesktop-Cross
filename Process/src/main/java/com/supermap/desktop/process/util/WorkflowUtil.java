package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.utilities.StringUtilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by highsad on 2017/7/27.
 */
public class WorkflowUtil {
	private WorkflowUtil() {
		// 工具类，不提供构造函数
	}

	public static IProcessLoader newProcessLoader(String loaderClassName, Map<String, String> properties, String index) {
		IProcessLoader loader = null;

		if (StringUtilities.isNullOrEmpty(loaderClassName)) {
			return new DefaultProcessLoader(properties, index);
		}

		Class classInstance = Application.getActiveApplication().getPluginManager().loadClass(loaderClassName);

		if (classInstance == null) {
			return null;
		}

		if (!IProcessLoader.class.isAssignableFrom(classInstance)) {
			return null;
		}

		try {
			Constructor constructor = classInstance.getConstructor(Map.class, String.class);
			loader = (IProcessLoader) constructor.newInstance(properties, index);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return loader;
	}

	public static IProcess newProcess(String className) {
		IProcess process = null;

		if (StringUtilities.isNullOrEmpty(className)) {
			return null;
		}

		Class classInstance;
		try {
			classInstance = Class.forName(className);
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
			classInstance = null;
		}

		if (classInstance == null) {
			return null;
		}

		if (!IProcess.class.isAssignableFrom(classInstance)) {
			return null;
		}

		try {
			process = (IProcess) classInstance.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return process;
	}
}
