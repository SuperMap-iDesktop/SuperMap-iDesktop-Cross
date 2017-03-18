package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.core.UniversalMatrix;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.tasks.TasksManagerContainer;
import com.supermap.desktop.process.util.TaskUtil;

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
