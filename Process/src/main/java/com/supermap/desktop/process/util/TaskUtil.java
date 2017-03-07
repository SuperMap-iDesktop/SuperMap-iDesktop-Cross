package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.UniversalMatrix;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.tasks.TasksManagerContainer;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xie on 2017/2/28.
 */
public class TaskUtil {
    private static final String TASKMANAGER = "com.supermap.desktop.process.tasks.TasksManagerContainer";

    private static TasksManagerContainer getManagerContainer(boolean isActive) {
        TasksManagerContainer fileManagerContainer = null;
        IDockbar dockbarPropertyContainer = null;
        try {
            dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager().get(Class.forName(TASKMANAGER));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (dockbarPropertyContainer != null) {
            fileManagerContainer = (TasksManagerContainer) dockbarPropertyContainer.getInnerComponent();
        }
        if (isActive && dockbarPropertyContainer != null) {
            dockbarPropertyContainer.setVisible(true);
            dockbarPropertyContainer.active();
        }
        return fileManagerContainer;
    }

//    /**
//     *
//     *
//     * @param universalMatrix
//     */
//    public static TasksManagerContainer addTasks(UniversalMatrix universalMatrix) {
//        TasksManagerContainer tasksManagerContainer = getManagerContainer(true);
//        ArrayList<String> startNodes = universalMatrix.getAllStartNode();
//        int startNodeSize = startNodes.size();
//        ExecutorService eService = Executors.newCachedThreadPool();
//        for (int i = 0; i < startNodeSize; i++) {
//            ProcessTask task = new ProcessTask((IProcess) universalMatrix.getNode(startNodes.get(i)));
//            eService.submit(task);
//        }
//        return tasksManagerContainer;
//    }

    /**
     * 现在只考虑串行结构，且只有一个串时的情境
     *
     * @param universalMatrix
     */
    public static TasksManagerContainer addTasks(UniversalMatrix universalMatrix) {
        TasksManagerContainer tasksManagerContainer = getManagerContainer(true);
        String processKey = (String) universalMatrix.getAllStartNode().get(0);
        ArrayList<String> process = new ArrayList<>();
        process.add(processKey);
        ArrayList<String> processArray = universalMatrix.getAllNextNode(processKey);
        process.addAll(processArray);
        int size = process.size();
        for (int i = 0; i < size; i++) {
            tasksManagerContainer.addItem(new ProcessTask((IProcess) universalMatrix.getNode(process.get(i))));
        }
        return tasksManagerContainer;
    }


//    public void doWorks(ExecutorService executorService, UniversalMatrix universalMatrix, String nodeName, ProcessTask preTask) {
//        if (universalMatrix.getPreNode(nodeName).size() > 0 && proNodeTasksFinished()) {
//            ProcessTask task = new ProcessTask((IProcess) universalMatrix.getNode(nodeName));
//
//        }
////        executorService.submit(processTask);
//    }
//
//    private boolean proNodeTasksFinished(UniversalMatrix universalMatrix, String nodeName) {
//        boolean allTaskFinished = false;
//        ArrayList<String> preNodes = universalMatrix.getPreNode(nodeName);
//        int length = preNodes.size();
//        for (int i = 0; i < length; i++) {
//
//        }
//        return allTaskFinished;
//    }

}
