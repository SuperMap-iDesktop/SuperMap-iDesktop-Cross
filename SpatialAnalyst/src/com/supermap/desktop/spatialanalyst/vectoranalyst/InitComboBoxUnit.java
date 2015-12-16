package com.supermap.desktop.spatialanalyst.vectoranalyst;

import javax.swing.JComboBox;

import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.Unit;
import com.supermap.desktop.controls.ControlDefaultValues;

public class InitComboBoxUnit {
	private JComboBox<Unit> comboBox;
	private BufferRadiusUnit bufferRadiusUnit;

	public static final String MILLIMETER = "毫米";
	public static final String CENTIMETER = "厘米";
	public static final String DECIMETER = "分米";
	public static final String METER = "米";
	public static final String KILOMETER = "千米";
	public static final String MILE = "英里";
	public static final String INCH = "英寸";
	public static final String FOOT = "英尺";
	public static final String YARD = "码";

	public JComboBox<Unit> createComboBoxUnit() {
		comboBox = new JComboBox<Unit>();
		comboBox.addItem(Unit.MILIMETER);
		comboBox.addItem(Unit.CENTIMETER);
		comboBox.addItem(Unit.DECIMETER);
		comboBox.addItem(Unit.METER);
		comboBox.addItem(Unit.KILOMETER);
		comboBox.addItem(Unit.INCH);
		comboBox.addItem(Unit.FOOT);
		comboBox.addItem(Unit.MILE);
		comboBox.addItem(Unit.YARD);
		comboBox.setSelectedItem(Unit.METER);
		comboBox.setEditable(false);
		comboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		return comboBox;
	}

	public BufferRadiusUnit getBufferRadiusUnit(Object unitName) {

		if (MILLIMETER.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.MiliMeter;
		} else if (CENTIMETER.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.CentiMeter;
		} else if (DECIMETER.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.DeciMeter;
		} else if (METER.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.Meter;
		} else if (KILOMETER.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.KiloMeter;
		} else if (INCH.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.Inch;
		} else if (FOOT.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.Foot;
		} else if (MILE.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.Mile;
		} else if (YARD.equals(unitName)) {
			bufferRadiusUnit = BufferRadiusUnit.Yard;
		}

		return bufferRadiusUnit;
	}

}
