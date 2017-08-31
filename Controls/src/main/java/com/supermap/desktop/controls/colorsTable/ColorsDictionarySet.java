package com.supermap.desktop.controls.colorsTable;

import java.awt.*;

/**
 * Created by yuanR on 2017/8/31 0031.
 * 实现栅格颜色表面板和model的联动，当修改颜色值时，实时应用到地图当中。
 */
public interface ColorsDictionarySet {
	void set(double[] keys, Color[] colors);

	void refresh();
}
