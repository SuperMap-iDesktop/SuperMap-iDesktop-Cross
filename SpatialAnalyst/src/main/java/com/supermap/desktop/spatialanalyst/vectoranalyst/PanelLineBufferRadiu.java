package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.data.DatasetVector;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxLengthUnit;
import com.supermap.desktop.ui.controls.comboBox.SmNumericFieldComboBox;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Created by yuanR on 2017/8/10 0010.
 */
public class PanelLineBufferRadiu extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel labelUnit;
	private ComboBoxLengthUnit comboBoxUnit;
	private JLabel labelLeftNumericFieldRadius;
	private JLabel labelRightNumericFieldRadius;
	private SmNumericFieldComboBox numericFieldComboBoxLeft;
	private SmNumericFieldComboBox numericFieldComboBoxRight;

	private JRadioButton radioButtonBufferTypeRound;

	private NumericLeftCaretListener numericLeftCaretListener = new NumericLeftCaretListener();
	private NumericRightCaretListener numericRightCaretListener = new NumericRightCaretListener();


	public ComboBoxLengthUnit getComboBoxUnit() {
		return comboBoxUnit;
	}

	public SmNumericFieldComboBox getNumericFieldComboBoxLeft() {
		return numericFieldComboBoxLeft;
	}

	public SmNumericFieldComboBox getNumericFieldComboBoxRight() {
		return numericFieldComboBoxRight;
	}

	public PanelLineBufferRadiu() {
		initComponent();
		initResources();
		initLayout();
		registerEvent();
	}

	public void setBufferType(JRadioButton round) {
		this.radioButtonBufferTypeRound = round;
	}


	public void initDataset(DatasetVector dataset) {
		if (dataset != null) {
			this.getNumericFieldComboBoxLeft().setDataset(dataset);
			this.getNumericFieldComboBoxRight().setDataset(dataset);
			// 初始化半径长度值
			this.getNumericFieldComboBoxLeft().setSelectedItem("10");
			this.getNumericFieldComboBoxRight().setSelectedItem("10");
		} else {
			this.getNumericFieldComboBoxLeft().setEnabled(false);
			this.getNumericFieldComboBoxRight().setEnabled(false);
		}
	}


	private void initComponent() {
		this.labelUnit = new JLabel("Unit");
		this.comboBoxUnit = new ComboBoxLengthUnit();
		this.labelLeftNumericFieldRadius = new JLabel("LeftNumericFieldRadius");
		this.labelRightNumericFieldRadius = new JLabel("RightNumericFieldRadius");
		this.numericFieldComboBoxLeft = new SmNumericFieldComboBox();
		this.numericFieldComboBoxRight = new SmNumericFieldComboBox();
	}

	private void initResources() {
		this.labelUnit.setText(SpatialAnalystProperties.getString("String_BufferRadiusUnit"));
		this.labelLeftNumericFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_LeftBufferRadius"));
		this.labelRightNumericFieldRadius.setText(SpatialAnalystProperties.getString("String_Label_RightBufferRadius"));

	}

	private void initLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_BufferRadius")));

		//@formatter:off
		GroupLayout panelBufferRadiusLayout = new GroupLayout(this);
		panelBufferRadiusLayout.setAutoCreateContainerGaps(true);
		panelBufferRadiusLayout.setAutoCreateGaps(true);
		this.setLayout(panelBufferRadiusLayout);

		panelBufferRadiusLayout.setHorizontalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.labelUnit)
						.addComponent(this.labelLeftNumericFieldRadius)
						.addComponent(this.labelRightNumericFieldRadius))

				.addGroup(panelBufferRadiusLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.comboBoxUnit, 5, 5, Short.MAX_VALUE)
						.addComponent(this.numericFieldComboBoxLeft, 5, 5, Short.MAX_VALUE)
						.addComponent(this.numericFieldComboBoxRight, 5, 5, Short.MAX_VALUE)));
		panelBufferRadiusLayout.setVerticalGroup(panelBufferRadiusLayout.createSequentialGroup()
				.addGroup(panelBufferRadiusLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelUnit)
						.addComponent(this.comboBoxUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelLeftNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(panelBufferRadiusLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.labelRightNumericFieldRadius)
						.addComponent(this.numericFieldComboBoxRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(5, 5, Short.MAX_VALUE));
		//@formatter:on

	}

	private void registerEvent() {
		((JTextField) this.getNumericFieldComboBoxLeft().getEditor().getEditorComponent()).addCaretListener(numericLeftCaretListener);
		((JTextField) this.getNumericFieldComboBoxRight().getEditor().getEditorComponent()).addCaretListener(numericRightCaretListener);

	}

	/**
	 * yuanR 2017.3.2
	 * 给“缓冲右半径长度”JComboBox添加光标改变事件，当值改变时，相应控件属性也更随改变：置灰确定按钮等
	 */
	class NumericRightCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			// 防止sql表达式面板弹出两次，当选中“表达式..”不进行同步--yuanR 2017.3.7
			String text = ((JTextField) e.getSource()).getText();
			if (radioButtonBufferTypeRound.isSelected() && !text.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
				// 当选择了圆头缓冲类型时，进行同步设置
				((JTextField) getNumericFieldComboBoxLeft().getEditor().getEditorComponent()).removeCaretListener(numericLeftCaretListener);
				getNumericFieldComboBoxLeft().setSelectedItem(text);
				((JTextField) getNumericFieldComboBoxLeft().getEditor().getEditorComponent()).addCaretListener(numericLeftCaretListener);
			}
		}
	}

	/**
	 * yuanR 2017.3.2
	 * 给“缓冲左半径长度”JComboBox添加光标改变事件，当值改变时，相应控件属性也更随改变：置灰确定按钮等
	 */
	class NumericLeftCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			// 防止sql表达式面板弹出两次，当选中“表达式..”不进行同步--yuanR 2017.3.7
			String text = ((JTextField) e.getSource()).getText();
			if (radioButtonBufferTypeRound.isSelected() && !text.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
				// 当选择了圆头缓冲类型时，进行同步设置
				((JTextField) getNumericFieldComboBoxRight().getEditor().getEditorComponent()).removeCaretListener(numericRightCaretListener);
				getNumericFieldComboBoxRight().setSelectedItem(text);
				((JTextField) getNumericFieldComboBoxRight().getEditor().getEditorComponent()).addCaretListener(numericRightCaretListener);
			}
		}
	}
}
