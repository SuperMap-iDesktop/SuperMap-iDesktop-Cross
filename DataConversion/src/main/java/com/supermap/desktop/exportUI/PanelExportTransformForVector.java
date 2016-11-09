package com.supermap.desktop.exportUI;

import com.supermap.data.Dataset;
import com.supermap.data.conversion.CADVersion;
import com.supermap.data.conversion.ExportSetting;
import com.supermap.data.conversion.ExportSettingDWG;
import com.supermap.data.conversion.ExportSettingDXF;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ExportFileInfo;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.utilities.CharsetUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/27.
 * 导出矢量数据集矢量参数设置
 */
public class PanelExportTransformForVector extends PanelExportTransform {
    private TristateCheckBox checkBoxExportExternalData;
    private TristateCheckBox checkBoxExportExternalRecord;
    //    private JCheckBox checkBoxExportPointAsWKT;
    private JLabel labelDatasets;
    private CharsetComboBox charsetComboBox;
    private JLabel labelCADVersion;
    private JComboBox<String> comboBoxCADVersion;
    private JLabel labelExpression;
    private JTextArea textAreaExpression;
    private JButton buttonExpression;
    private StateChangeListener externalDataListener = new StateChangeListener() {

        @Override
        public void stateChange(StateChangeEvent e) {
            if (null != panels) {
                for (PanelExportTransform tempPanel : panels) {
                    ((PanelExportTransformForVector) tempPanel).getCheckBoxExportExternalData().setSelected(checkBoxExportExternalData.isSelected());
                }
            } else {
                ExportSetting exportSetting = exportsFileInfo.getExportSetting();
                if (exportSetting instanceof ExportSettingDWG) {
                    ((ExportSettingDWG) exportSetting).setExportingExternalData(checkBoxExportExternalData.isSelected());
                } else if (exportSetting instanceof ExportSettingDXF) {
                    ((ExportSettingDXF) exportSetting).setExportingExternalData(checkBoxExportExternalData.isSelected());
                }
            }
        }
    };
    private StateChangeListener externalRecordListener = new StateChangeListener() {

        @Override
        public void stateChange(StateChangeEvent e) {
            if (null != panels) {
                for (PanelExportTransform tempPanel : panels) {
                    ((PanelExportTransformForVector) tempPanel).getCheckBoxExportExternalRecord().setSelected(checkBoxExportExternalRecord.isSelected());
                }
            } else {
                ExportSetting exportSetting = exportsFileInfo.getExportSetting();
                if (exportSetting instanceof ExportSettingDWG) {
                    ((ExportSettingDWG) exportSetting).setExportingXRecord(checkBoxExportExternalRecord.isSelected());
                } else if (exportSetting instanceof ExportSettingDXF) {
                    ((ExportSettingDXF) exportSetting).setExportingXRecord(checkBoxExportExternalRecord.isSelected());
                }
            }
        }
    };
    private ItemListener charsetListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panels) {
                    for (PanelExportTransform tempPanel : panels) {
                        if (tempPanel instanceof PanelExportTransformForVector) {
                            ((PanelExportTransformForVector) tempPanel).getCharsetComboBox().setSelectedItem(charsetComboBox.getSelectedItem());
                        }
                    }
                } else {
                    exportsFileInfo.getExportSetting().setTargetFileCharset(CharsetUtilities.valueOf(charsetComboBox.getSelectedItem().toString()));
                }
            }
        }
    };
    private ItemListener cadVersionListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (null != panels) {
                    for (PanelExportTransform tempPanel : panels) {
                        ((PanelExportTransformForVector) tempPanel).getComboBoxCADVersion().setSelectedItem(comboBoxCADVersion.getSelectedItem());
                    }
                } else {
                    String cadVersion = comboBoxCADVersion.getSelectedItem().toString();
                    ExportSetting exportSetting = exportsFileInfo.getExportSetting();
                    if (exportSetting instanceof ExportSettingDWG) {
                        ((ExportSettingDWG) exportSetting).setVersion(getCADVersion(cadVersion));
                    } else if (exportSetting instanceof ExportSettingDXF) {
                        ((ExportSettingDXF) exportSetting).setVersion(getCADVersion(cadVersion));
                    }
                }
            }
        }

        private CADVersion getCADVersion(String item) {
            CADVersion version = null;
            if ("CAD2007".equalsIgnoreCase(item)) {
                version = CADVersion.CAD2007;
            }
            if ("CAD2004".equalsIgnoreCase(item)) {
                version = CADVersion.CAD2004;
            }
            if ("CAD2000".equalsIgnoreCase(item)) {
                version = CADVersion.CAD2000;
            }
            if ("CAD12".equalsIgnoreCase(item)) {
                version = CADVersion.CAD12;
            }
            if ("CAD14".equalsIgnoreCase(item)) {
                version = CADVersion.CAD14;
            }
            if ("CAD13".equalsIgnoreCase(item)) {
                version = CADVersion.CAD13;
            }
            return version;
        }
    };
    private DocumentListener expressionListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setExpression();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setExpression();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setExpression();
        }

        private void setExpression() {
            String expression = textAreaExpression.getText();
            if (!StringUtilities.isNullOrEmpty(expression)) {
                exportsFileInfo.getExportSetting().setFilter(expression);
            }
        }
    };
    private ActionListener buttonExpressionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SQLExpressionDialog dialog = new SQLExpressionDialog();
            DialogResult result = dialog.showDialog(textAreaExpression.getText(), (Dataset) exportsFileInfo.getExportSetting().getSourceData());
            if (result == DialogResult.OK) {
                String filter = dialog.getQueryParameter().getAttributeFilter();
                if (!StringUtilities.isNullOrEmpty(filter)) {
                    textAreaExpression.setText(filter);
                }
            }
        }
    };
    private ActionListener exportPointAsWKTListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    public PanelExportTransformForVector(ExportFileInfo exportsFileInfo) {
        super(exportsFileInfo);
        registEvents();
    }

    public PanelExportTransformForVector(ArrayList<PanelExportTransform> panelExports, int layoutType) {
        super(panelExports, layoutType);
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxExportExternalData = new TristateCheckBox();
        this.checkBoxExportExternalRecord = new TristateCheckBox();
//        this.checkBoxExportPointAsWKT = new JCheckBox();
        this.labelDatasets = new JLabel();
        this.charsetComboBox = new CharsetComboBox();
        this.labelCADVersion = new JLabel();
        this.comboBoxCADVersion = new JComboBox<>();
        this.labelExpression = new JLabel();
        this.textAreaExpression = new JTextArea();
        this.textAreaExpression.setBorder(new LineBorder(Color.gray));
        this.textAreaExpression.setLineWrap(true);
        this.textAreaExpression.setColumns(5);
        this.buttonExpression = new JButton();
        initComboboxCADVersion();
        setUnEnabled();
        if (null != exportsFileInfo && null != exportsFileInfo.getExportSetting()) {
            initComponentsState(exportsFileInfo.getExportSetting());
            this.charsetComboBox.setSelectCharset(exportsFileInfo.getExportSetting().getTargetFileCharset().name());
        } else if (null != panels) {
            initComponentsState(panels, layoutType);
        }
    }

    private void initComponentsState(ArrayList<PanelExportTransform> panels, int layoutType) {
        this.charsetComboBox.setEnabled(true);
        this.charsetComboBox.setSelectedItem(selectSameCharsetItem(panels));
        if (isDtype(panels)) {
            //相同类型
            this.checkBoxExportExternalData.setEnabled(true);
            this.checkBoxExportExternalRecord.setEnabled(true);
            this.comboBoxCADVersion.setEnabled(true);
            this.checkBoxExportExternalData.setSelectedEx(externalDataSelectAll(panels));
            this.checkBoxExportExternalRecord.setSelectedEx(externalRecordSelectAll(panels));
            this.comboBoxCADVersion.setSelectedItem(selectSameItem(panels));
        }
    }

    private Object selectSameCharsetItem(ArrayList<PanelExportTransform> panels) {
        Object result = "";
        String temp = "";
        if (panels.get(0) instanceof PanelExportTransformForVector) {
            temp = ((PanelExportTransformForVector) panels.get(0)).getCharsetComboBox().getSelectedItem().toString();
        }
        boolean isSame = true;
        for (PanelExportTransform tempPanel : panels) {
            if (tempPanel instanceof PanelExportTransformForVector) {
                String tempObject = ((PanelExportTransformForVector) tempPanel).getCharsetComboBox().getSelectedItem().toString();
                if (!temp.equals(tempObject)) {
                    isSame = false;
                    break;
                }
            }
        }
        if (isSame) {
            result = temp;
        }
        return result;
    }

    private Object selectSameItem(ArrayList<PanelExportTransform> panels) {
        Object result = "";
        String temp = "";
        if (panels.get(0) instanceof PanelExportTransformForVector) {
            temp = ((PanelExportTransformForVector) panels.get(0)).getComboBoxCADVersion().getSelectedItem().toString();
        }
        boolean isSame = true;
        for (PanelExportTransform tempPanel : panels) {
            if (tempPanel instanceof PanelExportTransformForVector) {
                String tempObject = ((PanelExportTransformForVector) tempPanel).getComboBoxCADVersion().getSelectedItem().toString();
                if (!temp.equals(tempObject)) {
                    isSame = false;
                    break;
                }
            }
        }
        if (isSame) {
            result = temp;
        }
        return result;
    }

    private Boolean externalRecordSelectAll(ArrayList<PanelExportTransform> panels) {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelExportTransform tempPanel : panels) {
            if (((PanelExportTransformForVector) tempPanel).getCheckBoxExportExternalRecord().isSelected()) {
                selectCount++;
            } else if (!((PanelExportTransformForVector) tempPanel).getCheckBoxExportExternalRecord().isSelected()) {
                unSelectCount++;
            }
        }
        if (selectCount == panels.size()) {
            result = true;
        } else if (unSelectCount == panels.size()) {
            result = false;
        }
        return result;
    }

    private Boolean externalDataSelectAll(ArrayList<PanelExportTransform> panels) {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelExportTransform tempPanel : panels) {
            if (tempPanel instanceof PanelExportTransformForVector) {
                if (((PanelExportTransformForVector) tempPanel).getCheckBoxExportExternalData().isSelected()) {
                    selectCount++;
                } else if (!((PanelExportTransformForVector) tempPanel).getCheckBoxExportExternalData().isSelected()) {
                    unSelectCount++;
                }
            }
        }
        if (selectCount == panels.size()) {
            result = true;
        } else if (unSelectCount == panels.size()) {
            result = false;
        }
        return result;
    }

    private boolean isDtype(ArrayList<PanelExportTransform> panels) {
        int count = 0;
        for (PanelExportTransform tempPanel : panels) {
            if (tempPanel.getExportsFileInfo().getExportSetting() instanceof ExportSettingDXF || tempPanel.getExportsFileInfo().getExportSetting() instanceof ExportSettingDWG)
                count++;
        }
        return count == panels.size();
    }


    private void initComponentsState(ExportSetting tempExportSetting) {
        if (tempExportSetting instanceof ExportSettingDWG || tempExportSetting instanceof ExportSettingDXF) {
            this.checkBoxExportExternalData.setEnabled(true);
            this.checkBoxExportExternalRecord.setEnabled(true);
            this.comboBoxCADVersion.setEnabled(true);
        }
        this.charsetComboBox.setEnabled(true);
        this.textAreaExpression.setEnabled(true);
        this.buttonExpression.setEnabled(true);
    }

    private void initComboboxCADVersion() {
        this.comboBoxCADVersion.setModel(new DefaultComboBoxModel<>(new String[]{CADVersion.CAD12.toString(), CADVersion.CAD13.toString(),
                CADVersion.CAD14.toString(), CADVersion.CAD2000.toString(), CADVersion.CAD2004.toString(), CADVersion.CAD2007.toString()}));
        this.charsetComboBox.setEditable(true);
        ((JTextField) this.charsetComboBox.getEditor().getEditorComponent()).setEditable(false);
        this.comboBoxCADVersion.setEditable(true);
        ((JTextField) this.comboBoxCADVersion.getEditor().getEditorComponent()).setEditable(false);
    }

    @Override
    public void initLayerout() {
        JPanel panelContent = new JPanel();
        panelContent.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());
        this.add(panelContent, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1));

        panelContent.add(this.checkBoxExportExternalData, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(10, 0, 0, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.checkBoxExportExternalRecord, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
//        panelContent.add(this.checkBoxExportPointAsWKT, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.labelDatasets, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.charsetComboBox, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelCADVersion, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.comboBoxCADVersion, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelExpression, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.textAreaExpression, new GridBagConstraintsHelper(0, 5, 2, 3).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.BOTH).setWeight(1, 0).setIpad(0, 80));
        panelContent.add(this.buttonExpression, new GridBagConstraintsHelper(1, 8, 1, 1).setAnchor(GridBagConstraints.EAST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.NONE).setWeight(0, 0));
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelproperty")));
        this.checkBoxExportExternalData.setText(DataConversionProperties.getString("string_chcekbox_extends"));
        this.checkBoxExportExternalRecord.setText(DataConversionProperties.getString("String_ExportExternalRecord"));
//        this.checkBoxExportPointAsWKT.setText(DataConversionProperties.getString("String_ExportPointAsWKT"));
        this.labelDatasets.setText(ControlsProperties.getString("String_LabelCharset"));
        this.labelCADVersion.setText(DataConversionProperties.getString("string_label_lblCAD"));
        this.labelExpression.setText(DataConversionProperties.getString("String_FilterExpression"));
        this.buttonExpression.setText(ControlsProperties.getString("String_SQLExpression") + "...");
    }

    public void setUnEnabled() {
        this.checkBoxExportExternalData.setEnabled(false);
        this.checkBoxExportExternalRecord.setEnabled(false);
//        this.checkBoxExportPointAsWKT.setEnabled(false);
        this.charsetComboBox.setEnabled(false);
        this.comboBoxCADVersion.setEnabled(false);
        this.textAreaExpression.setEnabled(false);
        this.buttonExpression.setEnabled(false);
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.checkBoxExportExternalData.addStateChangeListener(this.externalDataListener);
        this.checkBoxExportExternalRecord.addStateChangeListener(this.externalRecordListener);
//        this.checkBoxExportPointAsWKT.addActionListener(this.exportPointAsWKTListener);
        this.charsetComboBox.addItemListener(this.charsetListener);
        this.comboBoxCADVersion.addItemListener(this.cadVersionListener);
        this.textAreaExpression.getDocument().addDocumentListener(this.expressionListener);
        this.buttonExpression.addActionListener(this.buttonExpressionListener);
    }

    @Override
    public void removeEvents() {

    }

    public TristateCheckBox getCheckBoxExportExternalData() {
        return checkBoxExportExternalData;
    }

    public TristateCheckBox getCheckBoxExportExternalRecord() {
        return checkBoxExportExternalRecord;
    }

    public CharsetComboBox getCharsetComboBox() {
        return charsetComboBox;
    }

    public JComboBox<String> getComboBoxCADVersion() {
        return comboBoxCADVersion;
    }
}
