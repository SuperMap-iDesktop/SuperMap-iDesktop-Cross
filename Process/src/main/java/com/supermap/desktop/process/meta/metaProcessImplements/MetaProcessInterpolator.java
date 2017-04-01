package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.*;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterSearchModeInfo;
import com.supermap.desktop.process.parameter.implement.*;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.PixelFormatProperties;

import javax.swing.*;

/**
 * Created by xie on 2017/2/16.
 */
public class MetaProcessInterpolator extends MetaProcess {
	private ParameterDatasource parameterDatasource;
	private ParameterSingleDataset parameterDataset;
	private ParameterComboBox parameterInterpolatorFields;
	private ParameterTextField parameterScaling;
	private ParameterDatasource parameterResultDatasource;
	private ParameterTextField parameterResultDatasetName;
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
			fireRunning(new RunningEvent(MetaProcessInterpolator.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
		}
	};

	public MetaProcessInterpolator(InterpolationAlgorithmType interpolationAlgorithmType) {
		this.interpolationAlgorithmType = interpolationAlgorithmType;
		parameters = new DefaultParameters();
		parameterDatasource = new ParameterDatasource();
		parameterDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		Datasource currentDatasource = null;
		if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
			currentDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		}
		parameterDatasource.setSelectedItem(currentDatasource);
		parameterDataset = new ParameterSingleDataset(DatasetType.POINT);
		parameterInterpolatorFields = new ParameterComboBox();
		parameterInterpolatorFields.setDescribe(ProcessProperties.getString("String_InterpolatorFields"));
		parameterScaling = new ParameterTextField().setDescribe(CommonProperties.getString("String_Scaling"));
		parameterScaling.setSelectedItem("1");
		parameterResultDatasource = new ParameterDatasource();
		parameterResultDatasource.setDescribe(CommonProperties.getString("String_TargetDatasource"));
		parameterResultDatasource.setSelectedItem(currentDatasource);
		parameterResultDatasetName = new ParameterTextField().setDescribe(CommonProperties.getString(CommonProperties.Label_Dataset));
		parameterResulotion = new ParameterTextField().setDescribe(CommonProperties.getString("String_Resolution"));
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
			parameters.setParameters(parameterDatasource, parameterDataset, parameterInterpolatorFields,
					parameterScaling, parameterResultDatasource, parameterScaling, parameterResultDatasource
					, parameterResultDatasetName, parameterResulotion, parameterPixelType, parameterColumn,
					parameterRow, parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom,
					searchMode, parameterPower);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			parameters.setParameters(parameterDatasource, parameterDataset, parameterInterpolatorFields,
					parameterScaling, parameterResultDatasource, parameterScaling, parameterResultDatasource
					, parameterResultDatasetName, parameterResulotion, parameterPixelType, parameterColumn,
					parameterRow, parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom,
					searchMode, parameterTension, parameterSmooth);
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.KRIGING)
				|| interpolationAlgorithmType.equals(InterpolationAlgorithmType.SimpleKRIGING)
				|| interpolationAlgorithmType.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
			parameters.setParameters(parameterDatasource, parameterDataset, parameterInterpolatorFields,
					parameterScaling, parameterResultDatasource, parameterScaling, parameterResultDatasource
					, parameterResultDatasetName, parameterResulotion, parameterPixelType, parameterColumn,
					parameterRow, parameterBoundsLeft, parameterBoundsTop, parameterBoundsRight, parameterBoundsBottom,
					searchMode, parameterVariogramMode, parameterStill, parameterAngle, parameterRange, parameterMean, parameterNugget);
		}
		processTask = new ProcessTask(this);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Interpolator");
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
			((InterpolationIDWParameter) interpolationParameter).setPower((Integer) parameterPower.getSelectedItem());
		} else if (interpolationAlgorithmType.equals(InterpolationAlgorithmType.RBF)) {
			interpolationParameter = new InterpolationRBFParameter();
			setInterpolationParameter(interpolationParameter);
			((InterpolationRBFParameter) interpolationParameter).setTension((Double) parameterTension.getSelectedItem());
			((InterpolationRBFParameter) interpolationParameter).setSmooth((Double) parameterSmooth.getSelectedItem());
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
			((InterpolationKrigingParameter) interpolationParameter).setSill((Double) parameterStill.getSelectedItem());
			((InterpolationKrigingParameter) interpolationParameter).setAngle((Double) parameterAngle.getSelectedItem());
			((InterpolationKrigingParameter) interpolationParameter).setRange((Double) parameterRange.getSelectedItem());
			((InterpolationKrigingParameter) interpolationParameter).setMean((Double) parameterMean.getSelectedItem());
			((InterpolationKrigingParameter) interpolationParameter).setNugget((Double) parameterNugget.getSelectedItem());
		}
		Interpolator.addSteppedListener(this.stepLitener);

		DatasetVector datasetVector = inputs.getData() instanceof DatasetVector ? (DatasetVector) inputs.getData() : (DatasetVector) parameterDataset.getSelectedItem();
		DatasetGrid dataset = Interpolator.interpolate(interpolationParameter, datasetVector,
				parameterInterpolatorFields.getSelectedItem().toString(), ((double) parameterScaling.getSelectedItem()),
				((Datasource) parameterResultDatasource.getSelectedItem()), parameterResultDatasetName.getSelectedItem().toString(),
				(PixelFormat) ((ParameterDataNode) parameterPixelType.getSelectedItem()).getData());
		Interpolator.removeSteppedListener(this.stepLitener);
		ProcessData processData = new ProcessData();
		processData.setData(dataset);
//		this.outputs.add(0, processData);
		fireRunning(new RunningEvent(this, 100, "finished"));
	}

	@Override
	public String getKey() {
		return MetaKeys.INTERPOLATOR;
	}

	public InterpolationAlgorithmType getInterpolationAlgorithmType() {
		return interpolationAlgorithmType;
	}

	public void setInterpolationParameter(InterpolationParameter interpolationParameter) {
		Rectangle2D bounds = new Rectangle2D();
		bounds.setLeft((Double) parameterBoundsLeft.getSelectedItem());
		bounds.setTop((Double) parameterBoundsTop.getSelectedItem());
		bounds.setRight((Double) parameterBoundsRight.getSelectedItem());
		bounds.setBottom((Double) parameterBoundsBottom.getSelectedItem());
		ParameterSearchModeInfo info = (ParameterSearchModeInfo) searchMode.getSelectedItem();
		interpolationParameter.setExpectedCount(info.expectedCount);
		interpolationParameter.setSearchMode(info.searchMode);
		interpolationParameter.setSearchRadius(info.searchRadius);
		interpolationParameter.setResolution((Double) parameterResulotion.getSelectedItem());
		interpolationParameter.setBounds(bounds);
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/interpolator.png");
	}
}
