package com.supermap.desktop.event;

import java.util.EventListener;

public interface ActiveDatasetsChangeListener extends EventListener {
	void activeDatasetsChange(ActiveDatasetsChangeEvent e);
}
