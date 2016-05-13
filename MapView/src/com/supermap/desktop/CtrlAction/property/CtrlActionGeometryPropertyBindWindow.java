package com.supermap.desktop.CtrlAction.property;

import java.awt.event.*;
import java.util.Iterator;

import javax.swing.JTable;

import net.infonode.util.Direction;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.*;

public class CtrlActionGeometryPropertyBindWindow extends CtrlAction {
	private IFormTabular tabular;
	private JTable tabularTable;
	private Map map;
	private Layer layer;

	public CtrlActionGeometryPropertyBindWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			IFormMap formMap = null;
			if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
				map = formMap.getMapControl().getMap();
				IFormMain formMain = Application.getActiveApplication().getMainFrame();
				TabWindow tabWindow = ((DockbarManager) formMain.getDockbarManager()).getChildFormsWindow();
				DatasetVector activeDataset = (DatasetVector) Application.getActiveApplication().getActiveDatasets()[0];
				if (null != activeDataset) {
					tabular = TabularUtilties.openDatasetVectorFormTabular(activeDataset);
				}

				tabWindow.split(tabWindow.getChildWindow(tabWindow.getChildWindowCount() - 1), Direction.RIGHT, 0.5f);

				layer = map.getLayers().get(0);
				map.addDrawingListener(new MapDrawingListener() {

					@Override
					public void mapDrawing(MapDrawingEvent arg0) {
						queryTabularTable();
					}

				});
				tabularTable = tabular.getjTableTabular();
				tabularTable.addMouseListener(new MouseAdapter() {
					// 属性表对应地图
					@Override
					public void mouseReleased(MouseEvent e) {
						queryMap();
					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void queryTabularTable() {
		// 地图选择集对应属性表
		if (null != layer.getSelection()) {
			Selection selection = layer.getSelection();
			Recordset recordset = selection.toRecordset();
			java.util.Map<Integer, Feature> featureMap = recordset.getAllFeatures();
			int[] rows = new int[featureMap.size()];
			Iterator<?> iterator = featureMap.entrySet().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) iterator.next();
				rows[i] = ((Feature) entry.getValue()).getID() - 1;
				i++;
			}
			tabular.addRow(rows);
			recordset.dispose();
		}

	}

	private void queryMap() {
		int[] selectRows = tabularTable.getSelectedRows();
		int[] idRows = new int[selectRows.length];
		for (int i = 0; i < selectRows.length; i++) {
			idRows[i] = selectRows[i] + 1;
		}
		DatasetVector dataset = (DatasetVector) map.getLayers().get(0).getDataset();
		Recordset recordset = dataset.query(idRows, CursorType.STATIC);
		Geometry geo = recordset.getGeometry();
		Rectangle2D rectangle2d = Rectangle2D.getEMPTY();
		if (map.isDynamicProjection()) {
			// 当前地图窗口中地图的投影信息与数据源的投影信息不同时，利用地图动态投影显示可以将当前地图的投影信息转换为数据源的投影信息
			unionRectangle(recordset, map.getPrjCoordSys(), rectangle2d);
		} else {
			Point2Ds points = new Point2Ds(new Point2D[] { new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
					new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop()) });
			rectangle2d = new Rectangle2D(points.getItem(0), points.getItem(1));
		}
		Selection selection = new Selection();
		selection.fromRecordset(recordset);
		layer.setSelection(selection);
		map.setCenter(rectangle2d.getCenter());
		map.refresh();
		selection.clear();
		recordset.dispose();
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

}
