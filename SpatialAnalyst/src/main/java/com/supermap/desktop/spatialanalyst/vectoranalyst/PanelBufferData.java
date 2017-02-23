package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;

public class PanelBufferData extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private DatasetComboBox comboBoxBufferDataDataset;
	private DatasourceComboBox comboBoxBufferDataDatasource;
	private JCheckBox checkBoxGeometrySelect;
	private JPanel panelBufferDataBasic;
	private JPanel panelSelect;
//	private final static Dimension DIMENSION = new Dimension(100, 20);

	public JCheckBox getCheckBoxGeometrySelect() {
		return checkBoxGeometrySelect;
	}

	public void setCheckBoxGeometrySelect(JCheckBox checkBoxGeometrySelect) {
		this.checkBoxGeometrySelect = checkBoxGeometrySelect;
	}

	public DatasetComboBox getComboBoxBufferDataDataset() {
		return comboBoxBufferDataDataset;
	}

	public void setComboBoxBufferDataDataset(DatasetComboBox comboBoxBufferDataDataset) {
		this.comboBoxBufferDataDataset = comboBoxBufferDataDataset;
	}

	public DatasourceComboBox getComboBoxBufferDataDatasource() {
		return comboBoxBufferDataDatasource;
	}

	public void setComboBoxBufferDataDatasource(DatasourceComboBox comboBoxBufferDataDatasource) {
		this.comboBoxBufferDataDatasource = comboBoxBufferDataDatasource;
	}

	public PanelBufferData() {
		initComponent();
		initResources();
		setPanelBufferDataLayout();
	}

	private void initComponent() {
		this.labelDataset = new JLabel("Dataset");
		this.labelDatasource = new JLabel("Datasource");
		this.comboBoxBufferDataDatasource = new DatasourceComboBox();
//		this.comboBoxBufferDataDatasource.setSize(DIMENSION);
		this.comboBoxBufferDataDataset = new DatasetComboBox();
//		this.comboBoxBufferDataDataset.setSize(DIMENSION);
		this.checkBoxGeometrySelect = new JCheckBox("GeometrySelect");
		this.panelBufferDataBasic = new JPanel();
		this.panelSelect = new JPanel();

	}

	private void initResources() {
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		this.checkBoxGeometrySelect.setText(SpatialAnalystProperties.getString("String_CheckeBox_BufferSelectedRecordset"));
	}

	private void setPanelBufferDataLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_SourceBufferData")));
		this.setLayout(new BorderLayout());

		this.add(this.panelBufferDataBasic, BorderLayout.CENTER);
		GroupLayout panelBufferDataLayout = new GroupLayout(this.panelBufferDataBasic);
		panelBufferDataLayout.setAutoCreateContainerGaps(true);
		panelBufferDataLayout.setAutoCreateGaps(true);
		this.panelBufferDataBasic.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset))
				.addGroup(panelBufferDataLayout.createParallelGroup()
						.addComponent(this.comboBoxBufferDataDatasource)
						.addComponent(this.comboBoxBufferDataDataset)));
		
		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxBufferDataDatasource,20,20,20))
				.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.comboBoxBufferDataDataset,20,20,20)));
		//@formatter:on
		
		this.add(this.checkBoxGeometrySelect, BorderLayout.SOUTH);
	}
}
