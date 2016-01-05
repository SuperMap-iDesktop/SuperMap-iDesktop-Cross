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

// $Id: HeavyWeightDragRectangle.java,v 1.4 2005/12/04 13:46:04 jesper Exp $
package com.supermap.desktop.ui.docking.internal;

import javax.swing.*;

import java.awt.*;

/**
 * @author johan
 */
public class HeavyWeightDragRectangle extends JPanel {
	private int widthTemp = 4;

	private Canvas northCanvas = new Canvas() {
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(widthTemp, widthTemp);
		}
	};
	private Canvas southCanvas = new Canvas() {
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(widthTemp, widthTemp);
		}
	};
	private Canvas eastCanvas = new Canvas() {
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(widthTemp, widthTemp);
		}
	};
	private Canvas westCanvas = new Canvas() {
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(widthTemp, widthTemp);
		}
	};

	public HeavyWeightDragRectangle() {
		super(new BorderLayout());
		setOpaque(false);

		add(northCanvas, BorderLayout.NORTH);
		add(southCanvas, BorderLayout.SOUTH);
		add(westCanvas, BorderLayout.WEST);
		add(eastCanvas, BorderLayout.EAST);

		setColor(Color.BLACK);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		revalidate();
	}

	public void setBorderWidth(int width) {
		this.widthTemp = width;

		revalidate();
	}

	public void setColor(Color c) {
		northCanvas.setBackground(c);
		southCanvas.setBackground(c);
		eastCanvas.setBackground(c);
		westCanvas.setBackground(c);
	}
}
