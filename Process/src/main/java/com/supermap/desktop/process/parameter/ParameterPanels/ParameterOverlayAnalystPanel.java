package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.LengthUnit;
import com.supermap.desktop.process.parameter.ParameterOverlayAnalystInfo;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterOverlayAnalyst;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FieldsSetDialog;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.enums.OverlayAnalystType;
import com.supermap.desktop.utilities.DatasetUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

/**
 * Created by xie on 2017/2/14.
 */
public class ParameterOverlayAnalystPanel extends JPanel {
    private JPanel panelSource;
    private JPanel panelOverlayAnalyst;
    private JPanel panelTarget;
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

    private ParameterOverlayAnalyst overlayAnalyst;
    private ParameterOverlayAnalystInfo overlayAnalystInfo;

    private final Color WORNINGCOLOR = Color.red;
    private final Color DEFUALTCOLOR = Color.black;

    private boolean isSelectingItem;
    private boolean isAllVectorType;

    public ParameterOverlayAnalystPanel(ParameterOverlayAnalyst overlayAnalyst) {
        this.overlayAnalyst = overlayAnalyst;
        this.overlayAnalystInfo = this.overlayAnalyst.getOverlayAnalystInfo();
        initComponents();
        initResources();
        initLayout();
        initListener();
    }

    private boolean isAllVectorType(OverlayAnalystType analystType) {
        boolean result = true;
        if (analystType == OverlayAnalystType.UNION || analystType == OverlayAnalystType.XOR || analystType == OverlayAnalystType.UPDATE) {
            result = false;
        }
        return result;
    }

    private void initComponents() {

        OverlayAnalystType analystType = overlayAnalyst.getOverlayAnalystType();
        this.isAllVectorType = isAllVectorType(analystType);
        this.labelSourceDatasource = new JLabel();
        this.comboboxSourceDatasource = new DatasourceComboBox();
        this.labelSourceDataset = new JLabel();
        if (comboboxSourceDatasource.getSelectedItemAlias() != null) {
            if (this.isAllVectorType) {
                this.comboboxSourceDataset = new DatasetComboBox(this.comboboxSourceDatasource.getSelectedDatasource().getDatasets());
                this.comboboxSourceDataset.setSupportedDatasetTypes(new DatasetType[]{
                        DatasetType.POINT,
                        DatasetType.LINE,
                        DatasetType.REGION
                });
            } else {
                this.comboboxSourceDataset = new DatasetComboBox(this.comboboxSourceDatasource.getSelectedDatasource().getDatasets());
                this.comboboxSourceDataset.setSupportedDatasetTypes(new DatasetType[]{
                        DatasetType.REGION});
            }
        } else {
            this.comboboxSourceDataset = new DatasetComboBox();
        }
        this.labelOverlayAnalystDatasource = new JLabel();
        this.comboboxOverlayAnalystDatasource = new DatasourceComboBox();
        if (comboboxOverlayAnalystDatasource.getSelectedItemAlias() != null) {
            this.comboboxOverlayAnalystDataset = new DatasetComboBox();
            if (comboboxSourceDataset.getSelectedItem().toString().equals(comboboxOverlayAnalystDataset.getSelectedItem().toString())) {
                this.comboboxOverlayAnalystDataset.removeItem(comboboxOverlayAnalystDataset.getSelectedItem());
            }
        } else {
            this.comboboxOverlayAnalystDataset = new DatasetComboBox();
        }
        this.labelOverlayAnalystDataset = new JLabel();
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
        this.panelSource = new JPanel();
        this.panelOverlayAnalyst = new JPanel();
        this.panelTarget = new JPanel();
        this.buttonFieldsSet.setEnabled(analystType != OverlayAnalystType.CLIP || analystType != OverlayAnalystType.ERASE
                || analystType != OverlayAnalystType.UPDATE);
        if (null != comboboxSourceDataset.getSelectedDataset()) {
            resetTextFieldToleranceInfo(comboboxSourceDataset.getSelectedDataset());
        }
    }

    private void resetTextFieldToleranceInfo(Dataset dataset) {
        this.textFieldTolerance.setValue(DatasetUtilities.getDefaultTolerance((DatasetVector) dataset).getNodeSnap());
        this.labelToleranceUnity.setText(LengthUnit.convertForm(dataset.getPrjCoordSys().getCoordUnit()).toString());
    }

    private void removeReadOnlyAndMemoryDatasource() {
        Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
        for (int i = 0; i < datasources.getCount(); i++) {
            if (datasources.get(i).isReadOnly()) {
                comboboxTargetDatasource.removeDataSource(datasources.get(i));
            }
        }
    }

    private void initListener() {
        this.comboboxSourceDataset.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
                    isSelectingItem = true;
                    if (comboboxSourceDataset.getSelectedItem().toString().equals(comboboxOverlayAnalystDataset.getSelectedItem().toString())) {
                        comboboxOverlayAnalystDataset.removeItem(comboboxOverlayAnalystDataset.getSelectedItem());
                    }
                    overlayAnalystInfo.sourceDataset = (DatasetVector) comboboxSourceDataset.getSelectedDataset();
                    overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                    isSelectingItem = false;
                }
            }
        });
        this.comboboxSourceDatasource.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
                    isSelectingItem = true;
                    comboboxSourceDataset.removeAllItems();
                    Datasets datasets = comboboxSourceDatasource.getSelectedDatasource().getDatasets();
                    int count = datasets.getCount();
                    for (int i = 0; i < count; i++) {
                        Dataset tempDataset = datasets.get(i);
                        if (isAllVectorType) {
                            if (tempDataset.getType() != DatasetType.POINT || tempDataset.getType() != DatasetType.LINE
                                    || tempDataset.getType() != DatasetType.REGION)
                                datasets.delete(i);
                        } else {
                            if (tempDataset.getType() != DatasetType.REGION)
                                datasets.delete(i);
                        }
                    }
                    comboboxSourceDataset.setDatasets(datasets);
                    overlayAnalystInfo.sourceDatatsource = comboboxSourceDatasource.getSelectedDatasource();
                    overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                    isSelectingItem = false;
                }
            }
        });
        this.comboboxOverlayAnalystDatasource.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
                    isSelectingItem = true;
                    Datasets datasets = comboboxOverlayAnalystDatasource.getSelectedDatasource().getDatasets();
                    int count = datasets.getCount();
                    for (int i = 0; i < count; i++) {
                        if (datasets.get(i).getType() != DatasetType.REGION)
                            datasets.delete(i);
                    }
                    comboboxOverlayAnalystDataset.setDatasets(datasets);
                    overlayAnalystInfo.overlayAnalystDatasource = comboboxOverlayAnalystDatasource.getSelectedDatasource();
                    overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                    isSelectingItem = false;
                }
            }
        });
        this.comboboxOverlayAnalystDataset.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
                    isSelectingItem = true;
                    overlayAnalystInfo.overlayAnalystDataset = (DatasetVector) comboboxOverlayAnalystDataset.getSelectedDataset();
                    overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                    isSelectingItem = false;
                }
            }
        });
        this.comboboxTargetDatasource.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
                    isSelectingItem = true;
                    String targetDatasetName = comboboxTargetDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(overlayAnalyst.getOverlayAnalystType().defaultResultName());
                    textFieldTargetDataset.setText(targetDatasetName);
                    isSelectingItem = false;
                }
            }
        });
        this.buttonFieldsSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != comboboxSourceDataset.getSelectedDataset() && null != comboboxOverlayAnalystDataset.getSelectedDataset()) {
                    FieldsSetDialog fieldSetDialog = new FieldsSetDialog((DatasetVector) comboboxSourceDataset.getSelectedDataset(), (DatasetVector) comboboxOverlayAnalystDataset.getSelectedDataset());
                    if (fieldSetDialog.showDialog().equals(DialogResult.OK)) {
                        if (!isSelectingItem && null != fieldSetDialog.getSourceFields() && null != fieldSetDialog.getOverlayAnalystFields()) {
                            isSelectingItem = true;
                            if (null == overlayAnalystInfo.analystParameter) {
                                overlayAnalystInfo.analystParameter = new OverlayAnalystParameter();
                            }
                            overlayAnalystInfo.analystParameter.setSourceRetainedFields(fieldSetDialog.getSourceFields());
                            overlayAnalystInfo.analystParameter.setOperationRetainedFields(fieldSetDialog.getOverlayAnalystFields());
                            overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                            isSelectingItem = false;
                        }
                    }
                }
            }
        });
        this.textFieldTargetDataset.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                Datasource datasource = comboboxTargetDatasource.getSelectedDatasource();
                String text = textFieldTargetDataset.getText();
                if (null != datasource && null != datasource.getDatasets()) {
                    Datasets datasets = datasource.getDatasets();
                    if (!datasets.getAvailableDatasetName(text).equals(text)) {
                        textFieldTargetDataset.setForeground(WORNINGCOLOR);
                    } else {
                        textFieldTargetDataset.setForeground(DEFUALTCOLOR);
                        if (!isSelectingItem && null != comboboxTargetDatasource.getSelectedDatasource()) {
                            isSelectingItem = true;
                            DatasetVectorInfo datasetVectorInfo = new DatasetVectorInfo();
                            if (null != comboboxSourceDataset.getSelectedDataset()) {
                                datasetVectorInfo.setType(comboboxSourceDataset.getSelectedDataset().getType());
                                datasetVectorInfo.setEncodeType(comboboxSourceDataset.getSelectedDataset().getEncodeType());
                            }
                            if (comboboxTargetDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(textFieldTargetDataset.getText()).equals(textFieldTargetDataset.getText())) {
                                // 名称合法时可以设置名称
                                datasetVectorInfo.setName(textFieldTargetDataset.getText());
                            }
                            DatasetVector targetDataset = comboboxTargetDatasource.getSelectedDatasource().getDatasets().create(datasetVectorInfo);
                            targetDataset.setPrjCoordSys(comboboxSourceDataset.getSelectedDataset().getPrjCoordSys());
                            overlayAnalystInfo.targetDataset = targetDataset;
                            overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                            isSelectingItem = false;
                        }
                    }
                }
            }
        });
        this.textFieldTolerance.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                double tolerance = (double) textFieldTolerance.getValue();
                if (!isSelectingItem) {
                    isSelectingItem = true;
                    if (null == overlayAnalystInfo.analystParameter) {
                        overlayAnalystInfo.analystParameter = new OverlayAnalystParameter();
                    }
                    overlayAnalystInfo.analystParameter.setTolerance(tolerance);
                    overlayAnalyst.setSelectedItem(overlayAnalystInfo);
                    isSelectingItem = false;
                }

            }
        });
        this.overlayAnalyst.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
                    isSelectingItem = true;
                    Object newValue = evt.getNewValue();
                    if (newValue instanceof ParameterOverlayAnalystInfo) {
                        ParameterOverlayAnalystInfo overlayAnalystInfo = (ParameterOverlayAnalystInfo) newValue;
                        comboboxSourceDatasource.setSelectedDatasource(overlayAnalystInfo.sourceDatatsource);
                        comboboxSourceDataset.setSelectedDataset(overlayAnalystInfo.sourceDataset);
                        labelToleranceUnity.setText(LengthUnit.convertForm(overlayAnalystInfo.sourceDataset.getPrjCoordSys().getCoordUnit()).toString());
                        comboboxOverlayAnalystDatasource.setSelectedDatasource(overlayAnalystInfo.overlayAnalystDatasource);
                        comboboxOverlayAnalystDataset.setSelectedDataset(overlayAnalystInfo.overlayAnalystDataset);
                        comboboxTargetDatasource.setSelectedDatasource(overlayAnalystInfo.targetDataset.getDatasource());
                        textFieldTargetDataset.setText(overlayAnalystInfo.targetDataset.getName());
                        textFieldTolerance.setValue(overlayAnalystInfo.analystParameter.getTolerance());
                    }
                    isSelectingItem = false;
                }
            }
        });
    }

    private void initResources() {
        this.labelSourceDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
        this.labelSourceDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
        this.labelOverlayAnalystDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
        this.labelOverlayAnalystDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
        this.labelTargetDatasource.setText(CommonProperties.getString(CommonProperties.Label_Datasource));
        this.labelTargetDataset.setText(CommonProperties.getString(CommonProperties.Label_Dataset));
        this.buttonFieldsSet.setText(CommonProperties.getString("String_FieldsSetting"));
        this.labelTolerance.setText(CommonProperties.getString("String_Label_Tolerance"));
        this.checkboxResultAnalyst.setText(CommonProperties.getString("String_CheckBox_ResultComparison"));
        this.panelSource.setBorder(new TitledBorder(ControlsProperties.getString("String_GroupBox_SourceDataset")));
        this.panelOverlayAnalyst.setBorder(new TitledBorder(CommonProperties.getString("String_GroupBox_OverlayDataset")));
        this.panelTarget.setBorder(new TitledBorder(CommonProperties.getString("String_ResultSet")));
    }

    private void initLayout() {
        initPanelSourceLayout();
        initPanelOverlayAnalystLayout();
        initPanelTargetLayout();
        this.setLayout(new GridBagLayout());
        this.add(this.panelSource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        this.add(this.panelOverlayAnalyst, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
        this.add(this.panelTarget, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(2).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
    }

    private void initPanelTargetLayout() {
        this.panelTarget.setLayout(new GridBagLayout());
        this.panelTarget.add(this.labelTargetDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.comboboxTargetDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.labelTargetDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.textFieldTargetDataset, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.buttonFieldsSet, new GridBagConstraintsHelper(3, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 1));
        this.panelTarget.add(this.labelTolerance, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.textFieldTolerance, new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelTarget.add(this.labelToleranceUnity, new GridBagConstraintsHelper(4, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelTarget.add(this.checkboxResultAnalyst, new GridBagConstraintsHelper(0, 3, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 1));
    }

    private void initPanelOverlayAnalystLayout() {
        this.panelOverlayAnalyst.setLayout(new GridBagLayout());
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelOverlayAnalyst.add(this.labelOverlayAnalystDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelOverlayAnalyst.add(this.comboboxOverlayAnalystDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
    }

    private void initPanelSourceLayout() {
        this.panelSource.setLayout(new GridBagLayout());
        this.panelSource.add(this.labelSourceDatasource, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSource.add(this.comboboxSourceDatasource, new GridBagConstraintsHelper(1, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
        this.panelSource.add(this.labelSourceDataset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.panelSource.add(this.comboboxSourceDataset, new GridBagConstraintsHelper(1, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));
    }
}
