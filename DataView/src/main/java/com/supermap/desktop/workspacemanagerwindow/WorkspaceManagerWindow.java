package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetGrid;
import com.supermap.data.DatasetImage;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceAliasModifiedEvent;
import com.supermap.data.DatasourceAliasModifiedListener;
import com.supermap.data.DatasourceClosingEvent;
import com.supermap.data.DatasourceClosingListener;
import com.supermap.data.DatasourceCreatedEvent;
import com.supermap.data.DatasourceCreatedListener;
import com.supermap.data.DatasourceOpenedEvent;
import com.supermap.data.DatasourceOpenedListener;
import com.supermap.data.Datasources;
import com.supermap.data.Layouts;
import com.supermap.data.MapAddedEvent;
import com.supermap.data.MapAddedListener;
import com.supermap.data.MapClearedEvent;
import com.supermap.data.MapClearedListener;
import com.supermap.data.MapRenamedEvent;
import com.supermap.data.MapRenamedListener;
import com.supermap.data.Maps;
import com.supermap.data.Resources;
import com.supermap.data.SceneAddedEvent;
import com.supermap.data.SceneAddedListener;
import com.supermap.data.SceneClearedEvent;
import com.supermap.data.SceneClearedListener;
import com.supermap.data.SceneRenamedEvent;
import com.supermap.data.SceneRenamedListener;
import com.supermap.data.Scenes;
import com.supermap.data.SymbolFillLibrary;
import com.supermap.data.SymbolLineLibrary;
import com.supermap.data.SymbolMarkerLibrary;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.dataview.DataViewResources;
import com.supermap.desktop.event.FormClosingEvent;
import com.supermap.desktop.event.FormClosingListener;
import com.supermap.desktop.event.FormShownEvent;
import com.supermap.desktop.event.FormShownListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.DatasetTypeComboBox;
import com.supermap.desktop.ui.controls.TextFieldSearch;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.supermap.desktop.Application.getActiveApplication;
import static com.supermap.desktop.ui.UICommonToolkit.getWorkspaceManager;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NULL;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_PRJCOORDSYS;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_TYPE;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.DATAVIEW_ICON_ROOTPATH;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.FIRST_LEVEL;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.SECOND_LEVEL;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.THIRD_LEVEL;

/**
 * @author YuanR 2016.12.28
 */
public class WorkspaceManagerWindow extends FormBaseChild {
	private JTable jTable;
	private JToolBar buttonToolBar;
	private JButton jButtonLastLevel;
	private JScrollPane scrollPaneInfo;
	private DatasetTypeComboBox datasetTypeComboBox;
	private TextFieldSearch textFieldSearch;
	private Datasource selectedDatasource;

	private DefaultMutableTreeNode selectedDatasourceNode;
	private TreePath selectedTreeNodePath;

	private int levelNum;

	private boolean isExistModel;
	private boolean isDefaultColWidth = true;

	private int columnNameWidth;
	private int columnTypeWidth;
	private int columnNumberWidth;
	private int columnPathWidth;
	private int columnNullWidth;

	private static int COMBOXITEM_ALLDATASTYPE = 0;
	private static final int RESOURCE_MARKER_NUM = 0;
	private static final int RESOURCE_LINE_NUM = 1;
	private static final int RESOURCE_FILL_NUM = 2;


	/**
	 * @param title
	 * @param icon
	 * @param component
	 */
	private WorkspaceManagerWindow(String title, Icon icon, Component component) {
		super(title, icon, component);
		initComponents();
		initLayout();
		//initListeners();
	}

	/**
	 * 构造函数
	 */
	public WorkspaceManagerWindow() {
		this(DataViewProperties.getString("String_Label_WorkspaceManagerWindow"), null, null);
	}

	/**
	 * 初始化控件
	 */
	private void initComponents() {
		this.scrollPaneInfo = new JScrollPane();
		this.buttonToolBar = new JToolBar();
		//返回上一级按钮
		this.jButtonLastLevel = new JButton();
		this.jButtonLastLevel.setIcon(DataViewResources.getIcon(DATAVIEW_ICON_ROOTPATH + "Reset.png"));
		this.jButtonLastLevel.setToolTipText(DataViewProperties.getString("String_ToolTip_BackLastLevel"));
		this.jButtonLastLevel.setEnabled(false);

		//获得数据集类型筛选器
		this.datasetTypeComboBox = new DatasetTypeComboBox();
		this.datasetTypeComboBox.setMaximumSize(new Dimension(200, 25));
		this.datasetTypeComboBox.setEnabled(false);
		//获得数据集搜索框
		this.textFieldSearch = new TextFieldSearch();
		this.textFieldSearch.setMaximumSize(new Dimension(200, 25));
		this.textFieldSearch.setText("");
		this.textFieldSearch.setEnabled(false);

		this.jTable = new JTable();
		//赋予jtable初始化的model
		initializeJTable();
		//当不选中任何节点，或者选择了初始化中不包括的节点都赋予起始页
		if (!isExistModel) {
			jTable.setModel(new GetTableModel().getWorkspaceTableModel(getActiveApplication().getWorkspace()));
			jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
			//设置起始页“个数”的对齐方式
			jTable.setDefaultRenderer(Integer.class, new TableCellRendererWorkspace());
			//设置
			setOtherComponents(jTable.getModel());
		}
		//取消网格
		this.jTable.setShowHorizontalLines(false);
		this.jTable.setShowVerticalLines(false);
		this.jTable.setAutoCreateRowSorter(true);
		//列头不可拖拽
		this.jTable.getTableHeader().setReorderingAllowed(false);
		//关闭自适应
		this.jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.jTable.repaint();

		//增加窗口监听
		this.addFormShownListener(shownListener);
		this.addFormClosingListener(closingListener);
	}

	/**
	 * 初始化jtable
	 */
	private void initializeJTable() {
		TreePath[] treePaths = getWorkspaceManager().getWorkspaceTree().getSelectionPaths();
		if (treePaths == null) {
			return;
		}
		int length = treePaths.length;
		if (length == 0) {
			return;
		} else {
			DefaultMutableTreeNode[] treeNodes = new DefaultMutableTreeNode[length];
			TreeNodeData[] selectedNodeDatas = new TreeNodeData[length];
			for (int i = 0; i < length; i++) {
				treeNodes[i] = (DefaultMutableTreeNode) treePaths[i].getLastPathComponent();
				selectedNodeDatas[i] = (TreeNodeData) treeNodes[i].getUserObject();
			}
			//获得最后一个节点的父节点
			DefaultMutableTreeNode treeNodesParent = (DefaultMutableTreeNode) treeNodes[0].getParent();
			TreeNodeData selectedNodeDatasParent;
			if (treeNodesParent != null) {
				selectedNodeDatasParent = (TreeNodeData) treeNodesParent.getUserObject();
			} else {
				selectedNodeDatasParent = null;
			}
			if (selectedNodeDatas[0].getData() instanceof Workspace) {//工作空间
				jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
				//设置起始页“个数”的对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererWorkspace());
			} else if (selectedNodeDatas[0].getData() instanceof Datasources) {//数据源节点
				jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
				//设置起始页“个数”的对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererDatasources());
			} else if (selectedNodeDatas[0].getData() instanceof Maps || selectedNodeDatasParent.getData() instanceof Maps) {//地图节点
				//设置model为地图
				jTable.setModel(new GetTableModel().getMapsTableModel(getActiveApplication().getWorkspace().getMaps()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererMaps());

				//当父节点为为地图节点时，即选中了地图
				if (selectedNodeDatasParent.getData() instanceof Maps) {
					//当点击了tree中数据集节点，窗口中相应节点高亮显示
					String[] selectedNodeNames = new String[length];
					for (int i = 0; i < length; i++) {
						selectedNodeNames[i] = (String) selectedNodeDatas[i].getData();
					}
					setJTableInterval(selectedNodeNames);
				}

			} else if (selectedNodeDatas[0].getData() instanceof Scenes || selectedNodeDatasParent.getData() instanceof Scenes) {
				//设置model为场景
				jTable.setModel(new GetTableModel().getScenesTableModel(getActiveApplication().getWorkspace().getScenes()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererScenes());
				//当父节点为为场景节点时，即选中了场景
				if (selectedNodeDatasParent.getData() instanceof Scenes) {
					//当点击了tree中数据集节点，窗口中相应节点高亮显示
					String[] selectedNodeNames = new String[length];
					for (int i = 0; i < length; i++) {
						selectedNodeNames[i] = (String) selectedNodeDatas[i].getData();
					}
					setJTableInterval(selectedNodeNames);
				}

			} else if (selectedNodeDatas[0].getData() instanceof Layouts) {//暂不实现此方法
			} else if (selectedNodeDatas[0].getData() instanceof Resources
					|| selectedNodeDatas[0].getData() instanceof SymbolMarkerLibrary
					|| selectedNodeDatas[0].getData() instanceof SymbolLineLibrary
					|| selectedNodeDatas[0].getData() instanceof SymbolFillLibrary) {
				jTable.setModel(new GetTableModel().getResourcesTableModel());
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererResources());
			} else if (selectedNodeDatas[0].getData() instanceof Datasource) {//数据源
				//设置model为数据集
				selectedDatasource = (Datasource) selectedNodeDatas[0].getData();
				selectedDatasourceNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
				//model的设置需要依据一下combox的选择值
				if (datasetTypeComboBox.getSelectedItem().toString().equals(CommonProperties.getString("String_DatasetType_All"))) {
					jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
				} else {
					jTable.setModel(new GetTableModel().getDatasourceTypeTableModel(selectedDatasource, CommonToolkit.DatasetTypeWrap.findType(datasetTypeComboBox.getSelectedItem().toString())));
				}
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
				//设置数据集“个数”显示方式及对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererDatasource());
			} else if (selectedNodeDatas[0].getData() instanceof Dataset) {//数据集
				//获得选中数据集所属的数据源节点
				selectedDatasourceNode = (DefaultMutableTreeNode) treeNodes[0].getParent();
				TreeNodeData selectedLastNodeData = (TreeNodeData) selectedDatasourceNode.getUserObject();
				selectedDatasource = (Datasource) selectedLastNodeData.getData();
				//model的设置需要依据一下combox的选择值
				if (datasetTypeComboBox.getSelectedItem().toString().equals(CommonProperties.getString("String_DatasetType_All"))) {
					jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
				} else {
					jTable.setModel(new GetTableModel().getDatasourceTypeTableModel(selectedDatasource, CommonToolkit.DatasetTypeWrap.findType(datasetTypeComboBox.getSelectedItem().toString())));
				}
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
				//设置数据集“个数”显示方式及对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererDatasource());
				//当点击了tree中数据集节点，窗口中相应节点高亮显示
				String[] selectedNodeNames = new String[length];
				for (int i = 0; i < length; i++) {
					selectedNodeNames[i] = ((Dataset) selectedNodeDatas[i].getData()).getName();
				}
				setJTableInterval(selectedNodeNames);
			}
			setOtherComponents(jTable.getModel());
		}
	}


	/**
	 * 当点击了tree中数据集节点，窗口中相应节点高亮显示
	 */
	private void setJTableInterval(String[] selectNames) {
		for (String current : selectNames) {
			for (int j = 0; j < jTable.getModel().getRowCount(); j++) {
				if (jTable.getModel().getValueAt(j, COLUMN_NAME).equals(current)) {
					jTable.addRowSelectionInterval(j, j);
					//使高亮行可见
					jTable.scrollRectToVisible(jTable.getCellRect(j, 0, true));
				}
			}
		}
	}


	/**
	 * 初始化布局
	 */
	private void initLayout() {
		this.buttonToolBar.add(jButtonLastLevel);
		this.buttonToolBar.addSeparator();
		this.buttonToolBar.add(datasetTypeComboBox);
		this.buttonToolBar.addSeparator();
		this.buttonToolBar.add(textFieldSearch);

		this.scrollPaneInfo.setViewportView(jTable);
		this.scrollPaneInfo.getViewport().setBackground(Color.white);

		this.add(buttonToolBar, BorderLayout.NORTH);
		this.add(scrollPaneInfo, BorderLayout.CENTER);
	}

	/**
	 * 初始化监听
	 */
	private void initListeners() {
		this.jTable.addMouseListener(this.jtableListeners);
		this.jTable.getTableHeader().addMouseListener(this.jtableHeaderListeners);
		this.datasetTypeComboBox.addItemListener(this.datasetTypeComboBoxLisyeners);
		this.jButtonLastLevel.addActionListener(this.jButtonLastLevelActionListeners);
		this.textFieldSearch.getDocument().addDocumentListener(this.textFieldSearchChangeListener);
		//tree对窗口的监听
		getWorkspaceManager().getWorkspaceTree().addMouseListener(this.treeToJtable);

		/**
		 * 将对工作空间的监听写在initLiseteners外，不进行注销监听的操作
		 * 当关闭工作空间时，由于会执行closeAll（），导致窗口关闭，窗口关闭又会移除监听，因此不进行移除监听的操作
		 * 尝试当关闭或打开工作空间时，不关闭窗口_yuanR16.12.27
		 */
		//移除工作空间关闭监听
		getWorkspaceManager().getWorkspace().removeClosingListener(this.workspaceClosingListener);
		//移除工作空间打开监听
		getWorkspaceManager().getWorkspace().removeOpenedListener(this.workspaceOpenedListener);
		//工作空间关闭监听
		getWorkspaceManager().getWorkspace().addClosingListener(this.workspaceClosingListener);
		//工作空间打开监听
		getWorkspaceManager().getWorkspace().addOpenedListener(this.workspaceOpenedListener);

		//数据源打开监听
		getWorkspaceManager().getWorkspace().getDatasources().addOpenedListener(this.datasourceOpenedListener);
		//数据源关闭监听
		getWorkspaceManager().getWorkspace().getDatasources().addClosingListener(this.datasourceClosingListener);
		//数据源创建监听
		getWorkspaceManager().getWorkspace().getDatasources().addCreatedListener(this.datasourceCreatedListener);
		//数据源别名被修改监听
		getWorkspaceManager().getWorkspace().getDatasources().addAliasModifiedListener(this.datasourceAliasModifiedListener);
		//地图名称被改变监听
		getWorkspaceManager().getWorkspace().getMaps().addRenamedListener(this.mapRenamedListener);
		//增加地图监听
		getWorkspaceManager().getWorkspace().getMaps().addAddedListener(this.mapAddedListener);
		//删除地图监听
		getWorkspaceManager().getWorkspace().getMaps().addClearedListener(this.mapClearedListener);
		//增加场景监听
		getWorkspaceManager().getWorkspace().getScenes().addAddedListener(this.sceneAddedListener);
		//创建场景监听
		getWorkspaceManager().getWorkspace().getScenes().addClearedListener(this.sceneClearedListener);
		//重命名场景监听
		getWorkspaceManager().getWorkspace().getScenes().addRenamedListener(this.sceneRenamedListener);
	}

	/**
	 * 注销监听事件
	 */
	private void removeListeners() {
		this.jTable.removeMouseListener(this.jtableListeners);
		this.jTable.getTableHeader().removeMouseListener(this.jtableHeaderListeners);
		this.datasetTypeComboBox.removeItemListener(this.datasetTypeComboBoxLisyeners);
		this.jButtonLastLevel.removeActionListener(this.jButtonLastLevelActionListeners);
		this.textFieldSearch.getDocument().removeDocumentListener(this.textFieldSearchChangeListener);
		//移除tree对窗口的监听
		getWorkspaceManager().getWorkspaceTree().removeMouseListener(this.treeToJtable);

		//工作空间关闭监听
		//getWorkspaceManager().getWorkspace().removeClosingListener(this.workspaceClosingListener);
		//工作空间打开监听
		//getWorkspaceManager().getWorkspace().removeOpenedListener(this.workspaceOpenedListener);

		//移除数据源打开监听
		getWorkspaceManager().getWorkspace().getDatasources().removeOpenedListener(this.datasourceOpenedListener);
		//移除数据源关闭监听
		getWorkspaceManager().getWorkspace().getDatasources().removeClosingListener(this.datasourceClosingListener);
		//移除数据源创建监听
		getWorkspaceManager().getWorkspace().getDatasources().removeCreatedListener(this.datasourceCreatedListener);
		//移除数据源别名被修改监听
		getWorkspaceManager().getWorkspace().getDatasources().removeAliasModifiedListener(this.datasourceAliasModifiedListener);
		//移除地图名称被改变监听
		getWorkspaceManager().getWorkspace().getMaps().removeRenamedListener(this.mapRenamedListener);
		//移除增加地图监听
		getWorkspaceManager().getWorkspace().getMaps().removeAddedListener(this.mapAddedListener);
		//移除删除地图监听
		getWorkspaceManager().getWorkspace().getMaps().removeClearedListener(this.mapClearedListener);
		//移除增加场景监听
		getWorkspaceManager().getWorkspace().getScenes().removeAddedListener(this.sceneAddedListener);
		//移除创建场景监听
		getWorkspaceManager().getWorkspace().getScenes().removeClearedListener(this.sceneClearedListener);
		//移除重命名场景监听
		getWorkspaceManager().getWorkspace().getScenes().removeRenamedListener(this.sceneRenamedListener);

	}

	/**
	 * JTable事件
	 */
	private MouseAdapter jtableListeners = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				//初始化jtable
				initializeJTable();
				//获得树节点选中的对象
				DefaultMutableTreeNode selectTreeNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
				TreeNodeData selectedTreeNodeData = (TreeNodeData) selectTreeNode.getUserObject();
				//获得选中节点的父节点
				DefaultMutableTreeNode selectTreeNodeParent = (DefaultMutableTreeNode) selectTreeNode.getParent();
				TreeNodeData selectTreeNodeParentData = (TreeNodeData) selectTreeNodeParent.getUserObject();
				if (selectedTreeNodeData.getData() instanceof Dataset) {//当树节点选中对象为数据集时，进行打开操作
					MapViewUIUtilities.addDatasetsToNewWindow(new Dataset[]{(Dataset) selectedTreeNodeData.getData()}, true);
				} else if (selectTreeNodeParentData.getData() instanceof Maps) {//当树节点选中对象为地图时，进行打开操作
					MapViewUIUtilities.openMap((String) selectedTreeNodeData.getData());
				} else if (selectTreeNodeParentData.getData() instanceof Scenes) {//当树节点选中对象为场景时，进行打开操作
					// TODO something
				} else if (selectTreeNodeParentData.getData() instanceof Layouts) {//当树节点选中对象为布局时，进行打开操作
					// TODO something
				} else if (selectTreeNodeParentData.getData() instanceof SymbolMarkerLibrary) {
					// TODO something
				} else if (selectTreeNodeParentData.getData() instanceof SymbolLineLibrary) {
					// TODO something
				} else if (selectTreeNodeParentData.getData() instanceof SymbolFillLibrary) {
					// TODO something
				}
				/**
				 * 双击左键，根据高亮的树节点，执行打开操作
				 */
			} else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				DefaultMutableTreeNode selectTreeNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
				TreeNodeData selectedTreeNodeData = (TreeNodeData) selectTreeNode.getUserObject();
				//获得选中节点的父节点
				DefaultMutableTreeNode selectTreeNodeParent = (DefaultMutableTreeNode) selectTreeNode.getParent();
				TreeNodeData selectTreeNodeParentData = (TreeNodeData) selectTreeNodeParent.getUserObject();
				//通过选中节点的数据类型给予相应的右键菜单
				if (selectedTreeNodeData.getData() instanceof Datasources) {//数据源节点
					JPopupMenu jPopupMenu_datasources = getWorkspaceManager().getDatasourcesPopupMenu();
					jPopupMenu_datasources.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof Maps) {//地图节点
					JPopupMenu jPopupMenu_maps = getWorkspaceManager().getMapsPopupMenu();
					jPopupMenu_maps.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof Layouts) {//布局节点
				} else if (selectedTreeNodeData.getData() instanceof Scenes) {//场景节点
					JPopupMenu jPopupMenu_scenes = getWorkspaceManager().getScenesPopupMenu();
					jPopupMenu_scenes.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof Resources) {//资源节点
				} else if (selectedTreeNodeData.getData() instanceof SymbolMarkerLibrary) {//符号资源节点
					JPopupMenu jPopupMenu_Marker_Resources = getWorkspaceManager().getSymbolMarkerPopupMenu();
					jPopupMenu_Marker_Resources.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof SymbolLineLibrary) {//线资源节点
					JPopupMenu jPopupMenu_Line_Resources = getWorkspaceManager().getSymbolLinePopupMenu();
					jPopupMenu_Line_Resources.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof SymbolFillLibrary) {//填充资源节点
					JPopupMenu jPopupMenu_Fill_Resources = getWorkspaceManager().getSymbolFillPopupMenu();
					jPopupMenu_Fill_Resources.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof Datasource) {//数据源
					JPopupMenu jPopupMenu_datasource = getWorkspaceManager().getDatasourcePopupMenu();
					jPopupMenu_datasource.show(jTable, e.getX(), e.getY());
				} else if (selectedTreeNodeData.getData() instanceof Dataset) {//数据集
					// TODO: 2016/12/24 add JPopupMenu  of datasettype
					if (selectedTreeNodeData.getData() instanceof DatasetGrid || selectedTreeNodeData.getData() instanceof DatasetImage) {//栅格/影像(待补充)
						JPopupMenu jPopupMenu_dataset = getWorkspaceManager().getDatasetGridPopupMenu();
						jPopupMenu_dataset.show(jTable, e.getX(), e.getY());
					} else {
						JPopupMenu jPopupMenu_dataset = getWorkspaceManager().getDatasetVectorPopupMenu();
						jPopupMenu_dataset.show(jTable, e.getX(), e.getY());
					}
				} else if (selectTreeNodeParentData.getData() instanceof Maps) {//地图
					JPopupMenu jPopupMenu_map = getWorkspaceManager().getMapPopupMenu();
					jPopupMenu_map.show(jTable, e.getX(), e.getY());
				} else if (selectTreeNodeParentData.getData() instanceof Scenes) {//场景
					JPopupMenu jPopupMenu_scene = getWorkspaceManager().getScenePopupMenu();
					jPopupMenu_scene.show(jTable, e.getX(), e.getY());
				} else if (selectTreeNodeParentData.getData() instanceof Layouts) {//布局（暂无）
					JPopupMenu jPopupMenu_layout = getWorkspaceManager().getLayoutPopupMenu();
					jPopupMenu_layout.show(jTable, e.getX(), e.getY());
				}
			}
		}

		/**
		 * 左键单击jtable树节点对应高亮选中
		 * @param e
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
				int rowIndex = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
				if (rowIndex >= 0) {
					//当只选中一条数据时，点击刷新高亮显示
					if (jTable.getSelectedRowCount() <= 1) {
						jTable.changeSelection(rowIndex, COLUMN_NAME, false, false);
					}
					Object cellVal = jTable.getValueAt(rowIndex, COLUMN_NAME);
					if (cellVal instanceof Datasources) {
						DefaultMutableTreeNode datasourcesNode = getWorkspaceManager().getWorkspaceTree().getDatasourcesNode();
						selectedTreeNodePath = new TreePath(datasourcesNode.getPath());
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
					} else if (cellVal instanceof Maps) {
						DefaultMutableTreeNode mapsNode = getWorkspaceManager().getWorkspaceTree().getMapsNode();
						selectedTreeNodePath = new TreePath(mapsNode.getPath());
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
					} else if (cellVal instanceof Layouts) {//暂不实现任何功能
					} else if (cellVal instanceof Scenes) {
						DefaultMutableTreeNode scenesNode = getWorkspaceManager().getWorkspaceTree().getScenesNode();
						selectedTreeNodePath = new TreePath(scenesNode.getPath());
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
					} else if (cellVal instanceof Resources) {
						DefaultMutableTreeNode resourcesNode = getWorkspaceManager().getWorkspaceTree().getResourcesNode();
						selectedTreeNodePath = new TreePath(resourcesNode.getPath());
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
					} else if (cellVal instanceof SymbolMarkerLibrary
							|| cellVal instanceof SymbolLineLibrary
							|| cellVal instanceof SymbolFillLibrary) {
						if (cellVal instanceof SymbolMarkerLibrary) {
							DefaultMutableTreeNode resourceMarkerNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getResourcesNode().getChildAt(RESOURCE_MARKER_NUM);
							selectedTreeNodePath = new TreePath(resourceMarkerNode.getPath());
						} else if (cellVal instanceof SymbolLineLibrary) {
							DefaultMutableTreeNode resourceLineNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getResourcesNode().getChildAt(RESOURCE_LINE_NUM);
							selectedTreeNodePath = new TreePath(resourceLineNode.getPath());
						} else if (cellVal instanceof SymbolFillLibrary) {
							DefaultMutableTreeNode resourceFillNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getResourcesNode().getChildAt(RESOURCE_FILL_NUM);
							selectedTreeNodePath = new TreePath(resourceFillNode.getPath());
						}
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
					}

					//点击了数据源
					/**
					 * 如果将数据源也以对象的方式存入，→	else if （cellVal instanceof datasource）
					 */
					else if (getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()) != null && cellVal.equals(getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()).getAlias()) && levelNum == SECOND_LEVEL) {
						//获得数据源文件节点
						for (int i = 0; i < getWorkspaceManager().getWorkspace().getDatasources().getCount(); i++) {
							if ((getWorkspaceManager().getWorkspace().getDatasources().get(i).getAlias()).equals(cellVal)) {
								//展开数据源文件节点
								DefaultMutableTreeNode datasourceNode = getWorkspaceManager().getWorkspaceTree().getDatasourcesNode();
								selectedTreeNodePath = new TreePath(datasourceNode.getPath());
								getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
								//设置选中数据源
								selectedDatasourceNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getDatasourcesNode().getChildAt(i);
								selectedTreeNodePath = new TreePath(selectedDatasourceNode.getPath());
								getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
								break;
							}
						}
					}
					//点击了数据集(多选单选一起判断)
					/**
					 *
					 * 如果将数据源也以对象的方式存入，→	else if （cellVal instanceof dataset）
					 *
					 */
					else if (levelNum == THIRD_LEVEL && selectedDatasource != null && getActiveApplication().getWorkspace().getDatasources().get(selectedDatasource.getAlias().toString()).getDatasets() != null) {
						//清除选择
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(null);
						for (int i = 0; i < jTable.getSelectedRowCount(); i++) {
							for (int num = 0; num < selectedDatasource.getDatasets().getCount(); num++) {
								if (jTable.getValueAt(jTable.getSelectedRows()[i], COLUMN_NAME).toString().equals(selectedDatasource.getDatasets().get(num).getName())) {
									//选中数据集节点数据
									getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
									DefaultMutableTreeNode selectedDatasetNode = (DefaultMutableTreeNode) selectedDatasourceNode.getChildAt(num);
									selectedTreeNodePath = new TreePath(selectedDatasetNode.getPath());
									if (jTable.getSelectedRowCount() > 1) {//此时为多选状态
										//增加选择
										getWorkspaceManager().getWorkspaceTree().addSelectionPath(selectedTreeNodePath);
									} else {
										getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
									}
									break;
								}
							}
						}
					}
					//点击了地图(多选单选一起判断)
					else if (levelNum == SECOND_LEVEL && getActiveApplication().getWorkspace().getMaps().getCount() > 0 && jTable.getModel().getValueAt(0, COLUMN_TYPE).equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {
						//清除选择
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(null);
						for (int i = 0; i < jTable.getSelectedRowCount(); i++) {
							for (int num = 0; num < getActiveApplication().getWorkspace().getMaps().getCount(); num++) {
								if (jTable.getValueAt(jTable.getSelectedRows()[i], COLUMN_NAME).toString().equals(getActiveApplication().getWorkspace().getMaps().get(num))) {
									//设置联动及焦点获取
									//展开地图节点
									DefaultMutableTreeNode mapNode = getWorkspaceManager().getWorkspaceTree().getMapsNode();
									selectedTreeNodePath = new TreePath(mapNode.getPath());
									getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
									//设置选中节点为地图
									DefaultMutableTreeNode selectedMapNode = (DefaultMutableTreeNode) mapNode.getChildAt(num);
									selectedTreeNodePath = new TreePath(selectedMapNode.getPath());
									if (jTable.getSelectedRowCount() > 1) {//此时为多选状态
										//增加选择
										getWorkspaceManager().getWorkspaceTree().addSelectionPath(selectedTreeNodePath);
									} else {
										getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
									}
									break;
								}
							}
						}
					}
					//点击了场景节点(多选单选一起判断)
					else if (levelNum == SECOND_LEVEL && getActiveApplication().getWorkspace().getScenes().getCount() > 0 && jTable.getModel().getValueAt(0, COLUMN_TYPE).equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {
						//清除选择
						getWorkspaceManager().getWorkspaceTree().setSelectionPath(null);
						for (int i = 0; i < jTable.getSelectedRowCount(); i++) {
							for (int num = 0; num < getActiveApplication().getWorkspace().getScenes().getCount(); num++) {
								if (jTable.getValueAt(jTable.getSelectedRows()[i], COLUMN_NAME).toString().equals(getActiveApplication().getWorkspace().getScenes().get(num))) {
									//设置联动及焦点获取
									//展开场景节点
									DefaultMutableTreeNode sceneNode = getWorkspaceManager().getWorkspaceTree().getScenesNode();
									selectedTreeNodePath = new TreePath(sceneNode.getPath());
									getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
									//设置选中节点为场景
									DefaultMutableTreeNode selectedSceneNode = (DefaultMutableTreeNode) sceneNode.getChildAt(num);
									selectedTreeNodePath = new TreePath(selectedSceneNode.getPath());
									if (jTable.getSelectedRowCount() > 1) {//此时为多选状态
										//增加选择
										getWorkspaceManager().getWorkspaceTree().addSelectionPath(selectedTreeNodePath);
									} else {
										getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
									}
									break;
								}
							}
						}
					}
					//将选择的节点展示出来
					getWorkspaceManager().getWorkspaceTree().scrollPathToVisible(selectedTreeNodePath);
					//获得工作空间节点
					DefaultMutableTreeNode workspaceNode = getWorkspaceManager().getWorkspaceTree().getWorkspaceNode();
					selectedTreeNodePath = new TreePath(workspaceNode.getPath());
					//展开工作空间节点
					getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
				}
			}
		}
	};

	/**
	 * JTable列头宽度改变监听
	 */
	private MouseAdapter jtableHeaderListeners = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			columnNameWidth = jTable.getColumnModel().getColumn(COLUMN_NAME).getPreferredWidth();
			columnTypeWidth = jTable.getColumnModel().getColumn(COLUMN_TYPE).getPreferredWidth();
			columnNumberWidth = jTable.getColumnModel().getColumn(COLUMN_NUMBER).getPreferredWidth();
			columnPathWidth = jTable.getColumnModel().getColumn(COLUMN_PRJCOORDSYS).getPreferredWidth();
			columnNullWidth = jTable.getColumnModel().getColumn(COLUMN_NULL).getPreferredWidth();
			isDefaultColWidth = false;
		}
	};

	/**
	 * combox改变监听
	 */
	private ItemListener datasetTypeComboBoxLisyeners = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			//comboxBox改变监听只在第三层生效
			if (levelNum == THIRD_LEVEL) {
				//选择了“所有数据集类型
				if (datasetTypeComboBox.getSelectedItem().toString().equals(CommonProperties.getString("String_DatasetType_All"))) {
					jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
				} else {
					jTable.setModel(new GetTableModel().getDatasourceTypeTableModel(selectedDatasource, CommonToolkit.DatasetTypeWrap.findType(datasetTypeComboBox.getSelectedItem().toString())));
				}
				//设置table的渲染
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
				//设置数据集“个数”显示方式及对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererDatasource());
				jTable.repaint();
				setOtherComponents(jTable.getModel());
			}
		}
	};

	/**
	 * 返回上级按钮监听事件
	 */
	private ActionListener jButtonLastLevelActionListeners = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (levelNum == SECOND_LEVEL) {//此时处于第二层，需要返回起始页
				jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
				//设置起始页“个数”的对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererWorkspace());
			} else if (levelNum == THIRD_LEVEL) {//此时处于第三层（数据集层），需要返回数据源文件层
				//当需要返回第二层时，提前将ComboBox设置为初始条目，防止之后设置为第二层后，又设置ComboBox，层级错乱
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
				//设置“个数”的对齐方式
				jTable.setDefaultRenderer(Integer.class, new TableCellRendererDatasources());
			}
			setOtherComponents(jTable.getModel());
		}
	};

	/**
	 * textFieldSearch改变事件
	 */
	private DocumentListener textFieldSearchChangeListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			newFilter();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			newFilter();
		}
	};

	/**
	 * 树对窗口的监听
	 */
	private MouseAdapter treeToJtable = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			initializeJTable();
		}
	};

	/**
	 * 工作空间打开监听
	 */
	private WorkspaceOpenedListener workspaceOpenedListener = new WorkspaceOpenedListener() {
		@Override
		public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
			jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
			jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
			//设置起始页“个数”的对齐方式
			jTable.setDefaultRenderer(Integer.class, new TableCellRendererWorkspace());
			setOtherComponents(jTable.getModel());
		}
	};

	/**
	 * 工作空间关闭监听
	 */
	private WorkspaceClosingListener workspaceClosingListener = new WorkspaceClosingListener() {
		@Override
		public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
			jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
			jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
			//设置起始页“个数”的对齐方式
			jTable.setDefaultRenderer(Integer.class, new TableCellRendererWorkspace());
			setOtherComponents(jTable.getModel());
		}
	};

	/**
	 * 数据源打开监听
	 */
	private DatasourceOpenedListener datasourceOpenedListener = new DatasourceOpenedListener() {
		@Override
		public void datasourceOpened(DatasourceOpenedEvent datasourceOpenedEvent) {
			refresh();
		}
	};

	/**
	 * 数据源关闭监听
	 */
	private DatasourceClosingListener datasourceClosingListener = new DatasourceClosingListener() {
		@Override
		public void datasourceClosing(DatasourceClosingEvent datasourceClosingEvent) {
			//关闭数据源时需要更换jtable的のmodel，不然会出现对象释放的报错
			jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
			jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
			//设置起始页“个数”的对齐方式
			jTable.setDefaultRenderer(Integer.class, new TableCellRendererDatasources());
			setOtherComponents(jTable.getModel());
			refresh();
		}
	};

	/**
	 * 数据源创建监听
	 */
	private DatasourceCreatedListener datasourceCreatedListener = new DatasourceCreatedListener() {
		@Override
		public void datasourceCreated(DatasourceCreatedEvent datasourceCreatedEvent) {
			refresh();
		}
	};


	/**
	 * 数据源别名改变监听
	 */
	private DatasourceAliasModifiedListener datasourceAliasModifiedListener = new DatasourceAliasModifiedListener() {
		@Override
		public void datasourceAliasModified(DatasourceAliasModifiedEvent datasourceAliasModifiedEvent) {
			refresh();
		}
	};
	/**
	 * 地图名称改变监听
	 */
	private MapRenamedListener mapRenamedListener = new MapRenamedListener() {
		@Override
		public void mapRenamed(MapRenamedEvent mapRenamedEvent) {
			refresh();
		}
	};

	/**
	 * 增加地图监听
	 */
	private MapAddedListener mapAddedListener = new MapAddedListener() {
		@Override
		public void mapAdded(MapAddedEvent mapAddedEvent) {
			refresh();
		}
	};

	/**
	 * 删除地图监听
	 */
	private MapClearedListener mapClearedListener = new MapClearedListener() {
		@Override
		public void mapCleared(MapClearedEvent mapClearedEvent) {
			refresh();
		}
	};

	/**
	 * 重命名场景监听
	 */
	private SceneRenamedListener sceneRenamedListener = new SceneRenamedListener() {
		@Override
		public void sceneRenamed(SceneRenamedEvent sceneRenamedEvent) {
			refresh();
		}
	};
	/**
	 * 增加场景监听
	 */
	private SceneAddedListener sceneAddedListener = new SceneAddedListener() {
		@Override
		public void sceneAdded(SceneAddedEvent sceneAddedEvent) {
			refresh();
		}
	};

	/**
	 * 删除场景监听
	 */
	private SceneClearedListener sceneClearedListener = new SceneClearedListener() {
		@Override
		public void sceneCleared(SceneClearedEvent sceneClearedEvent) {
			refresh();
		}
	};

	/**
	 * 窗口显示监听
	 */
	private FormShownListener shownListener = new FormShownListener() {
		@Override
		public void formShown(FormShownEvent e) {
			removeListeners();
			initListeners();
			initializeJTable();
		}
	};

	/**
	 * 窗口隐藏监听
	 */
	private FormClosingListener closingListener = new FormClosingListener() {
		@Override
		public void formClosing(FormClosingEvent e) {
			removeListeners();
		}
	};

	/**
	 * 刷新动作
	 */
	private void refresh() {
		if (this.jTable.getModel() != null) {
			AbstractTableModel nowModel = (AbstractTableModel) this.jTable.getModel();
			nowModel.fireTableDataChanged();
			this.jTable.repaint();
		}
	}

	/**
	 * 通过重写include方法，对筛选器筛选方式进行修改
	 */
	private void newFilter() {
		RowFilter<TableModel, Object> rf = new RowFilter<TableModel, Object>() {
			public boolean include(Entry<? extends TableModel, ? extends Object> entry) {
				//当第一列单元格内容包含输入的字符串，进行显示
				if ((entry.getValue(COLUMN_NAME).toString().toLowerCase()).indexOf((textFieldSearch.getText().toLowerCase())) >= 0) {
					return true;
				}
				return false;
			}
		};
		TableRowSorter sorter = new TableRowSorter(this.jTable.getModel());
		sorter.setRowFilter(rf);
		this.jTable.setRowSorter(sorter);
		this.jTable.repaint();
	}

	/**
	 * 设置列宽
	 */
	private void setColumnWith() {
		if (isDefaultColWidth) {
			this.jTable.getColumnModel().getColumn(COLUMN_NAME).setPreferredWidth(200);
			this.jTable.getColumnModel().getColumn(COLUMN_TYPE).setPreferredWidth(150);
			this.jTable.getColumnModel().getColumn(COLUMN_NUMBER).setPreferredWidth(150);
			this.jTable.getColumnModel().getColumn(COLUMN_PRJCOORDSYS).setPreferredWidth(500);
			this.jTable.getColumnModel().getColumn(COLUMN_NULL).setPreferredWidth(522);
		} else {
			this.jTable.getColumnModel().getColumn(COLUMN_NAME).setPreferredWidth(this.columnNameWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_TYPE).setPreferredWidth(this.columnTypeWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_NUMBER).setPreferredWidth(this.columnNumberWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_PRJCOORDSYS).setPreferredWidth(this.columnPathWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_NULL).setPreferredWidth(this.columnNullWidth);
		}
	}

	/**
	 * 设置当model改变时设置相关控件的属性
	 */
	private void setOtherComponents(TableModel tableModel) {
		if (tableModel != null) {
			setColumnWith();
			if (tableModel instanceof TableModelWorkspace) {
				this.textFieldSearch.setText("");
				this.datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				this.jButtonLastLevel.setEnabled(false);
				this.textFieldSearch.setEnabled(false);
				this.datasetTypeComboBox.setEnabled(false);
				this.isExistModel = true;
				this.selectedDatasource = null;
				this.levelNum = FIRST_LEVEL;
			} else if (tableModel instanceof TableModelDatasources || tableModel instanceof TableModelMaps || tableModel instanceof TableModelScenes) {
				this.jButtonLastLevel.setEnabled(true);
				this.textFieldSearch.setEnabled(true);
				this.textFieldSearch.setText("");
				this.datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				this.datasetTypeComboBox.setEnabled(false);
				this.isExistModel = true;
				this.selectedDatasource = null;
				this.levelNum = SECOND_LEVEL;
			} else if (tableModel instanceof TableModelResources) {
				this.textFieldSearch.setText("");
				this.datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				this.jButtonLastLevel.setEnabled(true);
				this.textFieldSearch.setEnabled(false);
				this.datasetTypeComboBox.setEnabled(false);
				this.isExistModel = true;
				this.selectedDatasource = null;
				this.levelNum = SECOND_LEVEL;
			} else if (tableModel instanceof TableModelDatasource || tableModel instanceof TableModelDatasType) {
				this.textFieldSearch.setText("");
				this.jButtonLastLevel.setEnabled(true);
				this.textFieldSearch.setEnabled(true);
				this.datasetTypeComboBox.setEnabled(true);
				this.isExistModel = true;
				this.levelNum = THIRD_LEVEL;
			}
		}
	}
}
