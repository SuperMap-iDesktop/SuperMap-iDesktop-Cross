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

// $Id: TabWindowItem.java,v 1.13 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking.model;

import net.infonode.properties.propertymap.PropertyMap;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.internal.WriteContext;
import com.supermap.desktop.ui.docking.properties.TabWindowProperties;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.13 $
 */
public class TabWindowItem extends AbstractTabWindowItem {
	public static final TabWindowProperties emptyPropertiesTemp = new TabWindowProperties();

	private TabWindowProperties tabWindowProperties;
	private TabWindowProperties parentProperties = emptyPropertiesTemp;

	public TabWindowItem() {
		tabWindowProperties = new TabWindowProperties(emptyPropertiesTemp);
	}

	public TabWindowItem(TabWindowItem windowItem) {
		super(windowItem);
		tabWindowProperties = new TabWindowProperties(windowItem.getTabWindowProperties().getMap().copy(true, true));
		tabWindowProperties.getMap().replaceSuperMap(windowItem.getParentTabWindowProperties().getMap(), emptyPropertiesTemp.getMap());
	}
	@Override
	protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows) {
		return childWindows.isEmpty() ? null : viewReader.createTabWindow((DockingWindow[]) childWindows.toArray(new DockingWindow[childWindows.size()]), this);
	}

	public TabWindowProperties getTabWindowProperties() {
		return tabWindowProperties;
	}

	public void setTabWindowProperties(TabWindowProperties tabWindowProperties) {
		this.tabWindowProperties = tabWindowProperties;
	}

	public TabWindowProperties getParentTabWindowProperties() {
		return parentProperties;
	}

	public void setParentTabWindowProperties(TabWindowProperties parentProperties) {
		tabWindowProperties.getMap().replaceSuperMap(this.parentProperties.getMap(), parentProperties.getMap());
		this.parentProperties = parentProperties;
	}

	@Override
	public WindowItem copy() {
		return new TabWindowItem(this);
	}

	@Override
	public void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
		out.writeInt(WindowItemDecoder.TAB);
		super.write(out, context, viewWriter);
	}

	@Override
	protected PropertyMap getPropertyObject() {
		return getTabWindowProperties().getMap();
	}

	@Override
	public void clearWindows() {
		// Do nothing
	}

	@Override
	public String toString() {
		return "TabWindow: " + super.toString();
	}

}
