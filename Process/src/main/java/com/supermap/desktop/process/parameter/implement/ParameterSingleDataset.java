package com.supermap.desktop.process.parameter.implement;

import com.supermap.analyst.spatialanalyst.AggregationType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterSingleDatasetPanel;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.process.util.EnumParser;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * Created by xie on 2017/2/16.
 */
public class ParameterSingleDataset extends AbstractParameter implements ISelectionParameter {
    private Dataset selectedItem;
    private JPanel panel;
    private DatasetType[] datasetTypes;
    public ParameterSingleDataset(DatasetType ...datasetTypes){
        this.datasetTypes = datasetTypes;
        String[] enumValues = {"LEFT","TOP","BOTTOM"};
        String[] enumChName = {"左","上","下"};
        new EnumParser(DockSite.class,enumValues,enumChName).getEnumItems();
//        String[] enumValues = {"AVERRAGE","MEDIAN","SUM"};
//        String[] enumChName = {"平均值","中值","和"};
//        EnumParser parser = new EnumParser(AggregationType.class,enumValues,enumChName);
//        System.out.println(parser.getEnumItems());
    }
    @Override
    public void setSelectedItem(Object item) {
        if (item instanceof Dataset) {
            Dataset oldValue = this.selectedItem;
            this.selectedItem = (Dataset) item;
            firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, selectedItem));
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public JPanel getPanel() {
        if (null == panel) {
            panel = new ParameterSingleDatasetPanel(this,datasetTypes);
        }
        return panel;
    }

    @Override
    public void dispose() {

    }
}
