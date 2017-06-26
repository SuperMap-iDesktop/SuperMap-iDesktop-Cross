package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Datasource;
import com.supermap.data.EngineType;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.events.ParameterValueSelectedEvent;

/**
 * @author XiaJT
 */
public class ParameterBigDatasourceDatasource extends ParameterDatasourceConstrained {
	public ParameterBigDatasourceDatasource() {
		super();
		this.addValueLegalListener(new ParameterValueLegalListener() {
			@Override
			public boolean isValueLegal(ParameterValueLegalEvent event) {
				if (event.getFieldName().equals(ParameterDatasource.DATASOURCE_FIELD_NAME)) {
					Object parameterValue = event.getParameterValue();
					if (parameterValue instanceof Datasource) {
						if (((Datasource) parameterValue).getEngineType() == EngineType.DATASERVER) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public Object isValueSelected(ParameterValueSelectedEvent event) {
				return ParameterValueLegalListener.DO_NOT_CARE;
			}
		});
	}
}
