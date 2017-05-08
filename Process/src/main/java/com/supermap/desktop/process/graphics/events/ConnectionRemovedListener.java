package com.supermap.desktop.process.graphics.events;

import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/5/8.
 */
public interface ConnectionRemovedListener extends ActionListener {
	void connectionRemoved(ConnectionRemovedEvent e);
}
