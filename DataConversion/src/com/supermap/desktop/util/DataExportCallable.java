package com.supermap.desktop.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.swing.JTable;

import com.supermap.data.Dataset;
import com.supermap.data.conversion.DataExport;
import com.supermap.data.conversion.ExportResult;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.ExportSettings;
import com.supermap.data.conversion.ExportSteppedEvent;
import com.supermap.data.conversion.ExportSteppedListener;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.Application;
import com.supermap.desktop.ExportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

public class DataExportCallable extends UpdateProgressCallable {
	private ArrayList<ExportFileInfo> exports;
	private JTable exportTable;

	/**
	 * 是否强制覆盖
	 */
	private boolean isCover;

	public DataExportCallable(List<ExportFileInfo> exports, JTable exportTable, boolean isCover) {
		this.exportTable = exportTable;
		this.exports = (ArrayList<ExportFileInfo>) exports;
		this.isCover = isCover;
	}

	private String getDatasetAlis(ExportSetting tempSetting) {
		Dataset tempDataset = (Dataset) tempSetting.getSourceData();
		return tempDataset.getName() + DataConversionProperties.getString("string_index_and") + tempDataset.getDatasource().getAlias();
	}

	@Override
	public Boolean call() throws Exception {
		try {
			for (int i = 0; i < exports.size(); i++) {
				ExportFileInfo export = exports.get(i);
				ExportSetting tempExportSetting = export.getExportSetting();
				Dataset dataset = export.getDataset();
				String filePath = "";
				if (FileType.TEMSClutter == export.getTargetFileType()) {
					filePath = export.getFilePath() + export.getFileName() + DataConversionProperties.getString("string_index_pause") + "b";
				} else {
					filePath = export.getFilePath() + export.getFileName() + DataConversionProperties.getString("string_index_pause")
							+ export.getTargetFileType();
				}
				tempExportSetting.setOverwrite(isCover);
				tempExportSetting.setSourceData(dataset);
				// 暂时不支持导出为Microsoft类型的文件
				if (export.getTargetFileType() == FileType.CSV) {
					tempExportSetting.setTargetFileType(FileType.DBF);
					filePath = export.getFilePath() + export.getFileName() + DataConversionProperties.getString("string_index_pause")
							+ FileType.DBF;
				} else {
					tempExportSetting.setTargetFileType(export.getTargetFileType());
				}
				tempExportSetting.setTargetFilePath(filePath);
				final DataExport dataExport = new DataExport();
				ExportSettings exportSettings = dataExport.getExportSettings();
				exportSettings.add(tempExportSetting);
				PercentProgress progress = new PercentProgress(i);
				dataExport.addExportSteppedListener(progress);
				long startTime = System.currentTimeMillis();
				ExportResult result = dataExport.run();
				long endTime = System.currentTimeMillis();
				String time = String.valueOf((endTime - startTime) / 1000.0);
				printExportInfo(result, i, time);
				if (null != progress && progress.isCancel()) {
					break;
				}
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return true;
	}

	/**
	 * 进度事件得到运行时间
	 * 
	 * @author Administrator
	 *
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
				int count = exports.size();
				int totalPercent = (int) ((100 * this.i + arg0.getSubPercent()) / count);
				updateProgressTotal(arg0.getSubPercent(),
						MessageFormat.format(DataConversionProperties.getString("String_TotalTaskNumber"), String.valueOf(exports.size())),
						totalPercent,
						MessageFormat.format(DataConversionProperties.getString("String_DatasetOutport"), ((Dataset)arg0.getCurrentTask().getSourceData()).getName()));
			} catch (CancellationException e) {
				arg0.setCancel(true);
				this.isCancel = true;
			}
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
					exports.get(i).setState(DataConversionProperties.getString("String_FormImport_Succeed"));
					String successDatasetAlis = getDatasetAlis(successExportSettings[0]);
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(successExportInfo, successDatasetAlis, successExportSettings[0].getTargetFilePath(), time));
				} else if (null != failExportSettings && 0 < failExportSettings.length) {
					exports.get(i).setState(DataConversionProperties.getString("String_FormImport_NotSucceed"));
					String failDatasetAlis = getDatasetAlis(failExportSettings[0]);
					Application.getActiveApplication().getOutput()
							.output(MessageFormat.format(failExportInfo, failDatasetAlis));
				}

				// 刷新table
				((ExportModel) exportTable.getModel()).updateRows(exports);
			} else {
				Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("string_exporterror"));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}