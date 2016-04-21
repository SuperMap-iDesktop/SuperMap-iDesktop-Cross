package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.mapping.Layer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 图层管理树单元格渲染器
 * 
 * @author xuzw
 *
 */
public class LayersTreeCellRenderer implements TreeCellRenderer {
	public final static int HGAP = 2;
	private Map<NodeDataType, ArrayList<ArrayList<TreeNodeDecorator>>> decoratorsMap;
	private JPanel jPanelLayer;

	public LayersTreeCellRenderer() {
		initDecoratorsMap();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component component = null;
		try {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (null != node.getUserObject()) {
				TreeNodeData data = (TreeNodeData) node.getUserObject();
				component = getPanel(data);
				if (selected) {
					component.setBackground(new Color(150, 185, 255));
				} else {
					component.setBackground(Color.WHITE);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return component;
	}

	public void setDecoratorLists(NodeDataType type, ArrayList<ArrayList<TreeNodeDecorator>> lists) {
		decoratorsMap.put(type, lists);
	}

	public ArrayList<ArrayList<TreeNodeDecorator>> getDecoratorLists(NodeDataType type) {
		return decoratorsMap.get(type);
	}

	HitTestInfo getHitTestIconType(DefaultMutableTreeNode node, int offset) {
		HitTestInfo result = null;
		HitTestIconType iconType = HitTestIconType.NONE;
		TreeNodeData data = (TreeNodeData) node.getUserObject();
		jPanelLayer = this.getPanel(data);
		/*
		 * 获取点击的位置所在的icon的偏移序号 modified by gouyu 2010-12-24
		 */
		int index = (int) (offset / (TreeNodeDecorator.IMAGEICON_WIDTH + LayersTreeCellRenderer.HGAP));

		/*
		 * 偏移要大于0，同时序号应该在0到panel的子对象的个数之间 modified by gouyu 2010-12-24
		 */
		if (offset >= 0 && index >= 0 && index < jPanelLayer.getComponentCount()) {
			TreeNodeDecorator decorator = this.getDecoratorLists(data.getType()).get(index).get(0);
			if (decorator != null) {
				iconType = HitTestIconType.TYPE;

				if (decorator instanceof VisibleDecorator) {
					// 只读控制
					if ((data.getData() instanceof Layer) && (((Layer) data.getData()).getDataset() != null || data.getType() == NodeDataType.LAYER_GROUP)) {
						iconType = HitTestIconType.VISIBLE;
					} else {
						iconType = HitTestIconType.VISIBLE;
					}
				} else if (decorator instanceof SelectableDecorator) {
					iconType = HitTestIconType.SELECTABLE;
				} else if (decorator instanceof SnapableDecorator) {
					iconType = HitTestIconType.SNAPABLE;
				} else if (decorator instanceof EditableDecorator) {
					iconType = HitTestIconType.EDITABLE;
				}
			}

			result = new HitTestInfo(node, index, iconType);
			result.setIndexCount(jPanelLayer.getComponentCount());

			if ((iconType == HitTestIconType.EDITABLE && (data.getData() instanceof Layer))
					&& (((Layer) data.getData()).getDataset() == null || ((Layer) data.getData()).getDataset().isReadOnly())) {
				result = null;
			}
		}

		return result;
	}

	JPanel getPanel(TreeNodeData data) {
		jPanelLayer = new JPanel();
		jPanelLayer.setLayout(new FlowLayout(FlowLayout.CENTER, HGAP, 0));
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<>();

		arrayList = decoratorsMap.get(data.getType());
		for (ArrayList<TreeNodeDecorator> list : arrayList) {
			JLabel label = new JLabel(new ImageIcon());
			
			for (TreeNodeDecorator decorator : list) {
				decorator.decorate(label, data);
			}
			jPanelLayer.add(label);
		}

		return jPanelLayer;
	}

	private void initDecoratorsMap() {
		decoratorsMap = new HashMap<NodeDataType, ArrayList<ArrayList<TreeNodeDecorator>>>();

		/*
		 * 普通图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> vectorDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> vectorList = new ArrayList<TreeNodeDecorator>();
		vectorList.add(new LayerNodeDecorator());
		vectorDecoratorList.add(vectorList);
		decoratorsMap.put(NodeDataType.LAYER, vectorDecoratorList);


		/*
		 * 影像图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> imageDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> imageList = new ArrayList<TreeNodeDecorator>();
		imageList.add(new LayerImageNodeDecorator());
		imageDecoratorList.add(imageList);
		decoratorsMap.put(NodeDataType.LAYER_IMAGE, imageDecoratorList);

		/*
		 * 影像集合图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> imageCollectionDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> imageCollectionList = new ArrayList<TreeNodeDecorator>();
		imageCollectionList.add(new LayerImageCollectionNodeDecorator());
		imageCollectionDecoratorList.add(imageCollectionList);
		decoratorsMap.put(NodeDataType.DATASET_IMAGE_COLLECTION, imageCollectionDecoratorList);

		/*
		 * 格网图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> gridDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> gridList = new ArrayList<TreeNodeDecorator>();
		gridList.add(new LayerGridNodeDecorator());
		gridDecoratorList.add(gridList);
		decoratorsMap.put(NodeDataType.LAYER_GRID, gridDecoratorList);

		/*
		 * 栅格集合图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> gridCollectionDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> gridCollectionList = new ArrayList<TreeNodeDecorator>();
		gridCollectionList.add(new LayerGridCollectionNodeDecorator());
		gridCollectionDecoratorList.add(gridCollectionList);
		decoratorsMap.put(NodeDataType.DATASET_GRID_COLLECTION, gridCollectionDecoratorList);

		/*
		 * 拓扑图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> topologyDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> topologyList = new ArrayList<TreeNodeDecorator>();
		topologyList.add(new LayerNodeDecorator());
		topologyDecoratorList.add(topologyList);
		decoratorsMap.put(NodeDataType.LAYER, topologyDecoratorList);

		/*
		 * 专题图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> themeDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> themeList = new ArrayList<TreeNodeDecorator>();
		themeList.add(new LayerThemeNodeDecorator());
		themeDecoratorList.add(themeList);
		decoratorsMap.put(NodeDataType.LAYER_THEME, themeDecoratorList);

		/*
		 * 可编辑的专题图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> themeEditableDecoratorList = prepareDecoratorListsForLayerThemeEditable();
		ArrayList<TreeNodeDecorator> themeEditableList = new ArrayList<TreeNodeDecorator>();
		themeEditableList.add(new LayerThemeNodeDecorator());
		themeEditableDecoratorList.add(themeEditableList);
		decoratorsMap.put(NodeDataType.THEME_UNIQUE, themeEditableDecoratorList);
		decoratorsMap.put(NodeDataType.THEME_RANGE, themeEditableDecoratorList);
		decoratorsMap.put(NodeDataType.THEME_CUSTOM, themeEditableDecoratorList);

		/*
		 * 单值专题图子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> uniqueItemDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> uniqueItemList = new ArrayList<TreeNodeDecorator>();
		uniqueItemList.add(new ThemeItemGeneralNodeDecorator());
		uniqueItemDecoratorList.add(uniqueItemList);
		decoratorsMap.put(NodeDataType.THEME_UNIQUE_ITEM, uniqueItemDecoratorList);

		/*
		 * 三维单值专题图子项 
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> uniqueItem3DDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> uniqueItem3DList = new ArrayList<TreeNodeDecorator>();
		uniqueItem3DList.add(new Theme3DItemGeneralNodeDecorator());
		uniqueItem3DDecoratorList.add(uniqueItem3DList);
		decoratorsMap.put(NodeDataType.THEME3D_UNIQUE_ITEM, uniqueItem3DDecoratorList);
		
		/*
		 * 分段专题图子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> rangeItemDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> rangeItemList = new ArrayList<TreeNodeDecorator>();
		rangeItemList.add(new ThemeItemGeneralNodeDecorator());
		rangeItemDecoratorList.add(rangeItemList);
		decoratorsMap.put(NodeDataType.THEME_RANGE_ITEM, rangeItemDecoratorList);

		/*
		 * 三维分度专题图子项 
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> rangeItem3DDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> rangeItem3DList = new ArrayList<TreeNodeDecorator>();
		rangeItem3DList.add(new Theme3DItemGeneralNodeDecorator());
		rangeItem3DDecoratorList.add(rangeItem3DList);
		decoratorsMap.put(NodeDataType.THEME3D_RANGE_ITEM, rangeItem3DDecoratorList);
		
		/*
		 * 栅格单值专题图子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> gridUniqueItemDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> gridUniqueItemList = new ArrayList<TreeNodeDecorator>();
		gridUniqueItemList.add(new ThemeItemGeneralNodeDecorator());
		gridUniqueItemDecoratorList.add(gridUniqueItemList);
		decoratorsMap.put(NodeDataType.THEME_GRID_UNIQUE_ITEM, gridUniqueItemDecoratorList);

		/*
		 * 栅格分段专题图子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> gridRangeItemDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> gridRangeItemList = new ArrayList<TreeNodeDecorator>();
		gridRangeItemList.add(new ThemeItemGeneralNodeDecorator());
		gridRangeItemDecoratorList.add(gridRangeItemList);
		decoratorsMap.put(NodeDataType.THEME_GRID_RANGE_ITEM, gridRangeItemDecoratorList);

		/*
		 * 标签专题图子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> labelItemDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> labelItemList = new ArrayList<TreeNodeDecorator>();
		labelItemList.add(new ThemeItemGeneralNodeDecorator());
		labelItemDecoratorList.add(labelItemList);
		decoratorsMap.put(NodeDataType.THEME_LABEL_ITEM, labelItemDecoratorList);

		/*
		 * 统计专题图子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> graphItemDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> graphItemList = new ArrayList<TreeNodeDecorator>();
		graphItemList.add(new ThemeGraphItemNodeDecorator());
		graphItemDecoratorList.add(graphItemList);
		decoratorsMap.put(NodeDataType.THEME_GRAPH_ITEM, graphItemDecoratorList);

		/**
		 * wms
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> wmsLayersDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		wmsLayersDecoratorList.add(firstDecoratorList);

		ArrayList<TreeNodeDecorator> wmsLayersList = new ArrayList<TreeNodeDecorator>();
		wmsLayersList.add(new LayerNodeWMSDecorator());
		wmsLayersDecoratorList.add(wmsLayersList);
		decoratorsMap.put(NodeDataType.LAYER_WMS, wmsLayersDecoratorList);


		/*
		 * WMS数据图层子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> wmsSubLayersDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		// // TODO: 2016/4/21 可见
//		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
//		firstDecoratorList.add(new VisibleDecorator());
//		wmsLayersDecoratorList.add(firstDecoratorList);

		ArrayList<TreeNodeDecorator> subLayersList = new ArrayList<TreeNodeDecorator>();
		subLayersList.add(new WMSSubLayerDecorator());
		wmsSubLayersDecoratorList.add(subLayersList);
		decoratorsMap.put(NodeDataType.WMSSUB_LAYER, wmsSubLayersDecoratorList);


		/*
		 * 分组图层子项
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> layerGroupDecoratorList = prepareDecoratorListsForLayerChangeVisible();
		ArrayList<TreeNodeDecorator> layerGroupList = new ArrayList<TreeNodeDecorator>();
		layerGroupList.add(new LayerGroupNodeDecorator());
		layerGroupDecoratorList.add(layerGroupList);
		decoratorsMap.put(NodeDataType.LAYER_GROUP, layerGroupDecoratorList);

		/*
		 * 未知类型图层
		 */
		ArrayList<ArrayList<TreeNodeDecorator>> nullDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		decoratorsMap.put(NodeDataType.UNKNOWN, nullDecoratorList);
	}

	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForLayer() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

		ArrayList<TreeNodeDecorator> secondDecoratorList = new ArrayList<TreeNodeDecorator>();
		secondDecoratorList.add(new SelectableDecorator());
		arrayList.add(secondDecoratorList);

		ArrayList<TreeNodeDecorator> thirdDecoratorList = new ArrayList<TreeNodeDecorator>();
		thirdDecoratorList.add(new EditableDecorator());
		arrayList.add(thirdDecoratorList);

		ArrayList<TreeNodeDecorator> fourthDecoratorList = new ArrayList<TreeNodeDecorator>();
		fourthDecoratorList.add(new SnapableDecorator());
		arrayList.add(fourthDecoratorList);

		return arrayList;
	}

	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForLayerChangeVisible() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

		return arrayList;
	}

	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForLayerThemeEditable() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

		ArrayList<TreeNodeDecorator> secondDecoratorList = new ArrayList<TreeNodeDecorator>();
		secondDecoratorList.add(new SelectableDecorator());
		arrayList.add(secondDecoratorList);

		ArrayList<TreeNodeDecorator> thirdDecoratorList = new ArrayList<TreeNodeDecorator>();
		thirdDecoratorList.add(new EditableDecorator());
		arrayList.add(thirdDecoratorList);

		ArrayList<TreeNodeDecorator> fourthDecoratorList = new ArrayList<TreeNodeDecorator>();
		fourthDecoratorList.add(new SnapableDecorator());
		arrayList.add(fourthDecoratorList);

		return arrayList;
	}
}
