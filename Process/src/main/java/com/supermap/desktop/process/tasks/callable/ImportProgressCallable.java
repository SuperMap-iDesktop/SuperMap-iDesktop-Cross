package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;

/**
 * Created by xie on 2016/11/28.
 */
public class ImportProgressCallable extends UpdateProgressCallable {
    private IProcess importProcess;
    private  boolean isFinished = false;
    private RunningListener runningListener = new RunningListener() {
        @Override
        public void running(RunningEvent e) {
            if (e.getProgress() >= 100) {
                updateProgress(100, String.valueOf(e.getRemainTime()), ControlsProperties.getString("String_ImportProgressFinished"));
            } else {
                updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
            }
        }
    };

    public ImportProgressCallable(IProcess process) {
        this.importProcess = process;
    }

    @Override
    public Boolean call() throws Exception {
        importProcess.addRunningListener(this.runningListener);
        importProcess.run();
        return null;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
