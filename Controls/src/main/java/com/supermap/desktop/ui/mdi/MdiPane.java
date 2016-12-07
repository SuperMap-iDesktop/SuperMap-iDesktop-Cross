package com.supermap.desktop.ui.mdi;

import com.supermap.desktop.ui.mdi.events.*;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.event.EventListenerList;

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

	/**
	 * @see #getUIClassID
	 */
	private static final String uiClassID = "MdiPaneUI";
	private List<MdiGroup> groups;
	private MdiGroup selectedGroup;
	private JSplitPane mainSplit; // 第一层 split，与 MdiPane 共存亡
	private JSplitPane topSplit; // 最顶层 split，绑定一个 MdiGroup
	private int layoutMode = HORIZONTAL;
	private MdiEventsHelper eventsHelper = new MdiEventsHelper();

	private FocusListener focusListener = new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof MdiGroup) {
				MdiPane.this.selectedGroup = (MdiGroup) e.getSource();
			}
		}
	};
	private PageAddedListener pageAddedListener = new PageAddedListener() {
		@Override
		public void pageAdded(PageAddedEvent e) {
			MdiPane.this.eventsHelper.firePageAdded(e);
		}
	};
	private PageClosingListener pageClosingListener = new PageClosingListener() {
		@Override
		public void pageClosing(PageClosingEvent e) {
			MdiPane.this.eventsHelper.firePageClosing(e);
		}
	};
	private PageClosedListener pageClosedListener = new PageClosedListener() {
		@Override
		public void pageClosed(PageClosedEvent e) {
			MdiPane.this.eventsHelper.firePageClosed(e);
			if (e.getSource() instanceof MdiGroup) {
				MdiGroup group = (MdiGroup) e.getSource();

			}
		}
	};
	private PageActivatingListener pageActivatingListener = new PageActivatingListener() {
		@Override
		public void pageActivating(PageActivatingEvent event) {

		}
	};
	private PageActivatedListener pageActivatedListener = new PageActivatedListener() {
		@Override
		public void pageActivated(PageActivatedEvent event) {

		}
	};

	public MdiPane() {
		this(HORIZONTAL);
	}

	public MdiPane(int layoutMode) {
		this.layoutMode = layoutMode;
		this.groups = new ArrayList<>();
		this.groups.add(createGroup());
		this.selectedGroup = this.groups.get(0);
		this.mainSplit = createSplitPane(this.groups.get(0));
		this.topSplit = this.mainSplit;
		setLayout(new BorderLayout());
		add(this.topSplit, BorderLayout.CENTER);
	}

	public void setLayoutMode(int layoutMode) {
		if (this.layoutMode != layoutMode) {
			this.layoutMode = layoutMode;
		}
	}

	private Map<JSplitPane, Double> getSplitPanes() {
		Map<JSplitPane, Double> splitPanes = new HashMap<>();

		return splitPanes;
	}

	private JSplitPane createSplitPane(JComponent component) {
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(getOrientaition());

		// remove the border from the split pane
		splitPane.setBorder(null);
		splitPane.setOneTouchExpandable(false);
		splitPane.setLeftComponent(component);
		splitPane.setRightComponent(null);
		splitPane.setDividerSize(0);
		return splitPane;
	}

	private int getOrientaition() {
		int orientation = JSplitPane.HORIZONTAL_SPLIT;
		if (this.layoutMode == HORIZONTAL || this.layoutMode < 1) {
			orientation = JSplitPane.HORIZONTAL_SPLIT;
		} else if (this.layoutMode == VERTICAL || this.layoutMode > 2) {
			orientation = JSplitPane.VERTICAL_SPLIT;
		}
		return orientation;
	}

	/**
	 * 创建一个属于该 MdiPane 的 Group
	 *
	 * @return
	 */
	public MdiGroup createGroup() {
		MdiGroup group = new MdiGroup(this);
		registerEvents(group);
		this.groups.add(group);
		return group;
	}

	private void registerEvents(MdiGroup group) {
		group.addFocusListener(this.focusListener);

	}

	// MdiPane

	public MdiPage getSelectedPage() {
		return this.selectedGroup == null ? null : this.selectedGroup.getActivePage();
	}

	public MdiGroup getSelectedGroup() {
		return this.selectedGroup;
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
}
