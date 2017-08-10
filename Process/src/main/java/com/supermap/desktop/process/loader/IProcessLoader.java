package com.supermap.desktop.process.loader;

import com.supermap.desktop.process.core.IProcess;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcessLoader {
	IProcessDescriptor getProcessDescriptor();

	IProcess loadProcess();
}
