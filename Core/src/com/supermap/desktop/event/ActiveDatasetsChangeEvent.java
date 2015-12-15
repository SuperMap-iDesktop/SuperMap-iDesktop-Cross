package com.supermap.desktop.event;

import java.util.EventObject;

import com.supermap.data.Dataset;

public class ActiveDatasetsChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Dataset[] oldActiveDatasets;
	private transient Dataset[] newActiveDatasets;

	public ActiveDatasetsChangeEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public ActiveDatasetsChangeEvent(Object source, Dataset[] oldActiveDatasets, Dataset[] newActiveDatasets) {
		super(source);
		this.oldActiveDatasets = oldActiveDatasets;
		this.newActiveDatasets = newActiveDatasets;
	}

	public Dataset[] getOldActiveDatasets() {
		return oldActiveDatasets;
	}

	public Dataset[] getNewActiveDatasets() {
		return newActiveDatasets;
	}
}
