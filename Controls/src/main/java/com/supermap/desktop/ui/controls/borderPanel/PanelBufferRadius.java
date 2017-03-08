package com.supermap.desktop.ui.controls.borderPanel;

import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by hanyz on 2017/2/20.
 * <p>
 * 对缓冲区半径面板进行重构，类比“缓冲数据”面板--yuanR
 */
public class PanelBufferRadius extends JPanel {

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
	public ComboBoxLengthUnit getComboBoxUnit() {
		return this.comboBoxUnit;
	}

	/**
	 * @return
	 */
	public SmNumericFieldComboBox getNumericFieldComboBox() {
		return this.numericFieldComboBox;
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

		TitledBorder border = new TitledBorder(ControlsProperties.getString("String_BufferRadius"));
		this.setBorder(border);
	}

	private void initResources() {
		this.labelUnit.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
		this.labelField.setText(ControlsProperties.getString("String_LabelLength"));
	}

	private void initLayout() {
		//内部JPanel布局
		GroupLayout panelBufferDataLayout = new GroupLayout(this);
		panelBufferDataLayout.setAutoCreateContainerGaps(true);
		panelBufferDataLayout.setAutoCreateGaps(true);
		this.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.labelField))
				.addGroup(panelBufferDataLayout.createParallelGroup()
						.addComponent(this.comboBoxUnit,5,5,Short.MAX_VALUE)
						.addComponent(this.numericFieldComboBox,5,5,Short.MAX_VALUE)));
		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
				.addGroup(panelBufferDataLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelField)
						.addComponent(this.numericFieldComboBox,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)).addGap(5,5,Short.MAX_VALUE));

		//@formatter:on
	}

	/**
	 * 创建面板是否可用方法
	 * 2017.3.2 yuanR
	 *
	 * @param isEnable
	 */
	public void setPanelEnable(boolean isEnable) {
		if (isEnable) {
			this.comboBoxUnit.setEnabled(true);
			this.numericFieldComboBox.setEnabled(true);
		} else {
			this.comboBoxUnit.setEnabled(false);
			this.numericFieldComboBox.setEnabled(false);
		}
	}
}
