package com.supermap.desktop.ui.mdi.events;

import com.supermap.desktop.ui.mdi.MdiPage;

import java.util.EventObject;

/**
 * Created by highsad on 2016/9/22.
 */
public class PageClosedEvent extends EventObject {

	private MdiPage page;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PageClosedEvent(Object source, MdiPage page) {
		super(source);
		this.page = page;
	}

	public MdiPage getPage() {
		return page;
	}
}
