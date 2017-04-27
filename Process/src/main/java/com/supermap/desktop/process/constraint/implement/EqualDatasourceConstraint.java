package com.supermap.desktop.process.constraint.implement;

import com.supermap.data.Datasource;
import com.supermap.desktop.process.parameter.events.ParameterValueSelectedEvent;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class EqualDatasourceConstraint extends DefaultConstraint {

	private Datasource datasource;

	@Override
	public Object isValueSelected(ParameterValueSelectedEvent event) {
		for (ParameterNode parameterNode : parameterNodes) {
			if (parameterNode.getParameter() == event.getParameter() && parameterNode.getName().equals(event.getFieldName())) {
				return datasource;
			}
		}
		return AbstractParameter.DO_NOT_CARE;
	}

	@Override
	protected void constrainedHook(final IParameter parameter, final String name) {
		parameter.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(name)) {
					datasource = (Datasource) evt.getNewValue();
					fireConstraintChanged(parameter);
				}
			}
		});
	}

	private void fireConstraintChanged(IParameter parameter) {
		for (ParameterNode parameterNode : parameterNodes) {
			if (parameterNode.getParameter() != parameter) {
				parameterNode.getParameter().fireFieldConstraintChanged(parameterNode.getName());
			}
		}
	}
}
