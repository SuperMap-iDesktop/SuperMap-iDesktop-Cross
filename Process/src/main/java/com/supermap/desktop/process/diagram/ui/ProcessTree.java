package com.supermap.desktop.process.diagram.ui;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.drop.DropAndDragHandler;
import com.supermap.desktop.controls.utilities.JTreeUIUtilities;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IProcessGroup;
import com.supermap.desktop.process.core.ProcessGroup;
import com.supermap.desktop.process.meta.metaProcessImplements.*;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DragGestureEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by xie on 2017/2/23.
 */
public class ProcessTree extends JPanel {
	private JTree processTree;
	private JScrollPane processTreeView;
	private DefaultMutableTreeNode rootNode;

	public ProcessTree() {
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initResouces();
		new TreeDropAndDragHandler(DataFlavor.stringFlavor).bindSource(this.processTree);
		// fixme 使用自行实现的TreeNode
		// @see com.supermap.desktop.dialog.symbolDialogs.symbolTrees.SymbolGroupTreeNode
		ProcessGroup root = new ProcessGroup(null);
		root.setKey(ProcessProperties.getString("String_Process"));

		ProcessGroup online = new ProcessGroup(root);
		online.setKey("Online");
		online.addProcess(new MetaProcessHeatMap());
		online.addProcess(new MetaProcessKernelDensity());

		ProcessGroup standAloneGroup = new ProcessGroup(root);
		standAloneGroup.setKey(ProcessProperties.getString("String_StandAlone"));
		standAloneGroup.addProcess(new MetaProcessBuffer());
		standAloneGroup.addProcess(new MetaProcessImport());
		standAloneGroup.addProcess(new MetaProcessProjection());
		standAloneGroup.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.IDW));
		standAloneGroup.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.INTERSECT));
		standAloneGroup.addProcess(new MetaProcessSpatialIndex());
		standAloneGroup.addProcess(new MetaProcessSqlQuery());
		standAloneGroup.addProcess(new MetaProcessISOLine());
		root.addProcess(standAloneGroup);
		root.addProcess(online);

		createNodes(rootNode, root);
		JTreeUIUtilities.expandTree(processTree, true);
		processTree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				value = ((DefaultMutableTreeNode) value).getUserObject();
				JLabel jLabel = new JLabel();
				jLabel.setOpaque(true);
				if (value instanceof IProcess) {
					jLabel.setText(((IProcess) value).getTitle());
				} else if (value instanceof String) {
					jLabel.setText((String) value);
				}
				if (selected) {
					jLabel.setForeground(Color.WHITE);
					jLabel.setBackground(new Color(150, 185, 255));
				} else {
					jLabel.setBackground(Color.WHITE);
				}
				if (leaf) {
					jLabel.setIcon(getLeafIcon());
				} else if (expanded) {
					jLabel.setIcon(getOpenIcon());
				} else {
					jLabel.setIcon(getClosedIcon());
				}
				return jLabel;
			}
		});
		processTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Object lastSelectedPathComponent = processTree.getLastSelectedPathComponent();
					if (lastSelectedPathComponent == null) {
						return;
					}
					Object userObject = ((DefaultMutableTreeNode) lastSelectedPathComponent).getUserObject();
					if (userObject instanceof IProcess && !(userObject instanceof IProcessGroup)) {
						if (Application.getActiveApplication().getActiveForm() instanceof FormProcess) {
							((FormProcess) Application.getActiveApplication().getActiveForm()).addProcess((IProcess) userObject);
						}
					}
				}
			}
		});
	}

	class TreeDropAndDragHandler extends DropAndDragHandler {
		public TreeDropAndDragHandler(DataFlavor... flavor) {
			super(flavor);
		}

		@Override
		public Object getTransferData(DragGestureEvent dge) {
			JTree tree = (JTree) dge.getComponent();
			if (tree.getLastSelectedPathComponent() == null) {
				return "";
			}
			String result = "";
			if (((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject() instanceof IProcess) {
				result = ((IProcess) ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject()).getTitle();
			} else {
				result = ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject().toString();
			}
			return result;
		}
	}

	public void initComponents() {
		this.rootNode = new DefaultMutableTreeNode(ProcessProperties.getString("String_Process"));
		this.processTreeView = new JScrollPane();
		this.processTree = new JTree(rootNode);
		this.processTree.putClientProperty("JTree.lineStyle", "Horizontal");
		this.processTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * 添加节点
	 *
	 * @param processGroup
	 */
	public void createNodes(DefaultMutableTreeNode node, IProcessGroup... processGroup) {
		for (IProcessGroup iProcessGroup : processGroup) {
			int childCount = iProcessGroup.getChildCount();
			for (int i = 0; i < childCount; i++) {
				IProcess process = iProcessGroup.getProcessByIndex(i);
				DefaultMutableTreeNode processNode = new DefaultMutableTreeNode(process);
				if (process instanceof IProcessGroup) {
					createNodes(processNode, (IProcessGroup) process);
				}
				node.add(processNode);
			}
		}
	}

	public void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(this.processTreeView, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1).setInsets(0));
		this.processTreeView.setViewportView(processTree);
	}

	public void initResouces() {

	}
}
