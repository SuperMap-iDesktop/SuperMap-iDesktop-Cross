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


// $Id: FloatingWindowItem.java,v 1.6 2005/12/04 13:46:04 jesper Exp $
package com.supermap.desktop.ui.docking.model;

import net.infonode.properties.propertymap.PropertyMap;

import java.util.ArrayList;

import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.properties.FloatingWindowProperties;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 */
public class FloatingWindowItem extends WindowItem {
  private FloatingWindowProperties properties = new FloatingWindowProperties();

  protected DockingWindow createWindow(ViewReader viewReader, ArrayList childWindows) {
    return null;
  }

  public WindowItem copy() {
    return null;
  }

  public boolean isRestoreWindow() {
    return false;
  }

  public FloatingWindowProperties getFloatingWindowProperties() {
    return properties;
  }

  protected PropertyMap getPropertyObject() {
    return properties.getMap();
  }

}
