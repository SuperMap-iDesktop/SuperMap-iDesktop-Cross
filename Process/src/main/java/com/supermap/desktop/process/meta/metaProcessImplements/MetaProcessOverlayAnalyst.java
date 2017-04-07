package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.SteppedEvent;
import com.supermap.data.SteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterFieldSetDialog;
import com.supermap.desktop.process.parameter.implement.ParameterLabel;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DataType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.enums.OverlayAnalystType;
import com.supermap.desktop.utilities.DoubleUtilities;

import javax.swing.*;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/2/14.
 * 叠加分析
 */
public class MetaProcessOverlayAnalyst extends MetaProcess {
	private final static String INPUT_SOURCE_DATASET = "SourceDataset";
	private final static String OUTPUT_DATASET = "OverlayAnalyst";

	private OverlayAnalystType analystType;
	private ParameterDatasource parameterSourceDatasource = new ParameterDatasource();
	private ParameterSingleDataset parameterSourceDataset = new ParameterSingleDataset(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
	private ParameterDatasource parameterOverlayDatasource = new ParameterDatasource();
	private ParameterSingleDataset parameterOverlayDataset = new ParameterSingleDataset(DatasetType.REGION);
	private ParameterDatasource parameterResultDatasource = new ParameterDatasource();
	private ParameterTextField parameterSaveDataset = new ParameterTextField();
	private ParameterFieldSetDialog parameterFieldSetDialog = new ParameterFieldSetDialog();
	private ParameterTextField parameterTolerance = new ParameterTextField();
	private ParameterLabel parameterUnit = new ParameterLabel();
	private ParameterCheckBox parameterCheckBoxIsCompareResult = new ParameterCheckBox();

	private SteppedListener steppedListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			fireRunning(new RunningEvent(MetaProcessOverlayAnalyst.this, steppedEvent.getPercent(), steppedEvent.getMessage()));
		}
	};

	public MetaProcessOverlayAnalyst(OverlayAnalystType analystType) {
		this.analystType = analystType;
		initParameters();
		initParameterLayout();
		initParameterConstraint();
		initParameterStates();
	}

	private void initParameters() {
		this.inputs.addData(INPUT_SOURCE_DATASET, DataType.DATASET_POINT | DataType.DATASET_LINE | DataType.DATASET_REGION);
		this.outputs.addData(OUTPUT_DATASET, DataType.DATASET_POINT | DataType.DATASET_LINE | DataType.DATASET_REGION);
		parameterSourceDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterOverlayDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterResultDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterSaveDataset.setDescribe(CommonProperties.getString(CommonProperties.Label_Dataset));
		parameterTolerance.setDescribe(CommonProperties.getString("String_Label_Tolerance"));
		parameterUnit.setDescribe(CommonProperties.getString("String_DistanceUnit_Meter"));//单位和数据集有关系
		parameterCheckBoxIsCompareResult.setDescribe(CommonProperties.getString("String_CheckBox_ResultComparison"));
	}

	private void initParameterLayout() {
		parameters = new DefaultParameters();
		ParameterCombine parameterCombineSource = new ParameterCombine();
		parameterCombineSource.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		parameterCombineSource.addParameters(parameterSourceDatasource, parameterSourceDataset);

		ParameterCombine parameterCombineResult = new ParameterCombine();
		parameterCombineResult.setDescribe(CommonProperties.getString("String_GroupBox_OverlayDataset"));
		parameterCombineResult.addParameters(parameterOverlayDatasource, parameterOverlayDataset);

		ParameterCombine parameterCombineResultSet = new ParameterCombine();
		parameterCombineResultSet.setDescribe(CommonProperties.getString("String_ResultSet"));
		ParameterCombine parameterCombineParent = new ParameterCombine(ParameterCombine.HORIZONTAL);
		parameterCombineParent.addParameters(
				new ParameterCombine().addParameters(parameterSaveDataset, parameterTolerance),
				new ParameterCombine().addParameters(parameterFieldSetDialog, parameterUnit));
		parameterCombineParent.setWeightIndex(0);
		parameterCombineResultSet.addParameters(parameterResultDatasource, parameterCombineParent, parameterCheckBoxIsCompareResult);

		parameters.setParameters(parameterCombineSource, parameterCombineResult, parameterCombineResultSet);

	}

	private void initParameterConstraint() {
		DatasourceConstraint.getInstance().constrained(parameterSourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterOverlayDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(parameterResultDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(parameterSourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterSourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint1 = new EqualDatasourceConstraint();
		equalDatasourceConstraint1.constrained(parameterOverlayDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint1.constrained(parameterOverlayDataset, ParameterDatasource.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(parameterSourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterFieldSetDialog, ParameterFieldSetDialog.SOURCE_DATASET_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint1 = new EqualDatasetConstraint();
		equalDatasetConstraint1.constrained(parameterOverlayDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterFieldSetDialog, ParameterFieldSetDialog.RESULT_DATASET_FIELD_NAME);
	}

	private void initParameterStates() {
		parameterSaveDataset.setSelectedItem("OverlayAnalystDataset");
		parameterTolerance.setSelectedItem("0");
	}

	public OverlayAnalystType getAnalystType() {
		return analystType;
	}

	@Override
	public String getTitle() {
		String title = "";
		switch (analystType) {
			case CLIP:
				title = ControlsProperties.getString("String_OverlayAnalyst_CLIP");
				break;
			case ERASE:
				title = ControlsProperties.getString("String_OverlayAnalyst_ERASE");
				break;
			case IDENTITY:
				title = ControlsProperties.getString("String_OverlayAnalyst_IDENTITY");
				break;
			case INTERSECT:
				title = ControlsProperties.getString("String_OverlayAnalyst_INTERSECT");
				break;
			case UNION:
				title = ControlsProperties.getString("String_OverlayAnalyst_UNION");
				break;
			case XOR:
				title = ControlsProperties.getString("String_OverlayAnalyst_XOR");
				break;
			case UPDATE:
				title = ControlsProperties.getString("String_OverlayAnalyst_UPDATE");
				break;
			default:
				break;
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
		ParameterOverlayAnalystInfo info = new ParameterOverlayAnalystInfo();
		if (inputs.getData(INPUT_SOURCE_DATASET) != null && inputs.getData(INPUT_SOURCE_DATASET).getValue() instanceof DatasetVector) {
			info.sourceDatatsource = ((DatasetVector) inputs.getData(INPUT_SOURCE_DATASET).getValue()).getDatasource();
			info.sourceDataset = ((DatasetVector) inputs.getData(INPUT_SOURCE_DATASET).getValue());
		} else {
			info.sourceDatatsource = (Datasource) parameterSourceDatasource.getSelectedItem();
			info.sourceDataset = (DatasetVector) parameterSourceDataset.getSelectedItem();
		}
		info.overlayAnalystDatasource = (Datasource) parameterOverlayDatasource.getSelectedItem();
		info.overlayAnalystDataset = (DatasetVector) parameterOverlayDataset.getSelectedItem();
		info.targetDatasource = (Datasource) parameterResultDatasource.getSelectedItem();
		info.targetDataset = (String) parameterSaveDataset.getSelectedItem();
		OverlayAnalystParameter overlayAnalystParameter = new OverlayAnalystParameter();
		overlayAnalystParameter.setSourceRetainedFields(parameterFieldSetDialog.getSourceFieldNames());
		overlayAnalystParameter.setOperationRetainedFields(parameterFieldSetDialog.getResultFieldNames());
		overlayAnalystParameter.setTolerance(DoubleUtilities.stringToValue(((String) parameterTolerance.getSelectedItem())));
		info.analystParameter = overlayAnalystParameter;

		if (null == info.sourceDataset || null == info.overlayAnalystDataset
				|| null == info.targetDataset) {
			return;
		}
		if (!isSameProjection(info.sourceDataset.getPrjCoordSys(), info.overlayAnalystDataset.getPrjCoordSys())) {
			Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_PrjCoordSys_Different") + "\n" + ControlsProperties.getString("String_Parameters"));
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_OverlayAnalyst_Failed"), info.sourceDataset.getName() + "@" + info.sourceDataset.getDatasource().getAlias()
					, info.overlayAnalystDataset.getName() + "@" + info.overlayAnalystDataset.getDatasource().getAlias(), analystType.toString()));
			return;
		}
		OverlayAnalyst.addSteppedListener(this.steppedListener);
		DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
		datasetVectorInfo.setType(info.sourceDataset.getType());
		datasetVectorInfo.setEncodeType(info.sourceDataset.getEncodeType());
		// 名称合法时可以设置名称
		datasetVectorInfo.setName(info.targetDatasource.getDatasets().getAvailableDatasetName(info.targetDataset));
		DatasetVector targetDataset = info.targetDatasource.getDatasets().create(datasetVectorInfo);
		targetDataset.setPrjCoordSys(info.sourceDataset.getPrjCoordSys());

		switch (analystType) {
			case CLIP:
				OverlayAnalyst.clip(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			case ERASE:
				OverlayAnalyst.erase(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			case IDENTITY:
				OverlayAnalyst.identity(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			case INTERSECT:
				OverlayAnalyst.intersect(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			case UNION:
				OverlayAnalyst.union(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			case XOR:
				OverlayAnalyst.xOR(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			case UPDATE:
				OverlayAnalyst.update(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
				break;
			default:
				break;
		}
		OverlayAnalyst.removeSteppedListener(this.steppedListener);
		outputs.getData(OUTPUT_DATASET).setValue(targetDataset);
		fireRunning(new RunningEvent(this, 100, "finished"));
		setFinished(true);
//		ProcessData processData = new ProcessData();
//		processData.setData(info.sourceDataset);
//		outPuts.add(0, processData);
	}

	@Override
	public String getKey() {
		return MetaKeys.OVERLAY_ANALYST;
	}

	private boolean isSameProjection(PrjCoordSys prjCoordSys, PrjCoordSys prjCoordSys1) {
		if (prjCoordSys.getType() != prjCoordSys1.getType()) {
			return false;
		}
		if (prjCoordSys.getGeoCoordSys() == prjCoordSys1.getGeoCoordSys()) {
			return true;
		}
		if (prjCoordSys.getGeoCoordSys() == null || prjCoordSys1.getGeoCoordSys() == null) {
			return false;
		}
		if (prjCoordSys.getGeoCoordSys().getType() != prjCoordSys1.getGeoCoordSys().getType()) {
			return false;
		}
		return true;
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/OverlayAnalyst.png");
	}


}
