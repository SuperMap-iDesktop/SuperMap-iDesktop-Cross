package com.supermap.desktop.ui.controls.SortTable;

import com.supermap.desktop.ui.controls.table.SmTable;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SortTable extends SmTable {

	private SortButtonRenderer sortButtonRenderer;
	private HeaderListener headerListener;

	public SortTable() {
		sortButtonRenderer = new SortButtonRenderer();
		headerListener = new HeaderListener();
		headerListener.setRenderer(sortButtonRenderer);
		JTableHeader header = super.getTableHeader();
		headerListener.setHeader(header);
		header.addMouseListener(headerListener);

	}

	public void setSortButtonRenderer(SortButtonRenderer sortButtonRenderer) {
		this.sortButtonRenderer = sortButtonRenderer;
		if (this.headerListener == null) {
			return;
		}
		this.headerListener.setRenderer(sortButtonRenderer);
		TableColumnModel model = this.getColumnModel();
		int n = model.getColumnCount();
		for (int i = 0; i < n; i++) {
			model.getColumn(i).setHeaderRenderer(sortButtonRenderer);
		}
	}

	@Override
	public void setTableHeader(JTableHeader tableHeader) {
		super.setTableHeader(tableHeader);
		if (this.headerListener == null) {
			return;
		}
		this.getTableHeader().removeMouseListener(this.headerListener);
		this.headerListener.setHeader(tableHeader);
		this.getTableHeader().addMouseListener(headerListener);
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
		this.setRowHeight(23);
//		this.getModel().addTableModelListener(new TableModelListener() {
//			@Override
//			public void tableChanged(TableModelEvent e) {
//				TableColumnModel model = SortTable.this.getColumnModel();
//				int n = model.getColumnCount();
//				for (int i = 0; i < n; i++) {
//					model.getColumn(i).setHeaderRenderer(sortButtonRenderer);
//				}
//				SortTable.this.updateUI();
//			}
//		});
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

		@Override
		public void mouseClicked(MouseEvent e) {
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
			renderer.setPressedColumn(-1);                // clear
			header.repaint();
		}
	}
}
