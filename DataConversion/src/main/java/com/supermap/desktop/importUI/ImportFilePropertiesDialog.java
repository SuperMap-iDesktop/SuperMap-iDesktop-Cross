package com.supermap.desktop.importUI;

import com.supermap.data.conversion.ImportSetting;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.FileUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by xie on 2016/10/14.
 * 自定义的导入文件属性信息显示界面
 */
public class ImportFilePropertiesDialog extends SmDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JLabel labelFileType = new JLabel();
    private JLabel labelFileLocation = new JLabel();
    private JLabel labelFileSize = new JLabel();
    private JLabel labelFileModify = new JLabel();
    private JLabel labelProperty = new JLabel();
    private JCheckBox checkboxHidden = new JCheckBox();
    private JLabel labelDate = new JLabel("date");
    private JLabel labelSize = new JLabel("size");
    private JLabel labelPath = new JLabel("location");
    private JLabel labelType = new JLabel("type");
    private SmButton buttonSure = new SmButton();
    private SmButton buttonQuit = new SmButton();

    public ImportFilePropertiesDialog(JDialog owner, ImportSetting importSetting) {
        super(owner, true);
        initComponent();
        setFileInfo(importSetting);
        setLocationRelativeTo(owner);
        this.componentList.add(this.buttonSure);
        this.componentList.add(this.buttonQuit);
        this.setFocusTraversalPolicy(policy);
        setComponentName();
    }

    public ImportFilePropertiesDialog() {
        initComponent();
        setComponentName();
    }

    private void setFileInfo(ImportSetting importSetting) {
        if (null != importSetting) {
            String filePath = importSetting.getSourceFilePath();
            this.labelPath.setText(filePath);
            File file = new File(filePath);
            if (file.isDirectory()) {
                this.labelType.setText(CommonProperties.getString("String_Dir"));
            } else {
                this.labelType.setText(FileUtilities.getFileType(filePath));
            }
            this.labelSize.setText(parseFileSize(file.length()));
            String dateStr = new String(DataConversionProperties.getString("string_dataFormat_ch"));
            SimpleDateFormat sdf = new SimpleDateFormat(dateStr);
            this.labelDate.setText(sdf.format(file.lastModified()));
            this.checkboxHidden.setSelected(file.isHidden());
            this.checkboxHidden.setEnabled(false);
        }
    }

    private String parseFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size > kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    private void initComponent() {
        setBounds(100, 100, 600, 250);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), DataConversionProperties
                .getString("string_border_panelBase"), TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(0, 0, 0)));
        buttonSure.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonQuit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        initResource();
        //@formatter:off
        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
                                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                                                .addGap(14))
                                        .addGroup(GroupLayout.Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
                                                .addComponent(buttonSure)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonQuit)
                                                .addGap(22))))
        );
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gl_contentPanel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonSure)
                                        .addComponent(buttonQuit))
                                .addContainerGap(37, Short.MAX_VALUE))
        );
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addGap(12)
                                                .addComponent(labelProperty))
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelFileType)
                                                        .addComponent(labelFileLocation)
                                                        .addComponent(labelFileSize)
                                                        .addComponent(labelFileModify))))
                                .addGap(25)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelDate)
                                        .addComponent(labelSize)
                                        .addComponent(labelPath)
                                        .addComponent(labelType)
                                        .addComponent(checkboxHidden))
                                .addContainerGap(218, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelFileType)
                                                        .addComponent(labelType))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelFileLocation)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelFileSize)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelFileModify)
                                                .addGap(10))
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addComponent(labelPath)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelSize)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelDate)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelProperty)
                                        .addComponent(checkboxHidden))
                                .addContainerGap(44, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
        contentPanel.setLayout(gl_contentPanel);
    }

    public void setComponentName() {
        ComponentUIUtilities.setName(this.contentPanel, "ImportFilePropertiesDialog_contentPanel");
        ComponentUIUtilities.setName(this.labelFileType, "ImportFilePropertiesDialog_labelFileType");
        ComponentUIUtilities.setName(this.labelFileLocation, "ImportFilePropertiesDialog_labelFileLocation");
        ComponentUIUtilities.setName(this.labelFileSize, "ImportFilePropertiesDialog_labelFileSize");
        ComponentUIUtilities.setName(this.labelFileModify, "ImportFilePropertiesDialog_labelFileModify");
        ComponentUIUtilities.setName(this.labelProperty, "ImportFilePropertiesDialog_labelProperty");
        ComponentUIUtilities.setName(this.checkboxHidden, "ImportFilePropertiesDialog_checkboxHidden");
        ComponentUIUtilities.setName(this.labelDate, "ImportFilePropertiesDialog_labelDate");
        ComponentUIUtilities.setName(this.labelSize, "ImportFilePropertiesDialog_labelSize");
        ComponentUIUtilities.setName(this.labelPath, "ImportFilePropertiesDialog_labelPath");
        ComponentUIUtilities.setName(this.labelType, "ImportFilePropertiesDialog_labelType");
        ComponentUIUtilities.setName(this.buttonSure, "ImportFilePropertiesDialog_buttonSure");
        ComponentUIUtilities.setName(this.buttonQuit, "ImportFilePropertiesDialog_buttonQuit");
    }

    private void initResource() {
        setTitle(DataConversionProperties.getString("string_fileProperty"));
        this.labelFileLocation.setText(DataConversionProperties.getString("string_label_lblFileLocation"));
        this.labelFileType.setText(DataConversionProperties.getString("string_label_lblFileType"));
        this.labelFileModify.setText(DataConversionProperties.getString("string_label_lblFileLastModify"));
        this.labelFileSize.setText(DataConversionProperties.getString("string_label_lblFileSize"));
        this.labelProperty.setText(DataConversionProperties.getString("string_label_lblFileProperty"));
        this.checkboxHidden.setText(DataConversionProperties.getString("string_chcekbox_hidden"));
        this.buttonSure.setText(DataConversionProperties.getString("string_button_sure"));
        this.buttonQuit.setText(DataConversionProperties.getString("string_button_quit"));
    }
}
