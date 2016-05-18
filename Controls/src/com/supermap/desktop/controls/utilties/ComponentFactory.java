package com.supermap.desktop.controls.utilties;

import javax.swing.JButton;

import com.supermap.desktop.properties.CommonProperties;

/**
 * 控件构造工厂
 * 
 * @author highsad
 *
 */
public class ComponentFactory {

	/**
	 * 生产一个OK按钮
	 * 
	 * @return
	 */
	public static JButton createButtonOK() {
		JButton buttonOK = new JButton();
		buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		return buttonOK;
	}

	/**
	 * 生成一个Cancel按钮
	 * 
	 * @return
	 */
	public static JButton createButtonCancel() {
		JButton buttonCancel = new JButton();
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
		return buttonCancel;
	}
}
