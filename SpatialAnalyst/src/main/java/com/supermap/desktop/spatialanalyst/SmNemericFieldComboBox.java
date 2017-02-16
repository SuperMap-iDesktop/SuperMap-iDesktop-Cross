package com.supermap.desktop.spatialanalyst;

import com.supermap.data.*;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by hanyz on 2017/2/15.
 */
public class SmNemericFieldComboBox extends JComboBox {
    private JLabel labelUnit;
    private JLabel labelField;
    private JComboBox<LengthUnit> comboBoxLengthUnit;
    private JComboBox<Object> comboBoxField;
    private JPanel panelUnit;
    private JPanel panelField;
    private ComboBoxListener comboboxListenser = new ComboBoxListener();
    private DatasetVector dataset;
    private JoinItems joinItems;
    private ArrayList<DatasetFieldInfo> datasetFieldInfos;
    private String[] additionalItems;
    private boolean hasSQLExpression = true;//是否具有“表达式..”选项
    private String labelFieldText = "";
    private double DEFAULT_NUMERICVALUE = 0;//数值控件默认值
    private NUMERICTYPE DEFAULT_NUMERICTYPE = NUMERICTYPE.POSITIVENUMBER;//数值控件默类型，默认正数

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
    public SmNemericFieldComboBox(DatasetVector dataset) {
        super();
        this.dataset = dataset;
        init();
    }

    /**
     * 自定义数据集和字段
     *
     * @param datasetFieldInfos
     */
    public SmNemericFieldComboBox(ArrayList<DatasetFieldInfo> datasetFieldInfos) {
        super();
        ;
        this.datasetFieldInfos = datasetFieldInfos;
        init();
    }


    /**
     * 自定义字符数组作为字段选择项
     *
     * @param fieldInfos
     */
    public SmNemericFieldComboBox(String[] fieldInfos) {
        super();
        this.additionalItems = fieldInfos;
        init();
    }

    public void init() {
        initComponent();
        initLayout();
        registListener();
    }

    private void initComponent() {
        this.setPreferredSize(new Dimension(200, 120));
        /*this.labelUnit = new JLabel();
        labelUnit.setText(ControlsProperties.getString("String_ProjectionInfoControl_LabelGeographyUnit"));
        this.labelField = new JLabel();
        labelField.setText(SpatialAnalystProperties.getString("String_BufferRadiusUnit"));
        this.comboBoxLengthUnit = createComboBoxUnit();*/
        createComboBoxField();
    }

    /**
     * 数据集字段信息类：绑定数据集和字段信息，关联表能明确字段归属
     */
    private JComboBox<Object> createComboBoxField() {
        this.setEditable(true);
        /**
         *添加数据集数值字段
         */
        initFieldInfo();
        for (int i = 0; i < datasetFieldInfos.size(); i++) {
            this.addItem(datasetFieldInfos.get(i));
        }
        /**
         * 添加额外的字符串，如“0”
         */
        if (additionalItems != null && additionalItems.length > 0) {
            for (int i = 0; i < additionalItems.length; i++) {
                if (JComboBoxUIUtilities.getItemIndex(this, additionalItems[i]) == -1) {
                    this.addItem(additionalItems[i]);
                }
            }
        }
        /**
         * 添加“表达式...”
         */
        if (hasSQLExpression) {
            String SQLExpressionText = CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression");
            if (JComboBoxUIUtilities.getItemIndex(this, SQLExpressionText) == -1) {
                this.addItem(SQLExpressionText);
            }
        }
        this.setRenderer(new DatasetFieldInfoRender());
        return this;
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

    public class DatasetFieldInfo {
        DatasetVector dataset;

        FieldInfo fieldInfo;

        public DatasetFieldInfo(DatasetVector dataset, FieldInfo fieldInfo) {
            this.dataset = dataset;
            this.fieldInfo = fieldInfo;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof DatasetFieldInfo){
                return super.equals(obj);
            }
            return this.toString().equals(obj.toString());
        }

        @Override
        public String toString() {
            String datasetName = this.dataset == null ? "" : this.dataset.getName();
            String FieldCaption = this.fieldInfo == null ? "" : this.fieldInfo.getCaption();
            if (this.dataset != null && this.dataset.equals(SmNemericFieldComboBox.this.dataset)) {
                return this.fieldInfo.getCaption();
            } else {
                return datasetName + "." + FieldCaption;
            }
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

    private void initLayout() {

    }

    protected void registListener() {
        this.addItemListener(this.comboboxListenser);
    }

    protected void unRegistListener() {
        this.removeItemListener(this.comboboxListenser);
    }

    class ComboBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                /**
                 * 选中的是FieldInfo，直接设置为选中项
                 */
                if (item instanceof DatasetFieldInfo) {
                    SmNemericFieldComboBox.this.setSelectedItem(item);
                }
                /**
                 *选中或输入字符串
                 */
                String itemStr = e.getItem().toString();
                //表达式...
                if (itemStr.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
                    SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
                    Dataset[] datasets = null;
                    if (joinItems != null && joinItems.getCount() > 0) {
                        datasets = new Dataset[joinItems.getCount() + 1];
                        datasets[0] = dataset;
                        for (int i = 0; i < joinItems.getCount(); i++) {
                            String foreignTable = joinItems.get(i).getForeignTable();
                            datasets[i + 1] = DatasetUIUtilities.getDatasetFromDatasource(foreignTable, dataset.getDatasource());
                        }
                    } else {
                        datasets = new Dataset[1];
                        datasets[0] = dataset;
                    }
                    DialogResult dialogResult = sqlDialog.showDialog("", datasets);
                    if (dialogResult == DialogResult.OK) {
                        itemStr = sqlDialog.getQueryParameter().getAttributeFilter();
                    }
                }
                //属于combobox的字符串直接选择
                try {
                    int itemIndex = JComboBoxUIUtilities.getItemIndex(SmNemericFieldComboBox.this, itemStr);
                    if (itemIndex > -1) {
                        SmNemericFieldComboBox.this.setSelectedIndex(itemIndex);
                    }
                    //不属于combox的字符串判断是否为数值
                    else if ((DEFAULT_NUMERICTYPE.equals(NUMERICTYPE.POSITIVENUMBER) && Double.parseDouble(itemStr) > 0)
                            || (DEFAULT_NUMERICTYPE.equals(NUMERICTYPE.POSITIVEINTEGER) && Integer.parseInt(itemStr) > 0)
                            || (DEFAULT_NUMERICTYPE.equals(NUMERICTYPE.INTEGER) && Integer.parseInt(itemStr) == Integer.parseInt(itemStr))
                            || (DEFAULT_NUMERICTYPE.equals(NUMERICTYPE.POSITIVENUMBER) && Double.parseDouble(itemStr) + 1 > Double.parseDouble(itemStr))) {
                        SmNemericFieldComboBox.this.setSelectedItem(item);
                    }
                } catch (NumberFormatException e1) {
//                e1.printStackTrace();
//                ignore
                }
            }
        }
    }

    public class DatasetFieldInfoRender implements ListCellRenderer {

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
                 *
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

    /**
     * 返回选中对象名称，如果选中的是字段，返回字段名称或者带前缀的字段名称（combobox可能显示的是caption）
     * 如果需要返回与所见一致的字符串，直接使用getSelectedItem().toString();
     *
     * @return
     */
    public Object getSelectedField() {
        Object selectedItem = this.getSelectedItem();
        if (selectedItem instanceof DatasetFieldInfo) {
            DatasetFieldInfo datasetFieldInfo = (DatasetFieldInfo) selectedItem;
            String datasetName = datasetFieldInfo.getDataset() == null ? "" : datasetFieldInfo.getDataset().getName();
            String FieldName = datasetFieldInfo.getFieldInfo() == null ? "" : datasetFieldInfo.getFieldInfo().getName();
            if (datasetFieldInfo.getDataset() != null && datasetFieldInfo.getDataset().equals(dataset)) {
                return datasetFieldInfo.getFieldInfo().getName();
            } else {
                return datasetName + "." + FieldName;
            }
        }
        return selectedItem.toString();
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

    public static void main(String[] args) {
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setServer("C:\\Users\\hanyz\\Desktop\\2.udb");
        connectionInfo.setEngineType(EngineType.UDB);
        Datasource datasoure = new Datasource(EngineType.UDB);
        datasoure.open(connectionInfo);
        System.out.println(datasoure.isConnected());
        Dataset dataset = datasoure.getDatasets().get("BaseMap_R");

        JFrame jFrame = new JFrame();
        jFrame.setSize(new Dimension(300,120));
        jFrame.setLocationRelativeTo(null);
        JButton jButton = new JButton("Action");
        SmNemericFieldComboBox smNemericFieldComboBox = new SmNemericFieldComboBox(((DatasetVector) dataset));
        smNemericFieldComboBox.setMaximumSize(new Dimension(80,jButton.getPreferredSize().height));
        Container contentPane = jFrame.getContentPane();
        GroupLayout groupLayout = new GroupLayout(contentPane);
        jFrame.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
        hGroup.addComponent(smNemericFieldComboBox).addComponent(jButton);
        groupLayout.setHorizontalGroup(hGroup);
        GroupLayout.ParallelGroup vGroup = groupLayout.createParallelGroup();
        vGroup.addComponent(smNemericFieldComboBox).addComponent(jButton);
        groupLayout.setVerticalGroup(vGroup);
        jFrame.setVisible(true);
    }

}
