package com.supermap.desktop.CtrlAction.settings;

import com.supermap.desktop.ui.controls.TextFields.SmTextFieldLegit;
import com.supermap.desktop.utilities.SelectionModeUtilities;
import com.supermap.ui.SelectionMode;

import javax.swing.*;
import java.awt.*;

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
		comboBoxSelectionMode = new JComboBox<>();
		checkBoxThemeRefresh = new JCheckBox();
	}

	@Override
	protected void initLayout() {
		this.setLayout(new GridBagLayout());
	}

	@Override
	protected void initListeners() {
		super.initListeners();
	}

	@Override
	protected void initResources() {
		super.initResources();
	}

	@Override
	protected void initComponentStates() {
		super.initComponentStates();
	}

	@Override
	public void apply() {

	}

	@Override
	public void dispose() {

	}
}
