package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.data.GeoRegion;
import com.supermap.desktop.ui.controls.SmDialog;

/**
 * @author YuanR
 *         2017.3.21
 *         地图裁剪主窗体
 */
public class DialogMapClip extends SmDialog {
	GeoRegion geoRegion;
	public DialogMapClip(GeoRegion region) {
		super();
		geoRegion=region;
	}
}

