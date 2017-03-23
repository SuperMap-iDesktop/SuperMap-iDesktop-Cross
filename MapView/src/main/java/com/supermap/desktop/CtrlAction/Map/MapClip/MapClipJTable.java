package com.supermap.desktop.CtrlAction.Map.MapClip;

import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;

/**
 * @author YuanR
 */
public class MapClipJTable extends MutiTable {

	private static final int COLUMN_INDEX_IsClip = 0;
	private static final int COLUMN_INDEX_LayerCaption = 1;
	private static final int COLUMN_INDEX_AimDatasource = 2;
	private static final int COLUMN_INDEX_AimDataset = 3;

	private String[] title = {MapViewProperties.getString("String_MapClip_IsClip"),
			MapViewProperties.getString("String_MapClip_LayerCaption"),
			MapViewProperties.getString("String_MapClip_TargetDatasource"),
			MapViewProperties.getString("String_MapClip_TargetDataset")};

	public MapClipJTable() {
		super();


	}
}
