package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.implement.ParameterOverlayAnalyst;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/2/14.
 * 叠加分析
 */
public class MetaProcessOverlayAnalyst extends MetaProcess {
    private IParameters parameters;
    private OverlayAnalystType analystType;

    private SteppedListener steppedListener = new SteppedListener() {
        @Override
        public void stepped(SteppedEvent steppedEvent) {
            fireRunning(new RunningEvent(MetaProcessOverlayAnalyst.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
        }
    };

    public MetaProcessOverlayAnalyst(OverlayAnalystType analystType) {
        this.analystType = analystType;
        initMetaInfo();
    }

    private void initMetaInfo() {
        ParameterOverlayAnalyst parameterOverlayAnalyst = new ParameterOverlayAnalyst();
        parameterOverlayAnalyst.setOverlayAnalystType(analystType);
        parameters.setParameters(new IParameter[]{parameterOverlayAnalyst});
    }

    public OverlayAnalystType getAnalystType() {
        return analystType;
    }

    @Override
    public String getTitle() {
        return "叠加分析";
    }

    @Override
    public JComponent getComponent() {
        return parameters.getPanel();
    }

    @Override
    public void run() {
        ParameterOverlayAnalystInfo info = (ParameterOverlayAnalystInfo) parameters.getParameter(0).getSelectedItem();
        if (null == info.sourceDataset || null == info.overlayAnalystDataset
                || null == info.targetDataset || null == info.analystParameter) {
            return;
        }
        if (null != info.sourceDataset && null != info.overlayAnalystDataset && !isSameProjection(info.sourceDataset.getPrjCoordSys(), info.overlayAnalystDataset.getPrjCoordSys())) {
            Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_PrjCoordSys_Different") + "\n" + ControlsProperties.getString("String_Parameters"));
            Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_OverlayAnalyst_Failed"), info.sourceDataset.getName() + "@" + info.sourceDataset.getDatasource().getAlias()
                    , info.overlayAnalystDataset.getName() + "@" + info.overlayAnalystDataset.getDatasource().getAlias(), analystType.toString()));
            return;
        }
        OverlayAnalyst.addSteppedListener(this.steppedListener);

        switch (analystType) {
            case CLIP:
                OverlayAnalyst.clip(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            case ERASE:
                OverlayAnalyst.erase(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            case IDENTITY:
                OverlayAnalyst.identity(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            case INTERSECT:
                OverlayAnalyst.intersect(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            case UNION:
                OverlayAnalyst.union(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            case XOR:
                OverlayAnalyst.xOR(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            case UPDATE:
                OverlayAnalyst.update(info.sourceDataset, info.overlayAnalystDataset, info.targetDataset, info.analystParameter);
                break;
            default:
                break;
        }
        OverlayAnalyst.removeSteppedListener(this.steppedListener);
    }

    private boolean isSameProjection(PrjCoordSys prjCoordSys, PrjCoordSys prjCoordSys1) {
        if (prjCoordSys.getType() != prjCoordSys1.getType()) {
            return false;
        }
        if (prjCoordSys.getGeoCoordSys() == prjCoordSys1.getGeoCoordSys()) {
            return true;
        }
        if (prjCoordSys.getGeoCoordSys() == null || prjCoordSys1.getGeoCoordSys() == null) {
            return false;
        }
        if (prjCoordSys.getGeoCoordSys().getType() != prjCoordSys1.getGeoCoordSys().getType()) {
            return false;
        }
        return true;
    }

}
