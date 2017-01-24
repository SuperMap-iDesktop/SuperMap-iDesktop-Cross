package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.conversion.DataImport;
import com.supermap.data.conversion.ImportMode;
import com.supermap.data.conversion.ImportResult;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingSHP;
import com.supermap.data.conversion.ImportSteppedEvent;
import com.supermap.data.conversion.ImportSteppedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.IParameter;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterComboBox;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.utilities.EncodeTypeUtilities;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessImport extends MetaProcess {

	private IParameters parameters;

	public MetaProcessImport() {
		parameters = new DefaultParameters();
		ParameterFile parameterFile = new ParameterFile().setDescribe(ProcessProperties.getString("label_ChooseFile"));

		ParameterSaveDataset parameterSaveDataset = new ParameterSaveDataset();
		if (Application.getActiveApplication().getActiveDatasources().length > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
		} else if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
			parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
		}
		if (parameterSaveDataset.getResultDatasource() != null) {
			parameterSaveDataset.setDatasetName(parameterSaveDataset.getResultDatasource().getDatasets().getAvailableDatasetName("dataset"));
		}

		ParameterComboBox comboBoxEncodeType = new ParameterComboBox();
		ParameterDataNode[] parameterEncodeType = new ParameterDataNode[]{
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.NONE), EncodeType.NONE),
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.BYTE), EncodeType.BYTE),
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.INT16), EncodeType.INT16),
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.INT24), EncodeType.INT24),
				new ParameterDataNode(EncodeTypeUtilities.toString(EncodeType.INT32), EncodeType.INT32),
		};
		comboBoxEncodeType.setDescribe(ProcessProperties.getString("label_encodingType")).setItems(parameterEncodeType);
		comboBoxEncodeType.setSelectedItem(EncodeType.NONE);

		ParameterDataNode[] parameterImportMode = new ParameterDataNode[]{
				new ParameterDataNode(ProcessProperties.getString("String_FormImport_None"), ImportMode.NONE),
				new ParameterDataNode(ProcessProperties.getString("String_FormImport_Append"), ImportMode.APPEND),
				new ParameterDataNode(ProcessProperties.getString("String_FormImport_OverWrite"), ImportMode.OVERWRITE)
		};
		ParameterComboBox comboBoxImportMode = new ParameterComboBox().setDescribe(ProcessProperties.getString("Label_ImportMode")).setItems(parameterImportMode);
		comboBoxImportMode.setSelectedItem(ImportMode.NONE);
		ParameterCheckBox checkBoxCreateFieldIndex = new ParameterCheckBox().setDescribe(ProcessProperties.getString("String_FieldIndex"));
		ParameterCheckBox checkBoxCreateSpaceIndex = new ParameterCheckBox().setDescribe(ProcessProperties.getString("String_CreateSpatialIndex"));

		parameters.setParameters(new IParameter[]{
				parameterFile,
				parameterSaveDataset,
				comboBoxEncodeType,
				comboBoxImportMode,
				checkBoxCreateFieldIndex,
				checkBoxCreateSpaceIndex
		});
	}

	@Override
	public JComponent getComponent() {

		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		return "数据导入";
	}

	@Override
	public void run() {
		String filePath = (String) parameters.getParameter(0).getSelectedItem();
		String datasetName = ((ParameterSaveDataset) parameters.getParameter(1)).getDatasetName();
		Datasource datasource = ((ParameterSaveDataset) parameters.getParameter(1)).getResultDatasource();
		EncodeType data = (EncodeType) ((ParameterDataNode) parameters.getParameter(2).getSelectedItem()).getData();
		ImportMode importMode = (ImportMode) parameters.getParameter(3).getSelectedItem();
		boolean createFieldIndex = (boolean) parameters.getParameter(4).getSelectedItem();// 喵喵喵？？？
		boolean createSpaceIndex = (boolean) parameters.getParameter(5).getSelectedItem();// 喵喵喵？？？

		ImportSettingSHP importSettingSHP = new ImportSettingSHP(filePath, datasource);
		importSettingSHP.setTargetEncodeType(data);
		importSettingSHP.setImportMode(importMode);
		importSettingSHP.setTargetDatasetName(datasetName);
		DataImport dataImport = new DataImport();
		dataImport.getImportSettings().add(importSettingSHP);
		dataImport.addImportSteppedListener(new ImportSteppedListener() {
			@Override
			public void stepped(ImportSteppedEvent importSteppedEvent) {
				fireRunning(new RunningEvent(MetaProcessImport.this, importSteppedEvent.getSubPercent(), ""));
			}
		});
		ImportResult run = dataImport.run();
		ImportSetting[] succeedSettings = run.getSucceedSettings();
		Dataset dataset = succeedSettings[0].getTargetDatasource().getDatasets().get(0);
	}
}
