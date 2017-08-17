package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.BlockSizeOption;
import com.supermap.data.PixelFormat;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.BlockSizeOptionUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建影像数据-图像属性面板
 */
public class PanelDatasetImageProperty extends JPanel {

	private static final long serialVersionUID = 1L;

	// 栅格分块
	private JLabel blockSizeOptionLabel;
	// 影像格式
	private JLabel pixelFormatLabel;
	// 影像数据集的波段个数
	private JLabel bandCountLabel;

	private JComboBox<String> comboboxBlockSizeOption;
	private JComboBox<String> comboboxPixelFormat;
	private WaringTextField textFieldImageDatasetbandCount;

	ArrayList<String> tempBlockSizeOptionType;
	ArrayList<String> tempImagePixelFormatType;

	public JComboBox<String> getComboboxBlockSizeOption() {
		return comboboxBlockSizeOption;
	}

	public JComboBox<String> getComboboxPixelFormat() {
		return comboboxPixelFormat;
	}

	public WaringTextField getTextFieldImageDatasetbandCount() {
		return textFieldImageDatasetbandCount;
	}


	public PanelDatasetImageProperty() {
		initComponents();
		initLayout();
//		initStates();
		registerEvent();
	}

	private void initComponents() {
		this.blockSizeOptionLabel = new JLabel(DataEditorProperties.getString("String_NewDatasetBlockSizeOption"));
		this.pixelFormatLabel = new JLabel(CommonProperties.getString("String_PixelType"));
		this.bandCountLabel = new JLabel(ControlsProperties.getString("String_LabelBandsSize"));

		this.comboboxBlockSizeOption = new JComboBox<>();
		this.tempBlockSizeOptionType = new ArrayList<>();
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_64));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_128));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_256));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_512));
		this.tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_1024));
		this.comboboxBlockSizeOption.setModel(new DefaultComboBoxModel<>(tempBlockSizeOptionType.toArray(new String[tempBlockSizeOptionType.size()])));


		this.comboboxPixelFormat = new JComboBox<>();
		this.tempImagePixelFormatType = new ArrayList<>();
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT1));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT4));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT8));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT16));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT32));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT8));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT16));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT32));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT64));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.RGB));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.RGBA));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.SINGLE));
		this.tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.DOUBLE));
		this.comboboxPixelFormat.setModel(new DefaultComboBoxModel<>(tempImagePixelFormatType.toArray(new String[tempImagePixelFormatType.size()])));

		this.textFieldImageDatasetbandCount = new WaringTextField();
		this.textFieldImageDatasetbandCount.setInitInfo(1, 100, WaringTextField.INTEGER_TYPE, "0");

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
								.addComponent(this.bandCountLabel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(16).addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
										.addGap(16).addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE))
								.addComponent(this.textFieldImageDatasetbandCount, 150, 150, Short.MAX_VALUE))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.blockSizeOptionLabel)
						.addComponent(this.comboboxBlockSizeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.pixelFormatLabel)
						.addComponent(this.comboboxPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.bandCountLabel)
						.addComponent(this.textFieldImageDatasetbandCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		//@formatter:on

	}

	public void initStates(BlockSizeOption blockSizeOption, PixelFormat format, int count) {
		this.comboboxBlockSizeOption.setSelectedItem(BlockSizeOptionUtilities.toString(blockSizeOption));
		this.comboboxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(format));
		this.textFieldImageDatasetbandCount.setText(String.valueOf(count));
	}

	private void registerEvent() {
		this.textFieldImageDatasetbandCount.registEvents();
	}
}
