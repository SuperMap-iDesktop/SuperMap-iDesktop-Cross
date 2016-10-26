package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingDGN;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
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
    private JCheckBox checkBoxImportCellAsPoint;
    private JCheckBox checkBoxImportByLayer;
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
                    ((PanelTransformForDGN) tempPanelImport.getTransform()).getCheckBoxImportByLayer().setSelected(checkBoxImportByLayer.isSelected());
                }
            } else {
                ((ImportSettingDGN) importSetting).setImportingByLayer(checkBoxImportByLayer.isSelected());
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
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxImportCellAsPoint = new JCheckBox();
        this.checkBoxImportByLayer = new JCheckBox();
        this.checkBoxImportCellAsPoint.setSelected(((ImportSettingDGN) importSetting).isImportingCellAsPoint());
        this.checkBoxImportByLayer.setSelected(((ImportSettingDGN) importSetting).isImportingByLayer());
    }

    @Override
    public void initLayerout() {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        this.add(this.checkBoxImportCellAsPoint, new GridBagConstraintsHelper(0, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(1, 0));
        this.add(this.checkBoxImportByLayer, new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(1, 0).setIpad(82, 0));
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
