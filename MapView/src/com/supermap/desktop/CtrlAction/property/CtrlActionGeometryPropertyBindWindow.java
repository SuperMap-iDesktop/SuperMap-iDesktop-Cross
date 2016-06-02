package com.supermap.desktop.CtrlAction.property;

import java.awt.event.*;
import java.util.HashMap;

import javax.swing.JTable;

import net.infonode.util.Direction;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.event.*;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.docking.*;
import com.supermap.desktop.utilties.TabularUtilties;
import com.supermap.mapping.*;

public class CtrlActionGeometryPropertyBindWindow extends CtrlAction {
	private SplitWindow splitWindow;
	private IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
	private JTable tabularTable;
	private Map map;
	private IFormTabular tabular;
	private HashMap<Map, IFormTabular> tabularMap = new HashMap<Map, IFormTabular>();
	private DockingWindow newTabWindow;

	private MouseMotionListener listMouseMotionListener = new ListMouseMotionListener();
	private MouseListener listMouseListener = new ListMouseListener();
	private MouseAdapter tabularTableListener = new TabularTableListener();
	private MapDrawingListener mapDrawingListener;
	private ActiveFormChangedListener activeFormChangeListener = new LocalFormChangedListener();
	int tabSize = 0;// 属性表个数

	public CtrlActionGeometryPropertyBindWindow(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	@Override
	public void run() {
		try {
			if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
				//
				IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
				this.map = formMap.getMapControl().getMap();
				TabWindow tabWindow = ((DockbarManager) (Application.getActiveApplication().getMainFrame()).getDockbarManager()).getChildFormsWindow();
				//获取当前活动图层对应的数据集
				Dataset dataset = formMap.getActiveLayers()[0].getDataset();
				if (null != dataset && dataset instanceof DatasetVector) {
					Recordset recordset = ((DatasetVector)dataset).getRecordset(false, CursorType.DYNAMIC);
					// 打开一个默认的属性表，然后修改属性表的title和数据与当前图层对应的数据匹配
					this.tabular = TabularUtilties.openDatasetVectorFormTabular(dataset);
					this.tabular.setText(dataset.getName()+"@"+dataset.getDatasource().getAlias());
					this.tabular.setRecordset(recordset);
				}
				// 清空tabularMap保证一个map对应一个属性表
				this.tabularMap.clear();
				this.tabularMap.put(map, tabular);
				this.newTabWindow = tabWindow.getChildWindow(tabWindow.getChildWindowCount() - 1);
				this.tabSize += 1;
				if (null == splitWindow) {
					this.splitWindow = tabWindow.split(newTabWindow, Direction.RIGHT, 0.5f);
				} else if (splitWindow.getChildWindowCount() > 0) {
					((TabWindow) this.splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1)).addTab(newTabWindow);
				}
				this.tabularTable = tabular.getjTableTabular();
				this.mapDrawingListener = new LocalMapDrawingListener(tabular);
				registEvents();
				formMap.actived();
				this.formManager.addActiveFormChangedListener(this.activeFormChangeListener);
				this.newTabWindow.addListener(new DockingWindowAdapter() {

					@Override
					public void windowClosed(DockingWindow window) {
						// 当前属性表关闭时清空map
						tabularMap.clear();
						tabSize -= 1;
						if (0 == tabSize) {
							splitWindow = null;
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
		this.map.addDrawingListener(mapDrawingListener);
		this.tabularTable.addMouseListener(this.tabularTableListener);
		this.tabular.getRowHeader().addMouseMotionListener(this.listMouseMotionListener);
		this.tabular.getRowHeader().addMouseListener(this.listMouseListener);
	}

	private void removeEventes() {
		this.map.removeDrawingListener(mapDrawingListener);
		this.tabularTable.removeMouseListener(this.tabularTableListener);
		this.tabular.getRowHeader().removeMouseMotionListener(this.listMouseMotionListener);
		this.tabular.getRowHeader().removeMouseListener(this.listMouseListener);
	};

	private void queryTabularTable(MapDrawingEvent event, IFormTabular tabular) {
		// 地图选择集对应属性表
		if (event.getMap().getLayers().getCount() > 0 && null != event.getMap().getLayers().get(0) && null != event.getMap().getLayers().get(0).getSelection()) {
			Layer layer = event.getMap().getLayers().get(0);
			Selection selection = layer.getSelection();
			Recordset recordset = selection.toRecordset();
			int[] rows = new int[recordset.getAllFeatures().size()];
			int i = 0;
			recordset.moveFirst();
			while (!recordset.isEOF()) {
				rows[i] = recordset.getID() - 1;
				i++;
				recordset.moveNext();
			}
			if (isRightRows(rows, tabular.getRowCount())) {
				tabular.addRow(rows);
			}
			recordset.dispose();
		}

	}

	private boolean isRightRows(int[] rows, int tableRowCount) {
		boolean isRightRows = true;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] > tableRowCount) {
				isRightRows = false;
				break;
			}
		}
		return isRightRows;
	}

	private void queryMap() {
		int[] selectRows = tabularTable.getSelectedRows();
		int[] idRows = new int[selectRows.length];
		for (int i = 0; i < selectRows.length; i++) {
			idRows[i] = selectRows[i] + 1;
		}
		if (map.getLayers().getCount() > 0) {
			DatasetVector dataset = (DatasetVector) map.getLayers().get(0).getDataset();
			Recordset maxIdSet = dataset.query("SmId", CursorType.STATIC);
			int idCount = (int) maxIdSet.statistic("SmId", StatisticMode.MAX);
			if (!isRightRows(idRows, idCount)) {
				return;
			}
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
	}

	class TabularTableListener extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent e) {
			map.removeDrawingListener(mapDrawingListener);
			mapDrawingListener = null;
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

	class LocalFormChangedListener implements ActiveFormChangedListener {

		@Override
		public void activeFormChanged(ActiveFormChangedEvent e) {
			if (null == e.getNewActiveForm()) {
				// 当所有地图关闭时将splitWindow设置为空，重新关联,并移除事件
				splitWindow = null;
				tabularMap.clear();
				removeEventes();
				map.removeDrawingListener(mapDrawingListener);
				return;
			}
			if (null != tabularMap.get(map) && e.getNewActiveForm() instanceof IFormMap
					&& ((IFormMap) e.getNewActiveForm()).getMapControl().getMap().equals(map) && null != tabularMap.get(map) && null == mapDrawingListener) {
				// 活动窗口为IFormMap,且活动地图为当前地图时添加事件
				mapDrawingListener = new LocalMapDrawingListener(tabularMap.get(map));
				map.addDrawingListener(mapDrawingListener);
				return;
			}
			if (null != tabularMap.get(map) && e.getNewActiveForm() instanceof IFormTabular) {
				map.removeDrawingListener(mapDrawingListener);
				mapDrawingListener = null;
				return;
			}
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
