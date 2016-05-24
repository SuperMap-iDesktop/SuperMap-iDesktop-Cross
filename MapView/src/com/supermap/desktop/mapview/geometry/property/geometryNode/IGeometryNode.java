package com.supermap.desktop.mapview.geometry.property.geometryNode;

import com.supermap.data.Recordset;

import javax.swing.*;

/**
 * @author XiaJT
 */
public interface IGeometryNode {

	void refreshData();

	JPanel getPanel();

	void dispose();

	boolean isModified();

	void reset();

	void apply(Recordset recordset);

	void addModifiedChangedListener(ModifiedChangedListener modifiedChangedListener);

	void removeModifiedChangedListener(ModifiedChangedListener modifiedChangedListener);
}
