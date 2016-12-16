package com.supermap.desktop.ui.mdi.events;

import com.supermap.desktop.ui.mdi.MdiPage;

import java.util.EventObject;

/**
 * Created by highsad on 2016/9/26.
 */
public class PageActivatedEvent extends EventObject {

	private MdiPage oldPage;
	private MdiPage newPage;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PageActivatedEvent(Object source, MdiPage newPage, MdiPage oldPage) {
		super(source);
		this.newPage = newPage;
		this.oldPage = oldPage;
	}

	public MdiPage getActivedPage() {
		return this.newPage;
	}

	public MdiPage getOldActivedPage() {
		return this.oldPage;
	}
}
