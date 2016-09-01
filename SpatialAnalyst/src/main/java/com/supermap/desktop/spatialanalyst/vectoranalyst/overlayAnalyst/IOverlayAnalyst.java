package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.DatasetVector;

/**
 * Created by xie on 2016/8/30.
 */
public interface IOverlayAnalyst {
    void setType(OverlayAnalystType type);

    void setOverlayAnalystParameter(OverlayAnalystParameter parameter);

    void setSourceDataset(DatasetVector sourceDataset);

    void setOverlayAnalystDataset(DatasetVector overlayAnalystDataset);

    void setTargetDataset(DatasetVector targetDataset);
}
