package com.supermap.desktop;

import com.supermap.data.conversion.ImportSetting;

public class ImportFileInfo {
    // 导入文件路径
    private String filePath = "";
    // 导入文件名称
    private String fileName = "";
    // 导入文件类型
    private String fileType = "";
    // 导入状态
    private String state = "";
    // 导入设置信息类
    private ImportSetting importSetting = null;
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
