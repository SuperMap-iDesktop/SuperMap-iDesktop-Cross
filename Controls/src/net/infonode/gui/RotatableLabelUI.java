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

// $Id: RotatableLabelUI.java,v 1.11 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class RotatableLabelUI extends BasicLabelUI {
	// Optimization
	private static Rectangle paintIconR = new Rectangle();
	private static Rectangle paintTextR = new Rectangle();
	private static Rectangle paintViewR = new Rectangle();

	private Direction direction;
	private boolean mirror;

	public RotatableLabelUI(Direction direction) {
		this(direction, false);
	}

	public RotatableLabelUI(Direction direction, boolean mirror) {
		this.direction = direction;
		this.mirror = mirror;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isMirror() {
		return mirror;
	}

	public void setMirror(boolean mirror) {
		this.mirror = mirror;
	}

	@Override
	public void paint(Graphics graphics, JComponent jComponent) {
		JLabel label = (JLabel) jComponent;
		String text = label.getText();
		Icon icon = label.isEnabled() ? label.getIcon() : label.getDisabledIcon();

		if (icon == null && text == null)
			return;

		FontMetrics fontMetrics = graphics.getFontMetrics();
		Insets insets = jComponent.getInsets();

		paintViewR.x = insets.left;
		paintViewR.y = insets.top;

		if (direction.isHorizontal()) {
			paintViewR.height = jComponent.getHeight() - (insets.top + insets.bottom);
			paintViewR.width = jComponent.getWidth() - (insets.left + insets.right);
		} else {
			paintViewR.height = jComponent.getWidth() - (insets.top + insets.bottom);
			paintViewR.width = jComponent.getHeight() - (insets.left + insets.right);
		}

		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

		String clippedText = layoutCL(label, fontMetrics, text, icon, paintViewR, paintIconR, paintTextR);

		Graphics2D graphics2D = (Graphics2D) graphics;
		AffineTransform affineTransform = graphics2D.getTransform();

		int m = mirror ? -1 : 1;
		graphics2D.transform(direction == Direction.RIGHT ? new AffineTransform(1, 0, 0, m, 0, mirror ? jComponent.getHeight() : 0)
				: direction == Direction.DOWN ? new AffineTransform(0, 1, -m, 0, mirror ? 0 : jComponent.getWidth(), 0)
						: direction == Direction.LEFT ? new AffineTransform(-1, 0, 0, -m, jComponent.getWidth(), mirror ? 0 : jComponent.getHeight())
								: new AffineTransform(0, -1, m, 0, mirror ? jComponent.getWidth() : 0, jComponent.getHeight()));

		if (icon != null) {
			icon.paintIcon(jComponent, graphics, paintIconR.x, paintIconR.y);
		}

		if (text != null) {
			int textX = paintTextR.x;
			int textY = paintTextR.y + fontMetrics.getAscent();

			if (label.isEnabled()) {
				paintEnabledText(label, graphics, clippedText, textX, textY);
			} else {
				paintDisabledText(label, graphics, clippedText, textX, textY);
			}
		}

		graphics2D.setTransform(affineTransform);
	}
}
