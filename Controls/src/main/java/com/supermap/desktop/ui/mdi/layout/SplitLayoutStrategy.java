package com.supermap.desktop.ui.mdi.layout;

import com.supermap.desktop.ui.mdi.IMdiContainer;
import com.supermap.desktop.ui.mdi.MdiGroup;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by highsad on 2016/12/14.
 */
public abstract class SplitLayoutStrategy implements ILayoutStrategy {

	private Map<MdiGroup, JSplitPane> splits = new HashMap<>();
	private IMdiContainer container;

	public SplitLayoutStrategy(IMdiContainer container) {
		this.container = container;
	}

	public Map<MdiGroup, JSplitPane> getSplits() {
		return splits;
	}

	@Override
	public IMdiContainer getContainer() {
		return this.container;
	}

	@Override
	public void addGroup(MdiGroup group) {
		if (!validateGroup(group)) {
			return;
		}

		if (!this.splits.containsKey(group)) {
			this.splits.put(group, createSplit(group));
		}
	}

	@Override
	public void removeGroup(MdiGroup group) {
		if (!validateGroup(group)) {
			return;
		}

		if (this.splits.containsKey(group)) {
			this.splits.remove(group);
		}
	}

	public boolean validateGroup(MdiGroup group) {
		return group != null && group.getMdiContainer() == this.container;
	}

	@Override
	public void layoutGroups() {

	}

	protected JSplitPane createSplit(MdiGroup group) {
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		// remove the border from the split pane
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(null);
		splitPane.setRightComponent(null);
		return splitPane;
	}
}
