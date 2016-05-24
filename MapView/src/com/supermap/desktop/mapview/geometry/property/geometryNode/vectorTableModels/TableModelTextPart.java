package com.supermap.desktop.mapview.geometry.property.geometryNode.vectorTableModels;

import com.supermap.data.Recordset;
import com.supermap.data.TextPart;
import com.supermap.desktop.geometry.Abstract.IGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Implements.DGeometryFactory;

/**
 * @author XiaJT
 */
public class TableModelTextPart extends VectorTableModel {
	private TextPart textPartBase;
	private TextPart textPart;

	public TableModelTextPart(TextPart textPart) {
		this.textPartBase = textPart;
		this.textPart = this.textPartBase.clone();

	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public void removeRows(int[] selectedRows) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertPoint(int selectedRow) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addPoint(int selectedRow) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			return textPart.getX();
		} else if (column == 2) {
			return textPart.getY();
		}
		throw new UnsupportedOperationException(String.valueOf(column));
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		if (column == 1) {
			textPart.setX(Double.valueOf((String) aValue));
		} else if (column == 2) {
			textPart.setY(Double.valueOf((String) aValue));
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public void revert() {
		textPart.setX(textPartBase.getX());
		textPart.setY(textPartBase.getY());
	}

	@Override
	public void apply(Recordset recordset) {
		textPartBase.setX(textPart.getX());
		textPartBase.setY(textPart.getY());

		recordset.moveFirst();
		recordset.seekID(id);
		IGeometry iGeometry = DGeometryFactory.create(recordset.getGeometry());
		((IMultiPartFeature) iGeometry).setPart(partIndex, textPartBase);
		recordset.edit();
		recordset.setGeometry(iGeometry.getGeometry());
		recordset.update();
	}

	@Override
	public void dispose() {
		textPart.dispose();
		textPartBase.dispose();
	}
}
