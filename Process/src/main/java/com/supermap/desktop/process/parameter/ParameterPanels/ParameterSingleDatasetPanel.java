package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Created by xie on 2017/2/16.
 */
public class ParameterSingleDatasetPanel extends JPanel {
    private ParameterSingleDataset parameterSingleDataset;
    private JLabel labelDataset;
    private DatasetComboBox datasetComboBox;
    private boolean isSelectingItem = false;
    private DatasetType[] datasetTypes;

    public ParameterSingleDatasetPanel(ParameterSingleDataset parameterSingleDataset, DatasetType[] datasetTypes) {
        this.parameterSingleDataset = parameterSingleDataset;
        this.datasetTypes = datasetTypes;
        init();
    }

    private void init() {
        initComponents();
        initLayout();
        initListener();
    }

    private void initComponents() {
        this.labelDataset = new JLabel();
        this.labelDataset.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
	    if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
		    this.datasetComboBox = new DatasetComboBox(Application.getActiveApplication().getWorkspace().getDatasources().get(0).getDatasets());
		    this.datasetComboBox.setSupportedDatasetTypes(datasetTypes);
	    }
    }

    private void initLayout() {
        labelDataset.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
        datasetComboBox.setPreferredSize(new Dimension(20, 23));
        this.setLayout(new GridBagLayout());
        this.add(labelDataset, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE));
        this.add(datasetComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
    }

    private void initListener() {
        this.datasetComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED && null != datasetComboBox.getSelectedDataset()) {
                    isSelectingItem = true;
                    parameterSingleDataset.setSelectedItem(datasetComboBox.getSelectedDataset());
                    isSelectingItem = false;
                }
            }
        });
        this.parameterSingleDataset.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!isSelectingItem && Objects.equals(evt.getPropertyName(), AbstractParameter.PROPERTY_VALE)) {
                    isSelectingItem = true;
                    if (evt.getNewValue() instanceof Dataset)
                        datasetComboBox.setSelectedDataset((Dataset) evt.getNewValue());
                    isSelectingItem = false;
                }
            }
        });
    }

}
