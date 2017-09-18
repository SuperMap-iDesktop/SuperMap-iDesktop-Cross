package com.supermap.desktop.ui.controls.ExpressionComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lixiaoyao on 2017/9/18.
 */
public class FunctionComboBox extends JComboBox<String> {

	private FunctionComboBoxSelectedChangeListener selectedChangeListener=null;

	public FunctionComboBox(String[] functions){
		this.setModel(new DefaultComboBoxModel<String>(functions));
	}

	private void removeEvents(){
		this.removeActionListener(this.functionComboBoxActionListener);
	}

	private void registerEvents(){
		this.addActionListener(this.functionComboBoxActionListener);
	}

	public void addFunctionListener(FunctionComboBoxSelectedChangeListener functionComboBoxSelectedChangeListener){
		this.selectedChangeListener=functionComboBoxSelectedChangeListener;
		registerEvents();
	}

	public void removeFunctionListener(FunctionComboBoxSelectedChangeListener functionComboBoxSelectedChangeListener){
		removeEvents();
	}

	private ActionListener functionComboBoxActionListener = new LocalComboboxAction();

	class LocalComboboxAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (selectedChangeListener!=null) {
				selectedChangeListener.comboBoxFunction_SelectedChanged(((JComboBox<?>) e.getSource()).getSelectedItem().toString());
			}
		}
	}
}
