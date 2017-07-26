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
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.EqualDatasetConstraint;
import com.supermap.desktop.process.constraint.implement.EqualDatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.implement.ParameterDatasourceConstrained;
import com.supermap.desktop.process.parameter.implement.ParameterFieldSetDialog;
import com.supermap.desktop.process.parameter.implement.ParameterLabel;
import com.supermap.desktop.process.parameter.implement.ParameterSingleDataset;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.enums.OverlayAnalystType;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/2/14.
 * 叠加分析
 */
public class MetaProcessOverlayAnalyst extends MetaProcess {
	private final static String INPUT_DATA = CommonProperties.getString("String_GroupBox_SourceData");
	private final static String OVERLAY_DATA = CommonProperties.getString("String_label_OverlayDataset");
	private final static String OUTPUT_DATA = "OverlayResult";

	private OverlayAnalystType analystType;
	private ParameterDatasourceConstrained parameterSourceDatasource = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterSourceDataset = new ParameterSingleDataset();
	private ParameterDatasourceConstrained parameterOverlayDatasource = new ParameterDatasourceConstrained();
	private ParameterSingleDataset parameterOverlayDataset = new ParameterSingleDataset(DatasetType.REGION);
	private ParameterDatasourceConstrained parameterResultDatasource = new ParameterDatasourceConstrained();
	private ParameterTextField parameterSaveDataset = new ParameterTextField();
	private ParameterFieldSetDialog parameterFieldSetDialog = new ParameterFieldSetDialog();
	private ParameterTextField parameterTolerance = new ParameterTextField();
	private ParameterLabel parameterUnit = new ParameterLabel();
//	private ParameterCheckBox parameterCheckBoxIsCompareResult = new ParameterCheckBox();

	private SteppedListener steppedListener = new SteppedListener() {
		@Override
		public void stepped(SteppedEvent steppedEvent) {
			RunningEvent event = new RunningEvent(MetaProcessOverlayAnalyst.this, steppedEvent.getPercent(), steppedEvent.getMessage());
			fireRunning(event);

			if (event.isCancel()) {
				steppedEvent.setCancel(true);
			}
		}
	};

	public PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (parameterSourceDataset.getSelectedItem() != null && evt.getNewValue() instanceof DatasetVector) {
				parameterTolerance.setSelectedItem(DatasetUtilities.getDefaultTolerance((DatasetVector) evt.getNewValue()).getNodeSnap());
				parameterUnit.setSelectedItem(LengthUnit.convertForm(((DatasetVector) evt.getNewValue()).getPrjCoordSys().getCoordUnit()));
			}
		}
	};

	public MetaProcessOverlayAnalyst(OverlayAnalystType analystType) {
		this.analystType = analystType;
		initParameters();
		initParameterLayout();
		initParameterConstraint();
		initParameterStates();
		registerEvents();
	}

	private void initParameters() {

		parameterSourceDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterOverlayDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterResultDatasource.setDescribe(CommonProperties.getString(CommonProperties.Label_Datasource));
		parameterSaveDataset.setDescribe(CommonProperties.getString(CommonProperties.Label_Dataset));
		parameterTolerance.setDescribe(CommonProperties.getString("String_Label_Tolerance"));

//		parameterCheckBoxIsCompareResult.setDescribe(CommonProperties.getString("String_CheckBox_ResultComparison"));
	}

	private void initParameterLayout() {
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
		parameterCombineResultSet.addParameters(parameterResultDatasource, parameterCombineParent
//				, parameterCheckBoxIsCompareResult);
		);
		parameters.setParameters(parameterCombineSource, parameterCombineResult, parameterCombineResultSet);
		this.getParameters().addInputParameters(INPUT_DATA, DatasetTypes.VECTOR, parameterCombineSource);
		this.getParameters().addInputParameters(OVERLAY_DATA, DatasetTypes.VECTOR, parameterCombineResult);
		this.getParameters().addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, parameterCombineResultSet);

	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(parameterSourceDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(parameterSourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasourceConstraint equalDatasourceConstraint1 = new EqualDatasourceConstraint();
		equalDatasourceConstraint1.constrained(parameterOverlayDatasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint1.constrained(parameterOverlayDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint = new EqualDatasetConstraint();
		equalDatasetConstraint.constrained(parameterSourceDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint.constrained(parameterFieldSetDialog, ParameterFieldSetDialog.SOURCE_DATASET_FIELD_NAME);

		EqualDatasetConstraint equalDatasetConstraint1 = new EqualDatasetConstraint();
		equalDatasetConstraint1.constrained(parameterOverlayDataset, ParameterSingleDataset.DATASET_FIELD_NAME);
		equalDatasetConstraint1.constrained(parameterFieldSetDialog, ParameterFieldSetDialog.RESULT_DATASET_FIELD_NAME);
	}

	private void initParameterStates() {
		String resultName = this.analystType.defaultResultName();
		parameterSaveDataset.setSelectedItem(resultName);
		if (this.analystType == OverlayAnalystType.UNION || this.analystType == OverlayAnalystType.XOR || this.analystType == OverlayAnalystType.UPDATE) {
			parameterSourceDataset.setDatasetTypes(DatasetType.REGION);
		} else {
			parameterSourceDataset.setDatasetTypes(DatasetType.POINT, DatasetType.LINE, DatasetType.REGION);
		}
		DatasetVector datasetVector = DatasetUtilities.getDefaultDatasetVector();
		if (datasetVector != null) {
			parameterSourceDatasource.setSelectedItem(datasetVector.getDatasource());
			parameterSourceDataset.setSelectedItem(datasetVector);
			parameterOverlayDatasource.setSelectedItem(datasetVector.getDatasource());
			parameterOverlayDataset.setSelectedItem(datasetVector);
			parameterResultDatasource.setSelectedItem(datasetVector.getDatasource());
			if ((this.analystType == OverlayAnalystType.UNION || this.analystType == OverlayAnalystType.XOR || this.analystType == OverlayAnalystType.UPDATE) && datasetVector.getType() == DatasetType.REGION) {
				parameterTolerance.setSelectedItem(DatasetUtilities.getDefaultTolerance(datasetVector).getNodeSnap());
				parameterUnit.setDescribe(LengthUnit.convertForm(datasetVector.getPrjCoordSys().getCoordUnit()).toString());
			}
			if (this.analystType == OverlayAnalystType.CLIP || this.analystType == OverlayAnalystType.ERASE || this.analystType == OverlayAnalystType.INTERSECT || this.analystType == OverlayAnalystType.IDENTITY) {
				parameterTolerance.setSelectedItem(DatasetUtilities.getDefaultTolerance(datasetVector).getNodeSnap());
				parameterUnit.setDescribe(LengthUnit.convertForm(datasetVector.getPrjCoordSys().getCoordUnit()).toString());
			}

		} else {
			parameterTolerance.setSelectedItem("");
			parameterUnit.setDescribe("");
		}
		if (this.analystType == OverlayAnalystType.CLIP || this.analystType == OverlayAnalystType.ERASE || this.analystType == OverlayAnalystType.UPDATE) {
			parameterFieldSetDialog.setEnabled(false);
		} else {
			parameterFieldSetDialog.setEnabled(true);
		}
	}

	private void registerEvents() {
		this.parameterSourceDataset.addPropertyListener(this.propertyChangeListener);
	}

	@Override
	public String getTitle() {
		return analystType.toString();
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
			ParameterOverlayAnalystInfo info = new ParameterOverlayAnalystInfo();
			if (parameters.getInputs().getData(INPUT_DATA) != null && parameters.getInputs().getData(INPUT_DATA).getValue() instanceof DatasetVector) {
				info.sourceDataset = (DatasetVector) parameters.getInputs().getData(INPUT_DATA).getValue();
				info.sourceDatatsource = info.sourceDataset.getDatasource();
			} else {
				info.sourceDatatsource = (Datasource) parameterSourceDatasource.getSelectedItem();
				info.sourceDataset = (DatasetVector) parameterSourceDataset.getSelectedItem();
			}

			if (parameters.getInputs().getData(OVERLAY_DATA) != null && parameters.getInputs().getData(OVERLAY_DATA).getValue() instanceof DatasetVector) {
				info.overlayAnalystDataset = (DatasetVector) parameters.getInputs().getData(OVERLAY_DATA).getValue();
				info.overlayAnalystDatasource = info.overlayAnalystDataset.getDatasource();
			} else {
				info.overlayAnalystDatasource = (Datasource) parameterOverlayDatasource.getSelectedItem();
				info.overlayAnalystDataset = (DatasetVector) parameterOverlayDataset.getSelectedItem();
			}

			if (info.sourceDataset == info.overlayAnalystDataset) {
				Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_SameDataSet_error"));
				return false;
			}

			info.targetDatasource = (Datasource) parameterResultDatasource.getSelectedItem();
			info.targetDataset = (String) parameterSaveDataset.getSelectedItem();
			OverlayAnalystParameter overlayAnalystParameter = new OverlayAnalystParameter();
			if (parameterFieldSetDialog.getSourceFieldNames() != null) {
				overlayAnalystParameter.setSourceRetainedFields(parameterFieldSetDialog.getSourceFieldNames());
				overlayAnalystParameter.setOperationRetainedFields(parameterFieldSetDialog.getResultFieldNames());
			}
			overlayAnalystParameter.setTolerance(DoubleUtilities.stringToValue(((String) parameterTolerance.getSelectedItem())));
			info.analystParameter = overlayAnalystParameter;

			if (null == info.sourceDataset || null == info.overlayAnalystDataset
					|| null == info.targetDataset) {
				return false;
			}
			if (!isSameProjection(info.sourceDataset.getPrjCoordSys(), info.overlayAnalystDataset.getPrjCoordSys())) {
				Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_PrjCoordSys_Different") + "\n" + ControlsProperties.getString("String_Parameters"));
				Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_OverlayAnalyst_Failed"), info.sourceDataset.getName() + "@" + info.sourceDataset.getDatasource().getAlias()
						, info.overlayAnalystDataset.getName() + "@" + info.overlayAnalystDataset.getDatasource().getAlias(), analystType.toString()));
				return false;
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
					isSuccessful = OverlayAnalyst.clip(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				case ERASE:
					isSuccessful = OverlayAnalyst.erase(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				case IDENTITY:
					isSuccessful = OverlayAnalyst.identity(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				case INTERSECT:
					isSuccessful = OverlayAnalyst.intersect(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				case UNION:
					isSuccessful = OverlayAnalyst.union(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				case XOR:
					isSuccessful = OverlayAnalyst.xOR(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				case UPDATE:
					isSuccessful = OverlayAnalyst.update(info.sourceDataset, info.overlayAnalystDataset, targetDataset, info.analystParameter);
					break;
				default:
					break;
			}
			fireRunning(new RunningEvent(this, 100, "finished"));
			this.parameters.getOutputs().getData(OUTPUT_DATA).setValue(targetDataset);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			OverlayAnalyst.removeSteppedListener(this.steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getKey() {
		String key = "";
		switch (analystType) {
			case CLIP:
				key = MetaKeys.OVERLAY_ANALYST_CLIP;
				break;
			case UNION:
				key = MetaKeys.OVERLAY_ANALYST_UNION;
				break;
			case ERASE:
				key = MetaKeys.OVERLAY_ANALYST_ERASE;
				break;
			case IDENTITY:
				key = MetaKeys.OVERLAY_ANALYST_IDENTITY;
				break;
			case INTERSECT:
				key = MetaKeys.OVERLAY_ANALYST_INTERSECT;
				break;
			case UPDATE:
				key = MetaKeys.OVERLAY_ANALYST_UPDATE;
				break;
			case XOR:
				key = MetaKeys.OVERLAY_ANALYST_XOR;
				break;

		}
		return key;
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


}
