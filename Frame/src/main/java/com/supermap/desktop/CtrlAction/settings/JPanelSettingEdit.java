package com.supermap.desktop.CtrlAction.settings;

import com.supermap.desktop.Application;
import com.supermap.desktop.GlobalParameters;
import com.supermap.desktop.Interface.IFormManager;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.frame.FrameProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.TextFields.ISmTextFieldLegit;
import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.SelectionModeUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.ui.SelectionMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;

/**
 * @author XiaJT
 */
public class JPanelSettingEdit extends BaseSettingPanel {

	private JLabel labelMaxVisibleVertex;
	private SmTextFieldLegit smTextFieldLegitMaxVisibleVertex;
	private JLabel labelSelectionMode;
	private JComboBox<String> comboBoxSelectionMode;
	private JCheckBox checkBoxThemeRefresh;

	private static final String[] selectionModes = new String[]{
			SelectionModeUtilities.toString(SelectionMode.CONTAIN_INNER_POINT),
			SelectionModeUtilities.toString(SelectionMode.INTERSECT),
			SelectionModeUtilities.toString(SelectionMode.CONTAIN_OBJECT),
	};


	@Override
	protected void initComponents() {
		labelMaxVisibleVertex = new JLabel();
		smTextFieldLegitMaxVisibleVertex = new SmTextFieldLegit();
		labelSelectionMode = new JLabel();
		comboBoxSelectionMode = new JComboBox<>(selectionModes);
		checkBoxThemeRefresh = new JCheckBox();
	}

	@Override
	protected void initLayout() {
		this.setLayout(new GridBagLayout());
		JPanel panelMaxVisibleVertex = new JPanel();
		panelMaxVisibleVertex.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_VisibleSetting")));
		panelMaxVisibleVertex.setLayout(new GridBagLayout());
		panelMaxVisibleVertex.add(labelMaxVisibleVertex, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(5, 5, 0, 0).setWeight(0, 0).setAnchor(GridBagConstraints.WEST));
		panelMaxVisibleVertex.add(smTextFieldLegitMaxVisibleVertex, new GridBagConstraintsHelper(1, 0, 1, 1).setInsets(5, 5, 0, 0).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

		JPanel panelCaptionElementSetting = new JPanel();
		panelCaptionElementSetting.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_CaptionElementSetting")));
		panelCaptionElementSetting.setLayout(new GridBagLayout());
		panelCaptionElementSetting.add(labelSelectionMode, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(5, 5, 0, 0).setWeight(0, 0).setAnchor(GridBagConstraints.WEST));
		panelCaptionElementSetting.add(comboBoxSelectionMode, new GridBagConstraintsHelper(1, 0, 1, 1).setInsets(5, 5, 0, 0).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL));

		JPanel panelTheme = new JPanel();
		panelTheme.setBorder(BorderFactory.createTitledBorder(FrameProperties.getString("String_CaptionThream")));
		panelTheme.setLayout(new GridBagLayout());
		panelTheme.add(checkBoxThemeRefresh, new GridBagConstraintsHelper(0, 0, 2, 1).setInsets(5, 5, 0, 0).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));

		this.add(panelMaxVisibleVertex, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(panelCaptionElementSetting, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(panelTheme, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.CENTER));
		this.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
	}

	@Override
	protected void initListeners() {
		smTextFieldLegitMaxVisibleVertex.setSmTextFieldLegit(new ISmTextFieldLegit() {
			@Override
			public boolean isTextFieldValueLegit(String textFieldValue) {
				if (StringUtilities.isNullOrEmpty(textFieldValue)) {
					return false;
				}
				try {
					Integer integer = Integer.valueOf(textFieldValue);
					if (integer < 0 || integer > 100000000) {
						return false;
					}
					if (integer == GlobalParameters.getMaxVisibleVertex()) {
						changedValues.remove(smTextFieldLegitMaxVisibleVertex);
					} else {
						changedValues.add(smTextFieldLegitMaxVisibleVertex);
					}
				} catch (NumberFormatException e) {
					return false;
				}
				return true;
			}

			@Override
			public String getLegitValue(String currentValue, String backUpValue) {
				return backUpValue;
			}
		});
		comboBoxSelectionMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (comboBoxSelectionMode.getSelectedIndex() == GlobalParameters.getPositiveSelect()) {
						changedValues.remove(comboBoxSelectionMode);
					} else {
						changedValues.add(comboBoxSelectionMode);
					}
				}
			}
		});
		checkBoxThemeRefresh.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (changedValues.contains(checkBoxThemeRefresh)) {
					changedValues.remove(checkBoxThemeRefresh);
				} else {
					changedValues.add(checkBoxThemeRefresh);
				}
			}
		});
	}

	@Override
	protected void initResources() {
		labelMaxVisibleVertex.setText(FrameProperties.getString("String_VisibleItemCount"));
		labelSelectionMode.setText(FrameProperties.getString("String_Positive"));
		checkBoxThemeRefresh.setText(FrameProperties.getString("String_ThemeRefersh"));
	}

	@Override
	protected void initComponentStates() {
		smTextFieldLegitMaxVisibleVertex.setText(new BigDecimal(GlobalParameters.getMaxVisibleVertex()).toString());
		comboBoxSelectionMode.setSelectedIndex(GlobalParameters.getPositiveSelect());
		checkBoxThemeRefresh.setSelected(GlobalParameters.isThemeRefresh());
	}

	@Override
	public void apply() {
		for (Component changedValue : changedValues) {
			if (changedValue == smTextFieldLegitMaxVisibleVertex) {
				GlobalParameters.setMaxVisibleVertex(Integer.valueOf(smTextFieldLegitMaxVisibleVertex.getBackUpValue()));
			} else if (changedValue == checkBoxThemeRefresh) {
				GlobalParameters.setThemeRefresh(checkBoxThemeRefresh.isSelected());
			} else if (changedValue == comboBoxSelectionMode) {
				int selectedIndex = comboBoxSelectionMode.getSelectedIndex();
				GlobalParameters.setPositiveSelect(selectedIndex);
				SelectionMode selectionMode = SelectionMode.CONTAIN_INNER_POINT;
				if (selectedIndex == 1) {
					selectionMode = SelectionMode.INTERSECT;
				} else if (selectedIndex == 2) {
					selectionMode = SelectionMode.CONTAIN_OBJECT;
				}
				IFormManager formManager = Application.getActiveApplication().getMainFrame().getFormManager();
				for (int i = 0; i < formManager.getCount(); i++) {
					if (formManager.get(i) instanceof IFormMap) {
						((IFormMap) formManager.get(i)).getMapControl().setSelectionMode(selectionMode);
					}
				}
			}
		}
	}

	@Override
	public void dispose() {

	}
}
