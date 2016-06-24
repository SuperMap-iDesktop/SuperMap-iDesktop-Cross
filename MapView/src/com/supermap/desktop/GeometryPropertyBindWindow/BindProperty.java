package com.supermap.desktop.GeometryPropertyBindWindow;

import java.util.Vector;

import com.supermap.data.*;
import com.supermap.mapping.*;

public class BindProperty implements IBindProperty {

	private Map map;
	private MapDrawingListener mapDrawingListener;
	private boolean selectRowChange;
	private Vector<PropertySelectChangeListener> propertySelectChangeListener;

	public BindProperty(Map map) {
		this.map = map;
		registEvents();
	}

	private void registEvents() {
		this.mapDrawingListener = new MapDrawingListener() {

			@Override
			public void mapDrawing(MapDrawingEvent arg0) {
				if (arg0.getMap().equals(map)) {
					queryTabularTable();
				}
			}
		};
		removeEvents();
		this.map.addDrawingListener(this.mapDrawingListener);
	}

	private void queryTabularTable() {
		// 地图选择集对应属性表
		if (this.map.findSelection(true).length > 0) {
			Recordset recordset = this.map.findSelection(true)[0].toRecordset();
			int[] rows = new int[recordset.getAllFeatures().size()];
			int i = 0;
			recordset.moveFirst();
			while (!recordset.isEOF()) {
				rows[i] = recordset.getID();
				i++;
				recordset.moveNext();
			}
			if (rows.length > 0) {
				firePropertySelectChanged(rows,recordset.getDataset());
			}
			recordset.dispose();
		}

	}

	@Override
	public void refreshMap(Selection selection,Layer layer) {
		Selection tempSelection = selection;
		Recordset recordset = tempSelection.toRecordset();
		Geometry geo = recordset.getGeometry();
		Rectangle2D rectangle2d = Rectangle2D.getEMPTY();
		if (map.isDynamicProjection()) {
			// 当前地图窗口中地图的投影信息与数据源的投影信息不同时，利用地图动态投影显示可以将当前地图的投影信息转换为数据源的投影信息
			unionRectangle(recordset, map.getPrjCoordSys(), rectangle2d);
		} else if (null != geo) {
			Point2Ds points = new Point2Ds(new Point2D[] { new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
					new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop()) });
			rectangle2d = new Rectangle2D(points.getItem(0), points.getItem(1));
		}
		if (null!=map.getLayers().get(layer.getName())) {
			map.getLayers().get(layer.getName()).setSelection(tempSelection);
			map.setCenter(rectangle2d.getCenter());
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

					Point2Ds points = new Point2Ds(new Point2D[] { new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
							new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop()) });
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
		this.map.removeDrawingListener(this.mapDrawingListener);
	}

	public boolean isSelectRowChange() {
		return selectRowChange;
	}

	@Override
	public synchronized void addPropertySelectChangeListener(PropertySelectChangeListener l) {
		if (null == propertySelectChangeListener) {
			propertySelectChangeListener = new Vector<PropertySelectChangeListener>();
		}
		if (!propertySelectChangeListener.contains(l)) {
			propertySelectChangeListener.add(l);
		}
	}

	@Override
	public synchronized void removePropertySelectChangeListener(PropertySelectChangeListener l) {
		if (null != propertySelectChangeListener && propertySelectChangeListener.contains(l)) {
			propertySelectChangeListener.remove(l);
		}
	}

	@Override
	public void firePropertySelectChanged(int[] rows,Dataset dataset) {
		if (null != propertySelectChangeListener) {
			Vector<PropertySelectChangeListener> listeners = propertySelectChangeListener;
			int count = listeners.size();
			for (int i = 0; i < count; i++) {
				listeners.elementAt(i).selectChanged(rows,dataset);
			}
		}
	}

	@Override
	public void dispose() {
		removeEvents();
		this.map = null;
		this.mapDrawingListener = null;
	}

}
