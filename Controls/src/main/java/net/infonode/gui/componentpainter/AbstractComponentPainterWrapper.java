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


// $Id: AbstractComponentPainterWrapper.java,v 1.4 2005/02/16 11:28:11 jesper Exp $
package net.infonode.gui.componentpainter;

import net.infonode.util.Direction;

import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.4 $
 */
abstract public class AbstractComponentPainterWrapper extends AbstractComponentPainter {
  private static final long serialVersionUID = 1;

  private ComponentPainter painter;

  protected AbstractComponentPainterWrapper(ComponentPainter painter) {
    this.painter = painter;
  }

  public ComponentPainter getPainter() {
    return painter;
  }

  public Color getColor(Component component) {
    return painter.getColor(component);
  }

  public boolean isOpaque(Component component) {
    return painter.isOpaque(component);
  }

  public void paint(Component component,
                    Graphics graphics,
                    int x,
                    int y,
                    int width,
                    int height,
                    Direction direction,
                    boolean horizontalFlip,
                    boolean verticalFlip) {
    painter.paint(component, graphics, x, y, width, height, direction, horizontalFlip, verticalFlip);
  }
}
