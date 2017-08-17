package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.utilities.BlockSizeOptionUtilities;
import com.supermap.desktop.utilities.CharsetUtilities;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/16 0016.
 * 新建数据集-属性面板
 */
public class PropertyPanel extends JPanel {


	private JLabel encodingTypeLabel;
	private JLabel charestTypeLabel;
	// 栅格分块
	private JLabel blockSizeOptionLabel;
	// 影像格式
	private JLabel pixelFormatLabel;
	// 影像数据集的波段个数
	private JLabel bandCountLabel;
	// 高级设置
	private JLabel advancedSetLabel;


	private JComboBox comboboxEncodingType;
	private JComboBox comboboxCharest;
	private JComboBox comboboxBlockSizeOption;
	private JComboBox comboboxPixelFormat;
	private WaringTextField textFieldBandCount;
	private JButton buttonAdvancedSet;

	ArrayList<String> tempcharsharsetes;

	NewDatasetBean datasetBean;


	public PropertyPanel() {
		initComponents();
		initLayout(DatasetType.POINT);
//		initStates(this.datasetBean);
		registerEvent();
		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_GroupBox_ParameterSetting")));
	}

	private void initComponents() {
		this.encodingTypeLabel = new JLabel(CommonProperties.getString("String_Label_EncodeType"));
		this.charestTypeLabel = new JLabel(ControlsProperties.getString("String_LabelCharset"));
		this.pixelFormatLabel = new JLabel(CommonProperties.getString("String_PixelType"));
		this.blockSizeOptionLabel = new JLabel(DataEditorProperties.getString("String_NewDatasetBlockSizeOption"));
		this.bandCountLabel = new JLabel(ControlsProperties.getString("String_LabelBandsSize"));
		this.advancedSetLabel = new JLabel(ControlsProperties.getString("String_AdvancedSetLabel"));

		comboboxEncodingType = new JComboBox();
		comboboxCharest = new JComboBox();
		comboboxBlockSizeOption = new JComboBox();
		comboboxPixelFormat = new JComboBox();
		textFieldBandCount = new WaringTextField();

		buttonAdvancedSet = new JButton();
		buttonAdvancedSet.setText(ControlsProperties.getString("String_AdvancedSet"));
	}

	private void initLayout(DatasetType datasetType) {
		this.removeAll();
		if (datasetType.equals(DatasetType.IMAGE)) {
			GroupLayout groupLayout = new GroupLayout(this);
			groupLayout.setAutoCreateContainerGaps(true);
			groupLayout.setAutoCreateGaps(true);
			this.setLayout(groupLayout);
			//@formatter:off
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.encodingTypeLabel)
									.addComponent(this.blockSizeOptionLabel)
									.addComponent(this.pixelFormatLabel)
									.addComponent(this.bandCountLabel)
									.addComponent(this.advancedSetLabel))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)

									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxEncodingType, 150, 150, Short.MAX_VALUE))
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE))
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE))
									.addComponent(this.textFieldBandCount, 150, 150, Short.MAX_VALUE)
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.buttonAdvancedSet, 150, 150, Short.MAX_VALUE)))));
			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.encodingTypeLabel)
							.addComponent(this.comboboxEncodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.blockSizeOptionLabel)
							.addComponent(this.comboboxBlockSizeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.pixelFormatLabel)
							.addComponent(this.comboboxPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.bandCountLabel)
							.addComponent(this.textFieldBandCount, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.advancedSetLabel)
							.addComponent(this.buttonAdvancedSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
			//@formatter:on

		} else if (datasetType.equals(DatasetType.GRID)) {
			GroupLayout groupLayout = new GroupLayout(this);
			groupLayout.setAutoCreateContainerGaps(true);
			groupLayout.setAutoCreateGaps(true);
			this.setLayout(groupLayout);
			//@formatter:off
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.encodingTypeLabel)
									.addComponent(this.blockSizeOptionLabel)
									.addComponent(this.pixelFormatLabel)
									.addComponent(this.advancedSetLabel))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.comboboxEncodingType, 150, 150, Short.MAX_VALUE)
									.addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE)
									.addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE)
									.addComponent(this.buttonAdvancedSet, 150, 150, Short.MAX_VALUE))));
			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.encodingTypeLabel)
							.addComponent(this.comboboxEncodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.blockSizeOptionLabel)
							.addComponent(this.comboboxBlockSizeOption, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.pixelFormatLabel)
							.addComponent(this.comboboxPixelFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.advancedSetLabel)
							.addComponent(this.buttonAdvancedSet, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
			//@formatter:on

		} else {
			GroupLayout groupLayout = new GroupLayout(this);
			groupLayout.setAutoCreateContainerGaps(true);
			groupLayout.setAutoCreateGaps(true);
			this.setLayout(groupLayout);
			//@formatter:off
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(this.encodingTypeLabel)
									.addComponent(this.charestTypeLabel))
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxEncodingType, 150, 150, Short.MAX_VALUE))
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxCharest, 150, 150, Short.MAX_VALUE)))));
			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.encodingTypeLabel)
							.addComponent(this.comboboxEncodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(this.charestTypeLabel)
							.addComponent(this.comboboxCharest, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
			//@formatter:on
		}
	}

	public void initStates(NewDatasetBean datasetBean) {
		this.datasetBean = datasetBean;
		if (this.datasetBean != null) {

			if (this.datasetBean.getDatasetType().equals(DatasetType.IMAGE)) {
				// 编码类型
				comboboxEncodingType.removeAll();
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));

				// 栅格分块
				comboboxBlockSizeOption.removeAll();

				this.comboboxBlockSizeOption = new JComboBox<>();
				ArrayList<String> tempBlockSizeOptionType = new ArrayList<>();
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_64));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_128));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_256));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_512));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_1024));
				comboboxBlockSizeOption.setModel(new DefaultComboBoxModel<>(tempBlockSizeOptionType.toArray(new String[tempBlockSizeOptionType.size()])));
				comboboxBlockSizeOption.setSelectedItem(BlockSizeOptionUtilities.toString(this.datasetBean.getGridImageExtraDatasetBean().getBlockSizeOption()));

				// 像素格式
				this.comboboxPixelFormat = new JComboBox<>();
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
				comboboxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(this.datasetBean.getGridImageExtraDatasetBean().getPixelFormat()));
				// 波段数
				textFieldBandCount.setText(String.valueOf(this.datasetBean.getGridImageExtraDatasetBean().getBandCount()));
				textFieldBandCount.setInitInfo(1, 100, WaringTextField.INTEGER_TYPE, "0");

			} else if (this.datasetBean.getDatasetType().equals(DatasetType.GRID)) {
				comboboxEncodingType.removeAll();
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.SGL));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));


			} else if (DatasetType.LINE == datasetBean.getDatasetType() || DatasetType.REGION == datasetBean.getDatasetType()) {
				comboboxEncodingType.removeAll();
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.BYTE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT16));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT24));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT32));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));

				// 字符集
				comboboxCharest.removeAll();
				comboboxCharest.setModel(new DefaultComboBoxModel<>(tempcharsharsetes.toArray(new String[tempcharsharsetes.size()])));
			} else {
				// 编码类型
				comboboxEncodingType.removeAll();
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
				// 字符集
				comboboxCharest.removeAll();
				tempcharsharsetes = new ArrayList<String>();
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.OEM));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.EASTEUROPE));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.THAI));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.RUSSIAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.BALTIC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.ARABIC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.HEBREW));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.VIETNAMESE));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.TURKISH));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.GREEK));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.CHINESEBIG5));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.JOHAB));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.HANGEUL));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.SHIFTJIS));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.MAC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.SYMBOL));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.DEFAULT));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.ANSI));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.UTF8));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.UTF7));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.WINDOWS1252));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.KOREAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.UNICODE));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.CYRILLIC));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5GERMAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5SWEDISH));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.XIA5NORWEGIAN));
				tempcharsharsetes.add(CharsetUtilities.toString(Charset.GB18030));
				comboboxCharest.setModel(new DefaultComboBoxModel<>(tempcharsharsetes.toArray(new String[tempcharsharsetes.size()])));
			}
			comboboxEncodingType.setSelectedItem(EncodeTypeUtilities.toString(this.datasetBean.getEncodeType()));
			comboboxCharest.setSelectedItem(CharsetUtilities.toString(this.datasetBean.getCharset()));

			initLayout(datasetBean.getDatasetType());
		}
	}

	private void registerEvent() {
		textFieldBandCount.registEvents();
		// 给面板中的控件添加监听，当改变时，设置原数据的属性值也改变
		comboboxEncodingType.addItemListener(itemListener);
		comboboxCharest.addItemListener(itemListener);
		comboboxBlockSizeOption.addItemListener(itemListener);
		comboboxPixelFormat.addItemListener(itemListener);
		textFieldBandCount.getTextField().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int bandCount = Integer.valueOf(textFieldBandCount.getTextField().getText());
				if (!(bandCount < 1 || bandCount > 100)) {
					datasetBean.getGridImageExtraDatasetBean().setBandCount(bandCount);
				}
			}
		});

		buttonAdvancedSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogNewImageDataset dialogNewGridDataset = new JDialogNewImageDataset(datasetBean);
				dialogNewGridDataset.showDialog();
			}
		});

	}

	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboboxEncodingType) {
				datasetBean.setEncodeType(EncodeTypeUtilities.valueOf((String) comboboxEncodingType.getSelectedItem()));
			} else if (e.getSource() == comboboxCharest) {
				datasetBean.setCharset(CharsetUtilities.valueOf((String) comboboxCharest.getSelectedItem()));
			} else if (e.getSource() == comboboxBlockSizeOption) {
				datasetBean.getGridImageExtraDatasetBean().setBlockSizeOption(BlockSizeOptionUtilities.valueOf((String) comboboxBlockSizeOption.getSelectedItem()));
			} else if (e.getSource() == comboboxPixelFormat) {
				datasetBean.getGridImageExtraDatasetBean().setPixelFormat(PixelFormatUtilities.valueOf((String) comboboxPixelFormat.getSelectedItem()));
			}
		}
	};

	public void setPanelEnable(Boolean enable) {
		comboboxEncodingType.setEnabled(enable);
		comboboxCharest.setEnabled(enable);
		comboboxBlockSizeOption.setEnabled(enable);
		comboboxPixelFormat.setEnabled(enable);
		textFieldBandCount.getTextField().setEnabled(enable);
		buttonAdvancedSet.setEnabled(enable);
	}

}
