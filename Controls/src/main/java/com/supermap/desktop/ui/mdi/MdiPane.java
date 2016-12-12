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
import javax.swing.border.Border;
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
	private SplitGroup selectedGroup;
	private MdiPage selectedPage;
	private int layoutMode = HORIZONTAL;
	private MdiEventsHelper eventsHelper = new MdiEventsHelper();
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


	/**
	 * 创建一个属于该 MdiPane 的 Group
	 *
	 * @return
	 */
	synchronized public MdiGroup createGroup() {
		SplitGroup split = new SplitGroup();
		addGroup(split);
		adjustDividerProportion();
		return split.getGroup();
	}

	private void addGroup(SplitGroup split) {
		registerEvents(split.getGroup());
		SplitGroup preSplit = this.groups.size() > 0 ? this.groups.get(this.groups.size() - 1) : null;
		this.groups.add(split);

		if (preSplit != null) {
			preSplit.setNextSplit(split);
		} else {
			add(split.getSplitPane(), BorderLayout.CENTER);
			split.resetDividerSize();
		}

		if (this.selectedGroup == null) {

			// 如果此时没有选中的 group，则设置为选中
			this.selectedGroup = split;
		}
	}

	private void removeGroup(MdiGroup group) {
		SplitGroup splitGroup = findSplitGroup(group);

		if (splitGroup != null) {
			unregisterEvents(group);
			SplitGroup preSplit = findPreSplit(splitGroup);
			SplitGroup nextSplit = findNextSplit(splitGroup);

			if (preSplit != null) {
				preSplit.setNextSplit(nextSplit);
			} else {

				// 上级 split 为空，意味着当前 split 为第一级 split
				remove(splitGroup.getSplitPane());
				if (nextSplit != null) {
					add(nextSplit.getSplitPane(), BorderLayout.CENTER);
				}
			}
			this.groups.remove(splitGroup);
			unregisterEvents(splitGroup.getGroup());

			// 处理选中
			if (this.selectedGroup == splitGroup) {
				this.selectedGroup = this.groups.size() > 0 ? this.groups.get(0) : null;
				if (this.selectedGroup != null) {
					this.selectedGroup.getGroup().requestFocus();
				}
			}
		}
	}

	private SplitGroup findSplitGroup(MdiGroup group) {
		SplitGroup splitGroup = null;
		for (int i = 0; i < this.groups.size(); i++) {
			if (this.groups.get(i).getGroup() == group) {
				splitGroup = this.groups.get(i);
				break;
			}
		}
		return splitGroup;
	}

	private SplitGroup findPreSplit(SplitGroup split) {
		SplitGroup preSplit = null;
		int index = this.groups.indexOf(split);

		if (index > 0) {
			preSplit = this.groups.get(index - 1);
		}
		return preSplit;
	}

	private SplitGroup findNextSplit(SplitGroup split) {
		SplitGroup nextSplit = null;
		int index = this.groups.indexOf(split);

		if (index > -1 && index + 1 < this.groups.size()) {
			nextSplit = this.groups.get(index + 1);
		}
		return nextSplit;
	}

	/**
	 * 调整 DividerProportion，为其计算一个合适的 DividerProportion
	 */
	public void adjustDividerProportion() {
		for (int i = 0; i < this.groups.size(); i++) {
			this.groups.get(i).setDividerProportion(1d / (this.groups.size() - i));
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
		return this.selectedGroup == null ? null : this.selectedGroup.getGroup();
	}

	/**
	 * 添加到 selectedGroup 里，如果没有，则新建一个
	 *
	 * @param page
	 * @return
	 */
	public MdiGroup addPage(MdiPage page) {
		MdiGroup group = this.groups.size() > 0 ? this.selectedGroup.getGroup() : createGroup();

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
			this.dividerProportion = 0.5;
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
			this.splitPane.setResizeWeight(this.dividerProportion);
		}

		public void setLayoutMode(int layoutMode) {
			this.splitPane.setOrientation(getOrientaition(layoutMode));
			this.splitPane.setDividerLocation(this.dividerProportion);
			this.splitPane.setResizeWeight(this.dividerProportion);
			this.splitPane.doLayout();
		}

		public void setNextSplit(SplitGroup nextSplit) {
			if (nextSplit != null) {
				this.splitPane.setRightComponent(nextSplit.getSplitPane());
			} else {
				this.splitPane.setRightComponent(null);
			}
			resetDividerSize();
		}

		private void resetDividerSize(JSplitPane splitPane) {
			splitPane.setDividerSize(splitPane.getRightComponent() == null ? 0 : 3);
		}

		private void resetDividerSize() {
			resetDividerSize(this.splitPane);
		}

		private JSplitPane createSplitPane(JComponent component, int layoutMode) {
			this.splitPane = new JSplitPane();
			splitPane.setOrientation(getOrientaition(layoutMode));

			// remove the border from the split pane
			splitPane.setBorder(null);
			splitPane.setOneTouchExpandable(false);
			splitPane.setLeftComponent(component);
			splitPane.setRightComponent(null);
			resetDividerSize();

			if (splitPane.getUI() instanceof BasicSplitPaneUI) {
				BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
				divider.setBorder(null);

				divider.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// 当 Didiver 位置改变之后，记录一下 proportion，切换 LayoutMode 之后需要使用该值
						SplitGroup.this.dividerProportion = getDividerProportion(splitPane);
						SplitGroup.this.splitPane.setResizeWeight(SplitGroup.this.dividerProportion);
					}
				});
			}
			return splitPane;
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

		}

		@Override
		public void componentMoved(ComponentEvent e) {

		}

		@Override
		public void componentShown(ComponentEvent e) {
			System.out.println("ComponentShown");
		}

		@Override
		public void componentHidden(ComponentEvent e) {

		}

		@Override
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof MdiGroup) {
				MdiGroup oldSelectedGroup = MdiPane.this.getSelectedGroup();
				MdiPage oldSelectedPage = oldSelectedGroup == null ? null : MdiPane.this.selectedPage;
				MdiGroup newSelectedGroup = e.getSource() instanceof MdiGroup ? (MdiGroup) e.getSource() : null;

				if (newSelectedGroup != null) {
					MdiPage newSelectedPage = newSelectedGroup.getActivePage();
					MdiPane.this.eventsHelper.firePageActivating(new PageActivatingEvent(newSelectedGroup, newSelectedPage, oldSelectedPage));
					MdiPane.this.selectedGroup = MdiPane.this.findSplitGroup(newSelectedGroup);
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
