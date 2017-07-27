package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/21.
 */
public class ParameterTextArea extends AbstractParameter implements ISelectionParameter {
	private String describe;

	@ParameterField(name = "value")
	private String selectItem;
	private boolean isLineWrap=false;
	private boolean isWrapStyleWord=false;

    public ParameterTextArea() {
        this("");
    }

	public ParameterTextArea(String describe) {
		this.describe = describe;
	}

    @Override
    public void setSelectedItem(Object item) {
        if (item instanceof String) {
            String oldValue = (String) item;
            this.selectItem = (String) item;
	        firePropertyChangeListener(new PropertyChangeEvent(ParameterTextArea.this, "value", oldValue, selectItem));
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectItem;
    }

    @Override
    public String getType() {
	    return ParameterType.TEXT_AREA;
    }

	public ParameterTextArea setDescribe(String describe) {
		this.describe = describe;
		return this;
    }

	public String getDescribe() {
		return describe;
	}

    @Override
    public void dispose() {

    }

    public void setLineWrap(boolean lineWrap){
    	this.isLineWrap=lineWrap;
    }

    public boolean isLineWrap(){
    	return this.isLineWrap;
    }

	public void setWrapStyleWord(boolean wrapStyleWord){
		this.isWrapStyleWord=wrapStyleWord;
	}

	public boolean isWrapStyleWord(){
		return this.isWrapStyleWord;
	}
}
