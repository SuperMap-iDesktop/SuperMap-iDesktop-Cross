package com.supermap.desktop.newtheme.commonUtils;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.*;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * 专题图公用方法类
 *
 * @author xie
 */
public class ThemeUtil {

	/**
	 * 判断单值项是否大于3000条
	 *
	 * @return
	 */

	private static ArrayList<FieldType> fieldTypes = initIntTypeFields();

	private ThemeUtil() {
		// 工具类不提供构造函数
	}

	private static ArrayList<FieldType> initIntTypeFields() {
		fieldTypes = new ArrayList<FieldType>();
		fieldTypes.add(FieldType.INT16);
		fieldTypes.add(FieldType.INT32);
		fieldTypes.add(FieldType.INT64);
		fieldTypes.add(FieldType.DOUBLE);
		fieldTypes.add(FieldType.SINGLE);
		return fieldTypes;
	}

	public static boolean isCountBeyond(DatasetVector datasetVector, String expression) {
		boolean countBeyond = false;
		String tempExpression = expression;
		QueryParameter parameter = new QueryParameter();
		parameter.setCursorType(CursorType.STATIC);
		parameter.setAttributeFilter(MessageFormat.format("{0} is not null", tempExpression));
		String result = MessageFormat.format("distinct {0}", tempExpression);
		parameter.setHasGeometry(false);
		parameter.setResultFields(new String[]{result});
		Recordset recordset = datasetVector.query(parameter);
		if (null != recordset && recordset.getRecordCount() > 3000) {
			countBeyond = true;
		}
		recordset.dispose();
		return countBeyond;
	}

	public static void themeCustomClone(ThemeCustom sourceCustom, ThemeCustom desCustom) {
		desCustom.setMarkerAngleExpression(sourceCustom.getMarkerAngleExpression());
		desCustom.setMarkerSizeExpression(sourceCustom.getMarkerSizeExpression());
		desCustom.setMarkerSymbolIDExpression(sourceCustom.getMarkerSymbolIDExpression());
		desCustom.setLineColorExpression(sourceCustom.getLineColorExpression());
		desCustom.setLineSymbolIDExpression(sourceCustom.getLineSymbolIDExpression());
		desCustom.setLineWidthExpression(sourceCustom.getLineWidthExpression());
		desCustom.setFillBackColorExpression(sourceCustom.getFillBackColorExpression());
		desCustom.setFillForeColorExpression(sourceCustom.getFillForeColorExpression());
		desCustom.setFillGradientAngleExpression(sourceCustom.getFillGradientAngleExpression());
		desCustom.setFillForeColorExpression(sourceCustom.getFillForeColorExpression());
		desCustom.setFillBackColorExpression(sourceCustom.getFillBackColorExpression());
		desCustom.setFillGradientModeExpression(sourceCustom.getFillGradientModeExpression());
		desCustom.setFillGradientOffsetRatioXExpression(sourceCustom.getFillGradientOffsetRatioXExpression());
		desCustom.setFillGradientOffsetRatioYExpression(sourceCustom.getFillGradientOffsetRatioYExpression());
		desCustom.setFillOpaqueRateExpression(sourceCustom.getFillOpaqueRateExpression());
		desCustom.setFillSymbolIDExpression(sourceCustom.getFillSymbolIDExpression());
	}

	/**
	 * 判断字段中是否有小于0的数据
	 *
	 * @param datasetVector
	 * @param expression
	 * @return
	 */
	public static boolean hasNegative(DatasetVector datasetVector, String expression) {
		boolean hasNegative = false;
		String tempExpression = expression;
		if (!StringUtilities.isNullOrEmpty(tempExpression) && tempExpression.contains(".")) {
			tempExpression = tempExpression.substring(tempExpression.lastIndexOf(".") + 1, tempExpression.length());
		}
		Recordset recordset = datasetVector.query(tempExpression + "<0", CursorType.STATIC);
		if (recordset.getRecordCount() > 0) {
			hasNegative = true;
		}
		recordset.dispose();
		return hasNegative;
	}

	/**
	 * 获取当前活动图层
	 *
	 * @return
	 */
	public static Layer getActiveLayer() {
		Layer result = null;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof IFormMap) {
			result = ((IFormMap) activeForm).getActiveLayers()[0];
		}
		return result;
	}

	/**
	 * 初始化comboBoxExpression，并设置默认选项
	 *
	 * @param comboBoxExpression 添加表达式的控件
	 * @param tempExpression     默认表达式
	 * @param datasetVector      数据集
	 * @param joinItems          外部连接表
	 * @param comboBoxArray      存放表达式的容器
	 * @param isDataType         是否为数据类型
	 * @param addZero            是否添加0值
	 */
	public static void initComboBox(JComboBox<String> comboBoxExpression, String tempExpression, DatasetVector datasetVector, JoinItems joinItems,
	                                ArrayList<String> comboBoxArray, boolean isDataType, boolean addZero) {
		comboBoxExpression.setEditable(true);
		comboBoxExpression.removeAllItems();
		getFieldComboBox(comboBoxExpression, datasetVector, joinItems, comboBoxArray, isDataType);
		if (StringUtilities.isNullOrEmpty(tempExpression)) {
			tempExpression = "";
		}
		comboBoxExpression.setSelectedItem(tempExpression);
		if (!tempExpression.equals(comboBoxExpression.getSelectedItem())) {
			comboBoxExpression.addItem(tempExpression);
			comboBoxExpression.setSelectedItem(tempExpression);
		}
		if (addZero) {
			comboBoxExpression.addItem("0");
			//进行判断，当前tempExpression值为空时，给于"0",否则给予当前tempExpression值。主要防止，每次点击都显示为"0"__yuanR 16.12.28
			if (StringUtilities.isNullOrEmpty(tempExpression)) {
				comboBoxExpression.setSelectedItem("0");
			} else {
				comboBoxExpression.setSelectedItem(tempExpression);
			}
		}
	}

	/**
	 * 往combox中填充字段
	 *
	 * @param comboBox      要填充字段的combox
	 * @param datasetVector 当前矢量数据集
	 * @param joinItems     外部连接信息集合
	 * @param comboBoxArray 存放字段信息的数组
	 * @param isDataType    是否为数值类型
	 * @return
	 */
	public static JComboBox<String> getFieldComboBox(JComboBox<String> comboBox, DatasetVector datasetVector, JoinItems joinItems,
	                                                 ArrayList<String> comboBoxArray, boolean isDataType) {
		int count = datasetVector.getFieldCount();

		int itemsCount = -1;
		if (null != joinItems) {
			itemsCount = joinItems.getCount();
		}
		for (int j = 0; j < count; j++) {
			FieldInfo fieldInfo = datasetVector.getFieldInfos().get(j);
			if (isDataType && isDataType(fieldInfo.getType())) {
				String dataTypeitem = fieldInfo.getName();
				//原来item为name属性，改为Caption，使其完全与标头字符相同--yuanuR
//				String dataTypeitem = fieldInfo.getCaption();
				if (0 < itemsCount) {
					dataTypeitem = datasetVector.getName() + "." + dataTypeitem;
				}
				comboBox.addItem(dataTypeitem);
				comboBoxArray.add(dataTypeitem);
			}
			if (!isDataType) {
				String item = fieldInfo.getName();
				//原来item为name属性，改为Caption，使其完全与标头字符相同--yuanuR
//				String item = fieldInfo.getCaption();
				if (0 < itemsCount) {
					item = datasetVector.getName() + "." + item;
				}
				comboBox.addItem(item);
				comboBoxArray.add(item);
			}
		}
		if (null != joinItems) {
			for (int i = 0; i < itemsCount; i++) {
				if (datasetVector.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable()) instanceof DatasetVector) {
					DatasetVector tempDataset = (DatasetVector) datasetVector.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable());
					int tempDatasetFieldCount = tempDataset.getFieldCount();
					for (int j = 0; j < tempDatasetFieldCount; j++) {
						FieldInfo tempfieldInfo = tempDataset.getFieldInfos().get(j);
						if (isDataType && isDataType(tempfieldInfo.getType())) {
							String tempDataTypeItem = tempDataset.getName() + "." + tempDataset.getFieldInfos().get(j).getName();
							comboBox.addItem(tempDataTypeItem);
							comboBoxArray.add(tempDataTypeItem);
						}
						if (!isDataType) {
							String tempItem = tempDataset.getName() + "." + tempDataset.getFieldInfos().get(j).getName();
							comboBox.addItem(tempItem);
							comboBoxArray.add(tempItem);
						}
					}
				}
			}
		}
		comboBox.addItem(MapViewProperties.getString("String_Combobox_Expression"));
		return comboBox;
	}

	/**
	 * 判断字段是否为数值类型
	 *
	 * @param fieldType
	 * @return
	 */
	public static boolean isDataType(FieldType fieldType) {
		boolean result = false;
		for (int i = 0; i < fieldTypes.size(); i++) {
			if (fieldType == fieldTypes.get(i)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 打开SQLExpressionDialog并获取表达式字段
	 *
	 * @param jComboBoxField 要添加表达式的控件
	 * @param datasets       数据集集合
	 * @param comboBoxArray  jComboBoxField内部存放的字段集合
	 * @param expression     SQLExpressionDialog显示的字段和控件重新设置的表达式
	 * @param isDataType     是否过滤表达式，true表示只显示数值型的字段，false表示显示所有类型的字段
	 */
	public static boolean getSqlExpression(JComboBox<String> jComboBoxField, Dataset[] datasets, ArrayList<String> comboBoxArray, String expression,
	                                       boolean isDataType) {
		// 判断是否为“表达式”项
		boolean itemHasChanged = false;
		if (MapViewProperties.getString("String_Combobox_Expression").equals(jComboBoxField.getSelectedItem())) {
			SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
			int allItems = jComboBoxField.getItemCount();
			DialogResult dialogResult = DialogResult.CANCEL;
			if (isDataType) {
				dialogResult = sqlDialog.showDialog(datasets, fieldTypes, expression);
			} else {
				dialogResult = sqlDialog.showDialog(expression, datasets);
			}
			if (dialogResult == DialogResult.OK) {
				String filter = sqlDialog.getQueryParameter().getAttributeFilter();
				if (!StringUtilities.isNullOrEmpty(filter)) {
					if (!comboBoxArray.contains(filter) && !filter.equals(expression)) {
						jComboBoxField.insertItemAt(filter, allItems - 1);
						jComboBoxField.setSelectedIndex(allItems - 1);
						comboBoxArray.add(filter);
						itemHasChanged = true;
					} else {
						jComboBoxField.setSelectedItem(filter);
					}
				} else {
					resetComboBoxInfo(jComboBoxField, comboBoxArray, expression);
				}
			} else {
				resetComboBoxInfo(jComboBoxField, comboBoxArray, expression);
			}
		} else {
			// 非表达式字段
			if (!expression.equals(jComboBoxField.getSelectedItem().toString())) {
				itemHasChanged = true;
			}
		}
		return itemHasChanged;
	}

	/**
	 * 根据给定的图层，数据集创建数据集数组
	 *
	 * @param layer         指定的图层
	 * @param datasetVector 指定的矢量数据集
	 * @return
	 */
	public static Dataset[] getDatasets(Layer layer, DatasetVector datasetVector) {
		Dataset[] datasets = new Dataset[0];
		if (null != layer && !layer.isDisposed()) {
			datasets = new Dataset[layer.getDisplayFilter().getJoinItems().getCount() + 1];
			datasets[0] = datasetVector;
			for (int i = 1; i < datasets.length; i++) {
				String tableName = layer.getDisplayFilter().getJoinItems().get(i - 1).getForeignTable();
				datasets[i] = DatasetUIUtilities.getDatasetFromDatasource(tableName, datasetVector.getDatasource());
			}
		}
		return datasets;
	}

	/**
	 * 判断数据集是否为线数据集
	 *
	 * @param dataset
	 * @return
	 */
	public static boolean isLine(Dataset dataset) {
		DatasetType type = dataset.getType();
		boolean isLine = false;
		if (type.equals(DatasetType.LINE) || type.equals(DatasetType.LINE3D) || type.equals(DatasetType.LINEM) || type.equals(DatasetType.NETWORK)
				|| type.equals(DatasetType.NETWORK3D)) {
			isLine = true;
		}
		return isLine;
	}

	/**
	 * 判断数据集是否为点数据集
	 *
	 * @param dataset
	 * @return
	 */
	public static boolean isPoint(Dataset dataset) {
		DatasetType type = dataset.getType();
		boolean isPoint = false;
		if (type.equals(DatasetType.POINT) || type.equals(DatasetType.POINT3D)) {
			isPoint = true;
		}
		return isPoint;
	}

	/**
	 * 判断数据集是否为面数据集
	 *
	 * @param dataset
	 * @return
	 */
	public static boolean isRegion(Dataset dataset) {
		DatasetType type = dataset.getType();
		boolean isRegion = false;
		if (type.equals(DatasetType.REGION) || type.equals(DatasetType.REGION3D)) {
			isRegion = true;
		}
		return isRegion;
	}

	private static void resetComboBoxInfo(JComboBox<String> jComboBoxField, ArrayList<String> comboBoxArray, String expression) {
		if (comboBoxArray.contains(expression)) {
			jComboBoxField.setSelectedItem(expression);
		} else {
			jComboBoxField.setSelectedItem(expression.substring(expression.indexOf(".") + 1, expression.length()));
		}
	}

	public static Layer arrayNewThemeLayer(Map map, Layer datasetLayer, Theme theme) {
		Layer themeLayer = null;
		try {
			int start = 0;
			int end = 0;
			LayerGroup layerGroup = datasetLayer.getParentGroup();
			if (layerGroup != null) {
				int index = layerGroup.indexOf(datasetLayer);
				start = index;

				for (int i = index - 1; i >= 0 && layerGroup.get(i).getDataset() == datasetLayer.getDataset(); i--) {
					start = i;
				}
				end = index;
				for (int i = index + 1; i <= layerGroup.getCount() - 1 && layerGroup.get(i).getDataset() == datasetLayer.getDataset(); i++) {
					end = i;
				}
				themeLayer = insertThemeLayer(map, datasetLayer, theme, start, end);
			} else {
				int index = map.getLayers().indexOf(datasetLayer.getName());
				start = index;
				for (int i = index - 1; i >= 0 && map.getLayers().get(i).getDataset() == datasetLayer.getDataset(); i--) {
					start = i;
				}
				end = index;
				for (int i = index + 1; i <= map.getLayers().getCount() - 1 && map.getLayers().get(i).getDataset() == datasetLayer.getDataset(); i++) {
					end = i;
				}
				themeLayer = insertThemeLayer(map, datasetLayer, theme, start, end);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return themeLayer;
	}

	private static Layer insertThemeLayer(Map map, Layer datasetLayer, Theme theme, int start, int end) {
		Layer themeLayer = null;
		try {
			LayerGroup layerGroup = datasetLayer.getParentGroup();
			Dataset dataset = datasetLayer.getDataset();
			ThemeType currentThemeType = theme.getType();
			if (layerGroup != null) {
				if (start >= layerGroup.getCount()) {
					themeLayer = map.getLayers().add(dataset, theme, false);
					layerGroup.insert(start, themeLayer);
				} else {
					for (int i = start; i <= end; i++) {
						if (layerGroup.get(i).getDataset() != dataset || isRightPosition(layerGroup.get(i), currentThemeType, dataset)) {
							themeLayer = map.getLayers().add(dataset, theme, false);
							layerGroup.insert(i, themeLayer);
							break;
						} else if (i == end) {
							themeLayer = map.getLayers().add(dataset, theme, false);
							layerGroup.insert(end + 1, themeLayer);
						} else {
							//避免有遗漏的情况，用else结尾。
						}
					}
				}
			} else {
				if (start >= map.getLayers().getCount()) {
//					themeLayer = map.getLayers().add(dataset, theme, true);
//					map.getLayers().moveTo(0, start);
					//layers的insert方法会导致桌面崩溃故暂时绕一下
					themeLayer = map.getLayers().insert(start, dataset, theme);
				} else {
					for (int i = start; i <= end; i++) {
						if (map.getLayers().get(i).getDataset() != dataset || isRightPosition(map.getLayers().get(i), currentThemeType, dataset)) {
							themeLayer = map.getLayers().insert(i, dataset, theme);
//							themeLayer = map.getLayers().add(dataset, theme, true);
//							map.getLayers().moveTo(0, i);
							break;
						} else if (i == end) {
							themeLayer = map.getLayers().insert(end + 1, dataset, theme);
//							themeLayer = map.getLayers().add(dataset, theme, true);
//							map.getLayers().moveTo(0, end + 1);
						} else {
							//避免有遗漏的情况，用else结尾。s
						}
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return themeLayer;
	}

	private static Boolean isRightPosition(Layer currentLayer, ThemeType themeType, Dataset dataset) {
		Boolean isRight = false;
		try {
			if (dataset.getType() == DatasetType.POINT || dataset.getType() == DatasetType.POINT3D) {
				isRight = isRightPositionOnPointLayer(currentLayer, themeType, dataset);
			} else if (dataset instanceof DatasetVector) {
				isRight = isRightPositionOnOtherLayer(currentLayer, themeType, dataset);
			} else if (dataset instanceof DatasetGrid || dataset instanceof DatasetImage) {
				isRight = isRightPositionOnGridLayer(currentLayer, themeType, dataset);
			} else {
				//避免有遗漏的情况，用else结尾。
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isRight;
	}

	private static Boolean isRightPositionOnPointLayer(Layer currentLayer, ThemeType themeType, Dataset dataset) {
		Boolean isRight = false;
		try {
			if (dataset.getType() == DatasetType.POINT || dataset.getType() == DatasetType.POINT3D) {
				if (themeType == ThemeType.LABEL) {
					isRight = true;
				} else if (themeType == ThemeType.RANGE) {
					if (currentLayer.getTheme() == null || currentLayer.getTheme().getType() != ThemeType.LABEL) {
						isRight = true;
					}
				} else if (themeType == ThemeType.UNIQUE) {
					if (currentLayer.getTheme() == null || (currentLayer.getTheme().getType() != ThemeType.LABEL && currentLayer.getTheme().getType() != ThemeType.RANGE)) {
						isRight = true;
					}
				} else if (currentLayer.getTheme() != null && currentLayer.getTheme().getType() != ThemeType.LABEL &&
						currentLayer.getTheme().getType() != ThemeType.RANGE && currentLayer.getTheme().getType() != ThemeType.UNIQUE) {
					if (themeType == ThemeType.GRADUATEDSYMBOL) {
						isRight = true;
					} else if (themeType == ThemeType.GRAPH && currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL) {
						isRight = true;
					} else if (themeType == ThemeType.DOTDENSITY && currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL &&
							currentLayer.getTheme().getType() != ThemeType.GRAPH) {
						isRight = true;
					} else if (themeType == ThemeType.CUSTOM && currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL &&
							currentLayer.getTheme().getType() != ThemeType.GRAPH && currentLayer.getTheme().getType() != ThemeType.DOTDENSITY) {
						isRight = true;
					} else {
						//避免有遗漏的情况，用else结尾。
					}
				} else {
					//避免有遗漏的情况，用else结尾。
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isRight;
	}

	private static Boolean isRightPositionOnGridLayer(Layer currentLayer, ThemeType themeType, Dataset dataset) {
		Boolean isRight = false;
		try {
			if (dataset.getType() == DatasetType.GRID || dataset.getType() == DatasetType.IMAGE) {
				if (themeType == ThemeType.GRIDRANGE) {
					isRight = true;
				} else if (themeType == ThemeType.GRIDUNIQUE) {
					if (currentLayer.getTheme() == null || currentLayer.getTheme().getType() != ThemeType.GRIDRANGE) {
						isRight = true;
					}
				} else {
					//避免有遗漏的情况，用else结尾。
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isRight;
	}

	private static Boolean isRightPositionOnOtherLayer(Layer currentLayer, ThemeType themeType, Dataset dataset) {
		Boolean isRight = false;
		try {
			if (dataset instanceof DatasetVector && dataset.getType() != DatasetType.POINT && dataset.getType() != DatasetType.POINT3D) {
				if (themeType == ThemeType.LABEL) {
					isRight = true;
				} else if (themeType == ThemeType.GRADUATEDSYMBOL) {
					if (currentLayer.getTheme() == null || currentLayer.getTheme().getType() != ThemeType.LABEL) {
						isRight = true;
					}
				} else if (themeType == ThemeType.GRAPH) {
					if (currentLayer.getTheme() == null ||
							(currentLayer.getTheme().getType() != ThemeType.LABEL && currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL)) {
						isRight = true;
					}
				} else if (themeType == ThemeType.DOTDENSITY) {
					if (currentLayer.getTheme() == null ||
							(currentLayer.getTheme().getType() != ThemeType.LABEL &&
									currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL &&
									currentLayer.getTheme().getType() != ThemeType.GRAPH)) {
						isRight = true;
					}
				} else if (themeType == ThemeType.RANGE) {
					if (currentLayer.getTheme() == null ||
							(currentLayer.getTheme().getType() != ThemeType.LABEL &&
									currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL &&
									currentLayer.getTheme().getType() != ThemeType.GRAPH &&
									currentLayer.getTheme().getType() != ThemeType.DOTDENSITY)) {
						isRight = true;
					}
				} else if (themeType == ThemeType.UNIQUE) {
					if (currentLayer.getTheme() == null ||
							(currentLayer.getTheme().getType() != ThemeType.LABEL &&
									currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL &&
									currentLayer.getTheme().getType() != ThemeType.GRAPH &&
									currentLayer.getTheme().getType() != ThemeType.DOTDENSITY &&
									currentLayer.getTheme().getType() != ThemeType.RANGE)) {
						isRight = true;
					}
				} else {
					if (currentLayer.getTheme() == null ||
							(currentLayer.getTheme().getType() != ThemeType.LABEL &&
									currentLayer.getTheme().getType() != ThemeType.GRADUATEDSYMBOL &&
									currentLayer.getTheme().getType() != ThemeType.GRAPH &&
									currentLayer.getTheme().getType() != ThemeType.DOTDENSITY &&
									currentLayer.getTheme().getType() != ThemeType.RANGE &&
									currentLayer.getTheme().getType() != ThemeType.UNIQUE)) {
						isRight = true;
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return isRight;
	}
}
