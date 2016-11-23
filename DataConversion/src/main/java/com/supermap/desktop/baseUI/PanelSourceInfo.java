package com.supermap.desktop.baseUI;

import com.supermap.data.Charset;
import com.supermap.data.conversion.*;
import com.supermap.desktop.Interface.IImportSettingSourceInfo;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.importUI.ImportFilePropertiesDialog;
import com.supermap.desktop.importUI.PanelImport;
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
import java.util.ArrayList;

/**
 * Created by xie on 2016/9/29.
 * 源文件信息界面
 */
public class PanelSourceInfo extends JPanel implements IImportSettingSourceInfo {

    private JLabel labelSourceFilePath;
    private JTextField textFieldSourceFilePath;
    private JButton buttonProperty;
    private JLabel labelCharset;
    private CharsetComboBox comboBoxCharset;
    private ImportSetting importSetting;
    private JDialog owner;
    private ArrayList<PanelImport> panelImports;
    private int layoutType;

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
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    tempPanelImport.getSourceInfo().getComboBoxCharset().setSelectedItem(comboBoxCharset.getSelectedItem());
                }
            } else if (null != importSetting) {
                Charset newCharset = CharsetUtilities.valueOf(comboBoxCharset.getSelectedItem().toString());
                importSetting.setSourceFileCharset(newCharset);
            }
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

    public PanelSourceInfo(ArrayList<PanelImport> panelImports, int layoutType) {
        this.panelImports = panelImports;
        this.layoutType = layoutType;
        this.importSetting = panelImports.get(panelImports.size() - 1).getImportInfo().getImportSetting();
        initComponents();
        if (layoutType == PackageInfo.SAME_TYPE) {
            initLayerout();
        } else if (layoutType == PackageInfo.GRID_TYPE) {
            initLayerout();
        } else if (layoutType == PackageInfo.VERTICAL_TYPE || layoutType == PackageInfo.GRID_AND_VERTICAL_TYPE) {
            initVerticalLayout();
        }

        initResources();
        registEvents();
    }

    private void initVerticalLayout() {
        this.setLayout(new GridBagLayout());

        this.add(this.labelCharset, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxCharset, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 110).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(new JPanel(), new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.comboBoxCharset.setPreferredSize(PackageInfo.defaultSize);
        this.comboBoxCharset.setEditable(true);
        ((JTextField) this.comboBoxCharset.getEditor().getEditorComponent()).setEditable(false);
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelDatapath")));
    }


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
        if (null == panelImports && (importSetting instanceof ImportSettingTAB || importSetting instanceof ImportSettingMIF)
                && importSetting.getTargetDataInfos("").getCount() > 0) {
            ImportDataInfos dataInfos = importSetting.getTargetDataInfos("");
            Charset chartset = dataInfos.get(0).getSourceCharset();
            comboBoxCharset.setSelectCharset(chartset.name());
            importSetting.setSourceFileCharset(chartset);
        } else if (null == panelImports && null != importSetting.getSourceFileCharset()) {
            comboBoxCharset.setSelectCharset(importSetting.getSourceFileCharset().name());
        } else if (null != panelImports) {
            comboBoxCharset.setSelectedItem(selectedItem());
        }
    }

    private Object selectedItem() {
        Object result = null;
        String temp = panelImports.get(0).getSourceInfo().getComboBoxCharset().getSelectedItem().toString();

        boolean isSame = true;
        for (PanelImport tempPanel : panelImports) {
            String tempObject = tempPanel.getSourceInfo().getComboBoxCharset().getSelectedItem().toString();
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

    @Override
    public void initLayerout() {
        JPanel panelSourceFilePath = new JPanel();
        panelSourceFilePath.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());
        panelSourceFilePath.add(this.labelSourceFilePath, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 22).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelSourceFilePath.add(this.textFieldSourceFilePath, new GridBagConstraintsHelper(2, 0, 5, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelSourceFilePath.add(this.buttonProperty, new GridBagConstraintsHelper(7, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));

        JPanel panelCharset = new JPanel();
        panelCharset.setLayout(new GridBagLayout());
        panelCharset.add(this.labelCharset, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelCharset.add(this.comboBoxCharset, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 120).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelCharset.add(new JPanel(), new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(panelSourceFilePath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(panelCharset, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.comboBoxCharset.setPreferredSize(PackageInfo.defaultSize);
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

    public void needCharset(boolean visible) {
        this.labelCharset.setVisible(visible);
        this.comboBoxCharset.setVisible(visible);
        if (visible) {
            this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelDatapath")));
        }
    }

    @Override
    public CharsetComboBox getComboBoxCharset() {
        return comboBoxCharset;
    }
}
