package com.supermap.desktop.CtrlAction;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.assistant.AssistantProperties;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * 新建配准
 *
 * @author XiaJT
 */
public class JDialogNewTransformationForm extends SmDialog {

	private JPanel panelTransformationLayer = new JPanel();
	private JLabel labelTransformationDatasource = new JLabel();
	private DatasourceComboBox comboBoxTransformationDatasource;
	private JLabel labelTransformationDataset = new JLabel();
	private DatasetComboBox comboBoxTransformationDataset = new DatasetComboBox();

	private JPanel panelReferenceLayer = new JPanel();
	private JLabel labelReferenceDatasource = new JLabel();
	private DatasourceComboBox comboBoxReferenceDatasource;
	private JLabel labelReferenceDataset = new JLabel();
	private DatasetComboBox comboBoxReferenceDataset = new DatasetComboBox();

	private CompTitledPane panelResultDatasetMain;
	private JPanel panelResultDataset = new JPanel();
	private JCheckBox checkBoxSaveAsDataset = new JCheckBox();
	private JLabel labelResultDatasource = new JLabel();
	private DatasourceComboBox comboBoxResultDatasource;
	private JLabel labelResultDataset = new JLabel();
	private SmTextFieldLegit textFieldResultDatasetName = new SmTextFieldLegit();

	private JPanel panelButton = new JPanel();
	private SmButton smButtonOK = new SmButton();
	private SmButton smButtonCancel = new SmButton();


	public JDialogNewTransformationForm() {
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResources();
		initListeners();
		initComponentState();
	}

	private void initComponents() {
		textFieldResultDatasetName.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				return comboBoxResultDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(textFieldValue).equals(textFieldValue);
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		ArrayList<Datasource> datasourcesList = new ArrayList<>();
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			Datasource datasource = datasources.get(i);
			if (datasource.getDatasets().getCount() > 0 && !datasource.isReadOnly()) {
				datasourcesList.add(datasource);
			}
		}
		Datasource[] datasourcesArray = datasourcesList.toArray(new Datasource[datasourcesList.size()]);
		comboBoxTransformationDatasource = new DatasourceComboBox(datasourcesArray);
		comboBoxReferenceDatasource = new DatasourceComboBox(datasourcesArray);
		comboBoxResultDatasource = new DatasourceComboBox(datasourcesArray);

		panelResultDatasetMain = new CompTitledPane(checkBoxSaveAsDataset, panelResultDataset);
		panelTransformationLayer.setBorder(new TitledBorder(AssistantProperties.getString("String_Transfernation_TargetLayer")));
		panelReferenceLayer.setBorder(new TitledBorder(AssistantProperties.getString("String_Transfernation_ReferLayer")));
		this.setSize(new Dimension(300, 450));
		this.setLocationRelativeTo(null);
	}

	private void initLayout() {
		panelTransformationLayer.setLayout(new GridBagLayout());
		panelTransformationLayer.add(labelTransformationDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelTransformationLayer.add(comboBoxTransformationDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));
		panelTransformationLayer.add(labelTransformationDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 10, 10, 0));
		panelTransformationLayer.add(comboBoxTransformationDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));

		panelReferenceLayer.setLayout(new GridBagLayout());
		panelReferenceLayer.add(labelReferenceDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelReferenceLayer.add(comboBoxReferenceDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));
		panelReferenceLayer.add(labelReferenceDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 10, 10, 0));
		panelReferenceLayer.add(comboBoxReferenceDataset, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));

		panelResultDataset.setLayout(new GridBagLayout());
		panelResultDataset.add(labelResultDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 0, 0));
		panelResultDataset.add(comboBoxResultDatasource, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10));
		panelResultDataset.add(labelResultDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(5, 10, 10, 0));
		panelResultDataset.add(textFieldResultDatasetName, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 10, 10));

		panelButton.setLayout(new GridBagLayout());
		panelButton.add(smButtonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(1, 0).setInsets(0, 0, 0, 0));
		panelButton.add(smButtonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(0, 5, 0, 0));

		this.setLayout(new GridBagLayout());
		this.add(panelTransformationLayer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(10, 10, 0, 10));
		this.add(panelReferenceLayer, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(5, 10, 0, 10));
		this.add(panelResultDatasetMain, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(5, 10, 0, 10));
		this.add(panelButton, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setInsets(5, 10, 10, 10));
	}

	private void initResources() {
		this.setTitle(AssistantProperties.getString("String_TransfernationPreSetTitle"));
		labelTransformationDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		labelReferenceDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		labelResultDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		labelTransformationDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		labelReferenceDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		labelResultDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		checkBoxSaveAsDataset.setText(AssistantProperties.getString("String_Transfernation_Resave"));
		smButtonOK.setText(CommonProperties.getString(CommonProperties.OK));
		smButtonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initListeners() {
		comboBoxTransformationDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && comboBoxTransformationDatasource.getSelectedItem() != null) {
					comboBoxTransformationDataset.setDatasets(comboBoxTransformationDatasource.getSelectedDatasource().getDatasets());
				}
			}
		});
		comboBoxReferenceDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && comboBoxReferenceDatasource.getSelectedItem() != null) {
					comboBoxReferenceDataset.setDatasets(comboBoxReferenceDatasource.getSelectedDatasource().getDatasets());
					comboBoxReferenceDataset.addItemAt(0, null);
					comboBoxReferenceDataset.setSelectedIndex(0);
				}
			}
		});

		comboBoxResultDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && comboBoxResultDatasource.getSelectedItem() != null) {
					textFieldResultDatasetName.setText(getUniqueDatasetName(textFieldResultDatasetName.getText()));
				}
			}
		});

		smButtonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.OK;
				dispose();
			}
		});

		smButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});

		checkBoxSaveAsDataset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = checkBoxSaveAsDataset.isSelected();
				comboBoxResultDatasource.setEnabled(selected);
				textFieldResultDatasetName.setEditable(selected);
			}
		});
	}

	private void initComponentState() {
		comboBoxTransformationDatasource.setSelectedIndex(-1);
		if (comboBoxTransformationDatasource.getItemCount() > 0) {
			comboBoxTransformationDatasource.setSelectedIndex(0);
		}

		comboBoxReferenceDatasource.setSelectedIndex(-1);
		if (comboBoxReferenceDatasource.getItemCount() > 0) {
			comboBoxReferenceDatasource.setSelectedIndex(0);
		}
		checkBoxSaveAsDataset.setSelected(true);
		textFieldResultDatasetName.setText(getUniqueDatasetName("Result_adjust"));
	}

	private String getUniqueDatasetName(String datasetName) {
		Datasource selectedDatasource = comboBoxResultDatasource.getSelectedDatasource();
		return selectedDatasource.getDatasets().getAvailableDatasetName(datasetName);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public Dataset getTransformationDataset() {
		return comboBoxTransformationDataset.getSelectedDataset();
	}

	public Dataset getReferenceDataset() {
		return comboBoxReferenceDataset.getSelectedDataset();
	}

	public String getResultDatasetName() {
		if (checkBoxSaveAsDataset.isSelected()) {
			return textFieldResultDatasetName.getText();
		}
		return null;
	}

	public Datasource getResultDatasource() {
		if (checkBoxSaveAsDataset.isSelected()) {
			return comboBoxResultDatasource.getSelectedDatasource();
		}
		return null;
	}
}
