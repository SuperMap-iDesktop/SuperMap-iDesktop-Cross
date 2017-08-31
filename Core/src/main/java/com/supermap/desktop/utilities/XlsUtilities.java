package com.supermap.desktop.utilities;

import com.supermap.data.*;
import com.supermap.data.conversion.ExportSteppedEvent;
import com.supermap.data.conversion.ImportSteppedEvent;
import com.supermap.desktop.Application;
import com.supermap.desktop.implement.UserDefineType.ExportSettingExcel;
import com.supermap.desktop.implement.UserDefineType.ImportSettingExcel;
import com.supermap.desktop.implement.UserDefineType.UserDefineExportResult;
import com.supermap.desktop.implement.UserDefineType.UserDefineImportResult;
import com.supermap.desktop.properties.CommonProperties;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by xie on 2017/8/24.
 */
public class XlsUtilities {
	public static ImportSettingExcel importSettingExcel;
	public static ExportSettingExcel exportSettingExcel;

	/**
	 * 读取csv文件，将结果
	 *
	 * @param file csv文件(路径+文件)
	 * @return
	 */
	public static ArrayList<String> readCsv(File file) {
		ArrayList<String> result = new ArrayList<>();
		BufferedReader br = null;
		try {
			FileInputStream in = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(in, "GBK"));
			String line = "";
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 将xls数据放入二维集合中
	 *
	 * @param filePath
	 * @return
	 */
	public static ArrayList<Vector<Vector>> getXLSInfo(String filePath) {
		ArrayList<Vector<Vector>> result = new ArrayList<>();
		try {
			File xlsFile = new File(filePath);
			if (!xlsFile.exists()) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(CommonProperties.getString("String_FileNotExistsError"), filePath));
				return null;
			}
			FileInputStream stream = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(stream);
			HSSFSheet sheet;
			HSSFRow row;
			int rowCount;
			int columnCount = 0;
			Vector<Vector> vectors = new Vector<>();
			Vector vector = new Vector();
			int sheetCount = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				vectors.clear();
				sheet = workbook.getSheetAt(i);
				//获取总行数
				rowCount = sheet.getPhysicalNumberOfRows();
				//
				if (rowCount > 0) {
					//获取总列数
					columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
				}
				for (int j = 0; j < rowCount; j++) {
					vector.clear();
					row = sheet.getRow(j);
					for (int k = 0; k < columnCount; k++) {
						vector.add(row.getCell(k).getStringCellValue());
					}
					vectors.add(vector);
				}
				result.add(vectors);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 导入扩展名为xls的excel
	 *
	 * @param datasource      目标数据源
	 * @param filePath        文件目录
	 * @param importFieldName 是否将首行导入为字段名称
	 */
	public static UserDefineImportResult[] importXlsxFile(Datasource datasource, String filePath, boolean importFieldName) {
		UserDefineImportResult[] importResults = null;
		try {
			File xlsFile = new File(filePath);
			if (!xlsFile.exists()) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(CommonProperties.getString("String_FileNotExistsError"), filePath));
				return null;
			}
			String xlsName = xlsFile.getName();
			xlsName = xlsName.substring(0, xlsName.indexOf("."));
			FileInputStream stream = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(stream);
			XSSFSheet sheet;
			XSSFRow row;
			DatasetVector dataset;
			DatasetVectorInfo info;
			FieldInfos fieldInfos;
			FieldInfo fieldInfo = null;
			Recordset recordset;
			int columnCount = 0;
			int rowCount;
			ArrayList<String> fieldNames = new ArrayList<>();
			int sheetSize = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetSize; i++) {
				sheet = workbook.getSheetAt(i);
				//总行数
				rowCount = sheet.getPhysicalNumberOfRows();
				if (rowCount > 0) {
					row = sheet.getRow(0);
					//总列数
					columnCount = row.getPhysicalNumberOfCells();
				}
				info = new DatasetVectorInfo();
				Datasets datasets = datasource.getDatasets();
				info.setName(datasets.getAvailableDatasetName(xlsName + "_" + sheet.getSheetName()));
				info.setType(DatasetType.TABULAR);
				dataset = datasets.create(info);
				fieldInfos = dataset.getFieldInfos();
				if (importFieldName) {
					for (int j = 0; j < columnCount; j++) {
						fieldInfo = new FieldInfo();
						fieldInfo.setType(FieldType.TEXT);
						String name = "NewField" + "_" + sheet.getRow(0).getCell(j).getStringCellValue();
						name = "NewField" + "_" + sheet.getRow(0).getCell(j).getStringCellValue();
						fieldInfo.setName(name);
						fieldInfos.add(fieldInfo);
						fieldNames.add(name);
					}
				} else {
					for (int j = 0; j < columnCount; j++) {
						fieldInfo = new FieldInfo();
						fieldInfo.setType(FieldType.TEXT);
						String name = j == 0 ? "NewField" : "NewField" + "_" + String.valueOf(j);
						fieldInfo.setName(name);
						fieldInfos.add(fieldInfo);
						fieldNames.add(name);
					}
				}
				recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
				Recordset.BatchEditor editor = recordset.getBatch();
				editor.begin();
				HashMap map = new HashMap();
				int j = 0;
				int resultCount = rowCount;
				if (importFieldName) {
					j = 1;
					resultCount = rowCount - 1;
				}
				for (; j < rowCount; j++) {
					row = sheet.getRow(j);
					map.clear();
					for (int k = 0; k < columnCount; k++) {
						map.put(fieldNames.get(k), row.getCell(k).getStringCellValue());
					}
					boolean importResult = recordset.addNew(null, map);
					if (importResult && null != importSettingExcel) {
						//刷新进度
						int subPercent = (j + 1) * 100 / resultCount;
						importSettingExcel.fireStepped(new ImportSteppedEvent(importSettingExcel, 100, subPercent, importSettingExcel, sheetSize, false));
					}
				}
				editor.update();

				if (resultCount == dataset.getRecordCount() && null != importSettingExcel) {
					importResults = new UserDefineImportResult[sheetSize];
					importSettingExcel.setTargetDatasetName(dataset.getName());
					importResults[i] = new UserDefineImportResult(importSettingExcel, null);
				} else if (resultCount != dataset.getRecordCount() && null != importSettingExcel) {
					importResults = new UserDefineImportResult[sheetSize];
					importResults[i] = new UserDefineImportResult(null, importSettingExcel);
				}
				recordset.dispose();
				fieldInfo.dispose();
			}
		} catch (Exception e) {
			//todo 为字段设置名称时有可能异常（首行作为字段信息时，数据库字段类型长度名称重复问题）,直接输出异常终止导入
			Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_FieldName_Illegal"));
		}
		return importResults;
	}

	/**
	 * 导入扩展名为xls的excel
	 *
	 * @param datasource      目标数据源
	 * @param filePath        文件目录
	 * @param importFieldName 是否将首行导入为字段名称
	 */
	public static UserDefineImportResult[] importXlsFile(Datasource datasource, String filePath, boolean importFieldName) {
		UserDefineImportResult[] importResults = null;
		try {
			File xlsFile = new File(filePath);
			if (!xlsFile.exists()) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(CommonProperties.getString("String_FileNotExistsError"), filePath));
				return null;
			}
			String xlsName = xlsFile.getName();
			xlsName = xlsName.substring(0, xlsName.indexOf("."));
			FileInputStream stream = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(stream);
			HSSFSheet sheet;
			HSSFRow row;
			DatasetVector dataset;
			DatasetVectorInfo info;
			FieldInfos fieldInfos;
			FieldInfo fieldInfo = null;
			Recordset recordset;
			int columnCount = 0;
			int rowCount;
			ArrayList<String> fieldNames = new ArrayList<>();
			int sheetSize = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetSize; i++) {
				sheet = workbook.getSheetAt(i);
				//总行数
				rowCount = sheet.getPhysicalNumberOfRows();
				if (rowCount > 0) {
					row = sheet.getRow(0);
					//总列数
					columnCount = row.getPhysicalNumberOfCells();
				}
				info = new DatasetVectorInfo();
				Datasets datasets = datasource.getDatasets();
				info.setName(datasets.getAvailableDatasetName(xlsName + "_" + sheet.getSheetName()));
				info.setType(DatasetType.TABULAR);
				dataset = datasets.create(info);
				fieldInfos = dataset.getFieldInfos();
				if (importFieldName) {
					for (int j = 0; j < columnCount; j++) {
						fieldInfo = new FieldInfo();
						fieldInfo.setType(FieldType.TEXT);
						String name = "NewField" + "_" + sheet.getRow(0).getCell(j).getStringCellValue();
						fieldInfo.setName(name);
						fieldInfos.add(fieldInfo);
						fieldNames.add(name);
					}
				} else {
					for (int j = 0; j < columnCount; j++) {
						fieldInfo = new FieldInfo();
						fieldInfo.setType(FieldType.TEXT);
						String name = j == 0 ? "NewField" : "NewField" + "_" + String.valueOf(j);
						fieldInfo.setName(name);
						fieldInfos.add(fieldInfo);
						fieldNames.add(name);
					}
				}
				recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
				Recordset.BatchEditor editor = recordset.getBatch();
				editor.begin();
				HashMap map = new HashMap();
				int j = 0;
				int resultCount = rowCount;
				if (importFieldName) {
					j = 1;
					resultCount = rowCount - 1;
				}
				for (; j < rowCount; j++) {
					row = sheet.getRow(j);
					map.clear();
					for (int k = 0; k < columnCount; k++) {
						map.put(fieldNames.get(k), row.getCell(k).getStringCellValue());
					}
					boolean importResult = recordset.addNew(null, map);
					if (importResult && null != importSettingExcel) {
						//刷新进度
						int subPercent = (j + 1) * 100 / resultCount;
						importSettingExcel.fireStepped(new ImportSteppedEvent(importSettingExcel, 100, subPercent, importSettingExcel, sheetSize, false));
					}
				}
				editor.update();
				if (resultCount == dataset.getRecordCount() && null != importSettingExcel) {
					importResults = new UserDefineImportResult[sheetSize];
					importSettingExcel.setTargetDatasetName(dataset.getName());
					importResults[i] = new UserDefineImportResult(importSettingExcel, null);
				} else if (resultCount != dataset.getRecordCount() && null != importSettingExcel) {
					importResults = new UserDefineImportResult[sheetSize];
					importResults[i] = new UserDefineImportResult(null, importSettingExcel);
				}
				recordset.dispose();
				fieldInfo.dispose();
			}
		} catch (Exception e) {
			//todo 为字段设置名称时有可能异常（首行作为字段信息时，数据库字段类型长度名称重复问题）,直接输出异常终止导入
			Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_FieldName_Illegal"));
		}
		return importResults;
	}

	/**
	 * 利用apache.poi实现将数据集导出为扩展名为xlsx的excel文件
	 *
	 * @param dataset
	 * @param filePath
	 * @param exportFieldName
	 */

	public static UserDefineExportResult exportXlsxFile(DatasetVector dataset, String filePath, boolean exportFieldName) {
		return exportXlsxFile(dataset, filePath, exportFieldName, null);
	}

	/**
	 * 利用apache.poi实现将数据集导出为扩展名为xlsx的excel文件
	 *
	 * @param dataset         数据集
	 * @param filePath        文件目录
	 * @param exportFieldName 是否导出字段名称
	 * @param fieldNames      指定导出的字段集合，为空时全部导出
	 */
	public static UserDefineExportResult exportXlsxFile(DatasetVector dataset, String filePath, boolean exportFieldName, String[] fieldNames) {
		UserDefineExportResult result = null;
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();//产生工作簿对象
			XSSFSheet sheet = workbook.createSheet();//产生工作表对象
			XSSFRow row;//行
			XSSFCell cell;//单元格
			DecimalFormat decimalFormat = new DecimalFormat("######0.00000000");
			FieldInfos fieldInfos = dataset.getFieldInfos();
			Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
			recordset.moveFirst();
			int rowCount = 0;
			if (true == exportFieldName) {
				//导出字段信息
				row = sheet.createRow(rowCount);
				int column = 0;
				for (int i = 0, count = fieldInfos.getCount(); i < count; i++) {
					if (null == fieldNames) {
						cell = row.createCell(i);
						cell.setCellValue(fieldInfos.get(i).getName());
					} else if (null != fieldNames && isSelectedFieldName(fieldInfos.get(i).getName(), fieldNames)) {
						cell = row.createCell(column);
						cell.setCellValue(fieldInfos.get(i).getName());
						column++;
					}
				}
				rowCount++;
			}
			while (!recordset.isEOF()) {
				row = sheet.createRow(rowCount);
				int column = 0;
				for (int i = 0, count = fieldInfos.getCount(); i < count; i++) {
					sheet.setColumnWidth(i, 14 * 256);
					String fieldName = fieldInfos.get(i).getName();
					Object cellValue = null;
					FieldType fieldType = fieldInfos.get(i).getType();
					if (null == fieldNames) {
						cell = row.createCell(i);
						cellValue = recordset.getFieldValue(i);
						setCellValue(sheet, cell, decimalFormat, i, cellValue, fieldType);
					} else if (null != fieldNames && isSelectedFieldName(fieldName, fieldNames)) {
						cell = row.createCell(column);
						cellValue = recordset.getFieldValue(fieldName);
						setCellValue(sheet, cell, decimalFormat, column, cellValue, fieldType);
						column++;
					}
				}
				recordset.moveNext();
				rowCount++;
				if (null != exportSettingExcel) {
					int percent = rowCount * 100 / dataset.getRecordCount();
					ExportSteppedEvent steppedEvent = new ExportSteppedEvent(exportSettingExcel, 100, percent, exportSettingExcel, 1, false);
					exportSettingExcel.fireStepped(steppedEvent);
				}
			}
			FileOutputStream fOut = new FileOutputStream(filePath);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
			if (new File(filePath).exists()) {
				result = new UserDefineExportResult(exportSettingExcel, null);
			} else {
				result = new UserDefineExportResult(null, exportSettingExcel);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 利用apache.poi实现将数据集导出为扩展名为xls的excel文件
	 *
	 * @param dataset
	 * @param filePath
	 * @param exportFieldName
	 */

	public static void exportXlsFile(DatasetVector dataset, String filePath, boolean exportFieldName) {
		exportXlsFile(dataset, filePath, exportFieldName, null);
	}

	/**
	 * 利用apache.poi实现将数据集导出为扩展名为xls的excel文件
	 *
	 * @param dataset         数据集
	 * @param filePath        文件目录
	 * @param exportFieldName 是否导出字段名称
	 * @param fieldNames      指定导出的字段集合，为空时全部导出
	 */
	public static void exportXlsFile(DatasetVector dataset, String filePath, boolean exportFieldName, String[]
			fieldNames) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();//产生工作簿对象
			HSSFSheet sheet = workbook.createSheet();//产生工作表对象
			HSSFRow row;//行
			HSSFCell cell;//单元格
			DecimalFormat decimalFormat = new DecimalFormat("######0.00000000");
			FieldInfos fieldInfos = dataset.getFieldInfos();
			Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
			recordset.moveFirst();
			int rowCount = 0;
			if (true == exportFieldName) {
				//导出字段信息
				row = sheet.createRow(rowCount);
				int column = 0;
				for (int i = 0, count = fieldInfos.getCount(); i < count; i++) {
					if (null == fieldNames) {
						cell = row.createCell(i);
						cell.setCellValue(fieldInfos.get(i).getName());
					} else if (null != fieldNames && isSelectedFieldName(fieldInfos.get(i).getName(), fieldNames)) {
						cell = row.createCell(column);
						cell.setCellValue(fieldInfos.get(i).getName());
						column++;
					}
				}
				rowCount++;
			}
			while (!recordset.isEOF()) {
				row = sheet.createRow(rowCount);
				int column = 0;
				for (int i = 0, count = fieldInfos.getCount(); i < count; i++) {
					sheet.setColumnWidth(i, 14 * 256);
					String fieldName = fieldInfos.get(i).getName();
					Object cellValue = null;
					FieldType fieldType = fieldInfos.get(i).getType();
					if (null == fieldNames) {
						cell = row.createCell(i);
						cellValue = recordset.getFieldValue(i);
						setCellValue(sheet, cell, decimalFormat, i, cellValue, fieldType);
					} else if (null != fieldNames && isSelectedFieldName(fieldName, fieldNames)) {
						cell = row.createCell(column);
						cellValue = recordset.getFieldValue(fieldName);
						setCellValue(sheet, cell, decimalFormat, column, cellValue, fieldType);
						column++;
					}

				}
				recordset.moveNext();
				rowCount++;
			}
			FileOutputStream fOut = new FileOutputStream(filePath);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * 将数据转换为格式化的字符串并填充到单元格中
	 *
	 * @param sheet
	 * @param cell
	 * @param decimalFormat
	 * @param column
	 * @param cellValue
	 * @param fieldType
	 */
	private static void setCellValue(XSSFSheet sheet, XSSFCell cell, DecimalFormat decimalFormat, int column, Object
			cellValue, FieldType fieldType) {
		if (null != cellValue) {
			if (fieldType == FieldType.DATETIME) {
				//此处处理为格式化的字符串类型
				sheet.setColumnWidth(column, 20 * 256);
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
				cell.setCellValue(format.format((Date) cellValue));
			} else if (fieldType == FieldType.DOUBLE || fieldType == FieldType.SINGLE) {
				//浮点型格式化
				cell.setCellValue(decimalFormat.format(cellValue));
			} else {
				cell.setCellValue(cellValue.toString());
			}
		} else {
			cell.setCellValue("");
		}
	}

	/**
	 * 将数据转换为格式化的字符串并填充到单元格中
	 *
	 * @param sheet
	 * @param cell
	 * @param decimalFormat
	 * @param column
	 * @param cellValue
	 * @param fieldType
	 */
	private static void setCellValue(HSSFSheet sheet, HSSFCell cell, DecimalFormat decimalFormat, int column, Object
			cellValue, FieldType fieldType) {
		if (null != cellValue) {
			if (fieldType == FieldType.DATETIME) {
				//此处处理为格式化的字符串类型
				sheet.setColumnWidth(column, 20 * 256);
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
				cell.setCellValue(format.format((Date) cellValue));
			} else if (fieldType == FieldType.DOUBLE || fieldType == FieldType.SINGLE) {
				//浮点型格式化
				cell.setCellValue(decimalFormat.format(cellValue));
			} else {
				cell.setCellValue(cellValue.toString());
			}
		} else {
			cell.setCellValue("");
		}
	}

	private static boolean isSelectedFieldName(String fieldName, String[] fieldNames) {
		boolean result = false;
		for (String tempName : fieldNames) {
			if (fieldName.equals(tempName)) {
				result = true;
				break;
			}
		}
		return result;
	}


	public static void main(String[] args) {
		DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
		connectionInfo.setServer("F:\\SampleData711\\Ci ty\\Jingjin.udb");
		Datasource datasource = new Datasource(EngineType.UDB);
		datasource.open(connectionInfo);
		DatasetVector dataset = (DatasetVector) datasource.getDatasets().get("Grids");
//		exportXlsFile(dataset, "test2.xls", true, null);
		importXlsFile(datasource, "test2.xls", false);
	}

	public static void stepped(ImportSteppedEvent importSteppedEvent) {

	}
}
