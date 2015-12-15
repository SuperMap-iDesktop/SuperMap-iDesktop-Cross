package com.supermap.desktop.event;

import java.util.EventObject;

import com.supermap.mapping.Layer;

public class ActiveLayersChangedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Layer[] oldActiveLayers;
	private transient Layer[] newActiveLayers;

	public ActiveLayersChangedEvent(Object source) {
		super(source);
	}

	public ActiveLayersChangedEvent(Object source, Layer[] oldActiveLayers, Layer[] newActiveLayers) {
		super(source);
		this.oldActiveLayers = oldActiveLayers;
		this.newActiveLayers = newActiveLayers;
	}

	public Layer[] getOldActiveLayers() {
		return oldActiveLayers;
	}

	public Layer[] getNewActiveLayers() {
		return newActiveLayers;
	}
}
