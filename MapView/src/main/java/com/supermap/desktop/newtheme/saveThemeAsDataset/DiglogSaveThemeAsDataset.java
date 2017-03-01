package com.supermap.desktop.newtheme.saveThemeAsDataset;

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasource;
import com.supermap.data.EncodeType;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DatasetTypeComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.SystemPropertyUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;

import static com.supermap.desktop.ui.UICommonToolkit.getWorkspaceManager;

/**
 * @author YuanR  2016 12.21
 *         标签专题图保存为cad/文本数据集
 *         yuanR  2017.3.1
 *         代码优化
 */
public class DiglogSaveThemeAsDataset extends SmDialog {
	private JPanel panel;
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

	public final static Dimension DEFAULT_WINDOWS_DIMENSION = new Dimension(300, 150);
	public final static Dimension DEFAULT_LINUX_DIMENSION = new Dimension(300, 140);
	public final static int GAPDIMENSION =105;
	public final static int intMRecordaxCount=5000;

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
		//根据不同操作系统设置不同的面板尺寸
		if (SystemPropertyUtilities.isWindows()) {
			this.setSize(DEFAULT_WINDOWS_DIMENSION);
		} else {
			this.setSize(DEFAULT_LINUX_DIMENSION);
		}

		this.setLocationRelativeTo(null);
		this.setResizable(false);

		this.datasourcesLabel = new JLabel();
		this.datasourceTypeLabel = new JLabel();
		this.datasourceNameLabel = new JLabel();

		this.datasourceComboBox = new DatasourceComboBox();

		this.datasetTypes = new DatasetType[2];
		this.datasetTypes[0] = DatasetType.CAD;
		this.datasetTypes[1] = DatasetType.TEXT;

		this.datasetTypeComboBox = new DatasetTypeComboBox(this.datasetTypes);

		this.textFieldoutDatasetName = new JTextField();

		this.buttonOk = new SmButton();
		this.buttonCancel = new SmButton();
	}

	/**
	 * 初始化导出到的数据源
	 */
	private void initToDatasource() {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();//返回最后选定的节点
		if (null != selectedNode) {
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (null == selectedNodeData) {
				return;
			}
			//节点位置为数据源项
			if (selectedNodeData.getData() instanceof Datasource) {
				this.datasourceComboBox.setSelectedDatasource((Datasource) selectedNodeData.getData());
			}
			//节点位置为数据集
			if (selectedNodeData.getData() instanceof Dataset) {
				this.datasourceComboBox.setSelectedDatasource(((Dataset) selectedNodeData.getData()).getDatasource());
			}
		}
		this.toDatasource = this.datasourceComboBox.getSelectedDatasource();
	}

	/**
	 * 初始化导出的数据类型
	 * 打开面板不操作下拉列表框时默认导出数据为CAD
	 */
	private void initOutDatasetType() {
		this.outDatasetType = DatasetType.CAD;
	}

	/**
	 * 初始化导出数据集名称
	 */
	private void initOutDatasetName() {
		int num = 1;
		this.outDatasetName = ((ThemeUtil.getActiveLayer().getName()).replace("@", "_")).replace("#", "_");
		while (!isAvailableDatasetName(this.outDatasetName)) {
			this.outDatasetName = ((ThemeUtil.getActiveLayer().getName()).replace("@", "_")).replace("#", "_") + "_" + num;
			num++;
		}
		this.textFieldoutDatasetName.setText(this.outDatasetName);
		this.buttonOk.setEnabled(true);
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		this.panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(panel);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		panel.setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasourcesLabel)
								.addComponent(this.datasourceTypeLabel)
								.addComponent(this.datasourceNameLabel))
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(this.datasourceComboBox)
								.addComponent(this.datasetTypeComboBox)
								.addComponent(this.textFieldoutDatasetName)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(GAPDIMENSION)
						.addComponent(this.buttonOk)
						.addGap(ControlDefaultValues.DEFAULT_PREFERREDSIZE_GAP)
						.addComponent(this.buttonCancel)
						));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasourcesLabel)
						.addComponent(this.datasourceComboBox))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.datasourceTypeLabel)
						.addComponent(this.datasetTypeComboBox))
				.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this. datasourceNameLabel)
						.addComponent(this.textFieldoutDatasetName))
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
		this.setTitle(MapViewProperties.getString("String_SaveAsDataset"));
		this.datasourcesLabel.setText(CommonProperties.getString("String_Label_Datasource"));
		this.datasourceTypeLabel.setText(MapViewProperties.getString("String_Label_DatasetType"));
		this.datasourceNameLabel.setText(ControlsProperties.getString("String_Label_DatasetName"));
		this.buttonCancel.setText(CommonProperties.getString("String_Button_Cancel"));
		this.buttonOk.setText(CommonProperties.getString("String_Button_OK"));
	}

	/**
	 * 初始化监听
	 */
	private void addListeners() {
		removeListeners();
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
			//当存入数据源改变时，重新初始化保存数据集的名称
			//textFieldoutDatasetName.setText(xxx);会触发文本框的改变监听，所以初始化OutDatasetName之前需要移除一下
			textFieldoutDatasetName.getDocument().removeDocumentListener(JTextFieldDocumentListener);
			initOutDatasetName();
			textFieldoutDatasetName.getDocument().addDocumentListener(JTextFieldDocumentListener);
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
				//设置光标为等待
				CursorUtilities.setWaitCursor(buttonOk);
				CursorUtilities.setWaitCursor(panel);
				//选择了CAD数据集类型
				if (outDatasetType.equals(DatasetType.CAD)) {
					SaveAsCAD();
					//选择了文本数据集类型
				} else if (outDatasetType.equals(DatasetType.TEXT)) {
					SaveAsTEXT();
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
			//设置光标为正常
			CursorUtilities.setDefaultCursor(buttonOk);
			CursorUtilities.setDefaultCursor(panel);
		}
	};

	/**
	 * 保存为cad数据集
	 */
	private void SaveAsCAD() {
		try {
			//进行保存为CAD数据集操作
			DatasetVector datasetCAD = ThemeUtil.getActiveLayer().themeToDatasetVector(this.toDatasource, this.outDatasetName);
			this.outDataset = datasetCAD;
			//标签专题图保存为数据集“BaseMap_R”成功
			Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_LabelThemeSacveAsDatasetSuccessed"), this.outDatasetName));
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_LabelThemeSacveAsDatasetFailed"));
		}
	}

	/**
	 * 保存为文本数据集
	 */
	private void SaveAsTEXT() {
		try {
			//先创建Cad数据集和其记录集
			DatasetVector tempDataset = ThemeUtil.getActiveLayer().themeToDatasetVector(toDatasource, "tempDataset");
			Recordset tempRecordset = tempDataset.getRecordset(false, CursorType.STATIC);
			tempRecordset.moveFirst();
			//创建一个空的文本数据集
			createNullTextDataset();
			//得到创建的文本数据集和其记录集
			DatasetVector resultTextDataset = (DatasetVector) this.toDatasource.getDatasets().get(this.outDatasetName);
			Recordset resultTextRecordset = resultTextDataset.getRecordset(false, CursorType.DYNAMIC);
			resultTextRecordset.moveFirst();
			//批量操作
			Recordset.BatchEditor editor = resultTextRecordset.getBatch();
			// 设置批量更新每次提交的记录数目
			editor.setMaxRecordCount(intMRecordaxCount);
			editor.begin();
			while (!tempRecordset.isEOF()) {
				Geometry tempGeometry = tempRecordset.getGeometry();
				resultTextRecordset.addNew(tempGeometry);
				tempGeometry.dispose();
				tempRecordset.moveNext();
			}
			// 批量操作统一提交
			editor.update();
			//释放资源
			tempRecordset.dispose();
			tempDataset.close();
			//删除创建的cad数据集
			this.toDatasource.getDatasets().delete("tempDataset");
			Application.getActiveApplication().getOutput().output(MessageFormat.format(MapViewProperties.getString("String_LabelThemeSacveAsDatasetSuccessed"), this.outDatasetName));
			//高亮显示新创建的数据集
			DatasetInterval();
		} catch (Exception ex) {
			//删除创建的cad数据集
			this.toDatasource.getDatasets().delete("tempDataset");
			Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_LabelThemeSacveAsDatasetFailed"));
		}
	}

	/**
	 * 使workspacetree中数据集节点高亮显示并显示
	 */
	private void DatasetInterval() {
		//获得选择的数据源节点
		DefaultMutableTreeNode selectedDatasourceNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
		if (null != selectedDatasourceNode) {
			DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) selectedDatasourceNode.getLastChild();
			//将节点高亮显示
			getWorkspaceManager().getWorkspaceTree().setSelectionPath(new TreePath(datasetNode.getPath()));
			//将节点展示出来
			getWorkspaceManager().getWorkspaceTree().scrollPathToVisible(new TreePath(datasetNode.getPath()));
		}
	}

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
		if (isAvailableDatasetName(this.textFieldoutDatasetName.getText())) {
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
		if (this.outDatasetName != null && this.outDatasetType != null) {
			DatasetVectorInfo info = new DatasetVectorInfo(this.outDatasetName, outDatasetType);
			//压缩方式
			info.setEncodeType(EncodeType.NONE);
			try {
				this.outDataset = this.toDatasource.getDatasets().create(info);
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(MapViewProperties.getString("String_BuildTextDatasetFailed"));
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

