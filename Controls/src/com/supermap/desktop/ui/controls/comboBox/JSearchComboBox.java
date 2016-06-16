package com.supermap.desktop.ui.controls.comboBox;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JSearchComboBox<E> extends JComboBox<E> {
	public JSearchComboBox() {
		super();
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
}
