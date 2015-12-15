package com.supermap.desktop.spatialanalyst.vectoranalyst;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.ui.MapControl;

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
		this.comboBoxBufferDataDataset = new DatasetComboBox();
		this.checkBoxGeometrySelect = new JCheckBox("GeometrySelect");
		this.panelBufferDataBasic = new JPanel();

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
		this.panelBufferDataBasic.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset)).addGap(35)
				.addGroup(panelBufferDataLayout.createParallelGroup()
						.addComponent(this.comboBoxBufferDataDatasource)
						.addComponent(this.comboBoxBufferDataDataset)).addGap(5));
		
		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxBufferDataDatasource)).addGap(10)
				.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDataset)
						.addComponent(this.comboBoxBufferDataDataset)).addContainerGap()
			   );
		//@formatter:on

		this.add(this.checkBoxGeometrySelect, BorderLayout.SOUTH);

	}
}
