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

// $Id: TabAreaLineBorder.java,v 1.19 2005/12/04 13:46:05 jesper Exp $
package net.infonode.tabbedpanel.border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.colorprovider.*;
import net.infonode.tabbedpanel.*;
import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.Serializable;

/**
 * TabAreaLineBorder draws a 1 pixel wide border on all sides except the side towards the content area of a tabbed panel.
 *
 * @author $Author: jesper $
 * @author $Author: jesper $
 * @version $Revision: 1.19 $
 * @see Tab
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @see TabAreaProperties
 * @see TabAreaComponentsProperties
 * @since ITP 1.1.0
 */
public class TabAreaLineBorder implements Border, Serializable {
	private static final long serialVersionUID = 1;

	private transient ColorProvider color;
	private boolean drawTop;
	private boolean drawLeft;
	private boolean drawRight;
	private boolean flipLeftRight;

	/**
	 * Constructs a TabAreaLineBorder with color based on the look and feel
	 */
	public TabAreaLineBorder() {
		this(null);
	}

	/**
	 * Constructs a TabAreaLineBorder with the give color
	 *
	 * @param color color for the border
	 */
	public TabAreaLineBorder(Color color) {
		this(color, true, true, true, false);
	}

	/**
	 * Constructor.
	 *
	 * @param drawTop draw the top line
	 * @param drawLeft draw the left line
	 * @param drawRight draw the right line
	 * @param flipLeftRight if true the left line is rotated so that it is always to the left or at the top and vice versa for the right line, if false the left
	 *            and right lines are rotated the same way as the other lines
	 */
	public TabAreaLineBorder(boolean drawTop, boolean drawLeft, boolean drawRight, boolean flipLeftRight) {
		this((Color) null, drawTop, drawLeft, drawRight, flipLeftRight);
	}

	/**
	 * Constructor.
	 *
	 * @param color the line color
	 * @param drawTop draw the top line
	 * @param drawLeft draw the left line
	 * @param drawRight draw the right line
	 * @param flipLeftRight if true the left line is rotated so that it is always to the left or at the top and vice versa for the right line, if false the left
	 *            and right lines are rotated the same way as the other lines
	 */
	public TabAreaLineBorder(Color color, boolean drawTop, boolean drawLeft, boolean drawRight, boolean flipLeftRight) {
		this(ColorProviderUtil.getColorProvider(color, new ColorProviderList(UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,
				UIManagerColorProvider.CONTROL_DARK_SHADOW, FixedColorProvider.BLACK)), drawTop, drawLeft, drawRight, flipLeftRight);
	}

	/**
	 * Constructor.
	 *
	 * @param colorProvider the line color provider
	 * @param drawTop draw the top line
	 * @param drawLeft draw the left line
	 * @param drawRight draw the right line
	 * @param flipLeftRight if true the left line is rotated so that it is always to the left or at the top and vice versa for the right line, if false the left
	 *            and right lines are rotated the same way as the other lines
	 */
	public TabAreaLineBorder(ColorProvider colorProvider, boolean drawTop, boolean drawLeft, boolean drawRight, boolean flipLeftRight) {
		this.color = colorProvider;
		this.drawTop = drawTop;
		this.drawLeft = drawLeft;
		this.drawRight = drawRight;
		this.flipLeftRight = flipLeftRight;
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Insets insets = getBorderInsets(c);
		g.setColor(color.getColor(c));

		if (insets.top == 1)
			GraphicsUtil.drawOptimizedLine(g, x, y, x + width - 1, y);

		if (insets.bottom == 1)
			GraphicsUtil.drawOptimizedLine(g, x, y + height - 1, x + width - 1, y + height - 1);

		if (insets.left == 1)
			GraphicsUtil.drawOptimizedLine(g, x, y, x, y + height - 1);

		if (insets.right == 1)
			GraphicsUtil.drawOptimizedLine(g, x + width - 1, y, x + width - 1, y + height - 1);
	}

	private boolean drawTop(Direction orientation) {
		return orientation == Direction.UP ? drawTop : orientation == Direction.LEFT ? (flipLeftRight ? drawLeft : drawRight)
				: orientation == Direction.RIGHT ? drawLeft : false;
	}

	private boolean drawLeft(Direction orientation) {
		return orientation == Direction.UP ? drawLeft : orientation == Direction.LEFT ? drawTop : orientation == Direction.DOWN ? (flipLeftRight ? drawLeft
				: drawRight) : false;
	}

	private boolean drawRight(Direction orientation) {
		return orientation == Direction.UP ? drawRight : orientation == Direction.LEFT ? false : orientation == Direction.DOWN ? (flipLeftRight ? drawRight
				: drawLeft) : drawTop;
	}

	private boolean drawBottom(Direction orientation) {
		return orientation == Direction.UP ? false : orientation == Direction.LEFT ? (flipLeftRight ? drawRight : drawLeft)
				: orientation == Direction.RIGHT ? drawRight : drawTop;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		if (c instanceof JComponent && ((JComponent) c).getComponentCount() == 0)
			return new Insets(0, 0, 0, 0);

		Direction orientation = getTabAreaDirection(c);

		return orientation != null ? new Insets(drawTop(orientation) ? 1 : 0, drawLeft(orientation) ? 1 : 0, drawBottom(orientation) ? 1 : 0,
				drawRight(orientation) ? 1 : 0) : new Insets(0, 0, 0, 0);
	}

	private static Direction getTabAreaDirection(Component c) {
		TabbedPanel tp = TabbedUtils.getParentTabbedPanel(c);
		return tp != null ? tp.getProperties().getTabAreaOrientation() : null;

	}

}
