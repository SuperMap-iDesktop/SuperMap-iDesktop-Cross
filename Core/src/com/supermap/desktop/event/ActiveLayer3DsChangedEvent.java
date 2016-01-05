package com.supermap.desktop.event;

import java.util.EventObject;

import com.supermap.realspace.Layer3D;

public class ActiveLayer3DsChangedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Layer3D[] oldActiveLayer3Ds;
	private transient Layer3D[] newActiveLayer3Ds;

	public ActiveLayer3DsChangedEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public ActiveLayer3DsChangedEvent(Object source, Layer3D[] oldActiveLayer3Ds, Layer3D[] newActiveLayer3Ds) {
		super(source);
		this.oldActiveLayer3Ds = oldActiveLayer3Ds;
		this.newActiveLayer3Ds = newActiveLayer3Ds;
	}

	public Layer3D[] getOldActiveLayer3Ds() {
		return oldActiveLayer3Ds;
	}

	public Layer3D[] getNewActiveLayer3Ds() {
		return newActiveLayer3Ds;
	}
}
