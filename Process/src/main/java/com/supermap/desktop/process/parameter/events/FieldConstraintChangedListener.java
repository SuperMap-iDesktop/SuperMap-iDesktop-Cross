package com.supermap.desktop.process.parameter.events;

import java.util.EventListener;

/**
 * @author XiaJT
 */
public interface FieldConstraintChangedListener extends EventListener {
	void fieldConstraintChanged(FieldConstraintChangedEvent event);
}
