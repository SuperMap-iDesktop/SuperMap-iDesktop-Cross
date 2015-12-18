package com.supermap.desktop;

import javax.swing.JPanel;

import com.supermap.data.conversion.ImportSetting;

public class ImportFileInfo {

	private String filePath = "";
	private String fileName = "";
	private String fileType = "";
	private String state = "";
	private ImportSetting importSetting = null;
	private JPanel panel = null;
	// 是否创建字段索引
	private boolean isBuildFiledIndex = false;
	// 是否创建空间索引
	private boolean isBuildSpatialIndex = false;

	public ImportFileInfo() {
		super();
	}

	public boolean isBuildFiledIndex() {
		return isBuildFiledIndex;
	}

	public void setBuildFiledIndex(boolean isBuildFiledIndex) {
		this.isBuildFiledIndex = isBuildFiledIndex;
	}

	public boolean isBuildSpatialIndex() {
		return isBuildSpatialIndex;
	}

	public void setBuildSpatialIndex(boolean isBuildSpatialIndex) {
		this.isBuildSpatialIndex = isBuildSpatialIndex;
	}

	public ImportFileInfo(ImportSetting importSetting) {
		this.importSetting = importSetting;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public ImportSetting getImportSetting() {
		return importSetting;
	}

	public void setImportSetting(ImportSetting importSetting) {
		this.importSetting = importSetting;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
