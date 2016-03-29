package com.supermap.desktop.controls.property.dataset;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoRegion;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JDialogSetClipRegion extends SmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxDatasource;
	private JLabel labelDataset;
	private DatasetComboBox comboBoxDataset;
	private JLabel labelFilter;
	private JTextField textFieldFilter;
	private JButton buttonFilter;
	private SmButton buttonOK;
	private SmButton buttonCancel;

	private transient DatasetVector datasetVector;
	private transient GeoRegion region;

	private transient ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxDatasource) {
					comboBoxDatasourceSelectedChange();
				} else if (e.getSource() == comboBoxDataset) {
					comboBoxDatasetSelectedChange();
				}
			}
		}
	};
	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonOK) {
				buttonOKClicked();
			} else if (e.getSource() == buttonCancel) {
				buttonCancelClicked();
			} else if (e.getSource() == buttonFilter) {
				buttonFilterClicked();
			}
		}
	};

	public JDialogSetClipRegion(JDialog owner) {
		super(owner);
		initializeComponents();
		initializeResources();
		setModal(true);
		setResizable(false);
		setSize(new Dimension(355, 160));
		registerEvents();
		this.comboBoxDataset.setDatasets(this.comboBoxDatasource.getSelectedDatasource().getDatasets());
		setComponentEnabled();
		setLocationRelativeTo(null);
	}

	public GeoRegion getRegion() {
		return this.region;
	}

	public void disposeRegion() {
		if (this.region != null) {
			this.region.dispose();
			this.region = null;
		}
	}

	private void initializeComponents() {
		this.labelDatasource = new JLabel("Datasource:");
		this.comboBoxDatasource = new DatasourceComboBox();
		this.labelDataset = new JLabel("Dataset:");
		this.comboBoxDataset = new DatasetComboBox();
		this.comboBoxDataset.setDatasetTypes(new DatasetType[] { DatasetType.REGION });
		this.labelFilter = new JLabel("Filter:");
		this.textFieldFilter = new JTextField();
		this.buttonFilter = new JButton("...");
		this.buttonOK = new SmButton("OK");
		this.buttonCancel = new SmButton("Cancel");

		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		this.getContentPane().setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelDatasource)
								.addComponent(this.labelDataset)
								.addComponent(this.labelFilter))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.comboBoxDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(this.textFieldFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
										.addComponent(this.buttonFilter))))
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap(10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.comboBoxDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelFilter)
						.addComponent(this.textFieldFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonFilter))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	private void initializeResources() {
		this.setTitle(ControlsProperties.getString("String_SetClipRegion"));
		this.labelDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
		this.labelDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
		this.labelFilter.setText(ControlsProperties.getString("String_LabelFilter"));
		this.buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(this.itemListener);
		this.comboBoxDataset.addItemListener(this.itemListener);
		this.buttonOK.addActionListener(this.actionListener);
		this.buttonCancel.addActionListener(this.actionListener);
	}

	private void comboBoxDatasourceSelectedChange() {
		this.comboBoxDataset.setDatasets(this.comboBoxDatasource.getSelectedDatasource().getDatasets());
	}

	private void comboBoxDatasetSelectedChange() {
		this.datasetVector = (DatasetVector) this.comboBoxDataset.getSelectedDataset();
		setComponentEnabled();
	}

	private void buttonOKClicked() {
		Recordset recordset = null;

		try {
			if (this.datasetVector != null) {
				String queryString = this.textFieldFilter.getText();
				recordset = this.datasetVector.query(queryString, CursorType.STATIC);

				if (recordset != null && recordset.getRecordCount() > 0) {
					recordset.moveFirst();
					region = new GeoRegion();
					while (!recordset.isEOF()) {
						GeoRegion recordsetGeometry = (GeoRegion) recordset.getGeometry();
						if (recordsetGeometry != null) {
							for (int i = 0; i < recordsetGeometry.getPartCount(); i++) {
								region.addPart(recordsetGeometry.getPart(i));
							}
						}
						recordsetGeometry.dispose();
						recordsetGeometry = null;
						recordset.moveNext();
					}
				}
			}
			dialogResult = DialogResult.OK;
			setVisible(false);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.dispose();
				recordset = null;
			}
		}
	}

	private void buttonCancelClicked() {
		dialogResult = DialogResult.CANCEL;
		setVisible(false);
	}

	private void buttonFilterClicked() {
		// TODO something
	}

	private void setComponentEnabled() {
		this.buttonOK.setEnabled(this.datasetVector != null);
	}
}
