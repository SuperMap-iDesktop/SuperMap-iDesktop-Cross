package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.analyst.spatialanalyst.ConversionAnalyst;
import com.supermap.analyst.spatialanalyst.ConversionAnalystParameter;
import com.supermap.analyst.spatialanalyst.SmoothMethod;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
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

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Chen on 2017/6/30 0030.
 */
public class MetaProcessRasterToVector extends MetaProcess {
	private final static String INPUT_DATA = "InputData";
	private final static String OUTPUT_DATA = "ExtractResult";

	private ParameterDatasourceConstrained sourceDatasource;
	private ParameterSingleDataset sourceDataset;
	private ParameterCombine sourceData;

	private ParameterSaveDataset resultDataset;
	private ParameterComboBox comboBoxType;
	private ParameterCombine resultData;

	private ParameterComboBox comboBoxSmoothMethod;
	private ParameterNumber textFieldSmoothDegree;
	private ParameterCheckBox checkBoxThinRaster;
	private ParameterCombine vertorizeLineSetting;

	private ParameterNumber textFieldNoValue;
	private ParameterNumber textFieldNoValueTolerance;
	private ParameterTextField textFieldGridField;
	private ParameterCheckBox checkBoxChooseSpecifiedValue;
	private ParameterNumber textFieldGridValue;
	private ParameterNumber textFieldGridValueTolerance;
	private ParameterCombine gridDatasetSetting;

	private ParameterColor comboBoxBackColor;
	private ParameterNumber textFieldColorTolerance;
	private ParameterCombine imageDatasetSetting;

	public MetaProcessRasterToVector() {
		initParameters();
		initParameterConstraint();
		initParametersState();
		registerListener();
	}

	private void initParameters() {
		sourceDatasource = new ParameterDatasourceConstrained();
		sourceDatasource.setDescribe(CommonProperties.getString("String_SourceDatasource"));
		sourceDataset = new ParameterSingleDataset(DatasetType.GRID, DatasetType.IMAGE);
		sourceDataset.setDescribe(CommonProperties.getString("String_Label_Dataset"));

		resultDataset = new ParameterSaveDataset();
		this.resultDataset.setDatasourceDescribe(CommonProperties.getString("String_TargetDatasource"));
		this.resultDataset.setDatasetDescribe(CommonProperties.getString("String_TargetDataset"));
		comboBoxType = new ParameterComboBox(ProcessProperties.getString("string_label_lblDatasetType"));

		comboBoxSmoothMethod = new ParameterComboBox(CommonProperties.getString("String_SmoothMethod"));
		textFieldSmoothDegree = new ParameterNumber(CommonProperties.getString("String_Smooth"));
		checkBoxThinRaster = new ParameterCheckBox(CommonProperties.getString("String_CheckBox_IsThinRaster"));

		textFieldNoValue = new ParameterNumber(CommonProperties.getString("String_Label_NoData"));
		textFieldNoValueTolerance = new ParameterNumber(CommonProperties.getString("String_Label_NoValueTolerance"));
		textFieldGridField = new ParameterTextField(CommonProperties.getString("String_m_labelGridValueFieldText"));
		checkBoxChooseSpecifiedValue = new ParameterCheckBox(CommonProperties.getString("String_CheckBox_ChooseSpecifiedValue"));
		textFieldGridValue = new ParameterNumber(CommonProperties.getString("String_Label_GridValue"));
		textFieldGridValueTolerance = new ParameterNumber(CommonProperties.getString("String_Label_GridValueTolerance"));

		comboBoxBackColor = new ParameterColor(CommonProperties.getString("String_Label_BackColor"));
		textFieldColorTolerance = new ParameterNumber(CommonProperties.getString("String_Label_BackColoTolerance"));


		sourceData = new ParameterCombine();
		sourceData.setDescribe(CommonProperties.getString("String_GroupBox_SourceData"));
		sourceData.addParameters(sourceDatasource, sourceDataset);

		resultData = new ParameterCombine();
		resultData.setDescribe(CommonProperties.getString("String_GroupBox_ResultData"));
		resultData.addParameters(resultDataset, comboBoxType);

		vertorizeLineSetting = new ParameterCombine();
		vertorizeLineSetting.setDescribe(CommonProperties.getString("String_GroupBox_VertorizeLineSetting"));
		vertorizeLineSetting.addParameters(comboBoxSmoothMethod, textFieldSmoothDegree, checkBoxThinRaster);

		gridDatasetSetting = new ParameterCombine();
		gridDatasetSetting.setDescribe(CommonProperties.getString("String_GroupBox_GridDatasetSetting"));
		gridDatasetSetting.addParameters(textFieldNoValue, textFieldNoValueTolerance, textFieldGridField, checkBoxChooseSpecifiedValue, textFieldGridValue, textFieldGridValueTolerance);

		imageDatasetSetting = new ParameterCombine();
		imageDatasetSetting.setDescribe(CommonProperties.getString("String_GroupBox_ImageDatasetSetting"));
		imageDatasetSetting.addParameters(comboBoxBackColor, textFieldColorTolerance);

		this.parameters.setParameters(sourceData, vertorizeLineSetting, gridDatasetSetting, imageDatasetSetting, resultData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.GRID, sourceData);
		this.parameters.addInputParameters(INPUT_DATA, DatasetTypes.IMAGE, sourceData);
		this.parameters.addOutputParameters(OUTPUT_DATA, DatasetTypes.VECTOR, resultData);
	}

	private void initParametersState() {
		Dataset datasetGrid = DatasetUtilities.getDefaultDataset(DatasetType.IMAGE,DatasetType.GRID);
		if (datasetGrid != null) {
			sourceDatasource.setSelectedItem(datasetGrid.getDatasource());
			sourceDataset.setSelectedItem(datasetGrid);
			//System.out.println(datasetGrid.getName());
		}

		resultDataset.setSelectedItem("result_gridToVector");
		comboBoxType.setItems(new ParameterDataNode(CommonProperties.getString("String_Item_Point"), DatasetType.POINT),
				new ParameterDataNode(CommonProperties.getString("String_Item_Line"), DatasetType.LINE),
				new ParameterDataNode(CommonProperties.getString("String_Item_Region"), DatasetType.REGION));

		comboBoxSmoothMethod.setItems(new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_NONE"), SmoothMethod.NONE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_BSLine"), SmoothMethod.BSPLINE),
				new ParameterDataNode(CommonProperties.getString("String_SmoothMothod_POLISH"), SmoothMethod.POLISH));
		textFieldSmoothDegree.setSelectedItem("2");
		textFieldSmoothDegree.setMinValue(2);
		textFieldSmoothDegree.setMaxValue(10);
		textFieldSmoothDegree.setIncludeMax(true);
		textFieldSmoothDegree.setIsIncludeMin(true);
		checkBoxThinRaster.setSelectedItem(true);
		vertorizeLineSetting.setEnabled(comboBoxType.getSelectedData() == DatasetType.LINE);

		textFieldNoValue.setSelectedItem("-9999");
		textFieldNoValueTolerance.setSelectedItem("0");
		textFieldNoValueTolerance.setMinValue(0);
		textFieldNoValueTolerance.setIsIncludeMin(true);
		textFieldGridField.setSelectedItem("value");
		textFieldGridValue.setSelectedItem("0");
		textFieldGridValueTolerance.setSelectedItem("0");
		textFieldGridValueTolerance.setMinValue(0);
		textFieldGridValueTolerance.setIsIncludeMin(true);
		gridDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetGrid);
		comboBoxBackColor.setSelectedItem(Color.WHITE);

		textFieldColorTolerance.setSelectedItem("0");
		textFieldColorTolerance.setMinValue(0);
		textFieldColorTolerance.setMaxValue(255);
		textFieldColorTolerance.setIsIncludeMin(true);
		textFieldColorTolerance.setIncludeMax(true);
		imageDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetImage);
	}

	private void initParameterConstraint() {
		EqualDatasourceConstraint equalDatasourceConstraint = new EqualDatasourceConstraint();
		equalDatasourceConstraint.constrained(sourceDatasource, ParameterDatasourceConstrained.DATASOURCE_FIELD_NAME);
		equalDatasourceConstraint.constrained(sourceDataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
		DatasourceConstraint.getInstance().constrained(resultDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);
	}

	private void registerListener() {
		sourceDataset.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				gridDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetGrid);
				imageDatasetSetting.setEnabled(sourceDataset.getSelectedItem() instanceof DatasetImage);
				textFieldGridValue.setEnabled(false);
				textFieldGridValueTolerance.setEnabled(false);
				if (sourceDataset.getSelectedItem() instanceof DatasetGrid){
					textFieldNoValue.setSelectedItem(((DatasetGrid) sourceDataset.getSelectedItem()).getNoValue());
				}else if (sourceDataset.getSelectedItem() instanceof DatasetImage){
					textFieldNoValue.setSelectedItem("16777215");
				}
			}
		});
		comboBoxType.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				vertorizeLineSetting.setEnabled(comboBoxType.getSelectedData() == DatasetType.LINE);
				if(vertorizeLineSetting.isEnabled()){
					textFieldSmoothDegree.setEnabled(false);
				}
			}
		});
		comboBoxSmoothMethod.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				textFieldSmoothDegree.setEnabled(comboBoxSmoothMethod.getSelectedData() != SmoothMethod.NONE);
			}
		});
		checkBoxChooseSpecifiedValue.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (checkBoxChooseSpecifiedValue.isEnabled && checkBoxChooseSpecifiedValue.getSelectedItem().equals("true")) {
					textFieldGridValue.setEnabled(true);
					textFieldGridValueTolerance.setEnabled(true);
				} else {
					textFieldGridValue.setEnabled(false);
					textFieldGridValueTolerance.setEnabled(false);
				}
			}
		});
	}

	@Override
	public boolean execute() {
		boolean isSuccessful = false;

		try {

			fireRunning(new RunningEvent(MetaProcessRasterToVector.this, 0, "start"));
			ConversionAnalystParameter analystParameter = new ConversionAnalystParameter();

			String datasetName = resultDataset.getDatasetName();
			datasetName = resultDataset.getResultDatasource().getDatasets().getAvailableDatasetName(datasetName);
			if (parameters.getInputs().getData(INPUT_DATA).getValue()!=null){
				analystParameter.setSourceDataset((Dataset) parameters.getInputs().getData(INPUT_DATA).getValue());
			}else {
				analystParameter.setSourceDataset(sourceDataset.getSelectedDataset());
			}
			analystParameter.setTargetDatasource(resultDataset.getResultDatasource());
			analystParameter.setTargetDatasetName(datasetName);
			analystParameter.setTargetDatasetType((DatasetType) comboBoxType.getSelectedData());
			analystParameter.setValueFieldName("GridValue");      // 栅格转为矢量字段名是必须设置的，但是.NET那边只针对GRID数据设置
			                                                      //  没有针对影像数据设置

			if (vertorizeLineSetting.isEnabled) {
				analystParameter.setSmoothMethod((SmoothMethod) comboBoxSmoothMethod.getSelectedData());
				if (checkBoxThinRaster.getSelectedItem().equals("false")){
					analystParameter.setThinRaster(false);
				}else if (checkBoxThinRaster.getSelectedItem().equals("true")){
					analystParameter.setThinRaster(true);
				}
				if (textFieldSmoothDegree.isEnabled() && Integer.valueOf((String) textFieldSmoothDegree.getSelectedItem()) >= 2 && Integer.valueOf((String) textFieldSmoothDegree.getSelectedItem()) <= 10) {
					analystParameter.setSmoothDegree(Integer.valueOf((String) textFieldSmoothDegree.getSelectedItem()));
				}
			}
			if (gridDatasetSetting.isEnabled()) {
				analystParameter.setBackOrNoValue(Math.round( Double.valueOf(textFieldNoValue.getSelectedItem().toString())));
				analystParameter.setBackOrNoValueTolerance(Double.valueOf(textFieldNoValueTolerance.getSelectedItem().toString()));
				analystParameter.setValueFieldName(textFieldGridField.getSelectedItem().toString());
				if (textFieldGridValue.isEnabled()) {
					analystParameter.setSpecifiedValue((long) Integer.valueOf(textFieldGridValue.getSelectedItem().toString()));
					analystParameter.setSpecifiedValueTolerance(Double.valueOf(textFieldGridValueTolerance.getSelectedItem().toString()));
				}
			}
			if (imageDatasetSetting.isEnabled()) {
				if (comboBoxBackColor.getSelectedItem()==null){
					Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_GridToVector_NotSetColor"));
				}else {
					System.out.println((long) ((Color) comboBoxBackColor.getSelectedItem()).getRGB());
					analystParameter.setBackOrNoValue((long) ((Color) comboBoxBackColor.getSelectedItem()).getRGB());
				}
				analystParameter.setBackOrNoValueTolerance(Double.valueOf(textFieldColorTolerance.getSelectedItem().toString()));
			}

			ConversionAnalyst.addSteppedListener(steppedListener);

			DatasetVector result = ConversionAnalyst.rasterToVector(analystParameter);
			this.getParameters().getOutputs().getData(OUTPUT_DATA).setValue(result);
			isSuccessful = result != null;

			fireRunning(new RunningEvent(MetaProcessRasterToVector.this, 100, "finished"));
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
		return MetaKeys.GRIDTOVECTOR;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_Form_GridToVector");
	}
}
