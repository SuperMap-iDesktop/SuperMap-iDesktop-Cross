package com.supermap.desktop.process.parameter.interfaces;

import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

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

	void addInputParameters(String name, Type type, IParameter... parameters);

	void addOutputParameters(String name, Type type, IParameter... parameters);

	Inputs getInputs();

	Outputs getOutputs();
}
