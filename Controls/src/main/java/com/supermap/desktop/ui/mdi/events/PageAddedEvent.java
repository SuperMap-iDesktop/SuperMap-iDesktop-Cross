package com.supermap.desktop.ui.mdi.events;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;

import java.util.EventObject;

/**
 * Created by highsad on 2016/9/22.
 */
public class PageAddedEvent extends EventObject {

	private MdiPage page;
	private int index;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PageAddedEvent(Object source, MdiPage page, int index) {
		super(source);
		this.page = page;
		this.index = index;
	}

	public MdiGroup getGroup() {
		return (MdiGroup) getSource();
	}

	public MdiPage getPage() {
		return page;
	}

	public int getIndex() {
		return index;
	}
}
