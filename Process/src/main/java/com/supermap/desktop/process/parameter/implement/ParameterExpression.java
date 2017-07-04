package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;

import java.beans.PropertyChangeEvent;

/**
 * Created by Administrator on 2017/7/4 0004.
 * 表达式选择
 */
public class ParameterExpression extends AbstractParameter implements ISelectionParameter {
    @ParameterField(name = PROPERTY_VALE)
    private String value;
//    @ParameterField(name = BUTTON_VALUE)
    private String BUTTON_VALUE = "button_value";
    protected ISmTextFieldLegit smTextFieldLegit;


    public ParameterExpression() {

    }

    public ISmTextFieldLegit getSmTextFieldLegit() {
        return smTextFieldLegit;
    }

    public void setSmTextFieldLegit(ISmTextFieldLegit smTextFieldLegit) {
        this.smTextFieldLegit = smTextFieldLegit;
    }

    @Override
    public String getType() {
        return ParameterType.EXPRESSION;
    }

    @Override
    public void setSelectedItem(Object item) {
        Object oldValue = this.value;
        this.value = String.valueOf(value);
        firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, value));
    }

    @Override
    public Object getSelectedItem() {
        // TODO: 2017/7/4 0004  
        return value;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
