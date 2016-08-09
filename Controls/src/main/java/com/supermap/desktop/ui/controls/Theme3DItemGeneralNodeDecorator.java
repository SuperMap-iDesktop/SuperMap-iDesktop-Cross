package com.supermap.desktop.ui.controls;

import com.supermap.data.*;
import com.supermap.data.Toolkit;
import com.supermap.realspace.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 三维专题图子项节点装饰器
 * @author xuzw
 *
 */
class Theme3DItemGeneralNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		Object item = data.getData();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
				IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		ImageIcon icon = (ImageIcon) label.getIcon();
		if (item instanceof Theme3DRangeItem) {
			Layer3DDataset parentLayer = (Layer3DDataset) data
					.getParentLayer3D();
			Resources resources = parentLayer.getDataset().getDatasource()
					.getWorkspace().getResources();
			Theme3DRangeItem rangeItem = (Theme3DRangeItem) item;
			label.setText(rangeItem.getCaption());
			GeoStyle geoStyle = geoStyle3D2GeoStyle(rangeItem.getStyle());
			Geometry geometry = getGeometryByDatasetType(parentLayer
					.getDataset().getType());
			geometry.setStyle(geoStyle);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		} else if (item instanceof Theme3DUniqueItem) {
			Layer3DDataset parentLayer = (Layer3DDataset) data
					.getParentLayer3D();
			Resources resources = parentLayer.getDataset().getDatasource()
					.getWorkspace().getResources();
			Theme3DUniqueItem uniqueItem = (Theme3DUniqueItem) item;
			label.setText(uniqueItem.getCaption());
			GeoStyle geoStyle = geoStyle3D2GeoStyle(uniqueItem.getStyle());
			Geometry geometry = getGeometryByDatasetType(parentLayer
					.getDataset().getType());
			geometry.setStyle(geoStyle);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		} else if (item instanceof Feature3D) {
			Feature3D feature3D = (Feature3D) item;
			label.setText(feature3D.getName());
			graphics.drawImage(InternalImageIconFactory.FEATURE3D.getImage(),
					0, 0, label);
			icon.setImage(bufferedImage);
		} else if (item instanceof Feature3Ds) {
			Feature3Ds feature3Ds = (Feature3Ds) item;
			label.setText(feature3Ds.getName());
			graphics.drawImage(InternalImageIconFactory.FEATURE3DS.getImage(),
					0, 0, label);
			icon.setImage(bufferedImage);
		}
	}

	private Geometry getGeometryByDatasetType(DatasetType type) {
		if (type.equals(DatasetType.POINT) || type.equals(DatasetType.POINT3D)) {
			GeoPoint geoPoint = new GeoPoint();
			return geoPoint;
		}
		if (type.equals(DatasetType.LINE) || type.equals(DatasetType.LINE3D)) {
			Point2D[] pts = { new Point2D(0, 16), new Point2D(4, 0),
					new Point2D(12, 16), new Point2D(16, 0) };
			Point2Ds ds = new Point2Ds(pts);
			GeoLine geoLine = new GeoLine(ds);
			return geoLine;
		}
		if (type.equals(DatasetType.REGION)
				|| type.equals(DatasetType.REGION3D)) {
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
		}else{
			geoStyle.setLineWidth(style3D.getLineWidth());
		}
		geoStyle.setMarkerSize(new Size2D(style3D.getMarkerSize(), style3D
				.getMarkerSize()));
		return geoStyle;
	}

}
