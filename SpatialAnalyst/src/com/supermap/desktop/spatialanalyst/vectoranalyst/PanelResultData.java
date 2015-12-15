package com.supermap.desktop.spatialanalyst.vectoranalyst;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxDatasource;

public class PanelResultData extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxResultDataDatasource;

	
	public DatasourceComboBox getComboBoxResultDataDatasource() {
		return comboBoxResultDataDatasource;
	}

	public void setComboBoxResultDataDatasource(DatasourceComboBox comboBoxResultDataDatasource) {
		this.comboBoxResultDataDatasource = comboBoxResultDataDatasource;
	}

	private JTextField textFieldResultDataDataset;

	public JTextField getTextFieldResultDataDataset() {
		return textFieldResultDataDataset;
	}

	public void setTextFieldResultDataDataset(JTextField textFieldResultDataDataset) {
		this.textFieldResultDataDataset = textFieldResultDataDataset;
	}

	public PanelResultData() {
		initComponent();
		initResources();
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
	}

	private void setPanelResultDataLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_ResultData")));
		GroupLayout panelResultDataLayout = new GroupLayout(this);
		this.setLayout(panelResultDataLayout);

		//@formatter:off
		panelResultDataLayout.setHorizontalGroup(panelResultDataLayout.createSequentialGroup()
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.labelDataset)).addGap(40)
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.comboBoxResultDataDatasource)
						.addComponent(this.textFieldResultDataDataset)).addGap(5));
		
		panelResultDataLayout.setVerticalGroup(panelResultDataLayout.createSequentialGroup()
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxResultDataDatasource)).addGap(15)
				.addGroup(panelResultDataLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.labelDataset)
						.addComponent(this.textFieldResultDataDataset)).addContainerGap());
		//@formatter:on
	}

}
