package com.supermap.desktop.ui;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.Interface.IFormMain;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.ui.controls.TreeNodeData;
import com.supermap.desktop.ui.controls.WorkspaceTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * 通用UI工具类
 */
public class UICommonToolkit {
	private static SmOptionPane smOptionPane;

	/**
	 * 根据已有的WorkspaceTree得到选择的节点
	 *
	 * @return
	 */
	private UICommonToolkit() {
		// 工具类不提供构造方法
	}

	public static TreeNodeData getTreeNodeData(WorkspaceTree workspaceTree) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) workspaceTree.getLastSelectedPathComponent();
		if (null != selectedNode) {
			TreeNodeData selectedNodeData = (TreeNodeData) selectedNode.getUserObject();
			if (null != selectedNodeData) {
				return selectedNodeData;
			}
		}
		return null;
	}

	/**
	 * 根据提供的WorkspaceTree得到选择的数据源
	 *
	 * @param workspaceTree
	 * @return
	 */
	public static Datasource getDatasource(WorkspaceTree workspaceTree) {
		// 以当前选中的数据源作为要导入数据集的对象
		TreeNodeData selectedNodeData = getTreeNodeData(workspaceTree);
		Datasource datasource = null;
		if (null != selectedNodeData && selectedNodeData.getData() instanceof Datasource) {
			datasource = (Datasource) selectedNodeData.getData();
		}
		return datasource;
	}

	/**
	 * 刷新指定的数据源
	 *
	 * @return
	 */
	public static void refreshSelectedDatasourceNode(String datasourceName) {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		for (int i = 0; i < datasourceTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			TreeNodeData selectedNodeData = (TreeNodeData) childDatasourceTreeNode.getUserObject();
			TreePath path = new TreePath(childDatasourceTreeNode.getPath());
			if (null != selectedNodeData && selectedNodeData.getData() instanceof Datasource) {
				Datasource datasource = (Datasource) selectedNodeData.getData();
				if (datasource.getAlias().equals(datasourceName)) {
					workspaceTree.refreshNode(childDatasourceTreeNode);
					UICommonToolkit.getWorkspaceManager().getWorkspaceTree().setSelectionPath(path);
					UICommonToolkit.getWorkspaceManager().getWorkspaceTree().expandPath(path);
					// 设置选中的数据源在可见范围内
					workspaceTree.scrollPathToVisible(path);
				}
			}
		}
	}

	/**
	 * 根据数据集刷新当前选中的数据源,并选中该数据集
	 *
	 * @return
	 */
	public static void refreshSelectedDatasetNode(Dataset dataset) {
		WorkspaceTree workspaceTree = UICommonToolkit.getWorkspaceManager().getWorkspaceTree();
		DefaultTreeModel treeModel = (DefaultTreeModel) workspaceTree.getModel();
		MutableTreeNode treeNode = (MutableTreeNode) treeModel.getRoot();
		MutableTreeNode datasourceTreeNode = (MutableTreeNode) treeNode.getChildAt(0);
		for (int i = 0; i < datasourceTreeNode.getChildCount(); i++) {
			DefaultMutableTreeNode childDatasourceTreeNode = (DefaultMutableTreeNode) datasourceTreeNode.getChildAt(i);
			TreeNodeData selectedDatasourceNodeData = (TreeNodeData) childDatasourceTreeNode.getUserObject();
			TreePath datasourcePath = new TreePath(childDatasourceTreeNode.getPath());
			if (null != selectedDatasourceNodeData && selectedDatasourceNodeData.getData() instanceof Datasource) {
				Datasource datasource = (Datasource) selectedDatasourceNodeData.getData();
				if (datasource == dataset.getDatasource()) {
					workspaceTree.refreshNode(childDatasourceTreeNode);
					workspaceTree.expandPath(datasourcePath);
					for (int j = 0; j < childDatasourceTreeNode.getChildCount(); j++) {
						DefaultMutableTreeNode childDatasetTreeNode = (DefaultMutableTreeNode) childDatasourceTreeNode.getChildAt(j);
						TreeNodeData selectedNodeData = (TreeNodeData) childDatasetTreeNode.getUserObject();
						TreePath datasetPath = new TreePath(childDatasetTreeNode.getPath());
						if (null != selectedNodeData && selectedNodeData.getData() instanceof Dataset) {
							Dataset selecteDataset = (Dataset) selectedNodeData.getData();
							if (selecteDataset == dataset) {
								// 设置数据集节点被选中
								UICommonToolkit.getWorkspaceManager().getWorkspaceTree().setSelectionPath(datasetPath);
								// 设置选中的数据源在可见范围内
								workspaceTree.scrollPathToVisible(datasetPath);
							}
						}
					}

				}
			}
		}
	}

	/**
	 * 获取工作空间管理器
	 *
	 * @return
	 */
	public static WorkspaceComponentManager getWorkspaceManager() {
		WorkspaceComponentManager workspaceManager = null;
		try {
			IFormMain formMain = Application.getActiveApplication().getMainFrame();
			IDockbar workspaceTreeDockbar = ((DockbarManager) formMain.getDockbarManager()).getWorkspaceComponentManager();
			if (workspaceTreeDockbar != null && workspaceTreeDockbar.getComponent() instanceof WorkspaceComponentManager) {
				workspaceManager = (WorkspaceComponentManager) workspaceTreeDockbar.getComponent();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return workspaceManager;
	}

	/**
	 * 获取图层管理器
	 *
	 * @return
	 */
	public static LayersComponentManager getLayersManager() {
		LayersComponentManager layersManager = null;
		try {
			IFormMain formMain = Application.getActiveApplication().getMainFrame();
			IDockbar layersTreeDockbar = ((DockbarManager) formMain.getDockbarManager()).getLayersComponentManager();
			if (layersTreeDockbar != null && layersTreeDockbar.getComponent() instanceof LayersComponentManager) {
				layersManager = (LayersComponentManager) layersTreeDockbar.getComponent();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return layersManager;
	}

	/**
	 * 判断名称是否合法
	 *
	 * @param name         要判断的名字
	 * @param isAllowSpace 是否允许空格
	 * @return
	 */
	public static boolean isLawName(String name, boolean isAllowSpace) {
		String nameTemp = name;
		boolean isLaw = true;
		try {
			char[] lawlessCode = new char[]{'/', '\\', '"', ':', '?', '*', '<', '>', '|'};

			if (!isAllowSpace) {
				nameTemp = nameTemp.trim();
			}

			if (nameTemp.length() > 0) {
				for (char code : lawlessCode) {
					if (nameTemp.contains(String.valueOf(code))) {
						isLaw = false;
						break;
					}
				}
			} else {
				isLaw = false;
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return isLaw;
	}

	public static void showMessageDialog(String message) {
		try {
			SmOptionPane smOptionPane = getSmOptionPane();
			smOptionPane.showMessageDialog(message);
//			Component parent = (Component) Application.getActiveApplication().getMainFrame();
//			JOptionPane.showMessageDialog(parent, message, CoreProperties.getString("String_MessageBox_Title"),
//					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}


	public static void showErrorMessageDialog(String message) {
		try {
			getSmOptionPane().showErrorDialog(message);
//			Component parent = (Component) Application.getActiveApplication().getMainFrame();
//			JOptionPane.showMessageDialog(parent, message, CommonProperties.getString("String_Error"), JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	/**
	 * @param message
	 * @return 0 -- 是；1 -- 否；-1 -- 直接关闭。
	 */
	public static int showConfirmDialog(String message) {
		int result = 0;
		try {
			result = getSmOptionPane().showConfirmDialog(message);
//			Component parent = (Component) Application.getActiveApplication().getMainFrame();
//			result = JOptionPane.showConfirmDialog(parent, message, CoreProperties.getString("String_MessageBox_Title"),
//					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * @param message
	 * @param title
	 * @return 0 -- 是；1 -- 否；-1 -- 直接关闭。
	 */
	public static int showConfirmDialog(String message, String title) {
		int result = 0;
		try {
			SmOptionPane smOptionPane = getSmOptionPane();
			String titleTemp = smOptionPane.getTitle();
			smOptionPane.setTitle(title);
			result = smOptionPane.showConfirmDialog(message);
			smOptionPane.setTitle(titleTemp);
//			Component parent = (Component) Application.getActiveApplication().getMainFrame();
//			result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		} catch (Exception ex) {
			if (smOptionPane != null) {
				smOptionPane.dispose();
			}
			smOptionPane = null;
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	public static int showConfirmDialogWithCancel(String message) {
		int result = 0;
		try {
			result = getSmOptionPane().showConfirmDialogWithCancle(message);
//			Component parent = (Component) Application.getActiveApplication().getMainFrame();
//			result = JOptionPane.showConfirmDialog(parent, message, CoreProperties.getString("String_MessageBox_Title"),
//					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	private static SmOptionPane getSmOptionPane() {
		return new SmOptionPane();
//		if (smOptionPane == null) {
//			smOptionPane = new SmOptionPane();
//		}
//		return smOptionPane;
	}
}
