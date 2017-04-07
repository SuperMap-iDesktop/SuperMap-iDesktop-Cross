package com.supermap.desktop.process.dataconversion;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.utilities.FileUtilities;

/**
 * Created by xie on 2017/3/31.
 */
public class ImportSettingCreator implements IImportSettingCreator {
    private final String importSetting = "com.supermap.data.conversion.ImportSetting";

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
            if ("b".equalsIgnoreCase(fileType)) {
                fileType = "BIL";
            } else if ("jpk".equalsIgnoreCase(fileType)) {
                fileType = "JP2";
            } else if ("wal".equalsIgnoreCase(fileType) || "wap".equalsIgnoreCase(fileType) || "wat".equalsIgnoreCase(fileType) || "wan".equalsIgnoreCase(fileType)) {
                fileType = "MAPGIS";
            } else if ("sid".equalsIgnoreCase(fileType)) {
                fileType = "MrSID";
            } else if ("3ds".equalsIgnoreCase(fileType) || "x".equalsIgnoreCase(fileType)) {
                fileType = "Model" + fileType.toUpperCase();
            } else if ("osgb".equalsIgnoreCase(fileType)) {
                fileType = "ModelOSG";
            } else {
                fileType = fileType.toUpperCase();
            }
            try {
                Class importClass = Class.forName(importSetting + fileType);
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
