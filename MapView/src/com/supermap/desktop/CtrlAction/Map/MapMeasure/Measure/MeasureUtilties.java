package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.desktop.FormMap;
import com.supermap.desktop.enums.MeasureType;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.docking.DockingWindow;
import com.supermap.desktop.ui.docking.DockingWindowAdapter;
import com.supermap.desktop.utilities.LogUtilities;
import com.supermap.ui.MapControl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/31.
 */
public class MeasureUtilties {

	private static final DockingWindowAdapter dockingWindowAdapter = new DockingWindowAdapter() {
		@Override
		public void windowClosed(DockingWindow window) {
			if (window instanceof FormMap) {
				MapControl mapControl = ((FormMap) window).getMapControl();
				for (IMeasureAble measureAbleList : measureAbleLists) {
					if (measureAbleList.getMapControl() == mapControl && !measureAbleList.isMeasureAble()) {
						measureAbleList.stopMeasure();
					}
				}
			}
		}
	};


	private MeasureUtilties() {

	}

	private static List<IMeasureAble> measureAbleLists = new ArrayList<>();

	public static void startMeasure(FormMap formMap, MeasureType measureType) {
		if (formMap == null || formMap.getMapControl() == null) {
			return;
		}
		addListener(formMap);
		MapControl mapControl = formMap.getMapControl();

		// 查看地图是否正在量算
		for (IMeasureAble measureAbleList : measureAbleLists) {
			if (measureAbleList.getMapControl() == mapControl && !measureAbleList.isMeasureAble()) {
				// 地图正在量算中
				if (measureAbleList.getMeasureType() == measureType) {
					// 正在量算的
					return;
				} else {
					measureAbleList.stopMeasure();
				}
			}
		}

		// 地图不处于量算或量算类型不符
		// 查看当前是否存在对应类型的空闲量算对象
		for (IMeasureAble measureAbleList : measureAbleLists) {
			if (measureAbleList.isMeasureAble() && measureAbleList.getMeasureType() == measureType) {
				measureAbleList.setMapControl(mapControl);
				measureAbleList.startMeasure();
				return;
			}
		}

		// 不存在空闲对象，创建新对象
		IMeasureAble measureInstance = MeasureFactory.getMeasureInstance(measureType);
		if (measureInstance != null) {
			measureAbleLists.add(measureInstance);
			measureInstance.setMapControl(mapControl);
			measureInstance.startMeasure();
		} else {
			LogUtilities.debug(MessageFormat.format(MapViewProperties.getString("Log_MeasureCreateNewMeasureFailed"), formMap.getText(), measureType.toString()));
		}

	}

	public static void endMeasure(FormMap formMap) {
		MapControl mapControl = formMap.getMapControl();
		for (IMeasureAble measureAbleList : measureAbleLists) {
			if (measureAbleList.getMapControl() == mapControl) {
				measureAbleList.stopMeasure();
				measureAbleList.setMapControl(null);
			}
		}
	}

	private static void addListener(FormMap formMap) {
		formMap.removeListener(dockingWindowAdapter);
		formMap.addListener(dockingWindowAdapter);
	}
}
