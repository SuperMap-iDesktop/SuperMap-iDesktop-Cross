package com.supermap.desktop.WorkflowView;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.event.FormLoadedListener;
import com.supermap.desktop.ui.WorkspaceComponentManager;
import com.supermap.desktop.ui.controls.DockbarManager;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.EventObject;
import java.util.Vector;

/**
 * Created by highsad on 2017/7/28.
 */
public class WorkflowsManager implements WorkspaceClosingListener, WorkspaceOpenedListener {
	private final static String WORKFLOWS_MENU_NAME = "Supermap.Desktop.UI.WorkspaceControlManager.ContextMenuWorkflows";
	private final static String WORKFLOW_MENU_NAME = "Supermap.Desktop.UI.WorkspaceControlManager.ContextMenuWorkflow";
	private final static String NODE_ROOT_NAME = "Desktop";
	private final static String NODE_CROSS_NAME = "Cross";

	private DefaultMutableTreeNode workflowsTreeNode;
	private Vector<WorkflowTreeNodeData> workflowDatas = new Vector<>();
	private FormLoadedListener mainFrameLoadedListener = new FormLoadedListener() {
		@Override
		public void loadFinish(EventObject object) {
			initWorkspaceTree();
		}
	};
//	private Vector<String> workflowEntryNames = new Vector<>();
//	private Vector<String> workflowEntries = new Vector<>();

	public static WorkflowsManager INSTANCE;

	public static void init() {
		if (INSTANCE == null) {
			INSTANCE = new WorkflowsManager();
		}
	}

	public WorkflowsManager() {
		if (Application.getActiveApplication().getWorkspace() != null) {
			Application.getActiveApplication().getWorkspace().addClosingListener(this);
			Application.getActiveApplication().getWorkspace().addOpenedListener(this);
			Application.getActiveApplication().addFormLoadedListener(this.mainFrameLoadedListener);
			this.workflowsTreeNode = new DefaultMutableTreeNode("可视化建模");
		}
	}

	private void initWorkspaceTree() {
		// 添加可视化建模流程节点
		IDockbar workspaceDockbar = ((DockbarManager) Application.getActiveApplication().getMainFrame().getDockbarManager()).getWorkspaceComponentManager();
		if (workspaceDockbar == null) {
			return;
		}

		WorkspaceComponentManager workspaceComponentManager = (WorkspaceComponentManager) workspaceDockbar.getInnerComponent();
		DefaultMutableTreeNode workspaceTreeNode = workspaceComponentManager.getWorkspaceTree().getWorkspaceNode();

		if (!workspaceTreeNode.isNodeChild(this.workflowsTreeNode)) {
			DefaultMutableTreeNode sceneNode = workspaceComponentManager.getWorkspaceTree().getScenesNode();
//			workspaceTreeNode.insert(this.workflowsTreeNode, workspaceTreeNode.getIndex(sceneNode) + 1);
			workspaceTreeNode.add(this.workflowsTreeNode);
			workspaceComponentManager.getWorkspaceTree().updateUI();
		}
	}

	@Override
	public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {
		// 关闭工作空间前，重置 workflows 节点
		this.workflowsTreeNode.removeAllChildren();
	}

	@Override
	public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {

		// 重置 workflows 节点
		this.workflowsTreeNode.removeAllChildren();

		// 获取工作空间里的 Cross 数据存储节点
		Element crossElement = validateCrossInfo(workspaceOpenedEvent.getWorkspace());

		// 获取 workflows 节点并解析
		Element workflowsElement = XmlUtilities.getChildElementNodeByName(crossElement, "Workflows");
		Element[] workflowElements = XmlUtilities.getChildElementNodesByName(workflowsElement, "WorkflowEntry");
		if (workflowElements != null && workflowElements.length > 0) {
			for (int i = 0; i < workflowElements.length; i++) {
				Element workflowEntry = workflowElements[i];
				String workflowName = workflowEntry.getAttribute("Name");
				this.workflowDatas.add(new WorkflowTreeNodeData(workflowName, XmlUtilities.nodeToString(workflowEntry)));
			}
		}

		// 构造工作空间树节点
		for (int i = 0; i < this.workflowDatas.size(); i++) {
			DefaultMutableTreeNode workflowTreeNode = new DefaultMutableTreeNode(this.workflowDatas.get(i));
			this.workflowsTreeNode.add(workflowTreeNode);
		}
	}

	public void saveToWorkspace(FormWorkflow formWorkflow) {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		Element crossNode = validateCrossInfo(workspace);

		String workflowName = formWorkflow.getName();
		String workflowEntry = formWorkflow.serializeTo();
		WorkflowTreeNodeData treeNodeData = new WorkflowTreeNodeData(formWorkflow.getName(), formWorkflow.serializeTo());
		this.workflowDatas.add(treeNodeData);
		this.workflowsTreeNode.add(new DefaultMutableTreeNode(treeNodeData));
	}

	/**
	 * 检查 CrossInfo 信息，并返回 Cross 节点
	 *
	 * @param workspace
	 * @return
	 */
	private Element validateCrossInfo(Workspace workspace) {
		if (workspace == null) {
			throw new NullPointerException();
		}

		Document document = XmlUtilities.stringToDocument(workspace.getDesktopInfo());
		if (document == null) {
			document = XmlUtilities.getEmptyDocument();
		}

		Element desktopNode = XmlUtilities.getChildElementNodeByName(document, NODE_ROOT_NAME);
		if (desktopNode == null) {
			desktopNode = document.createElement(NODE_ROOT_NAME);
			document.appendChild(desktopNode);
		}

		Element crossNode = XmlUtilities.getChildElementNodeByName(desktopNode, NODE_CROSS_NAME);
		if (crossNode == null) {
			crossNode = document.createElement(NODE_CROSS_NAME);
			desktopNode.appendChild(crossNode);
		}
		return crossNode;
	}
}
