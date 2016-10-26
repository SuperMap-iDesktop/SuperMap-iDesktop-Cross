package com.supermap.desktop.importUI;

import com.supermap.data.Point3D;
import com.supermap.data.conversion.*;
import com.supermap.desktop.baseUI.PanelTransform;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataconversion.DataConversionProperties;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by xie on 2016/10/11.
 * 三维模型(osgb,osg,3ds,.x,.dxf,.obj,.ifc,.fbx,.dae)导入参数设置界面
 */
public class PanelTransformFor3D extends PanelTransform {
    private ArrayList<PanelImport> panelImports;
    private JLabel labelRotationType;
    private JComboBox comboBoxRotationType;//旋转模式
    private JCheckBox checkBoxSplitForMore;//拆分为多个子对象
    private JRadioButton radioButtonPrjSet;//投影设置
    private JButton buttonPrjSet;
    private JRadioButton radioButtonImportPrjFile;//导入投影文件
    private FileChooserControl fileChooserControlImportPrjFile;
    private JLabel labelPositionX;
    private JTextField textFieldPositionX;//x坐标
    private JLabel labelPositionY;
    private JTextField textFieldPositionY;//y坐标
    private JLabel labelPositionZ;
    private JTextField textFieldPositionZ;//z坐标
    private JTextArea textAreaPrjInfo;//投影信息显示
    private ItemListener radioListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource().equals(radioButtonPrjSet)) {
                buttonPrjSet.setEnabled(radioButtonPrjSet.isSelected());
                radioButtonImportPrjFile.setSelected(!radioButtonPrjSet.isSelected());
                fileChooserControlImportPrjFile.setEnabled(!radioButtonPrjSet.isEnabled());
            } else {
                buttonPrjSet.setEnabled(!radioButtonImportPrjFile.isSelected());
                radioButtonPrjSet.setSelected(!radioButtonImportPrjFile.isSelected());
                fileChooserControlImportPrjFile.setEnabled(radioButtonPrjSet.isEnabled());
            }
        }
    };
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updatePosition(e);
        }

        private void updatePosition(DocumentEvent e) {
            String point3DX = textFieldPositionX.getText();
            String point3DY = textFieldPositionY.getText();
            String point3dZ = textFieldPositionZ.getText();
            if (null != panelImports && e.getDocument().equals(textFieldPositionX.getDocument())) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformFor3D) tempPanelImport.getTransform()).getTextFieldPositionX().setText(textFieldPositionX.getText());
                }
            } else if (null != panelImports && e.getDocument().equals(textFieldPositionY.getDocument())) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformFor3D) tempPanelImport.getTransform()).getTextFieldPositionY().setText(textFieldPositionY.getText());
                }
            } else if (null != panelImports && e.getDocument().equals(textFieldPositionZ.getDocument())) {
                for (PanelImport tempPanelImport : panelImports) {
                    ((PanelTransformFor3D) tempPanelImport.getTransform()).getTextFieldPositionZ().setText(textFieldPositionZ.getText());
                }
            } else {
                if (!StringUtilities.isNullOrEmpty(point3DX) && StringUtilities.isNumber(point3DX) &&
                        !StringUtilities.isNullOrEmpty(point3DY) && StringUtilities.isNumber(point3DY) &&
                        !StringUtilities.isNullOrEmpty(point3dZ) && StringUtilities.isNumber(point3dZ)) {
                    Point3D newPoint3D = new Point3D(Double.parseDouble(point3DX), Double.parseDouble(point3DY), Double.parseDouble(point3dZ));

                    if (importSetting instanceof ImportSettingModelOSG) {
                        ((ImportSettingModelOSG) importSetting).setPosition(newPoint3D);
                    }
                    if (importSetting instanceof ImportSettingModelX) {
                        ((ImportSettingModelX) importSetting).setPosition(newPoint3D);
                    }
                    if (importSetting instanceof ImportSettingModel3DS) {
                        ((ImportSettingModel3DS) importSetting).setPosition(newPoint3D);
                    }
                    if (importSetting instanceof ImportSettingModelDXF) {
                        ((ImportSettingModelDXF) importSetting).setPosition(newPoint3D);
                    }
                    if (importSetting instanceof ImportSettingModelFBX) {
                        ((ImportSettingModelFBX) importSetting).setPosition(newPoint3D);
                    }
                    if (importSetting instanceof ImportSettingModelFLT) {
                        ((ImportSettingModelFLT) importSetting).setPosition(newPoint3D);
                    }
                }
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    };

    public PanelTransformFor3D(ImportSetting importSetting) {
        super(importSetting);
        registEvents();
    }

    public PanelTransformFor3D(ArrayList<PanelImport> panelImports, int layoutType) {
        super(panelImports, layoutType);
        this.panelImports = panelImports;
        initLayerout();
        registEvents();
    }

    @Override
    public void initComponents() {
        this.labelRotationType = new JLabel();
        this.comboBoxRotationType = new JComboBox();
        this.checkBoxSplitForMore = new JCheckBox();
        this.radioButtonPrjSet = new JRadioButton();
        this.buttonPrjSet = new JButton();
        this.radioButtonImportPrjFile = new JRadioButton();
        this.fileChooserControlImportPrjFile = new FileChooserControl();
        this.labelPositionX = new JLabel();
        this.textFieldPositionX = new JTextField();
        this.labelPositionY = new JLabel();
        this.textFieldPositionY = new JTextField();
        this.labelPositionZ = new JLabel();
        this.textFieldPositionZ = new JTextField();
        this.textAreaPrjInfo = new JTextArea();
        this.textAreaPrjInfo.setEditable(false);
        initTextFiledPosition();
    }

    private void initTextFiledPosition() {
        if (importSetting instanceof ImportSettingModelOSG) {
            Point3D point = ((ImportSettingModelOSG) importSetting).getPosition();
            this.textFieldPositionX.setText(String.valueOf(point.getX()));
            this.textFieldPositionY.setText(String.valueOf(point.getY()));
            this.textFieldPositionZ.setText(String.valueOf(point.getZ()));
        }

        if (importSetting instanceof ImportSettingModelX) {
            Point3D point = ((ImportSettingModelX) importSetting).getPosition();
            this.textFieldPositionX.setText(String.valueOf(point.getX()));
            this.textFieldPositionY.setText(String.valueOf(point.getY()));
            this.textFieldPositionZ.setText(String.valueOf(point.getZ()));
        }
        if (importSetting instanceof ImportSettingModel3DS) {
            Point3D point = ((ImportSettingModel3DS) importSetting).getPosition();
            this.textFieldPositionX.setText(String.valueOf(point.getX()));
            this.textFieldPositionY.setText(String.valueOf(point.getY()));
            this.textFieldPositionZ.setText(String.valueOf(point.getZ()));
        }
        if (importSetting instanceof ImportSettingModelDXF) {
            Point3D point = ((ImportSettingModelDXF) importSetting).getPosition();
            this.textFieldPositionX.setText(String.valueOf(point.getX()));
            this.textFieldPositionY.setText(String.valueOf(point.getY()));
            this.textFieldPositionZ.setText(String.valueOf(point.getZ()));
        }
        if (importSetting instanceof ImportSettingModelFBX) {
            Point3D point = ((ImportSettingModelFBX) importSetting).getPosition();
            this.textFieldPositionX.setText(String.valueOf(point.getX()));
            this.textFieldPositionY.setText(String.valueOf(point.getY()));
            this.textFieldPositionZ.setText(String.valueOf(point.getZ()));
        }
        if (importSetting instanceof ImportSettingModelFLT) {
            Point3D point = ((ImportSettingModelFLT) importSetting).getPosition();
            this.textFieldPositionX.setText(String.valueOf(point.getX()));
            this.textFieldPositionY.setText(String.valueOf(point.getY()));
            this.textFieldPositionZ.setText(String.valueOf(point.getZ()));
        }
    }

    @Override
    public void initLayerout() {
        this.setLayout(new GridBagLayout());
        this.add(this.labelRotationType, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.comboBoxRotationType, new GridBagConstraintsHelper(2, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 30).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.radioButtonPrjSet, new GridBagConstraintsHelper(4, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.buttonPrjSet, new GridBagConstraintsHelper(6, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

        this.add(this.checkBoxSplitForMore, new GridBagConstraintsHelper(0, 1, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.radioButtonImportPrjFile, new GridBagConstraintsHelper(4, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.fileChooserControlImportPrjFile, new GridBagConstraintsHelper(6, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

        JPanel panelModel = new JPanel();
        panelModel.setLayout(new GridBagLayout());
        panelModel.setBorder(new TitledBorder(DataConversionProperties.getString("string_modelPoint")));
        panelModel.add(this.labelPositionX, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelModel.add(this.textFieldPositionX, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelModel.add(this.labelPositionY, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelModel.add(this.textFieldPositionY, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        panelModel.add(this.labelPositionZ, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 5, 5, 20).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        panelModel.add(this.textFieldPositionZ, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 5, 10).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));

        this.add(panelModel, new GridBagConstraintsHelper(0, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 0, 20).setFill(GridBagConstraints.BOTH).setWeight(0, 0));
        this.add(this.textAreaPrjInfo, new GridBagConstraintsHelper(4, 2, 4, 1).setAnchor(GridBagConstraints.WEST).setInsets(0, 0, 0, 10).setFill(GridBagConstraints.BOTH).setWeight(0, 0));
        this.comboBoxRotationType.setEnabled(false);
        this.radioButtonPrjSet.setSelected(true);
        this.fileChooserControlImportPrjFile.setEnabled(false);
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.radioButtonPrjSet.addItemListener(this.radioListener);
        this.radioButtonImportPrjFile.addItemListener(this.radioListener);
        this.textFieldPositionX.getDocument().addDocumentListener(this.documentListener);
        this.textFieldPositionY.getDocument().addDocumentListener(this.documentListener);
        this.textFieldPositionZ.getDocument().addDocumentListener(this.documentListener);
    }

    @Override
    public void removeEvents() {
    }

    @Override
    public void initResources() {
        this.setBorder(new TitledBorder(DataConversionProperties.getString("string_border_panelTransform")));
        this.labelRotationType.setText(DataConversionProperties.getString("string_rotationType"));
        this.labelPositionX.setText(DataConversionProperties.getString("string_label_lblx"));
        this.labelPositionY.setText(DataConversionProperties.getString("string_label_lbly"));
        this.labelPositionZ.setText(DataConversionProperties.getString("string_label_lblz"));
        this.checkBoxSplitForMore.setText(DataConversionProperties.getString("string_splitForMore"));
        this.radioButtonImportPrjFile.setText(DataConversionProperties.getString("string_importPrjFile"));
        this.radioButtonPrjSet.setText(ControlsProperties.getString("String_SetProjection_Caption"));
        this.buttonPrjSet.setText(ControlsProperties.getString("String_Button_Setting"));
    }

    public JTextField getTextFieldPositionX() {
        return textFieldPositionX;
    }

    public JTextField getTextFieldPositionY() {
        return textFieldPositionY;
    }

    public JTextField getTextFieldPositionZ() {
        return textFieldPositionZ;
    }
}
