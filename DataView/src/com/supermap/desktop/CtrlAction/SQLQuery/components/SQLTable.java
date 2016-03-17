package com.supermap.desktop.CtrlAction.SQLQuery.components;

import com.supermap.desktop.CtrlAction.SQLQuery.SqlUtilties;
import com.supermap.desktop.dataview.DataViewProperties;
import com.supermap.desktop.utilties.StringUtilties;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/2.
 */
public class SQLTable extends JTable implements ISQLBuildComponent {
	public static final int SECOND_COLUMN_WIDTH = 70;
	public static final int THIRD_COLUMN_WIDTH = 35;

	private int selectRow = -1;

	public SQLTable() {
		super();
		this.setModel(new SQLTableModel());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		headerRenderer.setPreferredSize(new Dimension(0, 0));
		this.getTableHeader().setDefaultRenderer(headerRenderer);
		this.setRowHeight(23);
		setTableColumnWidthAndRanders();


		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (getRowCount() == 0) {
					((SQLTableModel) SQLTable.this.getModel()).addEmptyLine();
				}
			}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int rowAtPoint = SQLTable.this.rowAtPoint(e.getPoint());
				int columnAtPoint = SQLTable.this.columnAtPoint(e.getPoint());

				if (rowAtPoint == -1) {
					SQLTable.this.clearSelection();
				} else if (columnAtPoint == 2) {
					((SQLTableModel) SQLTable.this.getModel()).deleteLine(rowAtPoint);
				}
			}
		});
		((SQLTableModel) this.getModel()).setDoIt(new DoIt() {
			@Override
			public void doIt() {
				setTableColumnWidthAndRanders();
			}
		});
	}

	private void setTableColumnWidthAndRanders() {
		this.getColumnModel().getColumn(1).setPreferredWidth(SECOND_COLUMN_WIDTH);
		this.getColumnModel().getColumn(1).setMinWidth(SECOND_COLUMN_WIDTH);
		this.getColumnModel().getColumn(1).setMaxWidth(SECOND_COLUMN_WIDTH);

		this.getColumnModel().getColumn(2).setMinWidth(THIRD_COLUMN_WIDTH);
		this.getColumnModel().getColumn(2).setMaxWidth(THIRD_COLUMN_WIDTH);
		this.getColumnModel().getColumn(2).setPreferredWidth(THIRD_COLUMN_WIDTH);

		this.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel jLabel = new JLabel("X", JLabel.CENTER);
				jLabel.setForeground(Color.red);
				jLabel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
				return jLabel;
			}
		});

		this.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox()) {
			JComboBox comboBox;
			int selectedRow;
			JTable table;

			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				getCombobox();
				this.table = table;
				this.selectedRow = row;
				this.comboBox.setSelectedItem(value);
				return this.comboBox;
			}

			private void getCombobox() {
				if (comboBox == null) {
					comboBox = new JComboBox(new DefaultComboBoxModel(new String[]{DataViewProperties.getString("String_SQLQueryASC"), DataViewProperties.getString("String_Descend_D")}));
					comboBox.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								stopCellEditing();
							}
						}
					});

				}

			}

			@Override
			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}

			@Override
			public boolean stopCellEditing() {
				boolean result = super.stopCellEditing();
				table.setRowSelectionInterval(selectedRow, selectedRow);
				return result;
			}
		});
	}

	@Override
	public void push(String data, int addMode) {
		// 只能添加字段，不考虑addMode
		selectRow = this.getSelectedRow();
		int dataIndex = ((SQLTableModel) this.getModel()).getDataIndex(data);
		if (dataIndex != -1) {
			this.setRowSelectionInterval(dataIndex, dataIndex);
			// 显示
			this.scrollRectToVisible(this.getCellRect(dataIndex, 0, true));
		} else if (this.selectRow != -1) {
			((SQLTableModel) this.getModel()).setValueAt(data, selectRow, 0);
			this.setRowSelectionInterval(selectRow, selectRow);
		} else {
			((SQLTableModel) this.getModel()).addValue(data);
		}
	}

	@Override
	public String getSQLExpression() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < this.getModel().getRowCount(); i++) {
			if (!StringUtilties.isNullOrEmpty(this.getValueAt(i, 0).toString()) && !StringUtilties.isNullOrEmpty(this.getValueAt(i, 1).toString())) {
				stringBuilder.append(this.getValueAt(i, 0).toString());
				stringBuilder.append(" ");
				stringBuilder.append(SqlUtilties.covert(this.getValueAt(i, 1).toString()));
				stringBuilder.append(",");
			}
		}
		return stringBuilder.toString();
	}

	@Override
	public void clear() {
		((SQLTableModel) this.getModel()).removeAll();
	}

	@Override
	public void rememberSelectstate() {
		// 表不需要记住
	}

	public void add(String value, String order) {

	}

	class SQLTableModel extends DefaultTableModel {
		private DoIt doIt = null;

		public void setDoIt(DoIt doIt) {
			this.doIt = doIt;
		}

		private List<SQLTableData> tableDatas = new ArrayList<>();

		public SQLTableModel() {
			super();
			tableDatas = new ArrayList();
		}

		@Override
		public int getRowCount() {
			if (tableDatas != null) {
				return tableDatas.size();
			} else {
				return 0;
			}

		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return "";
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1 && !StringUtilties.isNullOrEmpty(this.getValueAt(rowIndex, columnIndex).toString());
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return tableDatas.get(rowIndex).getName();
			} else if (columnIndex == 1) {
				return SqlUtilties.format(tableDatas.get(rowIndex).getSort());
			} else {
				return "";
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				if (StringUtilties.isNullOrEmpty(tableDatas.get(rowIndex).getName())) {
					tableDatas.get(rowIndex).setSort("asc");
				}
				tableDatas.get(rowIndex).setName(aValue.toString());
			} else {
				tableDatas.get(rowIndex).setSort(SqlUtilties.covert(aValue.toString()));
			}
			fireTableChange();
		}

		public void addValue(String data) {
			addValue(data, "asc");
		}

		public void addValue(String data, String sort) {
			if (tableDatas.size() > 0 && StringUtilties.isNullOrEmpty(tableDatas.get(0).getName())) {
				tableDatas.get(0).setName(data);
				tableDatas.get(0).setSort(sort);
			} else {
				tableDatas.add(new SQLTableData(data, sort));
			}

			fireTableChange();
		}

		public int getDataIndex(String data) {
			for (int i = 0; i < tableDatas.size(); i++) {
				if (tableDatas.get(i).getName().equals(data)) {
					return i;
				}
			}
			return -1;
		}

		public void addEmptyLine() {
			SQLTableData sqlTableData = new SQLTableData("");
			sqlTableData.setSort("");
			tableDatas.add(sqlTableData);
			fireTableChange();
		}

		public void deleteLine(int rowAtPoint) {
			if (rowAtPoint != -1) {
				this.tableDatas.remove(rowAtPoint);
			}
			fireTableChange();
		}

		private void fireTableChange() {
			fireTableStructureChanged();
			if (doIt != null) {
				doIt.doIt();
			}
		}

		public void removeAll() {
			for (int i = tableDatas.size(); i > 0; i--) {
				deleteLine(i - 1);
			}
			fireTableChange();
		}
	}

	class SQLTableData {
		private String name;
		private String sort;

		public SQLTableData(String name) {
			this(name, "asc");
		}

		public SQLTableData(String name, String sort) {
			this.name = name;
			this.sort = sort;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSort() {
			return sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}
	}

	interface DoIt {
		void doIt();
	}
}
