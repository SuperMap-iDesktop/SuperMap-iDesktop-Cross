package com.supermap.desktop.ui.mdi.action;

import com.supermap.desktop.ui.mdi.MdiGroup;
import com.supermap.desktop.ui.mdi.MdiPage;
import com.supermap.desktop.ui.mdi.plaf.feature.IMdiFeature;
import com.supermap.desktop.ui.mdi.util.MdiResource;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by highsad on 2016/9/19.
 */
public class ActionPagesList extends AbstractAction {

	private MyAWTEventListener myAWTEventListener = new MyAWTEventListener();
	private JPopupMenu popup = new JPopupMenu() {
		@Override
		public void menuSelectionChanged(boolean isIncluded) {
			// 不隐藏
		}

		@Override
		public void setVisible(boolean b) {
			if (b) {
				Toolkit.getDefaultToolkit().addAWTEventListener(myAWTEventListener, AWTEvent.MOUSE_EVENT_MASK | sun.awt.SunToolkit.GRAB_EVENT_MASK);
			} else {
				Toolkit.getDefaultToolkit().removeAWTEventListener(myAWTEventListener);
			}
			popup.setPopupSize(160, 200);
			super.setVisible(b);
		}
	};
	private JList list;
	private boolean isInit = false;
	private ListSelectionListener selectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!isInit) {
				PagesListModel model = (PagesListModel) list.getModel();
				MdiPage selectedPage = model.getElementAt(list.getSelectedIndex());
				model.getGroup().activePage(selectedPage);
				popup.setVisible(false);
			}
		}
	};

	public ActionPagesList() {
		super(MdiResource.getIcon(MdiResource.LIST), MdiResource.getIcon(MdiResource.LIST_DISABLE), MdiResource.getIcon(MdiResource.LIST_ACTIVE));
		this.popup.setLayout(new BorderLayout());
		this.list = new JList();
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(this.selectionListener);
		JScrollPane scrollPane = new JScrollPane(this.list);
		this.popup.add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void action(MdiPage page, IMdiFeature feature) {
		try {
			this.isInit = true;
			PagesListModel model = new PagesListModel(page.getGroup());
			this.list.setModel(model);
			this.list.setSelectedValue(page, true);
			this.isInit = false;
			this.popup.show(feature.getGroup(), feature.getX(), feature.getY() + feature.getHeight());
		} finally {
			this.isInit = false;
		}
	}

	@Override
	public ActionMode getMode() {
		return ActionMode.GROUP;
	}

	private class PagesListModel implements ListModel<MdiPage> {

		private MdiGroup group;

		PagesListModel(MdiGroup group) {
			this.group = group;
		}

		public MdiGroup getGroup() {
			return this.group;
		}

		@Override
		public int getSize() {
			return this.group == null ? 0 : this.group.getPageCount() - this.group.getFloatingPageCount();
		}

		@Override
		public MdiPage getElementAt(int index) {
			ArrayList<MdiPage> tabbedPages = getTabbedPages();
			if (index >= 0 && index < tabbedPages.size()) {
				return tabbedPages.get(index);
			} else {
				return null;
			}
		}

		private ArrayList<MdiPage> getTabbedPages() {
			ArrayList<MdiPage> tabbedPages = new ArrayList<MdiPage>();

			for (int i = 0; i < this.group.getPageCount(); i++) {
				if (!this.group.isPageFloating(i)) {
					tabbedPages.add(this.group.getPageAt(i));
				}
			}
			return tabbedPages;
		}

		@Override
		public void addListDataListener(ListDataListener l) {

		}

		@Override
		public void removeListDataListener(ListDataListener l) {

		}
	}

	private class MyAWTEventListener implements AWTEventListener {

		@Override
		public void eventDispatched(AWTEvent event) {
			if (event instanceof sun.awt.UngrabEvent) {
				cancelPopupMenu();
				return;
			}
			if (!(event instanceof MouseEvent)) {
				// We are interested in MouseEvents only
				return;
			}
			MouseEvent me = (MouseEvent) event;
			Component src = me.getComponent();
			switch (me.getID()) {
				case MouseEvent.MOUSE_PRESSED:
//					checkPopupMenuChooseColorGroupState(src);
					if (isInPopup(src) ||
							(src instanceof JMenu && ((JMenu) src).isSelected())) {
						return;
					}
					// Cancel popup only if this property was not set.
					// If this property is set to TRUE component wants
					// to deal with this event by himself.
					cancelPopupMenu();
					src.dispatchEvent(event);
					// Ask UIManager about should we consume event that closes
					// popup. This made to match native apps behaviour.
					boolean consumeEvent =
							UIManager.getBoolean("PopupMenu.consumeEventOnClose");
					// Consume the event so that normal processing stops.
					if (consumeEvent && !(src instanceof MenuElement)) {
						me.consume();
					}
					break;

				case MouseEvent.MOUSE_RELEASED:
					if (!(src instanceof MenuElement)) {
						// Do not forward event to MSM, let component handle it
						if (isInPopup(src)) {
							break;
						}
					}
					if (src instanceof JMenu || !(src instanceof JMenuItem)) {
						MenuSelectionManager.defaultManager().
								processMouseEvent(me);
					}
					break;
				case MouseEvent.MOUSE_DRAGGED:
					if (!(src instanceof MenuElement)) {
						// For the MOUSE_DRAGGED event the src is
						// the Component in which mouse button was pressed.
						// If the src is in popupMenu,
						// do not forward event to MSM, let component handle it.
						if (isInPopup(src)) {
							break;
						}
					}
					MenuSelectionManager.defaultManager().
							processMouseEvent(me);
					break;
				case MouseEvent.MOUSE_WHEEL:
//					checkPopupMenuChooseColorGroupState(src);
					if (isInPopup(src)) {
						return;
					}
					cancelPopupMenu();
					break;
			}
		}


		private void cancelPopupMenu() {
			popup.setVisible(false);
		}

		private boolean isInPopup(Component src) {
			return getParentPopupMenu(src) != null;
		}

		private JPopupMenu getParentPopupMenu(Component src) {
			for (Component c = src; c != null; c = c.getParent()) {
				if (c instanceof Applet || c instanceof Window) {
					break;
				} else if (c instanceof JPopupMenu) {
					return ((JPopupMenu) c);
				}
			}
			return null;
		}
	}
}
