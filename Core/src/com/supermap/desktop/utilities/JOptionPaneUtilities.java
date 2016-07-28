package com.supermap.desktop.utilities;

import com.supermap.desktop.Interface.IOptionPane;
import com.supermap.desktop.properties.CoreProperties;

import javax.swing.*;

/**
 * UICommonToolKit在Control中，Core中部分代码要用到提示框，此处提供一个接口
 */
public class JOptionPaneUtilities {

	private static IOptionPane iOptionPane;

	private JOptionPaneUtilities() {

	}

	public static int showConfirmDialogWithCancel(String message) {
		if (iOptionPane != null) {
			return iOptionPane.showConfirmDialogWithCancel(message);
		} else {
			return JOptionPane.showConfirmDialog(null, message);
		}
	}

	public static int showConfirmDialog(String message, String title) {
		if (iOptionPane != null) {
			return iOptionPane.showConfirmDialog(message, title);
		} else {
			return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		}
	}

	public static int showConfirmDialog(String message) {
		if (iOptionPane != null) {
			return iOptionPane.showConfirmDialog(message);
		} else {
			return JOptionPane.showConfirmDialog(null, message);
		}
	}

	public static void showErrorMessageDialog(String message) {
		if (iOptionPane != null) {
			iOptionPane.showErrorMessageDialog(message);
		} else {
			JOptionPane.showMessageDialog(null, message, CoreProperties.getString("String_MessageBox_Title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void showMessageDialog(String message) {
		if (iOptionPane != null) {
			iOptionPane.showMessageDialog(message);
		} else {
			JOptionPane.showConfirmDialog(null, message);
		}
	}

	public static void setiOptionPane(IOptionPane iOptionPane) {
		JOptionPaneUtilities.iOptionPane = iOptionPane;
	}

}
