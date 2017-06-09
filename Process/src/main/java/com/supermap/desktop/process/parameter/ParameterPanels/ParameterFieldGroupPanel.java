package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.implement.ParameterFieldGroup;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.ui.controls.SortTable.SmSortTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FIELD_GROUP)
public class ParameterFieldGroupPanel extends SwingPanel {
	private ParameterFieldGroup parameterFieldGroup;
	private JLabel label = new JLabel();
	private SmSortTable table = new SmSortTable();
	private FieldGroupTableModel tableModel = new FieldGroupTableModel();

	public ParameterFieldGroupPanel(IParameter parameter) {
		super(parameter);
		parameterFieldGroup = ((ParameterFieldGroup) parameter);
		initComponent();
	}

	private void initComponent() {
		label.setText(parameterFieldGroup.getDescribe());
		label.setToolTipText(parameterFieldGroup.getDescribe());

		table.setModel(tableModel);
	}

	class FieldGroupTableModel extends DefaultTableModel {
		String[] columnHeaders = new String[]{
				"",

		};

		@Override
		public boolean isCellEditable(int row, int column) {
			return column == 0;
		}
	}
}
