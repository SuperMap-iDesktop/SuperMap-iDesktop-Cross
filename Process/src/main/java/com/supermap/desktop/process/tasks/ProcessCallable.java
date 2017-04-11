package com.supermap.desktop.process.tasks;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

/**
 * Created by xie on 2017/4/11.
 * Refresh gui callable
 */
public class ProcessCallable extends UpdateProgressCallable {

    private IProcess process;
    private RunningListener runningListener = new RunningListener() {
        @Override
        public void running(RunningEvent e) {
            updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
        }
    };

    public ProcessCallable(IProcess process) {
        this.process = process;
    }

    @Override
    public Boolean call() throws Exception {
        process.addRunningListener(this.runningListener);
        process.run();
        return false;
    }
}
