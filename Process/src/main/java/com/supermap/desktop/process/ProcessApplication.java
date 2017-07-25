package com.supermap.desktop.process;

import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Vector;

/**
 * 存储到工作空间里以如下固定格式存储
 * <Desktop>
 * <NET></NET>
 * <Cross></Cross>
 * </Desktop>
 * Created by highsad on 2017/7/19.
 */
public class ProcessApplication implements WorkspaceClosingListener, WorkspaceOpenedListener {
	private static String NODE_ROOT_NAME = "Desktop";
	private static String NODE_CROSS_NAME = "Cross";

	public static ProcessApplication INSTANCE;

	private Vector<Workflow> workflows = new Vector<>();

	public static ProcessApplication getProcessApplication() {
		return INSTANCE;
	}

	public static void init() {
		INSTANCE = new ProcessApplication();
	}

	public ProcessApplication() {
		if (Application.getActiveApplication().getWorkspace() != null) {
			Application.getActiveApplication().getWorkspace().addClosingListener(this);
			Application.getActiveApplication().getWorkspace().addOpenedListener(this);
		}
	}

	private Workflow[] getWorkflows() {
		return this.workflows.toArray(new Workflow[this.workflows.size()]);
	}


	@Override
	public void workspaceClosing(WorkspaceClosingEvent workspaceClosingEvent) {

	}

	@Override
	public void workspaceOpened(WorkspaceOpenedEvent workspaceOpenedEvent) {

	}

	private void saveToWorkspace(Workflow workflow) {
		Workspace workspace = Application.getActiveApplication().getWorkspace();
		Element crossNode = initCrossInfo(workspace);

//		workspace.setDesktopInfo(XmlUtilities.nodeToString(desktopNode, "UTF-8"));
	}

	private Element initCrossInfo(Workspace workspace) {
		if (workspace == null) {
			throw new NullPointerException();
		}

		Document document = XmlUtilities.stringToDocument(workspace.getDesktopInfo());
		if (document == null) {
			document = XmlUtilities.getEmptyDocument();
		}

		Element desktopNode = (Element) XmlUtilities.getChildElementNodeByName(document, NODE_ROOT_NAME);
		if (desktopNode == null) {
			desktopNode = document.createElement(NODE_ROOT_NAME);
			document.appendChild(desktopNode);
		}

		Element crossNode = document.createElement(NODE_CROSS_NAME);
		desktopNode.appendChild(crossNode);
		return crossNode;
	}
}
