package com.supermap.desktop.event;

import java.util.EventListener;

public interface ActiveDatasourcesChangeListener extends EventListener {
	void activeDatasourcesChange(ActiveDatasourcesChangeEvent e);
}
