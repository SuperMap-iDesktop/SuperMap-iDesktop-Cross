package com.supermap.desktop.process.loader;

import com.supermap.desktop.process.core.IProcess;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public class DefaultProcessLoader implements IProcessLoader {

	public static final DefaultProcessLoader SINGLETON = new DefaultProcessLoader();

	private DefaultProcessLoader() {
	}

	@Override
	public IProcess[] loadProcesses() {
		return new IProcess[0];
	}
}
