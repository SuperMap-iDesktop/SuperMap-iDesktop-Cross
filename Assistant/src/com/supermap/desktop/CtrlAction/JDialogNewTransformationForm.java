package com.supermap.desktop.CtrlAction;

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
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 新建配准
 *
 * @author XiaJT
 */
public class JDialogNewTransformationForm extends SmDialog {

	private JPanel panelTransformationLayer = new JPanel();
	private JLabel labelTransformationDatasource = new JLabel();
	private DatasourceComboBox comboBoxTransformationDatasource = new DatasourceComboBox();
	private JLabel labelTransformationDataset = new JLabel();
	private DatasetComboBox comboBoxTransformationDataset = new DatasetComboBox();

	private JPanel panelReferenceLayer = new JPanel();
	private JLabel labelReferenceDatasource = new JLabel();
	private DatasourceComboBox comboBoxReferenceDatasource = new DatasourceComboBox();
	private JLabel labelReferenceDataset = new JLabel();
	private DatasetComboBox comboBoxReferenceDataset = new DatasetComboBox();

	private CompTitledPane panelResultDatasetMain;
	private JPanel panelResultDataset = new JPanel();
	private JCheckBox checkBoxSaveAsDataset = new JCheckBox();
	private JLabel labelResultDatasource = new JLabel();
	private DatasourceComboBox comboBoxResultDatasource = new DatasourceComboBox();
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
		smButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialogResult = DialogResult.CANCEL;
				dispose();
			}
		});
	}

	private void initComponentState() {
		comboBoxTransformationDataset.setDatasets(Application.getActiveApplication().getWorkspace().getDatasources().get(0).getDatasets());
		comboBoxReferenceDataset.setDatasets(Application.getActiveApplication().getWorkspace().getDatasources().get(0).getDatasets());
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
