package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormWorkflow;

/**
 * Created by highsad on 2017/2/28.
 */
public class CtrlActionRun extends CtrlAction {
	private final static String TASKS_CONTAINER = "com.supermap.desktop.process.tasks.TasksManagerContainer";

	public CtrlActionRun(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (Application.getActiveApplication().getActiveForm() instanceof FormWorkflow) {
				IDockbar tasksContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TASKS_CONTAINER));
				tasksContainer.setVisible(true);

				FormWorkflow formWorkflow = (FormWorkflow) Application.getActiveApplication().getActiveForm();
				formWorkflow.getTasksManager().run();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
//		NodeMatrix nodeMatrix = new NodeMatrix();
//		FormWorkflow formWorkflow = (FormWorkflow) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
//		GraphCanvas canvas = formWorkflow.getCanvas();
//		IGraphStorage graphStorage = canvas.getGraphStorage();
//		IConnectionManager graphConnection = canvas.getConnection();
//		IGraph[] graphs = graphStorage.getGraphs();
//		for (IGraph graph : graphs) {
//			if (graph instanceof ProcessGraph) {
//				nodeMatrix.addNode(((ProcessGraph) graph).getProcess());
//			}
//		}
//
//		IGraphConnection[] connections = graphConnection.getConnections();
//		for (IGraphConnection connection : connections) {
//			IGraph startGraph = connection.getStart().getConnector();
//			IGraph endGraph = connection.getEnd().getConnector();
//
//			if (endGraph instanceof ProcessGraph && startGraph instanceof OutputGraph) {
//				IProcess end = ((ProcessGraph) connection.getEnd().getConnector()).getProcess();
//				IProcess start = ((OutputGraph) startGraph).getProcessGraph().getProcess();
//				nodeMatrix.addRelation(start, end, DirectConnect.class);
//			}
//		}
//
//		TasksManagerContainer container = TaskUtil.getManagerContainer(true);
////		container.clear();
//		Vector list = nodeMatrix.getNodes();
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i) instanceof IProcess) {
//				ProcessTask task = TaskUtil.getTask((IProcess) list.get(i));
//				task.setCancel(false);
////				container.addItem(task);
//			}
//		}
//
//		MatrixExecutor executor = new MatrixExecutor(nodeMatrix);
//		executor.run();
	}

	@Override
	public boolean enable() {
		return Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm() instanceof FormWorkflow;
	}
}
