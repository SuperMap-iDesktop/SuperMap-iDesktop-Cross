package com.supermap.desktop.geometryoperation;

import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.FieldType;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
import com.supermap.desktop.Application;
import com.supermap.desktop.mapeditor.MapEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxItemT;
import com.supermap.desktop.utilties.FieldTypeUtilties;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Map;
import com.supermap.mapping.Selection;
import com.supermap.mapping.TrackingLayer;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class JDialogFieldOperationSetting extends SmDialog implements ItemListener, ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TrackingLayerTag = "Tag_OperationTypeGeometry";

	private JLabel labelEditLayer;
	private JComboBox<ComboBoxItemT<Layer>> comboBoxEditLayer;
	private JTable table;
	private ButtonGroup radioButtonGroup;
	private JRadioButton radioButtonNull;
	private JRadioButton radioButtonAVG;
	private JRadioButton radioButtonSum;
	private JRadioButton radioButtonGeometry;
	private JComboBox<IOperationData> comboBoxWeight;
	private JComboBox<IOperationData> comboBoxGeometry;
	private JButton buttonOK;
	private JButton buttonCancel;

	private Map map;
	private Layer editLayer;
	private int[] selectedOperations;
	private DatasetType resultDatasetType;

	public JDialogFieldOperationSetting(String title, DatasetType resultDatasetType) {
		this(title, null, resultDatasetType);
	}

	public JDialogFieldOperationSetting(String title, Map map, DatasetType resultDatasetType) {
		setTitle(title);
		this.resultDatasetType = resultDatasetType;
		initializeComponents();
		initializeResources();
		registerEvents();
		setMap(map);
		setSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		this.componentList.add(this.buttonOK);
		this.componentList.add(this.buttonCancel);
		this.setFocusTraversalPolicy(policy);
	}

	public Layer getEditLayer() {
		return this.editLayer;
	}

	/**
	 * 获取字段操作的属性数据
	 * 
	 * @return
	 */
	public java.util.Map<String, Object> getPropertyData() {
		java.util.Map<String, Object> properties = new HashMap<String, Object>();
		FieldOperationTableModel model = (FieldOperationTableModel) this.table.getModel();
		Recordset recordset = null;

		try {
			recordset = this.editLayer.getSelection().toRecordset();

			for (int i = 0; i < model.getRowCount(); i++) {
				FieldOperation fieldOperation = model.getFieldOperation(i);
				int operationType = fieldOperation.getOperationType();
				String fieldName = fieldOperation.getFieldName();
				FieldType fieldType = fieldOperation.getFieldType();

				if (operationType == OperationType.NULL) {
					properties.put(fieldOperation.getFieldName(), null);
				} else if (operationType == OperationType.SUM) {
					properties.put(fieldName, getSumData(recordset, fieldName, fieldType));
				} else if (operationType == OperationType.AVG) {
					properties.put(fieldName, getAVGData(recordset, fieldName, fieldType, (AVGOperationData) fieldOperation.getOperationData()));
				} else if (operationType == OperationType.GEOMETRY) {
					int id = ((GeometryOperationData) fieldOperation.getOperationData()).getID();
					if (recordset.seekID(id)) {
						properties.put(fieldName, recordset.getFieldValue(fieldName));
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null) {
				recordset.close();
				recordset.dispose();
			}
		}
		return properties;
	}

	private Object getSumData(Recordset recordset, String fieldName, FieldType fieldType) {
		Object result = null;

		// 字符串型直接做连接
		if (FieldTypeUtilties.isString(fieldType)) {
			recordset.moveFirst();
			while (!recordset.isEOF()) {
				Object value = recordset.getObject(fieldName);

				if (result == null) {
					result = value;
				} else {
					if (value != null) {
						result = result + value.toString();
					}
				}
				recordset.moveNext();
			}
		} else {
			result = recordset.statistic(fieldName, StatisticMode.SUM);
		}
		return result;
	}

	private Object getAVGData(Recordset recordset, String fieldName, FieldType fieldType, AVGOperationData operationData) {
		Object result = null;

		if (FieldTypeUtilties.isNumber(fieldType)) {
			if (operationData.getFieldInfo() != null) {
				double weightSum = recordset.statistic(operationData.getFieldInfo().getName(), StatisticMode.SUM);

				if (weightSum != 0) {
					Object weight = null;
					Object value = null;
					double valueSum = 0;

					recordset.moveFirst();
					while (!recordset.isEOF()) {
						value = recordset.getObject(fieldName);
						weight = recordset.getObject(operationData.getFieldInfo().getName());
						if (value != null && weight != null) {
							valueSum += (Double.valueOf(value.toString()) * Double.valueOf(weight.toString()));
						}
						recordset.moveNext();
					}
					result = valueSum / weightSum;
				} else {
					result = 0;
				}
			} else {
				result = recordset.statistic(fieldName, StatisticMode.SUM) / recordset.getRecordCount();
			}
		}
		return result;
	}

	public Map getMap() {
		return this.map;
	}

	/**
	 * 设置地图数据，初始化图层选择控件（ComboBoxEditLayer），而后触发相应事件，回调 comboBoxEditLayerSelectChanged 进入下一步的设置
	 * 
	 * @param map
	 */
	public void setMap(Map map) {
		try {
			if (map != null) {
				this.map = map;

				this.comboBoxEditLayer.removeAllItems();
				ArrayList<Layer> layers = MapUtilties.getLayers(this.map);
				for (Layer layer : layers) {
					if (layer.isEditable() && (layer.getDataset().getType() == this.resultDatasetType || layer.getDataset().getType() == DatasetType.CAD)) {
						this.comboBoxEditLayer.addItem(new ComboBoxItemT<Layer>(layer, layer.getCaption()));
						if (layer.getSelection().getCount() > 0 && this.comboBoxEditLayer.getSelectedIndex() == -1) {
							this.comboBoxEditLayer.setSelectedIndex(this.comboBoxEditLayer.getItemCount() - 1);
						}
					}
				}

				if (this.comboBoxEditLayer.getItemCount() > 0 && this.comboBoxEditLayer.getSelectedIndex() == -1) {
					this.comboBoxEditLayer.setSelectedIndex(0);
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
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
		JScrollPane scrollPaneTable = new JScrollPane();
		this.table = new JTable();
		FieldOperationTableModel tableModel = new FieldOperationTableModel();
		this.table.setModel(tableModel);
		scrollPaneTable.setViewportView(this.table);
		this.radioButtonNull = new JRadioButton("NULL");
		this.radioButtonAVG = new JRadioButton("AVG");
		this.radioButtonSum = new JRadioButton("SUM");
		this.radioButtonGeometry = new JRadioButton("Geometry");
		this.radioButtonGroup = new ButtonGroup();
		this.radioButtonGroup.add(this.radioButtonNull);
		this.radioButtonGroup.add(this.radioButtonAVG);
		this.radioButtonGroup.add(this.radioButtonSum);
		this.radioButtonGroup.add(this.radioButtonGeometry);
		this.comboBoxWeight = new JComboBox<IOperationData>();
		this.comboBoxGeometry = new JComboBox<IOperationData>();
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
						.addComponent(this.comboBoxEditLayer, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
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
				.addComponent(scrollPaneTable, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
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
		this.comboBoxWeight.addItemListener(this);
		this.comboBoxGeometry.addItemListener(this);
		this.table.getSelectionModel().addListSelectionListener(this);
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentHidden(ComponentEvent e) {
				removeTrackingTags();
				map.refresh();
			}
		});
	}

	/**
	 * 设置选中的可编辑图层，刷新 Table，设置默认选中行，触发相应事件，回调 tableSelectionChanged 进入下一步的设置
	 * 
	 * @param layer
	 */
	private void setEditLayer(Layer layer) {
		if (layer != null && layer.getDataset() instanceof DatasetVector) {
			// 初始化 ComboBoxWeight
			loadComboBoxWeight();

			// 初始化 ComboBoxGeometry
			loadComboBoxGeometry();

			DatasetVector dataset = (DatasetVector) layer.getDataset();
			FieldInfos fieldInfos = dataset.getFieldInfos();
			FieldOperationTableModel model = (FieldOperationTableModel) this.table.getModel();

			// 初始化 Table
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				FieldInfo fieldInfo = fieldInfos.get(i);

				if (!fieldInfo.isSystemField()) {
					model.addFieldOperation(createFieldOperation(fieldInfo));
				}
			}

			if (model.getRowCount() > 0) {
				this.table.setRowSelectionInterval(0, 0);
			}
		}
	}

	private FieldOperation createFieldOperation(FieldInfo fieldInfo) {
		FieldOperation result = null;

		if (fieldInfo != null) {
			result = new FieldOperation((DatasetVector) this.editLayer.getDataset(), fieldInfo.getName(), fieldInfo.getCaption(), fieldInfo.getType());
			result.setOperationData(this.comboBoxGeometry.getItemAt(0));
		}
		return result;
	}

	// @formatter:off
	/**
	 * 设置下半部分字段操作设置，多选的情况下，值相同则显示这个值，值不同则置空，
	 * 同时可用性的控制采用严谨性原则，即此项设置对多选的所有记录都可用的情况下，才可用。
	 * 
	 * @param fieldOperations
	 */
	// @formatter:on
	private void setFieldOperations(FieldOperation[] fieldOperations) {
		// 重置所有 radioButton 到未选择状态
		this.radioButtonGroup.clearSelection();
		if (fieldOperations != null && fieldOperations.length > 0) {
			// 用第一条记录的操作类型初始化变量，后面遍历比较，不一致则跳出循环
			int operationType = fieldOperations[0].getOperationType();

			for (int i = 1; i < fieldOperations.length; i++) {
				if (operationType != fieldOperations[i].getOperationType()) {
					operationType = OperationType.NONE;
					break;
				}
			}

			switch (operationType) {
			case OperationType.NULL:
				this.radioButtonNull.setSelected(true);
				break;
			case OperationType.AVG:
				this.radioButtonAVG.setSelected(true);
				break;
			case OperationType.SUM:
				this.radioButtonSum.setSelected(true);
				break;
			case OperationType.GEOMETRY:
				this.radioButtonGeometry.setSelected(true);
				break;
			default:
				// 默认 NONE，就什么都不选
				break;
			}
		}
		setControlsEnabled(fieldOperations);
	}

	// @formatter:off
	/**
	 * 设置下半部分字段操作设置，多选的情况下，值相同则显示这个值，值不同则置空，
	 * 同时可用性的控制采用严谨性原则，即此项设置对多选的所有记录都可用的情况下，才可用。
	 * 
	 */
	// @formatter:on
	private void setControlsEnabled(FieldOperation[] fieldOperations) {
		boolean radioButtonNullEnabled = true;
		boolean radioButtonAVGEnabled = true;
		boolean radioButtonSumEnabled = true;
		boolean radioButtonGeometryEnabled = true;
		boolean buttonOKEnabled = true;

		if (fieldOperations != null && fieldOperations.length > 0) {
			for (int i = 0; i < fieldOperations.length; i++) {
				FieldOperation fieldOperation = fieldOperations[i];
				int availableOperationType = fieldOperation.getAvailableOperationType();
				radioButtonNullEnabled = radioButtonNullEnabled && isContains(availableOperationType, OperationType.NULL);
				radioButtonAVGEnabled = radioButtonAVGEnabled && isContains(availableOperationType, OperationType.AVG);
				radioButtonSumEnabled = radioButtonSumEnabled && isContains(availableOperationType, OperationType.SUM);
				radioButtonGeometryEnabled = radioButtonGeometryEnabled && isContains(availableOperationType, OperationType.GEOMETRY);
			}
		} else {
			radioButtonNullEnabled = false;
			radioButtonAVGEnabled = false;
			radioButtonSumEnabled = false;
			radioButtonGeometryEnabled = false;
			buttonOKEnabled = false;
		}

		this.radioButtonNull.setEnabled(radioButtonNullEnabled);
		this.radioButtonAVG.setEnabled(radioButtonAVGEnabled);
		this.radioButtonSum.setEnabled(radioButtonSumEnabled);
		this.radioButtonGeometry.setEnabled(radioButtonGeometryEnabled);
		this.comboBoxWeight.setEnabled(radioButtonAVGEnabled && this.radioButtonAVG.isSelected());
		this.comboBoxGeometry.setEnabled(radioButtonGeometryEnabled && this.radioButtonGeometry.isSelected());
		this.buttonOK.setEnabled(buttonOKEnabled);
	}

	/**
	 * 判断可用类型（availableType）是否包含指定类型（type）
	 * 
	 * @param availableType
	 * @param type
	 * @return
	 */
	private boolean isContains(int availableType, int type) {
		return (availableType & type) == type;
	}

	/**
	 * 切换图层之后，重新加载权重字段信息，仅数值型（Double、Single、Int16、Int32、Int64）字段允许作为权重字段
	 */
	private void loadComboBoxWeight() {
		this.comboBoxWeight.removeAllItems();

		if (this.editLayer != null && this.editLayer.getDataset() instanceof DatasetVector) {
			// 添加第一项 -- 无加权字段(平均)
			this.comboBoxWeight.addItem(new AVGOperationData());

			DatasetVector dataset = (DatasetVector) this.editLayer.getDataset();
			FieldInfos fieldInfos = dataset.getFieldInfos();
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				FieldInfo fieldInfo = fieldInfos.get(i);

				if (FieldTypeUtilties.isNumber(fieldInfo.getType())) {
					this.comboBoxWeight.addItem(new AVGOperationData(fieldInfo));
				}
			}
		}
	}

	// @formatter:off
	/**
	 * 切换图层之后，重新加载选中的对象信息，用于“保存对象”操作时的数据选择，可以保留的属性需要与结果图层一致。
	 * 这里只设置 smid，具体的字段值，在选择 table 记录的时候再行设置
	 */
	// @formatter:on
	private void loadComboBoxGeometry() {
		this.comboBoxGeometry.removeAllItems();

		if (this.editLayer != null && this.editLayer.getDataset() instanceof DatasetVector) {
			Selection selection = this.editLayer.getSelection();

			for (int i = 0; i < selection.getCount(); i++) {
				GeometryOperationData operationData = new GeometryOperationData(selection.get(i));
				this.comboBoxGeometry.addItem(operationData);
			}
		}
	}

	private void buttonOKClicked() {
		this.setVisible(false);
		removeTrackingTags();
		this.map.refresh();
		setDialogResult(DialogResult.OK);
	}

	private void buttonCancelClicked() {
		this.setVisible(false);
		setDialogResult(DialogResult.CANCEL);
	}

	private void radioButtonNullStateChanged() {
		try {
			if (this.radioButtonNull.isSelected() && this.selectedOperations != null) {
				updateSelectedOperationsData(new DefaultOperationData(OperationType.NULL, MapEditorProperties.getString("String_GeometryOperation_BeNull")));
				removeTrackingTags();
				this.map.refresh();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonAVGStateChanged() {
		try {
			if (this.radioButtonAVG.isSelected() && this.selectedOperations != null) {
				FieldOperationTableModel model = (FieldOperationTableModel) this.table.getModel();

				for (int i = 0; i < this.selectedOperations.length; i++) {
					int row = this.selectedOperations[i];
					FieldOperation fieldOperation = model.getFieldOperation(row);
					// 设置附加数据，如果不是当前操作的字段类型
					if (!(fieldOperation.getOperationData() instanceof AVGOperationData)) {
						model.changeOperationData(row, this.comboBoxWeight.getItemAt(0));
					}
				}

				// 设置 AVG 参数可用
				this.comboBoxWeight.setEnabled(true);

				// @formatter:off
				/**
				 * 设置 ComboBoxAVG 的选中状态
				 * 1.table 中只选中了一条记录，如果该记录的 OperationData 为空，初始化为“平均”；
				 * 2.table 中选中了多条记录，如果所有记录的 OperationData 都为空，全都初始化为“平均”；
				 * 3.table 中选中了多条记录，如果所有记录的 OperationData 相同且不为空，则初始化为改 OperationData；
				 * 4.table 中选中了多条记录，如果所有记录的 OperationData 不一致，则将 combobox 的当前选中置空。
				 */
				// @formatter:on
				IOperationData currentData = model.getFieldOperation(this.selectedOperations[0]).getOperationData();
				boolean isDifferent = false;

				for (int i = 0; i < this.selectedOperations.length; i++) {
					int row = this.selectedOperations[i];
					if (currentData != model.getFieldOperation(row).getOperationData()) {
						isDifferent = true;
						break;
					}
				}

				if (!isDifferent) {
					if (currentData == null) {
						this.comboBoxWeight.setSelectedIndex(0);
					} else {
						this.comboBoxWeight.setSelectedItem(currentData);
					}
				} else {
					this.comboBoxWeight.setSelectedItem(null);
				}
			} else {
				this.comboBoxWeight.setEnabled(false);
				this.comboBoxWeight.setSelectedItem(null);
			}
			removeTrackingTags();
			this.map.refresh();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonSumStateChanged() {
		try {
			if (this.radioButtonSum.isSelected() && this.selectedOperations != null) {
				updateSelectedOperationsData(new DefaultOperationData(OperationType.SUM, MapEditorProperties.getString("String_GeometryOperation_SUM")));
				removeTrackingTags();
				this.map.refresh();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void radioButtonGeometryStateChanged() {
		try {
			if (this.radioButtonGeometry.isSelected() && this.selectedOperations != null) {
				FieldOperationTableModel model = (FieldOperationTableModel) this.table.getModel();

				for (int i = 0; i < this.selectedOperations.length; i++) {
					int row = this.selectedOperations[i];
					FieldOperation fieldOperation = model.getFieldOperation(row);
					// 设置附加数据
					if (!(fieldOperation.getOperationData() instanceof GeometryOperationData)) {
						IOperationData data = this.comboBoxGeometry.getItemAt(0);
						model.changeOperationData(row, data);
						highlightGeometry(((GeometryOperationData) data).getID());
					}
				}

				// 设置“保留对象”参数可用
				this.comboBoxGeometry.setEnabled(true);

				// @formatter:off
				/**
				 * 设置 ComboBoxGeometry 的选中状态
				 * 1.table 中只选中了一条记录，如果该记录的 OperationData 为空，初始化为“第一个对象”；
				 * 2.table 中选中了多条记录，如果所有记录的 OperationData 都为空，全都初始化为“第一个对象”；
				 * 3.table 中选中了多条记录，如果所有记录的 OperationData 相同且不为空，则初始化为改 OperationData；
				 * 4.table 中选中了多条记录，如果所有记录的 OperationData 不一致，则将 combobox 的当前选中置空。
				 */
				// @formatter:on
				IOperationData currentData = model.getFieldOperation(this.selectedOperations[0]).getOperationData();
				boolean isDifferent = false;

				for (int i = 0; i < this.selectedOperations.length; i++) {
					int row = this.selectedOperations[i];

					if (currentData != model.getFieldOperation(row).getOperationData()) {
						isDifferent = true;
						break;
					}
				}

				if (!isDifferent) {
					if (currentData == null) {
						this.comboBoxGeometry.setSelectedIndex(0);
					} else {
						this.comboBoxGeometry.setSelectedItem(currentData);
					}
				} else {
					this.comboBoxGeometry.setSelectedItem(null);
				}

				// 如果 table 中选中单条记录，就用这个字段来初始化 ComboBoxGeometry 各子项的 FieldValue
				if (this.selectedOperations.length == 1) {
					refreshComboBoxGeometryItems(model.getFieldOperation(this.selectedOperations[0]).getFieldName());
				} else {
					refreshComboBoxGeometryItems(null);
				}
			} else {
				this.comboBoxGeometry.setEnabled(false);
				this.comboBoxGeometry.setSelectedItem(null);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 在 ComboBoxGeometryItem 上显示指定 fieldName 的 fieldValue
	 * 
	 * @param fieldName
	 */
	private void refreshComboBoxGeometryItems(String fieldName) {
		Recordset recordset = null;

		try {
			if (this.editLayer != null && this.editLayer.getSelection() != null && this.editLayer.getSelection().getCount() > 0) {
				recordset = this.editLayer.getSelection().toRecordset();

				for (int i = 0; i < this.comboBoxGeometry.getItemCount(); i++) {
					GeometryOperationData data = (GeometryOperationData) this.comboBoxGeometry.getItemAt(i);
					// 设置字段名
					data.setFieldName(fieldName);
					// 设置字段值
					if (!StringUtilties.isNullOrEmpty(fieldName)) {
						recordset.seekID(data.getID());
						data.setFieldValue(recordset.getFieldValue(fieldName));
					} else {
						data.setFieldValue(null);
					}
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			// 使用完毕，必须要释放 recordset
			if (recordset != null && !recordset.isClosed()) {
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private void comboBoxEditLayerSelectChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				ComboBoxItemT<Layer> selectedItem = (ComboBoxItemT<Layer>) this.comboBoxEditLayer.getSelectedItem();
				this.editLayer = selectedItem.getData();
				setEditLayer(this.editLayer);
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void comboBoxWeightSelectChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED && this.selectedOperations != null) {
				updateSelectedOperationsData((IOperationData) this.comboBoxWeight.getSelectedItem());
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void comboBoxGeometrySelectChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED && this.selectedOperations != null) {
				GeometryOperationData data = (GeometryOperationData) this.comboBoxGeometry.getSelectedItem();

				// 设置附加数据
				updateSelectedOperationsData(data);

				// 地图上高亮显示
				highlightGeometry(data.getID());
			}
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	/**
	 * 更新选中字段的操作数据
	 * 
	 * @param operationData
	 */
	private void updateSelectedOperationsData(IOperationData operationData) {
		FieldOperationTableModel model = (FieldOperationTableModel) this.table.getModel();

		for (int i = 0; i < this.selectedOperations.length; i++) {
			model.changeOperationData(this.selectedOperations[i], operationData);
		}
	}

	/**
	 * 在地图上高亮显示指定 id 的几何对象
	 * 
	 * @param id
	 */
	private void highlightGeometry(int id) {
		Recordset recordset = null;
		Geometry geometry = null;

		try {
			TrackingLayer trackingLayer = this.map.getTrackingLayer();
			removeTrackingTags();

			recordset = this.editLayer.getSelection().toRecordset();
			recordset.seekID(id);
			geometry = recordset.getGeometry();

			GeoStyle geoStyle = new GeoStyle();
			geoStyle.setLineColor(Color.RED);
			geoStyle.setLineWidth(1);
			geometry.setStyle(geoStyle);
			trackingLayer.add(geometry, TrackingLayerTag);
			this.map.refresh();
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (recordset != null && !recordset.isClosed()) {
				recordset.close();
				recordset.dispose();
			}
		}
	}

	private void removeTrackingTags() {
		TrackingLayer trackingLayer = this.map.getTrackingLayer();
		int index = trackingLayer.indexOf(TrackingLayerTag);
		while (index >= 0) {
			trackingLayer.remove(index);
			index = trackingLayer.indexOf(TrackingLayerTag);
		}
	}

	private void tableSelectionChanged() {
		FieldOperationTableModel model = (FieldOperationTableModel) this.table.getModel();
		this.selectedOperations = this.table.getSelectedRows();
		setFieldOperations(model.getFieldOperations(this.selectedOperations));
	}

	/**
	 * @author highsad 私有内部类
	 */
	private class FieldOperationTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
					return data.toString();
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

		public void addFieldOperation(FieldOperation fieldOperation) {
			this.datas.add(fieldOperation);
			fireTableDataChanged();
		}

		public void changeOperationData(int row, IOperationData operationData) {
			if (row >= 0 && row < this.datas.size()) {
				this.datas.get(row).setOperationData(operationData);
				fireTableCellUpdated(row, FIELD_OPERATION);
			}
		}
	}

	private class FieldOperation {
		private DatasetVector datasetVector;
		private String fieldName;
		private String fieldCaption;
		private FieldType fieldType;
		private int availableOperationType = OperationType.NULL | OperationType.SUM | OperationType.AVG | OperationType.GEOMETRY;
		private IOperationData operationData = null;

		public FieldOperation(DatasetVector datasetVector, String fieldName, String fieldCaption, FieldType fieldType) {
			this.datasetVector = datasetVector;
			this.fieldName = fieldName;
			this.fieldCaption = fieldCaption;
			this.fieldType = fieldType;

			// 非数值型字段不支持加权平均
			if (this.fieldType != FieldType.DOUBLE && this.fieldType != FieldType.SINGLE && this.fieldType != FieldType.INT16
					&& this.fieldType != FieldType.INT32 && this.fieldType != FieldType.INT64) {
				this.availableOperationType ^= OperationType.AVG;
			}
		}

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
		 * 可用的字段操作类型。仅数值型字段（double,float,short,int,long）支持加权平均。
		 * 
		 * @return
		 */
		public int getAvailableOperationType() {
			return this.availableOperationType;
		}

		public int getOperationType() {
			return this.operationData.getOperationType();
		}

		public IOperationData getOperationData() {
			return this.operationData;
		}

		public void setOperationData(IOperationData operationData) {
			this.operationData = operationData;
		}

		@Override
		public String toString() {
			String result = "";
			Recordset recordset = null;

			try {
				if (getOperationType() == OperationType.GEOMETRY) {
					if (!StringUtilties.isNullOrEmpty(this.fieldName) && this.operationData instanceof GeometryOperationData) {
						recordset = this.datasetVector.getRecordset(false, CursorType.STATIC);
						int id = ((GeometryOperationData) this.operationData).getID();
						recordset.seekID(id);
						Object fieldValue = recordset.getFieldValue(this.fieldName);
						return MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_TheGeometry"), String.valueOf(id), this.fieldName,
								fieldValue == null ? "NULL" : fieldValue.toString());
					}
				} else {
					result = this.operationData.getDescription();
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			} finally {
				if (recordset != null) {
					recordset.close();
					recordset.dispose();
				}
			}
			return result;
		}
	}

	/**
	 * 可用的字段操作类型。
	 * 
	 * @author highsad
	 *
	 */
	private class OperationType {
		public static final int NONE = 0;
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

	private class DefaultOperationData implements IOperationData {
		private int operationType = OperationType.NONE;;
		private String description;

		public DefaultOperationData(int operationType, String description) {
			this.operationType = operationType;
			this.description = description;
		}

		@Override
		public int getOperationType() {
			return this.operationType;
		}

		@Override
		public String getDescription() {
			return this.description;
		}

		@Override
		public String toString() {
			return getDescription();
		}
	}

	private class AVGOperationData implements IOperationData {

		private FieldInfo fieldInfo;

		public AVGOperationData() {
			// 什么都不做
		}

		public AVGOperationData(FieldInfo fieldInfo) {
			this.fieldInfo = fieldInfo;
		}

		public FieldInfo getFieldInfo() {
			return this.fieldInfo;
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

		@Override
		public String toString() {
			return getDescription();
		}
	}

	private class GeometryOperationData implements IOperationData {

		private int id = 0;
		private String fieldName;
		private Object fieldValue;

		public GeometryOperationData(int id) {
			this.id = id;
		}

		public int getID() {
			return this.id;
		}

		public String getFieldName() {
			return this.fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public Object getFieldValue() {
			return this.fieldValue;
		}

		public void setFieldValue(Object fieldValue) {
			this.fieldValue = fieldValue;
		}

		@Override
		public int getOperationType() {
			return OperationType.GEOMETRY;
		}

		@Override
		public String getDescription() {
			if (!StringUtilties.isNullOrEmpty(this.fieldName)) {
				return MessageFormat.format(MapEditorProperties.getString("String_GeometryOperation_TheGeometry"), String.valueOf(this.id), this.fieldName,
						this.fieldValue == null ? "NULL" : this.fieldValue.toString());
			} else {
				return MessageFormat.format("SmID:{0}", this.id);
			}
		}

		@Override
		public String toString() {
			return getDescription();
		}
	}

	@Override
	public void escapePressed() {
		buttonCancelClicked();
	}

	@Override
	public void enterPressed() {
		if (this.getRootPane().getDefaultButton() == this.buttonOK) {
			buttonOKClicked();
		}
		if (this.getRootPane().getDefaultButton() == this.buttonCancel) {
			buttonCancelClicked();
		}
	}
}
