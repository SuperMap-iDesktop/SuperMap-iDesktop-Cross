package com.supermap.desktop.importUI;

import com.supermap.data.conversion.*;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.localUtilities.CommonUtilities;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmFileChoose;
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
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/11.
 * bmp,png,gif,sit,tif,tiff,sid,ecw,img,jpk,jpg,jp2,jpeg等图像文件的转换参数设置界面
 * 多选时栅格数据集参数转换设置界面
 */
public class PanelTransformForImage extends PanelTransform {
    private static final int SAME_TYPE = 4;
    private static final int GRID_TYPE = 1;
    private static final Dimension defaultSize = new Dimension(36, 23);
    private JLabel labelBandImportModel;
    private JComboBox comboBoxBandImportModel;
    private JLabel labelPrjFile;
    private FileChooserControl chooserControlPrjFile;
    private TristateCheckBox checkBoxBuildImgPyramid;
    private JLabel labelPassWord;
    private JPasswordField passwordField;
    private final int SINGLEBAND = 0;
    private final int MULTIBAND = 1;
    private final int COMPOSITE = 2;
    public ArrayList<PanelImport> panelImports;
    private ItemListener multiBandImportModeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (null != panelImports) {
                for (PanelImport panelImport : panelImports) {
                    ((PanelTransformForImage) panelImport.getTransform()).getComboBoxBandImportModel().setSelectedItem(comboBoxBandImportModel.getSelectedItem());
                }
            } else if (importSetting instanceof ImportSettingIMG && null != ((ImportSettingIMG) importSetting).getMultiBandImportMode()) {
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
            if (null != panelImports) {
                for (PanelImport panelImport : panelImports) {
                    ((PanelTransformForImage) panelImport.getTransform()).getPasswordField().setText(password);
                }
            } else {
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

                if (CommonUtilities.isExtendsFile(worldFile)) {
                    setWorldFilePath(worldFile);
                }
            }
        }

        private void setWorldFilePath(String worldFile) {
            if (null != panelImports) {
                for (PanelImport panelImport : panelImports) {
                    ((PanelTransformForImage) panelImport.getTransform()).getChooserControlPrjFile().getEditor().setText(worldFile);
                }
            } else {
                if (importSetting instanceof ImportSettingBMP) {
                    ((ImportSettingBMP) importSetting).setWorldFilePath(worldFile);
                } else if (importSetting instanceof ImportSettingJPG) {
                    ((ImportSettingJPG) importSetting).setWorldFilePath(worldFile);
                } else if (importSetting instanceof ImportSettingPNG) {
                    ((ImportSettingPNG) importSetting).setWorldFilePath(worldFile);
                } else if (importSetting instanceof ImportSettingGIF) {
                    ((ImportSettingGIF) importSetting).setWorldFilePath(worldFile);
                }
            }
        }
    };
    private StateChangeListener checkBoxBuildImgPyramidListener = new StateChangeListener() {
        @Override
        public void stateChange(StateChangeEvent e) {
            if (null != panelImports) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformForImage) tempPanelImport.getTransform()).getCheckBoxBuildImgPyramid().setSelected(checkBoxBuildImgPyramid.isSelected());
                }
            } else {
                if (importSetting instanceof ImportSettingBMP) {
                    ((ImportSettingBMP) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                } else if (importSetting instanceof ImportSettingPNG) {
                    ((ImportSettingPNG) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                } else if (importSetting instanceof ImportSettingIMG) {
                    ((ImportSettingIMG) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                } else if (importSetting instanceof ImportSettingJPG) {
                    ((ImportSettingJPG) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                } else if (importSetting instanceof ImportSettingTIF) {
                    ((ImportSettingTIF) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                } else if (importSetting instanceof ImportSettingGIF) {
                    ((ImportSettingGIF) importSetting).setPyramidBuilt(checkBoxBuildImgPyramid.isSelected());
                }
            }
        }
    };

    public PanelTransformForImage(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformForImage(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        if (layoutType == SAME_TYPE) {
            initLayerout();
        } else if (layoutType == GRID_TYPE) {
            initGridLayout();
        }

        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelBandImportModel = new JLabel();
        this.comboBoxBandImportModel = new JComboBox();
        if (null == panelImports) {
            initComboBoxBandImportModel();
        }
        this.labelPrjFile = new JLabel();
        this.chooserControlPrjFile = new FileChooserControl();
        if (null == panelImports) {
            initChooserControlPrjFile();
        }
        this.labelPassWord = new JLabel();
        this.passwordField = new JPasswordField();
        this.checkBoxBuildImgPyramid = new TristateCheckBox();
        if (null == panelImports) {
            initPassWord();
        }
        if (null == panelImports) {
            initCheckboxState();
        }
    }
    @Override
    public void setComponentName() {
        super.setComponentName();
        ComponentUIUtilities.setName(this.labelBandImportModel, "PanelTransformForImage_labelBandImportModel");
        ComponentUIUtilities.setName(this.comboBoxBandImportModel, "PanelTransformForImage_comboBoxBandImportModel");
        ComponentUIUtilities.setName(this.labelPrjFile, "PanelTransformForImage_labelPrjFile");
        ComponentUIUtilities.setName(this.chooserControlPrjFile, "PanelTransformForImage_chooserControlPrjFile");
        ComponentUIUtilities.setName(this.checkBoxBuildImgPyramid, "PanelTransformForImage_checkBoxBuildImgPyramid");
        ComponentUIUtilities.setName(this.labelPassWord, "PanelTransformForImage_labelPassWord");
        ComponentUIUtilities.setName(this.passwordField, "PanelTransformForImage_passwordField");
    }
    private void initCheckboxState() {
        //bmp，png,img,jpg,tif,gif:是否自动建立影像金字塔
        if (importSetting instanceof ImportSettingBMP) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingBMP) importSetting).isPyramidBuilt());
        } else if (importSetting instanceof ImportSettingPNG) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingPNG) importSetting).isPyramidBuilt());
        } else if (importSetting instanceof ImportSettingIMG) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingIMG) importSetting).isPyramidBuilt());
        } else if (importSetting instanceof ImportSettingJPG) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingJPG) importSetting).isPyramidBuilt());
        } else if (importSetting instanceof ImportSettingTIF) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingTIF) importSetting).isPyramidBuilt());
        } else if (importSetting instanceof ImportSettingGIF) {
            checkBoxBuildImgPyramid.setSelected(((ImportSettingGIF) importSetting).isPyramidBuilt());
        }
    }

    private void initPassWord() {
        if (importSetting instanceof ImportSettingSIT && !StringUtilities.isNullOrEmpty(((ImportSettingSIT) importSetting).getPassword())) {
            passwordField.setText(((ImportSettingSIT) importSetting).getPassword());
        }
    }

    private void initChooserControlPrjFile() {
        if (importSetting instanceof ImportSettingBMP && !StringUtilities.isNullOrEmpty(((ImportSettingBMP) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingBMP) importSetting).getWorldFilePath());
        } else if (importSetting instanceof ImportSettingJPG && !StringUtilities.isNullOrEmpty(((ImportSettingJPG) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingJPG) importSetting).getWorldFilePath());
        } else if (importSetting instanceof ImportSettingPNG && !StringUtilities.isNullOrEmpty(((ImportSettingPNG) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingPNG) importSetting).getWorldFilePath());
        } else if (importSetting instanceof ImportSettingGIF && !StringUtilities.isNullOrEmpty(((ImportSettingGIF) importSetting).getWorldFilePath())) {
            chooserControlPrjFile.getEditor().setText(((ImportSettingGIF) importSetting).getWorldFilePath());
        }
    }

    private void initComboBoxBandImportModel() {
        this.comboBoxBandImportModel.setEditable(true);
        ((JTextField) this.comboBoxBandImportModel.getEditor().getEditorComponent()).setEditable(false);
        if (importSetting instanceof ImportSettingIMG && null != ((ImportSettingIMG) importSetting).getMultiBandImportMode()) {
            resetBandImportModel(((ImportSettingIMG) importSetting).isImportingAsGrid(), true, ((ImportSettingIMG) importSetting).getMultiBandImportMode());
            return;
        }
        if (importSetting instanceof ImportSettingTIF && null != ((ImportSettingTIF) importSetting).getMultiBandImportMode()) {
            resetBandImportModel(((ImportSettingTIF) importSetting).isImportingAsGrid(), true, ((ImportSettingTIF) importSetting).getMultiBandImportMode());
            return;
        }
        if (importSetting instanceof ImportSettingMrSID && null != ((ImportSettingMrSID) importSetting).getMultiBandImportMode()) {
            resetBandImportModel(((ImportSettingMrSID) importSetting).isImportingAsGrid(), false, ((ImportSettingMrSID) importSetting).getMultiBandImportMode());
            return;
        }
        if (importSetting instanceof ImportSettingECW && null != ((ImportSettingECW) importSetting).getMultiBandImportMode()) {
            resetBandImportModel(((ImportSettingECW) importSetting).isImportingAsGrid(), false, ((ImportSettingECW) importSetting).getMultiBandImportMode());
            return;
        }
    }

    private void resetBandImportModel(boolean isGrid, boolean isTiff, MultiBandImportMode multiBandImportMode) {
        if (isGrid && isTiff) {
            this.comboBoxBandImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_singleBand")}));
        } else if (isGrid && !isTiff) {
            this.comboBoxBandImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_singleBand"),
                    DataConversionProperties.getString("string_multiBand")}));
        } else {
            this.comboBoxBandImportModel.setModel(new DefaultComboBoxModel<Object>(new String[]{DataConversionProperties.getString("string_singleBand"),
                    DataConversionProperties.getString("string_multiBand"), DataConversionProperties.getString("string_compositeBand")}));
        }
        if (multiBandImportMode.equals(MultiBandImportMode.SINGLEBAND)) {
            this.comboBoxBandImportModel.setSelectedItem(DataConversionProperties.getString("string_singleBand"));
        } else if (multiBandImportMode.equals(MultiBandImportMode.MULTIBAND)) {
            this.comboBoxBandImportModel.setSelectedItem(DataConversionProperties.getString("string_multiBand"));
        } else if (multiBandImportMode.equals(MultiBandImportMode.COMPOSITE)) {
            this.comboBoxBandImportModel.setSelectedItem(DataConversionProperties.getString("string_compositeBand"));
        }
    }

    @Override
    public void initGridLayout() {
        this.setLayout(new GridBagLayout());

        this.add(this.labelPassWord, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 14).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.passwordField, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setIpad(80, 0));
        this.add(this.labelBandImportModel, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 6).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxBandImportModel, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 14).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.chooserControlPrjFile, new GridBagConstraintsHelper(2, 1, 6, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.passwordField.setEnabled(false);
        if (isComboBoxBandImportModelEnabled()) {
            initComboBoxBandImportModel();
            this.comboBoxBandImportModel.setEnabled(true);
            setBandImportModel();
        } else {
            this.comboBoxBandImportModel.setEnabled(false);
        }
        if (isChooserControlPrjFileEnabled()) {
            this.chooserControlPrjFile.setEnabled(true);
            setPrjText();
        } else {
            this.chooserControlPrjFile.setEnabled(false);
        }
    }

    private void setPrjText() {
        if (null != panelImports) {
            this.chooserControlPrjFile.getEditor().setText(getPrjFilePath());
        }
    }

    private void setBandImportModel() {
        if (null != panelImports) {
            this.comboBoxBandImportModel.setSelectedItem(selectedItem());
        }
    }

    private String getPrjFilePath() {
        String result = "";
        String temp = ((PanelTransformForImage) panelImports.get(0).getTransform()).getChooserControlPrjFile().getEditor().getText();
        boolean isSame = true;
        for (PanelImport tempPanel : panelImports) {
            String tempObject = ((PanelTransformForImage) tempPanel.getTransform()).getChooserControlPrjFile().getEditor().getText();
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

    private Object selectedItem() {
        Object result = null;
        String temp = "";
        Object info = ((PanelTransformForImage) panelImports.get(0).getTransform()).getComboBoxBandImportModel().getSelectedItem();
        if (null != info) {
            temp = info.toString();
        } else {
            result = temp;
            return result;
        }
        boolean isSame = true;
        for (PanelImport tempPanel : panelImports) {
            String tempObject = ((PanelTransformForImage) tempPanel.getTransform()).getComboBoxBandImportModel().getSelectedItem().toString();
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

    private boolean isChooserControlPrjFileEnabled() {
        //bmp,png,gif,jpk,jpg,jp2,jpeg
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            FileType tempFiletype = tempPanelImport.getImportInfo().getImportSetting().getSourceFileType();
            if (tempFiletype.equals(FileType.TIF) || tempFiletype.equals(FileType.BMP) || tempFiletype.equals(FileType.PNG)
                    || tempFiletype.equals(FileType.GIF) || tempFiletype.equals(FileType.JP2) || tempFiletype.equals(FileType.JPG)) {
                count++;
            }
        }
        return count == panelImports.size();
    }

    private boolean isComboBoxBandImportModelEnabled() {
        int count = 0;
        for (PanelImport tempPanelImport : panelImports) {
            FileType tempFiletype = tempPanelImport.getImportInfo().getImportSetting().getSourceFileType();
            if (tempFiletype.equals(FileType.TIF) || tempFiletype.equals(FileType.IMG) || tempFiletype.equals(FileType.MrSID) || tempFiletype.equals(FileType.ECW)) {
                count++;
            }
        }
        return count == panelImports.size();
    }

    @Override
    public void initLayerout() {
        //bmp,png,gif,sit,tif,tiff,sid,ecw,img,jpk,jpg,jp2,jpeg等图像文件的转换参数设置界面
        //img,tif,bmp,png,jpg,gif:是否自动建立影像金字塔
        this.removeAll();
        this.setLayout(new GridBagLayout());
        if (importSetting instanceof ImportSettingSIT) {
            this.add(this.labelPassWord, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 15).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.passwordField, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 120).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(new JPanel(), new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.passwordField.setPreferredSize(defaultSize);
            setPasswordField();
        } else if (importSetting instanceof ImportSettingTIF) {
            JPanel panelBandModel = new JPanel();
            panelBandModel.setLayout(new GridBagLayout());
            panelBandModel.add(this.labelBandImportModel, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            panelBandModel.add(this.comboBoxBandImportModel, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setIpad(-60, 0));
            panelBandModel.add(this.checkBoxBuildImgPyramid, new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

            JPanel panelPrjFile = new JPanel();
            panelPrjFile.setLayout(new GridBagLayout());
            this.setLayout(new GridBagLayout());
            panelPrjFile.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            panelPrjFile.add(this.chooserControlPrjFile, new GridBagConstraintsHelper(2, 0, 6, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

            this.add(panelBandModel, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(panelPrjFile, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setBandImportModel();
            setCheckboxState();
            setPrjText();
        } else if (importSetting instanceof ImportSettingIMG) {
            this.add(this.labelBandImportModel, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxBandImportModel, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 20).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setIpad(-60, 0));
            this.add(this.checkBoxBuildImgPyramid, new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setBandImportModel();
            setCheckboxState();
        } else if (importSetting instanceof ImportSettingMrSID || importSetting instanceof ImportSettingECW) {
            this.add(this.labelBandImportModel, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.comboBoxBandImportModel, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 176).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            this.add(new JPanel(), new GridBagConstraintsHelper(4, 0, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setBandImportModel();
        } else if (importSetting instanceof ImportSettingBMP || importSetting instanceof ImportSettingPNG
                || importSetting instanceof ImportSettingJPG || importSetting instanceof ImportSettingGIF) {
            //bmp，png,jpg,gif
            this.add(this.checkBoxBuildImgPyramid, new GridBagConstraintsHelper(0, 0, 8, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.chooserControlPrjFile, new GridBagConstraintsHelper(2, 1, 6, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setCheckboxState();
            setPrjText();
        } else {
            this.add(this.labelPrjFile, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
            this.add(this.chooserControlPrjFile, new GridBagConstraintsHelper(2, 0, 6, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
            setPrjText();
        }
    }

    private void setCheckboxState() {
        if (null != panelImports) {
            this.checkBoxBuildImgPyramid.setSelectedEx(externalDataSelectAll());
        }
    }

    private Boolean externalDataSelectAll() {
        Boolean result = null;
        int selectCount = 0;
        int unSelectCount = 0;
        for (PanelImport tempPanel : panelImports) {
            boolean select = ((PanelTransformForImage) tempPanel.getTransform()).getCheckBoxBuildImgPyramid().isSelected();
            if (select) {
                selectCount++;
            } else if (!select) {
                unSelectCount++;
            }
        }
        if (selectCount == panelImports.size()) {
            result = true;
        } else if (unSelectCount == panelImports.size()) {
            result = false;
        }
        return result;
    }

    private void setPasswordField() {
        if (null != panelImports) {
            this.passwordField.setText(getSamePassword());
        }
    }

    private String getSamePassword() {
        String result = "";
        String temp = ((PanelTransformForImage) panelImports.get(0).getTransform()).getPasswordField().getText();
        boolean isSame = true;
        for (PanelImport tempPanel : panelImports) {
            String tempObject = ((PanelTransformForImage) tempPanel.getTransform()).getPasswordField().getText();
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
    public void registEvents() {
        removeEvents();
        this.comboBoxBandImportModel.addItemListener(this.multiBandImportModeListener);
        this.passwordField.getDocument().addDocumentListener(this.passwordListener);
        this.chooserControlPrjFile.getButton().addActionListener(this.prjFileListener);
        this.checkBoxBuildImgPyramid.addStateChangeListener(this.checkBoxBuildImgPyramidListener);
    }

    @Override
    public void removeEvents() {
        this.comboBoxBandImportModel.removeItemListener(this.multiBandImportModeListener);
        this.passwordField.getDocument().removeDocumentListener(this.passwordListener);
        this.chooserControlPrjFile.getButton().removeActionListener(this.prjFileListener);
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelPassWord.setText(DataConversionProperties.getString("string_label_lblPassword") + "       ");
        this.labelPrjFile.setText(DataConversionProperties.getString("String_FormExport_LabelWorldFile"));
        this.labelBandImportModel.setText(DataConversionProperties.getString("string_label_lblSaveImport"));
        this.checkBoxBuildImgPyramid.setText(DataConversionProperties.getString("string_checkbox_chckbxImageInfo"));
    }

    public JComboBox getComboBoxBandImportModel() {
        return comboBoxBandImportModel;
    }

    public FileChooserControl getChooserControlPrjFile() {
        return chooserControlPrjFile;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public TristateCheckBox getCheckBoxBuildImgPyramid() {
        return checkBoxBuildImgPyramid;
    }
}
