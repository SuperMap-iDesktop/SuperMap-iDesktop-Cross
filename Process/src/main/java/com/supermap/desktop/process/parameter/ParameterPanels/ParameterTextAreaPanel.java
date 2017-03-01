package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.implement.ParameterTextArea;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by xie on 2017/2/21.
 */
public class ParameterTextAreaPanel extends JPanel {
    private ParameterTextArea parameterTextArea;
    private JLabel label;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private boolean selectingItem;

    public ParameterTextAreaPanel(ParameterTextArea parameterTextArea) {
        this.parameterTextArea = parameterTextArea;
        initComponents();
        initLayout();
        initListener();
    }

    private void initListener() {
        this.textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeItem();
            }

            private void changeItem() {
                if (!selectingItem&&!StringUtilities.isNullOrEmptyString(textArea.getText())){
                    selectingItem = true;
                    parameterTextArea.setSelectedItem(textArea.getText());
                    selectingItem = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeItem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeItem();
            }
        });
        this.parameterTextArea.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!selectingItem){
                    selectingItem = true;
                    textArea.setText((String) evt.getNewValue());
                    selectingItem = false;
                }
            }
        });
    }

    private void initLayout() {
        label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
        this.setLayout(new GridBagLayout());
        this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1).setAnchor(GridBagConstraints.WEST));
        this.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 0, 0).setIpad(0, 30));
        this.scrollPane.setViewportView(textArea);
    }

    private void initComponents() {
        this.scrollPane = new JScrollPane();
        this.label = new JLabel();
        if (!StringUtilities.isNullOrEmpty(parameterTextArea.getDiscribe()))
            this.label.setText(parameterTextArea.getDiscribe());
        this.textArea = new JTextArea();
        if (StringUtilities.isNullOrEmpty((String) parameterTextArea.getSelectedItem())) {
            this.textArea.setText((String) parameterTextArea.getSelectedItem());
        }

    }
}
