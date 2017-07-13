package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.data.Dataset;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.events.FieldConstraintChangedEvent;
import com.supermap.desktop.process.parameter.implement.ParameterSQLExpression;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by xie on 2017/6/28.
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.SQL_EXPRESSION)
public class ParameterSQLExpressionPanel extends SwingPanel implements IParameterPanel {
	private ParameterSQLExpression parameterSQLExpression;
	private JButton buttonExpression;
	private boolean isSelectingItem = false;

	public ParameterSQLExpressionPanel(IParameter parameter) {
		super(parameter);
		this.parameterSQLExpression = (ParameterSQLExpression) parameter;
		init();
	}

	private void init() {
		this.buttonExpression = new JButton();
		this.buttonExpression.setText(parameterSQLExpression.getDescribe());
		this.buttonExpression.setEnabled(parameterSQLExpression.isEnabled());
		this.panel.setLayout(new GridBagLayout());
		this.panel.add(this.buttonExpression, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(parameterSQLExpression.getAnchor()).setWeight(0, 0).setFill(GridBagConstraints.NONE));
		registEvents();
	}

	private void registEvents() {
		this.buttonExpression.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isSelectingItem) {
					isSelectingItem = true;
					SQLExpressionDialog sqlExpressionDialog = new SQLExpressionDialog();

					if (sqlExpressionDialog.showDialog("", parameterSQLExpression.getSelectDataset()) == DialogResult.OK) {
						parameterSQLExpression.setSelectedItem(sqlExpressionDialog.getQueryParameter().getAttributeFilter());
					}
					isSelectingItem = false;
				}
			}
		});
	}
}
