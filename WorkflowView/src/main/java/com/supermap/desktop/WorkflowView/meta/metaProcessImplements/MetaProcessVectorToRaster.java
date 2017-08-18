package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.ConversionAnalystParameter;
import com.supermap.data.*;
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
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.PixelFormatUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/7/10.
 */
public class MetaProcessVectorToRaster extends MetaProcess {

	private final static String SOURCE_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String BOUNDARY_DATA = CommonProperties.getString("String_BoundaryData");
	private final static String OUTPUT_DATA = "VectorToRasterResult";

	//  输入数据
	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterCombine sourceData;
	//  边界数据
	private ParameterDatasourceConstrained boundaryDatasource;
	private ParameterSingleDataset boundaryDataset;
	private ParameterCombine boundaryData;
	// 参数设置
	private ParameterFieldComboBox comboBoxValueField;
	private ParameterComboBox comboBoxPixelFormat;
	private ParameterNumber textCellSize;
	private ParameterCombine parameterSetting;
	// 结果数据
	private ParameterSaveDataset resultDataset;
	private ParameterCombine resultData;
	ParameterDataNode parameterDataNodeSingle = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.SINGLE), PixelFormat.SINGLE);
	ParameterDataNode parameterDataNodeDouble = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.DOUBLE), PixelFormat.DOUBLE);
	ParameterDataNode parameterDataNodeBit8 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT8), PixelFormat.BIT8);
	ParameterDataNode parameterDataNodeBit16 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT16), PixelFormat.BIT16);
	ParameterDataNode parameterDataNodeBit32 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT32), PixelFormat.BIT32);
	ParameterDataNode parameterDataNodeBit64 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT64), PixelFormat.BIT64);
	ParameterDataNode parameterDataNodeUbit1 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT1), PixelFormat.UBIT1);
	ParameterDataNode parameterDataNodeUbit4 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT4), PixelFormat.UBIT4);
	ParameterDataNode parameterDataNodeUbit8 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT8), PixelFormat.UBIT8);
	ParameterDataNode parameterDataNodeUbit16 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT16), PixelFormat.UBIT16);
	ParameterDataNode parameterDataNodeUbit32 = new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT32), PixelFormat.UBIT32);

	public MetaProcessVectorToRaster() {
		initParameters();
		initParametersState();
		initParameterConstrint();
		registerListener();
	}

	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		this.sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

		this.boundaryDatasource = new ParameterDatasourceConstrained();
		this.boundaryDatasource.setDescribe(CommonProperties.getString("String_BoundaryDatasource"));
		this.boundaryDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		this.boundaryDataset.setDescribe(CommonProperties.getString("String_BoundaryDataset"));

		this.resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));

		this.comboBoxValueField = new ParameterFieldComboBox(CommonProperties.getString("String_m_labelGridValueFieldText"));
		this.boundaryDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		this.boundaryDataset.setDescribe(CommonProperties.getString("String_BoundaryDataset"));
		this.comboBoxPixelFormat = new ParameterComboBox(CommonProperties.getString("String_PixelType"));
		this.textCellSize = new ParameterNumber(CommonProperties.getString("String_Resolution"));

		this.sourceData = new ParameterCombine();
		this.sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		this.sourceData.addParameters(this.sourceDatasource, this.sourceDataset);

		this.boundaryData = new ParameterCombine();
		this.boundaryData.setDescribe(CommonProperties.getString("String_BoundaryData"));
		this.boundaryData.addParameters(this.boundaryDatasource, this.boundaryDataset);

		this.parameterSetting = new ParameterCombine();
		this.parameterSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		this.parameterSetting.addParameters(this.comboBoxValueField, this.comboBoxPixelFormat, this.textCellSize);

		this.resultData = new ParameterCombine();
		this.resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		this.resultData.addParameters(this.resultDataset);

		this.parameters.setParameters(this.sourceData, this.boundaryData, this.parameterSetting, this.resultData);
//		this.parameters.addInputParameters(SOURCE_DATA, DatasetTypes.POINT, sourceData);
//		this.parameters.addInputParameters(SOURCE_DATA, DatasetTypes.LINE, sourceData);
		this.parameters.addInputParameters(SOURCE_DATA, DatasetTypes.SIMPLE_VECTOR, sourceData);
		this.parameters.addInputParameters(BOUNDARY_DATA, DatasetTypes.REGION, boundaryData);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				ProcessOutputResultProperties.getString("String_VectorToGridResult"),
				DatasetTypes.GRID, resultData);

	}

	private void initParametersState() {
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		this.comboBoxValueField.setFieldType(fieldType);
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			this.sourceDatasource.setSelectedItem(datasetVector.getDatasource());
			this.sourceDataset.setSelectedItem(datasetVector);
			this.boundaryDatasource.setSelectedItem(datasetVector.getDatasource());
			this.boundaryDataset.setDatasource(datasetVector.getDatasource());
			Rectangle2D bounds = datasetVector.getBounds();
			double maxEdge = bounds.getHeight();
			if (bounds.getWidth() > bounds.getHeight()) {
				maxEdge = bounds.getWidth();
			}
			double cellSize = maxEdge / 500;
			this.textCellSize.setSelectedItem(cellSize);
			this.comboBoxValueField.setFieldName(datasetVector);
			this.comboBoxValueField.setSelectedItem("SmUserID");
		}

		this.comboBoxPixelFormat.setItems(parameterDataNodeSingle, parameterDataNodeDouble, parameterDataNodeBit8,
				parameterDataNodeBit16, parameterDataNodeBit32, parameterDataNodeBit64, parameterDataNodeUbit1,
				parameterDataNodeUbit4, parameterDataNodeUbit8, parameterDataNodeUbit16, parameterDataNodeUbit32);

		this.textCellSize.setMinValue(0);
		this.textCellSize.setIsIncludeMin(false);
		this.comboBoxPixelFormat.setSelectedItem(parameterDataNodeBit32);
		this.resultDataset.setSelectedItem("result_vectorToGrid");
		this.comboBoxValueField.setRequisite(true);
		this.textCellSize.setRequisite(true);
		this.comboBoxPixelFormat.setRequisite(true);
	}

	private void initParameterConstrint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(this.sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(this.sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(this.resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint constraintSourceDataset = new EqualDatasetConstraint();
		constraintSourceDataset.constrained(this.sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		constraintSourceDataset.constrained(this.comboBoxValueField, ParameterFieldComboBox.DATASET_FIELD_NAME);

		EqualDatasourceConstraint constraintClip = new EqualDatasourceConstraint();
		constraintClip.constrained(this.boundaryDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintClip.constrained(this.boundaryDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		this.sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (sourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector) {
					Rectangle2D bounds = ((DatasetVector) evt.getNewValue()).getBounds();
					double maxEdge = bounds.getHeight();
					if (bounds.getWidth() > bounds.getHeight()) {
						maxEdge = bounds.getWidth();
					}
					double cellSize = maxEdge / 500;
					textCellSize.setSelectedItem(cellSize);
					comboBoxValueField.setSelectedItem("SmUserID");
				}
			}
		});

		this.comboBoxValueField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				if (comboBoxValueField.getSelectedItem() != null && evt.getNewValue() instanceof FieldInfo) {
					FieldInfo fieldInfo = (FieldInfo) evt.getNewValue();
					if (fieldInfo.getType() == FieldType.INT16) {
						comboBoxPixelFormat.setSelectedItem(parameterDataNodeBit16);
					} else if (fieldInfo.getType() == FieldType.INT32) {
						comboBoxPixelFormat.setSelectedItem(parameterDataNodeBit32);
					} else if (fieldInfo.getType() == FieldType.INT64) {
						comboBoxPixelFormat.setSelectedItem(parameterDataNodeBit64);
					} else if (fieldInfo.getType() == FieldType.DOUBLE) {
						comboBoxPixelFormat.setSelectedItem(parameterDataNodeDouble);
					} else if (fieldInfo.getType() == FieldType.SINGLE) {
						comboBoxPixelFormat.setSelectedItem(parameterDataNodeSingle);
					}
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {
			fireRunning(new RunningEvent(this, 0, "start"));

			ConversionAnalystParameter conversionParameter = new ConversionAnalystParameter();
			String datasetName = this.resultDataset.getDatasetName();
			datasetName = this.resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			if (parameters.getInputs().getData(SOURCE_DATA).getValue() != null) {
				conversionParameter.setSourceDataset((Dataset) parameters.getInputs().getData(SOURCE_DATA).getValue());
			} else {
				conversionParameter.setSourceDataset(this.sourceDataset.getSelectedDataset());
			}

			conversionParameter.setTargetDatasource(this.resultDataset.getResultDatasource());
			conversionParameter.setTargetDatasetName(datasetName);
			conversionParameter.setPixelFormat((PixelFormat) this.comboBoxPixelFormat.getSelectedData());
			conversionParameter.setValueFieldName((String) this.comboBoxValueField.getSelectedItem());
			conversionParameter.setCellSize(Double.valueOf(this.textCellSize.getSelectedItem().toString()));

			DatasetVector srcBoundary = null;
			if (parameters.getInputs().getData(BOUNDARY_DATA).getValue() != null) {
				srcBoundary = (DatasetVector) parameters.getInputs().getData(BOUNDARY_DATA).getValue();
			} else {
				srcBoundary = (DatasetVector) this.boundaryDataset.getSelectedDataset();
			}
			if (srcBoundary != null) {
				Recordset recordset = srcBoundary.getRecordset(false, CursorType.DYNAMIC);
				GeoRegion geoRegion = null;
				recordset.moveFirst();
				while (!recordset.isEOF()) {
					GeoRegion tempGeoregion = (GeoRegion) recordset.getGeometry().clone();
					if (geoRegion == null) {
						geoRegion = tempGeoregion.clone();
					} else {
						for (int i = 0; i < tempGeoregion.getPartCount(); i++) {
							geoRegion.addPart(tempGeoregion.getPart(i));
						}
					}
					recordset.moveNext();
				}
				conversionParameter.setClipRegion(geoRegion);
			}

			ConversionAnalyst.addSteppedListener(steppedListener);
			DatasetGrid resultDatasetGrid = ConversionAnalyst.vectorToRaster(conversionParameter);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(resultDatasetGrid);
			isSuccessful = resultDatasetGrid != null;

			fireRunning(new RunningEvent(MetaProcessVectorToRaster.this, 100, "finished"));

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
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
		return MetaKeys.VECTORTOGRID;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Form_VectorToGrid");
	}
}
