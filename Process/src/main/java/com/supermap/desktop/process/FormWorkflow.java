package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormWorkflow;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmDialogFormSaveAs;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.*;
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
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/1/6.
 */
public class FormWorkflow extends FormBaseChild implements IFormWorkflow {
	private static final String PROCESS_TREE_CLASS_NAME = "com.supermap.desktop.process.core.ProcessManager";

	private Workflow workflow;
	private TasksManager tasksManager;
	private WorkflowCanvas canvas;
	private boolean isNeedSave = true;

	public FormWorkflow() {
		this(ControlsProperties.getString("String_WorkFlows"));
	}

	public FormWorkflow(String name) {
		this(new Workflow(name));
	}


	public FormWorkflow(IWorkflow workflow) {
		super(workflow.getName(), null, null);

		this.workflow = (Workflow) workflow;
		this.canvas = new WorkflowCanvas(this.workflow);
		this.tasksManager = new TasksManager(this.workflow);

		initializeComponents();
		setText(workflow.getName());
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

	public String serializeTo() {
		Document doc = XmlUtilities.getEmptyDocument();

		// 新建 WorkflowEntry
		Element workflowEntryNode = doc.createElement("WorkflowEntry");
		doc.appendChild(workflowEntryNode);

		// 处理 Workflow
		Element workflowNode = doc.createElement("Workflow");
		this.workflow.serializeTo(workflowNode);
		workflowEntryNode.appendChild(workflowNode);

		// 处理 location
		Element locationsNode = doc.createElement("Locations");
		this.canvas.serializeTo(locationsNode);
		workflowEntryNode.appendChild(locationsNode);

		return XmlUtilities.nodeToString(doc, "UTF-8");
	}

	public static void serializeFrom(String description) {
		Document doc = XmlUtilities.stringToDocument(description);
		Element workflowEntryNode = (Element) XmlUtilities.getChildElementNodeByName(doc, "WorkflowEntry");

		// 处理 Workflow
		Element workflowNode = (Element) XmlUtilities.getChildElementNodeByName(workflowEntryNode, "Workflow");
	}

	@Override
	public boolean save() {
		boolean result = false;
		int index = -1;
		ArrayList<IWorkflow> workflows = Application.getActiveApplication().getWorkflows();
		for (IWorkflow workFlow : workflows) {
			if (workFlow.getName().equals(this.getText())) {
				index = workflows.indexOf(workFlow);
				Application.getActiveApplication().removeWorkflow(workFlow);
				break;
			}
		}

		if (index == -1) {
			IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
			SmDialogFormSaveAs dialogSaveAs = new SmDialogFormSaveAs();
			dialogSaveAs.setDescription(ProcessProperties.getString("String_NewWorkFlowName"));
			dialogSaveAs.setCurrentFormName(getText());
			dialogSaveAs.setTitle(ProcessProperties.getString("String_SaveWorkflow"));

			for (IWorkflow workFlow : Application.getActiveApplication().getWorkflows()) {
				dialogSaveAs.addExistNames(workFlow.getName());
			}

			for (int i = 0; i < formManager.getCount(); i++) {
				if (formManager.get(i) instanceof FormWorkflow && formManager.get(i) != this) {
					dialogSaveAs.addExistNames(formManager.get(i).getText());
				}
			}
			if (dialogSaveAs.showDialog() == DialogResult.OK) {
				this.setText(dialogSaveAs.getCurrentFormName());
				Application.getActiveApplication().addWorkflow(getWorkflow());
				result = true;
			}
		} else {
			Application.getActiveApplication().addWorkflow(index, getWorkflow());
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
		for (IWorkflow workFlow : Application.getActiveApplication().getWorkflows()) {
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
			Application.getActiveApplication().addWorkflow(getWorkflow());
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
		try {
			Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(PROCESS_TREE_CLASS_NAME)).setVisible(true);
		} catch (ClassNotFoundException e1) {
			Application.getActiveApplication().getOutput().output(e1);
		}
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
