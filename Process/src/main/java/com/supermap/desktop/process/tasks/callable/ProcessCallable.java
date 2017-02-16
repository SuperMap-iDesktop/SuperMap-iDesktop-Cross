package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

/**
 * Created by xie on 2017/2/13.
 */
public class ProcessCallable extends UpdateProgressCallable {
    private IProcess process;

    public ProcessCallable(IProcess process) {
        this.process = process;
    }

    private RunningListener runningListener = new RunningListener() {
        @Override
        public void running(RunningEvent e) {
            if (e.getProgress() >= 100) {
                updateProgress(100, String.valueOf(e.getRemainTime()), getFinishMessage());

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

    public String getFinishMessage() {
        String result = "";
        if (process.getKey().equals(MetaKeys.IMPORT)) {
            result = ControlsProperties.getString("String_ImportProgressFinished");
        } else if (process.getKey().equals(MetaKeys.PROJECTION)) {
            result = ControlsProperties.getString("String_ProjectionProgressFinished");
        } else if (process.getKey().equals(MetaKeys.SPATIALINDEX)) {
            result = ControlsProperties.getString("String_SpatialIndexProgressFinished");
        } else if (process.getKey().equals(MetaKeys.BUFFER)) {
            result = ControlsProperties.getString("String_BufferProgressFinished");
        } else if (process.getKey().equals(MetaKeys.HEATMAP)) {
            result = ControlsProperties.getString("String_HeatMapFinished");
        } else if (process.getKey().equals(MetaKeys.KERNELDENSITY)) {
            result = ControlsProperties.getString("String_KernelDensityFinished");
        } else if (process.getKey().equals(MetaKeys.OVERLAYANALYST)) {
            OverlayAnalystType analystType = ((MetaProcessOverlayAnalyst)process).getAnalystType();
            switch (analystType) {
                case CLIP:
                    result = ControlsProperties.getString("String_OverlayAnalyst_CLIPFinished");
                    break;
                case ERASE:
                    result = ControlsProperties.getString("String_OverlayAnalyst_ERASEFinished");
                    break;
                case IDENTITY:
                    result = ControlsProperties.getString("String_OverlayAnalyst_IDENTITYFinished");
                    break;
                case INTERSECT:
                    result = ControlsProperties.getString("String_OverlayAnalyst_INTERSECTFinished");
                    break;
                case UNION:
                    result = ControlsProperties.getString("String_OverlayAnalyst_UNIONFinished");
                    break;
                case XOR:
                    result = ControlsProperties.getString("String_OverlayAnalyst_XORFinished");
                    break;
                case UPDATE:
                    result = ControlsProperties.getString("String_OverlayAnalyst_UPDATEFinished");
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}
