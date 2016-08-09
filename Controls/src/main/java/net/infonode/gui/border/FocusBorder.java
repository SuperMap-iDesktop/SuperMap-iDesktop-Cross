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

// $Id: FocusBorder.java,v 1.15 2009/02/05 15:57:56 jesper Exp $
package net.infonode.gui.border;

import net.infonode.gui.UIManagerUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serializable;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.15 $
 */
public class FocusBorder implements Border, Serializable {
	private static final long serialVersionUID = 1;

	private static final String WINDOWS_UI_CLASS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	private static final Insets INSETS = new Insets(1, 1, 1, 1);

	private final Component component;

	private boolean enabled = true;

	public FocusBorder(final Component focusComponent) {
		this.component = focusComponent;
		focusComponent.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (enabled)
					focusComponent.repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (enabled)
					focusComponent.repaint();
			}
		});
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return INSETS;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
		}
	}

	@Override
	public void paintBorder(Component componentTemp, Graphics graphics, int x, int y, int width, int height) {
		if (enabled && component.hasFocus()) {
			graphics.setColor(UIManagerUtil.getColor("Button.focus", "TabbedPane.focus"));

			if (FocusBorder.WINDOWS_UI_CLASS.equals(UIManager.getLookAndFeel().getClass().getName()))
				BasicGraphicsUtils.drawDashedRect(graphics, x, y, width, height);
			else
				graphics.drawRect(x, y, width - 1, height - 1);
		}
	}
}
