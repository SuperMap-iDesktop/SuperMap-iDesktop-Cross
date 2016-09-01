package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

/**
 * Created by Administrator on 2016/9/1.
 */
public class OverlayAnalystCallable extends UpdateProgressCallable implements IOverlayAnalyst {
    private OverlayAnalystType type;
    private OverlayAnalystParameter parameter;
    private DatasetVector sourceDataset;
    private DatasetVector overlayAnalystDataset;
    private DatasetVector targetDataset;

    public OverlayAnalystCallable() {
        super();
    }

    @Override
    public Boolean call() throws Exception {
        boolean result = true;

//        try {
//           boolean overlayAnalystResult = false;
//            overlayAnalystResult = OverlayAnalyst.clip(sourceDataset,overlayAnalystResult,targetDataset,parameter);
//            if (overlayAnalystResult) {
//                topoInfo = MessageFormat.format(DataTopologyProperties.getString("String_TopoLineSucceed"), datasetName, time);
//            } else {
//                topoInfo = MessageFormat.format(DataTopologyProperties.getString("String_TopoLineFailed"), datasetName);
//            }
//            Application.getActiveApplication().getOutput().output(topoInfo);
//        } catch (Exception e) {
//            result = false;
//            Application.getActiveApplication().getOutput().output(e);
//        } finally {
//            TopologyProcessing.removeSteppedListener(percentListener);
//        }
        return result;
    }

    @Override
    public void setType(OverlayAnalystType type) {
        this.type = type;
    }

    @Override
    public void setOverlayAnalystParameter(OverlayAnalystParameter parameter) {

    }

    @Override
    public void setSourceDataset(DatasetVector sourceDataset) {

    }

    @Override
    public void setOverlayAnalystDataset(DatasetVector overlayAnalystDataset) {

    }

    @Override
    public void setTargetDataset(DatasetVector targetDataset) {

    }
}
