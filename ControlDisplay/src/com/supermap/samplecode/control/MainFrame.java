package com.supermap.samplecode.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.ui.controls.*;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;
import com.supermap.ui.MapControl;

/**
 * <p>
 * Title:閹貉傛閺勫墽銇�
 * </p>
 * 
 * <p>
 * Description: ============================================================================>
 * ------------------------------閻楀牊娼堟竟鐗堟---------------------------- 濮濄倖鏋冩禒鏈佃礋SuperMap Objects Java 閻ㄥ嫮銇氶懠鍐у敩閻拷 閻楀牊娼堥幍锟介張澶涚窗閸栨ぞ鍚搾鍛禈鏉烆垯娆㈤懖鈥插敜閺堝妾洪崗顒�寰�
 * ---------------------------------------------------------------- ---------------------SuperMap iObjects Java 缁�楦垮瘱缁嬪绨拠瀛樻------------------------
 * 
 * 1閵嗕浇瀵栨笟瀣暆娴犲绱扮粈楦垮瘱婵″倷缍嶆担璺ㄦ暏鏉堝懎濮幒褌娆㈢�瑰本鍨氱�佃鏆熼幑顔界埐閼哄倻鍋ｉ妴浣告勾閸ュ彞鑵戞禍宀�娣崶鎯х湴閸欏﹤婧�閺咁垯鑵戞稉澶屾樊閸ユ儳鐪伴惃鍕吀閻炲棗鎷扮拋鍓х枂閵嗭拷 2閵嗕胶銇氭笟瀣殶閹诡噯绱伴弮鐙呯幢 3閵嗕礁鍙ч柨顔捐閸拷/閹存劕鎲�:
 * WorkspaceTree.setDragEnabled 閺傝纭� WorkspaceTree.setTransferHandler 閺傝纭� WorkspaceTree.getSelectionPath 閺傝纭� WorkspaceTree.setWorkspace 閺傝纭�
 * WorkspaceTree.is/setDatasourcesNodeVisible 閺傝纭� WorkspaceTree.is/setMapsNodeVisible 閺傝纭� WorkspaceTree.is/setScenesNodeVisible 閺傝纭�
 * WorkspaceTree.is/setLayoutsNodeVisible 閺傝纭� WorkspaceTree.is/setResourcesNodeVisible 閺傝纭� Layer3DsTree.setScene 閺傝纭� LayersTree.setMap 閺傝纭�
 * 4閵嗕椒濞囬悽銊︻劄妤犮倧绱� (1)閻愮懓鍤柅澶嬪瀹搞儰缍旂粚娲？閼挎粌宕熼敍灞惧ⅵ瀵拷瀹搞儰缍旂粚娲？閺傚洣娆� (2)閸欏苯鍤粭锕�褰挎惔鎾憋拷浣哄殠閸ㄥ绨遍妴浣革綖閸忓懎绨遍懞鍌滃仯閿涘本妯夌粈楦跨カ濠ф劖绁荤憴鍫濐嚠鐠囨繃顢�
 * (3)闁瀚ㄩ崶鎯х湴閹貉傛閼哄倻鍋ｉ崜宥囨畱閹垮秳缍旈幐澶愭尦閿涘苯顕禍灞肩瑏缂佹潙娴樼仦鍌滄畱閻樿埖锟戒浇绻樼悰宀冾啎缂冿拷 (4)閸楁洖鍤崶鎯х湴閺勫墽銇氬鐟板毉閼挎粌宕熼敍灞惧灗閼板懎寮婚崙璇叉禈鐏炲偊绱濋崸鍥у讲閺勫墽銇氱粭锕�褰块柅澶嬪鐎电鐦藉锟�
 * (5)閻愮懓鍤銉ょ稊缁屾椽妫块弽鎴炲付娴犺绱濈�佃鏆熼幑顔垮Ν閻愮绻樼悰灞惧ⅵ瀵拷閵嗕礁鍨归梽銈冿拷渚�鍣搁崨钘夋倳缁涘鎼锋担锟� ------------------------------------------------------------------------------
 * ============================================================================>
 * </p>
 * 
 * <p>
 * Company: 閸栨ぞ鍚搾鍛禈鏉烆垯娆㈤懖鈥插敜閺堝妾洪崗顒�寰�
 * </p>
 * 
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JMenuBar m_menuBar = null;

	private JMenu m_menuWorkspace = null;

	private JMenuItem m_menuItemWsOpen = null;

	private JMenuItem m_menuItemWsSave = null;

	private JMenuItem m_menuItemClose = null;

	// private JMenu m_menuKML = null;

	// private JMenuItem m_menuItemVectorOpen = null;

	private JSplitPane m_mainsplitPane = null;

	private JSplitPane m_leftsplitPane = null;

	private Workspace m_workspace = null;

	private WorkspaceTree m_workspaceTree = null;

	private JScrollPane m_scrollPaneWsTree = null;

	private JScrollPane m_scrollPaneLegendTree = null;

	private JTabbedPane m_mainPane = null;

	private LayersTree m_layersTree = null;

	private Layer3DsTree m_layer3DsTree = null;

	private JPanel m_wsTreePanel = null;

	private JToolBar m_wsTreeOperationBar = null;

	private JButton m_btnIsVisibleResourceNode;
	private JButton m_btnIsVisibleLayoutNode;
	private JButton m_btnIsVisibleSceneNode;
	private JButton m_btnIsVisibleDatasourceNode;
	private JButton m_btnIsVisibleMapNode;

	private MapControl m_mapControl;

	private Map m_map = null;

	private GeoStyle m_preStyle = null;

	private Resources m_resources;

	private Layer m_currentLayer = null;

	private final static int MAPCONTROL_TAB_INDEX = 0;
	private final static int SCENECONTORL_TAB_INDEX = 1;
	private final static int MAPLAYOUTCONTROL_TAB_INDEX = 2;

	private final static ImageIcon DATASOURCENODEVISIBLE = createIconFromFile("datasource_node_visible.png");

	private final static ImageIcon DATASOURCENODEINVISIBLE = createIconFromFile("datasource_node_invisible.png");

	private final static ImageIcon MAPNODEVISIBLE = createIconFromFile("map_node_visible.png");

	private final static ImageIcon MAPNODEINVISIBLE = createIconFromFile("map_node_invisible.png");

	private final static ImageIcon SCENENODEVISIBLE = createIconFromFile("scene_node_visible.png");

	private final static ImageIcon SCENENODEINVISIBLE = createIconFromFile("scene_node_invisible.png");

	private final static ImageIcon LAYOUTNODEVISIBLE = createIconFromFile("layout_node_visible.png");

	private final static ImageIcon LAYOUTNODEINVISIBLE = createIconFromFile("layout_node_invisible.png");

	private final static ImageIcon RESOURCENODEVISIBLE = createIconFromFile("resource_node_visible.png");

	private final static ImageIcon RESOURCENODEINVISIBLE = createIconFromFile("resource_node_invisible.png");

	private static ImageIcon createIconFromFile(String fileName) {
		String strUrl = "/com/supermap/samples/images/" + fileName;
		URL url = MainFrame.class.getResource(strUrl);
		ImageIcon icon = null;
		if (url != null) {
			icon = new ImageIcon(url);
		}
		return icon;
	}

	/**
	 * 閸掓繂顫愰崠鏈朚enuBar鐎电钖勯妴锟�
	 * 
	 * @return JMenuBar鐎电钖�
	 */
	private JMenuBar getBar() {
		if (m_menuBar == null) {
			m_menuBar = new JMenuBar();
			m_menuBar.add(getMenuWorkspace());
		}
		return m_menuBar;
	}

	/**
	 * 閸掓繂顫愰崠鏈朚enu鐎电钖�
	 * 
	 * @return JMenu鐎电钖�
	 */
	private JMenu getMenuWorkspace() {
		if (m_menuWorkspace == null) {
			m_menuWorkspace = new JMenu();
			m_menuWorkspace.setText("瀹搞儰缍旂粚娲？");
			m_menuWorkspace.add(getMenuItemWsOpen());
			m_menuWorkspace.add(getMenuItemWsSave());
			m_menuWorkspace.add(getMenuItemClose());
		}
		return m_menuWorkspace;
	}

	/**
	 * 閸掓繂顫愰崠鏈朚enuItem鐎电钖�
	 * 
	 * @return JMenuItem鐎电钖�
	 */
	private JMenuItem getMenuItemWsOpen() {
		if (m_menuItemWsOpen == null) {
			m_menuItemWsOpen = new JMenuItem();
			m_menuItemWsOpen.setText("閹垫挸绱戝銉ょ稊缁屾椽妫�");
			m_menuItemWsOpen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					JFileChooser m_fileChooser = new JFileChooser(".");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("SuperMap瀹搞儰缍旂粚娲？閺傚洣娆�(*.sxw,*.sxw,*.smwu,*.sxwu)", "sxw", "smw", "sxwu",
							"smwu");
					m_fileChooser.addChoosableFileFilter(filter);
					// m_fileChooser.setCurrentDirectory(new
					// File("../../SampleData"));
					m_fileChooser.setCurrentDirectory(new File("../../SampleData/"));
					m_fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int returnVal = m_fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						if (m_workspace == null) {
							m_workspace = new Workspace();
						} else {
							if (m_layersTree != null) {
								m_layersTree.setMap(null);
							}

							m_mapControl.getMap().close();

							MainFrame.this.validate();
							m_workspace.close();
						}

						WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
						String filePath = m_fileChooser.getSelectedFile().getPath();
						info.setServer(filePath);
						if (filePath.endsWith("sxw")) {
							info.setType(WorkspaceType.SXW);
						} else if (filePath.endsWith("smw")) {
							info.setType(WorkspaceType.SMW);
						} else if (filePath.endsWith("sxwu")) {
							info.setType(WorkspaceType.SXWU);
						} else if (filePath.endsWith("smwu")) {
							info.setType(WorkspaceType.SMWU);
						} else {
							info.dispose();
							m_workspace.dispose();
							m_workspace = null;
						}
						if (m_workspace != null) {
							m_workspace.open(info);
							MainFrame.this.getWorkspaceTree().setWorkspace(m_workspace);
						}
					}

				}
			});
		}
		return m_menuItemWsOpen;
	}

	/**
	 * 閸掓繂顫愰崠鏈朚enuItem鐎电钖�
	 * 
	 * @return JMenuItem鐎电钖�
	 */
	private JMenuItem getMenuItemWsSave() {
		if (m_menuItemWsSave == null) {
			m_menuItemWsSave = new JMenuItem();
			m_menuItemWsSave.setText("娣囨繂鐡ㄥ銉ょ稊缁屾椽妫�");
			m_menuItemWsSave.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// m_workspace.save();

					// 閸掗攱鏌婄捄鐔婚嚋鐏炲倹绁寸拠锟�
					// m_mapControl.setTrackMode(TrackMode.TRACK);
					// m_mapControl.setAction(Action.CREATEPOLYLINE);
					//
					// GeoPoint point = new GeoPoint(m_mapControl.getMap().getCenter());
					// GeoStyle style = new GeoStyle();
					// style.setMarkerSymbolID(1410);
					// Size2D size = new Size2D(100, 100);
					// style.setMarkerSize(size);
					// point.setStyle(style);
					//
					// m_mapControl.getMap().getTrackingLayer().add(point, "閻愶拷");
					// m_mapControl.getMap().refreshTrackingLayer();

					//
					Layer layerLabel = m_mapControl.getMap().getLayers().get(0);
					Layer layerPoint = m_mapControl.getMap().getLayers().get(1);

					// if (layerPoint.getDisplayControledLayer() != null)
					// {
					// layerPoint.setDisplayControledLayer(null);
					// }
					// else
					// {
					// layerPoint.setDisplayControledLayer(layerLabel);
					// }

					m_mapControl.getMap().refresh();
				}

			});
		}
		return m_menuItemWsSave;
	}

	/**
	 * 閸掓繂顫愰崠鏈朚enuItem鐎电钖�
	 * 
	 * @return JMenuItem鐎电钖�
	 */
	private JMenuItem getMenuItemClose() {
		if (m_menuItemClose == null) {
			m_menuItemClose = new JMenuItem();
			m_menuItemClose.setText("閸忔娊妫村銉ょ稊缁屾椽妫�");
			m_menuItemClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (m_workspace != null) {
						if (m_layersTree != null) {
							m_layersTree.setMap(null);
						}

						m_mapControl.getMap().close();

						getScrollPaneLegendTree().setViewportView(null);
						MainFrame.this.validate();
						m_workspace.close();
					}
				}

			});
		}
		return m_menuItemClose;
	}

	/**
	 * 閸掓繂顫愰崠鏈朣plitPane鐎电钖�
	 * 
	 * @return JSplitPane鐎电钖�
	 */
	private JSplitPane getMainSplitPane() {
		if (m_mainsplitPane == null) {
			m_mainsplitPane = new JSplitPane();
			m_mainsplitPane.setDividerSize(10);
			m_mainsplitPane.setDividerLocation(260);
			m_mainsplitPane.add(getMainPane(), JSplitPane.RIGHT);
			m_mainsplitPane.add(getLeftSplitPane(), JSplitPane.LEFT);
		}
		return m_mainsplitPane;
	}

	/**
	 * 閸掓繂顫愰崠鏈朣plitPane鐎电钖�
	 * 
	 * @return JSplitPane鐎电钖�
	 */
	private JSplitPane getLeftSplitPane() {
		if (m_leftsplitPane == null) {
			m_leftsplitPane = new JSplitPane();
			m_leftsplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			m_leftsplitPane.setTopComponent(getScrollPaneWsTree());
			m_leftsplitPane.setBottomComponent(getScrollPaneLegendTree());
			m_leftsplitPane.setDividerLocation(250);
		}
		return m_leftsplitPane;
	}

	private JPanel getWsTreePanel() {
		if (m_wsTreePanel == null) {
			m_wsTreePanel = new JPanel();
			m_wsTreePanel.setLayout(new BorderLayout());
			m_wsTreePanel.add(getWsTreeOperationBar(), BorderLayout.NORTH);
			m_wsTreePanel.add(getWorkspaceTree(), BorderLayout.CENTER);
		}
		return m_wsTreePanel;
	}

	private JToolBar getWsTreeOperationBar() {
		if (m_wsTreeOperationBar == null) {
			m_wsTreeOperationBar = new JToolBar();
			m_wsTreeOperationBar.setBackground(Color.WHITE);
			m_wsTreeOperationBar.setFloatable(false);
			initializeWsTreeOperationPanel();
		}
		return m_wsTreeOperationBar;
	}

	private void initializeWsTreeOperationPanel() {
		m_btnIsVisibleDatasourceNode = new JButton();
		m_btnIsVisibleDatasourceNode.setIcon(DATASOURCENODEVISIBLE);
		m_btnIsVisibleDatasourceNode.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getWorkspaceTree().setDatasourcesNodeVisible(!getWorkspaceTree().isDatasourcesNodeVisible());
				if (getWorkspaceTree().isDatasourcesNodeVisible()) {
					m_btnIsVisibleDatasourceNode.setIcon(DATASOURCENODEVISIBLE);
				} else {
					m_btnIsVisibleDatasourceNode.setIcon(DATASOURCENODEINVISIBLE);
				}
				getWorkspaceTree().updateUI();
			}

		});
		m_btnIsVisibleMapNode = new JButton();
		m_btnIsVisibleMapNode.setIcon(MAPNODEVISIBLE);
		m_btnIsVisibleMapNode.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getWorkspaceTree().setMapsNodeVisible(!getWorkspaceTree().isMapsNodeVisible());
				if (getWorkspaceTree().isMapsNodeVisible()) {
					m_btnIsVisibleMapNode.setIcon(MAPNODEVISIBLE);
				} else {
					m_btnIsVisibleMapNode.setIcon(MAPNODEINVISIBLE);
				}
				getWorkspaceTree().updateUI();
			}

		});
		m_btnIsVisibleSceneNode = new JButton();
		m_btnIsVisibleSceneNode.setIcon(SCENENODEVISIBLE);
		m_btnIsVisibleSceneNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getWorkspaceTree().setScenesNodeVisible(!getWorkspaceTree().isScenesNodeVisible());
				if (getWorkspaceTree().isScenesNodeVisible()) {
					m_btnIsVisibleSceneNode.setIcon(SCENENODEVISIBLE);
				} else {
					m_btnIsVisibleSceneNode.setIcon(SCENENODEINVISIBLE);
				}
				getWorkspaceTree().updateUI();
			}

		});
		m_btnIsVisibleLayoutNode = new JButton();
		m_btnIsVisibleLayoutNode.setIcon(LAYOUTNODEVISIBLE);
		m_btnIsVisibleLayoutNode.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getWorkspaceTree().setLayoutsNodeVisible(!getWorkspaceTree().isLayoutsNodeVisible());
				if (getWorkspaceTree().isLayoutsNodeVisible()) {
					m_btnIsVisibleLayoutNode.setIcon(LAYOUTNODEVISIBLE);
				} else {
					m_btnIsVisibleLayoutNode.setIcon(LAYOUTNODEINVISIBLE);
				}
				getWorkspaceTree().updateUI();
			}

		});
		m_btnIsVisibleResourceNode = new JButton();
		m_btnIsVisibleResourceNode.setIcon(RESOURCENODEVISIBLE);
		m_btnIsVisibleResourceNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getWorkspaceTree().setResourcesNodeVisible(!getWorkspaceTree().isResourcesNodeVisible());
				if (getWorkspaceTree().isResourcesNodeVisible()) {
					m_btnIsVisibleResourceNode.setIcon(RESOURCENODEVISIBLE);
				} else {
					m_btnIsVisibleResourceNode.setIcon(RESOURCENODEINVISIBLE);
				}
				getWorkspaceTree().updateUI();
			}

		});
		getWsTreeOperationBar().add(m_btnIsVisibleDatasourceNode);
		getWsTreeOperationBar().add(m_btnIsVisibleLayoutNode);
		getWsTreeOperationBar().add(m_btnIsVisibleMapNode);
		getWsTreeOperationBar().add(m_btnIsVisibleSceneNode);
		getWsTreeOperationBar().add(m_btnIsVisibleResourceNode);
	}

	/**
	 * 閸掓繂顫愰崠鏈﹐rkspaceTree鐎电钖�
	 * 
	 * @return WorkspaceTree鐎电钖�
	 */
	private WorkspaceTree getWorkspaceTree() {
		if (m_workspaceTree == null) {
			m_workspaceTree = new WorkspaceTree();
			m_workspaceTree.setDragEnabled(true);
			m_workspaceTree.setTransferHandler(new WorkspaceTreeTransferHandler());
			m_workspaceTree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					TreePath path = m_workspaceTree.getSelectionPath();
					if (path != null) {
						Object object = path.getLastPathComponent();
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
						TreeNodeData data = (TreeNodeData) node.getUserObject();
						if (e.getClickCount() == 2) {
							if (data.getData() instanceof Dataset) {
								// getLayersTree().setMap(null);
								Dataset dataset = (Dataset) data.getData();
								m_mapControl.getMap().close();
								m_mapControl.getMap().setWorkspace(m_workspace);
								m_currentLayer = m_mapControl.getMap().getLayers().add(dataset, true);
								m_mapControl.getMap().refresh();
								m_map = m_mapControl.getMap();
								if (m_mainPane.getSelectedIndex() != MAPCONTROL_TAB_INDEX) {
									m_mainPane.setSelectedIndex(MAPCONTROL_TAB_INDEX);
								} else {
									getScrollPaneLegendTree().setViewportView(getLayersTree());
									MainFrame.this.validate();
								}
							} else if (data.getType().equals(NodeDataType.MAP_NAME)) {
								// getLayersTree().setMap(null);
								String mapName = (String) data.getData();
								m_mapControl.getMap().close();
								m_mapControl.getMap().setWorkspace(m_workspace);
								m_mapControl.getMap().open(mapName);
								m_mapControl.getMap().refresh();
								m_map = m_mapControl.getMap();
								if (m_mainPane.getSelectedIndex() != MAPCONTROL_TAB_INDEX) {
									m_mainPane.setSelectedIndex(MAPCONTROL_TAB_INDEX);
								} else {
									getScrollPaneLegendTree().setViewportView(getLayersTree());
									MainFrame.this.validate();
								}
							} else if (data.getType().equals(NodeDataType.LAYOUT_NAME)) {

							} else if (data.getType().equals(NodeDataType.SCENE_NAME)) {

							}
						}
					}
				}
			});
		}
		return m_workspaceTree;
	}

	/**
	 * 閸掓繂顫愰崠鏈朣crollPane鐎电钖�
	 * 
	 * @return JScrollPane鐎电钖�
	 */
	private JScrollPane getScrollPaneWsTree() {
		if (m_scrollPaneWsTree == null) {
			m_scrollPaneWsTree = new JScrollPane();
			m_scrollPaneWsTree.setViewportView(getWsTreePanel());
		}
		return m_scrollPaneWsTree;
	}

	/**
	 * 閸掓繂顫愰崠鏈朣crollPane鐎电钖�
	 * 
	 * @return JScrollPane鐎电钖�
	 */
	private JScrollPane getScrollPaneLegendTree() {
		if (m_scrollPaneLegendTree == null) {
			m_scrollPaneLegendTree = new JScrollPane();
		}
		return m_scrollPaneLegendTree;
	}

	/**
	 * 閸掓繂顫愰崠鏈朤abbedPane鐎电钖�
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getMainPane() {
		if (m_mainPane == null) {
			m_mainPane = new JTabbedPane();
			initTabbedPaneContorls();
			m_mainPane.addTab("Map", m_mapControl);

			m_mainPane.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					JTabbedPane pane = (JTabbedPane) e.getSource();
					int index = pane.getSelectedIndex();
					switch (index) {
					case MAPCONTROL_TAB_INDEX:
						m_mapControl.getMap().refresh();
						getScrollPaneLegendTree().setViewportView(getLayersTree());
						break;
					case SCENECONTORL_TAB_INDEX:

						break;
					case MAPLAYOUTCONTROL_TAB_INDEX:

						break;
					default:
						getScrollPaneLegendTree().setViewportView(null);
						break;
					}
					MainFrame.this.validate();
				}

			});
		}
		return m_mainPane;
	}

	/**
	 * 閸掓繂顫愰崠鏈朤abbedPane娑擃厺濞囬悽銊ュ煂閻ㄥ嚋ontrol鐎电钖�
	 */
	private void initTabbedPaneContorls() {
		m_mapControl = new MapControl();
		m_mapControl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (m_currentLayer != null && m_currentLayer.isEditable() && e.getButton() == 1 && e.getClickCount() == 2) {

					showTextStyleDialog();
				}
			}
		});
		DropTarget mapControlTarget = new DropTarget(m_mapControl, new DropTargetAdapter() {

			public void drop(DropTargetDropEvent dtde) {
				try {
					Transferable transferable = dtde.getTransferable();
					DataFlavor dataFlavor = transferable.getTransferDataFlavors()[0];
					if (dataFlavor == null) {
						return;
					}
					TreeNodeData transferData = (TreeNodeData) transferable.getTransferData(dataFlavor);
					if (transferData.getData() instanceof Dataset) {
						m_mapControl.getMap().setWorkspace(m_workspace);
						getScrollPaneLegendTree().setViewportView(getLayersTree());
						if (m_layersTree.getMap() == null) {
							m_layersTree.setMap(m_mapControl.getMap());
						}

						Dataset dataset = (Dataset) transferData.getData();
						m_mapControl.getMap().getLayers().add(dataset, true);
						m_mapControl.getMap().refresh();
					}
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		m_mapControl.setDropTarget(mapControlTarget);

		// m_mapLayoutControl = new MapLayoutControl();

	}

	/**
	 * 閸掓繂顫愰崠鏈檃yersTree鐎电钖�
	 * 
	 * @return LayersTree鐎电钖�
	 */
	private LayersTree getLayersTree() {
		if (m_layersTree == null) {
			m_layersTree = new LayersTree();
			m_layersTree.setMap(m_mapControl.getMap());
			m_layersTree.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						TreePath path = m_layersTree.getPathForLocation(e.getX(), e.getY());
						if (path != null) {
							Object object = path.getLastPathComponent();
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
							TreeNodeData data = (TreeNodeData) node.getUserObject();
							NodeDataType type = data.getType();
							if (type.equals(NodeDataType.LAYER)) {
								Layer layer = (Layer) data.getData();
								DatasetType datasetType = layer.getDataset().getType();
								if (datasetType.equals(DatasetType.POINT) || datasetType.equals(DatasetType.LINE) || datasetType.equals(DatasetType.REGION)
										|| datasetType.equals(DatasetType.TEXT)) {
									m_currentLayer = layer;
									if (layer.getTheme() == null) {
										getPopupMenu(layer).show(m_layersTree, e.getX(), e.getY());
									}
								}
							}
						}
					}
					if (e.getButton() == 1) {
						TreePath path = m_layersTree.getPathForLocation(e.getX(), e.getY());
						if (path != null) {
							Object object = path.getLastPathComponent();
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
							TreeNodeData data = (TreeNodeData) node.getUserObject();
							NodeDataType type = data.getType();
							if (type.equals(NodeDataType.LAYER)) {
								Layer layer = (Layer) data.getData();
								m_currentLayer = layer;
								if (e.getClickCount() == 2 && layer.getTheme() == null) {
									DatasetType datasetType = layer.getDataset().getType();

									if (datasetType.equals(DatasetType.POINT)) {
										showSymbolDialogBySymbolType(SymbolType.MARKER);
									} else if (datasetType.equals(DatasetType.LINE)) {
										showSymbolDialogBySymbolType(SymbolType.LINE);
									} else if (datasetType.equals(DatasetType.REGION)) {
										showSymbolDialogBySymbolType(SymbolType.FILL);
									}

								}
							}
						}
					}
				}

			});
		}

		m_layersTree.updateUI();
		return m_layersTree;
	}

	public JPopupMenu getPopupMenu(final Layer layer) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("妞嬪孩鐗哥拋鍓х枂");
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Dataset dataset = layer.getDataset();

				DatasetType type = dataset.getType();
				if (type.equals(DatasetType.POINT)) {
					showSymbolDialogBySymbolType(SymbolType.MARKER);
				} else if (type.equals(DatasetType.LINE)) {
					showSymbolDialogBySymbolType(SymbolType.LINE);
				} else if (type.equals(DatasetType.REGION)) {
					showSymbolDialogBySymbolType(SymbolType.FILL);
				}
			}

		});
		popupMenu.add(item);
		return popupMenu;
	}

	/**
	 * 閺嶈宓佺粭锕�褰跨猾璇茬�峰鐟板毉缁楋箑褰挎搴㈢壐鐠佸墽鐤嗙�电鐦藉锟�
	 * 
	 * @param type 缁楋箑褰跨猾璇茬��
	 */
	private void showSymbolDialogBySymbolType(SymbolType type) {
		m_resources = m_workspace.getResources();
		GeoStyle style = this.getPreStyle();

		SymbolDialog symbolDialog = new SymbolDialog();
		DialogResult dialogResult = symbolDialog.showDialog(m_resources, style, type);

		if (dialogResult.equals(DialogResult.OK)) {
			LayerSettingVector layerSettingVector = new LayerSettingVector();
			layerSettingVector.setStyle(symbolDialog.getStyle());

			m_currentLayer.setAdditionalSetting(layerSettingVector);
			m_map.refresh();
			m_layersTree.updateUI();
			symbolDialog.dispose();
		} else if (dialogResult.equals(DialogResult.CANCEL)) {
			System.out.println(symbolDialog.getStyle().getLineColor());
			LayerSettingVector layerSettingVector = new LayerSettingVector();
			layerSettingVector.setStyle(symbolDialog.getStyle());

			m_currentLayer.setAdditionalSetting(layerSettingVector);
			m_map.refresh();
			m_layersTree.updateUI();
			symbolDialog.dispose();
		}
	}

	/**
	 * 閺勫墽銇氶弬鍥ㄦ拱妞嬪孩鐗哥�电鐦藉锟�
	 */
	private void showTextStyleDialog() {
		Selection selection = m_currentLayer.getSelection();
		if (selection != null) {
			final Recordset recordset = selection.toRecordset();
			try {
				Geometry geometry = recordset.getGeometry();
				if (geometry.getType().equals(GeometryType.GEOTEXT)) {
					final GeoText geoText = (GeoText) geometry;
					TextStyleDialog dialog = new TextStyleDialog();
					dialog.setGeoText(geoText);
					dialog.setMapObject(m_mapControl.getMap());
					DialogResult dialogResult = dialog.showDialog();
					if (dialogResult.equals(DialogResult.OK)) {
						recordset.edit();
						recordset.setGeometry(dialog.getGeoText());
						recordset.update();
						m_mapControl.getMap().refresh();
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				if (recordset != null) {
					recordset.dispose();
				}
			}
		}
	}

	/**
	 * 閸掓繂顫愰崠鏈檃yer3DsTree鐎电钖�
	 * 
	 * @return Layer3DsTree鐎电钖�
	 */
	private Layer3DsTree getLayer3DsTree() {
		if (m_layer3DsTree == null) {
			m_layer3DsTree = new Layer3DsTree();
		}

		m_layer3DsTree.updateUI();
		return m_layer3DsTree;
	}

	/**
	 * 缁嬪绨崗銉ュ經閻愶拷
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// 缁備胶鏁ゆ妯款吇閻ㄥ嫮鐭栨担鎾崇摟閿涘本妯夌粈铏圭法鐟欏偊绱濈拠銉︽煙瀵繋璐烻un缁�楦垮瘱缁嬪绨稉顓犳畱閹恒劏宕橀弬鐟扮础
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				MainFrame thisClass = new MainFrame("閹貉傛濠曟梻銇�");
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * 姒涙顓婚弸鍕拷鐘插毐閺侊拷
	 */
	public MainFrame(String string) {
		super(string);
		initialize();
	}

	/**
	 * 閸掓繂顫愰崠鏍ь嚠鐠烇拷
	 * 
	 */
	private void initialize() {
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setSize(800, 500);
		this.setJMenuBar(getBar());
		this.setContentPane(getJContentPane());

		if (m_workspace == null) {
			m_workspace = new Workspace();
			WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
			info.setServer("../../SampleData/World/World.smwu");
			info.setServer("C:/Users/Administrator/Documents/World.smwu");
			info.setType(WorkspaceType.SMWU);
			if (m_workspace.open(info)) {
				getWorkspaceTree().setWorkspace(m_workspace);
			} else {
				m_workspace.dispose();
				m_workspace = null;
			}
		}
	}

	/**
	 * 閸掓繂顫愰崠鏈朠anel鐎电钖�
	 * 
	 * @return JPanel鐎电钖�
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * 閼惧嘲褰囪ぐ鎾冲閸ユ儳鐪伴惃鍕棑閺嶏拷
	 * 
	 * @return GeoStyle 瑜版挸澧犻崶鎯х湴閻ㄥ嫰顥撻弽锟�
	 */
	public GeoStyle getPreStyle() {
		m_preStyle = ((LayerSettingVector) m_currentLayer.getAdditionalSetting()).getStyle();
		return m_preStyle;
	}
}
