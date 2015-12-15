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

// $Id: HoverableShapedPanel.java,v 1.15 2005/12/04 13:46:03 jesper Exp $
package net.infonode.gui.hover.panel;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.gui.hover.hoverable.HoverManager;
import net.infonode.gui.hover.hoverable.Hoverable;
import net.infonode.gui.shaped.panel.ShapedPanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author johan
 */
public class HoverableShapedPanel extends ShapedPanel implements Hoverable {
	private transient HoverListener hoverListener;

	private Component hoveredComponent;

	private boolean hovered = false;

	public HoverableShapedPanel(HoverListener listener) {
		this(new BorderLayout(), listener, null);
	}

	public HoverableShapedPanel(LayoutManager layoutManager, HoverListener listener) {
		this(layoutManager, listener, null);
	}

	public HoverableShapedPanel(LayoutManager layoutManager, HoverListener listener, final Component hoveredComponent) {
		super(layoutManager);
		this.hoveredComponent = hoveredComponent != null ? hoveredComponent : this;
		HoverManager.getInstance().addHoverable(this);
		setHoverListener(listener);
	}

	public HoverListener getHoverListener() {
		return hoverListener;
	}

	public void setHoverListener(HoverListener newHoverListener) {
		if (hoverListener != newHoverListener) {
			HoverListener oldHoverListener = hoverListener;

			hoverListener = newHoverListener;
			if (oldHoverListener != null && newHoverListener != null && hovered) {
				HoverEvent event = new HoverEvent(hoveredComponent);
				oldHoverListener.mouseExited(event);
				newHoverListener.mouseEntered(event);
			}
		}
	}

	@Override
	public void hoverEnter() {
		if (hoverListener != null) {
			hovered = true;
			hoverListener.mouseEntered(new HoverEvent(HoverableShapedPanel.this.hoveredComponent));
		}
	}

	@Override
	public void hoverExit() {
		if (hoverListener != null) {
			hovered = false;
			hoverListener.mouseExited(new HoverEvent(HoverableShapedPanel.this.hoveredComponent));
		}
	}

	public Component getHoveredComponent() {
		return hoveredComponent;
	}

	public boolean isHovered() {
		return hovered;
	}

	@Override
	public boolean acceptHover(ArrayList enterableHoverables) {
		return true;
	}
}
