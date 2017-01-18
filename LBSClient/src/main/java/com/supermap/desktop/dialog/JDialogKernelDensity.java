package com.supermap.desktop.dialog;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IServerService;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.impl.IServerServiceImpl;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.messagebus.MessageBus;
import com.supermap.desktop.messagebus.MessageBus.MessageBusType;
import com.supermap.desktop.messagebus.NewMessageBus;
import com.supermap.desktop.params.JobResultResponse;
import com.supermap.desktop.params.KernelDensityJobSetting;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;
import com.supermap.ui.Action;
import com.supermap.ui.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JDialogKernelDensity extends SmDialog {
    private ActionListener kernelDensityListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//                buttonOKActionPerformed();
            queryInfo();
        }
    };
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonCancelActionPerformed();
        }
    };
    private ActionListener drawBoundsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonDrawBoundsActionPerformed();
        }
    };
    private ActionListener inputBrowserListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buttonInputBrowser();
        }
    };

    /**
     * Create the frame.
     */
    public JDialogKernelDensity(JFrame parent, boolean modal) {
        super(parent, modal);
        initializeComponents();
        initializeResources();
    }

    private JLabel labelInputURL;
    private JTextField textInputURL;
    private JButton buttonInputBrowser;

    private JLabel labelResolution;
    private JTextField textResolution;
    private JLabel labelRadius;
    private JTextField textRadius;

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

    private JLabel labelOutputURL;
    private JTextField textOutputURL;
//    private JButton buttonOutputBrowser;

    private JLabel labelIndex;
    private JTextField textFieldIndex;

    private JLabel labelSeperator;
    private JTextField textFieldSeperator;

    private JButton buttonOK;
    private JButton buttonCancel;

    private void initializeComponents() {

        this.labelInputURL = new JLabel();
        this.textInputURL = new JTextField("/opt/LBSData/newyork_taxi_2013-01_14k.csv");
        this.buttonInputBrowser = new JButton();

        this.labelResolution = new JLabel();
        this.textResolution = new JTextField("0.004");
        this.labelRadius = new JLabel();
        this.textRadius = new JTextField("0.004");

        this.panelBounds = new JPanel();
        this.labelBoundsLeft = new JLabel();
        this.textBoundsLeft = new JTextField("-74.050");
        this.labelBoundsBottom = new JLabel();
        this.textBoundsBottom = new JTextField("40.550");
        this.labelBoundsRight = new JLabel();
        this.textBoundsRight = new JTextField("-73.750");
        this.labelBoundsTop = new JLabel();
        this.textBoundsTop = new JTextField("40.950");
        this.buttonDrawBounds = new JButton();
        if (Application.getActiveApplication().getActiveForm() == null
                || !(Application.getActiveApplication().getActiveForm() instanceof IFormMap)) {
            buttonDrawBounds.setEnabled(false);
        }
        this.labelIndex = new JLabel();
        this.textFieldIndex = new JTextField("10");
        this.labelSeperator = new JLabel();
        this.textFieldSeperator = new JTextField(",");
        this.labelOutputURL = new JLabel();
        this.textOutputURL = new JTextField("/opt/supermap_iserver_811_14511_9_linux64_deploy/webapps/iserver/processingResultData/KernelDensity");
//        this.buttonOutputBrowser = new JButton();

        this.buttonOK = ComponentFactory.createButtonOK();
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.getRootPane().setDefaultButton(this.buttonOK);

        initContentPane();
        initIndexBoundsPanel();
        if (SystemPropertyUtilities.isWindows()) {
            setSize(700, 400);
        } else {
            setSize(860, 500);
        }
        setLocationRelativeTo(null);

        registerEvents();
    }

    private void initializeResources() {
        this.labelInputURL.setText(LBSClientProperties.getString("String_FileInputPath"));
        this.labelResolution.setText(LBSClientProperties.getString("String_Resolution"));
        this.labelRadius.setText(LBSClientProperties.getString("String_Radius"));
        this.buttonDrawBounds.setText(LBSClientProperties.getString("String_DrawBounds"));
        this.buttonDrawBounds.setToolTipText(LBSClientProperties.getString("String_DrawBounds"));
        this.labelIndex.setText(LBSClientProperties.getString("String_Index"));
        this.labelSeperator.setText(LBSClientProperties.getString("String_Seperator"));
        this.labelOutputURL.setText(LBSClientProperties.getString("String_OutputURL"));
        this.labelBoundsLeft.setText(LBSClientProperties.getString("String_Left"));
        this.labelBoundsBottom.setText(LBSClientProperties.getString("String_Bottom"));
        this.labelBoundsRight.setText(LBSClientProperties.getString("String_Right"));
        this.labelBoundsTop.setText(LBSClientProperties.getString("String_Top"));
        this.setTitle(LBSClientProperties.getString("String_KernelDensityAnalyst"));
        this.buttonInputBrowser.setText(LBSClientProperties.getString("String_Browser"));
        this.buttonInputBrowser.setToolTipText(LBSClientProperties.getString("String_Browser"));
        this.panelBounds.setBorder(new TitledBorder(null, LBSClientProperties.getString("String_AnalystBounds"), TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
    }

    private void initContentPane() {

        GroupLayout groupLayout = new GroupLayout(this.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.getContentPane().setLayout(groupLayout);

        // @formatter:off
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup().addComponent(this.labelInputURL)
                        .addComponent(this.textInputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.buttonInputBrowser, 75, 75, 75))
                .addComponent(this.panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.labelIndex)
                                .addComponent(this.labelSeperator)
                                .addComponent(this.labelResolution)
                                .addComponent(this.labelRadius))
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.textFieldIndex)
                                .addComponent(this.textFieldSeperator)
                                .addComponent(this.textResolution)
                                .addComponent(this.textRadius)))
                .addGroup(groupLayout.createSequentialGroup().addComponent(this.labelOutputURL)
                        .addComponent(this.textOutputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                        .addComponent(this.buttonOutputBrowser, 32, 32, 32))
                .addGroup(groupLayout.createSequentialGroup()
                        .addGap(10, 10, Short.MAX_VALUE)
                        .addComponent(this.buttonOK, 75, 75, 75)
                        .addComponent(this.buttonCancel, 75, 75, 75)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelInputURL)
                        .addComponent(this.textInputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonInputBrowser))
                .addComponent(this.panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelIndex)
                        .addComponent(this.textFieldIndex, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelSeperator)
                        .addComponent(this.textFieldSeperator, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelResolution)
                        .addComponent(this.textResolution, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.labelRadius)
                        .addComponent(this.textRadius, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelOutputURL)
                        .addComponent(this.textOutputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
//                        .addComponent(this.buttonOutputBrowser))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.buttonOK)
                        .addComponent(this.buttonCancel)));
        // @formatter:on
    }

    private void initIndexBoundsPanel() {
        GroupLayout groupLayout = new GroupLayout(this.panelBounds);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.panelBounds.setLayout(groupLayout);
        // @formatter:off
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.labelBoundsLeft)
                                .addComponent(this.labelBoundsBottom)
                                .addComponent(this.labelBoundsRight)
                                .addComponent(this.labelBoundsTop))
                        .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                .addComponent(this.textBoundsLeft)
                                .addComponent(this.textBoundsBottom)
                                .addComponent(this.textBoundsRight)
                                .addComponent(this.textBoundsTop))
                        .addComponent(buttonDrawBounds, 75, 75, 75)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelBoundsLeft)
                        .addComponent(this.textBoundsLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelBoundsBottom)
                        .addComponent(this.textBoundsBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelBoundsRight)
                        .addComponent(this.textBoundsRight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelBoundsTop)
                        .addComponent(this.textBoundsTop, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.buttonDrawBounds, 23, 23, 23)));
        // @formatter:on
    }

    private void registerEvents() {
        unRegisterEvents();
        this.buttonOK.addActionListener(this.kernelDensityListener);
        this.buttonCancel.addActionListener(this.cancelListener);
        this.buttonInputBrowser.addActionListener(this.inputBrowserListener);

//        this.buttonOutputBrowser.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                buttonOutputBrowserActionPerformed();
//            }
//        });

        this.buttonDrawBounds.addActionListener(this.drawBoundsListener);
    }

    private void queryInfo() {
        KernelDensityJobSetting kenelDensityJobSetting = new KernelDensityJobSetting();
        String queryInfo = textBoundsLeft.getText() + "," + textBoundsBottom.getText() + "," + textBoundsRight.getText() + "," + textBoundsTop.getText();
        kenelDensityJobSetting.analyst.query = queryInfo;
        kenelDensityJobSetting.analyst.geoidx = textFieldIndex.getText();
        kenelDensityJobSetting.analyst.separator = textFieldSeperator.getText();
        kenelDensityJobSetting.analyst.resolution = textResolution.getText();
        kenelDensityJobSetting.analyst.radius = textRadius.getText();
        kenelDensityJobSetting.input.filePath = textInputURL.getText();
        IServerService service = new IServerServiceImpl();
        JobResultResponse response = service.query(kenelDensityJobSetting);
        if (null != response) {
            NewMessageBus.producer(response);
            dispose();
        }
    }

    private void unRegisterEvents() {
        this.buttonOK.removeActionListener(this.kernelDensityListener);
        this.buttonCancel.removeActionListener(this.cancelListener);
        this.buttonDrawBounds.removeActionListener(this.drawBoundsListener);
        this.buttonInputBrowser.removeActionListener(this.inputBrowserListener);
    }

    public static String formatKernelDensity(MessageBusType messageBusType,
                                             String input, String output, String query, String index, String seperator, String resolution, String radius) {
        String result = "";

//		Usage: KernelDensity <spark> <csv> <left,top,right,bottom> <radius> <resolution> <resultgrd>
        String parmSpark = String.format("sh %s --class %s --master %s %s %s",
                "../bin/spark-submit",
                "com.supermap.gistark.main.Main",
                "spark://192.168.14.1:7077",
                "GIStark-0.1.0-SNAPSHOT.jar",
                "KernelDensity");

        String parmQuery = String.format("--input %s --geoidx %s --separator %s --query %s --resolution %s --radius %s --output %s",
                index, seperator, input, query, resolution, radius, output);
        result = String.format("%s %s %s", parmSpark, parmQuery, messageBusType.toString());

        return result;
    }

    class WorkThead extends Thread {

        @Override
        public void run() {
            try {
                doWork();
            } finally {
            }
        }
    }

    private void doWork() {
        String command = JDialogKernelDensity.formatKernelDensity(
                MessageBusType.KernelDensity,
                WebHDFS.getHDFSFilePath(),
                WebHDFS.getHDFSOutputDirectry() + "kerneldensity" + System.currentTimeMillis() + ".grd",
                String.format("%s,%s,%s,%s", this.textBoundsLeft.getText(), this.textBoundsBottom.getText(), this.textBoundsRight.getText(), this.textBoundsTop.getText()),
                this.textFieldIndex.getText(),
                this.textFieldSeperator.getText(),
                this.textResolution.getText(),
                this.textRadius.getText());

//		Runnable outPutRun = new Runnable() {
//		@Override
//		public void run() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        Application.getActiveApplication().getOutput().output(df.format(new Date()) + "  发送请求..."); //new Date()为获取当前系统时间
        Application.getActiveApplication().getOutput().output(command);
//		}
//	};

        MessageBus.producer(command);
    }

    /**
     * open按钮点击事件 <li>标记出不能为空的项目 <li>Search Location
     */
    private void buttonOKActionPerformed() {
        try {
            CursorUtilities.setWaitCursor();

            WorkThead thread = new WorkThead();
            thread.start();

            this.dispose();
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        } finally {
            CursorUtilities.setDefaultCursor();
        }
    }

    private void buttonInputBrowser() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        JDialogHDFSFiles hdfsFiles = new JDialogHDFSFiles();
        hdfsFiles.setIsOutputFolder(false);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (hdfsFiles.showDialog() == DialogResult.OK) {
            textInputURL.setText(WebHDFS.webURL);
        }
    }

    private void buttonOutputBrowserActionPerformed() {
        JDialogHDFSFiles hdfsFiles = new JDialogHDFSFiles();
        hdfsFiles.setIsOutputFolder(true);
        if (hdfsFiles.showDialog() == DialogResult.OK) {

        }
    }

    private void buttonDrawBoundsActionPerformed() {
        final MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
        activeMapControl.setTrackMode(TrackMode.TRACK);
        activeMapControl.setAction(Action.CREATERECTANGLE);
        activeMapControl.addMouseListener(controlMouseListener);
        activeMapControl.addTrackedListener(trackedListener);

        this.setVisible(false);
    }

    private void exitEdit() {
        MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
        activeMapControl.setAction(Action.SELECT2);
        activeMapControl.setTrackMode(TrackMode.EDIT);

        this.setVisible(true);
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

    /**
     * 关闭按钮点击事件
     */
    private void buttonCancelActionPerformed() {
        unRegisterEvents();
        DialogExit();
    }

    /**
     * 关闭窗口
     */
    private void DialogExit() {
        this.dispose();
    }

}
