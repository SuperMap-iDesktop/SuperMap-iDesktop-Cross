package com.supermap.desktop.process.dataconversion;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FileUtilities;

/**
 * Created by xie on 2017/3/31.
 */
public class ImportSettingCreator implements IImportSettingCreator {
    private String importSetting = "com.supermap.data.conversion.ImportSetting";

    @Override
    public ImportSetting create(Object o) {
        ImportSetting result = null;
        /**
         * If o is a filePath
         */
        if (o instanceof String && FileUtilities.isFilePath((String) o)) {
            String datasetName = FileUtilities.getFileAlias((String) o);
            //todo
            //some special type should be pick out
            String fileType = FileUtilities.getFileType((String) o);
            fileType = fileType.replace(".", "");
            try {
                Class importClass = Class.forName(importSetting + fileType.toUpperCase());
                result = (ImportSetting) importClass.newInstance();
                result.setTargetDatasetName(datasetName);
                result.setSourceFilePath((String) o);
            } catch (ClassNotFoundException e) {
                Application.getActiveApplication().getOutput().output(e);
            } catch (IllegalAccessException e) {
                Application.getActiveApplication().getOutput().output(e);
            } catch (InstantiationException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
        return result;
    }
}
