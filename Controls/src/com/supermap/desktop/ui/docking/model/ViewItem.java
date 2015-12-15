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

// $Id: ViewItem.java,v 1.8 2005/06/19 20:56:31 jesper Exp $
package com.supermap.desktop.ui.docking.model;

import net.infonode.properties.propertymap.PropertyMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.View;
import com.supermap.desktop.ui.docking.internal.ReadContext;
import com.supermap.desktop.ui.docking.internal.WriteContext;
import com.supermap.desktop.ui.docking.properties.ViewProperties;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.8 $
 */
public class ViewItem extends WindowItem {
	private ViewProperties viewProperties = new ViewProperties();

	public ViewItem() {
		// do nothing
	}

	public ViewItem(ViewItem viewItem) {
		super(viewItem);
	}

	@Override
	protected PropertyMap getPropertyObject() {
		return viewProperties.getMap();
	}

	@Override
	protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows) {
		return null;
	}

	public ViewProperties getViewProperties() {
		return viewProperties;
	}

	@Override
	public void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
		out.writeInt(WindowItemDecoder.VIEW);
		DockingWindow window = getConnectedWindow();
		viewWriter.writeView((View) getConnectedWindow(), out, context);
		out.writeBoolean(window != null && !window.isMinimized() && !window.isUndocked() && window.getRootWindow() != null);
	}

	@Override
	public DockingWindow read(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws IOException {
		return in.readBoolean() ? getConnectedWindow() : null;
	}

	@Override
	public WindowItem copy() {
		return new ViewItem(this);
	}

}
