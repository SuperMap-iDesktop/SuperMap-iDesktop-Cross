package com.supermap.desktop.process.constraint.interfaces;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

/**
 * @author XiaJT
 */
public interface IConstraint {
	/**
	 * 约束参数中所有符合要求的字段
	 *
	 * @param parameter 需要约束的参数
	 */
	void constrained(IParameter parameter);

	/**
	 * 约束参数中指定index的字段
	 *
	 * @param parameter 需要约束的参数
	 * @param index     参数字段ParameterField中index的值
	 */
	void constrained(IParameter parameter, int index);

	/**
	 * 约束参数中指定name的字段
	 *
	 * @param parameter 需要约束的参数
	 * @param name      参数字段ParameterField中name的值
	 */
	void constrained(IParameter parameter, String name);

}
