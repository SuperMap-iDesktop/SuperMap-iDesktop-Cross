package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterHDFSPath;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.lbs.impl.WebHDFS;
import com.supermap.desktop.ui.lbs.ui.JDialogHDFSFiles;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by xie on 2017/2/27.
 */
public class ParameterHDFSPathPanel extends JPanel {
    private JLabel labelFileInputPath;
    private JTextField textFieldFileInputPath;
    private JButton buttonInputBrowser;
    private ParameterHDFSPath parameterHDFSPath;
    private boolean isSelectingItem = false;

    public ParameterHDFSPathPanel(ParameterHDFSPath parameterHDFSPath) {
        this.parameterHDFSPath = parameterHDFSPath;
        initParameterInfo();
        initListener();
    }
    private void initParameterInfo() {
        this.labelFileInputPath = new JLabel();
        this.textFieldFileInputPath = new JTextField();
        this.textFieldFileInputPath.setText((String) parameterHDFSPath.getSelectedItem());
        this.buttonInputBrowser = new JButton();
        this.buttonInputBrowser.setText(ProcessProperties.getString("String_Browser"));
        this.labelFileInputPath.setText(ProcessProperties.getString("String_FileInputPath"));
        this.labelFileInputPath.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
        this.setLayout(new GridBagLayout());
        this.add(this.labelFileInputPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textFieldFileInputPath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0).setWeight(1, 0));
        this.add(this.buttonInputBrowser, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 5, 0, 0));
    }

    private void initListener() {
        this.parameterHDFSPath.addPropertyListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!isSelectingItem && AbstractParameter.PROPERTY_VALE.equals(evt.getPropertyName())) {
                    isSelectingItem = true;
                    WebHDFS.resultURL = textFieldFileInputPath.getText();
                    parameterHDFSPath.setSelectedItem(WebHDFS.getResultHDFSFilePath());
                    isSelectingItem = false;
                }
            }
        });
        this.textFieldFileInputPath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeHDFSPath();
            }

            private void changeHDFSPath() {
                if (!isSelectingItem && !StringUtilities.isNullOrEmptyString(textFieldFileInputPath.getText())) {
                    isSelectingItem = true;
                    WebHDFS.resultURL = textFieldFileInputPath.getText();
                    parameterHDFSPath.setSelectedItem(WebHDFS.getResultHDFSFilePath());
                    isSelectingItem = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeHDFSPath();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeHDFSPath();
            }
        });
        this.buttonInputBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialogHDFSFiles hdfsFiles = new JDialogHDFSFiles();
                if (!isSelectingItem && hdfsFiles.showDialog() == DialogResult.OK
                        && !StringUtilities.isNullOrEmptyString(WebHDFS.getResultHDFSFilePath())) {
                    isSelectingItem = true;
                    WebHDFS.resultURL = textFieldFileInputPath.getText();
                    parameterHDFSPath.setSelectedItem(WebHDFS.getResultHDFSFilePath());
                    isSelectingItem = false;
                }
            }
        });
    }

}
