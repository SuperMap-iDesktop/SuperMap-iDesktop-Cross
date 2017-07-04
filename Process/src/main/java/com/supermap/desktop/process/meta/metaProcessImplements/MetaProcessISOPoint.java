package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.analyst.spatialanalyst.TerrainInterpolateType;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by xie on 2017/3/10.
 */
public class MetaProcessISOPoint extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterFieldComboBox fields;
	private ParameterSaveDataset targetDataset;
	private ParameterTextField maxISOLine;
	private ParameterTextField minISOLine;
	private ParameterTextField isoLine;
	private ParameterComboBox terrainInterpolateType;
	private ParameterNumber resolution;
	private ParameterNumber datumValue;
	private ParameterNumber interval;
	private ParameterNumber resampleTolerance;
	private ParameterComboBox smoothMethod;
	private ParameterNumber smoothNess;
	private boolean isSelectChanged = false;
	private SteppedListener stepListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			RunningEvent event = new RunningEvent(MetaProcessISOPoint.this, steppedEvent.getPercent(), AbstractParameter.PROPERTY_VALE);
			fireRunning(event);

			if (event.isCancel()) {
				steppedEvent.setCancel(true);
			}
		}
	};

	public MetaProcessISOPoint() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		initParametersListener();
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(fields, ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	private void initParametersState() {
		this.sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		this.targetDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.targetDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT,DatasetType.POINT3D);
		if (datasetVector != null) {
			sourceDatasource.setSelectedItem(datasetVector.getDatasource());
			sourceDataset.setSelectedItem(datasetVector);
			targetDataset.setResultDatasource(datasetVector.getDatasource());
			targetDataset.setSelectedItem(datasetVector.getDatasource().getDatasets().getAvailableDatasetName("ISOLine"));
			this.fields.setSelectedItem("SmUserID");
			reloadValue();
		}
		ParameterDataNode selectedInterpolateType = new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_IDW"), TerrainInterpolateType.IDW);
		this.terrainInterpolateType.setItems(selectedInterpolateType,
				new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_Kriging"), TerrainInterpolateType.KRIGING),
				new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_TIN"), TerrainInterpolateType.TIN));
		ParameterDataNode selectedSmoothNode = new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE);
		this.smoothMethod.setItems(selectedSmoothNode,
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		this.smoothMethod.setSelectedItem(selectedSmoothNode);
		this.smoothNess.setEnabled(false);
	}

	private void reloadValue() {
		Dataset dataset = sourceDataset.getSelectedDataset();
		if (dataset != null ) {
			double resulation = getResulation(dataset);
			resolution.setSelectedItem(DoubleUtilities.getFormatString(resulation));
			double maxValue = 0;
			double minValue = 0;
			Recordset recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.STATIC);
			FieldInfos fieldInfos = recordset.getFieldInfos();
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				if(fieldInfos.get(i).getName().equals(fields.getFieldName())){
					maxValue = recordset.statistic(fieldInfos.get(i).getName(), StatisticMode.MAX);
					minValue = recordset.statistic(fieldInfos.get(i).getName(), StatisticMode.MIN);
					break;
				}
			}
			int dSpan = (int) getDefaultInterval(maxValue, minValue);
			interval.setSelectedItem(dSpan);
			double baseValue = Double.valueOf((String) datumValue.getSelectedItem());
			double lineDistance = Double.valueOf((String) interval.getSelectedItem());
			double dRemain = baseValue % lineDistance;
			double maxIsoValue = (int) ((maxValue - dRemain) / lineDistance) * lineDistance + dRemain;
			double minIsoValue = (int) ((minValue - dRemain) / lineDistance) * lineDistance + dRemain;
			int isoCount = (int) ((maxIsoValue - minIsoValue) / lineDistance) + 1;
			maxISOLine.setSelectedItem(DoubleUtilities.getFormatString(maxIsoValue));
			minISOLine.setSelectedItem(DoubleUtilities.getFormatString(minIsoValue));
			isoLine.setSelectedItem(String.valueOf(isoCount));
		}

	}

	private void reload() {
		Dataset dataset = sourceDataset.getSelectedDataset();
		if (dataset != null ) {
			double maxValue = 0;
			double minValue = 0;
			Recordset recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.STATIC);
			if (recordset != null && recordset.getFieldCount() > 0) {
				maxValue = recordset.statistic(fields.getFieldName(), StatisticMode.MAX);
				minValue = recordset.statistic(fields.getFieldName(), StatisticMode.MIN);
			}
			double baseValue = Double.valueOf((String) datumValue.getSelectedItem());
			double lineDistance = Double.valueOf((String) interval.getSelectedItem());
			double dRemain = baseValue % lineDistance;
			double maxIsoValue = (int) ((maxValue - dRemain) / lineDistance) * lineDistance + dRemain;
			double minIsoValue = (int) ((minValue - dRemain) / lineDistance) * lineDistance + dRemain;
			int isoCount = (int) ((maxIsoValue - minIsoValue) / lineDistance) + 1;
			maxISOLine.setSelectedItem(DoubleUtilities.getFormatString(maxIsoValue));
			minISOLine.setSelectedItem(DoubleUtilities.getFormatString(minIsoValue));
			isoLine.setSelectedItem(String.valueOf(isoCount));
		}
	}


	private double getResulation(Dataset dataset) {
		double resulation = 1.0;
		if (dataset != null && dataset.getBounds() != null && dataset.getBounds().getHeight() > 0 && dataset.getBounds().getWidth() > 0) {
			Rectangle2D bounds = dataset.getBounds();
			double x = bounds.getWidth() / 500.0;
			double y = bounds.getHeight() / 500.0;
			resulation = x > y ? x : y;
		}
		return resulation;

	}

	private double getDefaultInterval(double maxValue, double minValue) {
		double result = 1;
		try {
			double dCount = 12;
			Double dValue = maxValue - minValue;
			if (dValue > 0) {
				double dSpan = dValue / dCount;
				if (dSpan > 1 && dSpan <= 10) {
					result = (int) dSpan;
				} else if (dSpan > 10 && dSpan <= 100) {
					result = (int) (dSpan / 10) * 10;
				} else if (dSpan > 100) {
					result = (int) (dSpan / 100) * 100;
				} else {
					result = dSpan;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}


	private void initParameters() {
		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.POINT3D);
		this.fields = new ParameterFieldComboBox();
		this.fields.setDescribe(CommonProperties.getString("String_FieldsName"));
		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		this.fields.setFieldType(fieldType);
		this.fields.setEditable(true);
		this.targetDataset = new ParameterSaveDataset();
		this.maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
		this.minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
		this.isoLine = new ParameterTextField(CommonProperties.getString("String_ISOData"));
		this.terrainInterpolateType = new ParameterComboBox(CommonProperties.getString("String_InterpolateType"));
		this.resolution = new ParameterNumber(ProcessProperties.getString("String_Resolution"));
		resolution.setMinValue(0);
		resolution.setIsIncludeMin(false);
		this.datumValue = new ParameterNumber(CommonProperties.getString("String_DatumValue"));
		this.datumValue.setSelectedItem("0");
		datumValue.setMinValue(0);
		datumValue.setIsIncludeMin(true);
		this.interval = new ParameterNumber(CommonProperties.getString("String_Interval"));
		interval.setMinValue(0);
		interval.setIsIncludeMin(false);
		this.resampleTolerance = new ParameterNumber(CommonProperties.getString("String_ResampleTolerance"));
		this.resampleTolerance.setSelectedItem("0");
		resampleTolerance.setMinValue(0);
		resampleTolerance.setIsIncludeMin(true);
		this.smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
		this.smoothNess = new ParameterNumber(CommonProperties.getString("String_SmoothNess"));
		smoothNess.setMinValue(2);
		smoothNess.setMaxValue(5);
		smoothNess.setMaxBit(0);
		this.smoothNess.setSelectedItem("2");

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset);
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(targetDataset, maxISOLine, minISOLine, isoLine);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(fields, terrainInterpolateType, resolution, datumValue, interval,
				resampleTolerance, smoothMethod, smoothNess);

		this.parameters.setParameters(sourceData, paramSet, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE, resultData);
	}

	private void initParametersListener() {
		smoothMethod.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					smoothNess.setEnabled(smoothMethod.getSelectedIndex() != 0);
				}
			}
		});

		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectChanged && evt.getPropertyName().equals(ParameterSingleDataset.DATASET_FIELD_NAME)) {
					isSelectChanged = true;
					reloadValue();
					isSelectChanged = false;
				}
			}
		});
		datumValue.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectChanged && evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					isSelectChanged = true;
					reload();
					isSelectChanged = false;
				}
			}
		});
		interval.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectChanged && evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					isSelectChanged = true;
					reload();
					isSelectChanged = false;
				}
			}
		});
		fields.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectChanged && evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					isSelectChanged = true;
					reloadValue();
					isSelectChanged = false;
				}
			}
		});
	}

	;

	@Override
	public String getTitle() {
		return CommonProperties.getString("String_SurfaceISOPoint");
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {
			SurfaceExtractParameter surfaceExtractParameter = new SurfaceExtractParameter();
			surfaceExtractParameter.setDatumValue(Double.valueOf(datumValue.getSelectedItem().toString()));
			surfaceExtractParameter.setInterval(Double.valueOf(interval.getSelectedItem().toString()));
			surfaceExtractParameter.setResampleTolerance(Double.valueOf(resampleTolerance.getSelectedItem().toString()));
			surfaceExtractParameter.setSmoothMethod((SmoothMethod) ((ParameterDataNode) smoothMethod.getSelectedItem()).getData());
			surfaceExtractParameter.setSmoothness(Integer.valueOf(smoothNess.getSelectedItem().toString()));
			SurfaceAnalyst.addSteppedListener(this.stepListener);

			DatasetVector src = null;
			if (this.getParameters().getInputs().getData(INPUT_DATA).getValue() != null) {
				src = (DatasetVector) this.getParameters().getInputs().getData(INPUT_DATA).getValue();
			} else {
				src = (DatasetVector) sourceDataset.getSelectedItem();
			}
			GeoLine[] lines = SurfaceAnalyst.extractIsoline(surfaceExtractParameter, src, fields.getFieldName(), ((TerrainInterpolateType) ((ParameterDataNode) terrainInterpolateType.getSelectedItem()).getData()), (Double) resolution.getSelectedItem(), null);
			isSuccessful = (lines != null && lines.length > 0);
			fireRunning(new RunningEvent(MetaProcessISOPoint.this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
		} finally {
			SurfaceAnalyst.removeSteppedListener(this.stepListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		return MetaKeys.ISOPOINT;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

}
