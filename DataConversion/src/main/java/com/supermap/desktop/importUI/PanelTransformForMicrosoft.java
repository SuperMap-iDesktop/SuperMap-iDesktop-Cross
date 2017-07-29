package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingCSV;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
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
 * Created by xie on 2016/10/11.
 * 导入csv,xlsx
 */
public class PanelTransformForMicrosoft extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
    private JLabel labelSeparator;
    private JTextField textFieldSeparator;
    private TristateCheckBox checkBoxFirstRowAsField;
    private JLabel labelEmpty;
    private DocumentListener separatorListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateSeparator();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateSeparator();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateSeparator();
        }

        private void updateSeparator() {
            if (!StringUtilities.isNullOrEmpty(textFieldSeparator.getText())) {
                if (null != panelImports) {
                    for (PanelImport tempPanelImport : panelImports) {
                        ((PanelTransformForMicrosoft) tempPanelImport.getTransform()).getTextFieldSeparator().setText(textFieldSeparator.getText());
                    }
                } else {
                    ((ImportSettingCSV) importSetting).setSeparator(textFieldSeparator.getText());
                }
            }
        }
    };
    private ItemListener itemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForMicrosoft) tempPanelImport.getTransform()).getCheckBoxFirstRowAsField().setSelected(checkBoxFirstRowAsField.isSelected());
                }
            } else {
	            ((ImportSettingCSV) importSetting).setFirstRowIsField(!checkBoxFirstRowAsField.isSelected());
            }
        }
    };

    public PanelTransformForMicrosoft(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForMicrosoft(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        initLayerout();
        registEvents();
    }

    @Override
    public void initComponents() {
        this.checkBoxFirstRowAsField = new TristateCheckBox();
        this.checkBoxFirstRowAsField.setSelected(false);
        this.labelSeparator = new JLabel();
        this.textFieldSeparator = new JTextField();
        this.labelEmpty = new JLabel();
    }

    @Override
    public void setComponentName() {
        super.setComponentName();
        ComponentUIUtilities.setName(this.labelSeparator, "PanelTransformForMicrosoft_labelSeparator");
        ComponentUIUtilities.setName(this.textFieldSeparator, "PanelTransformForMicrosoft_textFieldSeparator");
        ComponentUIUtilities.setName(this.checkBoxFirstRowAsField, "PanelTransformForMicrosoft_checkBoxFirstRowAsField");
        ComponentUIUtilities.setName(this.labelEmpty, "PanelTransformForMicrosoft_labelEmpty");
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelSeparator, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 45).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textFieldSeparator, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setIpad(30, 0));
        this.add(this.checkBoxFirstRowAsField, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.labelEmpty, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.textFieldSeparator.setPreferredSize(new Dimension(18, 23));
        setFirstRowAsField();
        setSeparator();
    }

    private void setSeparator() {
        if (null != panelImports) {
            this.textFieldSeparator.setText(getSameSeparator());
        }
    }

    private void setFirstRowAsField() {
        if (null != panelImports) {
            this.checkBoxFirstRowAsField.setSelectedEx(externalDataSelectAll());
        }
    }

    private Boolean externalDataSelectAll() {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelImport tempPanel : panelImports) {
            boolean select = ((PanelTransformForMicrosoft) tempPanel.getTransform()).getCheckBoxFirstRowAsField().isSelected();
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

    @Override
    public void registEvents() {
        removeEvents();
        this.textFieldSeparator.getDocument().addDocumentListener(this.separatorListener);
        this.checkBoxFirstRowAsField.addItemListener(this.itemListener);
    }

    @Override
    public void removeEvents() {
        this.textFieldSeparator.getDocument().removeDocumentListener(this.separatorListener);
        this.checkBoxFirstRowAsField.removeItemListener(this.itemListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelSeparator.setText(DataConversionProperties.getString("String_Label_Separator"));
        this.textFieldSeparator.setText(",");
        this.checkBoxFirstRowAsField.setText(DataConversionProperties.getString("String_ImportSettingPanel_Checkbox_FirstRowisField"));
    }

    public JTextField getTextFieldSeparator() {
        return textFieldSeparator;
    }

    public JCheckBox getCheckBoxFirstRowAsField() {
        return checkBoxFirstRowAsField;
    }

    public String getSameSeparator() {
        String result = "";
        String temp = ((PanelTransformForMicrosoft) panelImports.get(0).getTransform()).getTextFieldSeparator().getText();
        boolean isSame = true;
        for (PanelImport tempPanel : panelImports) {
            String tempObject = ((PanelTransformForMicrosoft) tempPanel.getTransform()).getTextFieldSeparator().getText();
            if (!temp.equals(tempObject)) {
                isSame = false;
                break;
            }
        }
        if (isSame) {
            result = temp;
        }
        return result;
    }
}
