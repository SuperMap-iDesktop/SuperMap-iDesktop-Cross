package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.ui.FormManager;
import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.layout.ILayoutStrategy;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xie on 2016/12/22.
 */
public class BindLayoutStrategy implements ILayoutStrategy {

	private static final UserObject MAPS = new UserObject("maps");
	private static final UserObject TABULARS = new UserObject("tabulars");

	private BindHandler bindHandler;
	private FormManager container;
	private JSplitPane contentSplit;
	private ArrayList<MdiGroup> mapGroups; // 待关联的地图子窗口在上并列显示，待关联的属性表子窗口在下并列显示
	private ArrayList<MdiGroup> tabularGroups; // 需要并列显示的属性表 Group

	private HashMap<MdiGroup, JSplitPane> mapSplits;
	private HashMap<MdiGroup, JSplitPane> tabularSplits;

	public BindLayoutStrategy(FormManager container, BindHandler bindHandler) {
		this.bindHandler = bindHandler;
		this.container = container;
		this.mapGroups = new ArrayList<>();
		this.tabularGroups = new ArrayList<>();
		this.mapSplits = new HashMap<>();
		this.tabularSplits = new HashMap<>();

		this.contentSplit = new JSplitPane();
		this.contentSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.contentSplit.setBorder(null);
		this.contentSplit.setOneTouchExpandable(false);
		this.contentSplit.setLeftComponent(null);
		this.contentSplit.setRightComponent(null);
		this.contentSplit.setResizeWeight(0.7);
		if (this.contentSplit.getUI() instanceof BasicSplitPaneUI) {
			BasicSplitPaneDivider divider = ((BasicSplitPaneUI) this.contentSplit.getUI()).getDivider();
			divider.setBorder(null);
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
		if (!validateGroup(group)) {
			return false;
		}

		addGroup(group.getUserObject(), group);
		return true;
	}

	private void addGroup(Object type, MdiGroup group) {
		if (!getSplits(type).containsKey(group)) {
			getSplits(type).put(group, createSplit(group));
		}

		getGroups(type).add(group);
		JSplitPane split = getSplits(type).get(group);
		JSplitPane preSplit = findPreSplit(type, group);

		if (preSplit != null) {
			setNextSplit(preSplit, split);
		} else {
			if (type == TABULARS) {
				this.contentSplit.setRightComponent(split);
			} else {
				this.contentSplit.setLeftComponent(split);
			}
			resetDividerSize(split);
			this.contentSplit.revalidate();
		}
		adjustDividerProportion(type);
	}

	@Override
	public boolean removeGroup(MdiGroup group) {
		if (!validateGroup(group) || (!this.mapGroups.contains(group) && !this.tabularGroups.contains(group))) {
			return false;
		}

		removeGroup(group.getUserObject(), group);
		return true;
	}

	private void removeGroup(Object type, MdiGroup group) {

		// 将要移除的 group 不是 tabularGroup
		JSplitPane split = getSplits(type).get(group);
		JSplitPane preSplit = findPreSplit(type, group);
		JSplitPane nextSplit = findNextSplit(type, group);
		split.setRightComponent(null);

		if (preSplit != null) {
			setNextSplit(preSplit, nextSplit);
		} else {

			// 没有上级 split 了，就把自己从 content 中移除
			if (type == TABULARS) {
				this.contentSplit.setRightComponent(null);
			} else {
				this.contentSplit.setLeftComponent(null);
			}
			this.contentSplit.revalidate();
			this.contentSplit.repaint();
			if (nextSplit != null) {

				if (type == TABULARS) {
					this.contentSplit.setRightComponent(nextSplit);
				} else {
					this.contentSplit.setLeftComponent(nextSplit);
				}
			}
		}
		getGroups(type).remove(group);
	}

	private JSplitPane findPreSplit(Object type, MdiGroup group) {
		int index = getGroups(type).indexOf(group);
		MdiGroup preGroup = index > 0 ? getGroups(type).get(index - 1) : null;
		return preGroup == null ? null : getSplits(type).get(preGroup);
	}

	private JSplitPane findNextSplit(Object type, MdiGroup group) {
		int index = getGroups(type).indexOf(group);
		MdiGroup nextGroup = index >= 0 && index + 1 < getGroups(type).size() ? getGroups(type).get(index + 1) : null;
		return nextGroup == null ? null : getSplits(type).get(nextGroup);
	}

	private void setNextSplit(JSplitPane split, JSplitPane nextSplit) {
		if (split == null) {
			return;
		}

		if (nextSplit != null) {
			split.setRightComponent(nextSplit);
		} else {
			split.setRightComponent(null);
		}
		resetDividerSize(split);
	}

	public boolean validateGroup(MdiGroup group) {
		return group != null && group.getMdiContainer() == this.container;
	}

	@Override
	public void layoutGroups() {

		// 添加底层的 Split
		this.container.add(this.contentSplit);

		// 首先进行已有 group 的布局
		MdiGroup[] groups = container.getGroups();
		if (groups != null && groups.length > 0) {
			for (int i = 0; i < groups.length; i++) {
				addGroup(groups[i]);
			}
		}

		// 一山不容二虎，所有待关联子窗口都各自占用一个山头，哦不，是各自占用一个 Group
		// 其他非关联子窗口添加到第一个待关联子窗口的 Group 里
		MdiPage[] pages = this.container.getPages();
		ArrayList<MdiPage> unrelatedPages = new ArrayList<>();
		MdiGroup firstRelatedGroup = null;

		// 先进行关联窗口的布局
		for (int i = 0; i < pages.length; i++) {
			MdiPage page = pages[i];
			if (this.bindHandler.getFormMapList().contains(page.getComponent())) {
				MdiGroup group = this.container.addNewGroup(page, MAPS);
				if (firstRelatedGroup == null) {
					firstRelatedGroup = group;
				}
			} else if (this.bindHandler.getFormTabularList().contains(page.getComponent())) {
				this.container.addNewGroup(page, TABULARS);
			} else {
				unrelatedPages.add(page);
			}
		}

		// 再处理不需要关联的子窗口
		for (int i = 0; i < unrelatedPages.size(); i++) {
			if (firstRelatedGroup != null) {
				firstRelatedGroup.addPage(unrelatedPages.get(i));
			}
		}
	}

	@Override
	public void reset() {
		this.bindHandler = null;
		this.container.remove(this.contentSplit);
		this.mapGroups.clear();
		this.tabularGroups.clear();
		this.mapSplits.clear();
		this.tabularSplits.clear();
	}

	private JSplitPane createSplit(MdiGroup group) {
		JSplitPane splitPane = new JSplitPane();

		// remove the border from the split pane
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(group);
		splitPane.setRightComponent(null);

		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

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

	/**
	 * 调整 DividerProportion，为其计算一个合适的 DividerProportion
	 */
	private void adjustDividerProportion(Object type) {
		for (int i = 0; i < getGroups(type).size(); i++) {
			getSplits(type).get(getGroups(type).get(i)).setResizeWeight(1d / (getGroups(type).size() - i));
		}
	}

	private ArrayList<MdiGroup> getGroups(Object type) {
		return type == TABULARS ? this.tabularGroups : this.mapGroups;
	}

	private HashMap<MdiGroup, JSplitPane> getSplits(Object type) {
		return type == TABULARS ? this.tabularSplits : this.mapSplits;
	}
}
