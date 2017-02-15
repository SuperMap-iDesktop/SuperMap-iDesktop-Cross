package com.supermap.desktop.spatialanalyst;

import com.supermap.data.*;
import com.supermap.data.Enum;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.spatialanalyst.vectoranalyst.ComboBoxField;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by hanyz on 2017/2/15.
 */
public class SmPanelBufferRadius extends JPanel {
    private JLabel labelUnit;
    private JLabel labelField;
    private JComboBox<LengthUnit> comboBoxLengthUnit;
    private JComboBox<Object> comboBoxField;
    private JPanel panelUnit;
    private JPanel panelField;

    private DatasetVector dataset;
    private JoinItems joinItems;
    private ArrayList<DatasetFieldInfo> datasetFieldInfos;
    private String[] additionalItems;
    private String labelFieldText = "";
    private double DEFAULT_NUMERICVALUE = 0;//数值控件默认值
    private NUMERICTYPE DEFAULT_NUMERICTYPE = NUMERICTYPE.NUMBER;//数值控件默类型，默认是小数


    enum NUMERICTYPE {
        INTEGER,
        NUMBER,
        POSITIVENUMBER,
        POSITIVEINTEGER
    }

    /**
     * 用数据集提供可选字段，取所有数值型字段
     *
     * @param dataset
     */
    public SmPanelBufferRadius(DatasetVector dataset) {
        super();
        this.dataset = dataset;
        initComponent();
        initLayout();
        registListener();
    }

    /**
     * 自定义数据集和字段
     *
     * @param datasetFieldInfos
     */
    public SmPanelBufferRadius(ArrayList<DatasetFieldInfo> datasetFieldInfos) {
        super();
        this.datasetFieldInfos = datasetFieldInfos;
    }


    /**
     * 自定义字符数组作为字段选择项
     *
     * @param fieldInfos
     */
    public SmPanelBufferRadius(String[] fieldInfos) {
        super();
        this.additionalItems = fieldInfos;
    }

    /**
     * 提供获取选中单位
     *
     * @return
     */
    public Unit getSelectedUnit() {
        return null;
    }
    /**
     * 提供获取选中字段或文本或输入的数值
     * @return
     */

    /**
     * 设置combobox描述标签，如“半径”
     *
     * @param fieldText
     */
    public void setLabelFieldText(String fieldText) {

    }

    public Object getSelectedValue() {
        return null;
    }


    private void initComponent() {
        this.setPreferredSize(new Dimension(200, 120));
        this.labelUnit = new JLabel();
        labelUnit.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
        this.labelField = new JLabel();
        labelField.setText(SpatialAnalystProperties.getString("String_BufferRadiusUnit"));
        this.comboBoxLengthUnit = createComboBoxUnit();
        this.comboBoxField = createComboBoxField();
    }

    private void initLayout() {

    }

    class LocalComboboxAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            comboBoxFunction_SelectedIndexChanged((JComboBox<?>) e.getSource());
        }
    }

    private void registListener() {

    }

    private void unRegistListener() {

    }

    private ArrayList<FieldInfo> getNemericField(DatasetVector dataset) {
        return null;
    }

    private JComboBox<LengthUnit> createComboBoxUnit() {
        JComboBox<LengthUnit> comboBox = new JComboBox<LengthUnit>();
        comboBox.addItem(LengthUnit.MILLIMETER);
        comboBox.addItem(LengthUnit.CENTIMETER);
        comboBox.addItem(LengthUnit.DECIMETER);
        comboBox.addItem(LengthUnit.METER);
        comboBox.addItem(LengthUnit.KILOMETER);
        comboBox.addItem(LengthUnit.INCH);
        comboBox.addItem(LengthUnit.FOOT);
        comboBox.addItem(LengthUnit.MILE);
        comboBox.addItem(LengthUnit.DEGREE);
        comboBox.addItem(LengthUnit.YARD);
        comboBox.setSelectedItem(LengthUnit.METER);
        comboBox.setEditable(false);
        comboBox.setPreferredSize(ControlDefaultValues.DEFAULT_PREFERREDSIZE);
        return comboBox;
    }

    private JComboBox<Object> createComboBoxField() {
        JComboBox<Object> comboBox = new JComboBox<Object>();
        comboBox.setEditable(true);
        /**
         *添加数据集数值字段
         */
        initFieldInfo();
        for (int i = 0; i < datasetFieldInfos.size(); i++) {
            comboBox.addItem(datasetFieldInfos.get(i));
        }
        /**
         * 添加额外的字符串，如“表达式..”
         */
        if (additionalItems != null && additionalItems.length > 0) {
            for (int i = 0; i < additionalItems.length; i++) {
                comboBox.addItem(additionalItems[i]);
            }
        }
        comboBox.setRenderer(new DatasetFieldInfoRender(this.dataset));
        return comboBox;
    }

    private void initFieldInfo() {
        if (this.dataset == null)
            return;
        /**
         * 保留定义好的datasetFieldInfos列表
         */
        if (datasetFieldInfos != null && datasetFieldInfos.size() > 0)
            return;
        /**
         * 从数据集中获取数值字段
         */
        datasetFieldInfos = new ArrayList<>();
        FieldInfos fieldInfos = dataset.getFieldInfos();
        for (int i = 0; i < fieldInfos.getCount(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            if (isNemericField(fieldInfo)) {
                datasetFieldInfos.add(new DatasetFieldInfo(dataset, fieldInfo));
            }
        }
        /**
         * 获取关联表FieldInfos
         */
        if (joinItems != null) {
            for (int i = 0; i < joinItems.getCount(); i++) {
                DatasetVector joinDataset = (DatasetVector) dataset.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable());
                fieldInfos = joinDataset.getFieldInfos();
                for (int j = 0; j < fieldInfos.getCount(); j++) {
                    FieldInfo fieldInfo = fieldInfos.get(j);
                    if (isNemericField(fieldInfo)) {
                        datasetFieldInfos.add(new DatasetFieldInfo(joinDataset, fieldInfo));
                    }
                }
            }
        }
    }

    /**
     * 数据集字段信息类：绑定数据集和字段信息，关联表能明确字段归属
     */
    public class DatasetFieldInfo {
        DatasetVector dataset;
        FieldInfo fieldInfo;

        public DatasetFieldInfo(DatasetVector dataset, FieldInfo fieldInfo) {
            this.dataset = dataset;
            this.fieldInfo = fieldInfo;
        }

        public DatasetVector getDataset() {
            return dataset;
        }

        public void setDataset(DatasetVector dataset) {
            this.dataset = dataset;
        }

        public void setFieldInfo(FieldInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
        }

        public FieldInfo getFieldInfo() {
            return fieldInfo;
        }

    }

    public class DatasetFieldInfoRender implements ListCellRenderer {
        DatasetVector dataset;

        public DatasetFieldInfoRender(DatasetVector dataset) {
            super();
            this.dataset = dataset;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel jLabel = new JLabel();
            jLabel.setOpaque(true);
            if (isSelected) {
                jLabel.setBackground(list.getSelectionBackground());
                jLabel.setForeground(list.getSelectionForeground());
            } else {
                jLabel.setBackground(list.getBackground());
                jLabel.setForeground(list.getForeground());
            }

            if (value != null) {
                /**
                 * DatasetFieldInfo选项，关联表字段：表名.Caption；本表：Caption
                 */
                if (value instanceof DatasetFieldInfo) {
                    String valueText;
                    DatasetFieldInfo datasetFieldInfo = (DatasetFieldInfo) value;
                    String datasetName = datasetFieldInfo.getDataset() == null ? "" : datasetFieldInfo.getDataset().getName();
                    String FieldCaption = datasetFieldInfo.getFieldInfo() == null ? "" : datasetFieldInfo.getFieldInfo().getCaption();
                    if (datasetFieldInfo.getDataset() != null && datasetFieldInfo.getDataset().equals(dataset)) {
                        valueText = datasetFieldInfo.getFieldInfo().getCaption();
                    } else {
                        valueText = datasetName + "." + FieldCaption;
                    }
                    jLabel.setText(valueText);
                }
                /**
                 * 字符串选项直接作为标签
                 */
                if (value instanceof String) {
                    jLabel.setText((String) value);
                }
            }
            return jLabel;
        }
    }

    /**
     * 判断字段是否数值型字段
     *
     * @param fieldInfo
     * @return
     */
    private boolean isNemericField(FieldInfo fieldInfo) {
        return fieldInfo.getType().equals(FieldType.INT16)
                || fieldInfo.getType().equals(FieldType.INT32)
                || fieldInfo.getType().equals(FieldType.INT64)
                || fieldInfo.getType().equals(FieldType.DOUBLE)
                || fieldInfo.getType().equals(FieldType.SINGLE);
    }
}
