package com.supermap.desktop.iml;

import com.supermap.desktop.dataconversion.DataConversionProperties;

public class FileTypeLocale {
    private FileTypeLocale() {
        super();
    }

    // 文件类型描述
    private static final String[] descriptionNew = {
            DataConversionProperties.getString("string_filetype_all"),
            DataConversionProperties.getString("string_filetype_autocad"),
            DataConversionProperties.getString("string_filetype_arcgis"),
            DataConversionProperties.getString("string_filetype_mapinfo"),
            DataConversionProperties.getString("string_filetype_mapgis"),
            DataConversionProperties.getString("string_filetype_microsoft"),
            DataConversionProperties.getString("string_filetype_bitmap"),
            DataConversionProperties.getString("string_filetype_3ds"),
            DataConversionProperties.getString("string_filetype_kml"),
            DataConversionProperties.getString("string_filetype_grid"),
            DataConversionProperties.getString("string_filetype_lidar"),
            DataConversionProperties.getString("string_filetype_vct"),
            DataConversionProperties.getString("string_filetype_geojson"),
            DataConversionProperties.getString("string_filetype_gpx")
    };
    private static final String[] descriptionNewForLinux = {
            DataConversionProperties.getString("string_filetypeForLinux_all"),
            DataConversionProperties.getString("string_filetypeForLinux_arcgis"),
            DataConversionProperties.getString("string_filetypeForLinux_mapinfo"),
            DataConversionProperties.getString("string_filetypeForLinux_microsoft"),
            DataConversionProperties.getString("string_filetypeForLinux_bitmap"),
            DataConversionProperties.getString("string_filetypeForLinux_3ds"),
            DataConversionProperties.getString("string_filetypeForLinux_kml"),
            DataConversionProperties.getString("string_filetypeForLinux_grid"),
            DataConversionProperties.getString("string_filetypeForLinux_vct"),
            DataConversionProperties.getString("string_filetype_geojson"),
            DataConversionProperties.getString("string_filetype_gpx")};
    // 文件类型匹配数组
    private static final String[] extensionsNew = {"dxf", "dwg", "grd", "txt",
            "shp", "tab", "mif", "kml", "kmz", "wat", "wal", "wap", "wan",
            "csv", "bmp", "jpg", "jpeg", "jp2", "jpk", "png", "gif", "img", "raw", "sit",
            "tif", "tiff", "b", "wor", "osgb", "bip", "bil", "bsq", "sid", "dem",
            "e00", "3ds", "x", "vct", "dbf", "gjb", "dgn", "ecw","json","gpx","xls","xlsx"};
    // linux系统匹配的文件类型
    private static final String[] extensionsNewForLinux = {"grd", "txt",
            "shp", "tab", "mif", "kml", "kmz", "csv", "bmp", "jpg", "jpeg", "jp2", "jpk", "ecw",
            "png", "gif", "img", "raw", "sit", "tif", "tiff",
            "bip", "bil", "bsq", "dem", "e00", "wor", "vct", "ecw",
            "3ds","json","gpx","xls","xlsx"};

    public static String[] getDescriptionnew() {
        return descriptionNew;
    }

    public static String[] getDescriptionnewforlinux() {
        return descriptionNewForLinux;
    }

    public static String[] getExtensionsnew() {
        return extensionsNew;
    }

    public static String[] getExtensionsnewforlinux() {
        return extensionsNewForLinux;
    }


    // 导入文件格式
    public static final String DXF_STRING = ".dxf";
    public static final String DWG_STRING = ".dwg";
    public static final String SCV_STRING = ".scv";
    public static final String GRD_STRING = ".grd";
    public static final String TXT_STRING = ".txt";
    public static final String SHP_STRING = ".shp";
    public static final String TAB_STRING = ".tab";
    public static final String MIF_STRING = ".mif";
    public static final String KML_STRING = ".kml";
    public static final String KMZ_STRING = ".kmz";
    public static final String WAT_STRING = ".wat";
    public static final String WAL_STRING = ".wal";
    public static final String WAP_STRING = ".wap";
    public static final String WAN_STRING = ".wan";
    public static final String CSV_STRING = ".csv";
    public static final String BMP_STRING = ".bmp";
    public static final String JPG_STRING = ".jpg";
    public static final String JPEG_STRING = ".jpeg";
    public static final String JP2_STRING = ".jp2";
    public static final String JPK_STRING = ".jpk";
    public static final String PNG_STRING = ".png";
    public static final String GIF_STRING = ".gif";
    public static final String IMG_STRING = ".img";
    public static final String ECW_STRING = ".ecw";
    public static final String RAW_STRING = ".raw";
    public static final String SIT_STRING = ".sit";
    public static final String TIF_STRING = ".tif";
    public static final String TIFF_STRING = ".tiff";
    public static final String B_STRING = ".b";
    public static final String BIN_STRING = ".bin";
    public static final String WOR_STRING = ".wor";
    public static final String OSGB_STRING = ".osgb";
    public static final String BIL_STRING = ".bil";
    public static final String BSQ_STRING = ".bsq";
    public static final String SID_STRING = ".sid";
    public static final String DEM_STRING = ".dem";
    public static final String E00_STRING = ".e00";
    public static final String XLSX_STRING = ".xlsx";
    public static final String XLS_STRING = ".xls";
    public static final String DGN_STRING = ".dgn";
    // 3ds文件格式用TDS代替
    public static final String TDS_STRING = ".3ds";
    public static final String X_STRING = ".x";
    public static final String BIP_STRING = ".bip";
    public static final String DBF_STRING = ".dbf";
    public static final String VCT_STRING = ".vct";
    public static final String GJB_STRING = ".gjb";
    public static final String FBX_STRING = ".fbx";
    public static final String PRJ_STRING = ".prj";
    public static final String XML_STRING = ".xml";
    public static final String GEOJSON_STRING=".geojson";
    public static final String GPX_STRING=".gpx";
}
