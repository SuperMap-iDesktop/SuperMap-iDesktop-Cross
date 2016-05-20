package com.supermap.desktop.CtrlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
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

public class JDialogHeatMap extends SmDialog {
	
	String topicName = "KernelDensity";
	String topicNameRespond = "KernelDensity_Respond";
	/**
	 * Create the frame.
	 */
	public JDialogHeatMap(JFrame parent, boolean modal) {
		super(parent, modal);
		initializeComponents();
	}

	private JLabel labelBounds;
	private DatasetComboBox comboBoxBounds;	
	private JLabel labelResolution;
	private JTextField textResolution;	
	private JLabel labelRadius;
	private JTextField textRadius;
	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxDatasource;
	private JLabel labelResult;
	private JTextField textDatasetName;

	private JButton buttonOK;
	private JButton buttonCancel;

	private void initializeComponents() {
		
		this.labelResolution = new JLabel("分辨率:");
		this.textResolution = new JTextField("0.1");
		this.labelRadius = new JLabel("查找半径:");
		this.textRadius = new JTextField("0.5");
		
		this.labelDatasource = new JLabel("数据源:");
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDatasource.setBounds(10, 10, 200, 30);
		this.labelBounds = new JLabel("分析范围:");
		this.comboBoxBounds = new DatasetComboBox();
		this.labelResult = new JLabel("结果数据集:");
		this.textDatasetName = new JTextField("KernelDensity");
		
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
								.addComponent(this.labelResolution)
								.addComponent(this.labelRadius)
								.addComponent(this.labelDatasource)
								.addComponent(this.labelBounds)
								.addComponent(this.labelResult))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.textResolution)
								.addComponent(this.textRadius)
								.addComponent(this.comboBoxDatasource)
								.addComponent(this.comboBoxBounds)
								.addComponent(this.textDatasetName)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelResolution)
						.addComponent(this.textResolution, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelRadius)
						.addComponent(this.textRadius, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelBounds)
						.addComponent(this.comboBoxBounds, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelResult)
						.addComponent(this.textDatasetName, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(500, 240);
		setLocationRelativeTo(null);
		
		fillComponents();
		this.setTitle("计算热度图");
		this.resetControlEnabled();
		
		registerEvents();
	}
	
	private DatasetType[] getSupportDatasetTypes() {
		
		return new DatasetType[]{DatasetType.REGION};
	}
	
	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(comboBoxItemListener);
		this.comboBoxBounds.addItemListener(comboBoxItemListener);
	}
	
	private void unRegisterEvents() {
		this.comboBoxDatasource.removeItemListener(comboBoxItemListener);
		this.comboBoxBounds.removeItemListener(comboBoxItemListener);
	}
	
	protected void fillComponents() {
		
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		this.comboBoxBounds.setDatasetTypes(getSupportDatasetTypes());
		
		if (datasources.getCount() > 0) {
			this.resetComboboxDataset(datasources.get(0));
		}	
		
		if (this.comboBoxDatasource.getSelectedDatasource() != null) {
			this.resetComboboxDataset(this.comboBoxDatasource.getSelectedDatasource());
			
			String datasetName = this.textDatasetName.getText();
			datasetName = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
			this.textDatasetName.setText(datasetName);
		}		
		
		if (this.comboBoxBounds.getItemCount() > 0) {
			this.comboBoxBounds.setSelectedIndex(0);
		}
	}

	private void comboBoxDatasourceSelectedItemChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {	
				resetComboboxDataset(comboBoxDatasource.getSelectedDatasource());
				
				String datasetName = this.textDatasetName.getText();
				datasetName = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
				this.textDatasetName.setText(datasetName);
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void comboBoxDatasetSelectedItemChanged(ItemEvent e) {
		try {
			resetControlEnabled();
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
			} else if (e.getSource() == comboBoxBounds) {
				comboBoxDatasetSelectedItemChanged(e);
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

	private void resetComboboxDataset(Datasource datasource) {
		
		this.comboBoxBounds.removeAllItems();
		ArrayList<DatasetType> supportTypes = new ArrayList<DatasetType>();
		for (DatasetType type : this.comboBoxBounds.getDatasetTypes()) {
			supportTypes.add(type);
		}

		if (null != datasource) {
			Datasets datasets = datasource.getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (supportTypes.contains(datasets.get(i).getType()) ) {
					DataCell cell = new DataCell();
					cell.initDatasetType(datasets.get(i));
					this.comboBoxBounds.addItem(cell);
				}
			}
		}
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
			
		DatasetVector datasetBounds = (DatasetVector)this.comboBoxBounds.getSelectedDataset();
		String bounds = String.format("%f,%f,%f,%f", 
				datasetBounds.getBounds().getLeft(),
				datasetBounds.getBounds().getTop(),
				datasetBounds.getBounds().getRight(),
				datasetBounds.getBounds().getBottom());
		String resolution = this.textResolution.getText();
		String radius = this.textRadius.getText();	
		String resultDataset = "/home/demo-4.29/KernelDensity.grd";
//		String resultDataset = this.textDatasetName.getText() + "@huchenpu:" + topicName + ".udb";
		String resultPath = "192.168.14.227:/home/huchenpu/demo-4.29/result/";
//		Usage: KernelDensity <spark> <csv> <left,top,right,bottom> <radius> <resolution> <resultgrd>
		String parmSpark = String.format("sh %s --class %s --master %s %s %s", 
				"/home/spark-1.5.2-bin-hadoop2.6/bin/spark-submit", 
				"com.supermap.spark.test.KernelDensity", 
				"spark://192.168.12.103:7077", 
				"demo-lbsjava-0.0.1-SNAPSHOT.jar",
				"local[1]");
//		JDialogHDFSFiles.webFile = "mobile0426095637.csv";
		String parmCSV = webHDFS.getHDFSFilePath();
		String parmQuery = String.format("%s %s %s %s", bounds, radius, resolution, resultDataset);
		args[2] = String.format("%s %s %s %s %s %s", parmSpark, parmCSV, parmQuery, args[0], topicNameRespond, resultPath);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
		Application.getActiveApplication().getOutput().output(df.format(new Date()) + "  发送请求..."); //new Date()为获取当前系统时间	
//		Application.getActiveApplication().getOutput().output(args[0]);
//		Application.getActiveApplication().getOutput().output(args[1]);
//		Application.getActiveApplication().getOutput().output(args[2]);
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
			
			
//			String datasetName = this.textDatasetName.getText();
//			String temp = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
//			if (!datasetName.equals(temp)) {
//				UICommonToolkit.showMessageDialog("数据集名称不合法！");
//			} else {
//				WorkThead thread = new WorkThead();
//				thread.run();
//				
//				this.dispose();
//			}
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
