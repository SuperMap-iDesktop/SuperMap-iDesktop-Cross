package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.parameter.ParameterPanels.EmptyParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author XiaJT
 */
public class DefaultParameters implements IParameters {
	private IProcess process;
	protected ArrayList<IParameter> parameters = new ArrayList<>();
	protected JPanel panel;
	private ArrayList<ParameterClassBundleNode> packages = new ArrayList<>();
	protected EmptyParameterPanel parameterPanel = new EmptyParameterPanel();
	private InputParametersManager inputParametersManager = new InputParametersManager(this);

	public DefaultParameters(IProcess process) {
		this.process = process;
		packages.add(new ParameterClassBundleNode("com.supermap.desktop.process.parameter.ParameterPanels", "SuperMap.Desktop.Process"));
	}

	@Override
	public void bindWorkflow(Workflow workflow) {
		this.inputParametersManager.bindWorkflow(workflow);
	}

	@Override
	public void unbindWorkflow(Workflow workflow) {
		this.inputParametersManager.unbindWorkflow(workflow);
	}

	@Override
	public void setParameters(IParameter... iParameters) {
		if (this.parameters != null && this.parameters.size() > 0) {
			for (IParameter parameter : parameters) {
				parameter.dispose();
				parameter.setParameters(null);
			}
		}
		if (panel != null) {
			panel.removeAll();
		}
		panel = null;
		Collections.addAll(parameters, iParameters);
		if (this.parameters != null && this.parameters.size() > 0) {
			for (IParameter parameter : parameters) {
				parameter.setParameters(this);
			}
		}
	}

	@Override
	public void addParameters(IParameter... iParameters) {
		Collections.addAll(parameters, iParameters);
		for (IParameter iParameter : iParameters) {
			iParameter.setParameters(this);
		}
		if (panel != null) {
			panel.removeAll();
		}
		panel = null;
	}


	@Override
	public ArrayList<IParameter> getParameters() {
		return parameters;
	}

	@Override
	public IParameter getParameter(String key) {
		for (IParameter parameter : parameters) {
			if (parameter.getType().equals(key)) {
				return parameter;
			}
		}
		return null;
	}

	@Override
	public IParameter getParameter(int index) {
		if (index >= 0 && index < parameters.size()) {
			return parameters.get(index);
		}
		return null;
	}

	@Override
	public int size() {
		return parameters.size();
	}

	@Override
	public IParameterPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			for (int i = 0; i < parameters.size(); i++) {
				panel.add((JPanel) parameters.get(i).getParameterPanel().getPanel(), new GridBagConstraintsHelper(0, i, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 0, 10));
			}
			panel.add(new JPanel(), new GridBagConstraintsHelper(0, parameters.size(), 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
		}
		parameterPanel.setPanel(panel);
		return parameterPanel;
	}

	@Override
	public IParameterPanel createPanel(IParameter parameter) {
		Class clazz = ParameterUtil.getParameterPanel(parameter.getType(), packages);
		if (clazz != null) {
			try {
				Constructor constructor = clazz.getConstructor(IParameter.class);
				return (IParameterPanel) constructor.newInstance(parameter);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output((parameter.getType() + " can't find panel"));
			}
		}
		return null;
	}

	@Override
	public void addInputParameters(String name, Type type, IParameter... parameters) {
		this.process.getInputs().addData(name, type);
		this.process.getInputs().getData(name).addParameters(parameters);
		inputParametersManager.add(name, parameters);
	}

	@Override
	public void addOutputParameters(String name, Type type, IParameter... parameters) {
		this.process.getOutputs().addData(name, type);
	}

	@Override
	public void replace(ArrayList<IParameter> sources, IParameter... results) {
		int firstIndex = Integer.MAX_VALUE;
		for (IParameter source : sources) {
			int indexOf = parameters.indexOf(source);
			if (indexOf != -1) {
				firstIndex = Math.min(indexOf, firstIndex);
			}
			parameters.remove(source);
		}
		if (firstIndex != Integer.MAX_VALUE) {
			for (IParameter result : results) {
				parameters.add(firstIndex, result);
				firstIndex++;
			}
		}
	}

	@Override
	public IProcess getProcess() {
		return process;
	}

	@Override
	public Inputs getInputs() {
		return this.process.getInputs();
	}

	@Override
	public Outputs getOutputs() {
		return this.process.getOutputs();
	}
}
