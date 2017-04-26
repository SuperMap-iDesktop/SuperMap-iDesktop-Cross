package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;

/**
 * Created by hanyz on 2017/4/25.
 */
public class ParameterDatasourceConstrained extends ParameterDatasource {

	public ParameterDatasourceConstrained() {
		super();
		DatasourceConstraint.getInstance().constrained(this, ParameterDatasource.DATASOURCE_FIELD_NAME);
	}
}
