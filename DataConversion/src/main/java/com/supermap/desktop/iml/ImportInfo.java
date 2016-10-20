package com.supermap.desktop.iml;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Interface.IImportInfo;

/**
 * Created by xie on 2016/10/12.
 * 导入信息实体类
 */
public class ImportInfo implements IImportInfo {
    ImportSetting importSetting;
    boolean isFieldIndex;
    boolean isSpatialIndex;
    boolean isSplitMore;
    // 导入文件名称
    private String fileName = "";
    // 导入文件类型
    private String fileType = "";
    // 导入状态
    private String state = "";

    public ImportInfo() {
        super();
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public void setFiletype(String filetype) {
        this.fileType = filetype;
    }

    @Override
    public String getFileType() {
        return this.fileType;
    }

    @Override
    public void setImportSetting(ImportSetting importSetting) {
        this.importSetting = importSetting;
    }

    @Override
    public ImportSetting getImportSetting() {
        return this.importSetting;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public void setFieldIndex(boolean isFieldIndex) {
        this.isFieldIndex = isFieldIndex;
    }

    @Override
    public boolean getFieldIndex() {
        return this.isFieldIndex;
    }

    @Override
    public void setSpatialIndex(boolean isSpatialIndex) {
        this.isSpatialIndex = isSpatialIndex;
    }

    @Override
    public boolean getSpatialIndex() {
        return this.isSpatialIndex;
    }

    @Override
    public void setSplitMore(boolean isSplitMore) {
        this.isSplitMore = isSplitMore;
    }

    @Override
    public boolean getSplitMore() {
        return isSplitMore;
    }
}
