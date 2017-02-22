package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.analyst.spatialanalyst.BufferRadiusUnit;
import com.supermap.data.Unit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by hanyz on 2017/2/20.
 * <p>
 * 对缓冲区半径面板进行重构，类比“缓冲数据”面板--yuanR
 */
public class PanelBufferRadius extends JPanel {

	private JPanel panelContainer;
	private JLabel labelUnit;
	private JLabel labelField;
	private ComboBoxLengthUnit comboBoxUnit;
	private SmNumericFieldComboBox numericFieldComboBox;
//	private InitComboBoxUnit initComboBoxUnit = new InitComboBoxUnit();

	/**
	 * 获得缓冲“单位”下拉列表框
	 *
	 * @return
	 */
	public JComboBox getComboBoxUnit() {
		return this.comboBoxUnit;
	}

	/**
	 * @return
	 */
	public SmNumericFieldComboBox getNumericFieldComboBox() {
		return this.numericFieldComboBox;
	}

	/**
	 * @return
	 */
	public BufferRadiusUnit getUnit() {
		LengthUnit selectedItem = ((LengthUnit) this.comboBoxUnit.getSelectedItem());
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

	/**
	 * 设置panel的名称
	 *
	 * @param labelFieldText
	 */
	public void setLabelFieldText(String labelFieldText) {
		if (labelFieldText != null) {
			this.setBorder(BorderFactory.createTitledBorder(labelFieldText));
		}
	}

	public PanelBufferRadius() {
		initComponent();
		initResources();
		initLayout();
	}

	private void initComponent() {
		this.labelUnit = new JLabel("Unit");
		this.labelField = new JLabel("Length");
		this.comboBoxUnit = new ComboBoxLengthUnit();
		this.numericFieldComboBox = new SmNumericFieldComboBox();
		this.panelContainer = new JPanel();

		TitledBorder border = new TitledBorder(ControlsProperties.getString("String_BufferRadius"));
		this.setBorder(border);
	}

	private void initResources() {
		this.labelUnit.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
		this.labelField.setText(ControlsProperties.getString("String_LabelLength"));
	}

	private void initLayout() {
		this.setLayout(new BorderLayout());
		this.add(this.panelContainer, BorderLayout.CENTER);
		//内部JPanel布局
		GroupLayout panelBufferDataLayout = new GroupLayout(this.panelContainer);
		panelBufferDataLayout.setAutoCreateContainerGaps(true);
		panelBufferDataLayout.setAutoCreateGaps(true);
		this.panelContainer.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.labelField))
				.addGroup(panelBufferDataLayout.createParallelGroup()
						.addComponent(this.comboBoxUnit)
						.addComponent(this.numericFieldComboBox)));
		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit,25,25,25)).addGap(5)
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelField)
						.addComponent(this.numericFieldComboBox,25,25,25)));
		//@formatter:on
	}
}
