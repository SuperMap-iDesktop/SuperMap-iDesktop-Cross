package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.data.DatasetVector;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterOverlayAnalyst;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/2/14.
 * 叠加分析
 */
public class MetaProcessOverlayAnalyst extends MetaProcess {
	private IParameters parameters = new DefaultParameters();
	private OverlayAnalystType analystType;
	private ParameterOverlayAnalyst parameterOverlayAnalyst;

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
		parameterOverlayAnalyst = new ParameterOverlayAnalyst();
		parameterOverlayAnalyst.setOverlayAnalystType(analystType);
		parameters.setParameters(parameterOverlayAnalyst);
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
		ParameterOverlayAnalystInfo info = (ParameterOverlayAnalystInfo) parameterOverlayAnalyst.getSelectedItem();
		if (inputs.getData() != null && inputs.getData() instanceof DatasetVector) {
			info.sourceDataset = (DatasetVector) inputs.getData();
			info.sourceDatatsource = ((DatasetVector) inputs.getData()).getDatasource();
		}
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

		ProcessData processData = new ProcessData();
		processData.setData(info.sourceDataset);
		outPuts.set(0, processData);
	}

	@Override
	public String getKey() {
		return MetaKeys.OVERLAY_ANALYST;
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
