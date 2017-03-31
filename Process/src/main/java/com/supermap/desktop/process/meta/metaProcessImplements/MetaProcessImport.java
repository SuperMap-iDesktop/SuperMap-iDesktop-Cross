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
import com.supermap.desktop.process.constraint.implement.DatasourceConstraint;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterCheckBox;
import com.supermap.desktop.process.parameter.implement.ParameterEnum;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.process.parameter.implement.ParameterSaveDataset;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.EncodeTypeUtilities;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessImport extends MetaProcess {

    ParameterFile parameterImportFile;
    ParameterSaveDataset parameterSaveDataset;
    ParameterEnum comboBoxEncodeType;
    ParameterEnum comboBoxImportMode;
    ParameterCheckBox checkBoxCreateFieldIndex;
    ParameterCheckBox checkBoxCreateSpaceIndex;

    public MetaProcessImport() {
        parameters = new DefaultParameters();
        parameterImportFile = new ParameterFile().setDescribe(ProcessProperties.getString("label_ChooseFile"));

        parameterSaveDataset = new ParameterSaveDataset();
        if (Application.getActiveApplication().getActiveDatasources().length > 0) {
            parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
        } else if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
            parameterSaveDataset.setResultDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
        }
        parameterSaveDataset.setDatasetName("RoadLine");
	    DatasourceConstraint.getInstance().constrained(parameterSaveDataset, ParameterSaveDataset.DATASOURCE_FIELD_NAME);

        String[] encodingValue = new String[]{"NONE", "BYTE", "INT16", "INT24", "INT32"};
        String[] encoding = new String[]{
                EncodeTypeUtilities.toString(EncodeType.NONE),
                EncodeTypeUtilities.toString(EncodeType.BYTE),
                EncodeTypeUtilities.toString(EncodeType.INT16),
                EncodeTypeUtilities.toString(EncodeType.INT24),
                EncodeTypeUtilities.toString(EncodeType.INT32)
        };
        comboBoxEncodeType = new ParameterEnum(new EnumParser(EncodeType.class, encodingValue, encoding)).setDescribe(ProcessProperties.getString("label_encodingType"));
        comboBoxEncodeType.setSelectedItem(EncodeType.NONE);

        String[] importModel = new String[]{
                ProcessProperties.getString("String_FormImport_None"),
                ProcessProperties.getString("String_FormImport_Append"),
                ProcessProperties.getString("String_FormImport_OverWrite")
        };
        String[] importModelValue = new String[]{"NONE", "APPEND", "OVERWRITE"};
        comboBoxImportMode = new ParameterEnum(new EnumParser(ImportMode.class, importModelValue, importModel)).setDescribe(ProcessProperties.getString("Label_ImportMode"));
        comboBoxImportMode.setSelectedItem(ImportMode.NONE);
        checkBoxCreateFieldIndex = new ParameterCheckBox(ProcessProperties.getString("String_FieldIndex"));
        checkBoxCreateSpaceIndex = new ParameterCheckBox(ProcessProperties.getString("String_CreateSpatialIndex"));

        parameters.setParameters(
                parameterImportFile,
                parameterSaveDataset,
                comboBoxEncodeType,
                comboBoxImportMode,
                checkBoxCreateFieldIndex,
                checkBoxCreateSpaceIndex
        );
        processTask = new ProcessTask(this);
    }

    @Override
    public IParameterPanel getComponent() {
        return parameters.getPanel();
    }

    @Override
    public String getTitle() {
        return ProcessProperties.getString("String_Import");
    }

    @Override
    public void run() {
        String filePath = (String) parameterImportFile.getSelectedItem();
        fireRunning(new RunningEvent(this, 0, "start"));
        String datasetName = parameterSaveDataset.getDatasetName();
        Datasource datasource = parameterSaveDataset.getResultDatasource();
        EncodeType data = (EncodeType) comboBoxEncodeType.getSelectedItem();
        ImportMode importMode = (ImportMode) comboBoxImportMode.getSelectedItem();

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
        final Dataset dataset = succeedSettings[0].getTargetDatasource().getDatasets().get(succeedSettings[0].getTargetDatasetName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UICommonToolkit.refreshSelectedDatasourceNode(dataset.getDatasource().getAlias());
            }
        });
        ProcessData processData = new ProcessData();

        processData.setData(dataset);
        outPuts.add(0, processData);
        fireRunning(new RunningEvent(this, 100, "finished"));
    }

    @Override
    public String getKey() {
        return MetaKeys.IMPORT;
    }

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/import.png");
	}
}
