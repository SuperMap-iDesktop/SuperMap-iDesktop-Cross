package com.supermap.desktop.process.parameter.interfaces;

import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.events.ParameterValueLegalListener;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IParameter extends IAbstractParameter {
	String getType();


	void addValueLegalListener(ParameterValueLegalListener parameterValueLegalListener);

	void removeValueLegalListener(ParameterValueLegalListener parameterValueLegalListener);

	boolean isValueLegal(String fieldName, Object value);

	Object isValueSelected(String fieldName, Object value);

	void addFieldConstraintChangedListener(FieldConstraintChangedListener fieldConstraintChangedListener);

	void removeFieldConstraintChangedListener(FieldConstraintChangedListener fieldConstraintChangedListener);

	void fireFieldConstraintChanged(String fieldName);

	ArrayList<String> getFieldNameList(Class<AbstractParameter> clazz);

	/**
	 * 获取参数面板
	 * 对参数面板的修改需要设值到IParameter中，所以从这里获取比较好
	 *
	 * @return
	 */
	IParameterPanel getParameterPanel();

	void addPropertyListener(PropertyChangeListener propertyChangeListener);

	void removePropertyListener(PropertyChangeListener propertyChangeListener);

	void dispose();

	void setParameters(IParameters parameters);

	String getDescribe();

	IParameters getParameters();

}
