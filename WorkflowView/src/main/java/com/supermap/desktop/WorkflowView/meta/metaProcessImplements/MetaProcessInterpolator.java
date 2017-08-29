package com.supermap.desktop.WorkflowView.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.WorkflowView.meta.loader.InterpolatorProcessLoader;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.ipls.DatasourceConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.ipls.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterSearchModeInfo;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.PixelFormatProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/2/16.
 */
public class MetaProcessInterpolator extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OUTPUT_DATA = "InterpolateResult";

	private ParameterDatasourceConstrained parameterDatasource;
	private ParameterSingleDataset parameterDataset;
	private ParameterFieldComboBox parameterInterpolatorFields;
	private ParameterNumber parameterScaling;
	private ParameterSaveDataset parameterResultDatasetName;
	private ParameterNumber parameterResulotion;
	private ParameterComboBox parameterPixelType;
	private ParameterNumber parameterRow;
	private ParameterNumber parameterColumn;
	private ParameterNumber parameterBoundsLeft;
	private ParameterNumber parameterBoundsTop;
	private ParameterNumber parameterBoundsRight;
	private ParameterNumber parameterBoundsBottom;
	private ParameterNumber parameterPower;
	private ParameterSearchMode searchMode;
	private ParameterNumber parameterTension;
	private ParameterNumber parameterSmooth;
	private ParameterComboBox parameterVariogramMode;
	private ParameterNumber parameterStill;
	private ParameterNumber parameterAngle;
	private ParameterNumber parameterRange;
	private ParameterComboBox parameterSteps;
	private ParameterNumber parameterMean;
	private ParameterNumber parameterNugget;
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

	public PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (parameterDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector) {
				Rectangle2D bounds = ((DatasetVector) evt.getNewValue()).getBounds();
				parameterBoundsLeft.setSelectedItem(bounds.getLeft());
				parameterBoundsTop.setSelectedItem(bounds.getTop());
				parameterBoundsRight.setSelectedItem(bounds.getRight());
				parameterBoundsBottom.setSelectedItem(bounds.getBottom());
				double cellSize = Math.sqrt(Math.pow(bounds.getHeight(), 2) + Math.pow(bounds.getWidth(), 2)) / 500;
				parameterResulotion.setSelectedItem(cellSize);
				parameterRow.setSelectedItem((int) (bounds.getHeight() / cellSize));
				parameterColumn.setSelectedItem((int) (bounds.getWidth() / cellSize));
			}
		}
	};

	public MetaProcessInterpolator(InterpolationAlgorithmType interpolationAlgorithmType) {
		this.interpolationAlgorithmType = interpolationAlgorithmType;
		initParameters();
		initParameterStates();
		initParameterConstraint();
		registerEvents();
	}

	private void initParameters() {
		parameterDatasource = new ParameterDatasourceConstrained();
		parameterDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		parameterDataset = new ParameterSingleDataset(DatasetType.POINT);
		parameterInterpolatorFields = new ParameterFieldComboBox();
		parameterInterpolatorFields.setDescribe(ProcessProperties.getString("String_InterpolatorFields"));
		parameterScaling = new ParameterNumber(CommonProperties.getString("String_Scaling"));
		parameterScaling.setSelectedItem(1);
		parameterScaling.setMinValue(0);
		ParameterCombine sourceCombine = new ParameterCombine();
		sourceCombine.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceCombine.addParameters(parameterDatasource, parameterDataset);

		ParameterCombine parameterField = new ParameterCombine();
		parameterField.setDescribe(SETTING_PANEL_DESCRIPTION);
		parameterField.addParameters(parameterInterpolatorFields, parameterScaling);

		parameterResultDatasetName = new ParameterSaveDataset();
		parameterResultDatasetName.setSelectedItem("result_interpolator");
		parameterResultDatasetName.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		parameterResultDatasetName.setDatasetDescribe(CommonProperties.getString(CommonProperties.Label_Dataset));
		parameterResulotion = new ParameterNumber(CommonProperties.getString("String_Resolution"));
		parameterResulotion.setMinValue(0);
		parameterResulotion.setIsIncludeMin(false);
		parameterPixelType = new ParameterComboBox().setDescribe(CommonProperties.getString("String_PixelType"));
		ParameterDataNode selectedItem = new ParameterDataNode(PixelFormatProperties.getString("String_Bit32"), PixelFormat.BIT32);
		parameterPixelType.setItems(
				new ParameterDataNode(PixelFormatProperties.getString("String_UBit1"), PixelFormat.UBIT1),
				new ParameterDataNode(PixelFormatProperties.getString("String_Bit16"), PixelFormat.UBIT16),
				selectedItem,
				new ParameterDataNode(ProcessProperties.getString("String_PixelSingle"), PixelFormat.SINGLE),
				new ParameterDataNode(ProcessProperties.getString("String_PixelDouble"), PixelFormat.DOUBLE));
		parameterPixelType.setSelectedItem(selectedItem);
		parameterColumn = new ParameterNumber(CommonProperties.getString("String_Column"));
		parameterRow = new ParameterNumber(CommonProperties.getString("String_Row"));
		parameterColumn.setEnabled(false);
		parameterRow.setEnabled(false);
		ParameterCombine targetCombine = new ParameterCombine();
		targetCombine.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		targetCombine.addParameters(parameterResultDatasetName, parameterResulotion, parameterPixelType, parameterRow, parameterColumn);

		parameterBoundsLeft = new ParameterNumber(ControlsProperties.getString("String_LabelLeft"));
		parameterBoundsTop = new ParameterNumber(ControlsProperties.getString("String_LabelTop"));
		parameterBoundsRight = new ParameterNumber(ControlsProperties.getString("String_LabelRight"));
		parameterBoundsBottom = new ParameterNumber(ControlsProperties.getString("String_LabelBottom"));
		ParameterCombine boundsCombine = new ParameterCombine();
		boundsCombine.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_Bounds"));
		boundsCombine.addParameters(parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom);

		searchMode = new ParameterSearchMode();
		searchMode.setQuadTree(interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF) || interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING));
		ParameterSearchModeInfo info = new ParameterSearchModeInfo();
		info.searchMode = SearchMode.KDTREE_FIXED_COUNT;
		info.searchRadius = 0;
		info.expectedCount = 12;
		searchMode.setSelectedItem(info);
		ParameterCombine modeSetCombine = new ParameterCombine();
		modeSetCombine.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_SearchModeSetting"));
//			if(interpolationAlgorithmType == InterpolationAlgorithmType.RBF || interpolationAlgorithmType == InterpolationAlgorithmType.KRIGING){
//				modeSetCombine.addParameters(searchMode);
//			}
		modeSetCombine.addParameters(searchMode);

		parameterPower = new ParameterNumber(CommonProperties.getString("String_Power"));
		parameterPower.setSelectedItem(2);
		parameterPower.setMaxValue(100);
		parameterPower.setMinValue(1);
		parameterTension = new ParameterNumber(CommonProperties.getString("String_Tension"));
		parameterTension.setSelectedItem(40);
		parameterTension.setMinValue(0);
		parameterSmooth = new ParameterNumber(CommonProperties.getString("String_Smooth"));
		parameterSmooth.setSelectedItem(0.1);
		parameterSmooth.setMinValue(0);
		parameterSmooth.setMaxValue(1);
		ParameterDataNode spherical = new ParameterDataNode(CommonProperties.getString("String_VariogramMode_Spherical"), VariogramMode.SPHERICAL);
		parameterVariogramMode = new ParameterComboBox().setDescribe(CommonProperties.getString("String_VariogramMode"));
		parameterVariogramMode.setItems(new ParameterDataNode(CommonProperties.getString("String_VariogramMode_Exponential"), VariogramMode.EXPONENTIAL),
				new ParameterDataNode(CommonProperties.getString("String_VariogramMode_Gaussian"), VariogramMode.GAUSSIAN),
				spherical);
		parameterVariogramMode.setSelectedItem(spherical);
		parameterStill = new ParameterNumber(CommonProperties.getString("String_Still"));
		parameterStill.setSelectedItem(0);
		parameterAngle = new ParameterNumber(CommonProperties.getString("String_Angle"));
		parameterAngle.setSelectedItem(0);
		parameterAngle.setMinValue(0);
		parameterAngle.setMaxValue(360);
		parameterRange = new ParameterNumber(CommonProperties.getString("String_Range"));
		parameterRange.setSelectedItem(0);
		parameterRange.setMinValue(0);
		parameterMean = new ParameterNumber(CommonProperties.getString("String_Mean"));
		parameterMean.setSelectedItem(0);
		parameterSteps = new ParameterComboBox().setDescribe(CommonProperties.getString("String_Steps"));
		parameterSteps.setItems(new ParameterDataNode("1", Exponent.exp1), new ParameterDataNode("2", Exponent.exp2));
		parameterNugget = new ParameterNumber(CommonProperties.getString("String_Nugget"));
		parameterNugget.setSelectedItem(0);
		ParameterCombine otherParamCombine = new ParameterCombine();
		otherParamCombine.setDescribe(ProcessProperties.getString("String_InterpolationAnalyst_OtherParameters"));

		if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.IDW)) {
			otherParamCombine.addParameters(parameterPower);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			otherParamCombine.addParameters(parameterTension, parameterSmooth);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING)) {
			otherParamCombine.addParameters(new ParameterCombine().addParameters(parameterVariogramMode, parameterAngle, parameterMean)
					, new ParameterCombine().addParameters(parameterStill, parameterRange, parameterNugget));
			parameterMean.setEnabled(false);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
			otherParamCombine.addParameters(new ParameterCombine().addParameters(parameterVariogramMode, parameterAngle, parameterMean)
					, new ParameterCombine().addParameters(parameterStill, parameterRange, parameterNugget));
			parameterMean.setSelectedItem(4);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
			otherParamCombine.addParameters(new ParameterCombine().addParameters(parameterVariogramMode, parameterAngle, parameterSteps)
					, new ParameterCombine().addParameters(parameterStill, parameterRange, parameterNugget));
		}

		parameters.setParameters(sourceCombine, parameterField, targetCombine
				, modeSetCombine, otherParamCombine);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, sourceCombine);
		this.parameters.addOutputParameters(OUTPUT_DATA,
				MessageFormat.format(ProcessOutputResultProperties.getString("String_Result"), getTitle()),
				DatasetTypes.GRID, targetCombine);
	}

	private void initParameterStates() {
		Dataset datasetVector = DatasetUtilities.getDefaultDataset(DatasetType.POINT);
		if (datasetVector != null) {
			parameterDatasource.setSelectedItem(datasetVector.getDatasource());
			parameterDataset.setSelectedItem(datasetVector);
			parameterInterpolatorFields.setFieldName((DatasetVector) datasetVector);

			FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
			parameterInterpolatorFields.setFieldType(fieldType);

			Rectangle2D bounds = datasetVector.getBounds();
			parameterBoundsLeft.setSelectedItem(bounds.getLeft());
			parameterBoundsTop.setSelectedItem(bounds.getTop());
			parameterBoundsRight.setSelectedItem(bounds.getRight());
			parameterBoundsBottom.setSelectedItem(bounds.getBottom());
			Double x = bounds.getWidth() / 500;
			Double y = bounds.getHeight() / 500;
			Double resolution = x > y ? y : x;
			parameterResulotion.setSelectedItem(resolution);
			if (resolution != 0) {
				int rows = (int) Math.abs(bounds.getHeight() / resolution);
				int columns = (int) Math.abs(bounds.getWidth() / resolution);
				parameterRow.setSelectedItem(rows);
				parameterColumn.setSelectedItem(columns);
			}
		}
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

	private void registerEvents() {
		this.parameterDataset.addPropertyListener(this.propertyChangeListener);
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
	public boolean execute() {
		boolean isSuccessful = false;

		try {
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
				if (interpolationParameter.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
					((InterpolationKrigingParameter) interpolationParameter).setMean(Double.valueOf(parameterMean.getSelectedItem().toString()));
				}
				if (interpolationParameter.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
					((InterpolationKrigingParameter) interpolationParameter).setExponent((Exponent) parameterSteps.getSelectedData());
				}
				((InterpolationKrigingParameter) interpolationParameter).setNugget(Double.valueOf(parameterNugget.getSelectedItem().toString()));
			}
			Interpolator.addSteppedListener(this.stepLitener);

			DatasetVector datasetVector = null;
			if (this.parameters.getInputs().getData(INPUT_DATA).getValue() != null) {
				datasetVector = (DatasetVector) this.parameters.getInputs().getData(INPUT_DATA).getValue();
			} else {
				datasetVector = (DatasetVector) this.parameterDataset.getSelectedItem();
			}
			interpolationParameter.setBounds(datasetVector.getBounds());// // FIXME: 2017/7/20 范围问题
			Datasource targetDatasource = parameterResultDatasetName.getResultDatasource();
			String datasetName = parameterResultDatasetName.getDatasetName();
			datasetName = targetDatasource.getDatasets().getAvailableDatasetName(datasetName);
			DatasetGrid dataset = Interpolator.interpolate(interpolationParameter, datasetVector,
					parameterInterpolatorFields.getFieldName(), Double.valueOf(parameterScaling.getSelectedItem().toString()),
					targetDatasource, datasetName, (PixelFormat) parameterPixelType.getSelectedData());
			this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(dataset);
			isSuccessful = dataset != null;
			fireRunning(new RunningEvent(this, 100, "finished"));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			Interpolator.removeSteppedListener(this.stepLitener);
		}
		return isSuccessful;
	}

	@Override
	public Class<? extends IProcessLoader> getLoader() {
		return InterpolatorProcessLoader.class;
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
