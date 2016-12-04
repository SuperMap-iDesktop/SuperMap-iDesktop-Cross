package com.supermap.desktop.ui.controls;

import com.supermap.data.*;
import com.supermap.data.Toolkit;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图层节点装饰器
 *
 * @author xuzw
 */
class LayerNodeDecorator implements TreeNodeDecorator {
    @Override
    public void decorate(JLabel label, TreeNodeData data) {
        if (data.getType() == NodeDataType.LAYER && !((Layer) data.getData()).isDisposed()) {
            Layer layer = (Layer) data.getData();
            try {
                label.setText(layer.getCaption());
            } catch (Exception e) {
                // layer为空
                return;
            }
            Dataset dataset = layer.getDataset();
            if (dataset == null) {
                ImageIcon icon = (ImageIcon) label.getIcon();
                BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bufferedImage.getGraphics();
                graphics.drawImage(InternalImageIconFactory.DT_UNKNOWN.getImage(), 0, 0, label);
                icon.setImage(bufferedImage);
            } else {
                Resources resources = dataset.getDatasource().getWorkspace().getResources();
                ImageIcon icon = (ImageIcon) label.getIcon();
                BufferedImage bufferedImage = new BufferedImage(IMAGEICON_WIDTH, IMAGEICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = bufferedImage.getGraphics();
                if (dataset instanceof DatasetVector) {
                    DatasetVector datasetVector = (DatasetVector) dataset;
                    DatasetType type = datasetVector.getType();
                    GeoStyle geoStyle;
                    if (((DatasetVector) dataset).getParentDataset() != null) {
                        if (dataset.getType() == DatasetType.POINT) {
                            graphics.drawImage(getImage(DatasetType.NETWORK), 0, 0, label);
                        } else if (dataset.getType() == DatasetType.POINT3D) {
                            graphics.drawImage(getImage(DatasetType.NETWORK3D), 0, 0, label);
                        }
                    } else if (type == DatasetType.POINT || type == DatasetType.POINT3D) {
                        LayerSettingVector layerSettingVector = (LayerSettingVector) layer.getAdditionalSetting();
                        if (layerSettingVector == null) {
                            layerSettingVector = new LayerSettingVector();
                        }
                        geoStyle = layerSettingVector.getStyle();
                        GeoStyle geoStyle2 = geoStyle.clone();

                        SymbolMarkerLibrary library = resources.getMarkerLibrary();

                        int markerSymbolID = geoStyle2.getMarkerSymbolID();
                        if (markerSymbolID < 1) {
                            graphics.setColor(geoStyle2.getLineColor());
                            graphics.fillOval(IMAGEICON_WIDTH / 2 - 2, IMAGEICON_WIDTH / 2 - 2, 4, 4);
                        } else {
                            GeoPoint geoPoint = (GeoPoint) getGeometryByDatasetType(DatasetType.POINT);
                            // modify by zhaosy 2010-10-15
                            // 设置Size解决符号过大时无法显示的问题
                            geoStyle2.setMarkerSize(new Size2D(4, 4));
                            geoPoint.setStyle(geoStyle2);

                            Symbol symbol = library.findSymbol(geoStyle2.getMarkerSymbolID());

                            if (symbol instanceof SymbolMarker) {
                                SymbolMarker marker = (SymbolMarker) symbol;
                                Point point = marker.getOrigin();
                                int x = point.x * IMAGEICON_WIDTH / UIEnvironment.symbolPointMax;
                                int y = point.y * IMAGEICON_HEIGHT / UIEnvironment.symbolPointMax;

                                geoPoint.setX(x);
                                geoPoint.setY(y);
                            } else {
                                geoPoint.setX(IMAGEICON_WIDTH / 2.0);
                                geoPoint.setY(IMAGEICON_HEIGHT / 2.0);
                            }
                            InternalToolkitControl.internalDraw(geoPoint, resources, graphics);

                        }
                        icon.setImage(bufferedImage);
                    } else if (type == DatasetType.LINE || type == DatasetType.LINE3D) {
                        LayerSettingVector layerSettingVector = (LayerSettingVector) layer.getAdditionalSetting();
                        if (layerSettingVector == null) {
                            layerSettingVector = new LayerSettingVector();
                        }
                        geoStyle = layerSettingVector.getStyle();
                        GeoStyle geoStyle2 = geoStyle.clone();
                        if (geoStyle2.getLineWidth() > 0.2) {
                            geoStyle2.setLineWidth(0.2);
                        }
                        Geometry geometry = getGeometryByDatasetType(DatasetType.LINE);
                        geometry.setStyle(geoStyle2);
                        Toolkit.draw(geometry, resources, graphics);
                        icon.setImage(bufferedImage);
                    } else if (type == DatasetType.REGION || type == DatasetType.REGION3D) {
                        LayerSettingVector layerSettingVector = (LayerSettingVector) layer.getAdditionalSetting();
                        if (layerSettingVector == null) {
                            layerSettingVector = new LayerSettingVector();
                        }
                        geoStyle = layerSettingVector.getStyle();
                        GeoStyle geoStyle2 = geoStyle.clone();
                        if (geoStyle2.getLineWidth() > 0.2) {
                            geoStyle2.setLineWidth(0.2);
                        }
                        Geometry geometry = getGeometryByDatasetType(DatasetType.REGION);
                        geometry.setStyle(geoStyle2);
                        Toolkit.draw(geometry, resources, graphics);
                        icon.setImage(bufferedImage);
                    } else if (type.equals(DatasetType.CAD)) {
                        graphics.drawImage(getImage(DatasetType.CAD), 0, 0, label);
                    } else if (type.equals(DatasetType.LINEM)) {
                        graphics.drawImage(getImage(DatasetType.LINEM), 0, 0, label);
                    } else if (type.equals(DatasetType.LINKTABLE)) {
                        graphics.drawImage(getImage(DatasetType.LINKTABLE), 0, 0, label);
                    } else if (type.equals(DatasetType.NETWORK)) {
                        graphics.drawImage(getImage(DatasetType.NETWORK), 0, 0, label);
                    } else if (type.equals(DatasetType.TABULAR)) {
                        graphics.drawImage(getImage(DatasetType.TABULAR), 0, 0, label);
                    } else if (type.equals(DatasetType.TEXT)) {
                        graphics.drawImage(getImage(DatasetType.TEXT), 0, 0, label);
                    } else if (type.equals(DatasetType.PARAMETRICLINE)) {
                        graphics.drawImage(getImage(DatasetType.PARAMETRICLINE), 0, 0, label);
                    } else if (type.equals(DatasetType.PARAMETRICREGION)) {
                        graphics.drawImage(getImage(DatasetType.PARAMETRICREGION), 0, 0, label);
                    } else if (type == DatasetType.NETWORK3D) {
                        graphics.drawImage(getImage(DatasetType.NETWORK3D), 0, 0, label);
                    } else if (type == DatasetType.MODEL) {
                        graphics.drawImage(getImage(DatasetType.MODEL), 0, 0, label);
                    } else if (type == DatasetType.VOLUME) {
                        graphics.drawImage(getImage(DatasetType.VOLUME), 0, 0, label);
                    }
                } else if (dataset instanceof DatasetTopology) {
                    graphics.drawImage(InternalImageIconFactory.DT_TOPOLOGY.getImage(), 0, 0, label);
                } else if (dataset instanceof DatasetVolume) {
                    graphics.drawImage(getImage(DatasetType.VOLUME), 0, 0, label);

                }
                icon.setImage(bufferedImage);
            }
        }

    }

    private Image getImage(DatasetType cad) {
        return ((ImageIcon) ControlsResources.getIcon(CommonToolkit.DatasetImageWrap.getImageIconPath(cad))).getImage();
    }

    private Geometry getGeometryByDatasetType(DatasetType type) {
        if (type.equals(DatasetType.POINT)) {
            GeoPoint geoPoint = new GeoPoint();
            return geoPoint;
        }
        if (type.equals(DatasetType.LINE)) {
            Point2D[] pts = {new Point2D(0, 0), new Point2D(16, 16)};//, new Point2D(12, 16), new Point2D(16, 0)
            Point2Ds ds = new Point2Ds(pts);
            GeoLine geoLine = new GeoLine(ds);
            return geoLine;
        }
        if (type.equals(DatasetType.REGION)/* || type.equals(DatasetType.GRID) */) {
            Point2D[] points = {new Point2D(1, 15), new Point2D(1, 5), new Point2D(10, 1), new Point2D(15, 5), new Point2D(15, 15), new Point2D(1, 15)};
            Point2Ds ds = new Point2Ds(points);
            GeoRegion geoRegion = new GeoRegion(ds);
            return geoRegion;
        }
        return null;
    }

}
