package com.supermap.desktop.baseUI;

import com.supermap.data.Charset;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Interface.IImportSettingSourceInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.importUI.ImportFilePropertiesDialog;
import com.supermap.desktop.ui.controls.CharsetComboBox;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.CharsetUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by xie on 2016/9/29.
 * 源文件信息界面
 */
public class PanelSourceInfo extends JPanel implements IImportSettingSourceInfo {

    private Charset charset;
    private JLabel labelSourceFilePath;
    private JTextField textFieldSourceFilePath;
    private JButton buttonProperty;
    private JLabel labelCharset;
    private CharsetComboBox comboBoxCharset;
    private ImportSetting importSetting;
    private JDialog owner;

    private ActionListener propertyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImportFilePropertiesDialog dialog = new ImportFilePropertiesDialog(owner, importSetting);
            dialog.setVisible(true);
        }
    };
    private ItemListener charsetListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {

            Charset newCharset = CharsetUtilities.valueOf(comboBoxCharset.getSelectedItem().toString());
            importSetting.setSourceFileCharset(newCharset);
        }
    };

    public PanelSourceInfo(JDialog owner, ImportSetting importSetting) {
        this.owner = owner;
        this.importSetting = importSetting;
        initComponents();
        initLayerout();
        initResources();
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelSourceFilePath = new JLabel();
        this.textFieldSourceFilePath = new JTextField();
        this.textFieldSourceFilePath.setEditable(false);
        this.textFieldSourceFilePath.setText(importSetting.getSourceFilePath());
        this.textFieldSourceFilePath.setToolTipText(importSetting.getSourceFilePath());
        this.buttonProperty = new JButton();
        this.labelCharset = new JLabel();
        this.comboBoxCharset = new CharsetComboBox();
        // 设置字符集默认选中项
        if (importSetting != null && (importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF)
                && importSetting.getTargetDataInfos("").getCount() > 0) {
            ImportDataInfos dataInfos = importSetting.getTargetDataInfos("");
            Charset chartset = dataInfos.get(0).getSourceCharset();
            comboBoxCharset.setSelectCharset(chartset.name());
            importSetting.setSourceFileCharset(chartset);
        }
        if (importSetting != null && null != importSetting.getSourceFileCharset()) {
            comboBoxCharset.setSelectCharset(importSetting.getSourceFileCharset().name());
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelSourceFilePath, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.textFieldSourceFilePath, new GridBagConstraintsHelper(2, 0, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.buttonProperty, new GridBagConstraintsHelper(7, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));

        this.add(this.labelCharset, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxCharset, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0).setIpad(18, 0));
        this.comboBoxCharset.setEditable(true);
        ((JTextField) this.comboBoxCharset.getEditor().getEditorComponent()).setEditable(false);
        if (importSetting instanceof ImportSettingDXF || importSetting instanceof ImportSettingDWG) {
            needCharset(false);
        } else {
            needCharset(true);
        }
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.buttonProperty.addActionListener(this.propertyListener);
        this.comboBoxCharset.addItemListener(this.charsetListener);
    }

    @Override
    public void removeEvents() {
        this.buttonProperty.removeActionListener(this.propertyListener);
        this.comboBoxCharset.removeItemListener(this.charsetListener);
    }

    private void initResources() {
        this.labelSourceFilePath.setText(DataConversionProperties.getString("string_label_lblDataPath"));
        this.labelCharset.setText(DataConversionProperties.getString("string_label_lblCharset"));
        this.textFieldSourceFilePath.setText(importSetting.getSourceFilePath());
        this.textFieldSourceFilePath.setToolTipText(importSetting.getSourceFilePath());
        this.buttonProperty.setText(DataConversionProperties.getString("String_ImportSettingPanel_ButtonProperty"));
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void needCharset(boolean visible) {
        this.labelCharset.setVisible(visible);
        this.comboBoxCharset.setVisible(visible);
        if (visible) {
            this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelDatapath")));
        }
    }
}
