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
import com.supermap.desktop.ui.FieldsSetDialog;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.*;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.ui.enums.OverlayAnalystType;
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
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.text.NumberFormat;

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
    private SMFormattedTextField textFieldTolerance;//容限
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
    private final int CLIP_TYPE = 0;
    private final int UNION_TYPE = 1;
    private final int ERASE_TYPE = 2;
    private final int INTERSECT_TYPE = 3;
    private final int IDENTITY_TYPE = 4;
    private final int XOR_TYPE = 5;
    private final int UPDATE_TYPE = 6;

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
    private ItemListener sourceDatasourceListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectIndex = listOverlayAnalystType.getSelectedIndex();
                if (selectIndex == UNION_TYPE || selectIndex == XOR_TYPE || selectIndex == UPDATE_TYPE) {
                    resetItemToComboBox(comboboxSourceDataset, comboboxSourceDatasource.getSelectedDatasource(), REGIONTYPE);
                } else {
                    resetItemToComboBox(comboboxSourceDataset, comboboxSourceDatasource.getSelectedDatasource(), ALLTYPE);
                }
                if (null != comboboxSourceDataset.getSelectedDataset() && comboboxSourceDataset.getSelectedDataset().getType().equals(DatasetType.REGION)) {
                    comboboxOverlayAnalystDataset.removeDataset(comboboxSourceDataset.getSelectedDataset());
                }
                setButtonOKEnable();
            }
        }
    };
    private ItemListener overlayAnalystDatasourceListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                resetItemToComboBox(comboboxOverlayAnalystDataset, comboboxOverlayAnalystDatasource.getSelectedDatasource(), REGIONTYPE);
                if (null != comboboxSourceDataset.getSelectedDataset() && comboboxSourceDataset.getSelectedDataset().getType().equals(DatasetType.REGION)) {
                    comboboxOverlayAnalystDataset.removeDataset(comboboxSourceDataset.getSelectedDataset());
                }
                setButtonOKEnable();
            }
        }
    };
    private ItemListener targetDatasourceListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                initTextFieldTargetDataset(getDefualtName());
            }
        }
    };
    private ItemListener overLayAnalystDatasetListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != comboboxOverlayAnalystDataset.getSelectedDataset() && comboboxSourceDataset.getSelectedDataset().equals(comboboxOverlayAnalystDataset.getSelectedDataset())) {
                    Dataset selectedDataset = comboboxOverlayAnalystDataset.getSelectedDataset();
                    resetTextFieldToleranceInfo(selectedDataset);
                    if (null != comboboxSourceDatasource.getSelectedDatasource()) {
                        // 重置源数据集选项
                        resetItemToComboBox(comboboxSourceDataset, comboboxSourceDatasource.getSelectedDatasource(), REGIONTYPE);
                    }
                    // 删除叠加数据集中与源数据集选项中相同的数据集
                    comboboxSourceDataset.removeDataset(selectedDataset);
                }
            }
        }
    };

    private void disposeInfo() {
        removeEvents();
        OverlayAnalystDialog.this.dispose();
    }

    private String getDefualtName() {
        int index = listOverlayAnalystType.getSelectedIndex();
        String defualtName = "";
        switch (index) {
            case CLIP_TYPE:
                defualtName = clipResultDatasetName;
                break;
            case UNION_TYPE:
                defualtName = unionResultDatasetName;
                break;
            case ERASE_TYPE:
                defualtName = eraseResultDatasetName;
                break;
            case INTERSECT_TYPE:
                defualtName = intersectResultDatasetName;
                break;
            case IDENTITY_TYPE:
                defualtName = identityResultDatasetName;
                OVERLAYANALYSTTTYPE = OverlayAnalystType.IDENTITY;
                break;
            case XOR_TYPE:
                defualtName = xORResultDatasetName;
                break;
            case UPDATE_TYPE:
                defualtName = updateResultDatasetName;
                break;
            default:
                break;
        }
        return defualtName;
    }

    private void listSelectionChanged() {
        int index = listOverlayAnalystType.getSelectedIndex();
        Dataset sourceDataset = comboboxSourceDataset.getSelectedDataset();
        Dataset overlayAanlaystDataset = comboboxOverlayAnalystDataset.getSelectedDataset();
        switch (index) {
            case CLIP_TYPE:
                // 裁剪设置
                resetComboboxsInfo(ALLTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
                initTextFieldTargetDataset(clipResultDatasetName);
                buttonFieldsSet.setEnabled(false);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.CLIP;
                break;
            case UNION_TYPE:
                // 合并设置
                resetComboboxsInfo(REGIONTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
                initTextFieldTargetDataset(unionResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.UNION;
                break;
            case ERASE_TYPE:
                // 擦除设置
                resetComboboxsInfo(ALLTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
                initTextFieldTargetDataset(eraseResultDatasetName);
                buttonFieldsSet.setEnabled(false);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.ERASE;
                break;
            case INTERSECT_TYPE:
                // 求交设置
                resetComboboxsInfo(ALLTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
                initTextFieldTargetDataset(intersectResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.INTERSECT;
                break;
            case IDENTITY_TYPE:
                // 同一设置
                resetComboboxsInfo(ALLTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
                initTextFieldTargetDataset(identityResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.IDENTITY;
                break;
            case XOR_TYPE:
                // 对称差设置
                resetComboboxsInfo(REGIONTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
                initTextFieldTargetDataset(xORResultDatasetName);
                buttonFieldsSet.setEnabled(true);
                OVERLAYANALYSTTTYPE = OverlayAnalystType.XOR;
                break;
            case UPDATE_TYPE:
                // 更新设置
                resetComboboxsInfo(REGIONTYPE);
                if (null != sourceDataset && null != overlayAanlaystDataset) {
                    comboboxSourceDataset.setSelectedDataset(sourceDataset);
                    comboboxOverlayAnalystDataset.setSelectedDataset(overlayAanlaystDataset);
                }
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
                if (null != comboboxSourceDataset.getSelectedDataset() && comboboxSourceDataset.getSelectedDataset().equals(comboboxOverlayAnalystDataset.getSelectedDataset())) {
                    Dataset selectedDataset = comboboxSourceDataset.getSelectedDataset();
                    resetTextFieldToleranceInfo(selectedDataset);
                    if (null != comboboxOverlayAnalystDatasource.getSelectedDatasource()) {
                        // 重置叠加数据集选项
                        resetItemToComboBox(comboboxOverlayAnalystDataset, comboboxOverlayAnalystDatasource.getSelectedDatasource(), REGIONTYPE);
                    }
                    // 删除叠加数据集中与源数据集选项中相同的数据集
                    comboboxOverlayAnalystDataset.removeDataset(selectedDataset);
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
                    if (null == comboboxSourceDataset.getSelectedDataset() || null == comboboxOverlayAnalystDataset.getSelectedDataset()) {
                        buttonOK.setEnabled(false);
                    } else {
                        buttonOK.setEnabled(true);
                    }
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
        Dataset sourceDataset = null;
        Dataset overlayAnalystDataset = null;
        FormProgress progress = new FormProgress();
        progress.setTitle(SpatialAnalystProperties.getString("String_Form_OverlayAnalyst"));
        IOverlayAnalyst overlayAnalyst = new OverlayAnalystCallable();
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            sourceDataset = comboboxSourceDataset.getSelectedDataset();

        }
        if (null != comboboxOverlayAnalystDataset.getSelectedDataset()) {
            overlayAnalystDataset = comboboxOverlayAnalystDataset.getSelectedDataset();
        }
        if (null != sourceDataset && null != overlayAnalystDataset && !isSameProjection(sourceDataset.getPrjCoordSys(), overlayAnalystDataset.getPrjCoordSys())) {
            Application.getActiveApplication().getOutput().output(SpatialAnalystProperties.getString("String_PrjCoordSys_Different") + "\n" + SpatialAnalystProperties.getString("String_Parameters"));
            Application.getActiveApplication().getOutput().output(MessageFormat.format(SpatialAnalystProperties.getString("String_OverlayAnalyst_Failed"), sourceDataset.getName() + "@" + sourceDataset.getDatasource().getAlias()
                    , overlayAnalystDataset.getName() + "@" + overlayAnalystDataset.getDatasource().getAlias(), OVERLAYANALYSTTTYPE.toString()));
            return;
        } else if (null != sourceDataset && null != overlayAnalystDataset && isSameProjection(sourceDataset.getPrjCoordSys(), overlayAnalystDataset.getPrjCoordSys())) {
            overlayAnalyst.setSourceDataset((DatasetVector) sourceDataset);
            overlayAnalyst.setOverlayAnalystDataset((DatasetVector) overlayAnalystDataset);
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
            targetDataset.setPrjCoordSys(comboboxSourceDatasource.getSelectedDatasource().getPrjCoordSys());
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

    private boolean isSameProjection(PrjCoordSys prjCoordSys, PrjCoordSys prjCoordSys1) {
        if (prjCoordSys.getType() != prjCoordSys1.getType()) {
            return false;
        }
        if (prjCoordSys.getGeoCoordSys() == prjCoordSys1.getGeoCoordSys()) {
            return true;
        }
        if (prjCoordSys.getGeoCoordSys() == null || prjCoordSys1.getGeoCoordSys() == null) {
            return false;
        }
        if (prjCoordSys.getGeoCoordSys().getType() != prjCoordSys1.getGeoCoordSys().getType()) {
            return false;
        }
        return true;
    }

    public OverlayAnalystDialog() {
        super();
        initComponents();
        initLayout();
        initResources();
        registEvents();
        this.componentList.add(this.buttonOK);
        this.componentList.add(this.buttonCancel);
        this.setFocusTraversalPolicy(policy);
        setSize(new Dimension(560, 420));
        setMinimumSize(new Dimension(560, 420));
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
        scrollPane.setPreferredSize(new Dimension(120, 320));
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
        this.add(scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(0, 1).setInsets(10, 10, 10, 0).setFill(GridBagConstraints.BOTH));
        this.add(panelBasicAnalyst, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(5).setFill(GridBagConstraints.BOTH));
        this.add(panelButton, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));
    }

    private void initPanelTargetLayout() {
        this.panelTarget.setLayout(new GridBagLayout());
        this.panelTarget.add(this.labelTargetDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.comboboxTargetDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelTarget.add(this.labelTargetDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.textFieldTargetDataset, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelTarget.add(this.buttonFieldsSet, new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.labelTolerance, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.textFieldTolerance, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelTarget.add(this.labelToleranceUnity, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.checkboxResultAnalyst, new GridBagConstraintsHelper(0, 3, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0));
    }

    private void initPanelOverlayAnalystLayout() {
        this.panelOverlayAnalyst.setLayout(new GridBagLayout());
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
    }

    private void initPanelSourceLayout() {
        this.panelSource.setLayout(new GridBagLayout());
        this.panelSource.add(this.labelSourceDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSource.add(this.comboboxSourceDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.panelSource.add(this.labelSourceDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSource.add(this.comboboxSourceDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
    }

    private void registEvents() {
        removeEvents();
        this.listOverlayAnalystType.addListSelectionListener(this.listSelectionListener);
        this.comboboxSourceDatasource.addItemListener(this.sourceDatasourceListener);
        this.comboboxSourceDataset.addItemListener(this.sourceDatasetItemListener);
        this.comboboxOverlayAnalystDataset.addItemListener(this.overLayAnalystDatasetListener);
        this.comboboxOverlayAnalystDatasource.addItemListener(this.overlayAnalystDatasourceListener);
        this.comboboxTargetDatasource.addItemListener(this.targetDatasourceListener);
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
            sourceLayer.setCaption(SpatialAnalystProperties.getString("String_OverlayAnalyst_SourceDataset"));
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
            overlayAnalystLayer.setCaption(SpatialAnalystProperties.getString("String_OverlayAnalyst_OverlayDataset"));
            LayerSettingVector overlayAnalystLayerSetting = (LayerSettingVector) overlayAnalystLayer.getAdditionalSetting();
            overlayAnalystLayerSetting.setStyle(overlayAnalystGeoStyle);
        }
        GeoStyle resultGeoStyle = new GeoStyle();
        resultGeoStyle.setLineColor(new Color(255, 0, 0));
        resultGeoStyle.setLineWidth(0.1);
        resultGeoStyle.setMarkerSize(new Size2D(2.4, 2.4));
        resultGeoStyle.setFillOpaqueRate(50);
        resultGeoStyle.setFillSymbolID(1);
        resultGeoStyle.setFillForeColor(new Color(255, 255, 255));
        resultGeoStyle.setFillBackOpaque(false);
        Layer resultLayer = MapUtilities.addDatasetToMap(form.getMapControl().getMap(), dataset, true);
        resultLayer.setCaption(SpatialAnalystProperties.getString("String_OverlayAnalyst_ResultDataset"));
        LayerSettingVector resultLayerSetting = (LayerSettingVector) resultLayer.getAdditionalSetting();
        resultLayerSetting.setStyle(resultGeoStyle);
        // 更新地图属性面板
        Application.getActiveApplication().resetActiveForm();
        form.getMapControl().getMap().refresh();
        UICommonToolkit.getLayersManager().setMap(form.getMapControl().getMap());
    }

    private void removeEvents() {
        this.listOverlayAnalystType.removeListSelectionListener(this.listSelectionListener);
        this.comboboxSourceDatasource.removeItemListener(this.sourceDatasourceListener);
        this.comboboxSourceDataset.removeItemListener(this.sourceDatasetItemListener);
        this.comboboxOverlayAnalystDatasource.removeItemListener(this.overlayAnalystDatasourceListener);
        this.comboboxTargetDatasource.removeItemListener(this.targetDatasourceListener);
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
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_Clip"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Clip.png"))));
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_Union"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Union.png"))));
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_Erase"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Erase.png"))));
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_Intersect"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Intersect.png"))));
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_Identity"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Identity.png"))));
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_XOR"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_XOR.png"))));
        listModel.addElement(new DataCell(ControlsProperties.getString("String_OverlayAnalystMethod_Update"), new ImageIcon(OverlayAnalystDialog.class.getResource("/OverlayAnalyst/Image_OverlayAnalyst_Update.png"))));
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
        this.listOverlayAnalystType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.labelSourceDatasource = new JLabel();
        this.comboboxSourceDatasource = new DatasourceComboBox();
        this.labelSourceDataset = new JLabel();
        this.comboboxSourceDataset = new DatasetComboBox(new Dataset[0]);
        this.labelOverlayAnalystDatasource = new JLabel();
        this.comboboxOverlayAnalystDatasource = new DatasourceComboBox();
        this.labelOverlayAnalystDataset = new JLabel();
        this.comboboxOverlayAnalystDataset = new DatasetComboBox(new Dataset[0]);
        this.comboboxTargetDatasource = new DatasourceComboBox();
        removeReadOnlyAndMemoryDatasource();
        this.labelTargetDatasource = new JLabel();
        this.labelTargetDataset = new JLabel();
        this.textFieldTargetDataset = new JTextField();
        this.buttonFieldsSet = new JButton();
        this.labelTolerance = new JLabel();
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(20);
        NumberFormatter numberFormatter = new NumberFormatter(numberInstance);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setMinimum(0.0);
        this.textFieldTolerance = new SMFormattedTextField(numberFormatter);
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

    private void removeReadOnlyAndMemoryDatasource() {
        Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
        for (int i = 0; i < datasources.getCount(); i++) {
            if (datasources.get(i).isReadOnly()) {
                comboboxTargetDatasource.removeDataSource(datasources.get(i));
            }
        }
    }

    private void resetComboboxsInfo(int flag) {
        //重置数据集下拉控件
        resetItemToComboBox(comboboxSourceDataset, comboboxSourceDatasource.getSelectedDatasource(), flag);
        resetItemToComboBox(comboboxOverlayAnalystDataset, comboboxOverlayAnalystDatasource.getSelectedDatasource(), REGIONTYPE);
        if (null != comboboxSourceDataset.getSelectedDataset() && comboboxSourceDataset.getSelectedDataset().getType().equals(DatasetType.REGION)) {
            comboboxOverlayAnalystDataset.removeDataset(comboboxSourceDataset.getSelectedDataset());
        }
    }

    private void initComboboxsInfo(int flag) {
        //初始化数据集下拉控件
        Datasource analystDatasource = null;
        if (null != Application.getActiveApplication().getActiveDatasources() && Application.getActiveApplication().getActiveDatasources().length > 0) {
            analystDatasource = Application.getActiveApplication().getActiveDatasources()[0];
            comboboxSourceDatasource.setSelectedDatasource(analystDatasource);
            comboboxOverlayAnalystDatasource.setSelectedDatasource(analystDatasource);
            comboboxTargetDatasource.setSelectedDatasource(analystDatasource);
        }
        resetItemToComboBox(comboboxSourceDataset, comboboxSourceDatasource.getSelectedDatasource(), flag);
        resetItemToComboBox(comboboxOverlayAnalystDataset, comboboxOverlayAnalystDatasource.getSelectedDatasource(), REGIONTYPE);
        if (null != Application.getActiveApplication().getActiveDatasets() && Application.getActiveApplication().getActiveDatasets().length > 0) {
            Dataset sourceDataset = Application.getActiveApplication().getActiveDatasets()[0];
            if (comboboxSourceDataset.hasDataset(sourceDataset.getName())) {
                comboboxSourceDataset.setSelectedDataset(sourceDataset);
            }
        }
        if (null != comboboxSourceDataset.getSelectedDataset() && comboboxSourceDataset.getSelectedDataset().getType().equals(DatasetType.REGION)) {
            comboboxOverlayAnalystDataset.removeDataset(comboboxSourceDataset.getSelectedDataset());
        }
    }

    private void initTextFieldTargetDataset(String targetDatasetName) {
        if (null != comboboxTargetDatasource.getSelectedDatasource()) {
            textFieldTargetDataset.setText(comboboxTargetDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(targetDatasetName));
        } else {
            textFieldTargetDataset.setText("");
        }
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            resetTextFieldToleranceInfo(comboboxSourceDataset.getSelectedDataset());
        }
        setButtonOKEnable();
    }

    private void setButtonOKEnable() {
        if (null == comboboxSourceDataset.getSelectedDataset() || null == comboboxOverlayAnalystDataset.getSelectedDataset() || StringUtilities.isNullOrEmpty(textFieldTargetDataset.getText())) {
            buttonOK.setEnabled(false);
        } else {
            buttonOK.setEnabled(true);
        }
    }

    private void resetTextFieldToleranceInfo(Dataset dataset) {
        this.textFieldTolerance.setValue(DatasetUtilities.getDefaultTolerance((DatasetVector) dataset).getNodeSnap());
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
