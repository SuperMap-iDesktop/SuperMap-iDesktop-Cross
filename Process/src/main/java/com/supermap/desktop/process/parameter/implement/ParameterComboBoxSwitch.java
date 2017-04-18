package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class ParameterComboBoxSwitch extends AbstractParameter {
	private ParameterCombine parameterCombine = new ParameterCombine();
	private ParameterComboBox comboBox = new ParameterComboBox();
	private ParameterSwitch parameterSwitch = new ParameterSwitch();

	public ParameterComboBoxSwitch() {
		comboBox.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					IParameter parameter = (IParameter) ((ParameterDataNode) evt.getNewValue()).getData();
					parameterSwitch.switchParameter(parameter);
				}
			}
		});
		parameterCombine.addParameters(comboBox, parameterSwitch);
	}

	public void addParameter(String describe, IParameter parameter) {
		if (parameters != null) {
			parameter.setParameters(parameters);
		}
		comboBox.addItem(new ParameterDataNode(describe, parameter));
		parameterSwitch.add(describe, parameter);
	}

	@Override
	public void setParameters(IParameters parameters) {
		super.setParameters(parameters);
		comboBox.setParameters(parameters);
		parameterSwitch.setParameters(parameters);
		parameterCombine.setParameters(parameters);
	}

	@Override
	public IParameterPanel getParameterPanel() {
		return parameterCombine.getParameterPanel();
	}

	public String getCurrentTag() {
		return ((ParameterDataNode) comboBox.getSelectedItem()).getDescribe();
	}

	public IParameter getCurrentParameter() {
		return parameterSwitch.getCurrentParameter();
	}

	@Override
	public String getType() {
		return ParameterType.COMBO_BOX_SWITCH;
	}

	@Override
	public String getDescribe() {
		return comboBox.getDescribe();
	}

	public void setDescribe(String describe) {
		comboBox.setDescribe(describe);
	}
}
