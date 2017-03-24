package com.supermap.desktop.process.constraint.implement;

import com.supermap.data.DatasourceClosedEvent;
import com.supermap.data.DatasourceClosedListener;
import com.supermap.data.DatasourceCreatedEvent;
import com.supermap.data.DatasourceCreatedListener;
import com.supermap.data.DatasourceOpenedEvent;
import com.supermap.data.DatasourceOpenedListener;
import com.supermap.data.WorkspaceClosedEvent;
import com.supermap.data.WorkspaceClosedListener;
import com.supermap.data.WorkspaceCreatedEvent;
import com.supermap.data.WorkspaceCreatedListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.constraint.interfaces.IConstraint;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;


/**
 * 数据源持续更新
 *
 * @author XiaJT
 */
public class DatasourceConstraint implements IConstraint, ParameterValueLegalListener {
	private String name;
	private IParameter parameter;

	public DatasourceConstraint() {
		Application.getActiveApplication().getWorkspace().addClosedListener(new WorkspaceClosedListener() {
			@Override
			public void workspaceClosed(WorkspaceClosedEvent workspaceClosedEvent) {
				datasourceChanged();
			}
		});
		Application.getActiveApplication().getWorkspace().addOpenedListener(new WorkspaceOpenedListener() {
			@Override
			public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
				datasourceChanged();
			}
		});
		Application.getActiveApplication().getWorkspace().addCreatedListener(new WorkspaceCreatedListener() {
			@Override
			public void workspaceCreated(WorkspaceCreatedEvent workspaceCreatedEvent) {
				datasourceChanged();
			}
		});
		Application.getActiveApplication().getWorkspace().getDatasources().addClosedListener(new DatasourceClosedListener() {
			@Override
			public void datasourceClosed(DatasourceClosedEvent datasourceClosedEvent) {
				datasourceChanged();
			}
		});
		Application.getActiveApplication().getWorkspace().getDatasources().addCreatedListener(new DatasourceCreatedListener() {
			@Override
			public void datasourceCreated(DatasourceCreatedEvent datasourceCreatedEvent) {
				datasourceChanged();
			}
		});
		Application.getActiveApplication().getWorkspace().getDatasources().addOpenedListener(new DatasourceOpenedListener() {
			@Override
			public void datasourceOpened(DatasourceOpenedEvent datasourceOpenedEvent) {
				datasourceChanged();
			}
		});
	}

	private void datasourceChanged() {
		parameter.fireFieldConstraintChanged(name);
	}

	@Override
	public void constrained(IParameter parameter, String name) {
		this.parameter = parameter;
		this.name = name;
		parameter.addValueLegalListener(this);
	}


	@Override
	public boolean isValueLegal(ParameterValueLegalEvent event) {
		if (event.getFieldName().equals(name)) {
			return true;
		}
		return true;
	}
}
