package com.supermap.desktop.controls.utilities;

import javax.swing.JButton;

import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.StringUtilities;

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
		SmButton buttonOK = new SmButton();
		buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		return buttonOK;
	}

	/**
	 * 生产一个Cancel按钮
	 * 
	 * @return
	 */
	public static JButton createButtonCancel() {
		SmButton buttonCancel = new SmButton();
		buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
		return buttonCancel;
	}
	
	/**
	 * 生产一个Close按钮
	 * 
	 * @return
	 */
	public static JButton createButtonClose() {
		SmButton buttonClose = new SmButton();
		buttonClose.setText(CommonProperties.getString(CommonProperties.Close));
		return buttonClose;
	}

	/**
	 * 生产一个整型限制的输入控件
	 * 
	 * @param defaultValue
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static SmTextFieldLegit createIntegerTextField(int defaultValue, final int minValue, final int maxValue) {
		if (minValue > maxValue) {
			return null;
		}

		SmTextFieldLegit textField = new SmTextFieldLegit();
		textField.setSmTextFieldLegit(new ISmTextFieldLegit() {

			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				boolean result = false;

				if (StringUtilities.isPositiveInteger(textFieldValue)) {
					Integer value = Integer.valueOf(textFieldValue);

					if (value >= minValue && value <= maxValue) {
						result = true;
					}
				}
				return result;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		textField.setText(String.valueOf(defaultValue));

		return textField;
	}

	public static SmTextFieldLegit createNumericTextField(double defaultValue, final double minValue, final double maxValue) {
		if (minValue > maxValue) {
			return null;
		}

		SmTextFieldLegit textField = new SmTextFieldLegit();
		textField.setSmTextFieldLegit(new ISmTextFieldLegit() {

			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				boolean result = false;

				if (StringUtilities.isNumber(textFieldValue)) {
					Double value = Double.valueOf(textFieldValue);
					return value >= minValue && value <= maxValue;
				}
				return result;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		textField.setText(String.valueOf(defaultValue));

		return textField;
	}

	public static SmTextFieldLegit createNumericTextField(double defaultValue, ISmTextFieldLegit smTextFieldLegit) {
		SmTextFieldLegit textField = new SmTextFieldLegit();
		textField.setSmTextFieldLegit(smTextFieldLegit);
		textField.setText(String.valueOf(defaultValue));

		return textField;
	}
}
