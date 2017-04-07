package com.supermap.desktop.process.tasks;

import com.supermap.desktop.process.core.IProcess;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xie on 2017/4/7.
 */
public class TaskStore {
    private static CopyOnWriteArrayList<IProcessTask> store;

    public static void addTask(IProcessTask task) {
        if (null == store) {
            store = new CopyOnWriteArrayList();
        }
        if (!store.contains(task))
            store.add(task);
    }

    public static IProcessTask getTask(IProcess process) {
        IProcessTask result = null;
        if (null != store) {
            for (int i = 0; i < store.size(); i++) {
                if (store.get(i).getProcess().equals(process)) {
                    result = store.get(i);
                }
            }
        }
        return result;
    }
}
