package com.supermap.desktop.ui;

import com.supermap.data.Dataset;
import com.supermap.data.Datasets;
import com.supermap.data.Datasources;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.CtrlAction.WorkspaceRecovery;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IDockbarManager;
import com.supermap.desktop.Interface.IFormLayout;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IFormScene;
import com.supermap.desktop.Interface.IFrameMenuManager;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.Interface.IStatusbarManager;
import com.supermap.desktop.Interface.IToolbarManager;
import com.supermap.desktop.WorkEnvironment;
import com.supermap.desktop.controls.utilities.MapViewUIUtilities;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.FormLoadedListener;
import com.supermap.desktop.ui.controls.Dockbar;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.NodeDataType;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;
import com.supermap.desktop.utilities.DatasetUtilities;
import com.supermap.desktop.utilities.DatasourceUtilities;
import com.supermap.desktop.utilities.RecentFileUtilties;
import com.supermap.desktop.utilities.WorkspaceUtilities;
import com.supermap.layout.MapLayout;
import com.supermap.realspace.Scene;
import org.flexdock.docking.DockingManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class FormBase extends JFrame implements IFormMain {

	/**
	 *
	 */
	private DropTarget dropTargetTemp;

	private static final long serialVersionUID = 6966819038480336218L;
	private transient JMenuBar jMenuBarMain = null;
	private transient FormManager formManager = null;
	private transient FrameMenuManager frameMenuManager = null;
	private transient ContextMenuManager contextMenuManager = null;
	private transient ToolbarManager toolbarManager = null;
	private transient DockbarManager dockbarManager = null;
	private transient StatusbarManager statusbarManager = null;
	// private transient IPropertyManager propertyManager = null;
	private int defaultType = -1;
	private int workspaceType = 0;
	private int datasourceType = 1;
	private ArrayList<FormLoadedListener> formLoadedListeners = new ArrayList<>();

	public FormBase() {
		DockingManager.setApplicationWindow(this);
		this.formManager = new FormManager();
		this.frameMenuManager = new FrameMenuManager();
		this.contextMenuManager = new ContextMenuManager();
		this.toolbarManager = new ToolbarManager();
		this.dockbarManager = new DockbarManager(this.formManager);
		this.statusbarManager = new StatusbarManager();
		this.jMenuBarMain = new JMenuBar();
//        this.propertyManager = new JDialogDataPropertyContainer(this);

		JMenu menu = new JMenu("loading");
		this.jMenuBarMain.add(menu);
		jMenuBarMain.setMinimumSize(new Dimension(20, 23));
		jMenuBarMain.setPreferredSize(new Dimension(20, 23));
//		this.setJMenuBar(this.jMenuBarMain);

		this.addWindowListener(new FormBaseListener());
		initDrag();
	}

	@Override
	public IFormManager getFormManager() {
		return this.formManager;
	}

	@Override
	public IFrameMenuManager getFrameMenuManager() {
		return this.frameMenuManager;
	}

	@Override
	public IContextMenuManager getContextMenuManager() {
		return this.contextMenuManager;
	}

	@Override
	public IToolbarManager getToolbarManager() {
		return this.toolbarManager;
	}

	@Override
	public IDockbarManager getDockbarManager() {
		return this.dockbarManager;
	}

	@Override
	public IStatusbarManager getStatusbarManager() {
		return this.statusbarManager;
	}

	@Override
	public String getText() {
		return this.getTitle();
	}

	@Override
	public void setText(String text) {
		this.setTitle(text);
	}

	@Override
	public void loadUI() {
		try {
			// 首先融合UI信息
			WorkEnvironment workEnvironment = Application.getActiveApplication().getWorkEnvironmentManager().getActiveWorkEnvironment();

			workEnvironment.mergeUIElements();
			this.loadFrameMenu(workEnvironment);

			// 初始化最近文件列表
			RecentFileUtilties.initRecentFileMenu(RecentFileUtilties.FILE_TYPE_WORKSPACE);
			RecentFileUtilties.initRecentFileMenu(RecentFileUtilties.FILE_TYPE_DATASOURCE);

			initLayout();
			this.toolbarManager.load(workEnvironment);
			this.contextMenuManager.load(workEnvironment);
			this.statusbarManager.load(workEnvironment);
			this.dockbarManager.load(workEnvironment);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

			IDockbar outputDockbar = this.dockbarManager.getOutputFrame();
			if (outputDockbar != null && outputDockbar.getInnerComponent() instanceof OutputFrame) {
				Application.getActiveApplication().setOutput((OutputFrame) outputDockbar.getInnerComponent());
			}
			ToolbarUIUtilities.updataToolbarsState();

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					fireFormLoadedEvent();
					if (GlobalParameters.isWorkspaceRecovery()) {
						WorkspaceRecovery.getInstance().run();
					}
				}
			});

		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void fireFormLoadedEvent() {
		EventObject eventObject = new EventObject(this);
		for (int i = formLoadedListeners.size() - 1; i >= 0; i--) {
			formLoadedListeners.get(i).loadFinish(eventObject);
		}
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(jMenuBarMain, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER));
		this.add(this.toolbarManager.getToolbarsContainer(), new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 0, 0, 5));
		this.add(this.dockbarManager.getDockPort(), new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER));
	}

	@Override
	public IPropertyManager getPropertyManager() {
		IPropertyManager iPropertyManager = null;
		try {
			Dockbar dockbar = (Dockbar) getDockbarManager().get(Class.forName("com.supermap.desktop.controls.property.DataPropertyContainer"));
			iPropertyManager = (IPropertyManager) dockbar.getInnerComponent();
		} catch (ClassNotFoundException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return iPropertyManager;
	}

	private void loadFrameMenu(WorkEnvironment workEnvironment) {
		try {
			this.frameMenuManager.setMenuBar(this.jMenuBarMain);
			this.frameMenuManager.loadMainMenu(workEnvironment);

			// 这里必须要调用这段，刷新菜单项，否则新添加的菜单不可见
			JMenu menu = this.jMenuBarMain.getMenu(0);
			if ("loading".equals(menu.getText())) {
				this.jMenuBarMain.remove(0);
			}
			// end

//			this.setJMenuBar(this.jMenuBarMain);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	private void formBase_windowClosing() {
		this.toolbarManager.saveChange();
		Application.getActiveApplication().getWorkEnvironmentManager().getActiveWorkEnvironment().toXML();
	}

	private class FormBaseListener implements WindowListener {
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO 目前默认实现，后续会增加一些初始化操作
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO 目前默认实现，后续会增加一些初始化操作
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO 目前默认实现，后续会增加一些初始化操作
		}

		@Override
		public void windowClosing(WindowEvent e) {
			formBase_windowClosing();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO 目前默认实现，后续会增加一些初始化操作
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO 目前默认实现，后续会增加一些初始化操作
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

	/**
	 * 拖动实现打开文件型工作空间或者打开地图
	 */
	private void initDrag() {
		this.dropTargetTemp = new DropTarget(this, new WorkspaceTreeDropTargetAdapter());
	}

	@Override
	public DropTarget getDropTarget() {
		return this.dropTargetTemp;
	}

	/**
	 * 用于提供所涉及的 DropTarget 的 DnD 操作的通知
	 *
	 * @author xie
	 */
	private class WorkspaceTreeDropTargetAdapter extends DropTargetAdapter {
		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
				{
					dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);// 接收拖拽来的数据
					@SuppressWarnings("unchecked")
					List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
					for (File file : list) {
						if (workspaceType == getFileType(file)) {
							// 关闭单前可能已经打开的地图和图层
							Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
							for (int i = 0; i < datasources.getCount(); i++) {
								Datasets datasets = datasources.get(i).getDatasets();
								DatasetUtilities.closeDataset(datasets);
							}
							// 关闭当前工作空间
							WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo(file.getAbsolutePath());
							WorkspaceUtilities.openWorkspace(connectionInfo, false);
						}
						// 打开数据源类型的文件
						if (datasourceType == getFileType(file)) {
							DatasourceUtilities.openFileDatasource(file.getAbsolutePath(), null, false, true);
						}
					}
					dtde.dropComplete(true);// 指示拖拽操作已完成
				} else {
					Transferable transferable = dtde.getTransferable();
					DataFlavor[] dataFlavors = dtde.getCurrentDataFlavors();
					for (int i = 0; i < dataFlavors.length; i++) {
						// 拖拽的类型为空或者拖拽的源数据不为空时才能拖拽
						if (null != dataFlavors[i] && null != transferable.getTransferData(dataFlavors[i])) {
							Dataset[] datasets = Application.getActiveApplication().getActiveDatasets();
							if (0 < datasets.length) {
								if (Application.getActiveApplication().getActiveForm() == null || Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
									MapViewUIUtilities.addDatasetsToNewWindow(datasets, true);
								}
							} else {
								WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
								DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getSelectionPath().getLastPathComponent();
								TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
								if (null != selectedNodeData.getData() && selectedNodeData.getData() instanceof String) {
									String name = (String) selectedNodeData.getData();
									if (selectedNodeData.getType() == NodeDataType.MAP_NAME) {
										// 节点对应的数据为地图，直接拖拽打开地图
										MapViewUIUtilities.openMap(name);
									} else if (selectedNodeData.getType() == NodeDataType.SCENE_NAME) {
										// 节点对应的数据为场景，直接拖拽打开场景
										openScence(name);
									}
									if (selectedNodeData.getType() == NodeDataType.LAYOUT_NAME) {
										// 节点对应的数据为布局，直接拖拽打开布局
										openLayout(name);
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

		private void openLayout(String name) {
			IFormLayout formLayout = (IFormLayout) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.LAYOUT, name);
			if (formLayout != null) {
				MapLayout mapLayout = formLayout.getMapLayoutControl().getMapLayout();
				mapLayout.open(name);
				mapLayout.refresh();
				UICommonToolkit.getLayersManager().setMap(null);
				UICommonToolkit.getLayersManager().setScene(null);
			}
		}

		private void openScence(String name) {
			IFormScene formScene = (IFormScene) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.SCENE, name);
			if (formScene != null) {
				Scene scene = formScene.getSceneControl().getScene();
				formScene.setWorkspace(Application.getActiveApplication().getWorkspace());
				scene.open(name);
				scene.refresh();
				UICommonToolkit.getLayersManager().setScene(scene);
			}
		}
	}

	@Override
	public void addFormLoadedListener(FormLoadedListener formLoadedListener) {
		formLoadedListeners.add(formLoadedListener);
	}

	@Override
	public void removeFormLoadedListener(FormLoadedListener formLoadedListener) {
		formLoadedListeners.remove(formLoadedListener);


	}
}
