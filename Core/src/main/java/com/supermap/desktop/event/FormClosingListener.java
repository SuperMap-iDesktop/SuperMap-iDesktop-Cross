package com.supermap.desktop.event;

import java.util.EventListener;

/**
 * Created by highsad on 2016/11/24.
 */
public interface FormClosingListener extends EventListener {
	void formClosing(FormClosingEvent e);
}
