package com.supermap.desktop.process.loader;

/**
 * Created by highsad on 2017/8/10.
 */
public abstract class AbstractProcessLoader implements IProcessLoader {
	private IProcessDescriptor descriptor;

	public AbstractProcessLoader(IProcessDescriptor descriptor) {
		if (descriptor == null) {
			throw new NullPointerException();
		}

		this.descriptor = descriptor;
	}

	@Override

	public IProcessDescriptor getProcessDescriptor() {
		return this.descriptor;
	}
}
