package com.supermap.desktop.dialog;

import com.supermap.desktop.Interface.IOptionPane;
import com.supermap.desktop.ui.UICommonToolkit;

/**
 * Created by Administrator on 2016/3/25.
 */
public class OptionPaneImpl implements IOptionPane {
	@Override
	public int showConfirmDialogWithCancel(String message) {
		return UICommonToolkit.showConfirmDialogWithCancel(message);
	}

	@Override
	public int showConfirmDialog(String message, String title) {
		return UICommonToolkit.showConfirmDialog(message, title);
	}

	@Override
	public int showConfirmDialog(String message) {
		return UICommonToolkit.showConfirmDialog(message);

	}

	@Override
	public void showErrorMessageDialog(String message) {
		UICommonToolkit.showErrorMessageDialog(message);

	}

	@Override
	public void showMessageDialog(String message) {
		UICommonToolkit.showMessageDialog(message);

	}
}
