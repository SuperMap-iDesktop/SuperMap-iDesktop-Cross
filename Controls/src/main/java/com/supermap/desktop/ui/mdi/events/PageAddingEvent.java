package com.supermap.desktop.ui.mdi.events;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;

import java.util.EventObject;

/**
 * Created by highsad on 2016/12/24.
 */
public class PageAddingEvent extends EventObject {

	private int operation = Operation.ADD;
	private MdiGroup group; // destination
	private MdiPage page;
	private boolean isCancel = false;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param group The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public PageAddingEvent(MdiGroup group, MdiPage page, int operation) {
		super(group);
		this.group = group;
		this.page = page;
		this.operation = operation;
	}

	public int getOperation() {
		return operation;
	}

	public MdiGroup getGroup() {
		return group;
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
}
