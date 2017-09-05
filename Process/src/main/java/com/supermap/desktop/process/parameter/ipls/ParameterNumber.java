package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * @author XiaJT
 * 增加“单位”属性
 * 支持千分位的识别-yuanR2017.9.5
 */
public class ParameterNumber extends ParameterTextField {

	private int bit = -1;
	private boolean isMaxValueEnable = false;
	private double maxValue;

	private boolean isMinValueEnable = false;
	private double minValue;
	private boolean isIncludeMin = true;
	private boolean isIncludeMax = true;
	private String toolTip;
	private String unit = "";

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
					// 判断用千分位表示的数字是否正确-yuanR2017.9.5
					if (textFieldValue.contains(",")) {
						String temp = DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(textFieldValue));
						if (!temp.equals(textFieldValue)) {
							return false;
						}
					}
					for (int i = 0; i < textFieldValue.length(); i++) {
						// 可识别千分位-yuanR2017.9.5
						if (!(Character.isDigit(textFieldValue.charAt(i)) || textFieldValue.charAt(i) == ',' || textFieldValue.charAt(i) == '.' || textFieldValue.charAt(i) == '-')) {
							return false;
						}
					}
					textFieldValue = textFieldValue.replace(",", "");
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

	/**
	 * 防止千分位的影响，做一下处理
	 * yuanR
	 *
	 * @return
	 */
	@Override
	public String getSelectedItem() {
		return super.getSelectedItem().replace(",", "");
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
		return StringUtilities.isNullOrEmpty(toolTip) ? getDescribe() + getRangeDescribe() : toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	@Override
	public String getUnit() {
		return unit;
	}

	@Override
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
