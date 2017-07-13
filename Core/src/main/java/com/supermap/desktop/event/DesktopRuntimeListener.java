package com.supermap.desktop.event;

import java.util.EventListener;

/**
 * @author XiaJT
 */
public interface DesktopRuntimeListener extends EventListener {
	void stateChanged(DesktopRuntimeEvent event);
}
