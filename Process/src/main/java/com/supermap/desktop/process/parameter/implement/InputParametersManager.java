package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.process.graphics.connection.IConnection;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;
import com.supermap.desktop.process.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.process.graphics.events.GraphRemovingListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.parameter.ParameterDataNode;
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
	private boolean isDeleting = false;

	public InputParametersManager(IParameters parameters) {
		this.parameters = parameters;
	}

	public void add(final String name, final IParameter... parameter) {
		ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.setParameters(parameters);
		final ParameterComboBox parameterComboBox = new ParameterComboBox();
		parameterComboBox.setParameters(parameters);
		parameterComboBox.setDescribe(name + ":");
		reloadParameterComboBox(parameterComboBox);
		parameterComboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelecting && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					Object newValue = evt.getNewValue();
					if (isDeleting) {
						newValue = null;
					}
					firePropertyChangedListener(new PropertyChangeEvent(InputParametersManager.this, name, evt.getOldValue(), newValue));
				}
			}
		});
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormWorkflow) {
			GraphCanvas canvas = ((FormWorkflow) activeForm).getCanvas();
			canvas.addGraphCreatedListener(new GraphCreatedListener() {
				@Override
				public void graphCreated(GraphCreatedEvent e) {
					if (e.getGraph() instanceof OutputGraph && ((OutputGraph) e.getGraph()).getProcessGraph().getProcess() != parameters.getProcess()) {// 不一定都是OutputGraph
						parameterComboBox.addItem(new ParameterDataNode(((OutputGraph) e.getGraph()).getProcessGraph().getTitle() + "_" + ((OutputGraph) e.getGraph()).getTitle(), e.getGraph()));
					}
				}
			});
			canvas.addGraphRemovingListener(new GraphRemovingListener() {
				@Override
				public void graphRemoving(GraphRemovingEvent e) {
					isDeleting = true;
					try {
						IGraph graph = e.getGraph();
						if (graph instanceof OutputGraph) {
							parameterComboBox.removeItem(e.getGraph());
						} else if (graph instanceof ConnectionLineGraph) {
							IConnection connection = ((ConnectionLineGraph) graph).getConnection();
							if (connection.getStart() instanceof OutputGraph && connection.getEnd() instanceof ProcessGraph
									&& ((ProcessGraph) connection.getEnd()).getProcess() == parameters.getProcess()) {
								unBind(connection);
							}
						}
					} catch (Exception e1) {
						Application.getActiveApplication().getOutput().output(e1);
					} finally {
						isDeleting = false;
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

	public void unBind(IConnection connection) {
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
					ParameterComboBox parameterComboBox = (ParameterComboBox) ((ParameterCombine) inputParameterDataNode.getParameterSwitch().getParameterByTag("1")).getParameterList().get(0);
					inputParameterDataNode.getParameterSwitch().switchParameter("1");
					boolean isSelected = false;
					for (int i = 0; i < parameterComboBox.getItemCount(); i++) {
						OutputGraph outputGraph = (OutputGraph) parameterComboBox.getItemAt(i).getData();
						GraphCanvas canvas = outputGraph.getCanvas();
						IGraph[] nextGraphs = canvas.getConnection().getNextGraphs(outputGraph);
						for (IGraph nextGraph : nextGraphs) {
							if (nextGraph instanceof ProcessGraph && ((ProcessGraph) nextGraph).getProcess() == parameters.getProcess()) {
								parameterComboBox.setSelectedItem(parameterComboBox.getItemAt(i));
								isSelected = true;
								break;
							}
						}
						if (isSelected) {
							break;
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

	public void unBind(String name) {
		for (InputParameterDataNode inputParameterDataNode : list) {
			if (inputParameterDataNode.getName().equals(name)) {
				inputParameterDataNode.getParameterSwitch().switchParameter("0");
			}
		}
	}

	private void reloadParameterComboBox(ParameterComboBox parameterComboBox) {
		parameterComboBox.removeAllItems();
		FormWorkflow activeForm = (FormWorkflow) Application.getActiveApplication().getActiveForm();
		ArrayList<IGraph> allDataNode = activeForm.getAllDataNode();
		for (IGraph graph : allDataNode) {
			if (((OutputGraph) graph).getProcessGraph().getProcess() != this.parameters.getProcess())
				parameterComboBox.addItem(new ParameterDataNode(((OutputGraph) graph).getProcessGraph().getTitle() + "_" + ((OutputGraph) graph).getTitle(), graph));
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
