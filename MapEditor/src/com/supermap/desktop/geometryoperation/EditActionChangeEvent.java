package com.supermap.desktop.geometryoperation;

import java.util.EventObject;

public class EditActionChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EditAction oldEditAction;
	EditAction newEditAction;

	public EditActionChangeEvent(Object source, EditAction oldEditAction, EditAction newEditAction) {
		super(source);
		this.oldEditAction = oldEditAction;
		this.newEditAction = newEditAction;
	}

	public EditAction getOldEditAction() {
		return oldEditAction;
	}

	public EditAction getNewEditAction() {
		return newEditAction;
	}

}
