package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;

/**
 * @author XiaJT
 */
public class ParameterBigDatasourceDatasource extends ParameterDatasourceConstrained {
	public ParameterBigDatasourceDatasource() {
		super();
	}

	@Override
	protected boolean isDatasourceValueLegal(Datasource parameterValue) {
		return parameterValue != null && parameterValue.getEngineType() == EngineType.POSTGRESQL;
	}
}
