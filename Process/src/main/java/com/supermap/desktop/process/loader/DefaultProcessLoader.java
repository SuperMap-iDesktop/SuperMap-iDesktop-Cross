package com.supermap.desktop.process.loader;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/1/5.
 */
public class DefaultProcessLoader implements IProcessLoader {

	@Override
	public IProcess loadProcess(ProcessDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(descriptor.getClassName())) {
			return null;
		}

		IProcess process = null;

		if (StringUtilities.isNullOrEmpty(descriptor.getClassName())) {
			return null;
		}

		Class classInstance;
		try {
			classInstance = Class.forName(descriptor.getClassName());
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
