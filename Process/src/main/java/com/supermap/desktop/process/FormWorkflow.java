package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormProcess;
import com.supermap.desktop.Interface.IWorkFlow;
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
import com.supermap.desktop.process.core.DirectConnect;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.NodeMatrix;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.core.WorkflowParser;
import com.supermap.desktop.process.events.GraphSelectChangedListener;
import com.supermap.desktop.process.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.ScrollGraphCanvas;
import com.supermap.desktop.process.graphics.connection.IConnectable;
import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;
import com.supermap.desktop.process.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.process.graphics.events.GraphRemovingListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.interaction.canvas.CanvasActionProcessEvent;
import com.supermap.desktop.process.graphics.interaction.canvas.CanvasActionProcessListener;
import com.supermap.desktop.process.graphics.interaction.canvas.GraphDragAction;
import com.supermap.desktop.process.graphics.interaction.canvas.Selection;
import com.supermap.desktop.process.graphics.storage.IConnectionManager;
import com.supermap.desktop.process.graphics.storage.IGraphStorage;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessGroup;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.process.parameter.interfaces.datas.types.Type;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by highsad on 2017/1/6.
 */
public class FormWorkflow extends FormBaseChild implements IFormProcess {
	private ScrollGraphCanvas graphCanvas = new ScrollGraphCanvas();
	private boolean isNeedSave = true;
	private boolean isAutoAddOutPut = true;
	private transient DropTarget dropTargeted;


	public FormWorkflow() {
		this(ControlsProperties.getString("String_WorkFlows"));
	}


	public FormWorkflow(String name) {
		super(name, null, null);
		if (StringUtilities.isNullOrEmpty(name)) {
			name = ControlsProperties.getString("String_WorkFlows");
		}
		this.title = name;
		init();
	}

	public FormWorkflow(IWorkFlow workflow) {
		super(workflow.getName(), null, null);
		init();
		this.setText(workflow.getName());
		initFormWorkFlow(workflow);
		isNeedSave = false;
	}

	protected void initFormWorkFlow(IWorkFlow workflow) {
		if (workflow instanceof Workflow) {
			isAutoAddOutPut = false;
			try {
				NodeMatrix matrix = ((Workflow) workflow).getMatrix();
				CopyOnWriteArrayList allStartNodes = matrix.getAllNodes();
				for (Object node : allStartNodes) {
					IGraph graph = (IGraph) node;
					graphCanvas.getCanvas().addGraph(graph);
					graph.setCanvas(graphCanvas.getCanvas());
				}
				IConnectionManager connection = graphCanvas.getCanvas().getConnection();
				for (Object node : allStartNodes) {
					IGraph graph = (IGraph) node;
					CopyOnWriteArrayList nextNodes = matrix.getNextNodes(graph);
					if (nextNodes != null) {
						for (Object nextNode : nextNodes) {
							if (nextNode instanceof OutputGraph) {
								((OutputGraph) nextNode).setProcessGraph(((ProcessGraph) node));
							}
							String message = null;
							if (nextNode instanceof ProcessGraph) {
								ProcessGraph processGraph = (ProcessGraph) nextNode;
								message = processGraph.getProcess().getInputs().getBindedInput(((OutputGraph) graph).getProcessData());
							}

							connection.connect((IConnectable) graph, (IConnectable) nextNode, message);
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				isAutoAddOutPut = true;
			}
		}
	}

	private void init() {

		setLayout(new BorderLayout());
		add(graphCanvas, BorderLayout.CENTER);
		initListeners();
	}

	private void initListeners() {
		graphCanvas.getCanvas().getSelection().addGraphSelectChangedListener(new GraphSelectChangedListener() {

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

		graphCanvas.getCanvas().addGraphCreatedListener(new GraphCreatedListener() {
			@Override
			public void graphCreated(GraphCreatedEvent e) {
				isNeedSave = true;
				if (!isAutoAddOutPut) {
					return;
				}
				if (e.getGraph() instanceof ProcessGraph) {
					ProcessGraph processGraph = (ProcessGraph) e.getGraph();
					addOutPutGraph(processGraph);
				}
			}
		});
		graphCanvas.getCanvas().getGraphStorage().addGraphRemovingListener(new GraphRemovingListener() {
			@Override
			public void graphRemoving(GraphRemovingEvent e) {
				isNeedSave = true;
			}
		});
		graphCanvas.getCanvas().getActionsManager().addCanvasActionProcessListener(new CanvasActionProcessListener() {
			@Override
			public void canvasActionProcess(CanvasActionProcessEvent e) {
				if (e.getAction() instanceof GraphDragAction && e.getStatus() == CanvasActionProcessEvent.START) {
					isNeedSave = true;
				}
			}
		});
		addDrag();
	}


	private void addOutPutGraph(ProcessGraph processGraph) {
		IProcess process = processGraph.getProcess();

		int gap = 20;
		OutputData[] outputs = process.getOutputs().getDatas();
		int length = outputs.length;
		OutputGraph[] dataGraphs = new OutputGraph[length];
		int totalHeight = gap * (length - 1);

		for (int i = 0; i < length; i++) {
			dataGraphs[i] = new OutputGraph(graphCanvas.getCanvas(), processGraph, outputs[i]);
			graphCanvas.getCanvas().addGraph(dataGraphs[i]);
			graphCanvas.getCanvas().getConnection().connect(processGraph, dataGraphs[i]);
			totalHeight += dataGraphs[i].getHeight();
		}

		int locationX = processGraph.getLocation().x + processGraph.getWidth() * 3 / 2;
		int locationY = processGraph.getLocation().y + (processGraph.getHeight() - totalHeight) / 2;
		for (int i = 0; i < length; i++) {
			dataGraphs[i].setLocation(new Point(locationX, locationY));
			//						System.out.println(dataGraphs[i].getHeight());
			locationY += dataGraphs[i].getHeight() + gap;
		}
		graphCanvas.getCanvas().repaint();
	}

	private void addDrag() {
		if (dropTargeted == null) {
			dropTargeted = new DropTarget(this, new FormProcessDropTargetAdapter());
		}
	}

	/**
	 * 获取可连接的输入节点
	 *
	 * @return
	 */
	public ArrayList<IGraph> getAllDataNode() {
		ArrayList<IGraph> dataNodes = new ArrayList<>();
		IGraph[] graphs = getCanvas().getGraphStorage().getGraphs();
		for (IGraph graph : graphs) {
			if (graph instanceof OutputGraph) {
				dataNodes.add(graph);
			}
		}
		return dataNodes;
	}

	public ArrayList<IGraph> getAllDataNode(Type type) {
		ArrayList<IGraph> iGraphs = new ArrayList<>();
		return iGraphs;
	}


	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(1000, 650);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new FormWorkflow(), BorderLayout.CENTER);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
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
		ArrayList<IWorkFlow> workFlows = Application.getActiveApplication().getWorkFlows();
		for (IWorkFlow workFlow : workFlows) {
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
			for (IWorkFlow workFlow : Application.getActiveApplication().getWorkFlows()) {
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
				Application.getActiveApplication().addWorkFlow(getWorkFlow());
				result = true;
			}
		} else {
			Application.getActiveApplication().addWorkFlow(index, getWorkFlow());
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
		for (IWorkFlow workFlow : Application.getActiveApplication().getWorkFlows()) {
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
			Application.getActiveApplication().addWorkFlow(getWorkFlow());
			result = true;
		}
		isNeedSave = !result;
		return result;
	}

	@Override
	public IWorkFlow getWorkFlow() {
		NodeMatrix nodeMatrix = new NodeMatrix();
		IConnectionManager connectionManager = this.graphCanvas.getCanvas().getConnection();
		IGraphStorage graphStorage = this.graphCanvas.getCanvas().getGraphStorage();
		IGraph[] graphs = graphStorage.getGraphs();
		for (IGraph graph : graphs) {
			if (graph instanceof LineGraph) {
				continue;
			}

			nodeMatrix.addNode(graph);
		}
		for (IGraph graph : graphs) {
			IGraph[] nextGraphs = connectionManager.getNextGraphs(graph);
			if (nextGraphs.length > 0) {
				for (IGraph nextGraph : nextGraphs) {
					nodeMatrix.addConstraint(graph, nextGraph, new DirectConnect());
				}
			}
		}
		Workflow workflow = new Workflow(getText());
		workflow.setMatrix(nodeMatrix);
		return workflow;
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

	public GraphCanvas getCanvas() {
		return this.graphCanvas.getCanvas();
	}

	public void addProcess(IProcess process) {
		if (process instanceof MetaProcessGroup) {
//			graph = new ProcessGroupGraph();
			ArrayList<MetaProcess> addedMetaProcesses = new ArrayList<>();
			MetaProcessGroup processGroup = (MetaProcessGroup) process;
			int processCount = processGroup.getProcessCount();
			if (processCount > 0) {
				MetaProcess metaProcess = processGroup.getMetaProcess(0);
				addSubProcess(processGroup, metaProcess, addedMetaProcesses, 0, 0);

				int i = 0;
				while (addedMetaProcesses.size() != processCount) {
					for (; i < processCount; i++) {
						if (!addedMetaProcesses.contains(processGroup.getMetaProcess(i))) {
							addSubProcess(processGroup, processGroup.getMetaProcess(i), addedMetaProcesses, 0, 0);
							break;
						}
					}
				}
			}
		} else {
			IGraph graph = new ProcessGraph(graphCanvas.getCanvas(), process);
			graphCanvas.getCanvas().create(graph);
		}
	}

	private IGraph addSubProcess(MetaProcessGroup processGroup, MetaProcess currentMetaProcess, ArrayList<MetaProcess> addedMetaProcesses, int level, int YLevel) {
		IGraph graph = null;
		if (!addedMetaProcesses.contains(currentMetaProcess)) {
			graph = new ProcessGraph(graphCanvas.getCanvas(), currentMetaProcess);
			graph.setLocation(new Point(getLocation().x + level * 100, getLocation().y + YLevel));
			graphCanvas.getCanvas().create(graph);
			addedMetaProcesses.add(currentMetaProcess);
		} else {
			IGraph[] graphs = getCanvas().getGraphStorage().getGraphs();
			for (IGraph iGraph : graphs) {
				if (iGraph instanceof ProcessGraph && ((ProcessGraph) iGraph).getProcess() == currentMetaProcess) {
					graph = iGraph;
					break;
				}
			}
		}
		ArrayList<MetaProcess> subMetaProcesses = processGroup.getSubMetaProcess(currentMetaProcess);
		level++;
		for (int i = 0; i < subMetaProcesses.size(); i++) {
			MetaProcess subMetaProcess = subMetaProcesses.get(i);
			IGraph subProcess = addSubProcess(processGroup, subMetaProcess, addedMetaProcesses, level, 200 * (i - subMetaProcesses.size() / 2));
			getCanvas().getGraphStorage().getConnectionManager().connect(((ProcessGraph) graph), ((ProcessGraph) subProcess));
		}
		return graph;
	}

	private class FormProcessDropTargetAdapter extends DropTargetAdapter {
		@Override
		public void drop(DropTargetDropEvent dtde) {
			FormWorkflow.this.grabFocus();
			Transferable transferable = dtde.getTransferable();
			DataFlavor[] currentDataFlavors = dtde.getCurrentDataFlavors();
			for (DataFlavor currentDataFlavor : currentDataFlavors) {
				if (currentDataFlavor != null) {
					try {
						Object transferData = transferable.getTransferData(currentDataFlavor);
						if (transferData instanceof String) {
							MetaProcess metaProcess = WorkflowParser.getMetaProcess((String) transferData);
							if (metaProcess == null) {
								continue;
							}
							ProcessGraph graph = new ProcessGraph(getCanvas(), metaProcess);
							Point location = dtde.getLocation();
							location = new Point(location.x - graph.getWidth() / 2, location.y - graph.getHeight() / 2);
							Point inverse = getCanvas().getCoordinateTransform().inverse(location);
							graph.setLocation(inverse);
							getCanvas().getGraphStorage().add(graph);
							getCanvas().repaint();
						}
					} catch (Exception e) {
						// ignore 当然是原谅ta啦
						Application.getActiveApplication().getOutput().output(e);
					}
				}
			}
		}
	}
	//endregion
}
