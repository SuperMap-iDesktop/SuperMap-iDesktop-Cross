package com.supermap.desktop.geometryoperation.control;

import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JDialogRegionExtractCenter extends SmDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static String DEFAULT_DATASET_NAME = "ConvertResult";

	private JLabel labelDesDatasource;
	private JLabel labelMax;
	private JLabel labelMin;
	private JLabel labelNewDataset;
	private SmTextFieldLegit textFieldNewDataset;
	private SmTextFieldLegit textFieldMax;
	private SmTextFieldLegit textFieldMin;
	private DatasourceComboBox comboBoxDatasource;
	private JCheckBox checkBoxRemoveSrc;
	private JButton buttonOK;
	private JButton buttonCancel;

	private double max = 30d;
	private double min = 0d;
	private Datasource desDatasource;
	private String newDatasetName;
	private boolean canRemoveSrc = true;
	private boolean isRemoveSrc;

	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == JDialogRegionExtractCenter.this.comboBoxDatasource) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					comboBoxDesDatasourceSelectedChange();
				}
			} else if (e.getSource() == JDialogRegionExtractCenter.this.checkBoxRemoveSrc) {
				checkBoxRemoveSrcCheckedChange();
			}
		}
	};

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == JDialogRegionExtractCenter.this.buttonOK) {
				buttonOKClick();
			} else if (e.getSource() == JDialogRegionExtractCenter.this.buttonCancel) {
				buttonCancelClick();
			}
		}
	};

	private DocumentListener textFieldNewDatasetListener = new DocumentListener() {

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
			textFieldNewDatasetTextChange();
		}
	};

	private DocumentListener textFieldMaxListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldMaxTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldMaxTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldMaxTextChange();
		}
	};

	private DocumentListener textFieldMinListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldMinTextChange();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldMinTextChange();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			textFieldMinTextChange();
		}
	};

	private ISmTextFieldLegit smMaxTextFieldLegit = new ISmTextFieldLegit() {

		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			boolean result = false;

			if (StringUtilities.isNumber(textFieldValue)) {
				Double value = Double.valueOf(textFieldValue);
				return value > JDialogRegionExtractCenter.this.min && value > 0d && value <= Double.MAX_VALUE;
			}
			return result;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	private ISmTextFieldLegit smMinTextFieldLegit = new ISmTextFieldLegit() {

		@Override
		public boolean isTextFieldValueLegit(String textFieldValue) {
			boolean result = false;

			if (StringUtilities.isNumber(textFieldValue)) {
				Double value = Double.valueOf(textFieldValue);
				return value >= 0d && value < JDialogRegionExtractCenter.this.max;
			}
			return result;
		}

		@Override
		public String getLegitValue(String currentValue, String backUpValue) {
			return backUpValue;
		}
	};

	public JDialogRegionExtractCenter(boolean canRemoveSrc) {
		this.canRemoveSrc = canRemoveSrc;
		initializeDatas();
		initializeComponents();
		initializeComponentsValue();
		registerEvents();
	}

	public Datasource getDesDatasource() {
		return this.desDatasource;
	}

	public String getNewDatasetName() {
		return this.newDatasetName;
	}

	public boolean isRemoveSrc() {
		return this.canRemoveSrc ? this.isRemoveSrc : false;
	}

	public double getMax() {
		return this.max;
	}

	public double getMin() {
		return this.min;
	}

	private void initializeDatas() {

		// 目标数据源
		initializeDesDatasource();

		// 其他
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

	private boolean isDesDatasourceAvailable(Datasource datasource) {
		return datasource != null && !datasource.isReadOnly();
	}

	private void initializeComponents() {
		setTitle(MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenter"));
		this.labelDesDatasource = new JLabel(ControlsProperties.getString("String_Label_TargetDatasource"));
		this.labelMax = new JLabel(ControlsProperties.getString(ControlsProperties.Label_Max));
		this.labelMin = new JLabel(ControlsProperties.getString(ControlsProperties.Label_Min));
		this.labelNewDataset = new JLabel(ControlsProperties.getString("String_Label_TargetDataset"));
		this.textFieldMax = ComponentFactory.createNumericTextField(30, this.smMaxTextFieldLegit);
		this.textFieldMin = ComponentFactory.createNumericTextField(0, this.smMinTextFieldLegit);
		this.comboBoxDatasource = new DatasourceComboBox();
		this.textFieldNewDataset = new SmTextFieldLegit();
		this.checkBoxRemoveSrc = new JCheckBox(MapEditorProperties.getString("String_RemoveSrcObj"));
		this.checkBoxRemoveSrc.setEnabled(this.canRemoveSrc);
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();

		this.textFieldNewDataset.setSmTextFieldLegit(new ISmTextFieldLegit() {

			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (JDialogRegionExtractCenter.this.desDatasource == null) {
					return false;
				}

				return JDialogRegionExtractCenter.this.desDatasource.getDatasets().isAvailableDatasetName(textFieldValue);
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
								.addComponent(this.labelNewDataset)
								.addComponent(this.labelMax)
								.addComponent(this.labelMin)
								.addComponent(this.checkBoxRemoveSrc))
						.addGroup(gl.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldNewDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldMax, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldMin, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(gl.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDesDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelNewDataset)
						.addComponent(this.textFieldNewDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMax)
						.addComponent(this.textFieldMax, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelMin)
						.addComponent(this.textFieldMin, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.checkBoxRemoveSrc)
				.addGap(10, 10, Short.MAX_VALUE)
				.addGroup(gl.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(450, 220);
		setLocationRelativeTo(null);
	}

	private void initializeComponentsValue() {
		initializeComboBoxDatasourceValue();
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
				this.comboBoxDatasource.addItem(datasource);
			}
		}
		this.comboBoxDatasource.setSelectedDatasource(this.desDatasource);
	}

	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(this.itemListener);
		this.checkBoxRemoveSrc.addItemListener(this.itemListener);
		this.buttonOK.addActionListener(this.actionListener);
		this.buttonCancel.addActionListener(this.actionListener);
		this.textFieldNewDataset.getDocument().addDocumentListener(this.textFieldNewDatasetListener);
		this.textFieldMax.getDocument().addDocumentListener(this.textFieldMaxListener);
		this.textFieldMin.getDocument().addDocumentListener(this.textFieldMinListener);
	}

	private void comboBoxDesDatasourceSelectedChange() {
		try {
			this.desDatasource = this.comboBoxDatasource.getSelectedDatasource();

			// 重新初始化文本框
			this.textFieldNewDataset.getDocument().removeDocumentListener(this.textFieldNewDatasetListener);
			this.textFieldNewDataset.setText(this.desDatasource.getDatasets().getAvailableDatasetName(this.newDatasetName));

			setComponentsEnabled();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.textFieldNewDataset.getDocument().addDocumentListener(this.textFieldNewDatasetListener);
		}
	}

	private void textFieldMaxTextChange() {
		if (this.textFieldMax.isLegitValue(this.textFieldMax.getText())) {
			this.max = Double.valueOf(this.textFieldMax.getText());
		} else if (!StringUtilities.isNullOrEmpty(this.textFieldMax.getText())) {
			Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenterMaxError"));
		}
	}

	private void textFieldMinTextChange() {
		if (this.textFieldMin.isLegitValue(this.textFieldMin.getText())) {
			this.min = Double.valueOf(this.textFieldMin.getText());
		} else if (!StringUtilities.isNullOrEmpty(this.textFieldMin.getText())) {
			Application.getActiveApplication().getOutput().output(MapEditorProperties.getString("String_GeometryOperation_RegionExtractCenterMinError"));
		}
	}

	private void textFieldNewDatasetTextChange() {
		if (this.textFieldNewDataset.isLegitValue(this.textFieldNewDataset.getText())) {
			this.newDatasetName = this.textFieldNewDataset.getText();
		}
		setComponentsEnabled();
	}

	private void checkBoxRemoveSrcCheckedChange() {
		this.isRemoveSrc = this.canRemoveSrc ? this.checkBoxRemoveSrc.isSelected() : false;
	}

	private void setComponentsEnabled() {
		this.textFieldNewDataset.setEnabled(this.desDatasource != null);

		// @formatter:off
		this.buttonOK.setEnabled(this.desDatasource != null 
				&& this.textFieldNewDataset.isLegitValue(this.textFieldNewDataset.getText())
				&& this.textFieldMax.isLegitValue(this.textFieldMax.getText())
				&& this.textFieldMin.isLegitValue(this.textFieldMin.getText()));
		// @formatter:on
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
