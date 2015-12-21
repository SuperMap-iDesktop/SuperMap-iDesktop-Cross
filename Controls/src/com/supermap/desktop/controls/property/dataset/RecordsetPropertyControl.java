package com.supermap.desktop.controls.property.dataset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.NumberFormatter;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.Enum;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.event.TableCellValueChangeEvent;
import com.supermap.desktop.event.TableCellValueChangeListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilties.FieldTypeUtilties;
import com.supermap.desktop.utilties.StringUtilties;

/**
 * FieldInfo 添加到 DatasetVector 之后，就只有 Caption 可以进行修改。
 * 
 * @author highsad
 *
 */
public class RecordsetPropertyControl extends AbstractPropertyControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_INDEX_WIDTH = 80;

	private JTable tableRecordset;
	private JButton buttonAdd;
	private JButton buttonDelete;
	private JButton buttonReset;
	private JButton buttonApply;
	private JCheckBox checkBoxShowWarning;

	private transient DatasetVector datasetVector;
	private transient FieldInfos fieldInfos;
	private boolean showWarning = true;

	private ArrayList<ModifiedData> modifields = new ArrayList<ModifiedData>();
	private boolean isCellValueChange = false;

	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonAdd) {
				buttonAddClicked();
			} else if (e.getSource() == buttonDelete) {
				buttonRemoveClicked();
			} else if (e.getSource() == buttonReset) {
				buttonResetClicked();
			} else if (e.getSource() == buttonApply) {
				buttonApplyClicked();
			}
		}
	};

	private transient ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			checkBoxShowWarningClicked();
		}
	};

	private transient ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			tableSelectionChanged(e);
		}
	};

	private transient TableCellValueChangeListener tableCellValueChangeListener = new TableCellValueChangeListener() {

		@Override
		public void tableCellValueChange(TableCellValueChangeEvent e) {
			isCellValueChange = true;
			setComponentsEnabled();
		}
	};

	public RecordsetPropertyControl(DatasetVector datasetVector) {
		super(ControlsProperties.getString("String_RecordsetStruct"));
		initializeComponents();
		initializeResources();
		setDatasetVector(datasetVector);
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.RECORDSET;
	}

	private void initializeComponents() {
		this.buttonAdd = new JButton("Add");
		this.buttonDelete = new JButton("Delete");
		this.buttonReset = new JButton("Reset");
		this.buttonApply = new JButton("Apply");
		this.checkBoxShowWarning = new JCheckBox("ShowWarning");

		initializeTable();
		JScrollPane scrollPane = new JScrollPane(this.tableRecordset);

		GroupLayout gl_mainContent = new GroupLayout(this);
		gl_mainContent.setAutoCreateContainerGaps(true);
		gl_mainContent.setAutoCreateGaps(true);
		this.setLayout(gl_mainContent);

		// @formatter:off
		gl_mainContent.setHorizontalGroup(gl_mainContent.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createSequentialGroup()
						.addComponent(this.buttonAdd)
						.addComponent(this.buttonDelete)
						.addComponent(this.checkBoxShowWarning)
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		
		gl_mainContent.setVerticalGroup(gl_mainContent.createSequentialGroup()
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_mainContent.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonAdd)
						.addComponent(this.buttonDelete)
						.addComponent(this.checkBoxShowWarning)
						.addComponent(this.buttonReset)
						.addComponent(this.buttonApply)));
		//@formatter:on
	}

	public DatasetVector getDatasetVector() {
		return datasetVector;
	}

	public void setDatasetVector(DatasetVector datasetVector) {
		this.datasetVector = datasetVector;
		this.fieldInfos = datasetVector.getFieldInfos();
		unregisterEvents();
		fillComponents();
		setComponentsEnabled();
		registerEvents();
	}

	@Override
	public void refreshData() {
		setDatasetVector(this.datasetVector);
	}

	private void initializeResources() {
		this.buttonAdd.setText(CommonProperties.getString(CommonProperties.Add));
		this.buttonDelete.setText(CommonProperties.getString(CommonProperties.Delete));
		this.checkBoxShowWarning.setText(ControlsProperties.getString("String_Property_IsShowDeleteWarning"));
		this.buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
	}

	private void initializeTable() {
		this.tableRecordset = new JTable();
		this.tableRecordset.setModel(new RecordsetPropertyTableModel());
		this.tableRecordset.getColumnModel().getColumn(0).setMaxWidth(COLUMN_INDEX_WIDTH);

		// ColumnClass Integer Editor
		NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		JFormattedTextField intEditorControl = new JFormattedTextField(formatter);
		DefaultCellEditor integerEditor = new DefaultCellEditor(intEditorControl);
		this.tableRecordset.setDefaultEditor(Integer.class, integerEditor);

		// ColumnClass Boolean Editor
		JCheckBox booleanEditorControl = new JCheckBox();
		booleanEditorControl.setHorizontalAlignment(JLabel.CENTER);
		DefaultCellEditor booleanEditor = new DefaultCellEditor(booleanEditorControl);
		this.tableRecordset.setDefaultEditor(Boolean.class, booleanEditor);

		// ColumnClass FieldType Editor
		JComboBox<String> fieldTypeEditorControl = new JComboBox<String>();
		Enum[] values = FieldType.getEnums(FieldType.class);
		for (int i = 0; i < values.length; i++) {
			fieldTypeEditorControl.addItem(FieldTypeUtilties.getFieldTypeName((FieldType) values[i]));
		}
		DefaultCellEditor fieldTypeEditor = new DefaultCellEditor(fieldTypeEditorControl);
		this.tableRecordset.setDefaultEditor(FieldType.class, fieldTypeEditor);

		this.tableRecordset.setRowHeight(this.tableRecordset.getRowHeight() + 4);
		this.tableRecordset.setRowSelectionAllowed(false);
		this.tableRecordset.setCellSelectionEnabled(true);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setVerticalAlignment(SwingConstants.CENTER);
		this.tableRecordset.setDefaultRenderer(Object.class, renderer);
	}

	private void registerEvents() {
		this.buttonAdd.addActionListener(this.actionListener);
		this.buttonDelete.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.buttonApply.addActionListener(this.actionListener);
		this.checkBoxShowWarning.addItemListener(this.itemListener);
		this.tableRecordset.getSelectionModel().addListSelectionListener(this.listSelectionListener);
		((RecordsetPropertyTableModel) this.tableRecordset.getModel()).addTableCellValueChangeListener(this.tableCellValueChangeListener);
	}

	private void unregisterEvents() {
		this.buttonAdd.removeActionListener(this.actionListener);
		this.buttonDelete.removeActionListener(this.actionListener);
		this.buttonReset.removeActionListener(this.actionListener);
		this.buttonApply.removeActionListener(this.actionListener);
		this.checkBoxShowWarning.removeItemListener(this.itemListener);
		this.tableRecordset.getSelectionModel().removeListSelectionListener(this.listSelectionListener);
		((RecordsetPropertyTableModel) this.tableRecordset.getModel()).removeTableCellValueChangeListener(this.tableCellValueChangeListener);
	}

	private void setComponentsEnabled() {
		this.buttonAdd.setEnabled(!this.datasetVector.isReadOnly());
		this.buttonDelete.setEnabled(!this.datasetVector.isReadOnly() && this.isCellValueChange);
		this.checkBoxShowWarning.setEnabled(!this.datasetVector.isReadOnly());
		this.buttonReset.setEnabled(!this.modifields.isEmpty() || this.isCellValueChange);
		this.buttonApply.setEnabled(!this.modifields.isEmpty() || this.isCellValueChange);
	}

	private void buttonAddClicked() {
		RecordsetPropertyTableModel tableModel = (RecordsetPropertyTableModel) this.tableRecordset.getModel();
		FieldData fieldData = tableModel.addRow();
		if (fieldData != null) {
			this.modifields.add(new ModifiedData(fieldData, ModifiedData.ADD));
		}
		setComponentsEnabled();
	}

	private void fillComponents() {
		this.checkBoxShowWarning.setSelected(this.showWarning);
		((RecordsetPropertyTableModel) this.tableRecordset.getModel()).removeAllRows();
		((RecordsetPropertyTableModel) this.tableRecordset.getModel()).intializeRows(this.fieldInfos);
	}

	private void buttonRemoveClicked() {
		int[] rows = this.tableRecordset.getSelectedRows();

		if (showWarning) {
			int confirmResult = UICommonToolkit.showConfirmDialog(ControlsProperties.getString("String_ConfirmDeleteField"));

			if (confirmResult == 0) {
				removeRows(rows);
			}
		} else {
			removeRows(rows);
		}

		setComponentsEnabled();
	}

	private void removeRows(int[] rows) {
		RecordsetPropertyTableModel tableModel = (RecordsetPropertyTableModel) this.tableRecordset.getModel();

		// 由于删除操作会导致表格行序号发生改变，因此如果先删除了小序号的行，就会导致大序号的行序号变小，从而在删除该行的时候发生异常。所以保证从大到小的序号进行删除操作。
		for (int i = rows.length - 1; i >= 0; i--) {
			int row = rows[i];
			FieldData fieldInfo = tableModel.getRowData(row);
			if (fieldInfo != null && !fieldInfo.isSystemField()) {
				tableModel.removeRow(row);
				this.modifields.add(new ModifiedData(fieldInfo, ModifiedData.DELETE));
			}
		}
	}

	private void buttonResetClicked() {
		try {

			this.modifields.clear();
			fillComponents();
			this.isCellValueChange = false;
			setComponentsEnabled();

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void buttonApplyClicked() {
		try {
			RecordsetPropertyTableModel tableModel = (RecordsetPropertyTableModel) this.tableRecordset.getModel();

			// 应用属性的修改
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				FieldData fieldData = tableModel.getRowData(i);
				fieldData.applyChange();
			}

			// 应用字段的添加删除
			for (int i = 0; i < this.modifields.size(); i++) {
				ModifiedData modifiedData = this.modifields.get(i);
				if (modifiedData.getModifiedType() == ModifiedData.ADD) {
					int result = this.datasetVector.getFieldInfos().add(modifiedData.getFieldData().getFieldInfo());

					if (result > 0) {
						Application.getActiveApplication().getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_AddFieldSucceed"), modifiedData.getFieldData().getName()));
					} else {
						Application.getActiveApplication().getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_AddFieldFailed"), modifiedData.getFieldData().getName()));
					}
				} else if (modifiedData.getModifiedType() == ModifiedData.DELETE) {
					boolean result = this.datasetVector.getFieldInfos().remove(modifiedData.getFieldData().getName());

					if (result) {
						Application.getActiveApplication().getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_DeleteFieldSucceed"), modifiedData.getFieldData().getName()));
					} else {
						Application.getActiveApplication().getOutput()
								.output(MessageFormat.format(ControlsProperties.getString("String_DeleteFieldFailed"), modifiedData.getFieldData().getName()));
					}
				}
			}

			// 先移除所有行，再重新添加。如果不移出直接更换 Model，操作过程中将无法获取到正确的的 SelectedRow。
			tableModel.removeAllRows();
			tableModel.intializeRows(this.datasetVector.getFieldInfos());

			for (int i = 0; i < Application.getActiveApplication().getMainFrame().getFormManager().getCount(); i++) {
				IForm form = Application.getActiveApplication().getMainFrame().getFormManager().get(i);

				if (form instanceof IFormTabular && ((IFormTabular) form).getRecordset().getDataset() == this.datasetVector) {
					// 刷新已打开的当前修改数据的属性表，不同的窗口绑定不同的表格
					Recordset recordset = this.datasetVector.getRecordset(false, CursorType.DYNAMIC);
					((IFormTabular) form).setRecordset(recordset);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.isCellValueChange = false;
			this.modifields.clear();
			setComponentsEnabled();
		}
	}

	private void tableSelectionChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			RecordsetPropertyTableModel tableModel = (RecordsetPropertyTableModel) this.tableRecordset.getModel();

			boolean canRemove = true;
			int[] selectedRows = this.tableRecordset.getSelectedRows();
			for (int i = 0; i < selectedRows.length; i++) {
				FieldData field = tableModel.getRowData(selectedRows[i]);

				if (field.isSystemField()) {
					canRemove = false;
					break;
				}
			}

			this.buttonDelete.setEnabled(canRemove);
		}
	}

	private void checkBoxShowWarningClicked() {
		this.showWarning = this.checkBoxShowWarning.isSelected();
	}

	private class ModifiedData {
		public static final int ADD = 1;
		public static final int DELETE = 2;
		private FieldData fieldData;
		private int modifiedType;

		public ModifiedData(FieldData fieldData, int modifiedType) {
			this.fieldData = fieldData;
			this.modifiedType = modifiedType;
		}

		public FieldData getFieldData() {
			return this.fieldData;
		}

		public int getModifiedType() {
			return this.modifiedType;
		}
	}

	private class RecordsetPropertyTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private static final String DEFAULT_FIELDNAME = "NewField";
		private static final int INDEX = 0;
		private static final int FIELD_NAME = 1;
		private static final int FIELD_CAPTION = 2;
		private static final int FIELD_TYPE = 3;
		private static final int MAX_LENGTH = 4;
		private static final int DEFAULT_VALUE = 5;
		private static final int IS_REQUIRED = 6;

		private ArrayList<FieldData> fieldInfos = new ArrayList<FieldData>();
		private ArrayList<Integer> uneditableRows = new ArrayList<Integer>(); // 从 DatasetVector 中取出来的 FieldInfo 不可编辑（Caption 除外）
		private ArrayList<Integer> captionModifiedRows = new ArrayList<>(); // 主动修改过 caption 的行。没有主动修改过的 caption 随 name 的改变而改变

		public RecordsetPropertyTableModel() {
			// 默认实现
		}

		// 初始化内容使用的方法
		public void intializeRows(FieldInfos fieldInfos) {
			this.fieldInfos.clear();
			this.uneditableRows.clear();
			this.captionModifiedRows.clear();
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				this.fieldInfos.add(new FieldData(fieldInfos.get(i)));
				if (fieldInfos.get(i).isSystemField()) {
					this.getRowData(i).setName("*" + this.getRowData(i).getName());
				}
				this.uneditableRows.add(i);
			}
			fireTableRowsInserted(0, this.fieldInfos.size() - 1);
		}

		public void removeAllRows() {
			int removeRowCount = this.fieldInfos.size();

			if (removeRowCount > 0) {
				this.fieldInfos.clear();
				fireTableRowsDeleted(0, removeRowCount - 1);
			}
		}

		public void removeRow(int row) {
			Object oldValue = this.fieldInfos.remove(row);
			if (oldValue != null) {
				fireTableRowsDeleted(row, row);
			}
		}

		public FieldData addRow() {
			FieldData fieldInfo = new FieldData();
			fieldInfo.setName(getAvailableFieldName(DEFAULT_FIELDNAME));
			fieldInfo.setCaption(fieldInfo.getName());

			if (this.fieldInfos.add(fieldInfo)) {
				fireTableRowsInserted(this.fieldInfos.size() - 1, this.fieldInfos.size() - 1);
			} else {
				fieldInfo = null;
			}
			return fieldInfo;
		}

		public FieldData getRowData(int rowIndex) {
			return this.fieldInfos.get(rowIndex);
		}

		/**
		 * Returns true regardless of parameter values.
		 *
		 * @param row
		 *            the row whose value is to be queried
		 * @param column
		 *            the column whose value is to be queried
		 * @return true
		 * @see #setValueAt
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			if (column == INDEX) {
				return false;
			}

			if (column == FIELD_CAPTION && !datasetVector.isReadOnly()) {
				return true;
			}

			if (this.uneditableRows.contains(row)) {
				return false;
			}

			if (column == FIELD_NAME || column == IS_REQUIRED || column == FIELD_TYPE || column == DEFAULT_VALUE) {
				return true;
			}

			if ((this.fieldInfos.get(row).getType() == FieldType.TEXT || this.fieldInfos.get(row).getType() == FieldType.WTEXT) && column == MAX_LENGTH) {
				return true;
			}

			return false;
		}

		@Override
		public int getRowCount() {
			return this.fieldInfos.size();
		}

		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex + 1 > this.fieldInfos.size()) {
				return null;
			}

			FieldData fieldInfo = this.fieldInfos.get(rowIndex);
			if (columnIndex == INDEX) {
				return rowIndex + 1;
			} else if (columnIndex == FIELD_NAME) {
				return fieldInfo.getName();
			} else if (columnIndex == FIELD_CAPTION) {
				return fieldInfo.getCaption();
			} else if (columnIndex == FIELD_TYPE) {
				return FieldTypeUtilties.getFieldTypeName(fieldInfo.getType());
			} else if (columnIndex == MAX_LENGTH) {
				return fieldInfo.getMaxLength();
			} else if (columnIndex == DEFAULT_VALUE) {
				if (fieldInfo.getDefaultValue() == null) {
					return CommonProperties.getString(CommonProperties.NULL);
				} else {
					return fieldInfo.getDefaultValue();
				}
			} else if (columnIndex == IS_REQUIRED) {
				return fieldInfo.isRequired();
			} else {
				return null;
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex + 1 > this.fieldInfos.size()) {
				return;
			}

			if (columnIndex != DEFAULT_VALUE && aValue == null) {
				return;
			}

			try {
				if (columnIndex == INDEX) {
					return;
				} else if (columnIndex == FIELD_NAME) {
					String newName = getAvailableFieldName(String.valueOf(aValue));
					this.fieldInfos.get(rowIndex).setName(newName);

					// 如果 caption 的主动修改记录列表不包含当前修改记录，那么联动更新 caption
					if (!this.captionModifiedRows.contains(rowIndex)) {
						this.fieldInfos.get(rowIndex).setCaption(newName);
						fireTableCellUpdated(rowIndex, FIELD_CAPTION);
						fireTableCellValueChange(new TableCellValueChangeEvent(this, rowIndex, FIELD_CAPTION));
					}
				} else if (columnIndex == FIELD_CAPTION) {
					String oldCaption = this.fieldInfos.get(rowIndex).getCaption();
					if (!oldCaption.equals(String.valueOf(aValue))) {
						this.fieldInfos.get(rowIndex).setCaption(String.valueOf(aValue));

						// 添加到主动修改列表
						if (!this.captionModifiedRows.contains(rowIndex)) {
							this.captionModifiedRows.add(rowIndex);
						}
					}
				} else if (columnIndex == FIELD_TYPE) {
					this.fieldInfos.get(rowIndex).setType(FieldTypeUtilties.getFieldType((String) aValue));
					this.fieldInfos.get(rowIndex).setMaxLength(FieldTypeUtilties.getFieldTypeMaxLength(FieldTypeUtilties.getFieldType((String) aValue)));
					this.fireTableDataChanged();
				} else if (columnIndex == MAX_LENGTH) {
					this.fieldInfos.get(rowIndex).setMaxLength(Integer.valueOf(aValue.toString()));
				} else if (columnIndex == DEFAULT_VALUE) {
					FieldData fieldInfo = this.fieldInfos.get(rowIndex);
					if (aValue == null) {
						if (fieldInfo.isRequired()) {
							return;
						} else {
							fieldInfo.setDefaultValue(null);
						}
					} else if (aValue.toString().equalsIgnoreCase(CommonProperties.getString(CommonProperties.NULL))) {
						fieldInfo.setDefaultValue(null);
					} else {
						fieldInfo.setDefaultValue(aValue.toString());
					}
				} else if (columnIndex == IS_REQUIRED) {
					Boolean isRequired = Boolean.valueOf(aValue.toString());
					this.fieldInfos.get(rowIndex).setRequired(isRequired);
					if (isRequired && StringUtilties.isNullOrEmpty(this.fieldInfos.get(rowIndex).getDefaultValue())) {
						this.fieldInfos.get(rowIndex).setDefaultValue("0");
						fireTableCellUpdated(rowIndex, DEFAULT_VALUE);
						fireTableCellValueChange(new TableCellValueChangeEvent(this, rowIndex, DEFAULT_VALUE));
					}
				}
				fireTableCellUpdated(rowIndex, columnIndex);
				fireTableCellValueChange(new TableCellValueChangeEvent(this, rowIndex, columnIndex));
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		@Override
		public String getColumnName(int column) {
			if (column == INDEX) {
				return CommonProperties.getString(CommonProperties.Index);
			} else if (column == FIELD_NAME) {
				return CommonProperties.getString(CommonProperties.FieldName);
			} else if (column == FIELD_CAPTION) {
				return CommonProperties.getString(CommonProperties.Caption);
			} else if (column == FIELD_TYPE) {
				return CommonProperties.getString(CommonProperties.FieldType);
			} else if (column == MAX_LENGTH) {
				return CommonProperties.getString(CommonProperties.Length);
			} else if (column == DEFAULT_VALUE) {
				return CommonProperties.getString(CommonProperties.DefaultValue);
			} else if (column == IS_REQUIRED) {
				return CommonProperties.getString(CommonProperties.IsRequired);
			} else {
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == FIELD_NAME) {
				return String.class;
			} else if (columnIndex == FIELD_CAPTION) {
				return String.class;
			} else if (columnIndex == FIELD_TYPE) {
				return FieldType.class;
			} else if (columnIndex == MAX_LENGTH) {
				return Integer.class;
			} else if (columnIndex == DEFAULT_VALUE) {
				return String.class;
			} else if (columnIndex == IS_REQUIRED) {
				return Boolean.class;
			} else {
				return Object.class;
			}
		}

		public void addTableCellValueChangeListener(TableCellValueChangeListener listener) {
			listenerList.add(TableCellValueChangeListener.class, listener);
		}

		public void removeTableCellValueChangeListener(TableCellValueChangeListener listener) {
			listenerList.remove(TableCellValueChangeListener.class, listener);
		}

		private void fireTableCellValueChange(TableCellValueChangeEvent e) {
			Object[] listeners = listenerList.getListenerList();

			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == TableCellValueChangeListener.class) {
					((TableCellValueChangeListener) listeners[i + 1]).tableCellValueChange(e);
				}
			}
		}

		private String getAvailableFieldName(String name) {
			String availableName = datasetVector.getAvailableFieldName(name);
			String resultName = availableName;

			int suffix = 0;
			label: for (int i = 0; i < this.fieldInfos.size(); i++) {
				if (resultName.equalsIgnoreCase(this.fieldInfos.get(i).getName())) {
					suffix++;
					resultName = MessageFormat.format("{0}_{1}", availableName, suffix);
					continue label;
				}
			}
			return resultName;
		}
	}

	/**
	 * 由于在操作过程中可以对每一个字段的各个属性进行更改，而使用 FieldInfo 将会直接写到字段里，从而不能重置撤销操作，并且还慢，因此本类用来封装没行绑定的字段信息。
	 * 
	 * @author highsad
	 *
	 */
	private class FieldData {
		private boolean isSystemField = false;
		private String name = "";
		private String caption = "";
		private FieldType type = FieldType.TEXT;
		private int maxLength = 0;
		private String defaultValue = "";
		private boolean isRequired = false;
		private FieldInfo fieldInfo;

		private boolean isNew = false; // 是否是新建字段

		public FieldData(FieldInfo fieldInfo) {
			initialize(fieldInfo);
		}

		public FieldData() {
			initialize(new FieldInfo());
			this.isNew = true;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public FieldType getType() {
			return type;
		}

		public void setType(FieldType type) {
			this.type = type;
		}

		public int getMaxLength() {
			return maxLength;
		}

		public void setMaxLength(int maxLength) {
			this.maxLength = maxLength;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public boolean isRequired() {
			return isRequired;
		}

		public void setRequired(boolean isRequired) {
			this.isRequired = isRequired;
		}

		public boolean isSystemField() {
			return isSystemField;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public FieldInfo getFieldInfo() {
			return this.fieldInfo;
		}

		/**
		 * 不是新建的字段，只能更改应用 Caption 属性。
		 */
		public void applyChange() {
			if (isNew) {
				if (!this.fieldInfo.getName().equals(this.name)) {
					this.fieldInfo.setName(this.name);
				}
				if (!this.fieldInfo.getCaption().equals(this.caption)) {
					this.fieldInfo.setCaption(this.caption);
				}
				if (this.fieldInfo.getType() != this.type) {
					this.fieldInfo.setType(this.type);
				}
				if (this.fieldInfo.getMaxLength() != this.maxLength) {
					this.fieldInfo.setMaxLength(this.maxLength);
				}
				if (!isDefaultValueEquals()) {
					this.fieldInfo.setDefaultValue(this.defaultValue);
				}
				if (this.fieldInfo.isRequired() != this.isRequired) {
					this.fieldInfo.setRequired(this.isRequired);
				}
			} else {
				if (!this.fieldInfo.getCaption().equals(this.caption)) {
					this.fieldInfo.setCaption(this.caption);
				}
			}
		}

		private boolean isDefaultValueEquals() {
			return (this.fieldInfo.getDefaultValue() != null && this.fieldInfo.getDefaultValue().equals(this.defaultValue))
					|| (this.fieldInfo.getDefaultValue() == null && this.defaultValue == null);
		}

		private void initialize(FieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
			this.isSystemField = fieldInfo.isSystemField() || "smuserid".equalsIgnoreCase(fieldInfo.getName());
			this.name = fieldInfo.getName();
			this.caption = fieldInfo.getCaption();
			this.type = fieldInfo.getType();
			this.maxLength = fieldInfo.getMaxLength();
			this.defaultValue = fieldInfo.getDefaultValue();
			this.isRequired = fieldInfo.isRequired();
		}
	}
}
