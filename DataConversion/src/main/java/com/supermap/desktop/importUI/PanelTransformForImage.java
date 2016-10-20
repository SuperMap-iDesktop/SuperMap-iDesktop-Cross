package com.supermap.desktop.importUI;

import com.supermap.data.conversion.*;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.util.ImportInfoUtil;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

/**
 * Created by xie on 2016/10/11.
 * bmp,png,gif,sit,tif,tiff,sid,ecw,img,jpk,jpg,jp2,jpeg等图像文件的转换参数设置界面
 */
public class PanelTransformForImage extends PanelTransform {
    private JLabel labelBandImportModel;
    private JComboBox comboBoxBandImportModel;
    private JLabel labelPrjFile;
    private FileChooserControl chooserControlPrjFile;
    private JLabel labelPassWord;
    private JPasswordField passwordField;
    private final int SINGLEBAND = 0;
    private final int MULTIBAND = 1;
    private final int COMPOSITE = 2;
    private ItemListener multiBandImportModeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (importSetting instanceof ImportSettingIMG && null != ((ImportSettingIMG) importSetting).getMultiBandImportMode()) {
                int index = comboBoxBandImportModel.getSelectedIndex();
                switch (index) {
                    case SINGLEBAND:
                        ((ImportSettingIMG) importSetting).setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
                        break;
                    case MULTIBAND:
                        ((ImportSettingIMG) importSetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
                        break;
                    case COMPOSITE:
                        ((ImportSettingIMG) importSetting).setMultiBandImportMode(MultiBandImportMode.COMPOSITE);
                        break;
                    default:
                        break;
                }
            }
            if (importSetting instanceof ImportSettingTIF && null != ((ImportSettingTIF) importSetting).getMultiBandImportMode()) {
                int index = comboBoxBandImportModel.getSelectedIndex();
                switch (index) {
                    case SINGLEBAND:
                        ((ImportSettingTIF) importSetting).setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
                        break;
                    case MULTIBAND:
                        ((ImportSettingTIF) importSetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
                        break;
                    case COMPOSITE:
                        ((ImportSettingTIF) importSetting).setMultiBandImportMode(MultiBandImportMode.COMPOSITE);
                        break;
                    default:
                        break;
                }
            }
            if (importSetting instanceof ImportSettingMrSID && null != ((ImportSettingMrSID) importSetting).getMultiBandImportMode()) {
                int index = comboBoxBandImportModel.getSelectedIndex();
                switch (index) {
                    case SINGLEBAND:
                        ((ImportSettingMrSID) importSetting).setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
                        break;
                    case MULTIBAND:
                        ((ImportSettingMrSID) importSetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
                        break;
                    case COMPOSITE:
                        ((ImportSettingMrSID) importSetting).setMultiBandImportMode(MultiBandImportMode.COMPOSITE);
                        break;
                    default:
                        break;
                }
            }
            if (importSetting instanceof ImportSettingECW && null != ((ImportSettingECW) importSetting).getMultiBandImportMode()) {
                int index = comboBoxBandImportModel.getSelectedIndex();
                switch (index) {
                    case SINGLEBAND:
                        ((ImportSettingECW) importSetting).setMultiBandImportMode(MultiBandImportMode.SINGLEBAND);
                        break;
                    case MULTIBAND:
                        ((ImportSettingECW) importSetting).setMultiBandImportMode(MultiBandImportMode.MULTIBAND);
                        break;
                    case COMPOSITE:
                        ((ImportSettingECW) importSetting).setMultiBandImportMode(MultiBandImportMode.COMPOSITE);
                        break;
                    default:
                        break;
                }
            }
        }
    };
    private DocumentListener passwordListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updatePassword();
        }

        private void updatePassword() {
            String password = String.valueOf(passwordField.getPassword());
            if (!StringUtilities.isNullOrEmpty(password)) {
                ((ImportSettingSIT) importSetting).setPassword(password);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updatePassword();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updatePassword();
        }
    };
    private ActionListener prjFileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!SmFileChoose.isModuleExist("ImportPI")) {
                String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_tfw"), "tfw");
                SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                        DataConversionProperties.getString("String_Import"), "ImportPI", "OpenMany");
            }
            SmFileChoose fileChooser = new SmFileChoose("ImportPI");

            int state = fileChooser.showDefaultDialog();
            File file = fileChooser.getSelectedFile();
            if (state == JFileChooser.APPROVE_OPTION && null != file) {
                chooserControlPrjFile.getEditor().setText(file.getAbsolutePath());
                // 设置坐标参考文件
                String worldFile = chooserControlPrjFile.getEditor().getText();

                if (ImportInfoUtil.isExtendsFile(worldFile)) {
                    setWorldFilePath(worldFile);
                }
            }
        }

        private void setWorldFilePath(String worldFile) {
            if (importSetting instanceof ImportSettingBMP) {
                ((ImportSettingBMP) importSetting).setWorldFilePath(worldFile);
            }
            if (importSetting instanceof ImportSettingJPG) {
                ((ImportSettingJPG) importSetting).setWorldFilePath(worldFile);
            }
            if (importSetting instanceof ImportSettingPNG) {
                ((ImportSettingPNG) importSetting).setWorldFilePath(worldFile);
            }
            if (importSetting instanceof ImportSettingGIF) {
                ((ImportSettingGIF) importSetting).setWorldFilePath(worldFile);
            }
        }
    };

    public PanelTransformForImage(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelBandImportModel = new JLabel();
        this.comboBoxBandImportModel = new JComboBox();
        initComboBoxBandImportModel();
        this.labelPrjFile = new JLabel();
        this.chooserControlPrjFile = new FileChooserControl();
        initChooserControlPrjFile();
        this.labelPassWord = new JLabel();
        this.passwordField = new JPasswordField();
        initPassWord();
    }

    private void initPassWord() {
        if (importSetting instanceof ImportSettingSIT && !StringUtilities.isNullOrEmpty(((ImportSettingSIT) importSetting).getPassword())) {
            passwordField.setText(((ImportSettingSIT) importSetting).getPassword());
        }
    }

    private void initChooserControlPrjFile() {
        if (importSetting instanceof ImportSettingBMP && !StringUtilities.isNullOrEmpty(((ImportSettingBMP) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingBMP) importSetting).getWorldFilePath());
        }
        if (importSetting instanceof ImportSettingJPG && !StringUtilities.isNullOrEmpty(((ImportSettingJPG) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingJPG) importSetting).getWorldFilePath());
        }
        if (importSetting instanceof ImportSettingPNG && !StringUtilities.isNullOrEmpty(((ImportSettingPNG) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingPNG) importSetting).getWorldFilePath());
        }
        if (importSetting instanceof ImportSettingGIF && !StringUtilities.isNullOrEmpty(((ImportSettingGIF) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingGIF) importSetting).getWorldFilePath());
        }
    }

    private void initComboBoxBandImportModel() {
        this.comboBoxBandImportModel.setEditable(true);
        ((JTextField) this.comboBoxBandImportModel.getEditor().getEditorComponent()).setEditable(false);
        this.comboBoxBandImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_singleBand"),
                DataConversionProperties.getString("string_multiBand"), DataConversionProperties.getString("string_compositeBand")}));
        if (importSetting instanceof ImportSettingIMG && null != ((ImportSettingIMG) importSetting).getMultiBandImportMode()) {
            MultiBandImportMode multiBandImportMode = ((ImportSettingIMG) importSetting).getMultiBandImportMode();
            if (multiBandImportMode.equals(MultiBandImportMode.SINGLEBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(0);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.MULTIBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(1);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.COMPOSITE)) {
                this.comboBoxBandImportModel.setSelectedIndex(2);
            }
            return;
        }
        if (importSetting instanceof ImportSettingTIF && null != ((ImportSettingTIF) importSetting).getMultiBandImportMode()) {
            MultiBandImportMode multiBandImportMode = ((ImportSettingTIF) importSetting).getMultiBandImportMode();
            if (multiBandImportMode.equals(MultiBandImportMode.SINGLEBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(0);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.MULTIBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(1);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.COMPOSITE)) {
                this.comboBoxBandImportModel.setSelectedIndex(2);
            }
            return;
        }
        if (importSetting instanceof ImportSettingMrSID && null != ((ImportSettingMrSID) importSetting).getMultiBandImportMode()) {
            MultiBandImportMode multiBandImportMode = ((ImportSettingMrSID) importSetting).getMultiBandImportMode();
            if (multiBandImportMode.equals(MultiBandImportMode.SINGLEBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(0);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.MULTIBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(1);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.COMPOSITE)) {
                this.comboBoxBandImportModel.setSelectedIndex(2);
            }
            return;
        }
        if (importSetting instanceof ImportSettingECW && null != ((ImportSettingECW) importSetting).getMultiBandImportMode()) {
            MultiBandImportMode multiBandImportMode = ((ImportSettingECW) importSetting).getMultiBandImportMode();
            if (multiBandImportMode.equals(MultiBandImportMode.SINGLEBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(0);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.MULTIBAND)) {
                this.comboBoxBandImportModel.setSelectedIndex(1);
            }
            if (multiBandImportMode.equals(MultiBandImportMode.COMPOSITE)) {
                this.comboBoxBandImportModel.setSelectedIndex(2);
            }
            return;
        }
    }

    @Override
    public void initLayerout() {
        this.removeAll();
        this.setLayout(new GridBagLayout());
        if (importSetting instanceof ImportSettingJP2) {
            return;
        }
        if (importSetting instanceof ImportSettingSIT) {
            this.add(this.labelPassWord, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 15).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.passwordField, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        } else if (importSetting instanceof ImportSettingTIF) {
            this.add(this.labelBandImportModel, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxBandImportModel, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.chooserControlPrjFile, new GridBagConstraintsHelper(2, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        } else if (importSetting instanceof ImportSettingIMG || importSetting instanceof ImportSettingMrSID || importSetting instanceof ImportSettingECW) {
            this.add(this.labelBandImportModel, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxBandImportModel, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        } else {
            this.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.chooserControlPrjFile, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        }
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.comboBoxBandImportModel.addItemListener(this.multiBandImportModeListener);
        this.passwordField.getDocument().addDocumentListener(this.passwordListener);
        this.chooserControlPrjFile.getButton().addActionListener(this.prjFileListener);
    }

    @Override
    public void removeEvents() {
        this.comboBoxBandImportModel.removeItemListener(this.multiBandImportModeListener);
        this.passwordField.getDocument().removeDocumentListener(this.passwordListener);
        this.chooserControlPrjFile.getButton().removeActionListener(this.prjFileListener);
    }

    @Override
    public void initResources() {
        if (importSetting instanceof ImportSettingJP2) {
            return;
        }
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelPassWord.setText(DataConversionProperties.getString("string_label_lblPassword") + "       ");
        this.labelPrjFile.setText(DataConversionProperties.getString("String_FormExport_LabelWorldFile"));
        this.labelBandImportModel.setText(DataConversionProperties.getString("string_label_lblSaveImport"));
    }
}
