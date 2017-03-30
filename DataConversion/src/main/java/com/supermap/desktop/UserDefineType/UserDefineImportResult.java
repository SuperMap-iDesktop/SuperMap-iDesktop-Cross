package com.supermap.desktop.UserDefineType;

import com.supermap.data.conversion.ImportSetting;

/**
 * Created by xie on 2017/3/29.
 */
public class UserDefineImportResult {
    private ImportSetting success;
    private ImportSetting fail;

    public UserDefineImportResult(ImportSetting success, ImportSetting fail) {
        this.success = success;
        this.fail = fail;
    }

    public ImportSetting getFail() {
        return fail;
    }

    public ImportSetting getSuccess() {

        return success;
    }
}
