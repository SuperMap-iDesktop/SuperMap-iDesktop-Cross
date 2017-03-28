package com.supermap.desktop.dialog;

import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Unit;
import com.supermap.data.processing.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.ScaleModel;
import com.supermap.desktop.mapview.MapCache.CacheProgressCallable;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.map.propertycontrols.PanelGroupBoxViewBounds;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.SMSpinner;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxItem;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxTableHeaderCellRenderer;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxTableRenderer;
import com.supermap.desktop.ui.controls.ChooseTable.SmChooseTable;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.ProviderLabel.WarningOrHelpProvider;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.SmFileChoose;
import com.supermap.desktop.ui.controls.TextFields.WaringTextField;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.mutiTable.DDLExportTableModel;
import com.supermap.desktop.ui.controls.mutiTable.component.MutiTable;
import com.supermap.desktop.ui.controls.progress.FormProgress;
import com.supermap.desktop.utilities.Convert;
import com.supermap.desktop.utilities.CoreResources;
import com.supermap.desktop.utilities.DoubleUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.tilestorage.TileStorageConnection;
import com.supermap.tilestorage.TileStorageType;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by lixiaoyao on 2017/3/15.
 */
public class DialogMapCacheBuilder extends SmDialog {
    //  Master Control
    private JTabbedPane tabbedPane;
    private JPanel panelBasicSetting;
    private JPanel panelRangeParameter;
    private JPanel panelImageSave;
    private JCheckBox autoCloseDialog;
    private JCheckBox showProgressBar;
    private JCheckBox mutiProcessCache;
    private SmButton buttonOK;
    private SmButton buttonCancel;
    private SmButton buttonSetting;

    //  Scale Panel Control
    private BackgroundMenuBar menuBar;
    private JMenu jMenuAddScale;
    private JMenu jMenuSelectAll;
    private JMenu jMenuSelectInverse;
    private JMenu jMenuDelete;
    private JMenu jMenuImport;
    private JMenu jMenuExport;
    private JMenuItem jMenuItemAddScale;
    private JMenuItem jMenuItemDefaultScale;
    private JMenuItem jMenuItemImportCacheConfigs;
    private JMenuItem jMenuItemImportScale;
    private JMenuItem jMenuItemExportCacheConfigs;
    private JMenuItem jMenuItemExportScale;
    private JScrollPane scrollPane;
    private MutiTable localSplitTable;
    private SmChooseTable globalSplitTable;

    // BasicSetting Panel Control
    private JLabel labelVersion;
    private JLabel labelSplitMode;
    private JLabel labelConfig;
    private JLabel labelConfigValue;
    private JComboBox comboboxVersion;
    private JComboBox comboBoxSplitMode;
    private JCheckBox checkBoxUpdataOrAddCacheFile;
    private JCheckBox checkBoxGoOnOrRecoveryCacheFile;
    private JLabel labelCacheName;
    private JLabel labelCachePath;
    private WarningOrHelpProvider warningProviderCacheNameIllegal;
    private WarningOrHelpProvider warningProviderCachePathIllegal;
    private JTextField textFieldCacheName;
    private FileChooserControl fileChooserControlFileCache;

    // Parameter setting  Control
    private PanelGroupBoxViewBounds panelCacheRange;
    private PanelGroupBoxViewBounds panelIndexRange;
    private WaringTextField cacheRangeWaringTextFieldLeft;
    private WaringTextField cacheRangeWaringTextFieldTop;
    private WaringTextField cacheRangeWaringTextFieldRight;
    private WaringTextField cacheRangeWaringTextFieldBottom;
    private WaringTextField indexRangeWaringTextFieldLeft;
    private WaringTextField indexRangeWaringTextFieldTop;
    private WaringTextField indexRangeWaringTextFieldRight;
    private WaringTextField indexRangeWaringTextFieldBottom;

    // Image Svae Panel Control
    private JLabel labelImageType;
    private JLabel labelPixel;
    private JLabel labelImageCompressionRatio;
    private JLabel labelSaveType;
    private JLabel labelUserName;
    private JLabel labelUserPassword;
    private JLabel labelConfirmPassword;
    private JLabel labelServerName;
    private JLabel labelDatabaseName;
    private JLabel labelMutiTenseVersion;
    private WarningOrHelpProvider helpProvider;
    private WarningOrHelpProvider warningProviderPasswordNotSame;
    private JCheckBox checkBoxBackgroundTransparency;
    private JCheckBox checkBoxFullFillCacheImage;
    private JCheckBox checkBoxFilterSelectionObjectInLayer;
    private JComboBox comboBoxImageType;
    private JComboBox comboBoxPixel;
    private JComboBox comboBoxSaveType;
    private JComboBox comboBoxDatabaseName;
    private JComboBox comboBoxMutiTenseVersion;
    private JTextField textFieldUserName;
    private JPasswordField textFieldUserPassword;
    private JPasswordField textFieldConfirmPassword;
    private JTextField textFieldServerName;
    private SMSpinner smSpinnerImageCompressionRatio;

    private final String urlStr = "/coreresources/ToolBar/";
    private final int universalWidth = 200;
    private final int minSize = 20;
    private final int COLUMN_INDEX = 0;
    private final int COLUMN_SCALE = 1;
    private final int COLUMN_TITLE = 2;
    private MapCacheBuilder mapCacheBuilder = new MapCacheBuilder();
    java.text.NumberFormat scientificNotation = java.text.NumberFormat.getInstance();
    private double originMapCacheScale[];
    private HashMap<Double, String> globalSplitScale = new HashMap<>();
    private ArrayList<Double> currentMapCacheScale;
    private static final String showScalePrePart = "1:";
    private static final String colon = ":";
    private String[] globalSplitTableTitle = {"", MapViewProperties.getString("MapCache_Scale")};
    private Rectangle2D cacheRangeBounds = null;
    private Rectangle2D indexRangeBounds = null;
    private Double[] globalScaleSortKeys;
    private Mongo mongo = new Mongo();
    private boolean mongoDBConnectSate = true;
    private boolean textFieldServerNameGetFocus = false;
    private volatile Socket socket;
    private static final String defaultAddress = "localhost";
    private static final int defaultPort = 27017;
    private boolean validCacheRangeBounds = true;
    private boolean validIndexRangeBounds = true;

    public DialogMapCacheBuilder(JFrame owner, boolean model) {
        super(owner, model);
        initComponents();
        initLayout();
        initLocalSplitTable();
        initResouces();
        initGlobalValue();
        initBasicSettingPanelValue();
        initPanelImageSaveValue();
        removeEvents();
        registEvents();
    }

    private void initComponents() {
        this.tabbedPane = new JTabbedPane();
        this.panelBasicSetting = new JPanel();
        this.panelRangeParameter = new JPanel();
        this.panelImageSave = new JPanel();
        this.autoCloseDialog = new JCheckBox();
        this.showProgressBar = new JCheckBox();
        this.mutiProcessCache = new JCheckBox();
        this.buttonOK = new SmButton();
        this.buttonCancel = new SmButton();
        this.buttonSetting = new SmButton();


        this.scrollPane = new JScrollPane();
        this.menuBar = new BackgroundMenuBar();
        this.jMenuAddScale = new JMenu();
        this.jMenuSelectAll = new JMenu();
        this.jMenuSelectInverse = new JMenu();
        this.jMenuDelete = new JMenu();
        this.jMenuImport = new JMenu();
        this.jMenuExport = new JMenu();
        this.jMenuItemAddScale = new JMenuItem(MapViewProperties.getString("MapCache_AddScale"), CoreResources.getIcon(urlStr + "Image_ToolButton_AddScale.png"));
        this.jMenuItemDefaultScale = new JMenuItem(MapViewProperties.getString("MapCache_DefaultScale"), CoreResources.getIcon(urlStr + "Image_ToolButton_DefaultScale.png"));
        this.jMenuItemImportCacheConfigs = new JMenuItem(MapViewProperties.getString("MapCache_ImportCacheConfigs"), CoreResources.getIcon(urlStr + "Image_ToolButton_Export.png"));
        this.jMenuItemImportScale = new JMenuItem(MapViewProperties.getString("MapCache_ImportScale"), CoreResources.getIcon(urlStr + "Image_ToolButton_DefaultScale.png"));
        this.jMenuItemExportCacheConfigs = new JMenuItem(MapViewProperties.getString("MapCache_ExportCacheConfig"), CoreResources.getIcon(urlStr + "Image_ToolButton_Export.png"));
        this.jMenuItemExportScale = new JMenuItem(MapViewProperties.getString("MapCache_ExportScale"), CoreResources.getIcon(urlStr + "Image_ToolButton_DefaultScale.png"));

        this.labelVersion = new JLabel();
        this.labelSplitMode = new JLabel();
        this.labelConfig = new JLabel();
        this.labelConfigValue = new JLabel();
        this.comboboxVersion = new JComboBox();
        this.comboBoxSplitMode = new JComboBox();
        this.checkBoxUpdataOrAddCacheFile = new JCheckBox();
        this.checkBoxGoOnOrRecoveryCacheFile = new JCheckBox();
        this.labelCacheName = new JLabel();
        this.labelCachePath = new JLabel();
        this.warningProviderCacheNameIllegal = new WarningOrHelpProvider(MapViewProperties.getString("MapCache_WarningCacheNameIsEmpty"), true);
        this.warningProviderCachePathIllegal = new WarningOrHelpProvider(MapViewProperties.getString("MapCache_WarningCachePathIsEmpty"), true);
        this.textFieldCacheName = new JTextField();
        this.fileChooserControlFileCache = new FileChooserControl();

        this.labelImageType = new JLabel();
        this.labelPixel = new JLabel();
        this.labelImageCompressionRatio = new JLabel();
        this.labelSaveType = new JLabel();
        this.labelUserName = new JLabel();
        this.labelUserPassword = new JLabel();
        this.labelConfirmPassword = new JLabel();
        this.labelServerName = new JLabel();
        this.labelDatabaseName = new JLabel();
        this.labelMutiTenseVersion = new JLabel();
        this.helpProvider = new WarningOrHelpProvider(MapViewProperties.getString("MapCache_ServeNameHelp"), false);
        this.warningProviderPasswordNotSame = new WarningOrHelpProvider(MapViewProperties.getString("MapCache_PasswordIsNotSame"), true);
        this.checkBoxBackgroundTransparency = new JCheckBox();
        this.checkBoxFullFillCacheImage = new JCheckBox();
        this.checkBoxFilterSelectionObjectInLayer = new JCheckBox();
        this.comboBoxImageType = new JComboBox();
        this.comboBoxPixel = new JComboBox();
        this.comboBoxSaveType = new JComboBox();
        this.comboBoxDatabaseName = new JComboBox();
        this.comboBoxMutiTenseVersion = new JComboBox();
        this.textFieldUserName = new JTextField();
        this.textFieldUserPassword = new JPasswordField();
        this.textFieldConfirmPassword = new JPasswordField();
        this.textFieldServerName = new JTextField();
        this.smSpinnerImageCompressionRatio = new SMSpinner(new SpinnerNumberModel(this.mapCacheBuilder.getImageCompress(), 0, 100, 1));
    }

    private void initLayout() {
        Dimension dimension = new Dimension(752, 520);
        setSize(dimension);
        setMinimumSize(dimension);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(this.buttonOK);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(this.menuBar)
                                        .addComponent(this.scrollPane, 343, 343, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addComponent(this.tabbedPane, 362, 363, Short.MAX_VALUE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(this.autoCloseDialog)
                                .addComponent(this.showProgressBar)
                                .addComponent(this.mutiProcessCache)
                                .addGap(10, 10, Short.MAX_VALUE)
                                .addComponent(this.buttonSetting)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                                .addComponent(this.buttonOK)
                                .addComponent(this.buttonCancel)
                        ))
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(this.menuBar, 30, 30, 30)
                                .addComponent(this.scrollPane))
                        .addComponent(this.tabbedPane))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(this.autoCloseDialog)
                        .addComponent(this.showProgressBar)
                        .addComponent(this.mutiProcessCache)
                        .addComponent(this.buttonSetting)
                        .addComponent(this.buttonOK)
                        .addComponent(this.buttonCancel))
        );

        this.jMenuAddScale.add(this.jMenuItemAddScale);
        this.jMenuAddScale.add(this.jMenuItemDefaultScale);
        this.jMenuImport.add(this.jMenuItemImportCacheConfigs);
        this.jMenuImport.add(this.jMenuItemImportScale);
        this.jMenuExport.add(this.jMenuItemExportCacheConfigs);
        this.jMenuExport.add(this.jMenuItemExportScale);
        this.menuBar.add(this.jMenuAddScale);
        this.menuBar.add(this.jMenuSelectAll);
        this.menuBar.add(this.jMenuSelectInverse);
        this.menuBar.add(this.jMenuDelete);
        this.menuBar.add(this.jMenuImport);
        this.menuBar.add(this.jMenuExport);
        this.menuBar.setColor(this.getBackground());

        this.localSplitTable = new MutiTable();
        this.scrollPane.setViewportView(this.localSplitTable);

        this.tabbedPane.add(MapViewProperties.getString("MapCache_BasicSetting"), panelBasicSetting);
        this.tabbedPane.add(MapViewProperties.getString("MapCache_RangeParameter"), panelRangeParameter);
        this.tabbedPane.add(MapViewProperties.getString("MapCache_ImageSave"), panelImageSave);
        getContentPane().setLayout(groupLayout);
        initPanelBasicSetting();
        initPanelImageSave();
        initPanelRangeParameter();
    }

    private void initPanelBasicSetting() {
        JPanel outputSetting = new JPanel();
        outputSetting.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("MapCache_OutputSetting")));
        JPanel pathSetting = new JPanel();
        pathSetting.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("MapCache_PathSetting")));

        GroupLayout groupLayoutOutPutSetting = new GroupLayout(outputSetting);
        groupLayoutOutPutSetting.setAutoCreateContainerGaps(true);
        groupLayoutOutPutSetting.setAutoCreateGaps(true);
        groupLayoutOutPutSetting.setHorizontalGroup(groupLayoutOutPutSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayoutOutPutSetting.createSequentialGroup()
                        .addGroup(groupLayoutOutPutSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.labelVersion)
                                .addComponent(this.labelSplitMode))
                        .addGap(30)
                        .addGroup(groupLayoutOutPutSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.comboboxVersion, universalWidth, universalWidth, Short.MAX_VALUE)
                                .addComponent(this.comboBoxSplitMode, universalWidth, universalWidth, Short.MAX_VALUE)))
                .addComponent(this.checkBoxUpdataOrAddCacheFile)
                .addComponent(this.checkBoxGoOnOrRecoveryCacheFile)
                .addGroup(groupLayoutOutPutSetting.createSequentialGroup()
                        .addComponent(this.labelConfig)
                        .addComponent(this.labelConfigValue)
                )
        );
        groupLayoutOutPutSetting.setVerticalGroup(groupLayoutOutPutSetting.createSequentialGroup()
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelVersion)
                        .addComponent(this.comboboxVersion, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelSplitMode)
                        .addComponent(this.comboBoxSplitMode, minSize, minSize, minSize))
                .addComponent(this.checkBoxUpdataOrAddCacheFile)
                .addComponent(this.checkBoxGoOnOrRecoveryCacheFile)
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelConfig)
                        .addComponent(this.labelConfigValue))
        );
        outputSetting.setLayout(groupLayoutOutPutSetting);

        GroupLayout groupLayoutPathSetting = new GroupLayout(pathSetting);
        groupLayoutPathSetting.setAutoCreateContainerGaps(true);
        groupLayoutPathSetting.setAutoCreateGaps(true);
        groupLayoutPathSetting.setHorizontalGroup(groupLayoutPathSetting.createSequentialGroup()
                .addGroup(groupLayoutPathSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.labelCacheName)
                        .addComponent(this.labelCachePath))
                .addGroup(groupLayoutPathSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.warningProviderCacheNameIllegal, minSize, minSize, minSize)
                        .addComponent(this.warningProviderCachePathIllegal, minSize, minSize, minSize))
                .addGroup(groupLayoutPathSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.textFieldCacheName, universalWidth, universalWidth, Short.MAX_VALUE)
                        .addComponent(this.fileChooserControlFileCache, universalWidth, universalWidth, Short.MAX_VALUE)
                )
        );
        groupLayoutPathSetting.setVerticalGroup(groupLayoutPathSetting.createSequentialGroup()
                .addGroup(groupLayoutPathSetting.createParallelGroup()
                        .addComponent(this.labelCacheName)
                        .addComponent(this.warningProviderCacheNameIllegal, minSize, minSize, minSize)
                        .addComponent(this.textFieldCacheName, minSize, minSize, minSize))
                .addGroup(groupLayoutPathSetting.createParallelGroup()
                        .addComponent(this.labelCachePath)
                        .addComponent(this.warningProviderCachePathIllegal, minSize, minSize, minSize)
                        .addComponent(this.fileChooserControlFileCache, minSize, minSize, minSize)
                )
        );
        pathSetting.setLayout(groupLayoutPathSetting);

        GroupLayout groupLayoutPanelBasic = new GroupLayout(this.panelBasicSetting);
        groupLayoutPanelBasic.setHorizontalGroup(groupLayoutPanelBasic.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(outputSetting)
                .addComponent(pathSetting)
        );
        groupLayoutPanelBasic.setVerticalGroup(groupLayoutPanelBasic.createSequentialGroup()
                .addComponent(outputSetting)
                .addComponent(pathSetting)
        );
        this.panelBasicSetting.setLayout(groupLayoutPanelBasic);
    }

    private void initPanelImageSave() {
        JPanel panelImageParameter = new JPanel();
        panelImageParameter.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("MapCache_ImageParameter")));
        JPanel panelOutPutSetting = new JPanel();
        panelOutPutSetting.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("MapCache_OutputSetting")));

        GroupLayout groupLayoutImageParameter = new GroupLayout(panelImageParameter);
        groupLayoutImageParameter.setAutoCreateGaps(true);
        groupLayoutImageParameter.setAutoCreateContainerGaps(true);
        groupLayoutImageParameter.setHorizontalGroup(groupLayoutImageParameter.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayoutImageParameter.createSequentialGroup()
                        .addGroup(groupLayoutImageParameter.createParallelGroup()
                                .addComponent(this.labelImageType)
                                .addComponent(this.labelPixel)
                                .addComponent(this.labelImageCompressionRatio))
                        .addGroup(groupLayoutImageParameter.createParallelGroup()
                                .addComponent(this.comboBoxImageType, universalWidth, universalWidth, Short.MAX_VALUE)
                                .addComponent(this.comboBoxPixel, universalWidth, universalWidth, Short.MAX_VALUE)
                                .addComponent(this.smSpinnerImageCompressionRatio, universalWidth, universalWidth, Short.MAX_VALUE)))
                .addComponent(this.checkBoxBackgroundTransparency)
                .addComponent(this.checkBoxFullFillCacheImage)
        );
        groupLayoutImageParameter.setVerticalGroup(groupLayoutImageParameter.createSequentialGroup()
                .addGroup(groupLayoutImageParameter.createParallelGroup()
                        .addComponent(this.labelImageType)
                        .addComponent(this.comboBoxImageType, minSize, minSize, minSize))
                .addGroup(groupLayoutImageParameter.createParallelGroup()
                        .addComponent(this.labelPixel)
                        .addComponent(this.comboBoxPixel, minSize, minSize, minSize))
                .addGroup(groupLayoutImageParameter.createParallelGroup()
                        .addComponent(this.labelImageCompressionRatio)
                        .addComponent(this.smSpinnerImageCompressionRatio, minSize, minSize, minSize))
                .addComponent(this.checkBoxBackgroundTransparency)
                .addComponent(this.checkBoxFullFillCacheImage)
        );
        panelImageParameter.setLayout(groupLayoutImageParameter);

        GroupLayout groupLayoutOutPutSetting = new GroupLayout(panelOutPutSetting);
        groupLayoutOutPutSetting.setAutoCreateContainerGaps(true);
        groupLayoutOutPutSetting.setAutoCreateGaps(true);
        groupLayoutOutPutSetting.setHorizontalGroup(groupLayoutOutPutSetting.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayoutOutPutSetting.createSequentialGroup()
                        .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                                .addComponent(this.labelSaveType)
                                .addGroup(groupLayoutOutPutSetting.createSequentialGroup()
                                        .addComponent(this.labelServerName)
                                        .addComponent(this.helpProvider, minSize, minSize, minSize))
                                .addComponent(this.labelDatabaseName)
                                .addComponent(this.labelUserName)
                                .addComponent(this.labelUserPassword)
                                .addComponent(this.labelMutiTenseVersion)
                                .addGroup(groupLayoutOutPutSetting.createSequentialGroup()
                                        .addComponent(this.labelConfirmPassword)
                                        .addComponent(this.warningProviderPasswordNotSame, minSize, minSize, minSize)
                                ))
                        .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                                .addComponent(this.comboBoxSaveType)
                                .addComponent(this.textFieldServerName)
                                .addComponent(this.comboBoxDatabaseName)
                                .addComponent(this.textFieldUserName)
                                .addComponent(this.textFieldUserPassword)
                                .addComponent(this.comboBoxMutiTenseVersion)
                                .addComponent(this.textFieldConfirmPassword)))
                .addComponent(this.checkBoxFilterSelectionObjectInLayer)
        );
        groupLayoutOutPutSetting.setVerticalGroup(groupLayoutOutPutSetting.createSequentialGroup()
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelSaveType)
                        .addComponent(this.comboBoxSaveType, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelServerName)
                        .addComponent(this.helpProvider, minSize, minSize, minSize)
                        .addComponent(this.textFieldServerName, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelDatabaseName)
                        .addComponent(this.comboBoxDatabaseName, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelUserName)
                        .addComponent(this.textFieldUserName, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelUserPassword)
                        .addComponent(this.textFieldUserPassword, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelMutiTenseVersion)
                        .addComponent(this.comboBoxMutiTenseVersion, minSize, minSize, minSize))
                .addGroup(groupLayoutOutPutSetting.createParallelGroup()
                        .addComponent(this.labelConfirmPassword)
                        .addComponent(this.warningProviderPasswordNotSame, minSize, minSize, minSize)
                        .addComponent(this.textFieldConfirmPassword, minSize, minSize, minSize))
                .addComponent(this.checkBoxFilterSelectionObjectInLayer)
        );
        panelOutPutSetting.setLayout(groupLayoutOutPutSetting);

        GroupLayout groupLayoutImageSave = new GroupLayout(this.panelImageSave);
        groupLayoutImageSave.setHorizontalGroup(groupLayoutImageSave.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelImageParameter)
                .addComponent(panelOutPutSetting)
        );
        groupLayoutImageSave.setVerticalGroup(groupLayoutImageSave.createSequentialGroup()
                .addComponent(panelImageParameter)
                .addComponent(panelOutPutSetting)
        );
        this.panelImageSave.setLayout(groupLayoutImageSave);
    }

    private void initPanelRangeParameter() {
        this.panelCacheRange = new PanelGroupBoxViewBounds(this, MapViewProperties.getString("MapCache_CacheRange"));
        this.panelIndexRange = new PanelGroupBoxViewBounds(this, MapViewProperties.getString("MapCache_IndexRange"));
        GroupLayout groupLayoutImageSave = new GroupLayout(this.panelRangeParameter);
        groupLayoutImageSave.setHorizontalGroup(groupLayoutImageSave.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(this.panelCacheRange)
                .addComponent(this.panelIndexRange)
        );
        groupLayoutImageSave.setVerticalGroup(groupLayoutImageSave.createSequentialGroup()
                .addComponent(panelCacheRange)
                .addComponent(panelIndexRange)
        );
        this.panelRangeParameter.setLayout(groupLayoutImageSave);
        this.cacheRangeWaringTextFieldLeft = this.panelCacheRange.getTextFieldCurrentViewLeft();
        this.cacheRangeWaringTextFieldTop = this.panelCacheRange.getTextFieldCurrentViewTop();
        this.cacheRangeWaringTextFieldRight = this.panelCacheRange.getTextFieldCurrentViewRight();
        this.cacheRangeWaringTextFieldBottom = this.panelCacheRange.getTextFieldCurrentViewBottom();
        this.indexRangeWaringTextFieldLeft = this.panelIndexRange.getTextFieldCurrentViewLeft();
        this.indexRangeWaringTextFieldTop = this.panelIndexRange.getTextFieldCurrentViewTop();
        this.indexRangeWaringTextFieldRight = this.panelIndexRange.getTextFieldCurrentViewRight();
        this.indexRangeWaringTextFieldBottom = this.panelIndexRange.getTextFieldCurrentViewBottom();
        this.comboBoxDatabaseName.setEditable(true);
    }

    private void initResouces() {
        this.autoCloseDialog.setText(MapViewProperties.getString("MapCache_AutoCloseDailog"));
        this.showProgressBar.setText(MapViewProperties.getString("MapCache_ShowProgressBar"));
        this.mutiProcessCache.setText(MapViewProperties.getString("MapCache_MutiProcessCache"));
        this.buttonSetting.setText(MapViewProperties.getString("MapCache_Setting"));
        this.buttonOK.setText(MapViewProperties.getString("String_BatchAddColorTableOKButton"));
        this.buttonCancel.setText(MapViewProperties.getString("String_BatchAddColorTableCancelButton"));


        this.jMenuAddScale.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_AddScale.png"));
        this.jMenuAddScale.setToolTipText(MapViewProperties.getString("MapCache_AddScale"));
        this.jMenuSelectAll.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectAll.png"));
        this.jMenuSelectAll.setToolTipText(CommonProperties.getString("String_ToolBar_SelectAll"));
        this.jMenuSelectInverse.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_SelectInverse.png"));
        this.jMenuSelectInverse.setToolTipText(CommonProperties.getString("String_ToolBar_SelectInverse"));
        this.jMenuDelete.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_Delete.png"));
        this.jMenuDelete.setToolTipText(CommonProperties.getString("String_Delete"));
        this.jMenuImport.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_Import.png"));
        this.jMenuImport.setToolTipText(MapViewProperties.getString("MapCache_Import"));
        this.jMenuExport.setIcon(CoreResources.getIcon(urlStr + "Image_ToolButton_Export.png"));
        this.jMenuExport.setToolTipText(MapViewProperties.getString("MapCache_Export"));
        this.labelVersion.setText(MapViewProperties.getString("MapCache_LabelVersion"));
        this.labelSplitMode.setText(MapViewProperties.getString("MapCache_LabelSplitMode"));
        this.labelConfig.setText(MapViewProperties.getString("MapCache_LabelConfig"));
        this.labelConfigValue.setText(MapViewProperties.getString("MapCache_LabelConfigValue"));
        this.labelCacheName.setText(MapViewProperties.getString("MapCache_LabelCacheName"));
        this.labelCachePath.setText(MapViewProperties.getString("MapCache_LabelCachePath"));
        this.checkBoxUpdataOrAddCacheFile.setText(MapViewProperties.getString("MapCache_UpdataOrAddCacheFile"));
        this.checkBoxGoOnOrRecoveryCacheFile.setText(MapViewProperties.getString("MapCache_GoOnOrRecoveryCacheFile"));

        this.labelImageType.setText(MapViewProperties.getString("MapCache_ImageType"));
        this.labelPixel.setText(MapViewProperties.getString("MapCache_Pixel"));
        this.labelImageCompressionRatio.setText(MapViewProperties.getString("MapCache_ImageCompressionRation"));
        this.labelSaveType.setText(MapViewProperties.getString("MapCache_SaveType"));
        this.labelUserName.setText(MapViewProperties.getString("MapCache_UserName"));
        this.labelUserPassword.setText(MapViewProperties.getString("MapCache_UserPassword"));
        this.labelConfirmPassword.setText(MapViewProperties.getString("MapCache_ConfirmPassword"));
        this.labelServerName.setText(MapViewProperties.getString("MapCache_ServerName"));
        this.labelDatabaseName.setText(MapViewProperties.getString("MapCache_DatabaseName"));
        this.labelMutiTenseVersion.setText(MapViewProperties.getString("MapCache_LabelVersion"));
        this.checkBoxBackgroundTransparency.setText(MapViewProperties.getString("MapCache_BackgroundTransparency"));
        this.checkBoxFullFillCacheImage.setText(MapViewProperties.getString("MapCache_FullFillCacheImage"));
        this.checkBoxFilterSelectionObjectInLayer.setText(MapViewProperties.getString("MapCache_FilterSelectObjectInLayer"));

    }

    private void initGlobalValue() {
        IFormMap formMap = (IFormMap) Application.getActiveApplication().getActiveForm();
        this.mapCacheBuilder.setMap(formMap.getMapControl().getMap());
        this.setTitle(MapViewProperties.getString("MapCache_Title") + "(" + formMap.getMapControl().getMap().getName() + ")");
        this.scientificNotation.setGroupingUsed(false);
        this.originMapCacheScale = this.mapCacheBuilder.getDefultOutputScales();
        setLocalSplitTableValue(this.originMapCacheScale);
        this.currentMapCacheScale = new ArrayList<>();
        for (int i = 0; i < this.originMapCacheScale.length; i++) {
            this.currentMapCacheScale.add(this.originMapCacheScale[i]);
        }
        this.mutiProcessCache.setEnabled(false);
        this.buttonSetting.setEnabled(false);
        this.showProgressBar.setSelected(true);
        this.autoCloseDialog.setSelected(true);
        this.cacheRangeBounds = formMap.getMapControl().getMap().getBounds();
        this.indexRangeBounds = formMap.getMapControl().getMap().getBounds();
        this.mapCacheBuilder.setBounds(this.cacheRangeBounds);
        this.mapCacheBuilder.setIndexBounds(this.indexRangeBounds);
    }

    private void initBasicSettingPanelValue() {
        this.comboboxVersion.addItem(MapViewProperties.getString("MapCache_ComboboxVersionItem"));
        this.comboboxVersion.setEnabled(true);
        this.comboBoxSplitMode.addItem(MapViewProperties.getString("MapCache_ComboboxSplitModeLocalSplit"));
        this.comboBoxSplitMode.addItem(MapViewProperties.getString("MapCache_ComboboxSplitModeGlobalSplit"));
        this.checkBoxUpdataOrAddCacheFile.setEnabled(false);
        this.checkBoxGoOnOrRecoveryCacheFile.setEnabled(false);
        this.textFieldCacheName.setText(this.mapCacheBuilder.getMap().getName());
        this.fileChooserControlFileCache.getEditor().setText(CommonProperties.getString("String_DefaultFilePath"));
        this.warningProviderCacheNameIllegal.hideWarning();
        this.warningProviderCachePathIllegal.hideWarning();
    }

    private void initPanelImageSaveValue() {
        this.comboBoxImageType.addItem("PNG");
        this.comboBoxImageType.addItem("DXTZ");
        this.comboBoxImageType.addItem("GIF");
        this.comboBoxImageType.addItem("JPG");
        this.comboBoxImageType.addItem("JPG_PNG");
        this.comboBoxImageType.addItem("PNG8");
        this.comboBoxImageType.setSelectedItem("JPG");
        this.comboBoxPixel.addItem("64*64");
        this.comboBoxPixel.addItem("128*128");
        this.comboBoxPixel.addItem("256*256");
        this.comboBoxPixel.addItem("512*512");
        this.comboBoxPixel.addItem("1024*1024");
        //this.comboBoxPixel.addItem("2048*2048");
        this.comboBoxPixel.setSelectedItem("256*256");
        this.checkBoxBackgroundTransparency.setEnabled(false);
        this.checkBoxFullFillCacheImage.setEnabled(false);

        this.comboBoxSaveType.addItem(MapViewProperties.getString("MapCache_SaveType_Compact"));
        this.comboBoxSaveType.addItem(MapViewProperties.getString("MapCache_SaveType_Origin"));
        this.comboBoxSaveType.addItem(MapViewProperties.getString("MapCache_SaveType_MongoDB"));
        //this.comboBoxSaveType.addItem(MapViewProperties.getString("MapCache_SaveType_MongoDBMuti"));
        this.comboBoxSaveType.addItem(MapViewProperties.getString("MapCache_SaveType_GeoPackage"));
        this.comboBoxSaveType.setSelectedItem(MapViewProperties.getString("MapCache_SaveType_Origin"));
        initPanelImageSaveOutputDisplay(MapViewProperties.getString("MapCache_SaveType_Origin"));
        this.checkBoxFilterSelectionObjectInLayer.setEnabled(false);

        this.mapCacheBuilder.setTileFormat(TileFormat.JPG);
        this.mapCacheBuilder.setTileSize(TileSize.SIZE256);
        this.mapCacheBuilder.setStorageType(StorageType.Original);
    }

    private void initPanelImageSaveOutputDisplay(String currentShowItem) {
        if (currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_Compact")) || currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_Origin")) || currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_GeoPackage"))) {
            this.labelServerName.setVisible(false);
            this.helpProvider.setVisible(false);
            this.textFieldServerName.setVisible(false);
            this.labelDatabaseName.setVisible(false);
            this.comboBoxDatabaseName.setVisible(false);
            this.labelUserName.setVisible(false);
            this.textFieldUserName.setVisible(false);
            this.labelMutiTenseVersion.setVisible(false);
            this.comboBoxMutiTenseVersion.setVisible(false);

            this.labelUserPassword.setVisible(true);
            this.labelConfirmPassword.setVisible(true);
            this.warningProviderPasswordNotSame.setVisible(true);
            this.warningProviderPasswordNotSame.hideWarning();
            this.textFieldUserPassword.setVisible(true);
            this.textFieldConfirmPassword.setVisible(true);
            this.textFieldUserPassword.setText("");
            this.textFieldConfirmPassword.setText("");
            if (currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_Compact"))) {
                this.textFieldUserPassword.setEnabled(true);
                this.textFieldConfirmPassword.setEnabled(true);
                this.mapCacheBuilder.setStorageType(StorageType.Compact);
            } else {
                this.textFieldUserPassword.setEnabled(false);
                this.textFieldConfirmPassword.setEnabled(false);
                if (currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_Origin"))) {
                    this.mapCacheBuilder.setStorageType(StorageType.Original);
                } else if (currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_GeoPackage"))) {
                    this.mapCacheBuilder.setStorageType(StorageType.GPKG);
                }
            }

        } else if (currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_MongoDB")) || currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_MongoDBMuti"))) {
            this.labelServerName.setVisible(true);
            this.helpProvider.setVisible(true);
            this.textFieldServerName.setVisible(true);
            this.textFieldServerName.setText(MapViewProperties.getString("MapCache_MongoDB_DefaultServerName"));
            this.labelDatabaseName.setVisible(true);
            this.comboBoxDatabaseName.setVisible(true);
            this.comboBoxDatabaseName.removeAllItems();
            this.labelUserName.setVisible(true);
            this.textFieldUserName.setVisible(true);
            this.textFieldUserName.setText("");
            this.textFieldUserPassword.setEnabled(true);
            this.textFieldUserPassword.setText("");
            this.labelConfirmPassword.setVisible(false);
            this.warningProviderPasswordNotSame.setVisible(false);
            this.textFieldConfirmPassword.setVisible(false);

            if (currentShowItem.equals(MapViewProperties.getString("MapCache_SaveType_MongoDBMuti"))) {
                this.labelMutiTenseVersion.setVisible(true);
                this.comboBoxMutiTenseVersion.setVisible(true);
                this.comboBoxMutiTenseVersion.removeAllItems();
            } else {
                this.labelMutiTenseVersion.setVisible(false);
                this.comboBoxMutiTenseVersion.setVisible(false);
                this.mapCacheBuilder.setStorageType(StorageType.MongoDB);
            }
        }
    }

    private void initLocalSplitTable() {
        DDLExportTableModel tableModel = new DDLExportTableModel(new String[]{"index", "scale", "title"}) {
            boolean[] columnEditables = new boolean[]{false, true, true};

            @Override
            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        this.localSplitTable.setModel(tableModel);
        this.localSplitTable.setRowHeight(23);
        this.localSplitTable.getColumnModel().getColumn(COLUMN_INDEX).setMaxWidth(40);
        this.localSplitTable.getColumnModel().getColumn(COLUMN_TITLE).setMaxWidth(100);
        this.localSplitTable.getColumnModel().getColumn(COLUMN_INDEX).setHeaderValue("");
        this.localSplitTable.getColumnModel().getColumn(COLUMN_SCALE).setHeaderValue(MapViewProperties.getString("MapCache_Scale"));
        this.localSplitTable.getColumnModel().getColumn(COLUMN_TITLE).setHeaderValue(MapViewProperties.getString("MapCache_ScaleTitle"));
    }

    private void setLocalSplitTableValue(double value[]) {
        try {
            for (int i = 0; i < value.length; i++) {
                Object[] temp = new Object[3];
                temp[COLUMN_INDEX] = i + 1 + " ";
                temp[COLUMN_SCALE] = this.showScalePrePart + String.valueOf(this.scientificNotation.format(1 / value[i]));
                temp[COLUMN_TITLE] = (int) (Math.round(1 / value[i])) + " ";
                this.localSplitTable.addRow(temp);
            }
            addMoreScale();
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex.toString());
        }
    }

    private Object[][] getNewLocalSplitTableValue(double value[]) {
        Object[][] result = new Object[value.length][3];
        for (int i = 0; i < value.length; i++) {
            result[i][COLUMN_INDEX] = i + 1 + " ";
            result[i][COLUMN_SCALE] = this.showScalePrePart + String.valueOf(this.scientificNotation.format(1 / value[i]));
            result[i][COLUMN_TITLE] = (int) (Math.round(1 / value[i])) + " ";
        }
        return result;
    }

    // 初始化全球缓存的比例尺
    private void initGlobalCacheScales() {
        int[] levels = new int[21]; //将全球剖分增加为21层
        for (int i = 0; i < 21; i++) {
            levels[i] = i;
        }
        this.globalSplitScale = this.mapCacheBuilder.globalLevelToScale(levels);
        double avaliableScale = -1;
        if (!this.mapCacheBuilder.getBounds().isEmpty() && this.mapCacheBuilder.getMap().getPrjCoordSys().getGeoCoordSys() != null) {
            avaliableScale = GetAvaliableScale();
        }
        double dValue = Double.MAX_VALUE;
        String avaliableLevel = "0";
        if (avaliableScale != -1) {
            Object[] keys = this.globalSplitScale.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                double temp = Double.valueOf(keys[i].toString());
                double currentDValue = Math.abs(avaliableScale - (1.0 / temp));
                if (currentDValue < dValue) {
                    dValue = currentDValue;
                    avaliableLevel = this.globalSplitScale.get(temp);
                }
            }
        }
        initGlobalSplitTable();
        setGlobalSplitTableValue(avaliableLevel);
    }

    private void initGlobalSplitTable() {
        boolean[] columnEditables = new boolean[]{false, false};
        if (this.globalSplitTable == null) {
            this.globalSplitTable = new SmChooseTable(new Object[this.globalSplitScale.size()][2], globalSplitTableTitle, columnEditables);
        }
        this.globalSplitTable.getColumn(globalSplitTable.getModel().getColumnName(COLUMN_INDEX)).setMaxWidth(60);
        this.globalSplitTable.getColumnModel().getColumn(COLUMN_INDEX).setCellRenderer(new MultipleCheckboxTableRenderer());
        this.globalSplitTable.getTableHeader().getColumnModel().getColumn(COLUMN_INDEX).setHeaderRenderer(new MultipleCheckboxTableHeaderCellRenderer(this.globalSplitTable, MapViewProperties.getString("MapCache_LayerSeries"), true));
    }

    private void setGlobalSplitTableValue(String avaliableLevel) {
        try {
            this.globalScaleSortKeys = this.globalSplitScale.keySet().toArray(new Double[this.globalSplitScale.size()]);
            double temp;
            for (int i = 0; i < globalScaleSortKeys.length - 1; i++) {
                for (int j = 0; j < globalScaleSortKeys.length - i - 1; j++) {
                    if (Double.compare(globalScaleSortKeys[j], globalScaleSortKeys[j + 1]) == 1) {
                        temp = globalScaleSortKeys[j];
                        globalScaleSortKeys[j] = globalScaleSortKeys[j + 1];
                        globalScaleSortKeys[j + 1] = temp;
                    }
                }
            }
            boolean isSelected = false;
            for (int i = 0; i < globalScaleSortKeys.length; i++) {
                temp = globalScaleSortKeys[i];
                MultipleCheckboxItem multipleCheckboxItem = new MultipleCheckboxItem();
                multipleCheckboxItem.setText(this.globalSplitScale.get(temp));
                if (this.globalSplitScale.get(temp).equals(avaliableLevel)) {
                    isSelected = true;
                }
                multipleCheckboxItem.setSelected(isSelected);
                this.globalSplitTable.setValueAt(multipleCheckboxItem, i, 0);
                this.globalSplitTable.setValueAt(this.showScalePrePart + String.valueOf(this.scientificNotation.format(1 / temp)), i, COLUMN_SCALE);
            }
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex.toString());
        }
    }

    private double GetAvaliableScale() {
        try {
            double DPI = mapCacheBuilder.getMap().getDPI();
            double lp = DPI / 25.4;
            Integer tileSize = Convert.toInteger(mapCacheBuilder.getTileSize().value());
            double dLPSide = tileSize / lp * 10;
            Unit mapUnit = mapCacheBuilder.getMap().getCoordUnit();
            Rectangle2D rcFinalBounds = mapCacheBuilder.getMap().getBounds();
            double dLPMPCoordRatio = dLPSide / rcFinalBounds.getWidth();
            double dScale = 0;
            double axis = mapCacheBuilder.getMap().getPrjCoordSys().getGeoCoordSys().getGeoDatum().getGeoSpheroid().getAxis();
            if (mapUnit == Unit.DEGREE) {
                dScale = dLPMPCoordRatio / (axis * 10000 * Math.PI / 180);
            } else {
                dScale = dLPMPCoordRatio / (Convert.toDouble(mapUnit));
            }
            return 1.0 / dScale;
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex.toString());
            return 1.0;
        }
    }

    private void addMoreScale() {
        Object[] temp = new Object[3];
        temp[COLUMN_INDEX] = "*";
        temp[COLUMN_SCALE] = "";
        temp[COLUMN_TITLE] = "";
        this.localSplitTable.addRow(temp);
    }

    private void isCanRun() {
        if (isRightCacheName() && isRightCachePath() && isAllRightSaveTypeSetting() && isRightRangeBounds()) {
            this.buttonOK.setEnabled(true);
        } else {
            this.buttonOK.setEnabled(false);
        }
    }

    private boolean isRightCacheName() {
        boolean result = true;
        if (this.textFieldCacheName.getText().isEmpty()) {
            result = false;
            this.warningProviderCacheNameIllegal.showWarning();
        } else {
            this.warningProviderCacheNameIllegal.hideWarning();
        }
        return result;
    }

    private boolean isRightCachePath() {
        boolean result = true;
        if (this.fileChooserControlFileCache.getEditor().getText().isEmpty()) {
            result = false;
            this.warningProviderCachePathIllegal.showWarning();
            return result;
        } else {
            File file = new File(this.fileChooserControlFileCache.getEditor().getText());
            if (!file.exists() && !file.isDirectory()) {
                result = false;
                this.warningProviderCachePathIllegal.showWarning(MapViewProperties.getString("MapCache_WarningCachePathIsNotExits"));
                return result;
                //file.mkdir();
            } else {
                this.warningProviderCachePathIllegal.hideWarning();
            }
        }
        return result;
    }

    private boolean isRightRangeBounds() {
        boolean result = true;
        if (!this.validIndexRangeBounds || !this.validCacheRangeBounds) {
            result = false;
        }
        return result;
    }

    private boolean isAllRightSaveTypeSetting() {
        boolean result = true;
        if (this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_Compact"))) {
            result = isPasswordSame();
        } else if (this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_Origin")) || this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_GeoPackage"))) {

        } else if (this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDB")) || this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDBMuti"))) {
            if (this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDB"))) {
                if (textFieldServerNameGetFocus || !this.mongoDBConnectSate || this.comboBoxDatabaseName.getEditor().getItem().toString().isEmpty()) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    private boolean isPasswordSame() {
        boolean result = true;
        if (this.textFieldConfirmPassword.getText().isEmpty() && this.textFieldUserPassword.getText().isEmpty()) {
            this.warningProviderPasswordNotSame.hideWarning();
            return result;
        }
        if (this.textFieldConfirmPassword.isVisible() && this.textFieldConfirmPassword.isEnabled() && !this.textFieldConfirmPassword.getText().isEmpty()) {
            if (!this.textFieldConfirmPassword.getText().equals(this.textFieldUserPassword.getText())) {
                result = false;
                this.warningProviderPasswordNotSame.showWarning();
            } else {
                this.warningProviderPasswordNotSame.hideWarning();
            }
        }
        return result;
    }

    private void run() {
        try {
            double[] outputScalevalues;
            HashMap<Double, String> scaleNames = new HashMap<>();
            if (this.jMenuAddScale.isEnabled()) {
                outputScalevalues = new double[this.currentMapCacheScale.size()];
                for (int i = 0; i < outputScalevalues.length; i++) {
                    outputScalevalues[i] = this.currentMapCacheScale.get(i);
                    String scaleTitleName = String.valueOf(this.localSplitTable.getValueAt(i, COLUMN_TITLE));
                    scaleNames.put(outputScalevalues[i], scaleTitleName);
                }
            } else {
                ArrayList<Integer> selectedIndex = new ArrayList<>();
                for (int i = 0; i < this.globalSplitTable.getRowCount(); i++) {
                    MultipleCheckboxItem multipleCheckboxItem = (MultipleCheckboxItem) this.globalSplitTable.getValueAt(i, COLUMN_INDEX);
                    if (multipleCheckboxItem.getSelected()) {
                        selectedIndex.add(i);
                    }
                }
                outputScalevalues = new double[selectedIndex.size()];
                for (int i = 0; i < selectedIndex.size(); i++) {
                    outputScalevalues[i] = this.globalScaleSortKeys[selectedIndex.get(i)];
                    scaleNames.put(this.globalScaleSortKeys[selectedIndex.get(i)], this.globalSplitScale.get(this.globalScaleSortKeys[selectedIndex.get(i)]));
                }
            }
            this.mapCacheBuilder.setOutputScales(outputScalevalues);
            this.mapCacheBuilder.setOutputScaleCaptions(scaleNames);
            this.mapCacheBuilder.setVersion(MapCacheVersion.VERSION_50);
            this.mapCacheBuilder.setCacheName(this.textFieldCacheName.getText());
            this.mapCacheBuilder.setOutputFolder(this.fileChooserControlFileCache.getEditor().getText());
            this.mapCacheBuilder.setBounds(this.cacheRangeBounds);
            if (this.mapCacheBuilder.getTilingMode() == MapTilingMode.LOCAL) {
                this.mapCacheBuilder.setIndexBounds(this.indexRangeBounds);
            }
            this.mapCacheBuilder.setImageCompress(Integer.valueOf(this.smSpinnerImageCompressionRatio.getValue().toString()));
            this.mapCacheBuilder.setTransparent(this.checkBoxBackgroundTransparency.isSelected());
            TileStorageConnection tileStorageConnection = null;
            if (this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_Compact")) && !this.textFieldUserPassword.getText().isEmpty()) {
                this.mapCacheBuilder.setPassword(this.textFieldUserPassword.getText());
            } else if (this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDB")) || this.comboBoxSaveType.getSelectedItem().toString().equals(MapViewProperties.getString("MapCache_SaveType_MongoDBMuti"))) {
                tileStorageConnection = new TileStorageConnection();
                tileStorageConnection.setServer(this.textFieldServerName.getText());
                tileStorageConnection.setName(this.textFieldCacheName.getText());
                tileStorageConnection.setDatabase(this.comboBoxDatabaseName.getEditor().getItem().toString());
                tileStorageConnection.setStorageType(TileStorageType.MONGOV2);
                tileStorageConnection.setUser(null);
                tileStorageConnection.setPassword(null);
            }
            if (tileStorageConnection != null) {
                this.mapCacheBuilder.setConnectionInfo(tileStorageConnection);
            }
            boolean result = true;
            long startTime = System.currentTimeMillis();
            Date currentTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(currentTime);
            Application.getActiveApplication().getOutput().output(dateNowStr + " " + MapViewProperties.getString("MapCache_StartCreateCache"));
            if (this.showProgressBar.isSelected()) {
                FormProgress formProgress = new FormProgress();
                formProgress.setTitle(MapViewProperties.getString("MapCache_On") + this.getTitle());
                CacheProgressCallable cacheProgressCallable = new CacheProgressCallable(this.mapCacheBuilder);
                formProgress.doWork(cacheProgressCallable);
                result = cacheProgressCallable.getResult();
            } else {
                result = this.mapCacheBuilder.build();
            }
            long endTime = System.currentTimeMillis();
            String time = String.valueOf((endTime - startTime) / 1000.0);
            if (result) {
                Application.getActiveApplication().getOutput().output("\"" + this.mapCacheBuilder.getMap().getName() + "\"" + MapViewProperties.getString("MapCache_StartCreateSuccessed"));
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_FloderIs") + " " + this.fileChooserControlFileCache.getEditor().getText());
            } else {
                Application.getActiveApplication().getOutput().output("\"" + this.mapCacheBuilder.getMap().getName() + "\"" + MapViewProperties.getString("MapCache_StartCreateFailed"));
            }
            Application.getActiveApplication().getOutput().output(MapViewProperties.getString("MapCache_Time") + time + " " + MapViewProperties.getString("MapCache_ShowTime"));
            if (this.autoCloseDialog.isSelected()) {
                cancelAndCloseDailog();
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e.toString());
        }
    }

    private void cancelAndCloseDailog() {
        this.dispose();
    }

    // 非认证模式下连接mongodb，并读取数据库列表进行显示
    private void connectMongoDBPretreatment() {
        if (!this.textFieldServerName.getText().isEmpty()) {
            try {
                String address = "";
                Integer port = 27017;
                if (this.textFieldServerName.getText().indexOf(colon) != -1) {
                    String[] temp = this.textFieldServerName.getText().split(colon);
                    if (!temp[0].isEmpty() && !temp[1].isEmpty() && temp[1].matches("[0-9]+")) {
                        address = temp[0];
                        port = Integer.valueOf(temp[1]);
                    } else {
                        this.comboBoxDatabaseName.removeAllItems();
                        return;
                    }
                } else {
                    address = this.textFieldServerName.getText();
                }
                connectMongoDB(address, port);
            } catch (Exception ex) {
                this.comboBoxDatabaseName.removeAllItems();
                return;
            }
        } else {
            connectMongoDB(defaultAddress, defaultPort);
            this.textFieldServerName.setText(MapViewProperties.getString("MapCache_MongoDB_DefaultServerName"));
        }
    }

    private void connectMongoDB(String address, Integer port) {
        try {
            ServerAddress serverAddress = new ServerAddress(address, port);
            this.socket = new Socket();
            socket.connect(serverAddress.getSocketAddress(), 300);
        } catch (Exception e) {
            this.mongoDBConnectSate = false;
            this.comboBoxDatabaseName.removeAllItems();
            return;
        }
        Mongo.Holder holder = new Mongo.Holder();
        this.mongo = holder.connect(new MongoClientURI("mongodb://" + address));
        java.util.List<String> databaseNames = mongo.getDatabaseNames();
        this.comboBoxDatabaseName.removeAllItems();
        this.mongoDBConnectSate = true;
        for (int i = 0; i < databaseNames.size(); i++) {
            this.comboBoxDatabaseName.addItem(databaseNames.get(i));
        }
    }

    private ActionListener runListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            run();
        }
    };

    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelAndCloseDailog();
        }
    };

    private ActionListener addScaleListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int insertIndex = localSplitTable.getRowCount();
            if (localSplitTable.getRowCount() == 1) {
                return;
            }
            double insertScale = 0;
            int selectedRowsIndex[] = localSplitTable.getSelectedRows();
            if (selectedRowsIndex != null && selectedRowsIndex.length >= 1 && selectedRowsIndex[selectedRowsIndex.length - 1] + 1 != localSplitTable.getRowCount()) {
                insertIndex = selectedRowsIndex[selectedRowsIndex.length - 1] + 2;
            }
            if (insertIndex == localSplitTable.getRowCount()) {
                insertScale = currentMapCacheScale.get(insertIndex - 2) * 2;
            } else {
                insertScale = (currentMapCacheScale.get(insertIndex - 2) + currentMapCacheScale.get(insertIndex - 1)) / 2;
            }
            currentMapCacheScale.add(insertIndex - 1, insertScale);
            double addScaleAfter[] = new double[currentMapCacheScale.size()];
            for (int i = 0; i < addScaleAfter.length; i++) {
                addScaleAfter[i] = currentMapCacheScale.get(i);
            }
            DDLExportTableModel model = (DDLExportTableModel) localSplitTable.getModel();
            model.removeRows(0, model.getRowCount());
            setLocalSplitTableValue(addScaleAfter);
        }
    };

    private ActionListener defaultScaleListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DDLExportTableModel model = (DDLExportTableModel) localSplitTable.getModel();
            model.removeRows(0, model.getRowCount());
            setLocalSplitTableValue(originMapCacheScale);
            currentMapCacheScale.clear();
            for (int i = 0; i < originMapCacheScale.length; i++) {
                currentMapCacheScale.add(originMapCacheScale[i]);
            }
        }
    };

    private MouseListener selectAllScaleListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (jMenuAddScale.isEnabled()) {
                localSplitTable.setRowSelectionInterval(0, localSplitTable.getRowCount() - 1);
            } else {
                globalSplitTable.setRowSelectionInterval(0, globalSplitTable.getRowCount() - 1);
            }
        }
    };

    private MouseListener selectScaleInverseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                int[] temp;
                ListSelectionModel selectionModel;
                int allRowCount;
                if (jMenuAddScale.isEnabled()) {
                    selectionModel = localSplitTable.getSelectionModel();
                    temp = localSplitTable.getSelectedRows();
                    allRowCount = localSplitTable.getRowCount();
                } else {
                    selectionModel = globalSplitTable.getSelectionModel();
                    temp = globalSplitTable.getSelectedRows();
                    allRowCount = globalSplitTable.getRowCount();
                }

                ArrayList<Integer> selectedRows = new ArrayList<Integer>();
                for (int index = 0; index < temp.length; index++) {
                    selectedRows.add(temp[index]);
                }

                selectionModel.clearSelection();
                for (int index = 0; index < allRowCount; index++) {
                    if (!selectedRows.contains(index)) {
                        selectionModel.addSelectionInterval(index, index);
                    }
                }
            } catch (Exception ex) {
                Application.getActiveApplication().getOutput().output(ex);
            }
        }
    };

    private MouseListener deleteScaleListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int selectedRowsIndex[] = localSplitTable.getSelectedRows();
            int tableCount = localSplitTable.getRowCount();
            List<Double> removeInfo = new ArrayList<Double>();
            localSplitTable.clearSelection();
            if (selectedRowsIndex != null & selectedRowsIndex.length >= 1) {
                for (int i = 0; i < selectedRowsIndex.length; i++) {
                    if (selectedRowsIndex[i] + 1 != tableCount) {
                        removeInfo.add(currentMapCacheScale.get(selectedRowsIndex[i]));
                    }
                }
            }
            currentMapCacheScale.removeAll(removeInfo);
            double addScaleAfter[] = new double[currentMapCacheScale.size()];
            for (int i = 0; i < addScaleAfter.length; i++) {
                addScaleAfter[i] = currentMapCacheScale.get(i);
            }
            DDLExportTableModel model = (DDLExportTableModel) localSplitTable.getModel();
            model.removeRows(selectedRowsIndex);
            localSplitTable.refreshContents(getNewLocalSplitTableValue(addScaleAfter));
            addMoreScale();
        }
    };

    private TableModelListener tableModelListener = new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
            tableModelListener(e);
        }
    };

    private ItemListener comboboxSplitModeItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getItem().toString() == MapViewProperties.getString("MapCache_ComboboxSplitModeLocalSplit")) {
                mapCacheBuilder.setTilingMode(MapTilingMode.LOCAL);
                scrollPane.setViewportView(localSplitTable);
                jMenuAddScale.setEnabled(true);
                jMenuDelete.setEnabled(true);
                jMenuImport.setEnabled(true);
            } else if (e.getItem().toString() == MapViewProperties.getString("MapCache_ComboboxSplitModeGlobalSplit")) {
                DialogMapCacheBuilder.this.mapCacheBuilder.setTilingMode(MapTilingMode.GLOBAL);
                if (globalSplitScale == null || globalSplitScale.size() == 0) {
                    initGlobalCacheScales();
                }
                scrollPane.setViewportView(globalSplitTable);
                jMenuAddScale.setEnabled(false);
                jMenuDelete.setEnabled(false);
                jMenuImport.setEnabled(false);
            }
        }
    };

    private DocumentListener cacheNameTextChangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            isCanRun();
        }
    };

    private ActionListener chooseCacheFilePathListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String moduleName = "ChooseCacheDirectories";
            if (!SmFileChoose.isModuleExist(moduleName)) {
                SmFileChoose.addNewNode("", CommonProperties.getString("String_DefaultFilePath"), GlobalParameters.getDesktopTitle(),
                        moduleName, "GetDirectories");
            }
            SmFileChoose fileChoose = new SmFileChoose(moduleName);
            int state = fileChoose.showDefaultDialog();
            if (state == JFileChooser.APPROVE_OPTION) {
                String directories = fileChoose.getFilePath();
                if (!directories.endsWith(File.separator)) {
                    directories += File.separator;
                }
                fileChooserControlFileCache.getEditor().setText(directories);
            }
        }
    };

    private DocumentListener chooseCacheFilePathTextChangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            isCanRun();
        }
    };

    private void tableModelListener(TableModelEvent e) {
        int selectRow = e.getFirstRow();
        if (selectRow >= this.currentMapCacheScale.size()) {
            setTableCell(e.getFirstRow(), e.getColumn(), "");
            return;
        }
        String oldValue = showScalePrePart + String.valueOf(1 / this.currentMapCacheScale.get(selectRow));
        String selectValue = localSplitTable.getValueAt(selectRow, e.getColumn()).toString();
        if (e.getColumn() == COLUMN_SCALE) {

            if (!ScaleModel.isLegitScaleString(selectValue)) {
                setTableCell(selectRow, COLUMN_SCALE, oldValue);
                Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_ErrorInput"));
                return;
            }
            if (ScaleModel.isLegitScaleString(selectValue) && selectValue.contains(colon)) {
                setTableCell(selectRow, COLUMN_SCALE, selectValue);
                return;
            }
            if (!selectValue.contains(colon) && ScaleModel.isLegitScaleString(showScalePrePart + selectValue)) {
                setTableCell(selectRow, COLUMN_SCALE, showScalePrePart + selectValue);
                return;
            }
        } else if (e.getColumn() == COLUMN_TITLE) {
            setTableCell(selectRow, COLUMN_TITLE, selectValue);
        }
    }

    private void setTableCell(int selectRow, int selectColumn, String value) {
        this.localSplitTable.getModel().removeTableModelListener(this.tableModelListener);
        if (selectColumn == COLUMN_SCALE && !value.equals("")) {
            this.localSplitTable.setValueAt(showScalePrePart + DoubleUtilities.getFormatString(DoubleUtilities.stringToValue(value.split(":")[1])), selectRow, selectColumn);
            this.currentMapCacheScale.set(selectRow, 1 / DoubleUtilities.stringToValue(value.split(colon)[1]));
        } else if (selectColumn == COLUMN_TITLE && !value.equals("")) {
            this.localSplitTable.setValueAt(value, selectRow, selectColumn);
        } else if (value.equals("")) {
            this.localSplitTable.setValueAt(value, selectRow, selectColumn);
        }
        this.localSplitTable.getModel().addTableModelListener(this.tableModelListener);
    }

    private ItemListener comboboxImageTypeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getItem().toString().equals("PNG") || e.getItem().toString().equals("DXTZ") || e.getItem().toString().equals("PNG8") || e.getItem().toString().equals("GIF")) {
                checkBoxBackgroundTransparency.setSelected(false);
                checkBoxBackgroundTransparency.setEnabled(true);
                if (e.getItem().toString().equals("PNG")) {
                    mapCacheBuilder.setTileFormat(TileFormat.PNG);
                } else if (e.getItem().toString().equals("DXTZ")) {
                    mapCacheBuilder.setTileFormat(TileFormat.DXTZ);
                } else if (e.getItem().toString().equals("PNG8")) {
                    mapCacheBuilder.setTileFormat(TileFormat.PNG8);
                } else if (e.getItem().toString().equals("GIF")) {
                    mapCacheBuilder.setTileFormat(TileFormat.GIF);
                }
            } else if (e.getItem().toString().equals("JPG")) {
                checkBoxBackgroundTransparency.setSelected(false);
                checkBoxBackgroundTransparency.setEnabled(false);
                mapCacheBuilder.setTileFormat(TileFormat.JPG);
            } else if (e.getItem().toString().equals("JPG_PNG")) {
                checkBoxBackgroundTransparency.setSelected(true);
                checkBoxBackgroundTransparency.setEnabled(false);
                mapCacheBuilder.setTileFormat(TileFormat.JPG_PNG);
            }
        }
    };

    private ItemListener comboboxImagePixelListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getItem().toString().equals("64*64")) {
                mapCacheBuilder.setTileSize(TileSize.SIZE64);
            } else if (e.getItem().toString().equals("128*128")) {
                mapCacheBuilder.setTileSize(TileSize.SIZE128);
            } else if (e.getItem().toString().equals("256*256")) {
                mapCacheBuilder.setTileSize(TileSize.SIZE256);
            } else if (e.getItem().toString().equals("512*512")) {
                mapCacheBuilder.setTileSize(TileSize.SIZE512);
            } else if (e.getItem().toString().equals("1024*1024")) {
                mapCacheBuilder.setTileSize(TileSize.SIZE1024);
            } else if (e.getItem().toString().equals("2048*2048")) {

            }
            globalSplitScale = null;
            globalSplitTable = null;
            initGlobalCacheScales();
            if (!jMenuAddScale.isEnabled()) {
                scrollPane.setViewportView(globalSplitTable);
            }
        }
    };

    private ItemListener saveTypeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            initPanelImageSaveOutputDisplay(e.getItem().toString());
            isCanRun();
        }
    };

    private CaretListener cacheRangeCareListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (StringUtilities.isNumber(cacheRangeWaringTextFieldBottom.getText()) && StringUtilities.isNumber(cacheRangeWaringTextFieldLeft.getText())
                    && StringUtilities.isNumber(cacheRangeWaringTextFieldRight.getText()) && StringUtilities.isNumber(cacheRangeWaringTextFieldTop.getText())) {
                Rectangle2D rectangle2D = panelCacheRange.getRangeBound();
                if (rectangle2D != null) {
                    cacheRangeBounds = rectangle2D;
                    validCacheRangeBounds = true;
                } else {
                    validCacheRangeBounds = false;
                }
            } else {
                validCacheRangeBounds = false;
            }
            isCanRun();
        }
    };

    private CaretListener indexRangeCareListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            if (StringUtilities.isNumber(indexRangeWaringTextFieldTop.getText()) && StringUtilities.isNumber(indexRangeWaringTextFieldBottom.getText())
                    && StringUtilities.isNumber(indexRangeWaringTextFieldLeft.getText()) && StringUtilities.isNumber(indexRangeWaringTextFieldRight.getText())) {
                Rectangle2D rectangle2D = panelIndexRange.getRangeBound();
                if (rectangle2D != null) {
                    indexRangeBounds = rectangle2D;
                    validIndexRangeBounds = true;
                } else {
                    validIndexRangeBounds = false;
                }
            } else {
                validIndexRangeBounds = false;
            }
            isCanRun();
        }
    };

    private DocumentListener indexRangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            Rectangle2D rectangle2D = panelIndexRange.getRangeBound();
            if (rectangle2D != null) {
                indexRangeBounds = rectangle2D;
            }
            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            Rectangle2D rectangle2D = panelIndexRange.getRangeBound();
            if (rectangle2D != null) {
                indexRangeBounds = rectangle2D;
            }
            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            Rectangle2D rectangle2D = panelIndexRange.getRangeBound();
            if (rectangle2D != null) {
                indexRangeBounds = rectangle2D;
            }
            isCanRun();
        }
    };

    private DocumentListener cacheRangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
//            Rectangle2D rectangle2D = panelCacheRange.getRangeBound();
//            if (rectangle2D != null) {
//                cacheRangeBounds = rectangle2D;
//            }
//            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
//            Rectangle2D rectangle2D = panelCacheRange.getRangeBound();
//            if (rectangle2D != null) {
//                cacheRangeBounds = rectangle2D;
//            }
//            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            Rectangle2D rectangle2D = panelCacheRange.getRangeBound();
            if (rectangle2D != null) {
                cacheRangeBounds = rectangle2D;
            }
            isCanRun();
        }
    };

    private DocumentListener passwordChangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            isCanRun();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            isCanRun();
        }
    };

    private FocusListener serverNameFocusListener = new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (textFieldServerName.getText().equals(MapViewProperties.getString("MapCache_MongoDB_DefaultServerName"))) {
                textFieldServerName.setText("");
            }
            textFieldServerNameGetFocus = true;
            isCanRun();
        }

        @Override
        public void focusLost(FocusEvent e) {
            textFieldServerNameGetFocus = false;
            connectMongoDBPretreatment();
            isCanRun();
        }
    };

    private ItemListener databaseChangeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            isCanRun();
        }
    };

    private void registEvents() {
        this.jMenuItemAddScale.addActionListener(this.addScaleListener);
        this.jMenuItemDefaultScale.addActionListener(this.defaultScaleListener);
        this.jMenuSelectAll.addMouseListener(this.selectAllScaleListener);
        this.jMenuSelectInverse.addMouseListener(this.selectScaleInverseListener);
        this.jMenuDelete.addMouseListener(this.deleteScaleListener);
        this.localSplitTable.getModel().addTableModelListener(this.tableModelListener);
        this.comboBoxSplitMode.addItemListener(this.comboboxSplitModeItemListener);
        this.textFieldCacheName.getDocument().addDocumentListener(this.cacheNameTextChangeListener);
        this.fileChooserControlFileCache.getButton().addActionListener(this.chooseCacheFilePathListener);
        this.fileChooserControlFileCache.getEditor().getDocument().addDocumentListener(this.chooseCacheFilePathTextChangeListener);
        this.comboBoxImageType.addItemListener(this.comboboxImageTypeListener);
        this.comboBoxPixel.addItemListener(this.comboboxImagePixelListener);
        this.comboBoxSaveType.addItemListener(this.saveTypeListener);
        this.textFieldServerName.addFocusListener(this.serverNameFocusListener);
        this.cacheRangeWaringTextFieldLeft.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldTop.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldRight.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldBottom.getTextField().addCaretListener(this.cacheRangeCareListener);
        this.indexRangeWaringTextFieldLeft.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldTop.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldRight.getTextField().addCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldBottom.getTextField().addCaretListener(this.indexRangeCareListener);
        this.textFieldUserPassword.getDocument().addDocumentListener(this.passwordChangeListener);
        this.textFieldConfirmPassword.getDocument().addDocumentListener(this.passwordChangeListener);
        this.comboBoxDatabaseName.addItemListener(this.databaseChangeListener);
        this.buttonOK.addActionListener(this.runListener);
        this.buttonCancel.addActionListener(this.cancelListener);
    }

    private void removeEvents() {
        this.jMenuItemAddScale.removeActionListener(this.addScaleListener);
        this.jMenuItemDefaultScale.removeActionListener(this.defaultScaleListener);
        this.jMenuSelectAll.removeMouseListener(this.selectAllScaleListener);
        this.jMenuSelectInverse.removeMouseListener(this.selectScaleInverseListener);
        this.jMenuDelete.removeMouseListener(this.deleteScaleListener);
        this.localSplitTable.getModel().removeTableModelListener(this.tableModelListener);
        this.comboBoxSplitMode.removeItemListener(this.comboboxSplitModeItemListener);
        this.textFieldCacheName.getDocument().removeDocumentListener(this.cacheNameTextChangeListener);
        this.fileChooserControlFileCache.getButton().removeActionListener(this.chooseCacheFilePathListener);
        this.fileChooserControlFileCache.getEditor().getDocument().removeDocumentListener(this.chooseCacheFilePathTextChangeListener);
        this.comboBoxImageType.removeItemListener(this.comboboxImageTypeListener);
        this.comboBoxSaveType.removeItemListener(this.saveTypeListener);
        this.comboBoxPixel.removeItemListener(this.comboboxImagePixelListener);
        this.textFieldServerName.removeFocusListener(this.serverNameFocusListener);
        this.cacheRangeWaringTextFieldLeft.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldTop.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldRight.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.cacheRangeWaringTextFieldBottom.getTextField().removeCaretListener(this.cacheRangeCareListener);
        this.indexRangeWaringTextFieldLeft.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldTop.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldRight.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.indexRangeWaringTextFieldBottom.getTextField().removeCaretListener(this.indexRangeCareListener);
        this.textFieldUserPassword.getDocument().removeDocumentListener(this.passwordChangeListener);
        this.textFieldConfirmPassword.getDocument().removeDocumentListener(this.passwordChangeListener);
        this.comboBoxDatabaseName.removeItemListener(this.databaseChangeListener);
        this.buttonOK.removeActionListener(this.runListener);
        this.buttonCancel.removeActionListener(this.cancelListener);
    }

    private class BackgroundMenuBar extends JMenuBar {
        Color bgColor;

        public void setColor(Color color) {
            bgColor = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(bgColor);
            g2d.setBackground(bgColor);
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

}
