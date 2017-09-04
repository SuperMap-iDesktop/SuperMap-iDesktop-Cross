package com.supermap.desktop.localUtilities;

import com.supermap.data.conversion.FileType;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.FileTypeLocale;
import com.supermap.desktop.implement.UserDefineType.UserDefineFileType;
import com.supermap.desktop.utilities.FileUtilities;

import java.io.File;

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
	private static final Object[] gridValue = {FileType.BMP, FileType.SIT, FileType.GRD, FileType.RAW,
			FileType.IMG, FileType.TIF, FileType.PNG, FileType.JPG, FileType.JP2,
			FileType.GIF, FileType.GBDEM, FileType.USGSDEM, FileType.BSQ, FileType.BIP, FileType.BIL,
			FileType.MrSID, FileType.ECW, FileType.TEMSClutter};
	// 矢量文件
	private static final Object[] vectorValue = {FileType.WOR, FileType.SCV, FileType.DXF, FileType.SHP,
			FileType.E00, FileType.MIF, FileType.TAB, FileType.MAPGIS, FileType.ModelOSG, FileType.Model3DS,
			FileType.ModelDXF, FileType.ModelX, FileType.KML, FileType.KMZ, FileType.DWG, FileType.VCT, FileType.DBF,
			FileType.GJB5068, FileType.DGN, FileType.TEMSVector, FileType.CSV, FileType.TEMSBuildingVector, UserDefineFileType.GPX, FileType.GEOJSON,
			UserDefineFileType.EXCEL
	};

	/**
	 * 根据文件路径，文件过滤项获取文件中文类型
	 *
	 * @param filePath
	 * @param fileFilter
	 * @return
	 */
	public static String getParseFile(String filePath, String fileFilter) {
		String fileType = FileUtilities.getFileType(filePath);
		if (fileType.equalsIgnoreCase(FileTypeLocale.DBF_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_DBF");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.VCT_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_VCT");
			// vct文件
		} else if ((fileType.equalsIgnoreCase(FileTypeLocale.DXF_STRING) && !fileFilter.equals(DataConversionProperties.getString("string_filetype_3ds"))) || fileType.equalsIgnoreCase(FileTypeLocale.DWG_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_CAD");
			// AutoCAD 格式(*.dxf,*.dwg)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.SHP_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.GRD_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.TXT_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.E00_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_ArcGIS");
			// ArcGIS 交换格式(*.shp,*.grd,*.txt,*.e00,*.dem，*dbf)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.TAB_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.MIF_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.WOR_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_MapInfo");
			// MapInfo 交换格式(*.tab,*.mif,*.wor)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.WAL_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAN_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.WAP_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAT_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_MapGIS");
			// MapGIS 交换格式(*.wat,*.wan,*.wal,*.wap)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.XLSX_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.CSV_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.XLS_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_MicroSoft");
			// Microsoft 交换格式(*.xlsx,*.csv)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.SIT_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.IMG_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.TIF_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.TIFF_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.BMP_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.PNG_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.JPG_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.JPEG_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.GIF_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.JP2_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.JPK_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_FilterImage");
			// 影像位图文件(*.sit,*.img,*.tif,*.tiff,*.bmp,*.png,*.gif,*.jpg,*.jpeg)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.SCV_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.OSGB_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.TDS_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.X_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_FilterModel");
			// 三维模型文件(*.scv,*.osgb,*.3ds,*.x)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.KML_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.KMZ_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_FilterGoogle");
			// 谷歌KML交换格式(*.kml,*.kmz)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.BIL_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.RAW_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.BSQ_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.BIP_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.B_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_GRID");
			// 栅格文件(*.bil,*.raw,*.bsq,*.bip,*.sid,*.b)
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.SID_STRING)) {
			fileType = DataConversionProperties.getString("String_FileTypeSID");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.VCT_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_VCT");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.DGN_STRING)) {
			fileType = DataConversionProperties.getString("String_FileTypeDGN");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.ECW_STRING)) {
			fileType = DataConversionProperties.getString("String_FileTypeECW");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_ArcGIS");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.GPX_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_GPX");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.GEOJSON_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_GEOJSON");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.SIMPLEJSON_STRING)) {
			fileType = DataConversionProperties.getString("String_FormImport_SIMPLEJSON");
		} else if (fileType.equalsIgnoreCase(FileTypeLocale.JSON_STRING)) {
			// 对于GeoJson和SimpleJson，文件类型都可以为".json",不同在于simpleJson同目录下有xxx.meta文件
			// 判断是否存在xxx.mate文件-yuanR2017.9.4
			String newfilePath = filePath.replace(".json", ".meta");
			File file = new File(newfilePath);
			if (file.exists()) {
				fileType = DataConversionProperties.getString("String_FormImport_SIMPLEJSON");
			} else {
				fileType = DataConversionProperties.getString("String_FormImport_GEOJSON");
			}
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
	public static Object getFileType(String alias) {
		Object result = null;
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
		} else if (alias.equals(DataConversionProperties.getString("String_FileTypeGEOJSON"))) {
			result = FileType.GEOJSON;
		} else if (alias.equals(DataConversionProperties.getString("String_FileTypeGPS"))) {
			result = UserDefineFileType.GPX;
		} else if (alias.equals(DataConversionProperties.getString("String_FileTypeExcel"))) {
			result = UserDefineFileType.EXCEL;
		}

		return result;
	}

	/**
	 * 获取栅格类型数组
	 *
	 * @return
	 */
	public static Object[] getGridValue() {
		return gridValue;
	}

	/**
	 * 获取矢量类型数组
	 *
	 * @return
	 */
	public static Object[] getVectorValue() {
		return vectorValue;
	}

	/**
	 * 是否为栅格类型
	 *
	 * @param fileType
	 * @return
	 */
	public static boolean isGridType(Object fileType) {
		for (Object gridType : gridValue) {
			if (gridType.equals(fileType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否为矢量类型
	 *
	 * @param fileType
	 * @return
	 */
	public static boolean isVectorType(Object fileType) {
		for (Object vectorType : vectorValue) {
			if (vectorType.equals(fileType)) {
				return true;
			}
		}
		return false;
	}

}
