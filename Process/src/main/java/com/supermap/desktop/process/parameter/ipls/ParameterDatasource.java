package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Datasource;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CommonProperties;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterDatasource extends AbstractParameter implements ISelectionParameter {

	public static final String DATASOURCE_FIELD_NAME = "DATASOURCE_FIELD_NAME";

	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource datasource;
	private String describe = CommonProperties.getString(CommonProperties.Label_Datasource);

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
	public Object getSelectedItem() {
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
