package com.supermap.desktop.CtrlAction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.crypto.Data;

import com.supermap.data.DatasetType;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilties.CursorUtilties;

public class JDialogFindTrack extends SmDialog {
	
//	private JPanel contentPane;
	/**
	 * Create the frame.
	 */
	public JDialogFindTrack(JFrame parent, boolean modal) {
		super(parent, modal);
		initializeComponents();
	}

	private JLabel labelStart;
	private JTextField calendarStart;
	private JLabel labelEnd;
	private JTextField calendarEnd;

	private JLabel labelPhoneNumber;
	private JTextField textPhoneNumber;
	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxDatasource;
	private JLabel labelResult;
	private JTextField textDatasetName;

	private JButton buttonOK;
	private JButton buttonCancel;

	private void initializeComponents() {
		this.labelStart = new JLabel("开始时间:");
		this.calendarStart = new JTextField();
		this.calendarStart.setEditable(false);
		CalendarChooser startChooser = CalendarChooser.getInstance("yyyy年MM月dd日");
		this.calendarStart.setBounds(10, 10, 200, 30);
		this.calendarStart.setText("2016年4月28日");
		startChooser.register(this.calendarStart);

		this.labelEnd = new JLabel("结束时间:");
		this.calendarEnd = new JTextField();
		this.calendarEnd.setEditable(false);
		CalendarChooser endChooser = CalendarChooser.getInstance("yyyy年MM月dd日");
		this.calendarEnd.setBounds(10, 10, 200, 30);
		this.calendarEnd.setText("2016年4月28日");
		endChooser.register(this.calendarEnd);
		
		this.labelDatasource = new JLabel("数据源:");
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDatasource.setBounds(10, 10, 200, 30);
		this.labelResult = new JLabel("结果数据集:");
		this.textDatasetName = new JTextField("LocationTracking");

		this.labelPhoneNumber = new JLabel("电话号码:");
		this.textPhoneNumber = new JTextField("18645392679");
		this.buttonOK = new JButton("OK");
		this.buttonOK.setText(CommonProperties.getString("String_Button_OK"));
		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		
		this.getRootPane().setDefaultButton(this.buttonOK);;
		
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
								.addComponent(this.calendarStart)
								.addComponent(this.calendarEnd)
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
						.addComponent(this.calendarStart, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelEnd)
						.addComponent(this.calendarEnd, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
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
	}

	protected void fillComponents() {
		
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		Datasource activeDatasource = null;
		if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
			activeDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		}
		this.comboBoxDatasource.resetComboBox(datasources, activeDatasource);
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
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
				Application.getActiveApplication().getOutput().output(df.format(new Date()) + "开始任务..."); //new Date()为获取当前系统时间
				doWork();
				Application.getActiveApplication().getOutput().output(df.format(new Date()) + "任务完成。"); //new Date()为获取当前系统时间
			} finally {
			}
		}
	}

	private void doWork() {

		String startTime = this.calendarStart.getText();
		String endTime = this.calendarEnd.getText();	
		
		Application.getActiveApplication().getOutput().output(startTime + endTime);
	}
	
	/**
	 * open按钮点击事件 <li>标记出不能为空的项目 <li>Search Location
	 */
	private void buttonOKActionPerformed() {
		try {
			CursorUtilties.setWaitCursor();			
			WorkThead thread = new WorkThead();
			thread.run();
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
