package com.supermap.desktop.GeometryPropertyBindWindow;

public class BindUtilties {
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
}
