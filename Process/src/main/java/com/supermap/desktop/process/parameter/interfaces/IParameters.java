package com.supermap.desktop.process.parameter.interfaces;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IParameters {

	void setParameters(IParameter... iParameters);

	IParameter[] getParameters();

	IParameter getParameter(String key);

	IParameter getParameter(int index);

	int size();

	IParameterPanel getPanel();

	IParameterPanel createPanel(IParameter parameter);
}
