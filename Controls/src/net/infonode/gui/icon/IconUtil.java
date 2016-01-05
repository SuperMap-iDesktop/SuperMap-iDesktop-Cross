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

// $Id: IconUtil.java,v 1.5 2005/02/16 11:28:11 jesper Exp $
package net.infonode.gui.icon;

import javax.swing.*;
import java.awt.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public class IconUtil {
	private IconUtil() {
	}

	public static final Icon SMALL_ICON = new Icon() {
		@Override
		public int getIconHeight() {
			return 1;
		}

		@Override
		public int getIconWidth() {
			return 1;
		}

		@Override
		public void paintIcon(Component component, Graphics graphics, int x, int y) {
			// do nothing
		}
	};

	public static Icon copy(final Icon icon) {
		return new Icon() {
			@Override
			public void paintIcon(Component component, Graphics graphics, int x, int y) {
				icon.paintIcon(component, graphics, x, y);
			}

			@Override
			public int getIconWidth() {
				return icon.getIconWidth();
			}

			@Override
			public int getIconHeight() {
				return icon.getIconHeight();
			}
		};
	}

	public static Icon getIcon(Object object) {
		return object == null ? null : object instanceof AbstractButton ? (Icon) ((AbstractButton) object).getIcon()
				: object instanceof Action ? (Icon) ((Action) object).getValue(Action.SMALL_ICON) : object instanceof IconProvider ? ((IconProvider) object)
						.getIcon() : null;
	}

	public static int getIconWidth(Object object) {
		Icon icon = getIcon(object);
		return icon == null ? 0 : icon.getIconWidth();
	}

	public static int getIconHeight(Object object) {
		Icon icon = getIcon(object);
		return icon == null ? 0 : icon.getIconHeight();
	}

	public static int getMaxIconWidth(Object[] objects) {
		int max = 0;

		for (int i = 0; i < objects.length; i++) {
			int width = getIconWidth(objects[i]);

			if (width > max) {
				max = width;
			}
		}

		return max;
	}

}
