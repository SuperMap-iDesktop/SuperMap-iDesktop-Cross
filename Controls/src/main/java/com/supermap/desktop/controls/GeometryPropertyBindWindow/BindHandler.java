package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.event.*;
import com.supermap.desktop.ui.FormManager;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.*;
import com.supermap.ui.GeometrySelectChangedEvent;
import com.supermap.ui.GeometrySelectChangedListener;
import com.supermap.ui.MapControl;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xie on 2016/11/30.
 * 关联关系建立的基本原则
 * IFormScene暂时不考虑
 * IFormMap之间关联，没有相同图层的IFormMap之间只实现地图的联动，没有选择集的变化
 * 有相同图层的IFormMap之间地图的联动和选择集的变化
 * IFormTabular之间关联，有相同recordset的IFormTabular之间实现选择行的联动IFormMap
 * IFormMap和IFormTabular之间的关联，有相同选择集的需要实现选择IFormMap的geometory时
 * IFormTabular有选择行变化，IFormTabular上选择行变化时，IFormMap的选择集有相应的变化
 */
public class BindHandler {
	private List<IForm> formMapList;
	private List<IForm> formTabularList;
	private static final String TAG_MOVE = "MOVE";
	private static final int MARKET_WIDTH = 128;
	private static volatile BindHandler bindHandler;

	private MouseListener mapControlMouseListener = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				bindMapCenter(formMapList.size(), e);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			int size = formMapList.size();
			for (int j = 0; j < size; j++) {
				IForm formMap = (IForm) formMapList.get(j);
				Map map;
				if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl()) {
					map = ((IFormMap) formMap).getMapControl().getMap();
					MapUtilities.clearTrackingObjects(map, TAG_MOVE);
					map.refreshTrackingLayer();
				}
			}
		}
	};
	private MouseWheelListener mapControlMouseWheelListener = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			bindMapCenterAndScale(formMapList.size(), ((MapControl) e.getSource()).getMap());
		}
	};
	private MouseMotionListener mapControlMouseMotionListener = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			bindMapsMousePosition(formMapList.size(), e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			bindMapsMousePosition(formMapList.size(), e);
		}
	};
	private MapDrawingListener mapDrawingListener = new MapDrawingListener() {
		@Override
		public void mapDrawing(MapDrawingEvent e) {
			bindMapCenterAndScale(formMapList.size(), (Map) e.getSource());
		}
	};
	private GeometrySelectChangedListener geoMetroySelectChangeListener;
	private MapDrawnListener mapDrawnListener = new MapDrawnListener() {
		@Override
		public void mapDrawn(MapDrawnEvent mapDrawnEvent) {
			mapDrawnEvent.getMap().refresh();
		}
	};
	private MouseListener listMouseListener;
	private ArrayList<PropertyBindWindow> propertyBindWindows;
	private GeometrySelectChangedListener geoMetroyMapSelectChangeListener;
	private KeyListener tabularTableKeyListener;
	public static FormManager manager = (FormManager) Application.getActiveApplication().getMainFrame().getFormManager();

	private FormClosingListener formClosingListener = new FormClosingListener() {
		@Override
		public void formClosing(FormClosingEvent e) {
			try {
				if (BindHandler.this.formMapList.contains(e.getForm())) {
					removeFormMapBind(e.getForm());
				}

				if (BindHandler.this.formTabularList.contains(e.getForm())) {
					removeFormTabularBind(e.getForm());
				}

				// 当子窗口关闭到小于两个的时候，已经不再能够关联了
				if (BindHandler.this.formMapList.size() + BindHandler.this.formTabularList.size() <= 1) {
					removeFormMapsBind();
					removeFormTabularsBind();
					removeFormMapsAndFormTabularsBind();
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	};

	public static synchronized BindHandler getInstance() {
		if (null == bindHandler) {
			bindHandler = new BindHandler();
		}
		return bindHandler;
	}

	private BindHandler() {
		this.formMapList = new ArrayList();
		this.formTabularList = new ArrayList();
		registEvents();
	}

	private void registEvents() {
		removeEvents();
		manager.addFormClosingListener(this.formClosingListener);
	}

	public void removeEvents() {
		manager.removeFormClosingListener(this.formClosingListener);
	}

	//属性表之间关联
	private void bindTabularsSelectRow(IFormTabular formTabular) {
		int size = formTabularList.size();
		int[] selectRows = formTabular.getjTableTabular().getSelectedRows();
		int rowSize = selectRows.length;
		List<Integer> tempRows = new ArrayList();
		for (int i = 0; i < rowSize; i++) {
			tempRows.add(selectRows[i]);
		}
		for (int i = 0; i < size; i++) {
			IFormTabular tempFormTabular = (IFormTabular) formTabularList.get(i);
			if (!formTabular.equals(tempFormTabular) && formTabular.getRecordset().getDataset().equals(tempFormTabular.getRecordset().getDataset())) {
				tempFormTabular.addRows(tempRows);
			}
		}
	}

	private void bindMapsMousePosition(int size, MouseEvent e) {
		Map sourceMap = ((MapControl) e.getSource()).getMap();
		Map currentMap = sourceMap;
		currentMap.getTrackingLayer().clear();
		currentMap.refreshTrackingLayer();
		Point2Ds points = new Point2Ds();
		for (int j = 0; j < size; j++) {
			IForm formMap = (IForm) formMapList.get(j);
			Map map;
			if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !e.getSource().equals(((IFormMap) formMap).getMapControl())) {
				map = ((IFormMap) formMap).getMapControl().getMap();
				if (!sourceMap.getPrjCoordSys().equals(map.getPrjCoordSys())) {
					points.clear();
					points.add(map.pixelToLogical(e.getPoint()));
					CoordSysTranslator.convert(points, currentMap.getPrjCoordSys(), map.getPrjCoordSys(), new CoordSysTransParameter(), CoordSysTransMethod.MTH_COORDINATE_FRAME);
				}
				MapUtilities.clearTrackingObjects(map, TAG_MOVE);
				Geometry tempGeometry = getTrackingGeometry(map.pixelToMap(e.getPoint()), Color.gray);
				map.getTrackingLayer().add(tempGeometry, TAG_MOVE);
				tempGeometry.dispose();
				map.refreshTrackingLayer();

			}
		}
	}

	//region 绘制跟踪层图像模拟光标
	private Geometry getTrackingGeometry(Point2D point, Color color) {
		GeoCompound geoCompound = new GeoCompound();
		GeoPoint geoPoint = new GeoPoint(point);
		geoPoint.setStyle(getPointStyle(color));
		geoCompound.addPart(geoPoint);
		return geoCompound;
	}

	private GeoStyle getPointStyle(Color color) {
		GeoStyle style = new GeoStyle();
		style.setSymbolMarker(getCrossMarket(color));
		style.setMarkerSize(new Size2D(10, 10));
		return style;
	}

	private SymbolMarker getCrossMarket(Color color) {
		SymbolMarker sm = new SymbolMarker();

		Rectangle2D rect = new Rectangle2D(0, 0, MARKET_WIDTH, MARKET_WIDTH);
		GeoCompound compound = new GeoCompound();
		try {
			int start = 0;
			int end = MARKET_WIDTH - start;
			Point2Ds pnts = new Point2Ds();
			pnts.add(new Point2D(start, MARKET_WIDTH / 2));
			pnts.add(new Point2D(end, MARKET_WIDTH / 2));
			GeoLine line = new GeoLine(pnts);
			GeoStyle lineStyle = new GeoStyle();
			lineStyle.setLineColor(color);
			lineStyle.setLineWidth(5);
			line.setStyle(lineStyle);
			compound.addPart(line);

			pnts = new Point2Ds();
			pnts.add(new Point2D(MARKET_WIDTH / 2, start));
			pnts.add(new Point2D(MARKET_WIDTH / 2, end));
			line = new GeoLine(pnts);
			line.setStyle(lineStyle);
			compound.addPart(line);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		sm.fromGeometry(compound, rect);
		return sm;
	}

	private void bindMapCenterAndScale(int size, Map sourceMap) {
		Point2D center = sourceMap.getCenter();
		double scale = sourceMap.getScale();
		for (int j = 0; j < size; j++) {
			IForm formMap = (IForm) formMapList.get(j);
			Map map;
			if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !sourceMap.equals(((IFormMap) formMap).getMapControl().getMap())) {
				map = ((IFormMap) formMap).getMapControl().getMap();
				if (null != center && scale - 0.0 > 0) {
					map.setCenter(center);
					map.setScale(scale);
					map.refresh();
				}
			} else if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && sourceMap.equals(((IFormMap) formMap).getMapControl().getMap())) {
				map = ((IFormMap) formMap).getMapControl().getMap();
				center = map.getCenter();
				scale = map.getScale();
			}
		}
	}

	private void bindMapCenter(int size, MouseEvent e) {
		Map sourceMap = ((MapControl) e.getSource()).getMap();
		Point2D center = sourceMap.getCenter();
		for (int j = 0; j < size; j++) {
			IForm formMap = (IForm) formMapList.get(j);
			Map map;

			if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() && !e.getSource().equals(((IFormMap) formMap).getMapControl())) {
				map = ((IFormMap) formMap).getMapControl().getMap();
				if (null != center) {
					map.setCenter(center);
					map.refresh();
				}
			}
		}
	}

	private void bindMapsSelection(int size, Map map) {
		for (int j = 0; j < size; j++) {
			IForm formMap = (IForm) formMapList.get(j);
			if (formMap instanceof IFormMap && null != ((IFormMap) formMap).getMapControl() &&
					!map.equals(((IFormMap) formMap).getMapControl().getMap()) && includeSameLayer(((IFormMap) formMap).getMapControl().getMap(), map)) {
				Map sourceMap = ((IFormMap) formMap).getMapControl().getMap();
				Map targetMap = map;
				Layers sourceLayers = sourceMap.getLayers();
				Layers targetLayers = targetMap.getLayers();
				int sourceLayesSize = sourceLayers.getCount();
				int targetLaysersSize = targetLayers.getCount();
				for (int i = 0; i < sourceLayesSize; i++) {
					for (int k = 0; k < targetLaysersSize; k++) {
						Layer sourceLayer = sourceLayers.get(i);
						Layer targetLayer = targetLayers.get(k);
						if (sourceLayer.getDataset().equals(targetLayer.getDataset())) {
							sourceLayer.setSelection(targetLayer.getSelection());
						}
					}
				}
			}
		}
	}

	private boolean includeSameLayer(Map sourceMap, Map targetMap) {
		boolean result = false;
		Layers sourceLayers = sourceMap.getLayers();
		Layers targetLayers = targetMap.getLayers();
		int sourceLayesSize = sourceLayers.getCount();
		int targetLaysersSize = targetLayers.getCount();
		for (int i = 0; i < sourceLayesSize; i++) {
			for (int k = 0; k < targetLaysersSize; k++) {
				Layer sourceLayer = sourceLayers.get(i);
				Layer targetLayer = targetLayers.get(k);
				if (sourceLayer.getDataset().equals(targetLayer.getDataset())) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	class LocalSelectChangedListener implements GeometrySelectChangedListener {
		private Map map;

		public LocalSelectChangedListener(Map map) {
			this.map = map;
		}

		@Override
		public void geometrySelectChanged(GeometrySelectChangedEvent geometrySelectChangedEvent) {
			bindMapsSelection(formMapList.size(), map);
		}
	}

	public void bindFormMaps() {
		int formMapSize = formMapList.size();
		for (int i = 0; i < formMapSize; i++) {
			IFormMap temp = (IFormMap) formMapList.get(i);
			final MapControl mapControl = temp.getMapControl();
			mapControl.addMouseListener(this.mapControlMouseListener);
			mapControl.addMouseWheelListener(this.mapControlMouseWheelListener);
			mapControl.addMouseMotionListener(this.mapControlMouseMotionListener);
			mapControl.getMap().addDrawingListener(this.mapDrawingListener);
			mapControl.getMap().addDrawnListener(this.mapDrawnListener);
			this.geoMetroySelectChangeListener = new LocalSelectChangedListener(mapControl.getMap());
			mapControl.addGeometrySelectChangedListener(this.geoMetroySelectChangeListener);
		}
	}

	public void removeFormMapsBind() {
		int formMapSize = formMapList.size();
		for (int i = formMapSize - 1; i >= 0; i--) {
			removeFormMapBind(this.formMapList.get(i));
		}
	}

	public void removeFormMapBind(IForm form) {
		if (!(form instanceof IFormMap) || !this.formMapList.contains(form)) {
			return;
		}

		MapControl mapControl = ((IFormMap) form).getMapControl();
		mapControl.removeMouseListener(this.mapControlMouseListener);
		mapControl.removeMouseWheelListener(this.mapControlMouseWheelListener);
		mapControl.removeMouseMotionListener(this.mapControlMouseMotionListener);
		mapControl.getMap().removeDrawingListener(this.mapDrawingListener);
		mapControl.getMap().removeDrawnListener(this.mapDrawnListener);
		this.geoMetroySelectChangeListener = new LocalSelectChangedListener(mapControl.getMap());
		mapControl.removeGeometrySelectChangedListener(this.geoMetroySelectChangeListener);
		this.formMapList.remove(form);
	}

	public void bindFormTabulars() {
		int formTabularSize = formTabularList.size();
		for (int i = 0; i < formTabularSize; i++) {
			IFormTabular formTabular = (IFormTabular) formTabularList.get(i);
			this.listMouseListener = new LocalMouseListener(formTabular);
			this.tabularTableKeyListener = new LocalKeyListener(formTabular);
			formTabular.getjTableTabular().addMouseListener(this.listMouseListener);
			formTabular.getjTableTabular().getTableHeader().addMouseListener(this.listMouseListener);
			formTabular.getjTableTabular().addKeyListener(this.tabularTableKeyListener);
			formTabular.getRowHeader().addMouseListener(this.listMouseListener);
		}
	}

	public void removeFormTabularsBind() {
		int formTabularSize = formTabularList.size();
		for (int i = formTabularSize - 1; i >= 0; i--) {
			removeFormTabularBind(this.formTabularList.get(i));
		}
	}

	public void removeFormTabularBind(IForm form) {
		if (!(form instanceof IFormTabular) || !this.formTabularList.contains(form)) {
			return;
		}

		IFormTabular formTabular = (IFormTabular) form;
		this.listMouseListener = new LocalMouseListener(formTabular);
		this.tabularTableKeyListener = new LocalKeyListener(formTabular);
		formTabular.getjTableTabular().removeMouseListener(this.listMouseListener);
		formTabular.getjTableTabular().getTableHeader().removeMouseListener(this.listMouseListener);
		formTabular.getjTableTabular().removeKeyListener(this.tabularTableKeyListener);
		formTabular.getRowHeader().removeMouseListener(this.listMouseListener);
		this.formTabularList.remove(formTabular);
	}

	public void bindFormMapsAndFormTabulars() {
		bindFormMaps();
		int formMapSize = formMapList.size();
		int formTabularSize = formTabularList.size();
		propertyBindWindows = new ArrayList<>();
		for (int i = 0; i < formMapSize; i++) {
			IFormMap formMap = (IFormMap) formMapList.get(i);
			for (int j = 0; j < formTabularSize; j++) {
				IFormTabular formTabular = (IFormTabular) formTabularList.get(j);
				Layer layer = containsLayer(formMap, formTabular);
				if (null != layer) {
					PropertyBindWindow propertyBindWindow = new PropertyBindWindow();
					propertyBindWindow.setFormMap(formMap);
					propertyBindWindow.setBindProperty(new BindProperty(formMap.getMapControl()));
					propertyBindWindow.setBindWindow(new BindWindow(formTabular), layer);
					propertyBindWindow.registEvents();
					propertyBindWindows.add(propertyBindWindow);
				}
			}
		}

	}

	private Layer containsLayer(IFormMap formMap, IFormTabular formTabular) {
		Layer result = null;
		Layers layers = formMap.getMapControl().getMap().getLayers();
		int layersCount = layers.getCount();
		for (int i = 0; i < layersCount; i++) {
			Layer tempLayer = layers.get(i);
			if (tempLayer.getDataset().equals(formTabular.getRecordset().getDataset())) {
				result = tempLayer;
			}
		}
		return result;
	}

	public void removeFormMapsAndFormTabularsBind() {
		removeFormMapsBind();

		if (propertyBindWindows != null) {
			int propertyBindWindowSize = propertyBindWindows.size();
			for (int i = 0; i < propertyBindWindowSize; i++) {
				propertyBindWindows.get(i).removeEvents();
			}
			this.propertyBindWindows.clear();
		}
	}

	private class LocalKeyListener extends KeyAdapter {
		private IFormTabular formTabular;

		public LocalKeyListener(IFormTabular formTabular) {
			this.formTabular = formTabular;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			bindTabularsSelectRow(formTabular);
		}
	}

	private class LocalMouseListener extends MouseAdapter {
		//只有属性表时联动
		private IFormTabular formTabular;

		@Override
		public void mouseDragged(MouseEvent e) {
			bindTabularsSelectRow(formTabular);
		}

		public LocalMouseListener(IFormTabular formTabular) {
			this.formTabular = formTabular;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			bindTabularsSelectRow(formTabular);
		}
	}

	public List getFormMapList() {
		return formMapList;
	}

	public List getFormTabularList() {
		return formTabularList;
	}

}
