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


// $Id: ColorBlender.java,v 1.6 2009/02/05 15:57:56 jesper Exp $
package net.infonode.gui.colorprovider;

import java.awt.Color;
import java.awt.Component;

import net.infonode.util.ColorUtil;

/**
 * Blends two colors according to the given blend amount.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 */
public class ColorBlender extends AbstractColorProvider {
  private static final long serialVersionUID = 1;

  private final ColorProvider firstColor;
  private final ColorProvider secondColor;
  private final float blendAmount;

  /**
   * Constructor.
   *
   * @param firstColor      provides the first color
   * @param secondColor      provides the second color
   * @param blendAmount the blend amount, range 0 - 1 where 0 means only the first color and 1 means only the second color
   */
  public ColorBlender(ColorProvider firstColor, ColorProvider secondColor, float blendAmount) {
    this.firstColor = firstColor;
    this.secondColor = secondColor;
    this.blendAmount = blendAmount;
  }

  public Color getColor(Component component) {
    return ColorUtil.blend(firstColor.getColor(component), secondColor.getColor(component), blendAmount);
  }

  public Color getColor() {
    return ColorUtil.blend(firstColor.getColor(), secondColor.getColor(), blendAmount);
  }

}
