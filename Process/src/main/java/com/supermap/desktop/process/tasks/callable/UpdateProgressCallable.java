package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.tasks.ITask;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2016/11/28.
 */
public abstract class UpdateProgressCallable implements Callable<Boolean> {

    protected ITask update;

    public ITask getUpdate() {
        return update;
    }

    public IProcess getProcess() {
        return update.getProcess();
    }

    public abstract boolean isFinished();

    public void setUpdate(ITask update) {
        this.update = update;
    }

    /**
     * 单进度
     *
     * @param percent
     * @param remainTime
     * @param message
     * @throws CancellationException
     */
    public void updateProgress(int percent, String remainTime, String message) throws CancellationException {
        this.update.updateProgress(percent, remainTime, message);
    }

}
