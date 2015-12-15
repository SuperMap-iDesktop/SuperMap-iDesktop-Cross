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

// $Id: StretchLayout.java,v 1.7 2005/02/16 11:28:12 jesper Exp $
package net.infonode.gui.layout;

import java.awt.*;

public class StretchLayout implements LayoutManager {
	public static final StretchLayout BOTH = new StretchLayout();

	private boolean horizontal;
	private boolean vertical;

	public StretchLayout() {
		this(true, true);
	}

	public StretchLayout(boolean horizontal, boolean vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// do nothing
	}

	@Override
	public void layoutContainer(Container parent) {
		Dimension innerSize = LayoutUtil.getInteriorSize(parent);
		Insets insets = parent.getInsets();
		Component[] components = LayoutUtil.getVisibleChildren(parent);

		for (int i = 0; i < components.length; i++) {
			Dimension size = new Dimension(horizontal ? innerSize.width : components[i].getPreferredSize().width, vertical ? innerSize.height
					: components[i].getPreferredSize().height);
			components[i].setBounds((int) (insets.left + (innerSize.width - size.width) * components[i].getAlignmentX()),
					(int) (insets.top + (innerSize.height - size.height) * components[i].getAlignmentY()), size.width, size.height);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return LayoutUtil.add(LayoutUtil.getMaxMinimumSize(LayoutUtil.getVisibleChildren(parent)), parent.getInsets());
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return LayoutUtil.add(LayoutUtil.getMaxPreferredSize(LayoutUtil.getVisibleChildren(parent)), parent.getInsets());
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// do nothing
	}
}
