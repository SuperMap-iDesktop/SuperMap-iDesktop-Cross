package com.supermap.desktop.utilities;

import com.supermap.data.DatasetType;
import com.supermap.ui.Action;

/**
 * Created by xiajt on 2015/11/16.
 */
public class ActionUtilities {
	private ActionUtilities() {
		// 工具类不提供构造函数
	}

	/**
	 * Action是否为创建对象类型的Action
	 *
	 * @param action 要判断的Action
	 * @return
	 */
	public static boolean isCreateAction(Action action) {
		return action == Action.CREATE_ALONG_LINE_TEXT || action == Action.CREATE_ARC_3P
				|| action == Action.CREATETEXT || action == Action.CREATEBSPLINE || action == Action.CREATECARDINAL
				|| action == Action.CREATECIRCLE || action == Action.CREATE_CIRCLE_2P || action == Action.CREATE_CIRCLE_3P
				|| action == Action.CREATECURVE || action == Action.CREATEELLIPSE || action == Action.CREATE_ELLIPSE_ARC
				|| action == Action.CREATE_FREE_POLYLINE || action == Action.CREATELINE || action == Action.CREATE_OBLIQUE_ELLIPSE
				|| action == Action.CREATEPARALLEL || action == Action.CREATEPARALLELOGRAM || action == Action.CREATEPIE
				|| action == Action.CREATEPOINT || action == Action.CREATEPOLYGON || action == Action.CREATEPOLYLINE
				|| action == Action.CREATERECTANGLE || action == Action.CREATE_ROUND_RECTANGLE;
	}

	/**
	 * 数据集是否为当前Action支持的类型
	 *
	 * @param action
	 * @param datasetType
	 * @return
	 */
	public static boolean isSupportDatasetType(Action action, DatasetType datasetType) {
		boolean isSupport = false;
		if (!isCreateAction(action)) {
			isSupport = true;
		} else if (action == Action.CREATE_ALONG_LINE_TEXT || action == Action.CREATETEXT) {
			isSupport = DatasetType.TEXT == datasetType || DatasetType.CAD == datasetType;
		} else if (isCreateLine(action)) {
			isSupport = DatasetType.LINE == datasetType || DatasetType.LINE3D == datasetType || DatasetType.LINEM == datasetType || DatasetType.CAD == datasetType
					|| DatasetType.NETWORK == datasetType || DatasetType.NETWORK3D == datasetType;
		} else if (isCreateRegion(action)) {
			isSupport = DatasetType.REGION == datasetType || DatasetType.REGION3D == datasetType || DatasetType.CAD == datasetType || DatasetType.LINE == datasetType
					|| DatasetType.LINE3D == datasetType || DatasetType.LINEM == datasetType;
		} else if (action == Action.CREATEPOINT) {
			isSupport = DatasetType.POINT3D == datasetType || DatasetType.CAD == datasetType || DatasetType.POINT == datasetType;
		} else {
			isSupport = true;
		}
		return isSupport;
	}

	/**
	 * 判断Action是否为创建线对象的Action
	 *
	 * @param action
	 * @return
	 */
	public static boolean isCreateLine(Action action) {
		return action == Action.CREATEPOLYLINE || action == Action.CREATEPARALLEL || action == Action.CREATELINE
				|| action == Action.CREATE_FREE_POLYLINE || action == Action.CREATE_ELLIPSE_ARC
				|| action == Action.CREATE_ARC_3P || action == Action.CREATEBSPLINE || action == Action.CREATECARDINAL
				|| action == Action.CREATECURVE;
	}

	/**
	 * 判断Action是否为创建面对象的Action
	 *
	 * @param action
	 * @return
	 */
	public static boolean isCreateRegion(Action action) {
		return action == Action.CREATE_ROUND_RECTANGLE || action == Action.CREATERECTANGLE || action == Action.CREATEPOLYGON
				|| action == Action.CREATEPIE || action == Action.CREATEPARALLELOGRAM || action == Action.CREATE_OBLIQUE_ELLIPSE
				|| action == Action.CREATECIRCLE || action == Action.CREATE_CIRCLE_2P || action == Action.CREATE_CIRCLE_3P
				|| action == Action.CREATEELLIPSE;
	}
}
