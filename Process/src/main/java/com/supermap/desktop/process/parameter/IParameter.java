package com.supermap.desktop.process.parameter;

import com.supermap.desktop.process.enums.ParameterType;

import javax.swing.*;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IParameter {
	ParameterType getType();

	/**
	 * 获取参数面板
	 * 对参数面板的修改需要设值到IParameter中，所以从这里获取比较好
	 *
	 * @return
	 */
	JPanel getPanel();
}
