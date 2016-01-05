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

// $Id: DockingWindowsTheme.java,v 1.3 2004/09/28 14:48:03 jesper Exp $
package com.supermap.desktop.ui.docking.theme;

import com.supermap.desktop.ui.docking.properties.RootWindowProperties;

/**
 * A docking windows theme. A theme provides a {@link RootWindowProperties} object which can be applied to a {@link com.supermap.desktop.ui.docking.RootWindow}
 * like this:
 *
 * <pre>
 * rootWindow.getRootWindowProperties().addSuperObject(theme.getRootWindowProperties());
 * </pre>
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 * @since IDW 1.1.0
 */
abstract public class DockingWindowsTheme {
	/**
	 * Returns the name of this theme.
	 *
	 * @return the name of this theme
	 */
	abstract public String getName();

	/**
	 * Returns the root window properties for this theme.
	 *
	 * @return the root window properties for this theme
	 */
	abstract public RootWindowProperties getRootWindowProperties();

	protected DockingWindowsTheme() {
	}

	@Override
	public String toString() {
		return getName();
	}

}
