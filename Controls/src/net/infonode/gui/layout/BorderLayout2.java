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

// $Id: BorderLayout2.java,v 1.8 2005/02/16 11:28:12 jesper Exp $
package net.infonode.gui.layout;

import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.8 $
 */
public class BorderLayout2 implements LayoutManager2 {
	private Component[][] components;

	public BorderLayout2() {
		components = new Component[3][];

		for (int i = 0; i < components.length; i++) {
			components[i] = new Component[3];
		}
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// do nothing
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints instanceof Point) {
			Point point = (Point) constraints;
			components[point.x][point.y] = comp;
		} else
			throw new RuntimeException("BorderLayout2 constraint must be a Point!");
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return target.getAlignmentX();
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return target.getAlignmentY();
	}

	@Override
	public void invalidateLayout(Container target) {
		// do nothing
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		int width = 0;
		int height = 0;

		for (int i = 0; i < 3; i++) {
			width += getMaximumWidth(i);
			height += getMaximumHeight(i);
		}

		return new Dimension(width, height);
	}

	private int getPreferredHeight(int row) {
		int maxHeight = 0;

		for (int i = 0; i < 3; i++) {
			Component component = components[i][row];

			if (component != null && component.isVisible()) {
				int height = component.getPreferredSize().height;

				if (height > maxHeight)
					maxHeight = height;
			}
		}

		return maxHeight;
	}

	private int getPreferredWidth(int column) {
		int maxWidth = 0;

		for (int i = 0; i < 3; i++) {
			Component component = components[column][i];
			if (component != null && component.isVisible()) {
				int width = component.getPreferredSize().width;

				if (width > maxWidth)
					maxWidth = width;
			}
		}

		return maxWidth;
	}

	private int getMinimumHeight(int row) {
		int maxHeight = 0;

		for (int i = 0; i < 3; i++) {
			Component component = components[i][row];

			if (component != null && component.isVisible()) {
				int height = component.getMinimumSize().height;

				if (height > maxHeight)
					maxHeight = height;
			}
		}

		return maxHeight;
	}

	private int getMinimumWidth(int column) {
		int maxWidth = 0;

		for (int i = 0; i < 3; i++) {
			Component component = components[column][i];

			if (component != null && component.isVisible()) {
				int width = component.getMinimumSize().width;

				if (width > maxWidth)
					maxWidth = width;
			}
		}

		return maxWidth;
	}

	private int getMaximumHeight(int row) {
		int minHeight = Integer.MAX_VALUE;

		for (int i = 0; i < 3; i++) {
			Component component = components[i][row];

			if (component != null && component.isVisible()) {
				int height = component.getMaximumSize().height;

				if (height < minHeight)
					minHeight = height;
			}
		}

		return minHeight;
	}

	private int getMaximumWidth(int column) {
		int minWidth = 0;

		for (int i = 0; i < 3; i++) {
			Component component = components[column][i];

			if (component != null && component.isVisible()) {
				int width = component.getMaximumSize().width;

				if (width < minWidth)
					minWidth = width;
			}
		}

		return minWidth;
	}

	private static void setBounds(Component component, Rectangle bounds) {
		int width = Math.min(component.getMaximumSize().width, bounds.width);
		int height = Math.min(component.getMaximumSize().height, bounds.height);
		Rectangle rectangle = new Rectangle(bounds.x + (int) (component.getAlignmentX() * (bounds.width - width)), bounds.y
				+ (int) (component.getAlignmentY() * (bounds.height - height)), width, height);
		component.setBounds(rectangle);
	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		Dimension innerSize = LayoutUtil.getInteriorSize(parent);
		int[] widths = { getPreferredWidth(0), getPreferredWidth(2) };
		int[] heights = { getPreferredHeight(0), getPreferredHeight(2) };

		int y = insets.top;

		for (int row = 0; row < 3; row++) {
			int height = row == 1 ? innerSize.height - heights[0] - heights[1] : heights[row / 2];
			int x = insets.left;

			for (int col = 0; col < 3; col++) {
				int width = col == 1 ? innerSize.width - widths[0] - widths[1] : widths[col / 2];
				Component component = components[col][row];

				if (component != null && component.isVisible()) {
					setBounds(component, new Rectangle(x, y, width, height));
				}

				x += width;
			}

			y += height;
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		int width = 0;
		int height = 0;

		for (int i = 0; i < 3; i++) {
			width += getMinimumWidth(i);
			height += getMinimumHeight(i);
		}

		return new Dimension(width, height);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int width = 0;
		int height = 0;

		for (int i = 0; i < 3; i++) {
			width += getPreferredWidth(i);
			height += getPreferredHeight(i);
		}

		return new Dimension(width, height);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		for (int col = 0; col < 3; col++) {
			for (int row = 0; row < 3; row++) {
				if (components[col][row] == comp) {
					components[col][row] = null;
					return;
				}
			}
		}
	}
}
