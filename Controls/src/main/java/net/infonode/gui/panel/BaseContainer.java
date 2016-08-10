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

// $Id: BaseContainer.java,v 1.13 2005/12/04 13:46:03 jesper Exp $
package net.infonode.gui.panel;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;

public class BaseContainer extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color foregroundTemp;

	private Color backgroundTemp;

	private Font fontTemp;

	private Color overridedBackground;

	private Color overridedForeground;

	private Font overridedFont;

	private boolean forcedOpaque = true;

	private boolean opaque = true;

	private static PanelUI panelUI = new PanelUI() {
	};

	public BaseContainer() {
		this(true);
	}

	public BaseContainer(boolean opaque) {
		this(opaque, new BorderLayout());
	}

	public BaseContainer(LayoutManager layoutManager) {
		this(true, layoutManager);
	}

	public BaseContainer(final boolean opaque, LayoutManager layoutManager) {
		super(layoutManager);

		this.forcedOpaque = opaque;

		updateOpaque();
	}

	@Override
	public void setUI(PanelUI ui) {
		Color backgroundColor = overridedBackground;
		Color foregroundColor = overridedForeground;
		Font font = overridedFont;

		backgroundColor = null;
		foregroundColor = null;
		font = null;

		setBackground(null);
		setForeground(null);
		setFont(null);

		super.setUI(ui);

		backgroundTemp = getBackground();
		foregroundTemp = getForeground();
		this.fontTemp = getFont();

		overridedBackground = backgroundColor;
		overridedForeground = foregroundColor;
		overridedFont = font;

		if (!forcedOpaque)
			super.setUI(panelUI);

		updateBackground();
		updateForeground();
		updateFont();
	}

	void setForcedOpaque(final boolean forcedOpaque) {
		if (this.forcedOpaque != forcedOpaque) {
			this.forcedOpaque = forcedOpaque;

			updateUI();
			updateOpaque();
		}
	}

	// Overrided
	@Override
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;

		updateOpaque();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		if (forcedOpaque)
			super.paintComponent(graphics);
	}

	@Override
	public void setForeground(Color foreGroundColor) {
		this.foregroundTemp = foreGroundColor;

		updateForeground();
	}

	@Override
	public void setBackground(Color backGroundColor) {
		this.backgroundTemp = backGroundColor;

		updateBackground();
	}

	@Override
	public void setFont(Font font) {
		this.fontTemp = font;

		updateFont();
	}

	void setOverridedForeround(Color overridedForerGoundColor) {
		this.overridedForeground = overridedForerGoundColor;

		updateForeground();
	}

	void setOverridedBackground(Color overridedBackgroundColor) {
		this.overridedBackground = overridedBackgroundColor;

		updateBackground();
	}

	void setOverrideFont(Font font) {
		this.overridedFont = font;

		updateFont();
	}

	private void updateBackground() {
		super.setBackground(overridedBackground == null ? backgroundTemp : overridedBackground);
	}

	private void updateForeground() {
		super.setForeground(overridedForeground == null ? foregroundTemp : overridedForeground);
	}

	private void updateFont() {
		super.setFont(overridedFont == null ? fontTemp : overridedFont);
	}

	private void updateOpaque() {
		super.setOpaque(forcedOpaque ? opaque : forcedOpaque);
	}
}
