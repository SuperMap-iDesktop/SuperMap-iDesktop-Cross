package com.supermap.desktop.process.parameter.implement;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/6/28.
 */
public class ParameterSQLExpression extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "value")
	private String expression;
	private String describe;
	private Dataset selectDataset;
	private int anchor;

	public ParameterSQLExpression() {
		this("");
	}


	public ParameterSQLExpression(String describe) {
		this.describe = describe;
	}

	public int getAnchor() {
		return anchor;
	}

	public void setAnchor(int anchor) {
		this.anchor = anchor;
	}

	public Dataset getSelectDataset() {
		return selectDataset;
	}

	public void setSelectDataset(Dataset selectDataset) {
		this.selectDataset = selectDataset;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof String) {
			String oldValue = this.expression;
			this.expression = (String) item;
			firePropertyChangeListener(new PropertyChangeEvent(this, PROPERTY_VALE, oldValue, this.expression));
		}
	}


	@Override
	public Object getSelectedItem() {
		return expression;
	}

	@Override
	public String getType() {
		return ParameterType.SQL_EXPRESSION;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}
}
