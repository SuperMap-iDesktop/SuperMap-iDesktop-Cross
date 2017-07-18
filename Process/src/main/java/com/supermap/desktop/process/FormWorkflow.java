package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormWorkflow;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmDialogFormSaveAs;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.FormActivatedListener;
import com.supermap.desktop.event.FormClosedEvent;
import com.supermap.desktop.event.FormClosedListener;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.event.FormClosingListener;
import com.supermap.desktop.event.FormDeactivatedListener;
import com.supermap.desktop.event.FormShownEvent;
import com.supermap.desktop.event.FormShownListener;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.graphics.ScrollGraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.process.graphics.events.GraphRemovingListener;
import com.supermap.desktop.process.graphics.events.GraphSelectChangedListener;
import com.supermap.desktop.process.graphics.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.interaction.canvas.CanvasActionProcessEvent;
import com.supermap.desktop.process.graphics.interaction.canvas.CanvasActionProcessListener;
import com.supermap.desktop.process.graphics.interaction.canvas.GraphDragAction;
import com.supermap.desktop.process.graphics.interaction.canvas.Selection;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.process.tasks.TasksManager;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/1/6.
 */
public class FormWorkflow extends FormBaseChild implements IFormWorkflow {
	private Workflow workflow;

	private TasksManager tasksManager;
	private WorkflowCanvas canvas;
	private boolean isNeedSave = true;

	public FormWorkflow() {
		this(ControlsProperties.getString("String_WorkFlows"));
	}


	public FormWorkflow(String name) {
		super(name, null, null);
		if (StringUtilities.isNullOrEmpty(name)) {
			name = ControlsProperties.getString("String_WorkFlows");
		}
		this.title = name;
//		this.canvas = new WorkflowCanvas();
		initializeComponents();
	}


	public FormWorkflow(IWorkflow workflow) {
		super(workflow.getName(), null, null);

		this.workflow = (Workflow) workflow;
		this.canvas = new WorkflowCanvas(this.workflow);
		this.tasksManager = new TasksManager(this.workflow);

		initializeComponents();
		this.setText(workflow.getName());
		isNeedSave = false;
	}

	public TasksManager getTasksManager() {
		return tasksManager;
	}

	private void initializeComponents() {
		setLayout(new BorderLayout());
		ScrollGraphCanvas scrollCanvas = new ScrollGraphCanvas(this.canvas);
		add(scrollCanvas, BorderLayout.CENTER);
		initListeners();
	}

	private void initListeners() {
		this.canvas.getSelection().addGraphSelectChangedListener(new GraphSelectChangedListener() {

			@Override
			public void graphSelectChanged(GraphSelectedChangedEvent e) {
				try {
					ParameterManager component = (ParameterManager) ((Dockbar) Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName("com.supermap.desktop.process.ParameterManager"))).getInnerComponent();
					Selection selection = e.getSelection();
					if (selection.getItem(0) instanceof ProcessGraph) {
						component.setProcess(((ProcessGraph) selection.getItem(0)).getProcess());
					} else {
						component.setProcess(null);
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		this.canvas.getGraphStorage().addGraphRemovingListener(new GraphRemovingListener() {
			@Override
			public void graphRemoving(GraphRemovingEvent e) {
				isNeedSave = true;
			}
		});
		this.canvas.getActionsManager().addCanvasActionProcessListener(new CanvasActionProcessListener() {
			@Override
			public void canvasActionProcess(CanvasActionProcessEvent e) {
				if (e.getAction() instanceof GraphDragAction && e.getStatus() == CanvasActionProcessEvent.START) {
					isNeedSave = true;
				}
			}
		});
	}


	public ArrayList<IGraph> getAllDataNode(Type type) {
		ArrayList<IGraph> iGraphs = new ArrayList<>();
		IGraph[] graphs = getCanvas().getGraphStorage().getGraphs();
		for (IGraph graph : graphs) {
			if (graph instanceof OutputGraph && type.contains(((OutputGraph) graph).getProcessData().getType())) {
				iGraphs.add(graph);
			}
		}
		return iGraphs;
	}

	//region ignore


	@Override
	public WindowType getWindowType() {
		return WindowType.WORK_FLOW;
	}

	@Override
	public boolean save() {
		boolean result = false;
		int index = -1;
		ArrayList<IWorkflow> workFlows = Application.getActiveApplication().getWorkFlows();
		for (IWorkflow workFlow : workFlows) {
			if (workFlow.getName().equals(this.getText())) {
				index = workFlows.indexOf(workFlow);
				Application.getActiveApplication().removeWorkFlow(workFlow);
				break;
			}
		}

		if (index == -1) {
			SmDialogFormSaveAs dialogSaveAs = new SmDialogFormSaveAs();
			dialogSaveAs.setDescription(ProcessProperties.getString("String_NewWorkFlowName"));
			dialogSaveAs.setCurrentFormName(getText());
			for (IWorkflow workFlow : Application.getActiveApplication().getWorkFlows()) {
				dialogSaveAs.addExistNames(workFlow.getName());
			}
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			for (int i = 0; i < formManager.getCount(); i++) {
				if (formManager.get(i) instanceof FormWorkflow && formManager.get(i) != this) {
					dialogSaveAs.addExistNames(formManager.get(i).getText());
				}
			}
			dialogSaveAs.setTitle(ProcessProperties.getString("String_SaveWorkFLow"));
			if (dialogSaveAs.showDialog() == DialogResult.OK) {
				this.setText(dialogSaveAs.getCurrentFormName());
				Application.getActiveApplication().addWorkFlow(getWorkflow());
				result = true;
			}
		} else {
			Application.getActiveApplication().addWorkFlow(index, getWorkflow());
			result = true;
		}
		isNeedSave = !result;
		return result;
	}

	public boolean saveAs(boolean isNewWindow) {
		boolean result = false;
		SmDialogFormSaveAs dialogSaveAs = new SmDialogFormSaveAs();
		dialogSaveAs.setDescription(ProcessProperties.getString("String_NewWorkFlowName"));
		dialogSaveAs.setCurrentFormName(getText());
		for (IWorkflow workFlow : Application.getActiveApplication().getWorkFlows()) {
			dialogSaveAs.addExistNames(workFlow.getName());
		}
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		for (int i = 0; i < formManager.getCount(); i++) {
			if (formManager.get(i) instanceof FormWorkflow) {
				dialogSaveAs.addExistNames(formManager.get(i).getText());
			}
		}
		dialogSaveAs.setTitle(ProcessProperties.getString("Sting_SaveAsWorkFlow"));
		if (dialogSaveAs.showDialog() == DialogResult.OK) {
			this.setText(dialogSaveAs.getCurrentFormName());
			Application.getActiveApplication().addWorkFlow(getWorkflow());
			result = true;
		}
		isNeedSave = !result;
		return result;
	}

	@Override
	public IWorkflow getWorkflow() {
		return this.workflow;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public boolean isNeedSave() {
		return isNeedSave;
	}

	@Override
	public void setNeedSave(boolean needSave) {
		isNeedSave = needSave;
	}

	@Override
	public boolean isActivated() {
		return false;
	}

	@Override
	public void actived() {

	}

	@Override
	public void deactived() {

	}

	@Override
	public void formShown(FormShownEvent e) {
		Application.getActiveApplication().getOutput().output("test");
	}

	@Override
	public void formClosing(FormClosingEvent e) {
		if (!isNeedSave()) {
			return;
		}
		String message = String.format(ControlsProperties.getString("String_SaveProcessPrompt"), getText());
		int result = GlobalParameters.isShowFormClosingInfo() ? UICommonToolkit.showConfirmDialogWithCancel(message) : JOptionPane.NO_OPTION;
		if (result == JOptionPane.YES_OPTION) {
			save();
		} else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
			// 取消关闭操作
			e.setCancel(true);
		}

	}

	@Override
	public void formClosed(FormClosedEvent e) {

	}

	@Override
	public void addFormActivatedListener(FormActivatedListener listener) {

	}

	@Override
	public void removeFormActivatedListener(FormActivatedListener listener) {

	}

	@Override
	public void addFormDeactivatedListener(FormDeactivatedListener listener) {

	}

	@Override
	public void removeFormDeactivatedListener(FormDeactivatedListener listener) {

	}

	@Override
	public void addFormClosingListener(FormClosingListener listener) {

	}

	@Override
	public void removeFormClosingListener(FormClosingListener listener) {

	}

	@Override
	public void addFormClosedListener(FormClosedListener listener) {

	}

	@Override
	public void removeFormClosedListener(FormClosedListener listener) {

	}

	@Override
	public void addFormShownListener(FormShownListener listener) {

	}

	@Override
	public void removeFormShownListener(FormShownListener listener) {

	}

	@Override
	public void clean() {

	}

	@Override
	public boolean isClosed() {
		return false;
	}

	public WorkflowCanvas getCanvas() {
		return this.canvas;
	}

}
