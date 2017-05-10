package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.CellRenders.ListDataCellRender;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.DATASOURCE)
public class ParameterDatasourcePanel extends SwingPanel {
	private JLabel label = new JLabel();
	private JComboBox<Datasource> datasourceComboBox = new JComboBox<>();
	private ParameterDatasource parameterDatasource;
	private boolean isSelectingItem;

	public ParameterDatasourcePanel(IParameter parameterDatasource) {
		super(parameterDatasource);
		this.parameterDatasource = ((ParameterDatasource) parameterDatasource);
		this.label.setText(this.parameterDatasource.getDescribe());
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		for (int i = 0; i < datasources.getCount(); i++) {
			datasourceComboBox.addItem(datasources.get(i));
		}
		datasourceComboBox.setRenderer(new ListDataCellRender());
		this.datasourceComboBox.setSelectedItem(this.parameterDatasource.getSelectedItem());
		initLayout();
		initListener();
		if (datasourceComboBox.getSelectedIndex() == -1 && datasourceComboBox.getItemCount() > 0) {
			datasourceComboBox.setSelectedIndex(0);
		}
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasourceComboBox.setPreferredSize(new Dimension(20, 23));
		panel.setLayout(new GridBagLayout());
		panel.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		panel.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initListener() {
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterDatasource.setSelectedItem(datasourceComboBox.getSelectedItem());
					isSelectingItem = false;
				}
			}
		});
		parameterDatasource.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(ParameterDatasource.DATASOURCE_FIELD_NAME)) {
					isSelectingItem = true;
					datasourceComboBox.setSelectedItem(evt.getNewValue());
					isSelectingItem = false;
				}
			}
		});
	}

	@Override
	public void fieldConstraintChanged(FieldConstraintChangedEvent event) {
		if (event.getFieldName().equals(ParameterDatasource.DATASOURCE_FIELD_NAME)) {
			isSelectingItem = true;
			datasourceComboBox.removeAllItems();
			Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
			for (int i = 0; i < datasources.getCount(); i++) {
				Datasource datasource = datasources.get(i);
				if (ParameterDatasourcePanel.this.parameterDatasource.isValueLegal(ParameterDatasource.DATASOURCE_FIELD_NAME, datasource)) {
					datasourceComboBox.addItem(datasource);
				}
			}
			Object selectedItem = ParameterDatasourcePanel.this.parameterDatasource.getSelectedItem();
			if (selectedItem != null) {
				datasourceComboBox.setSelectedItem(selectedItem);
			}
			if (datasourceComboBox.getSelectedIndex() == -1 && datasourceComboBox.getItemCount() > 0) {
				datasourceComboBox.setSelectedIndex(0);
			}
			if (selectedItem != datasourceComboBox.getSelectedItem()) {
				ParameterDatasourcePanel.this.parameterDatasource.setSelectedItem(datasourceComboBox.getSelectedItem());
			}
			isSelectingItem = false;
		}
	}
}
