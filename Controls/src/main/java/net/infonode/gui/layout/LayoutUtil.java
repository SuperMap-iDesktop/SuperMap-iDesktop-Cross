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


// $Id: LayoutUtil.java,v 1.11 2005/02/16 11:28:12 jesper Exp $
package net.infonode.gui.layout;

import net.infonode.util.Direction;

import java.awt.*;

public class LayoutUtil {
  private LayoutUtil() {
  }

  public static Component[] getVisibleChildren(Container parent) {
    return getVisibleChildren(parent.getComponents());
  }

  public static Component[] getVisibleChildren(Component[] components) {
    int count = 0;

    for (int i = 0; i < components.length; i++)
      if (components[i].isVisible())
        count++;

    Component[] components2 = new Component[count];
    int index = 0;

    for (int i = 0; i < components.length; i++)
      if (components[i].isVisible())
        components2[index++] = components[i];

    return components2;
  }

  public static Rectangle getInteriorArea(Container container) {
    Insets insets = container.getInsets();
    return new Rectangle(insets.left,
                         insets.top,
                         container.getWidth() - insets.left - insets.right,
                         container.getHeight() - insets.top - insets.bottom);
  }

  public static Dimension getInteriorSize(Container container) {
    Insets insets = container.getInsets();
    return new Dimension(container.getWidth() - insets.left - insets.right,
                         container.getHeight() - insets.top - insets.bottom);
  }

  public static Dimension rotate(Dimension dimension, Direction direction) {
    return rotate(dimension, direction.isHorizontal());
  }

  public static Dimension rotate(Dimension dimension, boolean horizontal) {
    return dimension == null ? null : horizontal ? dimension : new Dimension(dimension.height, dimension.width);
  }

  public static boolean isDescendingFrom(Component component, Component parent) {
    return component == parent || (component != null && isDescendingFrom(component.getParent(), parent));
  }

  public static Dimension getMaxMinimumSize(Component[] components) {
    int maxWidth = 0;
    int maxHeight = 0;

    for (int i = 0; i < components.length; i++) {
      if (components[i] != null) {
        Dimension min = components[i].getMinimumSize();
        int width = min.width;
        int height = min.height;

        if (maxHeight < height)
          maxHeight = height;

        if (maxWidth < width)
          maxWidth = width;
      }
    }

    return new Dimension(maxWidth, maxHeight);
  }

  public static Dimension getMaxPreferredSize(Component[] components) {
    int maxWidth = 0;
    int maxHeight = 0;

    for (int i = 0; i < components.length; i++) {
      if (components[i] != null) {
        Dimension min = components[i].getPreferredSize();
        int width = min.width;
        int height = min.height;

        if (maxHeight < height)
          maxHeight = height;

        if (maxWidth < width)
          maxWidth = width;
      }
    }

    return new Dimension(maxWidth, maxHeight);
  }

  public static Dimension getMinMaximumSize(Component[] components) {
    int minWidth = Integer.MAX_VALUE;
    int minHeight = Integer.MAX_VALUE;

    for (int i = 0; i < components.length; i++) {
      if (components[i] != null) {
        Dimension minDimension = components[i].getMaximumSize();
        int width = minDimension.width;
        int height = minDimension.height;

        if (minWidth > width)
          minWidth = width;

        if (minHeight > height)
          minHeight = height;
      }
    }

    return new Dimension(minWidth, minHeight);
  }

  public static Insets rotate(Direction direction, Insets insets) {
    return direction == Direction.RIGHT ? insets :
           direction == Direction.DOWN ? new Insets(insets.right, insets.top, insets.left, insets.bottom) :
           direction == Direction.LEFT ? new Insets(insets.bottom, insets.right, insets.top, insets.left) :
           new Insets(insets.left, insets.bottom, insets.right, insets.top);
  }

  public static Insets unrotate(Direction direction, Insets insets) {
    return direction == Direction.RIGHT ? insets :
           direction == Direction.DOWN ? new Insets(insets.left, insets.bottom, insets.right, insets.top) :
           direction == Direction.LEFT ? new Insets(insets.bottom, insets.right, insets.top, insets.left) :
           new Insets(insets.right, insets.top, insets.left, insets.bottom);
  }

  public static Dimension add(Dimension dimension, Insets insets) {
    return new Dimension(dimension.width + insets.left + insets.right, dimension.height + insets.top + insets.bottom);
  }

  public static Dimension getValidSize(Dimension dimension, Component component) {
    Dimension minSize = component.getMinimumSize();
    Dimension maxSize = component.getMaximumSize();
    return new Dimension(Math.max(minSize.width, Math.min(dimension.width, maxSize.width)),
                         Math.max(minSize.height, Math.min(dimension.height, maxSize.height)));
  }

  public static Component getChildContaining(Component parent, Component component) {
    return component == null ?
           null : component.getParent() == parent ? component : getChildContaining(parent, component.getParent());
  }

  public static String getBorderLayoutOrientation(Direction direction) {
    return direction == Direction.UP ? BorderLayout.NORTH :
           direction == Direction.DOWN ? BorderLayout.SOUTH :
           direction == Direction.LEFT ? BorderLayout.WEST :
           BorderLayout.EAST;
  }

}
