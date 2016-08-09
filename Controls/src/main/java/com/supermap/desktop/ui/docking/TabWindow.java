/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */

// $Id: TabWindow.java,v 1.57 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking;

import com.supermap.desktop.ui.docking.drag.DockingWindowDragSource;
import com.supermap.desktop.ui.docking.drag.DockingWindowDragger;
import com.supermap.desktop.ui.docking.drag.DockingWindowDraggerProvider;
import com.supermap.desktop.ui.docking.internal.WriteContext;
import com.supermap.desktop.ui.docking.internalutil.*;
import com.supermap.desktop.ui.docking.model.TabWindowItem;
import com.supermap.desktop.ui.docking.model.ViewWriter;
import com.supermap.desktop.ui.docking.properties.TabWindowProperties;
import net.infonode.properties.base.Property;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;
import net.infonode.properties.util.PropertyChangeListener;
import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabEvent;
import net.infonode.tabbedpanel.TabRemovedEvent;
import net.infonode.util.ArrayUtil;
import net.infonode.util.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * A docking window containing a tabbed panel.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.57 $
 */
public class TabWindow extends AbstractTabWindow {
	private static final ButtonInfo[] buttonInfos = { new UndockButtonInfo(TabWindowProperties.UNDOCK_BUTTON_PROPERTIES),
			new DockButtonInfo(TabWindowProperties.DOCK_BUTTON_PROPERTIES), new MinimizeButtonInfo(TabWindowProperties.MINIMIZE_BUTTON_PROPERTIES),
			new MaximizeButtonInfo(TabWindowProperties.MAXIMIZE_BUTTON_PROPERTIES), new RestoreButtonInfo(TabWindowProperties.RESTORE_BUTTON_PROPERTIES),
			new CloseButtonInfo(TabWindowProperties.CLOSE_BUTTON_PROPERTIES) };

	private AbstractButton[] buttons = new AbstractButton[buttonInfos.length];

	private transient PropertyChangeListener minimumSizePropertiesListener = new PropertyChangeListener() {
		@Override
		public void propertyChanged(Property property, Object valueContainer, Object oldValue, Object newValue) {
			revalidate();
		}
	};
	private transient PropertyMapTreeListener buttonFactoryListener = new PropertyMapTreeListener() {
		@Override
		public void propertyValuesChanged(Map changes) {
			doUpdateButtonVisibility(changes);
		}
	};

	private boolean isAutoCloseNoChild = true;

	private transient DockingWindowDragSource dockingWindowDragSource;

	public boolean isAutoCloseNoChild() {
		return isAutoCloseNoChild;
	}

	public void setAutoCloseNoChild(boolean isAutoCloseNoChild) {
		this.isAutoCloseNoChild = isAutoCloseNoChild;
	}

	/**
	 * Creates an empty tab window.
	 */
	public TabWindow() {
		this((DockingWindow) null);
	}

	/**
	 * Creates a tab window with a tab containing the child window.
	 *
	 * @param window the child window
	 */
	public TabWindow(DockingWindow window) {
		this(window == null ? null : new DockingWindow[] { window });
	}

	/**
	 * Creates a tab window with tabs for the child windows.
	 *
	 * @param windows the child windows
	 */
	public TabWindow(DockingWindow[] windows) {
		this(windows, null);
	}

	protected TabWindow(DockingWindow[] windows, TabWindowItem windowItem) {
		super(true, windowItem == null ? new TabWindowItem() : windowItem);

		setTabWindowProperties(((TabWindowItem) getWindowItem()).getTabWindowProperties());

		PropertyMapWeakListenerManager.addWeakPropertyChangeListener(getTabWindowProperties().getMap(), TabWindowProperties.RESPECT_CHILD_WINDOW_MINIMUM_SIZE,
				minimumSizePropertiesListener);

		setDockingWindowDragSource(new DockingWindowDragSource(getTabbedPanel(), new DockingWindowDraggerProvider() {
			@Override
			public DockingWindowDragger getDragger(MouseEvent mouseEvent) {
				if (!getWindowProperties().getDragEnabled())
					return null;

				Point p = SwingUtilities.convertPoint((Component) mouseEvent.getSource(), mouseEvent.getPoint(), getTabbedPanel());

				return getTabbedPanel().tabAreaContainsPoint(p) ? (getChildWindowCount() == 1 ? getChildWindow(0) : TabWindow.this).startDrag(getRootWindow())
						: null;
			}
		}));

		initMouseListener();
		init();

		getTabbedPanel().addTabListener(new TabAdapter() {
			@Override
			public void tabAdded(TabEvent event) {
				doUpdateButtonVisibility(null);
			}

			@Override
			public void tabRemoved(TabRemovedEvent event) {
				doUpdateButtonVisibility(null);
			}
		});

		if (windows != null) {
			for (int i = 0; i < windows.length; i++)
				addTab(windows[i]);
		}

		PropertyMapWeakListenerManager.addWeakTreeListener(getTabWindowProperties().getMap(), buttonFactoryListener);
	}

	@Override
	public TabWindowProperties getTabWindowProperties() {
		return ((TabWindowItem) getWindowItem()).getTabWindowProperties();
	}

	@Override
	protected void tabSelected(WindowTab tab) {
		super.tabSelected(tab);

		if (getUpdateModel()) {
			((TabWindowItem) getWindowItem()).setSelectedItem(tab == null ? null : getWindowItem().getChildWindowContaining(tab.getWindow().getWindowItem()));
		}
	}

	@Override
	protected void update() {
		// do nothing
	}

	@Override
	protected void updateButtonVisibility() {
		doUpdateButtonVisibility(null);
	}

	private void doUpdateButtonVisibility(Map changes) {
		if (InternalDockingUtil.updateButtons(buttonInfos, buttons, null, this, getTabWindowProperties().getMap(), changes)) {
			updateTabAreaComponents();
		}

		super.updateButtonVisibility();
	}

	@Override
	protected int getTabAreaComponentCount() {
		return ArrayUtil.countNotNull(buttons);
	}

	@Override
	protected void getTabAreaComponents(int index, JComponent[] components) {
		for (int i = 0; i < buttons.length; i++)
			if (buttons[i] != null)
				components[index++] = buttons[i];
	}

	@Override
	protected void optimizeWindowLayout() {
		if (getWindowParent() == null)
			return;

		if (getTabbedPanel().getTabCount() == 0) {
			if (this.isAutoCloseNoChild) {
				internalClose();
			}
		} else if (getTabbedPanel().getTabCount() == 1 && (getWindowParent().showsWindowTitle() || !getChildWindow(0).needsTitleWindow())) {
			getWindowParent().internalReplaceChildWindow(this, getChildWindow(0).getBestFittedWindow(getWindowParent()));
		}
	}

	@Override
	public int addTab(DockingWindow w, int index) {
		int actualIndex = super.addTab(w, index);
		setSelectedTab(actualIndex);
		return actualIndex;
	}

	@Override
	protected int addTabNoSelect(DockingWindow window, int index) {
		DockingWindow beforeWindow = index == getChildWindowCount() ? null : getChildWindow(index);

		int i = super.addTabNoSelect(window, index);

		if (getUpdateModel()) {
			addWindowItem(window,
					beforeWindow == null ? -1 : getWindowItem().getWindowIndex(getWindowItem().getChildWindowContaining(beforeWindow.getWindowItem())));
		}

		return i;
	}

	@Override
	protected void updateWindowItem(RootWindow rootWindow) {
		super.updateWindowItem(rootWindow);
		((TabWindowItem) getWindowItem()).setParentTabWindowProperties(rootWindow == null ? TabWindowItem.emptyPropertiesTemp : rootWindow
				.getRootWindowProperties().getTabWindowProperties());
	}

	@Override
	protected PropertyMap getPropertyObject() {
		return getTabWindowProperties().getMap();
	}

	@Override
	protected PropertyMap createPropertyObject() {
		return new TabWindowProperties().getMap();
	}

	@Override
	protected int getEdgeDepth(Direction dir) {
		return dir == getTabbedPanel().getProperties().getTabAreaOrientation() ? 1 : super.getEdgeDepth(dir);
	}

	@Override
	protected int getChildEdgeDepth(DockingWindow window, Direction dir) {

		return dir == getTabbedPanel().getProperties().getTabAreaOrientation() ? 0 : 1 + super.getChildEdgeDepth(window, dir);
	}

	@Override
	protected DockingWindow getOptimizedWindow() {
		return getChildWindowCount() == 1 ? getChildWindow(0).getOptimizedWindow() : super.getOptimizedWindow();
	}

	@Override
	protected boolean acceptsSplitWith(DockingWindow window) {
		return super.acceptsSplitWith(window) && (getChildWindowCount() != 1 || getChildWindow(0) != window);
	}

	@Override
	protected DockingWindow getBestFittedWindow(DockingWindow parentWindow) {
		return getChildWindowCount() == 1 && (!getChildWindow(0).needsTitleWindow() || parentWindow.showsWindowTitle()) ? getChildWindow(0)
				.getBestFittedWindow(parentWindow) : this;
	}

	@Override
	protected void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
		out.writeInt(WindowIds.TAB);
		viewWriter.writeWindowItem(getWindowItem(), out, context);
		super.write(out, context, viewWriter);
	}

	public DockingWindowDragSource getDockingWindowDragSource() {
		return dockingWindowDragSource;
	}

	public void setDockingWindowDragSource(DockingWindowDragSource dockingWindowDragSource) {
		this.dockingWindowDragSource = dockingWindowDragSource;
	}

}
