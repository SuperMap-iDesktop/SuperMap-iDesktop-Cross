package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.events.*;
import com.supermap.desktop.ui.mdi.layout.FlowLayoutStrategy;
import com.supermap.desktop.ui.mdi.layout.ILayoutStrategy;
import com.supermap.desktop.ui.mdi.plaf.MdiGroupUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
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
public class MdiPane extends JPanel implements IMdiContainer, Accessible {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String PERMANENT_FOCUS_OWNER = "permanentFocusOwner";

	/**
	 * @see #getUIClassID
	 */
	private List<MdiGroup> groups; // 排列顺序与 List 顺序保持一致
	private MdiGroup selectedGroup;
	private MdiPage selectedPage;
	private ILayoutStrategy strategy;
	private ILayoutStrategy defaultStrategy = FlowLayoutStrategy.instance(this, FlowLayoutStrategy.HORIZONTAL);
	private MdiEventsHelper eventsHelper = new MdiEventsHelper();
	private MdiGroupHandler mdiGroupHandler = new MdiGroupHandler();
	private PropertyChangeHandler propertyChangeHandler = new PropertyChangeHandler();

	public MdiPane() {
		this(FlowLayoutStrategy.HORIZONTAL);
	}

	public MdiPane(int layoutMode) {
		setLayout(new BorderLayout());
		this.groups = new ArrayList<>();
		this.strategy = this.defaultStrategy;

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(MdiPane.this.propertyChangeHandler);
			}
		});

		this.strategy.layoutGroups();
	}

	public MdiPane(ILayoutStrategy strategy) {
		setLayout(new BorderLayout());
		this.groups = new ArrayList<>();
		this.strategy = strategy;

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(MdiPane.this.propertyChangeHandler);
			}
		});

		this.strategy.layoutGroups();
	}

	/**
	 * 创建一个属于该 MdiPane 的 Group
	 *
	 * @return
	 */
	synchronized public MdiGroup createGroup() {
		MdiGroup group = new MdiGroup(this);
		addGroup(group);
		return group;
	}

	private void addGroup(MdiGroup group) {
		registerEvents(group);
		this.groups.add(group);
		this.strategy.addGroup(group);

		if (this.selectedGroup == null) {

			// 如果此时没有选中的 group，则设置为选中
			this.selectedGroup = group;
		}
		revalidate();
	}

	private void removeGroup(MdiGroup group) {
		if (group != null && this.groups.contains(group)) {
			this.strategy.removeGroup(group);
			unregisterEvents(group);
			this.groups.remove(group);

			if (this.groups.size() == 0) {

				// 当移除最后一个 group 的时候，发送一次 Actived 事件

				this.selectedGroup = null;
				this.selectedPage = null;
				this.eventsHelper.firePageActivated(new PageActivatedEvent(group, null, group.getActivePage()));
			} else {

				// 如果移除 group 之后只剩一个 group 了，那么就把 Strategy 还原为 defaultStrategy
				if (this.groups.size() == 1) {
					setLayoutStrategy(this.defaultStrategy);
				}

				// 将要删除的是当前选中的 group
				if (this.selectedGroup == group) {
					MdiGroup selectedGroup = this.groups.size() > 0 ? this.groups.get(0) : null;
					active(selectedGroup);
				}
			}
		}
	}

	private void registerEvents(MdiGroup group) {
		group.addPageAddedListener(this.mdiGroupHandler);
		group.addPageClosingListener(this.mdiGroupHandler);
		group.addPageClosedListener(this.mdiGroupHandler);
		group.addPageActivatingListener(this.mdiGroupHandler);
		group.addPageActivatedListener(this.mdiGroupHandler);
	}

	private void unregisterEvents(MdiGroup group) {
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

	@Override
	public ILayoutStrategy getLayoutStrategy() {
		return this.strategy;
	}

	@Override
	public void setLayoutStrategy(ILayoutStrategy strategy) {

		// 新设置的 strategy 与 当前 strategy 不一致，则更换 strategy
		if (strategy != null && strategy.getClass() != this.strategy.getClass()) {
			this.strategy.reset();
			this.strategy = strategy;
			this.strategy.layoutGroups();
		}
	}

	@Override
	public MdiGroup[] getGroups() {
		return this.groups.toArray(new MdiGroup[this.groups.size()]);
	}

	public MdiGroup getSelectedGroup() {
		return this.selectedGroup;
	}

	@Override
	public int indexOf(MdiGroup group) {
		return this.groups.indexOf(group);
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

	/**
	 * 将 page 添加到当前激活的 group 中
	 *
	 * @param page
	 * @param pageIndex
	 */
	public void addPage(MdiPage page, int pageIndex) {
		MdiGroup group = this.groups.size() > 0 ? this.selectedGroup : createGroup();

		if (group != null) {
			group.addPage(page, pageIndex);
		}
	}

	/**
	 * 将 page 添加到指定的 group 中
	 *
	 * @param page
	 * @param groupIndex
	 * @param pageIndex
	 */
	public void addPage(MdiPage page, int groupIndex, int pageIndex) {
		if (groupIndex >= 0 && groupIndex < this.groups.size()) {
			MdiGroup group = this.groups.get(groupIndex);
			group.addPage(page, pageIndex);
		}
	}

	public boolean closeAll() {
		boolean isClosed = true;
		for (int i = 0; i < this.groups.size(); i++) {
			isClosed = isClosed && this.groups.get(i).closeAll();
		}
		return isClosed;
	}

	/**
	 * 关闭包含指定 component 的 page
	 *
	 * @param component
	 */
	public boolean close(Component component) {
		boolean isClosed = false;
		MdiPage page = getPage(component);
		if (page != null) {
			isClosed = close(page);
		}
		return isClosed;
	}

	/**
	 * 关闭指定的 page，如果该 page 或者 该 page 的 group 未受 MdiPane 管理，则什么都不做
	 *
	 * @param page
	 * @return
	 */
	public boolean close(MdiPage page) {
		boolean isClosed = false;

		if (page != null && page.getGroup().getMdiContainer() == this) {
			isClosed = page.getGroup().close(page);
		}
		return isClosed;
	}

	public MdiGroup addNewGroup(MdiPage page) {
		MdiGroup group = createGroup();
		group.addPage(page);
		return group;
	}

	public MdiGroup getGroup(int index) {
		return index >= 0 && index < this.groups.size() ? this.groups.get(index) : null;
	}

	@Override
	public int getGroupCount() {
		return this.groups.size();
	}

	/**
	 * 获取所有的 page 数
	 *
	 * @return
	 */
	public int getPageCount() {
		int count = 0;
		for (int i = 0; i < this.groups.size(); i++) {
			count += this.groups.get(i).getPageCount();
		}
		return count;
	}

	/**
	 * 获取指定 index 的 group 的 Page 数
	 * index 顺序为水平从左到右，竖直从上到下
	 *
	 * @param groupIndex
	 * @return
	 */
	public int getPageCount(int groupIndex) {
		if (groupIndex >= 0 && groupIndex < this.groups.size()) {
			return this.groups.get(groupIndex).getPageCount();
		} else {
			return -1;
		}
	}

	/**
	 * 获取所有的 page
	 *
	 * @return
	 */
	public MdiPage[] getPages() {
		ArrayList<MdiPage> pages = new ArrayList<>();

		for (int i = 0; i < this.groups.size(); i++) {
			MdiGroup group = this.groups.get(i);

			for (int j = 0; j < group.getPageCount(); j++) {
				pages.add(group.getPageAt(j));
			}
		}
		return pages.toArray(new MdiPage[pages.size()]);
	}

	/**
	 * 获取包含指定 component 的 page
	 *
	 * @param component
	 * @return
	 */
	public MdiPage getPage(Component component) {
		MdiPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			MdiPage page = pages[i];
			if (page.getComponent() == component) {
				return page;
			}
		}
		return null;
	}

	/**
	 * 获取指定 index 的 page
	 * 如果有多个 group，则按 左→右，上→下 的顺序处理 index
	 *
	 * @param index
	 * @return
	 */
	public MdiPage getPageAt(int index) {
		MdiPage[] pages = getPages();

		if (index >= 0 && index < pages.length) {
			return pages[index];
		}
		return null;
	}

	public MdiPage getActivePage() {
		return this.selectedGroup == null ? null : this.selectedGroup.getActivePage();
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

	/**
	 * 传入 null 则什么都不做，避免恶意传入 null 的事情发生。移除最后一个 group 的时候，需要发送一个 PageActived 事件，在 remove 相关方法中处理。
	 *
	 * @param group
	 */
	@Override
	public void active(MdiGroup group) {
		if (!this.groups.contains(group)) {
			return;
		}

		MdiGroup oldSelectedGroup = MdiPane.this.getSelectedGroup();
		MdiPage oldSelectedPage = oldSelectedGroup == null ? null : MdiPane.this.selectedPage;

		if (group != null && oldSelectedGroup != group) {
			MdiPage newSelectedPage = group == null ? null : group.getActivePage();
			MdiPane.this.eventsHelper.firePageActivating(new PageActivatingEvent(group, newSelectedPage, oldSelectedPage));
			MdiPane.this.selectedGroup = group;
			MdiPane.this.selectedPage = newSelectedPage;
			MdiPane.this.eventsHelper.firePageActivated(new PageActivatedEvent(group, newSelectedPage, oldSelectedPage));
		}
		repaint();
	}

	/**
	 * 激活指定 component 的 page
	 *
	 * @param component
	 */
	public void activePage(Component component) {
		MdiPage[] pages = getPages();
		for (int i = 0; i < pages.length; i++) {
			MdiPage page = pages[i];
			if (page.getComponent() == component) {
				activePage(page);
				break;
			}
		}
	}

	/**
	 * 激活指定的 page，同时需要激活该 page 所在的 group
	 *
	 * @param page
	 */
	public void activePage(MdiPage page) {
		MdiGroup group = page.getGroup();
		if (group != null && group.getMdiContainer() == this) {
			group.activePage(page);
			active(group);
		}
	}

	private class MdiGroupHandler implements PageAddedListener, PageClosingListener, PageClosedListener, PageActivatingListener, PageActivatedListener {

		@Override
		public void pageAdded(PageAddedEvent e) {
			MdiPane.this.eventsHelper.firePageAdded(e);
		}

		@Override
		public void pageClosed(PageClosedEvent e) {
			MdiPane.this.eventsHelper.firePageClosed(e);

			if (e.getSource() instanceof MdiGroup && ((MdiGroup) e.getSource()).getPageCount() == 0) {
				removeGroup((MdiGroup) e.getSource());
			}
		}

		@Override
		public void pageClosing(PageClosingEvent e) {
			MdiPane.this.eventsHelper.firePageClosing(e);
		}

		@Override
		public void pageActivated(PageActivatedEvent e) {

			// 当发送事件的 group 与当前选中的 group 相同时，发送 MdiPane 的 PageActived 事件
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

	private class PropertyChangeHandler implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							if (evt.getPropertyName().equals(PERMANENT_FOCUS_OWNER) && evt.getNewValue() instanceof Component) {
								Component c = (Component) evt.getNewValue();
								MdiGroup group = MdiGroupUtilities.findAncestorGroup(c);
								MdiPane.this.active(group);
							}
						}
					});
				}
			});
		}
	}
}
