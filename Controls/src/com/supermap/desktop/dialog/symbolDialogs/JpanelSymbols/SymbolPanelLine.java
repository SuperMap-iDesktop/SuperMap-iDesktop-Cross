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
public class SymbolPanelLine extends SymbolPanel {
	public SymbolPanelLine(Symbol symbol, Resources resources) {
		super(symbol, resources);
		Geometry paintGeometry = getPaintGeometry();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		paintGeometry.getStyle().setLineSymbolID(symbolID);
		InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());
		init(bufferedImage);
	}


	public SymbolPanelLine(int id, Resources resources) {
		super(id, resources);
		this.symbolName = "System " + id;

		Geometry paintGeometry = getPaintGeometry();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		paintGeometry.getStyle().setLineSymbolID(symbolID);
		InternalToolkitControl.internalDraw(paintGeometry, resources, bufferedImage.getGraphics());

		if (symbolID == 5) {
			Graphics graphics = bufferedImage.getGraphics();
			Font font = new Font("Dialog", 0, 14);
			graphics.setFont(font);
			graphics.setColor(Color.red);
			graphics.drawString("NULL", (width - FontUtilties.getStringWidth("NULL", font)) / 2, (height + FontUtilties.getStringHeight("NULL", font)) / 2);
		}
		init(bufferedImage);
	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.LINE;
	}
}
