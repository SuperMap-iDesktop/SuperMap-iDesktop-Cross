package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.core.DataMatch;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IRelation;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.events.*;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XiaJT
 */
public class InputParametersManager {
	private IParameters parameters;
	private boolean isSelecting = false;
	private boolean isDeleting = false;
	private ArrayList<InputParameterDataNode> list = new ArrayList<>();
	private Map<String, ParameterComboBox> paramsMap = new ConcurrentHashMap<>();
	private WorkflowChangeHandler workflowChangeHandler = new WorkflowChangeHandler();
	private RelationAddedHandler relationAddedHandler = new RelationAddedHandler();
	private RelationRemovingHandler relationRemovingHandler = new RelationRemovingHandler();
	private ParameterComboBoxPropertyChangeListener parameterComboBoxPropertyChangeListener = new ParameterComboBoxPropertyChangeListener();

	public InputParametersManager(IParameters parameters) {
		this.parameters = parameters;
	}

	public void bindWorkflow(Workflow workflow) {
		if (workflow == null) {
			return;
		}

		if (workflow != this.parameters.getProcess().getWorkflow()) {
			throw new IllegalArgumentException();
		}

		loadProcesses(workflow.getProcesses());
		loadRelations(workflow.getRelations());
		workflow.addWorkflowChangeListener(this.workflowChangeHandler);
		workflow.addRelationAddedListener(this.relationAddedHandler);
		workflow.addRelationRemovingListener(this.relationRemovingHandler);
	}

	private void loadProcesses(Vector<IProcess> processes) {
		if (processes != null && processes.size() > 0) {
			for (int i = 0; i < processes.size(); i++) {
				loadProcess(processes.get(i));
			}
		}
	}

	private void loadRelations(Vector<IRelation<IProcess>> relations) {
		if (relations != null && relations.size() > 0) {
			for (int i = 0; i < relations.size(); i++) {
				if ((relations.get(i) instanceof DataMatch) && relations.get(i).getTo() == this.parameters.getProcess()) {
					bind(((DataMatch) relations.get(i)).getToInputData(), ((DataMatch) relations.get(i)).getFromOutputData());
				}
			}
		}
	}

	private void loadProcess(IProcess process) {
		if (process != parameters.getProcess()) {
			OutputData[] outputs = process.getOutputs().getDatas();

			for (String name :
					paramsMap.keySet()) {
				ParameterComboBox comboBox = paramsMap.get(name);

				for (int i = 0; i < outputs.length; i++) {
					OutputData output = outputs[i];

					if (parameters.getInputs().getData(name).getType().intersects(output.getType())) {
						comboBox.addItem(new ParameterDataNode(process.getTitle() + "_" + output.getName(), output));
					}
				}
			}
		}
	}

	public void unbindWorkflow(Workflow workflow) {
		if (workflow != null && workflow == parameters.getProcess().getWorkflow()) {
			workflow.removeWorkflowChangeListener(this.workflowChangeHandler);
			workflow.removeRelationAddedListener(this.relationAddedHandler);
			workflow.removeRelationRemovingListener(this.relationRemovingHandler);
			unloadProcesses(workflow.getProcesses());
		}
	}

	private void unloadProcesses(Vector<IProcess> processes) {
		if (processes != null && processes.size() > 0) {
			for (int i = 0; i < processes.size(); i++) {
				unloadProcess(processes.get(i));
			}
		}
	}

	private void unloadProcess(IProcess process) {
		isDeleting = true;

		try {
			OutputData[] outputs = process.getOutputs().getDatas();

			for (String name :
					paramsMap.keySet()) {
				ParameterComboBox comboBox = paramsMap.get(name);

				for (int i = 0; i < outputs.length; i++) {
					comboBox.removeItem(outputs[i]);
				}
			}
		} catch (Exception e1) {
			Application.getActiveApplication().getOutput().output(e1);
		} finally {
			isDeleting = false;
		}
	}

	public void add(final String name, final IParameter... parameter) {
		ParameterSwitch parameterSwitch = new ParameterSwitch();
		parameterSwitch.setParameters(parameters);

		final ParameterComboBox parameterComboBox = new ParameterComboBox();
		parameterComboBox.setIConGetter(newConGetter());
		parameterComboBox.setParameters(parameters);
		parameterComboBox.setDescribe(name + ":");
//		reloadParameterComboBox(parameterComboBox, parameters.getInputs().getData(name).getType());
		parameterComboBox.addPropertyListener(this.parameterComboBoxPropertyChangeListener);
		this.paramsMap.put(name, parameterComboBox);

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

	private IConGetter newConGetter() {
		return new IConGetter() {
			private Icon icon = ProcessResources.getIcon("/processresources/ProcessOutputIcon.png");

			@Override
			public Icon getICon(ParameterDataNode parameterDataNode) {
				return icon;
			}
		};
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

		@Override
		public void workflowChange(WorkflowChangeEvent e) {
			if (e.getType() == WorkflowChangeEvent.ADDED) {
				loadProcess(e.getProcess());
			} else if (e.getType() == WorkflowChangeEvent.REMOVED) {
				unloadProcess(e.getProcess());
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

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelecting && evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
				Object newValue = evt.getNewValue();
				if (isDeleting) {
					newValue = null;
				}

				Workflow workflow = parameters.getProcess().getWorkflow();
				if (workflow == null) {
					return;
				}

				OutputData oldValue = (OutputData) evt.getOldValue();

				if (newValue == null) {

					// 约束解除！
					workflow.removeRelation(oldValue.getProcess(), InputParametersManager.this.parameters.getProcess());
				} else {
					String name = "";
					for (String key :
							paramsMap.keySet()) {
						if (evt.getSource() == paramsMap.get(key)) {
							name = key;
							break;
						}
					}

					if (StringUtilities.isNullOrEmpty(name)) {
						return;
					}

					OutputData newOutput = (OutputData) newValue;
					DataMatch newRelation = new DataMatch(newOutput.getProcess(), InputParametersManager.this.parameters.getProcess(),
							newOutput.getName(), name);
					workflow.addRelation(newRelation);
				}
			}
		}
	}
}
