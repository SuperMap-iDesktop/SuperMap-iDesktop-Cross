package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.Geometry;
import com.supermap.data.Resources;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolType;
import com.supermap.desktop.ui.controls.InternalToolkitControl;
import com.supermap.desktop.utilties.FontUtilties;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public class SymbolPanelFill extends SymbolPanel {
	public SymbolPanelFill(Symbol symbol, Resources resources) {
		super(symbol, resources);

		Geometry paintGeometry = getPaintGeometry();
		BufferedImage bufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		paintGeometry.getStyle().setFillSymbolID(symbolID);
		InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());
		init(bufferedImage);
	}

	public SymbolPanelFill(int id, Resources resources) {
		super(id, resources);
		this.symbolName = "System " + id;

		Geometry paintGeometry = getPaintGeometry();
		BufferedImage bufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		paintGeometry.getStyle().setFillSymbolID(symbolID);
		InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());
		if (symbolID == 1) {
			Graphics graphics = bufferedImage.getGraphics();
			Font font = new Font("Dialog", 0, 14);
			graphics.setFont(font);
			graphics.setColor(new Color(13, 80, 143));
			graphics.drawString("NULL", (getIconWidth() - FontUtilties.getStringWidth("NULL", font)) / 2, (getIconHeight() + FontUtilties.getStringHeight("NULL", font)) / 2);
		}
		init(bufferedImage);
	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.FILL;
	}
}
