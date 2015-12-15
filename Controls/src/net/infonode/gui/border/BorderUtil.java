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

// $Id: BorderUtil.java,v 1.6 2005/02/16 11:28:10 jesper Exp $
package net.infonode.gui.border;

import net.infonode.gui.InsetsUtil;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 */
public class BorderUtil {
	private BorderUtil() {
	}

	public static Insets getInsetsOutside(Component c, Border border, Border outside) {
		Insets insets = new Insets(0, 0, 0, 0);
		getInsetsOutside(c, border, outside, insets);
		return insets;
	}

	private static boolean getInsetsOutside(Component component, Border border, Border outside, Insets insets) {
		if (border == null)
			return false;
		else if (border == outside)
			return true;
		else if (border instanceof CompoundBorder) {
			CompoundBorder compoundBorder = (CompoundBorder) border;
			return getInsetsOutside(component, compoundBorder.getOutsideBorder(), outside, insets)
					|| getInsetsOutside(component, compoundBorder.getInsideBorder(), outside, insets);
		} else {
			InsetsUtil.addTo(insets, border.getBorderInsets(component));
			return false;
		}
	}

	public static Border copy(final Border border) {
		return new Border() {
			@Override
			public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
				border.paintBorder(component, graphics, x, y, width, height);
			}

			@Override
			public Insets getBorderInsets(Component component) {
				return border.getBorderInsets(component);
			}

			@Override
			public boolean isBorderOpaque() {
				return border.isBorderOpaque();
			}
		};
	}
}
