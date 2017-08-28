package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterFieldGroup;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.smTables.TableFactory;
import com.supermap.desktop.ui.controls.smTables.tables.TableFieldNameCaptionType;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.FIELD_GROUP)
public class ParameterFieldGroupPanel extends SwingPanel {
	private ParameterFieldGroup parameterFieldGroup;
	private JLabel label = new JLabel();
	private TableFieldNameCaptionType tableFieldNameCaptionType = (TableFieldNameCaptionType) TableFactory.getTable("FieldNameCaptionType");

	public ParameterFieldGroupPanel(IParameter parameter) {
		super(parameter);
		parameterFieldGroup = (ParameterFieldGroup) parameter;
		initComponent();
		initLayout();
		initListener();
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterFieldGroup.FIELD_DATASET)) {
			tableFieldNameCaptionType.setDataset(parameterFieldGroup.getDataset());
		}
	}

	private void initComponent() {
		label.setText(parameterFieldGroup.getDescribe());
		label.setToolTipText(parameterFieldGroup.getDescribe());
		tableFieldNameCaptionType.setDataset(parameterFieldGroup.getDataset());
	}

	private void initListener() {
		parameterFieldGroup.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ParameterFieldGroup.FIELD_DATASET)) {
					tableFieldNameCaptionType.setDataset(parameterFieldGroup.getDataset());
				}
			}
		});

		tableFieldNameCaptionType.getTablesModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					parameterFieldGroup.setSelectedFields(tableFieldNameCaptionType.getSelectedFields());
				}
			}
		});
	}

	private void initLayout() {
		panel.setPreferredSize(new Dimension(200, 200));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
		JScrollPane scrollPane = new JScrollPane(tableFieldNameCaptionType);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setInsets(5, 0, 0, 0));
	}
}
