package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.core.*;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessBuffer;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessImport;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessProjection;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessSqlQuery;
import com.supermap.desktop.process.tasks.TasksManagerContainer;
import com.supermap.desktop.process.util.TaskUtil;
import org.omg.CORBA.MARSHAL;

import javax.xml.soap.Node;

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
        NodeMatrix matrix = new NodeMatrix();
        MetaProcessImport metaProcessImport = new MetaProcessImport();
        MetaProcessBuffer buffer = new MetaProcessBuffer();
        MetaProcessProjection projection = new MetaProcessProjection();
        MetaProcessSqlQuery sqlQuery = new MetaProcessSqlQuery();
        matrix.addNode(metaProcessImport);
        matrix.addNode(buffer);
        matrix.addNode(projection);
        matrix.addNode(sqlQuery);
        matrix.addConstraint(metaProcessImport, buffer, new INodeConstraint() {});
        matrix.addConstraint(buffer, projection, new INodeConstraint() {});
        matrix.addConstraint(projection, sqlQuery, new INodeConstraint() {});

        Workflow workflow = new Workflow(matrix);
        workflow.parseToXmlFile("C://temp.xml");
        WorkflowParser parser = new WorkflowParser();
        NodeMatrix nodeMatrix = parser.parseXMLToMatrix("C://temp.xml");
        Workflow workflow1 = new Workflow(nodeMatrix);
        workflow1.parseToXmlFile("C://temp1.xml");
//        try {
//            IForm form = Application.getActiveApplication().getMainFrame().getFormManager().getActiveForm();
//            IDockbarManager manager = Application.getActiveApplication().getMainFrame().getDockbarManager();
//            IDockbar tasksDock = manager.get(Class.forName(TASKS));
//            TasksManagerContainer container = (TasksManagerContainer) tasksDock.getInnerComponent();
//            tasksDock.setVisible(true);
//            container.clear();
//
//            if (form instanceof FormProcess) {
//                GraphCanvas canvas = ((FormProcess) form).getCanvas();
//                UniversalMatrix matrix = canvas.getTasks();
//
//                TaskUtil.excuteTasks(matrix);
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
