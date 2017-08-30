package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.TerrainBuilder;
import com.supermap.analyst.spatialanalyst.TerrainBuilderParameter;
import com.supermap.analyst.spatialanalyst.TerrainInterpolateType;
import com.supermap.analyst.spatialanalyst.TerrainStatisticType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.EncodeType;
import com.supermap.data.PixelFormat;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.ParameterCheckBox;
import com.supermap.desktop.process.parameter.ipls.ParameterCombine;
import com.supermap.desktop.process.parameter.ipls.ParameterComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasource;
import com.supermap.desktop.process.parameter.ipls.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.ipls.ParameterNumber;
import com.supermap.desktop.process.parameter.ipls.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.ipls.ParameterTextField;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.EncodeTypeUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;
import com.supermap.desktop.utilities.TerrainInterpolateTypeUtilities;
import com.supermap.desktop.utilities.TerrainStatisticTypeUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Chen on 2017/6/22 0022.
 */
public class MetaProcessDEMBuild extends MetaProcess {
	private ParameterDatasourceConstrained sourceDatasources;
	private ParameterSingleDataset sourceDataset;

	//region参数设置
	// 基本设置
	private ParameterComboBox comboBoxInterpolateType;//插值类型
	private ParameterFieldComboBox comboBoxSourceField;
	private ParameterComboBox comboBoxTerrainStatisticType;//重复点处理
	private ParameterNumber textNumResampleTolerance;//重采样距离
	private ParameterNumber textNumZFactor;//高程缩放系数
	private ParameterFieldComboBox comboBoxLakeField;
	private ParameterCheckBox checkBox;//平坦区域

	//结果数据
	private ParameterSaveDataset resultDataset;//结果数据源+数据集
	private ParameterComboBox comboBoxEncodeType;//编码类型
	private ParameterComboBox comboBoxPixelFormat;//像素格式
	private ParameterNumber textFieldCellSize;//分辨率
	private ParameterTextField textFieldRowCount;//行数
	private ParameterTextField textFieldColumnCount;//列数
	private ParameterTextField textFieldSizeOf;//估计大小（MB）

	// endregion

	//region其他设置
	//湖数据
	private ParameterDatasourceConstrained lakeDatasource;
	private ParameterSingleDataset lakeDataset;
	//范围数据
	private ParameterDatasourceConstrained clipDatasource;
	private ParameterSingleDataset clipDataset;
	private ParameterDatasourceConstrained eraseDatasource;
	private ParameterSingleDataset eraseDataset;
	//endregion

	private static final String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private static final String LAKE_DATA = CommonProperties.getString("String_GroupBox_LakeData");
	private static final String CLIP_DATA = ProcessProperties.getString("String_GroupBox_ClipData");
	private static final String ERASE_DATA = ProcessProperties.getString("String_GroupBox_EraseData");
	private static final String OUTPUT_DATA = "DEMBuildResult";

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
		sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE);
		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasources, sourceDataset);

		lakeDatasource = new ParameterDatasourceConstrained();
		lakeDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		ParameterCombine lakeData = new ParameterCombine();
		lakeData.setDescribe(CommonProperties.getString("String_GroupBox_LakeData"));
		lakeData.addParameters(lakeDatasource, lakeDataset);

		clipDatasource = new ParameterDatasourceConstrained();
		clipDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		ParameterCombine clipData = new ParameterCombine();
		clipData.setDescribe(ProcessProperties.getString("String_GroupBox_ClipData"));
		clipData.addParameters(clipDatasource, clipDataset);

		eraseDatasource = new ParameterDatasourceConstrained();
		eraseDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		ParameterCombine eraseData = new ParameterCombine();
		eraseData.setDescribe(ProcessProperties.getString("String_GroupBox_EraseData"));
		eraseData.addParameters(eraseDatasource, eraseDataset);

		comboBoxSourceField = new ParameterFieldComboBox(ProcessProperties.getString("String_Label_HeightField"));
		comboBoxLakeField = new ParameterFieldComboBox(ProcessProperties.getString("String_Label_LakeHeightField"));
		comboBoxInterpolateType = new ParameterComboBox().setDescribe(CommonProperties.getString("String_InterpolateType"));
		comboBoxTerrainStatisticType = new ParameterComboBox().setDescribe(CommonProperties.getString("String_Label_TerrainStatisticType"));
		textNumResampleTolerance = new ParameterNumber(ProcessProperties.getString("String_Resample_Tolerance"));
		textNumZFactor = new ParameterNumber(ProcessProperties.getString("String_Label_ZFactor"));
		checkBox = new ParameterCheckBox(ProcessProperties.getString("String_ProcessFlatArea"));
		ParameterCombine baseSetting = new ParameterCombine();
		baseSetting.setDescribe(ProcessProperties.getString("String_GroupBox_ParameterSetting_Base"));
		baseSetting.addParameters(comboBoxSourceField, comboBoxInterpolateType, comboBoxTerrainStatisticType, textNumResampleTolerance, textNumZFactor, checkBox, comboBoxLakeField);

		comboBoxEncodeType = new ParameterComboBox().setDescribe(ProcessProperties.getString("label_encodingType"));
		comboBoxPixelFormat = new ParameterComboBox().setDescribe(CommonProperties.getString("String_PixelType"));
		textFieldCellSize = new ParameterNumber(ProcessProperties.getString("String_Resolution"));
		textFieldRowCount = new ParameterTextField(CommonProperties.getString("String_Row"));
		textFieldColumnCount = new ParameterTextField(CommonProperties.getString("String_Column"));
		textFieldSizeOf = new ParameterTextField(ProcessProperties.getString("String_Label_SizeOf"));
		ParameterCombine resultSetting = new ParameterCombine();
		resultSetting.setDescribe(CommonProperties.getString("String_GroupBox_ResultSetting"));
		resultSetting.addParameters(comboBoxEncodeType, comboBoxPixelFormat, textFieldCellSize, textFieldRowCount, textFieldColumnCount, textFieldSizeOf);

		resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(resultDataset);

		this.parameters.setParameters(sourceData, lakeData, clipData, eraseData, baseSetting, resultSetting, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceData);
		this.parameters.addInputParameters(LAKE_DATA, DatasetTypes.REGION, lakeData);
		this.parameters.addInputParameters(CLIP_DATA, DatasetTypes.REGION, clipData);
		this.parameters.addInputParameters(ERASE_DATA, DatasetTypes.REGION, eraseData);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_DEMBuildResult"),
				DatasetTypes.GRID, resultData);
	}

	private void initParametersState() {
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.LINE, DatasetType.POINT);
		if (datasetVector != null) {
			sourceDatasources.setSelectedItem(datasetVector.getDatasource());
			sourceDataset.setSelectedItem(datasetVector);
		}
		comboBoxSourceField.setFieldType(fieldType);

		comboBoxInterpolateType.setItems(new ParameterDataNode(TerrainInterpolateTypeUtilities.toString(TerrainInterpolateType.TIN), TerrainInterpolateType.TIN),
				new ParameterDataNode(TerrainInterpolateTypeUtilities.toString(TerrainInterpolateType.KRIGING), TerrainInterpolateType.KRIGING),
				new ParameterDataNode(TerrainInterpolateTypeUtilities.toString(TerrainInterpolateType.IDW), TerrainInterpolateType.IDW)
		);
		comboBoxTerrainStatisticType.setItems(new ParameterDataNode(TerrainStatisticTypeUtilities.toString(TerrainStatisticType.UNIQUE), TerrainStatisticType.UNIQUE),
				new ParameterDataNode(TerrainStatisticTypeUtilities.toString(TerrainStatisticType.MAJORITY), TerrainStatisticType.MAJORITY),
				new ParameterDataNode(TerrainStatisticTypeUtilities.toString(TerrainStatisticType.MAX), TerrainStatisticType.MAX),
				new ParameterDataNode(TerrainStatisticTypeUtilities.toString(TerrainStatisticType.MEAN), TerrainStatisticType.MEAN),
				new ParameterDataNode(TerrainStatisticTypeUtilities.toString(TerrainStatisticType.MEDIAN), TerrainStatisticType.MEDIAN),
				new ParameterDataNode(TerrainStatisticTypeUtilities.toString(TerrainStatisticType.MIN), TerrainStatisticType.MIN)
		);
		textNumResampleTolerance.setEnabled(false);
		textNumResampleTolerance.setSelectedItem(0);
		textFieldRowCount.setEnabled(false);
		textFieldColumnCount.setEnabled(false);
		textFieldSizeOf.setEnabled(false);
		textFieldCellSize.setSelectedItem(0);
		textFieldRowCount.setSelectedItem("0");
		textFieldColumnCount.setSelectedItem("0");
		textFieldSizeOf.setSelectedItem("0");
		if (sourceDataset.getSelectedDataset() != null) {
			double height = sourceDataset.getSelectedItem().getBounds().getHeight() / 10;
			double width = sourceDataset.getSelectedItem().getBounds().getWidth() / 10;
			textNumResampleTolerance.setMaxValue(height > width ? width : height);
			Rectangle2D bounds = datasetVector.getBounds();
			double cellSize = Math.sqrt(Math.pow(bounds.getHeight(), 2) + Math.pow(bounds.getWidth(), 2)) / 500;
			textFieldCellSize.setSelectedItem(cellSize);
			textFieldRowCount.setSelectedItem((int) (bounds.getHeight() / cellSize));
			textFieldColumnCount.setSelectedItem((int) (bounds.getWidth() / cellSize));
		}
		textNumResampleTolerance.setIncludeMax(false);
		textNumZFactor.setSelectedItem(1);
		textNumZFactor.setMinValue(0);
		textNumZFactor.setIsIncludeMin(false);

		resultDataset.setSelectedItem("result_DEMBuild");

		comboBoxEncodeType.setItems(new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.NONE), EncodeType.NONE),
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.SGL), EncodeType.SGL),
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.LZW), EncodeType.LZW)
		);
		comboBoxPixelFormat.setItems(new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.SINGLE), PixelFormat.SINGLE),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.DOUBLE), PixelFormat.DOUBLE),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT8), PixelFormat.BIT8),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT16), PixelFormat.BIT16),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT32), PixelFormat.BIT32),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT1), PixelFormat.UBIT1),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT4), PixelFormat.UBIT4),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT8), PixelFormat.UBIT8),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT16), PixelFormat.UBIT16),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT32), PixelFormat.UBIT32)
		);

		if (datasetVector != null) {
			lakeDatasource.setSelectedItem(datasetVector.getDatasource());
			lakeDataset.setDatasource(datasetVector.getDatasource());
		}
		comboBoxLakeField.setFieldType(fieldType);

		if (datasetVector != null) {
			clipDatasource.setSelectedItem(datasetVector.getDatasource());
			clipDataset.setDatasource(datasetVector.getDatasource());
		}

		if (datasetVector != null) {
			eraseDatasource.setSelectedItem(datasetVector.getDatasource());
			eraseDataset.setDatasource(datasetVector.getDatasource());
		}
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint constraintSource = new EqualDatasourceConstraint();
		constraintSource.constrained(sourceDatasources, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintSource.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint constraintSourceDataset = new EqualDatasetConstraint();
		constraintSourceDataset.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		constraintSourceDataset.constrained(comboBoxSourceField, ParameterFieldComboBox.DATASET_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint constraintLake = new EqualDatasourceConstraint();
		constraintLake.constrained(lakeDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintLake.constrained(lakeDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint constraintLakeDataset = new EqualDatasetConstraint();
		constraintLakeDataset.constrained(lakeDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		constraintLakeDataset.constrained(comboBoxLakeField, ParameterFieldComboBox.DATASET_FIELD_NAME);

		EqualDatasourceConstraint constraintClip = new EqualDatasourceConstraint();
		constraintClip.constrained(clipDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintClip.constrained(clipDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint constraintErase = new EqualDatasourceConstraint();
		constraintErase.constrained(eraseDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintErase.constrained(eraseDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
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
				sizeOf = Math.round(sizeOf * 100000) / 100000;
				textFieldSizeOf.setSelectedItem(sizeOf);
				if (null != evt.getNewValue() && evt.getNewValue() instanceof DatasetVector) {
					isDatasetLine = ((DatasetVector) evt.getNewValue()).getType() == DatasetType.LINE;
					textNumResampleTolerance.setEnabled(isDatasetLine && isInterpolateTypeTIN);
				}
			}
		});
		comboBoxInterpolateType.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				isInterpolateTypeTIN = ((ParameterDataNode) evt.getNewValue()).getData() == TerrainInterpolateType.TIN;
				textNumResampleTolerance.setEnabled(isDatasetLine && isInterpolateTypeTIN);
				eraseDatasource.setEnabled(isInterpolateTypeTIN);
				eraseDataset.setEnabled(isInterpolateTypeTIN);
			}
		});
		textFieldCellSize.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Rectangle2D bounds = sourceDataset.getSelectedDataset().getBounds();
				double cellSize = Double.parseDouble(textFieldCellSize.getSelectedItem().toString());
				textFieldRowCount.setSelectedItem((int) (bounds.getHeight() / cellSize));
				textFieldColumnCount.setSelectedItem((int) (bounds.getWidth() / cellSize));
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {
			fireRunning(new RunningEvent(this, 0, "start"));

			TerrainBuilderParameter terrainBuilderParameter = new TerrainBuilderParameter();

			DatasetVector src = null;
			if (parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) sourceDataset.getSelectedItem();
			}
			if (src.getType() == DatasetType.POINT) {
				terrainBuilderParameter.setPointDatasets(new DatasetVector[]{src});
				terrainBuilderParameter.setPointAltitudeFileds(new String[]{comboBoxSourceField.getSelectedItem().toString()});
			} else if (src.getType() == DatasetType.LINE) {
				terrainBuilderParameter.setLineDatasets(new DatasetVector[]{src});
				terrainBuilderParameter.setLineAltitudeFileds(new String[]{comboBoxSourceField.getSelectedItem().toString()});
			}

			DatasetVector srcLake = null;
			if (parameters.getInputs().getData(LAKE_DATA).getValue() != null) {
				srcLake = (DatasetVector) parameters.getInputs().getData(LAKE_DATA).getValue();
			} else {
				srcLake = (DatasetVector) lakeDataset.getSelectedItem();
			}
			if (srcLake != null) {
				terrainBuilderParameter.setLakeDataset(srcLake);
				terrainBuilderParameter.setLakeAltitudeFiled(comboBoxLakeField.getSelectedItem().toString());
			}

			DatasetVector srcClip = null;
			if (parameters.getInputs().getData(CLIP_DATA).getValue() != null) {
				srcClip = (DatasetVector) parameters.getInputs().getData(CLIP_DATA).getValue();
			} else {
				srcClip = (DatasetVector) clipDataset.getSelectedItem();
			}
			if (srcClip != null) {
				terrainBuilderParameter.setClipDataset(srcClip);
			}

			if (eraseDataset.isEnabled()) {
				DatasetVector srcErase = null;
				if (parameters.getInputs().getData(ERASE_DATA).getValue() != null) {
					srcErase = (DatasetVector) parameters.getInputs().getData(ERASE_DATA).getValue();
				} else {
					srcErase = (DatasetVector) eraseDataset.getSelectedItem();
				}
				if (srcErase != null) {
					terrainBuilderParameter.setEraseDataset(srcErase);
				}
			}

			terrainBuilderParameter.setEncodeType((EncodeType) comboBoxEncodeType.getSelectedData());
			terrainBuilderParameter.setInterpolateType((TerrainInterpolateType) comboBoxInterpolateType.getSelectedData());
			terrainBuilderParameter.setPixelFormat((PixelFormat) comboBoxPixelFormat.getSelectedData());
			terrainBuilderParameter.setProcessFlatArea(Boolean.parseBoolean(checkBox.getSelectedItem().toString()));
			terrainBuilderParameter.setStatisticType((TerrainStatisticType) comboBoxTerrainStatisticType.getSelectedData());
			terrainBuilderParameter.setZFactor(Double.valueOf(textNumZFactor.getSelectedItem().toString()));
			terrainBuilderParameter.setCellSize(Double.valueOf(textFieldCellSize.getSelectedItem().toString()));
			if (textNumResampleTolerance.isEnabled()) {
				terrainBuilderParameter.setResampleLen(Double.valueOf(textNumResampleTolerance.getSelectedItem().toString()));
			}

			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			DatasetGrid result = TerrainBuilder.buildTerrain(terrainBuilderParameter, resultDataset.getResultDatasource(), datasetName);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;

			TerrainBuilder.addSteppedListener(steppedListener);

			fireRunning(new RunningEvent(this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
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
