package com.supermap.desktop.ui.mdi.plaf;

import com.supermap.desktop.ui.mdi.MdiGroup;

import javax.swing.*;
import java.awt.*;

/**
 * Created by highsad on 2016/8/30.
 */
public class MdiGroupUtilities {

	private MdiGroupUtilities() {
		// 工具类，不提供构造方法
	}

	/**
	 * 获取指定 component 使用的字体的高度
	 *
	 * @param component
	 * @return
	 */
	public static int getFontHeight(JComponent component) {
		return component == null ? -1 : component.getFontMetrics(component.getFont()).getHeight();
	}

	public static MdiGroup findAncestorGroup(Component c) {
		if (c == null || c instanceof MdiGroup) {
			return null;
		}

		MdiGroup group = null;

		Component ancestor = c.getParent();
		while (ancestor != null) {
			if (ancestor instanceof MdiGroup) {
				group = (MdiGroup) ancestor;
				break;
			}
			ancestor = ancestor.getParent();
		}
		return group;
	}
}
