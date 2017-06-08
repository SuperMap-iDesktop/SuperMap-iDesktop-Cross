package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
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
	private boolean isIncludeMin = true;
	private boolean isIncludeMax = true;

	public ParameterNumber() {
		this("");
	}

	public ParameterNumber(String describe) {
		super(describe);
		setDescribe(describe);
		smTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				try {
					if (textFieldValue.endsWith(".")) {
						return false;
					}
					Double aDouble = Double.valueOf(textFieldValue);
					if (isMinValueEnable && (aDouble < minValue || (!isIncludeMin && aDouble == minValue))) {
						return false;
					}
					if (isMaxValueEnable && (aDouble > maxValue || (!isIncludeMax && aDouble == maxValue))) {
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


	private String getRangeDescribe() {
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append(isIncludeMin() && isMinValueEnable ? "[" : "(");
		stringBuffer.append(isMinValueEnable ? String.valueOf(minValue) : "-∞");
		stringBuffer.append(", ");
		stringBuffer.append(isMaxValueEnable ? String.valueOf(maxValue) : "+∞");
		stringBuffer.append(isIncludeMax() && isMaxValueEnable ? "]" : ")");
		return stringBuffer.toString();
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

	public void setIsIncludeMin(boolean isIncludeMin) {
		this.isIncludeMin = isIncludeMin;
	}

	public boolean isIncludeMin() {
		return isIncludeMin;
	}

	public void setIncludeMin(boolean includeMin) {
		isIncludeMin = includeMin;
	}

	public boolean isIncludeMax() {
		return isIncludeMax;
	}

	public void setIncludeMax(boolean includeMax) {
		isIncludeMax = includeMax;
	}

	@Override
	public String getType() {
		return ParameterType.NUMBER;
	}

	public String getToolTip() {
		return getDescribe() + getRangeDescribe();
	}
}
