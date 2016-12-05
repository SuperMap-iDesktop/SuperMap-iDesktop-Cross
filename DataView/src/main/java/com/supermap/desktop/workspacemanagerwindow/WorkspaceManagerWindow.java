package com.supermap.desktop.workspacemanagerwindow;

import com.sun.corba.se.impl.copyobject.FallbackObjectCopierImpl;
import com.supermap.analyst.spatialanalyst.TopologicalHierarchicalSchema;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
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
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceClosedEvent;
import com.supermap.data.WorkspaceClosedListener;
import com.supermap.data.WorkspaceClosingEvent;
import com.supermap.data.WorkspaceClosingListener;
import com.supermap.data.WorkspaceOpenedEvent;
import com.supermap.data.WorkspaceOpenedListener;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.dataview.DataViewResources;
import com.supermap.desktop.event.FormClosingEvent;
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
import java.awt.event.WindowListener;

import static com.supermap.desktop.Application.getActiveApplication;
import static com.supermap.desktop.ui.UICommonToolkit.getWorkspaceManager;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NAME;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.COLUMN_NUMBER;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.FIRST_LEVEL;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.SECOND_LEVEL;
import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.THIRD_LEVEL;
import static java.awt.SystemColor.window;

/**
 * @author YuanR 2016.12.3
 *         12.3存疑
 *         当关闭窗口时，窗口只是被隐藏，此时就存在问题，当窗口被“关闭”时，应该断开窗口与其他空间的联系，
 *         当打开或关闭工作空间时，其窗口也是被隐藏了，应该保持窗口的存在，
 *         即是关闭就是隐藏窗口，那也要手动切断联系，，jtable=null，即可。可是如何设置呢？
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
	private DefaultMutableTreeNode workspaceNode;
	private DefaultMutableTreeNode datasourcesNode;
	private DefaultMutableTreeNode mapsNode;
	private DefaultMutableTreeNode scenesNode;
	private DefaultMutableTreeNode resourcesNode;
	private DefaultMutableTreeNode selsectedDatasourceTreeNode;
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

	private static final int COMBOXITEM_ALLDATASTYPE = 0;
	private static final int RESOURCE_MARKER_NUM = 0;
	private static final int RESOURCE_LINE_NUM = 1;
	private static final int RESOURCE_FILL_NUM = 2;


	/**
	 * @param title
	 * @param icon
	 * @param component 默认构造函数
	 */
	private WorkspaceManagerWindow(String title, Icon icon, Component component) {
		super(title, icon, component);
		initComponents();
		initLayout();
		initListeners();
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
		this.jButtonLastLevel.setIcon(DataViewResources.getIcon("/dataviewresources/workspacemanagerwindow/Reset.png"));
		this.jButtonLastLevel.setEnabled(false);
		//刷新
		/*
		this.jButtonRefresh = new JButton();
		this.jButtonRefresh.setIcon(DataViewResources.getIcon("/dataviewresources/workspacemanagerwindow/Refresh.png"));
		*/
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
		if (selectedNode == null || jTable.getModel() == null) {
			jTable.setModel(new GetTableModel().getWorkspaceTableModel(getActiveApplication().getWorkspace()));
			jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
			TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
			column.setCellRenderer(render);
			this.levelNum = FIRST_LEVEL;
		}

		//取消网格
		this.jTable.setShowHorizontalLines(false);
		this.jTable.setShowVerticalLines(false);
		this.jTable.setAutoCreateRowSorter(true);
		this.jTable.repaint();

	}

	/**
	 * 初始化jtable
	 */
	private void initJTable() {
		if (jTable != null) {
			//System.out.println("要进行jtable的初始化，jtable！=null");
			selectedNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();//返回最后选定的节点
			//当未选中tree节点时。默认选中workspace节点
			if (null != selectedNode) {
				TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
				if (null == selectedNodeData) {
					return;
				}
				jButtonLastLevel.setEnabled(true);
				textFieldSearch.setText("");
				textFieldSearch.setEnabled(true);
				selectedDatasource = null;
				datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
				datasetTypeComboBox.setEnabled(false);
				if (selectedNodeData.getData() instanceof Workspace) {//当点击tree_workspace节点
					jTable.setModel(new GetTableModel().getWorkspaceTableModel(getActiveApplication().getWorkspace()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());

					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);

					textFieldSearch.setEnabled(false);
					jButtonLastLevel.setEnabled(false);
					levelNum = FIRST_LEVEL;
				} else if (selectedNodeData.getData() instanceof Datasources) {//当点击tree_datasources节点
					jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
					//设置渲染器
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());

					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
					levelNum = SECOND_LEVEL;
				} else if (selectedNodeData.getData() instanceof Maps) {
					//设置model为地图
					jTable.setModel(new GetTableModel().getMapsTableModel(getActiveApplication().getWorkspace().getMaps()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererMaps());
					levelNum = SECOND_LEVEL;
				} else if (selectedNodeData.getData() instanceof Scenes) {
					//设置model为场景
					jTable.setModel(new GetTableModel().getScenesTableModel(getActiveApplication().getWorkspace().getScenes()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererScenes());
					levelNum = SECOND_LEVEL;
				} else if (selectedNodeData.getData() instanceof Layouts) {//暂不实现此方法
				} else if (selectedNodeData.getData() instanceof Resources) {
					jTable.setModel(new GetTableModel().getResourcesTableModel());
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererResources());
					textFieldSearch.setEnabled(false);
					levelNum = SECOND_LEVEL;
				} else if (selectedNodeData.getData() instanceof Datasource) {
					//设置model为数据集
					selectedDatasource = (Datasource) selectedNodeData.getData();
					selsectedDatasourceTreeNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getLastSelectedPathComponent();
					jTable.setModel(new GetTableModel().getDatasourceTableModel(selectedDatasource));
					//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Datasource());
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());

					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);

					datasetTypeComboBox.getSelectedIndex();
					datasetTypeComboBox.setEnabled(true);
					levelNum = THIRD_LEVEL;
				}
			}

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
				if (jTable != null) {
					//System.out.println("关闭工作空间");
					//当工作空间关闭时，保持窗口存在，层级返回起始页
					jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
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
				if (jTable != null) {
					// 展示起始页
					//System.out.println("打开工作空间");
					jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());
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
				if (jTable != null) {
					refresh();
				}
			}
		});
		//数据源关闭
		this.getDatasources.addClosedListener(new DatasourceClosedListener() {
			@Override
			public void datasourceClosed(DatasourceClosedEvent datasourceClosedEvent) {
				if (jTable != null) {
					//当关闭数据源时，跳转到数据源文件界面
					jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());

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
				if (jTable != null) {
					refresh();
				}
			}
		});
		//数据源别名被修改
		this.getDatasources.addAliasModifiedListener(new DatasourceAliasModifiedListener() {
			@Override
			public void datasourceAliasModified(DatasourceAliasModifiedEvent datasourceAliasModifiedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
		//地图名称被改变
		this.getMaps = getWorkspaceManager().getWorkspace().getMaps();
		this.getMaps.addRenamedListener(new MapRenamedListener() {
			@Override
			public void mapRenamed(MapRenamedEvent mapRenamedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
		//增加地图
		this.getMaps.addAddedListener(new MapAddedListener() {
			@Override
			public void mapAdded(MapAddedEvent mapAddedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
		//删除地图
		this.getMaps.addClearedListener(new MapClearedListener() {
			@Override
			public void mapCleared(MapClearedEvent mapClearedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
		//增加场景
		this.getScenes = getWorkspaceManager().getWorkspace().getScenes();
		this.getScenes.addAddedListener(new SceneAddedListener() {
			@Override
			public void sceneAdded(SceneAddedEvent sceneAddedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
		//创建场景
		this.getScenes.addClearedListener(new SceneClearedListener() {
			@Override
			public void sceneCleared(SceneClearedEvent sceneClearedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
		//重命名场景
		this.getScenes.addRenamedListener(new SceneRenamedListener() {
			@Override
			public void sceneRenamed(SceneRenamedEvent sceneRenamedEvent) {
				if (jTable != null) {
					refresh();
				}
			}
		});
	}

	@Override
	public void formClosing(FormClosingEvent e) {
		//移除所有监听
		//System.out.println("手动关闭工作空间");
		jTable.removeAll();
		jTable = null;
		textFieldSearch.removeAll();
		textFieldSearch = null;
		datasetTypeComboBox.removeAll();
		datasetTypeComboBox = null;
		jButtonLastLevel.removeAll();
		jButtonLastLevel = null;

		//当窗口关闭的时候，设置其为空，（尝试一下）
		//下列操作多余？？
		IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
		formManager.close(WorkspaceManagerWindow.this);
	}

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
	 * 返回上一级按钮监听
	 * 判断所处层级，进行返回上一级操作（给予new model）
	 */
	private void jButtonLastLevelListeners() {
		this.jButtonLastLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (levelNum == SECOND_LEVEL) {//此时处于第二层，需要返回起始页
					jTable.setModel(new GetTableModel().getWorkspaceTableModel((getActiveApplication().getWorkspace())));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererWorkspace());

					jTable.repaint();
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
					datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
					datasetTypeComboBox.setEnabled(false);

					textFieldSearch.setText("");
					textFieldSearch.setEnabled(false);
					selectedDatasource = null;
					jButtonLastLevel.setEnabled(false);
					levelNum = FIRST_LEVEL;
				} else if (levelNum == THIRD_LEVEL) {//此时处于第三层（数据集层），需要返回数据源文件层
					jTable.setModel(new GetTableModel().getDatasourcesTableModel(getActiveApplication().getWorkspace().getDatasources()));
					jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasources());

					jTable.repaint();
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
					datasetTypeComboBox.setSelectedIndex(COMBOXITEM_ALLDATASTYPE);
					datasetTypeComboBox.setEnabled(false);

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
					jTable.repaint();
					TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
					column.setCellRenderer(render);
				} else {//不做任何实现
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
							TableColumn column = jTable.getColumnModel().getColumn(COLUMN_NUMBER);//获取某一列名字
							column.setCellRenderer(render);

							textFieldSearch.setEnabled(true);
							levelNum = SECOND_LEVEL;

						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {//地图
							jTable.setModel(new GetTableModel().getMapsTableModel(getActiveApplication().getWorkspace().getMaps()));
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Maps());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererMaps());
							textFieldSearch.setEnabled(true);
							levelNum = SECOND_LEVEL;

						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideLayout"))) {//暂不实现此方法，隐藏布局列
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {//场景
							jTable.setModel(new GetTableModel().getScenesTableModel(getActiveApplication().getWorkspace().getScenes()));
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Scenes());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererScenes());
							textFieldSearch.setEnabled(true);
							levelNum = SECOND_LEVEL;
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideResources"))) {//资源
							jTable.setModel(new GetTableModel().getResourcesTableModel());
							//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Resources());
							jTable.setDefaultRenderer(Icon.class, new TableCellRendererResources());
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
									//jTable.setDefaultRenderer(Integer.class, new TableCellRenderer_Datasource());
									jTable.setDefaultRenderer(Icon.class, new TableCellRendererDatasource());

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
									//较为累赘，先回去数据集地址，待优化：单元格中存在的直接为对象，直接打开
									//Dataset aimdataset = new FindDatasetbyName().getDatasetPath(getActiveApplication().getWorkspace(), cellVal.toString());

									//打来数据集到mapcontorl，默认以新建mapcontorl的方式打开
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
			public void mousePressed(MouseEvent e) {
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
							datasourcesNode = getWorkspaceManager().getWorkspaceTree().getDatasourcesNode();
							selectedTreeNodePath = new TreePath(datasourcesNode.getPath());
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideMap"))) {
							mapsNode = getWorkspaceManager().getWorkspaceTree().getMapsNode();
							selectedTreeNodePath = new TreePath(mapsNode.getPath());
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideLayout"))) {//暂不实现任何功能
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideScene"))) {
							scenesNode = getWorkspaceManager().getWorkspaceTree().getScenesNode();
							selectedTreeNodePath = new TreePath(scenesNode.getPath());
						} else if (cellVal.equals(ControlsProperties.getString("String_ToolBar_HideResources"))) {
							resourcesNode = getWorkspaceManager().getWorkspaceTree().getResourcesNode();
							selectedTreeNodePath = new TreePath(resourcesNode.getPath());
						} else if (cellVal.equals(ControlsProperties.getString("SymbolMarkerLibNodeName"))
								|| cellVal.equals(ControlsProperties.getString("SymbolLineLibNodeName"))
								|| cellVal.equals(ControlsProperties.getString("SymbolFillLibNodeName"))
								) {
							//DefaultMutableTreeNode resourceNode;
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
						}
						//点击了数据源
						else if (getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()) != null && cellVal.equals(getActiveApplication().getWorkspace().getDatasources().get(cellVal.toString()).getAlias()) && levelNum == SECOND_LEVEL) {
							//获得数据源文件节点
							for (int i = 0; i < getWorkspaceManager().getWorkspace().getDatasources().getCount(); i++) {
								if ((getWorkspaceManager().getWorkspace().getDatasources().get(i).getAlias()).equals(cellVal)) {
									//展开数据源文件节点
									datasourcesNode = getWorkspaceManager().getWorkspaceTree().getDatasourcesNode();
									selectedTreeNodePath = new TreePath(datasourcesNode.getPath());
									getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
									//设置选中数据源
									selsectedDatasourceTreeNode = (DefaultMutableTreeNode) getWorkspaceManager().getWorkspaceTree().getDatasourcesNode().getChildAt(i);
									selectedTreeNodePath = new TreePath(selsectedDatasourceTreeNode.getPath());
									break;
								}
							}
						}
						//点击了数据集
						else if (levelNum == THIRD_LEVEL && getActiveApplication().getWorkspace().getDatasources().get(selectedDatasource.getAlias().toString()).getDatasets() != null) {
							for (int num = 0; num < selectedDatasource.getDatasets().getCount(); num++) {
								if (cellVal.equals(selectedDatasource.getDatasets().get(num).getName())) {
									//展开数据源节点
									selectedTreeNodePath = new TreePath(selsectedDatasourceTreeNode.getPath());
									getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
									//选中数据集节点数据
									DefaultMutableTreeNode selectedDatasetNode = (DefaultMutableTreeNode) selsectedDatasourceTreeNode.getChildAt(num);
									selectedTreeNodePath = new TreePath(selectedDatasetNode.getPath());
								}
							}
						} else if (levelNum == SECOND_LEVEL && getActiveApplication().getWorkspace().getMaps().getCount() > 0) {//存在地图
							for (int num = 0; num < getActiveApplication().getWorkspace().getMaps().getCount(); num++) {
								if (cellVal.equals(getActiveApplication().getWorkspace().getMaps().get(num))) {
									//设置联动及焦点获取
									//展开地图节点
									mapsNode = getWorkspaceManager().getWorkspaceTree().getMapsNode();
									selectedTreeNodePath = new TreePath(mapsNode.getPath());
									getWorkspaceManager().getWorkspaceTree().expandPath(selectedTreeNodePath);
									//设置选中节点为地图
									DefaultMutableTreeNode selectedMapNode = (DefaultMutableTreeNode) mapsNode.getChildAt(num);
									selectedTreeNodePath = new TreePath(selectedMapNode.getPath());
									break;
								}
							}
						}
						//设置选中节点对应tree高亮显示
						if (jTable.getSelectedRowCount() > 1) {
							//暂时只实现单个点击进行多选
							getWorkspaceManager().getWorkspaceTree().addSelectionPath(selectedTreeNodePath);
						} else {
							getWorkspaceManager().getWorkspaceTree().setSelectionPath(selectedTreeNodePath);
						}
						getWorkspaceManager().getWorkspaceTree().scrollPathToVisible(selectedTreeNodePath);
						//获得工作空间节点
						workspaceNode = getWorkspaceManager().getWorkspaceTree().getWorkspaceNode();
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
				initJTable();
			}
		});
	}

	/**
	 * 数据集查找textField改变监听
	 */
	private void textFieldSearchChangedListeners() {
		this.textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}

			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}

			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}
		});
	}

	/**
	 *
	 */
	private void newFilter() {
		RowFilter<TableModel, Object> rf = null;
		try {
			rf = RowFilter.regexFilter(this.textFieldSearch.getText());
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		this.sorter = new TableRowSorter<TableModel>(this.jTable.getModel());
		this.sorter.setRowFilter(rf);
		this.jTable.setRowSorter(sorter);
		this.jTable.repaint();
	}
}
