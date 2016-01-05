package com.supermap.desktop.ui.controls;

/**
 * <p>Title: 工作空间管理器控件</p>
 * <p/>
 * <p>Description: 工作空间管理器控件</p>
 * <p/>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p/>
 * <p>Company: SuperMap GIS Technologies Inc.</p>
 *
 * @author 魏辰东
 * @version 6.0
 */

import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetCollectionChangeOrderEvent;
import com.supermap.data.DatasetCollectionChangeOrderListener;
import com.supermap.data.DatasetCollectionEvent;
import com.supermap.data.DatasetCollectionListener;
import com.supermap.data.DatasetCollectionRemoveAllEvent;
import com.supermap.data.DatasetCollectionRemoveAllListener;
import com.supermap.data.DatasetCollectionRenameEvent;
import com.supermap.data.DatasetCollectionRenameListener;
import com.supermap.data.DatasetCollectionRequireRefreshEvent;
import com.supermap.data.DatasetCollectionRequireRefreshListener;
import com.supermap.data.DatasetCreatedEvent;
import com.supermap.data.DatasetCreatedListener;
import com.supermap.data.DatasetDeletedAllEvent;
import com.supermap.data.DatasetDeletedAllListener;
import com.supermap.data.DatasetDeletedEvent;
import com.supermap.data.DatasetDeletedListener;
import com.supermap.data.DatasetDeletingEvent;
import com.supermap.data.DatasetDeletingListener;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetGridCollection;
import com.supermap.data.DatasetImage;
import com.supermap.data.DatasetImageCollection;
import com.supermap.data.DatasetRenamedEvent;
import com.supermap.data.DatasetRenamedListener;
import com.supermap.data.DatasetTopology;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVolume;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceAliasModifiedEvent;
import com.supermap.data.DatasourceAliasModifiedListener;
import com.supermap.data.DatasourceClosedEvent;
import com.supermap.data.DatasourceClosedListener;
import com.supermap.data.DatasourceCreatedEvent;
import com.supermap.data.DatasourceCreatedListener;
import com.supermap.data.DatasourceOpenedEvent;
import com.supermap.data.DatasourceOpenedListener;
import com.supermap.data.Datasources;
import com.supermap.data.IDisposable;
import com.supermap.data.LayoutAddedEvent;
import com.supermap.data.LayoutAddedListener;
import com.supermap.data.LayoutClearedEvent;
import com.supermap.data.LayoutClearedListener;
import com.supermap.data.LayoutRemovedEvent;
import com.supermap.data.LayoutRemovedListener;
import com.supermap.data.LayoutRenamedEvent;
import com.supermap.data.LayoutRenamedListener;
import com.supermap.data.Layouts;
import com.supermap.data.MapAddedEvent;
import com.supermap.data.MapAddedListener;
import com.supermap.data.MapClearedEvent;
import com.supermap.data.MapClearedListener;
import com.supermap.data.MapRemovedEvent;
import com.supermap.data.MapRemovedListener;
import com.supermap.data.MapRenamedEvent;
import com.supermap.data.MapRenamedListener;
import com.supermap.data.Maps;
import com.supermap.data.Recordset;
import com.supermap.data.Resources;
import com.supermap.data.SceneAddedEvent;
import com.supermap.data.SceneAddedListener;
import com.supermap.data.SceneClearedEvent;
import com.supermap.data.SceneClearedListener;
import com.supermap.data.SceneRemovedEvent;
import com.supermap.data.SceneRemovedListener;
import com.supermap.data.SceneRenamedEvent;
import com.supermap.data.SceneRenamedListener;
import com.supermap.data.Scenes;
import com.supermap.data.SymbolFillLibrary;
import com.supermap.data.SymbolLineLibrary;
import com.supermap.data.SymbolMarkerLibrary;
import com.supermap.data.TopologyDatasetRelationItems;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosedEvent;
import com.supermap.data.WorkspaceClosedListener;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceCreatedEvent;
import com.supermap.data.WorkspaceCreatedListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilties.ToolbarUtilties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.progress.FormProgressTotal;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Map;
import com.supermap.ui.Action;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class WorkspaceTree extends JTree implements IDisposable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private DefaultMutableTreeNode treeNodeWorkspace = null;

	private DefaultMutableTreeNode treeNodeDatasources = null;

	private DefaultMutableTreeNode treeNodeMaps = null;

	private DefaultMutableTreeNode treeNodeScenes = null;

	private DefaultMutableTreeNode treeNodeLayouts = null;

	private DefaultMutableTreeNode treeNodeResources = null;

	private DefaultMutableTreeNode treeNodeFillLibrary = null;

	private DefaultMutableTreeNode treeNodeLineLibrary = null;

	private DefaultMutableTreeNode treeNodeMakerLibrary = null;

	private transient Enumeration<TreePath> datasourcesTreePath = null;

	private transient Enumeration<TreePath> mapsTreePath = null;

	private transient Enumeration<TreePath> layoutsTreePath = null;

	private transient Enumeration<TreePath> scenesTreePath = null;

	private transient Enumeration<TreePath> resourcesTreePath = null;

	private transient Workspace currentWorkspace = null;
	private transient Workspace defaultWorkspace = null;

	private transient Datasources datasources = null;

	private transient Maps maps = null;

	private transient Scenes scenes = null;

	private transient Layouts layouts = null;

	private transient Resources resources = null;

	private transient SymbolFillLibrary symbolFillLibrary = null;

	private transient SymbolLineLibrary symbolLineLibrary = null;

	private transient SymbolMarkerLibrary symbolMarkerLibraray = null;

	private transient DefaultTreeModel treeModelTemp = null;

	private boolean isDatasourcesNodeVisible = true;

	private boolean isMapsNodeVisible = true;

	private boolean isScenesNodeVisible = true;

	private boolean isLayoutsNodeVisible = true;

	private boolean isResourcesNodeVisible = true;

	private int deleteingDatasetIndex = 0;

	private transient WorkspaceTreeCellRenderer treeRenderer = null;

	private transient WorkspaceTreeCellEditor cellEditorTempCellEditor = null;

	private transient WorkspaceTreeWorkspaceClosedListener workspaceClosedListener = null;

	private transient WorkspaceTreeWorkspaceCreatedListener workspaceCreatedListener = null;

	private transient WorkspaceTreeWorkspaceOpenedListener workspaceOpenedListener = null;

	private transient WorkspaceTreeDatasourceAliasModifiedListener datasourceAliasModifiedListener = null;

	private transient WorkspaceTreeDatasourceClosedListener datasourceClosedListener = null;

	private transient WorkspaceTreeDatasourceCreatedListener datasourceCreatedListener = null;

	private transient WorkspaceTreeDatasourceOpenedListener datasourceOpenedListener = null;

	private transient WorkspaceTreeDatasetRenamedListener datasetRenamedListener = null;

	private transient WorkspaceTreeDatasetCreatedListener datasetCreatedListener = null;

	private transient WorkspaceTreeDatasetDeletedAllListener datasetDeletedAllListener = null;

	private transient WorkspaceTreeDatasetDeletedListener datasetDeletedListener = null;

	private transient WorkspaceTreeDatasetDeletingListener datasetDeletingListener = null;

	private transient WorkspaceTreeMapAddedListener mapAddedListener = null;

	private transient WorkspaceTreeMapClearedListener mapClearedListener = null;

	private transient WorkspaceTreeMapRemovedListener mapRemovedListener = null;

	private transient WorkspaceTreeMapRenamedListener mapRenamedListener = null;

	private transient WorkspaceTreeLayoutAddedListener layoutsAddedListener = null;

	private transient WorkspaceTreeLayoutClearedListener layoutsClearedListener = null;

	private transient WorkspaceTreeLayoutRemovedListener layoutsLayoutRemovedListener = null;

	private transient WorkspaceTreeLayoutRenamedListener layoutsLayoutRenamedListener = null;

	private transient WorkspaceTreeSceneAddedListener sceneAddedListener = null;

	private transient WorkspaceTreeSceneClearedListener sceneClearedListener = null;

	private transient WorkspaceTreeSceneRemovedListener sceneRemovedListener = null;

	private transient WorkspaceTreeSceneRenamedListener sceneRenamedListener = null;

	private transient WorkspaceTreeDatasetCollectionAddedListener datasetCollectionAddedListener = null;

	private transient WorkspaceTreeDatasetCollectionRemovedListener datasetCollectionRemovedListener = null;

	private transient WorkspaceTreeDatasetCollectionRemovingListener datasetCollectionRemovingListener = null;

	private transient WorkspaceTreeDatasetCollectionRenamedListener datasetCollectionRenamedListener = null;

	private transient WorkspaceTreeDatasetCollectionClearedListener datasetCollectionClearedListener = null;

	private transient WorkspaceTreeDatasetCollectionRefreshListener datasetCollectionRefreshListener = null;

	private transient WorkspaceTreeDatasetCollectionOrderChangedListener datasetCollectionOrderChangedListener = null;

	private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			ToolbarUtilties.updataToolbarsState();
		}
	};
	// WorkspaceTree里面注册了Deleting事件等，用户用代码而非界面操作删除数据集等，也弹出提示框来
	// 所以增加一个变量控制只有在界面操作时才需要弹出提示框 by gouyu 2013-5-24
	private boolean needShowMessageBox = false;
	private int defaultType = -1;
	private int workspaceType = 0;
	private int datasourceType = 1;
	private DropTarget workspaceDropTarget;

	/**
	 * 默认构造函数
	 */
	public WorkspaceTree() {
		super();
		try {
			currentWorkspace = this.getDefaultWorkspace();
			treeModelTemp = new DefaultTreeModel(treeNodeWorkspace);
			setModel(treeModelTemp);
			buildWorkspaceNode(currentWorkspace);
			this.treeModelTemp.setRoot(this.treeNodeWorkspace);
			init();
			addListener();
			addMouseListener(new WorkspaceTreeMouseListener());
			addKeyListener(new WorkspaceTreeKeyListener());
			DragSource dragSource = DragSource.getDefaultDragSource();// 创建拖拽源
			dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, new WorkspaceTreeDragGestureListener()); // 建立拖拽源和事件的联系
			setWorkspaceDropTarget(new DropTarget(this, new WorkspaceTreeDropTargetAdapter()));
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 构造函数，根据工作空间构建工作空间管理器控件
	 */
	public WorkspaceTree(Workspace workspace) {
		super();
		try {
			if (workspace == null) {
				workspace = this.getDefaultWorkspace();
			}
			currentWorkspace = workspace;
			treeModelTemp = new DefaultTreeModel(treeNodeWorkspace);
			setModel(treeModelTemp);
			buildWorkspaceNode(currentWorkspace);
			this.treeModelTemp.setRoot(this.treeNodeWorkspace);
			init();
			addListener();
			addKeyListener(new WorkspaceTreeKeyListener());
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 设置/获取图例管理器的工作空间
	 */
	public Workspace getWorkspace() {
		return currentWorkspace;
	}

	public void setWorkspace(Workspace workspace) {
		Workspace workspaceTemp = workspace;
		try {
			if (workspaceTemp == null) {
				workspaceTemp = this.getDefaultWorkspace();
			}
			if (!currentWorkspace.equals(workspaceTemp)) {
				removeListener();
				currentWorkspace = workspaceTemp;
				buildWorkspaceNode(currentWorkspace);
				treeModelTemp.setRoot(treeNodeWorkspace);
				init();
				addListener();
				refreshNode(currentWorkspace);
				updateUI();
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 返回/设置数据源集合节点是否可见
	 */
	public boolean isDatasourcesNodeVisible() {
		return isDatasourcesNodeVisible;
	}

	public void setDatasourcesNodeVisible(boolean bool) {
		try {
			if (bool != isDatasourcesNodeVisible) {
				isDatasourcesNodeVisible = bool;
				if (isDatasourcesNodeVisible) {
					treeModelTemp.insertNodeInto(treeNodeDatasources, treeNodeWorkspace, 0);

					for (; datasourcesTreePath != null && datasourcesTreePath.hasMoreElements();) {
						this.setExpandedState(datasourcesTreePath.nextElement(), true);
					}
				} else {
					datasourcesTreePath = this.getExpandedDescendants(new TreePath(treeNodeDatasources.getPath()));
					treeModelTemp.removeNodeFromParent(treeNodeDatasources);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 返回/设置地图集合节点是否可见
	 */
	public boolean isMapsNodeVisible() {
		return isMapsNodeVisible;
	}

	public void setMapsNodeVisible(boolean bool) {
		try {
			if (bool != isMapsNodeVisible) {
				isMapsNodeVisible = bool;

				if (isMapsNodeVisible) {
					if (treeNodeDatasources.getParent() == null) {
						treeModelTemp.insertNodeInto(treeNodeMaps, treeNodeWorkspace, 0);
					} else {
						treeModelTemp.insertNodeInto(treeNodeMaps, treeNodeWorkspace, 1);
					}

					for (; mapsTreePath != null && mapsTreePath.hasMoreElements();) {
						this.setExpandedState(mapsTreePath.nextElement(), true);
					}
				} else {
					mapsTreePath = this.getExpandedDescendants(new TreePath(treeNodeMaps.getPath()));
					treeModelTemp.removeNodeFromParent(treeNodeMaps);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 返回/设置布局集合节点是否可见
	 */
	public boolean isLayoutsNodeVisible() {
		return isLayoutsNodeVisible;
	}

	public void setLayoutsNodeVisible(boolean bool) {
		try {
			if (bool != isLayoutsNodeVisible) {
				isLayoutsNodeVisible = bool;

				if (isLayoutsNodeVisible) {
					if (treeNodeDatasources.getParent() == null && treeNodeMaps.getParent() == null) {
						treeModelTemp.insertNodeInto(treeNodeLayouts, treeNodeWorkspace, 0);
					} else if (treeNodeDatasources.getParent() == null || treeNodeMaps.getParent() == null) {
						treeModelTemp.insertNodeInto(treeNodeLayouts, treeNodeWorkspace, 1);
					} else {
						treeModelTemp.insertNodeInto(treeNodeLayouts, treeNodeWorkspace, 2);
					}

					for (; layoutsTreePath != null && layoutsTreePath.hasMoreElements();) {
						this.setExpandedState(layoutsTreePath.nextElement(), true);
					}
				} else {
					layoutsTreePath = this.getExpandedDescendants(new TreePath(treeNodeLayouts.getPath()));
					treeModelTemp.removeNodeFromParent(treeNodeLayouts);
				}

			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 返回/设置三维场景集合节点是否可见
	 */
	public boolean isScenesNodeVisible() {
		return isScenesNodeVisible;
	}

	public void setScenesNodeVisible(boolean bool) {
		try {

			if (bool != isScenesNodeVisible) {
				isScenesNodeVisible = bool;

				if (isScenesNodeVisible) {
					if (treeNodeResources.getParent() == null) {
						treeModelTemp.insertNodeInto(treeNodeScenes, treeNodeWorkspace, treeNodeWorkspace.getChildCount());
					} else {
						treeModelTemp.insertNodeInto(treeNodeScenes, treeNodeWorkspace, treeNodeWorkspace.getChildCount() - 1);
					}

					for (; scenesTreePath != null && scenesTreePath.hasMoreElements();) {
						this.setExpandedState(scenesTreePath.nextElement(), true);
					}
				} else {
					scenesTreePath = this.getExpandedDescendants(new TreePath(treeNodeScenes.getPath()));
					treeModelTemp.removeNodeFromParent(treeNodeScenes);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 返回/设置资源集合节点是否可见
	 */
	public boolean isResourcesNodeVisible() {
		return isResourcesNodeVisible;
	}

	public void setResourcesNodeVisible(boolean bool) {
		try {
			if (bool != isResourcesNodeVisible) {
				isResourcesNodeVisible = bool;

				if (isResourcesNodeVisible) {
					treeModelTemp.insertNodeInto(treeNodeResources, treeNodeWorkspace, treeNodeWorkspace.getChildCount());

					for (; resourcesTreePath != null && resourcesTreePath.hasMoreElements();) {
						this.setExpandedState(resourcesTreePath.nextElement(), true);
					}

				} else {
					resourcesTreePath = this.getExpandedDescendants(new TreePath(treeNodeResources.getPath()));
					treeModelTemp.removeNodeFromParent(treeNodeResources);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 获取工作空间节点
	 *
	 * @return
	 */
	public DefaultMutableTreeNode getWorkspaceNode() {
		return treeNodeWorkspace;
	}

	/**
	 * 获取数据源集合节点
	 *
	 * @return
	 */
	public DefaultMutableTreeNode getDatasourcesNode() {
		return treeNodeDatasources;
	}

	/**
	 * 获取地图集合节点
	 *
	 * @return
	 */
	public DefaultMutableTreeNode getMapsNode() {
		return treeNodeMaps;
	}

	/**
	 * 获取场景集合节点
	 *
	 * @return
	 */
	public DefaultMutableTreeNode getScenesNode() {
		return treeNodeScenes;
	}

	/**
	 * 获取资源集合节点
	 *
	 * @return
	 */
	public DefaultMutableTreeNode getResourcesNode() {
		return treeNodeResources;
	}

	/**
	 * 当工作空间改变时，重新构建节点并刷新工作空间控件
	 */
	public void reload() {
		if (currentWorkspace != null) {
			try {
				refreshNode(currentWorkspace);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
	}

	/**
	 * 刷新指定节点
	 *
	 * @param node 指定节点
	 */
	public void refreshNode(DefaultMutableTreeNode node) {
		Object userObject = node.getUserObject();
		TreeNodeData data = (TreeNodeData) userObject;
		if (data.getType() == NodeDataType.WORKSPACE) {
			Workspace workspace = (Workspace) data.getData();
			refreshNode(workspace);
		}
		if (data.getType() == NodeDataType.DATASOURCES) {
			Datasources datasourcesTemp = (Datasources) data.getData();
			refreshNode(datasourcesTemp);
		}
		if (data.getType() == NodeDataType.MAPS) {
			Maps mapsTemp = (Maps) data.getData();
			refreshNode(mapsTemp);
		}
		if (data.getType() == NodeDataType.LAYOUTS) {
			Layouts layoutsTemp = (Layouts) data.getData();
			refreshNode(layoutsTemp);
		}
		if (data.getType() == NodeDataType.SCENES) {
			Scenes scenesTemp = (Scenes) data.getData();
			refreshNode(scenesTemp);
		}
		if (data.getType() == NodeDataType.DATASOURCE) {
			refreshDatasourceNode(node);
		}
		updateUI();
	}

	private Workspace getDefaultWorkspace() {
		disposeDefaultWorkspace();
		defaultWorkspace = new Workspace();

		return defaultWorkspace;
	}

	private void disposeDefaultWorkspace() {
		if (defaultWorkspace != null) {
			defaultWorkspace.dispose();
		}
	}

	private void refreshDatasourceNode(DefaultMutableTreeNode datasourceNode) {
		try {
			Object userObject = datasourceNode.getUserObject();
			TreeNodeData data = (TreeNodeData) userObject;
			Datasource datasource = (Datasource) data.getData();

			// 记住刷新之前书的状态
			Enumeration<TreePath> tempTreePath = this.getExpandedDescendants(new TreePath(datasourceNode.getPath()));

			// 第一步：删除子树节点事件监听器，
			Datasets datasets = datasource.getDatasets();
			removeDatasetListener(datasets);

			// 第二步：删除节点，
			datasourceNode.removeAllChildren();

			// 第三步：添加子树，
			addDataset(datasource, datasourceNode);

			// 第四步：添加子树事件监听器
			addDatasetListener(datasets);

			// 恢复到刷新之前的状态
			for (; tempTreePath != null && tempTreePath.hasMoreElements();) {
				this.setExpandedState(tempTreePath.nextElement(), true);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	/**
	 * 刷新工作空间
	 */
	private void refreshNode(Workspace workspace) {
		try {
			// 不应高重做树，因为一些树的状态只保存在对象中，并没有保存在文件中，如果重新打开工作空间，会导致树的状态前后不符！
			// modified by weicd 2010.3.16
			refreshNode(currentWorkspace.getDatasources());
			refreshNode(currentWorkspace.getMaps());
			refreshNode(currentWorkspace.getLayouts());
			refreshNode(currentWorkspace.getScenes());
			updateUI();

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

	}

	/**
	 * 刷新数据源
	 */
	private void refreshNode(Datasources datasources) {
		try {

			// 记住刷新之前树的状态
			Enumeration<TreePath> tempTreePath = this.getExpandedDescendants(new TreePath(treeNodeDatasources.getPath()));

			// 第一步：删除子树节点事件监听器，
			removeDatasourceListener();

			// 第二步：删除节点，
			treeNodeDatasources.removeAllChildren();

			// 第三步：添加子树，
			addDatasource();

			// 第四步：添加子树事件监听器。
			addDatasourceListener();

			// 恢复到刷新之前的状态
			for (; tempTreePath != null && tempTreePath.hasMoreElements();) {
				this.setExpandedState(tempTreePath.nextElement(), true);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 刷新地图节点
	 */
	private void refreshNode(Maps maps) {
		try {
			// 记住刷新之前书的状态
			Enumeration<TreePath> tempTreePath = this.getExpandedDescendants(new TreePath(treeNodeMaps.getPath()));

			// 第一步：删除子树节点事件监听器，
			removeMapListener();

			// 第二步：删除节点，
			treeNodeMaps.removeAllChildren();

			// 第三步：添加子树，
			addMap();

			// 第四步：添加子树事件监听器。
			addMapListener();

			// 恢复到刷新之前的状态
			for (; tempTreePath != null && tempTreePath.hasMoreElements();) {
				this.setExpandedState(tempTreePath.nextElement(), true);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 刷新布局节点
	 */
	private void refreshNode(Layouts layouts) {
		try {
			// 记住刷新之前书的状态
			Enumeration<TreePath> tempTreePath = this.getExpandedDescendants(new TreePath(treeNodeLayouts.getPath()));

			// 第一步：删除子树节点事件监听器，
			removeLayoutsListener();

			// 第二步：删除节点，
			treeNodeLayouts.removeAllChildren();

			// 第三步：添加子树，
			addLayout();

			// 第四步：添加子树事件监听器。
			addLayoutsListener();

			// 恢复到刷新之前的状态
			for (; tempTreePath != null && tempTreePath.hasMoreElements();) {
				this.setExpandedState(tempTreePath.nextElement(), true);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 刷新三维场景节点
	 */
	private void refreshNode(Scenes scenes) {
		try {
			// 记住刷新之前书的状态
			Enumeration<TreePath> tempTreePath = this.getExpandedDescendants(new TreePath(treeNodeScenes.getPath()));

			// 第一步：删除子树节点事件监听器，
			removeScenesListener();

			// 第二步：删除节点，
			treeNodeScenes.removeAllChildren();

			// 第三步：添加子树，
			addScene();

			// 第四步：添加子树事件监听器。
			addScenesListener();

			// 恢复到刷新之前的状态
			for (; tempTreePath != null && tempTreePath.hasMoreElements();) {
				this.setExpandedState(tempTreePath.nextElement(), true);
			}

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public boolean isPathEditable(TreePath path) {
		TreePath treePath = path;
		if (treePath != null) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			TreeNodeData data = (TreeNodeData) currentNode.getUserObject();
			Object userData = data.getData();
			if (userData instanceof Workspace || userData instanceof Maps || userData instanceof Datasources || userData instanceof Layouts
					|| userData instanceof Scenes || userData instanceof Resources || userData instanceof SymbolFillLibrary
					|| userData instanceof SymbolLineLibrary || userData instanceof SymbolMarkerLibrary) {
				return false;

			} else if (userData instanceof Dataset && ((Dataset) userData).getDatasource().isReadOnly()) {
				return false;
			}
		}
		return isEditable();
	}

	/**
	 * This method buildWorkspaceTree build workspace tree
	 */
	private void buildWorkspaceNode(Workspace workspace) {
		datasources = workspace.getDatasources();
		maps = workspace.getMaps();
		layouts = workspace.getLayouts();
		scenes = workspace.getScenes();
		resources = workspace.getResources();

		TreeNodeData workspaceNodeData = new TreeNodeData(workspace, NodeDataType.WORKSPACE);
		treeNodeWorkspace = new DefaultMutableTreeNode(workspaceNodeData);

		// 添加Datasources节点
		TreeNodeData datasourcesNodeData = new TreeNodeData(datasources, NodeDataType.DATASOURCES);
		treeNodeDatasources = new DefaultMutableTreeNode(datasourcesNodeData);
		if (isDatasourcesNodeVisible) {
			treeNodeWorkspace.add(treeNodeDatasources);
		}
		addDatasource();

		// 添加Maps节点
		TreeNodeData mapsNodeData = new TreeNodeData(maps, NodeDataType.MAPS);
		treeNodeMaps = new DefaultMutableTreeNode(mapsNodeData);
		if (isMapsNodeVisible) {
			treeNodeWorkspace.add(treeNodeMaps);
		}
		addMap();

		// 添加Layouts节点
		TreeNodeData layoutsNodeData = new TreeNodeData(layouts, NodeDataType.LAYOUTS);
		treeNodeLayouts = new DefaultMutableTreeNode(layoutsNodeData);
		if (isLayoutsNodeVisible) {
			treeNodeWorkspace.add(treeNodeLayouts);
		}
		addLayout();

		// 添加Scenes节点
		TreeNodeData scenesNodeData = new TreeNodeData(scenes, NodeDataType.SCENES);
		treeNodeScenes = new DefaultMutableTreeNode(scenesNodeData);
		if (isScenesNodeVisible) {
			treeNodeWorkspace.add(treeNodeScenes);
		}
		addScene();

		// 添加资源库节点
		TreeNodeData resourcesNodeData = new TreeNodeData(resources, NodeDataType.RESOURCES);
		treeNodeResources = new DefaultMutableTreeNode(resourcesNodeData);
		if (isResourcesNodeVisible) {
			treeNodeWorkspace.add(treeNodeResources);
		}

		// 添加符号库
		symbolMarkerLibraray = resources.getMarkerLibrary();
		TreeNodeData symbolMarkerNodeData = new TreeNodeData(symbolMarkerLibraray, NodeDataType.SYMBOL_MARKER_LIBRARY);
		treeNodeMakerLibrary = new DefaultMutableTreeNode(symbolMarkerNodeData);
		treeNodeResources.add(treeNodeMakerLibrary);

		// 添加线型库
		symbolLineLibrary = resources.getLineLibrary();
		TreeNodeData symbolLineNodeData = new TreeNodeData(symbolLineLibrary, NodeDataType.SYMBOL_LINE_LIBRARY);
		treeNodeLineLibrary = new DefaultMutableTreeNode(symbolLineNodeData);
		treeNodeResources.add(treeNodeLineLibrary);

		// 添加填充库
		symbolFillLibrary = resources.getFillLibrary();
		TreeNodeData symbolFillNodeData = new TreeNodeData(symbolFillLibrary, NodeDataType.SYMBOL_FILL_LIBRARY);
		treeNodeFillLibrary = new DefaultMutableTreeNode(symbolFillNodeData);
		treeNodeResources.add(treeNodeFillLibrary);

	}

	/**
	 * 初始化方法
	 */
	private void init() {
		treeRenderer = new WorkspaceTreeCellRenderer(currentWorkspace);
		setCellRenderer(treeRenderer);

		cellEditorTempCellEditor = new WorkspaceTreeCellEditor(this, treeRenderer, currentWorkspace);
		setCellEditor(cellEditorTempCellEditor);

		getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		setEditable(true);

		createListener();
	}

	/**
	 * 创建监听器
	 */
	private void createListener() {
		workspaceClosedListener = new WorkspaceTreeWorkspaceClosedListener();

		workspaceCreatedListener = new WorkspaceTreeWorkspaceCreatedListener();

		workspaceOpenedListener = new WorkspaceTreeWorkspaceOpenedListener();

		datasourceAliasModifiedListener = new WorkspaceTreeDatasourceAliasModifiedListener();

		datasourceClosedListener = new WorkspaceTreeDatasourceClosedListener();

		datasourceCreatedListener = new WorkspaceTreeDatasourceCreatedListener();

		datasourceOpenedListener = new WorkspaceTreeDatasourceOpenedListener();

		datasetRenamedListener = new WorkspaceTreeDatasetRenamedListener();

		datasetCreatedListener = new WorkspaceTreeDatasetCreatedListener();

		datasetDeletedAllListener = new WorkspaceTreeDatasetDeletedAllListener();

		datasetDeletedListener = new WorkspaceTreeDatasetDeletedListener();

		datasetDeletingListener = new WorkspaceTreeDatasetDeletingListener();

		mapAddedListener = new WorkspaceTreeMapAddedListener();

		mapClearedListener = new WorkspaceTreeMapClearedListener();

		mapRemovedListener = new WorkspaceTreeMapRemovedListener();

		mapRenamedListener = new WorkspaceTreeMapRenamedListener();

		layoutsAddedListener = new WorkspaceTreeLayoutAddedListener();

		layoutsClearedListener = new WorkspaceTreeLayoutClearedListener();

		layoutsLayoutRemovedListener = new WorkspaceTreeLayoutRemovedListener();

		layoutsLayoutRenamedListener = new WorkspaceTreeLayoutRenamedListener();

		sceneAddedListener = new WorkspaceTreeSceneAddedListener();

		sceneClearedListener = new WorkspaceTreeSceneClearedListener();

		sceneRemovedListener = new WorkspaceTreeSceneRemovedListener();

		sceneRenamedListener = new WorkspaceTreeSceneRenamedListener();

		datasetCollectionAddedListener = new WorkspaceTreeDatasetCollectionAddedListener();

		datasetCollectionRemovedListener = new WorkspaceTreeDatasetCollectionRemovedListener();

		datasetCollectionRemovingListener = new WorkspaceTreeDatasetCollectionRemovingListener();

		datasetCollectionRenamedListener = new WorkspaceTreeDatasetCollectionRenamedListener();

		datasetCollectionClearedListener = new WorkspaceTreeDatasetCollectionClearedListener();

		datasetCollectionRefreshListener = new WorkspaceTreeDatasetCollectionRefreshListener();

		datasetCollectionOrderChangedListener = new WorkspaceTreeDatasetCollectionOrderChangedListener();
	}

	/**
	 * 添加全部事件
	 */
	private void addListener() {
		addWorkspaceTreeWorkspaceListener();
		addDatasourceListener();
		addMapListener();
		addLayoutsListener();
		addScenesListener();
		addUpdataToolBarsListener();
	}

	private void addUpdataToolBarsListener() {
		this.addTreeSelectionListener(treeSelectionListener);
	}

	/**
	 * 删除全部事件，除了KeyListener,KeyListerer是注册在WorkspaceTree
	 */
	private void removeListener() {
		removeWorkspaceTreeWorkspaceListener();
		removeDatasourceListener();
		removeMapListener();
		removeLayoutsListener();
		removeScenesListener();
		removeUpdataToolBarsListener();
	}

	private void removeUpdataToolBarsListener() {
		this.removeTreeSelectionListener(treeSelectionListener);
	}

	/**
	 * 添加工作空间事件
	 */
	private void addWorkspaceTreeWorkspaceListener() {

		currentWorkspace.addClosedListener(workspaceClosedListener);

		currentWorkspace.addCreatedListener(workspaceCreatedListener);

		currentWorkspace.addOpenedListener(workspaceOpenedListener);
	}

	/**
	 * 删除工作空间事件
	 */
	private void removeWorkspaceTreeWorkspaceListener() {
		currentWorkspace.removeClosedListener(workspaceClosedListener);

		currentWorkspace.removeCreatedListener(workspaceCreatedListener);

		currentWorkspace.removeOpenedListener(workspaceOpenedListener);

	}

	/**
	 * 添加数据源事件
	 */
	private void addDatasourceListener() {

		datasources.addAliasModifiedListener(datasourceAliasModifiedListener);

		datasources.addClosedListener(datasourceClosedListener);

		datasources.addCreatedListener(datasourceCreatedListener);

		datasources.addOpenedListener(datasourceOpenedListener);

		int count = datasources.getCount();
		for (int i = 0; i < count; i++) {
			addDatasetListener(datasources.get(i).getDatasets());
		}

	}

	/**
	 * 删除数据源及数据集事件事件
	 */
	private void removeDatasourceListener() {
		datasources.removeAliasModifiedListener(datasourceAliasModifiedListener);

		datasources.removeClosedListener(datasourceClosedListener);

		datasources.removeCreatedListener(datasourceCreatedListener);

		datasources.removeOpenedListener(datasourceOpenedListener);

		int datasourceCount = datasources.getCount();
		for (int i = 0; i < datasourceCount; i++) {
			Datasets datasets = datasources.get(i).getDatasets();
			removeDatasetListener(datasets);
		}

	}

	/**
	 * 添加数据集事件事件
	 */
	private void addDatasetListener(Datasets datasets) {
		Datasets tempDatasets = datasets;
		/**
		 * 添加数据集创建事件
		 */
		tempDatasets.addCreatedListener(datasetCreatedListener);

		/**
		 * 添加当前数据源所有数据集删除事件
		 */
		tempDatasets.addDeletedAllListener(datasetDeletedAllListener);

		/**
		 * 添加数据集删除事件
		 */
		tempDatasets.addDeletedListener(datasetDeletedListener);

		/**
		 * 添加数据集删除中事件，计算m_deleteingDatasetIndex
		 */
		tempDatasets.addDeletingListener(datasetDeletingListener);

		/**
		 * 添加数据集重命名事件
		 */
		tempDatasets.addRenamedListener(datasetRenamedListener);

		int datasetCount = tempDatasets.getCount();
		for (int i = 0; i < datasetCount; i++) {
			Dataset dataset = tempDatasets.get(i);
			addDatasetImageGridCollectionListener(dataset);
		}
	}

	/**
	 * 删除数据集事件事件
	 */
	private void removeDatasetListener(Datasets datasets) {
		Datasets tempDatasets = datasets;
		/**
		 * 删除数据集创建事件
		 */
		tempDatasets.removeCreatedListener(datasetCreatedListener);

		/**
		 * 删除当前数据源所有数据集删除事件
		 */
		tempDatasets.removeDeletedAllListener(datasetDeletedAllListener);

		/**
		 * 删除数据集删除事件
		 */
		tempDatasets.removeDeletedListener(datasetDeletedListener);

		/**
		 * 删除数据集删除中事件，计算m_deleteingDatasetIndex
		 */
		tempDatasets.removeDeletingListener(datasetDeletingListener);

		/**
		 * 删除数据集重命名事件
		 */
		tempDatasets.removeRenamedListener(datasetRenamedListener);

		int datasetCount = tempDatasets.getCount();
		for (int i = 0; i < datasetCount; i++) {
			Dataset dataset = tempDatasets.get(i);
			removeDatasetImageGridCollectionListener(dataset);
		}
	}

	private void addDatasetImageGridCollectionListener(Dataset dataset) {
		if (dataset instanceof DatasetImageCollection) {
			DatasetImageCollection imageCollection = (DatasetImageCollection) dataset;
			imageCollection.addAddedListener(datasetCollectionAddedListener);
			imageCollection.addRemovedListener(datasetCollectionRemovedListener);
			imageCollection.addRemovingListener(datasetCollectionRemovingListener);
			imageCollection.addRenamedListener(datasetCollectionRenamedListener);
			imageCollection.addRemovedAllListener(datasetCollectionClearedListener);
			imageCollection.addRequireRefreshListener(datasetCollectionRefreshListener);
			imageCollection.addOrderChangedListener(datasetCollectionOrderChangedListener);
		} else if (dataset instanceof DatasetGridCollection) {
			DatasetGridCollection gridCollection = (DatasetGridCollection) dataset;
			gridCollection.addAddedListener(datasetCollectionAddedListener);
			gridCollection.addRemovedListener(datasetCollectionRemovedListener);
			gridCollection.addRemovingListener(datasetCollectionRemovingListener);
			gridCollection.addRenamedListener(datasetCollectionRenamedListener);
			gridCollection.addRemovedAllListener(datasetCollectionClearedListener);
			gridCollection.addRequireRefreshListener(datasetCollectionRefreshListener);
			gridCollection.addOrderChangedListener(datasetCollectionOrderChangedListener);
		}
	}

	private void removeDatasetImageGridCollectionListener(Dataset dataset) {
		if (dataset instanceof DatasetImageCollection) {
			DatasetImageCollection imageCollection = (DatasetImageCollection) dataset;
			imageCollection.removeAddedListener(datasetCollectionAddedListener);
			imageCollection.removeRemovedListener(datasetCollectionRemovedListener);
			imageCollection.removeRemovingListener(datasetCollectionRemovingListener);
			imageCollection.removeRenamedListener(datasetCollectionRenamedListener);
			imageCollection.removeRemovedAllListener(datasetCollectionClearedListener);
			imageCollection.removeRequireRefreshListener(datasetCollectionRefreshListener);
			imageCollection.removeOrderChangedListener(datasetCollectionOrderChangedListener);
		} else if (dataset instanceof DatasetGridCollection) {
			DatasetGridCollection gridCollection = (DatasetGridCollection) dataset;
			gridCollection.removeAddedListener(datasetCollectionAddedListener);
			gridCollection.removeRemovedListener(datasetCollectionRemovedListener);
			gridCollection.removeRemovingListener(datasetCollectionRemovingListener);
			gridCollection.removeRenamedListener(datasetCollectionRenamedListener);
			gridCollection.removeRemovedAllListener(datasetCollectionClearedListener);
			gridCollection.removeRequireRefreshListener(datasetCollectionRefreshListener);
			gridCollection.removeOrderChangedListener(datasetCollectionOrderChangedListener);
		}
	}

	/**
	 * 添加地图事件
	 */
	private void addMapListener() {

		maps.addAddedListener(mapAddedListener);

		maps.addClearedListener(mapClearedListener);

		maps.addRemovedListener(mapRemovedListener);

		maps.addRenamedListener(mapRenamedListener);

	}

	/**
	 * 删除地图事件
	 */
	private void removeMapListener() {

		maps.removeAddedListener(mapAddedListener);

		maps.removeClearedListener(mapClearedListener);

		maps.removeRemovedListener(mapRemovedListener);

		maps.removeRenamedListener(mapRenamedListener);

	}

	/**
	 * 添加布局事件
	 */
	private void addLayoutsListener() {

		layouts.addAddedListener(layoutsAddedListener);

		layouts.addClearedListener(layoutsClearedListener);

		layouts.addRemovedListener(layoutsLayoutRemovedListener);

		layouts.addRenamedListener(layoutsLayoutRenamedListener);
	}

	/**
	 * 删除布局事件
	 */
	private void removeLayoutsListener() {

		layouts.removeAddedListener(layoutsAddedListener);

		layouts.removeClearedListener(layoutsClearedListener);

		layouts.removeRemovedListener(layoutsLayoutRemovedListener);

		layouts.removeRenamedListener(layoutsLayoutRenamedListener);
	}

	/**
	 * 添加场景事件
	 */
	private void addScenesListener() {

		scenes.addAddedListener(sceneAddedListener);

		scenes.addClearedListener(sceneClearedListener);

		scenes.addRemovedListener(sceneRemovedListener);

		scenes.addRenamedListener(sceneRenamedListener);
	}

	/**
	 * 删除场景事件
	 */
	private void removeScenesListener() {

		scenes.removeAddedListener(sceneAddedListener);

		scenes.removeClearedListener(sceneClearedListener);

		scenes.removeRemovedListener(sceneRemovedListener);

		scenes.removeRenamedListener(sceneRenamedListener);
	}

	/**
	 * 添加数据源节点
	 */
	private void addDatasource() {
		DefaultMutableTreeNode tempDatasourceNode;

		int datasourceCount = datasources.getCount();
		for (int i = 0; i < datasourceCount; i++) {
			Datasource tempDatasource = datasources.get(i);
			TreeNodeData datasourceNodeData = new TreeNodeData(tempDatasource, NodeDataType.DATASOURCE);
			tempDatasourceNode = new DefaultMutableTreeNode(datasourceNodeData);
			addDataset(tempDatasource, tempDatasourceNode);
			treeNodeDatasources.add(tempDatasourceNode);
		}

		// add by xuzw 2010-09-28
		// 默认展开数据源集合节点 请不要将该代码移至buildWorkspaceNode中，
		// 实际上是refreshNode(Datasources datasources)才起作用
		TreePath treePathDatasources = new TreePath(treeNodeDatasources.getPath());
		this.expandPath(treePathDatasources);
	}

	/**
	 * 添加数据集及事件
	 */
	private void addDataset(Datasource ds, DefaultMutableTreeNode datasourceNode) {
		Datasets datasets = ds.getDatasets();

		int datasetCount = datasets.getCount();
		for (int j = 0; j < datasetCount; j++) {
			addDataset(datasets.get(j), datasourceNode);
		}
	}

	/**
	 * 添加数据集
	 *
	 * @param dataset
	 * @param datasourceNode
	 */
	private DefaultMutableTreeNode addDataset(Dataset dataset, DefaultMutableTreeNode datasourceNode) {
		if (dataset instanceof DatasetVolume) {
			// 暂不支持的数据集类型
			return null;
		}
		DefaultMutableTreeNode datasetNode;
		TreeNodeData datasetNodeData;

		if (dataset instanceof DatasetVector) {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.DATASET_VECTOR);
		} else if (dataset instanceof DatasetImage) {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.DATASET_IMAGE);
		} else if (dataset instanceof DatasetGrid) {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.DATASET_GRID);
		} else if (dataset instanceof DatasetTopology) {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.DATASET_TOPOLOGY);
		} else if (dataset instanceof DatasetImageCollection) {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.DATASET_IMAGE_COLLECTION);
		} else if (dataset instanceof DatasetGridCollection) {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.DATASET_GRID_COLLECTION);
		} else {
			datasetNodeData = new TreeNodeData(dataset, NodeDataType.UNKNOWN);
		}
		DatasetType type = dataset.getType();
		datasetNode = new DefaultMutableTreeNode(datasetNodeData);
		if (type.equals(DatasetType.NETWORK) || type.equals(DatasetType.NETWORK3D)) {
			// 添加网络数据集的子数据集
			DatasetVector datasetVector = (DatasetVector) dataset;
			TreeNodeData childDatasetNodeData = new TreeNodeData(datasetVector.getChildDataset(), NodeDataType.DATASET_VECTOR);
			DefaultMutableTreeNode childDatasetNode = new DefaultMutableTreeNode(childDatasetNodeData);
			datasetNode.add(childDatasetNode);
		}
		if (dataset instanceof DatasetTopology) {
			// 添加拓扑数据集的子数据集
			DatasetTopology datasetTopology = (DatasetTopology) dataset;
			datasetNode = new DefaultMutableTreeNode(datasetNodeData);
			DatasetVector errorLineDataset = datasetTopology.getErrorLineDataset();
			if (errorLineDataset != null) {
				TreeNodeData errorLineDatasetNodeData = new TreeNodeData(errorLineDataset, NodeDataType.TOPOLOGY_ERROR_DATASETS);
				DefaultMutableTreeNode errorLineDatasetNode = new DefaultMutableTreeNode(errorLineDatasetNodeData);
				datasetNode.add(errorLineDatasetNode);
			}

			DatasetVector errorPointDataset = datasetTopology.getErrorPointDataset();
			if (errorPointDataset != null) {
				TreeNodeData errorPointDatasetNodeData = new TreeNodeData(errorPointDataset, NodeDataType.TOPOLOGY_ERROR_DATASETS);
				DefaultMutableTreeNode errorPointDatasetNode = new DefaultMutableTreeNode(errorPointDatasetNodeData);
				datasetNode.add(errorPointDatasetNode);
			}

			DatasetVector errorRegionDataset = datasetTopology.getErrorRegionDataset();
			if (errorRegionDataset != null) {
				TreeNodeData errorRegionDatasetNodeData = new TreeNodeData(errorRegionDataset, NodeDataType.TOPOLOGY_ERROR_DATASETS);
				DefaultMutableTreeNode errorRegionDatasetNode = new DefaultMutableTreeNode(errorRegionDatasetNodeData);
				datasetNode.add(errorRegionDatasetNode);
			}

			TopologyDatasetRelationItems datasetRelationItems = datasetTopology.getRelationItems();
			if (datasetRelationItems != null) {
				TreeNodeData userObject = new TreeNodeData(datasetRelationItems, NodeDataType.TOPOLOGY_DATASET_RELATIONS);
				DefaultMutableTreeNode relationItemsNode = new DefaultMutableTreeNode(userObject);
				datasetNode.add(relationItemsNode);
			}

		}

		if (dataset instanceof DatasetImageCollection) {
			DatasetImageCollection imageCollection = (DatasetImageCollection) dataset;
			fillImageCollectionNodes(imageCollection, datasetNode);
		} else if (dataset instanceof DatasetGridCollection) {
			DatasetGridCollection gridCollection = (DatasetGridCollection) dataset;
			fillGridCollectionNodes(gridCollection, datasetNode);
		}

		// 使用下面的方式来刷新 Node，而不要使用 updateUI 来整个刷新 UGDJ-243
		this.treeModelTemp.insertNodeInto(datasetNode, datasourceNode, datasourceNode.getChildCount());
		return datasetNode;
	}

	private void fillImageCollectionNodes(DatasetImageCollection imageCollection, DefaultMutableTreeNode datasetNode) {
		String[] aliasNames = InternalDatasetImageCollection.getAliasNames(imageCollection);
		for (int i = 0; i < aliasNames.length; i++) {
			TreeNodeData userObject = new TreeNodeData(aliasNames[i], NodeDataType.DATASET_IMAGE_COLLECTION_ITEM);
			DefaultMutableTreeNode itemsNode = new DefaultMutableTreeNode(userObject);
			datasetNode.add(itemsNode);
		}
	}

	private void fillGridCollectionNodes(DatasetGridCollection gridCollection, DefaultMutableTreeNode datasetNode) {
		String[] aliasNames = InternalDatasetGridCollection.getAliasNames(gridCollection);
		for (int i = 0; i < aliasNames.length; i++) {
			TreeNodeData userObject = new TreeNodeData(aliasNames[i], NodeDataType.DATASET_GRID_COLLECTION_ITEM);
			DefaultMutableTreeNode itemsNode = new DefaultMutableTreeNode(userObject);
			datasetNode.add(itemsNode);
		}
	}

	/**
	 * 添加地图节点
	 */
	private void addMap() {
		DefaultMutableTreeNode tempMapNode;
		int mapCount = maps.getCount();
		for (int i = 0; i < mapCount; i++) {
			TreeNodeData mapNodeData = new TreeNodeData(maps.get(i), NodeDataType.MAP_NAME);
			tempMapNode = new DefaultMutableTreeNode(mapNodeData);
			treeNodeMaps.add(tempMapNode);
		}

		TreePath treePathMaps = new TreePath(treeNodeMaps.getPath());
		this.expandPath(treePathMaps);
	}

	/**
	 * 添加布局节点
	 */
	private void addLayout() {
		DefaultMutableTreeNode tempLayoutNode;
		int layoutCount = layouts.getCount();
		for (int i = 0; i < layoutCount; i++) {
			TreeNodeData layoutNodeData = new TreeNodeData(layouts.get(i), NodeDataType.LAYOUT_NAME);
			tempLayoutNode = new DefaultMutableTreeNode(layoutNodeData);

			treeNodeLayouts.add(tempLayoutNode);
		}
		TreePath treePathLayouts = new TreePath(treeNodeLayouts.getPath());
		this.expandPath(treePathLayouts);
	}

	/**
	 * 添加场景节点
	 */
	private void addScene() {
		DefaultMutableTreeNode tempSceneNode;
		int sceneCount = scenes.getCount();
		for (int i = 0; i < sceneCount; i++) {
			TreeNodeData sceneNodeData = new TreeNodeData(scenes.get(i), NodeDataType.SCENE_NAME);
			tempSceneNode = new DefaultMutableTreeNode(sceneNodeData);
			treeNodeScenes.add(tempSceneNode);
		}
		TreePath treePathScenes = new TreePath(treeNodeScenes.getPath());
		this.expandPath(treePathScenes);
	}

	private class WorkspaceTreeMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				TreePath mouseLocationPath = WorkspaceTree.this.getClosestPathForLocation(e.getX(), e.getY());
				if (mouseLocationPath != null && mouseLocationPath.getPath() != null && mouseLocationPath.getPath().length > 0
						&& !WorkspaceTree.this.isPathSelected(mouseLocationPath)) {
					WorkspaceTree.this.setSelectionPath(mouseLocationPath);
				}
			}
		}

	}

	private class WorkspaceTreeKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {

			TreePath treeSelectionPath = getSelectionPath();
			if (null != treeSelectionPath && e.getKeyChar() == KeyEvent.VK_ENTER) {
				// 打开地图
				openNewMap();
			}
			if (treeSelectionPath != null && e.getKeyCode() == KeyEvent.VK_DELETE) {
				needShowMessageBox = true;
				Object lastComponent = treeSelectionPath.getLastPathComponent();
				DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) lastComponent;
				Object userObject = tempNode.getUserObject();
				TreeNodeData currentNodeData = (TreeNodeData) userObject;
				Object data = currentNodeData.getData();
				if (data instanceof Workspace) {
					CommonToolkit.WorkspaceWrap.closeWorkspace();
				} else if (data instanceof Datasource) {
					try {
						String message = MessageFormat.format(ControlsProperties.getString("String_CloseDatasourseInfo"), Application.getActiveApplication()
								.getActiveDatasources()[0].getAlias());
						if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(message)) {
							Datasource[] activeDatasources = Application.getActiveApplication().getActiveDatasources();
							for (int i = activeDatasources.length - 1; i >= 0; i--) {
								// 关闭选中的数据源下的数据集
								Datasource activeDatasource = activeDatasources[i];
								Datasets datasets = activeDatasource.getDatasets();
								CommonToolkit.DatasetWrap.CloseDataset(datasets);
								// 关闭数据源
								String datasourceAlias = activeDatasource.getAlias();
								boolean flag = Application.getActiveApplication().getWorkspace().getDatasources().close(datasourceAlias);
								String resultInfo = "";
								if (flag) {
									resultInfo = MessageFormat.format(ControlsProperties.getString("String_CloseDatasourseSuccessful"), datasourceAlias);
								} else {
									resultInfo = MessageFormat.format(ControlsProperties.getString("String_CloseDatasourseFailed"), datasourceAlias);
								}
								Application.getActiveApplication().getOutput().output(resultInfo);
								Application.getActiveApplication().setActiveDatasources(null);
							}
						}
					} catch (Exception ex) {
						Application.getActiveApplication().getOutput().output(ex);
					}
				} else if (data instanceof Dataset) {
					if (Application.getActiveApplication().getActiveDatasources()[0].isReadOnly()) {
						UICommonToolkit.showErrorMessageDialog(ControlsProperties.getString("String_DelectReadonlyDataset"));
						return;
					}
					Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
					String message = "";
					if (datasets.length == 1) {
						message = MessageFormat.format(ControlsProperties.getString("String_DelectOneDataset"), Application.getActiveApplication()
								.getActiveDatasources()[0].getAlias(), datasets[0].getName());
					} else {
						message = MessageFormat.format(ControlsProperties.getString("String_DelectManyDataset"), datasets.length);
					}
					if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(message)) {
						CommonToolkit.DatasetWrap.CloseDataset(datasets);
						for (int i = 0; i < datasets.length; i++) {
							String resultInfo = MessageFormat.format(ControlsProperties.getString("String_DelectDatasetSuccessfulInfo"), datasets[i]
									.getDatasource().getAlias(), datasets[i].getName());
							datasets[i].getDatasource().getDatasets().delete(datasets[i].getName());
							Application.getActiveApplication().getOutput().output(resultInfo);
						}
						Application.getActiveApplication().setActiveDatasets(null);
					}
				} else {
					NodeDataType type = currentNodeData.getType();
					if (type.equals(NodeDataType.LAYOUT_NAME)) {
						ArrayList<String> layoutNames = new ArrayList<String>();
						for (TreePath treePath : getSelectionPaths()) {
							DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
							TreeNodeData selectedNodeData = (TreeNodeData) treeNode.getUserObject();
							layoutNames.add(selectedNodeData.getData().toString());
						}
						CommonToolkit.LayoutWrap.deleteMapLayout(layoutNames.toArray(new String[layoutNames.size()]));
					} else if (type.equals(NodeDataType.MAP_NAME)) {
						ArrayList<String> mapNames = new ArrayList<String>();
						for (TreePath treePath : getSelectionPaths()) {
							DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
							TreeNodeData selectedNodeData = (TreeNodeData) treeNode.getUserObject();
							mapNames.add(selectedNodeData.getData().toString());
						}
						MapUtilties.deleteMaps(mapNames.toArray(new String[mapNames.size()]));
					} else if (type.equals(NodeDataType.SCENE_NAME)) {
						ArrayList<String> sceneNames = new ArrayList<String>();
						for (TreePath treePath : getSelectionPaths()) {
							DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
							TreeNodeData selectedNodeData = (TreeNodeData) treeNode.getUserObject();
							sceneNames.add(selectedNodeData.getData().toString());
						}
						CommonToolkit.SceneWrap.deleteScenes(sceneNames.toArray(new String[sceneNames.size()]));
					}
				}
				ToolbarUtilties.updataToolbarsState();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// do nothing
		}

	}

	private class WorkspaceTreeWorkspaceClosedListener implements WorkspaceClosedListener {
		@Override
		public void workspaceClosed(WorkspaceClosedEvent event) {
			buildWorkspaceNode(currentWorkspace);
			treeModelTemp.setRoot(treeNodeWorkspace);
			treeModelTemp.reload();

		}

	}

	private class WorkspaceTreeWorkspaceCreatedListener implements WorkspaceCreatedListener {
		@Override
		public void workspaceCreated(WorkspaceCreatedEvent event) {
			currentWorkspace = event.getWorkspace();
			buildWorkspaceNode(currentWorkspace);
			treeModelTemp.setRoot(treeNodeWorkspace);
			treeModelTemp.reload();
		}
	}

	private class WorkspaceTreeWorkspaceOpenedListener implements WorkspaceOpenedListener {
		@Override
		public void workspaceOpened(WorkspaceOpenedEvent event) {
			removeListener();
			currentWorkspace = event.getWorkspace();
			buildWorkspaceNode(currentWorkspace);
			treeModelTemp.setRoot(treeNodeWorkspace);
			treeModelTemp.reload();
			init();
			addListener();
		}

	}

	private class WorkspaceTreeDatasourceAliasModifiedListener implements DatasourceAliasModifiedListener {
		@Override
		public void datasourceAliasModified(DatasourceAliasModifiedEvent event) {
			updateUI();
		}

	}

	private class WorkspaceTreeDatasourceClosedListener implements DatasourceClosedListener {
		@Override
		public void datasourceClosed(DatasourceClosedEvent event) {
			DefaultMutableTreeNode datasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(event.getIndex());
			treeModelTemp.removeNodeFromParent(datasourceNode);

		}

	}

	private class WorkspaceTreeDatasourceCreatedListener implements DatasourceCreatedListener {
		@Override
		public void datasourceCreated(DatasourceCreatedEvent event) {
			// 给新建的数据源添加数据集事件 Added by weicd 2010.3.22
			Datasource newDatasource = event.getDatasource();
			addDatasetListener(newDatasource.getDatasets());

			TreeNodeData addDatasourceNodeData = new TreeNodeData(newDatasource, NodeDataType.DATASOURCE);
			DefaultMutableTreeNode addDatasourceNode = new DefaultMutableTreeNode(addDatasourceNodeData);

			int index = datasources.indexOf(newDatasource.getAlias());
			treeModelTemp.insertNodeInto(addDatasourceNode, treeNodeDatasources, index);

		}

	}

	private class WorkspaceTreeDatasourceOpenedListener implements DatasourceOpenedListener {
		@Override
		public void datasourceOpened(DatasourceOpenedEvent event) {
			TreeNodeData openedDatasourceNodeData = new TreeNodeData(event.getDatasource(), NodeDataType.DATASOURCE);
			DefaultMutableTreeNode addDatasourceNode = new DefaultMutableTreeNode(openedDatasourceNodeData);

			treeModelTemp.insertNodeInto(addDatasourceNode, treeNodeDatasources, treeNodeDatasources.getChildCount());

			addDataset(event.getDatasource(), addDatasourceNode);

			// 添加数据集事件
			addDatasetListener(event.getDatasource().getDatasets());
		}

	}

	private class WorkspaceTreeDatasetRenamedListener implements DatasetRenamedListener {
		@Override
		public void datasetRenamed(DatasetRenamedEvent event) {
			updateUI();
		}

	}

	private class WorkspaceTreeDatasetCreatedListener implements DatasetCreatedListener {
		@Override
		public void datasetCreated(DatasetCreatedEvent event) {
			Datasets tempdatasets = (Datasets) event.getSource();
			Datasource datasource = tempdatasets.getDatasource();

			int index = datasources.indexOf(datasource.getAlias().trim());
			DefaultMutableTreeNode sourceDatasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(index);
			DefaultMutableTreeNode createdNode = addDataset(tempdatasets.get(event.getDatasetName()), sourceDatasourceNode);
			locateNode(createdNode);
		}
	}

	/**
	 * 定位到指定节点并选中
	 *
	 * @param node
	 */
	private void locateNode(DefaultMutableTreeNode node) {
		if (node == null || node.getParent() == null || node.getRoot() == null) {
			return;
		}

		// 获取新创建节点的 Path
		TreePath treePath = new TreePath(node.getPath());
		// 展开该节点的父节点
		if (!isExpanded(treePath.getParentPath())) {
			expandPath(treePath.getParentPath());
		}
		// 使新创建的节点可见
		scrollPathToVisible(treePath);
		// 选中新创建的节点
		setSelectionPath(treePath);
	}

	private class WorkspaceTreeDatasetDeletedAllListener implements DatasetDeletedAllListener {
		@Override
		public void datasetDeletedAll(DatasetDeletedAllEvent event) {
			Datasets tempDatasets = (Datasets) event.getSource();
			Datasource datasource = tempDatasets.getDatasource();
			int index = datasources.indexOf(datasource.getAlias());
			DefaultMutableTreeNode datasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(index);
			datasourceNode.removeAllChildren();
			updateUI();
		}
	}

	private class WorkspaceTreeDatasetDeletedListener implements DatasetDeletedListener {
		@Override
		public void DatasetDeleted(DatasetDeletedEvent event) {
			if (null != event.getSource()) {
				Datasets datasets = (Datasets) event.getSource();
				Datasource datasource = datasets.getDatasource();
				int index = datasources.indexOf(datasource.getAlias());
				DefaultMutableTreeNode datasourceNode = (DefaultMutableTreeNode) (treeNodeDatasources.getChildAt(index));
				if (0 < datasourceNode.getChildCount() && deleteingDatasetIndex < datasourceNode.getChildCount()) {
					DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) datasourceNode.getChildAt(deleteingDatasetIndex);
					treeModelTemp.removeNodeFromParent(datasetNode);
				}
				locateNode(datasourceNode);
			}
		}
	}

	private class WorkspaceTreeDatasetDeletingListener implements DatasetDeletingListener {
		@Override
		public void datasetDeleting(DatasetDeletingEvent event) {
			Datasets datasets = (Datasets) event.getSource();
			deleteingDatasetIndex = datasets.indexOf(event.getDatasetName());
		}
	}

	private class WorkspaceTreeMapAddedListener implements MapAddedListener {
		@Override
		public void mapAdded(MapAddedEvent event) {
			TreeNodeData newMapNodeData = new TreeNodeData(event.getMapName(), NodeDataType.MAP_NAME);
			DefaultMutableTreeNode addMapNode = new DefaultMutableTreeNode(newMapNodeData);

			treeModelTemp.insertNodeInto(addMapNode, treeNodeMaps, treeNodeMaps.getChildCount());
			locateNode(addMapNode);
		}
	}

	private class WorkspaceTreeMapClearedListener implements MapClearedListener {
		@Override
		public void mapCleared(MapClearedEvent event) {
			treeNodeMaps.removeAllChildren();
			updateUI();
		}

	}

	private class WorkspaceTreeMapRemovedListener implements MapRemovedListener {
		@Override
		public void mapRemoved(MapRemovedEvent event) {
			for (int i = 0; i < treeNodeMaps.getChildCount(); i++) {
				DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) treeNodeMaps.getChildAt(i);
				Object mapName = ((TreeNodeData) tempNode.getUserObject()).getData();
				if (mapName.toString().trim().equals(event.getMapName().trim())) {
					treeModelTemp.removeNodeFromParent(tempNode);
				}
			}
		}

	}

	private class WorkspaceTreeMapRenamedListener implements MapRenamedListener {
		@Override
		public void mapRenamed(MapRenamedEvent event) {
			int index = maps.indexOf(event.getNewName());
			DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) treeNodeMaps.getChildAt(index);
			TreeNodeData mapNameNodeData = new TreeNodeData(event.getNewName(), NodeDataType.MAP_NAME);
			tempNode.setUserObject(mapNameNodeData);
			updateUI();
		}

	}

	private class WorkspaceTreeLayoutAddedListener implements LayoutAddedListener {
		@Override
		public void layoutAdded(LayoutAddedEvent event) {
			TreeNodeData addLayoutNodeData = new TreeNodeData(event.getLayoutName(), NodeDataType.LAYOUT_NAME);
			DefaultMutableTreeNode addLayoutNode = new DefaultMutableTreeNode(addLayoutNodeData);
			treeModelTemp.insertNodeInto(addLayoutNode, treeNodeLayouts, treeNodeLayouts.getChildCount());
		}

	}

	private class WorkspaceTreeLayoutClearedListener implements LayoutClearedListener {
		@Override
		public void layoutCleared(LayoutClearedEvent event) {
			treeNodeLayouts.removeAllChildren();
			updateUI();
		}

	}

	private class WorkspaceTreeLayoutRemovedListener implements LayoutRemovedListener {
		@Override
		public void layoutRemoved(LayoutRemovedEvent event) {
			for (int i = 0; i < treeNodeLayouts.getChildCount(); i++) {
				DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) treeNodeLayouts.getChildAt(i);
				Object tempNodeData = ((TreeNodeData) tempNode.getUserObject()).getData();
				if (tempNodeData.toString().trim().equals(event.getLayoutName().trim())) {
					treeModelTemp.removeNodeFromParent(tempNode);
				}
			}
		}

	}

	private class WorkspaceTreeLayoutRenamedListener implements LayoutRenamedListener {
		@Override
		public void layoutRenamed(LayoutRenamedEvent event) {
			int index = layouts.indexOf(event.getNewName());
			DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) treeNodeLayouts.getChildAt(index);
			TreeNodeData tempNodeData = new TreeNodeData(event.getNewName(), NodeDataType.LAYOUT_NAME);
			tempNode.setUserObject(tempNodeData);
			updateUI();
		}

	}

	private class WorkspaceTreeSceneAddedListener implements SceneAddedListener {
		@Override
		public void sceneAdded(SceneAddedEvent event) {
			TreeNodeData addSceneNodeData = new TreeNodeData(event.getSceneName(), NodeDataType.SCENE_NAME);
			DefaultMutableTreeNode addSceneNode = new DefaultMutableTreeNode(addSceneNodeData);
			treeModelTemp.insertNodeInto(addSceneNode, treeNodeScenes, treeNodeScenes.getChildCount());
		}

	}

	private class WorkspaceTreeSceneClearedListener implements SceneClearedListener {
		@Override
		public void sceneCleared(SceneClearedEvent event) {
			treeNodeScenes.removeAllChildren();
			updateUI();
		}

	}

	private class WorkspaceTreeSceneRemovedListener implements SceneRemovedListener {
		@Override
		public void sceneRemoved(SceneRemovedEvent event) {
			for (int i = 0; i < treeNodeScenes.getChildCount(); i++) {
				DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) treeNodeScenes.getChildAt(i);
				Object tempNodeData = ((TreeNodeData) tempNode.getUserObject()).getData();
				if (tempNodeData.toString().trim().equals(event.getSceneName().trim())) {
					treeModelTemp.removeNodeFromParent(tempNode);
				}
			}
		}
	}

	private class WorkspaceTreeSceneRenamedListener implements SceneRenamedListener {
		@Override
		public void sceneRenamed(SceneRenamedEvent event) {
			int index = scenes.indexOf(event.getNewName());
			DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) treeNodeScenes.getChildAt(index);
			TreeNodeData tempNodeData = new TreeNodeData(event.getNewName(), NodeDataType.SCENE_NAME);
			tempNode.setUserObject(tempNodeData);

			updateUI();
		}

	}

	private class WorkspaceTreeDatasetCollectionAddedListener implements DatasetCollectionListener {
		@Override
		public void datasetCollection(DatasetCollectionEvent event) {
			Dataset dataset = (Dataset) event.getSource();
			Datasource datasource = dataset.getDatasource();
			int datasourceIndex = datasources.indexOf(datasource.getAlias());
			int datasetIndex = datasource.getDatasets().indexOf(dataset.getName());

			DefaultMutableTreeNode sourceDatasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(datasourceIndex);
			DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) sourceDatasourceNode.getChildAt(datasetIndex);
			TreeNodeData addNodeData = null;
			if (dataset instanceof DatasetGridCollection) {
				addNodeData = new TreeNodeData(event.getAliasName(), NodeDataType.DATASET_GRID_COLLECTION_ITEM);
			} else if (dataset instanceof DatasetImageCollection) {
				addNodeData = new TreeNodeData(event.getAliasName(), NodeDataType.DATASET_IMAGE_COLLECTION_ITEM);
			}

			DefaultMutableTreeNode addNode = new DefaultMutableTreeNode(addNodeData);
			treeModelTemp.insertNodeInto(addNode, datasetNode, event.getIndex());

			updateUI();
		}
	}

	private class WorkspaceTreeDatasetCollectionRemovedListener implements DatasetCollectionListener {
		@Override
		public void datasetCollection(DatasetCollectionEvent event) {
			Dataset dataset = (Dataset) event.getSource();
			Datasource datasource = dataset.getDatasource();
			int datasourceIndex = datasources.indexOf(datasource.getAlias());
			int datasetIndex = datasource.getDatasets().indexOf(dataset.getName());

			DefaultMutableTreeNode sourceDatasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(datasourceIndex);
			DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) sourceDatasourceNode.getChildAt(datasetIndex);

			treeModelTemp.removeNodeFromParent((DefaultMutableTreeNode) datasetNode.getChildAt(event.getIndex()));
			updateUI();
		}
	}

	private class WorkspaceTreeDatasetCollectionRemovingListener implements DatasetCollectionListener {
		@Override
		public void datasetCollection(DatasetCollectionEvent event) {
			if (needShowMessageBox) {
				needShowMessageBox = false;
				String message = MessageFormat.format(ControlsProperties.getString("String_DeleteDataset_Makesure"), event.getAliasName());
				event.setCancel(UICommonToolkit.showConfirmDialog(message) != JOptionPane.YES_OPTION);
			}
		}
	}

	private class WorkspaceTreeDatasetCollectionRenamedListener implements DatasetCollectionRenameListener {
		@Override
		public void datasetCollectionRename(DatasetCollectionRenameEvent event) {
			Dataset dataset = (Dataset) event.getSource();
			Datasource datasource = dataset.getDatasource();
			int datasourceIndex = datasources.indexOf(datasource.getAlias());
			int datasetIndex = datasource.getDatasets().indexOf(dataset.getName());

			DefaultMutableTreeNode sourceDatasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(datasourceIndex);
			DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) sourceDatasourceNode.getChildAt(datasetIndex);

			int itemIndex = -1;
			if (dataset instanceof DatasetGridCollection) {
				itemIndex = ((DatasetGridCollection) dataset).indexOf(event.getNewAliasName());
			} else if (dataset instanceof DatasetImageCollection) {
				itemIndex = ((DatasetImageCollection) dataset).indexOf(event.getNewAliasName());
			}

			if (itemIndex >= 0) {
				DefaultMutableTreeNode itemtNode = (DefaultMutableTreeNode) datasetNode.getChildAt(itemIndex);
				TreeNodeData tempNodeData = new TreeNodeData(event.getNewAliasName(), NodeDataType.LAYOUT_NAME);
				itemtNode.setUserObject(tempNodeData);
				updateUI();
			}
		}
	}

	private class WorkspaceTreeDatasetCollectionClearedListener implements DatasetCollectionRemoveAllListener {
		@Override
		public void datasetCollectionRemoveAll(DatasetCollectionRemoveAllEvent event) {
			Dataset dataset = (Dataset) event.getSource();
			Datasource datasource = dataset.getDatasource();
			int datasourceIndex = datasources.indexOf(datasource.getAlias());
			int datasetIndex = datasource.getDatasets().indexOf(dataset.getName());

			DefaultMutableTreeNode sourceDatasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(datasourceIndex);
			DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) sourceDatasourceNode.getChildAt(datasetIndex);
			datasetNode.removeAllChildren();
			updateUI();
		}
	}

	private class WorkspaceTreeDatasetCollectionRefreshListener implements DatasetCollectionRequireRefreshListener {
		@Override
		public void datasetCollectionRequireRefresh(DatasetCollectionRequireRefreshEvent event) {
			Dataset dataset = (Dataset) event.getSource();
			Datasource datasource = dataset.getDatasource();
			int datasourceIndex = datasources.indexOf(datasource.getAlias());
			int datasetIndex = datasource.getDatasets().indexOf(dataset.getName());

			DefaultMutableTreeNode sourceDatasourceNode = (DefaultMutableTreeNode) treeNodeDatasources.getChildAt(datasourceIndex);
			DefaultMutableTreeNode datasetNode = (DefaultMutableTreeNode) sourceDatasourceNode.getChildAt(datasetIndex);
			datasetNode.removeAllChildren();

			if (dataset instanceof DatasetGridCollection) {
				fillGridCollectionNodes((DatasetGridCollection) dataset, datasetNode);
			} else if (dataset instanceof DatasetImageCollection) {
				fillImageCollectionNodes((DatasetImageCollection) dataset, datasetNode);
			}
			updateUI();
		}
	}

	private class WorkspaceTreeDatasetCollectionOrderChangedListener implements DatasetCollectionChangeOrderListener {
		@Override
		public void datasetCollectionChangeOrder(DatasetCollectionChangeOrderEvent event) {
			// TODO Auto-generated method stub
			// 这个对Java控件用处不大，暂时不写了，有需要再添加 by gouyu 2012-12-26
		}
	}

	@Override
	public void dispose() {
		removeListener();
		disposeDefaultWorkspace();
	}

	public Dataset firstAvailableDataset(Dataset[] datasets) {
		Dataset dataset = null;
		for (Dataset datasetTemp : datasets) {
			if (datasetTemp.getType() != DatasetType.TABULAR && datasetTemp.getType() != DatasetType.TOPOLOGY) {
				dataset = datasetTemp;
				break;
			}
		}
		return dataset;
	}

	/**
	 * 点击enter键打开地图
	 */
	public void openNewMap() {
		IFormMap formMap = null;
		Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
		if (0 < datasets.length) {
			// 有选中的数据集时才能打开新地图
			// 拖动实现将数据集打开到新地图
			for (Dataset dataset : datasets) {
				if (dataset.getType() != DatasetType.TABULAR && dataset.getType() != DatasetType.TOPOLOGY) {
					if (formMap == null) {
						String name = MapUtilties.getAvailableMapName(String.format("%s@%s", dataset.getName(), dataset.getDatasource().getAlias()), true);
						formMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, name);
					}
					if (formMap != null) {
						Map map = formMap.getMapControl().getMap();
						MapUtilties.addDatasetToMap(map, dataset, true);
						map.refresh();
						UICommonToolkit.getLayersManager().setMap(map);
						// 新建的地图窗口，修改默认的Action为漫游

						formMap.getMapControl().setAction(Action.PAN);
					}

				} else if (dataset.getType() == DatasetType.TABULAR) {
					// 如果带有纯属性数据集，在单独的属性窗口中打开
					IFormTabular formTabular = (IFormTabular) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.TABULAR,
							String.format("%s@%s", dataset.getName(), dataset.getDatasource().getAlias()));
					Recordset recordset = ((DatasetVector) dataset).getRecordset(false, CursorType.DYNAMIC);
					formTabular.setRecordset(recordset);
				}
			}
		}
	}

	class WorkspaceTreeDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			// 将数据存储到Transferable中，然后通知组件开始调用startDrag()初始化
			JTree tree = (JTree) dge.getComponent();
			TreePath path = tree.getSelectionPath();
			if (path != null) {
				DefaultMutableTreeNode selection = (DefaultMutableTreeNode) path.getLastPathComponent();
				WorkspaceTreeTransferable dragAndDropTransferable = new WorkspaceTreeTransferable(selection);
				dge.startDrag(DragSource.DefaultCopyDrop, dragAndDropTransferable, new WorkspaceTreeSourceListener());
			}
		}

	}

	class WorkspaceTreeSourceListener implements DragSourceListener {
		@Override
		public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
			if (dragSourceDropEvent.getDropSuccess()) {
				int dropAction = dragSourceDropEvent.getDropAction();
				if (dropAction == DnDConstants.ACTION_MOVE) {
					System.out.println("MOVE: remove node");
				}
			}
		}

		@Override
		public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
			DragSourceContext context = dragSourceDragEvent.getDragSourceContext();
			int dropAction = dragSourceDragEvent.getDropAction();
			if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
				context.setCursor(DragSource.DefaultCopyDrop);
			} else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
				context.setCursor(DragSource.DefaultMoveDrop);
			} else {
				context.setCursor(DragSource.DefaultCopyNoDrop);
			}

		}

		@Override
		public void dragExit(DragSourceEvent dragSourceEvent) {
			// do nothing
		}

		@Override
		public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
			WorkspaceTree tree = WorkspaceTree.this;
			JViewport vp = (JViewport) tree.getParent();

			Point vpMousePosition = dragSourceDragEvent.getLocation();
			SwingUtilities.convertPointFromScreen(vpMousePosition, vp);
			Rectangle treeVisibleRectangle = tree.getVisibleRect();

			if (vpMousePosition != null) {
				Integer newY = null;

				// Make sure we aren't already scrolled all the way down
				if (tree.getHeight() - treeVisibleRectangle.y != vp.getHeight()) {
					/*
					 * Get Y coordinate for scrolling down
					 */
					if (vp.getHeight() - vpMousePosition.y < 30 && vp.getHeight() - vpMousePosition.y > 0) {
						newY = treeVisibleRectangle.y + (30 + vpMousePosition.y - vp.getHeight()) * 2;
					}
				}

				// Make sure we aren't already scrolled all the way up
				if (newY == null && treeVisibleRectangle.y != 0) {
					/*
					 * Get Y coordinate for scrolling up
					 */
					if (30 > vpMousePosition.y && vpMousePosition.y > 0) {
						newY = treeVisibleRectangle.y - (30 - vpMousePosition.y) * 2;
					}
				}

				// Do the scroll
				if (newY != null) {
					Rectangle treeNewVisibleRectangle = new Rectangle(treeVisibleRectangle.x, newY, treeVisibleRectangle.width, treeVisibleRectangle.height);
					tree.scrollRectToVisible(treeNewVisibleRectangle);
				}
			}
		}

		@Override
		public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {

		}
	}

	class WorkspaceTreeTransferable implements Transferable {
		private DefaultMutableTreeNode treeNode;

		WorkspaceTreeTransferable(DefaultMutableTreeNode treeNode) {
			this.treeNode = treeNode;
		}

		DataFlavor[] flavors = { DataFlavor.javaFileListFlavor };
		DataFlavor[] flavosString = { DataFlavor.stringFlavor };

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			if (null == this.treeNode) {
				return flavors;
			}
			return flavosString;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return this.treeNode;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (flavor == DataFlavor.javaFileListFlavor || flavor == DataFlavor.stringFlavor) {
				return true;
			}
			return false;
		}

		public DefaultMutableTreeNode getTreeNode() {
			return treeNode;
		}

		public void setTreeNode(DefaultMutableTreeNode treeNode) {
			this.treeNode = treeNode;
		}

	}

	// 判断是否支持拖拽
	private boolean isDropAcceptable(DropTargetDropEvent event) {
		return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
	}

	/**
	 * 用于提供所涉及的 DropTarget 的 DnD 操作的通知
	 *
	 * @author xie
	 */
	private class WorkspaceTreeDropTargetAdapter extends DropTargetAdapter {
		@Override
		public void drop(DropTargetDropEvent dtde) {

			if (!isDropAcceptable(dtde)) {
				dtde.rejectDrop();
				return;
			}
			// 接收拖拽来的数据
			dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
			try {
				Transferable transferable = dtde.getTransferable();
				DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
				if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					for (int i = 0; i < dataFlavors.length; i++) {
						DataFlavor dataFlavor = dataFlavors[i];
						if (dataFlavor.equals(DataFlavor.javaFileListFlavor))// 如果拖入的是文件格式
						{
							@SuppressWarnings("unchecked")
							List<File> filelList = (List<File>) transferable.getTransferData(dataFlavor);
							Iterator<File> iterator = (Iterator<File>) filelList.iterator();
							while (iterator.hasNext()) {
								File file = (File) iterator.next();
								if (workspaceType == getFileType(file)) {
									// 关闭当前可能已经打开的地图和图层
									Datasources datasourcestemp = Application.getActiveApplication().getWorkspace().getDatasources();
									for (int j = 0; j < datasourcestemp.getCount(); j++) {
										Datasets datasets = datasourcestemp.get(j).getDatasets();
										CommonToolkit.DatasetWrap.CloseDataset(datasets);
									}
									// 关闭当前工作空间
									WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(file.getAbsolutePath());
									CommonToolkit.WorkspaceWrap.openWorkspace(connectionInfo, false);
								}
								// 打开数据源类型的文件
								if (datasourceType == getFileType(file)) {
									CommonToolkit.DatasourceWrap.openFileDatasource(file.getAbsolutePath(), null, true);
								}
							}
							dtde.dropComplete(true);// 指示拖拽操作已完成
						} else {
							// 拖拽结束时复制数据集
							if (dtde.getDropTargetContext().getDropTarget().getComponent() instanceof JTree) {
								JTree workSpaceTree = null;
								if (null != dtde.getDropTargetContext().getDropTarget().getComponent()) {
									// 对拖拽源进行判断赋值
									workSpaceTree = (JTree) dtde.getDropTargetContext().getDropTarget().getComponent();
								}
								int rowCount = 0;
								if (null != workSpaceTree && null != workSpaceTree.getMousePosition()) {
									// 工作空间树不为空且工作空间树的鼠标位置不为空时，获取鼠标所在行行号，否则默认选择第一行
									rowCount = workSpaceTree.getRowForLocation((int) workSpaceTree.getMousePosition().getX(), (int) workSpaceTree
											.getMousePosition().getY());
								}
								TreePath path = workSpaceTree.getPathForRow(rowCount);
								if (null != path && null != path.getLastPathComponent()) {
									DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
									TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
									if (null != selectedNodeData) {
										Datasource datasource = null;
										// 如果拖拽结束时对应的节点为数据源时直接复制到数据源上
										if (null != selectedNodeData && selectedNodeData.getData() instanceof Datasource) {
											datasource = (Datasource) selectedNodeData.getData();
											boolean isDoWork = false;
											if (datasource.isReadOnly()) {
												// 只读数据源不能复制
												String info = MessageFormat.format(
														ControlsProperties.getString("String_PluginDataEditor_MessageCopyDatasetOne"), datasource.getAlias());
												Application.getActiveApplication().getOutput().output(info);
												return;
											}
											Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
											if (0 == datasets.length) {
												return;
											}
											if (1 == datasets.length) {
												// 只复制一个数据集
												Dataset targetDataset = datasets[0];
												// 提示是否进行复制操作
												if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(MessageFormat.format(
														ControlsProperties.getString("String_CopyDataset_Makesure"), targetDataset.getName(),
														datasource.getAlias()))) {
													isDoWork = true;
												}
											} else {
												if (JOptionPane.OK_OPTION == UICommonToolkit.showConfirmDialog(MessageFormat.format(
														ControlsProperties.getString("String_CopyDataset_Makesure2"), String.valueOf(datasets.length),
														datasource.getAlias()))) {
													isDoWork = true;
												}
											}
											if (isDoWork) {
												FormProgressTotal formProgress = new FormProgressTotal();
												formProgress.doWork(new DatasetCopyCallable(datasource));
											}
										} else if (null != selectedNodeData && selectedNodeData.getData() instanceof Dataset) {
											// 如果拖拽结束时对应的节点为数据集时，追加未实现

										}

									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}

		}

	}

	/**
	 * 得到文件类型
	 *
	 * @param file
	 * @return
	 */
	private int getFileType(File file) {
		int flag = defaultType;
		String fileName = file.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if ("smwu".equalsIgnoreCase(fileType) || "sxwu".equalsIgnoreCase(fileType)) {
			flag = workspaceType;
		}
		if ("udb".equalsIgnoreCase(fileType) || "udd".equalsIgnoreCase(fileType)) {
			flag = datasourceType;
		}
		return flag;
	}

	public DropTarget getWorkspaceDropTarget() {
		return workspaceDropTarget;
	}

	public void setWorkspaceDropTarget(DropTarget workspaceDropTarget) {
		this.workspaceDropTarget = workspaceDropTarget;
	}

}
