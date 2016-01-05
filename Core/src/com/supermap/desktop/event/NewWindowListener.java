package com.supermap.desktop.event;

import java.util.EventListener;

public interface NewWindowListener extends EventListener {
	public void newWindow(NewWindowEvent event);
}
