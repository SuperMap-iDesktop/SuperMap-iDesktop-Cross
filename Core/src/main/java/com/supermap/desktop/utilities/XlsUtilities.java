package com.supermap.desktop.utilities;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by xie on 2017/8/24.
 */
public class XlsUtilities {

	/**
	 * @param datasource      目标数据源
	 * @param filePath        文件目录
	 * @param importFieldName 是否将首行导入为字段名称
	 */
	public static void importXlsFile(Datasource datasource, String filePath, boolean importFieldName) {
		try {
			File xlsFile = new File(filePath);
			if (!xlsFile.exists()) {
				return;
			}
			String xlsName = xlsFile.getName();
			xlsName = xlsName.substring(0, xlsName.indexOf("."));
			FileInputStream stream = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(stream);
			HSSFSheet sheet;
			HSSFRow row;
			HSSFCell cell;
			DatasetVector dataset;
			DatasetVectorInfo info;
			FieldInfos fieldInfos;
			FieldInfo fieldInfo = null;
			String datasetName;
			Recordset recordset;
			int columnCount;
			int rowCount;
			HashMap map;
			for (int i = 0, sheetSize = workbook.getNumberOfSheets(); i < sheetSize; i++) {
				//有几个sheet新建几个数据集
				sheet = workbook.getSheetAt(i);
				info = new DatasetVectorInfo();
				datasetName = datasource.getDatasets().getAvailableDatasetName(xlsName + "_" + sheet.getSheetName());
				info.setName(datasetName);
				info.setType(DatasetType.TABULAR);
				dataset = datasource.getDatasets().create(info);
				dataset.open();
				fieldInfos = dataset.getFieldInfos();
				//获取sheet的行数
				rowCount = sheet.getPhysicalNumberOfRows();
				columnCount = 0;
				if (rowCount > 0) {
					//获取sheet的列数
					HSSFRow hssfRow = sheet.getRow(0);
					columnCount = hssfRow.getPhysicalNumberOfCells();
				}
				recordset = dataset.getRecordset(false, CursorType.DYNAMIC);
				Recordset.BatchEditor editor = recordset.getBatch();
				editor.begin();
				if (importFieldName) {
					row = sheet.getRow(0);
					for (int j = 0; j < columnCount; j++) {
						fieldInfo = new FieldInfo();
						fieldInfo.setName("NewField" + "_" + row.getCell(j).getStringCellValue());
						fieldInfo.setType(FieldType.CHAR);
						//将字段添加到数据集中
						fieldInfos.add(fieldInfo);
					}
				} else {
					for (int j = 0; j < columnCount; j++) {
						fieldInfo = new FieldInfo();
						fieldInfo.setName(j == 0 ? "NewField" : "NewField" + "_" + String.valueOf(j));
						fieldInfo.setType(FieldType.CHAR);
						//将字段添加到数据集中
						fieldInfos.add(fieldInfo);
					}
				}
				int j = 0;
				if (importFieldName) {
					j = 1;
				}
				for (; j < rowCount; j++) {
					row = sheet.getRow(j);
					map = new HashMap();
					for (int k = 0; k < columnCount; k++) {
						map.put(fieldInfos.get(k).getName(), row.getCell(k).getStringCellValue());
					}
					recordset.addNew(null, map);
				}
				editor.update();
				fieldInfo.dispose();
				recordset.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 利用apache.poi实现将数据集导出为excel文件
	 *
	 * @param dataset
	 * @param filePath
	 * @param exportFieldName
	 */
	public static void exportXlsFile(DatasetVector dataset, String filePath, boolean exportFieldName) {
		exportXlsFile(dataset, filePath, exportFieldName, null);
	}

	/**
	 * 利用apache.poi实现将数据集导出为excel文件
	 *
	 * @param dataset         数据集
	 * @param filePath        文件目录
	 * @param exportFieldName 是否导出字段名称
	 * @param fieldNames      指定导出的字段集合，为空时全部导出
	 */
	public static void exportXlsFile(DatasetVector dataset, String filePath, boolean exportFieldName, String[] fieldNames) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();//产生工作簿对象
			HSSFSheet sheet = workbook.createSheet();//产生工作表对象
			HSSFRow row;//行
			HSSFCell cell;//单元格
			DecimalFormat decimalFormat = new DecimalFormat("######0.00000000");
			FieldInfos fieldInfos = dataset.getFieldInfos();
			Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
			Recordset.BatchEditor editor = recordset.getBatch();
			editor.begin();
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
			editor.update();
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
	private static void setCellValue(HSSFSheet sheet, HSSFCell cell, DecimalFormat decimalFormat, int column, Object cellValue, FieldType fieldType) {
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
//		Dataset dataset = datasource.getDatasets().get("Grids");
//		exportXlsFile((DatasetVector) dataset, "test1.xls", true, new String[]{"SmID", "SmLength", "int64"});
		importXlsFile(datasource, "test1.xls", true);
	}
}
