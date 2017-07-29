package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/6/28.
 */
public class ParameterSQLExpression extends AbstractParameter implements ISelectionParameter {

	@ParameterField(name = "value")
	private String expression;
	private String describe;
    private int anchor;
    public static final String DATASET_FIELD_NAME = "Dataset";
    @ParameterField(name = DATASET_FIELD_NAME)
    private Dataset selectDataset;

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
        Dataset oldValue = this.selectDataset;
        this.selectDataset = selectDataset;
        firePropertyChangeListener(new PropertyChangeEvent(this, DATASET_FIELD_NAME, oldValue, this.selectDataset));
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
