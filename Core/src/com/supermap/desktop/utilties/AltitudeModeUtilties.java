package com.supermap.desktop.utilties;

import com.supermap.data.AltitudeMode;
import com.supermap.desktop.properties.CoreProperties;

/**
 * @author XiaJt
 */
public class AltitudeModeUtilties {
	private AltitudeModeUtilties() {
		// 工具类不提供构造函数
	}

	public static String toString(AltitudeMode altitudeMode) {
		if (altitudeMode == AltitudeMode.ABSOLUTE) {
			return CoreProperties.getString("String_AltitudeMode_Absolute");
		} else if (altitudeMode == AltitudeMode.ABSOLUTE_UNDER_GROUND) {
			return CoreProperties.getString("String_AltitudeMode_ABSOLUTE_UNDER_GROUND");
		} else if (altitudeMode == AltitudeMode.CLAMP_TO_GROUND) {
			return CoreProperties.getString("String_AltitudeMode_ClampToGround");
		} else if (altitudeMode == AltitudeMode.RELATIVE_TO_GROUND) {
			return CoreProperties.getString("String_AltitudeMode_RelativeToGround");
		} else if (altitudeMode == AltitudeMode.RELATIVE_UNDER_GROUND) {
			return CoreProperties.getString("String_AltitudeMode_RelativeToUnderground");
		}
		return "";
	}

	public static AltitudeMode getAltitudeMode(String s) {
		if (StringUtilties.isNullOrEmpty(s)) {
			return AltitudeMode.ABSOLUTE;
		} else if (CoreProperties.getString("String_AltitudeMode_Absolute").equals(s)) {
			return AltitudeMode.ABSOLUTE;
		} else if (CoreProperties.getString("String_AltitudeMode_ABSOLUTE_UNDER_GROUND").equals(s)) {
			return AltitudeMode.ABSOLUTE_UNDER_GROUND;
		} else if (CoreProperties.getString("String_AltitudeMode_ClampToGround").equals(s)) {
			return AltitudeMode.CLAMP_TO_GROUND;
		} else if (CoreProperties.getString("String_AltitudeMode_RelativeToGround").equals(s)) {
			return AltitudeMode.RELATIVE_TO_GROUND;
		} else if (CoreProperties.getString("String_AltitudeMode_RelativeToUnderground").equals(s)) {
			return AltitudeMode.RELATIVE_UNDER_GROUND;
		}
		return AltitudeMode.ABSOLUTE;
	}
}
