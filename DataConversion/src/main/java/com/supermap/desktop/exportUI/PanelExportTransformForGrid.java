package com.supermap.desktop.exportUI;

import com.supermap.data.conversion.*;
import com.supermap.desktop.baseUI.PanelExportTransform;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.iml.ExportFileInfo;
import com.supermap.desktop.localUtilities.CommonUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/26.
 * 导出栅格数据集矢量参数设置
 */
public class PanelExportTransformForGrid extends PanelExportTransform {
    private JLabel labelCompressionRatio;
    private JTextField textFieldCompressionRatio;
    private JLabel labelPrjFile;
    private FileChooserControl prjFileChooser;
    private TristateCheckBox checkBoxExportTFW;
    private JLabel labelPassword;
    private JPasswordField passwordField;
    private JLabel labelPasswordConfrim;
    private JPasswordField passwordFieldConfrim;
    private ActionListener prjListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!SmFileChoose.isModuleExist("DataExportFrame")) {
                String fileFilters = SmFileChoose.createFileFilter(DataConversionProperties.getString("string_filetype_tfw"), "tfw");
                SmFileChoose.addNewNode(fileFilters, CommonProperties.getString("String_DefaultFilePath"),
                        DataConversionProperties.getString("String_Export"), "DataExportFrame", "SaveOne");
            }

            SmFileChoose fileChooserc = new SmFileChoose("DataExportFrame");

            int state = fileChooserc.showDefaultDialog();
            File file = fileChooserc.getSelectedFile();
            if (state == JFileChooser.APPROVE_OPTION && null != file) {
                prjFileChooser.getEditor().setText(file.getAbsolutePath());
                if (null != panels) {
                    for (PanelExportTransform tempPanel : panels) {
                        ((PanelExportTransformForGrid) tempPanel).getPrjFileChooser().getEditor().setText(file.getAbsolutePath());
                    }
                }
            }
        }
    };
    private DocumentListener compressionRatioListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setCompressionRatio();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setCompressionRatio();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setCompressionRatio();
        }

        private void setCompressionRatio() {
            String compression = textFieldCompressionRatio.getText();
            ExportSetting exportSetting = exportsFileInfo.getExportSetting();
            if (!StringUtilities.isNullOrEmpty(compression) && (exportSetting instanceof ExportSettingJPG)) {
                if (null != panels) {
                    for (PanelExportTransform tempPanel : panels) {
                        ((PanelExportTransformForGrid) tempPanel).getTextFieldCompressionRatio().setText(compression);
                    }
                } else {
                    ((ExportSettingJPG) exportSetting).setCompression(Integer.valueOf(compression));
                }
            }
        }
    };
    private StateChangeListener tfwListener = new StateChangeListener() {

        @Override
        public void stateChange(StateChangeEvent e) {
            if (null != panels) {
                for (PanelExportTransform tempPanel : panels) {
                    ((PanelExportTransformForGrid) tempPanel).getCheckBoxExportTFW().setSelected(checkBoxExportTFW.isSelectedEx());
                }
            } else {
                ((ExportSettingTIF) exportsFileInfo.getExportSetting()).setExportingGeoTransformFile(checkBoxExportTFW.isSelectedEx());
            }
        }
    };
    private KeyListener keyAdapter = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String password = String.valueOf(passwordField.getPassword());
            String confrim = String.valueOf(passwordFieldConfrim.getPassword());
            if (!confrim.equals(password) && e.getSource().equals(passwordFieldConfrim)) {
                UICommonToolkit.showConfirmDialog(DataConversionProperties.getString("string_PasswordError"));
                passwordFieldConfrim.requestFocus();
                return;
            } else {
                ExportSetting exportSetting = exportsFileInfo.getExportSetting();
                if (!StringUtilities.isNullOrEmpty(password)) {
                    if (null != panels) {
                        for (PanelExportTransform tempPanel : panels) {
                            ((PanelExportTransformForGrid) tempPanel).getPasswordField().setText(password);
                            ((PanelExportTransformForGrid) tempPanel).getPasswordFieldConfrim().setText(password);
                        }
                    } else {
                        ((ExportSettingSIT) exportSetting).setPassword(password);
                    }
                }
            }
        }
    };
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            setPrjFile();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            setPrjFile();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            setPrjFile();
        }

        private void setPrjFile(ExportSetting exportSetting, String worldFile) {
            if (exportSetting instanceof ExportSettingBMP) {
                ((ExportSettingBMP) exportSetting).setWorldFilePath(worldFile);
            }
            if (exportSetting instanceof ExportSettingGIF) {
                ((ExportSettingGIF) exportSetting).setWorldFilePath(worldFile);
            }
            if (exportSetting instanceof ExportSettingJPG) {
                ((ExportSettingJPG) exportSetting).setWorldFilePath(worldFile);
            }
            if (exportSetting instanceof ExportSettingPNG) {
                ((ExportSettingPNG) exportSetting).setWorldFilePath(worldFile);
            }
        }

        private void setPrjFile() {
            // 设置坐标参考文件
            String worldFile = prjFileChooser.getEditor().getText();
            if (CommonUtilities.isExtendsFile(worldFile)) {
                if (null != panels) {
                    for (PanelExportTransform tempPanel : panels) {
                        setPrjFile(tempPanel.getExportsFileInfo().getExportSetting(), worldFile);
                    }
                } else {
                    setPrjFile(exportsFileInfo.getExportSetting(), worldFile);
                }
            }

        }
    };

    public PanelExportTransformForGrid(ExportFileInfo exportsFileInfo) {
        super(exportsFileInfo);
        registEvents();
    }

    public PanelExportTransformForGrid(ArrayList<PanelExportTransform> panelExportTransforms, int layoutType) {
        super(panelExportTransforms, layoutType);
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelCompressionRatio = new JLabel();
        this.textFieldCompressionRatio = new JTextField();
        this.labelPrjFile = new JLabel();
        this.prjFileChooser = new FileChooserControl();
        this.checkBoxExportTFW = new TristateCheckBox();
        this.labelPassword = new JLabel();
        this.passwordField = new JPasswordField();
        this.labelPasswordConfrim = new JLabel();
        this.passwordFieldConfrim = new JPasswordField();
        setUnEnabled();
        if (null != exportsFileInfo && null != exportsFileInfo.getExportSetting()) {
            initComponentsState(exportsFileInfo.getExportSetting());
            if (exportsFileInfo.getExportSetting() instanceof ExportSettingJPG) {
                this.textFieldCompressionRatio.setEnabled(true);
                this.textFieldCompressionRatio.setText(String.valueOf(((ExportSettingJPG) exportsFileInfo.getExportSetting()).getCompression()));
            } else if (exportsFileInfo.getExportSetting() instanceof ExportSettingTIF) {
                this.checkBoxExportTFW.setSelectedEx(((ExportSettingTIF) exportsFileInfo.getExportSetting()).isExportingGeoTransformFile());
            }
        } else if (null != panels) {
            initComponentsState(panels, layoutType);
        }
    }

    private void initComponentsState(ArrayList<PanelExportTransform> panels, int layoutType) {
        if (layoutType == 0) {
            //相同类型
            initComponentsState(panels.get(0).getExportsFileInfo().getExportSetting());
            if (panels.get(0).getExportsFileInfo().getExportSetting() instanceof ExportSettingJPG) {
                this.textFieldCompressionRatio.setEnabled(true);
            } else if (panels.get(0).getExportsFileInfo().getExportSetting() instanceof ExportSettingTIF) {
                this.checkBoxExportTFW.setSelectedEx(checkBoxSelectAll(panels));
            }
        } else if (isPrjTypes(panels)) {
            this.prjFileChooser.setEnabled(true);
        }
    }

    private Boolean checkBoxSelectAll(ArrayList<PanelExportTransform> panels) {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelExportTransform tempPanel : panels) {
            if (((PanelExportTransformForGrid) tempPanel).getCheckBoxExportTFW().isSelected()) {
                selectCount++;
            } else if (!((PanelExportTransformForGrid) tempPanel).getCheckBoxExportTFW().isSelected()) {
                unSelectCount++;
            }
        }
        if (selectCount == panels.size()) {
            result = true;
        } else if (unSelectCount == panels.size()) {
            result = false;
        }
        return result;
    }

    private boolean isPrjTypes(ArrayList<PanelExportTransform> panels) {
        int count = 0;
        for (PanelExportTransform tempPanel : panels) {
            ExportSetting exportSetting = tempPanel.getExportsFileInfo().getExportSetting();
            if (exportSetting instanceof ExportSettingBMP || exportSetting instanceof ExportSettingGIF ||
                    exportSetting instanceof ExportSettingJPG || exportSetting instanceof ExportSettingPNG) {
                count++;
            }
        }
        return count == panels.size();
    }

    private void initComponentsState(ExportSetting exportSetting) {
        if (exportSetting instanceof ExportSettingBMP || exportSetting instanceof ExportSettingGIF ||
                exportSetting instanceof ExportSettingJPG || exportSetting instanceof ExportSettingPNG) {
            this.prjFileChooser.setEnabled(true);
        } else if (exportSetting instanceof ExportSettingTIF) {
            this.checkBoxExportTFW.setEnabled(true);
        } else if (exportSetting instanceof ExportSettingSIT) {
            this.passwordField.setEnabled(true);
            this.passwordFieldConfrim.setEnabled(true);
        }
    }

    @Override
    public void initLayerout() {
        JPanel panelContent = new JPanel();
        panelContent.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());
        this.add(panelContent, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 1).setIpad(37, 0));

        panelContent.add(this.labelCompressionRatio, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.textFieldCompressionRatio, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.prjFileChooser, new GridBagConstraintsHelper(0, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.checkBoxExportTFW, new GridBagConstraintsHelper(0, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 0).setFill(GridBagConstraints.HORIZONTAL).setWeight(0, 0));
        panelContent.add(this.labelPassword, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.passwordField, new GridBagConstraintsHelper(1, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelContent.add(this.labelPasswordConfrim, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelContent.add(this.passwordFieldConfrim, new GridBagConstraintsHelper(1, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 5).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.getPrjFileChooser().getEditor().getDocument().addDocumentListener(this.documentListener);
        this.prjFileChooser.getButton().addActionListener(this.prjListener);
        this.textFieldCompressionRatio.getDocument().addDocumentListener(this.compressionRatioListener);
        this.checkBoxExportTFW.addStateChangeListener(this.tfwListener);
        this.passwordField.addKeyListener(this.keyAdapter);
        this.passwordFieldConfrim.addKeyListener(this.keyAdapter);
    }

    @Override
    public void removeEvents() {
        this.prjFileChooser.getButton().removeActionListener(this.prjListener);
        this.getPrjFileChooser().getEditor().getDocument().removeDocumentListener(this.documentListener);
        this.textFieldCompressionRatio.getDocument().removeDocumentListener(this.compressionRatioListener);
        this.checkBoxExportTFW.removeStateChangeListener(this.tfwListener);
        this.passwordField.removeKeyListener(this.keyAdapter);
        this.passwordFieldConfrim.removeKeyListener(this.keyAdapter);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(null, DataConversionProperties.getString("string_border_panelproperty")));
        this.labelCompressionRatio.setText(DataConversionProperties.getString("String_CompressionRatio"));
        this.labelPrjFile.setText(DataConversionProperties.getString("string_label_lblFile"));
        this.checkBoxExportTFW.setText(DataConversionProperties.getString("String_ExportingAsGeoTransformFile"));
        this.labelPassword.setText(DataConversionProperties.getString("string_label_lblPassword"));
        this.labelPasswordConfrim.setText(DataConversionProperties.getString("string_label_lblConfrimPassword"));
    }

    public void setUnEnabled() {
        this.textFieldCompressionRatio.setEnabled(false);
        this.prjFileChooser.setEnabled(false);
        this.checkBoxExportTFW.setEnabled(false);
        this.passwordField.setEnabled(false);
        this.passwordFieldConfrim.setEnabled(false);
    }

    public JTextField getTextFieldCompressionRatio() {
        return textFieldCompressionRatio;
    }

    public FileChooserControl getPrjFileChooser() {
        return prjFileChooser;
    }

    public TristateCheckBox getCheckBoxExportTFW() {
        return checkBoxExportTFW;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getPasswordFieldConfrim() {
        return passwordFieldConfrim;
    }
}
