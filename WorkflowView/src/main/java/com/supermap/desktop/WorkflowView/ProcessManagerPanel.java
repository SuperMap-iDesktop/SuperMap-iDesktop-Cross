package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.WorkflowView.ui.ProcessTree;
import com.supermap.desktop.process.ProcessManager;
import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.loader.IProcessGroup;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.trees.SmTreeSearchComboBox;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;

/**
 * Created by highsad on 2017/1/5.
 */
public class ProcessManagerPanel extends JPanel {

	private ProcessTree processTree;
	private JScrollPane jScrollPane;
	private SmTreeSearchComboBox<TreeNode> smTreeSearchComboBox;


	public ProcessManagerPanel() {
		initComponents();
		initLayout();
		initListeners();
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
		smTreeSearchComboBox = new SmTreeSearchComboBox<>(processTree);
	}

	private DefaultMutableTreeNode buildGroupNode(IProcessGroup processGroup) {
		if (processGroup == null) {
			throw new NullPointerException();
		}

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(processGroup);

		IProcessGroup[] childGroups = processGroup.getGroups();
		for (IProcessGroup childGroup : childGroups) {
			DefaultMutableTreeNode childNode = buildGroupNode(childGroup);
			if (childNode != null) {
				node.add(childNode);
			}
		}

		IProcessLoader[] childProcesses = processGroup.getProcesses();
		for (IProcessLoader childProcess : childProcesses) {
			node.add(new DefaultMutableTreeNode(childProcess));
		}
		return node;
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		JLabel labelSearch = new JLabel();
		labelSearch.setIcon(ProcessResources.getIcon("/processresources/processSearch.png"));
		this.add(labelSearch, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		this.add(smTreeSearchComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL));
		this.add(jScrollPane, new GridBagConstraintsHelper(0, 1, 2, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH));
	}

	private void initListeners() {

	}
}
