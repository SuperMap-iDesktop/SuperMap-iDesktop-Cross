package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;

/**
 * @author XiaJT
 */
public abstract class DefaultParameterPanel implements IParameterPanel, FieldConstraintChangedListener {
	protected IParameter parameter;

	DefaultParameterPanel(IParameter parameter) {
		this.parameter = parameter;
		parameter.addFieldConstraintChangedListener(this);
	}

	/**
	 * 字段值改变时会触发此事件
	 *
	 * @param event
	 */
	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {

	}
}
