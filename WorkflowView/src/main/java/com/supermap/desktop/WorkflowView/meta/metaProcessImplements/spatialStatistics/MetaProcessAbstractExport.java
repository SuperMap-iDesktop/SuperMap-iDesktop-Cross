package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
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
import com.supermap.desktop.utilities.StringUtilities;

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
	//	protected ParameterDatasetChooser chooseDataset;
	protected ParameterDatasource datasource;
	protected ParameterSingleDataset dataset;
	protected ParameterCombine sourceInfo;

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
				resetDataset();
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
		this.dataset.addPropertyListener(chooseDatasetListener);
		this.supportType.addPropertyListener(resetExportsettingListener);
	}

	protected void removeEvents() {
		this.dataset.removePropertyListener(chooseDatasetListener);
		this.supportType.removePropertyListener(resetExportsettingListener);
	}

	protected void initParameters() {
		this.datasource = new ParameterDatasource();
		this.sourceInfo = new ParameterCombine();
		this.sourceInfo.setDescribe(ControlsProperties.getString("String_GroupBox_SourceDataset"));
		this.dataset = new ParameterSingleDataset();
		if (Application.getActiveApplication().getWorkspace().getDatasources()!=null&&Application.getActiveApplication().getWorkspace().getDatasources().getCount()>0) {
			this.dataset.setDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
		}
		this.sourceInfo.addParameters(datasource, dataset);
		EqualDatasourceConstraint constraint = new EqualDatasourceConstraint();
		constraint.constrained(datasource, ParameterDatasource.DATASOURCE_FIELD_NAME);
		constraint.constrained(dataset, ParameterSingleDataset.DATASOURCE_FIELD_NAME);
//		this.chooseDataset = new ParameterDatasetChooser();
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
		this.basicCombine.addParameters(this.supportType, this.targetName, this.exportPath, this.cover);
		//输出为文件路径，没有控件能对应
		this.parameters.addOutputParameters(OUTPUT_DATA, BasicTypes.STRING, null);
	}

	protected void resetDataset() {
		selectDataset = (Dataset) dataset.getSelectedItem();
		if (null==selectDataset){
			return;
		}
		ExportSetting tempExportSetting = new ExportSetting();
		tempExportSetting.setSourceData(selectDataset);
		FileType[] fileTypes = tempExportSetting.getSupportedFileType();
		int size = fileTypes.length;
		if (size > 0) {
			boolean isGpx = false;
			if (tempExportSetting.getSourceData() instanceof DatasetVector) {
				isGpx = GPXAnalytic.isGPXType((DatasetVector) tempExportSetting.getSourceData());
			}
			supportType.removeAllItems();
			ParameterDataNode selectNode = null;
			if (isGpx) {
				selectNode = new ParameterDataNode(ExportSettingUtilities.getDatasetName(UserDefineFileType.GPX.toString()), UserDefineFileType.GPX);
				supportType.addItem(selectNode);
				for (int i = 0; i < size; i++) {
					if (!StringUtilities.isNullOrEmpty(ExportSettingUtilities.getDatasetName(fileTypes[i].toString()))) {
						supportType.addItem(new ParameterDataNode(ExportSettingUtilities.getDatasetName(fileTypes[i].toString()), fileTypes[i]));
					}
				}
				supportType.setSelectedItem(selectNode);
				exportSetting = ExportSettingUtilities.createExportSetting(UserDefineFileType.GPX);
			} else {
				for (int i = 0; i < size; i++) {
					if (!StringUtilities.isNullOrEmpty(ExportSettingUtilities.getDatasetName(fileTypes[i].toString()))) {
						if (0 == i) {
							selectNode = new ParameterDataNode(ExportSettingUtilities.getDatasetName(fileTypes[0].toString()), fileTypes[0]);
							supportType.addItem(selectNode);
						} else {
							supportType.addItem(new ParameterDataNode(ExportSettingUtilities.getDatasetName(fileTypes[i].toString()), fileTypes[i]));
						}
					}
				}
				supportType.setSelectedItem(selectNode);
				exportSetting = ExportSettingUtilities.createExportSetting(fileTypes[0]);
			}
		}
		exportSetting.setSourceData(selectDataset);
		targetName.setSelectedItem(selectDataset.getName());

		supportType.setEnabled(true);
		targetName.setEnabled(true);
		exportPath.setEnabled(true);
		cover.setEnabled(true);
	}

	protected void resetExportSetting() {
		exportSetting = ExportSettingUtilities.createExportSetting(supportType.getSelectedData());
		if (null != selectDataset) {
			exportSetting.setSourceData(selectDataset);
		}
	}

	protected void setExportSettingInfo(boolean isOverwrite) {
		if ((supportType.getSelectedData() instanceof FileType) && (supportType.getSelectedData().equals(FileType.SHP) || supportType.getSelectedData().equals(FileType.E00)
				|| supportType.getSelectedData().equals(FileType.MIF) || supportType.getSelectedData().equals(FileType.TAB)
				|| supportType.getSelectedData().equals(FileType.IMG) || supportType.getSelectedData().equals(FileType.GRD)
				|| supportType.getSelectedData().equals(FileType.DBF) || supportType.getSelectedData().equals(FileType.TEMSClutter))) {
			exportSetting.setTargetFileType((FileType) supportType.getSelectedData());
		}
		exportSetting.setOverwrite(isOverwrite);
		exportSetting.setTargetFilePath(getFilePath());
	}

	protected String getFilePath() {
		String result = "";
		if (null != exportPath.getSelectedItem()) {
			String filePath = exportPath.getSelectedItem().toString();
			String fileName = targetName.getSelectedItem().toString();
			if (supportType.getSelectedData() instanceof FileType) {
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
			} else {
				UserDefineFileType fileType = (UserDefineFileType) supportType.getSelectedData();
				if (!filePath.endsWith(File.separator)) {
					result = filePath + File.separator + fileName + "." + fileType.toString().toLowerCase();
				} else {
					result = filePath + fileName + "." + fileType.toString().toLowerCase();
				}
			}
		}
		return result;
	}

	protected boolean printResultInfo(boolean isSuccessful, String targetPath, ExportSteppedListener exportListener) {
		long startTime = System.currentTimeMillis();
		String time;
		if (exportSetting instanceof ExportSettingGPX) {
			((ExportSettingGPX) exportSetting).addExportSteppedListener(exportListener);
			UserDefineExportResult result = ((ExportSettingGPX) exportSetting).run();
			time = String.valueOf((System.currentTimeMillis() - startTime) / 1000.0);
			printExportInfo(result, time);
			((ExportSettingGPX) exportSetting).removeExportSteppedListener(exportListener);
		} else {
			DataExport dataExport = new DataExport();
			dataExport.getExportSettings().add(exportSetting);
			try {
				fireRunning(new RunningEvent(this, 0, "start"));
				dataExport.addExportSteppedListener(exportListener);

				ExportResult result = dataExport.run();
				ExportSetting[] succeedSettings = result.getSucceedSettings();
				if (succeedSettings.length > 0) {
					isSuccessful = true;
					time = String.valueOf((System.currentTimeMillis() - startTime) / 1000);
					fireRunning(new RunningEvent(this, 100, "finished"));
					Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_FormExport_OutPutInfoTwo"),
							selectDataset.getName() + "@" + selectDataset.getDatasource().getAlias(), targetPath, time));
				} else {
					fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ExportFailed")));
					Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_FormExport_OutPutInfoOne"), selectDataset.getName() + "@" + selectDataset.getDatasource().getAlias()));
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				dataExport.removeExportSteppedListener(exportListener);
			}
		}
		return isSuccessful;
	}

	/**
	 * 打印导出gps信息
	 *
	 * @param result
	 */
	private void printExportInfo(UserDefineExportResult result, String time) {
		try {
			if (null != result) {
				String successExportInfo = ProcessProperties.getString("String_FormExport_OutPutInfoTwo");
				String failExportInfo = ProcessProperties.getString("String_FormExport_OutPutInfoOne");
				if (null != result.getSuccess()) {
					String successDatasetAlis = getDatasetAlis(result.getSuccess());
					Application.getActiveApplication().getOutput().output(MessageFormat.format(successExportInfo, successDatasetAlis, result.getSuccess().getTargetFilePath(), time));
				} else if (null != result.getFail()) {
					String failDatasetAlis = getDatasetAlis(result.getFail());
					Application.getActiveApplication().getOutput().output(MessageFormat.format(failExportInfo, failDatasetAlis));
				}
				fireRunning(new RunningEvent(this, 100, "finished"));
			} else {
				fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ExportFailed")));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private String getDatasetAlis(ExportSetting tempSetting) {
		Dataset tempDataset = (Dataset) tempSetting.getSourceData();
		return tempDataset.getName() + ProcessProperties.getString("string_index_and") + tempDataset.getDatasource().getAlias();
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
