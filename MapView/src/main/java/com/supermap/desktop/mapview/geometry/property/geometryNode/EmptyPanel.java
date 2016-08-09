package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.data.Recordset;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class EmptyPanel implements IGeometryNode {
	@Override
	public void refreshData() {

	}

	@Override
	public JPanel getPanel() {
		return new JPanel();
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isModified() {
		return false;
	}

	@Override
	public void reset() {

	}

	@Override
	public void apply(Recordset recordset) {

	}

	@Override
	public void addModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {

	}

	@Override
	public void removeModifiedChangedListener(ModifiedChangedListener modifiedChangedListener) {

	}

	@Override
	public void hidden() {

	}

	@Override
	public void setIsCellEditable(boolean isCellEditable) {

	}
}
