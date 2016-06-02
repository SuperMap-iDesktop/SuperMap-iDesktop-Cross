package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.data.Geometry;
import com.supermap.data.Point2Ds;
import com.supermap.data.Point3Ds;
import com.supermap.data.PointMs;
import com.supermap.data.Recordset;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.IPoint3DFeature;
import com.supermap.desktop.geometry.Abstract.IPointFeature;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.mapview.geometry.property.geometryNode.compoundModels.GeometryCompoundTreeNode;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.GeometryTypeUtilties;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * @author XiaJT
 */
public class JPanelGeometryCompound extends JPanel implements IGeometryNode {

	private DGeoCompound geometry;
	private IGeometryNode subGeometryNode;

	private JLabel labelSubGeometryCount = new JLabel();
	private JTextField textFieldSubGeometryCount = new JTextField();

	private JLabel labelGeometryType = new JLabel();
	private JTextField textFieldGeometryType = new JTextField();

	private JLabel labelNodeCount = new JLabel();
	private JTextField textFieldNodeCount = new JTextField();

	private JTree tree = new JTree();
	private JPanel panelSubGeometry = new JPanel();


	public JPanelGeometryCompound(IGeometry geometry) {
		this.geometry = ((DGeoCompound) geometry);
		subGeometryNode = GeometryNodeFactory.getGeometryNode(DGeometryFactory.create(((DGeoCompound) geometry).getPart(0)));
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		initListeners();
		initResources();
		initComponentState();
	}

	private void initComponents() {
		tree.setModel(new DefaultTreeModel(new GeometryCompoundTreeNode(null, geometry)));
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		textFieldSubGeometryCount.setMinimumSize(new Dimension(100, 23));
		textFieldNodeCount.setEditable(false);
		textFieldSubGeometryCount.setEditable(false);
		textFieldGeometryType.setEditable(false);

	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		this.add(labelSubGeometryCount, new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.NONE).setInsets(10, 10, 0, 0).setWeight(0, 0).setAnchor(GridBagConstraints.WEST));
		this.add(textFieldSubGeometryCount, new GridBagConstraintsHelper(1, 0, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(10, 5, 0, 5).setWeight(0, 0));

		this.add(labelGeometryType, new GridBagConstraintsHelper(0, 1, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setWeight(0, 0).setAnchor(GridBagConstraints.WEST));
		this.add(textFieldGeometryType, new GridBagConstraintsHelper(1, 1, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 5).setWeight(0, 0));

		this.add(labelNodeCount, new GridBagConstraintsHelper(0, 2, 1, 1).setFill(GridBagConstraints.NONE).setInsets(5, 10, 0, 0).setWeight(0, 0).setAnchor(GridBagConstraints.WEST));
		this.add(textFieldNodeCount, new GridBagConstraintsHelper(1, 2, 1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 5, 0, 5).setWeight(0, 0));

		this.add(new JScrollPane(tree), new GridBagConstraintsHelper(0, 3, 2, 1).setFill(GridBagConstraints.BOTH).setInsets(5, 10, 5, 5).setWeight(0, 1));

		panelSubGeometry.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_Label_CurrentPartInfo")));
		panelSubGeometry.setLayout(new GridBagLayout());
		this.add(panelSubGeometry, new GridBagConstraintsHelper(2, 0, 1, 4).setFill(GridBagConstraints.BOTH).setInsets(10, 5, 0, 10).setWeight(1, 1));
	}

	private void initListeners() {
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object lastSelectedPathComponent = tree.getLastSelectedPathComponent();
				if (lastSelectedPathComponent != null && lastSelectedPathComponent instanceof GeometryCompoundTreeNode) {
					if (subGeometryNode != null) {
						subGeometryNode.dispose();
					}
					subGeometryNode = GeometryNodeFactory.getGeometryNode(((GeometryCompoundTreeNode) lastSelectedPathComponent).getGeometry());
					if (subGeometryNode != null && subGeometryNode != JPanelGeometryCompound.this) {

						panelSubGeometry.removeAll();
						panelSubGeometry.add(subGeometryNode.getPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
						Window windowAncestor = SwingUtilities.getWindowAncestor(JPanelGeometryCompound.this);
						if (windowAncestor != null) {
							windowAncestor.repaint();
						}
					}
				}
			}
		});
	}

	private void initResources() {
		labelSubGeometryCount.setText(ControlsProperties.getString("String_Label_PartCount_S"));
		labelNodeCount.setText(ControlsProperties.getString("String_Label_TotalNodeCount_T"));
		labelGeometryType.setText(ControlsProperties.getString("String_LabelGeometryType"));

	}

	private void initComponentState() {
		if (tree.getRowCount() > 0) {
			tree.setSelectionRow(0);
		}
		textFieldGeometryType.setText(GeometryTypeUtilties.toString(this.geometry.getGeometry().getType()));
		textFieldSubGeometryCount.setText(String.valueOf(this.geometry.getPartCount()));
		textFieldNodeCount.setText(String.valueOf(getNodeCount(null)));
	}


	private int getNodeCount(IGeometry iGeometry) {
		if (iGeometry == null) {
			iGeometry = geometry;
		}
		if (iGeometry instanceof DGeoCompound) {
			int nodeCount = 0;
			for (int i = 0; i < ((IMultiPartFeature) iGeometry).getPartCount(); i++) {
				nodeCount += getNodeCount(DGeometryFactory.create((Geometry) ((IMultiPartFeature) iGeometry).getPart(i)));
			}
			return nodeCount;
		}
		if (iGeometry instanceof IMultiPartFeature) {
			int nodeCount = 0;
			for (int i = 0; i < ((IMultiPartFeature) iGeometry).getPartCount(); i++) {
				Object part = ((IMultiPartFeature) iGeometry).getPart(i);
				if (part instanceof Point2Ds) {
					nodeCount += ((Point2Ds) part).getCount();
				} else if (part instanceof Point3Ds) {
					nodeCount += ((Point3Ds) part).getCount();
				} else if (part instanceof PointMs) {
					nodeCount += ((PointMs) part).getCount();
				}
			}
			return nodeCount;
		}
		if (iGeometry instanceof IPointFeature || iGeometry instanceof IPoint3DFeature) {
			return 1;
		}

		return 0;
	}

	@Override
	public void refreshData() {
		subGeometryNode.refreshData();
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void dispose() {
		subGeometryNode.dispose();
	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void reset() {
		// 不需要
	}

	@Override
	public void apply(Recordset recordset) {
		// 不需要
	}

	@Override
	public void addModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {
		// 不支持修改
	}

	@Override
	public void removeModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {
		// 不支持修改
	}

	@Override
	public void hidden() {
		subGeometryNode.hidden();
	}

	@Override
	public void setIsCellEditable(boolean isCellEditable) {
		// 不能修改
	}
}
