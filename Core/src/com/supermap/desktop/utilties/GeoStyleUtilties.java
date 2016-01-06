package com.supermap.desktop.utilties;

import java.awt.Color;
import java.util.Random;

import com.supermap.desktop.Application;

public class GeoStyleUtilties {
	static Random random;// 用于保存产生随机的线风格颜色的Random对象
	static Color[] fillColors;
	static int fillNum;

	/**
	 * 对象风格的候选填充颜色集合
	 * 
	 * @return
	 */
	private GeoStyleUtilties() {
		// 工具类不提供构造函数
	}

	private static Color[] getFillColors() {
		if (fillColors == null) {
			fillColors = new Color[10];
			fillColors[0] = new Color(224, 207, 226);
			fillColors[1] = new Color(151, 191, 242);
			fillColors[2] = new Color(242, 242, 186);
			fillColors[3] = new Color(190, 255, 232);
			fillColors[4] = new Color(255, 190, 232);
			fillColors[5] = new Color(255, 190, 190);
			fillColors[6] = new Color(255, 235, 175);
			fillColors[7] = new Color(233, 255, 190);
			fillColors[8] = new Color(234, 225, 168);
			fillColors[9] = new Color(174, 241, 176);
		}
		return fillColors;
	}

	/**
	 * 不同于上次选用的填充颜色的颜色
	 * 
	 * @return
	 */
	public static Color getFillColor() {

		Color result = Color.PINK;
		if (fillNum >= getFillColors().length) {
			fillNum = 0;
		}
		result = getFillColors()[fillNum];
		fillNum++;
		return result;

	}

	/**
	 * 获取随机的用于线风格的颜色
	 * 
	 * @return
	 */
	public static Color getLineColor() {

		return getRandomLineColor();
	}

	/**
	 * 产生随机的用于线风格的颜色 经过初步试验，新产生的线颜色，饱和度【0-240】最好在30-100之间 亮度【0-240】最好在75-120之间
	 * 
	 * @return
	 */
	private static Color getRandomLineColor() {
		Color result = Color.PINK;
		try {
			if (random == null) {
				random = new Random();
			}
			result = Color.getHSBColor((float) random.nextInt(360), (float) (random.nextInt(100) / 240.0), (float) (random.nextInt(120) / 240.0));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}
}
