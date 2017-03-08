package com.supermap.desktop.process.tasks;


import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2016/11/28.
 */
public interface IProcessTask extends IUpdateProgress {

    /**
     * get process
     *
     * @return
     */
    IProcess getProcess();

    void doWork();

}
