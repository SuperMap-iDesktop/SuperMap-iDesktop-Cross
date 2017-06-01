package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;

/**
 * @author XiaJT
 */
public class ParameterNumber extends ParameterTextField {

	private int bit = -1;
	private boolean isMaxValueEnable = false;
	private double maxValue;

	private boolean isMinValueEnable = false;
	private double minValue;

	public ParameterNumber() {
		this("");
	}

	public ParameterNumber(String describe) {
		super(describe);
		smTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				try {
					if (textFieldValue.endsWith(".")) {
						return false;
					}
					Double aDouble = Double.valueOf(textFieldValue);
					if (isMinValueEnable && aDouble < minValue) {
						return false;
					}
					if (isMaxValueEnable && aDouble > maxValue) {
						return false;
					}
					if (bit == 0) {
						Integer integer = Integer.valueOf(textFieldValue);
					} else if (bit > 0) {
						int bitCount = 0;
						String[] split = textFieldValue.split("\\.");
						if (split.length == 2) {
							bitCount = split[1].length();
						}
						if (bitCount > bit) {
							return false;
						}
					}
				} catch (NumberFormatException e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		};
	}

	public void setMaxValue(double maxValue) {
		isMaxValueEnable = true;
		this.maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		isMinValueEnable = true;
		this.minValue = minValue;
	}

	public void setMaxBit(int bit) {
		this.bit = bit;
	}

}
