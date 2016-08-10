package com.supermap.desktop.ui.controls;

import com.supermap.data.*;
import com.supermap.data.Toolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.ThemeGraphItem;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 统计专题图子项节点装饰器
 * @author xuzw
 *
 */
class ThemeGraphItemNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.THEME_GRAPH_ITEM)) {
			ThemeGraphItem graphItem = (ThemeGraphItem) data.getData();
			label.setText(graphItem.getCaption());
			Layer parentLayer = data.getParentLayer();
			Resources resources = parentLayer.getDataset().getDatasource()
					.getWorkspace().getResources();
			BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH,
					IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			ImageIcon icon = (ImageIcon) label.getIcon();
			Geometry geometry = getGeometryByDatasetType(DatasetType.REGION);
			GeoStyle geoStyle = graphItem.getUniformStyle();
			geometry.setStyle(geoStyle);
			Toolkit.draw(geometry, resources, graphics);
			icon.setImage(bufferedImage);
		}
	}

	private Geometry getGeometryByDatasetType(DatasetType type) {
		if (type.equals(DatasetType.POINT)) {
			GeoPoint geoPoint = new GeoPoint();
			return geoPoint;
		}
		if (type.equals(DatasetType.LINE)) {
			Point2D[] pts = { new Point2D(0, 16), new Point2D(4, 0),
					new Point2D(12, 16), new Point2D(16, 0) };
			Point2Ds ds = new Point2Ds(pts);
			GeoLine geoLine = new GeoLine(ds);
			return geoLine;
		}
		if (type.equals(DatasetType.REGION) || type.equals(DatasetType.GRID)) {
			Point2D[] points = { new Point2D(1, 15), new Point2D(1, 5),
					new Point2D(10, 1), new Point2D(15, 5),
					new Point2D(15, 15), new Point2D(1, 15) };
			Point2Ds ds = new Point2Ds(points);
			GeoRegion geoRegion = new GeoRegion(ds);
			return geoRegion;
		}
		return null;
	}

}
