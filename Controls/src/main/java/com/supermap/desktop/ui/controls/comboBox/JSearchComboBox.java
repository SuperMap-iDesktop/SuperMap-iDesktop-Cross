package com.supermap.desktop.ui.controls.comboBox;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JSearchComboBox<E> extends JComboBox<E> {
	public JSearchComboBox() {
		super();
		this.setEditable(true);
		setModel(new SearchBoxModel<>(this));
	}

	public void setSearchItemValueGetter(SearchItemValueGetter searchItemValueGetter) {
		if (getModel() instanceof SearchBoxModel) {
			((SearchBoxModel) getModel()).setSearchItemValueGetter(searchItemValueGetter);
		}
	}

	@Override
	public int getSelectedIndex() {
		if (getModel() instanceof SearchBoxModel) {
			return ((SearchBoxModel) getModel()).getSelectedIndex();
		}
		return super.getSelectedIndex();
	}

	public void setIcon(Icon icon) {
		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i) instanceof JButton) {
				((JButton) getComponent(i)).setIcon(icon);
			}
		}
	}
}
