package com.supermap.desktop.utilities;

import com.supermap.analyst.spatialanalyst.ResampleMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.ResampleModeProperties;

/**
 * Created By Chens on 2017/8/14 0014
 */
public class ResampleModeUtilities {
	public ResampleModeUtilities() {
		//不提供构造方法
	}

	public static String toString(ResampleMode mode) {
		String result = "";

		try {
			if (mode == ResampleMode.BILINEAR) {
				result = ResampleModeProperties.getString(ResampleModeProperties.BILINEAR);
			} else if (mode == ResampleMode.CUBIC) {
				result = ResampleModeProperties.getString(ResampleModeProperties.CUBIC);
			} else if (mode == ResampleMode.NEAREST) {
				result = ResampleModeProperties.getString(ResampleModeProperties.NEAREST);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static ResampleMode valueOf(String text) {
		ResampleMode mode = ResampleMode.BILINEAR;

		try {
			if (text.equals(ResampleModeProperties.getString(ResampleModeProperties.BILINEAR))) {
				mode = ResampleMode.BILINEAR;
			} else if (text.equals(ResampleModeProperties.getString(ResampleModeProperties.CUBIC))) {
				mode = ResampleMode.CUBIC;
			} else if (text.equals(ResampleModeProperties.getString(ResampleModeProperties.NEAREST))) {
				mode = ResampleMode.NEAREST;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return mode;
	}
}
