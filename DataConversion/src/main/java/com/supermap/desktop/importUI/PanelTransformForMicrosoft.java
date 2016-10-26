package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.data.conversion.ImportSettingCSV;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
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
    private JCheckBox checkBoxFristRowAsField;
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
                    ((PanelTransformForMicrosoft) tempPanelImport.getTransform()).getCheckBoxFristRowAsField().setSelected(checkBoxFristRowAsField.isSelected());
                }
            } else {
                ((ImportSettingCSV) importSetting).setFirstRowIsField(checkBoxFristRowAsField.isSelected());
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
        registEvents();
    }
    @Override
    public void initComponents() {
        this.checkBoxFristRowAsField = new JCheckBox();
        this.labelSeparator = new JLabel();
        this.textFieldSeparator = new JTextField();
        this.labelEmpty = new JLabel();
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelSeparator, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 45).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textFieldSeparator, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setIpad(30, 0));
        this.add(this.checkBoxFristRowAsField, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.labelEmpty, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

    }

    @Override
    public void registEvents() {
        removeEvents();
        this.textFieldSeparator.getDocument().addDocumentListener(this.separatorListener);
        this.checkBoxFristRowAsField.addItemListener(this.itemListener);
    }

    @Override
    public void removeEvents() {
        this.textFieldSeparator.getDocument().removeDocumentListener(this.separatorListener);
        this.checkBoxFristRowAsField.removeItemListener(this.itemListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelSeparator.setText(DataConversionProperties.getString("String_Label_Separator"));
        this.textFieldSeparator.setText(",");
        this.checkBoxFristRowAsField.setText(DataConversionProperties.getString("String_ImportSettingPanel_Checkbox_FirstRowisField"));
    }

    public JTextField getTextFieldSeparator() {
        return textFieldSeparator;
    }

    public JCheckBox getCheckBoxFristRowAsField() {
        return checkBoxFristRowAsField;
    }
}
