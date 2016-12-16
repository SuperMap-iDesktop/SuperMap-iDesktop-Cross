package com.supermap.desktop.ui.mdi.events;

import com.supermap.desktop.ui.mdi.MdiPage;

import java.util.EventObject;

/**
 * Created by highsad on 2016/9/22.
 */
public class PageClosedEvent extends EventObject {

	public static final int CHANGE_GROUP = 1;
	public static final int CLOSE = 2;

	private MdiPage page;
	private int operationType = CLOSE;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PageClosedEvent(Object source, MdiPage page, int operationType) {
		super(source);
		this.page = page;
		this.operationType = operationType == CHANGE_GROUP ? CHANGE_GROUP : CLOSE;
	}

	public MdiPage getPage() {
		return page;
	}

	public int getOperationType() {
		return operationType;
	}
}
