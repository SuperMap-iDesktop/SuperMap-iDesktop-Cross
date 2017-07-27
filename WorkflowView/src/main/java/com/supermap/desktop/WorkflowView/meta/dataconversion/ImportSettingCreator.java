package com.supermap.desktop.WorkflowView.meta.dataconversion;

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
                String type = o.toString();
                if ("B".equalsIgnoreCase(type)) {
                    type = "BIL";
                } else if ("DEM".equalsIgnoreCase(type)) {
                    type = "GBDEM";
                } else if ("3DS".equalsIgnoreCase(type) || "X".equalsIgnoreCase(type)) {
                    type = "Model" + type.toUpperCase();
                } else if ("OSGB".equalsIgnoreCase(type)) {
                    type = "ModelOSG";
                }else if("TXT".equalsIgnoreCase(type)){
                    type ="GRD";
                }else if ("wal".equalsIgnoreCase(type) || "wap".equalsIgnoreCase(type) || "wat".equalsIgnoreCase(type) || "wan".equalsIgnoreCase(type)) {
                    type = "MAPGIS";
                }else if ("jpk".equalsIgnoreCase(type)) {
                    type = "JP2";
                }else if ("sid".equalsIgnoreCase(type)) {
                    type = "MrSID";
                } else if ("TIFF".equalsIgnoreCase(type)) {
                    type = "TIF";
                }else if ("JPEG".equalsIgnoreCase(type)) {
                    type = "JPG";
                }else if ("GRD_DEM".equalsIgnoreCase(type)) {
                    type = "GRD";
                }
                Class importClass = Class.forName(importSetting + type);
                result = (ImportSetting) importClass.newInstance();
            } catch (ClassNotFoundException e) {
                Application.getActiveApplication().getOutput().output(e);
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
        return result;
    }

}
