package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.process.graphics.connection.IGraphConnection;
import com.supermap.desktop.process.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.process.graphics.events.GraphRemovingListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.storage.IConnectionManager;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;

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

	public void add(final String name, final IParameter... parameter) {
		ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.setParameters(parameters);
		final ParameterInputComboBox parameterComboBox = new ParameterInputComboBox(parameters.getInputs().getData(name).getType(), name);
		parameterComboBox.setParameters(parameters);
		parameterComboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelecting && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					Object newValue = evt.getNewValue();
					if (parameterComboBox.isDeleting()) {
						newValue = null;
					}
					firePropertyChangedListener(new PropertyChangeEvent(InputParametersManager.this, name, evt.getOldValue(), newValue));
				}
			}
		});
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormWorkflow) {
			GraphCanvas canvas = ((FormWorkflow) activeForm).getCanvas();
			canvas.addGraphRemovingListener(new GraphRemovingListener() {
				@Override
				public void graphRemoving(GraphRemovingEvent e) {
					IGraph graph = e.getGraph();
					if (graph instanceof ConnectionLineGraph) {
						IGraphConnection connection = ((ConnectionLineGraph) graph).getConnection();
						if (connection.getStart() instanceof OutputGraph && connection.getEnd() instanceof ProcessGraph
								&& ((ProcessGraph) connection.getEnd()).getProcess() == parameters.getProcess()) {
							unBind(connection);
						}
					}
				}
			});
		}
		ParameterCombine combine = new ParameterCombine();
		combine.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		combine.addParameters(parameterComboBox);
		ArrayList<IParameter> sources = new ArrayList<>();
		if (parameter.length == 1) {
			parameterSwitch.add("0", parameter[0]);
		} else {
			ParameterCombine parameterCombine = new ParameterCombine();
			parameterCombine.setParameters(parameters);
			parameterCombine.addParameters(parameter);
			parameterSwitch.add("0", parameterCombine);
		}
		parameterSwitch.add("1", combine);
		parameterSwitch.setParameters(parameters);

		Collections.addAll(sources, parameter);
		parameters.replace(sources, parameterSwitch);

		InputParameterDataNode inputParameterDataNode = new InputParameterDataNode(name, parameterSwitch, parameter);
		list.add(inputParameterDataNode);
	}

	public void unBind(IGraphConnection connection) {
		String name = connection.getMessage();
		for (InputParameterDataNode inputParameterDataNode : list) {
			if (inputParameterDataNode.getName().equals(name)) {
				inputParameterDataNode.getParameterSwitch().switchParameter("0");
				parameters.getInputs().unbind(name);
				break;
			}
		}
	}

	private void firePropertyChangedListener(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener listener : listeners) {
			listener.propertyChange(propertyChangeEvent);
		}
	}

	public void addPropertyChangedListener(PropertyChangeListener listener) {
		this.listeners.add(listener);
	}

	public void bind(String name) {
		try {
			isSelecting = true;
			for (InputParameterDataNode inputParameterDataNode : list) {
				if (inputParameterDataNode.getName().equals(name)) {
					ParameterInputComboBox parameterComboBox = (ParameterInputComboBox) ((ParameterCombine) inputParameterDataNode.getParameterSwitch().getParameterByTag("1")).getParameterList().get(0);
					inputParameterDataNode.getParameterSwitch().switchParameter("1");
					if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof FormWorkflow) {
						GraphCanvas canvas = ((FormWorkflow) Application.getActiveApplication().getActiveForm()).getCanvas();
						IConnectionManager connection = canvas.getConnection();
						IGraphConnection[] connections = connection.getConnections();
						for (IGraphConnection iGraphConnection : connections) {
							if (iGraphConnection.getEndGraph() instanceof ProcessGraph && ((ProcessGraph) iGraphConnection.getEndGraph()).getProcess() == parameterComboBox.getParameters().getProcess()
									&& iGraphConnection.getMessage().equals(parameterComboBox.getInputDataName())) {
								parameterComboBox.setSelectedGraph(iGraphConnection.getStartGraph());
								break;
							}
						}
					}
				}
			}
			isSelecting = false;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	public void unBind(String name) {
		for (InputParameterDataNode inputParameterDataNode : list) {
			if (inputParameterDataNode.getName().equals(name)) {
				inputParameterDataNode.getParameterSwitch().switchParameter("0");
			}
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
