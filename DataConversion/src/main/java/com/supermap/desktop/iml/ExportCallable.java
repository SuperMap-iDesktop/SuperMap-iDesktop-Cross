package com.supermap.desktop.iml;

import com.supermap.data.Dataset;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.exportUI.DataExportDialog;
import com.supermap.desktop.implement.UserDefineType.ExportSettingGPX;
import com.supermap.desktop.implement.UserDefineType.UserDefineExportResult;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;

import javax.swing.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2016/10/31.
 * 导出进度条
 */
public class ExportCallable extends UpdateProgressCallable {
	private ArrayList<PanelExportTransform> exportPanels;
	private JTable exportTable;

	/**
	 * 是否强制覆盖
	 */

	public ExportCallable(ArrayList<PanelExportTransform> exportPanels, JTable exportTable) {
		this.exportTable = exportTable;
		this.exportPanels = exportPanels;
	}

	private String getDatasetAlis(ExportSetting tempSetting) {
		Dataset tempDataset = (Dataset) tempSetting.getSourceData();
		return tempDataset.getName() + DataConversionProperties.getString("string_index_and") + tempDataset.getDatasource().getAlias();
	}

	@Override
	public Boolean call() throws Exception {
		try {
			for (int i = 0; i < exportPanels.size(); i++) {
				DataExport dataExport = new DataExport();
				ExportSettings exportSettings = dataExport.getExportSettings();
				ExportFileInfo fileInfo = exportPanels.get(i).getExportsFileInfo();
				ExportSetting tempExportSetting = fileInfo.getExportSetting();
				if (null == fileInfo.getFileType()) {
					Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_FileTypeErrorWarning"));
					continue;
				}
				String filePath = getFilePath(fileInfo, fileInfo.getFileName());
				if (fileInfo.getFileType().equals(FileType.SHP) || fileInfo.getFileType().equals(FileType.E00)
						|| fileInfo.getFileType().equals(FileType.MIF) || fileInfo.getFileType().equals(FileType.TAB)
						|| fileInfo.getFileType().equals(FileType.IMG) || fileInfo.getFileType().equals(FileType.GRD)
						|| fileInfo.getFileType().equals(FileType.DBF) || fileInfo.getFileType().equals(FileType.TEMSClutter)) {
					tempExportSetting.setTargetFileType((FileType) fileInfo.getFileType());
				}
				if (new File(filePath).exists() && !tempExportSetting.isOverwrite()) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(DataConversionProperties.getString("String_DuplicateFileError"), filePath));
				} else {
					tempExportSetting.setTargetFilePath(filePath);
					PercentProgress progress = new PercentProgress(i);
					long startTime;
					long endTime;
					String time;
					if (filePath.endsWith(".gpx")) {
						((ExportSettingGPX) tempExportSetting).addExportSteppedListener(progress);
						startTime = System.currentTimeMillis();
						UserDefineExportResult result = ((ExportSettingGPX) tempExportSetting).run();
						endTime = System.currentTimeMillis();
						time = String.valueOf((endTime - startTime) / 1000.0);
						printExportInfo(result, i, time);
						if (null != progress && progress.isCancel()) {
							break;
						}
						dataExport.removeExportSteppedListener(progress);
					} else {
						exportSettings.add(tempExportSetting);
						dataExport.addExportSteppedListener(progress);
						startTime = System.currentTimeMillis();
						ExportResult result = dataExport.run();
						endTime = System.currentTimeMillis();
						time = String.valueOf((endTime - startTime) / 1000.0);
						printExportInfo(result, i, time);
						if (null != progress && progress.isCancel()) {
							break;
						}
						dataExport.removeExportSteppedListener(progress);
					}
				}
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return true;
	}

	private String getFilePath(ExportFileInfo exportFileInfo, String fileName) {
		String result = "";
		String filePath = exportFileInfo.getFilePath();
		FileType fileType = exportFileInfo.getExportSetting().getTargetFileType();
		if (FileType.TEMSClutter == fileType) {
			if (!filePath.endsWith(File.separator)) {
				result = filePath + File.separator + fileName + DataConversionProperties.getString("string_index_pause") + "b";
			} else {
				result = filePath + fileName + DataConversionProperties.getString("string_index_pause") + "b";
			}
		} else if (FileType.ModelX == fileType) {
			if (!filePath.endsWith(File.separator)) {
				result = filePath + File.separator + fileName + ".x";
			} else {
				result = filePath + fileName + ".x";
			}
		} else {
			if (!filePath.endsWith(File.separator)) {
				result = filePath + File.separator + fileName + DataConversionProperties.getString("string_index_pause")
						+ exportFileInfo.getFileType().toString().toLowerCase();
			} else {
				result = filePath + fileName + DataConversionProperties.getString("string_index_pause")
						+ exportFileInfo.getFileType().toString().toLowerCase();
			}
		}
		return result;
	}

	/**
	 * 进度事件得到运行时间
	 *
	 * @author xie
	 */
	class PercentProgress implements ExportSteppedListener {
		private int i;
		private boolean isCancel = false;

		public PercentProgress(int i) {
			this.i = i;
		}

		public boolean isCancel() {
			return this.isCancel;
		}

		@Override
		public void stepped(ExportSteppedEvent arg0) {
			try {
				int count = exportPanels.size();
				int totalPercent = (100 * this.i + arg0.getSubPercent()) / count;
				updateProgressTotal(arg0.getSubPercent(),
						MessageFormat.format(CommonProperties.getString("String_TotalTaskNumber"), String.valueOf(exportPanels.size())),
						totalPercent,
						MessageFormat.format(DataConversionProperties.getString("String_DatasetOutport"), ((Dataset) arg0.getCurrentTask().getSourceData()).getName()));
			} catch (CancellationException e) {
				arg0.setCancel(true);
				this.isCancel = true;
			}
		}

	}

	/**
	 * 打印导出gps信息
	 *
	 * @param result
	 */
	private void printExportInfo(UserDefineExportResult result, int i, String time) {
		try {
			if (null != result) {
				String successExportInfo = DataConversionProperties.getString("String_FormExport_OutPutInfoTwo");
				String failExportInfo = DataConversionProperties.getString("String_FormExport_OutPutInfoOne");
				if (null != result.getSuccess()) {
					exportTable.setValueAt(DataConversionProperties.getString("String_FormImport_Succeed"), i, DataExportDialog.COLUMN_STATE);
					String successDatasetAlis = getDatasetAlis(result.getSuccess());
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(successExportInfo, successDatasetAlis, result.getSuccess().getTargetFilePath(), time));
				} else if (null != result.getFail()) {
					exportTable.setValueAt(DataConversionProperties.getString("String_FormImport_NotSucceed"), i, DataExportDialog.COLUMN_STATE);
					String failDatasetAlis = getDatasetAlis(result.getFail());
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(failExportInfo, failDatasetAlis));
				}

			} else {
				Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("string_exporterror"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 打印导出信息
	 *
	 * @param result
	 */
	private void printExportInfo(ExportResult result, int i, String time) {
		try {
			if (null != result) {
				String successExportInfo = DataConversionProperties.getString("String_FormExport_OutPutInfoTwo");
				String failExportInfo = DataConversionProperties.getString("String_FormExport_OutPutInfoOne");
				ExportSetting[] successExportSettings = result.getSucceedSettings();
				ExportSetting[] failExportSettings = result.getFailedSettings();

				if (null != successExportSettings && 0 < successExportSettings.length) {
					exportTable.setValueAt(DataConversionProperties.getString("String_FormImport_Succeed"), i, DataExportDialog.COLUMN_STATE);
					String successDatasetAlis = getDatasetAlis(successExportSettings[0]);
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(successExportInfo, successDatasetAlis, successExportSettings[0].getTargetFilePath(), time));
				} else if (null != failExportSettings && 0 < failExportSettings.length) {
					exportTable.setValueAt(DataConversionProperties.getString("String_FormImport_NotSucceed"), i, DataExportDialog.COLUMN_STATE);
					String failDatasetAlis = getDatasetAlis(failExportSettings[0]);
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(failExportInfo, failDatasetAlis));
				}

				// 刷新table
//                exportTable.updateUI();// 不需要刷新，调setValueAt的时候会刷新改变的单元格。by xiaJT
			} else {
				Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("string_exporterror"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
