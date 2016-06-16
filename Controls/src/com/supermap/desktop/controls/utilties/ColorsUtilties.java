package com.supermap.desktop.controls.utilties;

import com.supermap.data.Colors;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.colorScheme.ColorHSV;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author XiaJT
 */
public class ColorsUtilties {
	/**
	 * 生成随机色
	 *
	 * @param colorCount 颜色总数
	 * @param keyColors  关键色
	 * @return
	 */
	public static Colors buildRandom(int colorCount, Color[] keyColors) {
		Colors colors = new Colors();
		// 要产生的颜色比关键色还要少，太可怜了
		// 就不再进行随机色生成了，直接将随机色扔出去算了
		if (colorCount <= keyColors.length) {
			for (int index = 0; index < colorCount; index++) {
				colors.add(keyColors[index]);
			}
		} else {
			// 按照关键色计算每段需要生成多少随机色
			int colorCountPerSection = (int) (Math.floor(colorCount * 1.0 / (keyColors.length - 1)));
			int colorCountLastSection = colorCount - colorCountPerSection * (keyColors.length - 1);
			for (int index = 0; index < (keyColors.length - 1); index++) {
				colors.addRange(build(colorCountPerSection, keyColors[index], keyColors[index + 1]));
			}
			if (colorCountLastSection > 0) {
				colors.addRange(build(colorCountLastSection, keyColors[keyColors.length - 2], keyColors[keyColors.length - 1]));
			}
		}

		return colors;
	}

	private static Color[] build(int colorCount, Color startColor, Color endColor) {
		java.util.List<Color> colors = new ArrayList<>();

		try {

			ColorHSV startHSV = new ColorHSV();
			ColorHSV endHSV = new ColorHSV();
			startHSV.fromColor(startColor);
			endHSV.fromColor(endColor);

			colors.addAll(bulid(colorCount / 2, 12, startHSV));
			colors.addAll(bulid(colorCount - colorCount / 2, 12, endHSV));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return (Color[]) colors.toArray(new Color[colors.size()]);
	}


	/**
	 * 指定色调区块取随机色
	 */
	private static List<Color> bulid(int colorCount, int hueCount, ColorHSV colorHSV) {
		List<Color> colors = new ArrayList<>();
		try {
			double increment = 360 / hueCount;

			if (colorHSV.getV() < Math.pow(Math.E, -10)
					|| (Math.abs(colorHSV.getV() - 1.0) < Math.pow(Math.E, -10) && colorHSV.getS() < Math.pow(Math.E, -10))) {
				colors.addAll(Build(colorCount, colorHSV.getH(), colorHSV.getH(), colorHSV.getS(), colorHSV.getS(), 1, 0));
			} else {
				int currentSegment = (int) Math.floor(colorHSV.getH() / increment);
				colors.addAll(Build(colorCount, increment * (currentSegment + 1), increment * currentSegment, 1, 0, colorHSV.getV(), colorHSV.getV()));
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}


		return colors;
	}

	public static List<Color> Build(int colorCount,
	                                double endHue,
	                                double startHue,
	                                double maxSaturation,
	                                double minSaturation,
	                                double maxValue,
	                                double minValue) {
		if (endHue > 360) {
			endHue = 360;
		}

		if (startHue < 0) {
			startHue = 0;
		}

		if (maxSaturation > 1) {
			maxSaturation = 1;
		}

		if (minSaturation < 0) {
			minSaturation = 0;
		}

		if (maxValue > 1) {
			maxValue = 1;
		}

		if (minValue < 0) {
			minValue = 0;
		}

		List<Color> colors = new ArrayList<>();
		Random random = new Random();

		int tempMaxSaturation = ((int) (maxSaturation * 100));
		int tempMinSaturation = (int) (minSaturation * 100);

		int tempMaxValue = (int) (maxValue * 100);
		int tempMinValue = (int) (minValue * 100);

		int tempEndHue = (int) (endHue * 100);
		int tempStartHue = (int) (startHue * 100);

		for (int index = 0; index < colorCount; index++) {
			int tempS = tempMaxSaturation - tempMinSaturation;
			int saturation = tempMinSaturation;
			if (tempS != 0) {
				saturation = random.nextInt(tempS) + tempMinSaturation;
			}
			int tempValue = tempMaxValue - tempMinValue;
			int value = tempMinValue;
			if (tempValue != 0) {
				value = random.nextInt(tempValue) + tempMinValue;
			}

			int hue = tempStartHue;

			if (endHue < startHue) {
				int tempH = 36000 + tempEndHue - tempStartHue;
				if (tempH != 0) {
					hue = random.nextInt(tempH) + tempStartHue;
				}
				if (hue > 36000) {
					hue = hue - 36000;
				}
			} else {
				int tempH = tempEndHue - tempStartHue;
				if (tempH != 0) {
					hue = random.nextInt(tempH) + tempStartHue;
				}
			}

			colors.add(new ColorHSV(hue * 0.01, saturation * 0.01, value * 0.01).ToColor());
		}

		return colors;
	}

	public static boolean isEqualsColors(Colors c1, Colors c2) {
		if (c1 == c2) {
			return true;
		}
		if (c1 == null || c2 == null) {
			return false;
		}

		if (c1.getCount() != c2.getCount()) {
			return false;
		}

		for (int i = 0; i < c1.getCount(); i++) {
			if (!c1.get(i).equals(c2.get(i))) {
				return false;
			}
		}
		return true;
	}
}
