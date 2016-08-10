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


// $Id: InsertTabDropInfo.java,v 1.4 2005/12/04 13:46:04 jesper Exp $
package com.supermap.desktop.ui.docking.drop;

import com.supermap.desktop.ui.docking.DockingWindow;

import java.awt.*;

/**
 * <p>
 * Information about an insert tab drop.
 * </p>
 *
 * <p>
 * An insert tab drop is performed when a window is dragged and ropped
 * into the tab area of a {@link com.supermap.desktop.ui.docking.TabWindow} or a
 * {@link com.supermap.desktop.ui.docking.WindowBar}.
 * </p>
 *
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 * @since IDW 1.4.0
 */
public class InsertTabDropInfo extends DropInfo {
  public InsertTabDropInfo(DockingWindow window, DockingWindow dropWindow, Point point) {
    super(window, dropWindow, point);
  }
}
