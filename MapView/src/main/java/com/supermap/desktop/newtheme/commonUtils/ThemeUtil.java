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
import com.supermap.mapping.Layer;
import com.supermap.mapping.ThemeCustom;

import javax.swing.*;
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
        Recordset recordset = datasetVector.query(tempExpression, CursorType.STATIC);
        if (recordset.getRecordCount() > 3000) {
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
        tempExpression = tempExpression.substring(tempExpression.lastIndexOf(".") + 1, tempExpression.length());
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
            comboBoxExpression.setSelectedItem("0");
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
                if (0 < itemsCount) {
                    dataTypeitem = datasetVector.getName() + "." + dataTypeitem;
                }
                comboBox.addItem(dataTypeitem);
                comboBoxArray.add(dataTypeitem);
            }
            if (!isDataType) {
                String item = fieldInfo.getName();
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

}
