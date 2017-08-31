package com.supermap.desktop.WorkflowView.meta.dataconversion;

import com.supermap.data.conversion.*;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.implement.UserDefineType.ExportSettingExcel;
import com.supermap.desktop.implement.UserDefineType.ExportSettingGPX;
import com.supermap.desktop.implement.UserDefineType.UserDefineFileType;
import com.supermap.desktop.process.ProcessProperties;

/**
 * Created by xie on 2017/6/29.
 */
public class ExportSettingUtilities {
	public static ExportSetting createExportSetting(Object fileType) {
		ExportSetting result = new ExportSetting();
		if (fileType.equals(com.supermap.data.conversion.FileType.BMP)) {
			result = new ExportSettingBMP();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.DWG)) {
			result = new ExportSettingDWG();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.DXF)) {
			result = new ExportSettingDXF();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.GIF)) {
			result = new ExportSettingGIF();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.JPG)) {
			result = new ExportSettingJPG();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.KML)) {
			result = new ExportSettingKML();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.KMZ)) {
			result = new ExportSettingKMZ();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.ModelX)) {
			result = new ExportSettingModelX();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.PNG)) {
			result = new ExportSettingPNG();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.SIT)) {
			result = new ExportSettingSIT();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.TEMSVector)) {
			result = new ExportSettingTEMSVector();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.TEMSClutter)) {
			result = new ExportSettingTEMSClutter();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.TEMSBuildingVector)) {
			result = new ExportSettingTEMSBuildingVector();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.TIF)) {
			result = new ExportSettingTIF();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.VCT)) {
			result = new ExportSettingVCT();
		} else if (fileType.equals(com.supermap.data.conversion.FileType.CSV)) {
			result = new ExportSettingCSV();
		} else if (fileType.equals(FileType.GEOJSON)) {
			result = new ExportSettingGeoJson();
		} else if (fileType.equals(UserDefineFileType.GPX)) {
			result = new ExportSettingGPX();
		}else if(fileType.equals(UserDefineFileType.EXCEL)){
			result = new ExportSettingExcel();
		}
		// 复制目标文件路径到新的exportsetting中
		return result;
	}

	/**
	 * 根据fileType资源化
	 *
	 * @param fileType
	 * @return
	 */
	public static String getDatasetName(String fileType) {
		String resultFileType = "";
		if (UserDefineFileType.GPX.toString().equalsIgnoreCase(fileType)){
			//GPX type reflect GPS
			fileType = "GPS";
		}
		if (UserDefineFileType.EXCEL.toString().equalsIgnoreCase(fileType)){
			fileType = "Excel";
		}
		String tempFileType = "String_FileType" + fileType;
		if (tempFileType.contains(resultFileType)) {
			boolean isVisibleName = true;
			try {
				ProcessProperties.getString(tempFileType);
			} catch (Exception e) {
				// 此处通过抛出异常来确定导出类型是否已经对应
				isVisibleName = false;
			}
			if (isVisibleName) {
				resultFileType = ProcessProperties.getString(tempFileType);
			}
		}
		return resultFileType;
	}


}
