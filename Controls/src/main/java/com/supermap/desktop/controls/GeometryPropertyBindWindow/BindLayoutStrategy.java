package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.MdiPane;
import com.supermap.desktop.ui.mdi.events.Operation;
import com.supermap.desktop.ui.mdi.events.PageAddedEvent;
import com.supermap.desktop.ui.mdi.events.PageAddedListener;
import com.supermap.desktop.ui.mdi.layout.SplitLayoutStrategy;

/**
 * Created by xie on 2016/12/22.
 */
public class BindLayoutStrategy extends SplitLayoutStrategy {

	private MdiGroup tabularGroup;
	private PageAddedHandler pageAddedHandler = new PageAddedHandler();

	public BindLayoutStrategy(MdiPane container) {
		super(container);
		this.tabularGroup = new MdiGroup(container);
	}

	public MdiGroup getTabularGroup() {
		return tabularGroup;
	}

	@Override
	public IMdiContainer getContainer() {
		return container;
	}

	@Override
	public void addGroup(MdiGroup group) {

	}

	@Override
	public void removeGroup(MdiGroup group) {
		super.removeGroup(group);
	}

	@Override
	public void layoutGroups() {
		super.layoutGroups();
	}

	@Override
	public void reset() {
		super.reset();
	}

	private class PageAddedHandler implements PageAddedListener {
		@Override
		public void pageAdded(PageAddedEvent e) {
			if (e.getOperation() == Operation.ADD) {

				// 当新增 page 的时候，如果 page 是属性表的窗口，就把它移到到
			}
		}
	}

}
