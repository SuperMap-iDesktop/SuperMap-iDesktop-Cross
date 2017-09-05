package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Colors;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by yuanR on 2017/9/5 0005.
 * 栅格颜色表参数
 */
public class ParameterColorsTable extends AbstractParameter implements ISelectionParameter {
	@ParameterField(name = "value")
	private Colors value;
	private String describe;

	public ParameterColorsTable(String describe) {
		this.describe = describe;
	}

	public ParameterColorsTable() {
		this("");
	}

	@Override
	public String getType() {
		return ParameterType.COLORSTABLE;
	}


	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof Colors) {
			Object oldValue = this.value;
			this.value = (Colors) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, this.value));
		}
	}

	@Override
	public Object getSelectedItem() {
		return this.value;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterColorsTable setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}
