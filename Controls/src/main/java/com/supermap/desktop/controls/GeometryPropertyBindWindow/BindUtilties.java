package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.event.ActiveFormChangedEvent;
import com.supermap.desktop.event.ActiveFormChangedListener;
import com.supermap.desktop.implement.SmMenuItem;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.FormManager;
import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.layout.FlowLayoutStrategy;
import com.supermap.desktop.ui.mdi.layout.ILayoutStrategy;
import com.supermap.desktop.utilities.TabularUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import java.awt.*;

public class BindUtilties {

	private static final String KEY_FORM_MAP = "group_formMap";
	private static final String KEY_FORM_TABULAR = "group_formTabular";

	private static MdiGroup formMapGroup;
	private static MdiGroup formTabularGroup;
	private static MapControl mapControl;
	private static IFormTabular tabular;
	private static IPropertyBindWindow propertyBindWindow;
	private static BindUtilties utilties = new BindUtilties();
	private static ActiveFormChangedListener activeFormChangeListener = utilties.new LocalFormChangedListener();
	static int tabSize = 0;// 属性表个数

	public static void windowBindProperty(IFormMap formMap, Layer layer) {
		FormManager formManager = (FormManager) Application.getActiveApplication().getMainFrame().getFormManager();

		ILayoutStrategy strategy = formManager.getLayoutStrategy();
		if (strategy instanceof FlowLayoutStrategy) {
			((FlowLayoutStrategy) strategy).setLayoutMode(FlowLayoutStrategy.VERTICAL);
		}

		if (formManager.indexOf(formMapGroup) < 0) {
			formMapGroup = null;
		}

		if (formManager.indexOf(formTabularGroup) < 0) {
			formTabularGroup = null;
		}

		MdiPage mapPage = formManager.getPage((FormBaseChild) formMap);
		if (formMapGroup == null && mapPage != null) {
			formMapGroup = mapPage.getGroup();
		}
		formMapGroup.addPage(mapPage);

		MdiPage tabularPage = formManager.getPage((FormBaseChild) tabular);
		if (formTabularGroup == null) {
			if (tabularPage != null) {
				formTabularGroup = formManager.createGroup();
			}
		}
		formTabularGroup.addPage(tabularPage);

		mapControl = formMap.getMapControl();
		tabSize += 1;
		propertyBindWindow = new PropertyBindWindow();
		propertyBindWindow.setFormMap(formMap);
		propertyBindWindow.setBindProperty(new BindProperty(mapControl));
		propertyBindWindow.setBindWindow(new BindWindow(tabular), layer);
		propertyBindWindow.registEvents();
		formManager.addActiveFormChangedListener(activeFormChangeListener);
	}


	public static void openTabular(Dataset dataset, Recordset recordset) {
		// 打开一个默认的属性表，然后修改属性表的title和数据与当前图层对应的数据匹配
		tabular = TabularUtilities.openDatasetVectorFormTabular(dataset);
		tabular.setText(dataset.getName() + "@" + dataset.getDatasource().getAlias());
		tabular.setRecordset(recordset);
	}

	public static void showPopumenu(IBaseItem caller) {
		Point point = ((SmMenuItem) caller).getParent().getLocation();
		int x = (int) point.getX() + 46;
		final JPopupMenuBind popupMenuBind = JPopupMenuBind.instance();
		int y = (int) point.getY() + 52;
		JFrame mainFrame = (JFrame) Application.getActiveApplication().getMainFrame();
		popupMenuBind.removeBind();
		popupMenuBind.init();
		popupMenuBind.show(mainFrame, x, y);
		popupMenuBind.setVisible(true);
		//窗口关闭完后删除事件
		Application.getActiveApplication().getMainFrame().getFormManager().addActiveFormChangedListener(new ActiveFormChangedListener() {
			@Override
			public void activeFormChanged(ActiveFormChangedEvent e) {
				if (null == e.getNewActiveForm()) {
					popupMenuBind.removeBind();
					popupMenuBind.removeEvents();
					popupMenuBind.dispose();
				}
			}
		});
	}

	class LocalFormChangedListener implements ActiveFormChangedListener {

		@Override
		public void activeFormChanged(ActiveFormChangedEvent e) {
			if (null == e.getNewActiveForm()) {
				// 当所有地图关闭时将splitWindow设置为空，重新关联,并移除事件
//                splitWindow = null;

				propertyBindWindow.removeEvents();
			}
		}

	}
}
