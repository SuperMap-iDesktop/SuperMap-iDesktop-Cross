package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;
import org.apache.commons.lang.ClassUtils;

/**
 * Created by highsad on 2017/7/27.
 */
public class WorkflowUtil {
	private WorkflowUtil() {
		// 工具类，不提供构造函数
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
