package com.supermap.desktop.ui.controls;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 三维图层树单元格渲染器
 * 
 * @author xuzw
 *
 */
public class Layer3DsTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	private transient Map<NodeDataType, ArrayList<ArrayList<TreeNodeDecorator>>> decoratorsMap;

	public Layer3DsTreeCellRenderer() {
		initDecoratorsMap();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		TreeNodeData data = (TreeNodeData) node.getUserObject();
		Component component = getPanel(data);
		if (selected) {
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
		decoratorsMap = new HashMap<>();

		ArrayList<ArrayList<TreeNodeDecorator>> vectorDecoratorList = prepareDecoratorListsForLayer3D();
		ArrayList<TreeNodeDecorator> vectorList1 = new ArrayList<>();
		vectorList1.add(new SelectableDecorator());
		vectorDecoratorList.add(vectorList1);

		ArrayList<TreeNodeDecorator> vectorList = new ArrayList<>();
		vectorList.add(new Layer3DDatasetNodeDecorator());
		vectorDecoratorList.add(vectorList);

		decoratorsMap.put(NodeDataType.LAYER3D_DATASET, vectorDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> imageDecoratorList = prepareDecoratorListsForLayer3D();
		ArrayList<TreeNodeDecorator> imageList = new ArrayList<>();
		imageList.add(new Layer3DImageFileNodeDecorator());
		imageDecoratorList.add(imageList);
		decoratorsMap.put(NodeDataType.LAYER3D_IMAGE_FILE, imageDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> kmlDecoratorList = prepareDecoratorListsForLayer3D();
		ArrayList<TreeNodeDecorator> kmlList = new ArrayList<>();
		kmlList.add(new Layer3DKMLNodeDecorator());
		kmlDecoratorList.add(kmlList);
		decoratorsMap.put(NodeDataType.LAYER3D_KML, kmlDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> mapDecoratorList = prepareDecoratorListsForLayer3D();
		ArrayList<TreeNodeDecorator> mapList = new ArrayList<>();
		mapList.add(new Layer3DMapNodeDecorator());
		mapDecoratorList.add(mapList);
		decoratorsMap.put(NodeDataType.LAYER3D_MAP, mapDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> modelDecoratorList = prepareDecoratorListsForLayer3D();
		ArrayList<TreeNodeDecorator> modelList = new ArrayList<TreeNodeDecorator>();
		modelList.add(new Layer3DModelNodeDecorator());
		modelDecoratorList.add(modelList);
		decoratorsMap.put(NodeDataType.LAYER3D_MODEL, modelDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> vectorFileDecoratorList = prepareDecoratorListsForLayer3D();
		ArrayList<TreeNodeDecorator> vectorFileList = new ArrayList<TreeNodeDecorator>();
		vectorFileList.add(new Layer3DVectorFileNodeDecorator());
		vectorFileDecoratorList.add(vectorFileList);
		decoratorsMap.put(NodeDataType.LAYER3D_VECTOR_FILE, vectorFileDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> uniqueItemDecoratorList = prepareDecoratorListsForTheme3DItemGeneral();
		ArrayList<TreeNodeDecorator> uniqueItemList = new ArrayList<TreeNodeDecorator>();
		uniqueItemList.add(new Theme3DItemGeneralNodeDecorator());
		uniqueItemDecoratorList.add(uniqueItemList);
		decoratorsMap.put(NodeDataType.THEME3D_UNIQUE_ITEM, uniqueItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> rangeItemDecoratorList = prepareDecoratorListsForTheme3DItemGeneral();
		ArrayList<TreeNodeDecorator> rangeItemList = new ArrayList<TreeNodeDecorator>();
		rangeItemList.add(new Theme3DItemGeneralNodeDecorator());
		rangeItemDecoratorList.add(rangeItemList);
		decoratorsMap.put(NodeDataType.THEME3D_RANGE_ITEM, rangeItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> feature3DDecoratorList = prepareDecoratorListsForTheme3DItemGeneral();
		ArrayList<TreeNodeDecorator> feature3DList = new ArrayList<TreeNodeDecorator>();
		feature3DList.add(new Theme3DItemGeneralNodeDecorator());
		feature3DDecoratorList.add(feature3DList);
		decoratorsMap.put(NodeDataType.FEATURE3D, feature3DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> feature3DsDecoratorList = prepareDecoratorListsForTheme3DItemGeneral();
		ArrayList<TreeNodeDecorator> feature3DsList = new ArrayList<TreeNodeDecorator>();
		feature3DsList.add(new Theme3DItemGeneralNodeDecorator());
		feature3DsDecoratorList.add(feature3DsList);
		decoratorsMap.put(NodeDataType.FEATURE3DS, feature3DsDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> screenLayer3DDecoratorList = prepareDecoratorListsForTheme3DItemGeneral();
		ArrayList<TreeNodeDecorator> screenLayerList = new ArrayList<TreeNodeDecorator>();
		screenLayerList.add(new ScreenLayer3DNodeDecorator());
		screenLayer3DDecoratorList.add(screenLayerList);
		decoratorsMap.put(NodeDataType.SCREEN_LAYER3D, screenLayer3DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> screenLayer3DGeometryTagDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> tagList = new ArrayList<TreeNodeDecorator>();
		tagList.add(new ScreenLayer3DGeometryNodeDecorator());
		screenLayer3DGeometryTagDecoratorList.add(tagList);
		decoratorsMap.put(NodeDataType.SCREENLAYER3D_GEOMETRY_TAG, screenLayer3DGeometryTagDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> terrainLayerDecoratorList = prepareDecoratorListsForTheme3DItemGeneral();
		ArrayList<TreeNodeDecorator> terrainLayerList = new ArrayList<TreeNodeDecorator>();
		terrainLayerList.add(new TerrainLayerNodeDecorator());
		terrainLayerDecoratorList.add(terrainLayerList);
		decoratorsMap.put(NodeDataType.TERRAIN_LAYER, terrainLayerDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> terrainLayersDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> terrainLayersList = new ArrayList<TreeNodeDecorator>();
		terrainLayersList.add(new TerrainLayersNodeDecorator());
		terrainLayersDecoratorList.add(terrainLayersList);
		decoratorsMap.put(NodeDataType.TERRAIN_LAYERS, terrainLayersDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> layer3DsDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> layer3DsList = new ArrayList<TreeNodeDecorator>();
		layer3DsList.add(new Layer3DsNodeDecorator());
		layer3DsDecoratorList.add(layer3DsList);
		decoratorsMap.put(NodeDataType.LAYER3DS, layer3DsDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> nullDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> vector2DList1 = new ArrayList<TreeNodeDecorator>();
		vector2DList1.add(new LayerUnknowDecorator());
		nullDecoratorList.add(vector2DList1);
		decoratorsMap.put(NodeDataType.UNKNOWN, nullDecoratorList);

		// add by xuzw 2010-07-19 Layer3DMap需要的地图相关的一些装饰器
		ArrayList<ArrayList<TreeNodeDecorator>> vector2DDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> vector2DList = new ArrayList<TreeNodeDecorator>();
		vector2DList.add(new LayerNodeDecorator());
		vector2DDecoratorList.add(vector2DList);
		decoratorsMap.put(NodeDataType.LAYER, vector2DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> image2DDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> image2DList = new ArrayList<TreeNodeDecorator>();
		image2DList.add(new LayerImageNodeDecorator());
		image2DDecoratorList.add(image2DList);
		decoratorsMap.put(NodeDataType.LAYER_IMAGE, image2DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> gridDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> gridList = new ArrayList<TreeNodeDecorator>();
		gridList.add(new LayerGridNodeDecorator());
		gridDecoratorList.add(gridList);
		decoratorsMap.put(NodeDataType.LAYER_GRID, gridDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> topologyDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> topologyList = new ArrayList<TreeNodeDecorator>();
		topologyList.add(new Layer3DDatasetNodeDecorator());
		topologyDecoratorList.add(topologyList);
		decoratorsMap.put(NodeDataType.LAYER, topologyDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> theme2DDecoratorList = prepareDecoratorListsForLayer();
		ArrayList<TreeNodeDecorator> theme2DList = new ArrayList<TreeNodeDecorator>();
		theme2DList.add(new LayerThemeNodeDecorator());
		theme2DDecoratorList.add(theme2DList);
		decoratorsMap.put(NodeDataType.LAYER_THEME, theme2DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> uniqueItem2DDecoratorList = prepareDecoratorListsForThemeItemGeneral();
		ArrayList<TreeNodeDecorator> uniqueItem2DList = new ArrayList<TreeNodeDecorator>();
		uniqueItem2DList.add(new ThemeItemGeneralNodeDecorator());
		uniqueItem2DDecoratorList.add(uniqueItem2DList);
		decoratorsMap.put(NodeDataType.THEME_UNIQUE_ITEM, uniqueItem2DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> rangeItem2DDecoratorList = prepareDecoratorListsForThemeItemGeneral();
		ArrayList<TreeNodeDecorator> rangeItem2DList = new ArrayList<TreeNodeDecorator>();
		rangeItem2DList.add(new ThemeItemGeneralNodeDecorator());
		rangeItem2DDecoratorList.add(rangeItem2DList);
		decoratorsMap.put(NodeDataType.THEME_RANGE_ITEM, rangeItem2DDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> gridUniqueItemDecoratorList = prepareDecoratorListsForThemeItemGeneral();
		ArrayList<TreeNodeDecorator> gridUniqueItemList = new ArrayList<TreeNodeDecorator>();
		gridUniqueItemList.add(new ThemeItemGeneralNodeDecorator());
		gridUniqueItemDecoratorList.add(gridUniqueItemList);
		decoratorsMap.put(NodeDataType.THEME_GRID_UNIQUE_ITEM, gridUniqueItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> gridRangeItemDecoratorList = prepareDecoratorListsForThemeItemGeneral();
		ArrayList<TreeNodeDecorator> gridRangeItemList = new ArrayList<TreeNodeDecorator>();
		gridRangeItemList.add(new ThemeItemGeneralNodeDecorator());
		gridRangeItemDecoratorList.add(gridRangeItemList);
		decoratorsMap.put(NodeDataType.THEME_GRID_RANGE_ITEM, gridRangeItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> labelItemDecoratorList = prepareDecoratorListsForThemeItemGeneral();
		ArrayList<TreeNodeDecorator> labelItemList = new ArrayList<TreeNodeDecorator>();
		labelItemList.add(new ThemeItemGeneralNodeDecorator());
		labelItemDecoratorList.add(labelItemList);
		decoratorsMap.put(NodeDataType.THEME_LABEL_ITEM, labelItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> graphItemDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> graphItemList = new ArrayList<TreeNodeDecorator>();
		graphItemList.add(new ThemeGraphItemNodeDecorator());
		graphItemDecoratorList.add(graphItemList);
		decoratorsMap.put(NodeDataType.THEME_GRAPH_ITEM, graphItemDecoratorList);

		ArrayList<ArrayList<TreeNodeDecorator>> wmsSubLayersDecoratorList = new ArrayList<ArrayList<TreeNodeDecorator>>();
		ArrayList<TreeNodeDecorator> subLayersList = new ArrayList<TreeNodeDecorator>();
		subLayersList.add(new WMSSubLayerDecorator());
		wmsSubLayersDecoratorList.add(subLayersList);
		decoratorsMap.put(NodeDataType.WMSSUB_LAYER, wmsSubLayersDecoratorList);
	}

	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForLayer3D() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

//		ArrayList<TreeNodeDecorator> secondDecoratorList = new ArrayList<TreeNodeDecorator>();
//		secondDecoratorList.add(new SelectableDecorator());
//		arrayList.add(secondDecoratorList);

//		ArrayList<TreeNodeDecorator> fourthDecoratorList = new ArrayList<TreeNodeDecorator>();
//		fourthDecoratorList.add(new AlwaysRenderDecorator());
//		arrayList.add(fourthDecoratorList);

		return arrayList;
	}

	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForTheme3DItemGeneral() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

		return arrayList;
	}

	/**
	 * 对于Layer3Ds中显示的Layer3DMap树节点，Map中的Layer应该只有可见设置和始终渲染设置
	 * 
	 * @return
	 */
	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForLayer() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

		return arrayList;
	}

	private ArrayList<ArrayList<TreeNodeDecorator>> prepareDecoratorListsForThemeItemGeneral() {
		ArrayList<ArrayList<TreeNodeDecorator>> arrayList = new ArrayList<ArrayList<TreeNodeDecorator>>();

		ArrayList<TreeNodeDecorator> firstDecoratorList = new ArrayList<TreeNodeDecorator>();
		firstDecoratorList.add(new VisibleDecorator());
		arrayList.add(firstDecoratorList);

		return arrayList;
	}

}