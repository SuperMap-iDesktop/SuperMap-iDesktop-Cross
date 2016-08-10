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

// $Id: DragLabelWindow.java,v 1.8 2005/12/04 13:46:04 jesper Exp $

package net.infonode.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author johan
 */
public class DragLabelWindow extends Dialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel() {
		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
	};

	public DragLabelWindow(Dialog dialog) {
		super(dialog);
		init();
	}

	public DragLabelWindow(Frame f) {
		super(f);
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		add(label, BorderLayout.NORTH);
		setUndecorated(true);
		label.setOpaque(true);
	}

	public JLabel getLabel() {
		return label;
	}

	@Override
	public void setCursor(Cursor cursor) {
		label.setCursor(cursor);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible != isVisible() && visible) {
			setSize(0, 0);
			pack();
			setSize(getPreferredSize());
		}

		super.setVisible(visible);
	}
}