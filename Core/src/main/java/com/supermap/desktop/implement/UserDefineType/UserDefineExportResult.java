package com.supermap.desktop.implement.UserDefineType;

import com.supermap.data.conversion.ExportSetting;

/**
 * Created by xie on 2017/3/29.
 */
public class UserDefineExportResult {
    private ExportSetting success;
    private ExportSetting fail;

    public UserDefineExportResult(ExportSetting success, ExportSetting fail) {
        this.success = success;
        this.fail = fail;
    }

    public ExportSetting getSuccess() {
        return success;
    }

    public ExportSetting getFail() {
        return fail;
    }
}
