package com.supermap.desktop.ui.controls;

import com.supermap.data.*;
import com.supermap.data.Toolkit;
import com.supermap.mapping.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 二维专题图子项节点装饰器
 * 
 * @author xuzw
 *
 */
class ThemeItemGeneralNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		Layer parentLayer = data.getParentLayer();
		Resources resources = parentLayer.getDataset().getDatasource().getWorkspace().getResources();
		Object item = data.getData();
		BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		ImageIcon icon = (ImageIcon) label.getIcon();
		NodeDataType type = data.getType();
		if (type.equals(NodeDataType.THEME_GRID_UNIQUE_ITEM)) {
			ThemeGridUniqueItem gridRangeItem = (ThemeGridUniqueItem) item;
			label.setText(gridRangeItem.getCaption());
			Color color = gridRangeItem.getColor();
			Geometry geometry = getGeometryByDatasetType(parentLayer.getDataset().getType());
			GeoStyle geoStyle = new GeoStyle();
			geoStyle.setFillBackColor(color);
			geoStyle.setFillForeColor(color);
			geoStyle.setLineColor(color);
			geometry.setStyle(geoStyle);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		} else if(type.equals(NodeDataType.THEME_GRID_RANGE_ITEM)){
			ThemeGridRangeItem gridRangeItem = (ThemeGridRangeItem) item;
			label.setText(gridRangeItem.getCaption());
			Color color = gridRangeItem.getColor();
			Geometry geometry = getGeometryByDatasetType(parentLayer.getDataset().getType());
			GeoStyle geoStyle = new GeoStyle();
			geoStyle.setFillBackColor(color);
			geoStyle.setFillForeColor(color);
			geoStyle.setLineColor(color);
			geometry.setStyle(geoStyle);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		}else if (type.equals(NodeDataType.THEME_LABEL_ITEM)) {
			ThemeLabelItem labelItem = (ThemeLabelItem) item;
			label.setText(labelItem.getCaption());
			TextPart part = new TextPart();
			part.setText(labelItem.getCaption());
			if (parentLayer.getDataset().getType() == DatasetType.CAD) {
				graphics.drawImage(InternalImageIconFactory.DT_CAD.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			} else {
				Color color = labelItem.getStyle().getForeColor();
				GeoStyle geoStyle = new GeoStyle();
				geoStyle.setFillBackColor(color);
				geoStyle.setFillForeColor(color);
				geoStyle.setLineColor(color);
				geoStyle.setLineWidth(0.2);
				Geometry geometry = getGeometryByDatasetType(parentLayer
						.getDataset().getType());
				geometry.setStyle(geoStyle);
				Toolkit.draw(geometry, resources, graphics);
				icon.setImage(bufferedImage);
			}
		} else if (type.equals(NodeDataType.THEME_RANGE_ITEM)) {
			ThemeRangeItem rangeItem = (ThemeRangeItem) item;
			label.setText(rangeItem.getCaption());
			GeoStyle geoStyle = rangeItem.getStyle();
			Geometry geometry = getGeometryByDatasetType(parentLayer
					.getDataset().getType());
			GeoStyle geoStyleClone = geoStyle.clone();
			if (geoStyleClone.getLineWidth() > 0.2) {
				geoStyleClone.setLineWidth(0.2);
			}
			geometry.setStyle(geoStyleClone);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		} else if (item instanceof ThemeUniqueItem) {
			ThemeUniqueItem uniqueItem = (ThemeUniqueItem) item;
			label.setText(uniqueItem.getCaption());
			GeoStyle geoStyle = uniqueItem.getStyle();
			Geometry geometry = getGeometryByDatasetType(parentLayer
					.getDataset().getType());
			GeoStyle geoStyleClone = geoStyle.clone();
			if (geoStyleClone.getLineWidth() > 0.2) {
				geoStyleClone.setLineWidth(0.2);
			}
			geometry.setStyle(geoStyleClone);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		}
	}

	private Geometry getGeometryByDatasetType(DatasetType type) {
		if (type.equals(DatasetType.POINT) || type.equals(DatasetType.POINT3D)) {
			GeoPoint geoPoint = new GeoPoint(10, 10);
			return geoPoint;
		}
		if (type.equals(DatasetType.LINE) || type.equals(DatasetType.LINE3D) || type.equals(DatasetType.LINEM) || type.equals(DatasetType.NETWORK)
				|| type.equals(DatasetType.NETWORK3D)) {
			Point2D[] pts = { new Point2D(0, 0), new Point2D(16, 16) };
			Point2Ds ds = new Point2Ds(pts);
			GeoLine geoLine = new GeoLine(ds);
			return geoLine;
		}
		if (type.equals(DatasetType.REGION) || type.equals(DatasetType.GRID)) {
			Point2D[] points = { new Point2D(1, 15), new Point2D(1, 5), new Point2D(10, 1), new Point2D(15, 5), new Point2D(15, 15), new Point2D(1, 15) };
			Point2Ds ds = new Point2Ds(points);
			GeoRegion geoRegion = new GeoRegion(ds);
			return geoRegion;
		}
		return null;
	}

}

