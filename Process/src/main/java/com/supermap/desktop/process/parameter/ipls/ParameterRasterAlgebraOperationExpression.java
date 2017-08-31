package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.PixelFormat;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by lixiaoyao on 2017/8/29.
 */
public class ParameterRasterAlgebraOperationExpression extends AbstractParameter implements ISelectionParameter {
	public static final String RASTER_EXPRESSION = "RasterExpression";
	private String describe;
	private int anchor;
	private String expression = "";
	private PixelFormat pixelFormat = null;
	private boolean isZip = false;
	private boolean isIgnoreNoValue = false;


	public ParameterRasterAlgebraOperationExpression() {
		this("");
	}

	public ParameterRasterAlgebraOperationExpression(String describe) {
		this.describe = describe;
	}

	@Override
	public void setSelectedItem(Object item) {
		// 只是为了激活监听，去刷新面板
		firePropertyChangeListener(new PropertyChangeEvent(this, RASTER_EXPRESSION, null, item));
	}

	@Override
	public Object getSelectedItem() {
		return null;
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


	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public PixelFormat getPixelFormat() {
		return this.pixelFormat;
	}

	public void setPixelFormat(PixelFormat pixelFormat) {
		this.pixelFormat = pixelFormat;
	}

	public boolean isZip() {
		return this.isZip;
	}

	public void setZip(boolean zip) {
		this.isZip = zip;
	}

	public boolean isIgnoreNoValue() {
		return this.isIgnoreNoValue;
	}

	public void setIgnoreNoValue(boolean ignoreNoValue) {
		this.isIgnoreNoValue = ignoreNoValue;
	}
}
