package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.spatialanalyst.SpatialAnalystProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DatasetComboBox;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 缓冲区分析-缓冲数据面板
 * yuanR 2017.8.10重构
 */
public class PanelBufferData extends JPanel {


	private static final long serialVersionUID = 1L;
	private JLabel labelDataset;
	private JLabel labelDatasource;
	private DatasetComboBox comboBoxBufferDataDataset;
	private DatasourceComboBox comboBoxBufferDataDatasource;
	private JCheckBox checkBoxGeometrySelect;

	private MapControl mapControl;

	public ArrayList<Recordset> getRecordsetList() {
		return recordsetList;
	}

	private ArrayList<Recordset> recordsetList;

	public JCheckBox getCheckBoxGeometrySelect() {
		return checkBoxGeometrySelect;
	}


	public DatasetComboBox getComboBoxBufferDataDataset() {
		return comboBoxBufferDataDataset;
	}

	public DatasourceComboBox getComboBoxBufferDataDatasource() {
		return comboBoxBufferDataDatasource;
	}

	public PanelBufferData() {
		initComponent();
		setComponentName();
		initResources();
		setPanelBufferDataLayout();

		registerEvent();
	}

	/**
	 * 初始化数据,有两种方式，选中对象或者一个数据集
	 *
	 * @param datasetType
	 */
	public void initDataset(DatasetType datasetType) {
		setComboBoxDatasetType(datasetType);
		int layersCount;
		// 判断mapControl是否打开--yuanR
		if (Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
			this.mapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
			// 通过HashMap使用，当进行选择集的缓冲区分析时，对其图层进行：“去重”、“去坏”处理--yuanR 2017.3.13
			// 获得mapContorl中所有图层，包括分组图层下的
			ArrayList<Layer> arrayList;
			arrayList = MapUtilities.getLayers(this.mapControl.getMap(), true);
			layersCount = arrayList.size();
			//是否存在图层
			if (layersCount > 0) {
				// 遍历所有图层存在的图层，进行去重，去坏处理
				HashMap<Dataset, Layer> layerMap = new HashMap<>();
				for (int i = 0; i < layersCount; i++) {
					if (arrayList.get(i).getDataset() == null) {
						continue;
					}
					layerMap.put(arrayList.get(i).getDataset(), arrayList.get(i));
				}
				layersCount = layerMap.size();
				//是否存在不重复的、未损坏的图层
				if (layersCount > 0) {
					this.recordsetList = new ArrayList<>();
					for (Layer layer : layerMap.values()) {
						// 对不重复，未损毁图层进行其数据集类型的筛选
						if (datasetType.equals(DatasetType.LINE)) {
							if (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.NETWORK) {
								if (layer.getSelection() != null && layer.getSelection().getCount() != 0) {
									this.recordsetList.add(layer.getSelection().toRecordset());
									this.getComboBoxBufferDataDatasource().setSelectedDatasource(layer.getDataset().getDatasource());
									this.getComboBoxBufferDataDataset().setDatasets(layer.getDataset().getDatasource().getDatasets());
									this.getComboBoxBufferDataDataset().setSelectedDataset(layer.getDataset());
									this.getCheckBoxGeometrySelect().setEnabled(true);
									this.getCheckBoxGeometrySelect().setSelected(true);
								}
							}
						} else {
							if (layer.getDataset().getType() == DatasetType.POINT || layer.getDataset().getType() == DatasetType.REGION) {
								if (layer.getSelection() != null && layer.getSelection().getCount() != 0) {
									this.recordsetList.add(layer.getSelection().toRecordset());
									this.getComboBoxBufferDataDatasource().setSelectedDatasource(layer.getDataset().getDatasource());
									this.getComboBoxBufferDataDataset().setDatasets(layer.getDataset().getDatasource().getDatasets());
									this.getComboBoxBufferDataDataset().setSelectedDataset(layer.getDataset());
									this.getCheckBoxGeometrySelect().setEnabled(true);
									this.getCheckBoxGeometrySelect().setSelected(true);
								}
							}
						}
					}
					// 所有图层所指的数据都不存在或者图层对应的数据都不符合线缓冲区面板要求,此时选择tree节点进行缓冲区初始化--yuanR 2017.3.10
					if (this.recordsetList.size() <= 0) {
						setWorkspaceTreeNode(datasetType);
					}
				} else {
					setWorkspaceTreeNode(datasetType);
				}
			} else {
				setWorkspaceTreeNode(datasetType);
			}
		} else {
			setWorkspaceTreeNode(datasetType);
		}


	}

	private void setWorkspaceTreeNode(DatasetType datasetType) {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
			if (nodeData.getData() instanceof Datasource) {
				Datasource selectedDatasource = (Datasource) nodeData.getData();
				this.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDatasource);
				this.getComboBoxBufferDataDataset().setDatasets(selectedDatasource.getDatasets());
			} else if (nodeData.getData() instanceof Dataset) {
				Dataset selectedDataset = (Dataset) nodeData.getData();
				this.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
				this.getComboBoxBufferDataDataset().setDatasets(selectedDataset.getDatasource().getDatasets());
				if (datasetType.equals(DatasetType.LINE)) {
					if (selectedDataset.getType() == DatasetType.LINE || selectedDataset.getType() == DatasetType.NETWORK) {
						this.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);
					}
				} else {
					if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.REGION) {
						this.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);
					}
				}
			} else {
				initDatasourceAndDataSet();
			}
		} else {
			initDatasourceAndDataSet();
		}
		this.getCheckBoxGeometrySelect().setEnabled(false);
	}

	private void initDatasourceAndDataSet() {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		this.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		this.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}

	private void setComboBoxDatasetType(DatasetType datasetType) {
		// 暂不支持三维
		ArrayList<DatasetType> datasetTypes = new ArrayList<DatasetType>();
		if (datasetType.equals(DatasetType.LINE)) {
			datasetTypes.add(DatasetType.LINE);
			datasetTypes.add(DatasetType.NETWORK);
		} else {
			datasetTypes.add(DatasetType.POINT);
			datasetTypes.add(DatasetType.REGION);
		}
		this.getComboBoxBufferDataDataset().setSupportedDatasetTypes(datasetTypes.toArray(new DatasetType[datasetTypes.size()]));
	}


	private void registerEvent() {
		checkBoxGeometrySelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				getComboBoxBufferDataDataset().setEnabled(!getCheckBoxGeometrySelect().isSelected());
				getComboBoxBufferDataDatasource().setEnabled(!getCheckBoxGeometrySelect().isSelected());
			}
		});

	}

	private void initComponent() {
		this.labelDataset = new JLabel("Dataset");
		this.labelDatasource = new JLabel("Datasource");
		this.comboBoxBufferDataDatasource = new DatasourceComboBox();
		this.comboBoxBufferDataDataset = new DatasetComboBox();
		this.checkBoxGeometrySelect = new JCheckBox("GeometrySelect");
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.comboBoxBufferDataDataset, "PanelBufferData_comboBoxBufferDataDataset");
		ComponentUIUtilities.setName(this.comboBoxBufferDataDatasource, "PanelBufferData_comboBoxBufferDataDatasource");
		ComponentUIUtilities.setName(this.checkBoxGeometrySelect, "PanelBufferData_checkBoxGeometrySelect");
	}

	private void initResources() {
		this.labelDatasource.setText(ControlsProperties.getString("String_Label_ResultDatasource"));
		this.labelDataset.setText(ControlsProperties.getString("String_Label_ResultDataset"));
		this.checkBoxGeometrySelect.setText(SpatialAnalystProperties.getString("String_CheckeBox_BufferSelectedRecordset"));
	}

	private void setPanelBufferDataLayout() {
		this.setBorder(BorderFactory.createTitledBorder(SpatialAnalystProperties.getString("String_SourceBufferData")));

		GroupLayout panelBufferDataLayout = new GroupLayout(this);
		panelBufferDataLayout.setAutoCreateContainerGaps(true);
		panelBufferDataLayout.setAutoCreateGaps(true);
		this.setLayout(panelBufferDataLayout);
		//@formatter:off
		panelBufferDataLayout.setHorizontalGroup(panelBufferDataLayout.createParallelGroup()
				.addGroup(panelBufferDataLayout.createSequentialGroup()
						.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelDatasource)
								.addComponent(this.labelDataset))
						.addGroup(panelBufferDataLayout.createParallelGroup()
								.addComponent(this.comboBoxBufferDataDatasource, 5, 5, Short.MAX_VALUE)
								.addComponent(this.comboBoxBufferDataDataset, 5, 5, Short.MAX_VALUE)))
				.addGroup(panelBufferDataLayout.createSequentialGroup()
						.addComponent(this.checkBoxGeometrySelect)));

		panelBufferDataLayout.setVerticalGroup(panelBufferDataLayout.createSequentialGroup()
				.addGroup(panelBufferDataLayout.createSequentialGroup()
						.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.CENTER)
								.addComponent(this.labelDatasource)
								.addComponent(this.comboBoxBufferDataDatasource, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(panelBufferDataLayout.createParallelGroup(Alignment.CENTER)
								.addComponent(this.labelDataset)
								.addComponent(this.comboBoxBufferDataDataset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGroup(panelBufferDataLayout.createSequentialGroup()
						.addComponent(this.checkBoxGeometrySelect))
				.addGap(5, 5, Short.MAX_VALUE));

		//@formatter:on
	}


	/**
	 * 创建面板是否可用方法
	 * 2017.3.2 yuanR
	 *
	 * @param isEnable
	 */
	public void setPanelEnable(boolean isEnable) {
		this.comboBoxBufferDataDataset.setEnabled(isEnable);
	}
}
