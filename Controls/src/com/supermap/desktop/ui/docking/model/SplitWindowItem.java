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

// $Id: SplitWindowItem.java,v 1.12 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking.model;

import net.infonode.properties.propertymap.PropertyMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.internal.ReadContext;
import com.supermap.desktop.ui.docking.internal.WriteContext;
import com.supermap.desktop.ui.docking.properties.SplitWindowProperties;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.12 $
 */
public class SplitWindowItem extends WindowItem {
	public static final SplitWindowProperties emptyPropertiesTemp = new SplitWindowProperties();

	public static SplitWindowProperties getEmptypropertiestemp() {
		return emptyPropertiesTemp;
	}

	private boolean isHorizontal;
	private float dividerLocation;
	private SplitWindowProperties splitWindowProperties;
	private SplitWindowProperties parentProperties = SplitWindowItem.getEmptypropertiestemp();

	public SplitWindowItem() {
		splitWindowProperties = new SplitWindowProperties(SplitWindowItem.getEmptypropertiestemp());
	}

	public SplitWindowItem(SplitWindowItem windowItem) {
		super(windowItem);
		splitWindowProperties = new SplitWindowProperties(windowItem.getSplitWindowProperties().getMap().copy(true, true));
		splitWindowProperties.getMap().replaceSuperMap(windowItem.getParentSplitWindowProperties().getMap(), SplitWindowItem.getEmptypropertiestemp().getMap());
	}

	public SplitWindowItem(WindowItem leftWindow, WindowItem rightWindow, boolean horizontal, float dividerLocation) {
		addWindow(leftWindow);
		addWindow(rightWindow);
		isHorizontal = horizontal;
		this.dividerLocation = dividerLocation;
	}

	@Override
	protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows) {
		return childWindows.isEmpty() ? null : childWindows.size() == 1 ? (DockingWindow) childWindows.get(0) : viewReader.createSplitWindow(
				(DockingWindow) childWindows.get(0), (DockingWindow) childWindows.get(1), this);
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public float getDividerLocation() {
		return dividerLocation;
	}

	public void setHorizontal(boolean horizontal) {
		isHorizontal = horizontal;
	}

	public void setDividerLocation(float dividerLocation) {
		this.dividerLocation = dividerLocation;
	}

	public SplitWindowProperties getSplitWindowProperties() {
		return splitWindowProperties;
	}

	public SplitWindowProperties getParentSplitWindowProperties() {
		return parentProperties;
	}

	public void setParentSplitWindowProperties(SplitWindowProperties parentProperties) {
		splitWindowProperties.getMap().replaceSuperMap(this.parentProperties.getMap(), parentProperties.getMap());
		this.parentProperties = parentProperties;
	}

	@Override
	public WindowItem copy() {
		return new SplitWindowItem(this);
	}

	@Override
	public void write(ObjectOutputStream out, WriteContext context, ViewWriter viewWriter) throws IOException {
		out.writeInt(WindowItemDecoder.SPLIT);
		super.write(out, context, viewWriter);
	}

	@Override
	public void writeSettings(ObjectOutputStream out, WriteContext context) throws IOException {
		out.writeBoolean(isHorizontal);
		out.writeFloat(dividerLocation);
		super.writeSettings(out, context);
	}

	@Override
	public void readSettings(ObjectInputStream in, ReadContext context) throws IOException {
		if (context.getVersion() >= 3) {
			isHorizontal = in.readBoolean();
			dividerLocation = in.readFloat();
		}

		super.readSettings(in, context);
	}

	@Override
	protected PropertyMap getPropertyObject() {
		return getSplitWindowProperties().getMap();
	}

	@Override
	public String toString() {
		return "SplitWindow: " + super.toString();
	}

}
