package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.ConversionAnalystParameter;
import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Chen on 2017/6/30 0030.
 */
public class MetaProcessRasterToVector extends MetaProcess {
    private final static String INPUT_DATA = "InputData";
    private final static String OUTPUT_DATA = "ExtractResult";

    private ParameterDatasourceConstrained sourceDatasource;
    private ParameterSingleDataset sourceDataset;
    private ParameterCombine sourceData;

    private ParameterSaveDataset resultDataset;
    private ParameterComboBox comboBoxType;
    private ParameterCombine resultData;

    private ParameterComboBox comboBoxSmoothMethod;
    private ParameterTextField textFieldSmoothDegree;
    private ParameterCheckBox checkBoxThinRaster;
    private ParameterCombine vertorizeLineSetting;

    private ParameterTextField textFieldNoValue;
    private ParameterTextField textFieldNoValueTolerance;
    private ParameterTextField textFieldGridField;
    private ParameterCheckBox checkBoxChooseSpecifiedValue;
    private ParameterTextField textFieldGridValue;
    private ParameterTextField textFieldGridValueTolerance;
    private ParameterCombine gridDatasetSetting;

    private ParameterComboBox comboBoxBackColor;
    private ParameterTextField textFieldColorTolerance;
    private ParameterCombine imageDatasetSetting;

    public MetaProcessRasterToVector() {
        initParameters();
        initParameterConstraint();
        initParametersState();
        registerListener();
    }

    private void initParameters() {
        sourceDatasource=new ParameterDatasourceConstrained();
        sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
        sourceDataset = new ParameterSingleDataset(DatasetType.GRID, DatasetType.IMAGE);
        sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

        resultDataset = new ParameterSaveDataset();
        this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
        this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
        comboBoxType = new ParameterComboBox(ProcessProperties.getString("string_label_lblDatasetType"));

        comboBoxSmoothMethod = new ParameterComboBox(CommonProperties.getString("String_SmoothMethod"));
        textFieldSmoothDegree = new ParameterTextField(CommonProperties.getString("String_Smooth"));
        checkBoxThinRaster = new ParameterCheckBox(CommonProperties.getString("String_CheckBox_IsThinRaster"));

        textFieldNoValue = new ParameterTextField(CommonProperties.getString("String_Label_NoData"));
        textFieldNoValueTolerance = new ParameterTextField(CommonProperties.getString("String_Label_NoValueTolerance"));
        textFieldGridField = new ParameterTextField(CommonProperties.getString("String_m_labelGridValueFieldText"));
        checkBoxChooseSpecifiedValue = new ParameterCheckBox(CommonProperties.getString("String_CheckBox_ChooseSpecifiedValue"));
        textFieldGridValue = new ParameterTextField(CommonProperties.getString("String_Label_GridValue"));
        textFieldGridValueTolerance = new ParameterTextField(CommonProperties.getString("String_Label_GridValueTolerance"));

        comboBoxBackColor = new ParameterComboBox(CommonProperties.getString("String_Label_BackColor"));
        textFieldColorTolerance = new ParameterTextField(CommonProperties.getString("String_Label_BackColoTolerance"));


        sourceData = new ParameterCombine();
        sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
        sourceData.addParameters(sourceDatasource, sourceDataset);

        resultData = new ParameterCombine();
        resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
        resultData.addParameters(resultDataset,comboBoxType);

        vertorizeLineSetting = new ParameterCombine();
        vertorizeLineSetting.setDescribe(CommonProperties.getString("String_GroupBox_VertorizeLineSetting"));
        vertorizeLineSetting.addParameters(comboBoxSmoothMethod, textFieldSmoothDegree,checkBoxThinRaster);

        gridDatasetSetting = new ParameterCombine();
        gridDatasetSetting.setDescribe(CommonProperties.getString("String_GroupBox_GridDatasetSetting"));
        gridDatasetSetting.addParameters(textFieldNoValue, textFieldNoValueTolerance, textFieldGridField, checkBoxChooseSpecifiedValue, textFieldGridValue, textFieldGridValueTolerance);

        imageDatasetSetting = new ParameterCombine();
        imageDatasetSetting.setDescribe(CommonProperties.getString("String_GroupBox_ImageDatasetSetting"));
        imageDatasetSetting.addParameters(comboBoxSmoothMethod, textFieldSmoothDegree,checkBoxThinRaster);

        this.parameters.setParameters(sourceData, resultData, vertorizeLineSetting, gridDatasetSetting, imageDatasetSetting);
        this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
        this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.IMAGE, sourceData);
        this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, resultData);
    }

    private void initParametersState() {
        DatasetGrid datasetGrid = DatasetUtilities.getDefaultDatasetGrid();
        if (datasetGrid != null) {
            sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
            sourceDataset.setDatasource(datasetGrid.getDatasource());
        }

        resultDataset.setSelectedItem("result_vectorize");
        comboBoxType.setItems(new ParameterDataNode(CommonProperties.getString("String_Item_Point"), DatasetType.POINT),
                new ParameterDataNode(CommonProperties.getString("String_Item_Line"), DatasetType.LINE),
                new ParameterDataNode(CommonProperties.getString("String_Item_Region"), DatasetType.REGION));

        comboBoxSmoothMethod.setItems(new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE),
                new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
                new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
        textFieldSmoothDegree.setSelectedItem("2");
        checkBoxThinRaster.setSelectedItem(true);
        vertorizeLineSetting.setEnabled(comboBoxType.getSelectedData()==DatasetType.LINE);

        textFieldNoValue.setSelectedItem("-9999");
        textFieldNoValueTolerance.setSelectedItem("0");
        textFieldGridField.setSelectedItem("value");
        textFieldGridValue.setSelectedItem("0");
        textFieldGridValueTolerance.setSelectedItem("0");
        gridDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetGrid);

        comboBoxBackColor.setItems();//TODO
        textFieldColorTolerance.setSelectedItem("0");
        imageDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetImage);
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
        equalDatasourceConstraint.constrained(sourceDatasource,ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
        equalDatasourceConstraint.constrained(sourceDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
    }

    private void registerListener() {
        sourceDataset.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                gridDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetGrid);
                imageDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetImage);
            }
        });
        comboBoxType.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                vertorizeLineSetting.setEnabled(comboBoxType.getSelectedData()==DatasetType.LINE);
            }
        });
        comboBoxSmoothMethod.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                textFieldSmoothDegree.setEnabled(comboBoxSmoothMethod.getSelectedData() != SmoothMethod.NONE);
            }
        });
        checkBoxChooseSpecifiedValue.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                textFieldGridValue.setEnabled((boolean)checkBoxChooseSpecifiedValue.getSelectedItem());
                textFieldGridValueTolerance.setEnabled((boolean)checkBoxChooseSpecifiedValue.getSelectedItem());
            }
        });
    }

    @Override
    public boolean execute() {
        boolean isSuccessful = false;

        try {
            ConversionAnalyst.addSteppedListener(steppedListener);
            fireRunning(new RunningEvent(MetaProcessRasterToVector.this, 0, "start"));

            ConversionAnalystParameter analystParameter = new ConversionAnalystParameter();

            String datasetName = resultDataset.getDatasetName();
            datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
            analystParameter.setTargetDatasource(resultDataset.getResultDatasource());
            analystParameter.setTargetDatasetName(datasetName);
            analystParameter.setTargetDatasetType((DatasetType) comboBoxType.getSelectedData());
            if (vertorizeLineSetting.isEnabled()) {
                analystParameter.setSmoothMethod((SmoothMethod) comboBoxSmoothMethod.getSelectedData());
                analystParameter.setThinRaster((boolean) checkBoxThinRaster.getSelectedItem());
                if (textFieldSmoothDegree.isEnabled()) {
                    analystParameter.setSmoothDegree((int) textFieldSmoothDegree.getSelectedItem());
                }
            }
            if (imageDatasetSetting.isEnabled()) {
                analystParameter.setBackOrNoValue((long) comboBoxBackColor.getSelectedData());
                analystParameter.setBackOrNoValueTolerance(Double.valueOf(textFieldColorTolerance.getSelectedItem().toString()));
            }
            if (gridDatasetSetting.isEnabled()) {
                analystParameter.setBackOrNoValue((long) textFieldNoValue.getSelectedItem());
                analystParameter.setBackOrNoValueTolerance(Double.valueOf(textFieldNoValueTolerance.getSelectedItem().toString()));
                analystParameter.setValueFieldName(textFieldGridField.getSelectedItem().toString());
                if (textFieldGridValue.isEnabled()) {
                    analystParameter.setSpecifiedValue((long) textFieldGridValue.getSelectedItem());
                    analystParameter.setSpecifiedValueTolerance(Double.valueOf(textFieldGridValueTolerance.getSelectedItem().toString()));
                }
            }

            Dataset result = ConversionAnalyst.rasterToVector(analystParameter);
            this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
            isSuccessful = result != null;
            fireRunning(new RunningEvent(MetaProcessRasterToVector.this, 100, "finished"));
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
        } finally {
            ConversionAnalyst.removeSteppedListener(steppedListener);
        }

        return isSuccessful;
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public String getKey() {
        return MetaKeys.GRIDTOVECTOR;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Form_GridToVector");
    }
}
