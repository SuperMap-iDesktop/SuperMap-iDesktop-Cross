package com.supermap.desktop.process.parameter;

import javax.swing.*;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IParameters {

	void setParameters(IParameter[] iParameters);

	IParameter[] getParameters();

	IParameter getParameter(String key);

	IParameter getParameter(int index);

	int size();

	JPanel getPanel();
}
