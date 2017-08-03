package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Datasource;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalEvent;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.events.ParameterValueSelectedEvent;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;

import java.beans.PropertyChangeEvent;

import static com.supermap.desktop.process.parameter.events.ParameterValueLegalListener.DO_NOT_CARE;

/**
 * @author XiaJT
 */
public class ParameterDatasource extends AbstractParameter implements ISelectionParameter {

	public static final String DATASOURCE_FIELD_NAME = "DATASOURCE_FIELD_NAME";

	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource datasource;
	private String describe = CommonProperties.getString(CommonProperties.Label_Datasource);

	public ParameterDatasource() {
		this.addValueLegalListener(new ParameterValueLegalListener() {
			@Override
			public boolean isValueLegal(ParameterValueLegalEvent event) {
				if (event.getFieldName().equals(ParameterDatasource.DATASOURCE_FIELD_NAME)) {
					Object parameterValue = event.getParameterValue();
					if (parameterValue instanceof Datasource) {
						return isDatasourceValueLegal(((Datasource) parameterValue));
					}
				}
				return false;
			}

			@Override
			public Object isValueSelected(ParameterValueSelectedEvent event) {
				if (event.getFieldName().equals(DATASOURCE_FIELD_NAME)) {
					if (event.getParameterValue() == null || event.getParameterValue() instanceof Datasource) {
						return isDatasourceSelected(((Datasource) event.getParameterValue()));
					}
				}
				return DO_NOT_CARE;
			}
		});
	}

	protected Object isDatasourceSelected(Datasource parameterValue) {
		return DO_NOT_CARE;
	}

	protected boolean isDatasourceValueLegal(Datasource parameterValue) {
		return true;
	}

	@Override
	public String getType() {
		return ParameterType.DATASOURCE;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof Datasource) {
			Datasource oldValue = this.datasource;
			datasource = (Datasource) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, DATASOURCE_FIELD_NAME, oldValue, datasource));
		}
	}


	@Override
	public Datasource getSelectedItem() {
		return datasource;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public void dispose() {

	}
}
