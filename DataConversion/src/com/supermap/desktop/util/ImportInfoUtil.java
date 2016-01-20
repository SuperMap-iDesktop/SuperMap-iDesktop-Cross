package com.supermap.desktop.util;

import com.supermap.data.Charset;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.conversion.ImportMode;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.Application;
import com.supermap.desktop.ImportFileInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.*;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasourceComboBox;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImportInfoUtil {

	/**
	 * 得到目标数据源
	 *
	 * @param panels
	 * @param fileInfos
	 * @param fileInfo
	 * @param comboBoxDatasource
	 */
	public static void setDataSource(List<JPanel> panels, List<ImportFileInfo> fileInfos, ImportFileInfo fileInfo,
	                                 DatasourceComboBox comboBoxDatasource) {
		ImportInfoUtil util = new ImportInfoUtil();
		comboBoxDatasource.addActionListener(util.new ComboBoxDatasourceListener(comboBoxDatasource, fileInfo, panels, fileInfos));
	}

	/**
	 * 得到选中的数据源
	 *
	 * @param item
	 * @return
	 */
	private static Datasource getSelectDatasource(String item) {
		return Application.getActiveApplication().getWorkspace().getDatasources().get(item);
	}

	/**
	 * 设置设置导入模式
	 *
	 * @param panels
	 * @param importsetting
	 * @param comboBox
	 */
	public static void setImportMode(List<JPanel> panels, ImportSetting importsetting, JComboBox<Object> comboBox) {
		ImportInfoUtil util = new ImportInfoUtil();
		comboBox.addActionListener(util.new ComboBoxListener(panels, importsetting, comboBox));

	}

	/**
	 * 设置编码类型的默认选项
	 *
	 * @param tempPanel
	 * @param dataType
	 */

	private static void setPIItem(ImportPanelPI tempPanel, String dataType) {
		int size = tempPanel.getComboBoxCodingType().getItemCount();
		for (int j = 0; j < size; j++) {
			String item = tempPanel.getComboBoxCodingType().getItemAt(j).toString();
			if (item.equalsIgnoreCase(dataType)) {
				tempPanel.getComboBoxCodingType().setSelectedItem(item);
			}
		}
	}

	/**
	 * 设置编码类型
	 *
	 * @param panels
	 * @param importsetting
	 * @param comboBoxCodingType
	 */

	public static void setCodingType(List<JPanel> panels, ImportSetting importsetting, JComboBox<Object> comboBoxCodingType) {
		ImportInfoUtil util = new ImportInfoUtil();
		comboBoxCodingType.addActionListener(util.new ComboBoxCodingTypeListener(panels, importsetting, comboBoxCodingType));
	}

	/**
	 * 设置数据集类型
	 *
	 * @param panels
	 * @param importsetting
	 * @param comboBoxCharset
	 */

	public static void setCharset(List<JPanel> panels, ImportSetting importsetting, JComboBox<Object> comboBoxCharset) {
		ImportInfoUtil util = new ImportInfoUtil();
		comboBoxCharset.addActionListener(util.new ComboBoxCharsetListener(panels, importsetting, comboBoxCharset));
	}

	/**
	 * 设置ImportSetting的导入模式
	 *
	 * @param item
	 * @param importsetting
	 */
	private static void setImportInfo(int item, ImportSetting importsetting) {
		switch (item) {
			case 0:
				importsetting.setImportMode(ImportMode.NONE);
				break;
			case 1:
				importsetting.setImportMode(ImportMode.APPEND);
				break;
			case 2:
				importsetting.setImportMode(ImportMode.OVERWRITE);
				break;

			default:
				Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("String_ImportModel_Failed"));
				break;
		}
	}

	/**
	 * 判断多个文件的文件类型是否相同
	 *
	 * @param fileInfos
	 * @return
	 */
	public static boolean isSameFiles(List<ImportFileInfo> fileInfos) {
		boolean flag = false;
		HashSet<String> filePaths = new HashSet<String>();
		if (null != fileInfos && 1 < fileInfos.size()) {
			for (int i = 0; i < fileInfos.size(); i++) {
				filePaths.add(fileInfos.get(i).getFilePath());
			}
			if (1 == filePaths.size()) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 显示结果数据集名称，源文件路径并初始化ImportSetting
	 *
	 * @param fileInfos
	 * @param fileInfo
	 * @param textField
	 * @param importsetting
	 * @param textFieldResultSet
	 * @return
	 */
	public static ImportSetting setFileInfo(Datasource datasource, List<ImportFileInfo> fileInfos, ImportFileInfo fileInfo, JTextField textField,
	                                        ImportSetting importsetting,
	                                        JTextField textFieldResultSet) {
		ImportSetting resultSetting = importsetting;
		// 设置显示的文件名及结果数据集名称
		if (null != fileInfo) {
			if (null != textField) {
				textField.setText(fileInfo.getFilePath());
			}
			resultSetting = fileInfo.getImportSetting();
			String fileName = fileInfo.getFileName();
			String resultSetName = fileName.substring(0, fileName.lastIndexOf("."));
			textFieldResultSet.setText(resultSetName);
			resultSetting.setTargetDatasource(datasource);
		} else {
			if (isSameFiles(fileInfos)) {
				if (null != textField) {
					textField.setText(fileInfos.get(0).getFilePath());
				}
				String fileName = fileInfos.get(0).getFileName();
				String resultSetName = fileName.substring(0, fileName.lastIndexOf("."));
				textFieldResultSet.setText(resultSetName);
			} else {
				for (int i = 0; i < fileInfos.size(); i++) {
					ImportFileInfo tempFileInfo = fileInfos.get(i);
					ImportSetting tempSetting = tempFileInfo.getImportSetting();
					tempSetting.setTargetDatasource(datasource);
				}
			}

		}
		return resultSetting;
	}

	/**
	 * 设置importsetting的目标数据集名称
	 *
	 * @param textFieldResultSet
	 * @param importsetting
	 */
	public static void setDatasetName(JTextField textFieldResultSet, ImportSetting importsetting) {

		if (null != importsetting) {
			ImportInfoUtil fileInfoUtil = new ImportInfoUtil();
			textFieldResultSet.getDocument().addDocumentListener(fileInfoUtil.new DocumentChangeListener(textFieldResultSet, importsetting));
		}
	}

	/**
	 * 文本内容修改监听类
	 *
	 * @author xie
	 */
	class DocumentChangeListener implements DocumentListener {
		private JTextField textFieldResultSet;
		private ImportSetting importsetting;

		public DocumentChangeListener(JTextField textFieldResultSet, ImportSetting importsetting) {
			this.textFieldResultSet = textFieldResultSet;
			this.importsetting = importsetting;
		}

		private void setTargetDatasetName() {
			String text = "";
			text = textFieldResultSet.getText();
			if (!text.isEmpty()) {
				importsetting.setTargetDatasetName(text);
			}
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setTargetDatasetName();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setTargetDatasetName();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setTargetDatasetName();
		}

	}

	/**
	 * 数据源下拉选择框事件响应类
	 *
	 * @author xie
	 */
	class ComboBoxDatasourceListener implements ActionListener {
		private DatasourceComboBox comboBoxDatasource;
		private ImportFileInfo fileInfo;
		private ArrayList<JPanel> panels;
		private ArrayList<ImportFileInfo> fileInfos;

		public ComboBoxDatasourceListener(DatasourceComboBox comboBoxDatasource, ImportFileInfo fileInfo, List<JPanel> panels,
		                                  List<ImportFileInfo> fileInfos) {
			this.comboBoxDatasource = comboBoxDatasource;
			this.fileInfo = fileInfo;
			this.panels = (ArrayList<JPanel>) panels;
			this.fileInfos = (ArrayList<ImportFileInfo>) fileInfos;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comboBoxDatasourceChange(comboBoxDatasource, fileInfo, panels, fileInfos);
		}

	}

	/**
	 * 导入模式下拉选择框事件响应类
	 *
	 * @author xie
	 */
	class ComboBoxListener implements ActionListener {
		private ArrayList<JPanel> panels;
		private ImportSetting importsetting;
		private JComboBox<Object> comboBox;

		public ComboBoxListener(List<JPanel> panels, ImportSetting importsetting, JComboBox<Object> comboBox) {
			this.panels = (ArrayList<JPanel>) panels;
			this.importsetting = importsetting;
			this.comboBox = comboBox;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comboBoxChange(comboBox, importsetting, panels);
		}

	}

	/**
	 * 编码类型下拉选择框事件响应类
	 *
	 * @author xie
	 */
	class ComboBoxCodingTypeListener implements ActionListener {
		private ArrayList<JPanel> panels;
		private ImportSetting importsetting;
		private JComboBox<Object> comboBoxCodingType;

		public ComboBoxCodingTypeListener(List<JPanel> panels, ImportSetting importsetting, JComboBox<Object> comboBoxCodingType) {
			this.panels = (ArrayList<JPanel>) panels;
			this.importsetting = importsetting;
			this.comboBoxCodingType = comboBoxCodingType;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comboBoxCodingTypeChange(comboBoxCodingType, importsetting, panels);
		}

	}

	class ComboBoxCharsetListener implements ActionListener {
		private ArrayList<JPanel> panels;
		private ImportSetting importsetting;
		private JComboBox<Object> comboBoxCharset;

		public ComboBoxCharsetListener(List<JPanel> panels, ImportSetting importsetting, JComboBox<Object> comboBoxCharset) {
			this.panels = (ArrayList<JPanel>) panels;
			this.importsetting = importsetting;
			this.comboBoxCharset = comboBoxCharset;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			comboBoxCharsetChange(comboBoxCharset, importsetting, panels);
		}

	}

	/**
	 * 点击导入模式下拉选择框，修改导入的导入模式
	 *
	 * @param comboBox
	 * @param importsetting
	 * @param panels
	 */
	private void comboBoxChange(JComboBox<Object> comboBox, ImportSetting importsetting, ArrayList<JPanel> panels) {
		int item = comboBox.getSelectedIndex();
		if (null != importsetting) {
			setImportInfo(item, importsetting);
		} else if (null != panels) {
			for (int i = 0; i < panels.size(); i++) {
				JPanel tempJPanel = panels.get(i);
				if (tempJPanel instanceof ImportPanelPI) {
					((ImportPanelPI) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelSHP) {
					((ImportPanelSHP) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelTIF) {
					((ImportPanelTIF) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelD) {
					((ImportPanelD) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelMapGIS) {
					((ImportPanelMapGIS) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelArcGIS) {
					((ImportPanelArcGIS) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelIMG) {
					((ImportPanelIMG) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelModel) {
					((ImportPanelModel) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelMapInfo) {
					((ImportPanelMapInfo) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelCSV) {
					((ImportPanelCSV) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelWOR) {
					((ImportPanelWOR) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelE00) {
					((ImportPanelE00) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelKML) {
					((ImportPanelKML) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelSIT) {
					((ImportPanelSIT) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelSCV) {
					((ImportPanelSCV) tempJPanel).getComboBox().setSelectedIndex(item);
				}
				if (tempJPanel instanceof ImportPanelLIDAR) {
					((ImportPanelLIDAR) tempJPanel).getComboBox().setSelectedIndex(item);
				}
			}
		}
	}

	/**
	 * 点击目标数据源下拉选择框，修改导入的目标数据源
	 *
	 * @param comboBoxDatasource
	 * @param fileInfo
	 * @param panels
	 * @param fileInfos
	 */
	private void comboBoxDatasourceChange(DatasourceComboBox comboBoxDatasource, ImportFileInfo fileInfo, ArrayList<JPanel> panels,
	                                      ArrayList<ImportFileInfo> fileInfos) {

		String item = comboBoxDatasource.getSelectItem();
		DataCell cell = (DataCell) comboBoxDatasource.getSelectedItem();
		if (null != fileInfo) {
			ImportSetting importSetting = fileInfo.getImportSetting();
			importSetting.setTargetDatasource(getSelectDatasource(item));
		} else if (null != panels) {
			for (int i = 0; i < fileInfos.size(); i++) {
				ImportFileInfo tempFileInfo = fileInfos.get(i);
				ImportSetting tempImportSetting = tempFileInfo.getImportSetting();
				tempImportSetting.setTargetDatasource(getSelectDatasource(item));
				JPanel tempJPanel = panels.get(i);
				if (tempJPanel instanceof ImportPanelPI) {
					((ImportPanelPI) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelSHP) {
					((ImportPanelSHP) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelTIF) {
					((ImportPanelTIF) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelD) {
					((ImportPanelD) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelMapGIS) {
					((ImportPanelMapGIS) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelArcGIS) {
					((ImportPanelArcGIS) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelIMG) {
					((ImportPanelIMG) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelModel) {
					((ImportPanelModel) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelMapInfo) {
					((ImportPanelMapInfo) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelCSV) {
					((ImportPanelCSV) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelWOR) {
					((ImportPanelWOR) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelE00) {
					((ImportPanelE00) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelKML) {
					((ImportPanelKML) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelSIT) {
					((ImportPanelSIT) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelSCV) {
					((ImportPanelSCV) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
				if (tempJPanel instanceof ImportPanelLIDAR) {
					((ImportPanelLIDAR) tempJPanel).getComboBoxDatasource().setSelectedItem(cell);
				}
			}
		}

	}

	/**
	 * 点击编码类型下拉选择框，修改导入的编码类型
	 *
	 * @param comboBoxCodingType
	 * @param importsetting
	 * @param panels
	 */
	private void comboBoxCodingTypeChange(JComboBox<Object> comboBoxCodingType, ImportSetting importsetting, ArrayList<JPanel> panels) {

		String codingType = comboBoxCodingType.getSelectedItem().toString();
		if (null != importsetting) {
			setCodingInfo(codingType, importsetting);
		} else if (null != panels) {
			for (int i = 0; i < panels.size(); i++) {
				JPanel tempJPanel = panels.get(i);
				if (tempJPanel instanceof ImportPanelPI) {
					setPIItem((ImportPanelPI) tempJPanel, codingType);
				}
				if (tempJPanel instanceof ImportPanelSHP) {
					int size = ((ImportPanelSHP) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelSHP) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelSHP) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelTIF) {
					int size = ((ImportPanelTIF) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelTIF) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelTIF) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelD) {
					int size = ((ImportPanelD) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelD) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelD) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelMapGIS) {
					int size = ((ImportPanelMapGIS) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelMapGIS) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelMapGIS) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelArcGIS) {
					int size = ((ImportPanelArcGIS) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelArcGIS) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelArcGIS) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelIMG) {
					int size = ((ImportPanelIMG) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelIMG) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelIMG) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelMapInfo) {
					int size = ((ImportPanelMapInfo) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelMapInfo) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelMapInfo) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelWOR) {
					int size = ((ImportPanelWOR) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelWOR) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelWOR) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelE00) {
					int size = ((ImportPanelE00) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelE00) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelE00) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelKML) {
					int size = ((ImportPanelKML) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelKML) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelKML) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelSIT) {
					int size = ((ImportPanelSIT) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelSIT) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelSIT) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
				if (tempJPanel instanceof ImportPanelLIDAR) {
					int size = ((ImportPanelLIDAR) tempJPanel).getComboBoxCodingType().getItemCount();
					for (int j = 0; j < size; j++) {
						String item = ((ImportPanelLIDAR) tempJPanel).getComboBoxCodingType().getItemAt(j).toString();
						if (item.equalsIgnoreCase(codingType)) {
							((ImportPanelLIDAR) tempJPanel).getComboBoxCodingType().setSelectedItem(item);
						}
					}
				}
			}

		}

	}

	/**
	 * 点击字符集下拉选择框，修改导入的字符集
	 *
	 * @param comboBoxCharset
	 * @param importsetting
	 * @param panels
	 */
	private void comboBoxCharsetChange(JComboBox<Object> comboBoxCharset, ImportSetting importsetting, ArrayList<JPanel> panels) {

		int type = comboBoxCharset.getSelectedIndex();
		if (null != importsetting) {
			setCharInfo(type, importsetting);
		} else if (null != panels) {
			for (int i = 0; i < panels.size(); i++) {
				JPanel tempJPanel = panels.get(i);
				if (tempJPanel instanceof ImportPanelPI) {
					((ImportPanelPI) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelSHP) {
					((ImportPanelSHP) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelTIF) {
					((ImportPanelTIF) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelMapGIS) {
					((ImportPanelMapGIS) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelArcGIS) {
					((ImportPanelArcGIS) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelIMG) {
					((ImportPanelIMG) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelModel) {
					((ImportPanelModel) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelMapInfo) {
					((ImportPanelMapInfo) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelCSV) {
					((ImportPanelCSV) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelWOR) {
					((ImportPanelWOR) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelE00) {
					((ImportPanelE00) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelKML) {
					((ImportPanelKML) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelSIT) {
					((ImportPanelSIT) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
				if (tempJPanel instanceof ImportPanelLIDAR) {
					((ImportPanelLIDAR) tempJPanel).getComboBoxCharset().setSelectedIndex(type);
				}
			}
		}

	}

	/**
	 * 设置编码类型
	 *
	 * @param codingType
	 * @param importsetting
	 */
	private static void setCodingInfo(String codingType, ImportSetting importsetting) {
		if ("none".equalsIgnoreCase(codingType) || "未编码".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.NONE);
		}
		if ("BYTE".equalsIgnoreCase(codingType) || "单字节".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.BYTE);
		}
		if ("INT16".equalsIgnoreCase(codingType) || "双字节".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.INT16);
		}
		if ("INT24".equalsIgnoreCase(codingType) || "三字节".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.INT24);
		}
		if ("INT32".equalsIgnoreCase(codingType) || "四字节".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.INT32);
		}
		if ("DCT".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.DCT);
		}
		if ("SGL".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.SGL);
		}
		if ("LZW".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.LZW);
		}
		if ("PNG".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.PNG);
		}
		if ("COMPOUND".equalsIgnoreCase(codingType)) {
			importsetting.setEncodeType(EncodeType.COMPOUND);
		}
	}

	/**
	 * 设置源文件字符集
	 *
	 * @param type
	 * @param importsetting
	 */
	private static void setCharInfo(int type, ImportSetting importsetting) {
		switch (type) {
			case 0:
				importsetting.setSourceFileCharset(Charset.ANSI);
				break;
			case 1:
				importsetting.setSourceFileCharset(Charset.DEFAULT);
				break;
			case 2:
				importsetting.setSourceFileCharset(Charset.OEM);
				break;
			case 3:
				importsetting.setSourceFileCharset(Charset.CHINESEBIG5);
				break;
			case 4:
				importsetting.setSourceFileCharset(Charset.GB18030);
				break;
			case 5:
				importsetting.setSourceFileCharset(Charset.CYRILLIC);
				break;
			case 6:
				importsetting.setSourceFileCharset(Charset.XIA5);
				break;
			case 7:
				importsetting.setSourceFileCharset(Charset.XIA5GERMAN);
				break;
			case 8:
				importsetting.setSourceFileCharset(Charset.XIA5NORWEGIAN);
				break;
			case 9:
				importsetting.setSourceFileCharset(Charset.XIA5SWEDISH);
				break;
			case 10:
				importsetting.setSourceFileCharset(Charset.MAC);
				break;
			case 11:
				importsetting.setSourceFileCharset(Charset.UNICODE);
				break;
			case 12:
				importsetting.setSourceFileCharset(Charset.UTF7);
				break;
			case 13:
				importsetting.setSourceFileCharset(Charset.UTF8);
				break;
			case 14:
				importsetting.setSourceFileCharset(Charset.WINDOWS1252);
				break;
			case 15:
				importsetting.setSourceFileCharset(Charset.ARABIC);
				break;
			case 16:
				importsetting.setSourceFileCharset(Charset.BALTIC);
				break;
			case 17:
				importsetting.setSourceFileCharset(Charset.JOHAB);
				break;
			case 18:
				importsetting.setSourceFileCharset(Charset.HANGEUL);
				break;
			case 19:
				importsetting.setSourceFileCharset(Charset.EASTEUROPE);
				break;
			case 20:
				importsetting.setSourceFileCharset(Charset.RUSSIAN);
				break;
			case 21:
				importsetting.setSourceFileCharset(Charset.SYMBOL);
				break;
			case 22:
				importsetting.setSourceFileCharset(Charset.KOREAN);
				break;
			case 23:
				importsetting.setSourceFileCharset(Charset.SHIFTJIS);
				break;
			case 24:
				importsetting.setSourceFileCharset(Charset.THAI);
				break;
			case 25:
				importsetting.setSourceFileCharset(Charset.TURKISH);
				break;
			case 26:
				importsetting.setSourceFileCharset(Charset.HEBREW);
				break;
			case 27:
				importsetting.setSourceFileCharset(Charset.GREEK);
				break;
			case 28:
				importsetting.setSourceFileCharset(Charset.VIETNAMESE);
				break;
			default:
				Application.getActiveApplication().getOutput().output(DataConversionProperties.getString("String_Charset_Failed"));
				break;
		}
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param worldFile
	 * @return
	 */
	public static boolean isExtendsFile(String worldFile) {
		File file = new File(worldFile);
		if (file.exists()) {
			return true;
		}
		return false;
	}

}