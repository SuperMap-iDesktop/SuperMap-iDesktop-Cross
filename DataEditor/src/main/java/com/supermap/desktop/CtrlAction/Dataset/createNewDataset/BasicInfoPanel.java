package com.supermap.desktop.CtrlAction.Dataset.createNewDataset;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxDatasetType;
import com.supermap.desktop.utilities.EncodeTypeUtilities;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by yuanR on 2017/8/15 0015.
 * 新建栅格/影像数据集-基本信息面板
 */
public class BasicInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private String datasetName;
	private Datasource targetDatasource;
	private EncodeType encodeType = EncodeType.NONE;
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

	public BasicInfoPanel(DatasetType inputDatasetType) {
		this.inputDatasetType = inputDatasetType;
		initComponents();
		initLayout();

		initStates(this.inputDatasetType);
		registerEvent();
	}

	private void registerEvent() {
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (inputDatasetType.equals(DatasetType.GRID)) {
					datasetNameTextField.setText(datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("New_Grid"));
				} else if (inputDatasetType.equals(DatasetType.IMAGE)) {
					datasetNameTextField.setText(datasourceComboBox.getSelectedDatasource().getDatasets().getAvailableDatasetName("New_Image"));
				}
			}
		});

//		comboboxEncodingType.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				encodeType = EncodeTypeUtilities.valueOf((String) comboboxEncodingType.getSelectedItem());
//			}
//		});

	}

	/**
	 * @param inputDatasetType
	 */
	private void initStates(DatasetType inputDatasetType) {
		if (inputDatasetType.equals(DatasetType.GRID)) {
			Datasource datasource = datasourceComboBox.getSelectedDatasource();
			datasetNameTextField.setText(datasource.getDatasets().getAvailableDatasetName("New_Grid"));
			ArrayList<String> tempGridEncodeType = new ArrayList<>();
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.SGL));
			tempGridEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
			comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempGridEncodeType.toArray(new String[tempGridEncodeType.size()])));
			comboboxEncodingType.setSelectedItem(tempGridEncodeType.get(0));
		} else if (inputDatasetType.equals(DatasetType.IMAGE)) {
			Datasource datasource = datasourceComboBox.getSelectedDatasource();
			datasetNameTextField.setText(datasource.getDatasets().getAvailableDatasetName("New_Image"));
			ArrayList<String> tempImageEncodeType = new ArrayList<>();
			tempImageEncodeType.add(EncodeTypeUtilities.toString(EncodeType.NONE));
			tempImageEncodeType.add(EncodeTypeUtilities.toString(EncodeType.DCT));
			tempImageEncodeType.add(EncodeTypeUtilities.toString(EncodeType.LZW));
			comboboxEncodingType.setModel(new DefaultComboBoxModel<>(tempImageEncodeType.toArray(new String[tempImageEncodeType.size()])));
			comboboxEncodingType.setSelectedItem(tempImageEncodeType.get(0));
		}

	}

	private void initComponents() {
		this.datasetNameLabel = new JLabel(ControlsProperties.getString("String_Label_DatasetName"));
		this.targetDatasourceLabel = new JLabel(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.encodeTypeLabel = new JLabel(CommonProperties.getString("String_Label_EncodeType"));
		datasetNameTextField = new JTextField();
		datasourceComboBox = new DatasourceComboBox();
		comboboxEncodingType = new ComboBoxDatasetType();
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


