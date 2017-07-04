package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
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
 * Created by Chen on 2017/6/22 0022.
 */
public class MetaProcessDEMBuild extends MetaProcess{
    private ParameterDatasourceConstrained sourceDatasources;
    private ParameterSingleDataset sourceDataset;
    private ParameterFieldComboBox comboBoxSourceField;

    //region参数设置
    // 基本设置
    private ParameterComboBox comboBoxInterpolateType;//插值类型
    private ParameterComboBox comboBoxTerrainStatisticType;//重复点处理
    private ParameterTextField textFieldResampleTolerance;//重采样距离
    private ParameterTextField textFieldZFactor;//高程缩放系数
    private ParameterCheckBox checkBox;//平坦区域

    //结果数据
    private ParameterSaveDataset resultDataset;//结果数据源+数据集
    private ParameterComboBox comboBoxEncodeType;//编码类型
    private ParameterComboBox comboBoxPixelFormat;//像素格式
    private ParameterTextField textFieldCellSize;//分辨率
    private ParameterTextField textFieldRowCount;//行数
    private ParameterTextField textFieldColumnCount;//列数
    private ParameterTextField textFieldSizeOf;//估计大小（MB）
    // endregion

    //region其他设置
    //湖数据
    private ParameterDatasourceConstrained lakeDatasource;
    private ParameterSingleDataset lakeDataset;
    private ParameterFieldComboBox comboBoxLakeField;
    //范围数据
    private ParameterDatasourceConstrained clipDatasource;
    private ParameterSingleDataset clipDataset;
    private ParameterDatasourceConstrained eraseDatasource;
    private ParameterSingleDataset eraseDataset;
    //endregion

    private static final String INPUT_DATA = "SourceData";
    private static final String LAKE_DATA = "LakeData";
    private static final String CLIP_DATA = "ClipData";
    private static final String ERASE_DATA = "EraseData";
    private static final String OUTPUT_DATA = "ResultData";

    /*重采样距离需要源数据集为线类型且插值方法为TIN，因此设置两个开关*/
    private boolean isDatasetLine = false;//判定源数据集是否为线类型
    private boolean isInterpolateTypeTIN = true;//判定插值方法是否为TIN

    public MetaProcessDEMBuild() {
        initParameters();
        initParameterConstraint();
        initParametersState();
        registerListener();
    }

    private void initParameters() {
        sourceDatasources = new ParameterDatasourceConstrained();
        sourceDatasources.setDescribe(CommonProperties.getString("String_SourceDatasource"));
        sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE);
        sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));
        comboBoxSourceField = new ParameterFieldComboBox(ProcessProperties.getString("String_BuildLake_LakeField"));
        ParameterCombine sourceData = new ParameterCombine();
        sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
        sourceData.addParameters(sourceDatasources, sourceDataset, comboBoxSourceField);

        comboBoxInterpolateType = new ParameterComboBox().setDescribe(CommonProperties.getString("String_InterpolateType"));
        comboBoxTerrainStatisticType = new ParameterComboBox().setDescribe(CommonProperties.getString("String_Label_TerrainStatisticType"));
        textFieldResampleTolerance = new ParameterTextField(ProcessProperties.getString("String_Resample_Tolerance"));
        textFieldZFactor = new ParameterTextField(ProcessProperties.getString("String_Label_ZFactor"));
        checkBox = new ParameterCheckBox(ProcessProperties.getString("String_ProcessFlatArea"));
        ParameterCombine baseSetting = new ParameterCombine();
        baseSetting.setDescribe(ProcessProperties.getString("String_GroupBox_ParameterSetting_Base"));
        baseSetting.addParameters(comboBoxInterpolateType, comboBoxTerrainStatisticType, textFieldResampleTolerance, textFieldZFactor, checkBox);

        resultDataset = new ParameterSaveDataset();
        this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
        this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
        ParameterCombine resultData = new ParameterCombine();
        resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
        resultData.addParameters(resultDataset);

        comboBoxEncodeType = new ParameterComboBox().setDescribe(ProcessProperties.getString("label_encodingType"));
        comboBoxPixelFormat = new ParameterComboBox().setDescribe(CommonProperties.getString("String_PixelType"));
        textFieldCellSize = new ParameterTextField(ProcessProperties.getString("String_Resolution"));
        textFieldRowCount = new ParameterTextField(CommonProperties.getString("String_Row"));
        textFieldColumnCount = new ParameterTextField(CommonProperties.getString("String_Column"));
        textFieldSizeOf = new ParameterTextField(ProcessProperties.getString("String_Label_SizeOf"));
        ParameterCombine resultSetting = new ParameterCombine();
        resultSetting.setDescribe(CommonProperties.getString("String_GroupBox_ResultSetting"));
        resultSetting.addParameters(comboBoxEncodeType, comboBoxPixelFormat, textFieldCellSize, textFieldRowCount, textFieldColumnCount, textFieldSizeOf);

        lakeDatasource = new ParameterDatasourceConstrained();
        lakeDatasource.setDescribe(CommonProperties.getString("String_LakeDatasource"));
        lakeDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
        lakeDataset.setDescribe(CommonProperties.getString("String_LakeDataset"));
        comboBoxLakeField = new ParameterFieldComboBox(ProcessProperties.getString("String_BuildLake_LakeField"));
        ParameterCombine lakeData = new ParameterCombine();
        lakeData.setDescribe(CommonProperties.getString("String_GroupBox_LakeData"));
        lakeData.addParameters(lakeDatasource, lakeDataset, comboBoxLakeField);

        clipDatasource = new ParameterDatasourceConstrained();
        clipDatasource.setDescribe(ProcessProperties.getString("String_Label_ClipDatasource"));
        clipDataset=new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
        clipDataset.setDescribe(ProcessProperties.getString("String_Label_ClipDataset"));
        ParameterCombine clipData = new ParameterCombine();
        clipData.setDescribe(ProcessProperties.getString("String_GroupBox_ClipData"));
        clipData.addParameters(clipDatasource, clipDataset);

        eraseDatasource = new ParameterDatasourceConstrained();
        eraseDatasource.setDescribe(ProcessProperties.getString("String_Label_EraseDatasource"));
        eraseDataset=new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
        eraseDataset.setDescribe(ProcessProperties.getString("String_Label_EraseDataset"));
        ParameterCombine eraseData = new ParameterCombine();
        eraseData.setDescribe(ProcessProperties.getString("String_GroupBox_EraseData"));
        eraseData.addParameters(eraseDatasource, eraseDataset);

        this.parameters.setParameters(sourceData, baseSetting, resultData, resultSetting, lakeData, clipData, eraseData);
        this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR,sourceData);
        this.parameters.addInputParameters(LAKE_DATA, DatasetTypes.VECTOR,lakeData);
        this.parameters.addInputParameters(CLIP_DATA, DatasetTypes.VECTOR,clipData);
        this.parameters.addInputParameters(ERASE_DATA, DatasetTypes.VECTOR,eraseData);
        this.parameters.addInputParameters(OUTPUT_DATA, DatasetTypes.GRID,resultData);
    }

    private void initParametersState() {
        FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};

        DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
        if (datasetVector != null) {
            sourceDatasources.setSelectedItem(datasetVector.getDatasource());
            sourceDataset.setSelectedItem(datasetVector);
        }
        comboBoxSourceField.setFieldType(fieldType);

        comboBoxInterpolateType.setItems(new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_TIN"), TerrainInterpolateType.TIN),
                new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_Kriging"), TerrainInterpolateType.KRIGING),
                new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_IDW"), TerrainInterpolateType.IDW)
        );
        comboBoxTerrainStatisticType.setItems(new ParameterDataNode(ProcessProperties.getString("String_TerrainStatisticType_Unique"), TerrainStatisticType.UNIQUE),
                new ParameterDataNode(ProcessProperties.getString("String_TerrainStatisticType_Majority"), TerrainStatisticType.MAJORITY),
                new ParameterDataNode(CommonProperties.getString("String_StatisticsType_MAX"), TerrainStatisticType.MAX),
                new ParameterDataNode(CommonProperties.getString("String_StatisticsType_MEAN"), TerrainStatisticType.MEAN),
                new ParameterDataNode(CommonProperties.getString("String_StatisticsType_MEDIAN"), TerrainStatisticType.MEDIAN),
                new ParameterDataNode(CommonProperties.getString("String_StatisticsType_MIN"), TerrainStatisticType.MIN)
        );
        textFieldResampleTolerance.setEnabled(false);
        textFieldResampleTolerance.setSelectedItem("0");
        textFieldZFactor.setSelectedItem("1");

        resultDataset.setSelectedItem("DatasetDEM");

        comboBoxEncodeType.setItems(new ParameterDataNode(CommonProperties.getString("String_EncodeType_None"), EncodeType.NONE),
                new ParameterDataNode(CommonProperties.getString("String_EncodeType_SGL"), EncodeType.SGL),
                new ParameterDataNode(CommonProperties.getString("String_EncodeType_LZW"), EncodeType.LZW)
        );
        comboBoxPixelFormat.setItems(new ParameterDataNode(CommonProperties.getString("String_PixelSingle"), PixelFormat.SINGLE),
                new ParameterDataNode(CommonProperties.getString("String_PixelDouble"), PixelFormat.DOUBLE),
                new ParameterDataNode(CommonProperties.getString("String_PixelBit8"), PixelFormat.BIT8),
                new ParameterDataNode(CommonProperties.getString("String_PixelBit16"), PixelFormat.BIT16),
                new ParameterDataNode(CommonProperties.getString("String_PixelBit32"), PixelFormat.BIT32),
                new ParameterDataNode(CommonProperties.getString("String_PixelBit64"), PixelFormat.BIT64),
                new ParameterDataNode(CommonProperties.getString("String_PixelUBit1"), PixelFormat.UBIT1),
                new ParameterDataNode(CommonProperties.getString("String_PixelUBit4"), PixelFormat.UBIT4),
                new ParameterDataNode(CommonProperties.getString("String_PixelUBit8"), PixelFormat.UBIT8),
                new ParameterDataNode(CommonProperties.getString("String_PixelUBit16"), PixelFormat.UBIT16),
                new ParameterDataNode(CommonProperties.getString("String_PixelUBit32"), PixelFormat.UBIT32)
        );
        textFieldRowCount.setEnabled(false);
        textFieldColumnCount.setEnabled(false);
        textFieldSizeOf.setEnabled(false);
        textFieldCellSize.setSelectedItem("10");
        textFieldRowCount.setSelectedItem("0");
        textFieldColumnCount.setSelectedItem("0");
        textFieldSizeOf.setSelectedItem("0");

        DatasetVector datasetRegion = DatasetUtilities.getDefaultDatasetVector();
        if (datasetRegion != null) {
            lakeDatasource.setSelectedItem(datasetRegion.getDatasource());
            lakeDataset.setDatasource(datasetRegion.getDatasource());
        }
        comboBoxLakeField.setFieldType(fieldType);

        DatasetVector datasetRegion1 = DatasetUtilities.getDefaultDatasetVector();
        if (datasetRegion1 != null) {
            clipDatasource.setSelectedItem(datasetRegion1.getDatasource());
            clipDataset.setDatasource(datasetRegion1.getDatasource());
        }

        DatasetVector datasetRegion2 = DatasetUtilities.getDefaultDatasetVector();
        if (datasetRegion2 != null) {
            eraseDatasource.setSelectedItem(datasetRegion2.getDatasource());
            eraseDataset.setDatasource(datasetRegion2.getDatasource());
        }
    }

    private void initParameterConstraint() {
        EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
        constraintSource.constrained(sourceDatasources,ParameterDatasource.DATASOURCE_FIELD_NAME);
        constraintSource.constrained(sourceDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        EqualDatasetConstraint constraintSourceDataset = new EqualDatasetConstraint();
        constraintSourceDataset.constrained(sourceDataset,ParameterSingleDataset.DATASET_FIELD_NAME);
        constraintSourceDataset.constrained(comboBoxSourceField,ParameterFieldComboBox.DATASET_FIELD_NAME);

        DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

        EqualDatasourceConstraint constraintLake = new EqualDatasourceConstraint();
        constraintLake.constrained(lakeDatasource,ParameterDatasource.DATASOURCE_FIELD_NAME);
        constraintLake.constrained(lakeDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        EqualDatasetConstraint constraintLakeDataset = new EqualDatasetConstraint();
        constraintLakeDataset.constrained(lakeDataset,ParameterSingleDataset.DATASET_FIELD_NAME);
        constraintLakeDataset.constrained(comboBoxLakeField,ParameterFieldComboBox.DATASET_FIELD_NAME);

        EqualDatasourceConstraint constraintClip = new EqualDatasourceConstraint();
        constraintClip.constrained(clipDatasource,ParameterDatasource.DATASOURCE_FIELD_NAME);
        constraintClip.constrained(clipDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);

        EqualDatasourceConstraint constraintErase = new EqualDatasourceConstraint();
        constraintErase.constrained(eraseDatasource,ParameterDatasource.DATASOURCE_FIELD_NAME);
        constraintErase.constrained(eraseDataset,ParameterSingleDataset.DATASOURCE_FIELD_NAME);
    }

    private void registerListener() {
        sourceDataset.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector) {
                    Rectangle2D bounds = ((DatasetVector) evt.getNewValue()).getBounds();
                    double cellSize = Math.sqrt(Math.pow(bounds.getHeight(), 2) + Math.pow(bounds.getWidth(), 2)) / 500;
                    textFieldCellSize.setSelectedItem(cellSize);
                    textFieldRowCount.setSelectedItem((int) (bounds.getHeight() / cellSize));
                    textFieldColumnCount.setSelectedItem((int) (bounds.getWidth() / cellSize));
                }

                int pixelFormatByte = 0;
                if (comboBoxPixelFormat.getSelectedData() == PixelFormat.UBIT1) {
                    pixelFormatByte = 1;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.UBIT4) {
                    pixelFormatByte = 4;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.UBIT8 || comboBoxPixelFormat.getSelectedData() == PixelFormat.BIT8) {
                    pixelFormatByte = 8;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.UBIT16 || comboBoxPixelFormat.getSelectedData() == PixelFormat.BIT16) {
                    pixelFormatByte = 16;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.UBIT32 || comboBoxPixelFormat.getSelectedData() == PixelFormat.BIT32) {
                    pixelFormatByte = 32;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.BIT64) {
                    pixelFormatByte = 64;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.SINGLE) {
                    pixelFormatByte = 32;
                } else if (comboBoxPixelFormat.getSelectedData() == PixelFormat.DOUBLE) {
                    pixelFormatByte = 64;
                }
                int row = Integer.valueOf(textFieldRowCount.getSelectedItem().toString());
                int column = Integer.valueOf(textFieldColumnCount.getSelectedItem().toString());
                double sizeOf = (row * column * pixelFormatByte) / (8 * 1024 * 1024) + 0.000005;
                sizeOf = Math.round(sizeOf * 100000)/100000;
                textFieldSizeOf.setSelectedItem(sizeOf);

                isDatasetLine = ((DatasetVector)evt.getNewValue()).getType() == DatasetType.LINE;
                textFieldResampleTolerance.setEnabled(isDatasetLine&&isInterpolateTypeTIN);
            }
        });
        comboBoxInterpolateType.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                isInterpolateTypeTIN = ((ParameterDataNode) evt.getNewValue()).getData() == TerrainInterpolateType.TIN;
                textFieldResampleTolerance.setEnabled(isDatasetLine && isInterpolateTypeTIN);
                eraseDatasource.setEnabled(isInterpolateTypeTIN);
                eraseDataset.setEnabled(isInterpolateTypeTIN);
            }
        });
    }

    @Override
    public boolean execute() {
        boolean isSuccessful = false;

        try {
            fireRunning(new RunningEvent(this, 0, "start"));

            TerrainBuilderParameter terrainBuilderParameter = new TerrainBuilderParameter();
            if (clipDataset.getSelectedItem() != null) {
                terrainBuilderParameter.setClipDataset((DatasetVector) clipDataset.getSelectedItem());
            }
            if (eraseDataset.getSelectedItem() != null && eraseDataset.isEnabled()) {
                terrainBuilderParameter.setEraseDataset((DatasetVector) eraseDataset.getSelectedItem());
            }
            if (lakeDataset.getSelectedItem() != null) {
                terrainBuilderParameter.setLakeDataset((DatasetVector) lakeDataset.getSelectedItem());
                terrainBuilderParameter.setLakeAltitudeFiled(comboBoxLakeField.getSelectedItem().toString());
            }
            if (sourceDataset.getSelectedItem() != null) {
                DatasetVector datasetVector = (DatasetVector) sourceDataset.getSelectedItem();
                if (datasetVector.getType()==DatasetType.POINT) {
                    terrainBuilderParameter.setPointDatasets(new DatasetVector[]{datasetVector});
                    terrainBuilderParameter.setPointAltitudeFileds(new String[]{comboBoxSourceField.getSelectedItem().toString()});
                } else if (datasetVector.getType() == DatasetType.LINE) {
                    terrainBuilderParameter.setLineDatasets(new DatasetVector[]{datasetVector});
                    terrainBuilderParameter.setLineAltitudeFileds(new String[]{comboBoxSourceField.getSelectedItem().toString()});
                }
            }
            terrainBuilderParameter.setEncodeType((EncodeType) comboBoxEncodeType.getSelectedData());
            terrainBuilderParameter.setInterpolateType((TerrainInterpolateType) comboBoxInterpolateType.getSelectedData());
            terrainBuilderParameter.setPixelFormat((PixelFormat) comboBoxPixelFormat.getSelectedData());
            terrainBuilderParameter.setProcessFlatArea(Boolean.parseBoolean(checkBox.getSelectedItem().toString()));
            terrainBuilderParameter.setStatisticType((TerrainStatisticType)comboBoxTerrainStatisticType.getSelectedData());
            terrainBuilderParameter.setZFactor(Double.valueOf(textFieldZFactor.getSelectedItem().toString()));
            terrainBuilderParameter.setResampleLen(Double.valueOf(textFieldResampleTolerance.getSelectedItem().toString()));
            terrainBuilderParameter.setCellSize(Double.valueOf(textFieldCellSize.getSelectedItem().toString()));

            String datasetName = resultDataset.getDatasetName();
            datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

            DatasetGrid result = TerrainBuilder.buildTerrain(terrainBuilderParameter, resultDataset.getResultDatasource(), datasetName);
            this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
            isSuccessful = result != null;

            TerrainBuilder.addSteppedListener(steppedListener);

            fireRunning(new RunningEvent(this, 100, "finished"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
        } finally{
            TerrainBuilder.removeSteppedListener(steppedListener);
        }

        return isSuccessful;
    }

    @Override
    public IParameters getParameters() {
        return parameters;
    }

    @Override
    public String getKey() {
        return MetaKeys.DEMBUILD;
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_DEMBuild");
    }
}
