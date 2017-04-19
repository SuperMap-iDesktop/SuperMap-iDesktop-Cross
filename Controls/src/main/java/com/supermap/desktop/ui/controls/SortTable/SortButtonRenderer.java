package com.supermap.desktop.ui.controls.SortTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Hashtable;

/**
 * @version 1.0 02/25/99
 */
public class SortButtonRenderer extends JButton implements TableCellRenderer {
	public static final int NONE = 0;
	public static final int DOWN = 1;
	public static final int UP = 2;

	int pushedColumn;
	Hashtable state;
	JButton downButton, upButton;

	public SortButtonRenderer() {
		pushedColumn = -1;
		state = new Hashtable();

		setMargin(new Insets(0, 0, 0, 0));
		setHorizontalTextPosition(LEFT);
		setIcon(new BlankIcon());

	}

	class DownButton extends JButton {
		DownButton() {
			super();
			this.setMargin(new Insets(0, 0, 0, 0));
			this.setHorizontalTextPosition(LEFT);
			this.setIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, false));
			this.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, true));
		}
	}

	class UpButton extends JButton {
		UpButton() {
			super();
			this.setMargin(new Insets(0, 0, 0, 0));
			this.setHorizontalTextPosition(LEFT);
			this.setIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, false));
			this.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, true));
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		JButton button = this;

		Object obj = state.get(column);
		if (obj != null) {
			if ((Integer) obj == DOWN) {
				button = new DownButton();
			} else {
				button = new UpButton();
			}
		}
		button.setText((value == null) ? "" : value.toString());
		boolean isPressed = (column == pushedColumn);
		button.getModel().setPressed(isPressed);
		button.getModel().setArmed(isPressed);
		setButtonStyle(button);
		return button;
	}

	public void setButtonStyle(JButton button) {
		try {
			button.setPreferredSize(new Dimension(button.getWidth(), 23));
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
			button.setBorder(new LineBorder(Color.LIGHT_GRAY));
		} catch (Exception e) {
		}
	}

	public void setPressedColumn(int col) {
		pushedColumn = col;
	}

	public void setSelectedColumn(int col) {
		if (col < 0) return;
		Integer value = null;
		Object obj = state.get(col);
		if (obj == null) {
			value = DOWN;
		} else {
			if ((Integer) obj == DOWN) {
				value = UP;
			} else {
				value = DOWN;
			}
		}
		state.clear();
		state.put(col, value);
	}

	public int getState(int col) {
		int retValue;
		Object obj = state.get(col);
		if (obj == null) {
			retValue = NONE;
		} else {
			if ((Integer) obj == DOWN) {
				retValue = DOWN;
			} else {
				retValue = UP;
			}
		}
		return retValue;
	}
}



