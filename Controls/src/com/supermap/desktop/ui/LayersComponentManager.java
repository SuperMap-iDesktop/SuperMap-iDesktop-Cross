package com.supermap.desktop.ui;

import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.ui.controls.Layer3DsTree;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerGroup;
import com.supermap.mapping.Map;
import com.supermap.mapping.ThemeCustom;
import com.supermap.mapping.ThemeGridRange;
import com.supermap.mapping.ThemeGridUnique;
import com.supermap.mapping.ThemeRange;
import com.supermap.mapping.ThemeUnique;
import com.supermap.realspace.Scene;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;

public class LayersComponentManager extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JScrollPane jScrollPane = null;
	private transient LayersTree layersTree = null;
	private transient Layer3DsTree layer3DsTree = null;
	// 临时的变量，现在还没有自动加载Dockbar，所以暂时用这个变量测试
	private Boolean isContextMenuBuilded = false;

	/**
	 * Create the panel.
	 */
	public LayersComponentManager() {

		initializeComponent();
		initializeResources();
		initialize();
	}

	private void initializeComponent() {

		this.jScrollPane = new javax.swing.JScrollPane();
		this.layersTree = new LayersTree();
		this.layer3DsTree = new Layer3DsTree();

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane,
				javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane,
				javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));

		this.layersTree.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				layersTreeMousePressed(evt);
			}
		});

		this.layer3DsTree.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				layer3DsTreeMousePressed(evt);
			}
		});
	}

	private void initialize() {
		try {
			initializeToolBar();
			buildContextMenu();

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void initializeToolBar() {
		// 默认实现，后续进行初始化操作
	}

	private void initializeResources() {
		// 默认实现，后续进行初始化操作
	}

	private void layersTreeMousePressed(java.awt.event.MouseEvent evt) {
		try {
			int buttonType = evt.getButton();
			int clickCount = evt.getClickCount();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.layersTree.getLastSelectedPathComponent();
			if (selectedNode != null) {
				TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
				if (buttonType == MouseEvent.BUTTON3 && clickCount == 1 && this.layersTree.getLastSelectedPathComponent() != null) {

					if (!this.isContextMenuBuilded) {
						this.buildContextMenu();
					}

					JPopupMenu popupMenu = this.getPoputMenu(selectedNodeData);
					if (popupMenu != null) {
						popupMenu.show(this.layersTree, evt.getX(), evt.getY());
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void layer3DsTreeMousePressed(java.awt.event.MouseEvent evt) {
		try {
			int buttonType = evt.getButton();
			int clickCount = evt.getClickCount();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.layer3DsTree.getLastSelectedPathComponent();
			if (selectedNode != null) {
				TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
				if (buttonType == MouseEvent.BUTTON3 && clickCount == 1 && this.layer3DsTree.getLastSelectedPathComponent() != null) {

					if (!this.isContextMenuBuilded) {
						this.buildContextMenu();
					}

					JPopupMenu popupMenu = this.getPoputMenu(selectedNodeData);
					if (popupMenu != null) {
						popupMenu.show(this.layer3DsTree, evt.getX(), evt.getY());
					}
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private JPopupMenu getPoputMenu(TreeNodeData nodeData) {
		JPopupMenu popupMenu = null;
		try {
			NodeDataType type = nodeData.getType();

			if (type == NodeDataType.LAYER) {
				popupMenu = this.layerPopupMenu;
			} else if (type == NodeDataType.FEATURE3D) {
				popupMenu = this.feature3DPopupMenu;
			} else if (type == NodeDataType.FEATURE3DS) {
				popupMenu = this.feature3DsPopupMenu;
			} else if (type == NodeDataType.LAYER3DS) {
				popupMenu = this.generalLayersPopupMenu;
			} else if (type == NodeDataType.LAYER3D_DATASET) {
				popupMenu = this.layer3DDatasetPopupMenu;
			} else if (type == NodeDataType.LAYER3D_KML) {
				popupMenu = this.layer3DKMLPopupMenu;
			} else if (type == NodeDataType.LAYER3D_IMAGE_FILE) {
				popupMenu = this.layer3DImagePopupMenu;
			} else if (type == NodeDataType.SCREENLAYER3D_GEOMETRY_TAG) {
				popupMenu = this.screenLayer3DPopupMenu;
			} else if (type == NodeDataType.TERRAIN_LAYER) {
				popupMenu = this.terrainLayerPopupMenu;
			} else if (type == NodeDataType.TERRAIN_LAYERS) {
				popupMenu = this.terrainLayersPopupMenu;
			} else if (type == NodeDataType.LAYER3D_MAP) {
				popupMenu = this.layer3DMapPopupMenu;
			}

			if (nodeData.getData() != null && nodeData.getData() instanceof Layer) {
				Layer layer = (Layer) nodeData.getData();

				if (layer.getTheme() != null) {
					if ((layer.getTheme() instanceof ThemeUnique) || (layer.getTheme() instanceof ThemeRange) || (layer.getTheme() instanceof ThemeCustom)) {
						popupMenu = this.layerVectorThemeUniqueAndRangePopupMenu;
					} else if ((layer.getTheme() instanceof ThemeGridRange) || (layer.getTheme() instanceof ThemeGridUnique)) {
						popupMenu = this.layerGridThemePopupMenu;
					} else {
						popupMenu = this.layerVectorThemeOtherPopupMenu;
					}
				} else if (layer.getDataset() != null) {
					if (layer.getDataset().getType() == DatasetType.CAD) {
						popupMenu = this.layerVectorCADPopupMenu;
					} else if (layer.getDataset().getType() == DatasetType.TEXT) {
						popupMenu = this.layerTextPopupMenu;
					} else if (layer.getDataset() instanceof DatasetVector) {
						popupMenu = this.layerVectorPopupMenu;
					} else if (layer.getDataset().getType() == DatasetType.IMAGE || layer.getDataset().getType() == DatasetType.IMAGECOLLECTION) {
						popupMenu = this.layerImagePopupMenu;
					} else if (layer.getDataset().getType() == DatasetType.GRID || layer.getDataset().getType() == DatasetType.GRIDCOLLECTION) {
						popupMenu = this.layerGridPopupMenu;
					} else {
						popupMenu = this.layerPopupMenu;
					}
				} else if (layer instanceof LayerGroup) {
					popupMenu = this.layerGroupPopupMenu;
				} else {
					popupMenu = this.layerPopupMenu;
				}

				if (popupMenu != null) {
					// 默认实现，后续进行初始化操作
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return popupMenu;
	}

	public LayersTree getLayersTree() {
		if (this.layersTree == null) {
			this.layersTree = new LayersTree();
		}
		return this.layersTree;
	}

	public Layer3DsTree getLayer3DsTree() {
		if (this.layer3DsTree == null) {
			this.layer3DsTree = new Layer3DsTree();
		}
		return this.layer3DsTree;
	}

	public Map getMap() {
		return this.layersTree.getMap();
	}

	public void setMap(Map map) {
		this.jScrollPane.setViewportView(this.layersTree);

		// 这里先这么绕一下，保证每次设置 map 都会生效，绕过相同地图不处理的问题。
		if (map != null) {
			this.layersTree.setMap(null);
		}
		this.layersTree.setMap(map);
		if (map!=null && map.getLayers() != null && map.getLayers().getCount() > 0) {
			layersTree.setSelectionRow(0);
		}
		this.layersTree.updateUI();
	}

	public Scene getScene() {
		return this.layer3DsTree.getScene();
	}

	public void setScene(Scene scene) {
		this.jScrollPane.setViewportView(this.layer3DsTree);

		// 这里先这么绕一下，保证每次设置 map 都会生效，绕过相同地图不处理的问题。
		if (scene != null) {
			this.layer3DsTree.setScene(null);
		}
		this.layer3DsTree.setScene(scene);
		this.layer3DsTree.updateUI();
	}

	private JPopupMenu layerVectorPopupMenu = null;

	/**
	 * 获取二维图层管理器中矢量图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerVectorPopupMenu() {
		return this.layerVectorPopupMenu;
	}

	private JPopupMenu layerVectorCADPopupMenu = null;

	/**
	 * 获取二维图层管理器中CAD图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerVectorCADPopupMenu() {
		return this.layerVectorCADPopupMenu;
	}

	private JPopupMenu layerTextPopupMenu = null;

	/**
	 * 获取二维图层管理器中文本图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerTextPopupMenu() {
		return this.layerTextPopupMenu;
	}

	private JPopupMenu layerGridPopupMenu = null;

	/**
	 * 获取二维图层管理器中栅格图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerGridPopupMenu() {
		return this.layerGridPopupMenu;
	}

	private JPopupMenu layerImagePopupMenu = null;

	/**
	 * 获取二维图层管理器中矢量图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerImagePopupMenu() {
		return this.layerImagePopupMenu;
	}

	private JPopupMenu layerVectorThemeUniqueAndRangePopupMenu = null;

	/**
	 * 获取二维图层管理器中矢量单值、分段专题图图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerVectorThemeUniqueAndRangePopupMenu() {
		return this.layerVectorThemeUniqueAndRangePopupMenu;
	}

	private JPopupMenu layerGridThemePopupMenu = null;

	/**
	 * 获取二维图层管理器中栅格单值、分段专题图图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerGridThemePopupMenu() {
		return this.layerGridThemePopupMenu;
	}

	private JPopupMenu layerVectorThemeOtherPopupMenu = null;

	/**
	 * 获取二维图层管理器中矢量点密度、统计、等级符号、标签、自定义专题图图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerVectorThemeOtherPopupMenu() {
		return this.layerVectorThemeOtherPopupMenu;
	}

	private JPopupMenu layerPopupMenu = null;

	/**
	 * 获取选中二维图层管理器中多个图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayerPopupMenu() {
		return this.layerPopupMenu;
	}

	private JPopupMenu layerGroupPopupMenu = null;

	/**
	 * 获取图层分组右键菜单
	 * 
	 * @return
	 */
	public JPopupMenu getLayerGroupPopupMenu() {
		return this.layerGroupPopupMenu;
	}

	private JPopupMenu themeItemUniqueAndRangePopupMenu = null;

	/**
	 * 获取二维矢量、栅格，单值、分段专题图子项右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getThemeItemUniqueAndRangePopupMenu() {
		return this.themeItemUniqueAndRangePopupMenu;
	}

	private JPopupMenu themeItemVectorStatPopupMenu = null;

	/**
	 * 获取二维矢量统计专题图子项右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getThemeItemVectorStatPopupMenu() {
		return this.themeItemVectorStatPopupMenu;
	}

	private JPopupMenu feature3DPopupMenu = null;

	/**
	 * 获取三维图层管理器中三维要素对象的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getFeature3DPopupMenu() {
		return this.feature3DPopupMenu;
	}

	private JPopupMenu feature3DsPopupMenu = null;

	/**
	 * 获取三维图层管理器中三维要素集合的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getFeature3DsPopupMenu() {
		return this.feature3DsPopupMenu;
	}

	private JPopupMenu generalLayersPopupMenu = null;

	/**
	 * 获取三维图层管理器中普通图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getGeneralLayersPopupMenu() {
		return this.generalLayersPopupMenu;
	}

	private JPopupMenu geometryPopupMenu = null;

	/**
	 * 获取选中对象的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getGeometryPopupMenu() {
		return this.geometryPopupMenu;
	}

	private JPopupMenu layer3DDatasetPopupMenu = null;

	/**
	 * 获取三维图层管理器中三维数据集图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DDatasetPopupMenu() {
		return this.layer3DDatasetPopupMenu;
	}

	private JPopupMenu layer3DKMLPopupMenu = null;

	/**
	 * 获取三维图层管理器中KML图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DKMLPopupMenu() {
		return this.layer3DKMLPopupMenu;
	}

	private JPopupMenu layer3DImagePopupMenu = null;

	/**
	 * 获取三维图层管理器中三维影像图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DImagePopupMenu() {
		return this.layer3DImagePopupMenu;
	}

	private JPopupMenu layer3DSCMPopupMenu = null;

	/**
	 * 获取三维图层管理器中三维SCM模型缓存的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DSCMPopupMenu() {
		return this.layer3DSCMPopupMenu;
	}

	private JPopupMenu layer3DVectorCachePopupMenu = null;

	/**
	 * 获取三维图层管理器中三维矢量缓存的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DVectorCachePopupMenu() {
		return this.layer3DVectorCachePopupMenu;
	}

	private JPopupMenu screenLayer3DPopupMenu = null;

	/**
	 * 获取三维图层管理器中屏幕图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getScreenLayer3DPopupMenu() {
		return this.screenLayer3DPopupMenu;
	}

	private JPopupMenu terrainLayerPopupMenu = null;

	/**
	 * 获取三维图层管理器中地形图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getTerrainLayerPopupMenu() {
		return this.terrainLayerPopupMenu;
	}

	private JPopupMenu terrainLayersPopupMenu = null;

	/**
	 * 获取三维图层管理器中地形图层集合的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getTerrainLayersPopupMenu() {
		return this.terrainLayersPopupMenu;
	}

	private JPopupMenu layer3DThemePopupMenu = null;

	/**
	 * 获取三维图层管理器中三维专题图图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DThemePopupMenu() {
		return this.layer3DThemePopupMenu;
	}

	private JPopupMenu trackingLayerPopupMenu = null;

	/**
	 * 获取三维图层管理器中跟踪图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getTrackingLayerPopupMenu() {
		return this.trackingLayerPopupMenu;
	}

	private JPopupMenu layer3DPopupMenu = null;

	/**
	 * 获取三维图层管理器中三维图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DPopupMenu() {
		return this.layer3DPopupMenu;
	}

	private JPopupMenu layer3DMapPopupMenu = null;

	/**
	 * 获取三维图层管理器中三维地图图层的右键菜单。
	 * 
	 * @return
	 */
	public JPopupMenu getLayer3DMapPopupMenu() {
		return this.layer3DMapPopupMenu;
	}

	/**
	 * 创建右键菜单对象
	 */
	private void buildContextMenu() {
		try {

			if (Application.getActiveApplication().getMainFrame() != null) {
				IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();

				this.layerVectorPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerVectorContextMenu");
				this.layerVectorCADPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerVectorCADContextMenu");
				this.layerTextPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerTextContextMenu");
				this.layerGridPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerGridContextMenu");
				this.layerImagePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerImageContextMenu");
				this.layerVectorThemeUniqueAndRangePopupMenu = (JPopupMenu) manager
						.get("SuperMap.Desktop.UI.LayersControlManager.LayerVectorThemeUniqueAndRangeContextMenu");
				this.layerGridThemePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerGridThemeContextMenu");
				this.layerVectorThemeOtherPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerVectorThemeOtherContextMenu");
				this.layerPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerContextMenu");
				this.layerGroupPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.LayerFolderContextMenu");
				this.themeItemUniqueAndRangePopupMenu = (JPopupMenu) manager
						.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuPlanarUniqueAndRangeTheme");
				this.themeItemVectorStatPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuPlanarVectorStatTheme");
				this.feature3DPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuFeature3D");
				this.geometryPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuGeometry");
				this.layer3DDatasetPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DDataset");
				this.layer3DKMLPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DKML");
				this.layer3DImagePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DImage");
				this.layer3DSCMPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DSCM");
				this.layer3DVectorCachePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DVectorCache");
				this.screenLayer3DPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuScreenLayer3D");
				this.terrainLayerPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuTerrainLayer");
				this.layer3DThemePopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DTheme");
				this.trackingLayerPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuTrackingLayer");
				this.layer3DPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3D");
				this.layer3DMapPopupMenu = (JPopupMenu) manager.get("SuperMap.Desktop.UI.LayersControlManager.ContextMenuLayer3DMap");

				this.isContextMenuBuilded = true;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}
