package com.supermap.desktop.process.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.ConceptualizationModel;
import com.supermap.analyst.spatialstatistics.DistanceMethod;
import com.supermap.analyst.spatialstatistics.PatternsParameter;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedListener;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterCombine;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.implement.ParameterNumber;
import com.supermap.desktop.process.parameter.implement.ParameterSwitch;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;

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

	private ParameterFieldComboBox parameterSelfWeightFieldComboBox = new ParameterFieldComboBox();

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
		parameterAssessmentFieldComboBox.setFieldType(fieldType);
		parameterSelfWeightFieldComboBox.setFieldType(fieldType);
		parameterAssessmentFieldComboBox.setDescribe(ProcessProperties.getString("String_AssessmentField"));
		parameterComboBoxConceptModel.setDescribe(ProcessProperties.getString("String_ConceptModel"));
		parameterDistanceMethod.setDescribe(ProcessProperties.getString("String_DistanceMethod"));
		parameterTextFieldDistanceTolerance.setDescribe(ProcessProperties.getString("String_DistanceTolerance"));
		parameterTextFieldDistanceTolerance.setToolTip(ProcessProperties.getString("String_DistanceTolerance") + " {-1, [0, +∞) }");
		parameterTextFieldExponent.setDescribe(ProcessProperties.getString("String_Exponent"));
		parameterCheckBoxFDRAdjusted.setDescribe(ProcessProperties.getString("String_FDRAdjusted"));
		parameterFile.setDescribe(ProcessProperties.getString("String_Label_SWM"));
		parameterTextFieldKNeighbors.setMinValue(1);
		parameterTextFieldKNeighbors.setMaxBit(0);

		parameterTextFieldExponent.setMinValue(0.0);
		parameterTextFieldDistanceTolerance.setSmTextFieldLegit(new ISmTextFieldLegit() {
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

		String moduleName = "swmb";
		if (!SmFileChoose.isModuleExist(moduleName)) {
			String fileFilters = SmFileChoose.bulidFileFilters(
					SmFileChoose.createFileFilter(ProcessProperties.getString("String_SWMFilePath"), "swmb"));
			SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
					ProcessProperties.getString("String_SWMFile"), moduleName, "OpenOne");
		}
		SmFileChoose fileChoose = new SmFileChoose(moduleName);
		parameterFile.setFileChoose(fileChoose);
		parameterTextFieldKNeighbors.setDescribe(ProcessProperties.getString("String_KNeighbors"));
		parameterSelfWeightFieldComboBox.setDescribe(ProcessProperties.getString("String_SelfWeightField"));

		parameterComboBoxConceptModel.setItems(
				new ParameterDataNode(ProcessProperties.getString("String_FIXEDDISTANCEBAND"), ConceptualizationModel.FIXEDDISTANCEBAND),
				new ParameterDataNode(ProcessProperties.getString("String_CONTIGUITYEDGESNODE"), ConceptualizationModel.CONTIGUITYEDGESNODE),
				new ParameterDataNode(ProcessProperties.getString("String_CONTIGUITYEDGESONLY"), ConceptualizationModel.CONTIGUITYEDGESONLY),
				new ParameterDataNode(ProcessProperties.getString("String_INVERSEDISTANCE"), ConceptualizationModel.INVERSEDISTANCE),
				new ParameterDataNode(ProcessProperties.getString("String_INVERSEDISTANCESQUARED"), ConceptualizationModel.INVERSEDISTANCESQUARED),
				new ParameterDataNode(ProcessProperties.getString("String_KNEARESTNEIGHBORS"), ConceptualizationModel.KNEARESTNEIGHBORS),
				new ParameterDataNode(ProcessProperties.getString("String_SPATIALWEIGHTMATRIXFILE"), ConceptualizationModel.SPATIALWEIGHTMATRIXFILE),
				new ParameterDataNode(ProcessProperties.getString("String_ZONEOFINDIFFERENCE"), ConceptualizationModel.ZONEOFINDIFFERENCE));

		parameterDistanceMethod.setItems(new ParameterDataNode(ProcessProperties.getString("String_EUCLIDEAN"), DistanceMethod.EUCLIDEAN));
		//new ParameterDataNode(ProcessProperties.getString("String_MANHATTAN"), DistanceMethod.MANHATTAN) 暂不支持这种距离方式
		parameterCheckBoxStandardization.setDescribe(ProcessProperties.getString("String_Standardization"));


		final ParameterSwitch parameterSwitchMain = new ParameterSwitch();
		ParameterCombine parameterCombineInverseDistance = new ParameterCombine();
		parameterCombineInverseDistance.setRebuildEveryTime(true);
		parameterCombineInverseDistance.addParameters(parameterTextFieldDistanceTolerance, parameterTextFieldExponent);

		ParameterCombine parameterCombineFixedDistanceBand = new ParameterCombine();
		parameterCombineFixedDistanceBand.setRebuildEveryTime(true);
		parameterCombineFixedDistanceBand.addParameters(parameterTextFieldDistanceTolerance);

		ParameterCombine parameterCombineKNeighbors = new ParameterCombine();
		parameterCombineKNeighbors.addParameters(parameterTextFieldKNeighbors);

		ParameterCombine parameterCombineFile = new ParameterCombine();
		parameterCombineFile.addParameters(parameterFile);

		parameterSwitchMain.add("InverseDistance", parameterCombineInverseDistance);
		parameterSwitchMain.add("FixedDistanceBand", parameterCombineFixedDistanceBand);
		parameterSwitchMain.add("KNeighbors", parameterCombineKNeighbors);
		parameterSwitchMain.add("File", parameterCombineFile);
		parameterSwitchMain.switchParameter((IParameter) null);

		parameterComboBoxConceptModel.addPropertyListener(new PropertyChangeListener() {
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

		this.addParameters(parameterAssessmentFieldComboBox, parameterComboBoxConceptModel, parameterDistanceMethod);
		if (metaKeys.equals(MetaKeys.hotSpotAnalyst) || metaKeys.equals(MetaKeys.optimizedHotSpotAnalyst)) {
			this.addParameters(parameterSelfWeightFieldComboBox);
		} else {
			this.addParameters(parameterCheckBoxStandardization);
		}
		if (metaKeys.equals(MetaKeys.hotSpotAnalyst) || metaKeys.equals(MetaKeys.optimizedHotSpotAnalyst) || metaKeys.equals(MetaKeys.clusterOutlierAnalyst)) {
			this.addParameters(parameterCheckBoxFDRAdjusted);
		}
		this.addParameters(parameterSwitchMain);
		this.setDescribe(CommonProperties.getString("String_GroupBox_ParamSetting"));
	}

	private void initParameterState() {
		parameterTextFieldDistanceTolerance.setSelectedItem("-1.0");
		parameterTextFieldExponent.setSelectedItem("1.0");
		parameterTextFieldKNeighbors.setSelectedItem("1");
	}

	public void setCurrentDataset(DatasetVector currentDataset) {
		this.currentDataset = currentDataset;
		datasetChanged();
	}

	private void datasetChanged() {
		parameterAssessmentFieldComboBox.setDataset(currentDataset);
		parameterSelfWeightFieldComboBox.setDataset(currentDataset);
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
		patternsParameter.setAssessmentFieldName(parameterAssessmentFieldComboBox.getFieldName());
		ConceptualizationModel conceptualizationModel = (ConceptualizationModel) ((ParameterDataNode) parameterComboBoxConceptModel.getSelectedItem()).getData();
		patternsParameter.setConceptModel(conceptualizationModel);
		patternsParameter.setDistanceMethod((DistanceMethod) ((ParameterDataNode) parameterDistanceMethod.getSelectedItem()).getData());
		if (conceptualizationModel == ConceptualizationModel.INVERSEDISTANCE || conceptualizationModel == ConceptualizationModel.INVERSEDISTANCESQUARED
				|| conceptualizationModel == ConceptualizationModel.ZONEOFINDIFFERENCE) {
			patternsParameter.setDistanceTolerance(Double.valueOf((String) parameterTextFieldDistanceTolerance.getSelectedItem()));
			patternsParameter.setExponent(Double.valueOf((String) parameterTextFieldExponent.getSelectedItem()));
		} else if (conceptualizationModel == ConceptualizationModel.FIXEDDISTANCEBAND) {
			patternsParameter.setDistanceTolerance(Double.valueOf((String) parameterTextFieldDistanceTolerance.getSelectedItem()));
		} else if (conceptualizationModel == ConceptualizationModel.KNEARESTNEIGHBORS) {
			patternsParameter.setKNeighbors(Integer.valueOf((String) parameterTextFieldKNeighbors.getSelectedItem()));
		} else if (conceptualizationModel == ConceptualizationModel.SPATIALWEIGHTMATRIXFILE) {
			patternsParameter.setFilePath((String) parameterFile.getSelectedItem());
		}
		if (metaKeys.equals(MetaKeys.hotSpotAnalyst) || metaKeys.equals(MetaKeys.clusterOutlierAnalyst)) {
			patternsParameter.setFDRAdjusted(Boolean.valueOf((String) parameterCheckBoxFDRAdjusted.getSelectedItem()));
		}
		if (metaKeys.equals(MetaKeys.hotSpotAnalyst)) {
			patternsParameter.setSelfWeightFieldName(parameterSelfWeightFieldComboBox.getFieldName());
		} else {
			patternsParameter.setStandardization(Boolean.valueOf((String) parameterCheckBoxStandardization.getSelectedItem()));
		}
		return patternsParameter;
	}
}
