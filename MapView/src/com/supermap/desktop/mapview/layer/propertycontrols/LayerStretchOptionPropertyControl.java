package com.supermap.desktop.mapview.layer.propertycontrols;

import com.supermap.desktop.DefaultValues;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.mapview.layer.propertymodel.LayerStretchOptionPropertyModel;
import com.supermap.desktop.ui.SMFormattedTextField;
import com.supermap.desktop.ui.StateChangeEvent;
import com.supermap.desktop.ui.StateChangeListener;
import com.supermap.desktop.ui.TristateCheckBox;
import com.supermap.desktop.ui.controls.CaretPositionListener;
import com.supermap.desktop.utilities.ImageStretchTypeUtilities;
import com.supermap.mapping.ImageStretchType;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

public class LayerStretchOptionPropertyControl extends AbstractLayerPropertyControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private transient CaretPositionListener caretPositionListener = new CaretPositionListener();

	private JLabel labelStretchType; // 拉伸方式
	private JComboBox<String> comboBoxStretchType; // 拉伸方式
	private JLabel labelSdDeviationStretchFactor; // 标准差拉伸系数
	private SMFormattedTextField textFieldSdDeviationStretchFactor; // 标准差拉伸系数
	private JLabel labelGaussianStretchRatioFactor; // 高斯拉伸系数
	private SMFormattedTextField textFieldGaussionStretchRatioFactor; // 高斯拉伸系数
	private TristateCheckBox checkBoxIsGaussionStMiddleFactor; // 高斯拉伸时使用中间值

	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyChar();
			if (keyCode < KeyEvent.VK_0 || keyCode > KeyEvent.VK_9) {
				e.consume();
			} else {
				if (e.getSource() == textFieldSdDeviationStretchFactor) {
					textFieldSdDeviationStretchFactorValueChanged(String.valueOf(textFieldSdDeviationStretchFactor.getText()));
				} else if (e.getSource() == textFieldGaussionStretchRatioFactor) {
					textFieldGaussianStretchRatioFactorValueChanged(String.valueOf(textFieldGaussionStretchRatioFactor.getText()));
				}
			}

		}
	};

	private ItemListener comboBoxItemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == comboBoxStretchType) {
				comboBoxStretchTypeSelectedChanged(e);
			}
		}
	};
	private StateChangeListener stateChangeListener = new StateChangeListener() {

		@Override
		public void stateChange(StateChangeEvent e) {
			if (e.getSource() == checkBoxIsGaussionStMiddleFactor) {
				checkBoxIsGaussianStretchMiddleFactorCheckedChanged();
			}
		}
	};

	public LayerStretchOptionPropertyControl() {
		// TODO
	}

	@Override
	public LayerStretchOptionPropertyModel getLayerPropertyModel() {
		return (LayerStretchOptionPropertyModel) super.getLayerPropertyModel();
	}

	@Override
	public LayerStretchOptionPropertyModel getModifiedLayerPropertyModel() {
		return (LayerStretchOptionPropertyModel) super.getModifiedLayerPropertyModel();
	}

	@Override
	protected void initializeComponents() {
		this.setBorder(BorderFactory.createTitledBorder(MapViewProperties.getString("String_LayerControl_Grid_GroupStretchSettings")));

		this.labelStretchType = new JLabel("StretchType:");
		this.comboBoxStretchType = new JComboBox<String>();
		this.labelSdDeviationStretchFactor = new JLabel("StandardDeviationStretchFactor:");
		this.textFieldSdDeviationStretchFactor = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.labelGaussianStretchRatioFactor = new JLabel("GaussianStretchRatioFactor:");
		this.textFieldGaussionStretchRatioFactor = new SMFormattedTextField(NumberFormat.getNumberInstance());
		this.checkBoxIsGaussionStMiddleFactor = new TristateCheckBox("IsGaussianStretchMiddleFctor");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.labelStretchType, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelSdDeviationStretchFactor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH)
								.addComponent(this.labelGaussianStretchRatioFactor, GroupLayout.PREFERRED_SIZE, DefaultValues.DEFAULT_LABEL_WIDTH, DefaultValues.DEFAULT_LABEL_WIDTH))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.comboBoxStretchType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldSdDeviationStretchFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(this.textFieldGaussionStretchRatioFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addComponent(this.checkBoxIsGaussionStMiddleFactor));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelStretchType)
						.addComponent(this.comboBoxStretchType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelSdDeviationStretchFactor)
						.addComponent(this.textFieldSdDeviationStretchFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.labelGaussianStretchRatioFactor)
						.addComponent(this.textFieldGaussionStretchRatioFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(this.checkBoxIsGaussionStMiddleFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		// @formatter:on
	}

	@Override
	protected void initializeResources() {
		this.labelStretchType.setText(MapViewProperties.getString("String_LayerControl_Grid_StretchMode"));
		this.labelSdDeviationStretchFactor.setText(MapViewProperties.getString("String_LayerControl_Grid_StretchRatio"));
		this.labelGaussianStretchRatioFactor.setText(MapViewProperties.getString("String_LayerControl_Grid_StretchGuassRatio"));
		this.checkBoxIsGaussionStMiddleFactor.setText(MapViewProperties.getString("String_LayerControl_Grid_StretchGuassIsStretchMiddle"));
	}

	@Override
	protected void fillComponents() {
		if (getLayerPropertyModel() != null) {
			fillComboBoxStretchType();
			this.comboBoxStretchType.setSelectedItem(ImageStretchTypeUtilities.toString(getLayerPropertyModel().getStretchType()));
			this.textFieldSdDeviationStretchFactor.setValue(getLayerPropertyModel().getStandardDeviationStretchFactor());
			this.textFieldGaussionStretchRatioFactor.setValue(getLayerPropertyModel().getGaussianStretchRatioFactor());
			this.checkBoxIsGaussionStMiddleFactor.setSelectedEx(getLayerPropertyModel().isGaussianStretchMiddleFactor());
		}
	}

	@Override
	protected void registerEvents() {
		caretPositionListener.registerComponent(textFieldSdDeviationStretchFactor, textFieldGaussionStretchRatioFactor);

		this.comboBoxStretchType.addItemListener(this.comboBoxItemListener);

		this.textFieldSdDeviationStretchFactor.addKeyListener(keyAdapter);
		this.textFieldGaussionStretchRatioFactor.addKeyListener(keyAdapter);
		this.checkBoxIsGaussionStMiddleFactor.addStateChangeListener(this.stateChangeListener);
	}

	@Override
	protected void unregisterEvents() {
		caretPositionListener.unRegisterComponent(textFieldSdDeviationStretchFactor, textFieldGaussionStretchRatioFactor);

		this.comboBoxStretchType.removeItemListener(this.comboBoxItemListener);
		this.textFieldSdDeviationStretchFactor.removeKeyListener(keyAdapter);
		this.textFieldGaussionStretchRatioFactor.removeKeyListener(keyAdapter);
		this.checkBoxIsGaussionStMiddleFactor.removeStateChangeListener(this.stateChangeListener);
	}

	private void fillComboBoxStretchType() {
		this.comboBoxStretchType.removeAllItems();
		this.comboBoxStretchType.addItem(ImageStretchTypeUtilities.toString(ImageStretchType.NONE));
		this.comboBoxStretchType.addItem(ImageStretchTypeUtilities.toString(ImageStretchType.GAUSSIAN));
		this.comboBoxStretchType.addItem(ImageStretchTypeUtilities.toString(ImageStretchType.MINIMUMMAXIMUM));
		this.comboBoxStretchType.addItem(ImageStretchTypeUtilities.toString(ImageStretchType.STANDARDDEVIATION));
	}

	private void comboBoxStretchTypeSelectedChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			getModifiedLayerPropertyModel().setStretchType(ImageStretchTypeUtilities.valueOf((String) this.comboBoxStretchType.getSelectedItem()));
			checkChanged();
		}
	}

	private void textFieldSdDeviationStretchFactorValueChanged(String newValue) {
		getModifiedLayerPropertyModel().setStandardDeviationStretchFactor(Double.valueOf(newValue));
		checkChanged();
	}

	private void textFieldGaussianStretchRatioFactorValueChanged(String newValue) {
		getModifiedLayerPropertyModel().setGaussianStretchRatioFactor(Double.valueOf(newValue));
		checkChanged();
	}

	private void checkBoxIsGaussianStretchMiddleFactorCheckedChanged() {
		getModifiedLayerPropertyModel().setGaussianStretchMiddleFactor(this.checkBoxIsGaussionStMiddleFactor.isSelectedEx());
		checkChanged();
	}

	@Override
	protected void setControlEnabled(String propertyName, boolean enabled) {
		if (propertyName.equals(LayerStretchOptionPropertyModel.STRETCH_TYPE)) {
			this.comboBoxStretchType.setEnabled(enabled);
		} else if (propertyName.equals(LayerStretchOptionPropertyModel.STANDARD_DEVIATION_STRETCH_FACTOR)) {
			this.textFieldSdDeviationStretchFactor.setEnabled(enabled);
		} else if (propertyName.equals(LayerStretchOptionPropertyModel.GAUSSIAN_STRETCH_RATIO_FACTOR)) {
			this.textFieldGaussionStretchRatioFactor.setEnabled(enabled);
		} else if (propertyName.equals(LayerStretchOptionPropertyModel.IS_GAUSSIAN_STRETCH_MIDDLE_FACTOR)) {
			this.checkBoxIsGaussionStMiddleFactor.setEnabled(enabled);
		}
	}
}
