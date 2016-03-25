package com.supermap.desktop.Interface;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface IOptionPane {
	int showConfirmDialogWithCancel(String message);

	int showConfirmDialog(String message, String title);

	int showConfirmDialog(String message);

	void showErrorMessageDialog(String message);

	void showMessageDialog(String message);
}
