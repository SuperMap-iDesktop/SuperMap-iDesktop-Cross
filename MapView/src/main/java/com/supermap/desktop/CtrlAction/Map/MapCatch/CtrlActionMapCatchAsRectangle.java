package com.supermap.desktop.CtrlAction.Map.MapCatch;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.map.propertycontrols.MapActionSelectTargetInfoPanel;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapClosedEvent;
import com.supermap.mapping.MapClosedListener;
import com.supermap.mapping.TrackingLayer;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yuanR on 2017/8/24 0024.
 * 以矩形框捕捉地图中的原点和目标点
 * 目前服务于工作流中的两点最短地表路径和两点最小耗费距离
 * 重构：
 * 修改为拾取坐标 功能
 */
public class CtrlActionMapCatchAsRectangle extends CtrlAction {
	public CtrlActionMapCatchAsRectangle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	private transient MapActionSelectTargetInfoPanel panelSelectTargetInfo = new MapActionSelectTargetInfoPanel("");
	private transient MapControl mapControl;
	private IFormMap formMap;

	private static final String PICKUPCOORDINATE_TAG = "PickupCoordinate";

	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				overPickup();
//				copyParameter();
			} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				addPickup(e);
			}
		}
	};

	private transient MouseMotionListener mouseMotionListener = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			Point point = e.getPoint();
			Point2D point2D = activeMapControl.getMap().pixelToMap(point);
			panelSelectTargetInfo.setShowedText("X:" + point2D.x + ",Y:" + point2D.y);
			panelSelectTargetInfo.setLocation(point.x + 15, point.y + 20);
			panelSelectTargetInfo.setVisible(true);
			panelSelectTargetInfo.updateUI();
		}
	};

	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
				TrackingLayer trackingLayer = mapControl.getMap().getTrackingLayer();
				for (int count = trackingLayer.getCount() - 1; count >= 0; count--) {
					if (trackingLayer.getTag(count).startsWith(PICKUPCOORDINATE_TAG)) {
						trackingLayer.remove(count);
					}
				}
				mapControl.getMap().refreshTrackingLayer();
				mapControl.removeKeyListener(keyAdapter);
			}
		}
	};

	private MapClosedListener mapClosedListener = new MapClosedListener() {

		@Override
		public void mapClosed(MapClosedEvent arg0) {
			overPickup();
		}
	};

	@Override
	public void run() {
		super.run();
		try {
			this.formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
			if (null != this.formMap) {
				this.mapControl = this.formMap.getMapControl();
				startPickup();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}


	@Override
	public boolean enable() {
		boolean result = true;
		MapControl mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		ArrayList<Layer> arrayList;
		arrayList = MapUtilities.getLayers(mapControl.getMap(), true);
		//是否存在图层
		if (arrayList.size() > 0) {
			HashMap<Dataset, Layer> layerMap = new HashMap<>();
			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i).getDataset() != null) {
					result = true;
					break;
				}
			}
		}

		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		ArrayList<Datasource> isCanUseDatasources = new ArrayList<>();
		for (int i = 0; i < datasources.getCount(); i++) {
			if (!datasources.get(i).isReadOnly()) {
				isCanUseDatasources.add(datasources.get(i));
			}
		}
		if (isCanUseDatasources == null || isCanUseDatasources.size() == 0) {
			result = false;
		}
		return result;
	}

	/**
	 * 开始捕捉
	 */
	private void startPickup() {
		this.mapControl.setAction(Action.SELECT);
		this.mapControl.add(this.panelSelectTargetInfo);
		addListener();
		this.mapControl.setLayout(null);
	}

	/**
	 * 结束捕捉
	 */
	private void overPickup() {
		this.formMap.showPopupMenu();
		removeListener();
		this.mapControl.setAction(Action.SELECT2);
		this.mapControl.remove(this.panelSelectTargetInfo);
	}

	private void addListener() {
		removeListener();
		this.formMap.dontShowPopupMenu();
		this.mapControl.addMouseMotionListener(this.mouseMotionListener);
		this.mapControl.addKeyListener(this.keyAdapter);
		this.mapControl.addMouseListener(this.mouseAdapter);
		this.mapControl.getMap().addMapClosedListener(this.mapClosedListener);
	}

	private void removeListener() {
		this.mapControl.removeMouseMotionListener(this.mouseMotionListener);
		this.mapControl.removeMouseListener(this.mouseAdapter);
		this.mapControl.getMap().removeMapClosedListener(this.mapClosedListener);
	}

	/**
	 * 复制参数
	 */
	private void copyParameter(GeoPoint geoPoint) {
		String text = "X:" + geoPoint.getX() + ",Y:" + geoPoint.getY();
		setSysClipboardText(text);
	}

	/**
	 * 调用windows的剪贴板
	 *
	 * @param coypText
	 */
	public static void setSysClipboardText(String coypText) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable Text = new StringSelection(coypText);
		clip.setContents(Text, null);
	}


	protected void addPickup(MouseEvent e) {
		GeoPoint geoPoint = new GeoPoint(this.mapControl.getMap().pixelToMap(e.getPoint()));
		GeoStyle geoStyle = new GeoStyle();
		geoStyle.setLineColor(Color.RED);
		geoStyle.setMarkerSize(new Size2D(3, 3));
		geoPoint.setStyle(geoStyle);
		TrackingLayer trackingLayer = this.mapControl.getMap().getTrackingLayer();
		int geoCount = 0;
		for (int i = 0; i < trackingLayer.getCount(); i++) {
			if (trackingLayer.getTag(i).equals(PICKUPCOORDINATE_TAG + "Point")) {
				geoCount++;
			}
		}
		int pointCount = 1;
		if (0 != geoCount) {
			pointCount = 1 + geoCount;
		}
		// 在跟踪层上画点
		Point2D point2DNumber = this.mapControl.getMap().pixelToMap(e.getPoint());
		TextPart textPartNumber = new TextPart();
		textPartNumber.setAnchorPoint(point2DNumber);
		textPartNumber.setText(String.valueOf(pointCount));
		// 在跟踪层上绘制数字
		GeoText geoTextNumber = new GeoText(textPartNumber);
		TextStyle textStyleNumber = new TextStyle();
		textStyleNumber.setBold(true);
		textStyleNumber.setAlignment(TextAlignment.TOPLEFT);
		geoTextNumber.setTextStyle(textStyleNumber);

		trackingLayer.add(geoPoint, PICKUPCOORDINATE_TAG + "Point");
		trackingLayer.add(geoTextNumber, PICKUPCOORDINATE_TAG + "PointCount");

		copyParameter(geoPoint);
		Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_Label_PointsCoordinate"), pointCount) +
				geoPoint.getX() + "," + geoPoint.getY());
		Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_PickupCoordinateSuccess"));
	}

}
