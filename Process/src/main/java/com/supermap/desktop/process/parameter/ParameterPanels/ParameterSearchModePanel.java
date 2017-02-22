package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.analyst.spatialanalyst.SearchMode;
import com.supermap.desktop.process.parameter.ParameterSearchModeInfo;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterSearchMode;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Created by xie on 2017/2/17.
 */
public class ParameterSearchModePanel extends JPanel {
    private JLabel labelSearchModel;
    private JRadioButton radioSearchModelCount;
    private JRadioButton radioSearchModelRadius;
    private JLabel labelMaxRadius;
    private JLabel labelSearchCount;
    private JTextField textFieldMaxRadius;
    private JTextField textFieldSearchCount;

    private boolean isSelectingItem = false;
    private ParameterSearchMode parameterSearchMode;
    private ParameterSearchModeInfo info;

    public ParameterSearchModePanel(ParameterSearchMode parameterSearchMode) {
        this.parameterSearchMode = parameterSearchMode;
        this.info = (ParameterSearchModeInfo) parameterSearchMode.getSelectedItem();
        initComponents();
        initLayout();
        initListener();
    }

    private void initListener() {
        this.radioSearchModelRadius.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSelectingItem) {
                    isSelectingItem = true;
                    boolean selected = radioSearchModelRadius.isSelected();
                    labelMaxRadius.setText(selected ? CommonProperties.getString("String_SearchRadius") : CommonProperties.getString("String_MaxRadius"));
                    labelSearchCount.setText(selected ? CommonProperties.getString("String_MinCount") : CommonProperties.getString("String_SearchCount"));
                    if (null == info) {
                        info = new ParameterSearchModeInfo();
                    }
                    info.searchMode = selected ? SearchMode.KDTREE_FIXED_RADIUS : SearchMode.KDTREE_FIXED_COUNT;
                    parameterSearchMode.setSelectedItem(info);
                    isSelectingItem = false;
                }
            }
        });
        this.radioSearchModelCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSelectingItem) {
                    isSelectingItem = true;
                    boolean selected = radioSearchModelCount.isSelected();
                    labelMaxRadius.setText(selected ? CommonProperties.getString("String_MaxRadius") : CommonProperties.getString("String_SearchRadius"));
                    labelSearchCount.setText(selected ? CommonProperties.getString("String_SearchCount") : CommonProperties.getString("String_MinCount"));
                    if (null == info) {
                        info = new ParameterSearchModeInfo();
                    }
                    info.searchMode = selected ? SearchMode.KDTREE_FIXED_COUNT : SearchMode.KDTREE_FIXED_RADIUS;
                    parameterSearchMode.setSelectedItem(info);
                    isSelectingItem = false;
                }
            }
        });
        this.textFieldMaxRadius.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeRadius();
            }

            private void changeRadius() {
                if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldMaxRadius.getText())) {
                    isSelectingItem = true;
                    if (null == info) {
                        info = new ParameterSearchModeInfo();
                    }
                    info.searchRadius = Double.parseDouble(textFieldMaxRadius.getText());
                    isSelectingItem = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeRadius();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeRadius();
            }
        });
        this.textFieldSearchCount.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeSearchCount();
            }

            private void changeSearchCount() {
                if (!isSelectingItem && !StringUtilities.isNullOrEmpty(textFieldSearchCount.getText())) {
                    isSelectingItem = true;
                    if (null == info) {
                        info = new ParameterSearchModeInfo();
                    }
                    info.expectedCount = Integer.parseInt(textFieldSearchCount.getText());
                    isSelectingItem = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeSearchCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeSearchCount();
            }
        });
        parameterSearchMode.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!isSelectingItem && Objects.equals(evt.getPropertyName(), AbstractParameter.PROPERTY_VALE)) {
                    isSelectingItem = true;
                    if (null != evt.getNewValue() && evt.getNewValue() instanceof ParameterSearchModeInfo) {
                        ParameterSearchModeInfo selectItem = (ParameterSearchModeInfo) evt.getNewValue();
                        radioSearchModelRadius.setSelected(selectItem.searchMode==SearchMode.KDTREE_FIXED_RADIUS?true:false);
                        radioSearchModelCount.setSelected(selectItem.searchMode==SearchMode.KDTREE_FIXED_COUNT?true:false);
                        labelMaxRadius.setText(selectItem.searchMode==SearchMode.KDTREE_FIXED_COUNT?CommonProperties.getString("String_MaxRadius"):CommonProperties.getString("String_SearchRadius"));
                        labelSearchCount.setText(selectItem.searchMode==SearchMode.KDTREE_FIXED_COUNT?CommonProperties.getString("String_SearchCount") : CommonProperties.getString("String_MinCount"));
                    }
                    isSelectingItem = false;
                }
            }
        });
    }

    private void initComponents() {
        this.labelSearchModel = new JLabel();
        this.radioSearchModelCount = new JRadioButton();
        this.radioSearchModelRadius = new JRadioButton();
        this.labelMaxRadius = new JLabel();
        this.labelSearchCount = new JLabel();
        this.textFieldMaxRadius = new JTextField();
        this.textFieldSearchCount = new JTextField();
        if (null != info) {
            this.radioSearchModelCount.setSelected(info.searchMode == SearchMode.KDTREE_FIXED_COUNT);
            this.radioSearchModelRadius.setSelected(info.searchMode == SearchMode.KDTREE_FIXED_RADIUS);
            this.labelMaxRadius.setText(info.searchMode == SearchMode.KDTREE_FIXED_COUNT ? CommonProperties.getString("String_MaxRadius") : CommonProperties.getString("String_SearchRadius"));
            this.labelSearchCount.setText(info.searchMode == SearchMode.KDTREE_FIXED_COUNT ? CommonProperties.getString("String_SearchCount") : CommonProperties.getString("String_MinCount"));
        }
    }

    private void initLayout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelSearchModel, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
        this.add(this.radioSearchModelCount, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1));
        this.add(this.radioSearchModelRadius, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 1));
        this.add(this.labelMaxRadius, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setInsets(5, 0, 0, 0));
        this.add(this.textFieldMaxRadius, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setInsets(5, 5, 0, 0));
        this.add(this.labelSearchCount, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(0, 1).setInsets(5, 0, 0, 0));
        this.add(this.textFieldSearchCount, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setInsets(5, 5, 0, 0));
    }
}
