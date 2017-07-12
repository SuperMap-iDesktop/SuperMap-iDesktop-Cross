package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.ConversionAnalystParameter;
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
import com.supermap.desktop.utilities.PixelFormatUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by lixiaoyao on 2017/7/10.
 */
public class MetaProcessVectorToRaster extends MetaProcess {

	private final static String SOURCE_DATA = "SourceData";
	//private final static String BOUNDARY_DATA = "BoundaryData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterCombine sourceData;

	private ParameterFieldComboBox comboBoxValueField;
	private ParameterDatasource boundaryDatasource;
	private ParameterSingleDataset boundaryDataset;
	//private ParameterCheckBox comboBoxDatasetIsCanBoundary;
	private ParameterComboBox comboBoxPixelFormat;
	private ParameterNumber textCellSize;
	private ParameterCombine parameterSetting;

	private ParameterSaveDataset resultDataset;
	private ParameterCombine resultData;

	public MetaProcessVectorToRaster() {
		initParameters();
		initParametersState();
		initParameterConstrint();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

		resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));

		comboBoxValueField = new ParameterFieldComboBox(CommonProperties.getString("String_m_labelGridValueFieldText"));
		boundaryDatasource = new ParameterDatasource();
		boundaryDatasource.setDescribe(CommonProperties.getString("String_BoundaryDatasource"));
		boundaryDataset = new ParameterSingleDataset(DatasetType.REGION).setShowNullValue(true);
		boundaryDataset.setDescribe(CommonProperties.getString("String_BoundaryDataset"));
		//comboBoxDatasetIsCanBoundary = new ParameterCheckBox(CommonProperties.getString("String_SelectedDatasetIsCanBoundary"));
		comboBoxPixelFormat = new ParameterComboBox(CommonProperties.getString("String_PixelType"));
		textCellSize = new ParameterNumber(CommonProperties.getString("String_Resolution"));

		sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset);

		resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(resultDataset);

		parameterSetting = new ParameterCombine();
		parameterSetting.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
		parameterSetting.addParameters(comboBoxValueField, boundaryDatasource, boundaryDataset, comboBoxPixelFormat, textCellSize);

		this.parameters.setParameters(sourceData, parameterSetting, resultData);
		this.parameters.addInputParameters(SOURCE_DATA, DatasetTypes.POINT, sourceData);
		this.parameters.addInputParameters(SOURCE_DATA, DatasetTypes.LINE, sourceData);
		this.parameters.addInputParameters(SOURCE_DATA, DatasetTypes.REGION, sourceData);
		//this.parameters.addInputParameters(BOUNDARY_DATA, DatasetTypes.REGION, b);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID, resultData);

	}

	private void initParametersState() {
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			sourceDatasource.setSelectedItem(datasetVector.getDatasource());
			sourceDataset.setDatasource(datasetVector.getDatasource());
			boundaryDataset.setDatasource(datasetVector.getDatasource());
		}

		DatasetVector datasetVector1 = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector1 != null) {
			boundaryDatasource.setSelectedItem(datasetVector1.getDatasource());
			boundaryDataset.setDatasource(datasetVector1.getDatasource());
		}

		comboBoxValueField.setFieldType(fieldType);

		comboBoxPixelFormat.setItems(new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.SINGLE), PixelFormat.SINGLE),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.DOUBLE), PixelFormat.DOUBLE),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT8), PixelFormat.BIT8),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT16), PixelFormat.BIT16),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT32), PixelFormat.BIT32),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT64), PixelFormat.BIT64),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT1), PixelFormat.UBIT1),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT4), PixelFormat.UBIT4),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT8), PixelFormat.UBIT8),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT16), PixelFormat.UBIT16),
				new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.UBIT32), PixelFormat.UBIT32)
		);
		textCellSize.setSelectedItem("1");
		textCellSize.setMinValue(0);
		textCellSize.setIsIncludeMin(false);
		comboBoxPixelFormat.setSelectedItem(new ParameterDataNode(PixelFormatUtilities.toString(PixelFormat.BIT32), PixelFormat.BIT32));
		resultDataset.setSelectedItem("ConversionGridResult");
	}

	private void initParameterConstrint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint constraintSourceDataset = new EqualDatasetConstraint();
		constraintSourceDataset.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		constraintSourceDataset.constrained(comboBoxValueField, ParameterFieldComboBox.DATASET_FIELD_NAME);

		EqualDatasourceConstraint constraintClip = new EqualDatasourceConstraint();
		constraintClip.constrained(boundaryDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraintClip.constrained(boundaryDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
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
				}
			}
		});

		comboBoxValueField.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				if (comboBoxValueField.getSelectedItem() != null && evt.getNewValue() instanceof FieldInfo) {
					FieldInfo fieldInfo = (FieldInfo) evt.getNewValue();
					if (fieldInfo.getType() == FieldType.INT16) {
						comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(PixelFormat.BIT16));
					} else if (fieldInfo.getType() == FieldType.INT32) {
						comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(PixelFormat.BIT32));
					} else if (fieldInfo.getType() == FieldType.INT64) {
						comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(PixelFormat.BIT64));
					} else if (fieldInfo.getType() == FieldType.DOUBLE) {
						comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(PixelFormat.DOUBLE));
					} else if (fieldInfo.getType() == FieldType.SINGLE) {
						comboBoxPixelFormat.setSelectedItem(PixelFormatUtilities.toString(PixelFormat.SINGLE));
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
			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);

			if (parameters.getInputs().getData(SOURCE_DATA).getValue() != null) {
				conversionParameter.setSourceDataset((Dataset) parameters.getInputs().getData(SOURCE_DATA).getValue());
			} else {
				conversionParameter.setSourceDataset(sourceDataset.getSelectedDataset());
			}

			//'conversionParameter.setSourceDataset(sourceDataset.getSelectedDataset());
			conversionParameter.setTargetDatasource(resultDataset.getResultDatasource());
			conversionParameter.setTargetDatasetName(datasetName);
			conversionParameter.setPixelFormat((PixelFormat) comboBoxPixelFormat.getSelectedData());
			conversionParameter.setValueFieldName((String) comboBoxValueField.getSelectedItem());
			conversionParameter.setCellSize(Double.valueOf(this.textCellSize.getSelectedItem().toString()));
//			DatasetVector dataset = null;
//			if (parameters.getInputs().getData(BOUNDARY_DATA).getValue() != null) {
//				dataset = (DatasetVector) parameters.getInputs().getData(BOUNDARY_DATA).getValue();
//			} else {
//				dataset = (DatasetVector) this.boundaryDataset.getSelectedDataset();
//			}
			if (this.boundaryDataset.getSelectedDataset() != null) {
				DatasetVector dataset=(DatasetVector) this.boundaryDataset.getSelectedDataset();
				Recordset recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
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
		return MetaKeys.VECTORTOGRID;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Form_VectorToGrid");
	}
}
