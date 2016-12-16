package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
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
import com.supermap.data.WorkspaceClosedEvent;
import com.supermap.data.WorkspaceClosedListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.dataview.DataViewResources;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.controls.DatasetTypeComboBox;
import com.supermap.desktop.ui.controls.TextFieldSearch;
import com.supermap.desktop.ui.controls.TreeNodeData;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
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
 * @author YuanR 2016.12.3
 */
public class WorkspaceManagerWindow extends FormBaseChild {
	private JTable jTable;
	private JToolBar buttonToolBar;
	private JButton jButtonLastLevel;
	private JScrollPane scrollPaneInfo;
	private DatasetTypeComboBox datasetTypeComboBox;
	private TextFieldSearch textFieldSearch;
	private Datasource selectedDatasource;
	private DefaultMutableTreeNode selectedNode;
	private DefaultMutableTreeNode selectedDatasourceNode;
	private TreePath selectedTreeNodePath;

	private TableRowSorter<TableModel> sorter;
	private int levelNum;
	private DefaultTableCellRenderer render;
	private JTree getJtree;
	private Workspace getWorkspace;
	private Datasources getDatasources;
	private Maps getMaps;
	private Scenes getScenes;
	private Resources getResources;
	private boolean isWindowShown;
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
		initListeners();
	}

//	@Override
//	public void close() {
//		super.close();
//		this.setVisible(false);
//		this.isWindowShown = false;
//	}
//
//	// 当窗口显示
//	@Override
//	public void windowShown() {
//		//刷新jtable，其显示跟随tree焦点
//		this.isWindowShown = true;
//		this.add(buttonToolBar, BorderLayout.NORTH);
//		this.add(scrollPaneInfo, BorderLayout.CENTER);
//		initJTable();
//	}

//	@Override
//	public boolean isUndockable() {
//		//暂时关闭undockable，当undockable时窗口不稳定
//		return false;
//	}
//
//	// 当窗口隐藏
//	@Override
//	public void windowHidden() {
//		//断开窗口与外部的连接
//		this.isWindowShown = false;
//	}

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
		//model的对齐方式
		this.render = new DefaultTableCellRenderer();
		this.render.setHorizontalAlignment(SwingConstants.LEFT);
		//获得tree选中节点,如果有节点被选中，则show出相应节点下的管理窗口
		this.selectedNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();//返回最后选定的节点

		//赋予jtable初始化的model
		initJTable();
		//当不选中任何节点，或者选择了初始化中不包括的节点都赋予起始页
		if (!isExistModel) {
			jTable.setModel(new GetTableModel().getWorkspaceTableModel(getActiveApplication().getWorkspace()));
			jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
			//设置列宽
			setColumnWith();
			TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
			column.setCellRenderer(render);
			this.levelNum = FIRST_LEVEL;
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
	}

	/**
	 * 初始化jtable
	 */
	private void initJTable() {
		selectedNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();//返回最后选定的节点
		if (null != selectedNode) {
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (null == selectedNodeData) {
				return;
			}
			if (selectedNodeData.getData() instanceof Workspace) {//当点击tree_workspace节点
				//当点击工作空间——tree
				//设置model
				jTable.setModel(new GetTableModel().getWorkspaceTableModel(getActiveApplication().getWorkspace()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
				isExistModel = true;
				//单元格靠左对齐
				TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
				column.setCellRenderer(render);
				//设置层级其他空间属性
				jButtonLastLevel.setEnabled(false);
				textFieldSearch.setText("");
				textFieldSearch.setEnabled(false);
				selectedDatasource = null;
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				datasetTypeComboBox.setEnabled(false);
				levelNum = FIRST_LEVEL;
			} else if (selectedNodeData.getData() instanceof Datasources) {//当点击tree_datasources节点
				jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
				//设置渲染器
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
				isExistModel = true;
				TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
				column.setCellRenderer(render);

				textFieldSearch.setText("");
				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setEnabled(true);
				selectedDatasource = null;
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				datasetTypeComboBox.setEnabled(false);
				levelNum = SECOND_LEVEL;
			} else if (selectedNodeData.getData() instanceof Maps || selectedNodeData.getData() instanceof String) {// 当选择地图数据时，等于string
				//设置model为地图
				jTable.setModel(new GetTableModel().getMapsTableModel(getActiveApplication().getWorkspace().getMaps()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererMaps());
				isExistModel = true;
				textFieldSearch.setText("");
				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setEnabled(true);
				selectedDatasource = null;
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				datasetTypeComboBox.setEnabled(false);
				levelNum = SECOND_LEVEL;
			} else if (selectedNodeData.getData() instanceof Scenes) {
				//设置model为场景
				jTable.setModel(new GetTableModel().getScenesTableModel(getActiveApplication().getWorkspace().getScenes()));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererScenes());
				isExistModel = true;
				textFieldSearch.setText("");
				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setEnabled(true);
				selectedDatasource = null;
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				datasetTypeComboBox.setEnabled(false);
				levelNum = SECOND_LEVEL;
			} else if (selectedNodeData.getData() instanceof Layouts) {//暂不实现此方法
			} else if (selectedNodeData.getData() instanceof Resources
					|| selectedNodeData.getData() instanceof SymbolMarkerLibrary
					|| selectedNodeData.getData() instanceof SymbolLineLibrary
					|| selectedNodeData.getData() instanceof SymbolFillLibrary) {
				jTable.setModel(new GetTableModel().getResourcesTableModel());
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererResources());
				isExistModel = true;
				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setText("");
				textFieldSearch.setEnabled(false);
				selectedDatasource = null;
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				datasetTypeComboBox.setEnabled(false);
				levelNum = SECOND_LEVEL;

			} else if (selectedNodeData.getData() instanceof Datasource) {
				//设置model为数据集
				selectedDatasource = (Datasource) selectedNodeData.getData();
				selectedDatasourceNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
				jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
				jTable.setDefaultRenderer(DatasetType.class, new TableCellRendererDatasource());
				isExistModel = true;
				TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
				column.setCellRenderer(render);

				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setText("");
				textFieldSearch.setEnabled(true);
				datasetTypeComboBox.getSelectedIndex();
				datasetTypeComboBox.setEnabled(true);
				levelNum = THIRD_LEVEL;
				//选择节点为地图或或者数据集
			} else if (selectedNodeData.getData() instanceof Dataset) {
				//获得选中数据集所属的数据源节点
				selectedDatasourceNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent()).getParent();
				TreeNodeData selectedLastNodeData = (TreeNodeData) selectedDatasourceNode.getUserObject();
				selectedDatasource = (Datasource) selectedLastNodeData.getData();
				jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
				jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
				jTable.setDefaultRenderer(DatasetType.class, new TableCellRendererDatasource());
				isExistModel = true;
				TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
				column.setCellRenderer(render);

				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setText("");
				textFieldSearch.setEnabled(true);
				datasetTypeComboBox.getSelectedIndex();
				datasetTypeComboBox.setEnabled(true);
				levelNum = THIRD_LEVEL;
			}
			//设置列宽
			setColumnWith();
		}
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		this.buttonToolBar.add(jButtonLastLevel);
		//this.buttonToolBar.add(this.jButtonRefresh);
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
		jButtonLastLevelListeners();
		jcomboxBoxListeners();
		jTableDoubleClickedListeners();
		jTableToTreeListeners();
		addTreeToJtableListeners();
		addJPopupMenuListeners();
		textFieldSearchChangedListeners();
		addRefreshListeners();
		addColumnWithChangedListeners();
	}

	/**
	 * 刷新监听
	 */
	private void addRefreshListeners() {
		//工作空间关闭
		this.getWorkspace = getWorkspaceManager().getWorkspace();
		this.getWorkspace.addClosedListener(new WorkspaceClosedListener() {
			@Override
			public void workspaceClosed(WorkspaceClosedEvent workspaceClosedEvent) {
				if (isWindowShown) {
					//System.out.println("关闭工作空间");
					//当工作空间关闭时，保持窗口存在，层级返回起始页
					jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
					//设置列宽
					setColumnWith();
					textFieldSearch.setText("");
					textFieldSearch.setEnabled(false);
					selectedDatasource = null;
					jButtonLastLevel.setEnabled(false);
					levelNum = FIRST_LEVEL;
				}
			}
		});
		//工作空间打开
		this.getWorkspace.addOpenedListener(new WorkspaceOpenedListener() {
			@Override
			public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {
				//当工作空间打开，刷新
				// 展示起始页
				if (isWindowShown) {
					//System.out.println("打开工作空间");
					jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
					//设置列宽
					setColumnWith();
					textFieldSearch.setText("");
					textFieldSearch.setEnabled(false);
					selectedDatasource = null;
					jButtonLastLevel.setEnabled(false);
					levelNum = FIRST_LEVEL;
				}

			}
		});
		//数据源打开
		this.getDatasources = getWorkspaceManager().getWorkspace().getDatasources();
		this.getDatasources.addOpenedListener(new DatasourceOpenedListener() {
			@Override
			public void datasourceOpened(DatasourceOpenedEvent datasourceOpenedEvent) {
				refresh();
			}
		});
		//数据源关闭
		this.getDatasources.addClosingListener(new DatasourceClosingListener() {
			@Override
			public void datasourceClosing(DatasourceClosingEvent datasourceClosingEvent) {
				if (isWindowShown) {
					//当关闭数据源时，跳转到数据源文件界面
					jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
					//设置列宽
					setColumnWith();
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);

					jButtonLastLevel.setEnabled(true);
					textFieldSearch.setEnabled(true);
					levelNum = SECOND_LEVEL;
				}
			}
		});

		//数据源创建
		this.getDatasources.addCreatedListener(new DatasourceCreatedListener() {
			@Override
			public void datasourceCreated(DatasourceCreatedEvent datasourceCreatedEvent) {
				refresh();
			}
		});
		//数据源别名被修改
		this.getDatasources.addAliasModifiedListener(new DatasourceAliasModifiedListener() {
			@Override
			public void datasourceAliasModified(DatasourceAliasModifiedEvent datasourceAliasModifiedEvent) {
				refresh();
			}
		});
		//地图名称被改变
		this.getMaps = getWorkspaceManager().getWorkspace().getMaps();
		this.getMaps.addRenamedListener(new MapRenamedListener() {
			@Override
			public void mapRenamed(MapRenamedEvent mapRenamedEvent) {
				refresh();
			}
		});
		//增加地图
		this.getMaps.addAddedListener(new MapAddedListener() {
			@Override
			public void mapAdded(MapAddedEvent mapAddedEvent) {
				refresh();
			}
		});
		//删除地图
		this.getMaps.addClearedListener(new MapClearedListener() {
			@Override
			public void mapCleared(MapClearedEvent mapClearedEvent) {
				refresh();
			}
		});
		//增加场景
		this.getScenes = getWorkspaceManager().getWorkspace().getScenes();
		this.getScenes.addAddedListener(new SceneAddedListener() {
			@Override
			public void sceneAdded(SceneAddedEvent sceneAddedEvent) {
				refresh();
			}
		});
		//创建场景
		this.getScenes.addClearedListener(new SceneClearedListener() {
			@Override
			public void sceneCleared(SceneClearedEvent sceneClearedEvent) {
				refresh();
			}
		});
		//重命名场景
		this.getScenes.addRenamedListener(new SceneRenamedListener() {
			@Override
			public void sceneRenamed(SceneRenamedEvent sceneRenamedEvent) {
				refresh();
			}
		});
	}

	/**
	 * 刷新动作
	 */
	private void refresh() {
		if (isWindowShown) {
			if (this.jTable.getModel() != null) {
				AbstractTableModel nowModel = (AbstractTableModel) this.jTable.getModel();
				nowModel.fireTableDataChanged();
				this.jTable.repaint();
			}
		}
	}

	/**
	 * 返回上一级按钮监听
	 * 判断所处层级，进行返回上一级操作（给予new model）
	 */
	private void jButtonLastLevelListeners() {
		this.jButtonLastLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (levelNum == SECOND_LEVEL) {//此时处于第二层，需要返回起始页
					jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
					//设置列宽
					setColumnWith();
					isExistModel = true;
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
					jTable.repaint();

					textFieldSearch.setText("");
					textFieldSearch.setEnabled(false);
					selectedDatasource = null;
					jButtonLastLevel.setEnabled(false);
					levelNum = FIRST_LEVEL;
				} else if (levelNum == THIRD_LEVEL) {//此时处于第三层（数据集层），需要返回数据源文件层
					datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
					datasetTypeComboBox.setEnabled(false);
					jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
					//设置列宽
					setColumnWith();
					isExistModel = true;
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
					jTable.repaint();

					levelNum = SECOND_LEVEL;
				}
			}
		});
	}

	/**
	 * JComboxBox改变监听
	 */
	private void jcomboxBoxListeners() {
		this.datasetTypeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				//comboxBox改变监听只在第三层生效
				if (levelNum == THIRD_LEVEL) {
					//选择了“所有数据集类型
					if (datasetTypeComboBox.getSelectedItem().toString().equals(CommonProperties.getString("String_DatasetType_All"))) {
						jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
					}
					//选择了“其他数据类型”
					else {
						jTable.setModel(new GetTableModel().getDatasourceTypeTableModel(selectedDatasource, CommonToolkit.DatasetTypeWrap.findType(datasetTypeComboBox.getSelectedItem().toString())));
					}
					//设置table的渲染
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
					jTable.setDefaultRenderer(DatasetType.class, new TableCellRendererDatasource());
					//设置列宽
					setColumnWith();
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
					jTable.repaint();
				}
			}
		});
	}

	/**
	 * JTable鼠标双击单元格事件
	 * 待优化：将监听鼠标事件改为JTable选中事件，操作更为直接
	 */
	private void jTableDoubleClickedListeners() {
		this.jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					//System.out.println("JTable,Clicked,BUTTON1,jTableMouseListeners");
					//获得首列单元格内容，以此作为跳转判断依据
					int rowIndex = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
					if (rowIndex >= 0) {
						Object cellVal = jTable.getValueAt(rowIndex, COLUMN_NAME);
						jButtonLastLevel.setEnabled(true);
						// textFieldSearch.setEnabled(true);
						//暂时通过首列字符串以判断方式进行所选判断
						if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideDatasource"))) {//数据源
							//设置model
							jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Datasources());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());
							isExistModel = true;
							TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
							column.setCellRenderer(render);

							textFieldSearch.setEnabled(true);
							levelNum = SECOND_LEVEL;

						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {//地图
							jTable.setModel(new GetTableModel().getMapsTableModel(getActiveApplication().getWorkspace().getMaps()));
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Maps());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererMaps());
							isExistModel = true;
							textFieldSearch.setEnabled(true);
							levelNum = SECOND_LEVEL;

						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideLayout"))) {//暂不实现此方法，隐藏布局列
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {//场景
							jTable.setModel(new GetTableModel().getScenesTableModel(getActiveApplication().getWorkspace().getScenes()));
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Scenes());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererScenes());
							isExistModel = true;
							textFieldSearch.setEnabled(true);
							levelNum = SECOND_LEVEL;
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideResources"))) {//资源
							jTable.setModel(new GetTableModel().getResourcesTableModel());
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Resources());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererResources());
							isExistModel = true;
							textFieldSearch.setText("");
							textFieldSearch.setEnabled(false);
							levelNum = SECOND_LEVEL;
							//通过cellVal,数据源文件存在其选择的数据源，并选中了其数据源
						} else if (getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()) != null && cellVal.equals(getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()).getAlias())) {//数据源文件
							for (int num = 0; num < getWorkspaceManager().getWorkspace().getDatasources().getCount(); num++) {
								if ((getWorkspaceManager().getWorkspace().getDatasources().get(num).getAlias()).equals(cellVal)) {
									selectedDatasource = getActiveApplication().getWorkspace().getDatasources().get(num);
									//设置model
									jTable.setModel(new GetTableModel().getDatasourceTableModel(getActiveApplication().getWorkspace().getDatasources().get(num)));
									jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());
									jTable.setDefaultRenderer(DatasetType.class, new TableCellRendererDatasource());

									isExistModel = true;

									TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
									column.setCellRenderer(render);

									datasetTypeComboBox.getSelectedIndex();
									datasetTypeComboBox.setEnabled(true);
									textFieldSearch.setEnabled(true);
									levelNum = THIRD_LEVEL;
									break;
								}
							}
							//选中数据源中存在数据
						} else if (levelNum == THIRD_LEVEL && selectedDatasource != null) {
							for (int num = 0; num < selectedDatasource.getDatasets().getCount(); num++) {
								if (cellVal.equals(selectedDatasource.getDatasets().get(num).getName())) {
									MapViewUIUtilities.addDatasetsToNewWindow(new Dataset[]{(Dataset) selectedDatasource.getDatasets().get(num)}, true);
									break;
								}
							}
						} else if (levelNum == SECOND_LEVEL && getActiveApplication().getWorkspace().getMaps().getCount() > 0) {//存在地图
							for (int num = 0; num < getActiveApplication().getWorkspace().getMaps().getCount(); num++) {
								if (cellVal.equals(getActiveApplication().getWorkspace().getMaps().get(num))) {
									MapViewUIUtilities.openMap(cellVal.toString());
									break;
								}
							}
						} else {
						}
						//设置列宽
						setColumnWith();
					}
				}
			}
		});
	}

	/**
	 * 给“窗口添加右键菜单”
	 */
	private void addJPopupMenuListeners() {
		this.jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				//当点击松开了右键，设置右键菜单
				if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
					//System.out.println("JTable,Pressed,BUTTON3,addJPopupMenuListeners");
					int rowIndex = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
					if (rowIndex >= 0) {
						Object cellVal = jTable.getValueAt(rowIndex, COLUMN_NAME);
						if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideDatasource"))) {
							//设置右键菜单
							JPopupMenu jPopupMenu_datasources = getWorkspaceManager().getDatasourcesPopupMenu();
							jPopupMenu_datasources.show(jTable, e.getX(), e.getY());

						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {
							JPopupMenu jPopupMenu_maps = getWorkspaceManager().getMapsPopupMenu();
							jPopupMenu_maps.show(jTable, e.getX(), e.getY());
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideLayout"))) {//暂不实现此功能
							//暂无右键菜单
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {
							JPopupMenu jPopupMenu_scenes = getWorkspaceManager().getScenesPopupMenu();
							jPopupMenu_scenes.show(jTable, e.getX(), e.getY());
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideResources"))) {
							//暂无右键菜单
						} else if (cellVal.equals(ControlsProperties.getString("SymbolMarkerLibNodeName"))) {
							//设置被选中
							JPopupMenu jPopupMenu_Marker_Resources = getWorkspaceManager().getSymbolMarkerPopupMenu();
							jPopupMenu_Marker_Resources.show(jTable, e.getX(), e.getY());
						} else if (cellVal.equals(ControlsProperties.getString("SymbolLineLibNodeName"))) {
							//设置被选中
							JPopupMenu jPopupMenu_Line_Resources = getWorkspaceManager().getSymbolLinePopupMenu();
							jPopupMenu_Line_Resources.show(jTable, e.getX(), e.getY());
						} else if (cellVal.equals(ControlsProperties.getString("SymbolFillLibNodeName"))) {
							//设置被选中
							JPopupMenu jPopupMenu_Fill_Resources = getWorkspaceManager().getSymbolFillPopupMenu();
							jPopupMenu_Fill_Resources.show(jTable, e.getX(), e.getY());
						}
						//存在数据集(处于第二层，存在数据源文件，并且选中了)
						else if (levelNum == SECOND_LEVEL && (getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()) != null && cellVal.equals(getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()).getAlias()))) {
							JPopupMenu jPopupMenu_datasource = getWorkspaceManager().getDatasourcePopupMenu();
							jPopupMenu_datasource.show(jTable, e.getX(), e.getY());
						}
						//存在数据（需要进入对数据进行遍历，获得num，如何简化？）
						else if (levelNum == THIRD_LEVEL && getActiveApplication().getWorkspace().getDatasources().get(selectedDatasource.getAlias().toString()).getDatasets() != null) {//选中了数据集
							for (int num = 0; num < selectedDatasource.getDatasets().getCount(); num++) {
								if (cellVal.equals(selectedDatasource.getDatasets().get(num).getName())) {
									//根据数据类型赋予不同的右键菜单,待补充
									Object datasetType = jTable.getValueAt(rowIndex, 1);
									if (datasetType.equals(DatasetType.GRID)) {
										JPopupMenu jPopupMenu_dataset = getWorkspaceManager().getDatasetGridPopupMenu();
										jPopupMenu_dataset.show(jTable, e.getX(), e.getY());
										break;
									} else {
										JPopupMenu jPopupMenu_dataset = getWorkspaceManager().getDatasetVectorPopupMenu();
										jPopupMenu_dataset.show(jTable, e.getX(), e.getY());
										break;
									}
								}
							}
						}
						//存在地图（需要进入对地图进行遍历，获得num，如何简化？）
						else if (levelNum == SECOND_LEVEL && getActiveApplication().getWorkspace().getMaps().getCount() > 0) {
							for (int num = 0; num < getActiveApplication().getWorkspace().getMaps().getCount(); num++) {
								if (cellVal.equals(getActiveApplication().getWorkspace().getMaps().get(num))) {
									JPopupMenu jPopupMenu_map = getWorkspaceManager().getMapPopupMenu();
									jPopupMenu_map.show(jTable, e.getX(), e.getY());
									break;
								}
							}
						}
					}
				}

			}
		});
	}

	/**
	 * Jtable对Tree的监听，主要实现与tree的联动：节点展开与焦点获取
	 */
	private void jTableToTreeListeners() {
		this.jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
					//System.out.println("JTable,Pressed,BUTTON1||BUTTON3,jTableToTreeListeners");
					int rowIndex = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
					if (rowIndex >= 0) {
						//当只选中一条数据时，点击刷新高亮显示
						if (jTable.getSelectedRowCount() <= 1) {
							jTable.changeSelection(rowIndex, COLUMN_NAME, false, false);
						}
						Object cellVal = jTable.getValueAt(rowIndex, COLUMN_NAME);
						if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideDatasource"))) {
							DefaultMutableTreeNode datasourcesNode = getWorkspaceManager().getWorkspaceTree().getDatasourcesNode();
							selectedTreeNodePath = new TreePath(datasourcesNode.getPath());
							getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {
							DefaultMutableTreeNode mapsNode = getWorkspaceManager().getWorkspaceTree().getMapsNode();
							selectedTreeNodePath = new TreePath(mapsNode.getPath());
							getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideLayout"))) {//暂不实现任何功能
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {
							DefaultMutableTreeNode scenesNode = getWorkspaceManager().getWorkspaceTree().getScenesNode();
							selectedTreeNodePath = new TreePath(scenesNode.getPath());
							getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideResources"))) {
							DefaultMutableTreeNode resourcesNode = getWorkspaceManager().getWorkspaceTree().getResourcesNode();
							selectedTreeNodePath = new TreePath(resourcesNode.getPath());
							getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
						} else if (cellVal.equals(ControlsProperties.getString("SymbolMarkerLibNodeName"))
								|| cellVal.equals(ControlsProperties.getString("SymbolLineLibNodeName"))
								|| cellVal.equals(ControlsProperties.getString("SymbolFillLibNodeName"))
								) {

							if (cellVal.equals(ControlsProperties.getString("SymbolMarkerLibNodeName"))) {
								DefaultMutableTreeNode resourceMarkerNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getResourcesNode().getChildAt(RESOURCE_MARKER_NUM);
								selectedTreeNodePath = new TreePath(resourceMarkerNode.getPath());
							} else if (cellVal.equals(ControlsProperties.getString("SymbolLineLibNodeName"))) {
								DefaultMutableTreeNode resourceLineNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getResourcesNode().getChildAt(RESOURCE_LINE_NUM);
								selectedTreeNodePath = new TreePath(resourceLineNode.getPath());
							} else if (cellVal.equals(ControlsProperties.getString("SymbolFillLibNodeName"))) {
								DefaultMutableTreeNode resourceFillNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getResourcesNode().getChildAt(RESOURCE_FILL_NUM);
								selectedTreeNodePath = new TreePath(resourceFillNode.getPath());
							}
							getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
						}
						//点击了数据源
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
						else if (levelNum == SECOND_LEVEL && getActiveApplication().getWorkspace().getMaps().getCount() > 0) {
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
		});
	}

	/**
	 * 增加tree的监听事件，实现tree对“窗口”的操作
	 */
	private void addTreeToJtableListeners() {
		//给tree添加鼠标监听，不设valChanged，避免操作“窗口”给予tree获得焦点时的歧义
		this.getJtree = getWorkspaceManager().getWorkspaceTree();
		this.getJtree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//当点击tree时，同初始化JTable
				if (isWindowShown) {
					initJTable();
				}
			}
		});
	}

	/**
	 * 数据集查找textField改变监听
	 */
	private void textFieldSearchChangedListeners() {
		//this.textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
		this.textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}
		});
	}

	/**
	 * 手动改变列宽监听
	 */
	private void addColumnWithChangedListeners() {
		this.jTable.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				columnNameWidth = jTable.getColumnModel().getColumn(COLUMN_NAME).getPreferredWidth();
				columnTypeWidth = jTable.getColumnModel().getColumn(COLUMN_TYPE).getPreferredWidth();
				columnNumberWidth = jTable.getColumnModel().getColumn(COLUMN_NUMBER).getPreferredWidth();
				columnPathWidth = jTable.getColumnModel().getColumn(COLUMN_PRJCOORDSYS).getPreferredWidth();
				columnNullWidth = jTable.getColumnModel().getColumn(COLUMN_NULL).getPreferredWidth();
				isDefaultColWidth = false;
			}
		});
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
		this.sorter = new TableRowSorter<TableModel>(this.jTable.getModel());
		this.sorter.setRowFilter(rf);
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
			this.jTable.getColumnModel().getColumn(COLUMN_NULL).setPreferredWidth(800);
		} else {
			this.jTable.getColumnModel().getColumn(COLUMN_NAME).setPreferredWidth(this.columnNameWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_TYPE).setPreferredWidth(this.columnTypeWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_NUMBER).setPreferredWidth(this.columnNumberWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_PRJCOORDSYS).setPreferredWidth(this.columnPathWidth);
			this.jTable.getColumnModel().getColumn(COLUMN_NULL).setPreferredWidth(this.columnNullWidth);
		}
	}
}
