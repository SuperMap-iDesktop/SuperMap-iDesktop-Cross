package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.GeoPoint;
import com.supermap.data.Geometry;
import com.supermap.data.Resources;
import com.supermap.data.Size2D;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolMarker;
import com.supermap.data.SymbolMarker3D;
import com.supermap.data.SymbolType;
import com.supermap.desktop.enums.SymbolMarkerType;
import com.supermap.desktop.ui.controls.InternalToolkitControl;
import com.supermap.desktop.ui.controls.UIEnvironment;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author XiaJt
 */
public class SymbolPanelPoint extends SymbolPanel {
	public SymbolPanelPoint(Symbol symbol, Resources resources) {
		super(symbol, resources);
		BufferedImage bufferedImage;
		Geometry paintGeometry = getPaintGeometry();

		// 二维点符号与二三维点符号分别处理
		if (symbol instanceof SymbolMarker) {
			bufferedImage = getSymbolMarkerBuffedImage(getIconWidth(), getIconHeight(), (SymbolMarker) symbol, ((GeoPoint) paintGeometry));
		} else {
			bufferedImage = ((SymbolMarker3D) symbol).getThumbnail();
		}
		init(bufferedImage);
	}

	public SymbolPanelPoint(int id, Resources resources) {
		super(id, resources);
		this.symbolName = "System " + (id + 1);

		Geometry paintGeometry = getPaintGeometry();
		BufferedImage bufferedImage = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		paintGeometry.getStyle().setMarkerSymbolID(symbolID);
		InternalToolkitControl.internalDraw(paintGeometry, this.resources, bufferedImage.getGraphics());
		init(bufferedImage);
	}

	@Override
	protected SymbolType getSymbolType() {
		return SymbolType.MARKER;
	}

	private BufferedImage getSymbolMarkerBuffedImage(int width, int height, SymbolMarker symbolMarker, GeoPoint geoPoint) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Point point = symbolMarker.getOrigin();
		int x = point.x * width / UIEnvironment.symbolPointMax;
		int y = point.y * height / UIEnvironment.symbolPointMax;
		geoPoint.setX(x);
		geoPoint.setY(y);
		geoPoint.getStyle().setMarkerSymbolID(symbolMarker.getID());
		if (SymbolMarkerType.getSymbolMarkerType(symbolMarker) != SymbolMarkerType.Vector) {
			geoPoint.getStyle().setMarkerSize(new Size2D(0, 1));
		}
		InternalToolkitControl.internalDraw(geoPoint, resources, bufferedImage.getGraphics());

		return bufferedImage;
	}

	@Override
	protected int getIconWidth() {
		return 40;
	}

	@Override
	protected int getIconHeight() {
		return 40;
	}
}
