package com.supermap.desktop.newtheme.saveThemeAsDataset;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetTypeComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.mapping.Layer;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.supermap.data.CursorType.DYNAMIC;
import static com.supermap.data.CursorType.STATIC;

/**
 * @author YuanR
 */
public class DiglogSaveThemeAsDataset extends SmDialog {
	private SmButton buttonOk;
	private SmButton buttonCancel;
	private JLabel datasourcesLabel;
	private JLabel datasourceTypeLabel;
	private JLabel datasourceNameLabel;
	private DatasourceComboBox datasourceComboBox;
	private DatasetTypeComboBox datasetTypeComboBox;
	private JTextField textFieldoutDatasetName;
	private DatasetType[] datasetTypes;

	private String outDatasetName;
	private Datasource toDatasource;
	private DatasetType outDatasetType;
	private DatasetVector outDataset;


	/**
	 * 创建保存为cad/文本数据集Dialog
	 */
	public DiglogSaveThemeAsDataset() {
		initComponents();
		initToDatasource();
		initOutDatasetType();
		initOutDatasetName();
		initResources();
		initLayout();
		addListeners();
	}

	/**
	 * 初始化控件
	 */
	private void initComponents() {
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		this.datasourcesLabel = new JLabel();
		this.datasourceTypeLabel = new JLabel();
		this.datasourceNameLabel = new JLabel();

		this.datasourceComboBox = new DatasourceComboBox();
		this.datasetTypes = new DatasetType[2];
		this.datasetTypes[0] = DatasetType.CAD;
		this.datasetTypes[1] = DatasetType.TEXT;
		this.datasetTypeComboBox = new DatasetTypeComboBox(datasetTypes);
		this.datasetTypeComboBox.setAllShown(false);
		this.textFieldoutDatasetName = new JTextField();

		this.buttonOk = new SmButton();
		this.buttonCancel = new SmButton();
	}

	/**
	 * 初始化导出到的数据源
	 */
	private void initToDatasource() {
		this.toDatasource = this.datasourceComboBox.getSelectedDatasource();
	}

	/**
	 * 初始化导出的数据类型
	 */
	private void initOutDatasetType() {
		this.outDatasetType = DatasetType.CAD;
	}

	/**
	 * 初始化导出数据集名称
	 */
	private void initOutDatasetName() {
		int num = 1;
		while (!isAvailableDatasetName(this.outDatasetName)) {
			this.outDatasetName = ((ThemeUtil.getActiveLayer().getName()).replace("@", "_")).replace("#", "_") + "_" + num;
			num++;
		}
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(panel);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		panel.setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.datasourcesLabel)
								.addComponent(this.datasourceTypeLabel)
								.addComponent(this. datasourceNameLabel))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.datasourceComboBox)
								.addComponent(this.datasetTypeComboBox)
								.addComponent(this.textFieldoutDatasetName)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOk, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(this.buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasourcesLabel)
						.addComponent(this.datasourceComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasourceTypeLabel)
						.addComponent(this.datasetTypeComboBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this. datasourceNameLabel)
						.addComponent(this.textFieldoutDatasetName, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.buttonOk)
						.addComponent(this.buttonCancel)
						));
		// @formatter:on
		this.add(panel);
	}

	/**
	 * 资源化
	 */
	private void initResources() {
		this.setTitle(MapViewProperties.getString("String_SaveAsCAD/TextDataset"));
		this.datasourcesLabel.setText(CommonProperties.getString("String_Label_Datasource"));
		this.datasourceTypeLabel.setText(MapViewProperties.getString("String_Label_DatasetType"));
		this.datasourceNameLabel.setText(ControlsProperties.getString("String_Label_DatasetName"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
		this.textFieldoutDatasetName.setText(this.outDatasetName);
	}

	/**
	 * 根据选中的专题图类型，初始化相关功能开启与否
	 */
	private void initThemeStyle() {

	}

	/**
	 * 初始化监听
	 */
	private void addListeners() {
		this.datasourceComboBox.addItemListener(this.DatasourceComboBoxListener);
		this.datasetTypeComboBox.addItemListener(this.DatasetTypeComboBoxListener);
		this.textFieldoutDatasetName.getDocument().addDocumentListener(this.JTextFieldDocumentListener);
		this.buttonOk.addActionListener(this.OKActionListener);
		this.buttonCancel.addActionListener(this.CancelActionListener);
	}

	/**
	 * 注销事件
	 */
	private void removeListeners() {
		this.datasourceComboBox.removeItemListener(this.DatasourceComboBoxListener);
		this.datasetTypeComboBox.removeItemListener(this.DatasetTypeComboBoxListener);
		this.buttonOk.removeActionListener(this.OKActionListener);
		this.buttonCancel.removeActionListener(this.CancelActionListener);
		this.textFieldoutDatasetName.getDocument().removeDocumentListener(this.JTextFieldDocumentListener);
	}

	/**
	 * DatasourceComboBox改变监听
	 */
	private ItemListener DatasourceComboBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			toDatasource = datasourceComboBox.getSelectedDatasource();
		}
	};
	/**
	 * DatasetTypeComboBox改变监听
	 */
	private ItemListener DatasetTypeComboBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			outDatasetType = CommonToolkit.DatasetTypeWrap.findType(datasetTypeComboBox.getSelectedItem().toString());
		}
	};

	/**
	 * OK按钮点击监听
	 */
	private ActionListener OKActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (toDatasource != null && outDatasetType != null && isAvailableDatasetName(outDatasetName)) {
				//选择了CAD数据集类型
				if (outDatasetType.equals(DatasetType.CAD)) {
					try {
						//进行保存为CAD数据集操作
						DatasetVector datasetCAD = ThemeUtil.getActiveLayer().themeToDatasetVector(toDatasource, outDatasetName);
						outDataset = datasetCAD;
						Application.getActiveApplication().getOutput().output(outDatasetName + MapViewProperties.getString("String_OutSuccessed"));
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutFailed"));
					}
					//选择了文本数据集类型
				} else if (outDatasetType.equals(DatasetType.TEXT)) {
					try {
						//进行保存为文本数据集操作
						/*
						//先创建Cad数据集
						DatasetVector datasetTEXT = ThemeUtil.getActiveLayer().themeToDatasetVector(toDatasource, "tempDataset");
						//首先创建一个空的文本数据集
						createNullTextDataset();
						//追加
						DatasetVector aimDatasetVector = (DatasetVector) toDatasource.getDatasets().get(outDatasetName);
						Recordset recordset =  datasetTEXT.getRecordset(false, CursorType.DYNAMIC);
						if (aimDatasetVector.append(recordset)) {
							System.out.println("追加数据集成功");
						}else {
							System.out.println("追加数据集失败");
						}
						datasetTEXT.close();
						toDatasource.getDatasets().delete("tempDataset");
						// 释放资源
						recordset.dispose();

						Application.getActiveApplication().getOutput().output(outDatasetName + MapViewProperties.getString("String_OutSuccessed"));
						*/
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutFailed"));
					}
				}
			} else {
				//保存失败
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutFailed"));
				if (toDatasource == null) {
					Application.getActiveApplication().getOutput().output(CommonProperties.getString("String_Label_Datasource") + toDatasource);
				} else if (outDatasetType == null) {
					Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_Label_DatasetType") + outDatasetType);
				}
			}
			//注销事件
			removeListeners();
			//关闭窗口
			DiglogSaveThemeAsDataset.this.dispose();
		}
	};

	/**
	 * Cancel按钮点击监听
	 */
	private ActionListener CancelActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//注销事件
			removeListeners();
			//关闭窗口
			DiglogSaveThemeAsDataset.this.dispose();
		}
	};

	/**
	 * JTextField改变监听
	 */
	private DocumentListener JTextFieldDocumentListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			newFilter();
		}
	};

	/**
	 * 当文本框改变时
	 */
	private void newFilter() {
		if (isAvailableDatasetName(this.textFieldoutDatasetName.getText()) && !this.textFieldoutDatasetName.getText().equals("")) {
			this.buttonOk.setEnabled(true);
			this.outDatasetName = this.textFieldoutDatasetName.getText();
		} else {
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_DatasetNameMessage"));
			this.buttonOk.setEnabled(false);
		}
	}

	/**
	 * 判断数据集名称是否合法
	 *
	 * @param name
	 * @return
	 */
	private boolean isAvailableDatasetName(String name) {
		if (getToDatasource().getDatasets().isAvailableDatasetName(name)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建一个空的文本数据集
	 */
	private void createNullTextDataset() {
		if (outDatasetName != null && outDatasetType != null) {
			DatasetVectorInfo info = new DatasetVectorInfo(outDatasetName, outDatasetType);
			//压缩方式
			info.setEncodeType(EncodeType.NONE);
			try {
				outDataset = toDatasource.getDatasets().create(info);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_OutFailed"));
			}
		}
	}

	/**
	 * 获得导出到的数据源
	 *
	 * @return
	 */
	public Datasource getToDatasource() {
		if (this.toDatasource != null) {
			return this.toDatasource;
		}
		return null;
	}

	/**
	 * 获得导出数据集的类型cad/文本
	 *
	 * @return
	 */
	public DatasetType getOutDatasetType() {
		if (null != this.outDatasetType) {
			return this.outDatasetType;
		}
		return null;
	}

	/**
	 * 获得导出数据集的名称
	 *
	 * @return
	 */
	public String getoutDatasetName() {
		if (null != this.outDatasetName) {
			return this.outDatasetName;
		}
		return null;
	}

	/**
	 * 获得导出的数据集
	 *
	 * @return
	 */
	public DatasetVector getoutDataset() {
		if (null != this.outDataset) {
			return this.outDataset;
		}
		return null;
	}
}

