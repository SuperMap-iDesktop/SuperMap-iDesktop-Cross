package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingDWG;
import com.supermap.data.conversion.ImportSettingDXF;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.Convert;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/30.
 * dwg,dxf转化参数设置界面
 */
public class PanelTransformForD extends PanelTransform {

    private ArrayList<PanelImport> panelImports;
    private JLabel labelCurveSegment;
    private JTextField textFieldCurveSegment;
    private JButton buttonFontset;

    private JCheckBox checkBoxExtendsData;//导入扩展数据
    private JCheckBox checkBoxImportingXRecord;//导入扩展记录
    private JCheckBox checkBoxSaveHeight;//保留对象高度
    private JCheckBox checkBoxImportInvisibleLayer;//导入不可见图层
    private JCheckBox checkBoxSaveWPLineWidth;//保留多义线宽度
    private JCheckBox checkBoxMergeLayer;//合并图层
    private JCheckBox checkBoxImportProperty;//导入块属性
    private JCheckBox checkBoxKeepingParametricPart;//保留参数化对象
    private JCheckBox checkBoxImportSymbol;//导入符号块
    private DocumentListener curveSegmentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateCurveSegment();
        }

        private void updateCurveSegment() {

            if (!StringUtilities.isNullOrEmpty(textFieldCurveSegment.getText()) && StringUtilities.isPositiveInteger(textFieldCurveSegment.getText())) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        ((PanelTransformForD) tempPanelImport.getTransform()).getTextFieldCurveSegment().setText(textFieldCurveSegment.getText());
                    }
                } else {
                    if (importSetting instanceof ImportSettingDXF) {
                        ((ImportSettingDXF) importSetting).setCurveSegment(Convert.toInteger(textFieldCurveSegment.getText()));
                    }
                    if (importSetting instanceof ImportSettingDWG) {
                        ((ImportSettingDWG) importSetting).setCurveSegment(Convert.toInteger(textFieldCurveSegment.getText()));
                    }
                }
            }

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateCurveSegment();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateCurveSegment();
        }
    };
    private ItemListener extendsDataListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxExtendsData().setSelected(checkBoxExtendsData.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setImportingExternalData(checkBoxExtendsData.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setImportingExternalData(checkBoxExtendsData.isSelected());
                }
            }
        }
    };
    private ItemListener importingXRecordListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxImportingXRecord().setSelected(checkBoxImportingXRecord.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setImportingXRecord(checkBoxImportingXRecord.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setImportingXRecord(checkBoxImportingXRecord.isSelected());
                }
            }
        }
    };
    private ItemListener importInvisibleLayerListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxImportInvisibleLayer().setSelected(checkBoxImportInvisibleLayer.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setImportingInvisibleLayer(checkBoxImportInvisibleLayer.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setImportingInvisibleLayer(checkBoxImportInvisibleLayer.isSelected());
                }
            }
        }
    };
    private ItemListener importPropertyListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxImportProperty().setSelected(checkBoxImportProperty.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setBlockAttributeIgnored(checkBoxImportProperty.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setBlockAttributeIgnored(checkBoxImportProperty.isSelected());
                }
            }
        }
    };
    private ItemListener importSymbolListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxImportSymbol().setSelected(checkBoxImportSymbol.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setImportingBlockAsPoint(checkBoxImportSymbol.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setImportingBlockAsPoint(checkBoxImportSymbol.isSelected());
                }
            }
        }
    };
    private ItemListener keepParametricPartListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxKeepingParametricPart().setSelected(checkBoxKeepingParametricPart.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setKeepingParametricPart(checkBoxKeepingParametricPart.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setKeepingParametricPart(checkBoxKeepingParametricPart.isSelected());
                }
            }
        }
    };
    private ItemListener mergeLayerListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxMergeLayer().setSelected(checkBoxMergeLayer.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setImportingByLayer(checkBoxMergeLayer.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setImportingByLayer(checkBoxMergeLayer.isSelected());
                }
            }
        }
    };
    private ItemListener saveHeightListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxSaveHeight().setSelected(checkBoxSaveHeight.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setImporttingAs3D(checkBoxSaveHeight.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setImporttingAs3D(checkBoxSaveHeight.isSelected());
                }
            }
        }
    };
    private ItemListener saveWPLineWidthListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForD) tempPanelImport.getTransform()).getCheckBoxSaveWPLineWidth().setSelected(checkBoxSaveWPLineWidth.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingDXF) {
                    ((ImportSettingDXF) importSetting).setLWPLineWidthIgnored(checkBoxSaveWPLineWidth.isSelected());
                }
                if (importSetting instanceof ImportSettingDWG) {
                    ((ImportSettingDWG) importSetting).setLWPLineWidthIgnored(checkBoxSaveWPLineWidth.isSelected());
                }
            }
        }
    };

    public PanelTransformForD(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForD(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelCurveSegment = new JLabel();
        this.textFieldCurveSegment = new JTextField();
        initTextFieldCurveSegment();
        this.buttonFontset = new JButton();
        this.checkBoxExtendsData = new JCheckBox();
        this.checkBoxImportingXRecord = new JCheckBox();
        this.checkBoxSaveHeight = new JCheckBox();
        this.checkBoxImportInvisibleLayer = new JCheckBox();
        this.checkBoxSaveWPLineWidth = new JCheckBox();
        this.checkBoxMergeLayer = new JCheckBox();
        this.checkBoxImportProperty = new JCheckBox();
        this.checkBoxKeepingParametricPart = new JCheckBox();
        this.checkBoxImportSymbol = new JCheckBox();
        initImportSetting();
        initCheckbox();
    }

    @Override
    public void initImportSetting() {
        if (importSetting instanceof ImportSettingDXF) {
            ((ImportSettingDXF) importSetting).setBlockAttributeIgnored(false);
            ((ImportSettingDXF) importSetting).setImportingBlockAsPoint(true);
            ((ImportSettingDXF) importSetting).setImportingByLayer(true);
            ((ImportSettingDXF) importSetting).setLWPLineWidthIgnored(true);
        }
        if (importSetting instanceof ImportSettingDWG) {
            ((ImportSettingDWG) importSetting).setBlockAttributeIgnored(false);
            ((ImportSettingDWG) importSetting).setImportingBlockAsPoint(true);
            ((ImportSettingDWG) importSetting).setImportingByLayer(true);
            ((ImportSettingDWG) importSetting).setLWPLineWidthIgnored(true);
        }
    }

    private void initCheckbox() {
        if (importSetting instanceof ImportSettingDXF) {
            this.checkBoxExtendsData.setSelected(((ImportSettingDXF) importSetting).isImportingExternalData());
            this.checkBoxImportingXRecord.setSelected(((ImportSettingDXF) importSetting).isImportingXRecord());
            this.checkBoxImportInvisibleLayer.setSelected(((ImportSettingDXF) importSetting).isImportingInvisibleLayer());
            this.checkBoxImportProperty.setSelected(((ImportSettingDXF) importSetting).isBlockAttributeIgnored());
            this.checkBoxImportSymbol.setSelected(((ImportSettingDXF) importSetting).isImportingBlockAsPoint());
            this.checkBoxKeepingParametricPart.setSelected(((ImportSettingDXF) importSetting).isKeepingParametricPart());
            this.checkBoxMergeLayer.setSelected(((ImportSettingDXF) importSetting).isImportingByLayer());
            this.checkBoxSaveHeight.setSelected(((ImportSettingDXF) importSetting).isImporttingAs3D());
            this.checkBoxSaveWPLineWidth.setSelected(((ImportSettingDXF) importSetting).isLWPLineWidthIgnored());
        }
        if (importSetting instanceof ImportSettingDWG) {
            this.checkBoxExtendsData.setSelected(((ImportSettingDWG) importSetting).isImportingExternalData());
            this.checkBoxImportingXRecord.setSelected(((ImportSettingDWG) importSetting).isImportingXRecord());
            this.checkBoxImportInvisibleLayer.setSelected(((ImportSettingDWG) importSetting).isImportingInvisibleLayer());
            this.checkBoxImportProperty.setSelected(((ImportSettingDWG) importSetting).isBlockAttributeIgnored());
            this.checkBoxImportSymbol.setSelected(((ImportSettingDWG) importSetting).isImportingBlockAsPoint());
            this.checkBoxKeepingParametricPart.setSelected(((ImportSettingDWG) importSetting).isKeepingParametricPart());
            this.checkBoxMergeLayer.setSelected(((ImportSettingDWG) importSetting).isImportingByLayer());
            this.checkBoxSaveHeight.setSelected(((ImportSettingDWG) importSetting).isImporttingAs3D());
            this.checkBoxSaveWPLineWidth.setSelected(((ImportSettingDWG) importSetting).isLWPLineWidthIgnored());
        }
    }

    private void initTextFieldCurveSegment() {
        if (importSetting instanceof ImportSettingDXF && ((ImportSettingDXF) importSetting).getCurveSegment() >= 0) {
            this.textFieldCurveSegment.setText(String.valueOf(((ImportSettingDXF) importSetting).getCurveSegment()));
        }
        if (importSetting instanceof ImportSettingDWG && ((ImportSettingDWG) importSetting).getCurveSegment() >= 0) {
            this.textFieldCurveSegment.setText(String.valueOf(((ImportSettingDWG) importSetting).getCurveSegment()));
        }
    }

    @Override
    public void initLayerout() {
        JPanel panelCheckBox = new JPanel();
        this.setLayout(new GridBagLayout());
        this.add(this.labelCurveSegment, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textFieldCurveSegment, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 90).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(new JPanel(), new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

        this.add(panelCheckBox, new GridBagConstraintsHelper(0, 1, 8, 1).setAnchor(GridBagConstraints.WEST).setWeight(1, 1).setInsets(0).setFill(GridBagConstraints.BOTH));

        panelCheckBox.setLayout(new GridBagLayout());
        panelCheckBox.add(this.checkBoxMergeLayer, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 5, 5, 20));
        panelCheckBox.add(this.checkBoxImportInvisibleLayer, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
        panelCheckBox.add(this.checkBoxSaveHeight, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 10));
        panelCheckBox.add(this.checkBoxImportSymbol, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 5, 5, 20));
        panelCheckBox.add(this.checkBoxImportProperty, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
        panelCheckBox.add(this.checkBoxKeepingParametricPart, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 10));
        panelCheckBox.add(this.checkBoxExtendsData, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 5, 5, 20));
        panelCheckBox.add(this.checkBoxImportingXRecord, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
        panelCheckBox.add(this.checkBoxSaveWPLineWidth, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 10));
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.textFieldCurveSegment.getDocument().addDocumentListener(this.curveSegmentListener);
        this.checkBoxExtendsData.addItemListener(extendsDataListener);
        this.checkBoxImportingXRecord.addItemListener(importingXRecordListener);
        this.checkBoxImportInvisibleLayer.addItemListener(this.importInvisibleLayerListener);
        this.checkBoxImportProperty.addItemListener(this.importPropertyListener);
        this.checkBoxImportSymbol.addItemListener(this.importSymbolListener);
        this.checkBoxKeepingParametricPart.addItemListener(this.keepParametricPartListener);
        this.checkBoxMergeLayer.addItemListener(this.mergeLayerListener);
        this.checkBoxSaveHeight.addItemListener(this.saveHeightListener);
        this.checkBoxSaveWPLineWidth.addItemListener(this.saveWPLineWidthListener);
    }

    @Override
    public void removeEvents() {
        this.textFieldCurveSegment.getDocument().removeDocumentListener(this.curveSegmentListener);
        this.checkBoxExtendsData.removeItemListener(extendsDataListener);
        this.checkBoxImportingXRecord.removeItemListener(importingXRecordListener);
        this.checkBoxImportInvisibleLayer.removeItemListener(this.importInvisibleLayerListener);
        this.checkBoxImportProperty.removeItemListener(this.importPropertyListener);
        this.checkBoxImportSymbol.removeItemListener(this.importSymbolListener);
        this.checkBoxKeepingParametricPart.removeItemListener(this.keepParametricPartListener);
        this.checkBoxMergeLayer.removeItemListener(this.mergeLayerListener);
        this.checkBoxSaveHeight.removeItemListener(this.saveHeightListener);
        this.checkBoxSaveWPLineWidth.removeItemListener(this.saveWPLineWidthListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelCurveSegment.setText(DataConversionProperties.getString("string_label_lblCurve"));
        this.checkBoxExtendsData.setText(DataConversionProperties.getString("string_checkbox_chckbxExtendsData"));//导入扩展数据
        this.checkBoxImportingXRecord.setText(DataConversionProperties.getString("string_checkbox_chckbxExtendsData"));//导入扩展记录
        this.checkBoxSaveHeight.setText(DataConversionProperties.getString("string_checkbox_chckbxHeight"));//保留对象高度
        this.checkBoxImportInvisibleLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxImportLayer"));//导入不可见图层
        this.checkBoxSaveWPLineWidth.setText(DataConversionProperties.getString("string_checkbox_chckbxLineWidth"));//保留多义线宽度
        this.checkBoxMergeLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxMergeLayer"));//合并图层
        this.checkBoxImportProperty.setText(DataConversionProperties.getString("string_checkbox_chckbxProperty"));//导入块属性
        this.checkBoxKeepingParametricPart.setText(DataConversionProperties.getString("string_checkbox_chckbxSaveField"));//保留参数化对象
        this.checkBoxImportSymbol.setText(DataConversionProperties.getString("string_checkbox_chckbxSymbol"));//导入符号块
    }

    public JTextField getTextFieldCurveSegment() {
        return textFieldCurveSegment;
    }

    public JCheckBox getCheckBoxExtendsData() {
        return checkBoxExtendsData;
    }

    public JCheckBox getCheckBoxImportingXRecord() {
        return checkBoxImportingXRecord;
    }

    public JCheckBox getCheckBoxSaveHeight() {
        return checkBoxSaveHeight;
    }

    public JCheckBox getCheckBoxImportInvisibleLayer() {
        return checkBoxImportInvisibleLayer;
    }

    public JCheckBox getCheckBoxSaveWPLineWidth() {
        return checkBoxSaveWPLineWidth;
    }

    public JCheckBox getCheckBoxMergeLayer() {
        return checkBoxMergeLayer;
    }

    public JCheckBox getCheckBoxImportProperty() {
        return checkBoxImportProperty;
    }

    public JCheckBox getCheckBoxKeepingParametricPart() {
        return checkBoxKeepingParametricPart;
    }

    public JCheckBox getCheckBoxImportSymbol() {
        return checkBoxImportSymbol;
    }
}
