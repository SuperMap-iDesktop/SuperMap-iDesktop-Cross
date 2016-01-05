package com.supermap.desktop.event;

import java.util.EventObject;

import com.supermap.data.WorkspaceConnectionInfo;

public class SaveWorkspaceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSaveCurrentWorkspace;
	private boolean isCloseAllOpenedWindows;
	private boolean isCloseWorkspace;
	private transient WorkspaceConnectionInfo workspaceConnectionInfo;
	private boolean handled = true;

	public SaveWorkspaceEvent(Object source, boolean isSaveCurrentWorkspace, boolean isCloseAllOpenedWindows, boolean isCloseWorkspace,
			WorkspaceConnectionInfo info) {
		super(source);
		this.isSaveCurrentWorkspace = isSaveCurrentWorkspace;
		this.isCloseAllOpenedWindows = isCloseAllOpenedWindows;
		this.isCloseWorkspace = isCloseWorkspace;
		this.workspaceConnectionInfo = info;
	}

	public boolean isSaveCurrentWorkspace() {
		return this.isSaveCurrentWorkspace;
	}

	public boolean isCloseAllOpenedWindows() {
		return this.isCloseAllOpenedWindows;
	}

	public boolean isCloseWorkspace() {
		return this.isCloseWorkspace;
	}

	public WorkspaceConnectionInfo getWorkspaceConnectionInfo() {
		return this.workspaceConnectionInfo;
	}

	public boolean getHandled() {
		return this.handled;
	}

	public void setHandled(boolean value) {
		this.handled = value;
	}
}
