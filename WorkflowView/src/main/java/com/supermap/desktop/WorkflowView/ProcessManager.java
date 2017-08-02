package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.WorkflowView.ui.ProcessTree;
import com.supermap.desktop.WorkflowView.ui.ProcessTreeNode;
import com.supermap.desktop.WorkflowView.ui.ProcessTreeNodeBean;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilities.PathUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessManager extends JPanel {

	private static final String PROCESS_FILE_PATH = "../Templates/process/process.xml";
	public ProcessTree processTree;
	public JScrollPane jScrollPane;

	private Vector<IProcessLoader> loaders = new Vector<>();
	private Vector<IProcess> processes = new Vector<>();
	public static final ProcessManager SINGLETON = new ProcessManager();

	public ProcessManager() {
		initComponents();
		initLayout();
//		registerProcessLoader(DefaultProcessLoader.SINGLETON);

	}

	private void initComponents() {
		ProcessTreeNodeBean processGroup = initProcessGroup();
		this.processTree = new ProcessTree(new ProcessTreeNode(null, processGroup));
		this.jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(processTree);
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}

	private ProcessTreeNodeBean initProcessGroup() {
		String processXmlPath = PathUtilities.getFullPathName(PROCESS_FILE_PATH, false);
		Document document = XmlUtilities.getDocument(processXmlPath);
		Node root = document.getChildNodes().item(0);
		Node processNode = XmlUtilities.getChildElementNodeByName(root, "process");
		return initProcessTreeNodeBean(processNode);
	}

	private ProcessTreeNodeBean initProcessTreeNodeBean(Node node) {
		ProcessTreeNodeBean processTreeNodeBeanChild = new ProcessTreeNodeBean();
		processTreeNodeBeanChild.setName(((Element) node).getAttribute("name"));
		processTreeNodeBeanChild.setKey(((Element) node).getAttribute("key"));
		processTreeNodeBeanChild.setIconPath(((Element) node).getAttribute("iconPath"));

		Element[] processes = XmlUtilities.getChildElementNodesByName(node, "process");
		for (Element process : processes) {
			processTreeNodeBeanChild.addChild(initProcessTreeNodeBean(process));
		}
		return processTreeNodeBeanChild;
	}


	public void registerProcessLoader(IProcessLoader loader) {
		this.loaders.add(loader);
//        addProcesses(loader.loadProcesses());
	}

	public void addProcesses(IProcess... processes) {
		if (processes != null && processes.length > 0) {
			Collections.addAll(this.processes, processes);
		}
	}
}
