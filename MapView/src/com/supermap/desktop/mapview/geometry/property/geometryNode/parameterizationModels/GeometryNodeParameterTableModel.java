package com.supermap.desktop.mapview.geometry.property.geometryNode.parameterizationModels;

import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.utilties.StringUtilities;
import org.jdesktop.swingx.treetable.TreeTableModel;

import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author XiaJT
 */
public class GeometryNodeParameterTableModel extends DefaultTableModel implements TreeTableModel {

	private IGeometry geometry;
	private GeometryParameterModel geometryParameterModel;
	private DefaultMutableTreeNode root;

	public GeometryNodeParameterTableModel(IGeometry iGeometry) {
		geometry = iGeometry;
		geometryParameterModel = GeometryParameterizationFactory.getIGeometryPInstance(geometry);
		initRoot();
		init();
	}

	private void initRoot() {
		root = new DefaultMutableTreeNode();
		if (geometryParameterModel instanceof TableModelPoints) {
			for (int i = 0; i < geometryParameterModel.getRowCount(); i++) {
				root.add(new DefaultMutableTreeNode(geometryParameterModel.getValue(i, 0)));
			}
		} else {
			DefaultMutableTreeNode firstNode = new DefaultMutableTreeNode(geometryParameterModel.getValue(0, 0));
			firstNode.add(new DefaultMutableTreeNode(geometryParameterModel.getValue(1, 0)));
			firstNode.add(new DefaultMutableTreeNode(geometryParameterModel.getValue(2, 0)));
			root.add(firstNode);
			for (int i = 3; i < geometryParameterModel.getRowCount(); i++) {
				root.add(new DefaultMutableTreeNode(geometryParameterModel.getValue(i, 0)));
			}
		}
	}

	private void init() {
//		fireTableStructureChanged();
	}

	public void refreshData() {
		geometryParameterModel = GeometryParameterizationFactory.getIGeometryPInstance(geometry);
		init();
	}

	@Override
	public int getRowCount() {
		if (geometryParameterModel == null) {
			return 0;
		}
		return geometryParameterModel.getRowCount();
	}

	@Override
	public int getColumnCount() {
		if (geometryParameterModel == null) {
			return 2;
		}
		return geometryParameterModel.getColumnCount();
	}


	@Override
	public Object getValueAt(int row, int column) {
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getValue(row, column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return TreeTableModel.class;
		} else if (columnIndex == 1) {
			return Double.class;
		} else if (columnIndex == 2) {
			return Double.class;
		}
		return Object.class;
	}

	@Override
	public Object getValueAt(Object node, int column) {
		int row = getRow(node);
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getValue(row, column);
	}

	private int getRow(Object node) {
		String userObject = (String) ((DefaultMutableTreeNode) node).getUserObject();
		if (StringUtilities.isNullOrEmpty(userObject)) {
			return 0;
		}
		for (int i = 0; i < geometryParameterModel.getRowCount(); i++) {
			if (userObject.equals(geometryParameterModel.getValue(i, 0))) {
				return i;
			}
		}
		return -1;
	}


	@Override
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, Object node, int column) {

	}


	public Object getValueAtRow0(int row, int column, boolean isSelected) {
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getValueAtRow0(row, column, isSelected);
	}

	@Override
	public String getColumnName(int column) {
		if (geometryParameterModel == null) {
			return "";
		}
		return geometryParameterModel.getColumnName(column);
	}

	@Override
	public int getHierarchicalColumn() {
		return 0;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		return ((DefaultMutableTreeNode) parent).getChildAt(index);
	}

	@Override
	public int getChildCount(Object parent) {
		return ((DefaultMutableTreeNode) parent).getChildCount();
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((DefaultMutableTreeNode) node).getChildCount() == 0;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return ((DefaultMutableTreeNode) parent).getIndex(((DefaultMutableTreeNode) child));
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {

	}


}
