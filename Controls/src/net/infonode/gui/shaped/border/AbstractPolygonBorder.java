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

// $Id: AbstractPolygonBorder.java,v 1.18 2005/12/04 13:46:04 jesper Exp $

package net.infonode.gui.shaped.border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.HighlightPainter;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.colorprovider.BackgroundPainterColorProvider;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.shaped.ShapedUtil;
import net.infonode.gui.shaped.panel.ShapedPanel;

import java.awt.*;

/**
 * @author johan
 */
abstract public class AbstractPolygonBorder extends AbstractShapedBorder {
	private static final long serialVersionUID = 1;

	private static final Insets HIGHLIGHT_INSETS = new Insets(1, 1, 0, 0);
	private ColorProvider lineColor;
	private ColorProvider highlightColor = new FixedColorProvider(new Color(255, 255, 255));
	private ColorProvider middleColor;
	private ColorProvider shadowColor;

	protected AbstractPolygonBorder(ColorProvider lineColor) {
		this(lineColor, FixedColorProvider.WHITE);
	}

	protected AbstractPolygonBorder(ColorProvider lineColor, ColorProvider highlightColor) {
		this(lineColor, highlightColor, BackgroundPainterColorProvider.INSTANCE, null);
	}

	protected AbstractPolygonBorder(ColorProvider lineColor, ColorProvider highlightColor, ColorProvider middleColor, ColorProvider shadowColor) {
		this.lineColor = lineColor;
		this.highlightColor = highlightColor;
		this.middleColor = middleColor;
		this.shadowColor = shadowColor;
	}

	@Override
	public Shape getShape(Component component, int x, int y, int width, int height) {
		int widthTemp = ShapedUtil.getWidth(component, width, height);
		int heightTemp = ShapedUtil.getHeight(component, width, height);
		Polygon polygon = getPolygon(component, x, y, widthTemp, heightTemp);

		return polygon;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
		Shape clip = graphics.getClip();
		graphics.clipRect(x, y, width, height);

		try {
			int widthTemp = ShapedUtil.getWidth(component, width, height);
			height = ShapedUtil.getHeight(component, width, height);
			width = widthTemp;

			Polygon polygon = getPolygon(component, x, y, width, height);
			Graphics2D graphics2D = (Graphics2D) graphics;

			if (highlightColor != null) {
				paintHighlight(component, graphics2D, polygon, width, height);
			}

			if (lineColor != null) {
				graphics.setColor(lineColor.getColor());
				paintPolygon(component, graphics2D, polygon, width, height);
			}
		} finally {
			graphics.setClip(clip);
		}
	}

	@Override
	public Insets getBorderInsets(Component component) {
		Insets insets = getShapedBorderInsets(component);
		insets = ShapedUtil.transformInsets(component, insets);
		return highlightColor != null ? InsetsUtil.add(getShapedBorderHighlightInsets(component), insets) : insets;
	}

	protected Insets getShapedBorderInsets(Component component) {
		return new Insets(0, 0, 0, 0);
	}

	protected Insets getShapedBorderHighlightInsets(Component component) {
		return HIGHLIGHT_INSETS;
	}

	protected Polygon createPolygon(Component component, int width, int height) {
		return new Polygon();
	}

	protected void paintPolygon(Component component, Graphics2D graphics2D, Polygon polygon, int width, int height) {
		int i = 0;

		while (i < polygon.npoints) {
			if (lineIsDrawn(i, polygon)) {
				int ni = (i + 1) % polygon.npoints;
				GraphicsUtil.drawOptimizedLine(graphics2D, polygon.xpoints[i], polygon.ypoints[i], polygon.xpoints[ni], polygon.ypoints[ni]);
			}

			i++;
		}
	}

	protected void paintHighlight(Component component, Graphics2D graphics2D, Polygon polygon, int width, int height) {
		Color c1 = highlightColor == null ? null : highlightColor.getColor(component);
		Color c2 = middleColor.getColor(component);
		Color c3 = shadowColor == null ? null : shadowColor.getColor(component);

		boolean clockWise = isPointsClockwise(component);

		for (int i = 0; i < polygon.npoints; i++) {
			int ni = (i + 1) % polygon.npoints;

			if (lineIsDrawn(i, polygon)) {
				HighlightPainter.drawLine(graphics2D, polygon.xpoints[i], polygon.ypoints[i], polygon.xpoints[ni], polygon.ypoints[ni], clockWise, true, c1,
						c2, c3);
			}
		}
	}

	protected boolean lineIsDrawn(int index, Polygon polygon) {
		return true;
	}

	protected boolean isHighlightable(int deltaX, int deltaY) {
		return deltaX > deltaY;
	}

	protected boolean isPointsClockwise(Component component) {
		if (component instanceof ShapedPanel)
			return !(((ShapedPanel) component).isHorizontalFlip() ^ ((ShapedPanel) component).isVerticalFlip());

		return true;
	}

	protected int getHighlightOffsetX(int deltaX, int deltaY) {
		return deltaY - deltaX > 0 ? (deltaX + deltaY > 0 ? -1 : 0) : (deltaX + deltaY > 0 ? 0 : 1);
	}

	protected int getHighlightOffsetY(int deltaX, int deltaY) {
		return deltaY - deltaX > 0 ? (deltaX + deltaY > 0 ? 0 : -1) : (deltaX + deltaY > 0 ? 1 : 0);
	}

	protected void setPoint(Polygon polygon, int x, int y) {
		polygon.xpoints[polygon.npoints] = x;
		polygon.ypoints[polygon.npoints] = y;
		polygon.npoints++;
	}

	private Polygon getPolygon(Component component, int x, int y, int width, int height) {
		Polygon polygon = createPolygon(component, width, height);
		flipPolygon(component, polygon, width, height);
		rotatePolygon(component, polygon, width, height);
		fixGraphicsOffset(component, polygon, x, y);
		return polygon;
	}

	private void flipPolygon(Component component, Polygon polygon, int width, int height) {
		if (component instanceof ShapedPanel) {
			if (((ShapedPanel) component).isHorizontalFlip()) {
				for (int i = 0; i < polygon.npoints; i++)
					polygon.xpoints[i] = Math.abs(width - polygon.xpoints[i]) - 1;
			}

			if (((ShapedPanel) component).isVerticalFlip()) {
				for (int i = 0; i < polygon.npoints; i++)
					polygon.ypoints[i] = Math.abs(height - polygon.ypoints[i]) - 1;
			}
		}
	}

	private void rotatePolygon(Component component, Polygon polygon, int width, int height) {
		ShapedUtil.rotate(polygon, ShapedUtil.getDirection(component), width, height);
	}

	private void fixGraphicsOffset(Component component, Polygon polygon, int x, int y) {
		for (int i = 0; i < polygon.npoints; i++) {
			polygon.xpoints[i] += x;
			polygon.ypoints[i] += y;
		}
	}

}