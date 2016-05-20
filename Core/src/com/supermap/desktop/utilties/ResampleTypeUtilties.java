package com.supermap.desktop.utilties;

import com.supermap.data.ResampleType;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.ResampleTypeProperties;

/**
 * 重采样方法工具类
 * 
 * @author highsad
 *
 */
public class ResampleTypeUtilties {

	private ResampleTypeUtilties() {
		// 工具类，不提供构造方法
	}

	public static String toString(ResampleType type) {
		String result = "";

		try {
			if (type == ResampleType.RTBEND) {
				result = ResampleTypeProperties.getString(ResampleTypeProperties.RTBend);
			} else if (type == ResampleType.RTGENERAL) {
				result = ResampleTypeProperties.getString(ResampleTypeProperties.RTGeneral);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	public static ResampleType valueOf(String text) {
		ResampleType type = ResampleType.RTBEND;

		try {
			if (text.equals(ResampleTypeProperties.getString(ResampleTypeProperties.RTBend))) {
				type = ResampleType.RTBEND;
			} else if (text.equals(ResampleTypeProperties.getString(ResampleTypeProperties.RTGeneral))) {
				type = ResampleType.RTGENERAL;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return type;
	}
}
