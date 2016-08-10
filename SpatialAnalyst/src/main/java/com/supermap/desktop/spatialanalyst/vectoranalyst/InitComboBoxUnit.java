package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.Unit;
import com.supermap.desktop.controls.ControlDefaultValues;

import javax.swing.*;

public class InitComboBoxUnit {
	private JComboBox<Unit> comboBox;
	private BufferRadiusUnit bufferRadiusUnit;

	public JComboBox<Unit> createComboBoxUnit() {
		this.comboBox = new JComboBox<Unit>();
		this.comboBox.addItem(Unit.MILIMETER);
		this.comboBox.addItem(Unit.CENTIMETER);
		this.comboBox.addItem(Unit.DECIMETER);
		this.comboBox.addItem(Unit.METER);
		this.comboBox.addItem(Unit.KILOMETER);
		this.comboBox.addItem(Unit.INCH);
		this.comboBox.addItem(Unit.FOOT);
		this.comboBox.addItem(Unit.MILE);
		this.comboBox.addItem(Unit.YARD);
		this.comboBox.setSelectedItem(Unit.METER);
		this.comboBox.setEditable(false);
		this.comboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
		return this.comboBox;
	}

	public BufferRadiusUnit getBufferRadiusUnit(Unit unitName) {

		if (Unit.MILIMETER.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.MiliMeter;
		} else if (Unit.CENTIMETER.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.CentiMeter;
		} else if (Unit.DECIMETER.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.DeciMeter;
		} else if (Unit.METER.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.Meter;
		} else if (Unit.KILOMETER.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.KiloMeter;
		} else if (Unit.INCH.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.Inch;
		} else if (Unit.FOOT.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.Foot;
		} else if (Unit.MILE.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.Mile;
		} else if (Unit.YARD.equals(unitName)) {
			this.bufferRadiusUnit = BufferRadiusUnit.Yard;
		}
		return this.bufferRadiusUnit;
	}
}
