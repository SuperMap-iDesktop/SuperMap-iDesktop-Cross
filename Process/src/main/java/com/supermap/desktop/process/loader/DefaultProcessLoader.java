package com.supermap.desktop.process.loader;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.Map;

/**
 * Created by highsad on 2017/1/5.
 */
public class DefaultProcessLoader extends AbstractProcessLoader {

	public DefaultProcessLoader(Map<String, String> properties, String index) {
		super(properties, index);
	}

	@Override
	public IProcess loadProcess() {
		if (getProperties() == null) {
			return null;
		}

		IProcess process = null;
		if (StringUtilities.isNullOrEmpty(getClassName())) {
			return null;
		}

		Class classInstance = Application.getActiveApplication().getPluginManager().loadClass(getClassName());

		if (classInstance == null) {
			return null;
		}

		if (!IProcess.class.isAssignableFrom(classInstance)) {
			return null;
		}

		try {
			process = (IProcess) classInstance.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return process;
	}
}
