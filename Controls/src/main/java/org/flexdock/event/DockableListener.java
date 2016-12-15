package org.flexdock.event;

import java.util.EventListener;

/**
 * Created by highsad on 2016/12/15.
 */
public interface DockableListener extends EventListener {
	void dockable(DockableEvent e);
}
