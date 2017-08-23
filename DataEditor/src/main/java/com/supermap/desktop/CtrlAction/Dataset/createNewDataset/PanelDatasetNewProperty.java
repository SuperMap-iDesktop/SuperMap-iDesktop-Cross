package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxCharset;
import com.supermap.desktop.utilities.BlockSizeOptionUtilities;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/16 0016.
 * 新建数据集-属性面板
 * 属性面板支持对选中数据集集合的批量操作
 */
public class PanelDatasetNewProperty extends JPanel {

	private static final long serialVersionUID = 1L;

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


	private JComboBox<String> comboboxEncodingType;

	private ComboBoxCharset comboboxCharest;
	private JComboBox<String> comboboxBlockSizeOption;
	private JComboBox<String> comboboxPixelFormat;
	private WaringTextField textFieldBandCount;
	private JButton buttonAdvancedSet;

	//	ArrayList<String> tempcharsharsetes;
	ArrayList<NewDatasetBean> datasetBeans;
	NewDatasetBean datasetBeanFrist;

	public ComboBoxCharset getComboboxCharest() {
		return comboboxCharest;
	}

	public JComboBox<String> getComboboxEncodingType() {
		return comboboxEncodingType;
	}

	public PanelDatasetNewProperty() {
		initComponents();
		initLayout(DatasetType.POINT);
//		initStates(this.datasetBeanFrist);
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

		comboboxEncodingType = new JComboBox<>();
		comboboxCharest = new ComboBoxCharset();
		comboboxCharest.setSelectedItem(Charset.UTF8);
		comboboxEncodingType.setEnabled(false);
		comboboxCharest.setEnabled(false);

		comboboxBlockSizeOption = new JComboBox<String>();
		comboboxPixelFormat = new JComboBox<>();
		textFieldBandCount = new WaringTextField();
		textFieldBandCount.setInitInfo(1, 100, WaringTextField.INTEGER_TYPE, "0");

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
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxEncodingType, 150, 150, Short.MAX_VALUE))
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxBlockSizeOption, 150, 150, Short.MAX_VALUE))
									.addGroup(groupLayout.createSequentialGroup()
											.addGap(16).addComponent(this.comboboxPixelFormat, 150, 150, Short.MAX_VALUE))
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

	/**
	 * 根据JTable选择情况，初始化属性面板
	 *
	 * @param datasetBeans
	 */
	public void initStates(ArrayList<NewDatasetBean> datasetBeans) {
		this.datasetBeans = datasetBeans;
		// 以排头对象的属性值进行面板的初始值设置
		datasetBeanFrist = datasetBeans.get(0);
		if (this.datasetBeanFrist != null) {

			if (this.datasetBeanFrist.getDatasetType().equals(DatasetType.IMAGE)) {
				// 编码类型
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
				// 栅格分块
				ArrayList<String> tempBlockSizeOptionType = new ArrayList<>();
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_64));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_128));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_256));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_512));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_1024));
				comboboxBlockSizeOption.setModel(new DefaultComboBoxModel<>(tempBlockSizeOptionType.toArray(new String[tempBlockSizeOptionType.size()])));
				comboboxBlockSizeOption.setSelectedItem(BlockSizeOptionUtilities.toString(this.datasetBeanFrist.getGridImageExtraDatasetBean().getBlockSizeOption()));
				// 像素格式
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
				comboboxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(this.datasetBeanFrist.getGridImageExtraDatasetBean().getPixelFormatImage()));
				// 波段数
				textFieldBandCount.getTextField().removeCaretListener(caretListener);
				textFieldBandCount.setText(String.valueOf(this.datasetBeanFrist.getGridImageExtraDatasetBean().getBandCount()));
				textFieldBandCount.getTextField().addCaretListener(caretListener);

			} else if (this.datasetBeanFrist.getDatasetType().equals(DatasetType.GRID)) {
				// 编码类型
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.SGL));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
				// 栅格分块
				ArrayList<String> tempBlockSizeOptionType = new ArrayList<>();
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_64));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_128));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_256));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_512));
				tempBlockSizeOptionType.add(BlockSizeOptionUtilities.toString(BlockSizeOption.BS_1024));
				comboboxBlockSizeOption.setModel(new DefaultComboBoxModel<>(tempBlockSizeOptionType.toArray(new String[tempBlockSizeOptionType.size()])));
				comboboxBlockSizeOption.setSelectedItem(BlockSizeOptionUtilities.toString(this.datasetBeanFrist.getGridImageExtraDatasetBean().getBlockSizeOption()));
				// 像素格式
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
				comboboxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(this.datasetBeanFrist.getGridImageExtraDatasetBean().getPixelFormatGrid()));

			} else if (DatasetType.LINE == datasetBeanFrist.getDatasetType() || DatasetType.REGION == datasetBeanFrist.getDatasetType()) {
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.BYTE));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT16));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT24));
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.INT32));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));

			} else {
				// 编码类型
				ArrayList<String> tempEncodeType = new ArrayList<>();
				tempEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
				comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempEncodeType.toArray(new String[tempEncodeType.size()])));
			}
			comboboxEncodingType.setSelectedItem(EncodeTypeUtilities.toString(this.datasetBeanFrist.getEncodeType()));
			comboboxCharest.setSelectedItem(this.datasetBeanFrist.getCharset());
			initLayout(datasetBeanFrist.getDatasetType());
		}
	}

	private void registerEvent() {
		textFieldBandCount.registEvents();
		// 给面板中的控件添加监听，当改变时，设置原数据的属性值也改变
		comboboxEncodingType.addItemListener(itemListener);
		comboboxCharest.addItemListener(itemListener);
		comboboxBlockSizeOption.addItemListener(itemListener);
		comboboxPixelFormat.addItemListener(itemListener);
		textFieldBandCount.getTextField().addCaretListener(caretListener);

		buttonAdvancedSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (datasetBeanFrist.getDatasetType().equals(DatasetType.GRID)) {

					JDialogDatasetGridAdvanceSet dialogDatasetGridAdvanceSet = new JDialogDatasetGridAdvanceSet(datasetBeans);
					dialogDatasetGridAdvanceSet.showDialog();
				} else if (datasetBeanFrist.getDatasetType().equals(DatasetType.IMAGE)) {
					JDialogDatasetImageAdvanceSet dialogDatasetImageAdvanceSet = new JDialogDatasetImageAdvanceSet(datasetBeans);
					dialogDatasetImageAdvanceSet.showDialog();
				}
			}
		});
	}


	private CaretListener caretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			int bandCount = 0;
			if (!StringUtilities.isNullOrEmpty(textFieldBandCount.getTextField().getText())) {
				bandCount = Integer.valueOf(textFieldBandCount.getTextField().getText());
			}
			if (!(bandCount < 1 || bandCount > 100)) {
				textFieldBandCount.getTextField().removeCaretListener(caretListener);
				for (int i = 0; i < datasetBeans.size(); i++) {
					datasetBeans.get(i).getGridImageExtraDatasetBean().setBandCount(bandCount);
				}
				textFieldBandCount.getTextField().addCaretListener(caretListener);
			}
		}
	};
	private ItemListener itemListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			batchSet(e);
		}
	};

	private void batchSet(ItemEvent e) {
		for (int i = 0; i < datasetBeans.size(); i++) {
			if (e.getSource() == comboboxEncodingType) {
				datasetBeans.get(i).setEncodeType(EncodeTypeUtilities.valueOf((String) comboboxEncodingType.getSelectedItem()));
			} else if (e.getSource() == comboboxCharest) {
				datasetBeans.get(i).setCharset(comboboxCharest.getSelectedItem());
			} else if (e.getSource() == comboboxBlockSizeOption) {
				datasetBeans.get(i).getGridImageExtraDatasetBean().setBlockSizeOption(BlockSizeOptionUtilities.valueOf((String) comboboxBlockSizeOption.getSelectedItem()));
			} else if (e.getSource() == comboboxPixelFormat) {
				if (datasetBeans.get(i).getDatasetType().equals(DatasetType.GRID)) {
					datasetBeans.get(i).getGridImageExtraDatasetBean().setPixelFormatGrid(PixelFormatUtilities.valueOf((String) comboboxPixelFormat.getSelectedItem()));
				} else if (datasetBeans.get(i).getDatasetType().equals(DatasetType.IMAGE)) {
					datasetBeans.get(i).getGridImageExtraDatasetBean().setPixelFormatImage(PixelFormatUtilities.valueOf((String) comboboxPixelFormat.getSelectedItem()));
				}
			}
		}
	}

	protected void setPanelEnable(Boolean enable) {
		comboboxEncodingType.setEnabled(enable);
		comboboxCharest.setEnabled(enable);
		comboboxBlockSizeOption.setEnabled(enable);
		comboboxPixelFormat.setEnabled(enable);
		textFieldBandCount.getTextField().setEnabled(enable);
		buttonAdvancedSet.setEnabled(enable);
	}
}
