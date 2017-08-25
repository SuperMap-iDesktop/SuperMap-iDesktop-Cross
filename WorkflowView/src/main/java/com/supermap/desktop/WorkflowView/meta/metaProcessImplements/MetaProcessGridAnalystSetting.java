package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.desktop.GridAnalystSettingInstance;
import com.supermap.desktop.WorkflowView.WorkflowViewProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.events.RunningEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class MetaProcessGridAnalystSetting extends MetaProcess {

	private boolean isSelecting = false;

	public MetaProcessGridAnalystSetting() {


		GridAnalystSettingInstance instance = GridAnalystSettingInstance.getInstance();
		instance.addPropertyChangedListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {

			}
		});
	}

	@Override
	public String getTitle() {
		return WorkflowViewProperties.getString("String_GridAnalystSetting");
	}

	@Override
	public boolean execute() {
		fireRunning(new RunningEvent(this, 0, WorkflowViewProperties.getString("String_GridAnalystSetStart")));
		GridAnalystSettingInstance.getInstance().run();
		fireRunning(new RunningEvent(this, 100, WorkflowViewProperties.getString("String_SetGridAnalystSetSuccess")));
		return true;
	}

	@Override
	protected boolean isReadyHook() {
		GridAnalystSettingInstance.getInstance().run();
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.GRID_ANALYST_SETTING;
	}
}
