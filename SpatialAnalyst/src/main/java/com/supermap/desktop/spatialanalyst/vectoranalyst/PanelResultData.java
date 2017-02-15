package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PanelResultData extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxResultDataDatasource;
	private JTextField textFieldResultDataDataset;

	public DatasourceComboBox getComboBoxResultDataDatasource() {
		return comboBoxResultDataDatasource;
	}

	public void setComboBoxResultDataDatasource(DatasourceComboBox comboBoxResultDataDatasource) {
		this.comboBoxResultDataDatasource = comboBoxResultDataDatasource;
	}

	public JTextField getTextFieldResultDataDataset() {
		return textFieldResultDataDataset;
	}

	public void setTextFieldResultDataDataset(JTextField textFieldResultDataDataset) {
		this.textFieldResultDataDataset = textFieldResultDataDataset;
	}

	public PanelResultData() {
		initComponent();
		initResources();
		resetDatasetName();
		initComboBoxResultDataDatasource();
		setPanelResultDataLayout();

	}

	private void initResources() {
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
	}

	private void initComponent() {
		this.labelDataset = new JLabel("Dataset");
		this.labelDatasource = new JLabel("Datasource");
		this.comboBoxResultDataDatasource = new DatasourceComboBox();
		this.textFieldResultDataDataset = new JTextField("Buffer");

		comboBoxResultDataDatasource.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				resetDatasetName();
			}
		});

	}

	private void setPanelResultDataLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_ResultData")));
		GroupLayout panelResultDataLayout = new GroupLayout(this);
		panelResultDataLayout.setAutoCreateGaps(true);
		panelResultDataLayout.setAutoCreateContainerGaps(true);
		this.setLayout(panelResultDataLayout);

		//@formatter:off
		panelResultDataLayout.setHorizontalGroup(panelResultDataLayout.createSequentialGroup()
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset))
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.comboBoxResultDataDatasource)
						.addComponent(this.textFieldResultDataDataset)));
		
		panelResultDataLayout.setVerticalGroup(panelResultDataLayout.createSequentialGroup()
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxResultDataDatasource)).addGap(5)
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.textFieldResultDataDataset)));
		//@formatter:on
	}

	private void initComboBoxResultDataDatasource() {

		for (int i = this.comboBoxResultDataDatasource.getItemCount() - 1; i >= 0; i--) {
			if (this.comboBoxResultDataDatasource.getItemAt(i) instanceof Datasource && this.comboBoxResultDataDatasource.getItemAt(i).isReadOnly()) {
				this.comboBoxResultDataDatasource.removeItemAt(i);
			}
		}
	}

	private void resetDatasetName() {
		String name = "Buffer";
		if (this.comboBoxResultDataDatasource.getSelectedDatasource() != null) {
			this.textFieldResultDataDataset.setText(this.comboBoxResultDataDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(name));
		}
	}
}
