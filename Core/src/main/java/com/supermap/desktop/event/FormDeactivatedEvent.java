package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IForm;

import java.util.EventObject;

/**
 * Created by highsad on 2017/1/12.
 */
public class FormDeactivatedEvent extends EventObject {

	private IForm form;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public FormDeactivatedEvent(Object source) {
		super(source);
	}

	public IForm getForm() {
		return form;
	}
}
