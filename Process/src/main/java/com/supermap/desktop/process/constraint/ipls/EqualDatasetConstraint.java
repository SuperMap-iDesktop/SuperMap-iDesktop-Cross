package com.supermap.desktop.process.constraint.ipls;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.events.ParameterValueSelectedEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class EqualDatasetConstraint extends DefaultConstraint {
	private Dataset dataset;

	@Override
	public Object isValueSelected(ParameterValueSelectedEvent event) {
		for (ParameterNode parameterNode : parameterNodes) {
			if (parameterNode.getParameter() == event.getParameter() && parameterNode.getName().equals(event.getFieldName())) {
				return dataset;
			}
		}
		return ParameterValueLegalListener.DO_NOT_CARE;
	}

	@Override
	public void constrainedHook(final IParameter parameter, final String name) {
		parameter.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(name)) {
					dataset = (Dataset) evt.getNewValue();
					fireConstraintChanged(parameter);
				}
			}
		});
	}

	private void fireConstraintChanged(IParameter parameter) {
		for (ParameterNode parameterNode : parameterNodes) {
			if (parameterNode.getParameter() != parameter) {
				parameterNode.getParameter().setFieldVale(parameterNode.getName(), dataset);
				parameterNode.getParameter().fireFieldConstraintChanged(parameterNode.getName());
			}
		}
	}
}
