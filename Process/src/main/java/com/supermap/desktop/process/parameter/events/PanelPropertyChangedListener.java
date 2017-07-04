package com.supermap.desktop.process.parameter.events;

import java.beans.PropertyChangeEvent;
import java.util.EventListener;

/**
 * @author XiaJT
 */
public interface PanelPropertyChangedListener extends EventListener {
	String ENABLE = "ENABLE";
    String DESCRIPTION_VISIBLE = "DESCRIPTION_VISIBLE";

    void propertyChanged(PropertyChangeEvent propertyChangeEvent);
}
