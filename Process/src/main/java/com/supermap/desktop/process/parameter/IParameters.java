package com.supermap.desktop.process.parameter;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IParameters {

	IParameter[] getParameters();

	IParameter getParameter(String key);

	IParameter getParameter(int index);

	int size();
}
