package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingDGN;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/14.
 * dgn类型参数转换界面
 */
public class PanelTransformForDGN extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
    private TristateCheckBox checkBoxImportCellAsPoint;
    private TristateCheckBox checkBoxImportByLayer;
    private final int CELLASPOINT = 0;
    private final int IMPORTBYLAYER = 1;
    private ItemListener importCellAsPointListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForDGN) tempPanelImport.getTransform()).getCheckBoxImportCellAsPoint().setSelected(checkBoxImportCellAsPoint.isSelected());
                }
            } else {
                ((ImportSettingDGN) importSetting).setImportingCellAsPoint(checkBoxImportCellAsPoint.isSelected());
            }
        }
    };
    private ItemListener importByLayerListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForDGN) tempPanelImport.getTransform()).getCheckBoxImportByLayer().setSelected(!checkBoxImportByLayer.isSelected());
                }
            } else {
                ((ImportSettingDGN) importSetting).setImportingByLayer(!checkBoxImportByLayer.isSelected());
            }
        }
    };

    public PanelTransformForDGN(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForDGN(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        initLayerout();
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxImportCellAsPoint = new TristateCheckBox();
        this.checkBoxImportByLayer = new TristateCheckBox();
    }

    @Override
    public void initLayerout() {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        this.add(this.checkBoxImportCellAsPoint, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(1, 0));
        this.add(this.checkBoxImportByLayer, new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 20, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0).setIpad(82, 0));
        setCheckboxState();
    }

    private void setCheckboxState() {
        if (null == panelImports) {
            this.checkBoxImportCellAsPoint.setSelected(((ImportSettingDGN) importSetting).isImportingCellAsPoint());
            this.checkBoxImportByLayer.setSelected(!((ImportSettingDGN) importSetting).isImportingByLayer());
        } else {
            this.checkBoxImportCellAsPoint.setSelectedEx(externalDataSelectAll(CELLASPOINT));
            this.checkBoxImportByLayer.setSelectedEx(!externalDataSelectAll(IMPORTBYLAYER));
        }
    }

    private Boolean externalDataSelectAll(int type) {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelImport tempPanel : panelImports) {
            boolean select = getCheckbox(tempPanel, type).isSelected();
            if (select) {
                selectCount++;
            } else if (!select) {
                unSelectCount++;
            }
        }
        if (selectCount == panelImports.size()) {
            result = true;
        } else if (unSelectCount == panelImports.size()) {
            result = false;
        }
        return result;
    }

    private JCheckBox getCheckbox(PanelImport panelImport, int type) {
        JCheckBox result = null;
        if (type == CELLASPOINT) {
            result = ((PanelTransformForDGN) panelImport.getTransform()).getCheckBoxImportCellAsPoint();
        } else if (type == IMPORTBYLAYER) {
            result = ((PanelTransformForDGN) panelImport.getTransform()).getCheckBoxImportByLayer();
        }
        return result;
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.checkBoxImportCellAsPoint.addItemListener(this.importCellAsPointListener);
        this.checkBoxImportByLayer.addItemListener(this.importByLayerListener);
    }

    @Override
    public void removeEvents() {
        this.checkBoxImportCellAsPoint.removeItemListener(this.importCellAsPointListener);
        this.checkBoxImportByLayer.removeItemListener(this.importByLayerListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.checkBoxImportCellAsPoint.setText(DataConversionProperties.getString("String_importCellAsPoint"));
        this.checkBoxImportByLayer.setText(DataConversionProperties.getString("string_checkbox_chckbxMergeLayer"));
    }

    public JCheckBox getCheckBoxImportCellAsPoint() {
        return checkBoxImportCellAsPoint;
    }

    public JCheckBox getCheckBoxImportByLayer() {
        return checkBoxImportByLayer;
    }
}
