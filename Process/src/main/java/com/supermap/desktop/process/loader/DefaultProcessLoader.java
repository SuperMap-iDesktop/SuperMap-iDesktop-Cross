package com.supermap.desktop.process.loader;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * Created by highsad on 2017/1/5.
 */
public class DefaultProcessLoader extends AbstractProcessLoader {

	public DefaultProcessLoader(IProcessDescriptor descriptor) {
		super(descriptor);
	}
//	public final static DefaultProcessLoader INSTANCE = new DefaultProcessLoader();

	@Override
	public IProcess loadProcess() {
		if (getProcessDescriptor() == null) {
			return null;
		}

		if (StringUtilities.isNullOrEmpty(getProcessDescriptor().getClassName())) {
			return null;
		}

		IProcess process = null;

		if (StringUtilities.isNullOrEmpty(getProcessDescriptor().getClassName())) {
			return null;
		}

		Class classInstance = Application.getActiveApplication().getPluginManager().loadClass(getProcessDescriptor().getClassName());

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
