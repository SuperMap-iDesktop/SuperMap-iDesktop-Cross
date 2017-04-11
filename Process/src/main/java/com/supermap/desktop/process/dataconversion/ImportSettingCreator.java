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
            } else if ("dem".equalsIgnoreCase(fileType)) {
                fileType = "GBDEM";
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
        } else {
            // o is an import file type
            try {
                String type = o.toString().toUpperCase();
                if ("B".equalsIgnoreCase(type)) {
                    type = "BIL";
                } else if ("DEM".equalsIgnoreCase(type)) {
                    type = "GBDEM";
                } else if ("3ds".equalsIgnoreCase(type) || "x".equalsIgnoreCase(type)) {
                    type = "Model" + type.toUpperCase();
                } else if ("osgb".equalsIgnoreCase(type)) {
                    type = "ModelOSG";
                }
                Class importClass = Class.forName(importSetting + type);
                result = (ImportSetting) importClass.newInstance();
            } catch (ClassNotFoundException e) {
                Application.getActiveApplication().getOutput().output(e);
                Application.getActiveApplication().getOutput().output(e);
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
        return result;
    }

}
