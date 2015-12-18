package com.supermap.desktop;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.ui.controls.DataCell;

public class ExportFileInfo {
	// 数据集封装类型
	DataCell datasetCell;
	// 数据源封装类型
	DataCell datasourceCell;
	// 数据集
	private Dataset dataset;
	// 显示的数据集名称
	private String datasetName;
	// 数据源名称
	private Datasource datasource;
	// 数据集类型
	private String dataType;
	// 导出的文件夹路径
	private String filepath;
	// 状态
	private String state;
	// 文件名
	private String fileName;
	// 支持的文件类型
	private FileType[] fileTypes;
	// 默认的exportSetting
	private ExportSetting exportSetting;
	// 默认的导出文件类型
	private FileType targetFileType;
	// 是否强制覆盖
	private boolean isCover;

	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}

	public DataCell getDatasourceCell() {
		Datasource tempDatasource = getDatasource();
		String datasourceName = tempDatasource.getAlias();
		String datasourcePath = CommonToolkit.DatasourceImageWrap.getImageIconPath(tempDatasource.getEngineType());
		DataCell resultdatasourceCell = new DataCell(datasourcePath, datasourceName);
		return resultdatasourceCell;
	}

	public void setDatasourceCell(DataCell datasourceCell) {
		this.datasourceCell = datasourceCell;
	}

	public DataCell getDatasetCell() {
		Dataset tempDataset = getDataset();
		String datasetPath = CommonToolkit.DatasetImageWrap.getImageIconPath(tempDataset.getType());
		datasetCell = new DataCell(datasetPath, getDatasetName());
		return datasetCell;
	}

	public void setDatasetCell(DataCell datasetCell) {
		this.datasetCell = datasetCell;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public FileType[] getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(FileType[] fileTypes) {
		this.fileTypes = fileTypes;
	}

	public ExportSetting getExportSetting() {
		return exportSetting;
	}

	public void setExportSetting(ExportSetting exportSetting) {
		this.exportSetting = exportSetting;
	}

	public Datasource getDatasource() {
		return datasource;
	}

	public void setDatasource(Datasource datasource) {
		this.datasource = datasource;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public String getFilePath() {
		return filepath;
	}

	public void setFilePath(String filepath) {
		this.filepath = filepath;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileType getTargetFileType() {
		return targetFileType;
	}

	public void setTargetFileType(FileType targetFileType) {
		this.targetFileType = targetFileType;
	}

}
