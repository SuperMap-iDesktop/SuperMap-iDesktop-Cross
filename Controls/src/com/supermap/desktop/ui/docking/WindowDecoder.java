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

// $Id: WindowDecoder.java,v 1.19 2007/01/28 21:25:10 jesper Exp $
package com.supermap.desktop.ui.docking;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.supermap.desktop.ui.docking.internal.ReadContext;
import com.supermap.desktop.ui.docking.model.SplitWindowItem;
import com.supermap.desktop.ui.docking.model.TabWindowItem;
import com.supermap.desktop.ui.docking.model.ViewReader;
import com.supermap.desktop.ui.docking.model.WindowItem;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.19 $
 */
class WindowDecoder {
	private WindowDecoder() {
	}

	static DockingWindow decodeWindow(ObjectInputStream in, ReadContext context) throws IOException {
		int id = in.readInt();

		switch (id) {
		case WindowIds.VIEW:
			return View.read(in, context);

		case WindowIds.SPLIT:
			SplitWindow w = new SplitWindow(true);
			return w.oldRead(in, context);

		case WindowIds.TAB:
			TabWindow tabWindow = new TabWindow();
			return tabWindow.oldRead(in, context);

		default:
			throw new IOException("Invalid window ID: " + id + '!');
		}
	}

	static DockingWindow decodeWindow(ObjectInputStream in, ReadContext context, ViewReader viewReader) throws IOException {
		int id = in.readInt();

		if (id == WindowIds.VIEW) {
			return viewReader.readView(in, context);
		} else {
			WindowItem windowItem = viewReader.readWindowItem(in, context);

			switch (id) {
			case WindowIds.SPLIT:
				return setCaseSplit(in, context, viewReader, windowItem);

			case WindowIds.TAB:
				return setCaseTab(in, context, viewReader, windowItem);

			default:
				throw new IOException("Invalid window ID: " + id + '!');
			}
		}
	}

	private static DockingWindow setCaseTab(ObjectInputStream in, ReadContext context, ViewReader viewReader, WindowItem windowItem) throws IOException {
		TabWindowItem tabWindowItem = (TabWindowItem) windowItem;

		if (tabWindowItem == null) {
			tabWindowItem = new TabWindowItem();
			tabWindowItem.readSettings(in, context);
		}

		TabWindow tabWindow = new TabWindow(null, tabWindowItem);
		return tabWindow.newRead(in, context, viewReader);
	}

	private static DockingWindow setCaseSplit(ObjectInputStream in, ReadContext context, ViewReader viewReader, WindowItem windowItem) throws IOException {
		SplitWindowItem item = (SplitWindowItem) windowItem;

		if (item == null) {
			item = new SplitWindowItem();
			item.readSettings(in, context);
		}

		SplitWindow w = new SplitWindow(item.isHorizontal(), item.getDividerLocation(), null, null, item);
		return w.newRead(in, context, viewReader);
	}
}
