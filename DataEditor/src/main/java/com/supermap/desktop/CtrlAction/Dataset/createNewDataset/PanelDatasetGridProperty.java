package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.BlockSizeOption;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.BlockSizeOptionUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/17 0017.
 * 新建栅格数据集高级设置-属性面板
 */
public class PanelDatasetGridProperty extends JPanel {

	private static final long serialVersionUID = 1L;

	// 栅格分块
	private JLabel blockSizeOptionLabel;
	// 影像格式
	private JLabel pixelFormatLabel;
	//
	private JLabel noValueLabel;
	private JLabel maxValueLabel;
	private JLabel minValueLabel;


	private JComboBox<String> comboboxBlockSizeOption;
	private JComboBox<String> comboboxPixelFormat;

	private WaringTextField textFieldNoValue;
	private WaringTextField textFieldMaxValue;
	private WaringTextField textFieldMinValue;

	ArrayList<String> tempBlockSizeOptionType;
	ArrayList<String> tempGridPixelFormatType;

	public JComboBox<String> getComboboxBlockSizeOption() {
		return comboboxBlockSizeOption;
	}

	public JComboBox<String> getComboboxPixelFormat() {
		return comboboxPixelFormat;
	}

	public WaringTextField getTextFieldMaxValue() {
		return textFieldMaxValue;
	}

	public WaringTextField getTextFieldMinValue() {
		return textFieldMinValue;
	}

	public WaringTextField getTextFieldNoValue() {
		return textFieldNoValue;
	}


	private CaretListener textFieldMaxCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldMaxValueChange();
		}
	};
	private CaretListener textFieldMinCaretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			textFieldMinValueChange();
		}
	};

	/**
	 * 范围文本框——Max——改变事件
	 */
	private void textFieldMaxValueChange() {
		// 当左范围文本框改变时，设置右范围文本框的限制范围
		String str = this.textFieldMaxValue.getTextField().getText();
		if (!StringUtilities.isNullOrEmpty(str) && DoubleUtilities.isDouble(str)) {
			this.textFieldMinValue.setInitInfo(
					(-Double.MAX_VALUE),
					DoubleUtilities.stringToValue(textFieldMaxValue.getTextField().getText()),
					WaringTextField.FLOAT_TYPE,
					"22");
		} else {
			this.textFieldMinValue.setInitInfo(
					(-Double.MAX_VALUE),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"22");
		}
	}

	/**
	 * 范围文本框——Min——改变事件
	 */
	private void textFieldMinValueChange() {
		// 当右范围文本框改变时，设置左范围文本框的限制范围
		String str = this.textFieldMinValue.getTextField().getText();
		if (!StringUtilities.isNullOrEmpty(str) && DoubleUtilities.isDouble(str)) {
			this.textFieldMaxValue.setInitInfo(
					DoubleUtilities.stringToValue(this.textFieldMinValue.getTextField().getText()),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"22");
		} else {
			this.textFieldMaxValue.setInitInfo(
					(-Double.MAX_VALUE),
					Double.MAX_VALUE,
					WaringTextField.FLOAT_TYPE,
					"22");
		}
	}


	public PanelDatasetGridProperty() {
		initComponents();
		initLayout();
//		initStates();
		registerEvent();
	}


	public void initStates(BlockSizeOption blockSizeOption, PixelFormat format, double noValue, double maxValue, double minValue) {
		this.comboboxBlockSizeOption.setSelectedItem(BlockSizeOptionUtilities.toString(blockSizeOption));
		this.comboboxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(format));
		this.textFieldNoValue.setText(String.valueOf(noValue));
		this.textFieldMaxValue.setText(String.valueOf(maxValue));
		this.textFieldMinValue.setText(String.valueOf(minValue));
	}

	private void initComponents() {
		this.blockSizeOptionLabel = new JLabel(DataEditorProperties.getString("String_NewDatasetBlockSizeOption"));
		this.pixelFormatLabel = new JLabel(CommonProperties.getString("String_PixelType"));
		this.noValueLabel = new JLabel(DataEditorProperties.getString("String_NewDatasetNoValue"));
		this.maxValueLabel = new JLabel(CommonProperties.getString("String_MAXGrid"));
		this.minValueLabel = new JLabel(CommonProperties.getString("String_MINGrid"));

		this.comboboxBlockSizeOption = new JComboBox<>();
		this.tempBlockSizeOptionType = new ArrayList<>();
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_64));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_128));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_256));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_512));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_1024));
		this.comboboxBlockSizeOption.setModel(new DefaultComboBoxModel<>(tempBlockSizeOptionType.toArray(new String[tempBlockSizeOptionType.size()])));

		this.comboboxPixelFormat = new JComboBox<>();
		this.tempGridPixelFormatType = new ArrayList<>();
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT1));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT4));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT8));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT16));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT32));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT8));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT16));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT32));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT64));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.SINGLE));
		tempGridPixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.DOUBLE));
		this.comboboxPixelFormat.setModel(new DefaultComboBoxModel<>(tempGridPixelFormatType.toArray(new String[tempGridPixelFormatType.size()])));

		this.textFieldNoValue = new WaringTextField();
		this.textFieldMaxValue = new WaringTextField();
		this.textFieldMinValue = new WaringTextField();

		this.textFieldNoValue.setInitInfo((-Double.MAX_VALUE), Double.MAX_VALUE, WaringTextField.FLOAT_TYPE, "null");
	}

	private void initLayout() {
		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDatasetProperty")));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.blockSizeOptionLabel)
								.addComponent(this.pixelFormatLabel)
								.addComponent(this.noValueLabel)
								.addComponent(this.maxValueLabel)
								.addComponent(this.minValueLabel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(16).addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(16).addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE))
								.addComponent(this.textFieldNoValue, 150, 150, Short.MAX_VALUE)
								.addComponent(this.textFieldMaxValue, 150, 150, Short.MAX_VALUE)
								.addComponent(this.textFieldMinValue, 150, 150, Short.MAX_VALUE))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.blockSizeOptionLabel)
						.addComponent(this.comboboxBlockSizeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.pixelFormatLabel)
						.addComponent(this.comboboxPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.noValueLabel)
						.addComponent(this.textFieldNoValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.maxValueLabel)
						.addComponent(this.textFieldMaxValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.minValueLabel)
						.addComponent(this.textFieldMinValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on

	}

	private void registerEvent() {
		this.textFieldNoValue.registEvents();
		this.textFieldMaxValue.registEvents();
		this.textFieldMinValue.registEvents();
		this.textFieldMaxValue.getTextField().addCaretListener(textFieldMaxCaretListener);
		this.textFieldMinValue.getTextField().addCaretListener(textFieldMinCaretListener);
	}
}
