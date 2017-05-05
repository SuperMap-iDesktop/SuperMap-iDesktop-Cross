package com.supermap.desktop.dialog.cacheClip;

import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.JFileChooserControl;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by xie on 2017/5/3.
 * Multi cache builder dialog
 */
public class DialogCacheBuilder extends SmDialog {
    private JLabel labelTaskPath;
    private JLabel labelWorkspacePath;
    private JLabel labelMapName;
    private JLabel labelProcessCount;
    private JLabel labelCachePath;
    private JLabel labelTotalProcessCount;
    private JLabel labelTotalProgress;
    private JLabel labelDetailProgressInfo;
    public JFileChooserControl fileChooserTaskPath;
    public JFileChooserControl fileChooserWorkspacePath;
    public JTextField textFieldMapName;
    private JTextField textFieldProcessCount;
    private JButton buttonApply;
    public JFileChooserControl fileChooserCachePath;
    private JTextField textFieldTotalProcessCount;
    private JProgressBar progressBarTotal;
    private JScrollPane scrollPaneProgresses;
    private JButton buttonCreate;
    private JButton buttonClose;
    private JButton buttonRefresh;
    private WarningOrHelpProvider helpProviderForTaskPath;
    private WarningOrHelpProvider helpProviderForProcessCount;

    public DialogCacheBuilder() {
        init();
    }

    private void init() {
        initComponents();
        initResources();
        initLayout();
        registEvents();
        Dimension dimension = new Dimension(723, 365);
        this.setSize(dimension);
        this.setLocationRelativeTo(null);
        this.componentList.add(this.buttonCreate);
        this.componentList.add(this.buttonClose);
        this.setFocusTraversalPolicy(policy);
    }

    private void initComponents() {
        this.labelTaskPath = new JLabel();
        this.labelWorkspacePath = new JLabel();
        this.labelMapName = new JLabel();
        this.labelCachePath = new JLabel();
        this.labelTotalProcessCount = new JLabel();
        this.labelProcessCount = new JLabel();
        this.labelTotalProgress = new JLabel();
        this.labelDetailProgressInfo = new JLabel();
        String moduleName = "ChooseTaskDirectories";
        if (!SmFileChoose.isModuleExist(moduleName)) {
            SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleName, "OpenOne");
        }

        SmFileChoose fileChoose = new SmFileChoose(moduleName);
        this.fileChooserTaskPath = new JFileChooserControl();
        this.fileChooserTaskPath.setFileChooser(fileChoose);
        this.fileChooserTaskPath.setPath(System.getProperty("user.dir"));
        String newModuleName = "ChooseWorkspaceDirectories";
        if (!SmFileChoose.isModuleExist(moduleName)) {
            SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleName, "OpenOne");
        }

        SmFileChoose fileChooser = new SmFileChoose(newModuleName);
        this.fileChooserWorkspacePath = new JFileChooserControl();
        this.fileChooserWorkspacePath.setFileChooser(fileChooser);
        this.fileChooserWorkspacePath.setPath(System.getProperty("user.dir"));
        this.textFieldMapName = new JTextField();
        this.textFieldProcessCount = new JTextField();
        this.buttonApply = ComponentFactory.createButtonApply();
        String moduleNameForCachePath = "ChooseWorkspaceDirectories";
        if (!SmFileChoose.isModuleExist(moduleNameForCachePath)) {
            SmFileChoose.addNewNode("", System.getProperty("user.dir"), GlobalParameters.getDesktopTitle(),
                    moduleNameForCachePath, "OpenOne");
        }
        SmFileChoose fileChooserForCachePath = new SmFileChoose(moduleNameForCachePath);
        this.fileChooserCachePath = new JFileChooserControl();
        this.fileChooserCachePath.setFileChooser(fileChooserForCachePath);
        this.textFieldTotalProcessCount = new JTextField();
        this.progressBarTotal = new JProgressBar();
        this.scrollPaneProgresses = new JScrollPane();
        this.buttonRefresh = new SmButton();
        this.buttonCreate = new SmButton();
        this.buttonClose = ComponentFactory.createButtonClose();
        this.helpProviderForTaskPath = new WarningOrHelpProvider(MapViewProperties.getString("String_SciFilePath"), false);
        this.helpProviderForProcessCount = new WarningOrHelpProvider(MapViewProperties.getString("String_HelpForProcessCount"), false);
    }

    private void initResources() {
        this.setTitle(MapViewProperties.getString("String_MultiProcessClipMapCache"));
        this.labelTaskPath.setText(MapViewProperties.getString("String_TaskPath"));
        this.labelWorkspacePath.setText(MapViewProperties.getString("String_WorkspacePath"));
        this.labelMapName.setText(MapViewProperties.getString("String_Label_MapName"));
        this.labelProcessCount.setText(MapViewProperties.getString("String_ProcessCount"));
        this.labelCachePath.setText(MapViewProperties.getString("String_CachePath"));
        this.labelTotalProcessCount.setText(MapViewProperties.getString("String_TotalProcessCount"));
        this.labelTotalProgress.setText(ControlsProperties.getString("String_ProgressControl_TotalProgress"));
        this.labelDetailProgressInfo.setText(MapViewProperties.getString("String_DetailProcessInfo"));
        this.buttonCreate.setText(MapViewProperties.getString("String_BatchAddColorTableOKButton"));
        this.buttonRefresh.setText(ControlsProperties.getString("String_Refresh"));
    }


    private void initLayout() {
        JPanel panelClip = new JPanel();
        panelClip.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_MultiProcessClip")));
        panelClip.setLayout(new GridBagLayout());
        panelClip.add(this.labelTaskPath, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
        panelClip.add(this.helpProviderForTaskPath,new GridBagConstraintsHelper(1,0,1,1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5,0,5,10));
        panelClip.add(this.fileChooserTaskPath, new GridBagConstraintsHelper(2, 0, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
        panelClip.add(this.labelWorkspacePath, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelClip.add(this.fileChooserWorkspacePath, new GridBagConstraintsHelper(2, 1, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelClip.add(this.labelMapName, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelClip.add(this.textFieldMapName, new GridBagConstraintsHelper(2, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelClip.add(this.labelProcessCount, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelClip.add(this.helpProviderForProcessCount,new GridBagConstraintsHelper(1,3,1,1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0,0,5,10));
        panelClip.add(this.textFieldProcessCount, new GridBagConstraintsHelper(2, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 5).setWeight(1, 0));
        panelClip.add(this.buttonApply, new GridBagConstraintsHelper(4, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
        panelClip.add(this.labelCachePath, new GridBagConstraintsHelper(0, 4, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelClip.add(this.fileChooserCachePath, new GridBagConstraintsHelper(2, 4, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelClip.add(new JPanel(), new GridBagConstraintsHelper(0, 5, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(1, 1));

        JPanel panelClipProgress = new JPanel();
        panelClipProgress.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_ClipBuilderProgress")));
        panelClipProgress.setLayout(new GridBagLayout());
        panelClipProgress.add(this.labelTotalProcessCount, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(5, 10, 5, 10));
        panelClipProgress.add(this.textFieldTotalProcessCount, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 0, 5, 10).setWeight(1, 0));
        panelClipProgress.add(this.labelTotalProgress, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelClipProgress.add(this.progressBarTotal, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 0, 5, 10).setWeight(1, 0));
        panelClipProgress.add(this.buttonRefresh, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 5, 10));
        panelClipProgress.add(this.labelDetailProgressInfo, new GridBagConstraintsHelper(0, 2, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(0, 10, 5, 10));
        panelClipProgress.add(this.scrollPaneProgresses, new GridBagConstraintsHelper(0, 3, 3, 5).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(0, 10, 5, 10).setWeight(1, 1));

        JPanel panelContent = (JPanel) this.getContentPane();
        panelContent.setLayout(new GridBagLayout());
        panelContent.add(panelClip, new GridBagConstraintsHelper(0, 0, 4, 4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 5, 10).setWeight(1, 1));
        panelContent.add(panelClipProgress, new GridBagConstraintsHelper(4, 0, 4, 4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 5, 10).setWeight(1, 1));
        panelContent.add(this.buttonCreate, new GridBagConstraintsHelper(6, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(1, 0));
        panelContent.add(this.buttonClose, new GridBagConstraintsHelper(7, 4, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(0, 0, 10, 10).setWeight(0, 0));

    }

    private void registEvents() {

    }
}
