package com.supermap.desktop.ui;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.*;
import com.supermap.desktop.controls.property.WorkspaceTreeDataPropertyFactory;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.controls.utilities.SymbolDialogFactory;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.ui.controls.WorkspaceTreeTransferHandler;
import com.supermap.desktop.utilities.SystemPropertyUtilities;
import com.supermap.layout.MapLayout;
import com.supermap.realspace.Scene;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorkspaceComponentManager extends JComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JScrollPane jScrollPane = null;
	private transient WorkspaceTree workspaceTree = null;
	// 临时的变量，现在还没有自动加载Dockbar，所以暂时用这个变量测试
	private boolean isContextMenuBuilded = false;
	private TreePath[] oldSelectedTreePaths;

	private static final int DATASET_TYPE_TABULAR = 1;
	private static final int DATASET_TYPE_VECTOR = 2;
	private static final int DATASET_TYPE_IMAGE = 4;
	private static final int DATASET_TYPE_GRID = 8;
	private static final int DATASET_TYPE_IMAGECOLLECTION = 16;
	private static final int DATASET_TYPE_IMAGECOLLECTIONITEM = 32;
	private static final int DATASET_TYPE_GRIDCOLLECTIONITEM = 64;
	private static final int DATASET_TYPE_TOPOLOGY = 128;
	private JPopupMenu workflowsPopupMenu;
	private JPopupMenu workflowPopupMenu;

	public WorkspaceComponentManager() {

		initializeComponent();
		initializeResources();
		initilize(Application.getActiveApplication().getWorkspace());
	}

	public WorkspaceComponentManager(Workspace workspace) {

		initializeComponent();
		initializeResources();
		initilize(workspace);
	}

	private void initializeComponent() {

		this.jScrollPane = new javax.swing.JScrollPane();
		this.workspaceTree = new WorkspaceTree();
		this.workspaceTree.setDragEnabled(true);
		this.workspaceTree.setShowsRootHandles(true);
		this.workspaceTree.setTransferHandler(new WorkspaceTreeTransferHandler());
		this.workspaceTree.setLayoutsNodeVisible(false);
		if (SystemPropertyUtilities.isWindows()) {
			this.workspaceTree.setScenesNodeVisible(true);
		} else {
			this.workspaceTree.setScenesNodeVisible(false);
		}

		this.jScrollPane.setViewportView(this.workspaceTree);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane,
				javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane,
				javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
	}

	public JScrollPane getJScrollPanel() {
		return this.jScrollPane;
	}

	public WorkspaceTree getWorkspaceTree() {
		return this.workspaceTree;
	}

	public Workspace getWorkspace() {
		return this.workspaceTree.getWorkspace();
	}

	public void setWorkspace(Workspace workspace) {
		this.workspaceTree.setWorkspace(workspace);
	}

	private JPopupMenu workspacePopupMenu = null;

	/**
	 * 获取工作空间管理器中工作空间结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getWorkspacePopupMenu() {
		return this.workspacePopupMenu;
	}

	private JPopupMenu datasourcesPopupMenu = null;

	/**
	 * 获取工作空间管理器中数据源集合的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasourcesPopupMenu() {
		return this.datasourcesPopupMenu;
	}

	private JPopupMenu datasourcePopupMenu = null;

	/**
	 * 获取工作空间管理器中数据源结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasourcePopupMenu() {
		return this.datasourcePopupMenu;
	}

	private JPopupMenu datasetPopupMenu = null;

	/**
	 * 获取工作空间管理器中数据集结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetPopupMenu() {
		return this.datasetPopupMenu;
	}

	private JPopupMenu datasetVectorPopupMenu = null;

	/**
	 * 获取工作空间管理器中矢量数据集结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetVectorPopupMenu() {
		return this.datasetVectorPopupMenu;
	}

	private JPopupMenu datasetTabularPopupMenu = null;

	/**
	 * 获取工作空间管理器中属性表数据集结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetTabularPopupMenu() {
		return this.datasetTabularPopupMenu;
	}

	private JPopupMenu datasetImagePopupMenu = null;

	/**
	 * 获取工作空间管理器中影像数据集结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetImagePopupMenu() {
		return this.datasetImagePopupMenu;
	}

	private JPopupMenu datasetGridPopupMenu = null;

	/**
	 * 获取工作空间管理器中栅格数据集结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetGridPopupMenu() {
		return this.datasetGridPopupMenu;
	}

	private JPopupMenu datasetTopologyPopupMenu = null;

	/**
	 * 获取工作空间管理器中拓扑数据集结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetTopologyPopupMenu() {
		return this.datasetTopologyPopupMenu;
	}

	private JPopupMenu mapsPopupMenu = null;

	/**
	 * 获取工作空间管理器中地图集合结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getMapsPopupMenu() {
		return this.mapsPopupMenu;
	}

	private JPopupMenu mapPopupMenu = null;

	/**
	 * 获取工作空间管理器中地图结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getMapPopupMenu() {
		return this.mapPopupMenu;
	}

	private JPopupMenu layoutsPopupMenu = null;

	/**
	 * 获取工作空间管理器中布局集合结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getLayoutPopupMenu() {
		return this.layoutsPopupMenu;
	}

	private JPopupMenu layoutPopupMenu = null;

	/**
	 * 获取工作空间管理器中布局结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getLayoutsPopupMenu() {
		return this.layoutPopupMenu;
	}

	private JPopupMenu scenesPopupMenu = null;

	/**
	 * 获取工作空间管理器中场景集合结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getScenesPopupMenu() {
		return this.scenesPopupMenu;
	}

	private JPopupMenu scenePopupMenu = null;

	/**
	 * 获取工作空间管理器中场景结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getScenePopupMenu() {
		return this.scenePopupMenu;
	}

	private JPopupMenu resourcesPopupMenu = null;

	/**
	 * 获取工作空间管理器中资源集合结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getResourcesPopupMenu() {
		return this.resourcesPopupMenu;
	}

	private JPopupMenu symbolMarkerPopupMenu = null;

	/**
	 * 获取工作空间管理器中符号库结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getSymbolMarkerPopupMenu() {
		return this.symbolMarkerPopupMenu;
	}

	private JPopupMenu symbolLinePopupMenu = null;

	/**
	 * 获取工作空间管理器中线型库结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getSymbolLinePopupMenu() {
		return this.symbolLinePopupMenu;
	}

	private JPopupMenu symbolFillPopupMenu = null;

	/**
	 * 获取工作空间管理器中填充库结点的右键菜单。
	 *
	 * @return
	 */
	public JPopupMenu getSymbolFillPopupMenu() {
		return this.symbolFillPopupMenu;
	}

	private JPopupMenu datasetGroupPopupMenu = null;

	/**
	 * 获取组下存储的海图数据集(目前主要是海图使用)。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetGroupPopupMenu() {
		return this.datasetGroupPopupMenu;
	}

	private JPopupMenu gridCollectionItemPopupMenu = null;

	/**
	 * 获取栅格数据集集合子节点。
	 *
	 * @return
	 */
	public JPopupMenu getGridCollectionItemPopupMenu() {
		return this.gridCollectionItemPopupMenu;
	}

	private JPopupMenu datasetImageCollectionPopupMenu = null;

	/**
	 * 获取工作空间管理器中影像数据集集合根节点。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetImageCollectionPopupMenu() {
		return this.datasetImageCollectionPopupMenu;
	}

	private JPopupMenu datasetImageCollectionItemPopupMenu = null;

	public JPopupMenu getWorkflowsPopupMenu() {
		return workflowsPopupMenu;
	}

	public JPopupMenu getWorkflowPopupMenu() {
		return workflowPopupMenu;
	}

	/**
	 * 获取工作空间管理器中影像数据集集合子节点。
	 *
	 * @return
	 */
	public JPopupMenu getDatasetImageCollectionItemPopupMenu() {
		return this.datasetImageCollectionItemPopupMenu;
	}

	/**
	 * 创建右键菜单对象
	 */
	private void buildContextMenu() {
		try {

			if (Application.getActiveApplication().getMainFrame() != null) {
				IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();

				this.workspacePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuWorkspace");
				this.datasourcesPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasources");
				this.datasourcePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasource");
				this.datasetPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDataset");
				this.datasetVectorPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetVector");
				this.datasetTabularPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetTabular");
				this.datasetImagePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetImage");
				this.datasetImageCollectionPopupMenu = (JPopupMenu) manager
						.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetImageCollection");
				this.datasetGridPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetGrid");
				this.datasetTopologyPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetTopology");
				this.mapsPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuMaps");
				this.mapPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuMap");
				this.layoutsPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuLayouts");
				this.layoutPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuLayout");
				this.scenesPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuScenes");
				this.scenePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuScene");
				this.resourcesPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuResources");
				this.symbolMarkerPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuSymbolMarker");
				this.symbolLinePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuSymbolLine");
				this.symbolFillPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuSymbolFill");
				this.datasetGroupPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasetGroup");
				this.workflowPopupMenu = (JPopupMenu) manager.get("Supermap.Desktop.UI.WorkspaceControlManager.ContextMenuWorkflow");
				this.workflowsPopupMenu = (JPopupMenu) manager.get("Supermap.Desktop.UI.WorkspaceControlManager.ContextMenuWorkflows");

				this.isContextMenuBuilded = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initilize(Workspace workspace) {
		try {
			this.workspaceTree.setWorkspace(workspace);

			initializeToolBar();
			buildContextMenu();
			this.workspaceTree.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mousePressed(java.awt.event.MouseEvent evt) {
					workspaceTreeMousePressed(evt);
				}
			});
			this.workspaceTree.addTreeSelectionListener(new TreeSelectionListener() {

				@Override
				public void valueChanged(TreeSelectionEvent e) {
					setActiveDatasourcesAndDatasets();
					if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
						setSelectedDataProperty();
					}
				}
			});
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void setActiveDatasourcesAndDatasets() {
		TreePath[] selectedPaths = workspaceTree.getSelectionPaths();
		if (!isLegalSelectedPaths(selectedPaths)) {
			workspaceTree.setSelectionPaths(oldSelectedTreePaths);
		} else {
			oldSelectedTreePaths = selectedPaths == null ? null : selectedPaths.clone();
			if (selectedPaths != null) {
				ArrayList<Datasource> activeDatasources = null;
				ArrayList<Dataset> activeDatasets = null;

				for (TreePath selectedPath : selectedPaths) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
					TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
					if (selectedNodeData != null) {
						Object nodeData = selectedNodeData.getData();
						if (nodeData != null) {
							if (nodeData instanceof Datasource) {
								if (activeDatasources == null) {
									activeDatasources = new ArrayList<Datasource>();
								}
								activeDatasources.add((Datasource) nodeData);
							} else if (nodeData instanceof Dataset) {
								if (activeDatasets == null) {
									activeDatasets = new ArrayList<Dataset>();
								}
								activeDatasets.add((Dataset) nodeData);
							}
						}
					}
				}

				if (activeDatasets != null && !activeDatasets.isEmpty()) {
					Application.getActiveApplication().setActiveDatasets(activeDatasets.toArray(new Dataset[activeDatasets.size()]));
					Application.getActiveApplication().setActiveDatasources(new Datasource[]{activeDatasets.get(0).getDatasource()});
				} else if (activeDatasources != null && !activeDatasources.isEmpty()) {
					Application.getActiveApplication().setActiveDatasets(null);
					Application.getActiveApplication().setActiveDatasources(activeDatasources.toArray(new Datasource[activeDatasources.size()]));
				} else {
					Application.getActiveApplication().setActiveDatasets(null);
					Application.getActiveApplication().setActiveDatasources(null);
				}
			} else {
				Application.getActiveApplication().setActiveDatasets(null);
				Application.getActiveApplication().setActiveDatasources(null);
			}
		}
	}

	/**
	 * 设置选中节点的属性信息
	 */
	public void setSelectedDataProperty() {
		TreePath selectedPath = this.workspaceTree.getSelectionPath();

		if (selectedPath != null && selectedPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			TreeNodeData nodeData = (TreeNodeData) selectedNode.getUserObject();

			IProperty[] properties = WorkspaceTreeDataPropertyFactory.createProperties(nodeData);
			IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
			propertyManager.setProperty(properties);
		} else {
			Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
		}
	}

	private boolean isLegalSelectedPaths(TreePath[] selectPaths) {
		// TODO REDO
		if (null == selectPaths || 0 >= selectPaths.length) {
			// 不选中
			return true;
		}
		Object type = ((TreeNodeData) ((DefaultMutableTreeNode) selectPaths[0].getLastPathComponent()).getUserObject()).getData();
		for (int i = 1; i < selectPaths.length; i++) {
			Object typeTemp = ((TreeNodeData) ((DefaultMutableTreeNode) selectPaths[i].getLastPathComponent()).getUserObject()).getData();
			if (type instanceof Dataset) {
				if (typeTemp instanceof Dataset) {
					if (((Dataset) type).getDatasource() != ((Dataset) typeTemp).getDatasource()) {
						return false;
					} else {
						continue;
					}
				} else {
					return false;
				}
			}
			if (type.getClass() != typeTemp.getClass()) {
				return false;
			}
		}
		return true;
	}

	private void initializeToolBar() {
		// 默认实现，后续进行初始化操作
	}

	private void initializeResources() {
		// 默认实现，后续进行初始化操作
	}

	private void workspaceTreeMousePressed(java.awt.event.MouseEvent evt) {
		try {
			int buttonType = evt.getButton();
			int clickCount = evt.getClickCount();

			// 右键
			if (buttonType == MouseEvent.BUTTON3 && clickCount == 1) {
				TreePath[] selectedPaths = this.workspaceTree.getSelectionPaths();
				CopyOnWriteArrayList<TreeNodeData> selectedNodeDatas = new CopyOnWriteArrayList<TreeNodeData>();
				for (TreePath selectedPath : selectedPaths) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
					TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
					selectedNodeDatas.add(selectedNodeData);
				}

				if (!this.isContextMenuBuilded) {
					this.buildContextMenu();
				}

				JPopupMenu popupMenu = this.getPoputMenu(selectedNodeDatas);
				if (popupMenu != null) {
					popupMenu.show(this.workspaceTree, evt.getX(), evt.getY());
				}
			} else if (buttonType == MouseEvent.BUTTON1 && clickCount == 2 && this.getWorkspaceTree().getLastSelectedPathComponent() != null) {
				// 双击
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.workspaceTree.getLastSelectedPathComponent();
				TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
				String nodeText = selectedNodeData.getData().toString();

				if (selectedNodeData.getData() instanceof Dataset) {
					if (!GlobalParameters.isShowDataInNewWindow() && Application.getActiveApplication().getActiveForm() != null && Application.getActiveApplication().getActiveForm().getWindowType() == WindowType.MAP) {
						// 添加到当前地图
						MapViewUIUtilities.addDatasetsToMap(((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap(),
								new Dataset[]{(Dataset) selectedNodeData.getData()}, true);
					} else {
						// 添加到新地图
						MapViewUIUtilities.addDatasetsToNewWindow(new Dataset[]{(Dataset) selectedNodeData.getData()}, true);
					}
				} else if (selectedNodeData.getType() == NodeDataType.MAP_NAME) {
					TreePath[] selectedPaths = this.workspaceTree.getSelectionPaths();
					for (int i = 0; i < selectedPaths.length; i++) {
						nodeText = ((TreeNodeData) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject()).getData().toString();
						MapViewUIUtilities.openMap(nodeText);
//						ToolbarUIUtilities.updataToolbarsState();
					}
				} else if (selectedNodeData.getType() == NodeDataType.SCENE_NAME) {
					TreePath[] selectedPaths = this.workspaceTree.getSelectionPaths();
					for (int i = 0; i < selectedPaths.length; i++) {
						boolean isExist = false;
						nodeText = ((TreeNodeData) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject()).getData().toString();
						for (int i1 = 0; i1 < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); i1++) {
							IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
							if (formManager.get(i1).getText().equals(nodeText) && formManager.get(i1).getWindowType() == WindowType.SCENE) {
								isExist = true;
								if (i == selectedPaths.length - 1) {
									formManager.setActiveForm(formManager.get(i1));
								}
								break;
							}
						}
						if (isExist) {
							continue;
						}
						IFormScene formScene = (IFormScene) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.SCENE, nodeText);
						if (formScene != null) {
							Scene scene = formScene.getSceneControl().getScene();
							// add by huchenpu 20150706
							// 这里必须要设置工作空间，否则不能显示出来。
							// 而且不能在new SceneControl的时候就设置工作空间，必须等球显示出来的时候才能设置。
							formScene.setWorkspace(Application.getActiveApplication().getWorkspace());
							scene.open(nodeText);
							scene.refresh();
							UICommonToolkit.getLayersManager().setScene(scene);
						}
					}

				} else if (selectedNodeData.getType() == NodeDataType.LAYOUT_NAME) {
					TreePath[] selectedPaths = this.workspaceTree.getSelectionPaths();
					for (int i = 0; i < selectedPaths.length; i++) {
						nodeText = ((TreeNodeData) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject()).getData().toString();
						IFormLayout formLayout = (IFormLayout) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.LAYOUT, nodeText);
						if (formLayout != null) {
							MapLayout mapLayout = formLayout.getMapLayoutControl().getMapLayout();
							mapLayout.open(nodeText);
							mapLayout.refresh();
							UICommonToolkit.getLayersManager().setMap(null);
							UICommonToolkit.getLayersManager().setScene(null);
						}
					}
				} else if (selectedNodeData.getType() == NodeDataType.WORKFLOW) {
					String name = (String) ((TreeNodeData) ((DefaultMutableTreeNode) workspaceTree.getLastSelectedPathComponent()).getUserObject()).getData();
					CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.WORKFLOW, name);
				} else if (selectedNodeData.getType() == NodeDataType.SYMBOL_MARKER_LIBRARY) {
					SymbolDialogFactory.getSymbolDialog(SymbolType.MARKER).showDialog(new GeoStyle());
				} else if (selectedNodeData.getType() == NodeDataType.SYMBOL_LINE_LIBRARY) {
					SymbolDialogFactory.getSymbolDialog(SymbolType.LINE).showDialog(new GeoStyle());
				} else if (selectedNodeData.getType() == NodeDataType.SYMBOL_FILL_LIBRARY) {
					SymbolDialogFactory.getSymbolDialog(SymbolType.FILL).showDialog(new GeoStyle());
				}
			} else if (buttonType == MouseEvent.BUTTON1 && clickCount == 1 && -1 == this.workspaceTree.getRowForLocation(evt.getX(), evt.getY())) {
				// 单击空白处
				this.workspaceTree.clearSelection();
				Application.getActiveApplication().setActiveDatasets(null);
				Application.getActiveApplication().setActiveDatasources(null);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private JPopupMenu getPoputMenu(CopyOnWriteArrayList<TreeNodeData> nodeDatas) {

		JPopupMenu popupMenu = null;
		try {
			int dataType = 0;
			for (int i = 0; i < nodeDatas.size(); i++) {
				NodeDataType type = nodeDatas.get(i).getType();
				if (type == NodeDataType.WORKSPACE) {
					popupMenu = this.workspacePopupMenu;
					break;
				} else if (type == NodeDataType.DATASOURCES) {
					popupMenu = this.datasourcesPopupMenu;
					break;
				} else if (type == NodeDataType.DATASOURCE) {
					popupMenu = this.datasourcePopupMenu;
					break;
				} else if (type == NodeDataType.MAPS) {
					popupMenu = this.mapsPopupMenu;
					break;
				} else if (type == NodeDataType.MAP_NAME) {
					popupMenu = this.mapPopupMenu;
					break;
				} else if (type == NodeDataType.LAYOUTS) {
					popupMenu = this.layoutsPopupMenu;
					break;
				} else if (type == NodeDataType.LAYOUT_NAME) {
					popupMenu = this.layoutPopupMenu;
					break;
				} else if (type == NodeDataType.SCENES) {
					popupMenu = this.scenesPopupMenu;
					break;
				} else if (type == NodeDataType.SCENE_NAME) {
					popupMenu = this.scenePopupMenu;
					break;
				} else if (type == NodeDataType.WORKFLOWS) {
					popupMenu = this.workflowsPopupMenu;
					break;
				} else if (type == NodeDataType.WORKFLOW) {
					popupMenu = this.workflowPopupMenu;
					break;
				} else if (type == NodeDataType.RESOURCES) {
					popupMenu = this.resourcesPopupMenu;
					break;
				} else if (type == NodeDataType.SYMBOL_MARKER_LIBRARY) {
					popupMenu = this.symbolMarkerPopupMenu;
					break;
				} else if (type == NodeDataType.SYMBOL_LINE_LIBRARY) {
					popupMenu = this.symbolLinePopupMenu;
					break;
				} else if (type == NodeDataType.SYMBOL_FILL_LIBRARY) {
					popupMenu = this.symbolFillPopupMenu;
					break;
				} else if (type == NodeDataType.DATASET_VECTOR) {
					Dataset dataset = (Dataset) nodeDatas.get(i).getData();
					if (dataset.getType() == DatasetType.TABULAR) {
						dataType |= DATASET_TYPE_TABULAR;
					} else {
						dataType |= DATASET_TYPE_VECTOR;
					}
					continue;
				} else if (type == NodeDataType.DATASET_IMAGE) {
					dataType |= DATASET_TYPE_IMAGE;
				} else if (type == NodeDataType.DATASET_GRID) {
					dataType |= DATASET_TYPE_GRID;
				} else if (type == NodeDataType.DATASET_IMAGE_COLLECTION) {
					dataType |= DATASET_TYPE_IMAGECOLLECTION;
				} else if (type == NodeDataType.DATASET_IMAGE_COLLECTION_ITEM) {
					dataType |= DATASET_TYPE_IMAGECOLLECTIONITEM;
				} else if (type == NodeDataType.DATASET_GRID_COLLECTION) {
					// 默认实现
				} else if (type == NodeDataType.DATASET_GRID_COLLECTION_ITEM) {
					dataType |= DATASET_TYPE_GRIDCOLLECTIONITEM;
				} else if (type == NodeDataType.DATASET_TOPOLOGY) {
					dataType |= DATASET_TYPE_TOPOLOGY;
				}
			}
			if (popupMenu == null && !nodeDatas.isEmpty() && NodeDataType.RESOURCES != nodeDatas.get(0).getType()) {
				// 为空，不为资源节点
				popupMenu = getPopupMenuByDataType(dataType);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return popupMenu;
	}

	private JPopupMenu getPopupMenuByDataType(int dataType) {
		JPopupMenu popupMenu = null;
		if (dataType == DATASET_TYPE_TABULAR) {
			popupMenu = getDatasetTabularPopupMenu();
		} else if (dataType == DATASET_TYPE_VECTOR) {
			popupMenu = getDatasetVectorPopupMenu();
		} else if (dataType == DATASET_TYPE_IMAGE) {
			popupMenu = getDatasetImagePopupMenu();
		} else if (dataType == DATASET_TYPE_GRID) {
			popupMenu = getDatasetGridPopupMenu();
		} else if (dataType == DATASET_TYPE_IMAGECOLLECTION) {
			popupMenu = getDatasetImageCollectionPopupMenu();
		} else if (dataType == DATASET_TYPE_IMAGECOLLECTIONITEM) {
			popupMenu = getDatasetImageCollectionItemPopupMenu();
		} else if (dataType == DATASET_TYPE_GRIDCOLLECTIONITEM) {
			popupMenu = getGridCollectionItemPopupMenu();
		} else if (dataType == DATASET_TYPE_TOPOLOGY) {
			popupMenu = getDatasetTopologyPopupMenu();
		} else {
			popupMenu = getDatasetPopupMenu();
		}
		return popupMenu;
	}

	public void selectDatasources(Datasource[] datasources) {
		if (this.workspaceTree != null
				&& isDatasourcesAllInWorkspace(datasources)) {

			// 获取对应数据源的 TreePath
			ArrayList<TreePath> paths = new ArrayList<>();
			for (int i = 0; i < datasources.length; i++) {
				TreePath path = findDatasourcePath(datasources[i]);

				if (path != null) {
					paths.add(path);
				}
			}

			// 选中指定的数据源
			if (paths.size() > 0) {
				this.workspaceTree.addSelectionPaths(paths.toArray(new TreePath[paths.size()]));
			}

			// 展开 Datasources 节点
			DefaultMutableTreeNode datasourcesNode = this.workspaceTree.getDatasourcesNode();
			TreePath datasourcesPath = new TreePath(datasourcesNode.getPath());
			this.workspaceTree.expandPath(datasourcesPath);
		}
	}

	private TreePath findDatasourcePath(Datasource datasource) {
		TreePath path = null;

		if (this.workspaceTree != null
				&& datasource != null
				&& datasource.getWorkspace() == this.getWorkspace()) {
			DefaultMutableTreeNode datasourcesNode = this.workspaceTree.getDatasourcesNode();

			for (int i = 0; datasourcesNode != null && i < datasourcesNode.getChildCount(); i++) {
				DefaultMutableTreeNode datasourceNode = (DefaultMutableTreeNode) datasourcesNode.getChildAt(i);
				TreeNodeData datasourceNodeData = (TreeNodeData) datasourceNode.getUserObject();

				if (datasourceNodeData != null) {
					Object nodeData = datasourceNodeData.getData();

					if (nodeData == datasource) {
						path = new TreePath(datasourceNode.getPath());
						break;
					}
				}
			}
		}
		return path;
	}

	/*
	* 判断是否所有数据源都属于当前工作空间
	*/
	private boolean isDatasourcesAllInWorkspace(Datasource[] datasources) {
		boolean isDatasourcesAllInWorkspace = true;

		if (this.workspaceTree != null
				&& this.workspaceTree.getWorkspace() != null
				&& datasources != null
				&& datasources.length > 0) {
			for (int i = 0; i < datasources.length; i++) {
				if (datasources[i] != null && datasources[i].getWorkspace() != this.workspaceTree.getWorkspace()) {
					isDatasourcesAllInWorkspace = false;
					break;
				}
			}
		}
		return isDatasourcesAllInWorkspace;
	}
}
