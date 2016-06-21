package com.supermap.desktop.geometryoperation.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;

public class JDialogGeometryConvert extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String DEFAULT_DATASET_NAME = "ConvertResult";

	private JLabel labelDesDatasource;
	private JLabel labelDesDataset;
	private DatasourceComboBox comboBoxDatasource;
	private DatasetComboBox comboBoxDataset;
	private JCheckBox checkBoxNewDataset;
	private SmTextFieldLegit textFieldNewDataset;
	private JCheckBox checkBoxRemoveSrc;
	private JButton buttonOK;
	private JButton buttonCancel;

	private DatasetType datasetType; // 目标数据集类型
	private Datasource desDatasource;
	private DatasetVector desDataset;
	private String newDatasetName;
	private boolean isNewDataset;
	private boolean isRemoveSrc;

	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == JDialogGeometryConvert.this.comboBoxDatasource) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxDesDatasourceSelectedChange();
				}
			} else if (e.getSource() == JDialogGeometryConvert.this.comboBoxDataset) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxDesDatasetSelectedChange();
				}
			} else if (e.getSource() == JDialogGeometryConvert.this.checkBoxNewDataset) {
				checkBoxNewDatasetCheckedChange();
			} else if (e.getSource() == JDialogGeometryConvert.this.checkBoxRemoveSrc) {
				checkBoxRemoveSrcCheckedChange();
			}
		}
	};

	private DocumentListener documentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldNewDatasetTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldNewDatasetTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub

		}
	};

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == JDialogGeometryConvert.this.buttonOK) {
				buttonOKClick();
			} else if (e.getSource() == JDialogGeometryConvert.this.buttonCancel) {
				buttonCancelClick();
			}
		}
	};

	public JDialogGeometryConvert(String title, DatasetType datasetType) {
		this.datasetType = datasetType;
		initializeDatas();
		initializeComponents(title);
		initializeComponentsValue();
		registerEvents();
	}

	public Datasource getDesDatasource() {
		return this.desDatasource;
	}

	public DatasetVector getDesDataset() {
		return this.desDataset;
	}

	public boolean isNewDataset() {
		return this.isNewDataset;
	}

	public String getNewDatasetName() {
		return this.newDatasetName;
	}

	public boolean isRemoveSrc() {
		return this.isRemoveSrc;
	}

	private void initializeDatas() {

		// 目标数据源
		initializeDesDatasource();

		// 目标数据集
		initializeDesDataset();

		// 其他
		this.isNewDataset = false;
		this.isRemoveSrc = false;

		if (this.desDatasource != null) {
			this.newDatasetName = this.desDatasource.getDatasets().getAvailableDatasetName(DEFAULT_DATASET_NAME);
		}
	}

	private void initializeDesDatasource() {
		this.desDatasource = null;

		if (Application.getActiveApplication().getActiveDatasources() != null) {
			for (int i = 0; i < Application.getActiveApplication().getActiveDatasources().length; i++) {
				Datasource datasource = Application.getActiveApplication().getActiveDatasources()[0];

				if (isDesDatasourceAvailable(datasource)) {
					this.desDatasource = datasource;
					break;
				}
			}
		}

		// 如果在选中的数据源中找不到合适的，就从所有数据源中取第一个满足条件的数据源
		if (this.desDatasource == null) {
			Workspace workspace = Application.getActiveApplication().getWorkspace();
			for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
				Datasource datasource = workspace.getDatasources().get(i);

				if (isDesDatasourceAvailable(datasource)) {
					this.desDatasource = datasource;
					break;
				}
			}
		}
	}

	private void initializeDesDataset() {
		this.desDataset = null;

		if (this.desDatasource != null) {
			for (int i = 0; i < this.desDatasource.getDatasets().getCount(); i++) {
				Dataset dataset = this.desDatasource.getDatasets().get(i);

				if (dataset.getType() == this.datasetType || dataset.getType() == DatasetType.CAD) {
					this.desDataset = (DatasetVector) dataset;
					break;
				}
			}
		}
	}

	private void initializeComponents(String title) {
		setTitle(title);
		this.labelDesDatasource = new JLabel(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.labelDesDataset = new JLabel(ControlsProperties.getString("String_Label_TargetDataset"));
		this.comboBoxDatasource = new DatasourceComboBox(new Datasource[0]);
		this.comboBoxDataset = new DatasetComboBox(new Dataset[0]);
		this.checkBoxNewDataset = new JCheckBox(ControlsProperties.getString("String_Label_NewDataset"));
		this.textFieldNewDataset = new SmTextFieldLegit();
		this.checkBoxRemoveSrc = new JCheckBox(MapEditorProperties.getString("String_RemoveSrcObj"));
		this.buttonOK = new JButton(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel = new JButton(CommonProperties.getString(CommonProperties.Cancel));

		this.textFieldNewDataset.setSmTextFieldLegit(new ISmTextFieldLegit() {

			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (JDialogGeometryConvert.this.desDatasource == null) {
					return false;
				}

				return JDialogGeometryConvert.this.desDatasource.getDatasets().isAvailableDatasetName(JDialogGeometryConvert.this.newDatasetName);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});

		GroupLayout gl = new GroupLayout(getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		getContentPane().setLayout(gl);

		// @formatter:off
		gl.setHorizontalGroup(gl.createParallelGroup(Alignment.LEADING)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelDesDatasource)
								.addComponent(this.labelDesDataset)
								.addComponent(this.checkBoxNewDataset)
								.addComponent(this.checkBoxRemoveSrc))
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE,Short.MAX_VALUE)
								.addComponent(this.comboBoxDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldNewDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDesDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDesDataset)
						.addComponent(this.comboBoxDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.checkBoxNewDataset)
						.addComponent(this.textFieldNewDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.checkBoxRemoveSrc)
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(450, 200);
		setLocationRelativeTo(null);
	}

	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(this.itemListener);
		this.comboBoxDataset.addItemListener(this.itemListener);
		this.checkBoxNewDataset.addItemListener(itemListener);
		this.checkBoxRemoveSrc.addItemListener(this.itemListener);
		this.textFieldNewDataset.getDocument().addDocumentListener(this.documentListener);
		this.buttonOK.addActionListener(this.actionListener);
		this.buttonCancel.addActionListener(this.actionListener);
	}

	private void initializeComponentsValue() {
		initializeComboBoxDatasourceValue();
		initializeComboBoxDatasetValue();
		this.checkBoxNewDataset.setSelected(this.isNewDataset);
		this.checkBoxRemoveSrc.setSelected(this.isRemoveSrc);
		this.textFieldNewDataset.setText(this.newDatasetName);
		setComponentsEnabled();
	}

	private void initializeComboBoxDatasourceValue() {
		this.comboBoxDatasource.removeAllItems();
		Workspace workspace = Application.getActiveApplication().getWorkspace();

		for (int i = 0; i < workspace.getDatasources().getCount(); i++) {
			Datasource datasource = workspace.getDatasources().get(i);

			if (isDesDatasourceAvailable(datasource)) {
				DataCell cell = new DataCell();
				cell.initDatasourceType(datasource);
				this.comboBoxDatasource.addItem(cell);
			}
		}
		this.comboBoxDatasource.setSelectedDatasource(this.desDatasource);
	}

	private boolean isDesDatasourceAvailable(Datasource datasource) {
		return datasource != null && !datasource.isReadOnly();
	}

	private void initializeComboBoxDatasetValue() {
		this.comboBoxDataset.removeAllItems();

		if (this.desDatasource != null) {
			for (int i = 0; i < this.desDatasource.getDatasets().getCount(); i++) {
				Dataset dataset = this.desDatasource.getDatasets().get(i);

				if (dataset.getType() == this.datasetType) {
					DataCell cell = new DataCell();
					cell.initDatasetType(dataset);
					this.comboBoxDataset.addItem(cell);
				}
			}

			for (int i = 0; i < this.desDatasource.getDatasets().getCount(); i++) {
				Dataset dataset = this.desDatasource.getDatasets().get(i);

				if (dataset.getType() == DatasetType.CAD) {
					DataCell cell = new DataCell();
					cell.initDatasetType(dataset);
					this.comboBoxDataset.addItem(cell);
				}
			}
		}
		this.comboBoxDataset.setSelectedDataset(this.desDataset);

		// 如果目标数据集是 null，说明数据源下没有合适数据集，那就置为新建
		this.isNewDataset = this.desDataset == null;
	}

	private void comboBoxDesDatasourceSelectedChange() {
		try {
			this.desDatasource = this.comboBoxDatasource.getSelectedDatasource();

			// 重新初始化数据集组合框
			initializeDesDataset();
			this.comboBoxDataset.removeItemListener(this.itemListener);
			initializeComboBoxDatasetValue();

			// 重新初始化文本框
			this.textFieldNewDataset.getDocument().removeDocumentListener(this.documentListener);
			this.textFieldNewDataset.setText(this.desDatasource.getDatasets().getAvailableDatasetName(this.newDatasetName));

			setComponentsEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.comboBoxDataset.addItemListener(this.itemListener);
			this.textFieldNewDataset.getDocument().addDocumentListener(this.documentListener);
		}
	}

	private void comboBoxDesDatasetSelectedChange() {
		this.desDataset = (DatasetVector) this.comboBoxDataset.getSelectedDataset();
	}

	private void textFieldNewDatasetTextChange() {
		if (this.textFieldNewDataset.isLegitValue(this.textFieldNewDataset.getText())) {
			this.newDatasetName = this.textFieldNewDataset.getText();
		}
		setComponentsEnabled();
	}

	private void checkBoxNewDatasetCheckedChange() {
		this.isNewDataset = this.checkBoxNewDataset.isSelected();
		setComponentsEnabled();
	}

	private void checkBoxRemoveSrcCheckedChange() {
		this.isRemoveSrc = this.checkBoxRemoveSrc.isSelected();
	}

	private void setComponentsEnabled() {
		this.textFieldNewDataset.setEnabled(this.desDatasource != null && this.isNewDataset);
		this.comboBoxDataset.setEnabled(this.desDatasource != null && !this.isNewDataset);

		if (this.isNewDataset) {
			this.buttonOK.setEnabled(this.desDatasource != null && this.textFieldNewDataset.isLegitValue(this.newDatasetName));
		} else {
			this.buttonOK.setEnabled(this.desDataset != null);
		}
	}

	private void buttonOKClick() {
		setDialogResult(DialogResult.OK);
		setVisible(false);
	}

	private void buttonCancelClick() {
		setDialogResult(DialogResult.CANCEL);
		setVisible(false);
	}
}
