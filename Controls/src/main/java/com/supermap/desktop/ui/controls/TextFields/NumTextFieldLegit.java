package com.supermap.desktop.ui.controls.TextFields;

import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;


/**
 * Created By Chens on 2017/9/5 0005
 * 数字输入限制
 */
public class NumTextFieldLegit extends SmTextFieldLegit {
	private int bit = -1;
	private boolean isMaxValueEnable = false;
	private double maxValue;

	private boolean isMinValueEnable = false;
	private double minValue;
	private boolean isIncludeMin = true;
	private boolean isIncludeMax = true;

	public NumTextFieldLegit(String text) {
		super(text);
		smTextFieldLegit = new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				try {
					if (StringUtilities.isNullOrEmpty(textFieldValue)) {
						return false;
					}
					if (textFieldValue.endsWith(".") || textFieldValue.endsWith("-")) {
						return false;
					}
					if (textFieldValue.split("\\.").length > 2) {
						return false;
					}
					if (textFieldValue.lastIndexOf("-") > 0) {
						return false;
					}
					for (int i = 0; i < textFieldValue.length(); i++) {
						if (!(Character.isDigit(textFieldValue.charAt(i)) || textFieldValue.charAt(i) == '.' || textFieldValue.charAt(i) == '-')) {
							return false;
						}
					}
					Double aDouble = DoubleUtilities.stringToValue(textFieldValue);
					if (isMinValueEnable && (aDouble < minValue || (!isIncludeMin && aDouble == minValue))) {
						return false;
					}
					if (isMaxValueEnable && (aDouble > maxValue || (!isIncludeMax && aDouble == maxValue))) {
						return false;
					}
					if (bit == 0) {
						if (textFieldValue.contains(".")) {
							return false;
						}
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

	public NumTextFieldLegit() {
		this(null);
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public boolean isMaxValueEnable() {
		return isMaxValueEnable;
	}

	public void setMaxValueEnable(boolean maxValueEnable) {
		isMaxValueEnable = maxValueEnable;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isMinValueEnable() {
		return isMinValueEnable;
	}

	public void setMinValueEnable(boolean minValueEnable) {
		isMinValueEnable = minValueEnable;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
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
}

