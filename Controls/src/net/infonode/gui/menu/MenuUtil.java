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

// $Id: MenuUtil.java,v 1.3 2005/02/16 11:28:13 jesper Exp $
package net.infonode.gui.menu;

import net.infonode.gui.icon.IconUtil;

import javax.swing.*;

import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 */
public class MenuUtil {
	private MenuUtil() {
	}

	public static void optimizeSeparators(JPopupMenu menu) {
		boolean lastSeparator = true;

		for (int i = 0; i < menu.getComponentCount();) {
			if (menu.getComponent(i).isVisible() && menu.getComponent(i) instanceof JMenu)
				optimizeSeparators(((JMenu) menu.getComponent(i)).getPopupMenu());

			boolean separator = menu.getComponent(i) instanceof JPopupMenu.Separator;

			if (lastSeparator && separator)
				menu.remove(i);
			else
				i++;
			lastSeparator = separator;
		}

		if (menu.getComponentCount() > 0 && menu.getComponent(menu.getComponentCount() - 1) instanceof JPopupMenu.Separator)
			menu.remove(menu.getComponentCount() - 1);
	}

	public static void align(MenuElement menu) {
		MenuElement[] children = menu.getSubElements();
		final int maxWidth = IconUtil.getMaxIconWidth(children);

		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof AbstractButton) {
				AbstractButton abstractButton = (AbstractButton) children[i];
				final Icon icon = abstractButton.getIcon();
				abstractButton.setIcon(new Icon() {
					@Override
					public int getIconHeight() {
						return icon == null ? 1 : icon.getIconHeight();
					}

					@Override
					public int getIconWidth() {
						return maxWidth;
					}

					@Override
					public void paintIcon(Component component, Graphics graphics, int x, int y) {
						if (icon != null)
							icon.paintIcon(component, graphics, x, y);
					}
				});
			}

			align(children[i]);
		}
	}

}
