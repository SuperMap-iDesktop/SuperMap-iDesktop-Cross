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

// $Id: ShapedUtil.java,v 1.5 2005/02/16 11:28:13 jesper Exp $
package net.infonode.gui.shaped;

import net.infonode.gui.InsetsUtil;
import net.infonode.gui.RectangleUtil;
import net.infonode.gui.shaped.panel.ShapedPanel;
import net.infonode.util.Direction;

import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public class ShapedUtil {
	private ShapedUtil() {
	}

	public static Direction getDirection(Component component) {
		return component instanceof ShapedPanel ? ((ShapedPanel) component).getDirection() : Direction.RIGHT;
	}

	public static Insets transformInsets(Component component, Insets insets) {
		return InsetsUtil.rotate(getDirection(component), flipInsets(component, insets));
	}

	public static Insets flipInsets(Component component, Insets insets) {
		Insets insetsTemp = insets;
		if (component instanceof ShapedPanel) {
			if (((ShapedPanel) component).isHorizontalFlip())
				insetsTemp = InsetsUtil.flipHorizontal(insetsTemp);
			if (((ShapedPanel) component).isVerticalFlip())
				insetsTemp = InsetsUtil.flipVertical(insetsTemp);
		}

		return insets;
	}

	public static void rotateCW(Polygon polygon, int height) {
		for (int i = 0; i < polygon.npoints; i++) {
			int tmp = polygon.ypoints[i];
			polygon.ypoints[i] = polygon.xpoints[i];
			polygon.xpoints[i] = height - 1 - tmp;
		}
	}

	public static void rotate(Polygon polygon, Direction direction, int width, int height) {
		if (direction == Direction.UP) {
			rotateCW(polygon, height);
			rotateCW(polygon, width);
			rotateCW(polygon, height);
		} else if (direction == Direction.LEFT) {
			rotateCW(polygon, height);
			rotateCW(polygon, width);
		} else if (direction == Direction.DOWN) {
			rotateCW(polygon, height);
		}
	}

	public static Rectangle transform(Component component, Rectangle rectangle) {
		if (component instanceof ShapedPanel) {
			ShapedPanel shapedPanel = (ShapedPanel) component;
			return RectangleUtil.transform(rectangle, shapedPanel.getDirection(), shapedPanel.isHorizontalFlip(), shapedPanel.isVerticalFlip(),
					component.getWidth(), component.getHeight());
		} else
			return rectangle;
	}

	public static Dimension transform(Component component, Dimension dimension) {
		return getDirection(component).isHorizontal() ? dimension : new Dimension(dimension.height, dimension.width);
	}

	public static int getWidth(Component component, int width, int height) {
		return getDirection(component).isHorizontal() ? width : height;
	}

	public static int getHeight(Component component, int width, int height) {
		return getDirection(component).isHorizontal() ? height : width;
	}

}
