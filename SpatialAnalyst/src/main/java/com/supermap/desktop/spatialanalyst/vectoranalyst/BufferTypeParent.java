package com.supermap.desktop.spatialanalyst.vectoranalyst;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yuanR on 2017/8/10 0010.
 * 缓冲类型（点/面和线）的父类，将两种缓冲类型的公有内容提取出来
 */
public abstract class BufferTypeParent extends JPanel {

	private static final long serialVersionUID = 1L;

	private MapControl mapControl;
	private Recordset selectedRecordset;
	private ArrayList<Recordset> recordsetList;
	private String resultDatasetName;

	private boolean isArcSegmentSuitable = true;
	private boolean isComboBoxDatasetNotNull = true;
	private boolean isShowInMap = true;
	private boolean isRadiusNumSuitable = true;
	private boolean isHasResultDatasource = true;
	private boolean isBufferSucceed;

	private DoSome some;
	private DatasetVector sourceDatasetVector;
	private DatasetVector resultDatasetVector;

	private BufferProgressCallable bufferProgressCallable;
	private final static int DEFAULT_MIN = 4;
	private final static int DEFAULT_MAX = 200;

//	PanelBufferData panelBufferData;
//	PanelResultData panelResultData;


	protected BufferTypeParent() {
	}

	/**
	 * 根据数据类型获得缓冲数据类型
	 *
	 * @return
	 */
	protected void getPanelResultData(DatasetType datasetType, PanelBufferData panelBufferData) {
		int layersCount;
		// 判断mapControl是否打开--yuanR 2017.8.10
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

						if (datasetType.equals(DatasetType.LINE)) {
							if (layer.getDataset().getType() == DatasetType.LINE || layer.getDataset().getType() == DatasetType.NETWORK) {
								if (layer.getSelection() != null && layer.getSelection().getCount() != 0) {
									this.recordsetList.add(layer.getSelection().toRecordset());
									panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(layer.getDataset().getDatasource());
									panelBufferData.getComboBoxBufferDataDataset().setDatasets(layer.getDataset().getDatasource().getDatasets());
									panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(layer.getDataset());
									panelBufferData.getCheckBoxGeometrySelect().setEnabled(true);
									panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
									setComponentEnabled();
								}
							}
						} else {
							if (layer.getDataset().getType() == DatasetType.POINT || layer.getDataset().getType() == DatasetType.REGION) {
								if (layer.getSelection() != null && layer.getSelection().getCount() != 0) {
									this.recordsetList.add(layer.getSelection().toRecordset());
									panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(layer.getDataset().getDatasource());
									panelBufferData.getComboBoxBufferDataDataset().setDatasets(layer.getDataset().getDatasource().getDatasets());
									panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(layer.getDataset());
									panelBufferData.getCheckBoxGeometrySelect().setEnabled(true);
									panelBufferData.getCheckBoxGeometrySelect().setSelected(true);
									setComponentEnabled();
								}
							}
						}
					}


					// 所有图层所指的数据都不存在或者图层对应的数据都不符合线缓冲区面板要求,此时选择tree节点进行缓冲区初始化--yuanR 2017.3.10
					if (this.recordsetList.size() <= 0) {
						setWorkspaceTreeNode(panelBufferData);
					}
				} else {
					setWorkspaceTreeNode(panelBufferData);
				}
			} else {
				setWorkspaceTreeNode(panelBufferData);
			}
		} else {
			setWorkspaceTreeNode(panelBufferData);
		}
	}

	private void setWorkspaceTreeNode(PanelBufferData panelBufferData) {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		TreePath selectedPath = workspaceTree.getSelectionPath();
		if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();
			if (nodeData.getData() instanceof Datasource) {
				Datasource selectedDatasource = (Datasource) nodeData.getData();
				panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDatasource);
				panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDatasource.getDatasets());
				if (panelBufferData.getComboBoxBufferDataDataset().getSelectedDataset() == null) {
					setComboBoxDatasetNotNull(false);
				}
			} else if (nodeData.getData() instanceof Dataset) {
				Dataset selectedDataset = (Dataset) nodeData.getData();
				panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(selectedDataset.getDatasource());
				panelBufferData.getComboBoxBufferDataDataset().setDatasets(selectedDataset.getDatasource().getDatasets());
				if (selectedDataset.getType() == DatasetType.POINT || selectedDataset.getType() == DatasetType.REGION) {
					panelBufferData.getComboBoxBufferDataDataset().setSelectedDataset(selectedDataset);
				}
			} else {
				initDatasourceAndDataSet(panelBufferData);
			}
		} else {
			initDatasourceAndDataSet(panelBufferData);
		}
		panelBufferData.getCheckBoxGeometrySelect().setEnabled(false);
	}

	private void initDatasourceAndDataSet(PanelBufferData panelBufferData) {
		Datasource defaultDatasource = Application.getActiveApplication().getWorkspace().getDatasources().get(0);
		panelBufferData.getComboBoxBufferDataDatasource().setSelectedDatasource(defaultDatasource);
		panelBufferData.getComboBoxBufferDataDataset().setDatasets(defaultDatasource.getDatasets());
	}


	protected abstract void setComboBoxDatasetNotNull(Boolean bol);

	protected abstract void setComponentEnabled();
}
