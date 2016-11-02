package com.supermap.desktop.localUtilities;

import com.supermap.data.conversion.FileType;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.FileTypeLocale;

/**
 * Created by xie on 2016/10/14.
 * 文件类型转换工具类
 * 将文件类型对应为自己设定的文件类型
 */
public class FiletypeUtilities {
    private FiletypeUtilities() {
        // 工具类不提供公共的构造函数
    }

    // 栅格类型文件
    private static final FileType[] gridValue = {FileType.BMP, FileType.SIT, FileType.GRD, FileType.RAW,
            FileType.CSV, FileType.BIL, FileType.IMG, FileType.TIF, FileType.PNG, FileType.JPG, FileType.JP2,
            FileType.GIF, FileType.GBDEM, FileType.USGSDEM, FileType.BSQ, FileType.BIP, FileType.BIL,
            FileType.MrSID, FileType.ECW, FileType.TEMSClutter};
    // 矢量文件
    private static final FileType[] vectorValue = {FileType.WOR, FileType.SCV, FileType.DXF, FileType.SHP,
            FileType.E00, FileType.MIF, FileType.TAB, FileType.MAPGIS, FileType.ModelOSG, FileType.Model3DS,
            FileType.ModelDXF, FileType.ModelX, FileType.KML, FileType.KMZ, FileType.DWG, FileType.VCT, FileType.DBF,
            FileType.GJB5068, FileType.DGN};
    public static String getParseFile(String filePath, String fileFilter) {
        String fileType = "";
        if (filePath.equalsIgnoreCase(FileTypeLocale.DBF_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_DBF");
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.VCT_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_VCT");
            // vct文件
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.DXF_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.DWG_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_CAD");
            // AutoCAD 格式(*.dxf,*.dwg)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.SHP_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.GRD_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.TXT_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.E00_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_ArcGIS");
            // ArcGIS 交换格式(*.shp,*.grd,*.txt,*.e00,*.dem，*dbf)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.TAB_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.MIF_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.WOR_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_MapInfo");
            // MapInfo 交换格式(*.tab,*.mif,*.wor)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.WAL_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.WAN_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.WAP_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.WAT_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_MapGIS");
            // MapGIS 交换格式(*.wat,*.wan,*.wal,*.wap)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.XLSX_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.CSV_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.XLS_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_MicroSoft");
            // Microsoft 交换格式(*.xlsx,*.csv)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.SIT_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.IMG_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.TIF_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.TIFF_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.BMP_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.PNG_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.JPG_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.JPEG_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.GIF_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.JP2_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.JPK_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_FilterImage");
            // 影像位图文件(*.sit,*.img,*.tif,*.tiff,*.bmp,*.png,*.gif,*.jpg,*.jpeg)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.SCV_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.OSGB_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.TDS_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.X_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_FilterModel");
            // 三维模型文件(*.scv,*.osgb,*.3ds,*.x)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.KML_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.KMZ_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_FilterGoogle");
            // 谷歌KML交换格式(*.kml,*.kmz)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.BIL_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.RAW_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.BSQ_STRING) || filePath.equalsIgnoreCase(FileTypeLocale.BIP_STRING)
                || filePath.equalsIgnoreCase(FileTypeLocale.B_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_GRID");
            // 栅格文件(*.bil,*.raw,*.bsq,*.bip,*.sid,*.b)
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.SID_STRING)) {
            fileType = "SID";
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.VCT_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_VCT");
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.DGN_STRING)) {
            fileType = "DGN";
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.ECW_STRING)) {
            fileType = "ECW";
        } else if (filePath.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
            fileType = DataConversionProperties.getString("String_FormImport_ArcGIS");
        } else if (fileFilter.equalsIgnoreCase(DataConversionProperties.getString("string_filetype_lidar"))) {
            // LIDAR文件(*.txt)
            fileType = DataConversionProperties.getString("String_FormImport_FilterLIDAR");
        } else if (fileFilter.equalsIgnoreCase(DataConversionProperties.getString("string_filetype_3ds"))) {
            fileType = DataConversionProperties.getString("String_FormImport_FilterModel");
            // 三维dxf文件（.ModelDXF）
        }
        return fileType;
    }

    /**
     * 根据别名查找fileType
     *
     * @param alias
     * @return
     */
    public static FileType getFileType(String alias) {
        FileType result = null;
        if (alias.equals(DataConversionProperties.getString("String_FileTypeDWG"))) {
            result = FileType.DWG;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeDXF"))) {
            result = FileType.DXF;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeVCT"))) {
            result = FileType.VCT;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeBMP"))) {
            result = FileType.BMP;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeGIF"))) {
            result = FileType.GIF;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeJPG"))) {
            result = FileType.JPG;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypePNG"))) {
            result = FileType.PNG;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeSIT"))) {
            result = FileType.SIT;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeTIF"))) {
            result = FileType.TIF;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeIMG"))) {
            result = FileType.IMG;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeSCV"))) {
            result = FileType.SCV;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeOSGB"))) {
            result = FileType.ModelOSG;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeModel3DS"))) {
            result = FileType.Model3DS;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeModelDXF"))) {
            result = FileType.ModelDXF;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeModelFBX"))) {
            result = FileType.ModelFBX;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeModelFLT"))) {
            result = FileType.ModelOpenFlight;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeModelX"))) {
            result = FileType.ModelX;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeKML"))) {
            result = FileType.KML;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeKMZ"))) {
            result = FileType.KMZ;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeSHP"))) {
            result = FileType.SHP;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeGRD"))) {
            result = FileType.GRD;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeE00"))) {
            result = FileType.E00;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeCSV"))) {
            result = FileType.CSV;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeTAB"))) {
            result = FileType.TAB;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeMIF"))) {
            result = FileType.MIF;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeWOR"))) {
            result = FileType.WOR;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeUSGSDEM"))) {
            result = FileType.USGSDEM;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeGBDEM"))) {
            result = FileType.GBDEM;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeBIL"))) {
            result = FileType.BIL;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeRAM"))) {
            result = FileType.RAW;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeBSQ"))) {
            result = FileType.BSQ;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeBIP"))) {
            result = FileType.BIP;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeSID"))) {
            result = FileType.MrSID;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeTEMSClutter"))) {
            result = FileType.TEMSClutter;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeLIDAR"))) {
            result = FileType.LIDAR;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeDBF"))) {
            result = FileType.DBF;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeTEMSBuildingVector"))) {
            result = FileType.TEMSBuildingVector;
        } else if (alias.equals(DataConversionProperties.getString("String_FileTypeTEMSVector"))) {
            result = FileType.TEMSVector;
        }

        return result;
    }

    public static FileType[] getGridValue() {
        return gridValue;
    }

    public static FileType[] getVectorValue() {
        return vectorValue;
    }

    public static boolean isGridType(FileType fileType) {
        for (FileType gridType : gridValue) {
            if (gridType.equals(fileType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVectorType(FileType fileType) {
        for (FileType vectorType : vectorValue) {
            if (vectorType.equals(fileType)) {
                return true;
            }
        }
        return false;
    }

}
