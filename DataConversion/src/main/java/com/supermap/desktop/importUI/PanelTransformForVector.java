package com.supermap.desktop.importUI;

import com.supermap.data.conversion.FileType;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/26.
 * 矢量数据集集合导入参数设置界面
 */
public class PanelTransformForVector extends PanelTransformForD {
    private JCheckBox checkBoxImportInvisible;//导入不可见对象
    private JCheckBox checkBoxAttributeIgnored;//忽略属性信息
    private ItemListener attributeIgnoredListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            for (PanelImport tempPanelImport : panelImports) {
                ((PanelTransformForGRD) tempPanelImport.getTransform()).getCheckBoxAttributeIgnored().setSelected(checkBoxAttributeIgnored.isSelected());
            }
        }
    };
    private ItemListener importInvisibleListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            for (PanelImport tempPanelImport : panelImports) {
                ((PanelTransformForKML) tempPanelImport.getTransform()).getCheckBoxImportInvisible().setSelected(checkBoxImportInvisible.isSelected());
            }
        }
    };

    public PanelTransformForVector(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        registEvents();
    }

    @Override
    public void initComponents() {
        super.initComponents();
        this.checkBoxImportInvisible = new JCheckBox();
        this.checkBoxAttributeIgnored = new JCheckBox();
    }

    @Override
    public void registEvents() {
        super.registEvents();
        this.checkBoxAttributeIgnored.addItemListener(this.attributeIgnoredListener);
        this.checkBoxImportInvisible.addItemListener(this.importInvisibleListener);
    }

    @Override
    public void initLayerout() {
        super.initLayerout();
        panelCheckBox.add(this.checkBoxImportInvisible, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 5, 5, 20));
        panelCheckBox.add(this.checkBoxAttributeIgnored, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setWeight(30, 1).setInsets(0, 0, 5, 20));
        setCheckboxForDEnabled();
        this.checkBoxImportInvisible.setEnabled(isImportInvisibleEnabled());
        this.checkBoxAttributeIgnored.setEnabled(isAttributeIgnored());
    }

    private void setCheckboxForDEnabled() {
        boolean isCheckboxForDEnabled = isCheckboxForDEnabled();
        this.labelCurveSegment.setEnabled(isCheckboxForDEnabled);
        this.textFieldCurveSegment.setEnabled(isCheckboxForDEnabled);
        this.checkBoxExtendsData.setEnabled(isCheckboxForDEnabled);
        this.checkBoxImportingXRecord.setEnabled(isCheckboxForDEnabled);
        this.checkBoxSaveHeight.setEnabled(isCheckboxForDEnabled);
        this.checkBoxImportInvisibleLayer.setEnabled(isCheckboxForDEnabled);
        this.checkBoxSaveWPLineWidth.setEnabled(isCheckboxForDEnabled);
        this.checkBoxMergeLayer.setEnabled(isCheckboxForDEnabled);
        this.checkBoxImportProperty.setEnabled(isCheckboxForDEnabled);
        this.checkBoxKeepingParametricPart.setEnabled(isCheckboxForDEnabled);
        this.checkBoxImportSymbol.setEnabled(isCheckboxForDEnabled);
    }

    @Override
    public void initResources() {
        super.initResources();
        this.checkBoxImportInvisible.setText(DataConversionProperties.getString("string_checkbox_chckbxImport"));
        this.checkBoxAttributeIgnored.setText(DataConversionProperties.getString("string_checkbox_chckIngoreProperty"));
    }

    public boolean isCheckboxForDEnabled() {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            FileType tempFiletype = tempPanelImport.getImportInfo().getImportSetting().getSourceFileType();
            if (tempFiletype.equals(FileType.DXF) || tempFiletype.equals(FileType.DWG)) {
                count++;
            }
        }
        return count == panelImports.size();
    }

    public boolean isImportInvisibleEnabled() {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            FileType tempFiletype = tempPanelImport.getImportInfo().getImportSetting().getSourceFileType();
            if (tempFiletype.equals(FileType.KML) || tempFiletype.equals(FileType.KMZ)) {
                count++;
            }
        }
        return count == panelImports.size();
    }

    public boolean isAttributeIgnored() {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            FileType tempFiletype = tempPanelImport.getImportInfo().getImportSetting().getSourceFileType();
            if (tempFiletype.equals(FileType.SHP) || tempFiletype.equals(FileType.E00) || tempFiletype.equals(FileType.LIDAR)
                    || tempFiletype.equals(FileType.TAB) || tempFiletype.equals(FileType.MIF)) {
                count++;
            }
        }
        return count == panelImports.size();
    }
}
