package com.supermap.desktop.ui.controls.SortTable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SortTable extends JTable {

	private SortButtonRenderer sortButtonRenderer = new SortButtonRenderer();
	private HeaderListener headerListener = new HeaderListener();

	public SortTable() {
		headerListener.setRenderer(sortButtonRenderer);

		JTableHeader header = this.getTableHeader();
		headerListener.setHeader(header);
		header.addMouseListener(headerListener);
	}

	@Override
	public void setModel(TableModel dataModel) {
		JTableHeader header = this.getTableHeader();
		if (header != null) {
			header.removeMouseListener(headerListener);
		}

		super.setModel(dataModel);

		TableColumnModel model = this.getColumnModel();
		int n = model.getColumnCount();
		for (int i = 0; i < n; i++) {
			model.getColumn(i).setHeaderRenderer(sortButtonRenderer);
		}

		header = this.getTableHeader();
		if (header != null) {
			headerListener.setHeader(header);
			header.addMouseListener(headerListener);
			header.setReorderingAllowed(false);
		}
	}

	class HeaderListener extends MouseAdapter {
		public JTableHeader header;
		public SortButtonRenderer renderer;

		HeaderListener() {
		}

		public void setHeader(JTableHeader header) {
			this.header = header;
		}

		public SortButtonRenderer getRenderer() {
			return renderer;
		}

		public void setRenderer(SortButtonRenderer renderer) {
			this.renderer = renderer;
		}

		public HeaderListener(JTableHeader header, SortButtonRenderer renderer) {
			this.header = header;
			this.renderer = renderer;
		}

		public void mousePressed(MouseEvent e) {
			int col = header.columnAtPoint(e.getPoint());
			int sortCol = header.getTable().convertColumnIndexToModel(col);
			renderer.setPressedColumn(col);
			renderer.setSelectedColumn(col);
			header.repaint();

			if (header.getTable().isEditing()) {
				header.getTable().getCellEditor().stopCellEditing();
			}

			boolean isAscent = SortButtonRenderer.DOWN == renderer.getState(col);
			((SortableTableModel) header.getTable().getModel())
					.sortByColumn(sortCol, isAscent);
		}

		public void mouseReleased(MouseEvent e) {
//			int col = header.columnAtPoint(e.getPoint());
			renderer.setPressedColumn(-1);                // clear
			header.repaint();
		}
	}
}
