package com.supermap.desktop.spatialanalyst.vectoranalyst.overlayAnalyst;

import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

/**
 * Created by xie on 2016/8/30.
 */
public class OverlayAnalystDialog extends SmDialog {
    private JPanel panelSource;
    private JPanel panelOverlayAnalyst;
    private JPanel panelTarget;

    private JList listOverlayAnalystType;// 叠加分析方法
    private JLabel labelSourceDatasource;
    private DatasourceComboBox comboboxSourceDatasource;// 源数据源
    private JLabel labelSourceDataset;
    private DatasetComboBox comboboxSourceDataset;// 源数据集
    private JLabel labelOverlayAnalystDatasource;
    private DatasourceComboBox comboboxOverlayAnalystDatasource;//叠加数据源
    private JLabel labelOverlayAnalystDataset;
    private DatasetComboBox comboboxOverlayAnalystDataset;//叠加数据集
    private JLabel labelTargetDatasource;
    private DatasourceComboBox comboboxTargetDatasource;//目标数据源
    private JLabel labelTargetDataset;
    private JTextField textFieldTargetDataset;//目标数据集
    private JButton buttonFieldsSet;//字段设置
    private JLabel labelTolerance;
    private JTextField textFieldTolerance;//容限
    private JLabel labelToleranceUnity;//容限单位
    private JCheckBox checkboxResultAnalyst;
    private JButton buttonOK;
    private JButton buttonCancel;

    private OverlayAnalystType OVERLAYANALYSTTTYPE = OverlayAnalystType.CLIP;
    private final String clipResultDatasetName = "ClipResult";
    private final String unionResultDatasetName = "UnionResult";
    private final String eraseResultDatasetName = "EraseResult";
    private final String intersectResultDatasetName = "IntersectResult";
    private final String identityResultDatasetName = "IdentityResult";
    private final String xORResultDatasetName = "XORResult";
    private final String updateResultDatasetName = "UpdateResult";
    private final Color WORNINGCOLOR = Color.red;
    private final Color DEFUALTCOLOR = Color.black;
    private OverlayAnalystParameter parameter;


    private final int ALLTYPE = 0;
    private final int REGIONTYPE = 1;
    private ListSelectionListener listSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            listSelectionChanged();
        }
    };
    private ActionListener buttonCancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            disposeInfo();
        }
    };

    private void disposeInfo() {
        removeEvents();
        OverlayAnalystDialog.this.dispose();
    }

    private void listSelectionChanged() {
        int index = listOverlayAnalystType.getSelectedIndex();
        switch (index) {
            case 0:
                // 裁剪设置
                initComboboxsInfo(ALLTYPE);
                initTextFieldTargetDataset(clipResultDatasetName);
                buttonFieldsSet.setEnabled(false);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.CLIP;
                break;
            case 1:
                // 合并设置
                initComboboxsInfo(REGIONTYPE);
                initTextFieldTargetDataset(unionResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.UNION;
                break;
            case 2:
                // 擦除设置
                initComboboxsInfo(ALLTYPE);
                initTextFieldTargetDataset(eraseResultDatasetName);
                buttonFieldsSet.setEnabled(false);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.ERASE;
                break;
            case 3:
                // 求交设置
                initComboboxsInfo(ALLTYPE);
                initTextFieldTargetDataset(intersectResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.INTERSECT;
                break;
            case 4:
                // 同一设置
                initComboboxsInfo(ALLTYPE);
                initTextFieldTargetDataset(identityResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.IDENTITY;
                break;
            case 5:
                // 对称差设置
                initComboboxsInfo(REGIONTYPE);
                initTextFieldTargetDataset(xORResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.XOR;
                break;
            case 6:
                // 更新设置
                initComboboxsInfo(REGIONTYPE);
                initTextFieldTargetDataset(updateResultDatasetName);
                buttonFieldsSet.setEnabled(false);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.UPDATE;
                break;
            default:
                break;
        }
    }

    private ItemListener sourceDatasetItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != comboboxSourceDataset.getSelectedDataset()) {
                    Dataset selectedDataset = comboboxSourceDataset.getSelectedDataset();
                    resetTextFieldToleranceInfo(selectedDataset);
                    if (null != comboboxOverlayAnalystDatasource.getSelectedDatasource()) {
                        // 重置叠加数据集选项
                        resetItemToComboBox(comboboxOverlayAnalystDataset, comboboxOverlayAnalystDatasource.getSelectedDatasource(), REGIONTYPE);
                    }
                    if (selectedDataset.getType().equals(DatasetType.REGION)) {
                        // 删除叠加数据集中与源数据集选项中相同的数据集
                        comboboxOverlayAnalystDataset.removeDataset(selectedDataset);
                    }
                }
            }
        }
    };
    private CaretListener textFieldTargetDatasetCaretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            Datasource datasource = comboboxTargetDatasource.getSelectedDatasource();
            String text = textFieldTargetDataset.getText();
            if (null != datasource && null != datasource.getDatasets()) {
                Datasets datasets = datasource.getDatasets();
                if (!datasets.getAvailableDatasetName(text).equals(text)) {
                    textFieldTargetDataset.setForeground(WORNINGCOLOR);
                    buttonOK.setEnabled(false);
                } else {
                    textFieldTargetDataset.setForeground(DEFUALTCOLOR);
                    buttonOK.setEnabled(true);
                }
            }
        }
    };
    private ActionListener buttonFieldsSetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (null != comboboxSourceDataset.getSelectedDataset() && null != comboboxOverlayAnalystDataset.getSelectedDataset()) {
                FieldsSetDialog fieldSetDialog = new FieldsSetDialog((DatasetVector) comboboxSourceDataset.getSelectedDataset(), (DatasetVector) comboboxOverlayAnalystDataset.getSelectedDataset());
                if (fieldSetDialog.showDialog().equals(DialogResult.OK)) {
                    if (null != fieldSetDialog.getSourceFields() && null != fieldSetDialog.getOverlayAnalystFields()) {
                        parameter.setSourceRetainedFields(fieldSetDialog.getSourceFields());
                        parameter.setOperationRetainedFields(fieldSetDialog.getOverlayAnalystFields());
                    }
                }
            }
        }
    };
    private ActionListener buttonOKListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonOKClicked();
        }
    };

    private void buttonOKClicked() {
        DatasetVector targetDataset = null;
        if (!StringUtilities.isNullOrEmpty(textFieldTolerance.getText())) {
            // 设置容限
            parameter.setTolerance(Double.valueOf(textFieldTolerance.getText()));
        }
        FormProgress progress = new FormProgress();
        progress.setTitle(SpatialAnalystProperties.getString("String_Form_OverlayAnalyst"));
        IOverlayAnalyst overlayAnalyst = new OverlayAnalystCallable();
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            overlayAnalyst.setSourceDataset((DatasetVector) comboboxSourceDataset.getSelectedDataset());
        }
        if (null != comboboxOverlayAnalystDataset.getSelectedDataset()) {
            overlayAnalyst.setOverlayAnalystDataset((DatasetVector) comboboxOverlayAnalystDataset.getSelectedDataset());
        }
        if (null != comboboxTargetDatasource.getSelectedDatasource()) {
            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
            if (null != comboboxSourceDataset.getSelectedDataset()) {
                datasetVectorInfo.setType(comboboxSourceDataset.getSelectedDataset().getType());
                datasetVectorInfo.setEncodeType(comboboxSourceDataset.getSelectedDataset().getEncodeType());
            }
            if (comboboxTargetDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(textFieldTargetDataset.getText()).equals(textFieldTargetDataset.getText())) {
                // 名称合法时可以设置名称
                datasetVectorInfo.setName(textFieldTargetDataset.getText());
            }
            targetDataset = comboboxTargetDatasource.getSelectedDatasource().getDatasets().create(datasetVectorInfo);
            overlayAnalyst.setTargetDataset(targetDataset);
        }
        overlayAnalyst.setType(OVERLAYANALYSTTTYPE);
        overlayAnalyst.setOverlayAnalystParameter(parameter);
        if (null != targetDataset) {
            progress.doWork((UpdateProgressCallable) overlayAnalyst);
        }
        if (checkboxResultAnalyst.isSelected() && null != targetDataset) {
            showResult(targetDataset);
        }
        OverlayAnalystDialog.this.dispose();
    }

    public OverlayAnalystDialog() {
        super();
        initComponents();
        initLayout();
        initResources();
        registEvents();
        setSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initLayout() {
        initPanelSourceLayout();
        initPanelOverlayAnalystLayout();
        initPanelTargetLayout();
        JScrollPane scrollPane = new JScrollPane();
        listOverlayAnalystType.setCellRenderer(new ListCellRenderer<Object>() {

            @Override
            public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                ((DataCell) value).setSelected(isSelected);
                return (DataCell) value;
            }
        });
        scrollPane.setViewportView(listOverlayAnalystType);
        JPanel panelBasicAnalyst = new JPanel();
        this.setLayout(new GridBagLayout());
        panelBasicAnalyst.setLayout(new GridBagLayout());
        panelBasicAnalyst.add(this.panelSource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        panelBasicAnalyst.add(this.panelOverlayAnalyst, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        panelBasicAnalyst.add(this.panelTarget, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridBagLayout());
        panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(2, 0, 10, 10));
        this.add(scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(5).setFill(GridBagConstraints.BOTH));
        this.add(panelBasicAnalyst, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(5).setFill(GridBagConstraints.BOTH));
        this.add(panelButton, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
    }

    private void initPanelTargetLayout() {
        this.panelTarget.setLayout(new GridBagLayout());
        this.panelTarget.add(this.labelTargetDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.comboboxTargetDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.labelTargetDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.textFieldTargetDataset, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.buttonFieldsSet, new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.labelTolerance, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.textFieldTolerance, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.labelToleranceUnity, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.checkboxResultAnalyst, new GridBagConstraintsHelper(0, 3, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 1));
    }

    private void initPanelOverlayAnalystLayout() {
        this.panelOverlayAnalyst.setLayout(new GridBagLayout());
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
    }

    private void initPanelSourceLayout() {
        this.panelSource.setLayout(new GridBagLayout());
        this.panelSource.add(this.labelSourceDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelSource.add(this.comboboxSourceDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSource.add(this.labelSourceDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelSource.add(this.comboboxSourceDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
    }

    private void registEvents() {
        removeEvents();
        this.listOverlayAnalystType.addListSelectionListener(this.listSelectionListener);
        this.comboboxSourceDataset.addItemListener(this.sourceDatasetItemListener);
        this.textFieldTargetDataset.addCaretListener(this.textFieldTargetDatasetCaretListener);
        this.buttonFieldsSet.addActionListener(this.buttonFieldsSetListener);
        this.buttonOK.addActionListener(this.buttonOKListener);
        this.buttonCancel.addActionListener(this.buttonCancelListener);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeInfo();
            }
        });
    }

    private void showResult(DatasetVector dataset) {
        String name = MapUtilities.getAvailableMapName(
                MessageFormat.format("{0}@{1}", dataset.getName(), dataset.getDatasource().getAlias()), true);
        IFormMap form = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);
        Dataset[] datasets = new Dataset[3];
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            GeoStyle sourceGeoStyle = new GeoStyle();
            sourceGeoStyle.setLineColor(new Color(115, 115, 115));
            sourceGeoStyle.setLineWidth(0.1);
            sourceGeoStyle.setMarkerSize(new Size2D(2.4, 2.4));
            sourceGeoStyle.setFillOpaqueRate(50);
            sourceGeoStyle.setFillForeColor(new Color(233, 255, 190));
            sourceGeoStyle.setFillBackOpaque(false);
            Layer sourceLayer = MapUtilities.addDatasetToMap(form.getMapControl().getMap(), comboboxSourceDataset.getSelectedDataset(), true);
            LayerSettingVector sourceLayerSetting = (LayerSettingVector) sourceLayer.getAdditionalSetting();
            sourceLayerSetting.setStyle(sourceGeoStyle);
        }
        if (null != comboboxOverlayAnalystDataset.getSelectedDataset()) {
            GeoStyle overlayAnalystGeoStyle = new GeoStyle();
            overlayAnalystGeoStyle.setLineColor(new Color(151, 191, 242));
            overlayAnalystGeoStyle.setLineWidth(0.1);
            overlayAnalystGeoStyle.setFillOpaqueRate(50);
            overlayAnalystGeoStyle.setFillForeColor(new Color(151, 191, 242));
            overlayAnalystGeoStyle.setFillBackOpaque(false);
            Layer overlayAnalystLayer = MapUtilities.addDatasetToMap(form.getMapControl().getMap(), comboboxOverlayAnalystDataset.getSelectedDataset(), true);
            LayerSettingVector overlayAnalystLayerSetting = (LayerSettingVector) overlayAnalystLayer.getAdditionalSetting();
            overlayAnalystLayerSetting.setStyle(overlayAnalystGeoStyle);
        }
        GeoStyle resultGeoStyle = new GeoStyle();
        resultGeoStyle.setLineColor(new Color(255, 0, 0));
        resultGeoStyle.setLineWidth(0.1);
        resultGeoStyle.setMarkerSize(new Size2D(2.4, 2.4));
        resultGeoStyle.setFillOpaqueRate(50);
        resultGeoStyle.setFillForeColor(new Color(255, 255, 255));
        resultGeoStyle.setFillBackOpaque(false);
        Layer resultLayer = MapUtilities.addDatasetToMap(form.getMapControl().getMap(), dataset, true);
        LayerSettingVector resultLayerSetting = (LayerSettingVector) resultLayer.getAdditionalSetting();
        resultLayerSetting.setStyle(resultGeoStyle);
        // 更新地图属性面板
        Application.getActiveApplication().resetActiveForm();
        form.getMapControl().getMap().refresh();
        UICommonToolkit.getLayersManager().setMap(form.getMapControl().getMap());
    }

    private void removeEvents() {
        this.listOverlayAnalystType.removeListSelectionListener(this.listSelectionListener);
        this.comboboxSourceDataset.removeItemListener(this.sourceDatasetItemListener);
        this.textFieldTargetDataset.removeCaretListener(this.textFieldTargetDatasetCaretListener);
        this.buttonFieldsSet.removeActionListener(this.buttonFieldsSetListener);
        this.buttonOK.removeActionListener(this.buttonOKListener);
        this.buttonCancel.removeActionListener(this.buttonCancelListener);
    }

    /**
     * 添加叠加分析项
     */
    private void addListItem() {
        DefaultListModel<Object> listModel = new DefaultListModel<Object>();
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Clip"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Clip.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Union"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Union.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Erase"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Erase.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Intersect"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Intersect.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Identity"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Identity.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_XOR"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_XOR.png"))));
        listModel.addElement(new DataCell(SpatialAnalystProperties.getString("String_OverlayAnalystMethod_Update"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Update.png"))));
        this.listOverlayAnalystType.setModel(listModel);
        this.listOverlayAnalystType.setSelectedIndex(0);
        this.listOverlayAnalystType.setCellRenderer(new CommonListCellRenderer());
    }

    private void initResources() {
        this.setTitle(SpatialAnalystProperties.getString("String_Form_OverlayAnalyst"));
        this.labelSourceDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
        this.labelSourceDataset.setText(CommonProperties.getString("String_Label_Dataset"));
        this.labelOverlayAnalystDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
        this.labelOverlayAnalystDataset.setText(CommonProperties.getString("String_Label_Dataset"));
        this.labelTargetDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
        this.labelTargetDataset.setText(CommonProperties.getString("String_Label_Dataset"));
        this.buttonFieldsSet.setText(SpatialAnalystProperties.getString("String_Button_FieldsSetting"));
        this.labelTolerance.setText(SpatialAnalystProperties.getString("String_Label_Tolerance"));
        this.checkboxResultAnalyst.setText(SpatialAnalystProperties.getString("String_CheckBox_ResultComparison"));
        this.panelSource.setBorder(new TitledBorder(ControlsProperties.getString("String_GroupBox_SourceDataset")));
        this.panelOverlayAnalyst.setBorder(new TitledBorder(SpatialAnalystProperties.getString("String_GroupBox_OverlayDataset")));
        this.panelTarget.setBorder(new TitledBorder(SpatialAnalystProperties.getString("String_ResultSet")));
    }

    private void initComponents() {
        this.listOverlayAnalystType = new JList();
        this.labelSourceDatasource = new JLabel();
        this.comboboxSourceDatasource = new DatasourceComboBox();
        this.labelSourceDataset = new JLabel();
        this.comboboxSourceDataset = new DatasetComboBox(new Dataset[0]);
        this.labelOverlayAnalystDatasource = new JLabel();
        this.comboboxOverlayAnalystDatasource = new DatasourceComboBox();
        this.labelOverlayAnalystDataset = new JLabel();
        this.comboboxOverlayAnalystDataset = new DatasetComboBox(new Dataset[0]);
        this.comboboxTargetDatasource = new DatasourceComboBox();
        this.labelTargetDatasource = new JLabel();
        this.labelTargetDataset = new JLabel();
        this.textFieldTargetDataset = new JTextField();
        this.buttonFieldsSet = new JButton();
        this.labelTolerance = new JLabel();
        this.textFieldTolerance = new JTextField();
        this.labelToleranceUnity = new JLabel();
        this.checkboxResultAnalyst = new JCheckBox();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.panelSource = new JPanel();
        this.panelOverlayAnalyst = new JPanel();
        this.panelTarget = new JPanel();
        this.buttonFieldsSet.setEnabled(false);
        this.parameter = new OverlayAnalystParameter();
        addListItem();
        initComboboxsInfo(ALLTYPE);
        initTextFieldTargetDataset(clipResultDatasetName);
    }

    private void initComboboxsInfo(int flag) {
        Datasource analystDatasource = null;
        if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
            analystDatasource = Application.getActiveApplication().getActiveDatasources()[0];
            comboboxSourceDatasource.setSelectedDatasource(analystDatasource);
            comboboxOverlayAnalystDatasource.setSelectedDatasource(analystDatasource);
            comboboxTargetDatasource.setSelectedDatasource(analystDatasource);
        } else {
            analystDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
        }
        resetItemToComboBox(comboboxSourceDataset, analystDatasource, flag);
        resetItemToComboBox(comboboxOverlayAnalystDataset, analystDatasource, REGIONTYPE);
        if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
            Dataset sourceDataset = Application.getActiveApplication().getActiveDatasets()[0];
            if (comboboxSourceDataset.hasDataset(sourceDataset.getName())) {
                comboboxSourceDataset.setSelectedDataset(sourceDataset);
            }
            if (sourceDataset.getType().equals(DatasetType.REGION)) {
                comboboxOverlayAnalystDataset.removeDataset(sourceDataset);
            }
        } else {
            if (comboboxSourceDataset.getSelectedDataset().getType().equals(DatasetType.REGION)) {
                comboboxOverlayAnalystDataset.removeDataset(comboboxSourceDataset.getSelectedDataset());
            }
        }
    }

    private void initTextFieldTargetDataset(String targetDatasetName) {
        if (null != comboboxTargetDatasource.getSelectedDatasource()) {
            textFieldTargetDataset.setText(comboboxTargetDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(targetDatasetName));
        }
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            resetTextFieldToleranceInfo(comboboxSourceDataset.getSelectedDataset());
        }
    }

    private void resetTextFieldToleranceInfo(Dataset dataset) {
        textFieldTolerance.setText(String.valueOf(DatasetUtilities.getDefaultTolerance((DatasetVector) dataset).getNodeSnap()));
        this.labelToleranceUnity.setText(LengthUnit.convertForm(dataset.getPrjCoordSys().getCoordUnit()).toString());
    }

    /**
     * 为DatasetCombobox插入选项
     *
     * @param sourceDataset
     * @param datasource
     * @param flag
     * @return
     */
    private boolean resetItemToComboBox(DatasetComboBox sourceDataset, Datasource datasource, int flag) {
        sourceDataset.removeAllItems();
        int count = 0;
        if (null != datasource) {
            Datasets datasets = datasource.getDatasets();
            if (flag == ALLTYPE) {
                for (int i = 0; i < datasets.getCount(); i++) {
                    if (datasets.get(i).getType() == DatasetType.LINE || datasets.get(i).getType() == DatasetType.POINT || datasets.get(i).getType() == DatasetType.REGION) {
                        DataCell cell = new DataCell();
                        cell.initDatasetType(datasets.get(i));
                        sourceDataset.addItem(cell);
                        count++;
                    }
                }
            } else {
                for (int i = 0; i < datasets.getCount(); i++) {
                    if (datasets.get(i).getType() == DatasetType.REGION) {
                        DataCell cell = new DataCell();
                        cell.initDatasetType(datasets.get(i));
                        sourceDataset.addItem(cell);
                        count++;
                    }
                }
            }
        }
        if (0 < count) {
            sourceDataset.setSelectedIndex(0);
        }
        return 0 < count;
    }
}
