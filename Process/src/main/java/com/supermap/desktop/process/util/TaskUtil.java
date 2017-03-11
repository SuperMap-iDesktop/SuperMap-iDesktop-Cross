package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.UniversalMatrix;
import com.supermap.desktop.process.tasks.TasksManagerContainer;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xie on 2017/2/28.
 */
public class TaskUtil {
    /**
     * Utilities class,no public construction method
     */
    private TaskUtil() {

    }

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

    /**
     * Use ExecutorService to manager all task thread,
     * If task's prev tasks has finished,excute task;
     *
     * @param universalMatrix
     * @return
     */
    public static TasksManagerContainer addTasks(final UniversalMatrix universalMatrix) {
        TasksManagerContainer tasksManagerContainer = getManagerContainer(true);
        final ArrayList<Object> processes = universalMatrix.listAllNode();
        ExecutorService eService = Executors.newCachedThreadPool();
        final Lock lock = new ReentrantLock();
        int size = processes.size();
        for (int i = 0; i < size; i++) {
            if (processes.get(i) instanceof IProcess) {
                final IProcess nowProcess = ((IProcess) processes.get(i));
                final String nowProcessTitle = nowProcess.getTitle();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        lock.lock();
                        ArrayList<Object> preNodes = universalMatrix.getPreNode(nowProcessTitle);
                        boolean allPreTasksFinished = true;
                        int preNodesSize = preNodes.size();
                        for (int j = 0; j < preNodesSize; j++) {
                            if (!((IProcess) preNodes).getProcessTask().isFinished()) {
                                allPreTasksFinished = false;
                                break;
                            }
                        }
                        if (allPreTasksFinished) {
                            nowProcess.getProcessTask().doWork();
                        }
                        lock.unlock();
                    }
                };
                eService.submit(thread);
            }
        }
        return tasksManagerContainer;
    }


//    /**
//     * 现在只考虑串行结构，且只有一个串时的情境
//     *
//     * @param universalMatrix
//     */
//    public static TasksManagerContainer addTasks(UniversalMatrix universalMatrix) {
//        TasksManagerContainer tasksManagerContainer = getManagerContainer(true);
//        String processKey = (String) universalMatrix.getAllStartNode().get(0);
//        ArrayList<String> process = new ArrayList<>();
//        process.add(processKey);
//        ArrayList<String> processArray = universalMatrix.getAllNextNode(processKey);
//        process.addAll(processArray);
//        int size = process.size();
//        for (int i = 0; i < size; i++) {
//            tasksManagerContainer.addItem(new ProcessTask((IProcess) universalMatrix.getNode(process.get(i))));
//        }
//        return tasksManagerContainer;
//    }

}
