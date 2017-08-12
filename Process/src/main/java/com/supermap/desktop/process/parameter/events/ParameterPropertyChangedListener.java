package com.supermap.desktop.process.parameter.events;

import java.util.EventListener;

/**
 * @author XiaJT
 */
public interface ParameterPropertyChangedListener extends EventListener {
	void parameterPropertyChanged(ParameterPropertyChangedEvent parameterPropertyChangedEvent);
}
