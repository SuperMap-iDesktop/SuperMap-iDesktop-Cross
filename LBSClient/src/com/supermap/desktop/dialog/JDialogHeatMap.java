package com.supermap.desktop.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.GeoPie;
import com.supermap.data.GeoRegion;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.WebHDFS;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.messagebus.MessageBus;
import com.supermap.desktop.messagebus.MessageBus.MessageBusType;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilties.CursorUtilties;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;
import com.supermap.ui.TrackMode;
import com.supermap.ui.TrackedEvent;
import com.supermap.ui.TrackedListener;

public class JDialogHeatMap extends SmDialog {
	/**
	 * Create the frame.
	 */
	public JDialogHeatMap(JFrame parent, boolean modal) {
		super(parent, modal);
		initializeComponents();
		this.initializeResources();
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
	private JButton buttonOutputBrowser;
	
	private JButton buttonOK;
	private JButton buttonCancel;

	private void initializeComponents() {
		
		this.labelInputURL = new JLabel("输入数据:");
		this.textInputURL = new JTextField(WebHDFS.getHDFSFilePath());
		this.buttonInputBrowser = new JButton("浏览");
		
		this.labelResolution = new JLabel("分辨率:");
		this.textResolution = new JTextField("0.002");
		this.labelRadius = new JLabel("查找半径:");
		this.textRadius = new JTextField("0.02");
		
		this.panelBounds = new JPanel();
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
		
		this.labelOutputURL = new JLabel("输出路径:");
		this.textOutputURL = new JTextField(WebHDFS.getHDFSOutputDirectry());
		this.buttonOutputBrowser = new JButton("浏览");
		
		this.buttonOK = new JButton("OK");
		this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		
		this.getRootPane().setDefaultButton(this.buttonOK);

		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);

		initContentPane();
		initIndexBoundsPanel();
		
		setSize(640, 400);
		setLocationRelativeTo(null);
		
		registerEvents();
	}
	
	private void initializeResources() {
		this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.setTitle("计算热度图");
		this.panelBounds.setBorder(new TitledBorder(null, "计算范围", TitledBorder.LEADING,
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
				.addComponent(this.panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelResolution)
								.addComponent(this.labelRadius))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.textResolution)
								.addComponent(this.textRadius)))
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
				.addComponent(this.panelBounds, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelResolution)
						.addComponent(this.textResolution, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRadius)
						.addComponent(this.textRadius, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(this.labelOutputURL)
						.addComponent(this.textOutputURL, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonOutputBrowser))
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
	
	public static String formatKernelDensity(MessageBusType messageBusType, 
			String input, String output, String query, String resolution, String radius) {
		String result = "";
		
//		Usage: KernelDensity <spark> <csv> <left,top,right,bottom> <radius> <resolution> <resultgrd>
		String parmSpark = String.format("sh %s --class %s --master %s %s %s", 
				"../bin/spark-submit", 
				"com.supermap.gistark.main.Main", 
				"spark://192.168.14.1:7077", 
				"GIStark-0.1.0-SNAPSHOT.jar",
				"KernelDensity");
		
		String parmQuery = String.format("--input %s --geoidx 10 --separator , --query %s --resolution %s --radius %s --output %s", 
				input, query, resolution, radius, output);
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
		String command = JDialogHeatMap.formatKernelDensity(
				MessageBusType.KernelDensity,
				WebHDFS.getHDFSFilePath(),
				WebHDFS.getHDFSOutputDirectry() + "kerneldensity" + System.currentTimeMillis() + ".grd",
				String.format("%s,%s,%s,%s", this.textBoundsLeft.getText(), this.textBoundsBottom.getText(), this.textBoundsRight.getText(), this.textBoundsTop.getText()),
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
