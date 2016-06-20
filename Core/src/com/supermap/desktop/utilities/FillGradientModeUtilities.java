package com.supermap.desktop.utilities;

import com.supermap.data.FillGradientMode;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJt
 */
public class FillGradientModeUtilities {

	private FillGradientModeUtilities() {

	}

	public static FillGradientMode getFillGradientMode(String s) {
		if (s.equals(CoreProperties.getString("String_LINEAR"))) {
			return FillGradientMode.LINEAR;
		} else if (s.equals(CoreProperties.getString("String_SQUARE"))) {
			return FillGradientMode.SQUARE;
		} else if (s.equals(CoreProperties.getString("String_RADIAL"))) {
			return FillGradientMode.RADIAL;
		} else if (s.equals(CoreProperties.getString("String_None"))) {
			return FillGradientMode.NONE;
		} else if (s.equals(CoreProperties.getString("String_CONICAL"))) {
			return FillGradientMode.CONICAL;
		}
		throw new UnsupportedOperationException("unSupport Or Wrong String" + s);
	}

	public static String getFillGradientMode(FillGradientMode mode) {
		if (mode == FillGradientMode.LINEAR) {
			return CoreProperties.getString("String_LINEAR");
		} else if (mode == FillGradientMode.SQUARE) {
			return CoreProperties.getString("String_SQUARE");
		} else if (mode == FillGradientMode.RADIAL) {
			return CoreProperties.getString("String_RADIAL");
		} else if (mode == FillGradientMode.NONE) {
			return CoreProperties.getString("String_None");
		} else if (mode == FillGradientMode.CONICAL) {
			return CoreProperties.getString("String_CONICAL");
		}
		throw new UnsupportedOperationException("unSupport Or Wrong Mode" + mode);
	}
}
