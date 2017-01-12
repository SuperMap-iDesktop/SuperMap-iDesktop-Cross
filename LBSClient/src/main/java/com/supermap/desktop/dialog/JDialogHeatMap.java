package com.supermap.desktop.dialog;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IServerService;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.impl.IServerServiceImpl;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.messagebus.NewMessageBus;
import com.supermap.desktop.params.*;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.ui.Action;
import com.supermap.ui.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;

/**
 * Created by xie on 2017/1/11.
 */
public class JDialogHeatMap extends SmDialog {
    private JLabel labelCacheType;
    private JComboBox comboBoxCacheType;

    private JPanel panelBounds;
    private JLabel labelBoundsLeft;
    private JTextField textBoundsLeft;
    private JLabel labelBoundsBottom;
    private JTextField textBoundsBottom;
    private JLabel labelBoundsRight;
    private JTextField textBoundsRight;
    private JLabel labelBoundsTop;
    private JTextField textBoundsTop;
    private JButton buttonDrawBounds;


    private JLabel labelCacheLevel;
    private JTextField textFieldCacheLevel;
    private JLabel labelXYIndex;
    private JTextField textFieldXYIndex;
    private JLabel labelFileInputPath;
    private JTextField textFieldFileInputPath;
    private JLabel labelCacheName;
    private JTextField textFieldCacheName;
    private JLabel labelDatabaseType;
    private JComboBox comboBoxDatabaseType;
    private JLabel labelServiceAddress;
    private JTextField textFieldServiceAddress;
    private JLabel labelDatabase;
    private JTextField textFieldDatabase;
    private JLabel labelVersion;
    private JTextField textFieldVersion;
    private JButton buttonOK;
    private JButton buttonCancel;
    private ActionListener heatMapListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            qureyInfo();
        }
    };
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeEvents();
            JDialogHeatMap.this.dispose();
        }
    };
    private ActionListener drawBoundsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetBounds();
        }
    };

    public JDialogHeatMap(JFrame parent) {
        super(parent, true);
        init();
    }

    private void init() {
        initComponents();
        initLayout();
        initResources();
        registEvents();
    }

    private void initComponents() {
        this.labelCacheType = new JLabel();
        this.comboBoxCacheType = new JComboBox();
        this.comboBoxCacheType.addItem("Heatmap");
        this.panelBounds = new JPanel();
        this.labelBoundsLeft = new JLabel();
        this.textBoundsLeft = new JTextField("-74.050");
        this.labelBoundsBottom = new JLabel();
        this.textBoundsBottom = new JTextField("40.650");
        this.labelBoundsRight = new JLabel();
        this.textBoundsRight = new JTextField("-73.850");
        this.labelBoundsTop = new JLabel();
        this.textBoundsTop = new JTextField("40.850");
        this.buttonDrawBounds = new JButton();
        if (Application.getActiveApplication().getActiveForm() == null
                || !(Application.getActiveApplication().getActiveForm() instanceof IFormMap)) {
            buttonDrawBounds.setEnabled(false);
        }
        this.labelCacheLevel = new JLabel();
        this.textFieldCacheLevel = new JTextField("1");
        this.labelXYIndex = new JLabel();
        this.textFieldXYIndex = new JTextField("10,11");
        this.labelFileInputPath = new JLabel();
        this.textFieldFileInputPath = new JTextField("/opt/LBSData/newyork_taxi_2013-01_14k.csv");
        this.labelCacheName = new JLabel();
        this.textFieldCacheName = new JTextField("test1_heat");
        this.labelDatabaseType = new JLabel();
        this.comboBoxDatabaseType = new JComboBox();
        this.comboBoxDatabaseType.addItem("MongoDB");
        this.labelServiceAddress = new JLabel();
        this.textFieldServiceAddress = new JTextField("192.168.15.245:27017");
        this.labelDatabase = new JLabel();
        this.textFieldDatabase = new JTextField("test");
        this.labelVersion = new JLabel();
        this.textFieldVersion = new JTextField("V1");
        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.getRootPane().setDefaultButton(this.buttonOK);
        setComboBoxTheme(this.comboBoxCacheType);
        setComboBoxTheme(this.comboBoxDatabaseType);
        this.setSize(640, 480);
        this.setLocationRelativeTo(null);
    }

    private void setComboBoxTheme(JComboBox comboBox) {
        comboBox.setEditable(true);
        ((JTextField) comboBox.getEditor().getEditorComponent()).setEditable(false);
    }

    private void initLayout() {
        initPanelBoundsLayout();
        GroupLayout groupLayout = new GroupLayout(this.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.getContentPane().setLayout(groupLayout);
        // @formatter:off
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addComponent(this.labelCacheType)
                        .addComponent(this.comboBoxCacheType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(this.panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.labelXYIndex)
                                .addComponent(this.labelCacheLevel)
                                .addComponent(this.labelFileInputPath)
                                .addComponent(this.labelCacheName)
                                .addComponent(this.labelDatabaseType)
                                .addComponent(this.labelServiceAddress)
                                .addComponent(this.labelDatabase)
                                .addComponent(this.labelVersion)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.textFieldXYIndex)
                                .addComponent(this.textFieldCacheLevel)
                                .addComponent(this.textFieldFileInputPath)
                                .addComponent(this.textFieldCacheName)
                                .addComponent(this.comboBoxDatabaseType)
                                .addComponent(this.textFieldServiceAddress)
                                .addComponent(this.textFieldDatabase)
                                .addComponent(this.textFieldVersion)
                        ))
                .addGroup(groupLayout.createSequentialGroup()
                        .addGap(10, 10, Short.MAX_VALUE)
                        .addComponent(this.buttonOK, 75, 75, 75)
                        .addComponent(this.buttonCancel, 75, 75, 75)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.labelCacheType)
                        .addComponent(this.comboBoxCacheType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(this.panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelXYIndex)
                        .addComponent(this.textFieldXYIndex, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelCacheLevel)
                        .addComponent(this.textFieldCacheLevel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelFileInputPath)
                        .addComponent(this.textFieldFileInputPath, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelCacheName)
                        .addComponent(this.textFieldCacheName, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelDatabaseType)
                        .addComponent(this.comboBoxDatabaseType, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelServiceAddress)
                        .addComponent(this.textFieldServiceAddress, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelDatabase)
                        .addComponent(this.textFieldDatabase, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.labelVersion)
                        .addComponent(this.textFieldVersion, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(this.buttonOK)
                        .addComponent(this.buttonCancel)));
        // @formatter:on
    }

    private void initPanelBoundsLayout() {
        GroupLayout groupLayout = new GroupLayout(this.panelBounds);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.panelBounds.setLayout(groupLayout);
        // @formatter:off
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.labelBoundsLeft)
                                .addComponent(this.labelBoundsBottom)
                                .addComponent(this.labelBoundsRight)
                                .addComponent(this.labelBoundsTop))
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.textBoundsLeft)
                                .addComponent(this.textBoundsBottom)
                                .addComponent(this.textBoundsRight)
                                .addComponent(this.textBoundsTop))
                        .addComponent(buttonDrawBounds, 75, 75, 75)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.labelBoundsLeft)
                        .addComponent(this.textBoundsLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.labelBoundsBottom)
                        .addComponent(this.textBoundsBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.labelBoundsRight)
                        .addComponent(this.textBoundsRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.labelBoundsTop)
                        .addComponent(this.textBoundsTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonDrawBounds, 23, 23, 23)));
        // @formatter:on
    }

    private void initResources() {
        this.labelXYIndex.setText(LBSClientProperties.getString("String_XYIndex"));
        this.labelCacheType.setText(LBSClientProperties.getString("String_CacheType"));
        this.labelCacheLevel.setText(LBSClientProperties.getString("String_CacheLevel"));
        this.buttonDrawBounds.setText(LBSClientProperties.getString("String_DrawBounds"));
        this.labelFileInputPath.setText(LBSClientProperties.getString("String_FileInputPath"));
        this.labelCacheName.setText(LBSClientProperties.getString("String_CacheName"));
        this.labelDatabaseType.setText(LBSClientProperties.getString("String_DatabaseType"));
        this.labelServiceAddress.setText(LBSClientProperties.getString("String_ServiceAddress"));
        this.labelDatabase.setText(LBSClientProperties.getString("String_Database"));
        this.labelVersion.setText(LBSClientProperties.getString("String_Version"));
        this.labelBoundsLeft.setText(LBSClientProperties.getString("String_Left"));
        this.labelBoundsBottom.setText(LBSClientProperties.getString("String_Bottom"));
        this.labelBoundsRight.setText(LBSClientProperties.getString("String_Right"));
        this.labelBoundsTop.setText(LBSClientProperties.getString("String_Top"));
        this.setTitle(LBSClientProperties.getString("String_HeatMap"));
        this.panelBounds.setBorder(new TitledBorder(null, LBSClientProperties.getString("String_CacheBounds"), TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
    }

    private void registEvents() {
        removeEvents();
        this.buttonOK.addActionListener(this.heatMapListener);
        this.buttonCancel.addActionListener(this.cancelListener);
        this.buttonDrawBounds.addActionListener(this.drawBoundsListener);
    }

    private transient MouseListener controlMouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            MapControl control = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
            if (e.getButton() == MouseEvent.BUTTON3) {
                control.removeMouseListener(this);
                exitEdit();
            }
        }
    };

    private void exitEdit() {
        MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
        activeMapControl.setAction(Action.SELECT2);
        activeMapControl.setTrackMode(TrackMode.EDIT);

        this.setVisible(true);
    }

    private transient TrackedListener trackedListener = new TrackedListener() {

        @Override
        public void tracked(TrackedEvent arg0) {
            abstractTracked(arg0);
        }
    };

    private void abstractTracked(TrackedEvent arg0) {
        if (arg0.getGeometry() != null) {

            Rectangle2D rectangle = arg0.getGeometry().getBounds().clone();
            this.textBoundsLeft.setText(String.format("%f", rectangle.getLeft()));
            this.textBoundsBottom.setText(String.format("%f", rectangle.getBottom()));
            this.textBoundsRight.setText(String.format("%f", rectangle.getRight()));
            this.textBoundsTop.setText(String.format("%f", rectangle.getTop()));

            ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeTrackedListener(trackedListener);
            exitEdit();
        } else {
            ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addMouseListener(controlMouseListener);
        }
    }

    private void resetBounds() {
        final MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
        activeMapControl.setTrackMode(TrackMode.TRACK);
        activeMapControl.setAction(com.supermap.ui.Action.CREATERECTANGLE);
        activeMapControl.addMouseListener(controlMouseListener);
        activeMapControl.addTrackedListener(trackedListener);

        this.setVisible(false);
    }

    private void qureyInfo() {
        IServerService service = new IServerServiceImpl();
        BuildCacheJobSetting setting = new BuildCacheJobSetting();

        FileInputDataSetting input = new FileInputDataSetting();
        input.filePath = textFieldFileInputPath.getText();

        MongoDBOutputsetting output = new MongoDBOutputsetting();
        output.cacheName = textFieldCacheName.getText();
        output.cacheType = (String) comboBoxDatabaseType.getSelectedItem();
        output.serverAdresses[0] = textFieldServiceAddress.getText();
        output.database = textFieldDatabase.getText();
        output.version = textFieldVersion.getText();

        BuildCacheDrawingSetting drawing = new BuildCacheDrawingSetting();
        String bounds = textBoundsLeft.getText() + "," + textBoundsBottom.getText() + "," + textBoundsRight.getText() + "," + textBoundsTop.getText();
        drawing.imageType = (String) comboBoxCacheType.getSelectedItem();
        drawing.bounds = bounds;
        drawing.level = textFieldCacheLevel.getText();
        drawing.xyIndex = textFieldXYIndex.getText();
        setting.input = input;
        setting.output = output;
        setting.drawing = drawing;
        JobResultResponse response = service.query(setting);
        if (null != response) {
            NewMessageBus.producer(response);
            dispose();
        }
    }

    private void removeEvents() {
    }
}
