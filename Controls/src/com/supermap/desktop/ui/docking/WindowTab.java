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

// $Id: WindowTab.java,v 1.58 2009/02/05 15:57:55 jesper Exp $
package com.supermap.desktop.ui.docking;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;

import com.supermap.desktop.ui.docking.internalutil.*;
import com.supermap.desktop.ui.docking.properties.WindowTabProperties;
import com.supermap.desktop.ui.docking.properties.WindowTabStateProperties;

import net.infonode.gui.ContainerList;
import net.infonode.gui.panel.DirectionPanel;
import net.infonode.gui.panel.SimplePanel;
import net.infonode.properties.propertymap.PropertyMap;
import net.infonode.properties.propertymap.PropertyMapListener;
import net.infonode.properties.propertymap.PropertyMapTreeListener;
import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabStateProperties;
import net.infonode.util.Direction;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.58 $
 */
class WindowTab extends TitledTab {
	private static final TitledTabStateProperties EMPTY_PROPERTIES = new TitledTabStateProperties();
	private static final WindowTabProperties EMPTY_TAB_PROPERTIES = new WindowTabProperties();

	private static final ButtonInfo[] buttonInfos = { new UndockButtonInfo(WindowTabStateProperties.UNDOCK_BUTTON_PROPERTIES),
			new DockButtonInfo(WindowTabStateProperties.DOCK_BUTTON_PROPERTIES), new MinimizeButtonInfo(WindowTabStateProperties.MINIMIZE_BUTTON_PROPERTIES),
			new RestoreButtonInfo(WindowTabStateProperties.RESTORE_BUTTON_PROPERTIES), new CloseButtonInfo(WindowTabStateProperties.CLOSE_BUTTON_PROPERTIES) };

	private final DockingWindow window;
	private final AbstractButton[][] buttons = new AbstractButton[WindowTabState.getStateCount()][];
	private final DirectionPanel[] buttonBoxes = new DirectionPanel[WindowTabState.getStateCount()];
	private final DirectionPanel customComponents = new DirectionPanel();
	private final DirectionPanel highlightedFocusedPanel = new DirectionPanel() {
		@Override
		public Dimension getMinimumSize() {
			return new Dimension(0, 0);
		}
	};
	private transient  final WindowTabProperties windowTabProperties = new WindowTabProperties(EMPTY_TAB_PROPERTIES);
	private transient  ContainerList tabComponentsList;
	private boolean isFocused;

	private transient  final PropertyMapListener windowPropertiesListener = new PropertyMapListener() {
		@Override
		public void propertyValuesChanged(PropertyMap propertyObject, Map changes) {
			updateTabButtons(null);
		}
	};

	private transient  final PropertyMapTreeListener windowTabPropertiesListener = new PropertyMapTreeListener() {
		@Override
		public void propertyValuesChanged(Map changes) {
			updateTabButtons(changes);
		}
	};

	WindowTab(DockingWindow window, boolean emptyContent) {
		super(window.getTitle(), window.getIcon(), emptyContent ? null : new SimplePanel(window), null);
		this.window = window;

		for (int i = 0; i < WindowTabState.getStateCount(); i++) {
			buttonBoxes[i] = new DirectionPanel() {
				@Override
				public Dimension getMinimumSize() {
					return new Dimension(0, 0);
				}
			};
			buttons[i] = new AbstractButton[buttonInfos.length];
		}

		highlightedFocusedPanel.add(customComponents);
		highlightedFocusedPanel.add(buttonBoxes[WindowTabState.HIGHLIGHTED.getValue()]);
		setHighlightedStateTitleComponent(highlightedFocusedPanel);
		setNormalStateTitleComponent(buttonBoxes[WindowTabState.NORMAL.getValue()]);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				getWindow().fireTabWindowMouseButtonEvent(e);
				checkPopupMenu(e);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				getWindow().fireTabWindowMouseButtonEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				getWindow().fireTabWindowMouseButtonEvent(e);
				checkPopupMenu(e);
			}

			private void checkPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger() && contains(e.getPoint())) {
					WindowTab.this.window.showPopupMenu(e);
				}
			}

		});

		getProperties().addSuperObject(windowTabProperties.getTitledTabProperties());

		PropertyMapWeakListenerManager.addWeakTreeListener(windowTabProperties.getMap(), windowTabPropertiesListener);

		PropertyMapWeakListenerManager.addWeakListener(this.window.getWindowProperties().getMap(), windowPropertiesListener);

		windowTabProperties.getTitledTabProperties().getHighlightedProperties().addSuperObject(EMPTY_PROPERTIES);
	}
@Override
	public void updateUI() {
		super.updateUI();

		if (buttonBoxes != null)
			for (int i = 0; i < WindowTabState.getStateCount(); i++)
				if (buttonBoxes[i] != null)
					SwingUtilities.updateComponentTreeUI(buttonBoxes[i]);
	}

	void setFocused(boolean focused) {
		if (isFocused != focused) {
			isFocused = focused;
			TitledTabStateProperties properties = focused ? windowTabProperties.getFocusedProperties() : EMPTY_PROPERTIES;
			windowTabProperties.getTitledTabProperties().getHighlightedProperties().getMap()
					.replaceSuperMap(windowTabProperties.getTitledTabProperties().getHighlightedProperties().getMap().getSuperMap(), properties.getMap());
			highlightedFocusedPanel.remove(1);
			highlightedFocusedPanel.add(buttonBoxes[focused ? WindowTabState.FOCUSED.getValue() : WindowTabState.HIGHLIGHTED.getValue()]);
			highlightedFocusedPanel.revalidate();
		}
	}

	void setProperties(WindowTabProperties properties) {
		windowTabProperties.getMap().replaceSuperMap(windowTabProperties.getMap().getSuperMap(), properties.getMap());
	}

	void unsetProperties() {
		setProperties(EMPTY_TAB_PROPERTIES);
	}

	void updateTabButtons(Map changes) {
		WindowTabState[] states = WindowTabState.getStates();

		for (int i = 0; i < states.length; i++) {
			WindowTabState state = states[i];
			WindowTabStateProperties buttonProperties = state == WindowTabState.FOCUSED ? windowTabProperties.getFocusedButtonProperties()
					: state == WindowTabState.HIGHLIGHTED ? windowTabProperties.getHighlightedButtonProperties() : windowTabProperties
							.getNormalButtonProperties();

			InternalDockingUtil.updateButtons(buttonInfos, buttons[i], buttonBoxes[i], window, buttonProperties.getMap(), changes);

			buttonBoxes[i].setDirection((state == WindowTabState.NORMAL ? getProperties().getNormalProperties() : getProperties().getHighlightedProperties())
					.getDirection());
		}

		Direction dir = getProperties().getHighlightedProperties().getDirection();
		highlightedFocusedPanel.setDirection(dir);
		customComponents.setDirection(dir);
	}

	DockingWindow getWindow() {
		return window;
	}

	void windowTitleChanged() {
		setText(getWindow().getTitle());
		setIcon(getWindow().getIcon());
	}
	@Override
	public String toString() {
		return window != null ? window.toString() : null;
	}

	void setContentComponent(Component component) {
		((SimplePanel) getContentComponent()).setComponent(component);
	}

	java.util.List getCustomTabComponentsList() {
		if (tabComponentsList == null)
			tabComponentsList = new ContainerList(customComponents);

		return tabComponentsList;
	}
}
