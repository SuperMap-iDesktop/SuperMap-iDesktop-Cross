package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.BlockSizeOption;
import com.supermap.data.DatasetType;
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
 * 新建栅格/影像数据集-通用属性设置面板
 */

public class GridImageGeneralInfoPanel extends JPanel {

	private DatasetType inputDatasetType;
	// 栅格分块
	private JLabel blockSizeOptionLabel;
	// 影像格式
	private JLabel pixelFormatLabel;

	private JLabel resolutionXLabel;
	private JLabel resolutionYLabel;
	private JLabel rowCountLabel;
	private JLabel columnCountLabel;
	// 影像数据集的波段个数
	private JLabel imageDatasetbandCountLabel;
	// 栅格数据集的
	private JLabel gridDatasetNoValueLabel;
	private JLabel gridDatasetMaxValueLabel;
	private JLabel gridDatasetMinValueLabel;

	private JComboBox<String> comboboxBlockSizeOption;
	private JComboBox<String> comboboxPixelFormat;

	private WaringTextField textFieldResolutionX;
	private WaringTextField textFieldResolutionY;
	private JTextField textFieldRowCount;
	private JTextField textFieldColumnCount;
	private WaringTextField textFieldImageDatasetbandCount;
	private WaringTextField textFieldGridDatasetMaxValue;
	private WaringTextField textFieldGridDatasetMinValue;
	private WaringTextField textFieldGridDatasetNoValueLabel;


	public GridImageGeneralInfoPanel(DatasetType inputDatasetType) {
		super();
		this.inputDatasetType = inputDatasetType;
		initComponents();
		initLayout();

		initStates();
		registerEvent();
	}


	private void initComponents() {

		this.blockSizeOptionLabel = new JLabel(DataEditorProperties.getString("String_NewDatasetBlockSizeOption"));
		this.pixelFormatLabel = new JLabel(CommonProperties.getString("String_PixelType"));
		this.resolutionXLabel = new JLabel(ControlsProperties.getString("String_LabelXPixelFormat"));
		this.resolutionYLabel = new JLabel(ControlsProperties.getString("String_LabelYPixelFormat"));
		this.rowCountLabel = new JLabel(ControlsProperties.getString("String_LabelRowsSize"));
		this.columnCountLabel = new JLabel(ControlsProperties.getString("String_LabelColumnsSize"));
		this.imageDatasetbandCountLabel = new JLabel(ControlsProperties.getString("String_LabelBandsSize"));
		this.gridDatasetMaxValueLabel = new JLabel(ControlsProperties.getString("String_LabelMaxValue"));
		this.gridDatasetMinValueLabel = new JLabel(ControlsProperties.getString("String_LabelMinValue"));
		this.gridDatasetNoValueLabel = new JLabel(ControlsProperties.getString("String_LabelNoValue"));

		this.comboboxBlockSizeOption = new JComboBox<>();
		this.comboboxPixelFormat = new JComboBox<>();
		this.textFieldResolutionX = new WaringTextField();
		this.textFieldResolutionY = new WaringTextField();
		this.textFieldRowCount = new JTextField();
		this.textFieldColumnCount = new JTextField();
		this.textFieldGridDatasetMaxValue = new WaringTextField();
		this.textFieldGridDatasetMinValue = new WaringTextField();
		this.textFieldGridDatasetNoValueLabel = new WaringTextField();
		this.textFieldImageDatasetbandCount = new WaringTextField();

	}

	private void initLayout() {
		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDatasetProperty")));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		if (inputDatasetType.equals(DatasetType.GRID)) {
			//@formatter:off
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.blockSizeOptionLabel)
									.addComponent(this.pixelFormatLabel)
									.addComponent(this.resolutionXLabel)
									.addComponent(this.resolutionYLabel)
									.addComponent(this.rowCountLabel)
									.addComponent(this.columnCountLabel)
									.addComponent(this.gridDatasetMaxValueLabel)
									.addComponent(this.gridDatasetMinValueLabel)
									.addComponent(this.gridDatasetNoValueLabel)
									.addComponent(this.imageDatasetbandCountLabel))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE)
									.addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldResolutionX, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldResolutionY, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldRowCount, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldColumnCount, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldGridDatasetMaxValue, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldGridDatasetMinValue, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldGridDatasetNoValueLabel, 150, 150, Short.MAX_VALUE))));

			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.blockSizeOptionLabel)
									.addComponent(this.comboboxBlockSizeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.pixelFormatLabel)
									.addComponent(this.comboboxPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.resolutionXLabel)
									.addComponent(this.textFieldResolutionX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.resolutionYLabel)
									.addComponent(this.textFieldResolutionY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.rowCountLabel)
									.addComponent(this.textFieldRowCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.columnCountLabel)
									.addComponent(this.textFieldColumnCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.gridDatasetMaxValueLabel)
									.addComponent(this.textFieldGridDatasetMaxValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.gridDatasetMinValueLabel)
									.addComponent(this.textFieldGridDatasetMinValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.gridDatasetNoValueLabel)
									.addComponent(this.textFieldGridDatasetNoValueLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))));
			//@formatter:on
		} else if (inputDatasetType.equals(DatasetType.IMAGE)) {
			//@formatter:off
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.blockSizeOptionLabel)
									.addComponent(this.pixelFormatLabel)
									.addComponent(this.resolutionXLabel)
									.addComponent(this.resolutionYLabel)
									.addComponent(this.rowCountLabel)
									.addComponent(this.columnCountLabel)
									.addComponent(this.gridDatasetMaxValueLabel)
									.addComponent(this.gridDatasetMinValueLabel)
									.addComponent(this.gridDatasetNoValueLabel)
									.addComponent(this.imageDatasetbandCountLabel))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE)
									.addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldResolutionX, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldResolutionY, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldRowCount, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldColumnCount, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldGridDatasetMaxValue, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldGridDatasetMinValue, 150, 150, Short.MAX_VALUE)
									.addComponent(this.textFieldGridDatasetNoValueLabel, 150, 150, Short.MAX_VALUE))));

			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.blockSizeOptionLabel)
									.addComponent(this.comboboxBlockSizeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.pixelFormatLabel)
									.addComponent(this.comboboxPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.resolutionXLabel)
									.addComponent(this.textFieldResolutionX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.resolutionYLabel)
									.addComponent(this.textFieldResolutionY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.rowCountLabel)
									.addComponent(this.textFieldRowCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.columnCountLabel)
									.addComponent(this.textFieldColumnCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.gridDatasetMaxValueLabel)
									.addComponent(this.textFieldGridDatasetMaxValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.gridDatasetMinValueLabel)
									.addComponent(this.textFieldGridDatasetMinValue, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
									.addComponent(this.gridDatasetNoValueLabel)
									.addComponent(this.textFieldGridDatasetNoValueLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))));
			//@formatter:on


		}


	}

	private void initStates() {

		if (inputDatasetType.equals(DatasetType.GRID)) {
			ArrayList<String> tempGridPixelFormatType = new ArrayList<>();
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
			comboboxPixelFormat.setModel(new DefaultComboBoxModel<>(tempGridPixelFormatType.toArray(new String[tempGridPixelFormatType.size()])));
			comboboxPixelFormat.setSelectedItem(tempGridPixelFormatType.get(10));

		} else if (inputDatasetType.equals(DatasetType.IMAGE)) {
			ArrayList<String> tempImagePixelFormatType = new ArrayList<>();
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT1));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT4));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT8));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT16));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.UBIT32));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT8));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT16));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT32));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.BIT64));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.RGB));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.RGBA));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.SINGLE));
			tempImagePixelFormatType.add(PixelFormatUtilities.toString(PixelFormat.DOUBLE));
			comboboxPixelFormat.setModel(new DefaultComboBoxModel<>(tempImagePixelFormatType.toArray(new String[tempImagePixelFormatType.size()])));
			comboboxPixelFormat.setSelectedItem(tempImagePixelFormatType.get(10));
		}

		ArrayList<String> tempBlockSizeOptionType = new ArrayList<>();
		tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_64));
		tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_128));
		tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_256));
		tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_512));
		tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_1024));
		comboboxBlockSizeOption.setModel(new DefaultComboBoxModel<>(tempBlockSizeOptionType.toArray(new String[tempBlockSizeOptionType.size()])));
		comboboxBlockSizeOption.setSelectedItem(tempBlockSizeOptionType.get(0));

		this.textFieldResolutionX.setText("0.5");
		this.textFieldResolutionY.setText("0.5");
		this.textFieldRowCount.setText("800");
		this.textFieldColumnCount.setText("800");
		this.textFieldGridDatasetMaxValue.setText("10000");
		this.textFieldGridDatasetMinValue.setText("-1000");
		this.textFieldGridDatasetNoValueLabel.setText("-9999");
		this.textFieldImageDatasetbandCount.setText("1");


	}

	private void registerEvent() {

	}


}
