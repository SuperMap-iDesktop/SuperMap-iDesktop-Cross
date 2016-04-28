package com.supermap.desktop.CtrlAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Enum;
import com.supermap.desktop.Application;
import com.supermap.desktop.CtrlAction.JDialogFindTrack.WorkThead;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilties.CursorUtilties;

public class JDialogBoundsQuery extends SmDialog {
	
//	private JPanel contentPane;
	/**
	 * Create the frame.
	 */
	public JDialogBoundsQuery(JFrame parent, boolean modal) {
		super(parent, modal);
		initializeComponents();
	}

	private JLabel labelDatasource;
	private DatasourceComboBox comboBoxDatasource;
	private JLabel labelBounds;
	private DatasetComboBox comboBoxDataset;
	private JLabel labelResult;
	private JTextField textDatasetName;

	private JButton buttonOK;
	private JButton buttonCancel;

	private void initializeComponents() {
		this.labelDatasource = new JLabel("数据源:");
		this.comboBoxDatasource = new DatasourceComboBox();
		this.comboBoxDatasource.setBounds(10, 10, 200, 30);

		this.labelBounds = new JLabel("范围数据集:");
		this.comboBoxDataset = new DatasetComboBox();
		this.comboBoxDataset.setBounds(10, 10, 200, 30);

		this.labelResult = new JLabel("结果数据集:");
		this.textDatasetName = new JTextField("QueryResult");
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
								.addComponent(this.labelDatasource)
								.addComponent(this.labelBounds)
								.addComponent(this.labelResult))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxDatasource)
								.addComponent(this.comboBoxDataset)
								.addComponent(this.textDatasetName)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)));
		
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
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on

		setSize(500, 140);
		setLocationRelativeTo(null);
		
		fillComponents();
		this.resetControlEnabled();
		this.setTitle("范围查询");
	}
	
	private DatasetType[] getSupportDatasetTypes() {
		
		return new DatasetType[]{DatasetType.REGION};
	}

	protected void fillComponents() {
		
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		Datasource activeDatasource = null;
		if (Application.getActiveApplication().getActiveDatasources() != null && Application.getActiveApplication().getActiveDatasources().length > 0) {
			activeDatasource = Application.getActiveApplication().getActiveDatasources()[0];
		}
		this.comboBoxDatasource.resetComboBox(datasources, activeDatasource);
		this.comboBoxDataset.setDatasetTypes(getSupportDatasetTypes());
		
		if (this.comboBoxDatasource.getSelectedDatasource() != null) {
			this.resetComboboxDataset(this.comboBoxDatasource.getSelectedDatasource());
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
		for (DatasetType type : this.comboBoxDataset.getDatasetTypes()) {
			supportTypes.add(type);
		}

		if (null != datasource) {
			Datasets datasets = datasource.getDatasets();
			for (int i = 0; i < datasets.getCount(); i++) {
				if (supportTypes.contains(datasets.get(i).getType()) ) {
					DataCell cell = new DataCell();
					cell.initDatasetType(datasets.get(i));
					this.comboBoxDataset.addItem(cell);
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
//		if (this.comboBoxDataset.getItemCount() == 0 
//				|| this.comboBoxDataset.getSelectedIndex() == -1){
//			enable = false;
//		}
//		
//		if (enable) {
//			String datasetName = this.textDatasetName.getText();
//			datasetName.trim();
//			if (datasetName.equals("")) {
//				enable = false;
//			}
//		}
		
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

		Dataset dataset = this.comboBoxDataset.getSelectedDataset();
		String resultDatasetName = this.textDatasetName.getText();
		
		Application.getActiveApplication().getOutput().output(dataset.getName() + resultDatasetName);
	}
	
	/**
	 * open按钮点击事件 <li>标记出不能为空的项目 <li>Bounds Query
	 */
	private void buttonOKActionPerformed() {
		try {
			CursorUtilties.setWaitCursor();
			
			String datasetName = this.textDatasetName.getText();
			String temp = this.comboBoxDatasource.getSelectedDatasource().getDatasets().getAvailableDatasetName(datasetName);
			if (!datasetName.equals(temp)) {
				UICommonToolkit.showMessageDialog("数据集名称不合法！");
			} else {
				WorkThead thread = new WorkThead();
				thread.run();
				
				this.dispose();
			}
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
