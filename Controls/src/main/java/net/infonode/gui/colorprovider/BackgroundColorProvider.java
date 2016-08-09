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


// $Id: BackgroundColorProvider.java,v 1.10 2009/02/05 15:57:56 jesper Exp $
package net.infonode.gui.colorprovider;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectStreamException;

/**
 * Returns the background color of a component.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.10 $
 */
public class BackgroundColorProvider extends AbstractColorProvider {
  private static final long serialVersionUID = 1;

  /**
   * The only instance of this class.
   */
  public static final BackgroundColorProvider INSTANCE = new BackgroundColorProvider();

  private BackgroundColorProvider() {
  }

  public Color getColor(Component component) {
    Color color = component.getBackground();
    return color == null ? UIManager.getColor("control") : color;
  }

  protected Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
