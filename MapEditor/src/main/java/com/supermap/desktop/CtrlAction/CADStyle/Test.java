package com.supermap.desktop.CtrlAction.CADStyle;

import com.supermap.data.*;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Test {
    public void geoStyleTest() {
        // 假设打开一个工作空间 workspace 对象。
        // 实例化一个线几何对象，并对其进行风格设置
        GeoStyle geoStyle_L = new GeoStyle();
        geoStyle_L.setLineColor(java.awt.Color.black);
        geoStyle_L.setLineSymbolID(1);
        geoStyle_L.setLineWidth(5.0);
        Point2Ds point2Ds = new Point2Ds();
        Point2D[] point2DArray = {new Point2D(100, 100), new Point2D(200, 100),
                new Point2D(100, 200), new Point2D(200, 200)};
        point2Ds.addRange(point2DArray);
        GeoLine geoLine = new GeoLine(point2Ds);
        geoLine.setStyle(geoStyle_L);

        // 实例化一个点几何对象，并对其进行风格设置
        GeoStyle geoStyle_P = new GeoStyle();
        geoStyle_P.setMarkerAngle(14.0);
        geoStyle_P.setMarkerSize(new Size2D(10, 10));
        geoStyle_P.setMarkerSymbolID(1);
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setX(100.0);
        geoPoint.setY(100.0);
        geoPoint.setStyle(geoStyle_P);

        // 实例化一个面几何对象，并对其进行渐变风格设置
        GeoStyle geoStyle_R = new GeoStyle();
//        geoStyle_R.setFillBackColor(java.awt.Color.green);
//        geoStyle_R.setFillForeColor(java.awt.Color.red);
//        geoStyle_R.setFillBackOpaque(true);
//        geoStyle_R.setFillOpaqueRate(50);
//        geoStyle_R.setFillGradientAngle(30.0);
//        geoStyle_R.setFillGradientMode(FillGradientMode.LINEAR);
        geoStyle_R.setFillSymbolID(5);
        GeoRegion geoRegion = new GeoRegion();
        Point2Ds point2Ds_R = new Point2Ds();
        Point2D[] point2DArray_R = {new Point2D(10, 50), new Point2D(150, 50),
                new Point2D(100, 150), new Point2D(10, 150)};
        point2Ds_R.addRange(point2DArray_R);
        geoRegion.addPart(point2Ds_R);
        geoRegion.setStyle(geoStyle_R);

        // 打开工作空间，取出工作空间中名为“World”的数据集，查询其描述信息
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo workspaceConnectionInfo = new
                WorkspaceConnectionInfo();
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
        String file = "F:/SampleData711/City/Jingjin.smwu";
        workspaceConnectionInfo.setServer(file);
        workspace.open(workspaceConnectionInfo);

        // 出图查看所设置的风格
//        Map map = new Map(workspace);
//        TrackingLayer layer = map.getTrackingLayer();
//        layer.add(geoLine, "Line");
//        layer.add(geoPoint, "Point");
//        layer.add(geoRegion, "Region");
//        map.setAntialias(true);
//        map.setViewBounds(new Rectangle2D(0, 0, 250, 250));
//        map.outputMapToJPG("G:");
        JFrame frame = new JFrame();
        MapControl mapControl = new MapControl(workspace);
        mapControl.getMap().getTrackingLayer().add(geoLine, "Line");
        mapControl.getMap().getTrackingLayer().add(geoPoint, "Point");
        mapControl.getMap().getTrackingLayer().add(geoRegion, "Region");
        frame.add(mapControl);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void geometryTest() {
        // 假设打开一个工作空间 workspace 对象，工作空间中存在一个数据源 datasource 对象
        // 从数据源中取出一个数据集 dataset
        // 取出数据集中 SmID=53 的记录，返回其对应的几何对象
        // 打开工作空间，取出工作空间中名为“World”的数据集，查询其描述信息
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo workspaceConnectionInfo = new
                WorkspaceConnectionInfo();
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
        String file = "F:/SampleData711/City/Jingjin.smwu";
        workspaceConnectionInfo.setServer(file);
        workspace.open(workspaceConnectionInfo);
        DatasetVector dataset = (DatasetVector) workspace.getDatasources().get("Jingjin").getDatasets().get(
                "Lake_R");
        Recordset recordset = dataset.query("SmID=2", CursorType.STATIC);
        Geometry geometry = recordset.getGeometry();

        // 判断几何对象是否为空，不为空时依次对几何对象进行重构、设置样式、平移、对称、旋转以及缩放操作
        if (!geometry.isEmpty()) {
            geometry.fromXML(geometry.toXML());
            System.out.println(geometry.getStyle());
            GeoStyle geoStyle = new GeoStyle();
            geoStyle.setFillSymbolID(12);
            geoStyle.setFillForeColor(Color.red);
            geometry.setStyle(geoStyle);
            geometry.offset(100, 100);
            Rectangle2D rectangle2D = geometry.getBounds();
            geometry.mirror(geometry.getInnerPoint(), new Point2D(rectangle2D
                    .getBottom(), rectangle2D.getRight()));
            geometry.rotate(geometry.getInnerPoint(), 30);
            geometry.resize(new Rectangle2D(0, 0, 1024, 1024));
        }
        recordset.setGeometry(geometry);
        System.out.println(11);
        // 释放资源
//        recordset.dispose();
//        dataset.close();
//        geometry.dispose();
        JFrame frame = new JFrame();
        MapControl mapControl = new MapControl(workspace);
        mapControl.getMap().getLayers().add(dataset, true);
        frame.add(mapControl);
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        Test test = new Test();
        test.geometryTest();
    }

}
