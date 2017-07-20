package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.FormWorkflow;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.WorkflowCanvas;
import com.supermap.desktop.process.core.DataMatch;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.events.*;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author XiaJT
 */
public class InputParametersManager {
	private IParameters parameters;
	private boolean isSelecting = false;
	private boolean isDeleting = false;
	private ArrayList<InputParameterDataNode> list = new ArrayList<>();
	private ArrayList<PropertyChangeListener> listeners = new ArrayList<>();

	public InputParametersManager(IParameters parameters) {
		this.parameters = parameters;
	}

	public void add(final String name, final IParameter... parameter) {
		ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.setParameters(parameters);
		final ParameterComboBox parameterComboBox = new ParameterComboBox();
		parameterComboBox.setIConGetter(new IConGetter() {
			private Icon icon = ProcessResources.getIcon("/processresources/ProcessOutputIcon.png");

			@Override
			public Icon getICon(ParameterDataNode parameterDataNode) {
				return icon;
			}
		});
		parameterComboBox.setParameters(parameters);
		parameterComboBox.setDescribe(name + ":");
		reloadParameterComboBox(parameterComboBox, parameters.getInputs().getData(name).getType());
		parameterComboBox.addPropertyListener(new ParameterComboBoxPropertyChangeListener(name));

		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormWorkflow) {
			WorkflowCanvas canvas = ((FormWorkflow) activeForm).getCanvas();
			canvas.getWorkflow().addWorkflowChangeListener(new WorkflowChangeHandler(name, parameterComboBox));
			canvas.getWorkflow().addRelationAddedListener(new RelationAddedHandler());
			canvas.getWorkflow().addRelationRemovingListener(new RelationRemovingHandler());
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

	private void firePropertyChangedListener(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener listener : listeners) {
			listener.propertyChange(propertyChangeEvent);
		}
	}

	public void addPropertyChangedListener(PropertyChangeListener listener) {
		this.listeners.add(listener);
	}

	public void bind(InputData toInput, OutputData fromOutput) {
		try {
			isSelecting = true;
			for (InputParameterDataNode inputParameterDataNode : list) {
				if (inputParameterDataNode.getName().equals(toInput.getName())) {
					ParameterComboBox parameterComboBox = (ParameterComboBox) ((ParameterCombine) inputParameterDataNode.getParameterSwitch().getParameterByTag("1")).getParameterList().get(0);
					inputParameterDataNode.getParameterSwitch().switchParameter("1");

					for (int i = 0; i < parameterComboBox.getItemCount(); i++) {
						OutputData outputData = (OutputData) parameterComboBox.getItemAt(i).getData();
						if (outputData == fromOutput) {
							parameterComboBox.setSelectedItem(parameterComboBox.getItemAt(i));
							break;
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSelecting = false;
		}
	}

	public void unBind(String name) {
		for (InputParameterDataNode inputParameterDataNode : list) {
			if (inputParameterDataNode.getName().equals(name)) {
				inputParameterDataNode.getParameterSwitch().switchParameter("0");
			}
		}
	}

	private void reloadParameterComboBox(ParameterComboBox parameterComboBox, Type type) {
		parameterComboBox.removeAllItems();
		IForm form = Application.getActiveApplication().getActiveForm();
		if (!(form instanceof FormWorkflow)) {
			return;
		}
		FormWorkflow activeForm = (FormWorkflow) form;
		ArrayList<IGraph> allDataNode = activeForm.getAllDataNode(type);
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

	private class WorkflowChangeHandler implements WorkflowChangeListener {
		private String name;
		private ParameterComboBox comboBox;

		public WorkflowChangeHandler(String name, ParameterComboBox comboBox) {
			this.name = name;
			this.comboBox = comboBox;
		}

		@Override
		public void workflowChange(WorkflowChangeEvent e) {
			if (e.getType() == WorkflowChangeEvent.ADDED) {
				if (e.getProcess() != parameters.getProcess()) {
					OutputData[] outputs = e.getProcess().getOutputs().getDatas();

					for (int i = 0; i < outputs.length; i++) {
						OutputData output = outputs[i];

						if (parameters.getInputs().getData(name).getType().contains(output.getType())) {
							this.comboBox.addItem(new ParameterDataNode(e.getProcess().getTitle() + "_" + output.getName(), output));
						}
					}
				}
			} else if (e.getType() == WorkflowChangeEvent.REMOVED) {
				isDeleting = true;

				try {
					IProcess process = e.getProcess();
					OutputData[] outputs = process.getOutputs().getDatas();

					for (int i = 0; i < outputs.length; i++) {
						this.comboBox.removeItem(outputs[i]);
					}
				} catch (Exception e1) {
					Application.getActiveApplication().getOutput().output(e1);
				} finally {
					isDeleting = false;
				}
			}
		}
	}

	private class RelationAddedHandler implements RelationAddedListener<IProcess> {

		@Override
		public void relationAdded(RelationAddedEvent<IProcess> e) {
			if (e.getRelation().getTo() == parameters.getProcess() && e.getRelation() instanceof DataMatch) {
				DataMatch dataMatch = (DataMatch) e.getRelation();
				bind(dataMatch.getToInputData(), dataMatch.getFromOutputData());
			}
		}
	}


	private class RelationRemovingHandler implements RelationRemovingListener<IProcess> {

		@Override
		public void relaitonRemoving(RelationRemovingEvent<IProcess> e) {
			if (e.getRelation().getTo() == parameters.getProcess() && e.getRelation() instanceof DataMatch) {
				DataMatch dataMatch = (DataMatch) e.getRelation();
				unBind(dataMatch.getToInputData().getName());
			}
		}
	}

	private class ParameterComboBoxPropertyChangeListener implements PropertyChangeListener {
		private String name;

		public ParameterComboBoxPropertyChangeListener(String name) {
			this.name = name;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelecting && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
				Object newValue = evt.getNewValue();
				if (isDeleting) {
					newValue = null;
				}
//				firePropertyChangedListener(new PropertyChangeEvent(InputParametersManager.this, this.name, evt.getOldValue(), newValue));

				if (!(Application.getActiveApplication().getActiveForm() instanceof FormWorkflow)) {
					return;
				}

				Workflow workflow = (Workflow) ((FormWorkflow) Application.getActiveApplication().getActiveForm()).getWorkflow();
				OutputData oldValue = (OutputData) evt.getOldValue();

				if (newValue == null) {

					// 约束解除！
					workflow.removeRelation(oldValue.getProcess(), InputParametersManager.this.parameters.getProcess());
				} else {
					OutputData newOutput = (OutputData) newValue;
					DataMatch newRelation = new DataMatch(newOutput.getProcess(), InputParametersManager.this.parameters.getProcess(),
							newOutput.getName(), this.name);
					workflow.addRelation(newRelation);
				}
			}
		}
	}
}
