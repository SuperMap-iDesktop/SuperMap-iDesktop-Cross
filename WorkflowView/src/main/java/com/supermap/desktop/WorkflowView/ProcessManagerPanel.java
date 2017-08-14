package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.WorkflowView.ui.ProcessTree;
import com.supermap.desktop.process.ProcessManager;
import com.supermap.desktop.process.loader.IProcessGroup;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessManagerPanel extends JPanel {

	public ProcessTree processTree;
	public JScrollPane jScrollPane;

	public ProcessManagerPanel() {
		initComponents();
		initLayout();
	}

	private void initComponents() {
		initTree();
		this.jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(this.processTree);
	}

	private void initTree() {
		ProcessManager manager = ProcessManager.INSTANCE;
		DefaultMutableTreeNode rootNode = buildGroupNode(manager);
		this.processTree = new ProcessTree(rootNode);
	}

	private DefaultMutableTreeNode buildGroupNode(IProcessGroup processGroup) {
		if (processGroup == null) {
			throw new NullPointerException();
		}

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(processGroup);

		IProcessGroup[] childGroups = processGroup.getGroups();
		for (int i = 0; i < childGroups.length; i++) {
			DefaultMutableTreeNode childNode = buildGroupNode(childGroups[i]);
			if (childNode != null) {
				node.add(childNode);
			}
		}

		IProcessLoader[] childProcesses = processGroup.getProcesses();
		for (int i = 0; i < childProcesses.length; i++) {
			node.add(new DefaultMutableTreeNode(childProcesses[i]));
		}
		return node;
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(jScrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}
}
