package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.tasks.ITask;

/**
 * Created by xie on 2017/1/23.
 */
public class CallableFactory {
    public UpdateProgressCallable createCallable(ITask task) {
        UpdateProgressCallable callable = null;
        IProcess process = task.getProcess();
        if (process.getKey().equals(MetaKeys.IMPORT)) {
            callable = new ImportProgressCallable(process);
        } else if (process.getKey().equals(MetaKeys.PROJECTION)) {
            callable = new ProjectionProgressCallable(process);
        } else if (process.getKey().equals(MetaKeys.SPATIALINDEX)) {
            callable = new SpatialIndexProgressCallable(process);
        } else if (process.getKey().equals(MetaKeys.BUFFER)) {
            callable = new BufferProgressCallable(process);
        }
        return callable;
    }
}
