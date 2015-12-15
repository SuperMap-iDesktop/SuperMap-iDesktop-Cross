package com.supermap.desktop.event;

import java.util.EventListener;

public interface SaveWorkspaceListener extends EventListener {
	public void saveWorkspace(SaveWorkspaceEvent event);
}