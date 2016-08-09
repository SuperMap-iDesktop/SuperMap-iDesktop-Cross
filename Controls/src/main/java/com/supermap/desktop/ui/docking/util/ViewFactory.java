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


// $Id: ViewFactory.java,v 1.5 2005/02/16 11:28:14 jesper Exp $
package com.supermap.desktop.ui.docking.util;

import com.supermap.desktop.ui.docking.View;

import javax.swing.*;

/**
 * A factory that creates a view.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public interface ViewFactory {
  /**
   * Returns the icon for this factory.
   *
   * @return the icon for this factory
   */
  Icon getIcon();

  /**
   * Returns the title of this factory.
   *
   * @return the title of this factory
   */
  String getTitle();

  /**
   * Creates a view.
   *
   * @return the view
   */
  View createView();
}
