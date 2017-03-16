package com.supermap.desktop.process.parameter.interfaces;

import java.beans.PropertyChangeListener;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IParameter extends IAbstractParameter {
	String getType();

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

	IParameters getParameters();
}
