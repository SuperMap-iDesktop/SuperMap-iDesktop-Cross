package com.supermap.desktop.process.dataconversion;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.text.MessageFormat;

/**
 * Created by xie on 2017/4/7.
 */
public class FileType {
	public static String LastFileFilter = "";
	// 文件类型描述
	private static final String[] descriptionNew = {
			ProcessProperties.getString("String_filetype_all"),
			ProcessProperties.getString("String_filetype_autocad"),
			ProcessProperties.getString("String_filetype_arcgis"),
			ProcessProperties.getString("String_filetype_mapinfo"),
			ProcessProperties.getString("String_filetype_mapgis"),
			ProcessProperties.getString("String_filetype_microsoft"),
			ProcessProperties.getString("String_filetype_bitmap"),
			ProcessProperties.getString("String_filetype_3ds"),
			ProcessProperties.getString("String_filetype_kml"),
			ProcessProperties.getString("String_filetype_grid"),
			ProcessProperties.getString("String_filetype_lidar"),
			ProcessProperties.getString("String_filetype_vct")
	};
	private static final String[] descriptionNewForLinux = {
			ProcessProperties.getString("String_filetypeForLinux_all"),
			ProcessProperties.getString("String_filetypeForLinux_arcgis"),
			ProcessProperties.getString("String_filetypeForLinux_mapinfo"),
			ProcessProperties.getString("String_filetypeForLinux_microsoft"),
			ProcessProperties.getString("String_filetypeForLinux_bitmap"),
			ProcessProperties.getString("String_filetypeForLinux_3ds"),
			ProcessProperties.getString("String_filetypeForLinux_kml"),
			ProcessProperties.getString("String_filetypeForLinux_grid"),
			ProcessProperties.getString("String_filetypeForLinux_vct")};
	// 文件类型匹配数组
	private static final String[] extensionsNew = {"dxf", "dwg", "grd", "txt",
			"shp", "tab", "mif", "kml", "kmz", "wat", "wal", "wap", "wan",
			"csv", "bmp", "jpg", "jpeg", "jp2", "jpk", "png", "gif", "img", "raw", "sit",
			"tif", "tiff", "b", "wor", "osgb", "bip", "bil", "bsq", "sid", "dem",
			"e00", "3ds", "x", "vct", "dbf", "gjb", "dgn", "ecw", "gpx"};
	// linux系统匹配的文件类型
	private static final String[] extensionsNewForLinux = {"grd", "txt",
			"shp", "tab", "mif", "kml", "kmz", "csv", "bmp", "jpg", "jpeg", "jp2", "jpk", "ecw",
			"png", "gif", "img", "raw", "sit", "tif", "tiff",
			"bip", "bil", "bsq", "dem", "e00", "wor", "vct", "ecw",
			"3ds", "gpx"};


	public static SmFileChoose createFileChooser(String fileFilter, String moduleName) {
		if (!SmFileChoose.isModuleExist(moduleName)) {
			SmFileChoose.addNewNode(fileFilter, CommonProperties.getString("String_DefaultFilePath"),
					ProcessProperties.getString("String_FileType"), moduleName, "OpenOne");
		}
		SmFileChoose fileChoose = new SmFileChoose(moduleName);

		if (LastFileFilter != null) {
			// 设置过滤器为当前选中
			for (int i = 0; i < fileChoose.getChoosableFileFilters().length; i++) {
				FileFilter tempFileFilter = fileChoose.getChoosableFileFilters()[i];
				if (tempFileFilter.getDescription().equals(LastFileFilter)) {
					fileChoose.setFileFilter(tempFileFilter);
				}
			}
		}
		return fileChoose;
	}

	public static SmFileChoose createImportFileChooser(String importType) {
		String fileFilter;
		SmFileChoose fileChoose;
		if ("GJB".equalsIgnoreCase(importType) || "TEMSVector".equalsIgnoreCase(importType) || "TEMSBuildingVector".equalsIgnoreCase(importType) || "FileGDBVector".equalsIgnoreCase(importType)) {
			if (!SmFileChoose.isModuleExist("DataImportFrame_ImportDirectories")) {
				SmFileChoose.addNewNode("", "", ProcessProperties.getString("String_ScanDir"),
						"DataImportFrame_ImportDirectories", "GetDirectories");
			}
			fileChoose = new SmFileChoose("DataImportFrame_ImportDirectories");
			fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {
			String importModule = "MetaProcessImport" + importType;
			if (!SmFileChoose.isModuleExist(importModule)) {
				if ("MapGIS".equalsIgnoreCase(importType)) {
					fileFilter = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(descriptionNew[4], "wat", "wan", "wal", "wap"));
				} else if ("BIL".equalsIgnoreCase(importType)) {
					fileFilter = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MessageFormat.format(ProcessProperties.getString("String_ImportFileType"), "BIL", "bil"), "b"));
				} else if ("GBDEM".equalsIgnoreCase(importType) || "GRD_DEM".equalsIgnoreCase(importType)) {
					fileFilter = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MessageFormat.format(ProcessProperties.getString("String_ImportFileType"), "DEM", "dem"), "dem"));
				} else if("LIDAR".equalsIgnoreCase(importType)){
					fileFilter = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MessageFormat.format(ProcessProperties.getString("String_ImportFileType"), "LIDAR", "txt"), "txt"));
				} else{
					fileFilter = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MessageFormat.format(ProcessProperties.getString("String_ImportFileType"), importType, importType.toLowerCase()), importType.toLowerCase()));
				}
				SmFileChoose.addNewNode(fileFilter, CommonProperties.getString("String_DefaultFilePath"),
						ProcessProperties.getString("String_FileType"), importModule, "OpenOne");
			}
			fileChoose = new SmFileChoose(importModule);
			if (LastFileFilter != null) {
				// 设置过滤器为当前选中
				for (int i = 0; i < fileChoose.getChoosableFileFilters().length; i++) {
					FileFilter tempFileFilter = fileChoose.getChoosableFileFilters()[i];
					if (tempFileFilter.getDescription().equals(LastFileFilter)) {
						fileChoose.setFileFilter(tempFileFilter);
					}
				}
			}
		}
		return fileChoose;
	}

	/**
	 * 创建导入的文件选择器
	 *
	 * @return
	 */
	public static SmFileChoose createImportFileChooser() {
		if (!SmFileChoose.isModuleExist("MetaProcessImport")) {
			if (SystemPropertyUtilities.isWindows()) {
				String fileFilters = SmFileChoose.bulidFileFilters(
						SmFileChoose.createFileFilter(descriptionNew[0], extensionsNew),
						SmFileChoose.createFileFilter(descriptionNew[1], "dxf", "dwg"),
						SmFileChoose.createFileFilter(descriptionNew[2], "shp", "grd", "txt", "e00", "dem", "dbf"),
						SmFileChoose.createFileFilter(descriptionNew[3], "tab", "mif", "wor"),
						SmFileChoose.createFileFilter(descriptionNew[4], "wat", "wan", "wal", "wap"),
						SmFileChoose.createFileFilter(descriptionNew[5], "csv", "dbf"),
						SmFileChoose.createFileFilter(descriptionNew[6], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg", "jp2", "jpk", "sid", "ecw"),
						SmFileChoose.createFileFilter(descriptionNew[7], "osgb", "3ds", "dxf", "x", "fbx"),
						SmFileChoose.createFileFilter(descriptionNew[8], "kml", "kmz"),
						SmFileChoose.createFileFilter(descriptionNew[9], "dem", "bil", "raw", "bsq", "bip", "b"),
						SmFileChoose.createFileFilter(descriptionNew[10], "txt"),
						SmFileChoose.createFileFilter(descriptionNew[11], "dgn", "vct")
				);
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						ProcessProperties.getString("String_FileType"), "MetaProcessImport", "OpenOne");
			} else {
				String fileFilters = SmFileChoose.bulidFileFilters(
						SmFileChoose.createFileFilter(descriptionNewForLinux[0], extensionsNewForLinux),
						SmFileChoose.createFileFilter(descriptionNewForLinux[1], "shp", "grd", "txt", "e00", "dem", "dbf"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[2], "tab", "mif", "wor"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[3], "csv"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[4], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg", "jp2", "jpk", "sid", "ecw"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[5], "3ds"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[6], "kml", "kmz"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[7], "dem", "bil", "raw", "bsq", "bip"),
						SmFileChoose.createFileFilter(descriptionNewForLinux[8], "vct")
				);
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
						ProcessProperties.getString("String_FileType"), "MetaProcessImport", "OpenOne");
			}

		}
		SmFileChoose fileChoose = new SmFileChoose("MetaProcessImport");

		if (LastFileFilter != null) {
			// 设置过滤器为当前选中
			for (int i = 0; i < fileChoose.getChoosableFileFilters().length; i++) {
				FileFilter tempFileFilter = fileChoose.getChoosableFileFilters()[i];
				if (tempFileFilter.getDescription().equals(LastFileFilter)) {
					fileChoose.setFileFilter(tempFileFilter);
				}
			}
		}
		return fileChoose;
	}
}
