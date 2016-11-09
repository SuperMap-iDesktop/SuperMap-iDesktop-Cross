package com.supermap.desktop.importUI;

import com.supermap.data.conversion.FileType;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/26.
 * 矢量数据集集合导入参数设置界面
 */
public class PanelTransformForVector extends PanelTransformForD {
    private TristateCheckBox checkBoxImportInvisible;//导入不可见对象
    private TristateCheckBox checkBoxAttributeIgnored;//忽略属性信息
    private final int IMPORTINVISIBLE = 9;
    private final int ATTRIBUTEIGNORED = 10;
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
        this.checkBoxImportInvisible = new TristateCheckBox();
        this.checkBoxAttributeIgnored = new TristateCheckBox();
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
        this.checkBoxMergeLayer.setEnabled(isCheckboxForMergeLayerEnabled());
        this.checkBoxImportInvisible.setEnabled(isImportInvisibleEnabled());
        this.checkBoxAttributeIgnored.setEnabled(isAttributeIgnored());
        setCheckboxState();
    }

    public boolean isCheckboxForMergeLayerEnabled() {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            FileType tempFiletype = tempPanelImport.getImportInfo().getImportSetting().getSourceFileType();
            if (tempFiletype.equals(FileType.DXF) || tempFiletype.equals(FileType.DWG) || tempFiletype.equals(FileType.DGN)) {
                count++;
            }
        }
        return count == panelImports.size();
    }

    private void setCheckboxState() {
        if (null != panelImports && isImportInvisibleEnabled()) {
            this.checkBoxImportInvisible.setSelectedEx(externalDataSelectAll(IMPORTINVISIBLE));
        } else if (null != panelImports && isAttributeIgnored()) {
            this.checkBoxAttributeIgnored.setSelectedEx(externalDataSelectAll(ATTRIBUTEIGNORED));
        } else if (null != panelImports && isCheckboxForMergeLayerEnabled()) {
            this.checkBoxMergeLayer.setSelectedEx(externalDataSelectAll());
        }
    }

    private Boolean externalDataSelectAll() {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelImport tempPanel : panelImports) {
            boolean select = false;
            if (tempPanel.getTransform() instanceof PanelTransformForD) {
                select = ((PanelTransformForD) tempPanel.getTransform()).getCheckBoxMergeLayer().isSelected();
            } else if (tempPanel.getTransform() instanceof PanelTransformForDGN) {
                select = ((PanelTransformForDGN) tempPanel.getTransform()).getCheckBoxImportByLayer().isSelected();
            }

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

    private Boolean externalDataSelectAll(int type) {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelImport tempPanel : panelImports) {
            boolean select = getCheckboxState(tempPanel, type);
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

    private boolean getCheckboxState(PanelImport panelImport, int type) {
        boolean result = false;
        if (type == IMPORTINVISIBLE) {
            result = ((PanelTransformForKML) panelImport.getTransform()).getCheckBoxImportInvisible().isSelected();
        } else if (type == ATTRIBUTEIGNORED) {
            result = ((PanelTransformForGRD) panelImport.getTransform()).getCheckBoxAttributeIgnored().isSelected();
        }
        return result;
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

    public TristateCheckBox getCheckBoxImportInvisible() {
        return checkBoxImportInvisible;
    }

    public TristateCheckBox getCheckBoxAttributeIgnored() {
        return checkBoxAttributeIgnored;
    }
}
