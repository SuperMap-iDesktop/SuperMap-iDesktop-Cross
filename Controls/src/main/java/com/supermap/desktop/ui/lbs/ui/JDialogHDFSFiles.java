package com.supermap.desktop.ui.lbs.ui;

import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.lbs.impl.WebHDFS;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 下载,上传主界面
 *
 * @author
 */
public class JDialogHDFSFiles extends SmDialog {


    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanelHDFSFiles panelHDFSFiles;

    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeAndDispose();
        }
    };

    private void removeAndDispose() {
        removeEvents();
        JDialogHDFSFiles.this.dispose();
    }

    private ActionListener okListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!StringUtilities.isNullOrEmpty(panelHDFSFiles.getURL())) {
                String name = (String) panelHDFSFiles.getTable().getModel().getValueAt(panelHDFSFiles.getTable().getSelectedRow(), 1);
                String url = panelHDFSFiles.getURL();
                if (!url.endsWith("/")) {
                    url += "/";
                }
                WebHDFS.resultURL = url + name;
                dialogResult = DialogResult.OK;
                removeAndDispose();
            }
        }
    };

    public JDialogHDFSFiles() {
        initializeComponents();
        initializeLayout();
        registEvents();
        this.setTitle(CommonProperties.getString("String_SelectFile"));
        setLocationRelativeTo(null);
    }


    private void registEvents() {
        removeEvents();
        this.buttonOK.addActionListener(this.okListener);
        this.buttonCancel.addActionListener(this.cancelListener);
    }

    private void removeEvents() {
        this.buttonOK.removeActionListener(this.okListener);
        this.buttonCancel.removeActionListener(this.cancelListener);
    }

    public void initializeComponents() {
        this.setSize(900, 600);
        this.panelHDFSFiles = new JPanelHDFSFiles();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
    }

    private void initializeLayout() {
        GroupLayout gLayout = new GroupLayout(this.getContentPane());
        gLayout.setAutoCreateContainerGaps(true);
        gLayout.setAutoCreateGaps(true);
        this.getContentPane().setLayout(gLayout);

        // @formatter:off
        gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(panelHDFSFiles, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gLayout.createSequentialGroup()
                        .addGap(10, 10, Short.MAX_VALUE)
                        .addComponent(this.buttonOK, 60, 60, 60)
                        .addComponent(this.buttonCancel, 60, 60, 60))
        );

        gLayout.setVerticalGroup(gLayout.createSequentialGroup()
                .addComponent(panelHDFSFiles, 100, 200, Short.MAX_VALUE)
                .addGroup(gLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.buttonOK)
                        .addComponent(this.buttonCancel)));
        // @formatter:on
    }

    public DialogResult showDialog() {
        this.setVisible(true);
        return this.dialogResult;
    }

}
