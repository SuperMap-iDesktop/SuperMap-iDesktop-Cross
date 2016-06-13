package com.supermap.desktop.CtrlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilties.CursorUtilties;

import net.infonode.properties.propertymap.PropertyMapWeakListenerManager;

public class JDialogFindTrack extends SmDialog {
	
	String topicName = "AttributeQuery";
	String topicNameRespond = "AttributeQuery_Respond";
	
	/**
	 * Create the frame.
	 */
	public JDialogFindTrack(JFrame parent, boolean modal) {
		super(parent, modal);
		initializeComponents();
	}

	private JLabel labelStart;
	private JTextField textCalendarStart;
	private JLabel labelEnd;
	private JTextField textCalendarEnd;

	private JLabel labelPhoneNumber;
	private JTextField textPhoneNumber;
	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxDatasource;
	private JLabel labelResult;
	private JTextField textDatasetName;

	private JButton buttonOK;
	private JButton buttonCancel;
	
	CalendarChooser startChooser;
	CalendarChooser endChooser;
	String dataFormat = "yyyy-MM-dd:HH:mm:ss";
	
	private void initializeComponents() {
		this.labelStart = new JLabel("开始时间:");
		this.textCalendarStart = new JTextField();
		this.textCalendarStart.setEditable(false);
		this.textCalendarStart.setBounds(10, 10, 200, 30);

		startChooser = CalendarChooser.getInstance(dataFormat);
		startChooser.register(this.textCalendarStart);
		SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
		try {
			startChooser.setTime(sdf.parse("2016-04-11:12:00:00"));
			SimpleDateFormat time = new SimpleDateFormat(dataFormat); 
			this.textCalendarStart.setText(time.format(this.startChooser.getTime()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			Application.getActiveApplication().getOutput().output(e1.getMessage());
		}

		this.labelEnd = new JLabel("结束时间:");
		this.textCalendarEnd = new JTextField();
		this.textCalendarEnd.setEditable(false);
		this.textCalendarEnd.setBounds(10, 10, 200, 30);
		endChooser = CalendarChooser.getInstance(dataFormat);
		endChooser.register(this.textCalendarEnd);
		try {
			endChooser.setTime(sdf.parse("2016-04-11:12:30:00"));
			SimpleDateFormat time = new SimpleDateFormat(dataFormat); 
			this.textCalendarEnd.setText(time.format(this.endChooser.getTime()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			Application.getActiveApplication().getOutput().output(e1.getMessage());
		}
		
		this.labelDatasource = new JLabel("数据源:");
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDatasource.setBounds(10, 10, 200, 30);
		this.labelResult = new JLabel("结果数据集:");
		this.textDatasetName = new JTextField("AttributeQuery");

		this.labelPhoneNumber = new JLabel("电话号码:");
		this.textPhoneNumber = new JTextField("18645392679");
		this.buttonOK = new JButton("OK");
		this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		
		this.getRootPane().setDefaultButton(this.buttonOK);
		
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

		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelStart)
								.addComponent(this.labelEnd)
								.addComponent(this.labelPhoneNumber)
								.addComponent(this.labelDatasource)
								.addComponent(this.labelResult))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.textCalendarStart)
								.addComponent(this.textCalendarEnd)
								.addComponent(this.textPhoneNumber)
								.addComponent(this.comboBoxDatasource)
								.addComponent(this.textDatasetName)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelStart)
						.addComponent(this.textCalendarStart, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelEnd)
						.addComponent(this.textCalendarEnd, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelPhoneNumber)
						.addComponent(this.textPhoneNumber, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelResult)
						.addComponent(this.textDatasetName, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(500, 220);
		setLocationRelativeTo(null);
		
		fillComponents();
		this.setTitle("轨迹查询");
		this.resetControlEnabled();
		
		registerEvents();
	}
	
	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(comboBoxItemListener);
	}
	
	private void unRegisterEvents() {
		this.comboBoxDatasource.removeItemListener(comboBoxItemListener);
	}

	protected void fillComponents() {
		
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		Datasource activeDatasource = null;
		if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
			activeDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		}		
		
		if (datasources.getCount() > 0) {
			this.comboBoxDatasource.resetComboBox(datasources, activeDatasource);
		}	
		
		if (this.comboBoxDatasource.getSelectedDatasource() != null) {
			String datasetName = this.textDatasetName.getText();
			datasetName = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
			this.textDatasetName.setText(datasetName);
		}	
	}

	private void comboBoxDatasourceSelectedItemChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {				
				String datasetName = this.textDatasetName.getText();
				datasetName = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
				this.textDatasetName.setText(datasetName);
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();
	private class ComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxDatasource) {
				comboBoxDatasourceSelectedItemChanged(e);
			}
		}
	}
	
	private void resetControlEnabled() {
		Boolean enable = true;		
		
		if (enable) {
			String datasetName = this.textDatasetName.getText();
			datasetName.trim();
			if (datasetName.equals("")) {
				enable = false;
			}
		}
		
		this.buttonOK.setEnabled(enable);
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

	String[] args = new String[3];
	private void doWork() {		

//		String[] args = new String[3];
		args[0] = "192.168.14.240:9092"; // brokers
		args[1] = topicName; // topic
		
		SimpleDateFormat time = new SimpleDateFormat(dataFormat); 
		String startTime = time.format(this.startChooser.getTime());
		String endTime = time.format(this.endChooser.getTime());
		String phoneNumber = this.textPhoneNumber.getText();
		String resultDataset = this.textDatasetName.getText() + "@192.168.14.227:/home/huchenpu/demo-4.29/result/" + topicName + ".udb";
		String resultPath = "192.168.14.227:/home/huchenpu/demo-4.29/result/";
//		Usage: AttributeQuery <spark> <csv> <mobileNumber> <startTime> <endTime> <resultjson/dataset>
		String parmSpark = String.format("sh %s --class %s --master %s %s %s", 
				"/home/spark-1.5.2-bin-hadoop2.6/bin/spark-submit", 
				"com.supermap.spark.test.AttributeQuery", 
				"spark://192.168.12.103:7077", 
				"demo-lbsjava-0.0.1-SNAPSHOT.jar",
				"local[1]");
//		JDialogHDFSFiles.webFile = "test0.csv";
		String parmCSV = webHDFS.getHDFSFilePath();
		String parmQuery = String.format("%s %s %s %s", phoneNumber, startTime, endTime, resultDataset);
		args[2] = String.format("%s %s %s %s %s %s", parmSpark, parmCSV, parmQuery, args[0], topicNameRespond, resultPath);
		
//		Runnable outPutRun = new Runnable() {
//			@Override
//			public void run() {
				SimpleDateFormat df = new SimpleDateFormat(dataFormat); //设置日期格式
				Application.getActiveApplication().getOutput().output(df.format(new Date()) + "  发送请求..."); //new Date()为获取当前系统时间	
//				Application.getActiveApplication().getOutput().output(args[0]);
//				Application.getActiveApplication().getOutput().output(args[1]);
//				Application.getActiveApplication().getOutput().output(args[2]);
//			}
//		};
		
		lbsCommandProducer.commandProducer(args);
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
