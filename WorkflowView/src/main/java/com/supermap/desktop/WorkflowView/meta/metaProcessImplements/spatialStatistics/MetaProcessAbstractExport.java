package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.data.Dataset;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.meta.MetaProcess;
import com.supermap.desktop.WorkflowView.meta.dataconversion.ExportSettingUtilities;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.interfaces.datas.types.BasicTypes;
import com.supermap.desktop.process.parameter.ipls.*;
import com.supermap.desktop.properties.CommonProperties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/6/29.
 */
public class MetaProcessAbstractExport extends MetaProcess {
	protected final static String INPUT_DATA = "SourceDataset";
	protected final static String OUTPUT_DATA = "ExportResult";
	protected ParameterDatasetChooser chooseDataset;
	protected ParameterComboBox supportType;
	protected ParameterTextField targetName;
	protected ParameterFile exportPath;
	protected ParameterCheckBox cover;
	protected ParameterCombine basicCombine;
	protected Dataset selectDataset;
	protected ExportSetting exportSetting;
	protected boolean isSelectChanged = false;

	private PropertyChangeListener chooseDatasetListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelectChanged && null != evt.getNewValue()) {
				isSelectChanged = true;
				addDataset();
				isSelectChanged = false;
			}
		}
	};
	private PropertyChangeListener resetExportsettingListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!isSelectChanged && null != evt.getNewValue()) {
				isSelectChanged = true;
				resetExportSetting();
				isSelectChanged = false;
			}
		}
	};

	public MetaProcessAbstractExport() {

	}

	protected void registEvents() {
		removeEvents();
		this.chooseDataset.addPropertyListener(chooseDatasetListener);
		this.supportType.addPropertyListener(resetExportsettingListener);
	}

	protected void removeEvents() {
		this.chooseDataset.removePropertyListener(chooseDatasetListener);
		this.supportType.removePropertyListener(resetExportsettingListener);
	}

	protected void initParameters() {
		this.chooseDataset = new ParameterDatasetChooser();
		this.supportType = new ParameterComboBox(ProcessProperties.getString("String_ExportType"));
		this.supportType.setEnabled(false);
		this.targetName = new ParameterTextField(ProcessProperties.getString("String_TargetName"));
		this.targetName.setEnabled(false);
		this.exportPath = new ParameterFile(ProcessProperties.getString("String_ExportPath"));
		this.exportPath.setEnabled(false);
		this.cover = new ParameterCheckBox(ProcessProperties.getString("String_Cover"));
		this.cover.setEnabled(false);
		this.basicCombine = new ParameterCombine();
		this.basicCombine.setDescribe(CommonProperties.getString("String_ResultSet"));
		this.basicCombine.addParameters(this.chooseDataset, this.supportType, this.targetName, this.exportPath, this.cover);
		//输出为文件路径，没有控件能对应
		this.parameters.addOutputParameters(OUTPUT_DATA, BasicTypes.STRING, null);
	}

	protected void addDataset() {
		selectDataset = (Dataset) chooseDataset.getSelectedItem();
		ExportSetting tempExportSetting = new ExportSetting();
		tempExportSetting.setSourceData(selectDataset);
		FileType[] fileTypes = tempExportSetting.getSupportedFileType();
		int size = fileTypes.length;
		if (size > 0) {
//					if (isGpx) {
//						//GPS type
//						newExportSetting = exportSettingFactory.createExportSetting(UserDefineFileType.GPX);
//						temp[COLUMN_EXPORTTYPE] = DataConversionProperties.getString("String_FileTypeGPS");
//						exportsFileInfo.setFileType(UserDefineFileType.GPX);
//					} else {
			supportType.removeAllItems();
			ParameterDataNode selectNode = null;
			for (int i = 0; i < size; i++) {
				if (0 == i) {
					selectNode = new ParameterDataNode(ExportSettingUtilities.getDatasetName(fileTypes[0].toString()), fileTypes[0]);
					supportType.addItem(selectNode);
				} else {
					supportType.addItem(new ParameterDataNode(ExportSettingUtilities.getDatasetName(fileTypes[i].toString()), fileTypes[i]));
				}
			}
			supportType.setSelectedItem(selectNode);
		}
		exportSetting = ExportSettingUtilities.createExportSetting(fileTypes[0]);
		exportSetting.setSourceData(selectDataset);
		targetName.setSelectedItem(selectDataset.getName());

		supportType.setEnabled(true);
		targetName.setEnabled(true);
		exportPath.setEnabled(true);
		cover.setEnabled(true);
	}

	protected void resetExportSetting() {
		FileType selectFileType = (FileType) supportType.getSelectedData();
		exportSetting = ExportSettingUtilities.createExportSetting(selectFileType);
		if (null != selectDataset) {
			exportSetting.setSourceData(selectDataset);
		}
	}

	protected void setExportSettingInfo(boolean isOverwrite) {
		exportSetting.setTargetFileType((FileType) supportType.getSelectedData());
		exportSetting.setOverwrite(isOverwrite);
		exportSetting.setTargetFilePath(getFilePath());
	}

	protected String getFilePath() {
		String result = "";
		String filePath = exportPath.getSelectedItem().toString();
		String fileName = targetName.getSelectedItem().toString();
		FileType fileType = (FileType) supportType.getSelectedData();
		if (FileType.TEMSClutter == fileType) {
			if (!filePath.endsWith(File.separator)) {
				result = filePath + File.separator + fileName + "." + "b";
			} else {
				result = filePath + fileName + "." + "b";
			}
		} else {
			if (!filePath.endsWith(File.separator)) {
				result = filePath + File.separator + fileName + "." + fileType.toString().toLowerCase();
			} else {
				result = filePath + fileName + "." + fileType.toString().toLowerCase();
			}
		}
		return result;
	}

	protected boolean printResultInfo(boolean isSuccessful, String targetPath, ExportSteppedListener exportListener) {
		DataExport dataExport = new DataExport();
		dataExport.getExportSettings().add(exportSetting);
		try {
			fireRunning(new RunningEvent(this, 0, "start"));
			dataExport.addExportSteppedListener(exportListener);
			long startTime = System.currentTimeMillis();
			ExportResult result = dataExport.run();
			ExportSetting[] succeedSettings = result.getSucceedSettings();
			if (succeedSettings.length > 0) {
				isSuccessful = true;
				long totalTime = (System.currentTimeMillis() - startTime) / 1000;
				fireRunning(new RunningEvent(this, 100, "finished"));

				Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_FormExport_OutPutInfoTwo"),
						selectDataset.getName() + "@" + selectDataset.getDatasource().getAlias(), targetPath, String.valueOf(totalTime)));
			} else {
				fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ExportFailed")));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			dataExport.removeExportSteppedListener(exportListener);
		}
		return isSuccessful;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public String getKey() {
		return null;
	}
}
