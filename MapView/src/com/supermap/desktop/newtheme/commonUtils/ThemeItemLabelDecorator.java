package com.supermap.desktop.newtheme.commonUtils;

import com.supermap.data.*;
import com.supermap.data.Toolkit;
import com.supermap.desktop.Application;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 专题图缩略图工厂类,用于显示专题图的缩略图
 * 
 * @author xie
 *
 */
public class ThemeItemLabelDecorator {

	private ThemeItemLabelDecorator() {
		// 工具类不提供构造函数
	}

	/**
	 * 通过给定的数据集和专题图项样式绘制图片
	 *
	 * @param dataset 给定的数据集
	 * @param geoStyle 专题图项样式
	 * @return 目标图片
	 */
	public static ImageIcon buildGeoStyleIcon(Dataset dataset, GeoStyle geoStyle) {
		ImageIcon geoStyleIcon = new ImageIcon();
		BufferedImage bufferedImage = new BufferedImage(100, 20, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		Geometry geometry = getGeometryByDatasetType(dataset.getType());
		geometry.setStyle(geoStyle);
		Resources resources = dataset.getDatasource().getWorkspace().getResources();
		Toolkit.draw(geometry, resources, graphics);
		geoStyleIcon.setImage(bufferedImage);
		return geoStyleIcon;
	}

	/**
	 * 根据给定的统计专题图子项geoStyle绘制图片
	 * 
	 * @param geoStyle
	 * @return
	 */
	public static ImageIcon buildGraphIcon(GeoStyle geoStyle) {
		ImageIcon geoStyleIcon = new ImageIcon();
		BufferedImage bufferedImage = new BufferedImage(100, 25, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();
		Point2D[] points = { new Point2D(0, 0), new Point2D(0, 25), new Point2D(100, 25), new Point2D(100, 0) };
		Point2Ds ds = new Point2Ds(points);
		GeoRegion geoRegion = new GeoRegion(ds);
		geoRegion.setStyle(geoStyle);
		Resources resources = Application.getActiveApplication().getWorkspace().getResources();
		Toolkit.draw(geoRegion, resources, graphics);
		geoStyleIcon.setImage(bufferedImage);
		return geoStyleIcon;
	}

	/**
	 * 通过给定的数据集和专题图项样式绘制图片
	 *
	 * @param dataset 给定的数据集
	 * @param color 指定颜色
	 * @return 目标图片
	 */
	public static ImageIcon buildColorIcon(Dataset dataset, Color color) {
		ImageIcon colorIcon = new ImageIcon();
		BufferedImage bufferedImage = new BufferedImage(100, 20, BufferedImage.TYPE_INT_ARGB);
		Geometry geometry = getGeometryByDatasetType(dataset.getType());
		Graphics graphics = bufferedImage.getGraphics();
		GeoStyle geoStyle = new GeoStyle();
		geoStyle.setFillBackColor(color);
		geoStyle.setFillForeColor(color);
		geoStyle.setLineColor(color);
		geometry.setStyle(geoStyle);
		Resources resources = dataset.getDatasource().getWorkspace().getResources();
		Toolkit.draw(geometry, resources, graphics);
		colorIcon.setImage(bufferedImage);
		return colorIcon;
	}

	/**
	 * 根据给定的数据集类型构建指定类型的GeoMetry用于绘制图片
	 * 
	 * @param type
	 * @return
	 */
	public static Geometry getGeometryByDatasetType(DatasetType type) {
		if (type.equals(DatasetType.POINT) || type.equals(DatasetType.POINT3D)) {
			GeoPoint geoPoint = new GeoPoint(50, 10);
			return geoPoint;
		}
		if (type.equals(DatasetType.LINE) || type.equals(DatasetType.LINE3D) || type.equals(DatasetType.LINEM) || type.equals(DatasetType.NETWORK)
				|| type.equals(DatasetType.NETWORK3D)) {
			Point2D[] pts = { new Point2D(0, 10), new Point2D(100, 10) };
			Point2Ds ds = new Point2Ds(pts);
			GeoLine geoLine = new GeoLine(ds);
			return geoLine;
		}
		if (type.equals(DatasetType.REGION) || type.equals(DatasetType.REGION3D)||type.equals(DatasetType.GRID)) {
			Point2D[] points = { new Point2D(0, 0), new Point2D(0, 20), new Point2D(100, 20), new Point2D(100, 0) };
			Point2Ds ds = new Point2Ds(points);
			GeoRegion geoRegion = new GeoRegion(ds);
			return geoRegion;
		}
		return null;
	}
}
