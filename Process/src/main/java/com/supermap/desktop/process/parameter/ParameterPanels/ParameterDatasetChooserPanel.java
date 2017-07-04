package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterDatasetChooser;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.datasetChoose.DatasetChooser;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by xie on 2017/6/27.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DATASET_CHOOSER)
public class ParameterDatasetChooserPanel extends SwingPanel implements IParameterPanel {
	private ParameterDatasetChooser datasetChooser;
	private JLabel labelDatasetName;
	private JTextField textFieldDatasetName;
	private JButton buttonChooseDataset;
	private boolean isSelectingItem = false;

	public ParameterDatasetChooserPanel(IParameter datasetChooser) {
		super(datasetChooser);
		this.datasetChooser = (ParameterDatasetChooser) datasetChooser;
		init();
	}

	private void init() {
		initComponents();
		initResources();
		initLayout();
		registEvents();
	}

	private void initComponents() {
		this.labelDatasetName = new JLabel();
		this.textFieldDatasetName = new JTextField();
		this.buttonChooseDataset = new JButton();
		if (null != datasetChooser.getSelectedItem()) {
			this.textFieldDatasetName.setText(((Dataset) datasetChooser.getSelectedItem()).getName());
		}
		this.labelDatasetName.setEnabled(this.datasetChooser.isEnabled());
		this.textFieldDatasetName.setEnabled(this.datasetChooser.isEnabled());
		this.buttonChooseDataset.setEnabled(this.datasetChooser.isEnabled());
	}

	private void initResources() {
		this.labelDatasetName.setText(CommonProperties.getString("String_Dataset"));
		this.buttonChooseDataset.setText(CommonProperties.getString("String_Choose"));
	}

	private void initLayout() {
		this.labelDatasetName.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		this.textFieldDatasetName.setPreferredSize(new Dimension(20, 23));
		this.panel.setLayout(new GridBagLayout());
		this.panel.add(this.labelDatasetName, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
		this.panel.add(this.textFieldDatasetName, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0).setWeight(1, 0));
		this.panel.add(this.buttonChooseDataset, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 0));
	}

	private void registEvents() {

		this.buttonChooseDataset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isSelectingItem == false) {
					isSelectingItem = true;
					DatasetChooser tempDatasetChooser = new DatasetChooser(null) {
						@Override
						protected boolean isSupportDatasource(Datasource datasource) {
							return !DatasourceUtilities.isWebDatasource(datasource.getEngineType()) && super.isSupportDatasource(datasource);
						}
					};
					tempDatasetChooser.setSupportDatasetTypes(datasetChooser.getSupportTypes());
					tempDatasetChooser.setSelectionModel(ListSelectionModel.SINGLE_SELECTION);
					if (tempDatasetChooser.showDialog() == DialogResult.OK) {
						datasetChooser.setSelectedItem(tempDatasetChooser.getSelectedDatasets().get(0));
						textFieldDatasetName.setText(tempDatasetChooser.getSelectedDatasets().get(0).getName());
					}
					isSelectingItem = false;
				}
			}
		});
		this.textFieldDatasetName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				setDataset();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setDataset();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setDataset();
			}

			private void setDataset() {
				if (!isSelectingItem) {
					isSelectingItem = true;
					String datasetName = textFieldDatasetName.getText();
					if (!StringUtilities.isNullOrEmpty(datasetName)) {
						Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
						for (int i = 0; i < datasources.getCount(); i++) {
							Datasource tempDatasource = datasources.get(i);
							if (null != DatasourceUtilities.getDataset(datasetName, tempDatasource)) {
								datasetChooser.setSelectedItem(DatasourceUtilities.getDataset(datasetName, tempDatasource));
							}
						}
					}
					isSelectingItem = false;
				}
			}
		});
	}

	public static void main(String[] args) {
		new ParameterDatasetChooserPanel(new ParameterDatasetChooser());
	}
}
