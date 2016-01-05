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


// $Id: RectangleComponentPainter.java,v 1.8 2009/02/05 15:57:56 jesper Exp $
package net.infonode.gui.componentpainter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import net.infonode.gui.InsetsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.util.Direction;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.8 $
 */
public class RectangleComponentPainter extends AbstractComponentPainter {
  private static final long serialVersionUID = 1;

  private final ColorProvider color;
  private final ColorProvider xorColor;
  private final Insets insets;

  public RectangleComponentPainter(Color color, int lineWidth) {
    this(new FixedColorProvider(color), lineWidth);
  }

  public RectangleComponentPainter(Color color, Color xorColor, int lineWidth) {
    this(new FixedColorProvider(color), new FixedColorProvider(xorColor), lineWidth);
  }

  public RectangleComponentPainter(ColorProvider color, int lineWidth) {
    this(color, null, lineWidth);
  }

  public RectangleComponentPainter(ColorProvider color, ColorProvider xorColor, int lineWidth) {
    this(color, xorColor, new Insets(lineWidth, lineWidth, lineWidth, lineWidth));
  }

  public RectangleComponentPainter(ColorProvider color, ColorProvider xorColor, Insets insets) {
    this.color = color;
    this.xorColor = xorColor;
    this.insets = (Insets) insets.clone();
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
    Color xColor = null;
    graphics.setColor(color.getColor(component));

    if (xorColor != null) {
      xColor = xorColor.getColor(component);

      if (xColor != null)
        graphics.setXORMode(xColor);
    }

    Insets insetsTemp = InsetsUtil.rotate(direction, new Insets(verticalFlip ? insets.bottom : insets.top,
                                                                    horizontalFlip ? insets.right : insets.left,
                                                                                   verticalFlip ? insets.top : insets.bottom,
                                                                                                horizontalFlip ? insets.left : insets.right));

    graphics.fillRect(x + insetsTemp.left, y, width - insetsTemp.left - insetsTemp.right, insetsTemp.top);
    graphics.fillRect(x + insetsTemp.left, y + height - insetsTemp.bottom, width - insetsTemp.left - insetsTemp.right, insetsTemp.bottom);
    graphics.fillRect(x, y, insetsTemp.left, height);
    graphics.fillRect(x + width - insetsTemp.right, y, insetsTemp.right, height);

    if (xColor != null)
      graphics.setPaintMode();
  }

  public boolean isOpaque(Component component) {
    return false;
  }

  public Color getColor(Component component) {
    return color.getColor(component);
  }
}
