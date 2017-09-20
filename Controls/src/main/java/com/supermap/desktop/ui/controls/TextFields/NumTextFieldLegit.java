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
		updateTooltip();
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

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		isMaxValueEnable = true;
		this.maxValue = maxValue;
		updateTooltip();
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		isMinValueEnable = true;
		this.minValue = minValue;
		updateTooltip();
	}

	public boolean isIncludeMin() {
		return isIncludeMin;
	}

	public void setIncludeMin(boolean includeMin) {
		isIncludeMin = includeMin;
		updateTooltip();
	}

	public boolean isIncludeMax() {
		return isIncludeMax;
	}

	public void setIncludeMax(boolean includeMax) {
		isIncludeMax = includeMax;
		updateTooltip();
	}

	public void setInterval(int min, int max) {
		minValue = min;
		maxValue = max;
		updateTooltip();
	}

	public void updateTooltip() {
		StringBuilder stringBuffer = new StringBuilder();
		stringBuffer.append(isIncludeMin() && isMinValueEnable ? "[" : "(");
		stringBuffer.append(isMinValueEnable ? DoubleUtilities.toString(minValue, 0) : "-∞");
		stringBuffer.append(", ");
		stringBuffer.append(isMaxValueEnable ? DoubleUtilities.toString(maxValue, 0) : "+∞");
		stringBuffer.append(isIncludeMax() && isMaxValueEnable ? "]" : ")");
		this.setToolTipText(stringBuffer.toString());
	}
}

