package com.supermap.desktop.utilities;

import com.supermap.data.TransformationMode;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJT
 */
public class TransformationModeUtilities {
	private TransformationModeUtilities() {

	}

	public static String toString(TransformationMode value) {
		if (value == TransformationMode.OFFSET) {
			return CoreProperties.getString("String_TransformationModeOFFSET");
		} else if (value == TransformationMode.RECT) {
			return CoreProperties.getString("String_TransformationModeRECT");
		} else if (value == TransformationMode.LINEAR) {
			return CoreProperties.getString("String_TransformationModeLINEAR");
		} else if (value == TransformationMode.SQUARE) {
			return CoreProperties.getString("String_TransformationModeSQUARE");
		}
		return "";
	}
}
