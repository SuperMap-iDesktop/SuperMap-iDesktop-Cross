package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.ConceptualizationModel;
import com.supermap.analyst.spatialstatistics.DistanceMethod;
import com.supermap.analyst.spatialstatistics.PatternsParameter;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.desktop.Interface.ISmTextFieldLegit;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class ParameterPatternsParameter extends ParameterCombine {

	private String metaKeys;
	private ParameterFieldComboBox parameterAssessmentFieldComboBox = new ParameterFieldComboBox();
	private ParameterComboBox parameterComboBoxConceptModel = new ParameterComboBox();
	private ParameterComboBox parameterDistanceMethod = new ParameterComboBox();
	private ParameterNumber parameterTextFieldDistanceTolerance = new ParameterNumber();
	private ParameterNumber parameterTextFieldExponent = new ParameterNumber();
	private ParameterCheckBox parameterCheckBoxFDRAdjusted = new ParameterCheckBox();
	private ParameterFile parameterFile = new ParameterFile();
	private ParameterNumber parameterTextFieldKNeighbors = new ParameterNumber();

	private ParameterFieldComboBox parameterSelfWeightFieldComboBox = new ParameterFieldComboBox().setShowNullValue(true);

	private ParameterCheckBox parameterCheckBoxStandardization = new ParameterCheckBox();
	public static final String DATASET_FIELD_NAME = "dataset_Field";

	@ParameterField(name = DATASET_FIELD_NAME)
	private DatasetVector currentDataset;

	public ParameterPatternsParameter(String metaKeys) {
		this.metaKeys = metaKeys;
		initParameters();
		initParameterState();
		initParameterConstraint();
	}

	private void initParameters() {

		FieldType[] fieldType = {FieldType.INT16, FieldType.INT32, FieldType.INT64, FieldType.SINGLE, FieldType.DOUBLE};
		this.parameterAssessmentFieldComboBox.setFieldType(fieldType);
		this.parameterSelfWeightFieldComboBox.setFieldType(fieldType);
		this.parameterAssessmentFieldComboBox.setDescribe(ProcessProperties.getString("String_AssessmentField"));
		this.parameterComboBoxConceptModel.setDescribe(ProcessProperties.getString("String_ConceptModel"));
		this.parameterDistanceMethod.setDescribe(ProcessProperties.getString("String_DistanceMethod"));
		this.parameterTextFieldDistanceTolerance.setDescribe(ProcessProperties.getString("String_DistanceTolerance"));
		this.parameterTextFieldDistanceTolerance.setTipButtonMessage(ProcessProperties.getString("String_DistanceToleranceTip"));
		this.parameterTextFieldExponent.setDescribe(ProcessProperties.getString("String_Exponent"));
		this.parameterCheckBoxFDRAdjusted.setDescribe(ProcessProperties.getString("String_FDRAdjusted"));
		this.parameterFile.setDescribe(ProcessProperties.getString("String_Label_SWM"));
		this.parameterTextFieldKNeighbors.setMinValue(1);
		this.parameterTextFieldKNeighbors.setMaxBit(0);

		this.parameterTextFieldExponent.setMinValue(0.0);
		this.parameterTextFieldDistanceTolerance.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				try {
					if (textFieldValue.endsWith(".")) {
						return false;
					}
					Double aDouble = Double.valueOf(textFieldValue);
					if (aDouble < 0) {
						if (aDouble != -1) {
							return false;
						}
					}
				} catch (Exception e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});

		String modelName = "OpenSWMBFile";
		if (!SmFileChoose.isModuleExist(modelName)) {
			String fileFilters = SmFileChoose.createFileFilter(ProcessProperties.getString("String_SWMFilePath"), "swmb");
			SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"),
					CoreProperties.getString("String_Open"), modelName, "OpenOne");
		}
		this.parameterFile.setModuleName(modelName);
		this.parameterFile.setRequisite(true);

		this.parameterTextFieldKNeighbors.setDescribe(ProcessProperties.getString("String_KNeighbors"));
		this.parameterSelfWeightFieldComboBox.setDescribe(ProcessProperties.getString("String_SelfWeightField"));

		this.parameterComboBoxConceptModel.setItems(
				new ParameterDataNode(ProcessProperties.getString("String_FIXEDDISTANCEBAND"), ConceptualizationModel.FIXEDDISTANCEBAND),
				new ParameterDataNode(ProcessProperties.getString("String_CONTIGUITYEDGESNODE"), ConceptualizationModel.CONTIGUITYEDGESNODE),
				new ParameterDataNode(ProcessProperties.getString("String_CONTIGUITYEDGESONLY"), ConceptualizationModel.CONTIGUITYEDGESONLY),
				new ParameterDataNode(ProcessProperties.getString("String_INVERSEDISTANCE"), ConceptualizationModel.INVERSEDISTANCE),
				new ParameterDataNode(ProcessProperties.getString("String_INVERSEDISTANCESQUARED"), ConceptualizationModel.INVERSEDISTANCESQUARED),
				new ParameterDataNode(ProcessProperties.getString("String_KNEARESTNEIGHBORS"), ConceptualizationModel.KNEARESTNEIGHBORS),
				new ParameterDataNode(ProcessProperties.getString("String_SPATIALWEIGHTMATRIXFILE"), ConceptualizationModel.SPATIALWEIGHTMATRIXFILE),
				new ParameterDataNode(ProcessProperties.getString("String_ZONEOFINDIFFERENCE"), ConceptualizationModel.ZONEOFINDIFFERENCE));

		this.parameterDistanceMethod.setItems(new ParameterDataNode(ProcessProperties.getString("String_EUCLIDEAN"), DistanceMethod.EUCLIDEAN));
		//new ParameterDataNode(ProcessProperties.getString("String_MANHATTAN"), DistanceMethod.MANHATTAN) 暂不支持这种距离方式
		this.parameterCheckBoxStandardization.setDescribe(ProcessProperties.getString("String_Standardization"));


		final ParameterSwitch parameterSwitchMain = new ParameterSwitch();
		ParameterCombine parameterCombineInverseDistance = new ParameterCombine();
		parameterCombineInverseDistance.setRebuildEveryTime(true);
		parameterCombineInverseDistance.addParameters(this.parameterTextFieldDistanceTolerance, this.parameterTextFieldExponent);

		ParameterCombine parameterCombineFixedDistanceBand = new ParameterCombine();
		parameterCombineFixedDistanceBand.setRebuildEveryTime(true);
		parameterCombineFixedDistanceBand.addParameters(this.parameterTextFieldDistanceTolerance);

		ParameterCombine parameterCombineKNeighbors = new ParameterCombine();
		parameterCombineKNeighbors.addParameters(this.parameterTextFieldKNeighbors);

		ParameterCombine parameterCombineFile = new ParameterCombine();
		parameterCombineFile.addParameters(this.parameterFile);

		parameterSwitchMain.add("InverseDistance", parameterCombineInverseDistance);
		parameterSwitchMain.add("FixedDistanceBand", parameterCombineFixedDistanceBand);
		parameterSwitchMain.add("KNeighbors", parameterCombineKNeighbors);
		parameterSwitchMain.add("File", parameterCombineFile);
		parameterSwitchMain.switchParameter((IParameter) null);

		this.parameterComboBoxConceptModel.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterComboBox.comboBoxValue)) {
					ConceptualizationModel conceptualizationModel = (ConceptualizationModel) ((ParameterDataNode) evt.getNewValue()).getData();
					if (conceptualizationModel == ConceptualizationModel.INVERSEDISTANCE || conceptualizationModel == ConceptualizationModel.INVERSEDISTANCESQUARED
							|| conceptualizationModel == ConceptualizationModel.ZONEOFINDIFFERENCE) {
						parameterSwitchMain.switchParameter("InverseDistance");
					} else if (conceptualizationModel == ConceptualizationModel.FIXEDDISTANCEBAND) {
						parameterSwitchMain.switchParameter("FixedDistanceBand");
					} else if (conceptualizationModel == ConceptualizationModel.KNEARESTNEIGHBORS) {
						parameterSwitchMain.switchParameter("KNeighbors");
					} else if (conceptualizationModel == ConceptualizationModel.SPATIALWEIGHTMATRIXFILE) {
						parameterSwitchMain.switchParameter("File");
					} else {
						parameterSwitchMain.switchParameter((IParameter) null);
					}
				}
			}
		});

		this.addParameters(this.parameterAssessmentFieldComboBox, this.parameterComboBoxConceptModel);
		// 调整界面布局-yuanR2017.9.5
		parameterSwitchMain.switchParameter("FixedDistanceBand");
		this.addParameters(parameterSwitchMain, this.parameterDistanceMethod);
		if (this.metaKeys.equals(MetaKeys.HOT_SPOT_ANALYST) || this.metaKeys.equals(MetaKeys.OPTIMIZED_HOT_SPOT_ANALYST)) {
			this.addParameters(this.parameterSelfWeightFieldComboBox);
		} else {
			this.addParameters(this.parameterCheckBoxStandardization);
		}
		if (this.metaKeys.equals(MetaKeys.HOT_SPOT_ANALYST) || this.metaKeys.equals(MetaKeys.OPTIMIZED_HOT_SPOT_ANALYST) || this.metaKeys.equals(MetaKeys.CLUSTER_OUTLIER_ANALYST)) {
			this.addParameters(this.parameterCheckBoxFDRAdjusted);
		}

		this.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
	}

	private void initParameterState() {
		this.parameterComboBoxConceptModel.setSelectedItem(ConceptualizationModel.FIXEDDISTANCEBAND);
		this.parameterTextFieldDistanceTolerance.setSelectedItem("-1.0");
		this.parameterTextFieldExponent.setSelectedItem("1.0");
		this.parameterTextFieldKNeighbors.setSelectedItem("1");
	}

	public void setCurrentDataset(DatasetVector currentDataset) {
		this.currentDataset = currentDataset;
		datasetChanged();
	}

	private void datasetChanged() {
		this.parameterAssessmentFieldComboBox.setFieldName(this.currentDataset);
		this.parameterSelfWeightFieldComboBox.setFieldName(this.currentDataset);
	}

	private void initParameterConstraint() {
		this.addFieldConstraintChangedListener(new FieldConstraintChangedListener() {
			@Override
			public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
				if (event.getFieldName().equals(DATASET_FIELD_NAME)) {
					datasetChanged();
				}
			}
		});
	}

	public PatternsParameter getPatternParameter() {
		PatternsParameter patternsParameter = new PatternsParameter();
		patternsParameter.setAssessmentFieldName(this.parameterAssessmentFieldComboBox.getFieldName());
		ConceptualizationModel conceptualizationModel = (ConceptualizationModel) ((ParameterDataNode) this.parameterComboBoxConceptModel.getSelectedItem()).getData();
		patternsParameter.setConceptModel(conceptualizationModel);
		patternsParameter.setDistanceMethod((DistanceMethod) ((ParameterDataNode) this.parameterDistanceMethod.getSelectedItem()).getData());
		if (conceptualizationModel == ConceptualizationModel.INVERSEDISTANCE || conceptualizationModel == ConceptualizationModel.INVERSEDISTANCESQUARED
				|| conceptualizationModel == ConceptualizationModel.ZONEOFINDIFFERENCE) {
			patternsParameter.setDistanceTolerance(Double.valueOf(this.parameterTextFieldDistanceTolerance.getSelectedItem()));
			patternsParameter.setExponent(Double.valueOf(this.parameterTextFieldExponent.getSelectedItem()));
		} else if (conceptualizationModel == ConceptualizationModel.FIXEDDISTANCEBAND) {
			patternsParameter.setDistanceTolerance(Double.valueOf(this.parameterTextFieldDistanceTolerance.getSelectedItem()));
		} else if (conceptualizationModel == ConceptualizationModel.KNEARESTNEIGHBORS) {
			patternsParameter.setKNeighbors(Integer.valueOf(this.parameterTextFieldKNeighbors.getSelectedItem()));
		} else if (conceptualizationModel == ConceptualizationModel.SPATIALWEIGHTMATRIXFILE) {
			patternsParameter.setFilePath(this.parameterFile.getSelectedItem());
		}
		if (this.metaKeys.equals(MetaKeys.HOT_SPOT_ANALYST) || this.metaKeys.equals(MetaKeys.CLUSTER_OUTLIER_ANALYST)) {
			patternsParameter.setFDRAdjusted(Boolean.valueOf(this.parameterCheckBoxFDRAdjusted.getSelectedItem()));
		}
		if (this.metaKeys.equals(MetaKeys.HOT_SPOT_ANALYST)) {
			patternsParameter.setSelfWeightFieldName(this.parameterSelfWeightFieldComboBox.getFieldName());
		} else {
			patternsParameter.setStandardization(Boolean.valueOf((String) this.parameterCheckBoxStandardization.getSelectedItem()));
		}
		return patternsParameter;
	}

	/**
	 * 获得概念化模型参数，实现外部下拉列表项的设置-yuanR2017.8.29
	 *
	 * @return
	 */
	public ParameterComboBox getParameterComboBoxConceptModel() {
		return this.parameterComboBoxConceptModel;
	}

}
