package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.FormMap;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.utilities.StringUtilities;

/**
 * @author XiaJT
 */
public abstract class VectorTableModel {
	protected boolean isModified = false;
	protected int id;
	protected int partIndex;

	public abstract int getRowCount();


	public void doRemoveRows(int[] selectedRows) {
		removeRows(selectedRows);
		isModified = true;
	}

	protected abstract void removeRows(int[] selectedRows);

	public void doInsertPoint(int selectedRow) {
		insertPoint(selectedRow);
		isModified = true;
	}

	protected abstract void insertPoint(int selectedRow);

	public void doAddPoint(int selectedRow) {
		if (selectedRow == -1) {
			selectedRow = 0;
		}
		addPoint(selectedRow);
		isModified = true;
	}

	protected abstract void addPoint(int selectedRow);


	public abstract Object getValueAt(int row, int column);

	public void doSetValueAt(Object aValue, int row, int column) {
		if (StringUtilities.isNullOrEmptyString(aValue) || ((String) aValue).contains("d")) {
			return;
		}
		try {
			Double aDouble = Double.valueOf((String) aValue);
		} catch (Exception e) {
			return;
		}
		setValueAt(aValue, row, column);
		isModified = true;
	}

	protected abstract void setValueAt(Object aValue, int row, int column);

	public abstract int getColumnCount();


	public void doRevert() {
		revert();
		isModified = false;
	}

	protected abstract void revert();

	public void doApply(Recordset recordset) {
		apply(recordset);
		isModified = false;
		IForm activeForm = Application.getActiveApplication().getActiveForm();
		if (activeForm instanceof FormMap) {
			((FormMap) activeForm).getMapControl().getMap().refresh();
		}
	}

	protected abstract void apply(Recordset recordset);

	public String getColumnName() {
		return ControlsProperties.getString("String_DataGridViewColumn_Z");
	}

	public boolean isModified() {
		return isModified;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setPartIndex(int partIndex) {
		this.partIndex = partIndex;
	}

	public void dispose() {

	}
}
