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

// $Id: BlueHighlightDockingTheme.java,v 1.11 2005/12/04 13:46:04 jesper Exp $
package com.supermap.desktop.ui.docking.theme;

import net.infonode.gui.icon.button.CloseIcon;
import net.infonode.gui.icon.button.MinimizeIcon;
import net.infonode.gui.icon.button.RestoreIcon;
import net.infonode.tabbedpanel.theme.BlueHighlightTheme;

import java.awt.*;

import com.supermap.desktop.ui.docking.properties.RootWindowProperties;
import com.supermap.desktop.ui.docking.properties.WindowTabProperties;

/**
 * A theme where the tab of the focused window has a blue background.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.11 $
 */
public final class BlueHighlightDockingTheme extends DockingWindowsTheme {
	private RootWindowProperties rootWindowProperties;

	public BlueHighlightDockingTheme() {
		rootWindowProperties = createRootWindowProperties();
	}

	@Override
	public String getName() {
		return "Blue Highlight Theme";
	}

	@Override
	public RootWindowProperties getRootWindowProperties() {
		return rootWindowProperties;
	}

	/**
	 * Create a root window properties object with the property values for this theme.
	 *
	 * @return the root window properties object
	 */
	public static final RootWindowProperties createRootWindowProperties() {
		BlueHighlightTheme blueHighlightTheme = new BlueHighlightTheme();
		BlueHighlightTheme greyHighlightTheme = new BlueHighlightTheme(new Color(150, 150, 150));

		RootWindowProperties properties = new RootWindowProperties();

		properties.getTabWindowProperties().getTabbedPanelProperties().addSuperObject(greyHighlightTheme.getTabbedPanelProperties());

		WindowTabProperties tabProperties = properties.getTabWindowProperties().getTabProperties();
		tabProperties.getTitledTabProperties().addSuperObject(greyHighlightTheme.getTitledTabProperties());

		tabProperties.getFocusedProperties().addSuperObject(blueHighlightTheme.getTitledTabProperties().getHighlightedProperties());

		tabProperties.getHighlightedButtonProperties().getCloseButtonProperties()
				.setIcon(new CloseIcon(Color.WHITE, RootWindowProperties.DEFAULT_WINDOW_TAB_BUTTON_ICON_SIZE));

		tabProperties.getHighlightedButtonProperties().getMinimizeButtonProperties()
				.setIcon(new MinimizeIcon(Color.WHITE, RootWindowProperties.DEFAULT_WINDOW_TAB_BUTTON_ICON_SIZE));

		tabProperties.getHighlightedButtonProperties().getRestoreButtonProperties()
				.setIcon(new RestoreIcon(Color.WHITE, RootWindowProperties.DEFAULT_WINDOW_TAB_BUTTON_ICON_SIZE));

		properties.getComponentProperties().setBackgroundColor(new Color(180, 190, 220));

		return properties;
	}

}
