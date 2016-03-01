package com.supermap.desktop.mapview.geometry.property;

import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.Recordset;
import com.supermap.desktop.Application;
import com.supermap.desktop.RecordsetFinalizer;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.property.AbstractPropertyControl;
import com.supermap.desktop.enums.PropertyType;
import com.supermap.desktop.event.TableCellValueChangeEvent;
import com.supermap.desktop.event.TableCellValueChangeListener;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilties.FieldTypeUtilties;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 对象属性信息
 */
public class GeometryRecordsetPropertyControl extends AbstractPropertyControl {

	private static final long serialVersionUID = 1L;
	private JCheckBox checkBoxHideSysField;
	private JCheckBox checkBoxHideDetail;
	private JButton buttonReset;
	private JButton buttonApply;
	private transient Recordset recordset;
	private JTable propertyTable;
	private PropertyTableModel propertyTableModel;
	private transient FieldInfos fieldInfos;
	private final static int COLUMN_WIDTH = 140;
	private final static int ROW_HEIGHT = 24;
	private static final Color COLOR_SELECTED = new Color(185, 214, 244);
	private static final Color COLOR_SYSTEM_NOT_SELECTED = new Color(230, 230, 230);
	private static final Color COLOR_EDITABLE_NOT_SELECTED = new Color(247, 247, 247);

	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == GeometryRecordsetPropertyControl.this.buttonReset) {
				buttonResetClicked();
			} else if (e.getSource() == GeometryRecordsetPropertyControl.this.buttonApply) {
				buttonApplyClicked();
			}
		}
	};

	private ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == GeometryRecordsetPropertyControl.this.checkBoxHideDetail) {
				checkBoxHideDetailClicked();
			} else if (e.getSource() == GeometryRecordsetPropertyControl.this.checkBoxHideSysField) {
				checkBoxHideSysFieldClicked();
			}
		}
	};

	private TableCellValueChangeListener tableCellValueChangeListener = new TableCellValueChangeListener() {

		@Override
		public void tableCellValueChange(TableCellValueChangeEvent e) {
			setTableCellValueChange();
		}
	};

	// 表格状态变化时，设置应用和重置按钮的置灰状态
	private void setTableCellValueChange() {
		Object recordsetValue;
		Object propertyValue;
		propertyTableModel.setCellValueChange(false);
		for (int i = 0; i < propertyTableModel.getRowCount(); i++) {
			propertyValue = propertyTableModel.getRowData(i).getFieldValue();
			if (propertyTableModel.getRowData(i).isSystemField || propertyTableModel.getRowData(i).getType() == FieldType.LONGBINARY) {
				recordsetValue = recordset.getFieldValue(propertyTableModel.getRowData(i).getName().substring(1));
			} else {
				recordsetValue = recordset.getFieldValue(propertyTableModel.getRowData(i).getName());
			}

			if (recordsetValue != null) {
				if (propertyValue != null) {
					if (propertyTableModel.getRowData(i).getType() != FieldType.DATETIME) {
						if (!recordsetValue.equals(propertyValue)) {
							propertyTableModel.setCellValueChange(true);
						}
					} else {
						SimpleDateFormat dateFormat = new SimpleDateFormat(PropertyTableModel.DATE_STYLE);
						String dateStyle = dateFormat.format((Date) recordsetValue);
						if (!dateStyle.equalsIgnoreCase(propertyValue.toString())) {
							propertyTableModel.setCellValueChange(true);
						}
					}
				} else {
					propertyTableModel.setCellValueChange(true);
				}

			} else {
				if (propertyValue != null && !"".equalsIgnoreCase(propertyValue.toString())) {
					propertyTableModel.setCellValueChange(true);
				}
			}
		}
		setComponentsEnabled();
	}

	public GeometryRecordsetPropertyControl(Recordset recordset) {
		super(ControlsProperties.getString("String_PropertyInfo"));
		initializeComponents();
		initResources();
		setRecordset(recordset);
	}

	public void setRecordset(Recordset recordset) {
		if (this.recordset != null) {
			RecordsetFinalizer.INSTANCE.closeRecordset(this.recordset);
		}
		this.recordset = recordset;
		this.fieldInfos = recordset.getFieldInfos();
		unregisterEvents();
		fillTableProperty();
		setPropertyTable();
		registerEvents();
	}

	/**
	 * 初始化对象属性控件
	 */
	private void initializeComponents() {

		this.propertyTable = new PropertyTable();
		this.propertyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.propertyTable.setRowHeight(ROW_HEIGHT);
		JScrollPane scrollPane = new JScrollPane(this.propertyTable);
		this.checkBoxHideSysField = new JCheckBox("Hidden_SystemField");
		this.checkBoxHideSysField.setSelected(true);
		this.checkBoxHideDetail = new JCheckBox("Hidden_DetailInfo");
		this.checkBoxHideDetail.setSelected(true);
		this.buttonReset = new JButton("reset");
		this.buttonApply = new JButton("apply");

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		setLayout(layout);

		// @formatter:off
          layout.setHorizontalGroup( layout
                    .createParallelGroup(Alignment.LEADING )
                    .addComponent( scrollPane)
                    .addGroup(
                              layout.createSequentialGroup()
                                        .addComponent(this.checkBoxHideSysField )
                                        .addComponent(this.checkBoxHideDetail )
                                        .addGap(10, 10, Short.MAX_VALUE )
                                        .addComponent(this.buttonReset )
                                        .addComponent(this.buttonApply )));
          layout.setVerticalGroup( layout
                    .createSequentialGroup()
                    .addComponent( scrollPane)
                    .addGroup(
                              layout.createParallelGroup(Alignment.CENTER)
                                        .addComponent(this.checkBoxHideSysField )
                                        .addComponent(this.checkBoxHideDetail )
                                        .addComponent(this.buttonReset )
                                        .addComponent(this.buttonApply )));
          // @formatter:on
	}

	private void initResources() {
		this.checkBoxHideSysField.setText(ControlsProperties.getString("String_Hidden_SystemField"));
		this.checkBoxHideDetail.setText(ControlsProperties.getString("String_DetailInfo"));
		this.buttonApply.setText(CommonProperties.getString(CommonProperties.Apply));
		this.buttonReset.setText(CommonProperties.getString(CommonProperties.Reset));
	}

	private void registerEvents() {
		this.buttonApply.addActionListener(this.actionListener);
		this.buttonReset.addActionListener(this.actionListener);
		this.checkBoxHideDetail.addItemListener(this.itemListener);
		this.checkBoxHideSysField.addItemListener(this.itemListener);
		if (this.propertyTable.getModel() instanceof PropertyTableModel) {
			((PropertyTableModel) this.propertyTable.getModel()).addTableCellValueChangeListener(this.tableCellValueChangeListener);
		}
	}

	private void unregisterEvents() {
		this.buttonApply.removeActionListener(this.actionListener);
		this.buttonReset.removeActionListener(this.actionListener);
		this.checkBoxHideDetail.removeItemListener(this.itemListener);
		this.checkBoxHideSysField.removeItemListener(this.itemListener);
		if (this.propertyTable.getModel() instanceof PropertyTableModel) {
			((PropertyTableModel) this.propertyTable.getModel()).removeTableCellValueChangeListener(this.tableCellValueChangeListener);
		}
	}

	private void fillTableProperty() {
		this.propertyTableModel = new PropertyTableModel();
		propertyTableModel.setHiddenDetailed(this.checkBoxHideDetail.isSelected());
		propertyTableModel.setHiddenSysField(this.checkBoxHideSysField.isSelected());
		this.propertyTable.setModel(this.propertyTableModel);
		fillTableData();

	}

	/**
	 * 对属性表信息进行设置
	 */
	private void setPropertyTable() {
		this.propertyTable.getColumnModel().getColumn(((PropertyTableModel) this.propertyTable.getModel()).getColumnCount() - 1)
				.setCellEditor(new PropertyTableCellEditor(new JTextField()));
		setColumnSize();
		setComponentsEnabled();
	}

	/**
	 * 加载模型数据
	 */
	private void fillTableData() {
		((PropertyTableModel) this.propertyTable.getModel()).intializeModelData(this.fieldInfos, this.recordset);
	}

	/**
	 * 设置每一列固定宽度
	 */
	private void setColumnSize() {
		TableColumnModel columnModel = this.propertyTable.getColumnModel();
		for (int i = 0; i < this.propertyTable.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(COLUMN_WIDTH);
		}
	}

	private void buttonResetClicked() {
		((PropertyTableModel) propertyTable.getModel()).resetClick();
		setComponentsEnabled();
	}

	private void buttonApplyClicked() {
		((PropertyTableModel) propertyTable.getModel()).applyClick();
		setComponentsEnabled();
	}

	/**
	 * 隐藏显示详细信息
	 */
	private void checkBoxHideDetailClicked() {
		if (this.propertyTable.getCellEditor() != null) {
			this.propertyTable.getCellEditor().stopCellEditing();
		}
		checkIsSelected();
		this.propertyTable.getColumnModel().getColumn(((PropertyTableModel) this.propertyTable.getModel()).getColumnCount() - 1)
				.setCellEditor(new PropertyTableCellEditor(new JTextField()));
		setColumnSize();
	}

	/**
	 * 隐藏显示系统字段
	 */
	private void checkBoxHideSysFieldClicked() {
		if (this.propertyTable.getCellEditor() != null) {
			this.propertyTable.getCellEditor().stopCellEditing();
		}
		((PropertyTableModel) this.propertyTable.getModel()).setHiddenSysField(this.checkBoxHideSysField.isSelected());
		((PropertyTableModel) this.propertyTable.getModel()).intializeModelData(this.fieldInfos, this.recordset);
	}

	private void checkIsSelected() {

		((PropertyTableModel) this.propertyTable.getModel()).setHiddenDetailed(this.checkBoxHideDetail.isSelected());
		((PropertyTableModel) this.propertyTable.getModel()).fireTableStructureChanged();
	}

	private void setComponentsEnabled() {
		this.buttonApply.setEnabled(this.propertyTableModel.isCellValueChange());
		this.buttonReset.setEnabled(this.propertyTableModel.isCellValueChange());
	}

	/**
	 * @author liu 创建内部类，构建自身布局模板
	 */
	class PropertyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private ArrayList<FieldData> fieldDataInfos = new ArrayList<FieldData>();
		private ArrayList<FieldData> fieldDataInfosTemp = new ArrayList<FieldData>();
		private static final int FIELD_NAME = 0;
		private static final int FIELD_CAPTION = 1;
		private static final int FIELD_TYPE = 2;
		private static final int IS_REQUIRED = 3;
		private static final int FIELD_VALUE = 4;
		private final static String DATE_STYLE = "yyyy-MM-dd HH:mm:ss";

		private transient FieldInfos fieldInfos;
		private boolean isCellValueChange = false;
		private boolean hiddenSysField = true;
		private boolean hiddenDetailed = true;

		public boolean isHiddenSysField() {
			return hiddenSysField;
		}

		public void setHiddenSysField(boolean hiddenSysField) {
			this.hiddenSysField = hiddenSysField;
		}

		public boolean isHiddenDetailed() {
			return this.hiddenDetailed;
		}

		public void setHiddenDetailed(boolean hiddenDetailed) {
			this.hiddenDetailed = hiddenDetailed;
		}

		public PropertyTableModel() {
			// 默认实现
		}

		/**
		 * @param fieldInfos
		 * @param recordset 初始化Model数据时
		 */
		public void intializeModelData(FieldInfos fieldInfos, Recordset recordset) {
			this.fieldInfos = fieldInfos;
			this.fieldDataInfosTemp = (ArrayList<GeometryRecordsetPropertyControl.FieldData>) fieldDataInfos.clone();
			this.fieldDataInfos.clear();
			FieldInfo fieldInfo;

			for (int i = 0; i < fieldInfos.getCount(); i++) {
				fieldInfo = fieldInfos.get(i);
				FieldData fieldData = new FieldData(fieldInfo, recordset);
				if (!hiddenSysField) {
					this.fieldDataInfos.add(fieldData);
					if (fieldData.isSystemField || fieldData.getType() == FieldType.LONGBINARY) {
						fieldData.setName("*" + fieldData.getName());
					}
				} else {
					if (!fieldData.isSystemField && fieldData.getType() != FieldType.LONGBINARY) {
						this.fieldDataInfos.add(fieldData);
					}
				}
			}
			saveSysValue();
			setDateStyle();
			fireTableRowsInserted(0, this.fieldDataInfos.size() - 1);
		}

		private void saveSysValue() {
			for (int i = 0; i < fieldDataInfosTemp.size(); i++) {
				String fieldDataNameTemp;
				String fieldDataName;
				Object fieldDataValueTemp = fieldDataInfosTemp.get(i).getFieldValue();
				fieldDataNameTemp = fieldDataInfosTemp.get(i).getName();
				for (int j = 0; j < fieldDataInfos.size(); j++) {
					fieldDataName = fieldDataInfos.get(j).getName();
					if (fieldDataNameTemp.equals(fieldDataName)) {
						fieldDataInfos.get(j).setFieldValue(fieldDataValueTemp);
					}
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount() 获取行数
		 */
		@Override
		public int getRowCount() {
			int count = 0;
			if (hiddenSysField) {
				for (int i = 0; i < fieldDataInfos.size(); i++) {
					FieldData fieldData = fieldDataInfos.get(i);
					if (fieldData.isSystemField) {
						count++;
					}
				}
				return fieldDataInfos.size() - count;

			} else {
				return this.fieldDataInfos.size();

			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount() 获取列数
		 */
		@Override
		public int getColumnCount() {
			if (!hiddenDetailed) {
				return 5;
			} else {
				return 2;
			}
		}

		/**
		 * 设置日期格式
		 */
		private void setDateStyle() {
			for (int i = 0; i < getRowCount(); i++) {
				Object fieldValue = getRowData(i).getFieldValue();
				if (this.getRowData(i).getType() == FieldType.DATETIME) {
					try {
						if (fieldValue != null) {
							SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STYLE);
							Object timeValue = dateFormat.format((Date) fieldValue);
							setValueAt(timeValue, i, getColumnCount() - 1);
						} else {
							setValueAt(null, i, getColumnCount() - 1);
						}
					} catch (Exception e) {
						// 默认实现
						setValueAt(fieldValue, i, getColumnCount() - 1);
					}
				}
			}
		}

		/**
		 * @param rowIndex
		 * @return 获得行数据
		 */
		private FieldData getRowData(int rowIndex) {
			return this.fieldDataInfos.get(rowIndex);
		}

		/**
		 * @param column
		 * @return 设置表的列名
		 */
		@Override
		public String getColumnName(int column) {
			if (!hiddenDetailed) {
				if (column == FIELD_NAME) {
					return CommonProperties.getString(CommonProperties.FieldName);
				} else if (column == FIELD_CAPTION) {
					return CommonProperties.getString(CommonProperties.Caption);
				} else if (column == FIELD_TYPE) {
					return CommonProperties.getString(CommonProperties.FieldType);
				} else if (column == IS_REQUIRED) {
					return CommonProperties.getString(CommonProperties.IsRequired);
				} else if (column == FIELD_VALUE) {
					return CommonProperties.getString(CommonProperties.FieldValue);
				}
			} else {
				if (column == FIELD_NAME) {
					return CommonProperties.getString(CommonProperties.Caption);
				} else if (column == getColumnCount() - 1) {
					return CommonProperties.getString(CommonProperties.FieldValue);
				}
			}
			return null;
		}

		/**
		 * @param rowIndex
		 * @param columnIndex
		 * @return 获取单元格值
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			FieldData fieldDataInfo = this.fieldDataInfos.get(rowIndex);
			if (!hiddenDetailed) {
				if (columnIndex == FIELD_NAME) {
					return fieldDataInfo.getName();
				} else if (columnIndex == FIELD_CAPTION) {
					return fieldDataInfo.getCaption();
				} else if (columnIndex == FIELD_TYPE) {
					return FieldTypeUtilties.getFieldTypeName(fieldDataInfo.getType());
				} else if (columnIndex == IS_REQUIRED) {
					return fieldDataInfo.isRequired();
				} else if (columnIndex == FIELD_VALUE) {
					return fieldDataInfo.getFieldValue();
				}
			} else {
				if (columnIndex == FIELD_NAME) {
					return fieldDataInfo.getCaption();
				} else if (columnIndex == getColumnCount() - 1) {
					return fieldDataInfo.getFieldValue();
				}

			}
			fireTableDataChanged();
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int) 单元格设值
		 */
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex + 1 > this.fieldDataInfos.size()) {
				return;
			}

			try {
				if (columnIndex == getColumnCount() - 1) {
					handleFieldValueType(aValue, rowIndex);
				}
				fireTableCellValueChange(new TableCellValueChangeEvent(this, rowIndex, columnIndex));
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		private void handleFieldValueType(Object aValue, int rowIndex) {
			FieldType fieldType = this.fieldDataInfos.get(rowIndex).getType();
			try {
				if (fieldType == FieldType.BOOLEAN) {
					if (aValue != null) {
						if ("true".equalsIgnoreCase(aValue.toString()) || "false".equalsIgnoreCase(aValue.toString())) {
							this.fieldDataInfos.get(rowIndex).setFieldValue(aValue.toString());
						}
					} else {
						this.fieldDataInfos.get(rowIndex).setFieldValue(null);
					}
					this.fieldDataInfos.get(rowIndex).setFieldValue(Boolean.parseBoolean(aValue.toString()));
				} else if (fieldType == FieldType.INT64) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(Long.parseLong(aValue.toString()));
				} else if (fieldType == FieldType.INT32) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(Integer.parseInt(aValue.toString()));
				} else if (fieldType == FieldType.INT16) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(Short.parseShort(aValue.toString()));
				} else if (fieldType == FieldType.DOUBLE) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(Double.parseDouble(aValue.toString()));
				} else if (fieldType == FieldType.SINGLE) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(Float.parseFloat(aValue.toString()));
				} else if (fieldType == FieldType.BYTE) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(Byte.parseByte(aValue.toString()));
				} else if (fieldType == FieldType.DATETIME) {
					SimpleDateFormat dateformat = new SimpleDateFormat(DATE_STYLE);
					if (null != dateformat.parse(aValue.toString())) {
						this.fieldDataInfos.get(rowIndex).setFieldValue(dateformat.format(dateformat.parse(aValue.toString())));
					}
				} else if (fieldType == FieldType.CHAR || fieldType == FieldType.TEXT || fieldType == FieldType.WTEXT) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(aValue);
				} else if (fieldType == FieldType.LONGBINARY) {
					this.fieldDataInfos.get(rowIndex).setFieldValue(aValue);
				}
			} catch (Exception e) {
				this.fieldDataInfos.get(rowIndex).setFieldValue(fieldDataInfos.get(rowIndex).getFieldValue());
			}

		}

		private void fireTableCellValueChange(TableCellValueChangeEvent e) {
			Object[] listeners = listenerList.getListenerList();

			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == TableCellValueChangeListener.class) {
					((TableCellValueChangeListener) listeners[i + 1]).tableCellValueChange(e);
				}
			}
		}

		/*
		 * (non- Javadoc) 设置单元格编辑
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			FieldData fieldData = getRowData(rowIndex);
			return setCellEditor(columnIndex, fieldData);

		}

		private boolean setCellEditor(int columnIndex, FieldData fieldData) {
			if (recordset.getDataset().isReadOnly()) {
				return false;
			} else {
				if (columnIndex == getColumnCount() - 1) {
					if (!hiddenSysField) {
						if (fieldData.isSystemField) {
							return false;
						} else {
							if (fieldData.getType() == FieldType.LONGBINARY) {
								return false;
							} else {
								return true;
							}
						}

					} else {
						if (fieldData.getType() == FieldType.LONGBINARY) {
							return false;
						} else {
							return true;
						}
					}
				}
			}
			return false;
		}

		/**
		 * @param listener 设置单元格值改变监听
		 */
		public void addTableCellValueChangeListener(TableCellValueChangeListener listener) {
			listenerList.add(TableCellValueChangeListener.class, listener);
		}

		public void removeTableCellValueChangeListener(TableCellValueChangeListener listener) {
			listenerList.remove(TableCellValueChangeListener.class, listener);
		}

		/**
		 * @param columnIndex
		 * @return 设置列对象类型
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (!hiddenDetailed && columnIndex == IS_REQUIRED) {

				return Boolean.class;
			}
			return Object.class;
		}

		/**
		 * 应用按钮点击
		 */
		public void applyClick() {
			for (int i = 0; i < this.getRowCount(); i++) {
				FieldData fieldData = this.fieldDataInfos.get(i);
				fieldData.apply();
			}
			this.isCellValueChange = false;
		}

		/**
		 * 重置按钮点击
		 */
		public void resetClick() {
			String filedDataName;
			for (int i = 0; i < this.getRowCount(); i++) {
				if (this.getRowData(i).isSystemField || this.getRowData(i).getType() == FieldType.LONGBINARY) {
					filedDataName = this.getRowData(i).getName().substring(1);
				} else {
					filedDataName = this.getRowData(i).getName();
				}
				this.getRowData(i).setFieldValue(recordset.getFieldValue(filedDataName));
				setDateStyle();
			}
			this.isCellValueChange = false;
		}

		public boolean isCellValueChange() {
			return this.isCellValueChange;
		}

		public void setCellValueChange(boolean isCellValueChange) {
			this.isCellValueChange = isCellValueChange;
		}
	}

	/**
	 * @author liu 设置单元格编辑器
	 */
	class PropertyTableCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		private JTextField textField;
		private int row;
		private JComboBox<Object> comboBox;

		public PropertyTableCellEditor(JTextField textField) {
			super(textField);
			this.textField = textField;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.row = row;

			// 字段值为布尔类型时，设置单元格为JComboBox类型
			if (propertyTableModel.getRowData(row).getType() == FieldType.BOOLEAN) {
				if (!propertyTableModel.getRowData(row).isRequired) {
					comboBox = new JComboBox<Object>(new String[] { "", "true", "false" });
					if (value != null && !"".equalsIgnoreCase(value.toString())) {
						this.comboBox.setSelectedItem(value);
					} else {
						this.comboBox.setSelectedItem("");
					}
				} else {
					this.comboBox = new JComboBox<Object>(new String[] { "true", "false" });
					this.comboBox.setSelectedItem(value);
				}
				return this.comboBox;
			} else {
				// 设置当前textField的值，如果不设置将会显示前一个textField的值
				if (value != null) {
					this.textField.setText(String.valueOf(value));
				} else {
					this.textField.setText("");
				}
				return this.textField;
			}
		}

		/*
		 * (non-Javadoc) 获取单元格的值，字段值为布尔类型时，设置显示值，同时返回其他类型的值
		 * 
		 * @see javax.swing.DefaultCellEditor#getCellEditorValue()
		 */
		@Override
		public Object getCellEditorValue() {
			if (propertyTableModel.getRowData(row).getType() == FieldType.BOOLEAN) {
				if ("".equalsIgnoreCase(this.comboBox.getSelectedItem().toString())) {
					return null;
				} else {
					return Boolean.parseBoolean(this.comboBox.getSelectedItem().toString());
				}
			} else {
				return this.textField.getText();
			}
		}
	}

	/**
	 * @author liu 设置表格的Renderer
	 */
	class PropertyTable extends JTable {

		private static final long serialVersionUID = 1L;

		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			return setComponentRenderer(renderer, row, column);
		}

		private Component setComponentRenderer(TableCellRenderer renderer, int row, int column) {
			Component component = super.prepareRenderer(renderer, row, column);
			if (((PropertyTableModel) propertyTable.getModel()).getRowData(row).isSystemField
					|| ((PropertyTableModel) propertyTable.getModel()).getRowData(row).getType() == FieldType.LONGBINARY) {
				if (isCellSelected(row, column)) {
					component.setBackground(COLOR_SELECTED);
					component.setForeground(Color.BLACK);
				} else {
					component.setBackground(COLOR_SYSTEM_NOT_SELECTED);
				}
			} else {
				if (isCellSelected(row, column)) {
					component.setBackground(COLOR_SELECTED);
					component.setForeground(Color.BLACK);
				} else {
					component.setBackground(COLOR_EDITABLE_NOT_SELECTED);
				}
			}

			return component;
		}

	}

	/**
	 * @author liu 数据封装
	 */
	private class FieldData {
		private boolean isSystemField = false;
		private String name;
		private String caption;
		private FieldType type = FieldType.TEXT;
		private boolean isRequired = false;
		private FieldInfo fieldInfo;
		private Object fieldValue;
		private Recordset recordset;

		public void setRequired(boolean isRequired) {
			this.isRequired = isRequired;
		}

		public Object getFieldValue() {
			return fieldValue;
		}

		public void setFieldValue(Object fieldValue) {
			this.fieldValue = fieldValue;
		}

		public FieldData(FieldInfo fieldInfo, Recordset recordset) {
			this.recordset = recordset;
			this.fieldInfo = fieldInfo;
			initialize(this.fieldInfo);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCaption() {
			return caption;
		}

		public FieldType getType() {
			return type;
		}

		public boolean isRequired() {
			return isRequired;
		}

		public void apply() {
			try {
				// 二进制字段当做系统字段处理
				if (!this.isSystemField && this.getType() != FieldType.LONGBINARY) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(PropertyTableModel.DATE_STYLE);
					Object resultValue = null;
					if (this.getFieldValue() != null) {
						resultValue = this.getType() == FieldType.DATETIME ? dateFormat.parse(String.valueOf(this.getFieldValue())) : this.getFieldValue();
					}
					this.recordset.edit();
					this.recordset.setFieldValue(this.getName(), resultValue);
					this.recordset.update();
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		/**
		 * @param fieldInfo 初始化数据
		 */
		private void initialize(FieldInfo fieldInfo) {
			this.setFieldInfo(fieldInfo);
			this.isSystemField = fieldInfo.isSystemField();
			this.setName(fieldInfo.getName());
			this.caption = fieldInfo.getCaption();
			this.type = fieldInfo.getType();
			this.isRequired = fieldInfo.isRequired();
			this.fieldValue = this.recordset.getFieldValue(fieldInfo.getName());
		}

		public FieldInfo getFieldInfo() {
			return fieldInfo;
		}

		public void setFieldInfo(FieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
		}
	}

	@Override
	public PropertyType getPropertyType() {
		// TODO Auto-generated method stub
		return PropertyType.GEOMETRY_REOCRD;
	}

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub

	}
}