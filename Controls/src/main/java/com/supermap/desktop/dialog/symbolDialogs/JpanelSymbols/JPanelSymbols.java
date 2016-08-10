package com.supermap.desktop.dialog.symbolDialogs.JpanelSymbols;

import com.supermap.data.GeoStyle;
import com.supermap.data.Resources;
import com.supermap.data.Symbol;
import com.supermap.data.SymbolGroup;
import com.supermap.desktop.dialog.symbolDialogs.WrapLayout;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author XiaJt
 */
public abstract class JPanelSymbols extends JPanel {

	private final WrapLayout wrapLayout;
	protected SymbolGroup symbolGroup;
	protected Resources resources;
	private SymbolPanel lastSelectedPanel;
	private java.util.List<SymbolSelectedChangedListener> symbolSelectedChangedListeners;

	private String searchString;
	protected GeoStyle geoStyle;
	private KeyAdapter keyAdapter = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 37) {
				// 左
				setSelectSymbol(-1);
			} else if (e.getKeyCode() == 38) {
				// 上
				setSelectSymbol(-wrapLayout.getColumn());
			} else if (e.getKeyCode() == 39) {
				//右
				setSelectSymbol(1);
			} else if (e.getKeyCode() == 40) {
				//下
				setSelectSymbol(wrapLayout.getColumn());
			}
		}
	};
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getSource() instanceof SymbolPanel) {
				JPanelSymbols.this.requestFocus();
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					fireSymbolDoubleClicked();
				}
				SymbolPanel symbolPanel = (SymbolPanel) e.getSource();
				setSelectedSymbolPanel(symbolPanel);
			}
		}
	};
	;
	private MouseAdapter mouseAdapter1 = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			JPanelSymbols.this.requestFocus();
		}
	};

	public JPanelSymbols() {
		this.setBackground(Color.WHITE);
		this.setFocusable(true);
		wrapLayout = new WrapLayout(FlowLayout.LEADING);
		wrapLayout.setAlignOnBaseline(true);
		this.setLayout(wrapLayout);
	}

	public void setSymbolGroup(Resources resources, final SymbolGroup symbolGroup) {
		if (symbolGroup == null || this.symbolGroup == symbolGroup) {
			return;
		}
		this.resources = resources;
		this.removeAll();
		this.symbolGroup = symbolGroup;
		if (symbolGroup.getLibrary().getRootGroup() == symbolGroup) {
			initSystemPanels();
		}
		initDefaultPanel();
		addListeners();
		search();
		this.updateUI();
	}

	private void addListeners() {
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).removeMouseListener(mouseAdapter);
			this.getComponent(i).addMouseListener(mouseAdapter);
		}
		this.removeKeyListener(keyAdapter);
		this.addKeyListener(keyAdapter);
		this.removeMouseListener(mouseAdapter1);
		this.addMouseListener(mouseAdapter1);
	}

	private void setSelectedSymbolPanel(SymbolPanel symbolPanel) {
		if (symbolPanel == lastSelectedPanel || symbolPanel == null) {
			return;
		}
		if (lastSelectedPanel != null) {
			lastSelectedPanel.setUnselected();
		}
		symbolPanel.setSelected();
		lastSelectedPanel = symbolPanel;
		changeGeoStyleId(lastSelectedPanel.getSymbolID());
		if (this.getParent() != null && this.getParent().getParent() instanceof JScrollPane) {
			this.scrollRectToVisible(lastSelectedPanel.getBounds());
		}
		fireSymbolSelectedChanged(lastSelectedPanel.getSymbol());
	}

	private void setSelectSymbol(int distance) {
		if (this.getComponentCount() <= 0 || distance == 0) {
			return;
		}
		int currentIndex = this.getComponentZOrder(lastSelectedPanel);
		SymbolPanel resultSymbolPanel = null;
		if (distance > 0) {
			for (int i = 0, j = currentIndex; i <= distance && j < this.getComponentCount(); j++) {
				if (this.getComponent(j).isVisible()) {
					resultSymbolPanel = ((SymbolPanel) this.getComponent(j));
					i++;
				}
			}
		} else {
			for (int i = 0, j = currentIndex; i <= -distance && j >= 0; j--) {
				if (this.getComponent(j).isVisible()) {
					resultSymbolPanel = ((SymbolPanel) this.getComponent(j));
					i++;
				}
			}
		}
		setSelectedSymbolPanel(resultSymbolPanel);
	}

	private void fireSymbolDoubleClicked() {
		if (symbolSelectedChangedListeners != null) {
			for (SymbolSelectedChangedListener symbolSelectedChangedListener : symbolSelectedChangedListeners) {
				symbolSelectedChangedListener.SymbolSelectedDoubleClicked();
			}
		}
	}

	private void fireSymbolSelectedChanged(Symbol symbol) {
		if (symbolSelectedChangedListeners != null) {
			for (SymbolSelectedChangedListener symbolSelectedChangedListener : symbolSelectedChangedListeners) {
				symbolSelectedChangedListener.SymbolSelectedChangedEvent(symbol);
			}
		}
	}

	public void addSymbolSelectedChangedListener(SymbolSelectedChangedListener symbolSelectedChangedListener) {
		if (this.symbolSelectedChangedListeners == null) {
			symbolSelectedChangedListeners = new ArrayList<>();
		}
		symbolSelectedChangedListeners.add(symbolSelectedChangedListener);
	}

	public void removeSymbolSelectedChangedListener(SymbolSelectedChangedListener symbolSelectedChangedListener) {
		symbolSelectedChangedListeners.remove(symbolSelectedChangedListener);
	}

	/**
	 * 点线面分别设置对应ID
	 *
	 * @param symbolID 修改后的id
	 */
	protected abstract void changeGeoStyleId(int symbolID);

	protected abstract void initSystemPanels();

	protected abstract void initDefaultPanel();

	/**
	 * 初始化的时候使用
	 */
	private void setSelectSymbol() {
		int symbolId = getCurrentSymbolId();
		boolean isFind = false;
		for (int i = 0; i < this.getComponentCount(); i++) {
			if (((SymbolPanel) this.getComponent(i)).getSymbolID() == symbolId) {
				if (lastSelectedPanel != null) {
					lastSelectedPanel.setUnselected();
				}
				lastSelectedPanel = (SymbolPanel) this.getComponent(i);
				lastSelectedPanel.setSelected();
				if (this.getParent() != null && this.getParent().getParent() instanceof JScrollPane) {
					((JScrollPane) this.getParent().getParent()).getVerticalScrollBar().setValue((int) lastSelectedPanel.getLocation().getY());
				}
				isFind = true;
				break;
			}
		}
		if (isFind) {
			fireSymbolSelectedChanged(lastSelectedPanel.getSymbol());
		}
	}

	protected abstract int getCurrentSymbolId();

	public void setGeoStyle(GeoStyle geoStyle) {
		this.geoStyle = geoStyle;
		setSelectSymbol();
	}


	public void setSearchString(String string) {
		this.searchString = string;
		search();
	}

	private void search() {
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setVisible(StringUtilities.isNullOrEmpty(searchString)
					|| ((SymbolPanel) this.getComponent(i)).getSymbolName().toLowerCase().contains(searchString.toLowerCase())
					|| String.valueOf(((SymbolPanel) this.getComponent(i)).getSymbolID()).toLowerCase().contains(searchString.toLowerCase()));
		}
	}

}
