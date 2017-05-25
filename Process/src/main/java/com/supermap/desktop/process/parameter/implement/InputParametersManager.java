package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * @author XiaJT
 */
public class InputParametersManager {
	private IParameters parameters;
	private ArrayList<InputParameterDataNode> list = new ArrayList<>();
	private boolean isSelecting = false;
	private ArrayList<PropertyChangeListener> listeners = new ArrayList<>();

	public InputParametersManager(IParameters parameters) {
		this.parameters = parameters;
	}

	public void add(final String name, IParameter... parameter) {
		ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.setParameters(parameters);
		ParameterComboBox parameterComboBox = new ParameterComboBox();
		parameterComboBox.setParameters(parameters);
		parameterComboBox.setDescribe(ProcessProperties.getString("String_Source"));
		parameterComboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelecting && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					firePropertyChangedListener(new PropertyChangeEvent(InputParametersManager.this, name, evt.getOldValue(), evt.getNewValue()));
				}
			}
		});
		if (parameter.length == 1) {
			parameterSwitch.add("0", parameter[0]);
		} else {
			ParameterCombine parameterCombine = new ParameterCombine();
			parameterCombine.setParameters(parameters);
			parameterCombine.addParameters(parameter);
			parameterSwitch.add("0", parameterCombine);
		}
		parameterSwitch.add("1", parameterComboBox);

		InputParameterDataNode inputParameterDataNode = new InputParameterDataNode(name, parameterSwitch, parameter);
		list.add(inputParameterDataNode);
	}

	private void firePropertyChangedListener(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener listener : listeners) {
			listener.propertyChange(propertyChangeEvent);
		}
	}

	public void addPropertyChangedListener(PropertyChangeListener listener) {
		this.listeners.add(listener);
	}

	public void bind(InputData inputData) {
		try {
			String name = inputData.getName();
			isSelecting = true;
			for (InputParameterDataNode inputParameterDataNode : list) {
				if (inputParameterDataNode.getName().equals(name)) {
					ParameterComboBox parameterComboBox = (ParameterComboBox) inputParameterDataNode.getParameterSwitch().getParameterByTag("1");
					reloadParameterComboBox(parameterComboBox);
					inputParameterDataNode.getParameterSwitch().switchParameter("1");

					for (int i = 0; i < parameterComboBox.getItemCount(); i++) {
						if (parameterComboBox.getItemAt(i).getDescribe().equals(name)) {
							parameterComboBox.setSelectedItem(parameterComboBox.getItemAt(i));
						}
					}
					break;
				}
			}
			isSelecting = false;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void reloadParameterComboBox(ParameterComboBox parameterComboBox) {
		parameterComboBox.removeAllItems();
		FormProcess activeForm = (FormProcess) Application.getActiveApplication().getActiveForm();
		ArrayList<IGraph> allDataNode = activeForm.getAllDataNode();
		for (IGraph graph : allDataNode) {
			parameterComboBox.addItem(new ParameterDataNode(((OutputGraph) graph).getName(), graph));
		}
	}

	class InputParameterDataNode {
		String name;
		IParameter[] parameter;
		ParameterSwitch parameterSwitch;

		public InputParameterDataNode(String name, ParameterSwitch parameterSwitch, IParameter... parameter) {
			this.name = name;
			this.parameter = parameter;
			this.parameterSwitch = parameterSwitch;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public IParameter[] getParameter() {
			return parameter;
		}

		public void setParameter(IParameter[] parameter) {
			this.parameter = parameter;
		}

		public ParameterSwitch getParameterSwitch() {
			return parameterSwitch;
		}

		public void setParameterSwitch(ParameterSwitch parameterSwitch) {
			this.parameterSwitch = parameterSwitch;
		}
	}
}
