package com.supermap.desktop.process.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/6/15.
 */
public interface StatusChangeListener extends EventListener {
	void statusChange(StatusChangeEvent e);
}
