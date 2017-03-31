package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterComboBoxCellRender;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterEnum;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.EnumParser;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xie
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.ENUM)
public class ParameterEnumPanel extends SwingPanel {

    private ParameterEnum parameterEnum;
    private boolean isSelectingItem = false;

    private JLabel label = new JLabel();
    private JComboBox comboBox = new JComboBox();

    public ParameterEnumPanel(IParameter parameterEnum) {
        super(parameterEnum);
        this.parameterEnum = (ParameterEnum) parameterEnum;
        label.setText(this.parameterEnum.getDescribe());
        initComboBoxItems();
        comboBox.setSelectedItem(this.parameterEnum.getSelectedItem());
        initListeners();
        initLayout();
    }

    private void initLayout() {
        label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
        comboBox.setPreferredSize(new Dimension(20, 23));
        panel.setLayout(new GridBagLayout());
        panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
        panel.add(comboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
    }

    /**
     * 根据class添加comboBox子项
     * Add comboBox sub items according to class
     */
    private void initComboBoxItems() {
        EnumParser parser = parameterEnum.getEnumParser();
        CopyOnWriteArrayList enumItems = parser.getEnumItems();
        if (enumItems.size() > 0) {
            int size = enumItems.size();
            for (int i = 0; i < size; i++) {
                comboBox.addItem(enumItems.get(i));
            }
        }
        comboBox.setRenderer(new ParameterComboBoxCellRender());
    }

    private void initListeners() {
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
                    isSelectingItem = true;
                    ParameterEnumPanel.this.parameterEnum.setSelectedItem(comboBox.getSelectedItem());
                    isSelectingItem = false;
                }
            }
        });
        parameterEnum.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
                    isSelectingItem = true;
                    ParameterEnumPanel.this.comboBox.setSelectedItem(evt.getNewValue());
                    isSelectingItem = false;
                }
            }
        });
    }

}
