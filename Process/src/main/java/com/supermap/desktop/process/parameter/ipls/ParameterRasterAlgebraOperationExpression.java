package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by lixiaoyao on 2017/8/29.
 */
public class ParameterRasterAlgebraOperationExpression extends AbstractParameter implements ISelectionParameter {
	public static final String RASTER_EXPRESSION = "RasterExpression";
	private String expression;
	private String describe;
	private int anchor;
	public ParameterRasterAlgebraOperationExpression(){
		this("");
	}

	public ParameterRasterAlgebraOperationExpression(String describe){
		this.describe=describe;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item instanceof String) {
			String oldValue = this.expression;
			this.expression = (String) item;
			firePropertyChangeListener(new PropertyChangeEvent(this, RASTER_EXPRESSION, oldValue, this.expression));
		}
	}

	@Override
	public Object getSelectedItem() {
		return expression;
	}

	@Override
	public String getType() {
		return ParameterType.RASTER_EXPRESSION;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}

	public int getAnchor() {
		return anchor;
	}

	public void setAnchor(int anchor) {
		this.anchor = anchor;
	}
}
