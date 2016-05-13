package com.supermap.desktop.controls.colorScheme;

import com.supermap.desktop.Application;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author XiaJT
 */
public class ColorHSV {
	public static final double MaxSaturation = 1;
	public static final double MinSaturation = 0;
	public static final double MaxValue = 1;
	public static final double MinValue = 0;
	public static final double MaxHue = 360;
	public static final double MinHue = 0;

	private double h;
	private double s;
	private double v;

	public ColorHSV() {

	}

	public ColorHSV(double h, double s, double v) {
		this.h = h;
		this.s = s;
		this.v = v;
	}

	public void fromColor(Color color) {

		try {
			double r = ((double) color.getRed() / 255.0);
			double g = ((double) color.getGreen() / 255.0);
			double b = ((double) color.getBlue() / 255.0);

			double max = Math.max(r, Math.max(g, b));
			double min = Math.min(r, Math.min(g, b));

			double h = 0.0;
			if (max == r && g >= b) {
				if (max - min == 0) {
					h = 0.0;
				} else {
					h = 60 * (g - b) / (max - min);
				}
			} else if (max == r && g < b) {
				h = 60 * (g - b) / (max - min) + 360;
			} else if (max == g) {
				h = 60 * (b - r) / (max - min) + 120;
			} else if (max == b) {
				h = 60 * (r - g) / (max - min) + 240;
			}

			double s = (max == 0) ? 0.0 : (1.0 - ((double) min / (double) max));


			this.h = h;
			this.s = s;
			this.v = max;
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public Color ToColor() {
		Color color = null;

		try {
			double r = 0;
			double g = 0;
			double b = 0;

			// 如果饱和度为零，则为灰度图
			if (s == 0) {
				r = this.v;
				g = this.v;
				b = this.v;
			} else {
				// 整个颜色空间被划分成六个扇区，先按照色调计算在哪一个扇区中。
				double sectorPos = this.h / 60.0;
				int sectorNumber = (int) (Math.floor(sectorPos));

				// 计算扇区的小数部分
				double fractionalSector = sectorPos - sectorNumber;

				// 计算颜色的三个轴的数值
				double p = this.v * (ColorHSV.MaxSaturation - this.s);
				double q = this.v * (ColorHSV.MaxSaturation - (this.s * fractionalSector));
				double t = this.v * (ColorHSV.MaxSaturation - (this.s * (Math.abs(this.s - fractionalSector))));

				// assign the fractional colors to r, g, and b based on the sector the angle is in.
				switch (sectorNumber) {
					case 0:
						r = this.v;
						g = t;
						b = p;
						break;
					case 1:
						r = q;
						g = this.v;
						b = p;
						break;
					case 2:
						r = p;
						g = this.v;
						b = t;
						break;
					case 3:
						r = p;
						g = q;
						b = this.v;
						break;
					case 4:
						r = t;
						g = p;
						b = this.v;
						break;
					case 5:
						r = this.v;
						g = p;
						b = q;
						break;
				}
			}

			DecimalFormat df = new DecimalFormat("0.00");
			color = new Color((int) ((double) Double.valueOf(df.format(r * 255))),
					(int) ((double) Double.valueOf(df.format(g * 255))),
					(int) ((double) Double.valueOf(df.format(b * 255))));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return color;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getS() {
		return s;
	}

	public void setS(double s) {
		this.s = s;
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}
}
