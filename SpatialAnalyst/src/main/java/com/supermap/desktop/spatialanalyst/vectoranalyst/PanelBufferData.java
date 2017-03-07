package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

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
//		this.comboBoxBufferDataDatasource.setPreferredSize(ControlDefaultValues.BUFFERCOMPONT_PREFERREDSIZE);
//		this.comboBoxBufferDataDatasource.setMaximumSize(ControlDefaultValues.BUFFERCOMPONT_PREFERREDSIZE);
		this.comboBoxBufferDataDataset = new DatasetComboBox();
//		this.comboBoxBufferDataDataset.setPreferredSize(ControlDefaultValues.BUFFERCOMPONT_PREFERREDSIZE);
//		this.comboBoxBufferDataDataset.setMaximumSize(ControlDefaultValues.BUFFERCOMPONT_PREFERREDSIZE);
		this.checkBoxGeometrySelect = new JCheckBox("GeometrySelect");

	}

	private void initResources() {
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		this.checkBoxGeometrySelect.setText(SpatialAnalystProperties.getString("String_CheckeBox_BufferSelectedRecordset"));
	}

	private void setPanelBufferDataLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_SourceBufferData")));

		GroupLayout panelBufferDataLayout = new GroupLayout(this);
		panelBufferDataLayout.setAutoCreateContainerGaps(true);
		panelBufferDataLayout.setAutoCreateGaps(true);
		this.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createParallelGroup()
				.addGroup(panelBufferDataLayout.createSequentialGroup()
					.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset))
					.addGroup(panelBufferDataLayout.createParallelGroup()
						.addComponent(this.comboBoxBufferDataDatasource)
						.addComponent(this.comboBoxBufferDataDataset)))
				.addGroup(panelBufferDataLayout.createSequentialGroup()
						.addComponent(this.checkBoxGeometrySelect)));

		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createSequentialGroup()
					.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxBufferDataDatasource,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
					.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDataset)
						.addComponent(this.comboBoxBufferDataDataset,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)))
				.addGroup(panelBufferDataLayout.createSequentialGroup()
						.addComponent(this.checkBoxGeometrySelect))
				.addGap(5,5,Short.MAX_VALUE));

		//@formatter:on
	}

	/**
	 * 创建面板是否可用方法
	 * 2017.3.2 yuanR
	 *
	 * @param isEnable
	 */
	public void setPanelEnable(boolean isEnable) {
		this.comboBoxBufferDataDataset.setEnabled(isEnable);
	}
}
