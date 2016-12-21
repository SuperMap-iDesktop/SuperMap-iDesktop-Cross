package com.supermap.desktop.ui.mdi.events;

import com.supermap.desktop.ui.mdi.MdiPage;

import java.util.EventObject;

/**
 * Created by highsad on 2016/9/22.
 */
public class PageClosingEvent extends EventObject {

	private MdiPage page;
	private boolean isCancel = false;
	private int operation = Operation.CLOSE;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PageClosingEvent(Object source, MdiPage page, int operationType) {
		super(source);
		this.page = page;
		this.operation = operationType == Operation.CHANGE_GROUP ? Operation.CHANGE_GROUP : Operation.CLOSE;
	}

	public MdiPage getPage() {
		return page;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean cancel) {
		isCancel = cancel;
	}

	public int getOperation() {
		return operation;
	}
}
