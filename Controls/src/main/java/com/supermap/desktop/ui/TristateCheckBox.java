package com.supermap.desktop.ui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.event.*;

// @formatter:off
/**
 * Maintenance tip - There were some tricks to getting this code working:
 * 
 * 1. You have to overwite addMouseListener() to do nothing 
 * 2. You have to add a mouse event on mousePressed by calling super.addMouseListener() 
 * 3. You have to replace the UIActionMap for the keyboard event "pressed" with your own one. 
 * 4. You have to remove the UIActionMap for the keyboard event "released". 
 * 5. You have to grab focus when the next state is entered, otherwise clicking on the component won't get the focus. 
 * 6. You have to make a TristateDecorator as a
 * button model that wraps the original button model and does state management.
 */
// @formatter:on
public class TristateCheckBox extends JCheckBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** This is a type-safe enumerated type */
	public static class State {
		private State() {
		}
	}

	public static final State NOT_SELECTED = new State();
	public static final State SELECTED = new State();
	public static final State DONT_CARE = new State();

	private transient final TristateDecorator tristateDecoratorModel;

	public TristateCheckBox(String text, Icon icon, State initial) {
		super(text, icon);
		// Add a listener for when the mouse is pressed
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				grabFocus();
				if (isEnabled()) {
					tristateDecoratorModel.nextState();
				}
			}
		});
		// Reset the keyboard action map
		ActionMap map = new ActionMapUIResource();
		map.put("pressed", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				grabFocus();
				if (isEnabled()) {
					tristateDecoratorModel.nextState();
				}
			}
		});
		map.put("released", null);
		SwingUtilities.replaceUIActionMap(this, map);
		// set the model to the adapted model
		tristateDecoratorModel = new TristateDecorator(getModel());
		setModel(tristateDecoratorModel);
		setState(initial);
	}

	public TristateCheckBox(String text, State initial) {
		this(text, null, initial);
	}

	public TristateCheckBox(String text) {
		this(text, DONT_CARE);
	}

	public TristateCheckBox() {
		this(null);
	}

	/** No one may add mouse listeners, not even Swing! */
	@Override
	public void addMouseListener(MouseListener l) {
		// 默认实现
	}

	/**
	 * Set the new state to either SELECTED, NOT_SELECTED or DONT_CARE. If state == null, it is treated as DONT_CARE.
	 */
	public void setState(State state) {
		tristateDecoratorModel.setState(state);
	}

	/**
	 * Return the current state, which is determined by the selection status of the model.
	 */
	public State getState() {
		return tristateDecoratorModel.getState();
	}

	public Boolean isSelectedEx() {
		Boolean b = null;

		if (this.getState() == SELECTED) {
			b = true;
		} else if (this.getState() == NOT_SELECTED) {
			b = false;
		} else {
			b = null;
		}
		return b;
	}

	public void setSelectedEx(Boolean b) {
		if (b == null) {
			setState(DONT_CARE);
		} else if (b) {
			setState(SELECTED);
		} else {
			setState(NOT_SELECTED);
		}
	}

	@Override
	public void setSelected(boolean b) {
		if (b) {
			setState(SELECTED);
		} else {
			setState(NOT_SELECTED);
		}
	}

	public void addStateChangeListener(StateChangeListener listener) {
		listenerList.add(StateChangeListener.class, listener);
	}

	public void removeStateChangeListener(StateChangeListener listener) {
		listenerList.remove(StateChangeListener.class, listener);
	}

	protected void fireStateChange(StateChangeEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == StateChangeListener.class) {
				((StateChangeListener) listeners[i + 1]).stateChange(e);
			}
		}
	}

	/**
	 * Exactly which Design Pattern is this? Is it an Adapter, a Proxy or a Decorator? In this case, my vote lies with the Decorator, because we are extending
	 * functionality and "decorating" the original model with a more powerful model.
	 */
	private class TristateDecorator implements ButtonModel {
		private final ButtonModel other;

		private TristateDecorator(ButtonModel other) {
			this.other = other;
		}

		private void setState(State state) {
			if (state == NOT_SELECTED) {
				other.setArmed(false);
				setPressed(false);
				setSelected(false);
			} else if (state == SELECTED) {
				other.setArmed(false);
				setPressed(false);
				setSelected(true);
			} else { // either "null" or DONT_CARE
				other.setArmed(true);
				setPressed(true);
				setSelected(true);
			}
			fireStateChange(new StateChangeEvent(TristateCheckBox.this, getState()));
		}

		/**
		 * The current state is embedded in the selection / armed state of the model.
		 * 
		 * We return the SELECTED state when the checkbox is selected but not armed, DONT_CARE state when the checkbox is selected and armed (grey) and
		 * NOT_SELECTED when the checkbox is deselected.
		 */
		private State getState() {
			if (isSelected() && !isArmed()) {
				// normal black tick
				return SELECTED;
			} else if (isSelected() && isArmed()) {
				// don't care grey tick
				return DONT_CARE;
			} else {
				// normal deselected
				return NOT_SELECTED;
			}
		}

		/** We rotate between NOT_SELECTED, SELECTED and DONT_CARE.第三态仅支持代码设置 */
		private void nextState() {
			State current = getState();
			if (current == NOT_SELECTED) {
				setState(SELECTED);
			} else if (current == DONT_CARE || current == SELECTED) {
				setState(NOT_SELECTED);
			}
		}

		/** Filter: No one may change the armed status except us. */
		@Override
		public void setArmed(boolean b) {
			// 默认实现，后续进行初始化操作
		}

		/**
		 * We disable focusing on the component when it is not enabled.
		 */
		@Override
		public void setEnabled(boolean b) {
			setFocusable(b);
			other.setEnabled(b);
		}

		/**
		 * All these methods simply delegate to the "other" model that is being decorated.
		 */
		@Override
		public boolean isArmed() {
			return other.isArmed();
		}

		@Override
		public boolean isSelected() {
			return other.isSelected();
		}

		@Override
		public boolean isEnabled() {
			return other.isEnabled();
		}

		@Override
		public boolean isPressed() {
			return other.isPressed();
		}

		@Override
		public boolean isRollover() {
			return other.isRollover();
		}

		@Override
		public void setSelected(boolean b) {
			other.setSelected(b);
		}

		@Override
		public void setPressed(boolean b) {
			other.setPressed(b);
		}

		@Override
		public void setRollover(boolean b) {
			other.setRollover(b);
		}

		@Override
		public void setMnemonic(int key) {
			other.setMnemonic(key);
		}

		@Override
		public int getMnemonic() {
			return other.getMnemonic();
		}

		@Override
		public void setActionCommand(String s) {
			other.setActionCommand(s);
		}

		@Override
		public String getActionCommand() {
			return other.getActionCommand();
		}

		@Override
		public void setGroup(ButtonGroup group) {
			other.setGroup(group);
		}

		@Override
		public void addActionListener(ActionListener l) {
			other.addActionListener(l);
		}

		@Override
		public void removeActionListener(ActionListener l) {
			other.removeActionListener(l);
		}

		@Override
		public void addItemListener(ItemListener l) {
			other.addItemListener(l);
		}

		@Override
		public void removeItemListener(ItemListener l) {
			other.removeItemListener(l);
		}

		@Override
		public void addChangeListener(ChangeListener l) {
			other.addChangeListener(l);
		}

		@Override
		public void removeChangeListener(ChangeListener l) {
			other.removeChangeListener(l);
		}

		@Override
		public Object[] getSelectedObjects() {
			return other.getSelectedObjects();
		}
	}
}