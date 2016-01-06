package com.supermap.desktop.ui.controls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.GeoStyle3D;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Resources;
import com.supermap.data.Size2D;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolMarkerLibrary;
import com.supermap.data.Toolkit;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DSettingVector;
import com.supermap.realspace.Theme3D;
import com.supermap.realspace.Theme3DType;

/**
 * 3D数据集图层节点装饰器
 * @author xuzw
 *
 */
class Layer3DDatasetNodeDecorator implements TreeNodeDecorator {

	public void decorate(JLabel label, TreeNodeData data) {
		if (data.getType().equals(NodeDataType.LAYER3D_DATASET)) {
			Layer3DDataset layer3DDataset = (Layer3DDataset) data.getData();
			label.setText(layer3DDataset.getCaption());
			Dataset dataset = layer3DDataset.getDataset();
			if (dataset == null) {
				ImageIcon icon = (ImageIcon) label.getIcon();
				BufferedImage bufferedImage = new BufferedImage(
						IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
						BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bufferedImage.getGraphics();
				graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN
						.getImage(), 0, 0, label);
				icon.setImage(bufferedImage);
			} else {
				Resources resources = dataset.getDatasource().getWorkspace()
						.getResources();
				if (layer3DDataset.getTheme() != null) {
					Theme3D theme3D = layer3DDataset.getTheme();
					ImageIcon icon = (ImageIcon) label.getIcon();
					BufferedImage bufferedImage = new BufferedImage(
							IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
							BufferedImage.TYPE_INT_ARGB);
					Graphics graphics = bufferedImage.getGraphics();
					Theme3DType type = theme3D.getType();
					if (type.equals(Theme3DType.LABEL3D)) {
						graphics.drawImage(
								InternalImageIconFactory.THEME3D_LABEL
										.getImage(), 0, 0, label);
					} else if (type.equals(Theme3DType.RANGE3D)) {
						graphics.drawImage(
								InternalImageIconFactory.THEME3D_RANGE
										.getImage(), 0, 0, label);
					} else if (type.equals(Theme3DType.UNIQUE3D)) {
						graphics.drawImage(
								InternalImageIconFactory.THEME3D_UNIQUE
										.getImage(), 0, 0, label);
					}
					icon.setImage(bufferedImage);
				} else {
					if (dataset instanceof DatasetVector) {
						DatasetVector datasetVector = (DatasetVector) dataset;
						ImageIcon icon = (ImageIcon) label.getIcon();
						BufferedImage bufferedImage = new BufferedImage(
								IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
								BufferedImage.TYPE_INT_ARGB);
						Graphics graphics = bufferedImage.getGraphics();
						DatasetType type = datasetVector.getType();
						GeoStyle3D geoStyle;
						if (type.equals(DatasetType.POINT)
								|| type.equals(DatasetType.POINT3D)) {
							Layer3DSettingVector layersetting = (Layer3DSettingVector) layer3DDataset
									.getAdditionalSetting();
							if (layersetting == null) {
								layersetting = new Layer3DSettingVector();
							}
							geoStyle = layersetting.getStyle();
							GeoStyle geoStyle2 = geoStyle3D2GeoStyle(geoStyle);
							Geometry geometry = getGeometryByDatasetType(type);
							geometry.setStyle(geoStyle2);
							SymbolMarkerLibrary library = resources
									.getMarkerLibrary();
							int markerSymbolID = geoStyle2.getMarkerSymbolID();
							if (markerSymbolID < 1) {
								graphics.setColor(geoStyle2.getLineColor());
								graphics.fillOval(IMAGEICON_WIDTH / 2 - 2,
										IMAGEICON_WIDTH / 2 - 2, 4, 4);
							} else {
								Symbol findSymbol = library
										.findSymbol(markerSymbolID);
								findSymbol.draw(graphics, geometry);
							}
							icon.setImage(bufferedImage);
						}
						if (type.equals(DatasetType.LINE)
								|| type.equals(DatasetType.LINE3D)) {
							Layer3DSettingVector layersetting = (Layer3DSettingVector) layer3DDataset
									.getAdditionalSetting();
							if (layersetting == null) {
								layersetting = new Layer3DSettingVector();
							}
							geoStyle = layersetting.getStyle();
							GeoStyle geoStyle2 = geoStyle3D2GeoStyle(geoStyle);
							geoStyle2.setLineWidth(0.2);
							Geometry geometry = getGeometryByDatasetType(type);
							geometry.setStyle(geoStyle2);
							Toolkit.draw(geometry, resources, graphics);
							icon.setImage(bufferedImage);
						}
						if (type.equals(DatasetType.REGION)
								|| type.equals(DatasetType.REGION3D)) {
							Layer3DSettingVector layerSettingVector = (Layer3DSettingVector) layer3DDataset
									.getAdditionalSetting();
							if (layerSettingVector == null) {
								layerSettingVector = new Layer3DSettingVector();
							}
							geoStyle = layerSettingVector.getStyle();
							GeoStyle geoStyle2 = geoStyle3D2GeoStyle(geoStyle);
							Geometry geometry = getGeometryByDatasetType(type);
							geometry.setStyle(geoStyle2);
							Toolkit.draw(geometry, resources, graphics);
							icon.setImage(bufferedImage);
						} else if (type.equals(DatasetType.CAD)) {
							graphics.drawImage(InternalImageIconFactory.DT_CAD
									.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.LINEM)) {
							graphics.drawImage(
									InternalImageIconFactory.DT_LINEM
											.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.LINKTABLE)) {
							graphics.drawImage(
									InternalImageIconFactory.DT_LINKTABLE
											.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.NETWORK)) {
							graphics.drawImage(
									InternalImageIconFactory.DT_NETWORK
											.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.TABULAR)) {
							graphics.drawImage(
									InternalImageIconFactory.DT_TABULAR
											.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.TEXT)) {
							graphics.drawImage(InternalImageIconFactory.DT_TEXT
									.getImage(), 0, 0, label);
						}
						icon.setImage(bufferedImage);
					} else if (dataset instanceof DatasetImage) {
						DatasetImage datasetImage = (DatasetImage) dataset;
						DatasetType type = datasetImage.getType();
						ImageIcon icon = (ImageIcon) label.getIcon();
						BufferedImage bufferedImage = new BufferedImage(
								IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
								BufferedImage.TYPE_INT_ARGB);
						Graphics graphics = bufferedImage.getGraphics();
						if (type.equals(DatasetType.IMAGE)) {
							graphics.drawImage(
									InternalImageIconFactory.DT_IMAGE
											.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.WCS)) {
							graphics.drawImage(InternalImageIconFactory.DT_WCS
									.getImage(), 0, 0, label);
						} else if (type.equals(DatasetType.WMS)) {
							graphics.drawImage(InternalImageIconFactory.DT_WMS
									.getImage(), 0, 0, label);
						}
						icon.setImage(bufferedImage);
					} else if (dataset instanceof DatasetGrid) {
						ImageIcon icon = (ImageIcon) label.getIcon();
						BufferedImage bufferedImage = new BufferedImage(
								IMAGEICON_WIDTH, IMAGEICON_HEIGHT,
								BufferedImage.TYPE_INT_ARGB);
						Graphics graphics = bufferedImage.getGraphics();
						graphics.drawImage(InternalImageIconFactory.DT_GRID
								.getImage(), 0, 0, label);
						icon.setImage(bufferedImage);
					}
				}
			}
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
		} else {
			geoStyle.setLineWidth(style3D.getLineWidth());
		}
		geoStyle.setMarkerSize(new Size2D(style3D.getMarkerSize(), style3D
				.getMarkerSize()));
		return geoStyle;
	}

}
