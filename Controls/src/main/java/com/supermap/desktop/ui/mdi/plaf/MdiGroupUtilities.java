package com.supermap.desktop.ui.mdi.plaf;

import com.supermap.desktop.ui.mdi.MdiGroup;

import javax.swing.*;

/**
 * Created by highsad on 2016/8/30.
 */
public class MdiGroupUtilities {

	private MdiGroupUtilities() {
		// 工具类，不提供构造方法
	}

	/**
	 * 获取指定 component 使用的字体的高度
	 * @param component
	 * @return
	 */
	public static int getFontHeight(JComponent component) {
		return component == null ? -1 : component.getFontMetrics(component.getFont()).getHeight();
	}
}
