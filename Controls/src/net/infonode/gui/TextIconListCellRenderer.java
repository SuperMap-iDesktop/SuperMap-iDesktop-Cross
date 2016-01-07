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

// $Id: TextIconListCellRenderer.java,v 1.11 2006/06/13 20:12:39 johan Exp $
package net.infonode.gui;

import net.infonode.gui.icon.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author johan
 */
public class TextIconListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;
	private transient ListCellRenderer renderer;
	private transient Icon emptyIcon;
	private int widthTemp;
	private int gap = -1;

	public TextIconListCellRenderer(ListCellRenderer renderer) {
		this.renderer = renderer;
	}

	public void calculateMaximumIconWidth(Object[] list) {
		widthTemp = IconUtil.getMaxIconWidth(list);
		emptyIcon = widthTemp == 0 ? null : new Icon() {
			@Override
			public int getIconHeight() {
				return 1;
			}

			@Override
			public int getIconWidth() {
				return widthTemp;
			}

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				// do nothing
			}
		};
	}

	public void setRenderer(ListCellRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (index == -1)
			return null;

		JLabel label = (JLabel) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (gap < 0)
			gap = label.getIconTextGap();

		Icon icon = IconUtil.getIcon(value);

		if (icon == null) {
			label.setIcon(emptyIcon);
		} else {
			label.setIcon(icon);
			label.setIconTextGap(gap + widthTemp - icon.getIconWidth());
		}

		return label;
	}
}