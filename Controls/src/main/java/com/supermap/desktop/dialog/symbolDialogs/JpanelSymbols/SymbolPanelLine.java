package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.Geometry;
import com.supermap.data.Resources;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolType;
import com.supermap.desktop.ui.controls.InternalToolkitControl;
import com.supermap.desktop.utilities.FontUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public class SymbolPanelLine extends SymbolPanel {
	private boolean isPaint = false;
	private static final Object lock = new Object();

	public SymbolPanelLine(Symbol symbol, Resources resources) {
		super(symbol, resources);

		initSize();

	}


	public SymbolPanelLine(int id, Resources resources) {
		super(id, resources);

		initSize();

	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.LINE;
	}

	@Override
	public void paint(final Graphics g) {
		if (isPaint) {
			super.paint(g);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					synchronized (lock) {
						if (isPaint) {
							return;
						}
						if (symbol != null) {
							Geometry paintGeometry = getPaintGeometry();
							BufferedImage bufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
							paintGeometry.getStyle().setLineSymbolID(symbolID);
							InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());
							init(bufferedImage);
						} else {
							Geometry paintGeometry = getPaintGeometry();
							BufferedImage bufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
							paintGeometry.getStyle().setLineSymbolID(symbolID);
							InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());
							if (symbolID == 5) {
								Graphics graphics = bufferedImage.getGraphics();
								Font font = new Font("Dialog", 0, 14);
								graphics.setFont(font);
								graphics.setColor(Color.red);
								graphics.drawString("NULL", (getIconWidth() - FontUtilities.getStringWidth("NULL", font)) / 2, (getIconHeight() + FontUtilities.getStringHeight("NULL", font)) / 2);
							}
							init(bufferedImage);
						}
						isPaint = true;
						revalidate();
						repaint();
					}
				}
			});
		}
	}

}
