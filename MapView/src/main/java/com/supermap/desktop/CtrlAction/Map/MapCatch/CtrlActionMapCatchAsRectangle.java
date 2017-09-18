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
import com.supermap.ui.*;

import java.awt.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

	private MapControl activeMapControl;

	private MapActionSelectTargetInfoPanel panelSelectTargetInfo;

	private transient ArrayList<GeoPoint> geoPoints;

	@Override
	public void run() {
		super.run();
		this.activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		this.panelSelectTargetInfo = new MapActionSelectTargetInfoPanel("");
		this.geoPoints = new ArrayList<>();
		setAction();
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
	 * 设置Action事件
	 */
	private void setAction() {
		this.activeMapControl.setTrackMode(TrackMode.TRACK);
		this.activeMapControl.setAction(Action.CREATEPOINT);

		this.activeMapControl.setLayout(null);
		this.activeMapControl.addMouseListener(this.controlMouseListener);
		this.activeMapControl.addMouseMotionListener(this.mouseMotionListener);
		this.activeMapControl.addTrackedListener(this.trackedListener);
		this.activeMapControl.add(this.panelSelectTargetInfo);
	}

	private transient MouseMotionListener mouseMotionListener = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			Point point = e.getPoint();
			Point2D point2D = activeMapControl.getMap().pixelToMap(point);
			panelSelectTargetInfo.setShowedText("X:" + point2D.x + ",Y:" + point2D.y);
			panelSelectTargetInfo.setLocation(point.x + 15, point.y + 20);
			panelSelectTargetInfo.setVisible(true);
			panelSelectTargetInfo.updateUI();
		}
	};

	/**
	 * 拾取结束监听
	 */
	private transient MouseListener controlMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				panelSelectTargetInfo.setVisible(false);
				activeMapControl.removeMouseListener(controlMouseListener);
				activeMapControl.removeMouseMotionListener(mouseMotionListener);
				activeMapControl.removeTrackedListener(trackedListener);
				exitEdit();
				// 将拾取到的坐标复制到剪贴板上
				copyParameter();
			}
		}

	};

	/**
	 * 复制参数
	 */
	private void copyParameter() {
		String text = "";
		for (int i = 0; i < geoPoints.size(); i++) {
			String label = MessageFormat.format(MapViewProperties.getString("String_Label_PointsCoordinate"), i + 1);
			text = text + label + geoPoints.get(i).getX() + geoPoints.get(i).getY() + "\n";
		}
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


	private transient TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			abstractTracked(arg0);
		}
	};

	private void abstractTracked(TrackedEvent arg0) {
		if (arg0.getGeometry() != null) {
			if (arg0.getGeometry() instanceof GeoPoint) {
				this.geoPoints.add((GeoPoint) arg0.getGeometry().clone());
				Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_PickupCoordinateResult")
						, geoPoints.size(), ((GeoPoint) arg0.getGeometry()).getX(), ((GeoPoint) arg0.getGeometry()).getY()));
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_PickupCoordinateSuccess"));
			}
		}
	}

	private void exitEdit() {
		activeMapControl.setAction(Action.SELECT2);
		activeMapControl.setTrackMode(TrackMode.EDIT);
	}
}
