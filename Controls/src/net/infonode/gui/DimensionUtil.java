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


// $Id: DimensionUtil.java,v 1.11 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import net.infonode.util.Direction;

import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.11 $
 */
public class DimensionUtil {
  public static final Dimension ZERO = new Dimension(0, 0);

  public static Dimension min(Dimension dimension1, Dimension dimension2) {
    return new Dimension(Math.min((int) dimension1.getWidth(), (int) dimension2.getWidth()),
                         Math.min((int) dimension1.getHeight(), (int) dimension2.getHeight()));
  }

  private DimensionUtil(){
	  //工具类不提供构造函数
  }
  public static Dimension max(Dimension dimension1, Dimension dimension2) {
    return new Dimension(Math.max((int) dimension1.getWidth(), (int) dimension2.getWidth()),
                         Math.max((int) dimension1.getHeight(), (int) dimension2.getHeight()));
  }

  public static Dimension getInnerDimension(Dimension dimension, Insets insets) {
    return new Dimension((int) (dimension.getWidth() - insets.left - insets.right),
                         (int) (dimension.getHeight() - insets.top - insets.bottom));
  }

  public static Dimension add(Dimension dimension, Insets insets) {
    return new Dimension(dimension.width + insets.left + insets.right, dimension.height + insets.top + insets.bottom);
  }

  public static Dimension add(Dimension dimension1, Dimension dimension2, boolean isHorizontalAdd) {
    return new Dimension(isHorizontalAdd ? (dimension1.width + dimension2.width) : Math.max(dimension1.width, dimension2.width),
                         isHorizontalAdd ? Math.max(dimension1.height, dimension2.height) : dimension1.height + dimension2.height);
  }

  public static Dimension rotate(Direction source, Dimension dimension, Direction destination) {
    if (source.isHorizontal() && destination.isHorizontal())
      return dimension;

    return new Dimension(dimension.height, dimension.width);
  }
}
