package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.Application;
import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.ScaleModel;
import com.supermap.desktop.exception.InvalidScaleException;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerBasePropertyModel;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.SMSpinner;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.utilties.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 有3点需要在结构中考虑。 1：及时刷新 2：多图层批量编辑 3：属性状态改变事件 本想让结构简单化，但是要处理以上三点，就不得不再做一个 Model 类了
 *
 * @author highsad
 */
public class LayerBasePropertyControl extends AbstractLayerPropertyControl implements ILayerProperty {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private TristateCheckBox checkBoxIsVisible;
	private TristateCheckBox checkBoxIsEditable;
	private TristateCheckBox checkBoxIsSelectable;
	private TristateCheckBox checkBoxIsSnapable;
	private JLabel labelLayerName;
	private JLabel labelCaption;
	private JLabel labelTransparence;
	private JLabel labelMinVisibleScale;
	private JLabel labelMaxVisibleScale;
	private JTextField textFieldLayerName;
	private JTextField textFieldLayerCaption;
	private SMSpinner spinnerTransparence;
	private JComboBox<Object> comboBoxMinVisibleScale;
	private JComboBox<Object> comboBoxMaxVisibleScale;
	private transient StateChangeListener checkBoxListener = new CheckBoxStateChangeListener();
	private transient TextFieldLayerCaptionDocumentListener textFieldLayerCaptionListener = new TextFieldLayerCaptionDocumentListener();
	private transient SpinnerTransparenceChangeListener spinnerTransparenceChangedListener = new SpinnerTransparenceChangeListener();
	private transient ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();
	private transient ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			textFieldLayerCaptionAction();
		}
	};
	private transient FocusAdapter focusAdapter = new FocusAdapter() {

		@Override
		public void focusLost(FocusEvent e) {
			textFieldLayerCaptionLostFocus();
		}
	};

	public LayerBasePropertyControl() {
		// TODO 后续根据需求进行一些初始化操作
	}

	@Override
	public LayerBasePropertyModel getLayerPropertyModel() {
		return (LayerBasePropertyModel) super.getLayerPropertyModel();
	}

	@Override
	protected LayerBasePropertyModel getModifiedLayerPropertyModel() {
		return (LayerBasePropertyModel) super.getModifiedLayerPropertyModel();
	}

	@Override
	protected void initializeComponents() {
		this.setBorder(BorderFactory.createTitledBorder("BaseSettings"));

		this.checkBoxIsVisible = new TristateCheckBox("IsVisible");
		this.checkBoxIsEditable = new TristateCheckBox("IsEditable");
		this.checkBoxIsSelectable = new TristateCheckBox("IsSelectable");
		this.checkBoxIsSnapable = new TristateCheckBox("IsSnapable");

		this.labelLayerName = new JLabel("Name:");
		this.labelCaption = new JLabel("Caption:");
		this.labelTransparence = new JLabel("Transparence:");
		this.labelMinVisibleScale = new JLabel("MinVisibleScale:");
		this.labelMaxVisibleScale = new JLabel("MaxVisibleScale:");
		this.textFieldLayerName = new JTextField();
		this.textFieldLayerName.setEditable(false);
		this.textFieldLayerCaption = new JTextField();
		this.spinnerTransparence = new SMSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		this.comboBoxMinVisibleScale = new JComboBox<>();
		this.comboBoxMaxVisibleScale = new JComboBox<>();

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(checkBoxIsVisible)
						.addComponent(checkBoxIsSelectable)
						.addComponent(labelLayerName, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelCaption, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelTransparence, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelMinVisibleScale, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE)
						.addComponent(labelMaxVisibleScale, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, Short.MAX_VALUE))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(checkBoxIsEditable)
						.addComponent(checkBoxIsSnapable)
						.addComponent(textFieldLayerName, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(textFieldLayerCaption, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(spinnerTransparence, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(comboBoxMinVisibleScale, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)
						.addComponent(comboBoxMaxVisibleScale, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_COMPONENT_WIDTH, Short.MAX_VALUE)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(checkBoxIsVisible)
						.addComponent(checkBoxIsEditable))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(checkBoxIsSelectable)
						.addComponent(checkBoxIsSnapable))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelLayerName)
						.addComponent(textFieldLayerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelCaption)
						.addComponent(textFieldLayerCaption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelTransparence)
						.addComponent(spinnerTransparence, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelMinVisibleScale)
						.addComponent(comboBoxMinVisibleScale, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(labelMaxVisibleScale)
						.addComponent(comboBoxMaxVisibleScale, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		// @formatter:on
	}

	@Override
	protected void initializeResources() {
		((TitledBorder) this.getBorder()).setTitle(MapViewProperties.getString("String_MapProperty_VisualControl"));
		this.checkBoxIsVisible.setText(MapViewProperties.getString("String_Visible"));
		this.checkBoxIsEditable.setText(MapViewProperties.getString("String_Editable"));
		this.checkBoxIsSelectable.setText(MapViewProperties.getString("String_LayerControl_Selectable"));
		this.checkBoxIsSnapable.setText(MapViewProperties.getString("String_LayerControl_Snapable"));
		this.labelLayerName.setText(MapViewProperties.getString("String_LayerControl_Label_LayerName"));
		this.labelCaption.setText(MapViewProperties.getString("String_LayerControl_Lalbel_LayerCaption"));
		this.labelTransparence.setText(MapViewProperties.getString("String_transparentRate"));
		this.labelMinVisibleScale.setText(MapViewProperties.getString("String_LayerControl_MinVisibleScale"));
		this.labelMaxVisibleScale.setText(MapViewProperties.getString("String_LayerControl_MaxVisibleScale"));
	}

	@Override
	protected void fillComponents() {
		try {
			if (getLayerPropertyModel() != null) {
				this.checkBoxIsVisible.setSelectedEx(getLayerPropertyModel().isVisible());
				this.checkBoxIsEditable.setSelectedEx(getLayerPropertyModel().isEditable());
				this.checkBoxIsSelectable.setSelectedEx(getLayerPropertyModel().isSelectable());
				this.checkBoxIsSnapable.setSelectedEx(getLayerPropertyModel().isSnapable());
				this.textFieldLayerName.setText(getLayerPropertyModel().getName());
				this.textFieldLayerCaption.setText(getLayerPropertyModel().getCaption());
				LayerPropertyControlUtilties.setSpinnerValue(this.spinnerTransparence, getLayerPropertyModel().getTransparence());
				initializeComboBoxScale(comboBoxMinVisibleScale);
				initializeComboBoxScale(comboBoxMaxVisibleScale);
				if (null != getLayerPropertyModel().getMinVisibleScale()) {
					comboBoxMinVisibleScale.setSelectedItem(new ScaleModel(getLayerPropertyModel().getMinVisibleScale()));
				}
				if (null != getLayerPropertyModel().getMaxVisibleScale()) {
					comboBoxMaxVisibleScale.setSelectedItem(new ScaleModel(getLayerPropertyModel().getMaxVisibleScale()));
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	protected void registerEvents() {
		this.checkBoxIsVisible.addStateChangeListener(checkBoxListener);
		this.checkBoxIsEditable.addStateChangeListener(checkBoxListener);
		this.checkBoxIsSelectable.addStateChangeListener(checkBoxListener);
		this.checkBoxIsSnapable.addStateChangeListener(checkBoxListener);
		this.textFieldLayerCaption.getDocument().addDocumentListener(this.textFieldLayerCaptionListener);
		this.textFieldLayerCaption.addActionListener(this.actionListener);
		this.textFieldLayerCaption.addFocusListener(this.focusAdapter);
		this.spinnerTransparence.addChangeListener(this.spinnerTransparenceChangedListener);
		this.comboBoxMinVisibleScale.addItemListener(comboBoxItemListener);
		this.comboBoxMaxVisibleScale.addItemListener(comboBoxItemListener);
	}

	@Override
	protected void unregisterEvents() {
		this.checkBoxIsVisible.removeStateChangeListener(checkBoxListener);
		this.checkBoxIsEditable.removeStateChangeListener(checkBoxListener);
		this.checkBoxIsSelectable.removeStateChangeListener(checkBoxListener);
		this.checkBoxIsSnapable.removeStateChangeListener(checkBoxListener);
		this.textFieldLayerCaption.getDocument().removeDocumentListener(this.textFieldLayerCaptionListener);
		this.textFieldLayerCaption.removeActionListener(this.actionListener);
		this.textFieldLayerCaption.removeFocusListener(this.focusAdapter);
		this.spinnerTransparence.removeChangeListener(this.spinnerTransparenceChangedListener);
		this.comboBoxMinVisibleScale.removeItemListener(comboBoxItemListener);
		this.comboBoxMaxVisibleScale.removeItemListener(comboBoxItemListener);
	}

	private void initializeComboBoxScale(JComboBox<Object> comboBox) {
		try {
			comboBox.removeAllItems();
			comboBox.setEditable(true);
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_5000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_10000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_25000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_50000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_100000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_250000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_500000));
			comboBox.addItem(new ScaleModel(ScaleModel.SCALE_1000000));
			comboBox.addItem(MapViewProperties.getString("String_SetCurrentScale"));
			comboBox.addItem(CoreProperties.getString(CoreProperties.Clear));

		} catch (InvalidScaleException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	private void checkBoxIsVisibleCheckedChanged() {
		getModifiedLayerPropertyModel().setVisible(this.checkBoxIsVisible.isSelectedEx());
		checkChanged();
	}

	private void checkBoxIsEditableCheckedChanged() {
		getModifiedLayerPropertyModel().setEditable(this.checkBoxIsEditable.isSelectedEx());
		checkChanged();
	}

	private void checkBoxIsSelectableCheckedChanged() {
		getModifiedLayerPropertyModel().setSelectable(this.checkBoxIsSelectable.isSelectedEx());
		checkChanged();
	}

	private void checkBoxIsSnapableCheckedChanged() {
		getModifiedLayerPropertyModel().setSnapable(this.checkBoxIsSnapable.isSelectedEx());
		checkChanged();
	}

	private void textFieldLayerCaptionTextChanged() {
		if (!StringUtilities.isNullOrEmpty(this.textFieldLayerCaption.getText().trim())) {
			getModifiedLayerPropertyModel().setCaption(this.textFieldLayerCaption.getText());
			checkChanged();
		}
	}

	private void textFieldLayerCaptionAction() {
		if (StringUtilities.isNullOrEmpty(this.textFieldLayerCaption.getText().trim())) {
			this.textFieldLayerCaption.setText(getModifiedLayerPropertyModel().getCaption());
		}
	}

	private void textFieldLayerCaptionLostFocus() {
		if (StringUtilities.isNullOrEmpty(this.textFieldLayerCaption.getText().trim())) {
			this.textFieldLayerCaption.setText(getModifiedLayerPropertyModel().getCaption());
		}
	}

	private void spinnerTransparenceChanged() {
		getModifiedLayerPropertyModel().setTransparence(LayerPropertyControlUtilties.getSpinnerValue(this.spinnerTransparence));
		checkChanged();
	}

	private void comboBoxMinVisibleScaleSelectedItemChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				double selectedScale = getScale(e.getItem());
				boolean isChanged = false;

				if (Double.compare(selectedScale, ScaleModel.INVALID_SCALE) == 0) {
					this.comboBoxMinVisibleScale.setSelectedItem(new ScaleModel(getModifiedLayerPropertyModel().getMinVisibleScale()));
				} else {
					if (Double.compare(getModifiedLayerPropertyModel().getMaxVisibleScale(), selectedScale) >= 0
							|| Double.compare(getModifiedLayerPropertyModel().getMaxVisibleScale(), 0) == 0) {
						getModifiedLayerPropertyModel().setMinVisibleScale(selectedScale);
						isChanged = true;
					} else {
						this.comboBoxMinVisibleScale.setSelectedItem(new ScaleModel(getModifiedLayerPropertyModel().getMinVisibleScale()));
					}

					// 如果选中的是当前可见比例尺，那么就把文本设置为当前比例尺，如果选中的是清除，那么就把文本设置为 NONE
					if (isChanged
							&& this.comboBoxMinVisibleScale.getSelectedItem().toString()
									.equalsIgnoreCase(MapViewProperties.getString("String_SetCurrentScale"))) {
						this.comboBoxMinVisibleScale.removeItemListener(comboBoxItemListener);
						this.comboBoxMinVisibleScale.setSelectedItem(new ScaleModel(selectedScale));
						this.comboBoxMinVisibleScale.addItemListener(comboBoxItemListener);
					} else if (isChanged
							&& this.comboBoxMinVisibleScale.getSelectedItem().toString().equalsIgnoreCase(CoreProperties.getString(CoreProperties.Clear))) {
						this.comboBoxMinVisibleScale.removeItemListener(comboBoxItemListener);
						this.comboBoxMinVisibleScale.setSelectedItem(new ScaleModel(ScaleModel.NONE_SCALE));
						this.comboBoxMinVisibleScale.addItemListener(comboBoxItemListener);
					}
				}
			}
			checkChanged();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
	}

	private void comboBoxMaxVisibleScaleSelectedItemChanged(ItemEvent e) {
		try {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				double selectedScale = getScale(e.getItem());
				boolean isChanged = false;
				if (Double.compare(selectedScale, ScaleModel.INVALID_SCALE) == 0) {
					this.comboBoxMaxVisibleScale.setSelectedItem(new ScaleModel(getModifiedLayerPropertyModel().getMaxVisibleScale()));
				} else {
					if (Double.compare(selectedScale, getModifiedLayerPropertyModel().getMinVisibleScale()) >= 0 || Double.compare(selectedScale, 0) == 0) {
						getModifiedLayerPropertyModel().setMaxVisibleScale(selectedScale);
						isChanged = true;
					} else {
						this.comboBoxMaxVisibleScale.setSelectedItem(new ScaleModel(getModifiedLayerPropertyModel().getMaxVisibleScale()));
					}
					// 如果选中的是当前可见比例尺，那么就把文本设置为当前比例尺，如果选中的是清除，那么就把文本设置为 NONE
					if (isChanged
							&& this.comboBoxMaxVisibleScale.getSelectedItem().toString()
									.equalsIgnoreCase(MapViewProperties.getString("String_SetCurrentScale"))) {
						this.comboBoxMaxVisibleScale.removeItemListener(comboBoxItemListener);
						this.comboBoxMaxVisibleScale.setSelectedItem(new ScaleModel(selectedScale));
						this.comboBoxMaxVisibleScale.addItemListener(comboBoxItemListener);
					} else if (isChanged
							&& this.comboBoxMaxVisibleScale.getSelectedItem().toString().equalsIgnoreCase(CoreProperties.getString(CoreProperties.Clear))) {
						this.comboBoxMaxVisibleScale.removeItemListener(comboBoxItemListener);
						this.comboBoxMaxVisibleScale.setSelectedItem(new ScaleModel(ScaleModel.NONE_SCALE));
						this.comboBoxMaxVisibleScale.addItemListener(comboBoxItemListener);
					}
				}
			}
			checkChanged();
		} catch (Exception e2) {
			Application.getActiveApplication().getOutput().output(e2);
		}
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
				} else if (StringUtilities.isNullOrEmpty(selectedString) || selectedString.equalsIgnoreCase(CoreProperties.getString(CoreProperties.Clear))) {
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

	private class CheckBoxStateChangeListener implements StateChangeListener {

		@Override
		public void stateChange(StateChangeEvent e) {
			if (e.getSource() == checkBoxIsVisible) {
				checkBoxIsVisibleCheckedChanged();
			} else if (e.getSource() == checkBoxIsEditable) {
				checkBoxIsEditableCheckedChanged();
			} else if (e.getSource() == checkBoxIsSelectable) {
				checkBoxIsSelectableCheckedChanged();
			} else if (e.getSource() == checkBoxIsSnapable) {
				checkBoxIsSnapableCheckedChanged();
			}
		}
	}

	private class TextFieldLayerCaptionDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			textFieldLayerCaptionTextChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			textFieldLayerCaptionTextChanged();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// nothing
		}
	}

	private class SpinnerTransparenceChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			spinnerTransparenceChanged();
		}
	}

	private class ComboBoxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxMinVisibleScale) {
				comboBoxMinVisibleScaleSelectedItemChanged(e);
			} else if (e.getSource() == comboBoxMaxVisibleScale) {
				comboBoxMaxVisibleScaleSelectedItemChanged(e);
			}
		}
	}

	@Override
	protected void setControlEnabled(String propertyName, boolean enabled) {
		if (propertyName.equals(LayerBasePropertyModel.IS_VISIBLE)) {
			this.checkBoxIsVisible.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.IS_EDITABLE)) {
			this.checkBoxIsEditable.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.IS_SELECTABLE)) {
			this.checkBoxIsSelectable.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.IS_SNAPABLE)) {
			this.checkBoxIsSnapable.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.NAME)) {
			this.textFieldLayerName.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.CAPTION)) {
			this.textFieldLayerCaption.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.TRANSPARENCE)) {
			this.spinnerTransparence.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.MIN_VISIBLE_SCALE)) {
			this.comboBoxMinVisibleScale.setEnabled(enabled);
		} else if (propertyName.equals(LayerBasePropertyModel.MAX_VISIBLE_SCALE)) {
			this.comboBoxMaxVisibleScale.setEnabled(enabled);
		}
	}
}
