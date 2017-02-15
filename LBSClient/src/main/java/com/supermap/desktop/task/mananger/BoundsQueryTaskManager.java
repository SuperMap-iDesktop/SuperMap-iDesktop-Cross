package com.supermap.desktop.task.mananger;

import com.supermap.Interface.ITaskManager;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.utilities.CursorUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * 范围查询界面
 * 
 * @author xie
 *
 */
public class BoundsQueryTaskManager extends JPanel implements ITaskManager {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public BoundsQueryTaskManager() {
		initializeComponents();
		initiallizeResources();
	}

	private void initiallizeResources() {
		this.labelDatasource.setText(CommonProperties.getString("String_Label_Datasource"));
		this.labelResult.setText(CommonProperties.getString("String_Label_ResultDataset") + ":");
		this.labelBounds.setText(LBSClientProperties.getString("String_BoundsDataset"));
	}

	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxDatasource;
	private JLabel labelBounds;
	private DatasetComboBox comboBoxDataset;
	private JLabel labelResult;
	private JTextField textDatasetName;

	// private JButton buttonOK;
	// private JButton buttonCancel;

	private void initializeComponents() {
		this.labelDatasource = new JLabel();
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDatasource.setBounds(10, 10, 200, 30);

		this.labelBounds = new JLabel();
		this.comboBoxDataset = new DatasetComboBox();
		this.comboBoxDataset.setBounds(10, 10, 200, 30);

		this.labelResult = new JLabel();
		this.textDatasetName = new JTextField("SpatialQuery");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelDatasource)
								.addComponent(this.labelBounds)
								.addComponent(this.labelResult))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource)
								.addComponent(this.comboBoxDataset)
								.addComponent(this.textDatasetName)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
//						.addComponent(this.buttonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
//								javax.swing.GroupLayout.PREFERRED_SIZE)
//						.addComponent(this.buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
//								javax.swing.GroupLayout.PREFERRED_SIZE)
						));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelDatasource)
						.addComponent(this.comboBoxDatasource, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelBounds)
						.addComponent(this.comboBoxDataset, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelResult)
						.addComponent(this.textDatasetName, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(this.buttonOK)
//						.addComponent(this.buttonCancel)
						));
		// @formatter:on

		setSize(500, 180);
		fillComponents();
		this.resetControlEnabled();
		// this.setTitle("范围查询");

		registerEvents();
	}

	private DatasetType[] getSupportDatasetTypes() {
		return new DatasetType[] { DatasetType.REGION };
	}

	private void registerEvents() {
		this.comboBoxDatasource.addItemListener(comboBoxItemListener);
		this.comboBoxDataset.addItemListener(comboBoxItemListener);
	}

	private void unRegisterEvents() {
		this.comboBoxDatasource.removeItemListener(comboBoxItemListener);
		this.comboBoxDataset.removeItemListener(comboBoxItemListener);
	}

	protected void fillComponents() {

		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		Datasource activeDatasource = null;
		if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
			activeDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		}

		if (datasources.getCount() > 0) {
			this.comboBoxDatasource.resetComboBox(datasources, activeDatasource);
			this.comboBoxDataset.setSupportedDatasetTypes(getSupportDatasetTypes());
		}

		if (this.comboBoxDatasource.getSelectedDatasource() != null) {
			this.resetComboboxDataset(this.comboBoxDatasource.getSelectedDatasource());

			String datasetName = this.textDatasetName.getText();
			datasetName = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
			this.textDatasetName.setText(datasetName);
		}

		if (this.comboBoxDataset.getItemCount() > 0) {
			this.comboBoxDataset.setSelectedIndex(0);
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

	private void resetComboboxDataset(Datasource datasource) {

		this.comboBoxDataset.removeAllItems();
		ArrayList<DatasetType> supportTypes = new ArrayList<DatasetType>();
		for (DatasetType type : this.comboBoxDataset.getSupportedDatasetTypes()) {
			supportTypes.add(type);
		}

		if (null != datasource) {
			Datasets datasets = datasource.getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (supportTypes.contains(datasets.get(i).getType())) {
//					DataCell cell = new DataCell();
//					cell.initDatasetType(datasets.get(i));
//					this.comboBoxDataset.addItem(cell);
					this.comboBoxDataset.addItem(datasets.get(i));
				}
			}
		}
	}

	private ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();

	private class ComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxDatasource) {
				comboBoxDatasourceSelectedItemChanged(e);
			} else if (e.getSource() == comboBoxDataset) {
				comboBoxDatasetSelectedItemChanged(e);
			}
		}
	}

	private void resetControlEnabled() {
		Boolean enable = true;
		// if (this.comboBoxDataset.getItemCount() == 0
		// || this.comboBoxDataset.getSelectedIndex() == -1){
		// enable = false;
		// }
		//
		// if (enable) {
		// String datasetName = this.textDatasetName.getText();
		// datasetName.trim();
		// if (datasetName.equals("")) {
		// enable = false;
		// }
		// }

		// this.buttonOK.setEnabled(enable);
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
		// // copy bounds dataset to temp folder
		// String udbName = "SpatialQuery";// + System.currentTimeMillis();
		// String tempPath = "/home/huchenpu/demo-4.29/temp/";
		// String udbFullPath = String.format("%s%s.udb", tempPath, udbName);
		// DatasourceConnectionInfo info = new DatasourceConnectionInfo(udbFullPath, udbName, "");
		// Datasource datasource = Application.getActiveApplication().getWorkspace().getDatasources().create(info);
		// DatasetVector dataset = (DatasetVector)this.comboBoxDataset.getSelectedDataset();
		// Dataset boundsDataset = datasource.copyDataset(dataset, dataset.getName(), dataset.getEncodeType());
		// String boundDatasetParm = boundsDataset.getName() + "@192.168.14.227:" + udbFullPath;
		// datasource.close();
		//
		// // String resultDatasetParm = this.textDatasetName.getText() + "@192.168.14.227:" + topicName + ".udb";
		// String resultDatasetParm = "/home/demo-4.29/SpatialQuery.json";
		//
		// String resultPath = "192.168.14.227:/home/huchenpu/demo-4.29/result/";
		// // SpatialQuery <spark> <csv> <json/dataset> <resultjson>
		// String parmSpark = String.format("sh %s --class %s --master %s %s %s",
		// "/home/spark-1.5.2-bin-hadoop2.6/bin/spark-submit",
		// "com.supermap.spark.test.SpatialQuery",
		// "spark://192.168.12.103:7077",
		// "demo-lbsjava-0.0.1-SNAPSHOT.jar",
		// "local[1]");
		//
		// String parmQuery = String.format("%s %s %s", webHDFS.getHDFSFilePath(), boundDatasetParm, resultDatasetParm);
		// args[2] = String.format("%s %s %s %s", parmSpark, parmQuery, MessageBusType.SpatialQuery.toString(), resultPath);
		//
		// // Runnable outPutRun = new Runnable() {
		// // @Override
		// // public void run() {
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
		// Application.getActiveApplication().getOutput().output(df.format(new Date()) + "  发送请求..."); //new Date()为获取当前系统时间
		// Application.getActiveApplication().getOutput().output(args[0]);
		// Application.getActiveApplication().getOutput().output(args[1]);
		// Application.getActiveApplication().getOutput().output(args[2]);
		// // }
		// // };
		//
		// lbsCommandProducer.commandProducer(args);
	}

	/**
	 * open按钮点击事件 <li>标记出不能为空的项目 <li>Bounds Query
	 */
	private void buttonOKActionPerformed() {
		try {
			CursorUtilities.setWaitCursor();

			String datasetName = this.textDatasetName.getText();
			String temp = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
			if (!datasetName.equals(temp)) {
				UICommonToolkit.showMessageDialog(LBSClientProperties.getString("String_IlligalDatasetName"));
			} else {
				WorkThead thread = new WorkThead();
				thread.start();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnable() {
		boolean isEnable = false;
		if (null != this.comboBoxDatasource.getSelectedDatasource() && this.comboBoxDatasource.getSelectedDatasource().getDatasets().getCount() > 0) {
			isEnable = true;
		}
		return isEnable;
	}

	@Override
	public void run() {
		try {
			CursorUtilities.setWaitCursor();

			String datasetName = this.textDatasetName.getText();
			String temp = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
			if (!datasetName.equals(temp)) {
				UICommonToolkit.showMessageDialog(LBSClientProperties.getString("String_IlligalDatasetName"));
			} else {
				WorkThead thread = new WorkThead();
				thread.start();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
	}

}
