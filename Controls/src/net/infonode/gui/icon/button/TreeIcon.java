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

// $Id: TreeIcon.java,v 1.6 2005/12/04 13:46:03 jesper Exp $

package net.infonode.gui.icon.button;

import net.infonode.gui.GraphicsUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jesper Nordenberg
 * @version $Revision: 1.6 $ $Date: 2005/12/04 13:46:03 $
 */
public class TreeIcon implements Icon {
	public static final int PLUS = 1;
	public static final int MINUS = 2;

	private int type;
	private int width;
	private int height;
	private Color color;
	private Color backgroundColor;
	private boolean border = true;

	public TreeIcon(int type, int width, int height, boolean border, Color color, Color backgroundColor) {
		this.type = type;
		this.width = width;
		this.height = height;
		this.border = border;
		this.color = color;
		this.backgroundColor = backgroundColor;
	}

	public TreeIcon(int type, int width, int height) {
		this(type, width, height, true, Color.BLACK, null);
	}

	@Override
	public void paintIcon(Component component, Graphics graphics, int x, int y) {
		if (backgroundColor != null) {
			graphics.setColor(backgroundColor);
			graphics.fillRect(x + 1, y + 1, width - 2, height - 2);
		}

		graphics.setColor(color);

		if (border) {
			graphics.drawRect(x + 1, y + 1, width - 2, height - 2);
		}

		GraphicsUtil.drawOptimizedLine(graphics, x + 3, y + height / 2, x + width - 3, y + height / 2);

		if (type == PLUS)
			GraphicsUtil.drawOptimizedLine(graphics, x + width / 2, y + 3, x + width / 2, y + height - 3);
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}
}
