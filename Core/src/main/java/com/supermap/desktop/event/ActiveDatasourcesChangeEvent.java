package com.supermap.desktop.event;

import com.supermap.data.Datasource;

import java.util.EventObject;

public class ActiveDatasourcesChangeEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Datasource[] oldActiveDatasources;
	private transient Datasource[] newActiveDatasources;

	public ActiveDatasourcesChangeEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public ActiveDatasourcesChangeEvent(Object source, Datasource[] oldActiveDatasources, Datasource[] newActiveDatasources) {
		super(source);
		this.oldActiveDatasources = oldActiveDatasources;
		this.newActiveDatasources = newActiveDatasources;
	}

	public Datasource[] getOldActiveDatasources() {
		return oldActiveDatasources;
	}

	public Datasource[] getNewActiveDatasources() {
		return newActiveDatasources;
	}
}
