package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.events.*;
import com.supermap.desktop.ui.mdi.exception.MdiActionModeException;
import com.supermap.desktop.ui.mdi.exception.NullParameterException;
import com.supermap.desktop.ui.mdi.action.*;
import com.supermap.desktop.ui.mdi.exception.UnknownPageException;
import com.supermap.desktop.ui.mdi.plaf.MdiGroupUI;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;

// @formatter:off
/**
 * MdiGroup 不能单独存在，与 MdiPane 强制绑定，因此不提供开放构造方法。 使用 MdiPane 创建一个 Group，@see
 * MdiPane.createGroup()
 * 
 * @author highsad
 *
 */
// @formatter:on
public class MdiGroup extends JComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String uiClassID = "MdiGroupUI";

	private EventListenerList listenerList = new EventListenerList();

	private List<MdiPage> pages;
	private MdiPane mdiPane = null;
	private List<Integer> floatingPages = new ArrayList<Integer>();

	/**
	 * 与 groupActions 互斥。
	 * 所有 Page 的功能按钮，排布在 MdiPage 的 Tab 页上。
	 * 按索引顺序从右往左排列。
	 */
	private List<IMdiAction> pageActions = new ArrayList<IMdiAction>();

	/**
	 * 与 pageActions 互斥。
	 * 所有 group 的功能按钮，排布在 MdiGroup 的右上角。
	 * 按索引顺序从右往左排列。
	 */
	private List<IMdiAction> groupActions = new ArrayList<IMdiAction>();
	private IMdiAction closeAction = new ActionPageClose();
	private IMdiAction floatAction = new ActionPageFloat();
	private IMdiAction prePageAction = new ActionPrePage();
	private IMdiAction nextPageAction = new ActionNextPage();
	private IMdiAction pagesListAction = new ActionPagesList();

	/**
	 * Group 中当前显示的 page，如果 Group 又同时被选中，那么该 page 就是 MdiPane 的selectedPage。
	 * 除非没有 page，否则默认总有一个 page 被选中。
	 */
	private MdiPage activePage = null;

	static {
		UIManager.put(uiClassID, "com.supermap.desktop.ui.mdi.plaf.MdiGroupUI");
	}

	public MdiGroup(MdiPane mdiPane) {
		this.mdiPane = mdiPane;
		this.pages = new ArrayList<MdiPage>();
		initializeActions();
		registerEvents();
		updateUI();
	}

	private void registerEvents() {

	}

	// L&F

	public MdiGroupUI getUI() {
		return (MdiGroupUI) ui;
	}

	public void setUI(MdiGroupUI ui) {
		if (this.ui != ui) {
			super.setUI(ui);
			repaint();
		}
	}

	// 覆盖父类的getUIClassID，此方法会被UIManager调用。
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	@Override
	public void updateUI() {
		setUI((MdiGroupUI) UIManager.getUI(this));
	}

	// MdiGroup

	public void initializeActions() {
		this.pageActions.add(this.closeAction);
//		this.pageActions.add(this.floatAction);
		this.groupActions.add(this.pagesListAction);
		this.groupActions.add(this.nextPageAction);
		this.groupActions.add(this.prePageAction);
	}

	public MdiPage getActivePage() {
		return this.activePage;
	}

	/**
	 * @param page
	 */
	public void addPage(MdiPage page) {
		addPage(page, getPageCount());
	}

	/**
	 * @param page
	 * @param index
	 */
	public void addPage(MdiPage page, int index) {
		if (page == null) {
			return;
		}

		for (MdiPage mdiPage : pages) {

			// 如果该分组已经存在相同的 page 内容，则直接返回
			if (mdiPage == page || mdiPage.getComponent() == page.getComponent()) {
				return;
			}
		}

		// 设置一下初始状态
		page.getComponent().setVisible(false);
		page.setGroup(this);
		this.pages.add(page);
		add(page.getComponent(), index);
		firePageAdded(new PageAddedEvent(this, page, this.pages.indexOf(page)));
		activePage(page);
	}

	public void activePage(int pageIndex) {
		if (this.pages.size() == 0 || pageIndex < 0 || pageIndex >= this.pages.size()) {
			return;
		}

		activePage(getPageAt(pageIndex));
	}

	public void activePage(MdiPage activePage) {
		if (activePage == null) {
			return;
		}

		if (this.pages.size() == 0 || !this.pages.contains(activePage)) {
			return;
		}

		firePageActivating(new PageActivatingEvent(this, activePage));
		this.activePage = activePage;

		// activePage 可见，其余不可见
		for (int i = 0; i < this.pages.size(); i++) {
			this.pages.get(i).getComponent().setVisible(this.pages.get(i) == activePage);
		}
		firePageActivated(new PageActivatedEvent(this, activePage));

		// 更改状态重绘
		revalidate();
		repaint();
	}

	public void activePage(Component component) {
		MdiPage page = getPage(component);
		if (page != null) {
			activePage(page);
		}
	}

	public int indexOf(MdiPage page) {
		return this.pages.indexOf(page);
	}

	public int getActivePageIndex() {
		return this.pages.size() == 0 ? -1 : this.pages.indexOf(this.activePage);
	}

	public boolean isActive(int pageIndex) {
		return getActivePageIndex() == pageIndex;
	}

	public boolean isActive(MdiPage page) {
		return this.pages.contains(page) && this.activePage == page;
	}

	public void close(MdiPage page) {
		if (page != null && this.pages.contains(page)) {

			PageClosingEvent removingEvent = new PageClosingEvent(this, page);
			firePageClosing(removingEvent);

			if (!removingEvent.isCancel()) {
				boolean isCloseActivePage = page.isActive();
				int closingIndex = this.pages.indexOf(page);
				page.getComponent().setVisible(false);
				this.remove(page.getComponent());
				this.pages.remove(page);
				page.setGroup(null);
				firePageClosed(new PageClosedEvent(this, page));

				if (isCloseActivePage) {

					// 关闭一个在激活状态的页面之后，默认激活前一个页面，关闭的已经是最前的页面，则激活下一个页面
					int activeIndex = -1;
					for (int i = closingIndex - 1; i >= 0; i--) {
						if (!isPageFloating(i)) {

							// page 不在浮动状态，就激活它
							activeIndex = i;
							break;
						}
					}

					if (activeIndex == -1) {
						for (int i = activeIndex >= 0 ? activeIndex : 0; i < this.pages.size(); i++) {
							if (!isPageFloating(i)) {

								// page 不在浮动状态，就激活它
								activeIndex = i;
								break;
							}
						}
					}

					if (activeIndex >= 0) {
						activePage(activeIndex);
					} else {

						// 直到最后都还没找到合适的，那就是没有 page 了，这时候 activePage 只能是 null，别无选择
						this.activePage = null;
					}
				}

				// 重绘
				validate();
				repaint();
			}
		}
	}

	public void close(int pageIndex) {
		close(getPageAt(pageIndex));
	}

	public MdiPane getMdiParent() {
		return this.mdiPane;
	}

	public Component getComponent(int index) {
		Component c = null;

		if (this.pages.size() > index && 0 <= index) {
			c = this.pages.get(index).getComponent();
		}
		return c;
	}

	public MdiPage getPage(Component component) {
		MdiPage page = null;
		for (int i = 0; i < this.pages.size(); i++) {
			if (this.pages.get(i).getComponent() == component) {
				page = this.pages.get(i);
				break;
			}
		}
		return page;
	}

	public MdiPage[] getPages() {
		return this.pages.toArray(new MdiPage[this.pages.size()]);
	}

	public int getPageCount() {
		return this.pages.size();
	}

	public MdiPage getPageAt(int index) {
		MdiPage page = null;

		if (this.pages != null && this.pages.size() > index && index >= 0) {
			page = this.pages.get(index);
		}
		return page;
	}

	public boolean isContain(MdiPage page) {
		return indexOf(page) >= 0;
	}

	public boolean isFunctionOnPage(IMdiAction function) {
		return this.pageActions.contains(function);
	}

	/**
	 * 将指定的自定义功能添加到 MdiPage 的标签页上，仅 {@link IMdiAction#getMode()} 值
	 * 为 {@link ActionMode#PAGE} 或者 {@link ActionMode#PAGE_GROUP} 时可用
	 *
	 * @param action 自定义功能
	 */
	public void addPageAction(IMdiAction action) {
		if (action == null) {
			throw new NullParameterException("action");
		}

		if (action.getMode() == ActionMode.GROUP) {
			throw new MdiActionModeException("GROUP");
		}

		if (!this.pageActions.contains(action)) {
			this.pageActions.add(action);
		}
	}

	/**
	 * 将指定的自定义功能添加到 MdiPage 的标签页上指定的位置，仅 {@link IMdiAction#getMode()} 值
	 * 为 {@link ActionMode#PAGE} 或者 {@link ActionMode#PAGE_GROUP} 时可用
	 *
	 * @param action 自定义功能
	 * @param index  索引
	 */
	public void addPageAction(IMdiAction action, int index) {
		if (action == null) {
			throw new NullParameterException("action");
		}

		if (action.getMode() == ActionMode.GROUP) {
			throw new MdiActionModeException("GROUP");
		}

		if (this.pageActions.contains(action)) {

			// 已有，什么都不做
			return;
		}

		if (index >= 0 && index < this.pageActions.size()) {
			this.pageActions.add(index, action);
		} else {
			this.pageActions.add(action);
		}
	}

	/**
	 * 将指定的自定义功能添加到 MdiGroup 的右上角，仅 {@link IMdiAction#getMode()} 值
	 * 为 {@link ActionMode#GROUP} 或者 {@link ActionMode#PAGE_GROUP} 时可用
	 *
	 * @param action
	 */
	public void addGroupActions(IMdiAction action) {
		if (action == null) {
			throw new NullParameterException("action");
		}

		if (action.getMode() == ActionMode.PAGE) {
			throw new MdiActionModeException("PAGE");
		}

		if (!this.groupActions.contains(action)) {
			this.groupActions.add(action);
		}
	}

	/**
	 * 将指定的自定义功能添加到 MdiPage 的右上角指定的位置，仅 {@link IMdiAction#getMode()} 值
	 * 为 {@link ActionMode#GROUP} 或者 {@link ActionMode#PAGE_GROUP} 时可用
	 *
	 * @param action
	 * @param index
	 */
	public void addGroupActions(IMdiAction action, int index) {
		if (action == null) {
			throw new NullParameterException("action");
		}

		if (action.getMode() == ActionMode.PAGE) {
			throw new MdiActionModeException("PAGE");
		}

		if (this.groupActions.contains(action)) {

			// 已有，什么都不做
			return;
		}

		if (index >= 0 && index < this.groupActions.size()) {
			this.groupActions.add(index, action);
		} else {
			this.groupActions.add(action);
		}
	}

	public boolean isCloseOnPage() {
		return this.pageActions.contains(this.closeAction);
	}

	public void setCloseOnPage(boolean isCloseOnPage) {
		if (isCloseOnPage) {
			this.groupActions.remove(this.closeAction);

			// 关闭功能默认加在最右
			addPageAction(this.closeAction, 0);
		} else {
			this.pageActions.remove(this.closeAction);

			// 关闭功能默认加在 pagesList 隔壁，不能存在 pagesList，就加在最右
			int pageListIndex = this.groupActions.indexOf(this.pagesListAction);
			addGroupActions(this.closeAction, pageListIndex >= 0 ? pageListIndex + 1 : 0);
		}
	}

	/**
	 * 暂时不支持 page 的 float
	 * 以后添加该功能
	 *
	 * @return
	 */
	public boolean isFloatOnPage() {
		return this.pageActions.contains(this.floatAction);
	}

	/**
	 * 暂时不支持 float
	 * 以后添加该功能
	 *
	 * @param isFloatOnPage
	 */
	public void setFloatOnPage(boolean isFloatOnPage) {
//		if (isFloatOnPage) {
//			this.groupActions.remove(this.floatAction);
//
//			// 浮动功能默认加在关闭按钮隔壁，没有关闭按钮则加在最右
//			int closeIndex = this.pageActions.indexOf(this.closeAction);
//			if (closeIndex >= 0) {
//				addPageAction(this.floatAction, closeIndex + 1);
//			} else {
//				addPageAction(this.floatAction, 0);
//			}
//		} else {
//			this.pageActions.remove(this.floatAction);
//
//			// 浮动功能默认加在关闭按钮隔壁，没有关闭按钮则加在 pagesList 隔壁
//			int closeIndex = this.groupActions.indexOf(this.closeAction);
//			if (closeIndex >= 0) {
//				addGroupActions(this.floatAction, closeIndex + 1);
//			} else {
//				addGroupActions(this.floatAction, 1);
//			}
//		}
	}

	/**
	 * 获取该组处于浮动状态的 pages
	 * 暂时不支持 float，以后添加该功能
	 *
	 * @return
	 */
	public Integer[] getFloatingPages() {
		return this.floatingPages.toArray(new Integer[]{this.floatingPages.size()});
	}

	public int getFloatingPageCount() {
		return this.floatingPages.size();
	}

	public boolean isPageFloating(int pageIndex) {
		if (pageIndex < 0 || pageIndex >= this.pages.size()) {
			throw new IndexOutOfBoundsException();
		}

		return this.floatingPages.indexOf(pageIndex) >= 0;
	}

	public boolean isPageFloating(MdiPage page) {
		if (page == null) {
			throw new NullParameterException(page.getTitle());
		}

		int pageIndex = indexOf(page);
		if (pageIndex >= 0) {
			return isPageFloating(pageIndex);
		} else {
			throw new UnknownPageException(page.getTitle());
		}
	}

	public void floatPage(MdiPage page) {
		// TODO
	}

	public void floatPage(int pageIndex) {
		// TODO
	}

	/**
	 * 获取 groupActions，即 MdiGroup 右上角功能区的所有功能。返回 IMdiAction[] 是为了避免对 this.groupActions 的错误修改。
	 *
	 * @return
	 */
	public IMdiAction[] getGroupActions() {
		return this.groupActions.toArray(new IMdiAction[this.groupActions.size()]);
	}

	/**
	 * 获取 pageActions，即 MdiPage 的 Tab 上的所有功能。返回 IMdiAction[] 是为了避免对 this.pageActions 的错误修改。
	 *
	 * @return
	 */
	public IMdiAction[] getPageActions() {
		return this.pageActions.toArray(new IMdiAction[this.pageActions.size()]);
	}

	///// 事件处理 /////

	public void addPageAddedListener(PageAddedListener listener) {
		this.listenerList.add(PageAddedListener.class, listener);
	}

	public void removePageAddedListener(PageAddedListener listener) {
		this.listenerList.remove(PageAddedListener.class, listener);
	}

	public void addPageClosedListener(PageClosedListener listener) {
		this.listenerList.add(PageClosedListener.class, listener);
	}

	public void removePageClosedListener(PageClosedListener listener) {
		this.listenerList.remove(PageClosedListener.class, listener);
	}

	public void addPageClosingListener(PageClosingListener listener) {
		this.listenerList.add(PageClosingListener.class, listener);
	}

	public void removePageClosingListener(PageClosingListener listener) {
		this.listenerList.remove(PageClosingListener.class, listener);
	}

	public void addPageActivatingListener(PageActivatingListener listener) {
		this.listenerList.add(PageActivatingListener.class, listener);
	}

	public void removePageActivatingListener(PageActivatingListener listener) {
		this.listenerList.remove(PageActivatingListener.class, listener);
	}

	public void addPageActivatedListener(PageActivatedListener listener) {
		this.listenerList.add(PageActivatedListener.class, listener);
	}

	public void removePageActivatedListener(PageActivatedListener listener) {
		this.listenerList.remove(PageActivatedListener.class, listener);
	}

	private void firePageAdded(PageAddedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageAddedListener.class) {
				((PageAddedListener) listeners[i + 1]).pageAdded(e);
			}
		}
	}

	private void firePageClosed(PageClosedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageClosedListener.class) {
				((PageClosedListener) listeners[i + 1]).pageRemoved(e);
			}
		}
	}

	private void firePageClosing(PageClosingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageClosingListener.class) {
				((PageClosingListener) listeners[i + 1]).pageRemoving(e);
			}
		}
	}

	private void firePageActivating(PageActivatingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageActivatingListener.class) {
				((PageActivatingListener) listeners[i + 1]).pageActivating(e);
			}
		}
	}

	private void firePageActivated(PageActivatedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PageActivatedListener.class) {
				((PageActivatedListener) listeners[i + 1]).pageActivated(e);
			}
		}
	}
}
