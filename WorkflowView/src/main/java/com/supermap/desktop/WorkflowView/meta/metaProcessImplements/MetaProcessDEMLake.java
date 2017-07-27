package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.TerrainBuilder;
import com.supermap.analyst.spatialanalyst.TerrainBuilderParameter;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameters.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Created by Chen on 2017/6/22 0022.
 */
public class MetaProcessDEMLake extends MetaProcess {
    private final static String DEM_DATA = "DEMData";
    private final static String LAKE_DATA = "LakeData";
    private final static String OUTPUT_DATA = "OutputData";

    private ParameterDatasourceConstrained DEMDatasource;
    private ParameterSingleDataset DEMDataset;
    private ParameterDatasourceConstrained lakeDatasource;
    private ParameterSingleDataset lakeDataset;
    private ParameterRadioButton fieldOrValue;
    private ParameterFieldComboBox heightFieldComboBox;
    private ParameterNumber heightValue;



    public MetaProcessDEMLake() {
        initParameters();
        initParameterConstraint();
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint DEMEqualDatasourceConstraint = new EqualDatasourceConstraint();
        DEMEqualDatasourceConstraint.constrained(DEMDatasource,ParameterDatasource.DATASOURCE_FIELD_NAME);
        DEMEqualDatasourceConstraint.constrained(DEMDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        EqualDatasourceConstraint lakeEqualDatasourceConstraint = new EqualDatasourceConstraint();
        lakeEqualDatasourceConstraint.constrained(lakeDatasource,ParameterDatasource.DATASOURCE_FIELD_NAME);
        lakeEqualDatasourceConstraint.constrained(lakeDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
        equalDatasetConstraint.constrained(lakeDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
        equalDatasetConstraint.constrained(heightFieldComboBox,ParameterFieldComboBox.DATASET_FIELD_NAME);
    }

    private void initParameters() {
        /*Parameters*/
        DEMDatasource = new ParameterDatasourceConstrained();
        DEMDatasource.setDescribe(CommonProperties.getString("String_DEMDatasource"));
        DEMDataset=new ParameterSingleDataset(DatasetType.GRID);
        DEMDataset.setDescribe(CommonProperties.getString("String_DEMDataset"));
        DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
        if (datasetGrid != null) {
            DEMDatasource.setSelectedItem(datasetGrid.getDatasource());
            DEMDataset.setSelectedItem(datasetGrid);
        }

        lakeDatasource = new ParameterDatasourceConstrained();
        lakeDatasource.setDescribe(CommonProperties.getString("String_LakeDatasource"));
        lakeDataset=new ParameterSingleDataset(DatasetType.REGION);
        lakeDataset.setDescribe(CommonProperties.getString("String_LakeDataset"));
        Dataset datasetRegion = DatasetUtilities.getDefaultDataset(DatasetType.REGION);
        if (datasetRegion != null) {
            lakeDatasource.setSelectedItem(datasetRegion.getDatasource());
            lakeDataset.setSelectedItem(datasetRegion);
        }

        FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
        heightFieldComboBox = new ParameterFieldComboBox(ProcessProperties.getString("String_Label_HeightField"));
        heightFieldComboBox.setFieldType(fieldType);
        heightFieldComboBox.setDataset((DatasetVector) lakeDataset.getSelectedItem());

        heightValue = new ParameterNumber(ProcessProperties.getString("String_BuildLake_Elevation"));
        heightValue.setSelectedItem(-9999);

        heightValue.setEnabled(false);

        fieldOrValue = new ParameterRadioButton();
        ParameterDataNode field = new ParameterDataNode(ProcessProperties.getString("String_Field_Rely"),null);
        ParameterDataNode value = new ParameterDataNode(ProcessProperties.getString("String_Value_Rely"), null);
        fieldOrValue.setItems(new ParameterDataNode[]{field, value});
        fieldOrValue.setSelectedItem(field);

        /*GroupBox*/
        ParameterCombine DEMDataCombine = new ParameterCombine();
        DEMDataCombine.setDescribe(CommonProperties.getString("String_GroupBox_DEMData"));
        DEMDataCombine.addParameters(DEMDatasource, DEMDataset);

        ParameterCombine lakeDataCombine = new ParameterCombine();
        lakeDataCombine.setDescribe(CommonProperties.getString("String_GroupBox_LakeData"));
        lakeDataCombine.addParameters(lakeDatasource, lakeDataset);
        ParameterCombine parameterSetting = new ParameterCombine();
        parameterSetting.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
        parameterSetting.addParameters(fieldOrValue, heightFieldComboBox, heightValue);
        this.parameters.setParameters(DEMDataCombine,lakeDataCombine,parameterSetting);
        this.parameters.addInputParameters(DEM_DATA, DatasetTypes.GRID,DEMDataCombine);
        this.parameters.addInputParameters(LAKE_DATA, DatasetTypes.REGION,lakeDataCombine);
        this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID,DEMDataCombine);


        fieldOrValue.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ParameterRadioButton.RADIO_BUTTON_VALUE)) {
                    heightFieldComboBox.setEnabled(fieldOrValue.getSelectedItem() == fieldOrValue.getItemAt(0));
                    heightValue.setEnabled(fieldOrValue.getSelectedItem() == fieldOrValue.getItemAt(1));
                }
            }
        });

    }

    @Override
    public boolean execute() {
        boolean isSuccessful = false;

        try {
            fireRunning(new RunningEvent(this, 0, "start"));

            TerrainBuilderParameter terrainBuilderParameter = new TerrainBuilderParameter();
            terrainBuilderParameter.setLakeDataset((DatasetVector) lakeDataset.getSelectedItem());
            terrainBuilderParameter.setLakeAltitudeFiled(heightFieldComboBox.getSelectedItem().toString());

            TerrainBuilder.addSteppedListener(this.steppedListener);

            DatasetGrid src = null;
            if (this.getParameters().getInputs().getData(DEM_DATA).getValue() != null) {
                src = (DatasetGrid) this.getParameters().getInputs().getData(DEM_DATA).getValue();
            } else {
                src = (DatasetGrid) DEMDataset.getSelectedItem();
            }

            ParameterDataNode node = (ParameterDataNode) fieldOrValue.getSelectedItem();
            if (fieldOrValue.getItemIndex(node) == 0) {
                TerrainBuilder.buildLake(src, (DatasetVector) lakeDataset.getSelectedItem(), heightFieldComboBox.getSelectedItem().toString());
            } else if (fieldOrValue.getItemIndex(node) == 1) {
                TerrainBuilder.buildLake(src, (DatasetVector) lakeDataset.getSelectedItem(), Double.valueOf(heightValue.getSelectedItem().toString()));
            }
            this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(DEMDataset.getSelectedItem());
            isSuccessful = true;

            fireRunning(new RunningEvent(this, 100, "finished"));
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        } finally {
            TerrainBuilder.removeSteppedListener(this.steppedListener);
        }
        return isSuccessful;
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public String getKey() {
        return MetaKeys.DEMLAKE;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_DEMLake");
    }
}
