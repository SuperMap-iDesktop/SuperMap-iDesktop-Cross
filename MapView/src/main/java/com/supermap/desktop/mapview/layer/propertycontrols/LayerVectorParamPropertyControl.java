package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.data.Dataset;
import com.supermap.data.FieldInfo;
import com.supermap.data.JoinItems;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.ScaleModel;
import com.supermap.desktop.controls.ControlDefaultValues;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerVectorParamPropertyModel;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.CaretPositionListener;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.ui.controls.dialogs.dialogJoinItems.JDialogJoinItems;
import com.supermap.desktop.utilities.FieldTypeUtilities;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 显示过滤条件、对象显示顺序、设置图层关联属性表因为有额外的功能界面要开发实现，暂缓
 *
 * @author highsad
 */
public class LayerVectorParamPropertyControl extends AbstractLayerPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private transient CaretPositionListener caretPositionListener = new CaretPositionListener();

	private TristateCheckBox checkBoxIsCompleteLineSymbolDisplayed; // 显示完整线型
	private TristateCheckBox checkBoxIsCrossroadOptimized; // 十字路口优化
	private TristateCheckBox checkBoxIsSymbolScalable; // 符号随图缩放
	private TristateCheckBox checkBoxIsAntialias; // 反走样显示
	private TristateCheckBox checkBoxIsOverlapDisplayed; // 显示压盖对象
	private TristateCheckBox checkBoxDesc; // 降序

	private JLabel labelSymbolScale; // 缩放基准比例尺
	private JLabel labelMinVisibleGeometrySize; // 对象最小尺寸
	private JLabel labelDisplayFilter; // 显示过滤条件
	private JLabel labelGeometryDisplayedOrder; // 对象显示顺序

	private JComboBox<Object> comboBoxSymbolScale;
	private SMFormattedTextField textFieldMinVisibleGeometrySize;
	private JTextField textFieldDisplayFilter;
	private SmButton buttonDisplayFilter;
	private JComboBox<String> comboBoxOrder;
	private SmButton buttonJoinItem;
	private ArrayList<Dataset> datasets = new ArrayList<Dataset>();

	DocumentListener documentListener = new DocumentListener() {

		@Override
		public void insertUpdate(DocumentEvent e) {
			setDisplayAttributeFilterValue();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			setDisplayAttributeFilterValue();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setDisplayAttributeFilterValue();
		}
	};
	private StateChangeListener checkBoxStateChangeListener = new StateChangeListener() {

		@Override
		public void stateChange(StateChangeEvent e) {
			if (e.getSource() == checkBoxIsCompleteLineSymbolDisplayed) {
				checkBoxIsCompleteLineSymbolDisplayedCheckedChanged();
			} else if (e.getSource() == checkBoxIsCrossroadOptimized) {
				checkBoxIsCrossroadOptimizedCheckedChanged();
			} else if (e.getSource() == checkBoxIsSymbolScalable) {
				checkBoxIsSymbolScalableCheckedChanged();
			} else if (e.getSource() == checkBoxIsAntialias) {
				checkBoxIsAntialiasCheckedChanged();
			} else if (e.getSource() == checkBoxIsOverlapDisplayed) {
				checkBoxIsOverlapDisplayedCheckedChanged();
			} else if (e.getSource() == checkBoxDesc) {
				checkBoxDescCheckedChanged();
			}
		}
	};
	private ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();
	private TextFieldMinVisibleGeometrySizePropertyChangeListener textFieldPropertyChangeListener = new TextFieldMinVisibleGeometrySizePropertyChangeListener();
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
			IFormMap iFormMap = Application.getActiveApplication().getActiveForm() instanceof IFormMap ? ((IFormMap) Application.getActiveApplication()
					.getActiveForm()) : null;
			if (iFormMap != null) {
				datasets.clear();
				Dataset dataset = getModifiedLayerPropertyModel().getDataset();
				JoinItems tempJoinItems = getModifiedLayerPropertyModel().getLayers()[0].getDisplayFilter().getJoinItems();
				datasets.add(getModifiedLayerPropertyModel().getDataset());
				for (int i = 0; i < tempJoinItems.getCount(); i++) {
					datasets.add(dataset.getDatasource().getDatasets().get(tempJoinItems.get(i).getForeignTable()));
				}
				DialogResult dialogResult = sqlDialog.showDialog(getModifiedLayerPropertyModel().getDisplayAttributeFilter(),
						datasets.toArray(new Dataset[datasets.size()]));
				if (dialogResult == DialogResult.OK) {
					String filter = sqlDialog.getQueryParameter().getAttributeFilter();

					if (StringUtilities.isNullOrEmpty(filter)) {
						filter = "";
					}
					textFieldDisplayFilter.setText(filter);
					setDisplayAttributeFilterValue();
				}
			}
		}
	};

	private MouseJoinItemsListener joinItemsListener = new MouseJoinItemsListener();

	public LayerVectorParamPropertyControl() {
		// TODO
	}

	@Override
	public LayerVectorParamPropertyModel getLayerPropertyModel() {
		return (LayerVectorParamPropertyModel) super.getLayerPropertyModel();
	}

	@Override
	protected LayerVectorParamPropertyModel getModifiedLayerPropertyModel() {
		return (LayerVectorParamPropertyModel) super.getModifiedLayerPropertyModel();
	}

	@Override
	protected void initializeComponents() {
		this.setBorder(BorderFactory.createTitledBorder("VectorParameter"));

		this.checkBoxIsCompleteLineSymbolDisplayed = new TristateCheckBox("IsCompleteLineSymbolDisplayed");
		this.checkBoxIsCrossroadOptimized = new TristateCheckBox("IsCrossroadOptimized");
		this.checkBoxIsSymbolScalable = new TristateCheckBox("IsSymbolScalable");
		this.checkBoxIsAntialias = new TristateCheckBox("IsAntialias");
		this.checkBoxIsOverlapDisplayed = new TristateCheckBox("IsOverlapDisplayed");
		this.checkBoxDesc = new TristateCheckBox("Des");

		this.labelSymbolScale = new JLabel("SymbolScale:");
		this.labelMinVisibleGeometrySize = new JLabel("MinVisibleGeometrySize:");
		this.labelDisplayFilter = new JLabel("DisplayFilter:");
		this.labelGeometryDisplayedOrder = new JLabel("GeometryDisplayedOrder:");

		this.comboBoxSymbolScale = new JComboBox<Object>();
		this.textFieldMinVisibleGeometrySize = new SMFormattedTextField(NumberFormat.getInstance());
		this.textFieldDisplayFilter = new JTextField();
		this.buttonDisplayFilter = new SmButton("...");
		this.comboBoxOrder = new JComboBox<String>();
		this.buttonJoinItem = new SmButton("SetLayerJoinItem...");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(checkBoxIsCompleteLineSymbolDisplayed)
								.addComponent(checkBoxIsSymbolScalable)
								.addComponent(checkBoxIsOverlapDisplayed)
								.addComponent(labelSymbolScale)
								.addComponent(labelMinVisibleGeometrySize))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(checkBoxIsCrossroadOptimized)
								.addComponent(checkBoxIsAntialias)
								.addComponent(comboBoxSymbolScale)
								.addComponent(textFieldMinVisibleGeometrySize)))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(labelDisplayFilter)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(textFieldDisplayFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(buttonDisplayFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(labelGeometryDisplayedOrder)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(checkBoxDesc)
								.addComponent(comboBoxOrder, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
						.addComponent(buttonJoinItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(checkBoxIsCompleteLineSymbolDisplayed)
						.addComponent(checkBoxIsCrossroadOptimized))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(checkBoxIsSymbolScalable)
						.addComponent(checkBoxIsAntialias))
				.addComponent(checkBoxIsOverlapDisplayed)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelSymbolScale)
						.addComponent(comboBoxSymbolScale, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelMinVisibleGeometrySize)
						.addComponent(textFieldMinVisibleGeometrySize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(labelDisplayFilter)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(textFieldDisplayFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonDisplayFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(labelGeometryDisplayedOrder)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(checkBoxDesc)
						.addComponent(comboBoxOrder, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(buttonJoinItem, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		// @formatter:on
	}

	@Override
	protected void initializeResources() {
		((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_LayerProperty_Vector"));
		this.checkBoxIsCompleteLineSymbolDisplayed.setText(MapViewProperties.getString("String_LayerControl_CompleteLineSymbolDisplayed"));
		this.checkBoxIsCrossroadOptimized.setText(MapViewProperties.getString("String_LayerControl_CrossroadOptimized"));
		this.checkBoxIsSymbolScalable.setText(MapViewProperties.getString("String_LayerControl_SymbolScalable"));
		this.checkBoxIsAntialias.setText(MapViewProperties.getString("String_CheckBox_IsLayerAntialias"));
		this.checkBoxIsOverlapDisplayed.setText(MapViewProperties.getString("String_CheckBox_IsOverlapDisplayed"));
		this.labelSymbolScale.setText(MapViewProperties.getString("String_LayerControl_SymbolScale"));
		this.labelMinVisibleGeometrySize.setText(MapViewProperties.getString("String_LayerControl_MinVisibleGeometrySize"));
		this.labelDisplayFilter.setText(MapViewProperties.getString("String_LayerControl_DisplayFilter"));
		this.labelGeometryDisplayedOrder.setText(MapViewProperties.getString("String_LayerControl_ObjShowOrderFiled"));
		this.checkBoxDesc.setText(MapViewProperties.getString("String_LayerControl_Descending"));
		this.buttonJoinItem.setText(MapViewProperties.getString("String_LayerControl_SetJoinItems"));
	}

	@Override
	protected void fillComponents() {
		try {
			if (getLayerPropertyModel() != null) {
				this.checkBoxIsCompleteLineSymbolDisplayed.setSelectedEx(getLayerPropertyModel().isCompleteLineSymbolDisplayed());
				this.checkBoxIsCrossroadOptimized.setSelectedEx(getLayerPropertyModel().isCrossroadOptimized());
				this.checkBoxIsSymbolScalable.setSelectedEx(getLayerPropertyModel().isSymbolScalable());
				this.checkBoxIsAntialias.setSelectedEx(getLayerPropertyModel().isAntialias());
				this.checkBoxIsOverlapDisplayed.setSelectedEx(getLayerPropertyModel().isOverlapDisplayed());

				this.comboBoxSymbolScale.removeAllItems();
				this.comboBoxSymbolScale.setEditable(true);
				this.comboBoxSymbolScale.addItem(MapViewProperties.getString("String_SetCurrentScale"));
				this.comboBoxSymbolScale.addItem(CoreProperties.getString(CoreProperties.Clear));
				if (null != getLayerPropertyModel().getSymbolScale()) {
					this.comboBoxSymbolScale.setSelectedItem(new ScaleModel(getLayerPropertyModel().getSymbolScale()));
				} else {
					this.comboBoxSymbolScale.setSelectedItem("");
				}

				this.textFieldMinVisibleGeometrySize.setValue(getLayerPropertyModel().getMinVisibleGeometrySize());

				this.comboBoxOrder.removeAllItems();
				this.comboBoxOrder.addItem("");
				if (getLayerPropertyModel().getDataset() != null) {
					for (int i = 0; i < getLayerPropertyModel().getDataset().getFieldCount(); i++) {
						FieldInfo fieldInfo = getLayerPropertyModel().getDataset().getFieldInfos().get(i);

						if (FieldTypeUtilities.isNumber(fieldInfo.getType())) {
							this.comboBoxOrder.addItem(getLayerPropertyModel().getDataset().getName()+"."+fieldInfo.getName());
						}
					}
					if (StringUtilities.isNullOrEmpty(getLayerPropertyModel().getDisplayOrderField())) {
						this.comboBoxOrder.setSelectedIndex(0);
					} else {
						this.comboBoxOrder.setSelectedItem(getLayerPropertyModel().getDisplayOrderField());
					}
				}

				this.checkBoxDesc.setSelectedEx(getLayerPropertyModel().isDesc());
				this.textFieldDisplayFilter.setText(getLayerPropertyModel().getDisplayAttributeFilter());
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	protected void registerEvents() {
		caretPositionListener.registerComponent(textFieldMinVisibleGeometrySize);
		this.checkBoxIsCompleteLineSymbolDisplayed.addStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsCrossroadOptimized.addStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsSymbolScalable.addStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsAntialias.addStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsOverlapDisplayed.addStateChangeListener(this.checkBoxStateChangeListener);
		this.comboBoxSymbolScale.addItemListener(comboBoxItemListener);
		this.textFieldMinVisibleGeometrySize.addPropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.textFieldPropertyChangeListener);
		this.comboBoxOrder.addItemListener(comboBoxItemListener);
		this.checkBoxDesc.addStateChangeListener(this.checkBoxStateChangeListener);
		this.buttonDisplayFilter.addActionListener(this.actionListener);
		this.textFieldDisplayFilter.getDocument().addDocumentListener(this.documentListener);
		this.buttonJoinItem.addMouseListener(this.joinItemsListener);
	}

	private void setDisplayAttributeFilterValue() {
		String attributeFilter = textFieldDisplayFilter.getText() == null ? "" : textFieldDisplayFilter.getText();
//		if (!StringUtilities.isNullOrEmpty(textFieldDisplayFilter.getText())) {
			getModifiedLayerPropertyModel().setDisplayAttributeFilter(attributeFilter);
			checkChanged();
//		}
	}

	@Override
	protected void unregisterEvents() {
		caretPositionListener.unRegisterComponent(textFieldMinVisibleGeometrySize);
		this.checkBoxIsCompleteLineSymbolDisplayed.removeStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsCrossroadOptimized.removeStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsSymbolScalable.removeStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsAntialias.removeStateChangeListener(this.checkBoxStateChangeListener);
		this.checkBoxIsOverlapDisplayed.removeStateChangeListener(this.checkBoxStateChangeListener);
		this.comboBoxSymbolScale.removeItemListener(comboBoxItemListener);
		this.textFieldMinVisibleGeometrySize.removePropertyChangeListener(ControlDefaultValues.PROPERTYNAME_VALUE, this.textFieldPropertyChangeListener);
		this.comboBoxOrder.removeItemListener(comboBoxItemListener);
		this.checkBoxDesc.removeStateChangeListener(this.checkBoxStateChangeListener);
		this.buttonDisplayFilter.removeActionListener(actionListener);
		this.textFieldDisplayFilter.getDocument().removeDocumentListener(documentListener);
		this.buttonJoinItem.removeMouseListener(this.joinItemsListener);
	}

	private void checkBoxIsCompleteLineSymbolDisplayedCheckedChanged() {
		getModifiedLayerPropertyModel().setCompleteLineSymbolDisplayed(this.checkBoxIsCompleteLineSymbolDisplayed.isSelectedEx());
		checkChanged();
	}

	private void checkBoxIsCrossroadOptimizedCheckedChanged() {
		getModifiedLayerPropertyModel().setCrossroadOptimized(this.checkBoxIsCrossroadOptimized.isSelectedEx());
		checkChanged();
	}

	private void checkBoxIsSymbolScalableCheckedChanged() {
		getModifiedLayerPropertyModel().setSymbolScalable(this.checkBoxIsSymbolScalable.isSelectedEx());
		if (this.checkBoxIsSymbolScalable.isSelectedEx()
				&& (getModifiedLayerPropertyModel().getSymbolScale() == null || Double.compare(getModifiedLayerPropertyModel().getSymbolScale(), 0) == 0)) {
			// 选中符号随图显示 而且没设置比例尺
			this.comboBoxSymbolScale.setSelectedIndex(0);
		}
		checkChanged();
	}

	private void checkBoxIsAntialiasCheckedChanged() {
		getModifiedLayerPropertyModel().setAntialias(this.checkBoxIsAntialias.isSelectedEx());
		checkChanged();
	}

	private void checkBoxIsOverlapDisplayedCheckedChanged() {
		getModifiedLayerPropertyModel().setOverlapDisplayed(this.checkBoxIsOverlapDisplayed.isSelectedEx());
		checkChanged();
	}

	private void checkBoxDescCheckedChanged() {
		getModifiedLayerPropertyModel().setDesc(this.checkBoxDesc.isSelectedEx());
		checkChanged();
	}

	private void textFieldMinVisibleGeometrySizeTextChanged() {
		getModifiedLayerPropertyModel().setMinVisibleGeometrySize(Double.valueOf(this.textFieldMinVisibleGeometrySize.getValue().toString()));
		checkChanged();
	}

	private void comboBoxSymbolScaleSelectedItemChanged(ItemEvent e) {
		try {
			double selectedScale = getScale(e.getItem());

			if (Double.compare(selectedScale, ScaleModel.INVALID_SCALE) == 0) {
				this.comboBoxSymbolScale.setSelectedItem(getModifiedLayerPropertyModel().getSymbolScale());
			} else {
				getModifiedLayerPropertyModel().setSymbolScale(selectedScale);
			}

			if (this.comboBoxSymbolScale.getSelectedItem().toString().equalsIgnoreCase(MapViewProperties.getString("String_SetCurrentScale"))) {
				this.comboBoxSymbolScale.removeItemListener(this.comboBoxItemListener);
				this.comboBoxSymbolScale.setSelectedItem(new ScaleModel(selectedScale));
				this.comboBoxSymbolScale.addItemListener(this.comboBoxItemListener);
			} else if (this.comboBoxSymbolScale.getSelectedItem().toString().equalsIgnoreCase(CoreProperties.getString(CoreProperties.Clear))) {
				this.comboBoxSymbolScale.removeItemListener(this.comboBoxItemListener);
				this.comboBoxSymbolScale.setSelectedItem(new ScaleModel(ScaleModel.NONE_SCALE));
				this.comboBoxSymbolScale.addItemListener(this.comboBoxItemListener);
			}

			checkChanged();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void comboBoxOrderSelectedItemChanged(ItemEvent e) {
		Object displayOrderField = e.getItem();

		if (StringUtilities.isNullOrEmptyString(displayOrderField)) {
			getModifiedLayerPropertyModel().setDisplayOrderField(null);
		} else {
			getModifiedLayerPropertyModel().setDisplayOrderField((String) displayOrderField);
		}
		checkChanged();
	}

	private double getScale(Object selectedItem) {
		double scale = 0;

		try {
			if (selectedItem instanceof ScaleModel) {
				scale = ((ScaleModel) selectedItem).getScale();
			} else {
				String selectedString = selectedItem.toString();

				if (selectedString.equalsIgnoreCase(MapViewProperties.getString("String_SetCurrentScale"))) {
					scale = getLayerPropertyModel().getFormMap().getMapControl().getMap().getScale();
				} else if (selectedString.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Clear))) {
					scale = ScaleModel.NONE_SCALE;
				} else {
					ScaleModel scaleModel = new ScaleModel(selectedString);
					scale = scaleModel.getScale();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return scale;
	}

	private class ComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxSymbolScale) {
					comboBoxSymbolScaleSelectedItemChanged(e);
				} else if (e.getSource() == comboBoxOrder) {
					comboBoxOrderSelectedItemChanged(e);
				}
			}
		}
	}

	private class MouseJoinItemsListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON1 && arg0.getClickCount() == 1) {
				setJoinItems();
			}
		}
	}

	private class TextFieldMinVisibleGeometrySizePropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			textFieldMinVisibleGeometrySizeTextChanged();
		}
	}

	@Override
	protected void setControlEnabled(String propertyName, boolean enabled) {
		if (propertyName.equals(LayerVectorParamPropertyModel.IS_COMPLETE_LINE_SYMBOL_DISPLAYED)) {
			this.checkBoxIsCompleteLineSymbolDisplayed.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.IS_CROSSROAD_OPTIMIZED)) {
			this.checkBoxIsCrossroadOptimized.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.IS_SYMBOL_SCALABLE)) {
			this.checkBoxIsSymbolScalable.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.IS_ANTIALIAS)) {
			this.checkBoxIsAntialias.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.IS_OVERLAP_DISPLAYED)) {
			this.checkBoxIsOverlapDisplayed.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.SYMBOL_SCALE)) {
			this.comboBoxSymbolScale.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.MIN_VISIBLE_GEOMETRY_SIZE)) {
			this.textFieldMinVisibleGeometrySize.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.DISPLAY_ORDER_FIELD)) {
			this.comboBoxOrder.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.IS_DESC)) {
			this.checkBoxDesc.setEnabled(enabled);
		} else if (propertyName.equals(LayerVectorParamPropertyModel.DISPLAY_ATTRIBUTE_FILTER)) {
			this.textFieldDisplayFilter.setEnabled(enabled);
			this.buttonDisplayFilter.setEnabled(enabled);
			this.buttonJoinItem.setEnabled(enabled);
		}
	}

	protected void setJoinItems() {
		JoinItems joinItems = getModifiedLayerPropertyModel().getLayers()[0].getDisplayFilter().getJoinItems();
		JDialogJoinItems jDialogJoinItem = new JDialogJoinItems(joinItems);
		Dataset dataset = getModifiedLayerPropertyModel().getDataset();
		jDialogJoinItem.setCurrentDataset(dataset);
		if (jDialogJoinItem.showDialog() == DialogResult.OK) {
			// 修改属性
			JoinItems tempJoinItems = jDialogJoinItem.getJoinItems();
			getModifiedLayerPropertyModel().getLayers()[0].getDisplayFilter().setJoinItems(tempJoinItems);
			jDialogJoinItem.dispose();
			checkChanged();
		}
	}
}
