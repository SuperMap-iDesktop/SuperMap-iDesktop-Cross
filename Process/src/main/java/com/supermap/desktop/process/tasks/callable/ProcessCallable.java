package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

/**
 * Created by xie on 2017/2/13.
 */
public class ProcessCallable extends UpdateProgressCallable{
    private IProcess process;
    public ProcessCallable(IProcess process){
        this.process = process;
    }

    private RunningListener runningListener = new RunningListener() {
        @Override
        public void running(RunningEvent e) {
            if (e.getProgress() >= 100) {
                if (process.getKey().equals(MetaKeys.IMPORT)) {
                    updateProgress(100, String.valueOf(e.getRemainTime()), ControlsProperties.getString("String_ImportProgressFinished"));
                } else if (process.getKey().equals(MetaKeys.PROJECTION)) {
                    updateProgress(100, String.valueOf(e.getRemainTime()),  ControlsProperties.getString("String_ProjectionProgressFinished"));
                } else if (process.getKey().equals(MetaKeys.SPATIALINDEX)) {
                    updateProgress(100, String.valueOf(e.getRemainTime()), ControlsProperties.getString("String_SpatialIndexProgressFinished"));
                } else if (process.getKey().equals(MetaKeys.BUFFER)) {
                    updateProgress(100, String.valueOf(e.getRemainTime()), ControlsProperties.getString("String_BufferProgressFinished"));
                }
            } else {
                updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
            }
        }
    };
    @Override
    public Boolean call() throws Exception {
        process.addRunningListener(this.runningListener);
        process.run();
        return null;
    }
}
