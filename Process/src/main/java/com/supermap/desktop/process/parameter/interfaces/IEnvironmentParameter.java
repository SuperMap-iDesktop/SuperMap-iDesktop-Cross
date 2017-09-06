package com.supermap.desktop.process.parameter.interfaces;

/**
 * @author XiaJT
 */
public interface IEnvironmentParameter extends IParameter {
	boolean isModified();

	boolean setAsGlobalEnvironment();

	boolean reset();
}
