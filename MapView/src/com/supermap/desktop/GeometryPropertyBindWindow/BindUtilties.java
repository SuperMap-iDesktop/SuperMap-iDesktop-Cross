package com.supermap.desktop.GeometryPropertyBindWindow;

import net.infonode.util.Direction;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.event.*;
import com.supermap.desktop.ui.docking.*;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.ui.MapControl;

public class BindUtilties {
	private static SplitWindow splitWindow;
	private static IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
	private static MapControl mapControl;
	private static IFormTabular tabular;
	private static DockingWindow newTabWindow;
	private static IPropertyBindWindow propertyBindWindow;
	private static BindUtilties utilties = new BindUtilties();
	private static ActiveFormChangedListener activeFormChangeListener = utilties.new LocalFormChangedListener();
	static int tabSize = 0;// 属性表个数

	/**
	 * 判断数组是否会越界
	 * 
	 * @param rows
	 * @param tableRowCount
	 * @return
	 */
	public static boolean isRightRows(int[] rows, int tableRowCount) {
		boolean isRightRows = true;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] > tableRowCount) {
				isRightRows = false;
				break;
			}
		}
		return isRightRows;
	}

	public static void windowBindProperty(IFormMap formMap, TabWindow tabWindow, Layer layer) {
		mapControl = formMap.getMapControl();
		newTabWindow = tabWindow.getChildWindow(tabWindow.getChildWindowCount() - 1);
		tabSize += 1;
		if (null == splitWindow) {
			splitWindow = tabWindow.split(newTabWindow, Direction.DOWN, 0.5f);
		} else if (splitWindow.getChildWindowCount() > 0) {
			((TabWindow) splitWindow.getChildWindow(splitWindow.getChildWindowCount() - 1)).addTab(newTabWindow);
		}
		propertyBindWindow = new PropertyBindWindow();
		propertyBindWindow.setFormMap(formMap);
		propertyBindWindow.setBindProperty(new BindProperty(mapControl));
		propertyBindWindow.setBindWindow(new BindWindow(tabular), layer);
		propertyBindWindow.registEvents();
		newTabWindow.addListener(new DockingWindowAdapter() {

			@Override
			public void windowClosed(DockingWindow window) {
				// 当前属性表关闭时清空map
				tabSize -= 1;
				if (0 == tabSize) {
					splitWindow = null;
				}
			}

		});
		formManager.addActiveFormChangedListener(activeFormChangeListener);

		Application.getActiveApplication().setActiveForm(formMap);
	}

	public static void openTabular(Dataset dataset, Recordset recordset) {
		// 打开一个默认的属性表，然后修改属性表的title和数据与当前图层对应的数据匹配
		tabular = TabularUtilities.openDatasetVectorFormTabular(dataset);
		tabular.setText(dataset.getName() + "@" + dataset.getDatasource().getAlias());
		tabular.getIdMap().clear();
		tabular.getRowIndexMap().clear();
		tabular.setRecordset(recordset);
	}

	class LocalFormChangedListener implements ActiveFormChangedListener {

		@Override
		public void activeFormChanged(ActiveFormChangedEvent e) {
			if (null == e.getNewActiveForm()) {
				// 当所有地图关闭时将splitWindow设置为空，重新关联,并移除事件
				splitWindow = null;
				propertyBindWindow.removeEvents();
			}
		}

	}

}
