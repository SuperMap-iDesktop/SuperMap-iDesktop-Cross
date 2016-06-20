package com.supermap.desktop.mapview.geometry.property.geometryNode.compoundModels;

import com.supermap.desktop.core.IteratorEnumerationAdapter;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Implements.DGeoCompound;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;
import com.supermap.desktop.utilities.GeometryTypeUtilities;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author XiaJT
 */
public class GeometryCompoundTreeNode extends DefaultMutableTreeNode {

	private GeometryCompoundTreeNode parent;
	private IGeometry geometry;
	private List<GeometryCompoundTreeNode> childrens = null;

	public GeometryCompoundTreeNode(GeometryCompoundTreeNode parent, IGeometry geometry) {
		this.parent = parent;
		this.geometry = geometry;
		if (geometry instanceof DGeoCompound) {
			childrens = new ArrayList<>();
			for (int i = 0; i < ((DGeoCompound) geometry).getPartCount(); i++) {
				childrens.add(new GeometryCompoundTreeNode(this, DGeometryFactory.create(((DGeoCompound) geometry).getPart(i))));
			}
		}
	}

	@Override
	public boolean isLeaf() {
		return childrens == null || childrens.size() <= 0;
	}

	@Override
	public boolean isRoot() {
		return parent == null;
	}

	@Override
	public void remove(int childIndex) {
		childrens.remove(childIndex);
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getChildCount() {
		if (childrens == null) {
			return 0;
		}
		return childrens.size();
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public int getIndex(TreeNode aChild) {
		if (aChild == null || !(aChild instanceof GeometryCompoundTreeNode)) {
			return -1;
		}
		return childrens.indexOf(aChild);
	}

	@Override
	public TreeNode getChildAt(int index) {
		return childrens.get(index);
	}

	@Override
	public Enumeration children() {
		if (childrens == null) {
			childrens = new ArrayList<>();
		}
		return new IteratorEnumerationAdapter<>(childrens.iterator());
	}

	public IGeometry getGeometry() {
		return geometry;
	}

	@Override
	public String toString() {
		return GeometryTypeUtilities.toString(geometry.getGeometry().getType());
	}
}
