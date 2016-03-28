package com.supermap.desktop.geometryoperation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.supermap.data.DatasetType;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldType;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.utilties.FieldTypeUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;

public class JDialogFieldOperationSetting extends JDialog implements ItemListener, ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelEditLayer;
	private JComboBox<Layer> comboBoxEditLayer;
	private JTable table;
	private JRadioButton radioButtonNull;
	private JRadioButton radioButtonAVG;
	private JRadioButton radioButtonSum;
	private JRadioButton radioButtonGeometry;
	private JComboBox<String> comboBoxWeight;
	private JComboBox<String> comboBoxGeometry;
	private JButton buttonOK;
	private JButton buttonCancel;

	private Map map;
	private Layer editLayer;
	private DatasetType resultDatasetType;

	public JDialogFieldOperationSetting(String title) {
		this(title, null);
	}

	public JDialogFieldOperationSetting(String title, Map map) {
		this.map = map;
		setTitle(title);
		initializeComponents();
		initializeResources();
		registerEvents();

	}

	public Map getMap() {
		return this.map;
	}

	public void setMap(Map map) {
		if (map != null) {
			this.map = map;

			this.comboBoxEditLayer.removeAllItems();
			ArrayList<Layer> layers = MapUtilties.getLayers(this.map);
			for (Layer layer : layers) {
				if (layer.isEditable() && (layer.getDataset().getType() == this.resultDatasetType || layer.getDataset().getType() == DatasetType.CAD)) {
					this.comboBoxEditLayer.addItem(layer);
					if (layer.getSelection().getCount() > 0 && this.comboBoxEditLayer.getSelectedIndex() == -1) {
						this.comboBoxEditLayer.setSelectedIndex(this.comboBoxEditLayer.getItemCount() - 1);
						this.editLayer = layer;
					}
				}
			}

			if (this.comboBoxEditLayer.getItemCount() > 0 && this.comboBoxEditLayer.getSelectedIndex() == -1) {
				this.comboBoxEditLayer.setSelectedIndex(0);
				this.editLayer = (Layer) this.comboBoxEditLayer.getSelectedItem();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonOK) {
			buttonOKClicked();
		} else if (e.getSource() == this.buttonCancel) {
			buttonCancelClicked();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.radioButtonNull) {
			radioButtonNullStateChanged();
		} else if (e.getSource() == this.radioButtonAVG) {
			radioButtonAVGStateChanged();
		} else if (e.getSource() == this.radioButtonSum) {
			radioButtonSumStateChanged();
		} else if (e.getSource() == this.radioButtonGeometry) {
			radioButtonGeometryStateChanged();
		} else if (e.getSource() == this.comboBoxEditLayer) {
			comboBoxEditLayerSelectChanged(e);
		} else if (e.getSource() == this.comboBoxWeight) {
			comboBoxWeightSelectChanged(e);
		} else if (e.getSource() == this.comboBoxGeometry) {
			comboBoxGeometrySelectChanged(e);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		tableSelectionChanged();
	}

	private void initializeComponents() {
		this.labelEditLayer = new JLabel("EditLayer:");
		this.comboBoxEditLayer = new JComboBox<>();
		this.table = new JTable();
		this.radioButtonNull = new JRadioButton("NULL");
		this.radioButtonAVG = new JRadioButton("AVG");
		this.radioButtonSum = new JRadioButton("SUM");
		this.radioButtonGeometry = new JRadioButton("Geometry");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.radioButtonNull);
		buttonGroup.add(this.radioButtonAVG);
		buttonGroup.add(this.radioButtonSum);
		buttonGroup.add(this.radioButtonGeometry);
		this.comboBoxWeight = new JComboBox<String>();
		this.comboBoxGeometry = new JComboBox<String>();
		this.buttonOK = new JButton("OK");
		this.buttonCancel = new JButton("Cancel");

		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.labelEditLayer)
						.addComponent(this.comboBoxEditLayer, GroupLayout.DEFAULT_SIZE, groupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addComponent(this.table, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.radioButtonNull)
								.addComponent(this.radioButtonSum))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.radioButtonAVG)
								.addComponent(this.radioButtonGeometry))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxWeight, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
								.addComponent(this.comboBoxGeometry, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)))
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10, 10, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelEditLayer)
						.addComponent(this.comboBoxEditLayer,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE))
				.addComponent(this.table, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonNull)
						.addComponent(this.radioButtonAVG)
						.addComponent(this.comboBoxWeight, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.radioButtonSum)
						.addComponent(this.radioButtonGeometry)
						.addComponent(this.comboBoxGeometry, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));
		// @formatter:on
	}

	private void initializeResources() {
		this.labelEditLayer.setText(MapEditorProperties.getString("String_GeometryOperation_EditLayer"));
		this.radioButtonNull.setText(MapEditorProperties.getString("String_GeometryOperation_BeNull"));
		this.radioButtonAVG.setText(MapEditorProperties.getString("String_GeometryOperation_AVG"));
		this.radioButtonSum.setText(MapEditorProperties.getString("String_GeometryOperation_SUM"));
		this.radioButtonGeometry.setText(MapEditorProperties.getString("String_GeometryOperation_Geometry"));
		this.buttonOK.setText(CommonProperties.getString(CommonProperties.OK));
		this.buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void registerEvents() {
		this.comboBoxEditLayer.addItemListener(this);
		this.buttonOK.addActionListener(this);
		this.buttonCancel.addActionListener(this);
		this.radioButtonNull.addItemListener(this);
		this.radioButtonAVG.addItemListener(this);
		this.radioButtonSum.addItemListener(this);
		this.radioButtonGeometry.addItemListener(this);
		this.table.getSelectionModel().addListSelectionListener(this);
	}

	private void initializeDatas() {

	}

	private void buttonOKClicked() {

	}

	private void buttonCancelClicked() {

	}

	private void radioButtonNullStateChanged() {

	}

	private void radioButtonAVGStateChanged() {

	}

	private void radioButtonSumStateChanged() {

	}

	private void radioButtonGeometryStateChanged() {

	}

	private void comboBoxEditLayerSelectChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		}
	}

	private void comboBoxWeightSelectChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		}
	}

	private void comboBoxGeometrySelectChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		}
	}

	private void tableSelectionChanged() {

	}

	public static void main(String[] args) {
		JDialogFieldOperationSetting dialog = new JDialogFieldOperationSetting("");
		dialog.setSize(new Dimension(400, 300));
		dialog.setVisible(true);
	}

	/**
	 * @author highsad 私有内部类
	 */
	private class FieldOperationTableModel extends AbstractTableModel {

		private static final int FIELD_NAME = 0;
		private static final int FIELD_TYPE = 1;
		private static final int FIELD_OPERATION = 2;

		private ArrayList<FieldOperation> datas = new ArrayList<>();

		@Override
		public int getRowCount() {
			return this.datas.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (datas.size() > 0 && rowIndex >= 0 && rowIndex < datas.size() && columnIndex >= 0 && columnIndex < 3) {
				FieldOperation data = datas.get(rowIndex);

				if (columnIndex == 0) {
					return MessageFormat.format("{0}({1})", data.getFieldName(), data.getFieldCaption());
				} else if (columnIndex == 1) {
					return FieldTypeUtilties.getFieldTypeName(data.getFieldType());
				} else if (columnIndex == 2) {
					return data.getOperationData().getDescription();
				}
			}

			return null;
		}

		@Override
		public String getColumnName(int column) {
			if (column == FIELD_NAME) {
				return MapEditorProperties.getString("String_GeometryOperation_ListViewHeader_Field");
			} else if (column == FIELD_TYPE) {
				return CommonProperties.getString(CommonProperties.FieldType);
			} else if (column == FIELD_OPERATION) {
				return CommonProperties.getString(CommonProperties.Operation);
			}
			return null;
		}

		public FieldOperation getFieldOperation(int row) {
			if (row >= 0 && row < datas.size()) {
				return datas.get(row);
			} else {
				throw new IndexOutOfBoundsException(MessageFormat.format("row index {0} is out of bounds.", row));
			}
		}

		public FieldOperation[] getFieldOperations(int[] rows) {
			if (rows != null && rows.length > 0) {
				FieldOperation[] operations = new FieldOperation[rows.length];

				for (int i = 0; i < rows.length; i++) {
					operations[i] = getFieldOperation(rows[i]);
				}
				return operations;
			} else {
				throw new IndexOutOfBoundsException("rows can not be null or empty.");
			}
		}
	}

	private class FieldOperation {
		private String fieldName;
		private String fieldCaption;
		private FieldType fieldType;
		private int operationType = OperationType.GEOMETRY;
		private int availableOperationType = OperationType.NULL | OperationType.SUM | OperationType.AVG | OperationType.GEOMETRY;
		private IOperationData operationData = null;

		public String getFieldName() {
			return this.fieldName;
		}

		public String getFieldCaption() {
			return this.fieldCaption;
		}

		public FieldType getFieldType() {
			return this.fieldType;
		}

		/**
		 * 可用的字段操作类型。String、Boolean 等不支持加权平均。
		 * 
		 * @return
		 */
		public int getAvailableOperationType() {
			return this.availableOperationType;
		}

		public IOperationData getOperationData() {
			return this.operationData;
		}

		public void setOperationData(IOperationData operationData) {
			this.operationData = operationData;
		}
	}

	/**
	 * 可用的字段操作类型。
	 * 
	 * @author highsad
	 *
	 */
	private class OperationType {
		public static final int NULL = 1;
		public static final int SUM = 2;
		public static final int AVG = 4;
		public static final int GEOMETRY = 8;
	}

	/**
	 * 对应不同操作类型的附加数据。比如 AVG 有加权平均的字段选择，保存对象有对象的选择等。
	 * 
	 * @author highsad
	 *
	 */
	private interface IOperationData {

		/**
		 * 获取对应的字段操作类型。
		 * 
		 * @return
		 */
		int getOperationType();

		/**
		 * 获取该数据的字符串表述。
		 * 
		 * @return
		 */
		String getDescription();
	}

	private class AVGOperationData implements IOperationData {

		private FieldInfo fieldInfo;

		public FieldInfo getFieldInfo() {
			return this.fieldInfo;
		}

		public void setFieldInfo(FieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
		}

		@Override
		public int getOperationType() {
			return OperationType.AVG;
		}

		@Override
		public String getDescription() {
			if (this.fieldInfo == null) {
				return MapEditorProperties.getString("String_GeometryOperation_NoWeight");
			} else {
				return this.fieldInfo.getCaption() + ":" + this.fieldInfo.getName();
			}
		}
	}

	private class GeometryOperationData implements IOperationData {

		private int id = 0;
		private String fieldName;
		private Object fieldValue;

		public int getID() {
			return this.id;
		}

		public String getFieldName() {
			return this.fieldName;
		}

		public Object getFieldValue() {
			return this.fieldValue;
		}

		@Override
		public int getOperationType() {
			return OperationType.GEOMETRY;
		}

		@Override
		public String getDescription() {
			return MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_TheGeometry"), String.valueOf(this.id), this.fieldName,
					this.fieldValue.toString());
		}
	}
}
