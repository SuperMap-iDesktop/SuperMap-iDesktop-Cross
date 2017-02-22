package com.supermap.desktop.ui.controls.comboBox;

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
        this.addItem(LengthUnit.DEGREE);
        this.addItem(LengthUnit.YARD);
        this.setSelectedItem(LengthUnit.METER);
        this.setEditable(false);
        this.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
    }
}
