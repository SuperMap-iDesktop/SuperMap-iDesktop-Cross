package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;

/**
 * Created by caolp on 2017-05-26.
 */
public class MetaProcessPolygonAggregation extends MetaProcess {
	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_PolygonAggregation");
	}

	@Override
	public IParameterPanel getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public String getKey() {
		return MetaKeys.POLYGON_AGGREGATION;
	}
}
