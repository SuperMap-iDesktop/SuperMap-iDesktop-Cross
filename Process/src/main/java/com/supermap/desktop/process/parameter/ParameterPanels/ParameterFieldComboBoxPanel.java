package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.DatasetVector;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.utilities.JComboBoxUIUtilities;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterFieldComboBox;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FIELD_COMBO_BOX)
public class ParameterFieldComboBoxPanel extends SwingPanel implements IParameterPanel {
	private ParameterFieldComboBox parameterFieldComboBox;
	private boolean isSelectingItem = false;
	private JLabel label = new JLabel();
	private JComboBox<FieldInfo> comboBox = new JComboBox<>();

	public ParameterFieldComboBoxPanel(IParameter parameter) {
		super(parameter);
		this.parameterFieldComboBox = (ParameterFieldComboBox) parameter;
		initComponentState();
		initLayout();
		initComponentListener();

	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		comboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 1));
		panel.add(comboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initComponentListener() {
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterFieldComboBox.setSelectedItem(comboBox.getSelectedItem());
					isSelectingItem = false;
				}
			}
		});
	}

	private void initComponentState() {
		comboBox.setRenderer(new ListCellRenderer<FieldInfo>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends FieldInfo> list, FieldInfo value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = new JLabel();
				if (value != null) {
					jLabel.setText(value.getCaption());
				}
				jLabel.setOpaque(true);
				if (isSelected) {
					jLabel.setBackground(list.getSelectionBackground());
					jLabel.setForeground(list.getSelectionForeground());
				} else {
					jLabel.setBackground(list.getBackground());
					jLabel.setForeground(list.getForeground());
				}
				return jLabel;
			}
		});
		String describe = parameterFieldComboBox.getDescribe();
		if (describe != null) {
			label.setText(describe);
		}
		DatasetVector dataset = parameterFieldComboBox.getDataset();
		resetComboBoxItems(dataset);
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterFieldComboBox.DATASET_FIELD_NAME)) {
			resetComboBoxItems(parameterFieldComboBox.getDataset());
		}
	}

	private void resetComboBoxItems(DatasetVector dataset) {
		isSelectingItem = true;
		comboBox.removeAllItems();
		if (dataset != null) {
			FieldInfos fieldInfos;
			try {
				fieldInfos = dataset.getFieldInfos();
			} catch (Exception e) {
				parameterFieldComboBox.setSelectedItem(null);
				return;
				//ignore
			}
			for (int i = 0; i < fieldInfos.getCount(); i++) {
				if (parameterFieldComboBox.isValueLegal(ParameterFieldComboBox.FILED_INFO_FILED_NAME, fieldInfos.get(i))) {
					comboBox.addItem(fieldInfos.get(i));
				}
			}
			comboBox.setSelectedIndex(-1);
			if (comboBox.getItemCount() > 0) {
				// 先询问参数是否满意当前选项
				// Ask if the parameters are satisfied with the current option
				for (int i = 0; i < comboBox.getItemCount(); i++) {
					Object valueSelected = parameterFieldComboBox.isValueSelected(ParameterFieldComboBox.FILED_INFO_FILED_NAME, fieldInfos.get(i));
					if (valueSelected == AbstractParameter.DO_NOT_CARE) {
						break;
					} else if (valueSelected instanceof FieldInfo) {
						if (JComboBoxUIUtilities.getItemIndex(comboBox, valueSelected) != -1) {
							isSelectingItem = false;
							try {
								comboBox.setSelectedItem(valueSelected);
							} catch (Exception e) {
								isSelectingItem = true;
								Application.getActiveApplication().getOutput().output(e);
							}
							isSelectingItem = true;
						}
						break;
					}
				}
				if (comboBox.getSelectedItem() == null) {
					// 如果没有满意的选项则与当前已设置的值保持一致
					// If there is no satisfactory option, it is consistent with the current set value
					if (JComboBoxUIUtilities.getItemIndex(comboBox, parameterFieldComboBox.getSelectedItem()) != -1) {
						comboBox.setSelectedItem(parameterFieldComboBox.getSelectedItem());
					} else {
						isSelectingItem = false;
						try {
							comboBox.setSelectedIndex(0);
						} catch (Exception e) {
							isSelectingItem = true;
							Application.getActiveApplication().getOutput().output(e);
						}
						isSelectingItem = true;
					}
				}
			} else {
				parameterFieldComboBox.setSelectedItem(null);
			}
		} else {
			parameterFieldComboBox.setSelectedItem(null);
		}
		isSelectingItem = false;
	}
}