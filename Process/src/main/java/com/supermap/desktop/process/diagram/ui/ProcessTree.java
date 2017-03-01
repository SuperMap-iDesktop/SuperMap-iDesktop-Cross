package com.supermap.desktop.process.diagram.ui;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.drop.DropAndDragHandler;
import com.supermap.desktop.process.FormProcess;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IProcessGroup;
import com.supermap.desktop.process.core.ProcessGroup;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessBuffer;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessHeatMap;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessImport;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessKernelDensity;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessProjection;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessSpatialIndex;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessSqlQuery;
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
		root.addProcess(new MetaProcessBuffer());
		root.addProcess(new MetaProcessHeatMap());
		root.addProcess(new MetaProcessImport());
		root.addProcess(new MetaProcessInterpolator(InterpolationAlgorithmType.IDW));
		root.addProcess(new MetaProcessKernelDensity());
		root.addProcess(new MetaProcessOverlayAnalyst(OverlayAnalystType.CLIP));
		root.addProcess(new MetaProcessProjection());
		root.addProcess(new MetaProcessSpatialIndex());
		root.addProcess(new MetaProcessSqlQuery());
		createNodes(root);
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
					Object userObject = ((DefaultMutableTreeNode) processTree.getLastSelectedPathComponent()).getUserObject();
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
	public void createNodes(IProcessGroup... processGroup) {
		for (IProcessGroup iProcessGroup : processGroup) {
//			DefaultMutableTreeNode processGroupNode = new DefaultMutableTreeNode(iProcessGroup);
			int childCount = iProcessGroup.getChildCount();
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode processNode = new DefaultMutableTreeNode(iProcessGroup.getProcessByIndex(i));
				rootNode.add(processNode);
			}
//			this.rootNode.add(processGroupNode);
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
