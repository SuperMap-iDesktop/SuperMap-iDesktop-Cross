package com.supermap.desktop.utilities;

import com.supermap.data.Colors;

import java.awt.*;

public class ColorsUtilities {
	private ColorsUtilities(){
		// 工具类不提供构造函数
	}
	public static boolean isColorsEqual(Colors colors1, Colors colors2) {
		if (colors1 == colors2) {
			return true;
		}
		if (colors1 == null && colors2 != null) {
			return false;
		}

		if (colors1 != null && colors2 == null) {
			return false;
		}

		if (colors1.getCount() != colors2.getCount()) {
			return false;
		}

		for (int i = 0; i < colors1.getCount(); i++) {
			Color color1 = colors1.get(i);
			Color color2 = colors2.get(i);
			if (color1.getRGB() != color2.getRGB()) {
				return false;
			}
		}

		return true;
	}

	//获取ABGR值（大端RGB，小端BGR,先按照引擎的ABGR传入）
	public static long getColorABGRValue(Color color) {
		if (color == null) {
			return 0;
		}
		long red = color.getRed();
		long green = color.getGreen();
		long blue = color.getBlue();
		long alpha = color.getAlpha();
		long result = (alpha << 24) + (blue << 16) + (green << 8) + red;
		return result;
	}
}
