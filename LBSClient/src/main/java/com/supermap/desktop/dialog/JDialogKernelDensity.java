package com.supermap.desktop.dialog;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.messagebus.MessageBus;
import com.supermap.desktop.messagebus.MessageBus.MessageBusType;
import com.supermap.desktop.messagebus.NewMessageBus;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.impl.WebHDFS;
import com.supermap.desktop.ui.lbs.params.CommonSettingCombine;
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.ui.lbs.ui.JDialogHDFSFiles;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

	private JLabel labelXIndex;
	private JTextField textFieldXIndex;
	private JLabel labelYIndex;
	private JTextField textFieldYIndex;
	private JLabel labelSeparator;
	private JTextField textFieldSeparator;
	private JLabel analystMethod;
	private JComboBox comboBoxMethod;
	private JLabel gridType;
	private JComboBox comboBoxType;

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

	private JLabel labelIndex;
	private JTextField textFieldIndex;

	private JButton buttonOK;
	private JButton buttonCancel;

	private void initializeComponents() {

		this.labelInputURL = new JLabel();
		this.textInputURL = new JTextField(WebHDFS.getHDFSFilePath());
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
		this.labelXIndex =new JLabel();
		this.textFieldXIndex = new JTextField("10");
		this.labelYIndex =new JLabel();
		this.textFieldYIndex= new JTextField("11");
		this.labelSeparator = new JLabel();
		this.textFieldSeparator = new JTextField(",");
		this.analystMethod = new JLabel();
		this.comboBoxMethod = new JComboBox();
		this.comboBoxMethod.addItem(LBSClientProperties.getString("String_SimpleDensity"));
		this.comboBoxMethod.addItem(LBSClientProperties.getString("String_KernelDensityAnalyst"));
		this.gridType = new JLabel();
		this.comboBoxType= new JComboBox();
		this.comboBoxType.addItem(LBSClientProperties.getString("String_QuadrilateralMesh"));
		this.comboBoxType.addItem(LBSClientProperties.getString("String_HexagonalMesh"));
		this.labelOutputURL = new JLabel();
		this.textOutputURL = new JTextField("/opt/supermap_iserver_811_14511_9_linux64_deploy/webapps/iserver/processingResultData/KernelDensity");
		this.textOutputURL.setEnabled(false);
//        this.buttonOutputBrowser = new JButton();

		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		this.getRootPane().setDefaultButton(this.buttonOK);

		initContentPane();
		setSize(429,572);
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
		this.labelSeparator.setText(LBSClientProperties.getString("String_Seperator"));
		this.labelXIndex.setText(LBSClientProperties.getString("String_XIndex"));
		this.labelYIndex.setText(LBSClientProperties.getString("String_YIndex"));
		this.analystMethod.setText(LBSClientProperties.getString("String_AnalyseType"));
		this.gridType.setText(LBSClientProperties.getString("String_MeshType"));
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
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.add(this.buttonOK, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));
		panelButton.add(this.buttonCancel, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0).setInsets(0, 0, 10, 10));

		this.setLayout(new GridBagLayout());
		this.add(this.labelInputURL, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textInputURL, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.add(this.buttonInputBrowser, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 0, 10));
		this.add(this.panelBounds, new GridBagConstraintsHelper(0, 1, 4, 4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setInsets(10, 10, 0, 10).setWeight(1, 0));
		this.add(this.labelXIndex, new GridBagConstraintsHelper(0, 5, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldXIndex, new GridBagConstraintsHelper(1, 5, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelYIndex, new GridBagConstraintsHelper(0, 6, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldYIndex, new GridBagConstraintsHelper(1, 6, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelSeparator, new GridBagConstraintsHelper(0, 7, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldSeparator, new GridBagConstraintsHelper(1, 7, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.analystMethod, new GridBagConstraintsHelper(0, 8, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.comboBoxMethod, new GridBagConstraintsHelper(1, 8, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.gridType, new GridBagConstraintsHelper(0, 9, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.comboBoxType, new GridBagConstraintsHelper(1, 9, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelIndex, new GridBagConstraintsHelper(0, 10, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textFieldIndex, new GridBagConstraintsHelper(1, 10, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelResolution, new GridBagConstraintsHelper(0, 11, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textResolution, new GridBagConstraintsHelper(1, 11, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(this.labelRadius, new GridBagConstraintsHelper(0, 12, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.add(this.textRadius, new GridBagConstraintsHelper(1, 12, 3, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 10).setWeight(1, 0));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 13, 4, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH).setWeight(0, 1));
		this.add(panelButton, new GridBagConstraintsHelper(0, 14, 4, 1).setAnchor(GridBagConstraints.EAST).setWeight(0, 0));

		this.panelBounds.setLayout(new GridBagLayout());
		this.panelBounds.add(this.labelBoundsLeft, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsLeft, new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsBottom, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsBottom, new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsRight, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsRight, new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 0).setWeight(1, 0));
		this.panelBounds.add(this.labelBoundsTop, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 10, 10, 0).setWeight(0, 0));
		this.panelBounds.add(this.textBoundsTop, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 10, 0).setWeight(1, 0));
		this.panelBounds.add(this.buttonDrawBounds, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 10, 10).setWeight(0, 0));
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
		IServerService service = new IServerServiceImpl();
		CommonSettingCombine filePath = new CommonSettingCombine("filePath",textInputURL.getText());
		CommonSettingCombine xIndex = new CommonSettingCombine("xIndex",textFieldXIndex.getText());
		CommonSettingCombine yIndex = new CommonSettingCombine("yIndex",textFieldYIndex.getText());
		CommonSettingCombine separator = new CommonSettingCombine("separator",textFieldSeparator.getText());
		CommonSettingCombine input = new CommonSettingCombine("input", "");
		input.add(filePath,xIndex,yIndex,separator);

		CommonSettingCombine method = new CommonSettingCombine("method",(String) comboBoxMethod.getSelectedItem());
		CommonSettingCombine meshType = new CommonSettingCombine("meshType",(String) comboBoxType.getSelectedItem());
		CommonSettingCombine fields = new CommonSettingCombine("fields", textFieldIndex.getText());
		CommonSettingCombine query = new CommonSettingCombine("query",textBoundsLeft.getText() + "," + textBoundsBottom.getText() + "," + textBoundsRight.getText() + "," + textBoundsTop.getText());
		CommonSettingCombine resolution = new CommonSettingCombine("resolution",textResolution.getText());
		CommonSettingCombine radius = new CommonSettingCombine("radius",textRadius.getText());
		CommonSettingCombine analyst = new CommonSettingCombine("analyst", "");
		analyst.add(query,resolution,radius,method,meshType,fields);

		CommonSettingCombine commonSettingCombine = new CommonSettingCombine("", "");
		commonSettingCombine.add(input,analyst);
		JobResultResponse response = service.queryResult("KernelDensity",commonSettingCombine.getFinalJSon());
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		if (null != response) {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
				this.textFieldSeparator.getText(),
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
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (hdfsFiles.showDialog() == DialogResult.OK) {
			textInputURL.setText(WebHDFS.getResultHDFSFilePath());
		}
	}

	private void buttonOutputBrowserActionPerformed() {
		JDialogHDFSFiles hdfsFiles = new JDialogHDFSFiles();
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
