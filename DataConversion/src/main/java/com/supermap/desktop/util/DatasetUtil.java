package com.supermap.desktop.util;

import com.supermap.data.conversion.FileType;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.utilities.StringUtilities;

import java.util.HashMap;
import java.util.Map;

public class DatasetUtil {

	private DatasetUtil() {
		super();
	}

	private static HashMap<String, String> datasetMap = new HashMap<String, String>();

	public static String getDatasetName(String dataset, String fileType, int flag) {
		String result_dataset = dataset;
		String result_fileType = fileType;
		String tempStr = "string_dataset_" + dataset.toLowerCase();
		String tempFileType = "String_FileType" + fileType;
		if (tempStr.contains(dataset.toLowerCase()) && 0 == flag) {
			result_dataset = DataConversionProperties.getString(tempStr);

		} else if (tempFileType.contains(result_fileType)) {
			boolean isVisibleName = true;
			try {
				DataConversionProperties.getString(tempFileType);
			} catch (Exception e) {
				// 此处通过抛出异常来确定导出类型是否已经对应
				isVisibleName = false;
			}
			if (isVisibleName) {
				result_fileType = DataConversionProperties.getString(tempFileType);
			}
		}
		if (!result_fileType.equals(fileType)) {
			datasetMap.put(tempFileType, result_fileType);
		}else{
			result_fileType = null;
		}
		return flag == 0 ? result_dataset : result_fileType;
	}

	public static FileType getFileType(String datasetName) {
		if (StringUtilities.isNullOrEmpty(datasetName)) {
			return FileType.NONE;
		}
		FileType fileType = null;
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeDWG"))) {
			fileType = FileType.DWG;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeDXF"))) {
			fileType = FileType.DXF;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeBMP"))) {
			fileType = FileType.BMP;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeGIF"))) {
			fileType = FileType.GIF;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeJPG"))) {
			fileType = FileType.JPG;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypePNG"))) {
			fileType = FileType.PNG;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeSIT"))) {
			fileType = FileType.SIT;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeTIF"))) {
			fileType = FileType.TIF;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeIMG"))) {
			fileType = FileType.IMG;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeModelX"))) {
			fileType = FileType.ModelX;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeKML"))) {
			fileType = FileType.KML;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeKMZ"))) {
			fileType = FileType.KMZ;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeTEMSClutter"))) {
			fileType = FileType.TEMSClutter;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeTEMSBuildingVector"))) {
			fileType = FileType.TEMSBuildingVector;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeTEMSVector"))) {
			fileType = FileType.TEMSVector;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeMIF"))) {
			fileType = FileType.MIF;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeTAB"))) {
			fileType = FileType.TAB;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeGRD"))) {
			fileType = FileType.GRD;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeE00"))) {
			fileType = FileType.E00;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeSHP"))) {
			fileType = FileType.SHP;
		}
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeVCT"))) {
			fileType = FileType.VCT;
		}
		// if (datasetName.equals(DataConversionProperties.getString("String_FileTypeCSV"))) {
		// fileType = FileType.CSV;
		// }
		if (datasetName.equals(DataConversionProperties.getString("String_FileTypeDBF"))) {
			fileType = FileType.DBF;
		}
		return fileType;
	}

	public static Map<String, String> getDatasetMap() {
		return datasetMap;
	}

	public static void setDatasetMap(Map<String, String> datasetMap) {
		DatasetUtil.datasetMap = (HashMap<String, String>) datasetMap;
	}

}
