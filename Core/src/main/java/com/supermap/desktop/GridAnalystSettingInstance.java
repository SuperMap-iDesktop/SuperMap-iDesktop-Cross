package com.supermap.desktop;

import com.supermap.analyst.spatialanalyst.GridAnalystSetting;

/**
 * @author XiaJT
 */
public class GridAnalystSettingInstance {
	private static GridAnalystSettingInstance ourInstance = new GridAnalystSettingInstance();

	public static GridAnalystSettingInstance getInstance() {
		return ourInstance;
	}

	private GridAnalystSettingInstance() {
		GridAnalystSetting gridAnalystSetting = new GridAnalystSetting();

	}
}
