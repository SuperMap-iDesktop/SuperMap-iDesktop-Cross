package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.DatasetType;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.properties.CommonProperties;

/**
 * Created by lixiaoyao on 2017/7/22.
 */
public class MetaProcessDualLineToCenterLine extends MetaProcessCenterLine {

	public DatasetType getSonDatasetType() {
		return DatasetType.LINE;
	}

	public String getResultDatasetName(){
		return "result_DualLineToCenterLine";
	}

	public String getSonKey(){
		return MetaKeys.DualLineToCenterLine;
	}

	public String getSonTitle(){
		return CommonProperties.getString("String_DualLineToCenterLine");
	}
}
