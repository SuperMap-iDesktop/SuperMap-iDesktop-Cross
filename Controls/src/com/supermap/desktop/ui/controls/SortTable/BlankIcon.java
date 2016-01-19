package com.supermap.desktop.ui.controls.SortTable;

import javax.swing.*;
import java.awt.*;

/**
 * @version 1.0 02/26/99
 */
public class BlankIcon implements Icon {
	private Color fillColor;
	private int size;

	public BlankIcon() {
		this(null, 11);
	}

	public BlankIcon(Color color, int size) {
		//UIManager.getColor("control")
		//UIManager.getColor("controlShadow")
		fillColor = color;

		this.size = size;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (fillColor != null) {
			g.setColor(fillColor);
			g.drawRect(x, y, size, size);
		}
	}

	public int getIconWidth() {
		return size;
	}

	public int getIconHeight() {
		return size;
	}
}
