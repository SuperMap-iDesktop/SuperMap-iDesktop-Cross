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

// $Id: HeavyWeightContainer.java,v 1.5 2005/12/04 13:46:04 jesper Exp $

package com.supermap.desktop.ui.docking.internal;

import java.awt.*;

/**
 * @author johan
 */
public class HeavyWeightContainer extends Panel {
	private transient Image bufferImage;
	private boolean doubleBuffer = false;

	public HeavyWeightContainer(Component c) {
		this(c, false);
	}

	public HeavyWeightContainer(Component c, boolean doubleBuffer) {
		super(new BorderLayout());
		this.doubleBuffer = doubleBuffer;
		add(c, BorderLayout.CENTER);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		bufferImage = null;
	}

	@Override
	public void update(Graphics g) {
		if (doubleBuffer)
			paint(g);
		else
			super.update(g);
	}

	@Override
	public boolean isDoubleBuffered() {
		return doubleBuffer;
	}

	@Override
	public void paint(Graphics g) {
		if (doubleBuffer) {
			if (bufferImage == null)
				bufferImage = createImage(getWidth(), getHeight());

			Graphics g2 = bufferImage.getGraphics();
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
			super.paint(g2);

			g.drawImage(bufferImage, 0, 0, null);
			g2.dispose();
		} else {
			super.paint(g);
		}
	}
}
