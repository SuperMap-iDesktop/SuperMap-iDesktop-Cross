package com.supermap.desktop.CtrlAction.property;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JTable;

import net.infonode.util.Direction;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Feature;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Recordset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.DockingWindowAdapter;
import com.supermap.desktop.ui.docking.SplitWindow;
import com.supermap.desktop.ui.docking.TabWindow;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.mapping.MapDrawingEvent;
import com.supermap.mapping.MapDrawingListener;
import com.supermap.mapping.MapOpenedEvent;
import com.supermap.mapping.MapOpenedListener;
import com.supermap.mapping.Selection;

public class CtrlActionGeometryPropertyBindWindow extends CtrlAction {
	private SplitWindow splitWindow;
	private IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
	private MouseMotionListener listMouseMotionListener = new ListMouseMotionListener();
	private MouseListener listMouseListener = new ListMouseListener();
	private MouseAdapter tabularTableListener = new TabularTableListener();
	private MapDrawingListener mapDrawingListener;

	private JTable tabularTable;
	private Map map;
	private IFormTabular tabular;
	private HashMap<Map, IFormTabular> tabularMap = new HashMap<Map, IFormTabular>();

	public CtrlActionGeometryPropertyBindWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				//
				IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
				map = formMap.getMapControl().getMap();
				IFormMain formMain = Application.getActiveApplication().getMainFrame();
				TabWindow tabWindow = ((DockbarManager) formMain.getDockbarManager()).getChildFormsWindow();
				DatasetVector activeDataset = (DatasetVector) Application.getActiveApplication().getActiveDatasets()[0];
				tabular = TabularUtilties.openDatasetVectorFormTabular(activeDataset);
				tabularMap.put(map, tabular);
				DockingWindow newTabWindow = tabWindow.getChildWindow(tabWindow.getChildWindowCount() - 1);
				if (null == splitWindow) {
					splitWindow = tabWindow.split(newTabWindow, Direction.RIGHT, 0.5f);
				} else if (splitWindow.getChildWindowCount() > 0) {
					// 注销事件保证单前地图关联当前的属性表
					((TabWindow) splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1)).addTab(newTabWindow);
				}
				tabularTable = tabular.getjTableTabular();
				this.mapDrawingListener = new LocalMapDrawingListener(tabular);

				// 注册事件
				registEvents();
				// // 焦点
				// formMap.actived();
				
				map.addMapOpenedListener(new MapOpenedListener() {
					
					@Override
					public void mapOpened(MapOpenedEvent arg0) {
						// 地图打开时销毁事件，保证当前地图只关联当前属性表
						removeEventes();
						tabularMap.clear();
					}
				});
				
				map.addMapClosedListener(new MapClosedListener() {

					@Override
					public void mapClosed(MapClosedEvent arg0) {
						// 地图关闭时销毁事件，保证当前地图只关联当前属性表
						removeEventes();
						tabularMap.clear();
					}
				});

				newTabWindow.addListener(new DockingWindowAdapter() {
					
					@Override
					public void windowClosed(DockingWindow window) {
						// 浮动窗口关闭时销毁事件，保证当前地图只关联当前属性表
						removeEventes();
						tabularMap.clear();
					}

				});

				formManager.addActiveFormChangedListener(new ActiveFormChangedListener() {

					@Override
					public void activeFormChanged(ActiveFormChangedEvent e) {
						if (null == e.getNewActiveForm()) {
							// 当所有地图关闭时将splitWindow设置为空，重新关联
							splitWindow = null;
						} else if (e.getNewActiveForm() instanceof IFormMap) {
							
							map = ((IFormMap) e.getNewActiveForm()).getMapControl().getMap();
							if (null != tabularMap.get(map)) {
								map.addDrawingListener(mapDrawingListener);
							}
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registEvents() {
		removeEventes();
		tabularTable.addMouseListener(this.tabularTableListener);
		map.addDrawingListener(this.mapDrawingListener);
		tabular.getRowHeader().addMouseMotionListener(this.listMouseMotionListener);
		tabular.getRowHeader().addMouseListener(this.listMouseListener);
	}

	private void removeEventes() {
		tabularTable.removeMouseListener(this.tabularTableListener);
		map.removeDrawingListener(this.mapDrawingListener);
		tabular.getRowHeader().removeMouseMotionListener(this.listMouseMotionListener);
		tabular.getRowHeader().removeMouseListener(this.listMouseListener);
	};

	private void queryTabularTable(MapDrawingEvent event, IFormTabular tabular) {
		// 地图选择集对应属性表
		if (event.getMap().getLayers().getCount() > 0 && null != event.getMap().getLayers().get(0)&& null != event.getMap().getLayers().get(0).getSelection()) {
			Layer layer = event.getMap().getLayers().get(0);
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
		} else if (null != geo) {
			Point2Ds points = new Point2Ds(new Point2D[] { new Point2D(geo.getBounds().getLeft(), geo.getBounds().getBottom()),
					new Point2D(geo.getBounds().getRight(), geo.getBounds().getTop()) });
			rectangle2d = new Rectangle2D(points.getItem(0), points.getItem(1));
		}
		Selection selection = new Selection();
		selection.fromRecordset(recordset);
		map.getLayers().get(0).setSelection(selection);
		map.setCenter(rectangle2d.getCenter());
		map.refresh();
		selection.clear();
		recordset.dispose();
	}

	class TabularTableListener extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent e) {
			map.removeDrawingListener(mapDrawingListener);
			queryMap();
		}

	}

	class ListMouseMotionListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			queryMap();
		}
	}

	class ListMouseListener extends MouseAdapter {
		// 属性表对应地图
		@Override
		public void mouseReleased(MouseEvent e) {
			map.removeDrawingListener(mapDrawingListener);
			queryMap();
		}
	}

	class LocalMapDrawingListener implements MapDrawingListener {
		private IFormTabular tabular;

		public LocalMapDrawingListener(IFormTabular tabular) {
			this.tabular = tabular;
		}

		@Override
		public void mapDrawing(MapDrawingEvent arg0) {
			queryTabularTable(arg0, tabular);
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

}
