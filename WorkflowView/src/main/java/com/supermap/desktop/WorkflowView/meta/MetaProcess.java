package com.supermap.desktop.WorkflowView.meta;

import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.ipls.DefaultParameters;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class MetaProcess extends AbstractProcess {

	protected static final String SOURCE_PANEL_DESCRIPTION = CommonProperties.getString("String_GroupBox_SourceData");
	protected static final String SETTING_PANEL_DESCRIPTION = CommonProperties.getString("String_FormEdgeCount_Text");
	protected static final String RESULT_PANEL_DESCRIPTION = CommonProperties.getString("String_GroupBox_ResultData");

	protected IParameters parameters = new DefaultParameters(this);
	protected boolean finished = false;

	protected SteppedListener steppedListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			RunningEvent event = new RunningEvent(MetaProcess.this, steppedEvent.getPercent(), AbstractParameter.PROPERTY_VALE);
			fireRunning(event);

			if (event.isCancel()) {
				steppedEvent.setCancel(true);
				cancel();
			}
		}
	};

	public MetaProcess() {
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	protected Icon getIconByPath(String path) {
		return new ImageIcon(ProcessResources.getResourceURL(path));
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	public boolean isFinished() {
		return this.getStatus() == RunningStatus.COMPLETED;
	}
}
