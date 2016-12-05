package com.supermap.desktop.ui.mdi;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.accessibility.Accessible;
import javax.swing.*;

/**
 * @author highsad
 */
public class MdiPane extends JPanel implements Accessible {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int HORIZONTAL = 1;
	private static final int VERTICAL = 2;
	private int layoutMode = HORIZONTAL;

	/**
	 * @see #getUIClassID
	 */
	private static final String uiClassID = "MdiPaneUI";
	private List<MdiGroup> groups;
	private MdiGroup selectedGroup;

	public MdiPane() {
		this(HORIZONTAL);
	}

	public MdiPane(int layoutMode) {
		this.layoutMode = layoutMode;
		this.groups = new ArrayList<>();
		this.groups.add(createGroup());
		this.selectedGroup = this.groups.get(0);
		layoutComponents();
	}

	private void layoutComponents() {
		if (this.groups.size() == 1) {
			setLayout(new BorderLayout());
			add(this.groups.get(0));
		} else {
			JSplitPane splitPane = createSplitPane(this.groups.get(0), this.layoutMode);
			for (int i = 1; i < this.groups.size() - 1; i++) {
				JSplitPane currentSplit = createSplitPane(this.groups.get(i), this.layoutMode);
				splitPane.setRightComponent(currentSplit);
				splitPane = currentSplit;
			}
			splitPane.setRightComponent(this.groups.get(this.groups.size() - 1));
		}
	}

	public void setLayoutMode(int layoutMode) {
		this.layoutMode = layoutMode;
		layoutComponents();
	}

	private JSplitPane createSplitPane(JComponent component, int layoutMode) {
		JSplitPane splitPane = new JSplitPane();

		int orientation = JSplitPane.HORIZONTAL_SPLIT;
		if (this.layoutMode == HORIZONTAL) {
			orientation = JSplitPane.HORIZONTAL_SPLIT;
		} else if (this.layoutMode == VERTICAL) {
			orientation = JSplitPane.VERTICAL_SPLIT;
		}
		splitPane.setOrientation(orientation);

		// remove the border from the split pane
		splitPane.setBorder(null);

		// set the divider size for a more reasonable, less bulky look
		splitPane.setDividerSize(3);
		splitPane.setOneTouchExpandable(false);  //zw

		splitPane.setLeftComponent(component);
		return splitPane;
	}

	/**
	 * 创建一个属于该 MdiPane 的 Group
	 *
	 * @return
	 */
	public MdiGroup createGroup() {
		MdiGroup group = new MdiGroup(this);
		this.groups.add(group);
		return group;
	}

	// MdiPane

	public MdiPage getSelectedPage() {
		return this.selectedGroup == null ? null : this.selectedGroup.getActivePage();
	}

	/**
	 * 如果当前 pane 只有一个 group，就添加到这个 group 里，如果没有或者有多个，则添加为新的 group
	 *
	 * @param content
	 * @return
	 */
	public MdiGroup add(MdiPage content) {
		MdiGroup group = this.groups.size() == 1 ? this.groups.get(0) : createGroup();

		if (group != null) {
			group.addPage(content);
		}
		return group;
	}

	/**
	 * @param content
	 * @param group
	 */
	public void add(MdiPage content, MdiGroup group) {
		if (!this.groups.contains(group)) {
			this.groups.add(group);
			group.addPage(content);
		}
	}
}
