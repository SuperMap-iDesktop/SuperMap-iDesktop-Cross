package com.supermap.desktop.CtrlAction.Map.MapCatch;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.GeoPoint;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yuanR on 2017/8/24 0024.
 * 以矩形框捕捉地图中的原点和目标点
 * 目前服务于工作流中的两点最短地表路径和两点最小耗费距离
 */
public class CtrlActionMapCatchAsRectangle extends CtrlAction {
	public CtrlActionMapCatchAsRectangle(IBaseItem caller, IForm formClass) {
		super(caller, formClass);
	}

	private MapActionSelectTargetInfoPanel panelSelectTargetInfo;

	private transient ArrayList<GeoPoint> geoPoints;

	@Override
	public void run() {
		super.run();
		geoPoints = new ArrayList<>();
		setAction();
	}

	@Override
	public boolean enable() {
		boolean result = true;
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		ArrayList<Layer> arrayList;
		arrayList = MapUtilities.getLayers(activeMapControl.getMap(), true);
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
		final MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setTrackMode(TrackMode.TRACK);
		activeMapControl.setAction(Action.CREATEPOINT);

		activeMapControl.setLayout(null);
		activeMapControl.addMouseListener(controlMouseListener);
		activeMapControl.addMouseMotionListener(mouseMotionListener);
		activeMapControl.addTrackedListener(trackedListener);

		panelSelectTargetInfo = new MapActionSelectTargetInfoPanel(MapViewProperties.getString("String_SelectTwoPoints"));
		activeMapControl.add(panelSelectTargetInfo);

	}

	private transient MouseMotionListener mouseMotionListener = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			Point point = e.getPoint();
			panelSelectTargetInfo.setLocation(point.x + 15, point.y + 20);
			panelSelectTargetInfo.setVisible(true);
			panelSelectTargetInfo.updateUI();
		}
	};

	private transient MouseListener controlMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				panelSelectTargetInfo.setVisible(false);
				MapControl control = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
				control.removeMouseListener(controlMouseListener);
				control.removeMouseMotionListener(mouseMotionListener);
				control.removeTrackedListener(trackedListener);
				exitEdit();
				if (geoPoints.size() >= 2) {
					DialogMapBoundsCatch dialogMapBoundsCatch = new DialogMapBoundsCatch(geoPoints);
					dialogMapBoundsCatch.showDialog();
				} else {
					Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_SelectTwoError"));
				}
			}
		}

	};

	private transient TrackedListener trackedListener = new TrackedListener() {

		@Override
		public void tracked(TrackedEvent arg0) {
			abstractTracked(arg0);
		}
	};

	private void abstractTracked(TrackedEvent arg0) {
		if (arg0.getGeometry() != null) {
			if (arg0.getGeometry() instanceof GeoPoint) {
				// 当链表中的值大于两个时，清空链表
				if (geoPoints.size() >= 2) {
					geoPoints.clear();
				}
				geoPoints.add((GeoPoint) arg0.getGeometry().clone());
			}
		}
	}

	private void exitEdit() {
		MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
		activeMapControl.setAction(Action.SELECT2);
		activeMapControl.setTrackMode(TrackMode.EDIT);
	}
}
