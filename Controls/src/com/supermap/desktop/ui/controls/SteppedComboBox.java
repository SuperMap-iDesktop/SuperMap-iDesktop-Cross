package com.supermap.desktop.ui.controls;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.synth.SynthComboBoxUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * 下拉项可以全幅显示ComboBox，显示项的实现通过setUI中的SteppedComboBoxUI来实现。
 * @author
 *
 */

public class SteppedComboBox extends JComboBox {
	protected int popupWidth;

	public SteppedComboBox(ComboBoxModel aModel) {
		super(aModel);
		init();
	}

	public SteppedComboBox(final Object[] items) {
		super(items);
		init();
	}

	public SteppedComboBox(Vector items) {
		super(items);
		init();
	}

	private void init() {
		if (this.getUI() instanceof MotifComboBoxUI) {
			this.setUI(new SteppedComboBoxMotifComboBoxUI());
		} else if (this.getUI() instanceof SynthComboBoxUI) {
			this.setUI(new SteppedComboBoxSynthComboBoxUI());
		} else if (this.getUI() instanceof WindowsComboBoxUI) {
			this.setUI(new SteppedComboBoxWindowsComboBoxUI());
		} else {
			this.setUI(new SteppedComboBoxMetalUI());
		}
		popupWidth = 0;
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (2==e.getClickCount()) {
					showPopup();
				}
			}
		});
	}

	public void setPopupWidth(int width) {
		popupWidth = width;
	}

	public Dimension getPopupSize() {
		Dimension size = getSize();
		if (popupWidth < 1)
			popupWidth = size.width;
		return new Dimension(popupWidth, size.height);
	}

	class SteppedComboBoxMetalUI extends MetalComboBoxUI {


		protected ComboPopup createPopup() {
			return getComboPopup(comboBox);
		}
	}

	class SteppedComboBoxSynthComboBoxUI extends SynthComboBoxUI {

		@Override
		protected ComboPopup createPopup() {
			return getComboPopup(comboBox);
		}
	}

	class SteppedComboBoxWindowsComboBoxUI extends WindowsComboBoxUI {

		@Override
		protected ComboPopup createPopup() {
			return getComboPopup(comboBox);
		}
	}

	class SteppedComboBoxMotifComboBoxUI extends MotifComboBoxUI {
		@Override
		protected ComboPopup createPopup() {
			return getComboPopup(comboBox);
		}
	}

	private ComboPopup getComboPopup(final JComboBox comboBox) {
		BasicComboPopup popup = new BasicComboPopup(comboBox) {

			public void show() {
				Dimension popupSize = ((SteppedComboBox) comboBox).getPopupSize();
				popupSize.setSize(popupSize.width, getPopupHeightForRowCount(comboBox.getMaximumRowCount()));
				Rectangle popupBounds = computePopupBounds(0, comboBox.getBounds().height, popupSize.width, popupSize.height);
				scroller.setMaximumSize(popupBounds.getSize());
				scroller.setPreferredSize(popupBounds.getSize());
				scroller.setMinimumSize(popupBounds.getSize());
				list.invalidate();
				int selectedIndex = comboBox.getSelectedIndex();
				if (selectedIndex == -1) {
					list.clearSelection();
				} else {
					list.setSelectedIndex(selectedIndex);
				}
				list.ensureIndexIsVisible(list.getSelectedIndex());
				setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());

				show(comboBox, popupBounds.x, popupBounds.y);
			}
		};
		popup.getAccessibleContext().setAccessibleParent(comboBox);
		return popup;
	}

}
