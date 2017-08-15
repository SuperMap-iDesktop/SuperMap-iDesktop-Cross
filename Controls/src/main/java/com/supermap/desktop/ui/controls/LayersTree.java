package com.supermap.desktop.ui.controls;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.GeoStyle;
import com.supermap.data.SymbolType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.SortUIUtilities;
import com.supermap.desktop.controls.utilities.SymbolDialogFactory;
import com.supermap.desktop.dialog.symbolDialogs.ISymbolApply;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.CursorUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerAddedEvent;
import com.supermap.mapping.LayerAddedListener;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.LayerGroupAddedEvent;
import com.supermap.mapping.LayerGroupAddedListener;
import com.supermap.mapping.LayerGroupRemovedEvent;
import com.supermap.mapping.LayerGroupRemovedListener;
import com.supermap.mapping.LayerRemovedEvent;
import com.supermap.mapping.LayerRemovedListener;
import com.supermap.mapping.LayerSettingImage;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Map;
import com.supermap.mapping.Theme;
import com.supermap.mapping.ThemeGraph;
import com.supermap.mapping.ThemeGraphItem;
import com.supermap.mapping.ThemeGridRange;
import com.supermap.mapping.ThemeGridRangeItem;
import com.supermap.mapping.ThemeGridUnique;
import com.supermap.mapping.ThemeGridUniqueItem;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeRangeItem;
import com.supermap.mapping.ThemeUnique;
import com.supermap.mapping.ThemeUniqueItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * 图层管理树控件
 *
 * @author xuzw
 */
public class LayersTree extends JTree {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private transient Map currentMap = null;
	private transient LayerAddedListener layerAddedListener = null;
	private transient LayerRemovedListener layerRemovedListener = null;
	private transient LayerGroupAddedListener layerGroupAddedListener = null;
	private transient LayerGroupRemovedListener layerGroupRemovedListener = null;
	private transient KeyAdapter keyAdapter = null;
	private transient MouseAdapter mouseAdapter = null;
	private DropTarget dropTargetTemp;
	private DragSource dragSource;
	private int draggedNodeIndex = -1;
	private int dropTargetNodeIndex = -1;

	private DefaultMutableTreeNode dropTargetNode = null;
	private DefaultMutableTreeNode draggedNode = null;
	private DefaultTreeModel treeModeltemp;
	private boolean isUp = false;

	private static DataFlavor localObjectFlavor;
	private static DataFlavor[] supportedFlavors = {localObjectFlavor};

	static {
		try {
			localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException cnfe) {
			Application.getActiveApplication().getOutput().output(cnfe);
		}
	}

	public LayersTree() {
		super();
		this.treeModeltemp = new DefaultTreeModel(new DefaultMutableTreeNode(new Map()));
		this.setModel(this.treeModeltemp);
		this.setEditable(true);
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
		initDrag();
	}

	public LayersTree(Map map) {
		super();
		if (map == null) {
			TreeNodeData data = new TreeNodeData("", NodeDataType.UNKNOWN);
			this.treeModeltemp = new DefaultTreeModel(new DefaultMutableTreeNode(data));
			this.setModel(this.treeModeltemp);
		} else {
			this.currentMap = map;
			this.treeModeltemp = getTreeModel();
			this.setModel(this.treeModeltemp);
			LayersTreeCellRenderer cellRenderer = new LayersTreeCellRenderer();
			this.setCellRenderer(cellRenderer);
			this.setCellEditor(new LayersTreeCellEditor(this, cellRenderer));
			this.registerListeners();
		}
		this.setEditable(true);
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
		initDrag();
	}

	private void initDrag() {
		this.setDragEnabled(true);
		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new LayersTreeDragGestureListener());
		dropTargetTemp = new DropTarget(this, new LayersTreeDropTargetAdapter());
	}

	@Override
	public DropTarget getDropTarget() {
		return this.dropTargetTemp;
	}

	public Map getMap() {
		return currentMap;
	}

	public void setMap(Map map) {
		if (currentMap != null && !currentMap.equals(map)) {
			unRegisterListeners();
		}
		if (currentMap == null || !currentMap.equals(map)) {

			currentMap = map;
			if (currentMap == null) {
				TreeNodeData data = new TreeNodeData("", NodeDataType.UNKNOWN);
				this.treeModeltemp = new DefaultTreeModel(new DefaultMutableTreeNode(data));
				this.setModel(this.treeModeltemp);
			} else {
				this.treeModeltemp = getTreeModel();
				this.setModel(this.treeModeltemp);
				LayersTreeCellRenderer cellRenderer = new LayersTreeCellRenderer();
				this.setCellRenderer(cellRenderer);
				this.setCellEditor(new LayersTreeCellEditor(this, cellRenderer));
				this.registerListeners();
			}
		}
	}

	/**
	 * 控件关联的Map对象发生改变时，重新构建所有节点创建树控件
	 */
	public void reload() {
		if (currentMap != null) {
			this.setModel(getTreeModel());
		}
	}

	@Override
	public void paint(Graphics g) {
		if (!Beans.isDesignTime()) {
			super.paint(g);
		} else {
			g.drawString("LayersTree", this.getWidth() / 2 - 30, this.getHeight() / 2);
		}
	}

	@Override
	public boolean isPathEditable(TreePath path) {
		Object lastPathComponent = path.getLastPathComponent();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
		Object obj = node.getUserObject();
		TreeNodeData controlNodeData = (TreeNodeData) obj;
		NodeDataType type = controlNodeData.getType();
		if (type != NodeDataType.WMSSUB_LAYER) {
			return true;
		}
		return false;
	}

	// 返回位于(x,y)位置的HitTestInfo。当没有内容时，返回NULL
	public HitTestInfo hitTest(int x, int y) {
		HitTestInfo result = null;

		TreePath path = this.getPathForLocation(x, y);
		if (path != null) {
			Object object = path.getLastPathComponent();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;

			int offset = x - this.getUI().getPathBounds(this, path).x;
			/*
             * 修改为使用Render来计算点击的Icon类型 modified by gouyu 2010-12-24
			 */
			result = ((LayersTreeCellRenderer) this.getCellRenderer()).getHitTestIconType(node, offset);
		}

		return result;
	}

	/**
	 * 刷新指定图层，需要知道当前图层对应的treePath(路径)
	 *
	 * @param layer
	 * @param treePath
	 */
	public void refreshNode(Layer layer, TreePath treePath) {
		DefaultMutableTreeNode lastselectDefaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		TreeNodeData treeNodeData = (TreeNodeData) (lastselectDefaultMutableTreeNode).getUserObject();
		DefaultMutableTreeNode layerNode = null;
		if (treeNodeData.getData() instanceof Layer) {
			layerNode = lastselectDefaultMutableTreeNode;
		} else {
			layerNode = (DefaultMutableTreeNode) lastselectDefaultMutableTreeNode.getParent();
		}
		// 记住刷新之前树的状态
		Enumeration<TreePath> tempTreePath = null;
		if (null != layerNode) {
			tempTreePath = this.getExpandedDescendants(new TreePath(layerNode.getPath()));
			// 收起节点
			for (; tempTreePath != null && tempTreePath.hasMoreElements(); ) {
				this.setExpandedState(tempTreePath.nextElement(), false);
			}
			for (int childCount = layerNode.getChildCount() - 1; childCount >= 0; childCount--) {
				((DefaultTreeModel) getModel()).removeNodeFromParent((MutableTreeNode) layerNode.getChildAt(childCount));
			}
			// 删除指定节点
//			layerNode.removeAllChildren();
			// 添加子树
			addLayerItem(layer, layerNode);
		}
	}

	/**
	 * 针对专题图的刷新,通过传入当前图层来刷新 当前图层需在被选中状态下
	 *
	 * @param layer
	 */
	public void refreshNode(Layer layer) {
		try {
			DefaultMutableTreeNode lastselectDefaultMutableTreeNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
			refreshNodeByTreeNode(layer, lastselectDefaultMutableTreeNode);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void refreshNodeByTreeNode(Layer layer, DefaultMutableTreeNode lastselectDefaultMutableTreeNode) {
		if (null == lastselectDefaultMutableTreeNode) {
			return;
		}
		TreeNodeData treeNodeData = (TreeNodeData) (lastselectDefaultMutableTreeNode).getUserObject();
		DefaultMutableTreeNode layerNode = null;
		if (treeNodeData.getData() instanceof Layer) {
			layerNode = lastselectDefaultMutableTreeNode;
		} else {
			layerNode = (DefaultMutableTreeNode) lastselectDefaultMutableTreeNode.getParent();
		}
		// 记住刷新之前树的状态
		Enumeration<TreePath> tempTreePath = null;
		if (null != layerNode) {
			tempTreePath = this.getExpandedDescendants(new TreePath(layerNode.getPath()));
			// 删除指定节点
			for (int childCount = layerNode.getChildCount() - 1; childCount >= 0; childCount--) {
				((DefaultTreeModel) getModel()).removeNodeFromParent((MutableTreeNode) layerNode.getChildAt(childCount));
			}
			// // 第三步：添加子树，
			addLayerItem(layer, layerNode);
			// 恢复到刷新之前的状态
			for (; tempTreePath != null && tempTreePath.hasMoreElements(); ) {
				this.setExpandedState(tempTreePath.nextElement(), true);
			}
			this.setSelectionPath(new TreePath(layerNode.getPath()));
		}
//		updateUI();
	}

	private void addLayerItem(Layer layer, DefaultMutableTreeNode layerNode) {
		// 单值专题图
		if (layer.getTheme() instanceof ThemeUnique && ((ThemeUnique) layer.getTheme()).getCount() > 0) {
			ThemeUnique themeUnique = (ThemeUnique) layer.getTheme();
			for (int i = 0; i < themeUnique.getCount(); i++) {
				ThemeUniqueItem uniqueItem = themeUnique.getItem(i);
				TreeNodeData itemData = new TreeNodeData(uniqueItem, NodeDataType.THEME_UNIQUE_ITEM, layer);
				insertNode(itemData, layerNode, i);
			}
			return;
		}

		// 分段专题图
		if (layer.getTheme() instanceof ThemeRange && ((ThemeRange) layer.getTheme()).getCount() > 0) {
			ThemeRange themeRange = (ThemeRange) layer.getTheme();
			for (int i = 0; i < themeRange.getCount(); i++) {
				ThemeRangeItem rangeItem = themeRange.getItem(i);
				TreeNodeData itemData = new TreeNodeData(rangeItem, NodeDataType.THEME_RANGE_ITEM, layer);
				insertNode(itemData, layerNode, i);
			}
			return;
		}

		// 标签专题图
		if (layer.getTheme() instanceof ThemeLabel && ((ThemeLabel) layer.getTheme()).getCount() > 0) {
			ThemeLabel themeLabel = (ThemeLabel) layer.getTheme();

			for (int i = 0; i < themeLabel.getCount(); i++) {
				ThemeLabelItem labelItem = themeLabel.getItem(i);
				TreeNodeData itemData = new TreeNodeData(labelItem, NodeDataType.THEME_LABEL_ITEM, layer);
				insertNode(itemData, layerNode, i);
			}
			return;
		}
		// 栅格单值专题图
		if (layer.getTheme() instanceof ThemeGridUnique && ((ThemeGridUnique) layer.getTheme()).getCount() > 0) {
			ThemeGridUnique themeGridUnique = (ThemeGridUnique) layer.getTheme();

			for (int i = 0; i < themeGridUnique.getCount(); i++) {
				ThemeGridUniqueItem gridUniqueItem = themeGridUnique.getItem(i);
				TreeNodeData itemData = new TreeNodeData(gridUniqueItem, NodeDataType.THEME_GRID_UNIQUE_ITEM, layer);
				insertNode(itemData, layerNode, i);
			}
			return;
		}
		// 栅格分段专题图
		if (layer.getTheme() instanceof ThemeGridRange && ((ThemeGridRange) layer.getTheme()).getCount() > 0) {
			ThemeGridRange themeGridRange = (ThemeGridRange) layer.getTheme();

			for (int i = 0; i < themeGridRange.getCount(); i++) {
				ThemeGridRangeItem themeGridItem = themeGridRange.getItem(i);
				TreeNodeData itemData = new TreeNodeData(themeGridItem, NodeDataType.THEME_GRID_RANGE_ITEM, layer);
				insertNode(itemData, layerNode, i);
			}
			return;
		}
		// 统计专题图
		if (layer.getTheme() instanceof ThemeGraph && ((ThemeGraph) layer.getTheme()).getCount() > 0) {
			ThemeGraph themeGraph = (ThemeGraph) layer.getTheme();
			for (int i = 0; i < themeGraph.getCount(); i++) {
				ThemeGraphItem themeGraphItem = themeGraph.getItem(i);
				TreeNodeData itemData = new TreeNodeData(themeGraphItem, NodeDataType.THEME_GRAPH_ITEM, layer);
				insertNode(itemData, layerNode, i);
			}
			return;
		}
	}

	private void insertNode(TreeNodeData itemNodeData, DefaultMutableTreeNode parentNode, int count) {
		DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(itemNodeData);
		((DefaultTreeModel) this.getModel()).insertNodeInto(itemNode, parentNode, count);
	}

	private DefaultTreeModel getTreeModel() {
		Map activeMap = MapUtilities.getActiveMap();
		if (activeMap != null) {
			currentMap = activeMap;
		}
		TreeNodeData mapData = new TreeNodeData(currentMap.getName(), NodeDataType.UNKNOWN);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(mapData);
		if (currentMap.getWorkspace() != null) {
			int count = currentMap.getLayers().getCount();
			for (int layerIndex = 0; layerIndex < count; layerIndex++) {
				Layer layer = currentMap.getLayers().get(layerIndex);
				root.add(getNodeByLayer(layer));
			}
		}
		treeModeltemp = new DefaultTreeModel(root);
		return treeModeltemp;
	}

	/**
	 * modify by xuzw 2010-07-19 根据图层获取树节点，为了在Layer3DTree中能使用，修改为protected modified by gouyu 2010-12-23 使用单一出口重构，增加对THEMERANGE、THEMEUNIQUE、THEMECUSTOM类型的编辑的控制
	 *
	 * @param layer
	 * @return
	 */
	protected DefaultMutableTreeNode getNodeByLayer(Layer layer) {
		DefaultMutableTreeNode result = null;

		Dataset dataset = layer.getDataset();

		Theme theme = layer.getTheme();
		if (theme == null) {
			if (dataset == null) {
				// 分组图层节点的构建
				if (layer instanceof LayerGroup) {
					result = getGroupNodeByLayer(layer);
				} else {
					result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER));
				}
			} else {
				if (dataset.getType().equals(DatasetType.IMAGE)) {
					result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER_IMAGE));
				} else if (dataset.getType().equals(DatasetType.GRID)) {
					result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER_GRID));
				} else if (dataset.getType().equals(DatasetType.GRIDCOLLECTION)) {
					result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.DATASET_GRID_COLLECTION));
				} else if (dataset.getType().equals(DatasetType.IMAGECOLLECTION)) {
					result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.DATASET_IMAGE_COLLECTION));
				} else {
					if (dataset.getType().equals(DatasetType.WMS)) {
						result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER_WMS));
						LayerSettingImage layerSettingImage = (LayerSettingImage) layer.getAdditionalSetting();
						String[] visibleSubLayers = layerSettingImage.getVisibleSubLayers();
						for (int i = 0; i < visibleSubLayers.length; i++) {
							DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new TreeNodeData(visibleSubLayers[i], NodeDataType.WMSSUB_LAYER));
							result.add(childNode);
						}
					}
					if (result == null) {
						result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER));
					}

				}
			}
		} else {
			int type = theme.getType().value();

				/*
                 * 如果是1(ThemeUnique)、2(ThemeRange)、8(ThemeCustom)就使用自己的构造 以在专题图的基础上增加编辑功能 modified by gouyu 2010-12-23
				 */
			if (type != 1 && type != 2 && type != 8) {
				result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER_THEME));
			}
			switch (type) {
				// ThemeType.UNIQUE
				case 1:
					result = setCaseUnique(layer, theme);
					break;
				// ThemeType.RANGE
				case 2:
					result = setCaseRange(layer, theme);

					break;
				// ThemeType.GRAPH
				case 3:
					setCaseGraph(layer, result, theme);

					break;
				// ThemeType.LABEL
				case 7:
					setCaseLabel(layer, result, theme);

					break;
				// ThemeType.CUSTOM
				case 8:
					result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.THEME_CUSTOM));

					break;
				// ThemeType.GRIDUNIQUE
				case 11:
					setCaseGridUnique(layer, result, theme);

					break;
				// ThemeType.GRIDRANGE
				case 12:
					setCaseGridRange(layer, result, theme);

					break;
				default:
					break;
			}
		}

		return result;
	}

	private void setCaseGridRange(Layer layer, DefaultMutableTreeNode result, Theme theme) {
		if (layer.getDataset() != null) {
			ThemeGridRange gridRange = (ThemeGridRange) theme;
			for (int i = 0; i < gridRange.getCount(); i++) {
				ThemeGridRangeItem gridRangeItem = gridRange.getItem(i);
				TreeNodeData itemData = new TreeNodeData(gridRangeItem, NodeDataType.THEME_GRID_RANGE_ITEM, layer);
				result.add(new DefaultMutableTreeNode(itemData));
			}
		}
	}

	private void setCaseGridUnique(Layer layer, DefaultMutableTreeNode result, Theme theme) {
		if (layer.getDataset() != null) {
			ThemeGridUnique gridUnique = (ThemeGridUnique) theme;
			for (int i = 0; i < gridUnique.getCount(); i++) {
				ThemeGridUniqueItem gridUniqueItem = gridUnique.getItem(i);
				TreeNodeData itemData = new TreeNodeData(gridUniqueItem, NodeDataType.THEME_GRID_UNIQUE_ITEM, layer);
				result.add(new DefaultMutableTreeNode(itemData));
			}
		}
	}

	private void setCaseLabel(Layer layer, DefaultMutableTreeNode result, Theme theme) {
		if (layer.getDataset() != null) {
			ThemeLabel themeLabel = (ThemeLabel) theme;
			for (int i = 0; i < themeLabel.getCount(); i++) {
				ThemeLabelItem labelItem = themeLabel.getItem(i);
				TreeNodeData itemData = new TreeNodeData(labelItem, NodeDataType.THEME_LABEL_ITEM, layer);

				result.add(new DefaultMutableTreeNode(itemData));
			}
		}

	}

	private void setCaseGraph(Layer layer, DefaultMutableTreeNode result, Theme theme) {
		if (layer.getDataset() != null) {
			ThemeGraph graph = (ThemeGraph) theme;
			for (int i = 0; i < graph.getCount(); i++) {
				ThemeGraphItem graphItem = graph.getItem(i);
				TreeNodeData itemData = new TreeNodeData(graphItem, NodeDataType.THEME_GRAPH_ITEM, layer);

				result.add(new DefaultMutableTreeNode(itemData));
			}
		}
	}

	private DefaultMutableTreeNode setCaseRange(Layer layer, Theme theme) {
		DefaultMutableTreeNode result;
		result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.THEME_RANGE));
		if (layer.getDataset() != null) {
			ThemeRange range = (ThemeRange) theme;
			for (int i = 0; i < range.getCount(); i++) {
				ThemeRangeItem rangeItem = range.getItem(i);
				TreeNodeData itemData = new TreeNodeData(rangeItem, NodeDataType.THEME_RANGE_ITEM, layer);
				result.add(new DefaultMutableTreeNode(itemData));
			}
		}
		return result;
	}

	private DefaultMutableTreeNode setCaseUnique(Layer layer, Theme theme) {
		DefaultMutableTreeNode result;
		result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.THEME_UNIQUE));
		if (layer.getDataset() != null) {
			ThemeUnique themeUnique = (ThemeUnique) theme;
			for (int i = 0; i < themeUnique.getCount(); i++) {
				ThemeUniqueItem uniqueItem = themeUnique.getItem(i);
				TreeNodeData itemData = new TreeNodeData(uniqueItem, NodeDataType.THEME_UNIQUE_ITEM, layer);
				result.add(new DefaultMutableTreeNode(itemData));

			}
		}
		return result;
	}

	// 还有Group和Group里面的Layer的事件注册没有写
	private HashMap<LayerGroup, DefaultMutableTreeNode> groupNodeMap;

	private DefaultMutableTreeNode createLayerGroupNode(Layer layer) {

		if (groupNodeMap == null) {
			groupNodeMap = new HashMap<LayerGroup, DefaultMutableTreeNode>();
		}

		LayerGroup group = (LayerGroup) layer;
		group.addLayerAddedListener(new GroupLayerAddedListener());
		group.addLayerRemovedListener(new GroupLayerRemovedListener());

		DefaultMutableTreeNode result = new DefaultMutableTreeNode(new TreeNodeData(layer, NodeDataType.LAYER_GROUP));

		groupNodeMap.put((LayerGroup) layer, result);

		return result;
	}

	private class GroupLayerAddedListener implements LayerAddedListener {
		@Override
		public void layerAdded(LayerAddedEvent event) {
			LayerGroup group = (LayerGroup) event.getSource();
			DefaultMutableTreeNode parentNode = groupNodeMap.get(group);

			Layer layer = event.getLayer();
			DefaultTreeModel model = (DefaultTreeModel) getModel();
			// 将节点插入到与图层索引一致处
			int index = group.indexOf(layer);
			if (layer instanceof LayerGroup) {
				model.insertNodeInto(getGroupNodeByLayer(layer), parentNode, index);
			} else {

				model.insertNodeInto(getNodeByLayer(layer), parentNode, index);
			}
		}
	}


	private class GroupLayerRemovedListener implements LayerRemovedListener {
		@Override
		public void layerRemoved(LayerRemovedEvent event) {
			LayerGroup group = (LayerGroup) event.getSource();
			DefaultMutableTreeNode parentNode = groupNodeMap.get(group);

			DefaultTreeModel model = (DefaultTreeModel) getModel();
			model.removeNodeFromParent((MutableTreeNode) parentNode.getChildAt(event.getIndex()));
		}
	}


	protected DefaultMutableTreeNode getGroupNodeByLayer(Layer layer) {
		DefaultMutableTreeNode result = null;

		if (layer instanceof LayerGroup) {
			result = createLayerGroupNode(layer);
			LayerGroup layerGroup = (LayerGroup) layer;
			int count = layerGroup.getCount();

			for (int i = 0; i < count; i++) {
				Layer child = layerGroup.get(i);
				result.add(getGroupNodeByLayer(child));
			}
		} else {
			result = getNodeByLayer(layer);
		}

		return result;
	}

	private String removeLayersControlNode() {
		String layerName = "";
		try {
			TreePath[] paths = this.getSelectionPaths();
			if (null == paths || paths.length <= 0) {
				return layerName;
			}
			String message = null;
			if (paths.length == 1 && (((TreeNodeData) ((DefaultMutableTreeNode) paths[0].getLastPathComponent()).getUserObject()).getData() instanceof Layer)) {

				message = MessageFormat.format(ControlsProperties.getString("String_RemoveLayerInfo"),
						((Layer) ((TreeNodeData) ((DefaultMutableTreeNode) paths[0].getLastPathComponent()).getUserObject()).getData()).getName());
			} else {
				message = MessageFormat.format(ControlsProperties.getString("String_RemoveLayersInfo"), paths.length);
			}
			if (JOptionPane.OK_OPTION != UICommonToolkit.showConfirmDialog(message)) {
				return layerName;
			}
			for (int i = paths.length - 1; i >= 0; i--) {
				TreePath path = paths[i];
				Object lastPathComponent = path.getLastPathComponent();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
				Object obj = node.getUserObject();
				TreeNodeData controlNodeData = (TreeNodeData) obj;
				Object itemObj = controlNodeData.getData();
				if (itemObj instanceof Layer) {
					Layer layer = (Layer) itemObj;
					layerName = layer.getName();
					LayerGroup parentGroup = layer.getParentGroup();
					if (parentGroup != null) {
						parentGroup.remove(layer);
						layer = null;
					} else {
						currentMap.getLayers().remove(layer.getName());
						layer = null;
					}
				}
				if (controlNodeData.getType().equals(NodeDataType.THEME_GRAPH_ITEM)) {
					ThemeGraphItem item = (ThemeGraphItem) itemObj;
					Layer layer = controlNodeData.getParentLayer();
					ThemeGraph graph = (ThemeGraph) layer.getTheme();
					graph.remove(graph.indexOf(item.getGraphExpression()));
					((DefaultTreeModel) getModel()).removeNodeFromParent(node);
				}
				if (controlNodeData.getType().equals(NodeDataType.THEME_UNIQUE_ITEM)) {
					ThemeUniqueItem item = (ThemeUniqueItem) itemObj;
					Layer layer = controlNodeData.getParentLayer();
					ThemeUnique unique = (ThemeUnique) layer.getTheme();
					unique.remove(unique.indexOf(item.getUnique()));
					((DefaultTreeModel) getModel()).removeNodeFromParent(node);
				}
			}
			if (currentMap != null) {
				currentMap.refresh();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
			// do nothing
		}
		return layerName;
	}

	/**
	 * 关闭地图的时候清理
	 */
	public void clean() {
		unRegisterListeners();
	}

	private void unRegisterListeners() {
		if (currentMap.getWorkspace() != null) {
			currentMap.getLayers().removeLayerAddedListener(getLayerAddedListener());
			currentMap.getLayers().removeLayerRemovedListener(getLayerRemovedListener());
			currentMap.getLayers().removeLayerGroupAddedListener(getLayerGroupAddedListener());
			currentMap.getLayers().removeLayerGroupRemovedListener(getLayerGroupRemovedListener());

			removeKeyListener(keyAdapter);
			removeMouseListener(mouseAdapter);
		}
	}

	private void registerListeners() {
		if (currentMap.getWorkspace() != null) {
			currentMap.getLayers().addLayerAddedListener(getLayerAddedListener());
			currentMap.getLayers().addLayerRemovedListener(getLayerRemovedListener());
			currentMap.getLayers().addLayerGroupAddedListener(getLayerGroupAddedListener());
			currentMap.getLayers().addLayerGroupRemovedListener(getLayerGroupRemovedListener());

			addKeyListener(getKeyListener());
			addMouseListener(getMouseListener());
		}
	}

	LayerAddedListener getLayerAddedListener() {
		if (layerAddedListener == null) {
			layerAddedListener = new TreeLayerAddedListener();
		}
		return layerAddedListener;
	}

	private class TreeLayerAddedListener implements LayerAddedListener {
		@Override
		public void layerAdded(LayerAddedEvent event) {
			Layer layer = event.getLayer();
			DefaultTreeModel model = (DefaultTreeModel) getModel();
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
			// 将节点插入到与图层索引一致处
			model.insertNodeInto(getNodeByLayer(layer), rootNode, currentMap.getLayers().indexOf(layer.getName()));
		}
	}

	LayerRemovedListener getLayerRemovedListener() {
		if (layerRemovedListener == null) {
			layerRemovedListener = new TreeLayerRemovedListener();
		}
		return layerRemovedListener;
	}

	private class TreeLayerRemovedListener implements LayerRemovedListener {
		@Override
		public void layerRemoved(LayerRemovedEvent event) {
			DefaultTreeModel model = (DefaultTreeModel) getModel();
			Object obj = model.getChild(model.getRoot(), event.getIndex());
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
			model.removeNodeFromParent(node);
		}
	}

	LayerGroupAddedListener getLayerGroupAddedListener() {
		if (layerGroupAddedListener == null) {
			layerGroupAddedListener = new TreeLayerGroupAddedListener();
		}

		return layerGroupAddedListener;
	}

	private class TreeLayerGroupAddedListener implements LayerGroupAddedListener {
		@Override
		public void layerGroupAdded(LayerGroupAddedEvent e) {
			LayerGroup layerGroup = e.getAddedGroup();
			LayerGroup parentGroup = e.getParentGroup();

			if (parentGroup != null) {
				DefaultMutableTreeNode parentNode = groupNodeMap.get(parentGroup);
				if (parentNode != null) {
					DefaultTreeModel model = (DefaultTreeModel) getModel();
					if (parentNode.getChildCount() < e.getIndex()) {
						model.insertNodeInto(getGroupNodeByLayer(layerGroup), parentNode, e.getIndex() - 1);
					} else {
						model.insertNodeInto(getGroupNodeByLayer(layerGroup), parentNode, e.getIndex());
					}
				}
			} else {
				LayerGroup addedGroup = e.getAddedGroup();
				DefaultTreeModel model = (DefaultTreeModel) getModel();
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
				// 将节点插入到与图层索引一致处
				if (rootNode.getChildCount() < e.getIndex()) {
					model.insertNodeInto(getGroupNodeByLayer(addedGroup), rootNode, e.getIndex() - 1);
				} else {
					model.insertNodeInto(getGroupNodeByLayer(addedGroup), rootNode, e.getIndex());
				}
			}
		}
	}

	LayerGroupRemovedListener getLayerGroupRemovedListener() {
		if (layerGroupRemovedListener == null) {
			layerGroupRemovedListener = new TreeLayerGroupRemovedListener();
		}

		return layerGroupRemovedListener;
	}

	private class TreeLayerGroupRemovedListener implements LayerGroupRemovedListener {
		@Override
		public void layerGroupRemoved(LayerGroupRemovedEvent event) {
			LayerGroup layerGroup = event.getRemovedGroup();
			LayerGroup parentGroup = event.getParentGroup();

			if (parentGroup != null) {
				DefaultMutableTreeNode parentNode = groupNodeMap.get(parentGroup);

				DefaultMutableTreeNode removedNode = groupNodeMap.get(layerGroup);

				if (parentNode != null && removedNode != null) {
					parentNode.remove(removedNode);
				}
			} else {
				DefaultTreeModel model = (DefaultTreeModel) getModel();
				DefaultMutableTreeNode removedNode = groupNodeMap.get(layerGroup);

				if (removedNode != null) {
					model.removeNodeFromParent(removedNode);
				}
			}
		}
	}

	KeyAdapter getKeyListener() {
		if (keyAdapter == null) {
			keyAdapter = new TreeKeyListener();
		}
		return keyAdapter;
	}

	private class TreeKeyListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			int keyCode = e.getKeyChar();
			switch (keyCode) {
				case KeyEvent.VK_DELETE:
					String layerName = removeLayersControlNode();
					setSelectionRow(0);
					layerRemoved(layerName);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * 修改图层属性
	 */
	public void firePropertyChangeWithLayerSelect() {
		firePropertyChange("LayerChange", null, null);
	}

	/**
	 * 属性变化页面添加外接表事件
	 *
	 * @param layer
	 */
	public void fireLayerPropertyChanged(Layer layer) {
		firePropertyChange("LayerPropertyChanged", null, layer);
	}

	/**
	 * 删除图层事件
	 */
	public void layerRemoved(String layerName) {
		firePropertyChange("LayerRemoved", null, layerName);
	}

	MouseAdapter getMouseListener() {
		if (mouseAdapter == null) {
			mouseAdapter = new TreeMouseListener();
		}
		return mouseAdapter;
	}

	private class TreeMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			layersTreeMouseClicked(e);
			TreePath path = LayersTree.this.getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				HitTestInfo hitTestInfo = hitTest(e.getX(), e.getY());
				if (hitTestInfo != null) {

					TreeNodeData nodeData = hitTestInfo.getData();
					Object obj = nodeData.getData();

					int type = hitTestInfo.getIconType().value();

					if (type == 1) {
						setCaseOne(obj);
						refresh();
					} else if (type == 2 && LayersTreeUtilties.isTreeNodeDataVisible(nodeData.getData())) {
						setCaseTwo(obj);
						updateLater();
					} else if (type == 3 && LayersTreeUtilties.isTreeNodeDataVisible(nodeData.getData())) {
						setCaseThree(obj);
						updateLater();
					} else if (type == 4 && LayersTreeUtilties.isTreeNodeDataVisible(nodeData.getData())) {
						setCaseFour(obj);
						updateLater();
					}
				}
			}
		}

		private void refresh() {
			updateLater();
			currentMap.refresh();
			firePropertyChangeWithLayerSelect();
		}

		private void setCaseFour(Object obj) {
			if (obj instanceof Layer) {
				Layer layer = (Layer) obj;
				layer.setSnapable(!layer.isSnapable());
			}
		}

		private void setCaseThree(Object obj) {
			if (obj instanceof Layer) {
				Layer layer = (Layer) obj;
				layer.setEditable(!layer.isEditable());
			}
		}

		private void setCaseTwo(Object obj) {
			if (obj instanceof Layer) {
				Layer layer = (Layer) obj;
				layer.setSelectable(!layer.isSelectable());
			}
		}

		private void setCaseOne(Object obj) {
			if (obj instanceof Layer) {
				Layer layer = (Layer) obj;
				layer.setVisible(!layer.isVisible());
				if (null != layer.getTheme()) {
					Theme tempTheme = layer.getTheme();
					if (tempTheme instanceof ThemeUnique && 0 < ((ThemeUnique) tempTheme).getCount()) {
						for (int i = 0; i < ((ThemeUnique) tempTheme).getCount(); i++) {
							((ThemeUnique) tempTheme).getItem(i).setVisible(layer.isVisible());
						}
						return;
					}
					if (tempTheme instanceof ThemeRange && 0 < ((ThemeRange) tempTheme).getCount()) {
						for (int i = 0; i < ((ThemeRange) tempTheme).getCount(); i++) {
							((ThemeRange) tempTheme).getItem(i).setVisible(layer.isVisible());
						}
						return;
					}
					if (tempTheme instanceof ThemeLabel && 0 < ((ThemeLabel) tempTheme).getCount()) {
						for (int i = 0; i < ((ThemeLabel) tempTheme).getCount(); i++) {
							((ThemeLabel) tempTheme).getItem(i).setVisible(layer.isVisible());
						}
						return;
					}
					if (tempTheme instanceof ThemeGridUnique && 0 < ((ThemeGridUnique) tempTheme).getCount()) {
						for (int i = 0; i < ((ThemeGridUnique) tempTheme).getCount(); i++) {
							((ThemeGridUnique) tempTheme).getItem(i).setVisible(layer.isVisible());
						}
						return;
					}
					if (tempTheme instanceof ThemeGridRange && 0 < ((ThemeGridRange) tempTheme).getCount()) {
						for (int i = 0; i < ((ThemeGridRange) tempTheme).getCount(); i++) {
							((ThemeGridRange) tempTheme).getItem(i).setVisible(layer.isVisible());
						}
						return;
					}
				}
			}
			if (obj instanceof ThemeUniqueItem) {
				ThemeUniqueItem item = (ThemeUniqueItem) obj;
				item.setVisible(!item.isVisible());
				return;
			}
			if (obj instanceof ThemeRangeItem) {
				ThemeRangeItem item = (ThemeRangeItem) obj;
				item.setVisible(!item.isVisible());
				return;
			}
			if (obj instanceof ThemeGridUniqueItem) {
				ThemeGridUniqueItem item = (ThemeGridUniqueItem) obj;
				item.setVisible(!item.isVisible());
				return;
			}
			if (obj instanceof ThemeGridRangeItem) {
				ThemeGridRangeItem item = (ThemeGridRangeItem) obj;
				item.setVisible(!item.isVisible());
				return;
			}
			if (obj instanceof ThemeLabelItem) {
				ThemeLabelItem item = (ThemeLabelItem) obj;
				item.setVisible(!item.isVisible());
				return;
			}
		}

		private void layersTreeMouseClicked(MouseEvent e) {
			try {
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					TreePath path = getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						Object object = path.getLastPathComponent();
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						TreeNodeData data = (TreeNodeData) node.getUserObject();
						NodeDataType type = data.getType();
						if (type.equals(NodeDataType.LAYER)) {
							Layer layer = (Layer) data.getData();
							if (layer.getTheme() == null) {
								// 设置图层属性
								showStyleSetDialog();
							}
						} else {
							// 修改专题图风格
							DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
							TreeNodeData parentNodeData = (TreeNodeData) parentNode.getUserObject();
							if (parentNodeData.getData() instanceof Layer) {
								Layer parentLayer = (Layer) parentNodeData.getData();
								showThemeItemStyleSetDialog(parentLayer);
							}
						}
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			}
		}
	}

	private void updateLater() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateUI();
			}
		});
	}

	/**
	 * 弹出风格设置窗口，返回选中的新风格
	 */
	public void showStyleSetDialog() {
		try {
			SymbolType symbolType = SymbolType.MARKER;
			TreePath[] selections = this.getSelectionPaths();

			Layer layer = null;
			for (int index = 0; index < selections.length; index++) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) selections[index].getLastPathComponent();
				TreeNodeData treeNodeData = (TreeNodeData) treeNode.getUserObject();
				Layer tempLayer = (Layer) treeNodeData.getData();
				if (tempLayer != null && tempLayer.getTheme() == null && tempLayer.getDataset() != null) {
					if (CommonToolkit.DatasetTypeWrap.isPoint(tempLayer.getDataset().getType())) {
						symbolType = SymbolType.MARKER;
						layer = tempLayer;
						break;
					} else if (CommonToolkit.DatasetTypeWrap.isLine(tempLayer.getDataset().getType())) {
						symbolType = SymbolType.LINE;
						layer = tempLayer;
						break;
					} else if (CommonToolkit.DatasetTypeWrap.isRegion(tempLayer.getDataset().getType())) {
						symbolType = SymbolType.FILL;
						layer = tempLayer;
						break;
					}
				}
			}
			java.util.List<GeoStyle> geoStyleList = new ArrayList<>();
			for (TreePath selection : selections) {
				Layer layerSelected = (Layer) ((TreeNodeData) ((DefaultMutableTreeNode) selection.getLastPathComponent()).getUserObject()).getData();
				if (layerSelected.getTheme() == null && layerSelected.getAdditionalSetting() != null) {
					geoStyleList.add(((LayerSettingVector) layerSelected.getAdditionalSetting()).getStyle());
				}
			}
			// notify by huchenpu 2015-06-30
			// 多选需要让用户指定设置哪些风格，现在暂时先只处理第一个图层
			if (layer != null && geoStyleList.size() > 1) {
				JDialogSymbolsChange jDialogSymbolsChange = new JDialogSymbolsChange(symbolType, geoStyleList);
				if (jDialogSymbolsChange.showDialog() == DialogResult.OK) {
					this.currentMap.refresh();
				}
				updateLater();
				// }
			} else if (layer != null && geoStyleList.size() == 1) {
				GeoStyle layerStyle = geoStyleList.get(0);
				final Layer finalLayer = layer;
				GeoStyle geostyle = changeGeoStyle(layerStyle, symbolType, new ISymbolApply() {
					@Override
					public void apply(GeoStyle geoStyle) {
						LayerSettingVector layerSetting = (LayerSettingVector) finalLayer.getAdditionalSetting();
						layerSetting.setStyle(geoStyle);
						currentMap.refresh();
						updateLater();
					}
				});
				if (geostyle != null) {
					LayerSettingVector layerSetting = (LayerSettingVector) layer.getAdditionalSetting();
					layerSetting.setStyle(geostyle);
					this.currentMap.refresh();
					updateLater();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private SymbolType getSymbolType(Dataset dataset) {
		if (dataset == null) {
			return SymbolType.MARKER;
		}
		SymbolType symbolType = SymbolType.MARKER;
		if (CommonToolkit.DatasetTypeWrap.isPoint(dataset.getType())) {
			symbolType = SymbolType.MARKER;
		} else if (CommonToolkit.DatasetTypeWrap.isLine(dataset.getType())) {
			symbolType = SymbolType.LINE;
		} else if (CommonToolkit.DatasetTypeWrap.isRegion(dataset.getType())) {
			symbolType = SymbolType.FILL;
		}
		return symbolType;
	}

	private void showThemeItemStyleSetDialog(Layer layer) {
		try {
			SymbolType symbolType = getSymbolType(layer.getDataset());
			TreePath[] selections = this.getSelectionPaths();
			// notify by xie
			// 多选需要让用户指定设置哪些风格，现在暂时先只处理第一个图层
			if (selections.length > 1) {
				upateItemStyles(symbolType, selections);
			} else {
				DefaultMutableTreeNode selecTreeNode = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
				TreeNodeData treeNodeData = (TreeNodeData) (selecTreeNode).getUserObject();
				int row = this.getRowForPath(new TreePath(selecTreeNode.getPath()));
				int x = this.getRowBounds(row).x;
				int y = this.getRowBounds(row).y;
				if (treeNodeData.getData() instanceof ThemeUniqueItem) {
					updateThemeUniqueItemStyle(symbolType, treeNodeData);
					return;
				}
				if (treeNodeData.getData() instanceof ThemeRangeItem) {
					updateRangeItemStyle(symbolType, treeNodeData);
					return;
				}
				if (treeNodeData.getData() instanceof ThemeGridUniqueItem) {
					updateGridUniqueItemStyle(treeNodeData, x, y);
					return;
				}
				if (treeNodeData.getData() instanceof ThemeGridRangeItem) {
					updateGridRangeItemStyle(treeNodeData, x, y);
					return;
				}
				if (treeNodeData.getData() instanceof ThemeGraphItem) {
					updateGraphItemStyle(treeNodeData);
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void updateGraphItemStyle(TreeNodeData treeNodeData) {
		final ThemeGraphItem item = (ThemeGraphItem) treeNodeData.getData();
		GeoStyle itemStyle = item.getUniformStyle();
		GeoStyle geostyle = changeGeoStyle(itemStyle, SymbolType.FILL, new ISymbolApply() {
			@Override
			public void apply(GeoStyle geoStyle) {
				item.setUniformStyle(geoStyle);
				currentMap.refresh();
				firePropertyChangeWithLayerSelect();
			}
		});
		if (geostyle != null) {
			item.setUniformStyle(geostyle);
			this.currentMap.refresh();
			firePropertyChangeWithLayerSelect();
		}
	}

	private void updateGridRangeItemStyle(TreeNodeData treeNodeData, int x, int y) {
		final ThemeGridRangeItem item = (ThemeGridRangeItem) treeNodeData.getData();
		final JPopupMenu popupMenu = new JPopupMenu();
		ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
		popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
		colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
		popupMenu.show(this, x, y);
		colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Color color = (Color) evt.getNewValue();
				item.setColor(color);
				popupMenu.setVisible(false);
				currentMap.refresh();
				firePropertyChangeWithLayerSelect();
			}
		});
	}

	private void updateGridUniqueItemStyle(TreeNodeData treeNodeData, int x, int y) {
		final ThemeGridUniqueItem item = (ThemeGridUniqueItem) treeNodeData.getData();
		final JPopupMenu popupMenu = new JPopupMenu();
		ColorSelectionPanel colorSelectionPanel = new ColorSelectionPanel();
		popupMenu.add(colorSelectionPanel, BorderLayout.CENTER);
		colorSelectionPanel.setPreferredSize(new Dimension(170, 205));
		popupMenu.show(this, x, y);
		colorSelectionPanel.addPropertyChangeListener("m_selectionColor", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Color color = (Color) evt.getNewValue();
				item.setColor(color);
				popupMenu.setVisible(false);
				currentMap.refresh();
				firePropertyChangeWithLayerSelect();
			}
		});
	}

	private void updateRangeItemStyle(SymbolType symbolType, TreeNodeData treeNodeData) {
		final ThemeRangeItem item = (ThemeRangeItem) treeNodeData.getData();
		GeoStyle itemStyle = item.getStyle();
		GeoStyle geostyle = changeGeoStyle(itemStyle, symbolType, new ISymbolApply() {
			@Override
			public void apply(GeoStyle geoStyle) {
				item.setStyle(geoStyle);
				currentMap.refresh();
				firePropertyChangeWithLayerSelect();
			}
		});
		if (geostyle != null) {
			item.setStyle(geostyle);
			this.currentMap.refresh();
			firePropertyChangeWithLayerSelect();
		}
	}

	private void updateThemeUniqueItemStyle(SymbolType symbolType, TreeNodeData treeNodeData) {
		final ThemeUniqueItem item = (ThemeUniqueItem) treeNodeData.getData();
		GeoStyle itemStyle = item.getStyle();
		GeoStyle geostyle = changeGeoStyle(itemStyle, symbolType, new ISymbolApply() {
			@Override
			public void apply(GeoStyle geoStyle) {
				item.setStyle(geoStyle);
				currentMap.refresh();
				firePropertyChangeWithLayerSelect();
			}
		});
		if (geostyle != null) {
			item.setStyle(geostyle);
			this.currentMap.refresh();
			firePropertyChangeWithLayerSelect();
		}
	}

	private void upateItemStyles(SymbolType symbolType, TreePath[] selections) {
		java.util.List<GeoStyle> geoStyleList = new ArrayList<>();
		for (TreePath selection : selections) {
			TreeNodeData treeNodeData = (TreeNodeData) ((DefaultMutableTreeNode) selection.getLastPathComponent()).getUserObject();
			if (treeNodeData.getData() instanceof ThemeUniqueItem) {
				ThemeUniqueItem item = (ThemeUniqueItem) treeNodeData.getData();
				geoStyleList.add(item.getStyle());
			}
			if (treeNodeData.getData() instanceof ThemeRangeItem) {
				ThemeRangeItem item = (ThemeRangeItem) treeNodeData.getData();
				geoStyleList.add(item.getStyle());
			}
			if (treeNodeData.getData() instanceof ThemeGraphItem) {
				ThemeGraphItem item = (ThemeGraphItem) treeNodeData.getData();
				geoStyleList.add(item.getUniformStyle());
				symbolType = SymbolType.FILL;
			}
		}
		JDialogSymbolsChange jDialogSymbolsChange = new JDialogSymbolsChange(symbolType, geoStyleList);
		if (jDialogSymbolsChange.showDialog() == DialogResult.OK) {
			firePropertyChangeWithLayerSelect();
			this.currentMap.refresh();
		}
		updateLater();
	}

	private GeoStyle changeGeoStyle(GeoStyle beforeStyle, SymbolType symbolType, ISymbolApply symbolApply) {
		GeoStyle result = null;
		SymbolDialog symbolDialog = null;
		try {
			CursorUtilities.setWaitCursor();
			symbolDialog = SymbolDialogFactory.getSymbolDialog(symbolType);
			DialogResult dialogResult = symbolDialog.showDialog(beforeStyle, symbolApply);
			if (dialogResult == DialogResult.OK) {
				result = symbolDialog.getCurrentGeoStyle();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			CursorUtilities.setDefaultCursor();
		}
		return result;
	}

	public Layer[] getCurrentSelectLayers() {
		if (this.getLastSelectedPathComponent() == null) {
			return null;
		}
		TreePath[] selectionPaths = this.getSelectionPaths();
		ArrayList<Layer> seleteLayers = new ArrayList<>();
		for (TreePath selectionPath : selectionPaths) {
			Layer layer = (Layer) ((TreeNodeData) ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject()).getData();
			seleteLayers.add(layer);
		}
		return seleteLayers.toArray(new Layer[seleteLayers.size()]);
	}

	/**
	 * DragGestureListener:当该（子）类的对象检测到拖动启动动作时，调用此接口
	 *
	 * @author wangdy
	 */
	private class LayersTreeDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			Point clickPoint = dge.getDragOrigin();
			TreePath path = getClosestPathForLocation(clickPoint.x, clickPoint.y);
			if (path != null && null != path.getLastPathComponent()) {
				draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				draggedNodeIndex = ((DefaultMutableTreeNode) LayersTree.this.getModel().getRoot()).getIndex(draggedNode);
				dropTargetNodeIndex = -1;

				// 判断拖拽的两个节点的父节点是否是根节点
				if (getRowForPath(getSelectionPath()) == -1 || !draggedNode.getParent().equals(getModel().getRoot()))
					return;
				Transferable trans = new RJLTransferable(draggedNode);
				dge.startDrag(DragSource.DefaultMoveDrop, trans, new MyDragSourceAdapter());
			}
		}
	}

	private class RJLTransferable implements Transferable {
		Object object;

		public RJLTransferable(Object o) {
			object = o;
		}

		@Override
		public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(df))
				return object;
			else
				return null;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor df) {
			boolean result = false;
			if (null != df && df.equals(localObjectFlavor)) {
				result = true;
			}
			return result;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return supportedFlavors;
		}
	}

	/**
	 * 用于提供所涉及的 DropTarget 的 DnD 操作的通知
	 *
	 * @author wangdy
	 */
	private class LayersTreeDropTargetAdapter extends DropTargetAdapter {
		private TreePath treePath = null;
		// 用于存上一次的矩形
		private Rectangle recordBounds = null;
		private DefaultMutableTreeNode oldNode = null;

		private int oldRow = 0;
		// 标志是向上还是向下拖动，true表示向上


		@Override
		public void drop(DropTargetDropEvent dtde) {
			recordBounds = null;
			oldNode = null;
			DataFlavor[] dataFlavors = dtde.getTransferable().getTransferDataFlavors();
			if (dataFlavors[0] != null) {
				// 拖进来的奇奇怪怪的东西屏蔽掉。以后在这里做判断拖工作空间树的东西进来的时候增加图层。
				return;
			}
			oldRow = getRowForPath(getSelectionPath());
			try {
				if (oldRow == -1 || dropTargetNodeIndex == -1 || draggedNodeIndex == dropTargetNodeIndex)
					return;
				moveRow();
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}


		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			JTree tree = LayersTree.this;
			JViewport vp = (JViewport) tree.getParent();

			Point vpMousePosition = dtde.getLocation();
			vpMousePosition = SwingUtilities.convertPoint(tree, vpMousePosition, vp.getParent());
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


			Point dragPoint = dtde.getLocation();
			if (dragPoint == null)
				return;
			if (null != getPathForLocation(dragPoint.x, dragPoint.y)) {
				treePath = getPathForLocation(dragPoint.x, dragPoint.y);
			}
			if (isPathSelected(treePath) || treePath == null) {
				dropTargetNode = null;
				dropTargetNodeIndex = -1;
				return;
			} else {
				dropTargetNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
				// 如果目标节点跟被拖拽节点的父节点不同，则将目标节点回滚到上一次
				if (null != draggedNode && !dropTargetNode.getParent().equals(draggedNode.getParent())) {
					dropTargetNode = oldNode;
					dropTargetNodeIndex = -1;
					return;
				}
				if (oldNode == null || oldRow == -1) {
					oldNode = dropTargetNode;
					oldRow = getRowForPath(getSelectionPath());
				}
			}
			if (oldNode != null)
				repaintTree();
		}

		/**
		 * 重新绘制树
		 */
		private void repaintTree() {
			Graphics2D graphics2D = (Graphics2D) getGraphics();
			Color color = graphics2D.getColor();
			Rectangle bounds = getPathBounds(treePath);
			if (bounds == null) {
				bounds = new Rectangle(0, 0, 0, 0);
			}
			dropTargetNodeIndex = ((DefaultMutableTreeNode) getModel().getRoot()).getIndex(dropTargetNode);

			// 被拖拽的节点索引大于目标节点索引，则表示向上拖拽，否则表示向下，在相应的位置画线
			if (draggedNodeIndex > dropTargetNodeIndex) {
				graphics2D.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y);
				isUp = true;
			} else if (draggedNodeIndex < dropTargetNodeIndex) {
				graphics2D.drawLine(bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y + bounds.height);
				isUp = false;
			}

			if (recordBounds == null) {
				recordBounds = bounds;
			} else {
				if (!recordBounds.equals(bounds)) {
					// 将graphics2D置为白色，用于清除上一次的线
					graphics2D.setColor(Color.white);
					if (isUp) {
						graphics2D.drawLine(recordBounds.x, recordBounds.y, recordBounds.x + recordBounds.width, recordBounds.y);
					} else {
						graphics2D.drawLine(recordBounds.x, recordBounds.y + recordBounds.height, recordBounds.x + recordBounds.width, recordBounds.y
								+ recordBounds.height);
					}
				}
			}
			recordBounds = bounds;
			oldNode = dropTargetNode;
			oldRow = getRowForPath(getSelectionPath());

			JPanel panel = (JPanel) getCellRenderer().getTreeCellRendererComponent(LayersTree.this, oldNode, false, isExpanded(treePath),
					getModel().isLeaf(oldNode), oldRow, false);
			panel.repaint();
			// 将graphics2D置回原来的颜色
			graphics2D.setColor(color);
		}
	}

	private void moveRow() {
		if (dropTargetNodeIndex == -1) {
			return;
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getModel().getRoot();
		int[] selectionRows = getSelectionRows();
		Integer[] integers = new Integer[selectionRows.length];
		for (int i = 0; i < selectionRows.length; i++) {
			integers[i] = selectionRows[i];
		}
		SortUIUtilities.sortList(integers);
		int count = 0; // 记录有多少图层移动到本图层之前
		int lowerCount = 0;// 记录有多少图层本来在目标图层之前
		for (int i = selectionRows.length - 1; i >= 0; i--) {
			int currentIndex = integers[i];
			int tempTargetRow = LayersTree.this.dropTargetNodeIndex;
			if (currentIndex >= tempTargetRow) {
				currentIndex += count;
			} else {
				if (i != selectionRows.length - 1) {
					tempTargetRow -= lowerCount;
				}
				lowerCount++;
			}
			count++;
			draggedNode = (DefaultMutableTreeNode) parent.getChildAt(currentIndex);
			((DefaultTreeModel) getModel()).removeNodeFromParent(draggedNode);
			((DefaultTreeModel) getModel()).insertNodeInto(draggedNode, parent, tempTargetRow);
			currentMap.getLayers().moveTo(currentIndex, tempTargetRow);
		}
		if (isUp) {
			lowerCount++;
		}
		setSelectionInterval(dropTargetNodeIndex - lowerCount + 1, dropTargetNodeIndex + selectionRows.length - lowerCount);
		currentMap.refresh();
		dropTargetNodeIndex = -1;
	}

	/**
	 * Drag 和 Drop 操作为用户提供合适的“拖动结束”反馈
	 *
	 * @author wangdy
	 */
	private class MyDragSourceAdapter extends DragSourceAdapter {
		@Override
		public void dragDropEnd(DragSourceDropEvent dsde) {
			moveRow();
			dropTargetNode = null;
			draggedNode = null;
			LayersTree.this.repaint();
		}
	}

}
