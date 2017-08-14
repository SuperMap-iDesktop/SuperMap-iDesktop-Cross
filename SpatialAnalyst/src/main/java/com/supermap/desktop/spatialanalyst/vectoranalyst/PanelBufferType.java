package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yuanR on 2017/8/10 .
 */
public class PanelBufferType extends JPanel {

	private static final long serialVersionUID = 1L;

	private JRadioButton radioButtonBufferTypeRound;
	private JRadioButton radioButtonBufferTypeFlat;
	private JCheckBox checkBoxBufferLeft;
	private JCheckBox checkBoxBufferRight;

	private SmNumericFieldComboBox numericFieldComboBoxLeft;
	private SmNumericFieldComboBox numericFieldComboBoxRight;

	public JRadioButton getRadioButtonBufferTypeRound() {
		return radioButtonBufferTypeRound;
	}

	public JRadioButton getRadioButtonBufferTypeFlat() {
		return radioButtonBufferTypeFlat;
	}

	public JCheckBox getCheckBoxBufferLeft() {
		return checkBoxBufferLeft;
	}

	public JCheckBox getCheckBoxBufferRight() {
		return checkBoxBufferRight;
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == radioButtonBufferTypeRound) {
				// 当点击圆头缓冲时
				checkBoxBufferLeft.setSelected(true);
				checkBoxBufferRight.setSelected(true);
				checkBoxBufferLeft.setEnabled(false);
				checkBoxBufferRight.setEnabled(false);
				numericFieldComboBoxLeft.setEnabled(true);
				numericFieldComboBoxRight.setEnabled(true);
				// 当点击圆头缓冲时，设置左右半径值相等
				numericFieldComboBoxRight.setSelectedItem(numericFieldComboBoxLeft.getSelectedItem());
			} else if (e.getSource() == radioButtonBufferTypeFlat) {
				checkBoxBufferLeft.setEnabled(!radioButtonBufferTypeRound.isSelected());
				checkBoxBufferRight.setEnabled(!radioButtonBufferTypeRound.isSelected());

			} else if (e.getSource() == checkBoxBufferLeft || e.getSource() == checkBoxBufferRight) {
				numericFieldComboBoxLeft.setEnabled(checkBoxBufferLeft.isSelected());
				numericFieldComboBoxRight.setEnabled(checkBoxBufferRight.isSelected());
			}
		}
	};


	public PanelBufferType() {

		initComponent();
		initResources();
		initLayout();
		registerEvent();
	}

	public void setLeftRightComboBox(SmNumericFieldComboBox left, SmNumericFieldComboBox right) {
		this.numericFieldComboBoxLeft = left;
		this.numericFieldComboBoxRight = right;
	}

	private void initComponent() {

		this.radioButtonBufferTypeRound = new JRadioButton("BufferTypeRound");
		this.radioButtonBufferTypeFlat = new JRadioButton("BufferTypeFlat");
		this.checkBoxBufferLeft = new JCheckBox("BufferTypeLeft");
		this.checkBoxBufferRight = new JCheckBox("BufferTypeRight");
		this.checkBoxBufferLeft.setEnabled(false);
		this.checkBoxBufferRight.setEnabled(false);

		ButtonGroup bufferTypeButtonGroup = new ButtonGroup();
		bufferTypeButtonGroup.add(this.radioButtonBufferTypeRound);
		bufferTypeButtonGroup.add(this.radioButtonBufferTypeFlat);

		this.radioButtonBufferTypeRound.setSelected(true);
		this.checkBoxBufferLeft.setSelected(true);
		this.checkBoxBufferRight.setSelected(true);
	}


	private void initResources() {
		this.radioButtonBufferTypeRound.setText(SpatialAnalystProperties.getString("String_BufferTypeRound"));
		this.radioButtonBufferTypeFlat.setText(SpatialAnalystProperties.getString("String_BufferTypeFlat"));
		this.checkBoxBufferLeft.setText(SpatialAnalystProperties.getString("String_BufferTypeLeft"));
		this.checkBoxBufferRight.setText(SpatialAnalystProperties.getString("String_BufferTypeRight"));
	}

	private void initLayout() {
		GroupLayout panelBufferTypeLayout = new GroupLayout(this);
//		panelBufferTypeLayout.setAutoCreateContainerGaps(true);
		panelBufferTypeLayout.setAutoCreateGaps(true);
		this.setLayout(panelBufferTypeLayout);
		//@formatter:off
		panelBufferTypeLayout.setHorizontalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addComponent(this.radioButtonBufferTypeRound)
				.addComponent(this.radioButtonBufferTypeFlat)
				.addComponent(this.checkBoxBufferLeft)
				.addComponent(this.checkBoxBufferRight));

		panelBufferTypeLayout.setVerticalGroup(panelBufferTypeLayout.createSequentialGroup()
				.addGroup(panelBufferTypeLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.radioButtonBufferTypeRound)
						.addComponent(this.radioButtonBufferTypeFlat)
						.addComponent(this.checkBoxBufferLeft)
						.addComponent(this.checkBoxBufferRight)).addContainerGap());
		//@formatter:on
	}


	private void registerEvent() {
		this.radioButtonBufferTypeFlat.addActionListener(actionListener);
		this.radioButtonBufferTypeRound.addActionListener(actionListener);
		this.checkBoxBufferLeft.addActionListener(actionListener);
		this.checkBoxBufferRight.addActionListener(actionListener);

	}
}
