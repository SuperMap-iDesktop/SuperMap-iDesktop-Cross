package com.supermap.desktop;

import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import java.text.MessageFormat;

public class ScaleModel {
	public static final double INVALID_SCALE = -1;
	public static final double NONE_SCALE = 0;
	public static final String FIRST_PART = "1:";
	public static final String SEPARATOR = ":";
	public static final String NUMERATORCAPTION = "1";

	public static final String SCALE_5000 = "1:5,000";
	public static final String SCALE_10000 = "1:10,000";
	public static final String SCALE_25000 = "1:25,000";
	public static final String SCALE_50000 = "1:50,000";
	public static final String SCALE_100000 = "1:100,000";
	public static final String SCALE_250000 = "1:250,000";
	public static final String SCALE_500000 = "1:500,000";
	public static final String SCALE_1000000 = "1:1,000,000";

	private static final String NONE_SCALE_CAPTION = "NONE";
	private static final String SCALECAPTION_FORMATTER = "1:{0}";

	private static final double MAX_SCALE_VALUE = 1E10;
	private static final double MIN_SCALE_VALUE = 1E-10;

	private double scale = INVALID_SCALE; // 比例尺的小数表示 0.0001
	private String scaleCaption = ""; // 比例尺的文本表示。 1:100000
	private double scaleDenominator = INVALID_SCALE; // 比例尺的分母

	public ScaleModel(double scale) throws InvalidScaleException {
		if (scale <= 0) {
			this.scale = NONE_SCALE;
			this.scaleDenominator = NONE_SCALE;
			this.scaleCaption = NONE_SCALE_CAPTION;
		} else {
			this.scale = scale;
			parse(scale);
		}
	}

	public ScaleModel(String scale) throws InvalidScaleException {
		if (StringUtilities.isNullOrEmpty(scale)) {
			throw new InvalidScaleException();
		}

		String[] array = scale.split(SEPARATOR);
		if (array.length != 1 && array.length != 2) {
			throw new InvalidScaleException();
		}

		if (array.length == 1) {
			parseDenominator(array[0]);
		} else {
			parse(array);
		}
	}

	public double getScale() {
		return scale;
	}

	public String getScaleCaption() {
		return scaleCaption;
	}

	public double getScaleDenominator() {
		return scaleDenominator;
	}

	private void parse(double scale) throws InvalidScaleException {
		this.scaleDenominator = DoubleUtilities.div(1.0, scale, 8);
//		this.scaleCaption = MessageFormat.format(SCALECAPTION_FORMATTER, BigDecimal.valueOf(this.scaleDenominator).toPlainString());
		this.scaleCaption = MessageFormat.format(SCALECAPTION_FORMATTER, DoubleUtilities.getFormatString(scaleDenominator));
	}

	private void parse(String[] scaleCaption) throws InvalidScaleException {
		if (scaleCaption == null || scaleCaption.length != 2) {
			throw new InvalidScaleException();
		}

		String scaleNumeratorCaption = scaleCaption[0];
		String scaleDenominatorCaption = scaleCaption[1];

		if (StringUtilities.isNullOrEmpty(scaleDenominatorCaption) || StringUtilities.isNullOrEmpty(scaleNumeratorCaption)) {
			throw new InvalidScaleException();
		}

		if (!scaleNumeratorCaption.equalsIgnoreCase(NUMERATORCAPTION)) {
			throw new InvalidScaleException();
		}

		try {
			this.scaleDenominator = DoubleUtilities.stringToValue(scaleDenominatorCaption);
		} catch (Exception e) {
			this.scaleDenominator = INVALID_SCALE;
			throw new InvalidScaleException();
		}

		if (this.scaleDenominator > 0) {
			this.scale = 1 / this.scaleDenominator;
			this.scaleCaption = MessageFormat.format(SCALECAPTION_FORMATTER, scaleDenominatorCaption);
		} else if (this.scaleDenominator <= 0) {
			this.scale = NONE_SCALE;
			this.scaleDenominator = NONE_SCALE;
			this.scaleCaption = NONE_SCALE_CAPTION;
		}
	}

	private void parseDenominator(String scaleDenominatorCaption) throws InvalidScaleException {
		if (StringUtilities.isNullOrEmpty(scaleDenominatorCaption)) {
			throw new InvalidScaleException();
		}

		try {
			this.scaleDenominator = DoubleUtilities.stringToValue(scaleDenominatorCaption);
		} catch (Exception e) {
			this.scaleDenominator = INVALID_SCALE;
			throw new InvalidScaleException();
		}

		if (this.scaleDenominator > 0) {
			this.scale = 1 / this.scaleDenominator;
			this.scaleCaption = MessageFormat.format(SCALECAPTION_FORMATTER, scaleDenominatorCaption);
		} else if (this.scaleDenominator <= 0) {
			this.scale = NONE_SCALE;
			this.scaleDenominator = NONE_SCALE;
			this.scaleCaption = NONE_SCALE_CAPTION;
		}
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		try {
			if (super.equals(obj)) {
				return true;
			}

			if (obj instanceof ScaleModel) {
				ScaleModel newScale = (ScaleModel) obj;
				if (Double.compare(this.scale, newScale.getScale()) == 0) {
					return true;
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	@Override
	public int hashCode() {
		return super.hashCode();

	}

	@Override
	public String toString() {
		return this.scaleCaption;
	}

	public ScaleModel getEmpty() {
		ScaleModel model = null;

		try {
			model = new ScaleModel(NONE_SCALE);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return model;
	}

	public static boolean isLegitScaleString(String scaleString) {
		if (StringUtilities.isNullOrEmpty(scaleString) || scaleString.contains("d")) {
			return false;
		} else {
			if (scaleString.startsWith("1:")) {
				// 包含 1:
				scaleString = scaleString.substring(2);
			}
			if (StringUtilities.isNullOrEmpty(scaleString)) {
				return false;
			}
			try {
				Double aDouble = DoubleUtilities.stringToValue(scaleString);
				if (aDouble == null || aDouble >= MAX_SCALE_VALUE || aDouble <= MIN_SCALE_VALUE) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	public static boolean isLegitScaleString(double scale) {
		return !(scale >= MAX_SCALE_VALUE || scale <= MIN_SCALE_VALUE);
	}
}
