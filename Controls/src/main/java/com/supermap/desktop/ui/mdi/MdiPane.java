package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.events.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * MdiPane 与 MdiGroup 可以完全分离，但 MdiGroup 与 MdiPage 则是强制绑定
 *
 * @author highsad
 */
public class MdiPane extends JPanel implements Accessible {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int HORIZONTAL = 1;
	private static final int VERTICAL = 2;

	/**
	 * @see #getUIClassID
	 */
	private List<SplitGroup> groups; // 排列顺序与 List 顺序保持一致
	private MdiGroup selectedGroup;
	private MdiPage selectedPage;
	private JSplitPane lastSplit; // 最后级 split，绑定一个 MdiGroup
	private int layoutMode = HORIZONTAL;
	private MdiEventsHelper eventsHelper = new MdiEventsHelper();
	private Map<JSplitPane, Double> dividerProportions = new HashMap<>();
	private MdiGroupHandler mdiGroupHandler = new MdiGroupHandler();
	private ComponentHandler componentHandler = new ComponentHandler();

	public MdiPane() {
		this(HORIZONTAL);
	}

	public MdiPane(int layoutMode) {
		setLayout(new BorderLayout());
		this.layoutMode = layoutMode;
		this.groups = new ArrayList<>();
		addComponentListener(this.componentHandler);
	}

	public void setLayoutMode(int layoutMode) {
		if (this.layoutMode != layoutMode) {
			this.layoutMode = layoutMode;

			for (int i = 0; i < this.groups.size(); i++) {
				this.groups.get(i).setLayoutMode(layoutMode);
			}
		}
	}

	private double getDividerProportion(JSplitPane splitPane) {
		if (splitPane == null) {
			return 0.5;
		}

		double proportion;
		int location = splitPane.getDividerLocation();
		if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
			proportion = (double) location / (splitPane.getHeight() - splitPane.getDividerSize());
		} else {
			proportion = (double) location / (splitPane.getWidth() - splitPane.getDividerSize());
		}
		return proportion;
	}


	private void setDividerSize(JSplitPane splitPane, boolean isLast) {
		splitPane.setDividerSize(isLast ? 0 : 3);
	}

	/**
	 * 创建一个属于该 MdiPane 的 Group
	 *
	 * @return
	 */
	public MdiGroup createGroup() {
		SplitGroup group = new SplitGroup();
		registerEvents(group.getGroup());
		this.groups.add(group);
		return group.getGroup();
	}

	private void removeGroup(MdiGroup group) {
		if (group != null && group.getParent() instanceof JSplitPane) {
			unregisterEvents(group);
			this.groups.remove(group);
			JSplitPane splitPane = (JSplitPane) group.getParent();
			JSplitPane parentSplit = splitPane.getParent() instanceof JSplitPane ? (JSplitPane) splitPane.getParent() : null;
			Component sibling = splitPane.getRightComponent();
			splitPane.setLeftComponent(null); // remove current group

			if (parentSplit != null) {

				// 上级 split 不为空，表示当前 split 并非第一级 split
				if (sibling == null) {

					// 当前 group 的 sibling 为空，意味着当前 group 是最后一个 group
					parentSplit.setRightComponent(null);
					setDividerSize(parentSplit, true);
					MdiPane.this.lastSplit = parentSplit;
				} else {

					// 当前 group 的 sibling 不为空，则用 sibling 替换当前 split
					splitPane.setRightComponent(null);
					parentSplit.setRightComponent(sibling);
					setDividerSize(parentSplit, false);
				}
			} else {

				// 上级 split 为空，意味着当前 split 为第一级 split
				if (sibling == null) {

					// sibling 为空，则移除当前 split
					MdiPane.this.remove(splitPane);
					MdiPane.this.lastSplit = null;
				} else {

					// sibling 不为空，则用 sibling 的内容替换到当前 split
					MdiPane.this.remove(splitPane);
					MdiPane.this.add(sibling, BorderLayout.CENTER);
					MdiPane.this.lastSplit = (JSplitPane) sibling;
				}
			}
		}
	}

	private void registerEvents(MdiGroup group) {
		group.addFocusListener(this.componentHandler);
		group.addPageAddedListener(this.mdiGroupHandler);
		group.addPageClosingListener(this.mdiGroupHandler);
		group.addPageClosedListener(this.mdiGroupHandler);
		group.addPageActivatingListener(this.mdiGroupHandler);
		group.addPageActivatedListener(this.mdiGroupHandler);
	}

	private void unregisterEvents(MdiGroup group) {
		group.removeFocusListener(this.componentHandler);
		group.removePageAddedListener(this.mdiGroupHandler);
		group.removePageClosingListener(this.mdiGroupHandler);
		group.removePageClosedListener(this.mdiGroupHandler);
		group.removePageActivatingListener(this.mdiGroupHandler);
		group.removePageActivatedListener(this.mdiGroupHandler);
		group.removePageActivatedListener(this.mdiGroupHandler);
	}

	// MdiPane

	public MdiPage getSelectedPage() {
		return this.selectedPage;
	}

	public MdiGroup getSelectedGroup() {
		return this.selectedGroup;
	}

	/**
	 * 添加到 selectedGroup 里，如果没有，则新建一个
	 *
	 * @param page
	 * @return
	 */
	public MdiGroup addPage(MdiPage page) {
		MdiGroup group = this.groups.size() > 0 ? this.selectedGroup : createGroup();

		if (group != null) {
			group.addPage(page);
		}
		return group;
	}

	public MdiGroup addNewGroup(MdiPage page) {
		MdiGroup group = createGroup();
		group.addPage(page);
		return group;
	}

	public MdiGroup getGroup(int index) {
		return index >= 0 && index < this.groups.size() ? this.groups.get(index).getGroup() : null;
	}

	public int getPageCount() {
		int count = 0;
		for (int i = 0; i < this.groups.size(); i++) {
			count += this.groups.get(i).getGroup().getPageCount();
		}
		return count;
	}

	/**
	 * 不能遍历 Groups 转发事件，因为如果在之后有新增加的 Group，则无法正常工作
	 *
	 * @param listener
	 */
	public void addPageAddedListener(PageAddedListener listener) {
		this.eventsHelper.addPageAddedListener(listener);
	}

	public void removePageAddedListener(PageAddedListener listener) {
		this.eventsHelper.removePageAddedListener(listener);
	}

	public void addPageClosedListener(PageClosedListener listener) {
		this.eventsHelper.addPageClosedListener(listener);
	}

	public void removePageClosedListener(PageClosedListener listener) {
		this.eventsHelper.removePageClosedListener(listener);
	}

	public void addPageClosingListener(PageClosingListener listener) {
		this.eventsHelper.addPageClosingListener(listener);
	}

	public void removePageClosingListener(PageClosingListener listener) {
		this.eventsHelper.removePageClosingListener(listener);
	}

	public void addPageActivatingListener(PageActivatingListener listener) {
		this.eventsHelper.addPageActivatingListener(listener);
	}

	public void removePageActivatingListener(PageActivatingListener listener) {
		this.eventsHelper.removePageActivatingListener(listener);
	}

	public void addPageActivatedListener(PageActivatedListener listener) {
		this.eventsHelper.addPageActivatedListener(listener);
	}

	public void removePageActivatedListener(PageActivatedListener listener) {
		this.eventsHelper.removePageActivatedListener(listener);
	}

	private class SplitGroup {
		private MdiGroup group;
		private JSplitPane splitPane;
		private double dividerProportion;

		public SplitGroup() {
			this(new MdiGroup(MdiPane.this));
		}

		public SplitGroup(MdiGroup group) {
			this(group, MdiPane.HORIZONTAL);
		}

		public SplitGroup(MdiGroup group, int layoutMode) {
			this.group = group;
			this.splitPane = createSplitPane(group, layoutMode);
		}

		public MdiGroup getGroup() {
			return group;
		}

		public JSplitPane getSplitPane() {
			return splitPane;
		}

		public void setDividerProportion(double dividerProportion) {
			this.dividerProportion = dividerProportion;
			this.splitPane.setDividerLocation(this.dividerProportion);
		}

		public void setLayoutMode(int layoutMode) {
			this.splitPane.setOrientation(getOrientaition(layoutMode));
			this.splitPane.setDividerLocation(this.dividerProportion);
		}

		private JSplitPane createSplitPane(JComponent component, int layoutMode) {
			final JSplitPane splitPane = new JSplitPane();
			splitPane.setOrientation(getOrientaition(layoutMode));

			// remove the border from the split pane
			splitPane.setBorder(null);
			splitPane.setOneTouchExpandable(false);
			splitPane.setLeftComponent(component);
			splitPane.setRightComponent(null);
			splitPane.setResizeWeight(1);
			setDividerSize(splitPane, true);

			if (splitPane.getUI() instanceof BasicSplitPaneUI) {
				BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
				divider.setBorder(null);
			}

			splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					int location = splitPane.getDividerLocation();
					if (splitPane.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
						SplitGroup.this.dividerProportion = (double) location / (splitPane.getHeight() - splitPane.getDividerSize());
					} else {
						SplitGroup.this.dividerProportion  = (double) location / (splitPane.getWidth() - splitPane.getDividerSize());
					}
				}
			});
			return splitPane;
		}

		private int getOrientaition(int layoutMode) {
			int orientation = JSplitPane.HORIZONTAL_SPLIT;
			if (layoutMode == HORIZONTAL || layoutMode < 1) {
				orientation = JSplitPane.HORIZONTAL_SPLIT;
			} else if (layoutMode == VERTICAL || layoutMode > 2) {
				orientation = JSplitPane.VERTICAL_SPLIT;
			}
			return orientation;
		}
	}

	private class MdiGroupHandler implements PageAddedListener, PageClosingListener, PageClosedListener, PageActivatingListener, PageActivatedListener {

		@Override
		public void pageAdded(PageAddedEvent e) {
			MdiPane.this.eventsHelper.firePageAdded(e);
		}

		@Override
		public void pageClosed(PageClosedEvent e) {
			if (e.getSource() instanceof MdiGroup) {
				removeGroup((MdiGroup) e.getSource());
			}
			MdiPane.this.eventsHelper.firePageClosed(e);
		}

		@Override
		public void pageClosing(PageClosingEvent e) {
			MdiPane.this.eventsHelper.firePageClosing(e);
		}

		@Override
		public void pageActivated(PageActivatedEvent e) {
			if (e.getSource() == MdiPane.this.selectedGroup) {
				MdiPane.this.selectedPage = e.getActivedPage();
				MdiPane.this.eventsHelper.firePageActivated(e);
			}
		}

		@Override
		public void pageActivating(PageActivatingEvent e) {
			if (e.getSource() == MdiPane.this.selectedGroup) {
				MdiPane.this.eventsHelper.firePageActivating(e);
			}
		}
	}

	private class ComponentHandler implements FocusListener, ComponentListener {

		@Override
		public void componentResized(ComponentEvent e) {
			Component splitPane = MdiPane.this.lastSplit;
			while (splitPane instanceof JSplitPane) {
				((JSplitPane) splitPane).setDividerLocation(0.5);
				splitPane = splitPane.getParent();
			}
		}

		@Override
		public void componentMoved(ComponentEvent e) {

		}

		@Override
		public void componentShown(ComponentEvent e) {

		}

		@Override
		public void componentHidden(ComponentEvent e) {

		}

		@Override
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof MdiGroup) {
				MdiGroup oldSelectedGroup = MdiPane.this.selectedGroup;
				MdiPage oldSelectedPage = oldSelectedGroup == null ? null : MdiPane.this.selectedPage;
				MdiGroup newSelectedGroup = e.getSource() instanceof MdiGroup ? (MdiGroup) e.getSource() : null;

				if (newSelectedGroup != null) {
					MdiPage newSelectedPage = newSelectedGroup.getActivePage();
					MdiPane.this.eventsHelper.firePageActivating(new PageActivatingEvent(newSelectedGroup, newSelectedPage, oldSelectedPage));
					MdiPane.this.selectedGroup = newSelectedGroup;
					MdiPane.this.selectedPage = newSelectedPage;
					MdiPane.this.eventsHelper.firePageActivated(new PageActivatedEvent(newSelectedGroup, newSelectedPage, oldSelectedPage));
				} else {
					oldSelectedGroup.requestFocus();
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {

		}
	}
}
