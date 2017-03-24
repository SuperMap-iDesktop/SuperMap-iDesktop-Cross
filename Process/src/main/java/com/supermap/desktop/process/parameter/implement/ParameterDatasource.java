package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Datasource;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterDatasource extends AbstractParameter implements ISelectionParameter {

	public static final String DATASOURCE_FIELD_NAME = "datasourceFieldName";

	@ParameterField(name = DATASOURCE_FIELD_NAME)
	private Datasource datasource;
    private String describe;

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
