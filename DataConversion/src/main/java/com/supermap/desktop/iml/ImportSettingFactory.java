package com.supermap.desktop.iml;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IImportSettingFactory;
import com.supermap.desktop.UserDefineType.ImportSettingGPX;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.localUtilities.FileUtilities;

/**
 * Created by xie on 2016/10/14.
 * ImportSetting实例化工厂
 */
public class ImportSettingFactory implements IImportSettingFactory {

    @Override
    public ImportSetting createImportSetting(String filePath, String fileFilter) {
        ImportSetting importSetting = null;
        String fileType = FileUtilities.getFileType(filePath);
        if (fileType.equalsIgnoreCase(FileTypeLocale.DXF_STRING)) {
            if (!fileFilter.equalsIgnoreCase(DataConversionProperties.getString("string_filetype_3ds"))) {
                importSetting = new ImportSettingDXF();
                ((ImportSettingDXF) importSetting).setImportEmptyDataset(true);
            } else {
                importSetting = new ImportSettingModelDXF();
            }
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.DWG_STRING)) {
            importSetting = new ImportSettingDWG();
            ((ImportSettingDWG) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.VCT_STRING)) {
            importSetting = new ImportSettingVCT();
            ((ImportSettingVCT) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.DBF_STRING)) {
            importSetting = new ImportSettingDBF();
            ((ImportSettingDBF) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.GRD_STRING)
                || fileType.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
            //.dem类型的文件默认导入为GRD
            importSetting = new ImportSettingGRD();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.TXT_STRING)) {
            if (fileFilter.equalsIgnoreCase(DataConversionProperties.getString("string_filetype_lidar"))) {
                // 雷达文件
                importSetting = new ImportSettingLIDAR();
            } else {
                importSetting = new ImportSettingGRD();
            }
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.SHP_STRING)) {
            importSetting = new ImportSettingSHP();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.TAB_STRING)) {
            importSetting = new ImportSettingTAB();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.MIF_STRING)) {
            importSetting = new ImportSettingMIF();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.KML_STRING)) {
            importSetting = new ImportSettingKML();
            ((ImportSettingKML) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.KMZ_STRING)) {
            importSetting = new ImportSettingKMZ();
            ((ImportSettingKMZ) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.WAL_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAN_STRING)
                || fileType.equalsIgnoreCase(FileTypeLocale.WAT_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAP_STRING)) {
            importSetting = new ImportSettingMAPGIS();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.CSV_STRING)) {
            importSetting = new ImportSettingCSV();
            ((ImportSettingCSV) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.BMP_STRING)) {
            importSetting = new ImportSettingBMP();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.JPG_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.JPEG_STRING)) {
            importSetting = new ImportSettingJPG();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.JP2_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.JPK_STRING)) {
            importSetting = new ImportSettingJP2();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.PNG_STRING)) {
            importSetting = new ImportSettingPNG();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.GIF_STRING)) {
            importSetting = new ImportSettingGIF();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.IMG_STRING)) {
            importSetting = new ImportSettingIMG();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.RAW_STRING)) {
            importSetting = new ImportSettingRAW();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.SIT_STRING)) {
            importSetting = new ImportSettingSIT();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.ECW_STRING)) {
            importSetting = new ImportSettingECW();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.TIF_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.TIFF_STRING)) {
            importSetting = new ImportSettingTIF();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.B_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.BIN_STRING)) {
            importSetting = new ImportSettingTEMSClutter();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.BIL_STRING)) {
            importSetting = new ImportSettingBIL();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.BIP_STRING)) {
            importSetting = new ImportSettingBIP();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.BSQ_STRING)) {
            importSetting = new ImportSettingBSQ();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.WOR_STRING)) {
            // wor导入时同时设置目标工作空间，才能正常导入
            importSetting = new ImportSettingWOR();
            ((ImportSettingWOR) importSetting).setTargetWorkspace(Application.getActiveApplication().getWorkspace());
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.OSGB_STRING)) {
            importSetting = new ImportSettingModelOSG();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.SID_STRING)) {
            importSetting = new ImportSettingMrSID();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.E00_STRING)) {
            importSetting = new ImportSettingE00();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.XLSX_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.XLS_STRING)) {
            // 暂时没有实现excel文件的导入类,用ImportSettingCSV实例化避免异常
            importSetting = new ImportSettingCSV();
            ((ImportSettingCSV) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.TDS_STRING)) {
            // 3ds数据的导入
            importSetting = new ImportSettingModel3DS();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.X_STRING)) {
            importSetting = new ImportSettingModelX();
        }
//        else if (fileType.equalsIgnoreCase(FileTypeLocale.SCV_STRING)) {
//            importSetting = new ImportSettingSCV();
//        }
        else if (fileType.equalsIgnoreCase(FileTypeLocale.DGN_STRING)) {
            importSetting = new ImportSettingDGN();
            ((ImportSettingDGN) importSetting).setImportEmptyDataset(true);
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.FBX_STRING)) {
            importSetting = new ImportSettingModelFBX();
        } else if (fileType.equalsIgnoreCase(FileTypeLocale.GPX_STRING)) {
            importSetting = new ImportSettingGPX();
        }
        if (null != importSetting) {
            //初始化设置
            importSetting.setSourceFilePath(filePath);
        }
        if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
            importSetting.setTargetDatasource(Application.getActiveApplication().getActiveDatasources()[0]);
        } else if (Application.getActiveApplication().getWorkspace().getDatasources().getCount() > 0) {
            importSetting.setTargetDatasource(Application.getActiveApplication().getWorkspace().getDatasources().get(0));
        }
        return importSetting;
    }
}
