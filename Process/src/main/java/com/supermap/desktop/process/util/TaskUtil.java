package com.supermap.desktop.process.util;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.NodeException;
import com.supermap.desktop.process.core.NodeMatrix;
import com.supermap.desktop.process.tasks.TasksManagerContainer;

import java.util.concurrent.CopyOnWriteArrayList;
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

    public static TasksManagerContainer getManagerContainer(boolean isActive) {
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
     * Use ExecutorService to manage all task thread,
     * If task's prev tasks has finished,excute task;
     *
     * @param nodeMatrix
     * @return
     */
    public static TasksManagerContainer excuteTasks(final NodeMatrix nodeMatrix) {
        TasksManagerContainer tasksManagerContainer = getManagerContainer(true);
        final CopyOnWriteArrayList<Object> processes = nodeMatrix.listAllNodes();
        ExecutorService eService = Executors.newCachedThreadPool();
        final Lock lock = new ReentrantLock();
        int size = processes.size();
        for (int i = 0; i < size; i++) {
            if (processes.get(i) instanceof IProcess) {
                final IProcess nowProcess = ((IProcess) processes.get(i));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        lock.lock();
                        CopyOnWriteArrayList<Object> preNodes = null;
                        try {
                            preNodes = nodeMatrix.getPreNodes(nowProcess);
                        } catch (NodeException e) {
                            Application.getActiveApplication().getOutput().output(e);
                        }
                        boolean allPreTasksFinished = true;
                        int preNodesSize = preNodes.size();
                        if (preNodesSize > 0) {
                            for (int j = 0; j < preNodesSize; j++) {
                                if (!((IProcess) preNodes.get(j)).getProcessTask().isFinished()) {
                                    allPreTasksFinished = false;
                                    break;
                                }
                            }
                        }
                        if (allPreTasksFinished || preNodes.size() == 0) {
                            nowProcess.getProcessTask().doWork();
                        }
                        lock.unlock();
                    }
                };
                eService.execute(thread);
            }
        }
        return tasksManagerContainer;
    }

}
