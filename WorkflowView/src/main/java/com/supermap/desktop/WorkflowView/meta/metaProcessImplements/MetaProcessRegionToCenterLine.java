package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by lixiaoyao on 2017/7/22.
 */
public class MetaProcessRegionToCenterLine extends MetaProcessCenterLine{
	public DatasetType getSonDatasetType() {
		return DatasetType.REGION;
	}

	public String getResultDatasetName(){
		return "result_RegionToCenterLine";
	}

	public String getSonKey(){
		return MetaKeys.REGION_TO_CENTERLINE;
	}

	public String getSonTitle(){
		return CommonProperties.getString("String_RegionToCenterLine");
	}
}
