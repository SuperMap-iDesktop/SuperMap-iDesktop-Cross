package com.supermap.desktop.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import com.supermap.data.conversion.CADVersion;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.ExportSettingBMP;
import com.supermap.data.conversion.ExportSettingDWG;
import com.supermap.data.conversion.ExportSettingDXF;
import com.supermap.data.conversion.ExportSettingGIF;
import com.supermap.data.conversion.ExportSettingJPG;
import com.supermap.data.conversion.ExportSettingKML;
import com.supermap.data.conversion.ExportSettingKMZ;
import com.supermap.data.conversion.ExportSettingModelX;
import com.supermap.data.conversion.ExportSettingPNG;
import com.supermap.data.conversion.ExportSettingSIT;
import com.supermap.data.conversion.ExportSettingTEMSBuildingVector;
import com.supermap.data.conversion.ExportSettingTEMSClutter;
import com.supermap.data.conversion.ExportSettingTEMSVector;
import com.supermap.data.conversion.ExportSettingTIF;
import com.supermap.data.conversion.ExportSettingVCT;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.ExportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.DataExportFrame;

public class ExportFunction {

	private static final String fileTypeChanged = "fileTypeChanged";
	private static final String fileTypeChangedPNG = "fileTypeChangedPNG";
	private static final String fileTypeChangedD = "fileTypeChangedD";
	private static final String fileTypeChangedTIF = "fileTypeChangedTIF";
	private static final String fileTypeChangedSIT = "fileTypeChangedSIT";

	private ExportFunction() {
		super();
	}

	/**
	 * 刷新导出界面的table
	 * 
	 * @param frame
	 * @param table
	 * @param exportTable
	 * @param selected
	 */
	public static void updateMainTable(DataExportFrame frame, JTable table, JTable exportTable) {
		int[] count = table.getSelectedRows();
		ExportModel model = (ExportModel) exportTable.getModel();
		int selected = 0;
		if (0 < exportTable.getRowCount()) {
			selected = exportTable.getRowCount() - 1;
		}
		for (int i = 0; i < count.length; i++) {
			ExportFileInfo tempOut = ((ChildExportModel) table.getModel()).getTagValueAt(count[i]);
			String datasetName = tempOut.getDatasetName();
			String datasourceAlias = tempOut.getDatasource().getAlias();
			ExportSetting exportSetting = new ExportSetting();
			exportSetting.setTargetFilePath(System.getProperty("user.dir") + File.separator);
			tempOut.setFilePath(exportSetting.getTargetFilePath());
			tempOut.setExportSetting(exportSetting);
			FileType fileType = null;
			if (0 < tempOut.getFileTypes().length) {
				fileType = tempOut.getFileTypes()[0];

			} else{
				fileType = FileType.DWG;
			}
			// 初始化exportSetting
			if (null != tempOut.getFileTypes() && 0 < tempOut.getFileTypes().length) {
				initExportSetting(tempOut, tempOut.getFileTypes()[0].name());
			}
			tempOut.setDatasetName(datasetName + DataConversionProperties.getString("string_index_and") + datasourceAlias);
			tempOut.setTargetFileType(fileType);
			model.addRow(tempOut);
		}

		// 设置新添加项被选中
		if (0 < count.length && 0 < table.getRowCount()) {
			int select = exportTable.getRowCount() - 1;
			if (selected <= select) {
				exportTable.setRowSelectionInterval(selected, select);

				// 刷新右边界面
				ExportFileInfo tempOut = ((ChildExportModel) table.getModel()).getTagValueAt(count[count.length - 1]);
				ArrayList<ExportFileInfo> exportFileInfos = (ArrayList<ExportFileInfo>) model.getExports();

				ArrayList<String> fileTypeArray = new ArrayList<String>();
				FileType[] fileTypes = tempOut.getFileTypes();
				for (int j = 0; j < fileTypes.length; j++) {
					fileTypeArray.add(fileTypes[j].name());
				}
				setComboBoxModel(fileTypeArray, frame.getComboBoxFileType());
				if (!fileTypeArray.isEmpty()) {
					refreshPanel(getRightPanelState(fileTypeArray.get(0)), frame, exportFileInfos);
				}
			}
		}

	}

	/**
	 * 根据table的选中行得到右边界面的改变
	 * 
	 * @param frame
	 * @param table
	 */
	public static void getRigthPanel(DataExportFrame frame, JTable table) {
		if (0 < table.getRowCount()) {

			int[] selectedRows = table.getSelectedRows();
			ExportModel model = (ExportModel) table.getModel();
			ArrayList<ExportFileInfo> tempExportFileInfos = new ArrayList<ExportFileInfo>();
			for (int i = 0; i < selectedRows.length; i++) {
				ExportFileInfo fileInfo = model.getTagValueAt(selectedRows[i]);
				tempExportFileInfos.add(fileInfo);
			}
			ArrayList<String> fileTypeArray = new ArrayList<String>();
			if (ExportFunction.isSame(tempExportFileInfos)) {
				FileType[] fileTypes = tempExportFileInfos.get(0).getFileTypes();
				for (int j = 0; j < fileTypes.length; j++) {
					fileTypeArray.add(fileTypes[j].name());
				}
			} else {
				fileTypeArray = (ArrayList<String>) getSameFileTypes(tempExportFileInfos);
			}
			setComboBoxModel(fileTypeArray, frame.getComboBoxFileType());
			if (!fileTypeArray.isEmpty()) {
				refreshPanel(getRightPanelState(fileTypeArray.get(0)), frame, tempExportFileInfos);
			} else {
				refreshPanel(getRightPanelState(""), frame, tempExportFileInfos);
			}
		}
	}

	// 单独选中一条数据时刷新
	public static void getRigthPanelAsSet(DataExportFrame frame, JTable table) {
		if (0 < table.getRowCount() && 0 < table.getSelectedRows().length) {
			int selectedRow = table.getSelectedRow();
			ExportModel model = (ExportModel) table.getModel();
			ExportFileInfo fileInfo = model.getTagValueAt(selectedRow);
			ArrayList<String> fileTypeArray = new ArrayList<String>();
			FileType[] fileTypes = fileInfo.getFileTypes();
			for (int j = 0; j < fileTypes.length; j++) {
				fileTypeArray.add(fileTypes[j].name());
			}
			setComboBoxModel(fileTypeArray, frame.getComboBoxFileType());
			ExportSetting exportSetting = fileInfo.getExportSetting();
			if (null != fileInfo.getTargetFileType()) {
				String tempFileType = "String_FileType" + fileInfo.getTargetFileType();
				String selectItem = DatasetUtil.getDatasetMap().get(tempFileType);
				frame.getComboBoxFileType().setSelectedItem(selectItem);
			}
			if (fileInfo.isCover()) {
				frame.getRadioButtonOK().setSelected(true);
				frame.getRadioButtonNO().setSelected(false);
			} else {
				frame.getRadioButtonNO().setSelected(true);
				frame.getRadioButtonOK().setSelected(false);
			}
			if (!exportSetting.getTargetFilePath().isEmpty()) {
				frame.getFilePath().getEditor().setText(exportSetting.getTargetFilePath());
			}
			// 导出类型为jpg
			if (null != exportSetting && (exportSetting instanceof ExportSettingJPG)) {
				String compression = String.valueOf(((ExportSettingJPG) exportSetting).getCompression());
				if (!compression.isEmpty()) {
					frame.getTextFieldCompression().setText(compression);
				}
				String worldFile = ((ExportSettingJPG) exportSetting).getWorldFilePath();
				if (!worldFile.isEmpty()) {
					frame.getFileChooser().getEditor().setText(worldFile);
				}
			}
			// 导出类型为tif
			if (null != exportSetting && (exportSetting instanceof ExportSettingTIF)) {
				boolean isExportingGeo = ((ExportSettingTIF) exportSetting).isExportingGeoTransformFile();
				frame.getCheckboxTFW().setSelected(isExportingGeo);
			}
			// 导出类型为dwg/dxf
			if (null != exportSetting && ((exportSetting instanceof ExportSettingDWG) || (exportSetting instanceof ExportSettingDXF))) {
				if (exportSetting instanceof ExportSettingDWG) {
					boolean isExportingExtDwg = ((ExportSettingDWG) exportSetting).isExportingExternalData();
					CADVersion cadVersion = ((ExportSettingDWG) exportSetting).getVersion();
					frame.getCheckboxExtends().setSelected(isExportingExtDwg);
					frame.getComboBoxCAD().setSelectedItem(getCadVersionString(cadVersion));
				}
				if (exportSetting instanceof ExportSettingDXF) {
					boolean isExportingExtDxf = ((ExportSettingDXF) exportSetting).isExportingExternalData();
					CADVersion cadVersion = ((ExportSettingDXF) exportSetting).getVersion();
					frame.getCheckboxExtends().setSelected(isExportingExtDxf);
					frame.getComboBoxCAD().setSelectedItem(getCadVersionString(cadVersion));
				}
			}
			// 导出类型为bmp，gif，png
			if (null != exportSetting
					&& ((exportSetting instanceof ExportSettingBMP) || (exportSetting instanceof ExportSettingGIF) || (exportSetting instanceof ExportSettingPNG))) {
				if (exportSetting instanceof ExportSettingBMP) {
					String worldFile = ((ExportSettingBMP) exportSetting).getWorldFilePath();
					frame.getFileChooser().getEditor().setText(worldFile);
				}
				if (exportSetting instanceof ExportSettingGIF) {
					String worldFile = ((ExportSettingGIF) exportSetting).getWorldFilePath();
					frame.getFileChooser().getEditor().setText(worldFile);
				}
				if (exportSetting instanceof ExportSettingPNG) {
					String worldFile = ((ExportSettingPNG) exportSetting).getWorldFilePath();
					frame.getFileChooser().getEditor().setText(worldFile);
				}
			}
			//
		}
	}

	private static String getCadVersionString(CADVersion cadVersion) {
		String version = null;
		if (cadVersion == CADVersion.CAD2007) {
			version = "CAD2007";
		}
		if (cadVersion == CADVersion.CAD2004) {
			version = "CAD2004";
		}
		if (cadVersion == CADVersion.CAD2000) {
			version = "CAD2000";
		}
		if (cadVersion == CADVersion.CAD12) {
			version = "CAD12";
		}
		if (cadVersion == CADVersion.CAD14) {
			version = "CAD14";
		}
		if (cadVersion == CADVersion.CAD13) {
			version = "CAD13";
		}
		return version;
	}

	/**
	 * 将frame中的控件设置为默认的状态
	 * 
	 * @param frame
	 */
	public static void setRightPanelAsDefualt(DataExportFrame frame) {
		String compression = frame.getTextFieldCompression().getText();
		if (!"75".equals(compression)) {
			frame.getTextFieldCompression().setText("75");
		}
		frame.getFilePath().getEditor().setText(System.getProperty("user.dir"));
		frame.getCheckboxTFW().setSelected(false);
		frame.getCheckboxExtends().setSelected(false);
		frame.getComboBoxCAD().setSelectedIndex(0);
		frame.getTextFieldPassword().setText("");
		frame.getTextFieldConfrim().setText("");
		frame.getRadioButtonOK().setEnabled(false);
		frame.getRadioButtonNO().setEnabled(false);
	}

	/**
	 * 选中不同的数据集类型是否相同,true为相同，false为不同
	 * 
	 * @param exportFileInfos
	 * @return
	 */
	public static boolean isSame(List<ExportFileInfo> exportFileInfos) {
		HashSet<String> datasetTypes = new HashSet<String>();
		for (int i = 0; i < exportFileInfos.size(); i++) {
			datasetTypes.add(exportFileInfos.get(i).getDataType());
		}
		// 选中的数据集只有一种类型
		if (1 == datasetTypes.size()) {
			return true;
		}
		return false;
	}

	/**
	 * 选中不同的数据是否有支持的导出文件类型，如果有就返回支持的导出文件类型，没有就返回空
	 * 
	 * @param exportFileInfos
	 * @return
	 */
	public static List<String> getSameFileTypes(List<ExportFileInfo> exportFileInfos) {
		ArrayList<String> sameFileType = new ArrayList<String>();
		if (!exportFileInfos.isEmpty()) {
			ExportFileInfo tempExportFileInfo = exportFileInfos.get(0);
			FileType[] fileTypes = tempExportFileInfo.getFileTypes();
			for (int i = 0; i < fileTypes.length; i++) {
				sameFileType.add(fileTypes[i].name());
			}
			for (int i = 0; i < exportFileInfos.size(); i++) {
				ArrayList<String> tempFileTypes = new ArrayList<String>();
				FileType[] compare = exportFileInfos.get(i).getFileTypes();
				for (int j = 0; j < compare.length; j++) {
					tempFileTypes.add(compare[j].name());
				}
				sameFileType.retainAll(tempFileTypes);
			}
		}
		if (sameFileType.isEmpty()) {
			return new ArrayList<String>();
		}
		return sameFileType;
	}

	/**
	 * 得到右边界面的选中状态
	 * 
	 * @param frame
	 * @param fileType
	 */
	public static Map<String, Boolean> getRightPanelState(String fileType) {
		HashMap<String, Boolean> state = new HashMap<String, Boolean>();
		if (("BMP".equalsIgnoreCase(fileType)) || ("GIF".equalsIgnoreCase(fileType)) || ("JPG".equalsIgnoreCase(fileType))
				|| ("PNG".equalsIgnoreCase(fileType))) {
			state.put(fileTypeChanged, true);
			if ("JPG".equalsIgnoreCase(fileType)) {
				state.put(fileTypeChangedPNG, true);
			} else {
				state.put(fileTypeChangedPNG, false);
			}
		} else {
			state.put(fileTypeChanged, false);
			state.put(fileTypeChangedPNG, false);
		}
		if (("DWG".equalsIgnoreCase(fileType)) || ("DXF".equalsIgnoreCase(fileType))) {
			state.put(fileTypeChangedD, true);
		} else {
			state.put(fileTypeChangedD, false);
		}
		if ("TIF".equalsIgnoreCase(fileType)) {
			state.put(fileTypeChangedTIF, true);
		} else {
			state.put(fileTypeChangedTIF, false);
		}
		if ("SIT".equalsIgnoreCase(fileType)) {
			state.put(fileTypeChangedSIT, true);
		} else {
			state.put(fileTypeChangedSIT, false);
		}
		return state;
	}

	public static void refreshPanelForCombobox(Map<String, Boolean> state, DataExportFrame frame) {
		String compression = frame.getTextFieldCompression().getText();
		if (!"75".equals(compression)) {
			frame.getTextFieldCompression().setText("75");
		}
		frame.getCheckboxTFW().setSelected(false);
		frame.getCheckboxExtends().setSelected(false);
		frame.getComboBoxCAD().setSelectedIndex(0);
		frame.getTextFieldPassword().setText("");
		frame.getTextFieldConfrim().setText("");
		if (null != state && !state.isEmpty()) {
			frame.getFileChooser().setEnabled(state.get(fileTypeChanged));
			frame.getTextFieldCompression().setEnabled(state.get(fileTypeChangedPNG));
			frame.getCheckboxExtends().setEnabled(state.get(fileTypeChangedD));
			frame.getComboBoxCAD().setEnabled(state.get(fileTypeChangedD));
			frame.getCheckboxTFW().setEnabled(state.get(fileTypeChangedTIF));
			frame.getTextFieldPassword().setEnabled(state.get(fileTypeChangedSIT));
			frame.getTextFieldConfrim().setEnabled(state.get(fileTypeChangedSIT));
		}
	}

	/**
	 * 刷新右边界面
	 * 
	 * @param state
	 * @param frame
	 */
	public static void refreshPanel(Map<String, Boolean> state, DataExportFrame frame, List<ExportFileInfo> fileInfos) {

		frame.getFilePath().getEditor().setText(System.getProperty("user.dir"));
		for (int i = 0; i < fileInfos.size(); i++) {
			fileInfos.get(i).setFilePath(System.getProperty("user.dir") + File.separator);
		}
		refreshPanelForCombobox(state, frame);
	}

	/**
	 * 根据可导出的文件类型设置初始的ExportSetting
	 * 
	 * @param exportSetting
	 * @param fileType
	 */
	public static void initExportSetting(ExportFileInfo fileInfo, String fileType) {
		String filePath = fileInfo.getExportSetting().getTargetFilePath();
		if ("BMP".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingBMP());
		} else if ("DWG".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingDWG());
		} else if ("DXF".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingDXF());
		} else if ("GIF".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingGIF());
		} else if ("JPG".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingJPG());
		} else if ("KML".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingKML());
		} else if ("KMZ".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingKMZ());
		} else if ("ModelX".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingModelX());
		} else if ("PNG".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingPNG());
		} else if ("SIT".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingSIT());
		} else if ("TEMSVector".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingTEMSVector());
		} else if ("TEMSClutter".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingTEMSClutter());
		} else if ("TEMSBuildingVector".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingTEMSBuildingVector());
		} else if ("TIF".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingTIF());
		} else if ("VCT".equalsIgnoreCase(fileType)) {
			fileInfo.setExportSetting(new ExportSettingVCT());
		} else {
			fileInfo.setExportSetting(new ExportSetting());
		}
		// 复制目标文件路径到新的exportsetting中
		fileInfo.getExportSetting().setTargetFilePath(filePath);
	}

	/**
	 * 根据不同类型的数据集设置combobox的model
	 * 
	 * @param fileTypes
	 * @param comboBox
	 */
	public static void setComboBoxModel(List<String> fileTypes, JComboBox<String> comboBox) {
		if (null != fileTypes) {
			List<String> fileTypeInfos = new ArrayList<String>();
			for (int i = 0; i < fileTypes.size(); i++) {
				// TODO 导出为Microsoft 文本文件未实现，此处先屏蔽
				if (!"CSV".equals(fileTypes.get(i))) {
					fileTypeInfos.add(DatasetUtil.getDatasetName("", fileTypes.get(i), 1));
				}
			}
			comboBox.setModel(new DefaultComboBoxModel<String>((String[]) fileTypeInfos.toArray(new String[fileTypeInfos.size()])));
		} else {
			comboBox.setModel(new DefaultComboBoxModel<String>());
		}
	}

}
