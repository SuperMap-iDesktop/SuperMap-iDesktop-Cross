package com.supermap.desktop.ui.controls;

import com.supermap.data.Workspace;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 工作空间树单元格渲染器
 *
 * @author xuzw
 */
class WorkspaceTreeCellRenderer extends DefaultTreeCellRenderer {

	private transient Workspace currentWorkspace;

	public Workspace getCurrentWorkspace() {
		return currentWorkspace;
	}

	public void setCurrentWorkspace(Workspace currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}

	Color backGroundColor;

	private transient Map<NodeDataType, ArrayList<ArrayList<TreeNodeDecorator>>> decoratorsMap;

	WorkspaceTreeCellRenderer(Workspace workspace) {

		currentWorkspace = workspace;
		backGroundColor = new Color(150, 185, 255);
		initDecoratorsMap();
	}

	public WorkspaceTreeCellRenderer() {
		currentWorkspace = new Workspace();
		backGroundColor = new Color(150, 185, 255);
		initDecoratorsMap();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		TreeNodeData data = (TreeNodeData) node.getUserObject();
		// 目前cellEditor中编辑时图标用的是CellRender中的图标，
		// 所以在修改返回的Component类型时要同步修改CellEditor中的获取方式，不然可能获取不到图标
		Component component = getPanel(data);
		if (sel) {
			component.setBackground(new Color(150, 185, 255));
		} else {
			component.setBackground(Color.WHITE);
		}
		return component;
	}

	public void setDecoratorLists(NodeDataType type, ArrayList<ArrayList<TreeNodeDecorator>> lists) {
		decoratorsMap.put(type, lists);
	}

	public ArrayList<ArrayList<TreeNodeDecorator>> getDecoratorLists(NodeDataType type) {
		return decoratorsMap.get(type);
	}

	JPanel getPanel(TreeNodeData data) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = decoratorsMap.get(data.getType());
		for (ArrayList<TreeNodeDecorator> list : arrayList) {
			JLabel label = new JLabel(new ImageIcon());
			for (TreeNodeDecorator decorator : list) {
				decorator.decorate(label, data);
			}
			panel.add(label);
		}
		return panel;
	}

	private void initDecoratorsMap() {
		decoratorsMap = new HashMap<NodeDataType, ArrayList<ArrayList<TreeNodeDecorator>>>();

		ArrayList<ArrayList<TreeNodeDecorator>> workspaceDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> workspaceList = new ArrayList<TreeNodeDecorator>();
		workspaceList.add(new WorkspaceNodeDecorator());
		workspaceDecoratorList.add(workspaceList);
		decoratorsMap.put(NodeDataType.WORKSPACE, workspaceDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> vectorDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> vectorList = new ArrayList<TreeNodeDecorator>();
		vectorList.add(new DatasetVectorNodeDecorator(NodeDataType.DATASET_VECTOR));
		vectorDecoratorList.add(vectorList);
		decoratorsMap.put(NodeDataType.DATASET_VECTOR, vectorDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> vectorItemDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> vectorItemList = new ArrayList<TreeNodeDecorator>();
		vectorItemList.add(new DatasetVectorItemNodeDecorator(NodeDataType.DATASET_VECTOR_ITEM));
		vectorItemDecoratorList.add(vectorItemList);
		decoratorsMap.put(NodeDataType.DATASET_VECTOR_ITEM, vectorItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> imageDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> imageList = new ArrayList<TreeNodeDecorator>();
		imageList.add(new DatasetImageNodeDecorator());
		imageDecoratorList.add(imageList);
		decoratorsMap.put(NodeDataType.DATASET_IMAGE, imageDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> gridDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> gridList = new ArrayList<TreeNodeDecorator>();
		gridList.add(new DatasetGridNodeDecorator());
		gridDecoratorList.add(gridList);
		decoratorsMap.put(NodeDataType.DATASET_GRID, gridDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> volumeDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> volumeList = new ArrayList<TreeNodeDecorator>();
		volumeList.add(new DatasetVolumeNodeDecorator());
		volumeDecoratorList.add(volumeList);
		decoratorsMap.put(NodeDataType.DATASET_VOLUME, volumeDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> relationShipDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> relationShipList = new ArrayList<TreeNodeDecorator>();
		relationShipList.add(new DatasetRelationshipNodeDecorator());
		relationShipDecoratorList.add(relationShipList);
		decoratorsMap.put(NodeDataType.DATASET_RELATION_SHIP, relationShipDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> topologyDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> topologyList = new ArrayList<TreeNodeDecorator>();
		topologyList.add(new DatasetTopologyNodeDecorator());
		topologyDecoratorList.add(topologyList);
		decoratorsMap.put(NodeDataType.DATASET_TOPOLOGY, topologyDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> topologyErrorDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> topologyErrorList = new ArrayList<TreeNodeDecorator>();
		topologyErrorList.add(new TopologyErrorDatasetsNodeDecorator());
		topologyErrorDecoratorList.add(topologyErrorList);
		decoratorsMap.put(NodeDataType.TOPOLOGY_ERROR_DATASETS, topologyErrorDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> topologyDatasetRelationItemsDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> topologyDatasetRelationItemsList = new ArrayList<TreeNodeDecorator>();
		topologyDatasetRelationItemsList.add(new TopologyDatasetRelationItemsNodeDecorator());
		topologyDatasetRelationItemsDecoratorList.add(topologyDatasetRelationItemsList);
		decoratorsMap.put(NodeDataType.TOPOLOGY_DATASET_RELATIONS, topologyDatasetRelationItemsDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> datasetGridCollectionDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> datasetGridCollectionList = new ArrayList<TreeNodeDecorator>();
		datasetGridCollectionList.add(new DatasetGridCollectionNodeDecorator());
		datasetGridCollectionDecoratorList.add(datasetGridCollectionList);
		decoratorsMap.put(NodeDataType.DATASET_GRID_COLLECTION, datasetGridCollectionDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> datasetGridCollectionItemDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> datasetGridCollectionItemList = new ArrayList<TreeNodeDecorator>();
		datasetGridCollectionItemList.add(new DatasetGridCollectionItemNodeDecorator());
		datasetGridCollectionItemDecoratorList.add(datasetGridCollectionItemList);
		decoratorsMap.put(NodeDataType.DATASET_GRID_COLLECTION_ITEM, datasetGridCollectionItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> datasetImageCollectionDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> datasetImageCollectionList = new ArrayList<TreeNodeDecorator>();
		datasetImageCollectionList.add(new DatasetImageCollectionNodeDecorator());
		datasetImageCollectionDecoratorList.add(datasetImageCollectionList);
		decoratorsMap.put(NodeDataType.DATASET_IMAGE_COLLECTION, datasetImageCollectionDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> datasetImageCollectionItemDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> datasetImageCollectionItemList = new ArrayList<TreeNodeDecorator>();
		datasetImageCollectionItemList.add(new DatasetImageCollectionItemNodeDecorator());
		datasetImageCollectionItemDecoratorList.add(datasetImageCollectionItemList);
		decoratorsMap.put(NodeDataType.DATASET_IMAGE_COLLECTION_ITEM, datasetImageCollectionItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> datasourcesDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> datasourcesList = new ArrayList<TreeNodeDecorator>();
		datasourcesList.add(new DatasourcesNodeDecorator());
		datasourcesDecoratorList.add(datasourcesList);
		decoratorsMap.put(NodeDataType.DATASOURCES, datasourcesDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> datasourceDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> datasourceList = new ArrayList<TreeNodeDecorator>();
		datasourceList.add(new DatasourceNodeDecorator());
		datasourceDecoratorList.add(datasourceList);
		decoratorsMap.put(NodeDataType.DATASOURCE, datasourceDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> mapsDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> mapsList = new ArrayList<TreeNodeDecorator>();
		mapsList.add(new MapsNodeDecorator());
		mapsDecoratorList.add(mapsList);
		decoratorsMap.put(NodeDataType.MAPS, mapsDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> mapDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> mapList = new ArrayList<TreeNodeDecorator>();
		mapList.add(new MapNameNodeDecorator());
		mapDecoratorList.add(mapList);
		decoratorsMap.put(NodeDataType.MAP_NAME, mapDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> layoutsDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> layoutsList = new ArrayList<TreeNodeDecorator>();
		layoutsList.add(new LayoutsNodeDecorator());
		layoutsDecoratorList.add(layoutsList);
		decoratorsMap.put(NodeDataType.LAYOUTS, layoutsDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> layoutDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> layoutList = new ArrayList<TreeNodeDecorator>();
		layoutList.add(new LayoutNameNodeDecorator());
		layoutDecoratorList.add(layoutList);
		decoratorsMap.put(NodeDataType.LAYOUT_NAME, layoutDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> scenesDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> scenesList = new ArrayList<TreeNodeDecorator>();
		scenesList.add(new ScenesNodeDecorator());
		scenesDecoratorList.add(scenesList);
		decoratorsMap.put(NodeDataType.SCENES, scenesDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> sceneDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> sceneList = new ArrayList<TreeNodeDecorator>();
		sceneList.add(new SceneNameNodeDecorator());
		sceneDecoratorList.add(sceneList);
		decoratorsMap.put(NodeDataType.SCENE_NAME, sceneDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> resourcesDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> resourcesList = new ArrayList<TreeNodeDecorator>();
		resourcesList.add(new ResourcesNodeDecorator());
		resourcesDecoratorList.add(resourcesList);
		decoratorsMap.put(NodeDataType.RESOURCES, resourcesDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> symbolFillLibDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> symbolFillLibList = new ArrayList<TreeNodeDecorator>();
		symbolFillLibList.add(new SymbolFillLibNodeDecorator());
		symbolFillLibDecoratorList.add(symbolFillLibList);
		decoratorsMap.put(NodeDataType.SYMBOL_FILL_LIBRARY, symbolFillLibDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> symbolLineLibDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> symbolLineList = new ArrayList<TreeNodeDecorator>();
		symbolLineList.add(new SymbolLineLibNodeDecorator());
		symbolLineLibDecoratorList.add(symbolLineList);
		decoratorsMap.put(NodeDataType.SYMBOL_LINE_LIBRARY, symbolLineLibDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> symbolMarkerLibDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> symbolMarkerLibList = new ArrayList<TreeNodeDecorator>();
		symbolMarkerLibList.add(new SymbolMarkerLibNodeDecorator());
		symbolMarkerLibDecoratorList.add(symbolMarkerLibList);
		decoratorsMap.put(NodeDataType.SYMBOL_MARKER_LIBRARY, symbolMarkerLibDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> nullDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		decoratorsMap.put(NodeDataType.UNKNOWN, nullDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> workFlowsDecoratorList = new ArrayList<>();
		ArrayList<TreeNodeDecorator> treeNodeWorkFlowsDecorators = new ArrayList<>();
		treeNodeWorkFlowsDecorators.add(new WorkflowsDecorator());
		workFlowsDecoratorList.add(treeNodeWorkFlowsDecorators);
		decoratorsMap.put(NodeDataType.WORKFLOWS, workFlowsDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> workFlowDecoratorList = new ArrayList<>();
		ArrayList<TreeNodeDecorator> treeNodeWorkFlowDecorators = new ArrayList<>();
		treeNodeWorkFlowDecorators.add(new WorkflowDecorator());
		workFlowDecoratorList.add(treeNodeWorkFlowDecorators);
		decoratorsMap.put(NodeDataType.WORKFLOW, workFlowDecoratorList);

	}
}