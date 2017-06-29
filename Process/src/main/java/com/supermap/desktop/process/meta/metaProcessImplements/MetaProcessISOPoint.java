package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.analyst.spatialanalyst.SurfaceAnalyst;
import com.supermap.analyst.spatialanalyst.SurfaceExtractParameter;
import com.supermap.analyst.spatialanalyst.TerrainInterpolateType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLine;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

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
	private ParameterTextField resolution;
	private ParameterTextField datumValue;
	private ParameterTextField interval;
	private ParameterTextField resampleTolerance;
	private ParameterComboBox smoothMethod;
	private ParameterTextField smoothNess;
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
		this.targetDataset.setSelectedItem("ISOLine");
		ParameterDataNode selectedInterpolateType = new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_IDW"), TerrainInterpolateType.IDW);
		this.terrainInterpolateType.setItems(selectedInterpolateType,
				new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_Kriging"), TerrainInterpolateType.KRIGING),
				new ParameterDataNode(CommonProperties.getString("String_TerrainInterpolateType_TIN"), TerrainInterpolateType.TIN));
		ParameterDataNode selectedSmoothNode = new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE);
		this.smoothMethod.setItems(selectedSmoothNode,
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		this.smoothMethod.setSelectedItem(selectedSmoothNode);
	}

	private void initParameters() {

		this.sourceDatasource = new ParameterDatasourceConstrained();
		this.sourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.POINT3D);
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			sourceDatasource.setSelectedItem(datasetVector.getDatasource());
			sourceDataset.setSelectedItem(datasetVector);
		}
		this.fields = new ParameterFieldComboBox();
		this.fields.setDescribe(CommonProperties.getString("String_FieldsName"));
		this.fields.setDataset((DatasetVector) sourceDataset.getSelectedItem());
		this.targetDataset = new ParameterSaveDataset();
		this.maxISOLine = new ParameterTextField(CommonProperties.getString("String_MAXISOLine"));
		this.minISOLine = new ParameterTextField(CommonProperties.getString("String_MINISOLine"));
		this.isoLine = new ParameterTextField(CommonProperties.getString("String_ISOData"));
		this.terrainInterpolateType = new ParameterComboBox(CommonProperties.getString("String_InterpolateType"));
		this.resolution = new ParameterTextField(ProcessProperties.getString("String_Resolution"));
		this.datumValue = new ParameterTextField(CommonProperties.getString("String_DatumValue"));
		this.interval = new ParameterTextField(CommonProperties.getString("String_Interval"));
		this.resampleTolerance = new ParameterTextField(CommonProperties.getString("String_ResampleTolerance"));
		this.smoothMethod = new ParameterComboBox().setDescribe(CommonProperties.getString("String_SmoothMethod"));
		this.smoothNess = new ParameterTextField(CommonProperties.getString("String_SmoothNess"));

		ParameterCombine sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset, fields);
		ParameterCombine resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(targetDataset, maxISOLine, minISOLine, isoLine, terrainInterpolateType);
		ParameterCombine paramSet = new ParameterCombine();
		paramSet.setDescribe(CommonProperties.getString("String_FormEdgeCount_Text"));
		paramSet.addParameters(resolution, datumValue, interval,
				resampleTolerance, smoothMethod, smoothNess);

		this.parameters.setParameters(sourceData, resultData, paramSet);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.POINT, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.LINE, resultData);
	}

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
//		this.outputs.getData(OUTPUT_DATA).setValue();
			fireRunning(new RunningEvent(MetaProcessISOPoint.this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_Params_error"));
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
