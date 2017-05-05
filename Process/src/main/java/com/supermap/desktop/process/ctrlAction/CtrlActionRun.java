package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.core.DirectConnect;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.MatrixExecutor;
import com.supermap.desktop.process.core.NodeMatrix;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.storage.IConnectionManager;
import com.supermap.desktop.process.graphics.storage.IGraphStorage;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.tasks.TasksManagerContainer;
import com.supermap.desktop.process.util.TaskUtil;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by highsad on 2017/2/28.
 */
public class CtrlActionRun extends CtrlAction {
    private final static String TASKS = "com.supermap.desktop.process.tasks.TasksManagerContainer";

    public CtrlActionRun(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
//        NodeMatrix matrix = new NodeMatrix();
//        MetaProcessImport metaProcessImport = new MetaProcessImport();
//        MetaProcessBuffer buffer = new MetaProcessBuffer();
//        MetaProcessProjection projection = new MetaProcessProjection();
//        MetaProcessSqlQuery sqlQuery = new MetaProcessSqlQuery();
//        matrix.addNode(metaProcessImport);
//        matrix.addNode(buffer);
//        matrix.addNode(projection);
//        matrix.addNode(sqlQuery);
//        matrix.addConstraint(metaProcessImport, buffer, new INodeConstraint() {});
//        matrix.addConstraint(buffer, projection, new INodeConstraint() {});
//        matrix.addConstraint(projection, sqlQuery, new INodeConstraint() {});
//
//        Workflow workflow = new Workflow(matrix);
//        workflow.parseToXmlFile("C://temp2.xml");
//        Workflow workflow1 = new Workflow(nodeMatrix);
//        workflow1.parseToXmlFile("C://temp1.xml");
//        try {
//            IForm form = Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
//            IDockbarManager manager = Application.getActiveApplication().getMainFrame().getDockbarManager();
//            IDockbar tasksDock = manager.get(Class.forName(TASKS));

//        WorkflowParser parser = new WorkflowParser();
//        NodeMatrix nodeMatrix = parser.parseXMLToMatrix("C://temp.xml");

        NodeMatrix nodeMatrix = new NodeMatrix();
        FormProcess formProcess = (FormProcess) Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
        GraphCanvas canvas = formProcess.getCanvas();
        IGraphStorage graphStorage = canvas.getGraphStorage();
        IConnectionManager graphConnection = canvas.getConnection();
        IGraph[] graphs = graphStorage.getGraphs();
        for (IGraph graph : graphs) {
            if (graph instanceof ProcessGraph) {
                nodeMatrix.addNode(((ProcessGraph) graph).getProcess());
            }
        }

        ConnectionLineGraph[] lines = graphConnection.getLines();
        for (ConnectionLineGraph line : lines) {
            if (line.getEndGraph() instanceof ProcessGraph) {
                IProcess end = ((ProcessGraph) line.getEndGraph()).getProcess();
                OutputGraph outputGraph = (OutputGraph) line.getStartGraph();
                IProcess start = outputGraph.getProcessGraph().getProcess();
                nodeMatrix.addConstraint(start, end, new DirectConnect());
            }
        }
        TasksManagerContainer container = TaskUtil.getManagerContainer(true);
//            tasksDock.setVisible(true);

        container.clear();
        CopyOnWriteArrayList list = nodeMatrix.getAllNodes();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof IProcess) {
                ProcessTask task = TaskUtil.getTask((IProcess) list.get(i));
                task.setCancel(false);
                container.addItem(task);
            }
        }
//
//            if (form instanceof FormProcess) {
//                GraphCanvas canvas = ((FormProcess) form).getCanvas();
//                UniversalMatrix matrix = canvas.getTasks();
//

//		TaskUtil.excuteTasks(nodeMatrix);
        MatrixExecutor executor = new MatrixExecutor(nodeMatrix);
        executor.run();

//            }
//        MetaProcessImport metaProcessImport = new MetaProcessImport();
//        ParameterManager manager = TaskUtil.getParameterManager(true);
//        manager.setProcess(metaProcessImport);

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean enable() {
        return Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm() instanceof FormProcess;
    }
}
