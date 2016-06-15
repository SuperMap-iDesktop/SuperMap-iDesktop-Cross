package com.supermap.desktop.ui.controls.comboBox;

/**
 * @author XiaJT
 */

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class SearchBoxModel<T> extends AbstractListModel
		implements MutableComboBoxModel, KeyListener, ItemListener {
	private SearchItemValueGetter searchItemValueGetter;
	ArrayList<T> data = new ArrayList<>();
	ArrayList<T> searchData = new ArrayList<>();
	T selection;
	JComboBox<Object> comboBox;
	ComboBoxEditor comboBoxEditor;
	int currentPos = 0;


	public SearchBoxModel(JComboBox<Object> comboBox, SearchItemValueGetter searchItemValueGetter) {
		this.comboBox = comboBox;
		this.searchItemValueGetter = searchItemValueGetter == null ? new SearchItemValueGetter() : searchItemValueGetter;
		comboBoxEditor = comboBox.getEditor();
		//here we add the key listener to the text field that the combobox is wrapped around
		comboBoxEditor.getEditorComponent().addKeyListener(this);

	}

	public void setSearchItemValueGetter(SearchItemValueGetter searchItemValueGetter) {
		this.searchItemValueGetter = searchItemValueGetter;
	}

	public void updateModel(String in) {
		resetSearchData();
//lets find any items which start with the string the user typed, and add it to the popup list
//here you would usually get your items from a database, or some other storage...
		// FIXME: 2016/6/14
		/*for (String s : data)
			if (s.startsWith(in))
				searchData.add(s);*/
		for (int i = searchData.size() - 1; i >= 0; i--) {
			String searchString = searchItemValueGetter.getSearchString(i);
			if (!isContain(searchString, in)) {
				searchData.remove(i);
			}
		}
		super.fireContentsChanged(this, 0, searchData.size());

//this is a hack to get around redraw problems when changing the list length of the displayed popups
		comboBox.hidePopup();
		comboBox.showPopup();
		if (searchData.size() != 0)
			comboBox.setSelectedIndex(0);
	}

	private boolean isContain(String searchString, String in) {

		return false;
	}

	private void resetSearchData() {
		searchData.clear();
		searchData.addAll(data);
	}

	public int getSize() {
		return searchData.size();
	}

	public Object getElementAt(int index) {
		return searchData.get(index);
	}

	public void setSelectedItem(Object anItem) {
		selection = (T) anItem;
	}

	public Object getSelectedItem() {
		return selection;
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		String str = comboBoxEditor.getItem().toString();
		JTextField jtf = (JTextField) comboBoxEditor.getEditorComponent();
		currentPos = jtf.getCaretPosition();

		if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
			if (e.getKeyCode() != KeyEvent.VK_ENTER) {
				comboBoxEditor.setItem(str);
				jtf.setCaretPosition(currentPos);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER)
			comboBox.setSelectedIndex(comboBox.getSelectedIndex());
		else {
			updateModel(comboBox.getEditor().getItem().toString());
			comboBoxEditor.setItem(str);
			jtf.setCaretPosition(currentPos);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		comboBoxEditor.setItem(e.getItem().toString());
		comboBox.setSelectedItem(e.getItem());
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		super.addListDataListener(l);
	}

	@Override
	public void addElement(Object item) {
		data.add((T) item);
		resetSearchData();
	}

	@Override
	public void removeElement(Object obj) {
		data.remove(obj);
		resetSearchData();
	}

	@Override
	public void insertElementAt(Object item, int index) {
		data.add(index, (T) item);
		resetSearchData();
	}

	@Override
	public void removeElementAt(int index) {
		data.remove(index);
		resetSearchData();
	}

	public static void main(String[] args) {
		JComboBox<Object> jComboBox = new JComboBox<>();
		jComboBox.setModel(new SearchBoxModel<>(jComboBox, null));
		jComboBox.setEditable(true);
		jComboBox.addItem("asd");
		JFrame jFrame = new JFrame();
		jFrame.add(jComboBox);
		jFrame.setSize(new Dimension(200, 200));
		jFrame.setVisible(true);
	}
}
