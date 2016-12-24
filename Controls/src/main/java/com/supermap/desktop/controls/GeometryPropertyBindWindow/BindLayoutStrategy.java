package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.ui.FormManager;
import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.MdiPane;
import com.supermap.desktop.ui.mdi.events.*;
import com.supermap.desktop.ui.mdi.layout.ILayoutStrategy;
import com.supermap.desktop.ui.mdi.layout.SplitLayoutStrategy;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xie on 2016/12/22.
 */
public class BindLayoutStrategy implements ILayoutStrategy {

	private FormManager container;
	private MdiGroup tabularGroup;
	private JSplitPane contentSplit;
	private ArrayList<MdiGroup> mapGroups;
	private HashMap<MdiGroup, JSplitPane> splits;

	private PageAddedHandler pageAddedHandler = new PageAddedHandler();

	public BindLayoutStrategy(FormManager container) {
		this.container = container;
		this.tabularGroup = new MdiGroup(container);
		this.mapGroups = new ArrayList<>();

		this.contentSplit = new JSplitPane();
		this.contentSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.contentSplit.setBorder(null);
		this.contentSplit.setOneTouchExpandable(false);
		this.contentSplit.setLeftComponent(null);
		this.contentSplit.setRightComponent(this.tabularGroup);
		this.contentSplit.setResizeWeight(0.7);
		if (this.contentSplit.getUI() instanceof BasicSplitPaneUI) {
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI) this.contentSplit.getUI()).getDivider();
			divider.setBorder(null);
		}
	}

	public MdiGroup getTabularGroup() {
		return this.tabularGroup;
	}

	private void initMapGroups() {
		MdiPage[] pages = this.container.getPages();
		for (int i = 0; i < pages.length; i++) {
			MdiPage page = pages[i];
			MdiGroup group = this.container.createGroup();
		}
	}

	@Override
	public IMdiContainer getContainer() {
		return this.container;
	}

	/**
	 * 添加一个 group，只要不是 tabularGroup 就放在上半部分水平并列布局
	 *
	 * @param group
	 * @return
	 */
	@Override
	public boolean addGroup(MdiGroup group) {
		if (!validateGroup(group) || group == this.tabularGroup) {
			return false;
		}

		boolean result = false;

		return result;
	}

	@Override
	public boolean removeGroup(MdiGroup group) {
		if (!validateGroup(group)) {
			return false;
		}
		return true;
	}

	public boolean validateGroup(MdiGroup group) {
		return group != null && group.getMdiContainer() == this.container;
	}

	@Override
	public void layoutGroups() {
	}

	@Override
	public void reset() {
		this.splits.clear();
	}

	private JSplitPane createSplit(MdiGroup group) {
		JSplitPane splitPane = new JSplitPane();

		// remove the border from the split pane
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(null);
		splitPane.setRightComponent(null);

		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(group);

		if (splitPane.getUI() instanceof BasicSplitPaneUI) {
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
			divider.setBorder(null);
		}
		return splitPane;
	}

	/**
	 * 设置 Divider 的大小，如果 rightComponent 为 null，设置为 0，否则设置为 3
	 *
	 * @param splitPane
	 */
	private void resetDividerSize(JSplitPane splitPane) {
		splitPane.setDividerSize(splitPane.getRightComponent() == null ? 0 : 3);
	}


	private class PageAddedHandler implements PageAddingListener {

		@Override
		public void pageAdding(PageAddingEvent e) {

			// 验证一下，属性表如果没添加到 tabularGroup 里，或者场景/地图窗口添加到 tabularGroup 里了，就纠正一下
			if (e.getOperation() == Operation.ADD) {
				MdiPage page = e.getPage();
				MdiGroup group = page.getGroup();

				if (page != null) {
					if (page.getComponent() instanceof IFormTabular && group != BindLayoutStrategy.this.tabularGroup) {

						// 如果将要添加一个 IFormTabular 到非指定的 tabularGroup，那么就处理一下，请它过去
						e.setCancel(true);
						BindLayoutStrategy.this.tabularGroup.addPage(page);
					} else if (page.getComponent() instanceof IForm && group == BindLayoutStrategy.this.tabularGroup) {

						// 如果将要添加一个非 IFormTabular 的 IForm 到指定的 tabularGroup，那么就处理一下，请它离开
						e.setCancel(true);
						BindLayoutStrategy.this.container.getSelectedGroup().addPage(page);
					}
				}
			}
		}
	}

}
