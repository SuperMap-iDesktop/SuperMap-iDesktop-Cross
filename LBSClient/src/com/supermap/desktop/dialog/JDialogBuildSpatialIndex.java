package com.supermap.desktop.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.messagebus.MessageBus;
import com.supermap.desktop.messagebus.MessageBus.MessageBusType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

public class JDialogBuildSpatialIndex extends SmDialog {
	/**
	 * Create the frame.
	 */
	public JDialogBuildSpatialIndex(JFrame parent, boolean modal) {
		super(parent, modal);
		initializeComponents();
		initializeResources();
	}

	private JLabel labelInputURL;
	private JTextField textInputURL;
	private JButton buttonInputBrowser;
	
	private JPanel panelIndexBounds;
	private JLabel labelBoundsLeft;
	private JTextField textBoundsLeft;
	private JLabel labelBoundsBottom;
	private JTextField textBoundsBottom;
	private JLabel labelBoundsRight;
	private JTextField textBoundsRight;
	private JLabel labelBoundsTop;
	private JTextField textBoundsTop;
	private JButton buttonDrawBounds;
	
	private JPanel panelStartPosition;
	private JLabel labelPositionX;
	private JTextField textPositionX;
	private JLabel labelPositionY;
	private JTextField textPositionY;
	
	private JLabel labelOutputURL;
	private JTextField textOutputURL;
	private JButton buttonOutputBrowser;
	
	private JLabel labelStepSize;
	private JTextField textStepSize;

	private JButton buttonOK;
	private JButton buttonCancel;

	private void initializeComponents() {
		this.labelInputURL = new JLabel("输入数据:");
		this.textInputURL = new JTextField(WebHDFS.getHDFSFilePath());
		this.buttonInputBrowser = new JButton("浏览");
		
		this.panelIndexBounds = new JPanel();
		labelBoundsLeft = new JLabel("Left:");
		textBoundsLeft = new JTextField("-74.050");
		labelBoundsBottom = new JLabel("Bottom:");
		textBoundsBottom = new JTextField("40.650");
		labelBoundsRight = new JLabel("Right:");
		textBoundsRight = new JTextField("-73.850");
		labelBoundsTop = new JLabel("Top:");
		textBoundsTop = new JTextField("40.850");
		buttonDrawBounds = new JButton("绘制范围");
		if (Application.getActiveApplication().getActiveForm() == null
				|| !(Application.getActiveApplication().getActiveForm() instanceof IFormMap)) {
			buttonDrawBounds.setEnabled(false);
		}
		
		this.panelStartPosition = new JPanel();
		labelPositionX = new JLabel("X:");
		textPositionX = new JTextField("10");
		labelPositionY = new JLabel("Y:");
		textPositionY = new JTextField("11");
		
		labelStepSize = new JLabel("步长:");
		textStepSize = new JTextField("0.006");

		this.labelOutputURL = new JLabel("输出路径:");
		this.textOutputURL = new JTextField(WebHDFS.getHDFSOutputDirectry());
		this.buttonOutputBrowser = new JButton("浏览");

		this.buttonOK = new JButton("OK");
		this.buttonCancel = new JButton("Cancel");
		
		this.getRootPane().setDefaultButton(this.buttonOK);
		
		initContentPane();
		initIndexBoundsPanel();
		this.initStartPositionPanel();

		setSize(640, 420);
		setLocationRelativeTo(null);
		
		registerEvents();
	}
	
	private void initializeResources() {
		this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.setTitle("创建索引");
		this.panelIndexBounds.setBorder(new TitledBorder(null, "索引范围", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		this.panelStartPosition.setBorder(new TitledBorder(null, "起始点", TitledBorder.LEADING,
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
						.addComponent(this.buttonInputBrowser, 32, 32, 32))
				.addComponent(this.panelIndexBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelStartPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup().addComponent(this.labelStepSize)
						.addGap(30)
						.addComponent(this.textStepSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup().addComponent(this.labelOutputURL)
						.addComponent(this.textOutputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.buttonOutputBrowser, 32, 32, 32))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, 75, 75, 75)
						.addComponent(this.buttonCancel, 75, 75, 75)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelInputURL)
						.addComponent(this.textInputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonInputBrowser))
				.addComponent(this.panelIndexBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelStartPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelStepSize)
						.addComponent(this.textStepSize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelOutputURL)
						.addComponent(this.textOutputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonOutputBrowser))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}
	
	private void initIndexBoundsPanel() {
		GroupLayout groupLayout = new GroupLayout(this.panelIndexBounds);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.panelIndexBounds.setLayout(groupLayout);
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
	
	private void initStartPositionPanel() {
		GroupLayout groupLayout = new GroupLayout(this.panelStartPosition);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.panelStartPosition.setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelPositionX)
								.addComponent(this.labelPositionY))
						.addGap(34)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.textPositionX)
								.addComponent(this.textPositionY))));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelPositionX)
						.addComponent(this.textPositionX, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))						
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelPositionY)
						.addComponent(this.textPositionY, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on
	}
	
	private void registerEvents() {
		this.buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOKActionPerformed();
			}
		});
		
		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancelActionPerformed();
			}
		});
		
		this.buttonInputBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonInputBrowserActionPerformed();
			}
		});
		
		this.buttonOutputBrowser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonOutputBrowserActionPerformed();
			}
		});
		
		this.buttonDrawBounds.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonDrawBoundsActionPerformed();
			}
		});	
		
	}
	
	private void unRegisterEvents() {
		
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
//		SpatialQuery <spark> <csv> <json/dataset> <resultjson>
		String parmSpark = String.format("sh %s --class %s --master %s %s %s", 
				"../bin/spark-submit",
				"com.supermap.gistark.main.Main", 
				"spark://192.168.14.1:7077", 
				"GIStark-0.1.0-SNAPSHOT.jar",
				"HDFSGridIndexBuild");

		String inputFile = WebHDFS.getHDFSFilePath();
		int index = WebHDFS.webFile.indexOf('.');
		String temp = "SpatialIndex";
		if (index != -1) {
			temp = WebHDFS.webFile.substring(0,  index);
		}
		String outputDir = WebHDFS.getHDFSOutputDirectry() + temp;
		String startPosition = String.format("%s %s", this.textPositionX.getText(), this.textPositionY.getText());
		String bounds = String.format("%s %s %s %s", this.textBoundsLeft.getText(), this.textBoundsBottom.getText(), this.textBoundsRight.getText(), this.textBoundsTop.getText());
		String stepSize = this.textStepSize.getText();
		String parmQuery = String.format("%s %s %s %s %s", inputFile, outputDir, startPosition, bounds, stepSize);
		String command = String.format("%s %s %s", parmSpark, parmQuery, MessageBusType.BuildSpatialIndex.toString());
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

	private void buttonInputBrowserActionPerformed() {
		JDialogHDFSFiles hdfsFiles = new JDialogHDFSFiles();
		hdfsFiles.setIsOutputFolder(false);
		if (hdfsFiles.showDialog() == DialogResult.OK) {
			
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
	 * open按钮点击事件 <li>标记出不能为空的项目 <li>Bounds Query
	 */
	private void buttonOKActionPerformed() {
		try {
			CursorUtilties.setWaitCursor();
			
			WorkThead thread = new WorkThead();
			thread.start();
			
			this.dispose();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilties.setDefaultCursor();
		}
	}
	
	/**
	 * 关闭按钮点击事件
	 */
	private void buttonCancelActionPerformed() {
		DialogExit();
	}

	/**
	 * 关闭窗口
	 */
	private void DialogExit() {
		this.dispose();
	}

}
