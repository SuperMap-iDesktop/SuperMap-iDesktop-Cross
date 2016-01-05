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

// $Id: RootWindowItem.java,v 1.8 2005/03/11 13:16:49 jesper Exp $
package com.supermap.desktop.ui.docking.model;

import net.infonode.properties.propertymap.PropertyMap;

import java.util.ArrayList;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.properties.RootWindowProperties;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.8 $
 */
public class RootWindowItem extends WindowItem {
	private RootWindowProperties rootWindowProperties = RootWindowProperties.createDefault();

	public RootWindowItem() {
		// 默认实现，后续进行初始化操作
	}

	public RootWindowItem(RootWindowItem windowItem) {
		super(windowItem);
	}

	protected PropertyMap createPropertyObject() {
		return new RootWindowProperties().getMap();
	}

	@Override
	protected PropertyMap getPropertyObject() {
		return rootWindowProperties.getMap();
	}

	public RootWindowProperties getRootWindowProperties() {
		return rootWindowProperties;
	}

	@Override
	public boolean isRestoreWindow() {
		return true;
	}

	@Override
	protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows) {
		return childWindows.isEmpty() ? null : (DockingWindow) childWindows.get(0);
	}

	@Override
	public RootWindowItem getRootItem() {
		return this;
	}

	@Override
	public WindowItem copy() {
		return new RootWindowItem(this);
	}

}
