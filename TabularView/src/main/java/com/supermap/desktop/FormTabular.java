package com.supermap.desktop;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldType;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
import com.supermap.desktop.Interface.IContextMenuManager;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.Interface.IProperty;
import com.supermap.desktop.Interface.IPropertyManager;
import com.supermap.desktop.controls.property.WorkspaceTreeDataPropertyFactory;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.event.CancellationEvent;
import com.supermap.desktop.implement.SmStatusbar;
import com.supermap.desktop.tabularview.TabularViewProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilties.TabularStatisticUtilties;
import com.supermap.desktop.utilties.TabularTableModel;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class FormTabular extends FormBaseChild implements IFormTabular {

	// region 变量定义
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final SetMouseAdapter columnHeaderMouseListener = new SetMouseAdapter();
	private static final int ROW_HEADER_WIDTH = 70;

	/**
	 * 序号列的列号，第0列为序号列
	 */

	private TabularTableModel tabularTableModel;
	private transient Recordset recordset;
	private JTable jTableTabular;
	private String title = "";
	private JScrollPane jScrollPaneChildWindow;
	private JPopupMenu FormSuperTabularContextMenu;
	public static final Color COLOR_SYSTEM_SELECTED = new Color(185, 214, 244);
	public static final Color COLOR_SYSTEM_NOT_SELECTED = new Color(230, 230, 230);
	public static final Color COLOR_EDITABLE_SELECTED = new Color(196, 225, 255);
	public static final Color COLOR_EDITABLE_NOT_SELECTED = new Color(247, 247, 247);
	private static final Color COLOR_WORD_SELECTED = Color.BLACK;
	private static final int PREFER_ROW_HEIGHT = 23;
	private static final int PREFER_COLUMN_WIDTH = 100;
	private static final int MIN_COLUMN_WIDTH = 20;
	private int[] selectColumns;

	private ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			checkStatisticsResultState(selectColumns);
			selectColumns = jTableTabular.getSelectedColumns();
		}
	};

	private KeyListener keyListener = new KeyAdapter() {

		@Override
		public void keyReleased(KeyEvent e) {
			TabularStatisticUtilties.updateSatusbars(FormTabular.this);
		}
	};

	private int tableClickedRow = -1;
	private int tableClickedColumn = -1;
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				showContextMenu(e);
			} else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				TabularStatisticUtilties.updateSatusbars(FormTabular.this);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			TabularStatisticUtilties.updateSatusbars(FormTabular.this);
		}

	};
	private JList rowHeader;
	private MouseAdapter rowHeaderMouseMotionListener = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {

			if (!e.isControlDown() && tableClickedRow != -1) {
				int row = jTableTabular.rowAtPoint(e.getPoint());
				if (row >= 0 && row < jTableTabular.getRowCount()) {
					jTableTabular.setRowSelectionInterval(tableClickedRow, row);
					jTableTabular.scrollRectToVisible(jTableTabular.getCellRect(row, 0, true));
				}
			}
			TabularStatisticUtilties.updateSatusbars(FormTabular.this);
		}

	};

	private MouseAdapter rowHeaderMouseListener = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			setRowHeaderMousePressed(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			tableClickedRow = -1;
		}
	};

	private MouseAdapter columnHeaderMouseMotionListener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!e.isControlDown() && tableClickedColumn != -1) {
				int column = jTableTabular.columnAtPoint(e.getPoint());
				if (column >= 0 && column < jTableTabular.getColumnCount()) {
					setSelectedColumn(tableClickedColumn, column);
					jTableTabular.scrollRectToVisible(jTableTabular.getCellRect(0, column, true));
				}
			}
			TabularStatisticUtilties.updateSatusbars(FormTabular.this);
		}
	};

	// endregion

	public FormTabular() {
		this("");
	}

	public FormTabular(String name) {
		this(name, null, null);
	}

	public FormTabular(String title, Icon icon, Component component) {
		super(title, icon, component);
		this.title = title;
		jTableTabular = new AbstractHandleTable();

		// 输入时直接开始编辑
		jTableTabular.setSurrendersFocusOnKeystroke(true);
		// 设置行高
		this.jTableTabular.setRowHeight(FormTabular.PREFER_ROW_HEIGHT);
		jScrollPaneChildWindow = new JScrollPane(jTableTabular);
		jScrollPaneChildWindow.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		ListModel listModel = new LeftTableHeaderListModel(jTableTabular);
		rowHeader = new JList(listModel);
		rowHeader.setFixedCellWidth(ROW_HEADER_WIDTH);
		rowHeader.setFixedCellHeight(jTableTabular.getRowHeight());
		rowHeader.setCellRenderer(new RowHeaderRenderer(jTableTabular));

		jScrollPaneChildWindow.setRowHeaderView(rowHeader);
		setLayout(new BorderLayout());
		this.add(jScrollPaneChildWindow);
		if (Application.getActiveApplication().getMainFrame() != null) {
			IContextMenuManager manager = Application.getActiveApplication().getMainFrame().getContextMenuManager();
			this.FormSuperTabularContextMenu = (JPopupMenu) manager.get("SuperMap.Desktop.FormSuperTabular.FormSuperTabularContextMenu");
		}

		initStatusbars();
		registerEvents();
	}

	private void registerEvents() {
		this.jTableTabular.addMouseListener(mouseAdapter);
		this.jTableTabular.getTableHeader().addMouseListener(mouseAdapter);
		this.jTableTabular.addKeyListener(keyListener);
		this.jTableTabular.getSelectionModel().addListSelectionListener(listSelectionListener);
		this.rowHeader.addMouseMotionListener(rowHeaderMouseMotionListener);
		this.rowHeader.addMouseListener(rowHeaderMouseListener);
		// 表头点击选中一列
		this.jTableTabular.getTableHeader().addMouseMotionListener(columnHeaderMouseMotionListener);

		this.jTableTabular.getTableHeader().addMouseListener(columnHeaderMouseListener);
	}

	@Override
	public void actived() {
		if (PropertyType.isGeometryPropertyType(Application.getActiveApplication().getMainFrame().getPropertyManager().getPropertyType())) {
			Application.getActiveApplication().getMainFrame().getPropertyManager().setProperty(null);
		}
//		setProperty();
	}

	private void setProperty() {
		if (Application.getActiveApplication().getMainFrame().getPropertyManager().isUsable()) {
			if (getRecordset() != null) {

				DatasetVector dataset = getRecordset().getDataset();
				IProperty propertie = dataset == null ? null : WorkspaceTreeDataPropertyFactory.getRecordsetPropertyControl(dataset);
				IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
				propertyManager.setProperty(new IProperty[]{propertie});
			}
		}
	}

	private void unRegisterEvents() {
		this.jTableTabular.removeMouseListener(mouseAdapter);
		this.jTableTabular.removeKeyListener(keyListener);
		this.jTableTabular.getSelectionModel().removeListSelectionListener(listSelectionListener);
		this.rowHeader.removeMouseMotionListener(rowHeaderMouseMotionListener);
		this.rowHeader.removeMouseListener(rowHeaderMouseListener);

		this.jTableTabular.getTableHeader().removeMouseMotionListener(columnHeaderMouseMotionListener);
		this.jTableTabular.getTableHeader().removeMouseListener(columnHeaderMouseListener);
	}

	private void setRowHeaderMousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
			int pick = jTableTabular.rowAtPoint(e.getPoint());
			if (pick < 0) {
				return;
			}
			tableClickedRow = pick;
			if (e.isShiftDown()) {
				if (tableClickedRow > jTableTabular.getSelectedRow()) {
					jTableTabular.setRowSelectionInterval(jTableTabular.getSelectedRow(), tableClickedRow);
				} else {
					jTableTabular.setRowSelectionInterval(tableClickedRow, jTableTabular.getSelectedRow());

				}
			} else {
				if (!e.isControlDown()) {
					jTableTabular.clearSelection();
				}
				jTableTabular.addRowSelectionInterval(pick, pick);
			}
			this.setSelectedColumn(jTableTabular.getColumnCount() - 1, 0);

		}
		TabularStatisticUtilties.updateSatusbars(FormTabular.this);
	}

	/**
	 * 设置不可编辑
	 */
	private void initStatusbars() {
		SmStatusbar smStatusbar = this.getStatusbar();
		((JTextField) smStatusbar.get(TabularStatisticUtilties.FIELD_TYPE)).setEditable(false);
		((JTextField) smStatusbar.get(TabularStatisticUtilties.FIELD_NAME)).setEditable(false);
		((JTextField) smStatusbar.get(TabularStatisticUtilties.STATISTIC_RESULT_INDEX)).setEditable(false);
	}

	private void showContextMenu(MouseEvent e) {
		FormSuperTabularContextMenu.show(jTableTabular, e.getX(), e.getY());
	}

	@Override
	public WindowType getWindowType() {
		return WindowType.TABULAR;
	}

	@Override
	public void formClosing(CancellationEvent e) {
		unRegisterEvents();
		recordset.dispose();
	}

	@Override
	public void windowShown() {
		// 显示
		UICommonToolkit.getLayersManager().setMap(null);
	}

	private void setColumnsWidth() {
		// 设置列宽
		for (int i = 0; i < jTableTabular.getColumnModel().getColumnCount(); i++) {
			// 设置宽度
			jTableTabular.getColumnModel().getColumn(i).setMaxWidth(Integer.MAX_VALUE);
			jTableTabular.getColumnModel().getColumn(i).setMinWidth(MIN_COLUMN_WIDTH);
			jTableTabular.getColumnModel().getColumn(i).setPreferredWidth(PREFER_COLUMN_WIDTH);
		}
	}

	/**
	 * bool类型的渲染器
	 *
	 * @author XiaJT
	 */
	private class BooleanTableCellRenderer extends JLabel implements TableCellRenderer {

		public BooleanTableCellRenderer() {
			super();
			// 必须设置背景透明才能正常显示选中状态
			this.setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				this.setBackground(COLOR_SYSTEM_SELECTED);
			} else {
				this.setBackground(Color.white);
			}
			if (value == null) {
				this.setText("");
			} else if (value.equals(true)) {
				this.setText("True");
			} else if (value.equals(false)) {
				this.setText("False");
			}
			this.setHorizontalAlignment(JLabel.CENTER);
			return this;
		}
	}

	class AbstractHandleTable extends JTable {
		private static final long serialVersionUID = 1L;

		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			Component component = super.prepareRenderer(renderer, row, column);

			if (!this.isCellEditable(row, column)) {
				if (isCellSelected(row, column)) {
					component.setBackground(COLOR_SYSTEM_SELECTED);
				} else {
					component.setBackground(COLOR_SYSTEM_NOT_SELECTED);
				}
			} else {
				if (isCellSelected(row, column)) {
					component.setBackground(COLOR_EDITABLE_SELECTED);
				} else {
					component.setBackground(COLOR_EDITABLE_NOT_SELECTED);
				}
			}
			return component;

		}

	}

	/**
	 * 日期类型的编辑器
	 *
	 * @author XiaJT
	 */
	private class DataTabelCellEditor extends DefaultCellEditor {
		public DataTabelCellEditor(final JTextField textField) {
			super(textField);
			textField.setHorizontalAlignment(JTextField.CENTER);

			this.delegate = new EditorDelegate() {
				@Override
				public void setValue(Object value) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
					textField.setText((value != null) ? dateFormat.format(value) : "");
				}

				@Override
				public Object getCellEditorValue() {
					return textField.getText();
				}
			};
		}
	}

	/**
	 * 日期类型渲染器
	 *
	 * @author XiaJT
	 */
	private class DataTabelCellRender extends JLabel implements TableCellRenderer {

		public DataTabelCellRender() {
			super();
			// 必须设置背景透明才能正常显示选中状态
			this.setOpaque(true);
			this.setHorizontalAlignment(JLabel.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				this.setBackground(COLOR_SYSTEM_SELECTED);
			} else {
				this.setBackground(Color.white);
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			this.setText((value != null) ? dateFormat.format(value) : "");
			return this;
		}

	}

	@Override
	public int getRowCount() {
		return this.jTableTabular.getRowCount();
	}

	@Override
	public int getSelectedRow() {
		return this.jTableTabular.getSelectedRow();
	}

	@Override
	public int[] getSelectedRows() {
		return this.jTableTabular.getSelectedRows();
	}

	@Override
	public Recordset getRecordset() {
		if (tabularTableModel == null) {
			return null;
		}
		return this.tabularTableModel.getRecordset();
	}

	@Override
	public void setRecordset(Recordset recordset) {

		// 数据信息
		if (this.tabularTableModel != null) {
			this.tabularTableModel.dispose();
		}
		this.recordset = recordset;
		this.tabularTableModel = new TabularTableModel(recordset);
		this.jTableTabular.setModel(this.tabularTableModel);

		// 编辑时保存
		// TODO: 2016/4/18
		TableCellEditor tableCellEditor = jTableTabular.getDefaultEditor(JTable.class);
		tableCellEditor.addCellEditorListener(new CellEditorListener() {
			@Override
			public void editingStopped(ChangeEvent e) {
				int column = jTableTabular.getSelectedColumn();
				int row = jTableTabular.getSelectedRow();
				if (row != -1 && column != -1) {
					((TabularTableModel) jTableTabular.getModel()).setValueAt(jTableTabular.getCellEditor(row, column).getCellEditorValue(), row, column);
				}
			}

			@Override
			public void editingCanceled(ChangeEvent e) {
				// do nothing
			}
		});

		// 设置选中时不默认选中一行或一列
		jTableTabular.setColumnSelectionAllowed(true);
		jTableTabular.setRowSelectionAllowed(true);

		// 设置选中字体颜色不变
		jTableTabular.setSelectionForeground(COLOR_WORD_SELECTED);

		// 设置多选可用
		jTableTabular.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// 不拖动
		jTableTabular.getTableHeader().setReorderingAllowed(false);

		// 设置居中显示
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JTextField.CENTER);
		jTableTabular.setDefaultRenderer(Object.class, cellRenderer);

		jTableTabular.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		((DefaultTableCellRenderer) jTableTabular.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		// bool类型编辑器
		JComboBox<String> booleanEditorControl = new JComboBox<String>();
		booleanEditorControl.addItem("");
		booleanEditorControl.addItem("True");
		booleanEditorControl.addItem("False");
		DefaultCellEditor booleanEditor = new DefaultCellEditor(booleanEditorControl);
		booleanEditor.setClickCountToStart(2);
		this.jTableTabular.setDefaultEditor(Boolean.class, booleanEditor);

		this.jTableTabular.setDefaultRenderer(Boolean.class, new BooleanTableCellRenderer());

		this.jTableTabular.setDefaultEditor(Time.class, new DataTabelCellEditor(new JTextField()));
		this.jTableTabular.setDefaultRenderer(Time.class, new DataTabelCellRender());
		JTextField objectEditorControl = new JTextField();
		objectEditorControl.setHorizontalAlignment(JTextField.CENTER);
		DefaultCellEditor objectCellEditor = new TableDefaultCellEditor(objectEditorControl);
		this.jTableTabular.setDefaultEditor(Object.class, objectCellEditor);
		// 设置列宽
		setColumnsWidth();
		// 设置行高
		this.jTableTabular.setRowHeight(FormTabular.PREFER_ROW_HEIGHT);
		this.jTableTabular.updateUI();
		TabularStatisticUtilties.updateSatusbars(FormTabular.this);
//		setProperty();
	}

	private void checkStatisticsResultState(int[] beforeSelectedColumn) {
		boolean isClear = false;
		int[] afterSelectedColumn = jTableTabular.getSelectedColumns();
		if (beforeSelectedColumn == null) {
			isClear = true;
		} else if (afterSelectedColumn.length != beforeSelectedColumn.length) {
			isClear = true;
		} else {
			for (int i = 0; i < beforeSelectedColumn.length; i++) {
				if (beforeSelectedColumn[i] != afterSelectedColumn[i]) {
					isClear = true;
					break;
				}
			}
		}
		if (isClear) {
			TabularStatisticUtilties.updataStatisticsResult("");
		}
	}

	private void setSelectedColumn(int column1, int column2) {
		jTableTabular.setColumnSelectionInterval(column1, column2);
	}

	private void addSelectedColumn(int column1, int column2) {
		jTableTabular.addColumnSelectionInterval(column1, column2);
	}

	/**
	 * 定位函数
	 */
	@Override
	public void goToRow(int goToRow) {
		this.jTableTabular.clearSelection();
		this.jTableTabular.setRowSelectionInterval(goToRow, goToRow);
		setSelectedColumn(0, jTableTabular.getColumnCount() - 1);
		Rectangle aRect = this.jTableTabular.getCellRect(goToRow, 0, true);
		this.jTableTabular.scrollRectToVisible(aRect);
		TabularStatisticUtilties.updateSatusbars(FormTabular.this);
	}

	@Override
	public void addRows(List<Integer> addRows) {
		this.jTableTabular.clearSelection();
		for (int i = 0; i < addRows.size(); i++) {
			this.jTableTabular.addRowSelectionInterval(addRows.get(i), addRows.get(i));
			setSelectedColumn(0, jTableTabular.getColumnCount() - 1);
		}
		TabularStatisticUtilties.updateSatusbars(FormTabular.this);
	}

	@Override
	public int getSelectColumnCount() {
		return this.jTableTabular.getSelectedColumnCount();
	}

	@Override
	public int[] getSelectedColumns() {
		return this.jTableTabular.getSelectedColumns();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return this.jTableTabular.getModel().getValueAt(row, column);
	}

	@Override
	public boolean sortRecordset(String sortKind, int... selectedColumns) {
		boolean flag = false;
		int selectColumn = jTableTabular.getSelectedColumn();
		this.recordset = ((TabularTableModel) this.jTableTabular.getModel()).sortRecordset(sortKind, selectedColumns);
		this.jTableTabular.updateUI();
		if (recordset != null) {
			flag = true;
		}
		for (int i = 0; i < jTableTabular.getRowCount(); i++) {
			this.jTableTabular.addRowSelectionInterval(i, i);
		}
		setSelectedColumn(selectColumn, selectColumn);
		setColumnsWidth();
		return flag;
	}

	@Override
	public boolean doStatisticAnalust(StatisticMode statisticMode, String successMessage) {
		int selectColumn = jTableTabular.getSelectedColumn();
		if (!TabularStatisticUtilties.isStatisticsSupportType(recordset, selectColumn)) {
			TabularStatisticUtilties.updataStatisticsResult(TabularViewProperties.getString("String_Output_ColumnNotStatistic"));
			return false;
		}
		int[] selectedRows = this.getSelectedRows();
		QueryParameter queryParameter = new QueryParameter(recordset.getQueryParameter());
		String smIdName = recordset.getDataset().getTableName() + "." + recordset.getFieldInfos().get(0).getName();
		if (selectedRows.length > 0 && selectedRows.length < this.getRowCount()) {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < selectedRows.length; i++) {
				// 第一列为ID
				int selectID = (int) jTableTabular.getModel().getValueAt(selectedRows[i], 0);
				if (i != 0) {
					stringBuilder.append(" or");
				}
				stringBuilder.append(" " + smIdName + "=" + selectID);
			}
			queryParameter.setAttributeFilter(stringBuilder.toString());
		}

		Recordset statisticRecordset = recordset.getDataset().query(queryParameter);

		double result = 0;
		try {
			result = statisticRecordset.statistic(selectColumn, statisticMode);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			statisticRecordset.close();
			statisticRecordset.dispose();
		}
		String columnType = getSelectColumnType(selectColumn);
		String caption = getColumnCaption(selectColumn);
		TabularStatisticUtilties.updataStatisticsResult(MessageFormat.format(successMessage, columnType, caption, result));
		return true;
	}

	@Override
	public String getSelectColumnType(int column) {
		FieldType fieldType = recordset.getFieldInfos().get(column).getType();
		return FieldTypeUtilities.getFieldTypeName(fieldType);
	}

	@Override
	public String getSelectColumnName(int column) {
		return recordset.getFieldInfos().get(column).getName();
	}

	public String getColumnCaption(int column) {
		return recordset.getFieldInfos().get(column).getCaption();
	}

	public JList getRowHeader() {
		return rowHeader;
	}

	public class RowHeaderRenderer extends JLabel implements ListCellRenderer {
		JTable table;

		RowHeaderRenderer(JTable table) {
			this.table = table;
			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			this.setText(String.valueOf(index + 1));
			this.setPreferredSize(new Dimension(100, 50));
			return this;
		}

	}

	public class LeftTableHeaderListModel extends AbstractListModel {
		private static final long serialVersionUID = 1L;

		JTable table;

		public LeftTableHeaderListModel(JTable table) {
			super();
			this.table = table;
		}

		@Override
		public int getSize() {
			return table.getRowCount();
		}

		@Override
		public Object getElementAt(int index) {
			return index;
		}
	}

	class SetMouseAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
				int pick = jTableTabular.columnAtPoint(e.getPoint());
				tableClickedColumn = pick;
				if (e.isShiftDown()) {
					setSelectedColumn(tableClickedColumn, jTableTabular.getSelectedColumn());
				} else {
					if (!e.isControlDown()) {
						jTableTabular.clearSelection();
					}
					addSelectedColumn(pick, pick);
				}
				if (jTableTabular.getRowCount() > 0) {
					jTableTabular.setRowSelectionInterval(jTableTabular.getRowCount() - 1, 0);
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			tableClickedColumn = -1;
			TabularStatisticUtilties.updateSatusbars(FormTabular.this);
		}
	}

	@Override
	public JTable getjTableTabular() {
		return jTableTabular;
	}

	class TableDefaultCellEditor extends DefaultCellEditor {

		public TableDefaultCellEditor(JTextField textField) {
			super(textField);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			Component tableCellEditorComponent = super.getTableCellEditorComponent(table, value, isSelected, row, column);
			if (tableCellEditorComponent instanceof JTextField) {
				((JTextField) tableCellEditorComponent).selectAll();
				((JTextField) tableCellEditorComponent).setHorizontalAlignment(JTextField.CENTER);
			}
			return tableCellEditorComponent;
		}
	}

	@Override
	public HashMap<Integer, Object> getRowIndexMap() {
		HashMap<Integer, Object> result = new HashMap<Integer, Object>();
		if (this.tabularTableModel != null) {
			result = this.tabularTableModel.getRowIndexMap();
		}
		return result;
	}

	@Override
	public HashMap<Object, Integer> getIdMap() {
		HashMap<Object, Integer> result = new HashMap<Object, Integer>();
		if (this.tabularTableModel != null) {
			result = this.tabularTableModel.getIdMap();
		}
		return result;
	}
}
