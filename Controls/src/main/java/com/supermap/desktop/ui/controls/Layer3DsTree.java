package com.supermap.desktop.ui.controls;

import com.supermap.data.DatasetType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.JTreeUIUtilities;
import com.supermap.desktop.controls.utilities.SortUIUtilities;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Layers;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeGridRangeItem;
import com.supermap.mapping.ThemeGridUniqueItem;
import com.supermap.mapping.ThemeLabelItem;
import com.supermap.mapping.ThemeRangeItem;
import com.supermap.mapping.ThemeUniqueItem;
import com.supermap.realspace.Feature3D;
import com.supermap.realspace.Feature3Ds;
import com.supermap.realspace.Layer3D;
import com.supermap.realspace.Layer3DAddedEvent;
import com.supermap.realspace.Layer3DAddedListener;
import com.supermap.realspace.Layer3DDataset;
import com.supermap.realspace.Layer3DImageFile;
import com.supermap.realspace.Layer3DKML;
import com.supermap.realspace.Layer3DMap;
import com.supermap.realspace.Layer3DModel;
import com.supermap.realspace.Layer3DRemovedEvent;
import com.supermap.realspace.Layer3DRemovedListener;
import com.supermap.realspace.Layer3DType;
import com.supermap.realspace.Layer3DVectorFile;
import com.supermap.realspace.Layer3Ds;
import com.supermap.realspace.Scene;
import com.supermap.realspace.ScreenLayer3D;
import com.supermap.realspace.TerrainLayer;
import com.supermap.realspace.TerrainLayers;
import com.supermap.realspace.Theme3DRange;
import com.supermap.realspace.Theme3DRangeItem;
import com.supermap.realspace.Theme3DType;
import com.supermap.realspace.Theme3DUnique;
import com.supermap.realspace.Theme3DUniqueItem;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
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
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Beans;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;

/**
 * 三维图层管理树控件
 *
 * @author xuzw
 */
public class Layer3DsTree extends JTree {

    private static final long serialVersionUID = 1L;

    private transient Scene nowScene = null;

    private boolean isScreenLayer3DNodeVisible = true;

    private boolean issLayer3DsNodeVisible = true;

    private boolean isTerrainLayersVisible = true;

    private DefaultTreeModel defaultTreeModel = null;

    private DefaultMutableTreeNode rootNode = null;

    private DefaultMutableTreeNode treeNodeScreenLayer = null;

    private DefaultMutableTreeNode treeNodeLayer3Ds = null;

    private DefaultMutableTreeNode treeNodeTerrainLayers = null;

    private transient Layer3DsTreeCellRenderer treeRenderer = null;

    private transient Layer3DsTreeCellEditor treeCellEditor = null;

    private transient Layer3DAddedListener layer3DAddListener = null;

    private transient Layer3DRemovedListener layer3DRemovedListener = null;

    private transient KeyListener keyListenerTemp = null;

    private transient MouseListener mouseListenerTemp = null;

    private transient ScreenLayer3D screenLayer;

    private transient Layer3Ds layers;

    private transient TerrainLayers terrainLayers;

    private transient Enumeration<TreePath> screenLayer3DTreePath = null;

    private transient Enumeration<TreePath> layersTreePath = null;

    private transient Enumeration<TreePath> terrainLayersTreePath = null;
    private DropTarget dropTargetTemp;
    private DragSource dragSource;
    private int draggedNodeIndex = -1;
    private int dropTargetNodeIndex = -1;

    private static DataFlavor localObjectFlavor;
    private static DataFlavor[] supportedFlavors = {localObjectFlavor};

    private DefaultMutableTreeNode dropTargetNode = null;
    private DefaultMutableTreeNode draggedNode = null;
    private DefaultTreeModel treeModeltemp;
    private boolean isUp = false;

    /**
     * 默认构造函数
     */
    public Layer3DsTree() {
        super();
        setDefault();
        TreeNodeData userObjectNULL = new TreeNodeData("", NodeDataType.UNKNOWN);
        rootNode = new DefaultMutableTreeNode(userObjectNULL);
        defaultTreeModel = new DefaultTreeModel(rootNode);
        setModel(defaultTreeModel);
        initDrag();
    }

    private void initDrag() {
        this.setDragEnabled(true);
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new Layer3DsTree.LayersTreeDragGestureListener());
        dropTargetTemp = new DropTarget(this, new Layer3DsTreeDropTargetAdapter());
    }

    /**
     * 构造函数，根据场景构建三维场景管理器控件
     */
    public Layer3DsTree(Scene scene) {
        super();
        setDefault();
        if (scene == null) {
            TreeNodeData userObjectNULL = new TreeNodeData("", NodeDataType.UNKNOWN);
            rootNode = new DefaultMutableTreeNode(userObjectNULL);
            defaultTreeModel = new DefaultTreeModel(rootNode);
            setModel(defaultTreeModel);
        } else {
            this.setScene(scene);
        }
        initDrag();
    }

    private void setDefault() {
        this.setEditable(true);
        this.setRootVisible(false);
        this.setShowsRootHandles(true);
    }

    /**
     * 设置/获取三维图例管理器的场景
     */
    public Scene getScene() {
        return this.nowScene;
    }

    @Override
    public void paint(Graphics g) {
        if (!Beans.isDesignTime()) {
            super.paint(g);
        } else {
            g.drawString("Layer3DsTree", this.getWidth() / 2 - 30, this.getHeight() / 2);
        }
    }

    public void setScene(Scene scene) {
        try {
            if (nowScene != null && !nowScene.equals(scene)) {
                this.unRegisterLayer3DsEventListener();
            }

            if (nowScene == null || !nowScene.equals(scene)) {
                nowScene = scene;
                if (nowScene == null) {
                    setDefault();
                    TreeNodeData userObjectNULL = new TreeNodeData("", NodeDataType.UNKNOWN);
                    rootNode = new DefaultMutableTreeNode(userObjectNULL);
                    defaultTreeModel = new DefaultTreeModel(rootNode);
                    setModel(defaultTreeModel);
                } else {
                    treeRenderer = new Layer3DsTreeCellRenderer();

                    treeCellEditor = new Layer3DsTreeCellEditor(this, treeRenderer);
//                    getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    this.registerLayer3DsEventListener();
                    buildSceneNode(nowScene);
                    defaultTreeModel = new DefaultTreeModel(rootNode);
                    setModel(defaultTreeModel);
                    setCellRenderer(treeRenderer);
                    this.setCellEditor(treeCellEditor);
                }
            }

        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
    }

    /**
     * 当工作空间改变时，重新构建所有的节点刷新树控件
     */
    public void reload() {
        if (nowScene != null) {
            try {
                buildSceneNode(nowScene);
                defaultTreeModel = new DefaultTreeModel(rootNode);
                setModel(defaultTreeModel);
                updateUI();
            } catch (Exception e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        }
    }

    public void setScreenLayer3DNodeVisible(boolean isScreenLayer3DNodeVisible) {
        if (this.isScreenLayer3DNodeVisible != isScreenLayer3DNodeVisible && nowScene != null) {
            this.isScreenLayer3DNodeVisible = isScreenLayer3DNodeVisible;
            if (!this.isScreenLayer3DNodeVisible) {
                screenLayer3DTreePath = this.getExpandedDescendants(new TreePath(treeNodeScreenLayer.getPath()));
                rootNode.remove(treeNodeScreenLayer);
            } else {
                rootNode.insert(treeNodeScreenLayer, 0);
                for (; screenLayer3DTreePath != null && screenLayer3DTreePath.hasMoreElements(); ) {
                    this.setExpandedState(screenLayer3DTreePath.nextElement(), true);
                }
            }
        }
        this.isScreenLayer3DNodeVisible = isScreenLayer3DNodeVisible;
        updateUI();
    }

    public boolean isScreenLayer3DNodeVisible() {
        return isScreenLayer3DNodeVisible;
    }

    public void setLayer3DsNodeVisible(boolean isLayer3DsNodeVisible) {
        if (this.issLayer3DsNodeVisible != isLayer3DsNodeVisible && nowScene != null) {
            this.issLayer3DsNodeVisible = isLayer3DsNodeVisible;
            if (!this.issLayer3DsNodeVisible) {
                layersTreePath = this.getExpandedDescendants(new TreePath(treeNodeLayer3Ds.getPath()));
                rootNode.remove(treeNodeLayer3Ds);
            } else {
                if (isScreenLayer3DNodeVisible) {

                    rootNode.insert(treeNodeLayer3Ds, 1);
                } else {
                    rootNode.insert(treeNodeLayer3Ds, 0);
                }
                for (; layersTreePath != null && layersTreePath.hasMoreElements(); ) {
                    this.setExpandedState(layersTreePath.nextElement(), true);
                }
            }

        }
        this.issLayer3DsNodeVisible = isLayer3DsNodeVisible;
        updateUI();
    }

    public boolean isLayer3DsNodeVisible() {
        return issLayer3DsNodeVisible;
    }

    public void setTerrainLayersVisible(boolean isTerrainLayersVisible) {
        if (this.isTerrainLayersVisible != isTerrainLayersVisible && nowScene != null) {
            this.isTerrainLayersVisible = isTerrainLayersVisible;
            if (!this.isTerrainLayersVisible) {
                terrainLayersTreePath = this.getExpandedDescendants(new TreePath(treeNodeTerrainLayers.getPath()));
                rootNode.remove(treeNodeTerrainLayers);
            } else {
                rootNode.add(treeNodeTerrainLayers);
                for (; terrainLayersTreePath != null && terrainLayersTreePath.hasMoreElements(); ) {
                    this.setExpandedState(terrainLayersTreePath.nextElement(), true);
                }
            }

        }
        this.isTerrainLayersVisible = isTerrainLayersVisible;
        updateUI();
    }

    public boolean isTerrainLayersVisible() {
        return isTerrainLayersVisible;
    }

    private void buildSceneNode(Scene scene) {
        screenLayer = scene.getScreenLayer();
        layers = scene.getLayers();
        terrainLayers = scene.getTerrainLayers();
        TreeNodeData userObjectScene = new TreeNodeData(scene.getName(), NodeDataType.UNKNOWN);
        rootNode = new DefaultMutableTreeNode(userObjectScene);
        // 添加ScreenLayer节点
        TreeNodeData userObjectScreenLayer = new TreeNodeData(screenLayer, NodeDataType.SCREEN_LAYER3D);
        treeNodeScreenLayer = new DefaultMutableTreeNode(userObjectScreenLayer);
        addScreenLayer(treeNodeScreenLayer);
        if (this.isScreenLayer3DNodeVisible()) {
            rootNode.add(treeNodeScreenLayer);
        }

        // 添加Layer3Ds节点
        TreeNodeData userObjectLayers = new TreeNodeData(layers, NodeDataType.LAYER3DS);
        treeNodeLayer3Ds = new DefaultMutableTreeNode(userObjectLayers);
        addLayer3Ds(treeNodeLayer3Ds);
        if (this.isLayer3DsNodeVisible()) {
            rootNode.add(treeNodeLayer3Ds);
        }

        // 添加TerrainLayers节点
        TreeNodeData userObjectTerrainLayers = new TreeNodeData(terrainLayers, NodeDataType.TERRAIN_LAYERS);
        treeNodeTerrainLayers = new DefaultMutableTreeNode(userObjectTerrainLayers);
        addTerrainLayers(treeNodeTerrainLayers);
        if (this.isTerrainLayersVisible()) {
            rootNode.add(treeNodeTerrainLayers);
        }

    }

    @Override
    public boolean isPathEditable(TreePath path) {
        Object lastPathComponent = path.getLastPathComponent();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
        TreeNodeData nodeData = (TreeNodeData) node.getUserObject();
        Object data = nodeData.getData();
        if (data instanceof Layer3D || data instanceof TerrainLayer || data instanceof Feature3D || data instanceof Feature3Ds) {
            return true;
        }
        return false;
    }

    // 返回位于(x,y)位置的HitTestInfo。当没有内容时，返回NULL
    HitTestInfo hitTest(int x, int y) {
        TreePath path = this.getPathForLocation(x, y);
        if (path != null) {
            Object object = path.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
            TreeNodeData data = (TreeNodeData) node.getUserObject();
            JPanel panel = ((Layer3DsTreeCellRenderer) getCellRenderer()).getPanel(data);

            int refX = x - this.getUI().getPathBounds(this, path).x;
            for (int i = 0; i < panel.getComponentCount(); i++) {
                FlowLayout flowLayout = (FlowLayout) panel.getLayout();
                refX = refX - TreeNodeDecorator.IMAGEICON_WIDTH - flowLayout.getHgap();
                if (refX < 0) {
                    return new HitTestInfo(node, i);
                }
            }
        }
        return null;
    }

    private void addScreenLayer(DefaultMutableTreeNode treeNode) {
        for (int i = 0; i < screenLayer.getCount(); i++) {
            TreeNodeData userObjecScreenLayer = new TreeNodeData(screenLayer.getTag(i), NodeDataType.SCREENLAYER3D_GEOMETRY_TAG);
            DefaultMutableTreeNode treeChildNodeScreenLayer = new DefaultMutableTreeNode(userObjecScreenLayer);
            treeNode.add(treeChildNodeScreenLayer);
        }
    }

    private void addLayer3Ds(DefaultMutableTreeNode treeNode) {
        for (int i = 0; i < layers.getCount(); i++) {
            Layer3D layer3D = layers.get(i);
            TreeNodeData layer3DNodeData;
            if (layer3D instanceof Layer3DDataset) {
                Layer3DDataset layer3DDataset = (Layer3DDataset) layer3D;
                if (((Layer3DDataset) layer3D).getDataset().getType() == DatasetType.IMAGE) {
                    layer3DNodeData = new TreeNodeData(layer3DDataset, NodeDataType.LAYER_IMAGE);
                } else if (((Layer3DDataset) layer3D).getDataset().getType() == DatasetType.GRID) {
                    layer3DNodeData = new TreeNodeData(layer3DDataset, NodeDataType.LAYER_GRID);
                } else {
                    layer3DNodeData = new TreeNodeData(layer3DDataset, NodeDataType.LAYER3D_DATASET);
                }
            } else if (layer3D instanceof Layer3DImageFile) {
                layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_IMAGE_FILE);
            } else if (layer3D instanceof Layer3DKML) {
                layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_KML);
            } else if (layer3D instanceof Layer3DModel) {
                layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_MODEL);
            } else if (layer3D instanceof Layer3DMap) {
                layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_MAP);
            } else if (layer3D instanceof Layer3DVectorFile) {
                layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_VECTOR_FILE);
            } else {
                layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.UNKNOWN);
            }

            DefaultMutableTreeNode layer3DNode = new DefaultMutableTreeNode(layer3DNodeData);
            treeNode.add(layer3DNode);
            if (layer3D.getType().equals(Layer3DType.KML)) {
                addFeature3DsContent(layer3DNode, layer3D.getFeatures());
            } else if (layers.get(i).getType().equals(Layer3DType.DATASET)) {
                if (((Layer3DDataset) layer3D).getDataset() != null) {
                    Layer3DDataset layer = (Layer3DDataset) layers.get(i);
                    if (layer.getTheme() != null) {
                        if (layer.getTheme().getType().equals(Theme3DType.RANGE3D)) {
                            addTheme3DRangeContent(layer3DNode, layer);
                        } else if (layer.getTheme().getType().equals(Theme3DType.UNIQUE3D)) {
                            addTheme3DUniqueContent(layer3DNode, layer);
                        }
                    }
                }
            } else if (layer3D.getType().equals(Layer3DType.MAP)) {
                Layer3DMap layer3DMap = (Layer3DMap) layer3D;
                addLayer3DMapContent(layer3DNode, layer3DMap.getMap());
            }
        }
    }

    /**
     * 将Layer3DMap以节点的形式加到树中
     *
     * @param treeNode
     * @param map
     */
    private void addLayer3DMapContent(DefaultMutableTreeNode treeNode, Map map) {
        LayersTree layersTree = new LayersTree(map);
        Layers layersTemp = map.getLayers();
        for (int i = 0; i < layersTemp.getCount(); i++) {
            DefaultMutableTreeNode defaultMutableTreeNode = layersTree.getNodeByLayer(layersTemp.get(i));
            treeNode.add(defaultMutableTreeNode);
        }
    }

    private void addTheme3DRangeContent(DefaultMutableTreeNode treeNode, Layer3DDataset layer) {

        Theme3DRange range = (Theme3DRange) layer.getTheme();
        for (int itemIndex = 0; itemIndex < range.getCount(); itemIndex++) {
            Theme3DRangeItem rangeItem = range.get(itemIndex);
            TreeNodeData userObjecScreenLayer = new TreeNodeData(rangeItem, NodeDataType.THEME3D_RANGE_ITEM, layer);
            DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(userObjecScreenLayer);
            treeNode.add(itemNode);
        }
    }

    private void addTheme3DUniqueContent(DefaultMutableTreeNode treeNode, Layer3DDataset layer) {
        Theme3DUnique unique = (Theme3DUnique) layer.getTheme();
        for (int itemIndex = 0; itemIndex < unique.getCount(); itemIndex++) {
            Theme3DUniqueItem uniqueItem = unique.get(itemIndex);
            TreeNodeData userObjecScreenLayer = new TreeNodeData(uniqueItem, NodeDataType.THEME3D_UNIQUE_ITEM, layer);
            DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(userObjecScreenLayer);
            treeNode.add(itemNode);
        }
    }

    private void addFeature3DsContent(DefaultMutableTreeNode treeNode, Feature3Ds feature3Ds) {
        TreeNodeData userObjecScreenLayer = new TreeNodeData(feature3Ds, NodeDataType.FEATURE3DS);
        DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(userObjecScreenLayer);
        treeNode.add(itemNode);
        for (int i = 0; i < feature3Ds.getCount(); i++) {
            if (feature3Ds.get(i) instanceof Feature3Ds) {
                addFeature3DsContent(itemNode, (Feature3Ds) (feature3Ds.get(i)));
            } else {
                TreeNodeData userObjecFeature3D = new TreeNodeData((Feature3D) feature3Ds.get(i), NodeDataType.FEATURE3D);
                DefaultMutableTreeNode itemNode2 = new DefaultMutableTreeNode(userObjecFeature3D);
                itemNode.add(itemNode2);
            }
        }
    }

    private void addTerrainLayers(DefaultMutableTreeNode treeNode) {
        for (int i = 0; i < terrainLayers.getCount(); i++) {

            TreeNodeData userObjecFeature3D = new TreeNodeData(terrainLayers.get(i), NodeDataType.TERRAIN_LAYER);
            DefaultMutableTreeNode treeChildNodeScreenLayer = new DefaultMutableTreeNode(userObjecFeature3D);
            treeNode.add(treeChildNodeScreenLayer);
        }
    }

    private void unRegisterLayer3DsEventListener() {
        if (layer3DAddListener != null) {
            nowScene.getLayers().removeLayer3DAddedListener(layer3DAddListener);
        }
        if (layer3DRemovedListener != null) {
            nowScene.getLayers().removeLayer3DRemovedListener(layer3DRemovedListener);
        }
        if (keyListenerTemp != null) {
            this.removeKeyListener(keyListenerTemp);
        }
        if (mouseListenerTemp != null) {
            this.removeMouseListener(mouseListenerTemp);
        }
    }

    private void registerLayer3DsEventListener() {
        if (layer3DAddListener == null) {
            layer3DAddListener = new Layer3DAddedListener() {
                @Override
                public void layer3DAdded(Layer3DAddedEvent event) {
                    setAbstractLayer3DAdded(event);

                }
            };
        }

        nowScene.getLayers().addLayer3DAddedListener(layer3DAddListener);
        if (layer3DRemovedListener == null) {
            layer3DRemovedListener = new Layer3DRemovedListener() {
                @Override
                public void layer3DRemoved(Layer3DRemovedEvent event) {
                    DefaultTreeModel model = (DefaultTreeModel) getModel();
                    Object obj = model.getChild(treeNodeLayer3Ds, event.getIndex());
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                    model.removeNodeFromParent(node);
                }

            };
        }
        nowScene.getLayers().addLayer3DRemovedListener(layer3DRemovedListener);
        if (keyListenerTemp == null) {
            keyListenerTemp = new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    int keyCode = e.getKeyChar();
                    switch (keyCode) {
                        case KeyEvent.VK_DELETE:
                            removeLayer3DsControlNode();
                            break;
                        default:
                            break;
                    }
                }
            };
        }
        this.addKeyListener(keyListenerTemp);
        if (mouseListenerTemp == null) {
            mouseListenerTemp = new TreeMouseListener();
        }
        this.addMouseListener(mouseListenerTemp);

    }

    private void setAbstractLayer3DAdded(Layer3DAddedEvent event) {
        Layer3D layer3D = event.getLayer3D();
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        TreeNodeData layer3DNodeData;
        if (layer3D instanceof Layer3DDataset) {
            Layer3DDataset layer3DDataset = (Layer3DDataset) layer3D;
            if (((Layer3DDataset) layer3D).getDataset().getType() == DatasetType.IMAGE) {
                layer3DNodeData = new TreeNodeData(layer3DDataset, NodeDataType.LAYER_IMAGE);
            } else if (((Layer3DDataset) layer3D).getDataset().getType() == DatasetType.GRID) {
                layer3DNodeData = new TreeNodeData(layer3DDataset, NodeDataType.LAYER_GRID);
            } else {
                layer3DNodeData = new TreeNodeData(layer3DDataset, NodeDataType.LAYER3D_DATASET);
            }
        } else if (layer3D instanceof Layer3DImageFile) {
            layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_IMAGE_FILE);
        } else if (layer3D instanceof Layer3DKML) {
            layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_KML);
        } else if (layer3D instanceof Layer3DModel) {
            layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_MODEL);
        } else if (layer3D instanceof Layer3DMap) {
            layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_MAP);
        } else if (layer3D instanceof Layer3DVectorFile) {
            layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.LAYER3D_VECTOR_FILE);
        } else {
            layer3DNodeData = new TreeNodeData(layer3D, NodeDataType.UNKNOWN);
        }
        // 将节点插入到与图层索引一致处
        DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(layer3DNodeData);
        model.insertNodeInto(itemNode, treeNodeLayer3Ds, nowScene.getLayers().indexOf(layer3D.getName()));
        if (layer3D.getType().equals(Layer3DType.KML)) {
            addFeature3DsContent(itemNode, layer3D.getFeatures());
        } else if (layer3D.getType().equals(Layer3DType.DATASET)) {
            Layer3DDataset layerDataset = (Layer3DDataset) layer3D;
            if (layerDataset.getTheme() != null) {
                if (layerDataset.getTheme().getType().equals(Theme3DType.RANGE3D)) {
                    addTheme3DRangeContent(itemNode, layerDataset);
                } else if (layerDataset.getTheme().getType().equals(Theme3DType.UNIQUE3D)) {
                    addTheme3DUniqueContent(itemNode, layerDataset);
                }
            }
        } else if (layer3D.getType().equals(Layer3DType.MAP)) {
            Layer3DMap layer3DMap = (Layer3DMap) layer3D;
            addLayer3DMapContent(itemNode, layer3DMap.getMap());
        }
        JTreeUIUtilities.locateNode(Layer3DsTree.this, itemNode);
    }


    private void removeLayer3DsControlNode() {
        TreePath[] paths = this.getSelectionPaths();
        if (paths.length <= 0) {
            return;
        }
        String message = null;
        if (paths.length == 1) {
            message = MessageFormat.format(ControlsProperties.getString("String_RemoveLayerInfo"),
                    ((Layer3D) ((TreeNodeData) ((DefaultMutableTreeNode) paths[0].getLastPathComponent()).getUserObject()).getData()).getName());
        } else {
            message = MessageFormat.format(ControlsProperties.getString("String_RemoveLayersInfo"), paths.length);
        }
        if (JOptionPane.OK_OPTION != UICommonToolkit.showConfirmDialog(message)) {
            return;
        }
        for (int i = 0; i < paths.length; i++) {
            TreePath path = paths[i];
            Object lastPathComponent = path.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
            TreeNodeData nodeData = (TreeNodeData) node.getUserObject();
            Object obj = nodeData.getData();

            if (obj instanceof Layer3D) {
                Layer3D layer = (Layer3D) obj;
                nowScene.getLayers().remove(layer.getName());
            }
            if (obj instanceof TerrainLayer) {
                TerrainLayer layer = (TerrainLayer) obj;
                int index = nowScene.getTerrainLayers().indexOf(layer.getName());
                nowScene.getTerrainLayers().remove(layer.getName());
                DefaultTreeModel model = (DefaultTreeModel) getModel();
                Object temp = model.getChild(treeNodeTerrainLayers, index);
                DefaultMutableTreeNode childnode = (DefaultMutableTreeNode) temp;
                model.removeNodeFromParent(childnode);
            }
        }
        nowScene.refresh();
    }

    private class TreeMouseListener extends MouseAdapter {
        @SuppressWarnings("deprecation")
        @Override
        public void mouseClicked(MouseEvent e) {
            TreePath path = Layer3DsTree.this.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                HitTestInfo hitTestInfo = hitTest(e.getX(), e.getY());
                if (hitTestInfo != null) {
                    TreeNodeData nodeData = hitTestInfo.getData();
                    Object obj = nodeData.getData();
                    int index = hitTestInfo.getIndex();
                    if (index == 0) {
                        if (obj instanceof Layer3D) {
                            Layer3D layer = (Layer3D) obj;
                            layer.setVisible(!layer.isVisible());
                        } else if (obj instanceof Theme3DUniqueItem) {
                            Theme3DUniqueItem item = (Theme3DUniqueItem) obj;
                            item.setVisible(!item.isVisible());
                        } else if (obj instanceof Theme3DRangeItem) {
                            Theme3DRangeItem item = (Theme3DRangeItem) obj;
                            item.setVisible(!item.isVisible());
                        } else if (obj instanceof ScreenLayer3D) {
                            ScreenLayer3D screenLayer3D = (ScreenLayer3D) obj;
                            screenLayer3D.setVisible(!screenLayer3D.isVisible());
                        } else if (obj instanceof TerrainLayer) {
                            TerrainLayer terrainLayer = (TerrainLayer) obj;
                            terrainLayer.setVisible(!terrainLayer.isVisible());
                        } else if (obj instanceof Feature3D) {
                            Feature3D feature3D = (Feature3D) obj;
                            feature3D.setVisible(!feature3D.isVisible());
                        } else if (obj instanceof Feature3Ds) {
                            Feature3Ds feature3Ds = (Feature3Ds) obj;
                            feature3Ds.setVisible(!feature3Ds.isVisible());
                        }
                        // 以下是Layer3DMap会用到的
                        else if (obj instanceof Layer) {
                            Layer layer = (Layer) obj;
                            layer.setVisible(!layer.isVisible());
                        } else if (obj instanceof ThemeUniqueItem) {
                            ThemeUniqueItem item = (ThemeUniqueItem) obj;
                            item.setVisible(!item.isVisible());
                        } else if (obj instanceof ThemeRangeItem) {
                            ThemeRangeItem item = (ThemeRangeItem) obj;
                            item.setVisible(!item.isVisible());
                        } else if (obj instanceof ThemeGridUniqueItem) {
                            ThemeGridUniqueItem item = (ThemeGridUniqueItem) obj;
                            item.setVisible(!item.isVisible());
                        } else if (obj instanceof ThemeGridRangeItem) {
                            ThemeGridRangeItem item = (ThemeGridRangeItem) obj;
                            item.setVisible(!item.isVisible());
                        } else if (obj instanceof ThemeLabelItem) {
                            ThemeLabelItem item = (ThemeLabelItem) obj;
                            item.setVisible(!item.isVisible());
                        }
                    }
                    if (index == 1 && obj instanceof Layer3D) {
                        Layer3D layer = (Layer3D) obj;
                        layer.setSelectable(!layer.isSelectable());
                    }
                    if (index == 2 && obj instanceof Layer3D) {
                        Layer3D layer = (Layer3D) obj;
                        layer.setAlwaysRender(!layer.isAlwaysRender());
                    }
                    updateUI();
                    nowScene.refresh();
                }
            }
        }
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
                draggedNodeIndex = treeNodeLayer3Ds.getIndex(draggedNode);
                dropTargetNodeIndex = -1;

                // 判断拖拽的两个节点的父节点是否是根节点
//                if (getRowForPath(getSelectionPath()) == -1 || !draggedNode.getParent().equals(getModel().getRoot()))
//                    return;
                Transferable trans = new Layer3DsTree.RJLTransferable(draggedNode);
                dge.startDrag(DragSource.DefaultMoveDrop, trans, new Layer3DsTree.MyDragSourceAdapter());
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
    private class Layer3DsTreeDropTargetAdapter extends DropTargetAdapter {
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
            JTree tree = Layer3DsTree.this;
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
            if (treePath == null) {
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
            dropTargetNodeIndex = treeNodeLayer3Ds.getIndex(dropTargetNode);

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

            JPanel panel = (JPanel) getCellRenderer().getTreeCellRendererComponent(Layer3DsTree.this, oldNode, false, isExpanded(treePath),
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
        int[] selectionRows = getSelectionRows();
        Integer[] integers = new Integer[selectionRows.length];
	    TreeNode parent = draggedNode.getParent();
	    int index = parent.getParent().getIndex(parent);
	    for (int i = 0; i < selectionRows.length; i++) {
		    integers[i] = selectionRows[i] - index - 1;
	    }
        SortUIUtilities.sortList(integers);
        int count = 0; // 记录有多少图层移动到本图层之前
        int lowerCount = 0;// 记录有多少图层本来在目标图层之前
        try {
	        for (int i = selectionRows.length - 1; i >= 0; i--) {
		        int currentIndex = integers[i];
		        int tempTargetRow = dropTargetNodeIndex;
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
//		        int beforeLayerIndex = draggedNode.getParent().getIndex(draggedNode);
//		        int resultIndex = lastPathComponent.getParent().getIndex(lastPathComponent);
		        ((DefaultTreeModel) getModel()).removeNodeFromParent(draggedNode);
		        ((DefaultTreeModel) getModel()).insertNodeInto(draggedNode, (MutableTreeNode) parent, tempTargetRow);
		        nowScene.getLayers().moveTo(currentIndex, tempTargetRow);
            }
            if (isUp) {
                lowerCount++;
            }
	        setSelectionInterval(dropTargetNodeIndex - lowerCount + 2 + index, dropTargetNodeIndex + selectionRows.length - lowerCount + index + 1);
	        nowScene.refresh();
            dropTargetNodeIndex = -1;
        } catch (Exception e) {
	        Application.getActiveApplication().getOutput().output(e);
	        //由于计算问题暂时先这样处理 nowScene.getLayers().moveTo(currentIndex, tempTargetRow);
        }
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
            Layer3DsTree.this.repaint();
        }
    }
}
