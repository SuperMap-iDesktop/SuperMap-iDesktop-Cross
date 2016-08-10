package com.supermap.desktop.mapview.layer.propertycontrols;

import java.util.EventListener;

public interface ChangedListener extends EventListener {
	void changed(ChangedEvent e);
}
