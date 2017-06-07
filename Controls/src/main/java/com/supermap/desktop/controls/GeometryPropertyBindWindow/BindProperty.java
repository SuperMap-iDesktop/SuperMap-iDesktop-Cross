package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Dataset;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapDrawingEvent;
import com.supermap.mapping.MapDrawingListener;
import com.supermap.mapping.Selection;
import com.supermap.ui.*;

import java.util.Vector;

public class BindProperty implements IBindProperty {

    private MapControl mapControl;
    private Map map;
    private MapDrawingListener mapDrawingListener;
    private Vector<PropertySelectChangeListener> propertySelectChangeListener;
    private GeometrySelectChangedListener geometrySelectChangedListener;
    private MapDrawingListener drawingListener;

    public BindProperty(MapControl mapControl) {
        this.mapControl = mapControl;
        this.map = mapControl.getMap();
    }

    private void registEvents() {
        this.geometrySelectChangedListener = new GeometrySelectChangedListener() {

            @Override
            public void geometrySelectChanged(GeometrySelectChangedEvent arg0) {
                queryTabularTable();
            }
        };
        removeEvents();
        this.mapControl.addGeometrySelectChangedListener(geometrySelectChangedListener);
    }

    private void queryTabularTable() {
        // 地图选择集对应属性表
        if (this.map.findSelection(true).length > 0) {
            Recordset tempRecordset = this.map.findSelection(true)[0].toRecordset();
            int[] rows = new int[tempRecordset.getAllFeatures().size()];
            int i = 0;
            tempRecordset.moveFirst();
            while (!tempRecordset.isEOF()) {
                rows[i] = tempRecordset.getID();
                i++;
                tempRecordset.moveNext();
            }
            if (rows.length > 0) {
                firePropertySelectChanged(rows, tempRecordset.getDataset());
            }
            tempRecordset.dispose();
        } else {
            firePropertySelectChanged(new int[0], null);
        }

    }

    @Override
    public void refreshMap(Selection selection, Layer layer) {
        Selection tempSelection = selection;
        Recordset recordset = tempSelection.toRecordset();
        Geometry geo = recordset.getGeometry();
        Rectangle2D rectangle2d = Rectangle2D.getEMPTY();
        if (map.isDynamicProjection()) {
            // 当前地图窗口中地图的投影信息与数据源的投影信息不同时，利用地图动态投影显示可以将当前地图的投影信息转换为数据源的投影信息
            unionRectangle(recordset, map.getPrjCoordSys(), rectangle2d);
        } else if (null != geo) {
            Point2Ds points = new Point2Ds(new Point2D[]{new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
                    new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop())});
            rectangle2d = new Rectangle2D(points.getItem(0), points.getItem(1));
        } else {
	        rectangle2d = null;
        }
        if (null != map.getLayers().get(layer.getName())) {
            map.getLayers().get(layer.getName()).setSelection(tempSelection);
	        if (rectangle2d != null) {
		        map.setCenter(rectangle2d.getCenter());
	        }
            map.refresh();
        }
    }

    private void unionRectangle(Recordset recordset, PrjCoordSys prjCoordSys, Rectangle2D rectangle) {
        // 用包含此矩形与指定矩形并集的最小矩形替换此矩形。
        recordset.moveFirst();
        if (prjCoordSys != null) {
            while (!recordset.isEOF()) {
                Geometry geo = recordset.getGeometry();
                if (geo != null) {
                    Point2Ds points = new Point2Ds(new Point2D[]{new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
                            new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop())});
                    CoordSysTranslator.convert(points, recordset.getDataset().getPrjCoordSys(), prjCoordSys, new CoordSysTransParameter(),
                            CoordSysTransMethod.MTH_COORDINATE_FRAME);
                    if (rectangle.equals(Rectangle2D.getEMPTY())) {
                        rectangle = new Rectangle2D(points.getItem(0), points.getItem(1));
                    } else {
                        rectangle.union(new Rectangle2D(points.getItem(0), points.getItem(1)));
                    }

                    geo.dispose();
                    recordset.moveNext();
                }
            }
        } else {
            while (!recordset.isEOF()) {
                Geometry geo = recordset.getGeometry();
                if (geo != null) {
                    if (rectangle.equals(Rectangle2D.getEMPTY())) {
                        rectangle = geo.getBounds();
                    } else {
                        rectangle.union(geo.getBounds());
                    }
                    geo.dispose();
                    recordset.moveNext();
                }
            }
        }
    }

    @Override
    public void removeEvents() {
        this.map.removeDrawingListener(this.drawingListener);
        this.mapControl.removeGeometrySelectChangedListener(geometrySelectChangedListener);
    }

    @Override
    public synchronized void addPropertySelectChangeListener(PropertySelectChangeListener l) {
        if (null == propertySelectChangeListener) {
            propertySelectChangeListener = new Vector<PropertySelectChangeListener>();
        }
        if (!propertySelectChangeListener.contains(l)) {
            propertySelectChangeListener.add(l);
        }
        registEvents();
    }

    @Override
    public synchronized void removePropertySelectChangeListener(PropertySelectChangeListener l) {
        if (null != propertySelectChangeListener && propertySelectChangeListener.contains(l)) {
            propertySelectChangeListener.remove(l);
        }
        removeEvents();
    }

    @Override
    public void firePropertySelectChanged(int[] rows, Dataset dataset) {
        if (null != propertySelectChangeListener) {
            Vector<PropertySelectChangeListener> listeners = propertySelectChangeListener;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                listeners.elementAt(i).selectChanged(rows, dataset);
            }
        }
    }

    @Override
    public void dispose() {
        removeEvents();
        this.mapControl = null;
        this.mapDrawingListener = null;
    }

}
