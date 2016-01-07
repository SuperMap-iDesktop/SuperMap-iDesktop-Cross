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


// $Id: SolidColorComponentPainter.java,v 1.9 2005/02/16 11:28:11 jesper Exp $
package net.infonode.gui.componentpainter;

import net.infonode.gui.colorprovider.BackgroundColorProvider;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.util.Direction;

import java.awt.*;

/**
 * Paints an area with a solid color.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.9 $
 */
public class SolidColorComponentPainter extends AbstractComponentPainter {
  private static final long serialVersionUID = 1;

  /**
   * Paints a component using the background color set in the component.
   */
  public static final SolidColorComponentPainter BACKGROUND_COLOR_PAINTER =
      new SolidColorComponentPainter(BackgroundColorProvider.INSTANCE);

  private ColorProvider colorProvider;

  /**
   * Constructor.
   *
   * @param colorProvider the color provider
   */
  public SolidColorComponentPainter(ColorProvider colorProvider) {
    this.colorProvider = colorProvider;
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
    graphics.setColor(colorProvider.getColor(component));
    graphics.fillRect(x, y, width, height);
  }

  public boolean isOpaque(Component component) {
    return colorProvider.getColor(component).getAlpha() == 255;
  }

  public Color getColor(Component component) {
    return colorProvider.getColor(component);
  }
}
