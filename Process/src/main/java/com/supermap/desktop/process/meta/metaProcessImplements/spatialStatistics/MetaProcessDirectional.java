package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;

/**
 * @author XiaJT
 */
public class MetaProcessDirectional extends MetaProcess {
	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Directional");
	}

	@Override
	public void run() {

	}

	@Override
	public String getKey() {
		return MetaKeys.Directional;
	}
}
