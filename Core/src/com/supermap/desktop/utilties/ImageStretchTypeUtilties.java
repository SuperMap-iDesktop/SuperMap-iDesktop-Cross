package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.ImageStretchTypeProperties;
import com.supermap.mapping.ImageStretchType;

public class ImageStretchTypeUtilties {

	private ImageStretchTypeUtilties() {
		// 工具类不提供构造函数
	}

	public static String toString(ImageStretchType data) {
		String result = "";

		try {
			if (data == ImageStretchType.NONE) {
				result = ImageStretchTypeProperties.getString(ImageStretchTypeProperties.None);
			} else if (data == ImageStretchType.GAUSSIAN) {
				result = ImageStretchTypeProperties.getString(ImageStretchTypeProperties.Gaussian);
			} else if (data == ImageStretchType.HISTOGRAMEQUALIZATION) {
				result = ImageStretchTypeProperties.getString(ImageStretchTypeProperties.HistogramEqualization);
			} else if (data == ImageStretchType.HISTOGRAMSPECIFICATION) {
				result = ImageStretchTypeProperties.getString(ImageStretchTypeProperties.HistogramSpecification);
			} else if (data == ImageStretchType.MINIMUMMAXIMUM) {
				result = ImageStretchTypeProperties.getString(ImageStretchTypeProperties.MinimumMaximum);
			} else if (data == ImageStretchType.STANDARDDEVIATION) {
				result = ImageStretchTypeProperties.getString(ImageStretchTypeProperties.StandardDeviation);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static ImageStretchType valueOf(String text) {
		ImageStretchType result = ImageStretchType.NONE;

		try {
			if (text.equalsIgnoreCase(ImageStretchTypeProperties.getString(ImageStretchTypeProperties.None))) {
				result = ImageStretchType.NONE;
			} else if (text.equalsIgnoreCase(ImageStretchTypeProperties.getString(ImageStretchTypeProperties.Gaussian))) {
				result = ImageStretchType.GAUSSIAN;
			} else if (text.equalsIgnoreCase(ImageStretchTypeProperties.getString(ImageStretchTypeProperties.HistogramEqualization))) {
				result = ImageStretchType.HISTOGRAMEQUALIZATION;
			} else if (text.equalsIgnoreCase(ImageStretchTypeProperties.getString(ImageStretchTypeProperties.HistogramSpecification))) {
				result = ImageStretchType.HISTOGRAMSPECIFICATION;
			} else if (text.equalsIgnoreCase(ImageStretchTypeProperties.getString(ImageStretchTypeProperties.MinimumMaximum))) {
				result = ImageStretchType.MINIMUMMAXIMUM;
			} else if (text.equalsIgnoreCase(ImageStretchTypeProperties.getString(ImageStretchTypeProperties.StandardDeviation))) {
				result = ImageStretchType.STANDARDDEVIATION;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}
}
