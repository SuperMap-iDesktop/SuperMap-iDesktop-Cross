package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Administrator on 2016/3/25.
 */
public class LayoutUtilties {
	private LayoutUtilties() {
		// 不提供构造函数
	}

	public static boolean checkAvailableLayoutName(String newLayoutName, String oldLayoutName) {
		boolean flag = false;
		String newLayoutNameTemp = newLayoutName;
		String oldLayoutNameTemp = oldLayoutName;
		newLayoutNameTemp = newLayoutNameTemp.toLowerCase();
		oldLayoutNameTemp = oldLayoutNameTemp.toLowerCase();
		if (newLayoutNameTemp == null || newLayoutNameTemp.length() <= 0) {
			flag = false;
		} else {
			ArrayList<String> allLayoutNames = new ArrayList<String>();
			for (int index = 0; index < Application.getActiveApplication().getWorkspace().getLayouts().getCount(); index++) {
				allLayoutNames.add(Application.getActiveApplication().getWorkspace().getLayouts().get(index).toLowerCase());
			}

			for (int index = 0; index < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); index++) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
				if (form instanceof IFormLayout && !form.getText().equalsIgnoreCase(oldLayoutNameTemp)) {
					allLayoutNames.add(form.getText().toLowerCase());
				}
			}

			if (!allLayoutNames.contains(newLayoutNameTemp)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取具有指定前缀的可用布局名称
	 *
	 * @param layoutName  指定前缀
	 * @param isNewWindow 是否是新窗口
	 * @return
	 */
	public static String getAvailableLayoutName(String layoutName, boolean isNewWindow) {
		String layoutNameTemp = layoutName;
		String availableLayoutName = layoutNameTemp.toLowerCase();

		try {
			if (layoutNameTemp == null || layoutNameTemp.length() <= 0) {
				layoutNameTemp = CoreProperties.getString("String_WorkspaceNodeCaptionLayout");
			}

			ArrayList<String> allLayoutNames = new ArrayList<String>();
			for (int index = 0; index < Application.getActiveApplication().getWorkspace().getLayouts().getCount(); index++) {
				allLayoutNames.add(Application.getActiveApplication().getWorkspace().getLayouts().get(index).toLowerCase());
			}

			for (int index = 0; index < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); index++) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(index);
				if (form instanceof IFormLayout) {
					allLayoutNames.add(form.getText().toLowerCase());
				}
			}

			if (!allLayoutNames.contains(availableLayoutName)) {
				availableLayoutName = layoutNameTemp;
			} else {
				int indexLayoutName = 1;
				while (true) {
					availableLayoutName = String.format("%s%d", layoutNameTemp, indexLayoutName);
					if (!allLayoutNames.contains(availableLayoutName.toLowerCase())) {
						break;
					}

					indexLayoutName += 1;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return availableLayoutName;
	}

	/**
	 * 批量删除指定名称的布局
	 *
	 * @param mapLayoutNames 布局名称
	 */
	public static void deleteMapLayout(String[] mapLayoutNames) {
		try {
			String message = "";
			if (mapLayoutNames.length == 1) {
				message = CoreProperties.getString("String_LayoutDelete_Confirm");
				message = message + System.lineSeparator() + String.format(CoreProperties.getString("String_LayoutDelete_Confirm_One"), mapLayoutNames[0]);
			} else {
				message = CoreProperties.getString("String_LayoutDelete_Confirm");
				message = message + System.lineSeparator() + String.format(CoreProperties.getString("String_LayoutDelete_Confirm_Multi"), mapLayoutNames.length);
			}
			if (!Objects.equals(message, "")
					&& (JOptionPaneUtilties.showConfirmDialog(message) == JOptionPane.OK_OPTION)) {

				for (String mapLayoutName : mapLayoutNames) {
					Application.getActiveApplication().getWorkspace().getLayouts().remove(mapLayoutName);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

}
