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

// $Id: RotatableLabel.java,v 1.8 2005/12/04 13:46:04 jesper Exp $
package net.infonode.gui;

import net.infonode.util.Direction;

import javax.swing.*;
import javax.swing.plaf.LabelUI;
import java.awt.*;

public class RotatableLabel extends JLabel {
	private transient RotatableLabelUI rotatableLabelUI = new RotatableLabelUI(Direction.RIGHT);

	public RotatableLabel(String text) {
		super(text);
		init();
	}

	public RotatableLabel(String text, Icon icon) {
		super(text, icon, LEFT);
		init();
	}

	private void init() {
		super.setUI(rotatableLabelUI);
		super.setOpaque(false);
	}

	public Direction getDirection() {
		return rotatableLabelUI.getDirection();
	}

	public void setDirection(Direction direction) {
		if (rotatableLabelUI.getDirection() != direction) {
			rotatableLabelUI.setDirection(direction);
			revalidate();
		}
	}

	public void setMirror(boolean mirror) {
		rotatableLabelUI.setMirror(mirror);
		revalidate();
	}

	public boolean isMirror() {
		return rotatableLabelUI.isMirror();
	}

	@Override
	public void setUI(LabelUI labelUI) {
		// Ignore
	}

	private boolean isVertical() {
		return !rotatableLabelUI.getDirection().isHorizontal();
	}

	private Dimension rotateDimension(Dimension dimension) {
		return dimension == null ? null : isVertical() ? new Dimension(dimension.height, dimension.width) : dimension;
	}

	@Override
	public Dimension getPreferredSize() {
		return rotateDimension(super.getPreferredSize());
	}

	@Override
	public Dimension getMinimumSize() {
		return rotateDimension(super.getMinimumSize());
	}

	@Override
	public Dimension getMaximumSize() {
		return rotateDimension(super.getMaximumSize());
	}

	@Override
	public void setMinimumSize(Dimension minimumSize) {
		super.setMinimumSize(rotateDimension(minimumSize));
	}

	@Override
	public void setMaximumSize(Dimension maximumSize) {
		super.setMaximumSize(rotateDimension(maximumSize));
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(rotateDimension(preferredSize));
	}

	@Override
	public void setOpaque(boolean opaque) {
		// do nothing
	}

}
