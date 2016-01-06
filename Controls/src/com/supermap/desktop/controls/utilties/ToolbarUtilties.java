package com.supermap.desktop.controls.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IToolbar;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.implement.SmButtonDropdown;
import com.supermap.desktop.ui.ToolbarManager;

/**
 * Created by Administrator on 2015/11/10.
 */
public class ToolbarUtilties {
	private ToolbarUtilties() {
		// 公共类不提供构造方法
	}

	/**
	 * 刷新工具条
	 */
	public static void updataToolbarsState() {
		ToolbarManager toolbarManager = (ToolbarManager) Application.getActiveApplication().getMainFrame().getToolbarManager();
		for (int toolbarIndex = 0; toolbarIndex < toolbarManager.getCount(); toolbarIndex++) {
			IToolbar toolbar = toolbarManager.get(toolbarIndex);
			if (toolbar.isVisible()) {
				for (int itemIndex = 0; itemIndex < toolbar.getCount(); itemIndex++) {
					IBaseItem item = toolbar.getAt(itemIndex);
					if (item.getCtrlAction() != null) {
						item.getCtrlAction().setCaller(item);
//						if (item instanceof SmButtonDropdown) {
//							// 默认实现
//						}
						item.getCtrlAction().getCaller().setEnabled(item.getCtrlAction().enable());
						item.getCtrlAction().getCaller().setChecked(item.getCtrlAction().check());
					}
				}
			}
		}

		// 刷新子窗口工具条
		if (Application.getActiveApplication().getActiveForm() != null) {
			WindowType windowType = Application.getActiveApplication().getActiveForm().getWindowType();
			for (int toolbarIndex = 0; toolbarIndex < toolbarManager.getChildToolbarCount(windowType); toolbarIndex++) {
				IToolbar toolbar = toolbarManager.getChildToolbar(windowType, toolbarIndex);
				if (toolbar.isVisible()) {
					for (int itemIndex = 0; itemIndex < toolbar.getCount(); itemIndex++) {
						IBaseItem item = toolbar.getAt(itemIndex);
						if (item.getCtrlAction() != null) {
							item.getCtrlAction().setCaller(item);
							item.getCtrlAction().getCaller().setEnabled(item.getCtrlAction().enable());
							item.getCtrlAction().getCaller().setChecked(item.getCtrlAction().check());
						}
					}
				}

			}
		}
	}
}
