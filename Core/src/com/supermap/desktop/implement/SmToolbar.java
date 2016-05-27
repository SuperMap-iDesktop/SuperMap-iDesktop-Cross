package com.supermap.desktop.implement;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.ICtrlAction;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IToolbar;
import com.supermap.desktop.ui.XMLButton;
import com.supermap.desktop.ui.XMLButtonDropdown;
import com.supermap.desktop.ui.XMLComboBox;
import com.supermap.desktop.ui.XMLCommand;
import com.supermap.desktop.ui.XMLLabel;
import com.supermap.desktop.ui.XMLSeparator;
import com.supermap.desktop.ui.XMLTextbox;
import com.supermap.desktop.ui.XMLToolbar;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.utilties.SystemPropertyUtilties;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SmToolbar extends JToolBar implements IToolbar {

	private static final long serialVersionUID = 1L;
	private boolean buildFinished = false;
	private transient XMLToolbar xmlToolbar;
	private java.util.List<IBaseItem> items;
	private JButton buttonMore;
	private JPopupMenu popupMenu;
	private Dimension dimension = new Dimension(0, 0);
	private int currentWidth = -1;
	private MouseAdapter popupMouseListener;
	private ActionListener buttonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (subPopupMenu != null) {
				subPopupMenu.setVisible(false);
			}
			popupMenu.setVisible(false);
		}
	};
	private JPopupMenu subPopupMenu;
	private PopupMenuListener subPopupMenuListener = new PopupMenuListener() {
		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			if (subPopupMenu != null) {
				subPopupMenu.setVisible(false);
			}
			subPopupMenu = (JPopupMenu) e.getSource();
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			subPopupMenu = null;
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {

		}
	};

	public SmToolbar(XMLToolbar xmlToolbar) {
		if (xmlToolbar != null) {
			this.xmlToolbar = xmlToolbar;
			initialize();
		}
	}

	protected boolean initialize() {
		boolean result = false;
		if (this.xmlToolbar != null) {
			items = new ArrayList<>();
			buttonMore = new JButton();
			buttonMore.setPreferredSize(new Dimension(16, 31));
			buttonMore.setIcon(new ImageIcon(this.getClass().getResource("/com/supermap/desktop/coreresources/ToolBar/moreButton.png")));
			buttonMore.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					popupMenu.show(buttonMore.getParent(), (int) (buttonMore.getLocation().getX() + buttonMore.getWidth() - popupMenu.getPreferredSize().getWidth() + 2), 0);

				}

//				@Override
//				public void mouseClicked(MouseEvent e) {
//					popupMenu.show(buttonMore.getParent(), (int) buttonMore.getLocation().getX(), 0);
//
//				}
			});
			popupMenu = new JPopupMenu() {
				@Override
				public void menuSelectionChanged(boolean isIncluded) {
					// hehe
				}
			};
			popupMenu.setLayout(new GridBagLayout());
			popupMenu.addPopupMenuListener(new PopupMenuListener() {
				@Override
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					// FIXME: 2016/5/25 以后在自己实现ToolTipManager
					ToolTipManager.sharedInstance().setEnabled(false);
					checkButtonsState();
				}

				@Override
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					ToolTipManager.sharedInstance().setEnabled(true);
				}

				@Override
				public void popupMenuCanceled(PopupMenuEvent e) {

				}
			});
			popupMouseListener = new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (e.getSource() != null && e.getSource() != popupMenu && e.getSource() != subPopupMenu && ((JComponent) e.getSource()).getParent() != null
							&& ((JComponent) e.getSource()).getParent() != subPopupMenu && !(((JComponent) e.getSource()).getParent() instanceof SmButtonDropdown) && subPopupMenu != null) {
						subPopupMenu.setVisible(false);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					Point point = e.getPoint();
					SwingUtilities.convertPointToScreen(point, e.getComponent());
					checkPopupMenuVisible(point);
				}
			};
			popupMenu.addMouseListener(popupMouseListener);
			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					if (currentWidth != getWidth()) {
						currentWidth = getWidth();
						reAddComponents();
					}
				}

			});
			this.setRollover(true);
			this.setToolTipText(this.xmlToolbar.getTooltip());
			setVisible(this.xmlToolbar.getVisible());
			this.setMinimumSize(new Dimension(45, 33));
			this.load();
			initPreferredSize();
			result = true;
		}

		return result;
	}

	/**
	 * 判断是否需要影藏
	 *
	 * @param point 需要判成相对屏幕的点，需要转换为绝对坐标
	 */
	private void checkPopupMenuVisible(Point point) {
		Point popupMenuLocationOnScreen = popupMenu.getLocationOnScreen();

		boolean isNotInPopupMenu = point.getX() < popupMenuLocationOnScreen.getX() + 1 || point.getY() < popupMenuLocationOnScreen.getY() + 1
				|| point.getX() > popupMenuLocationOnScreen.getX() + popupMenu.getWidth() - 1 || point.getY() > popupMenuLocationOnScreen.getY() + popupMenu.getHeight() - 1;

		boolean isNotInSubPopupMenu = true;
		if (subPopupMenu != null) {
			Point locationOnScreen = subPopupMenu.getLocationOnScreen();
			isNotInSubPopupMenu = point.getX() < locationOnScreen.getX() + 1 || point.getY() < locationOnScreen.getY() + 1
					|| point.getX() > locationOnScreen.getX() + subPopupMenu.getWidth() - 1 || point.getY() > locationOnScreen.getY() + subPopupMenu.getHeight() - 1;
		}
		if (isNotInPopupMenu && isNotInSubPopupMenu) {
			popupMenu.setVisible(false);
		}
	}

	private void checkButtonsState() {
		for (int i = 0; i < popupMenu.getComponentCount(); i++) {
			popupMenu.getComponent(i).setEnabled(popupMenu.getComponent(i).isEnabled());
		}
	}

	private void reAddComponents() {
		// 根据大小重新添加控件，放的下的就放，放不下就到小黑屋了待着
		this.removeAll();
		removePopupItem();
		int widthCount = 0;
		for (IBaseItem item : items) {
			if (item instanceof Component) {
				if (widthCount == -1) {                    // 已经放不下了 直接丢到弹出框里面
					addToPopUpMenu((Component) item);
				} else if (isCanPlaced(widthCount, item)) {
					widthCount += ((Component) item).getPreferredSize().getWidth();
					this.add(item);
				} else {
					widthCount = -1;
					if (items.get(0) != item && !(items.get(indexOf(item) - 1) instanceof javax.swing.JSeparator)) {
						JToolBar.Separator separator = new JToolBar.Separator();
						separator.setOrientation(SwingConstants.VERTICAL);
						this.add(separator);
					}
					this.add(buttonMore);
					addToPopUpMenu((Component) item);
				}
			}
		}
	}

	/**
	 * 判断能不能放置
	 *
	 * @param widthCount 当前长度
	 * @param item       需要放置的对象
	 * @return 是否能放置
	 */
	private boolean isCanPlaced(int widthCount, IBaseItem item) {
		int lastWidth = this.getWidth() - widthCount;
		int moreButtonSize = 40;
		if (lastWidth < ((Component) item).getPreferredSize().getWidth()) {
			// 当前剩余长度已经不能放置
			return false;
		}
		lastWidth -= ((Component) item).getPreferredSize().getWidth();
		if (lastWidth > moreButtonSize) {
			// 可以放MoreButton返回真
			return true;
		}
		// 判断能不能再放一个对象
		int start = items.indexOf(item);
		if (start == items.size() - 1) {
			// 绝后了就直接返回真
			return true;
		}
		if (lastWidth - ((Component) items.get(start + 1)).getWidth() > moreButtonSize) {
			// 再放一个还可以放个moreButton按钮
			return true;
		}
		// 再放一个控件后位置不够了需要看能不能把剩下的控件全放上
		for (int i = start + 1; i < items.size(); i++) {
			// 考虑剩下的项目长度比moreButton短的情况
			lastWidth -= ((Component) items.get(i)).getPreferredSize().getWidth();
			if (lastWidth < 0) {
				return false;
			}
		}
		// 耶，剩下的全放进去了，不需要moreButton了
		return true;
	}

	private void addToPopUpMenu(Component item) {
		if (item instanceof JSeparator) {
			JSeparator separator = new Separator(new Dimension(30, 2));
			separator.addMouseListener(popupMouseListener);
			popupMenu.add(separator, new GridBagConstraintsHelper(0, popupMenu.getComponentCount(), 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setIpad(0, 2).setWeight(1, 1));
		} else if (item instanceof SmCtrlActionButton) {
			SmCtrlActionButton button = (SmCtrlActionButton) item;
			button.setContentAreaFilled(false);
			button.addMouseListener(popupMouseListener);
			button.addActionListener(buttonActionListener);
			popupMenu.add(button, new GridBagConstraintsHelper(0, popupMenu.getComponentCount(), 1, 1).setAnchor(GridBagConstraints.WEST));
		} else if (item instanceof SmButtonDropdown) {
			((SmButtonDropdown) item).reLayout(true);
			addAutoHidingListener(((SmButtonDropdown) item).getPopupMenu());
			((SmButtonDropdown) item).getDisplayButton().addActionListener(buttonActionListener);
			item.addMouseListener(popupMouseListener);
			popupMenu.add(item, new GridBagConstraintsHelper(0, popupMenu.getComponentCount(), 1, 1).setAnchor(GridBagConstraints.WEST));
		}
	}

	private void removePopupItem() {
		for (int i = 0; i < popupMenu.getComponentCount(); i++) {
			if (popupMenu.getComponent(i) instanceof SmCtrlActionButton) {
				((AbstractButton) popupMenu.getComponent(i)).setContentAreaFilled(true);
				((AbstractButton) popupMenu.getComponent(i)).removeActionListener(buttonActionListener);
			} else if (popupMenu.getComponent(i) instanceof SmButtonDropdown) {
				((SmButtonDropdown) popupMenu.getComponent(i)).reLayout(false);
				((SmButtonDropdown) popupMenu.getComponent(i)).getDisplayButton().removeActionListener(buttonActionListener);
				removeSubPopupMenuListener(((SmButtonDropdown) popupMenu.getComponent(i)).getPopupMenu());
			}
			popupMenu.getComponent(i).removeMouseListener(popupMouseListener);
		}
		popupMenu.removeAll();
	}

	private void addAutoHidingListener(JPopupMenu popupMenu) {
		for (int i = 0; i < popupMenu.getComponentCount(); i++) {
			popupMenu.getComponent(i).addMouseListener(popupMouseListener);
		}
		popupMenu.addPopupMenuListener(subPopupMenuListener);
		popupMenu.addMouseListener(popupMouseListener);
	}

	private void removeSubPopupMenuListener(JPopupMenu popupMenu) {
		for (int i = 0; i < popupMenu.getComponentCount(); i++) {
			if (popupMenu.getComponent(i) instanceof AbstractButton) {
				((AbstractButton) popupMenu.getComponent(i)).addActionListener(buttonActionListener);
			}
			popupMenu.getComponent(i).removeMouseListener(popupMouseListener);
		}
		popupMenu.removePopupMenuListener(subPopupMenuListener);
		popupMenu.removeMouseListener(popupMouseListener);
	}

	public List<IBaseItem> items() {
		return items;
	}

	@Override
	public boolean isVisible() {
		return super.isVisible();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	@Override
	public int getIndex() {
		return this.xmlToolbar.getIndex();
	}

	@Override
	public void setIndex(int index) {
		this.xmlToolbar.setIndex(index);
	}

	@Override
	public String getID() {
		return this.xmlToolbar.getID();
	}

	public void build(IForm form) {
		if (!this.buildFinished) {
			// 加载相应的右键菜单
			load();
			this.buildFinished = true;
		}
	}

	private void load() {
		try {
			for (int index = 0; index < this.xmlToolbar.items().size(); index++) {
				XMLCommand xmlItem = xmlToolbar.items().get(index);
				IBaseItem item = null;
				if (xmlItem instanceof XMLButton) {
					item = new SmCtrlActionButton(null, xmlItem, this);
				} else if (xmlItem instanceof XMLButtonDropdown) {
					item = new SmButtonDropdown(null, xmlItem, this);
				} else if (xmlItem instanceof XMLLabel) {
					item = new SmLabel(null, xmlItem, this);
				} else if (xmlItem instanceof XMLTextbox) {
					item = new SmTextField(null, xmlItem, this);
				} else if (xmlItem instanceof XMLComboBox) {
					item = new SmComboBox(null, xmlItem, this);
				} else if (xmlItem instanceof XMLSeparator) {
					item = new SmSeparator(null, xmlItem, this);
				}

				if (item != null) {
					items.add(item);
					this.add(item);
				}
			}
			this.setName(xmlToolbar.getLabel());
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public void setChecked(boolean checked) {

	}

	@Override
	public IBaseItem getAt(int index) {
		IBaseItem item = null;
		try {
			if (index >= 0 && index < items.size()) {
				item = items.get(index);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return item;
	}

	@Override
	public int add(IBaseItem item) {
		try {
			super.add((Component) item);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}

		return super.getComponentCount();
	}

	@Override
	public void addRange(IBaseItem[] items) {
		try {
			for (IBaseItem item : items) {
				super.add((Component) item);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public boolean contains(IBaseItem item) {
		return items.contains(item);
	}

	@Override
	public int indexOf(IBaseItem item) {
		int result = -1;
		try {
			for (int i = 0; i < this.getCount(); i++) {
				if (this.getAt(i).equals(item)) {
					result = i;
					break;
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	@Override
	public void insert(IBaseItem item, int index) {
		try {
			super.add((Component) item, index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public void initPreferredSize() {
		int width = SystemPropertyUtilties.isWindows() ? 0 : 12;
		for (IBaseItem item : items) {
			if (item instanceof Component) {
				width += ((Component) item).getPreferredSize().getWidth();
			}
		}
		dimension = new Dimension(width, 33);
	}

	@Override
	public Dimension getPreferredSize() {
		return dimension;
	}

	@Override
	public void remove(IBaseItem item) {
		try {
			super.remove((Component) item);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public ICtrlAction getCtrlAction() {
		return null;
	}

	@Override
	public void setCtrlAction(ICtrlAction ctrlAction) {
		// DO NOTHING
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public void clear() {
		try {
			super.removeAll();
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void removeAt(int index) {
		try {
			super.remove(index);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	public XMLToolbar getXMLToolbar() {
		return this.xmlToolbar;
	}

	class ToolBarMorePopupMenu extends JMenu implements IBaseItem {

		private IBaseItem baseItem;

		public ToolBarMorePopupMenu(IBaseItem baseItem) {
			this.baseItem = baseItem;
			this.setFocusPainted(false);
			this.setFocusable(false);
			this.setIcon(((SmButtonDropdown) baseItem).getDisplayButton().getIcon());
		}

		@Override
		public void setPopupMenuVisible(boolean b) {
			((SmButtonDropdown) baseItem).getPopupMenu().show(popupMenu, 33, 30);
		}

		@Override
		public boolean isEnabled() {
			if (baseItem == null) {
				return true;
			}
			return baseItem.isEnabled();
		}

		@Override
		public void setEnabled(boolean enabled) {
			baseItem.setEnabled(enabled);
		}

		@Override
		public boolean isVisible() {
			return baseItem.isVisible();
		}

		@Override
		public void setVisible(boolean visible) {
			baseItem.setEnabled(visible);
		}

		@Override
		public boolean isChecked() {
			return baseItem.isChecked();
		}

		@Override
		public void setChecked(boolean checked) {
			baseItem.setChecked(checked);
		}

		@Override
		public int getIndex() {
			return baseItem.getIndex();
		}

		@Override
		public void setIndex(int index) {
			baseItem.setIndex(index);
		}

		@Override
		public String getID() {
			return baseItem.getID();
		}

		@Override
		public ICtrlAction getCtrlAction() {
			return baseItem.getCtrlAction();
		}

		@Override
		public void setCtrlAction(ICtrlAction ctrlAction) {
			baseItem.setCtrlAction(ctrlAction);
		}
	}


}
