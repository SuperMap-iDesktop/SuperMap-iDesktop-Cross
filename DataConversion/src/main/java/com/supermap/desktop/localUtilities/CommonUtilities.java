package com.supermap.desktop.localUtilities;

import com.supermap.data.DatasetVector;
import com.supermap.data.conversion.FileType;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ExportFileInfo;
import com.supermap.desktop.implement.UserDefineType.GPXAnalytic;
import com.supermap.desktop.implement.UserDefineType.UserDefineFileType;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/28.
 */
public class CommonUtilities {
	private CommonUtilities() {
		//工具类不提供公共构造方法
	}

	/**
	 * 修改JComboBox的显示方式
	 *
	 * @param comboBox
	 */
	public static void setComboBoxTheme(JComboBox comboBox) {
		comboBox.setEditable(true);
		((JTextField) comboBox.getEditor().getEditorComponent()).setEditable(false);
	}

	/**
	 * 全选
	 *
	 * @param table
	 */
	public static void selectAll(JTable table) {
		if (table.getRowCount() - 1 < 0) {
			table.setRowSelectionAllowed(true);
		} else {
			table.setRowSelectionAllowed(true);
			// 设置所有项全部选中
			table.setRowSelectionInterval(0, table.getRowCount() - 1);
		}
	}

	/**
	 * 反选
	 *
	 * @param table
	 */
	public static void invertSelect(JTable table) {
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
	}

	/**
	 * 替换父容器的panel为新的panel
	 *
	 * @param parentPanel
	 * @param newPanel
	 */
	public static void replace(JPanel parentPanel, JPanel newPanel) {
		for (int i = 0; i < parentPanel.getComponentCount(); i++) {
			if (parentPanel.getComponent(i) instanceof JPanel) {
				JPanel panel = (JPanel) parentPanel.getComponent(i);
				panel.removeAll();
				panel.add(newPanel, new GridBagConstraintsHelper(0, 0).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
				panel.revalidate();
				break;
			}
		}
		parentPanel.repaint();
	}

	/**
	 * 根据fileType资源化
	 *
	 * @param fileType
	 * @return
	 */
	public static String getDatasetName(String fileType) {
		String resultFileType = "";
		if (UserDefineFileType.GPX.toString().equals(fileType)) {
			//GPX type reflect GPS
			fileType = "GPS";
		} else if (UserDefineFileType.EXCEL.toString().equals(fileType)) {
			fileType = "Excel";
		}
		String tempFileType = "String_FileType" + fileType;
		if (tempFileType.contains(resultFileType)) {
			boolean isVisibleName = true;
			try {
				DataConversionProperties.getString(tempFileType);
			} catch (Exception e) {
				// 此处通过抛出异常来确定导出类型是否已经对应
				isVisibleName = false;
			}
			if (isVisibleName) {
				resultFileType = DataConversionProperties.getString(tempFileType);
			}
		}
		return resultFileType;
	}

	/**
	 * 选中不同的数据是否有支持的导出文件类型，如果有就返回支持的导出文件类型，没有就返回空
	 *
	 * @param exportFileInfos
	 * @return
	 */
	public static ArrayList<String> getSameFileTypes(ArrayList<ExportFileInfo> exportFileInfos) {
		ArrayList<String> sameFileType = new ArrayList();
		if (!exportFileInfos.isEmpty()) {
			ExportFileInfo tempExportFileInfo = exportFileInfos.get(0);
			boolean isGPX = false;
			if (tempExportFileInfo.getExportSetting().getSourceData() instanceof DatasetVector) {
				isGPX = GPXAnalytic.isGPXType((DatasetVector) tempExportFileInfo.getExportSetting().getSourceData());
			}
			if (isGPX) {
				sameFileType.add(UserDefineFileType.GPX.toString());
			}
			FileType[] fileTypes = tempExportFileInfo.getExportSetting().getSupportedFileType();
			for (int i = 0; i < fileTypes.length; i++) {
				sameFileType.add(fileTypes[i].name());
			}
			if (tempExportFileInfo.getExportSetting().getSourceData() instanceof DatasetVector) {
				sameFileType.add(UserDefineFileType.EXCEL.toString());
			}
			for (int i = 0; i < exportFileInfos.size(); i++) {
				ArrayList<String> tempFileTypes = new ArrayList<String>();
				boolean hasGPS = false;
				if (exportFileInfos.get(i).getExportSetting().getSourceData() instanceof DatasetVector) {
					hasGPS = GPXAnalytic.isGPXType((DatasetVector) exportFileInfos.get(i).getExportSetting().getSourceData());
				}
				if (hasGPS) {
					tempFileTypes.add(UserDefineFileType.GPX.toString());
				}
				FileType[] compare = exportFileInfos.get(i).getExportSetting().getSupportedFileType();
				for (int j = 0; j < compare.length; j++) {
					tempFileTypes.add(compare[j].name());
				}
				if (exportFileInfos.get(i).getExportSetting().getSourceData() instanceof DatasetVector) {
					tempFileTypes.add(UserDefineFileType.EXCEL.toString());
				}
				sameFileType.retainAll(tempFileTypes);
			}
		}
		if (sameFileType.isEmpty()) {
			return new ArrayList();
		}
		return sameFileType;
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean isExtendsFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}
