package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.analyst.spatialanalyst.InterpolationIDWParameter;
import com.supermap.analyst.spatialanalyst.InterpolationKrigingParameter;
import com.supermap.analyst.spatialanalyst.InterpolationParameter;
import com.supermap.analyst.spatialanalyst.InterpolationRBFParameter;
import com.supermap.analyst.spatialanalyst.Interpolator;
import com.supermap.analyst.spatialanalyst.SearchMode;
import com.supermap.analyst.spatialanalyst.VariogramMode;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.FieldInfo;
import com.supermap.data.PixelFormat;
import com.supermap.data.Rectangle2D;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterSearchModeInfo;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.implement.ParameterSearchMode;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.PixelFormatProperties;

/**
 * Created by xie on 2017/2/16.
 */
public class MetaProcessInterpolator extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "InterpolateResult";

	private ParameterDatasourceConstrained parameterDatasource;
	private ParameterSingleDataset parameterDataset;
	private ParameterFieldComboBox parameterInterpolatorFields;
	private ParameterTextField parameterScaling;
	private ParameterSaveDataset parameterResultDatasetName;
	private ParameterTextField parameterResulotion;
	private ParameterComboBox parameterPixelType;
	private ParameterTextField parameterRow;
	private ParameterTextField parameterColumn;
	private ParameterTextField parameterBoundsLeft;
	private ParameterTextField parameterBoundsTop;
	private ParameterTextField parameterBoundsRight;
	private ParameterTextField parameterBoundsBottom;
	private ParameterTextField parameterPower;
	private ParameterSearchMode searchMode;
	private ParameterTextField parameterTension;
	private ParameterTextField parameterSmooth;
	private ParameterComboBox parameterVariogramMode;
	private ParameterTextField parameterStill;
	private ParameterTextField parameterAngle;
	private ParameterTextField parameterRange;
	private ParameterTextField parameterMean;
	private ParameterTextField parameterNugget;

	private InterpolationAlgorithmType interpolationAlgorithmType;
	private SteppedListener stepLitener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			RunningEvent event = new RunningEvent(MetaProcessInterpolator.this, steppedEvent.getPercent(), steppedEvent.getMessage());
			fireRunning(event);

			if (event.isCancel()) {
				steppedEvent.setCancel(true);
			}
		}
	};

	public MetaProcessInterpolator(InterpolationAlgorithmType interpolationAlgorithmType) {

		this.interpolationAlgorithmType = interpolationAlgorithmType;
		parameterDatasource = new ParameterDatasourceConstrained();
		parameterDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		Datasource currentDatasource = null;
		if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
			currentDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		} else if (null != Application.getActiveApplication().getWorkspace().getDatasources() && Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			currentDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		}
		parameterDatasource.setSelectedItem(currentDatasource);
		parameterDataset = new ParameterSingleDataset(DatasetType.POINT);
		DatasetVector currentDataset = null;
		if (currentDatasource != null) {
			Datasets datasets = currentDatasource.getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (datasets.get(i).getType() == DatasetType.POINT) {
					currentDataset = (DatasetVector) datasets.get(i);
					break;
				}
			}
		}
		if (currentDataset != null) {
			parameterDataset.setSelectedItem(currentDataset);
		}

		parameterInterpolatorFields = new ParameterFieldComboBox();
		parameterInterpolatorFields.setDescribe(ProcessProperties.getString("String_InterpolatorFields"));
		if (currentDataset != null) {
			parameterInterpolatorFields.setSelectedItem(currentDataset.getFieldInfos().get(0));
		}

		parameterScaling = new ParameterTextField().setDescribe(CommonProperties.getString("String_Scaling"));
		parameterScaling.setSelectedItem("1");
		parameterResultDatasetName = new ParameterSaveDataset();
		parameterResultDatasetName.setDatasetName("Interpolator");
		parameterResultDatasetName.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		parameterResultDatasetName.setDatasetDescribe(CommonProperties.getString(CommonProperties.Label_Dataset));
		parameterResulotion = new ParameterTextField().setDescribe(CommonProperties.getString("String_Resolution"));
		parameterResulotion.setSelectedItem("0");
		parameterPixelType = new ParameterComboBox().setDescribe(CommonProperties.getString("String_PixelType"));
		ParameterDataNode selectedItem = new ParameterDataNode(PixelFormatProperties.getString("String_Bit32"), PixelFormat.BIT32);
		parameterPixelType.setItems(
				new ParameterDataNode(PixelFormatProperties.getString("String_UBit1"), PixelFormat.UBIT1),
				new ParameterDataNode(PixelFormatProperties.getString("String_Bit16"), PixelFormat.UBIT16),
				selectedItem,
				new ParameterDataNode(CommonProperties.getString("String_PixelSingle"), PixelFormat.SINGLE),
				new ParameterDataNode(CommonProperties.getString("String_PixelDouble"), PixelFormat.DOUBLE));
		parameterPixelType.setSelectedItem(selectedItem);
		parameterColumn = new ParameterTextField().setDescribe(CommonProperties.getString("String_Column"));
		parameterColumn.setSelectedItem("1");
		parameterRow = new ParameterTextField().setDescribe(CommonProperties.getString("String_Row"));
		parameterRow.setSelectedItem("1");
		parameterBoundsLeft = new ParameterTextField().setDescribe(ControlsProperties.getString("String_LabelLeft"));
		parameterBoundsTop = new ParameterTextField().setDescribe(ControlsProperties.getString("String_LabelTop"));
		parameterBoundsRight = new ParameterTextField().setDescribe(ControlsProperties.getString("String_LabelRight"));
		parameterBoundsBottom = new ParameterTextField().setDescribe(ControlsProperties.getString("String_LabelBottom"));
		if (null != parameterDataset.getSelectedItem()) {
			DatasetVector selectedDataset = (DatasetVector) parameterDataset.getSelectedItem();
			Rectangle2D bounds = selectedDataset.getBounds();
			parameterBoundsLeft.setSelectedItem(bounds.getLeft());
			parameterBoundsTop.setSelectedItem(bounds.getTop());
			parameterBoundsRight.setSelectedItem(bounds.getRight());
			parameterBoundsBottom.setSelectedItem(bounds.getBottom());
		}
		searchMode = new ParameterSearchMode();
		ParameterSearchModeInfo info = new ParameterSearchModeInfo();
		info.searchMode = SearchMode.KDTREE_FIXED_COUNT;
		info.searchRadius = 0;
		info.expectedCount = 12;
		searchMode.setSelectedItem(info);
		parameterPower = new ParameterTextField().setDescribe(CommonProperties.getString("String_Power"));
		parameterPower.setSelectedItem(2);
		parameterTension = new ParameterTextField().setDescribe(CommonProperties.getString("String_Tension"));
		parameterTension.setSelectedItem(40);
		parameterSmooth = new ParameterTextField().setDescribe(CommonProperties.getString("String_Smooth"));
		parameterSmooth.setSelectedItem(0.1);
		ParameterDataNode spherical = new ParameterDataNode(CommonProperties.getString("String_VariogramMode_Spherical"), VariogramMode.SPHERICAL);
		parameterVariogramMode = new ParameterComboBox().setDescribe(CommonProperties.getString("String_VariogramMode"));
		parameterVariogramMode.setItems(new ParameterDataNode(CommonProperties.getString("String_VariogramMode_Exponential"), VariogramMode.EXPONENTIAL),
				new ParameterDataNode(CommonProperties.getString("String_VariogramMode_Gaussian"), VariogramMode.GAUSSIAN),
				spherical);
		parameterVariogramMode.setSelectedItem(spherical);
		parameterStill = new ParameterTextField().setDescribe(CommonProperties.getString("String_Still"));
		parameterStill.setSelectedItem(0);
		parameterAngle = new ParameterTextField().setDescribe(CommonProperties.getString("String_Angle"));
		parameterAngle.setSelectedItem(0);
		parameterRange = new ParameterTextField().setDescribe(CommonProperties.getString("String_Range"));
		parameterRange.setSelectedItem(0);
		parameterMean = new ParameterTextField().setDescribe(CommonProperties.getString("String_Mean"));
		parameterMean.setSelectedItem(0);
		parameterNugget = new ParameterTextField().setDescribe(CommonProperties.getString("String_Nugget"));
		parameterNugget.setSelectedItem(0);

		if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.IDW)) {
			ParameterCombine sourceData = new ParameterCombine(ParameterCombine.HORIZONTAL);
			sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
			sourceData.addParameters(new ParameterCombine().addParameters(parameterDatasource, parameterInterpolatorFields)
					, new ParameterCombine().addParameters(parameterDataset, parameterScaling));
			ParameterCombine targetData = new ParameterCombine(ParameterCombine.HORIZONTAL);
			targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
			targetData.addParameters(new ParameterCombine().addParameters(parameterResultDatasetName, parameterResulotion
			), new ParameterCombine().addParameters(parameterPixelType, parameterColumn, parameterRow));
			ParameterCombine bounds = new ParameterCombine();
			bounds.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_Bounds"));
			bounds.addParameters(parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom);
			ParameterCombine modeSet = new ParameterCombine();
			modeSet.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_SearchModeSetting"));
			modeSet.addParameters(searchMode);

			ParameterCombine otherParam = new ParameterCombine();
			otherParam.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_OtherParameters"));
			otherParam.addParameters(parameterPower);
			parameters.setParameters(sourceData, targetData, bounds, modeSet, otherParam);
			this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceData);
			this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID, targetData);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			ParameterCombine sourceData = new ParameterCombine(ParameterCombine.HORIZONTAL);
			sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
			sourceData.addParameters(new ParameterCombine().addParameters(parameterDatasource, parameterInterpolatorFields)
					, new ParameterCombine().addParameters(parameterDataset, parameterScaling));
			ParameterCombine targetData = new ParameterCombine(ParameterCombine.HORIZONTAL);
			targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
			targetData.addParameters(new ParameterCombine().addParameters(parameterResultDatasetName, parameterResulotion
			), new ParameterCombine().addParameters(parameterPixelType, parameterColumn, parameterRow));
			ParameterCombine bounds = new ParameterCombine();
			bounds.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_Bounds"));
			bounds.addParameters(parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom);
			ParameterCombine modeSet = new ParameterCombine();
			modeSet.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_SearchModeSetting"));
			modeSet.addParameters(searchMode);

			ParameterCombine otherParam = new ParameterCombine();
			otherParam.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_OtherParameters"));
			otherParam.addParameters(parameterTension, parameterSmooth);
			parameters.setParameters(sourceData, targetData, bounds, modeSet, otherParam);
			this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceData);
			this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID, targetData);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING)
				|| interpolationAlgorithmType.equals(InterpolationAlgorithmType.SimpleKRIGING)
				|| interpolationAlgorithmType.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
			ParameterCombine sourceData = new ParameterCombine(ParameterCombine.HORIZONTAL);
			sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
			sourceData.addParameters(new ParameterCombine().addParameters(parameterDatasource, parameterInterpolatorFields)
					, new ParameterCombine().addParameters(parameterDataset, parameterScaling));
			ParameterCombine targetData = new ParameterCombine(ParameterCombine.HORIZONTAL);
			targetData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
			targetData.addParameters(new ParameterCombine().addParameters(parameterResultDatasetName, parameterResulotion
			), new ParameterCombine().addParameters(parameterPixelType, parameterColumn, parameterRow));
			ParameterCombine bounds = new ParameterCombine();
			bounds.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_Bounds"));
			bounds.addParameters(parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom);
			ParameterCombine modeSet = new ParameterCombine();
			modeSet.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_SearchModeSetting"));
			modeSet.addParameters(searchMode);

			ParameterCombine otherParam = new ParameterCombine(ParameterCombine.HORIZONTAL);
			otherParam.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_OtherParameters"));
			otherParam.addParameters(new ParameterCombine().addParameters(parameterVariogramMode, parameterAngle, parameterMean)
					, new ParameterCombine().addParameters(parameterStill, parameterRange, parameterNugget));
			parameters.setParameters(sourceData, targetData, bounds, modeSet, otherParam);
			this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceData);
			this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.GRID, targetData);
		}


		initParameterConstraint();
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(parameterDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		DatasourceConstraint.getInstance().constrained(parameterResultDatasetName, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(parameterDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterInterpolatorFields, ParameterFieldComboBox.DATASET_FIELD_NAME);
	}

	@Override
	public String getTitle() {
		String title = "";
		if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.IDW)) {
			title = ControlsProperties.getString("String_Interpolator_IDW");
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			title = ControlsProperties.getString("String_Interpolator_RBF");
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING)) {
			title = ControlsProperties.getString("String_Interpolator_KRIGING");
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
			title = ControlsProperties.getString("String_Interpolator_SimpleKRIGING");
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
			title = ControlsProperties.getString("String_Interpolator_UniversalKRIGING");
		}
		return title;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public void run() {
		fireRunning(new RunningEvent(this, 0, "start"));
		InterpolationParameter interpolationParameter = null;
		if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.IDW)) {
			interpolationParameter = new InterpolationIDWParameter();
			setInterpolationParameter(interpolationParameter);
			((InterpolationIDWParameter) interpolationParameter).setPower(Integer.valueOf(parameterPower.getSelectedItem().toString()));
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			interpolationParameter = new InterpolationRBFParameter();
			setInterpolationParameter(interpolationParameter);
			((InterpolationRBFParameter) interpolationParameter).setTension(Double.valueOf(parameterTension.getSelectedItem().toString()));
			((InterpolationRBFParameter) interpolationParameter).setSmooth(Double.valueOf(parameterSmooth.getSelectedItem().toString()));
		} else {
			if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING)) {
				interpolationParameter = new InterpolationKrigingParameter(InterpolationAlgorithmType.KRIGING);
			} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
				interpolationParameter = new InterpolationKrigingParameter(InterpolationAlgorithmType.SimpleKRIGING);
			} else {
				interpolationParameter = new InterpolationKrigingParameter(InterpolationAlgorithmType.UniversalKRIGING);
			}
			setInterpolationParameter(interpolationParameter);
			((InterpolationKrigingParameter) interpolationParameter).setVariogramMode((VariogramMode) ((ParameterDataNode) parameterVariogramMode.getSelectedItem()).getData());
			((InterpolationKrigingParameter) interpolationParameter).setSill(Double.valueOf(parameterStill.getSelectedItem().toString()));
			((InterpolationKrigingParameter) interpolationParameter).setAngle(Double.valueOf(parameterAngle.getSelectedItem().toString()));
			((InterpolationKrigingParameter) interpolationParameter).setRange(Double.valueOf(parameterRange.getSelectedItem().toString()));
			if (interpolationParameter.equals(InterpolationAlgorithmType.SimpleKRIGING))
				((InterpolationKrigingParameter) interpolationParameter).setMean(Double.valueOf(parameterMean.getSelectedItem().toString()));
			((InterpolationKrigingParameter) interpolationParameter).setNugget(Double.valueOf(parameterNugget.getSelectedItem().toString()));
		}
		Interpolator.addSteppedListener(this.stepLitener);

		DatasetVector datasetVector = null;
		if (this.parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
			datasetVector = (DatasetVector) this.parameters.getInputs().getData(INPUT_DATA).getValue();
		} else {
			datasetVector = (DatasetVector) this.parameterDataset.getSelectedItem();
		}
		Datasource targetDatasource = parameterResultDatasetName.getResultDatasource();
		String datasetName = parameterResultDatasetName.getDatasetName();
		datasetName = targetDatasource.getDatasets().getAvailableDatasetName(datasetName);
		DatasetGrid dataset = Interpolator.interpolate(interpolationParameter, datasetVector,
				((FieldInfo) parameterInterpolatorFields.getSelectedItem()).getName(), Double.valueOf(parameterScaling.getSelectedItem().toString()),
				targetDatasource, datasetName,
				(PixelFormat) ((ParameterDataNode) parameterPixelType.getSelectedItem()).getData());
		Interpolator.removeSteppedListener(this.stepLitener);
		this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(dataset);
		fireRunning(new RunningEvent(this, 100, "finished"));
		setFinished(true);
	}

	@Override
	public String getKey() {
		String key = "";
		if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.IDW)) {
			key = MetaKeys.INTERPOLATOR_IDW;
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			key = MetaKeys.INTERPOLATOR_RBF;
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING)) {
			key = MetaKeys.INTERPOLATOR_KRIGING;
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
			key = MetaKeys.INTERPOLATOR_SimpleKRIGING;
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
			key = MetaKeys.INTERPOLATOR_UniversalKRIGING;
		}
		return key;
	}


	public void setInterpolationParameter(InterpolationParameter interpolationParameter) {
		Rectangle2D bounds = new Rectangle2D();
		bounds.setLeft(Double.valueOf(parameterBoundsLeft.getSelectedItem().toString()));
		bounds.setTop(Double.valueOf(parameterBoundsTop.getSelectedItem().toString()));
		bounds.setRight(Double.valueOf(parameterBoundsRight.getSelectedItem().toString()));
		bounds.setBottom(Double.valueOf(parameterBoundsBottom.getSelectedItem().toString()));
		ParameterSearchModeInfo info = (ParameterSearchModeInfo) searchMode.getSelectedItem();
		interpolationParameter.setExpectedCount(info.expectedCount);
		interpolationParameter.setSearchMode(info.searchMode);
		interpolationParameter.setSearchRadius(info.searchRadius);
		interpolationParameter.setResolution(Double.valueOf(parameterResulotion.getSelectedItem().toString()));
		interpolationParameter.setBounds(bounds);
	}

}
