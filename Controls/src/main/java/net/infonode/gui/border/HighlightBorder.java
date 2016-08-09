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

// $Id: HighlightBorder.java,v 1.17 2005/12/04 13:46:03 jesper Exp $
package net.infonode.gui.border;

import net.infonode.gui.GraphicsUtil;
import net.infonode.gui.InsetsUtil;
import net.infonode.gui.colorprovider.BackgroundPainterColorProvider;
import net.infonode.gui.colorprovider.ColorMultiplier;
import net.infonode.gui.colorprovider.ColorProvider;
import net.infonode.gui.colorprovider.ColorProviderUtil;
import net.infonode.util.Direction;

import javax.swing.border.Border;
import java.awt.*;
import java.io.Serializable;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.17 $
 */
public class HighlightBorder implements Border, Serializable {
	private static final long serialVersionUID = 1;

	private static final Insets INSETS = new Insets(1, 1, 0, 0);
	private boolean lowered;
	private boolean pressed;
	private transient ColorProvider colorProvider;

	public HighlightBorder() {
		this(false);
	}

	public HighlightBorder(boolean lowered) {
		this(lowered, null);
	}

	public HighlightBorder(boolean lowered, Color color) {
		this(lowered, false, color);
	}

	public HighlightBorder(boolean lowered, boolean pressed, Color color) {
		this(lowered, pressed, ColorProviderUtil.getColorProvider(color, new ColorMultiplier(BackgroundPainterColorProvider.INSTANCE, lowered ? 0.7 : 1.70)));
	}

	public HighlightBorder(boolean lowered, boolean pressed, ColorProvider colorProvider) {
		this.lowered = lowered;
		this.pressed = pressed;
		this.colorProvider = colorProvider;
	}

	@Override
	public Insets getBorderInsets(Component component) {
		return pressed ? InsetsUtil.rotate(Direction.LEFT, INSETS) : INSETS;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
		graphics.setColor(colorProvider.getColor(component));

		if (pressed) {
			GraphicsUtil.drawOptimizedLine(graphics, x + (lowered ? 0 : 1), y + height - 1, x + width - 1, y + height - 1);
			GraphicsUtil.drawOptimizedLine(graphics, x + width - 1, y + (lowered ? 0 : 1), x + width - 1, y + height - 2);
		} else {
			GraphicsUtil.drawOptimizedLine(graphics, x, y, x + width - (lowered ? 1 : 2), y);
			GraphicsUtil.drawOptimizedLine(graphics, x, y, x, y + height - (lowered ? 1 : 2));
		}
	}
}
