package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.data.Dataset;
import com.supermap.desktop.Application;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author XiaJT
 */
public abstract class TableSqlCellEditor extends DefaultCellEditor {
	private SQLExpressionDialog sqlExpressionDialog = new SQLExpressionDialog();
	private JPanel panel = new JPanel();
	private JTextField textField = new JTextField();
	private JButton button = new JButton();
	private int row;

	public TableSqlCellEditor() {
		super(new JTextField());
		setClickCountToStart(1);
		editorComponent = textField;
		textField.addActionListener(delegate);
		panel.setLayout(new GridBagLayout());
		button.setText("...");
		panel.add(textField, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL));
		panel.add(button, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.HORIZONTAL));

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sqlExpressionDialog = new SQLExpressionDialog();
				DialogResult dialogResult = sqlExpressionDialog.showDialog(textField.getText(), getDatasets(row));
				if (dialogResult == DialogResult.OK) {
					String filter = sqlExpressionDialog.getQueryParameter().getAttributeFilter();
					if (filter != null) {
						textField.setText(filter);
					}
				}
				textField.requestFocus();
			}
		});
		panel.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Application.getActiveApplication().getOutput().output("fuck");
			}
		});
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Application.getActiveApplication().getOutput().output("fuck");
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		if (value == null) {
			textField.setText("");
		} else if (value instanceof String) {
			textField.setText((String) value);
		} else {
			textField.setText(String.valueOf(value));
		}
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return textField.getText();
	}

//	@Override
//	public boolean isCellEditable(EventObject anEvent) {
//		return true;
//	}

//	@Override
//	public boolean shouldSelectCell(EventObject anEvent) {
//		return true;
//	}

//	@Override
//	public boolean stopCellEditing() {
//		return false;
//	}
//
//	@Override
//	public void cancelCellEditing() {
//
//	}

//	@Override
//	public void addCellEditorListener(CellEditorListener l) {
//
//	}
//
//	@Override
//	public void removeCellEditorListener(CellEditorListener l) {
//
//	}

	/**
	 * sql表达式需要的数据集
	 *
	 * @return
	 */
	public abstract Dataset[] getDatasets(int row);
}
