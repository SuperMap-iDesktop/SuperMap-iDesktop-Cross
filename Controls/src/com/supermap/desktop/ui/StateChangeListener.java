package com.supermap.desktop.ui;

import java.util.EventListener;

public interface StateChangeListener extends EventListener {
	void stateChange(StateChangeEvent e);
}
