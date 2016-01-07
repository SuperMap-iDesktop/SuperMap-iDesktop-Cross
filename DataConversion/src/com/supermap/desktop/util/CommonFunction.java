package com.supermap.desktop.util;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingBIL;
import com.supermap.data.conversion.ImportSettingBIP;
import com.supermap.data.conversion.ImportSettingBMP;
import com.supermap.data.conversion.ImportSettingBSQ;
import com.supermap.data.conversion.ImportSettingCSV;
import com.supermap.data.conversion.ImportSettingCoverage;
import com.supermap.data.conversion.ImportSettingDBF;
import com.supermap.data.conversion.ImportSettingDWG;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.data.conversion.ImportSettingE00;
import com.supermap.data.conversion.ImportSettingGIF;
import com.supermap.data.conversion.ImportSettingGRD;
import com.supermap.data.conversion.ImportSettingIMG;
import com.supermap.data.conversion.ImportSettingJPG;
import com.supermap.data.conversion.ImportSettingKML;
import com.supermap.data.conversion.ImportSettingKMZ;
import com.supermap.data.conversion.ImportSettingLIDAR;
import com.supermap.data.conversion.ImportSettingMAPGIS;
import com.supermap.data.conversion.ImportSettingMIF;
import com.supermap.data.conversion.ImportSettingModel3DS;
import com.supermap.data.conversion.ImportSettingModelDXF;
import com.supermap.data.conversion.ImportSettingModelFLT;
import com.supermap.data.conversion.ImportSettingModelOSG;
import com.supermap.data.conversion.ImportSettingModelX;
import com.supermap.data.conversion.ImportSettingMrSID;
import com.supermap.data.conversion.ImportSettingPNG;
import com.supermap.data.conversion.ImportSettingRAW;
import com.supermap.data.conversion.ImportSettingSCV;
import com.supermap.data.conversion.ImportSettingSHP;
import com.supermap.data.conversion.ImportSettingSIT;
import com.supermap.data.conversion.ImportSettingTAB;
import com.supermap.data.conversion.ImportSettingTEMSClutter;
import com.supermap.data.conversion.ImportSettingTIF;
import com.supermap.data.conversion.ImportSettingUSGSDEM;
import com.supermap.data.conversion.ImportSettingVCT;
import com.supermap.data.conversion.ImportSettingWOR;
import com.supermap.data.conversion.MultiBandImportMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.FileTypeLocale;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.DataImportFrame;
import com.supermap.desktop.ui.ImportPanelEXCEL;
import com.supermap.desktop.ui.ImportPanelLIDAR;
import com.supermap.desktop.ui.ImportPanelModel;
import com.supermap.desktop.ui.ImportPanelCSV;
import com.supermap.desktop.ui.ImportPanelD;
import com.supermap.desktop.ui.ImportPanelE00;
import com.supermap.desktop.ui.ImportPanelSCV;
import com.supermap.desktop.ui.ImportPanelArcGIS;
import com.supermap.desktop.ui.ImportPanelGRID;
import com.supermap.desktop.ui.ImportPanelIMG;
import com.supermap.desktop.ui.ImportPanelKML;
import com.supermap.desktop.ui.ImportPanelMapInfo;
import com.supermap.desktop.ui.ImportPanelMapGIS;
import com.supermap.desktop.ui.ImportPanelPI;
import com.supermap.desktop.ui.ImportPanelSHP;
import com.supermap.desktop.ui.ImportPanelSIT;
import com.supermap.desktop.ui.ImportPanelTIF;
import com.supermap.desktop.ui.ImportPanelVECTOR;
import com.supermap.desktop.ui.ImportPanelVandG;
import com.supermap.desktop.ui.ImportPanelWOR;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

/**
 * @author Administrator 通用的方法类包括右边界面刷新，初始化右边界面等方法
 */
public class CommonFunction {

	private CommonFunction() {
		super();
	}

	private static String LastFileFilter = "";

	/**
	 * 设置JTable的中的数据显示
	 *
	 * @param dataImportFrame
	 * @param fileInfos
	 * @param panels
	 * @param table
	 * @param model
	 */
	public static void setTableInfo(DataImportFrame dataImportFrame, List<JPanel> panels, FileInfoModel model) {
		if (!SmFileChoose.isModuleExist("CommonFunction")) {
			if (SystemPropertyUtilties.isWindows()) {
				String fileFilters = SmFileChoose.bulidFileFilters(
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[0], FileTypeLocale.getExtensionsnew()),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[1], "dxf", "dwg"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[2], "shp", "grd", "txt", "e00", "dem"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[3], "tab", "mif", "wor"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[4], "wat", "wan", "wal", "wap"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[5], "csv", "dbf"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[6], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[7], "scv", "osgb", "3ds", "dxf", "flt", "x"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[8], "kml", "kmz"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[9], "dem", "bil", "raw", "bsq", "bip", "sid", "b"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[10], "txt"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[11], "vct"),
						SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnew()[12], "dbf"));
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), DataConversionProperties.getString("String_Import"),
						"CommonFunction", "OpenMany");
			} else {
				String fileFilters = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[0],
						FileTypeLocale.getExtensionsnewforlinux()), SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[1], "shp", "grd",
						"txt", "e00", "dem"), SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[2], "tab", "mif", "wor"), SmFileChoose
						.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[3], "csv"), SmFileChoose.createFileFilter(
						FileTypeLocale.getDescriptionnewforlinux()[4], "sit", "img", "tif", "tiff", "bmp", "png", "gif", "jpg", "jpeg"), SmFileChoose
						.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[5], "scv", "3ds"), SmFileChoose.createFileFilter(
						FileTypeLocale.getDescriptionnewforlinux()[6], "kml", "kmz"), SmFileChoose.createFileFilter(
						FileTypeLocale.getDescriptionnewforlinux()[7], "dem", "bil", "raw", "bsq", "bip"), SmFileChoose.createFileFilter(
						FileTypeLocale.getDescriptionnewforlinux()[8], "vct"), SmFileChoose.createFileFilter(FileTypeLocale.getDescriptionnewforlinux()[9],
						"dbf"));
				SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"), DataConversionProperties.getString("String_Import"),
						"CommonFunction", "OpenMany");
			}

		}
		SmFileChoose fileChooser1 = new SmFileChoose("CommonFunction");

		if (LastFileFilter != null) {
			// 设置过滤器为当前选中
			for (int i = 0; i < fileChooser1.getChoosableFileFilters().length; i++) {
				FileFilter tempFileFilter = fileChooser1.getChoosableFileFilters()[i];
				if (tempFileFilter.getDescription().equals(LastFileFilter)) {
					fileChooser1.setFileFilter(tempFileFilter);
				}
			}
		}
		int state = fileChooser1.showDefaultDialog();
		LastFileFilter = fileChooser1.getFileFilter().getDescription();
		File[] files = fileChooser1.getSelectFiles();
		String fileFilter = fileChooser1.getFileFilter().getDescription();
		if (null != files && state == JFileChooser.APPROVE_OPTION) {
			setImportFileInfos(files, dataImportFrame, panels, model, fileFilter);
		}
	}

	/**
	 * 设置ImportSetting与导入类型的关联
	 *
	 * @param files
	 * @param dataImportFrame
	 * @param panels
	 * @param table
	 * @param model
	 * @param fileFilter
	 */
	public static void setImportFileInfos(File[] files, DataImportFrame dataImportFrame, List<JPanel> panels, FileInfoModel model, String fileFilter) {
		for (File file : files) {
			ImportFileInfo fileInfo = new ImportFileInfo();
			String absolutePath = file.getAbsolutePath();
			String fileName = file.getName();
			String fileType = fileName.substring(fileName.lastIndexOf(DataConversionProperties.getString("string_index_pause")), fileName.length());
			if (!".xlsx".equalsIgnoreCase(fileType)) {
				fileInfo.setFilePath(absolutePath);
				fileInfo.setFileName(fileName);
				String fileDepth = FileTypeUtil.getParseFile(fileType, fileFilter);
				fileInfo.setFileType(fileDepth);
				fileInfo.setState(DataConversionProperties.getString("string_change"));
				setImportSetting(dataImportFrame, fileType, fileInfo, panels, fileFilter);
				model.addRow(fileInfo);
			}

		}
	}

	/**
	 * 暂时没用
	 *
	 * @param contentPane
	 * @param tempFileType
	 * @param panels
	 * @param table
	 * @param lblDataimportType
	 */
	public static void refreshPanelForKey(JPanel contentPane, List<ImportFileInfo> tempFileType, List<JPanel> panels, JLabel lblDataimportType) {
		JPanel tempPane = getRightPanel(contentPane);
		GroupLayout layout = (GroupLayout) contentPane.getLayout();
		replacePanel(layout, tempFileType, panels, lblDataimportType, tempPane);
	}

	/**
	 * 刷新右边界面方法
	 *
	 * @param table
	 * @param contentPane
	 * @param fileInfos
	 * @param panels
	 * @param lblDataimportType
	 */
	public static void refreshPanel(JTable table, JPanel contentPane, List<ImportFileInfo> fileInfos, List<JPanel> panels, JLabel lblDataimportType) {
		// 刷新页面方法
		int[] selectedRowCounts = table.getSelectedRows();
		GroupLayout layout = (GroupLayout) contentPane.getLayout();
		JPanel tempPane = getRightPanel(contentPane);

		if (1 <= selectedRowCounts.length) {
			// 界面处理方法，点击左边数据显示右边界面交互情况
			ArrayList<ImportFileInfo> tempFileType = new ArrayList<ImportFileInfo>();
			ArrayList<JPanel> tempPanels = new ArrayList<JPanel>();
			for (int i = 0; i < selectedRowCounts.length; i++) {
				tempFileType.add(fileInfos.get(selectedRowCounts[i]));
				tempPanels.add(panels.get(selectedRowCounts[i]));
			}

			replacePanel(layout, tempFileType, tempPanels, lblDataimportType, tempPane);
		}

	}

	/**
	 * 只选择一条数据时的刷新方法
	 *
	 * @param contentPane
	 * @param fileInfos
	 * @param panels
	 * @param table
	 * @param lblDataimportType
	 */
	public static void refreshPanelSingal(JPanel contentPane, List<ImportFileInfo> fileInfos, List<JPanel> panels, JLabel lblDataimportType) {
		ImportFileInfo tempFile = null;
		JPanel tempPane = null;
		if (!fileInfos.isEmpty()) {
			tempFile = fileInfos.get(0);
			tempPane = panels.get(0);
		}
		JPanel tempPanel = CommonFunction.getRightPanel(contentPane);
		ArrayList<ImportFileInfo> selectedFileInfos = new ArrayList<ImportFileInfo>();
		ArrayList<JPanel> selectedPanels = new ArrayList<JPanel>();
		selectedFileInfos.add(tempFile);
		selectedPanels.add(tempPane);
		GroupLayout layout = (GroupLayout) contentPane.getLayout();
		replacePanel(layout, selectedFileInfos, selectedPanels, lblDataimportType, tempPanel);
	}

	/**
	 * 数据被全部选中时的刷新
	 *
	 * @param contentPane
	 * @param fileInfos
	 * @param panels
	 * @param table
	 * @param lblDataimportType
	 */
	public static void refreshPanelSelectedAll(JPanel contentPane, List<ImportFileInfo> fileInfos, List<JPanel> panels, JLabel lblDataimportType) {
		// 选中多个选项时刷新页面方法
		// 如果
		JPanel tempPane = getRightPanel(contentPane);
		GroupLayout layout = (GroupLayout) contentPane.getLayout();
		replacePanel(layout, fileInfos, panels, lblDataimportType, tempPane);
	}

	/**
	 * 判断数据是否全部是MapGIS类型，如果是返回true，否则返回false
	 *
	 * @param fileInfos
	 * @return
	 */
	private static boolean isMapGisOnly(ArrayList<ImportFileInfo> fileInfos) {
		ArrayList<String> tempL = new ArrayList<String>();
		for (int i = 0; i < FileTypeLocale.getMapgisvalue().length; i++) {
			tempL.add(FileTypeLocale.getMapgisvalue()[i]);
		}
		boolean flag = false;
		if (tempL.containsAll(getDataType(fileInfos))) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断数据是否全部是GRD类型，如果是返回true，否则返回false
	 *
	 * @param fileInfos
	 * @return
	 */
	private static boolean isGrdOnly(List<ImportFileInfo> fileInfos) {
		ArrayList<String> tempL = new ArrayList<String>();
		for (int i = 0; i < FileTypeLocale.getGrdvalue().length; i++) {
			tempL.add(FileTypeLocale.getGrdvalue()[i]);
		}
		boolean flag = false;
		if (tempL.containsAll(getDataType(fileInfos))) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断数据是否全部是栅格类型，如果是返回true，否则返回false
	 *
	 * @param fileInfos
	 * @return
	 */
	private static boolean isGridTypeOnly(ArrayList<ImportFileInfo> fileInfos) {
		ArrayList<String> tempL = new ArrayList<String>();
		for (int i = 0; i < FileTypeLocale.getGridvalue().length; i++) {
			tempL.add(FileTypeLocale.getGridvalue()[i]);
		}
		boolean result = false;
		if (tempL.containsAll(getDataType(fileInfos))) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断数据是否全部是矢量类型，如果是返回true，否则返回false
	 *
	 * @param fileInfos
	 * @return
	 */
	private static boolean isVectorTypeOnly(List<ImportFileInfo> fileInfos) {
		ArrayList<String> tempL = new ArrayList<String>();
		for (int i = 0; i < FileTypeLocale.getVectorvalue().length; i++) {
			tempL.add(FileTypeLocale.getVectorvalue()[i]);
		}
		boolean result = false;
		if (tempL.containsAll(getDataType(fileInfos))) {
			result = true;
		}
		return result;
	}

	/**
	 * 根据不同的文件类型实例化不同的ImportSetting
	 *
	 * @param dataImportFrame
	 * @param fileType
	 * @param fileInfo
	 * @param panels
	 * @param fileFilter
	 */
	private static void setImportSetting(DataImportFrame dataImportFrame, String fileType, ImportFileInfo fileInfo, List<JPanel> panels, String fileFilter) {
		ImportSetting importSetting = null;
		JPanel panel = null;
		if (fileType.equalsIgnoreCase(FileTypeLocale.DXF_STRING)) {
			if (!fileFilter.equalsIgnoreCase(DataConversionProperties.getString("string_filetype_3ds"))) {
				importSetting = new ImportSettingDXF();
				fileInfo.setImportSetting(importSetting);
				panel = new ImportPanelD(dataImportFrame, fileInfo);
			} else {
				importSetting = new ImportSettingModelDXF();
				fileInfo.setImportSetting(importSetting);
				panel = new ImportPanelModel(dataImportFrame, fileInfo);
			}
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.DWG_STRING)) {
			importSetting = new ImportSettingDWG();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelD(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.SCV_STRING)) {
			importSetting = new ImportSettingSCV();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelSCV(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.VCT_STRING)) {
			importSetting = new ImportSettingVCT();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelSCV(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.DBF_STRING)) {
			importSetting = new ImportSettingDBF();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelSCV(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.GRD_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.TXT_STRING)) {
			importSetting = new ImportSettingGRD();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.SHP_STRING)) {
			importSetting = new ImportSettingSHP();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelSHP(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.TAB_STRING)) {
			importSetting = new ImportSettingTAB();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelMapInfo(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.MIF_STRING)) {
			importSetting = new ImportSettingMIF();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelMapInfo(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.KML_STRING)) {
			importSetting = new ImportSettingKML();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelKML(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.KMZ_STRING)) {
			importSetting = new ImportSettingKMZ();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelKML(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.WAL_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAN_STRING)
				|| fileType.equalsIgnoreCase(FileTypeLocale.WAT_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAP_STRING)) {
			importSetting = new ImportSettingMAPGIS();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelMapGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.CSV_STRING)) {
			importSetting = new ImportSettingCSV();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelCSV(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.BMP_STRING)) {
			importSetting = new ImportSettingBMP();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelPI(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.JPG_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.JPEG_STRING)) {
			importSetting = new ImportSettingJPG();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelPI(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.PNG_STRING)) {
			importSetting = new ImportSettingPNG();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelPI(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.GIF_STRING)) {
			importSetting = new ImportSettingGIF();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelPI(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.IMG_STRING)) {
			importSetting = new ImportSettingIMG();
			((ImportSettingIMG) importSetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelIMG(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.RAW_STRING)) {
			importSetting = new ImportSettingRAW();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.SIT_STRING)) {
			importSetting = new ImportSettingSIT();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelSIT(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.TIF_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.TIFF_STRING)) {
			importSetting = new ImportSettingTIF();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelTIF(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.B_STRING)) {
			importSetting = new ImportSettingTEMSClutter();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.BIL_STRING)) {
			importSetting = new ImportSettingBIL();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.BIP_STRING)) {
			importSetting = new ImportSettingBIP();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.BSQ_STRING)) {
			importSetting = new ImportSettingBSQ();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.WOR_STRING)) {
			importSetting = new ImportSettingWOR();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelWOR(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.OSGB_STRING)) {
			importSetting = new ImportSettingModelOSG();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelModel(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.SID_STRING)) {
			importSetting = new ImportSettingMrSID();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelTIF(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
			importSetting = new ImportSettingUSGSDEM();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelArcGIS(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.FLT_STRING)) {
			importSetting = new ImportSettingModelFLT();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelModel(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.E00_STRING)) {
			importSetting = new ImportSettingE00();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelE00(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.XLSX_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.XLS_STRING)) {
			// 暂时没有实现excel文件的导入类,用ImportSettingCoverage实例化避免异常
			importSetting = new ImportSettingCoverage();
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.TDS_STRING)) {
			// 3ds数据的导入
			importSetting = new ImportSettingModel3DS();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelModel(dataImportFrame, fileInfo);
		}
		if (fileType.equalsIgnoreCase(FileTypeLocale.X_STRING)) {
			importSetting = new ImportSettingModelX();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelModel(dataImportFrame, fileInfo);
		}
		if (fileFilter.equalsIgnoreCase(DataConversionProperties.getString("string_filetype_lidar"))) {
			// 雷达文件
			importSetting = new ImportSettingLIDAR();
			fileInfo.setImportSetting(importSetting);
			panel = new ImportPanelLIDAR(dataImportFrame, fileInfo);
		}
		if (null != panel) {
			panels.add(panel);
		}
	}

	private static JPanel getRightPanel(List<JPanel> panels) {
		return panels.get(0);
	}

	/**
	 * 界面刷新的具体方法
	 *
	 * @param layout
	 * @param fileInfos
	 * @param panels
	 * @param table
	 * @param lblDataimportType
	 * @param tempPane
	 * @return
	 */
	private static JPanel replacePanel(GroupLayout layout, List<ImportFileInfo> fileInfos, List<JPanel> panels, JLabel lblDataimportType, JPanel tempPane) {
		JPanel dataPane = new JPanel();

		// 类型全部相同
		if (isSame(fileInfos)) {
			String fileName = fileInfos.get(0).getFileName();
			String fileType = fileName.substring(fileName.lastIndexOf(DataConversionProperties.getString("string_index_pause")), fileName.length());
			if (fileType.equalsIgnoreCase(FileTypeLocale.BMP_STRING)) {
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelPI(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportBMP_Text"));

			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.SHP_STRING)) {
				// 将右边替换为放置shp文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelSHP(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}

				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportSHP_Text"));

			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.TIF_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.TIFF_STRING)) {
				// 将右边替换为放置tif文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelTIF(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}

				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportTIF_Text"));

			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.DWG_STRING)) {
				// 将右边替换为放置dwg文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelD(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportDWG_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.DXF_STRING)) {
				// 将右边替换为放置dxf文件类型的容器
				if (1 != fileInfos.size()) {
					if (fileInfos.get(0).getImportSetting() instanceof ImportSettingDXF) {
						dataPane = new ImportPanelD(fileInfos, panels);
					}
					if (fileInfos.get(0).getImportSetting() instanceof ImportSettingModelDXF) {
						dataPane = new ImportPanelModel(fileInfos, panels);
					}
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportDXF_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.GRD_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.TXT_STRING)
					|| fileType.equalsIgnoreCase(FileTypeLocale.DEM_STRING)) {
				// 将右边替换为放置grd,txt,dem文件类型的容器
				if (1 != fileInfos.size()) {
					if (fileInfos.get(0).getImportSetting() instanceof ImportSettingGRD) {
						dataPane = new ImportPanelArcGIS(fileInfos, panels);
					}
					if (fileInfos.get(0).getImportSetting() instanceof ImportSettingLIDAR) {
						dataPane = new ImportPanelLIDAR(fileInfos, panels);
					}
				} else {
					dataPane = getRightPanel(panels);
				}
				if (dataPane instanceof ImportPanelLIDAR) {
					lblDataimportType.setText(DataConversionProperties.getString("String_FormImportLIDAR_Text"));
				} else {
					lblDataimportType.setText(DataConversionProperties.getString("String_FormImportGRD_Text"));
				}
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.XLSX_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.XLS_STRING)) {
				// 将右边替换为放置excel文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelEXCEL(fileInfos);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportExcel_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.IMG_STRING)) {
				// 将右边替换为放置img文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelIMG(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportIMG_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.FLT_STRING)) {
				// 将右边替换为放置flt文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelModel(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportModelFLT_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.PNG_STRING)) {
				// 将右边替换为放置png文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelPI(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportPNG_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.JPG_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.JPEG_STRING)) {
				// 将右边替换为放置jpg文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelPI(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportJPG_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.GIF_STRING)) {
				// 将右边替换为放置gif文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelPI(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportGIF_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.MIF_STRING)) {
				// 将右边替换为放置MIF文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelMapInfo(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportMIF_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.TDS_STRING)) {
				// 将右边替换为放置3DS文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelModel(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImport3DS_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.CSV_STRING)) {
				// 将右边替换为放置csv文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelCSV(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportCSV_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.OSGB_STRING)) {
				// 将右边替换为放置osgb文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelModel(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportOSG_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.WOR_STRING)) {
				// 将右边替换为放置wor文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelWOR(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportWOR_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.TAB_STRING)) {
				// 将右边替换为放置tab文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelMapInfo(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportTAB_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.E00_STRING)) {
				// 将右边替换为放置e00文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelE00(fileInfos, panels);
				} else if (isSame(fileInfos)) {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportE00_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.WAL_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAT_STRING)
					|| fileType.equalsIgnoreCase(FileTypeLocale.WAP_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.WAN_STRING)) {
				// 将右边替换为放置wal，wan,wat,wap文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelMapGIS(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportMAPGIS_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.KML_STRING)) {
				// 将右边替换为放置kml文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelKML(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportKML_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.KMZ_STRING)) {
				// 将右边替换为放置kmz文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelKML(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportKMZ_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.SIT_STRING)) {
				// 将右边替换为放置sit文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelSIT(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportSIT_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.RAW_STRING)) {
				// 将右边替换为放置sit文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelArcGIS(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportRAW_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.B_STRING)) {
				// 将右边替换为放置.b文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelArcGIS(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportTEMSClutter_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.BIL_STRING)) {
				// 将右边替换为放置电信栅格文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelArcGIS(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportBIL_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.BIP_STRING)) {
				// 将右边替换为放置电信栅格文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelArcGIS(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportBIP_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.BSQ_STRING)) {
				// 将右边替换为放置电信栅格文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelArcGIS(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportBSQ_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.X_STRING)) {
				// 将右边替换为放置X模型文件的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelModel(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportModelX_Text"));
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.SCV_STRING) || fileType.equalsIgnoreCase(FileTypeLocale.VCT_STRING)
					|| fileType.equalsIgnoreCase(FileTypeLocale.DBF_STRING)) {
				// 将右边替换为放置.scv/.vct/.dbf模型文件的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelSCV(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				if (fileType.equalsIgnoreCase(FileTypeLocale.SCV_STRING)) {
					lblDataimportType.setText(DataConversionProperties.getString("String_FormImportSCV_Text"));
				}else if(fileType.equalsIgnoreCase(FileTypeLocale.VCT_STRING)){
					lblDataimportType.setText(DataConversionProperties.getString("String_FormImportVCT_Text"));
				}else{
					lblDataimportType.setText(DataConversionProperties.getString("String_FormImportDBF_Text"));
				}
			}
			if (fileType.equalsIgnoreCase(FileTypeLocale.SID_STRING)) {
				// // 将右边替换为放置sid文件类型的容器
				if (1 != fileInfos.size()) {
					dataPane = new ImportPanelTIF(fileInfos, panels);
				} else {
					dataPane = getRightPanel(panels);
				}
				lblDataimportType.setText(DataConversionProperties.getString("String_FormImportMrSID_Text"));
			}
			if (null != dataPane && null != tempPane) {

				layout.replace(tempPane, dataPane);
			}
		} else if (isGrdOnly(fileInfos)) {
			dataPane = new ImportPanelArcGIS(fileInfos, panels);
			lblDataimportType.setText(DataConversionProperties.getString("String_FormImportGRD_Text"));
			if (null != dataPane && null != tempPane) {
				layout.replace(tempPane, dataPane);
			}
		} else if (isMapGisOnly((ArrayList<ImportFileInfo>) fileInfos)) {
			dataPane = new ImportPanelMapGIS(fileInfos, panels);
			lblDataimportType.setText(DataConversionProperties.getString("String_FormImportMAPGIS_Text"));
			if (null != dataPane && null != tempPane) {
				layout.replace(tempPane, dataPane);
			}
		} else if (isGridTypeOnly((ArrayList<ImportFileInfo>) fileInfos)) {
			dataPane = new ImportPanelGRID(fileInfos, panels);
			lblDataimportType.setText(DataConversionProperties.getString("String_FormImportRaster_Text"));
			if (null != dataPane && null != tempPane) {
				layout.replace(tempPane, dataPane);
			}
		} else if (isVectorTypeOnly(fileInfos)) {
			dataPane = new ImportPanelVECTOR(fileInfos, panels);
			lblDataimportType.setText(DataConversionProperties.getString("String_FormImportVector_Text"));
			if (null != dataPane && null != tempPane) {
				layout.replace(tempPane, dataPane);
			}
		} else {
			dataPane = new ImportPanelVandG(fileInfos, panels);
			lblDataimportType.setText(DataConversionProperties.getString("String_FormImportMix_Text"));
			if (null != dataPane && null != tempPane) {
				layout.replace(tempPane, dataPane);
			}
		}

		return dataPane;
	}

	/**
	 * 得到已有的界面
	 *
	 * @param contentPane
	 * @return
	 */
	public static JPanel getRightPanel(JPanel contentPane) {
		int count = contentPane.getComponentCount();
		Component[] component = contentPane.getComponents();
		JPanel result = null;
		for (int i = 0; i < count; i++) {
			if (component[i] instanceof JPanel && 1 != i) {
				result = (JPanel) contentPane.getComponent(i);
			}
		}
		return result;
	}

	/**
	 * 得到选中项的文件类型
	 *
	 * @param fileInfos
	 * @return
	 */
	private static HashSet<String> getDataType(List<ImportFileInfo> fileInfos) {
		HashSet<String> result = new HashSet<String>();
		for (ImportFileInfo fileInfo : fileInfos) {
			String fileName = fileInfo.getFileName();
			String filePath = fileName.substring(fileName.lastIndexOf(DataConversionProperties.getString("string_index_pause")), fileName.length());
			result.add(filePath.toLowerCase());
		}
		return result;
	}

	private static HashSet<String> getFileType(List<ImportFileInfo> fileInfos) {
		HashSet<String> result = new HashSet<String>();
		for (ImportFileInfo fileInfo : fileInfos) {
			String fileType = fileInfo.getFileType().toLowerCase();
			result.add(fileType);
		}
		return result;
	}

	/**
	 * 判断选中的文件类型是否相同
	 *
	 * @param fileInfos
	 * @return
	 */
	private static boolean isSame(List<ImportFileInfo> fileInfos) {
		boolean flag = false;
		// 判断要导入文件的类型是否相同
		if (1 == getDataType(fileInfos).size() && 1 == getFileType(fileInfos).size()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 导入的具体实现
	 *
	 * @param table
	 * @param fileInfos
	 */
	public static void importData(JTable table, List<ImportFileInfo> fileInfos) {
		int[] selected = table.getSelectedRows();
		final JTable jTable = table;
		final List<ImportFileInfo> fileInfosTemp = fileInfos;
		// 导入表中数据
		if (selected.length > 0) {
			final FormProgressTotal formProgress = new FormProgressTotal();
			formProgress.setTitle(DataConversionProperties.getString("String_FormImport_FormText"));
			if (null != formProgress) {
				formProgress.doWork(new DataImportCallable(fileInfosTemp, jTable));
			}
		} else {
			UICommonToolkit.showMessageDialog(DataConversionProperties.getString("String_ImportSettingPanel_Cue_AddFiles"));
		}
	}

	/**
	 * 点击添加时的处理方法
	 *
	 * @param dataImportFrame
	 * @param fileInfos
	 * @param panels
	 * @param table
	 * @param model
	 * @param contentPane
	 * @param lblDataimportType
	 * @return
	 */
	public static void addData(DataImportFrame dataImportFrame, List<ImportFileInfo> fileInfos, List<JPanel> panels, JTable table, FileInfoModel model,
			JPanel contentPane, JLabel lblDataimportType) {
		// 添加文件
		int select = 0;
		if (0 != table.getRowCount()) {
			select = table.getRowCount() - 1;
		}
		setTableInfo(dataImportFrame, panels, model);
		// 设置表格的所有行不可选
		if (table.getRowCount() - 1 < 0) {
			table.setRowSelectionAllowed(false);
		} else {
			// 设置表格的所有行可选
			table.setRowSelectionAllowed(true);
			// 设置新添加的项被选中
			int selected = table.getRowCount() - 1;
			table.setRowSelectionInterval(select, selected);

		}

		table.updateUI();
		if (!fileInfos.isEmpty()) {
			// 刷新右边界面
			CommonFunction.refreshPanelSelectedAll(contentPane, fileInfos, panels, lblDataimportType);
		}
	}

	// 点击删除时的处理方法
	public static void deleteData(JTable table, List<ImportFileInfo> fileInfos, List<JPanel> panels, FileInfoModel model, JPanel contentPane,
			JLabel lblDataimportType, JPanel newPanel) {
		// 执行删除
		int[] selectedRow = table.getSelectedRows();
		ArrayList<JPanel> removePanel = new ArrayList<JPanel>();
		if (!panels.isEmpty()) {
			for (int i = 0; i < selectedRow.length; i++) {
				removePanel.add(panels.get(selectedRow[i]));
			}
			model.removeRows(selectedRow);
			panels.removeAll(removePanel);
		}
		// 如果表中没有数据，右边部分显示为默认界面。
		if (fileInfos.isEmpty()) {
			JPanel tempPanel = CommonFunction.getRightPanel(contentPane);
			GroupLayout thisLayout = (GroupLayout) contentPane.getLayout();
			thisLayout.replace(tempPanel, newPanel);
		} else {
			CommonFunction.refreshPanelSingal(contentPane, fileInfos, panels, lblDataimportType);
			table.setRowSelectionInterval(0, 0);
		}
	}

	/**
	 * 反选
	 *
	 * @param table
	 */
	public static void selectInvert(JTable table) {
		try {
			int[] temp = table.getSelectedRows();
			ArrayList<Integer> selectedRows = new ArrayList<Integer>();
			for (int index = 0; index < temp.length; index++) {
				selectedRows.add(temp[index]);
			}

			ListSelectionModel selectionModel = table.getSelectionModel();
			selectionModel.clearSelection();
			for (int index = 0; index < table.getRowCount(); index++) {
				if (!selectedRows.contains(index)) {
					selectionModel.addSelectionInterval(index, index);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// 全选
	public static void selectedAll(List<ImportFileInfo> fileInfos, List<JPanel> panels, JTable table, JPanel contentPane, JLabel lblDataimportType) {
		if (table.getRowCount() - 1 < 0) {
			table.setRowSelectionAllowed(true);
		} else {
			table.setRowSelectionAllowed(true);
			// 设置所有项全部选中
			table.setRowSelectionInterval(0, table.getRowCount() - 1);
		}
		if (1 < table.getRowCount()) {
			// 刷新右边界面
			CommonFunction.refreshPanelSelectedAll(contentPane, fileInfos, panels, lblDataimportType);
		}
	}
}
