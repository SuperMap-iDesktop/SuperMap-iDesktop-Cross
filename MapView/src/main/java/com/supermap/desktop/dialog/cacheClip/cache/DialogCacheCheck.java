package com.supermap.desktop.dialog.cacheClip.cache;

import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.ComponentBorderPanel.CompTitledPane;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2017/5/10.
 */
public class DialogCacheCheck extends SmDialog {
    private JLabel labelTotalSciPath;
    private JLabel labelCheckBounds;
    private JLabel labelSciPath;
    private JLabel labelProcessCount;
    private JLabel labelMergeSciCount;
    private JFileChooserControl fileChooseTotalSciPath;
    private JFileChooserControl fileChooseCheckBounds;
    private JFileChooserControl fileChooseSciPath;
    private JTextField textFieldProcessCount;
    private JTextField textFieldMergeSciCount;
    private JRadioButton radioButtonSingleCheck;
    private JRadioButton radioButtonMultiCheck;
    private JCheckBox checkBoxSaveErrorData;
    private JCheckBox checkBoxCacheBuild;
    private JButton buttonOK;
    private JButton buttonCancel;

    public DialogCacheCheck() {
        super();
        init();
    }

    private void init() {
        initComponents();
        initResources();
        initLayout();
    }

    private void initComponents() {
        this.labelTotalSciPath = new JLabel();
        this.labelCheckBounds = new JLabel();
        this.labelSciPath = new JLabel();
        this.labelProcessCount = new JLabel();
        this.labelMergeSciCount = new JLabel();
        String moduleNameForCachePath = "ChooseCachePath";
        if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
            SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleNameForCachePath, "GetDirectories");
        }
        SmFileChoose fileChooserForCachePath = new SmFileChoose(moduleNameForCachePath);
        this.fileChooseTotalSciPath = new JFileChooserControl();
        this.fileChooseTotalSciPath.setFileChooser(fileChooserForCachePath);

        String moduleNameForCheckBounds = "ChooseCheckBoundsFile";
        if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
            String fileFilters = SmFileChoose.bulidFileFilters(SmFileChoose.createFileFilter(MapViewProperties.getString("String_FileType_GeoJson"), "geojson"));
            SmFileChoose.addNewNode(fileFilters, System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleNameForCachePath, "OpenOne");
        }
        SmFileChoose fileChooserForCheckBounds = new SmFileChoose(moduleNameForCachePath);
        this.fileChooseCheckBounds = new JFileChooserControl();
        this.fileChooseCheckBounds.setFileChooser(fileChooserForCheckBounds);
        String moduleNameForSciPath = "ChooseSciPath";
        if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
            SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleNameForCachePath, "GetDirectories");
        }
        SmFileChoose fileChooserForSciPath = new SmFileChoose(moduleNameForSciPath);
        this.fileChooseSciPath = new JFileChooserControl();
        this.fileChooseSciPath.setFileChooser(fileChooserForSciPath);
        this.textFieldProcessCount = new JTextField();
        this.textFieldMergeSciCount = new JTextField();
        this.radioButtonSingleCheck = new JRadioButton();
        this.radioButtonMultiCheck = new JRadioButton();
        this.checkBoxSaveErrorData = new JCheckBox();
        this.checkBoxCacheBuild = new JCheckBox();
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
    }

    private void initResources() {
        this.labelTotalSciPath.setText(MapViewProperties.getString("String_CachePath"));
        this.labelCheckBounds.setText(MapViewProperties.getString("String_CheckBounds"));
        this.labelSciPath.setText(MapViewProperties.getString("String_SciPath"));
        this.labelProcessCount.setText(MapViewProperties.getString("String_NewProcessCount"));
        this.labelMergeSciCount.setText(MapViewProperties.getString("String_MergeSciCount"));
        this.textFieldProcessCount.setText("3");
        this.textFieldMergeSciCount.setText("0");
        this.radioButtonSingleCheck.setText(MapViewProperties.getString("String_CheckCache_Single"));
        this.radioButtonMultiCheck.setText(MapViewProperties.getString("String_CheckCache_Multi"));
        this.checkBoxSaveErrorData.setText(MapViewProperties.getString("String_SaveErrorData"));
        this.checkBoxCacheBuild.setText(MapViewProperties.getString("String_ClipErrorCache"));
        this.setTitle(MapViewProperties.getString(""));
    }

    private void initLayout() {
        JPanel panelContent = (JPanel) this.getContentPane();
        panelContent.setLayout(new GridBagLayout());
        JPanel panelMultiCheck = new JPanel();
        panelMultiCheck.setLayout(new GridBagLayout());
        panelMultiCheck.add(this.labelSciPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
        panelMultiCheck.add(this.fileChooseSciPath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
        panelMultiCheck.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelMultiCheck.add(this.textFieldProcessCount, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelMultiCheck.add(this.labelMergeSciCount, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelMultiCheck.add(this.textFieldMergeSciCount, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        CompTitledPane compTitledPane = new CompTitledPane(this.radioButtonMultiCheck, panelMultiCheck);
        panelContent.add(this.labelTotalSciPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
        panelContent.add(this.fileChooseTotalSciPath, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
        panelContent.add(this.labelCheckBounds, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelContent.add(this.fileChooseCheckBounds, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelContent.add(this.radioButtonSingleCheck, new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelContent.add(compTitledPane, new GridBagConstraintsHelper(1, 3, 3, 3).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 3));

    }
}
