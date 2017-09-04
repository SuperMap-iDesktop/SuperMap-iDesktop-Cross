package com.supermap.desktop.iml;

import com.supermap.data.conversion.*;
import com.supermap.desktop.Interface.IExportSettingFactory;
import com.supermap.desktop.implement.UserDefineType.ExportSettingExcel;
import com.supermap.desktop.implement.UserDefineType.ExportSettingGPX;
import com.supermap.desktop.implement.UserDefineType.UserDefineFileType;

/**
 * Created by xie on 2016/10/28.
 */
public class ExportSettingFactory implements IExportSettingFactory {
	@Override
	public ExportSetting createExportSetting(Object fileType) {
		ExportSetting result = new ExportSetting();
		if (null == fileType) {
			return result;
		}
		if (fileType.equals(FileType.BMP)) {
			result = new ExportSettingBMP();
		} else if (fileType.equals(FileType.DWG)) {
			result = new ExportSettingDWG();
		} else if (fileType.equals(FileType.DXF)) {
			result = new ExportSettingDXF();
		} else if (fileType.equals(FileType.GIF)) {
			result = new ExportSettingGIF();
		} else if (fileType.equals(FileType.JPG)) {
			result = new ExportSettingJPG();
		} else if (fileType.equals(FileType.KML)) {
			result = new ExportSettingKML();
		} else if (fileType.equals(FileType.KMZ)) {
			result = new ExportSettingKMZ();
		} else if (fileType.equals(FileType.ModelX)) {
			result = new ExportSettingModelX();
		} else if (fileType.equals(FileType.PNG)) {
			result = new ExportSettingPNG();
		} else if (fileType.equals(FileType.SIT)) {
			result = new ExportSettingSIT();
		} else if (fileType.equals(FileType.TEMSVector)) {
			result = new ExportSettingTEMSVector();
		} else if (fileType.equals(FileType.TEMSClutter)) {
			result = new ExportSettingTEMSClutter();
		} else if (fileType.equals(FileType.TEMSBuildingVector)) {
			result = new ExportSettingTEMSBuildingVector();
		} else if (fileType.equals(FileType.TIF)) {
			result = new ExportSettingTIF();
		} else if (fileType.equals(FileType.VCT)) {
			result = new ExportSettingVCT();
		} else if (fileType.equals(FileType.CSV)) {
			result = new ExportSettingCSV();
		} else if (fileType.equals(FileType.GEOJSON)) {
			result = new ExportSettingGeoJson();
		} else if (fileType.equals(FileType.SimpleJson)) {
			// 增加导出SimpleJson数据-yuanR2017.9.4
			result = new ExportSettingSimpleJson();
		} else if (fileType.equals(UserDefineFileType.GPX)) {
			result = new ExportSettingGPX();
		} else if (fileType.equals(UserDefineFileType.EXCEL)) {
			result = new ExportSettingExcel();
		}
		// 复制目标文件路径到新的exportsetting中
		return result;
	}
}
