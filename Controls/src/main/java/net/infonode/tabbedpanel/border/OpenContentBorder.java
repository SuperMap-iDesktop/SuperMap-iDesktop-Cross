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

// $Id: OpenContentBorder.java,v 1.31 2009/02/24 13:49:23 jesper Exp $
package net.infonode.tabbedpanel.border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.gui.colorprovider.FixedColorProvider;
import net.infonode.gui.colorprovider.UIManagerColorProvider;
import net.infonode.tabbedpanel.Tab;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedUtils;
import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.PathIterator;
import java.io.Serializable;

/**
 * <p>
 * OpenContentBorder is a border that draws a 1 pixel wide line border around a component that is used as content area component in a tabbed panel. The border
 * also optionally draws a highlight inside the line on the top and left sides of the component. It is open, i.e. no content border will be drawn where the
 * highlighted tab in the tabbed panel is located.
 * </p>
 *
 * <p>
 * If the highlighted tab has a {@link net.infonode.gui.shaped.border.ShapedBorder} its shape will be used to calculate where the OpenContentBorder should be
 * open.
 * </p>
 *
 * @author $Author: jesper $
 * @version $Revision: 1.31 $
 * @see TabbedPanel
 */
public class OpenContentBorder implements Border, Serializable {
	private static final long serialVersionUID = 1;

	private transient final ColorProvider topLeftLineColor;
	private transient final ColorProvider bottomRightLineColor;
	private transient final ColorProvider highlightColorProvider;
	private int tabLeftInset = 1;

	/**
	 * Constructor.
	 *
	 * @param color the line color
	 * @param tabLeftInset the left border inset of the tab
	 */
	public OpenContentBorder(Color color, int tabLeftInset) {
		this(color);
		this.tabLeftInset = tabLeftInset;
	}

	/**
	 * Constructor. Uses the TabbedPane.darkShadow color from the UIManager as line color.
	 */
	public OpenContentBorder() {
		this(null);
	}

	/**
	 * Constructs a OpenContentBorder without highlight and with the given color as line color.
	 *
	 * @param color the line color
	 */
	public OpenContentBorder(Color color) {
		this(color, null);
	}

	/**
	 * Constructs a OpenContentBorder with highlight and with the given colors as line color and highlight color.
	 *
	 * @param color the line color
	 * @param highlightColor the highlight color
	 */
	public OpenContentBorder(Color color, Color highlightColor) {
		this(ColorProviderUtil.getColorProvider(color, UIManagerColorProvider.TABBED_PANE_DARK_SHADOW), highlightColor == null ? null : new FixedColorProvider(
				highlightColor), 1);
	}

	/**
	 * Constructs a OpenContentBorder with highlight and with the given colors as line color and highlight color.
	 *
	 * @param lineColor the line color provider
	 * @param highlightColorProvider the highlight color provider
	 * @param tabLeftInset the left border inset of the tab
	 */
	public OpenContentBorder(ColorProvider lineColor, ColorProvider highlightColorProvider, int tabLeftInset) {
		this(lineColor, lineColor, highlightColorProvider, tabLeftInset);
	}

	/**
	 * Constructs a OpenContentBorder with highlight and with the given colors as line color and highlight color.
	 *
	 * @param topLeftLineColor the line color provider for the top and left lines
	 * @param bottomRightLineColor the line color provider for the bottom and right lines
	 * @param highlightColorProvider the highlight color provider
	 * @param tabLeftInset the left border inset of the tab
	 */
	public OpenContentBorder(ColorProvider topLeftLineColor, ColorProvider bottomRightLineColor, ColorProvider highlightColorProvider, int tabLeftInset) {
		this.topLeftLineColor = topLeftLineColor;
		this.bottomRightLineColor = bottomRightLineColor;
		this.highlightColorProvider = highlightColorProvider;
		this.tabLeftInset = tabLeftInset;
	}

	private static int getLineIntersection(int edge, float x1, float y1, float x2, float y2, Direction orientation) {
		int floatX1 = Float.floatToIntBits(x1);
		int floatY1 = Float.floatToRawIntBits(y1);
		int floatX2 = Float.floatToRawIntBits(x2);
		int floatY2 = Float.floatToRawIntBits(y2);
		return orientation.isHorizontal() ?

		((floatX1 <= edge && floatX2 >= edge) || (floatX1 >= edge && floatX2 <= edge) ? Math.round(floatX2 == floatX1 ? floatY2 : floatY1 + (edge - floatX1)
				* (floatY2 - floatY1) / (floatX2 - floatX1)) : Integer.MAX_VALUE) :

		((floatY1 <= edge && floatY2 >= edge) || (floatY1 >= edge && floatY2 <= edge) ? Math.round(floatY2 == floatY1 ? floatX2 : floatX1 + (edge - floatY1)
				* (floatX2 - floatX1) / (floatY2 - floatY1)) : Integer.MAX_VALUE);
	}

	private static Point getTabBounds(Component c, Tab tab, Direction orientation, int x, int y) {
		Rectangle r = tab.getVisibleRect();
		r = SwingUtilities.convertRectangle(tab, r, c);
		int start = orientation.isHorizontal() ? Math.max(y, r.y) : Math.max(x, r.x);
		int end = start + (orientation.isHorizontal() ? r.height : r.width) - 1;
		Shape shape = tab.getShape();

		if (shape != null) {
			int edge = orientation == Direction.UP ? tab.getHeight() : orientation == Direction.RIGHT ? -1 : orientation == Direction.DOWN ? -1 : tab
					.getWidth();

			float[] coords = new float[6];
			PathIterator it = shape.getPathIterator(null);
			it.currentSegment(coords);
			it.next();
			float x1 = coords[0];
			float y1 = coords[1];
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;

			for (; !it.isDone(); it.next()) {
				float lastX = coords[0];
				float lastY = coords[1];
				it.currentSegment(coords);
				int li = getLineIntersection(edge, lastX, lastY, coords[0], coords[1], orientation);

				if (li != Integer.MAX_VALUE) {
					if (li < min)
						min = li;

					if (li > max)
						max = li;
				}
			}

			int li = getLineIntersection(edge, coords[0], coords[1], x1, y1, orientation);

			if (li != Integer.MAX_VALUE) {
				if (li < min)
					min = li;

				if (li > max)
					max = li;
			}

			Point p0 = SwingUtilities.convertPoint(tab, 0, 0, c);

			if (orientation.isHorizontal()) {
				min += p0.y;
				max += p0.y;
			} else {
				min += p0.x;
				max += p0.x;
			}

			start = Math.max(start, min);
			end = Math.min(end, max);
		}

		return new Point(start, end);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		TabbedPanel tabbedPanel = TabbedUtils.getParentTabbedPanelContentPanel(c).getTabbedPanel();

		if (c != null && tabbedPanel != null) {
			Tab tab = tabbedPanel.getHighlightedTab();
			int tabStart = -1;
			int tabEnd = -1;
			int clipOffset = 0;
			Direction orientation = tabbedPanel.getProperties().getTabAreaOrientation();

			if (tab != null) {
				Point p = getTabBounds(c, tab, orientation, x, y);
				tabStart = p.x;
				tabEnd = p.y;

				Rectangle visible = tab.getVisibleRect();
				int tabWidth = (int) visible.getWidth();
				int tabHeight = (int) visible.getHeight();
				clipOffset = (orientation.isHorizontal() ? tab.getHeight() > tabHeight : tab.getWidth() > tabWidth) ? -1 : 0;
			}

			Color topLeftColor = topLeftLineColor != null ? topLeftLineColor.getColor(c) : null;
			Color bottomRightColor = bottomRightLineColor != null ? bottomRightLineColor.getColor(c) : null;
			Color hc1 = highlightColorProvider == null ? null : highlightColorProvider.getColor(c);

			if (orientation == Direction.UP && tab != null) {
				if (topLeftColor != null) {
					g.setColor(topLeftColor);
					drawLine(g, x, y, tabStart - 1 + tabLeftInset, y);
					drawLine(g, tabEnd - clipOffset, y, x + width - 1, y);
				}

				if (highlightColorProvider != null) {
					g.setColor(hc1);
					drawLine(g, x + 1, y + 1, tabStart + tabLeftInset - 1, y + 1);

					if (tabEnd > tabStart)
						drawLine(g, tabStart + tabLeftInset, y, tabStart + tabLeftInset, y + 1);

					drawLine(g, tabEnd, y + 1, x + width - 3, y + 1);
				}
			} else {
				if (topLeftColor != null) {
					g.setColor(topLeftColor);
					drawLine(g, x, y, x + width - 1, y);
				}

				if (highlightColorProvider != null) {
					g.setColor(hc1);
					drawLine(g, x + 1, y + 1, x + width - (orientation == Direction.RIGHT && tabStart == 0 ? 1 : 3), y + 1);
				}
			}

			if (orientation == Direction.LEFT && tab != null) {
				if (topLeftColor != null) {
					g.setColor(topLeftColor);
					drawLine(g, x, y + 1, x, tabStart - 1 + tabLeftInset);
					drawLine(g, x, tabEnd - clipOffset, x, y + height - 1);
				}

				if (highlightColorProvider != null) {
					g.setColor(hc1);
					drawLine(g, x + 1, y + 2, x + 1, tabStart + tabLeftInset - 1);

					if (tabEnd > tabStart)
						drawLine(g, x, tabStart + tabLeftInset, x + 1, tabStart + tabLeftInset);

					drawLine(g, x + 1, tabEnd, x + 1, y + height - 3);
				}
			} else {
				if (topLeftColor != null) {
					g.setColor(topLeftColor);
					drawLine(g, x, y + 1, x, y + height - 1);
				}

				if (highlightColorProvider != null) {
					g.setColor(hc1);
					drawLine(g, x + 1, y + 2, x + 1, y + height - (orientation == Direction.DOWN && tabStart == 0 ? 1 : 3));
				}
			}

			if (bottomRightColor != null) {
				g.setColor(bottomRightColor);

				if (orientation == Direction.RIGHT && tab != null) {
					drawLine(g, x + width - 1, y + 1, x + width - 1, tabStart - 1 + tabLeftInset);
					drawLine(g, x + width - 1, tabEnd - clipOffset, x + width - 1, y + height - 1);
				} else {
					drawLine(g, x + width - 1, y + 1, x + width - 1, y + height - 1);
				}

				if (orientation == Direction.DOWN && tab != null) {
					g.setColor(bottomRightColor);
					drawLine(g, x + 1, y + height - 1, tabStart - 1 + tabLeftInset, y + height - 1);
					drawLine(g, tabEnd - clipOffset, y + height - 1, x + width - 2, y + height - 1);
				} else {
					drawLine(g, x + 1, y + height - 1, x + width - 2, y + height - 1);
				}
			}
		}
	}

	private static void drawLine(Graphics graphics, int x1, int y1, int x2, int y2) {
		if (x2 < x1 || y2 < y1)
			return;

		GraphicsUtil.drawOptimizedLine(graphics, x1, y1, x2, y2);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		int hInset = highlightColorProvider != null ? 1 : 0;
		int tlInset = (topLeftLineColor != null ? 1 : 0) + hInset;
		int brInset = bottomRightLineColor != null ? 1 : 0;
		return new Insets(tlInset, tlInset, brInset, brInset);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

}
