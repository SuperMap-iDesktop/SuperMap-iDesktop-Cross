package com.supermap.desktop.ui.controls;

import com.supermap.data.*;
import com.supermap.data.Toolkit;
import com.supermap.realspace.Layer3DVectorFile;
import com.supermap.realspace.Layer3DVectorFileDataType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 矢量文件图层节点装饰器
 * @author xuzw
 *
 */
class Layer3DVectorFileNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.LAYER3D_VECTOR_FILE)) {
			ImageIcon icon = (ImageIcon) label.getIcon();
			Layer3DVectorFile layer3DVectorFile = (Layer3DVectorFile) data
					.getData();
			label.setText(layer3DVectorFile.getCaption());
			Layer3DVectorFileDataType type = layer3DVectorFile.getDataType();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			GeoStyle3D geoStyle3D = layer3DVectorFile.getStyle();
			if (type.equals(Layer3DVectorFileDataType.POINT)) {
				Geometry geometry = getGeometryByLayer3DVectorFileDataType(type);
				GeoStyle geoStyle = geoStyle3D2GeoStyle(geoStyle3D);
				geometry.setStyle(geoStyle);
				graphics.setColor(geoStyle.getLineColor());
				graphics.drawImage(InternalImageIconFactory.LAYER3D_VECTOR_FILE_BACK.getImage(), 0, 0, label);
				graphics.fillOval(IMAGEICON_WIDTH / 2 - 2,
						IMAGEICON_WIDTH / 2 - 2, 4, 4);
				icon.setImage(bufferedImage);
			} else if (type.equals(Layer3DVectorFileDataType.LINE)) {
				GeoStyle geoStyle = geoStyle3D2GeoStyle(geoStyle3D);
				graphics.setColor(geoStyle.getLineColor());
				geoStyle.setLineWidth(0.2);
				Geometry geometry = getGeometryByLayer3DVectorFileDataType(type);
				geometry.setStyle(geoStyle);
				Toolkit.draw(geometry, new Resources(), graphics);
				graphics.drawImage(InternalImageIconFactory.LAYER3D_VECTOR_FILE_BACK.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			} else if (type.equals(Layer3DVectorFileDataType.REGION)) {
				GeoStyle geoStyle = geoStyle3D2GeoStyle(geoStyle3D);
				Geometry geometry = getGeometryByLayer3DVectorFileDataType(type);
				geometry.setStyle(geoStyle);
				Toolkit.draw(geometry, new Resources(), graphics);
				graphics.drawImage(InternalImageIconFactory.LAYER3D_VECTOR_FILE_BACK.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			} else if (type.equals(Layer3DVectorFileDataType.TEXT)) {
				graphics.drawImage(InternalImageIconFactory.DT_TEXT.getImage(),
						0, 0, label);
				graphics.drawImage(InternalImageIconFactory.LAYER3D_VECTOR_FILE_BACK.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			} else if (type.equals(Layer3DVectorFileDataType.MODEL)) {
				graphics.drawImage(InternalImageIconFactory.LAYER3D_VECTOR_FILE
						.getImage(), 0, 0, label);
				graphics.drawImage(InternalImageIconFactory.LAYER3D_VECTOR_FILE_BACK.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			}
		}
	}

	private Geometry getGeometryByLayer3DVectorFileDataType(
			Layer3DVectorFileDataType type) {
		if (type.equals(Layer3DVectorFileDataType.POINT)) {
			GeoPoint geoPoint = new GeoPoint();
			return geoPoint;
		}
		if (type.equals(Layer3DVectorFileDataType.LINE)) {
			Point2D[] pts = { new Point2D(0, 16), new Point2D(4, 0),
					new Point2D(12, 16), new Point2D(16, 0) };
			Point2Ds ds = new Point2Ds(pts);
			GeoLine geoLine = new GeoLine(ds);
			return geoLine;
		}
		if (type.equals(Layer3DVectorFileDataType.REGION)) {
			Point2D[] points = { new Point2D(1, 15), new Point2D(1, 5),
					new Point2D(10, 1), new Point2D(15, 5),
					new Point2D(15, 15), new Point2D(1, 15) };
			Point2Ds ds = new Point2Ds(points);
			GeoRegion geoRegion = new GeoRegion(ds);
			return geoRegion;
		}
		return null;
	}

	private GeoStyle geoStyle3D2GeoStyle(GeoStyle3D style3D) {
		GeoStyle geoStyle = new GeoStyle();
		geoStyle.setFillBackColor(style3D.getFillForeColor());
		geoStyle.setFillForeColor(style3D.getFillForeColor());
		geoStyle.setLineColor(style3D.getLineColor());
		if (style3D.getLineWidth() > 0.2) {
			geoStyle.setLineWidth(0.2);
		} else {
			geoStyle.setLineWidth(style3D.getLineWidth());
		}
		geoStyle.setMarkerSize(new Size2D(style3D.getMarkerSize(), style3D
				.getMarkerSize()));
		return geoStyle;
	}
}
