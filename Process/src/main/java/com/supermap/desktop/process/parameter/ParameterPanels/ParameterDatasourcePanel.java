package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Datasource;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterDatasource;
import com.supermap.desktop.process.util.ParameterUtil;
import com.supermap.desktop.ui.controls.DatasourceComboBox;
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
public class ParameterDatasourcePanel extends JPanel {
	private JLabel label = new JLabel();
	private DatasourceComboBox datasourceComboBox = new DatasourceComboBox(Application.getActiveApplication().getWorkspace().getDatasources());
	private ParameterDatasource parameterDatasource;
	private boolean isSelectingItem;

	public ParameterDatasourcePanel(ParameterDatasource parameterDatasource) {
		this.parameterDatasource = parameterDatasource;
		this.label.setText(parameterDatasource.getDescribe());
		this.datasourceComboBox.setSelectedDatasource((Datasource) parameterDatasource.getSelectedItem());
		initLayout();
		initListener();
	}

	private void initLayout() {
		label.setPreferredSize(ParameterUtil.LABEL_DEFAULT_SIZE);
		datasourceComboBox.setPreferredSize(new Dimension(20, 23));
		this.setLayout(new GridBagLayout());
		this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		this.add(datasourceComboBox, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 5, 0, 0));
	}

	private void initListener() {
		datasourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (!isSelectingItem && e.getStateChange() == ItemEvent.SELECTED) {
					isSelectingItem = true;
					parameterDatasource.setSelectedItem(datasourceComboBox.getSelectedDatasource());
					isSelectingItem = false;
				}
			}
		});
		parameterDatasource.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingItem && evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					isSelectingItem = true;
					datasourceComboBox.setSelectedDatasource((Datasource) evt.getNewValue());
					isSelectingItem = false;
				}
			}
		});
	}
}
