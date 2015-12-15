package com.supermap.desktop.event;

import java.util.EventObject;

import com.supermap.desktop.Interface.IForm;

public class ActiveFormChangedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient IForm oldActiveForm;
	private transient IForm newActiveForm;

	public ActiveFormChangedEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public ActiveFormChangedEvent(Object source, IForm oldActiveForm, IForm newActiveForm) {
		super(source);
		this.oldActiveForm = oldActiveForm;
		this.newActiveForm = newActiveForm;
	}

	public IForm getOldActiveForm() {
		return oldActiveForm;
	}

	public IForm getNewActiveForm() {
		return newActiveForm;
	}
}
