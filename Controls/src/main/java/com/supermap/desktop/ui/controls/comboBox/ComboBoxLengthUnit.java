package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.Unit;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.enums.LengthUnit;

import javax.swing.*;

/**
 * Created by hanyz on 2017/2/21.
 */
public class ComboBoxLengthUnit extends JComboBox<LengthUnit> {

    public ComboBoxLengthUnit() {
        super();
        this.addItem(LengthUnit.MILLIMETER);
        this.addItem(LengthUnit.CENTIMETER);
        this.addItem(LengthUnit.DECIMETER);
        this.addItem(LengthUnit.METER);
        this.addItem(LengthUnit.KILOMETER);
        this.addItem(LengthUnit.INCH);
        this.addItem(LengthUnit.FOOT);
        this.addItem(LengthUnit.MILE);
//        this.addItem(LengthUnit.DEGREE);
        this.addItem(LengthUnit.YARD);
        this.setSelectedItem(LengthUnit.METER);
        this.setEditable(false);
        this.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
    }

	/**
	 * 获得选中的单位
	 */
	public BufferRadiusUnit getUnit() {
		LengthUnit selectedItem = ((LengthUnit) this.getSelectedItem());
		return getBufferRadiusUnit(selectedItem.getUnit());
	}

	private BufferRadiusUnit getBufferRadiusUnit(Unit unitName) {
		if (Unit.MILIMETER.equals(unitName)) {
			return BufferRadiusUnit.MiliMeter;
		} else if (Unit.CENTIMETER.equals(unitName)) {
			return BufferRadiusUnit.CentiMeter;
		} else if (Unit.DECIMETER.equals(unitName)) {
			return BufferRadiusUnit.DeciMeter;
		} else if (Unit.METER.equals(unitName)) {
			return BufferRadiusUnit.Meter;
		} else if (Unit.KILOMETER.equals(unitName)) {
			return BufferRadiusUnit.KiloMeter;
		} else if (Unit.INCH.equals(unitName)) {
			return BufferRadiusUnit.Inch;
		} else if (Unit.FOOT.equals(unitName)) {
			return BufferRadiusUnit.Foot;
		} else if (Unit.MILE.equals(unitName)) {
			return BufferRadiusUnit.Mile;
		} else if (Unit.YARD.equals(unitName)) {
			return BufferRadiusUnit.Yard;
		}
		return null;
	}
}
