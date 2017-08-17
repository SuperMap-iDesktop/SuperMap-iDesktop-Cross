package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.DatasetType;
import com.supermap.data.EncodeType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建栅格/影像数据集-基本信息面板
 */
public class PanelBasicInfoSet extends JPanel {

	private static final long serialVersionUID = 1L;

	private EncodeType encodeType;
	private DatasetType inputDatasetType;

	private JLabel datasetNameLabel;
	private JLabel targetDatasourceLabel;
	private JLabel encodeTypeLabel;
	private JTextField datasetNameTextField;
	private DatasourceComboBox datasourceComboBox;
	private JComboBox<String> comboboxEncodingType;

	public EncodeType getEncodeType() {
		encodeType = EncodeTypeUtilities.valueOf((String) comboboxEncodingType.getSelectedItem());
		return encodeType;
	}

	public JTextField getDatasetNameTextField() {
		return datasetNameTextField;
	}

	public DatasourceComboBox getDatasourceComboBox() {
		return datasourceComboBox;
	}

	public PanelBasicInfoSet(DatasetType inputDatasetType) {
		this.inputDatasetType = inputDatasetType;
		initComponents();
		initLayout();

//		initStates(this.inputDatasetType);
		registerEvent();
	}

	private void registerEvent() {
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				resetDatasetName();
			}
		});

		datasetNameTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (StringUtilities.isNullOrEmpty(datasetNameTextField.getText())) {
					resetDatasetName();
				}
			}
		});
	}

	/**
	 * 根据数据集类型重设数据集名称
	 */
	public void resetDatasetName() {
		if (inputDatasetType.equals(DatasetType.GRID)) {
			datasetNameTextField.setText(datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("New_Grid"));
		} else if (inputDatasetType.equals(DatasetType.IMAGE)) {
			datasetNameTextField.setText(datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("New_Image"));
		}
	}

	/**
	 * @param datasetBean
	 */
	public void initStates(NewDatasetBean datasetBean) {
		datasourceComboBox.setSelectedDatasource(datasetBean.getDatasource());
		datasetNameTextField.setText(datasetBean.getDatasetName());
		comboboxEncodingType.setSelectedItem(EncodeTypeUtilities.toString(datasetBean.getEncodeType()));
	}

	private void initComponents() {
		this.datasetNameLabel = new JLabel(ControlsProperties.getString("String_Label_DatasetName"));
		this.targetDatasourceLabel = new JLabel(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.encodeTypeLabel = new JLabel(CommonProperties.getString("String_Label_EncodeType"));
		this.datasetNameTextField = new JTextField();
		this.datasourceComboBox = new DatasourceComboBox();
		this.comboboxEncodingType = new JComboBox<>();

		if (inputDatasetType.equals(DatasetType.GRID)) {
//			comboboxEncodingType.removeAllItems();
			ArrayList<String> tempGridEncodeType = new ArrayList<>();
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.SGL));
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
			comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempGridEncodeType.toArray(new String[tempGridEncodeType.size()])));
		} else if (inputDatasetType.equals(DatasetType.IMAGE)) {
//			comboboxEncodingType.removeAllItems();
			ArrayList<String> tempImageEncodeType = new ArrayList<>();
			tempImageEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
			tempImageEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
			tempImageEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
			comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempImageEncodeType.toArray(new String[tempImageEncodeType.size()])));
		}
	}


	private void initLayout() {
		this.setBorder(BorderFactory.createTitledBorder(DataEditorProperties.getString("String_NewDatasetBase")));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);
		//@formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasetNameLabel)
								.addComponent(this.targetDatasourceLabel)
								.addComponent(this.encodeTypeLabel))
						.addGroup(groupLayout.createParallelGroup()
								.addComponent(this.datasetNameTextField, 150, 150, Short.MAX_VALUE)
								.addComponent(this.datasourceComboBox, 150, 150, Short.MAX_VALUE)
								.addComponent(this.comboboxEncodingType, 150, 150, Short.MAX_VALUE))));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.datasetNameLabel)
								.addComponent(this.datasetNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.targetDatasourceLabel)
								.addComponent(this.datasourceComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.encodeTypeLabel)
								.addComponent(this.comboboxEncodingType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))));
		//@formatter:on
	}
}


